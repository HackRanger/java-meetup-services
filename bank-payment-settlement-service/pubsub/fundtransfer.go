package pubsub

import (
	"log"
	"os"
	"os/signal"
	"strconv"
	"syscall"

	"github.com/hackranger/bank-payment-settlement-service/models"
	"gopkg.in/confluentinc/confluent-kafka-go.v1/kafka"
	"gopkg.in/square/go-jose.v2/json"
)

func init() {
	broker = "134.209.144.156:9092"
	topicStatus = "transactionstatus"
	topicTransaction = "transaction"
}

var (
	broker           string
	topicStatus      string
	topicTransaction string
)

func FundtransferStatusProduce(msg string) {
	p, err := kafka.NewProducer(&kafka.ConfigMap{"bootstrap.servers": broker})

	if err != nil {
		log.Printf("Failed to create producer: %s\n", err)
	}

	log.Printf("Created Producer %v\n", p)

	// Optional delivery channel, if not specified the Producer object's
	// .Events channel is used.
	deliveryChan := make(chan kafka.Event)

	value := msg
	err = p.Produce(&kafka.Message{
		TopicPartition: kafka.TopicPartition{Topic: &topicStatus, Partition: kafka.PartitionAny},
		Value:          []byte(value),
		Headers:        []kafka.Header{{Key: "myTestHeader", Value: []byte("header values are binary")}},
	}, deliveryChan)

	e := <-deliveryChan
	m := e.(*kafka.Message)

	if m.TopicPartition.Error != nil {
		log.Printf("Delivery failed: %v\n", m.TopicPartition.Error)
	} else {
		log.Printf("Delivered message to topic %s [%d] at offset %v\n",
			*m.TopicPartition.Topic, m.TopicPartition.Partition, m.TopicPartition.Offset)
	}

	close(deliveryChan)

	p.Close()
}

func FundTransferSub() {
	broker := broker
	group := "fundtransfergrp"
	topics := []string{topicTransaction}
	sigchan := make(chan os.Signal, 1)
	signal.Notify(sigchan, syscall.SIGINT, syscall.SIGTERM)

	c, err := kafka.NewConsumer(&kafka.ConfigMap{
		"bootstrap.servers": broker,
		// Avoid connecting to IPv6 brokers:
		// This is needed for the ErrAllBrokersDown show-case below
		// when using localhost brokers on OSX, since the OSX resolver
		// will return the IPv6 addresses first.
		// You typically don't need to specify this configuration property.
		"broker.address.family": "v4",
		"group.id":              group,
		"session.timeout.ms":    6000,
		"auto.offset.reset":     "latest"})

	if err != nil {
		log.Printf("Failed to create consumer: %s\n", err)
		//os.Exit(1)
	}

	log.Printf("Created Consumer %v\n", c)

	err = c.SubscribeTopics(topics, nil)

	run := true

	for run == true {
		select {
		case sig := <-sigchan:
			log.Printf("Caught signal %v: terminating\n", sig)
			run = false
		default:
			ev := c.Poll(100)
			if ev == nil {
				continue
			}

			switch e := ev.(type) {
			case *kafka.Message:
				log.Printf("%% Message on %s:\n%s\n",
					e.TopicPartition, string(e.Value))

				var ftInstruction models.FundTransferInstruction
				json.Unmarshal(e.Value, &ftInstruction)

				src := models.GetCustomerByAccNo(ftInstruction.SourceAccountNumber)
				dest := models.GetCustomerByAccNo(ftInstruction.DestinationAccountNumber)

				src.Balance = src.Balance - ftInstruction.Amount
				dest.Balance = dest.Balance + ftInstruction.Amount

				models.UpdateCustomerBalance(src)
				models.UpdateCustomerBalance(dest)

				ftId, _ := strconv.Atoi(string(e.Key))
				FundtransferStatusProduce("Success:" + string(ftId))
				if e.Headers != nil {
					log.Printf("%% Headers: %v\n", e.Headers)
				}
			case kafka.Error:
				// Errors should generally be considered
				// informational, the client will try to
				// automatically recover.
				// But in this example we choose to terminate
				// the application if all brokers are down.
				log.Printf("%% Error: %v: %v\n", e.Code(), e)
				if e.Code() == kafka.ErrAllBrokersDown {
					run = false
				}
			default:
				log.Printf("Ignored %v\n", e)
			}
		}
	}

	log.Printf("Closing consumer\n")
	c.Close()
}
