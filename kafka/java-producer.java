package com.scania.kafka.ssl;

import org.apache.kafka.common.config.SslConfigs;

import java.util.Properties;

public class SSLKafkaProducerDemo
{
  public SSLKafkaProducerDemo() {}
  
  public void produce(String kafkaBrokers, String topicName)
  {

    System.out.println("Kafka Producer for..." + topicName);

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", kafkaBrokers);
    properties.setProperty("key.serializer", org.apache.kafka.common.serialization.StringSerializer.class.getName());
    properties.setProperty("value.serializer", org.apache.kafka.common.serialization.StringSerializer.class.getName());
    properties.setProperty("security.protocol", "SASL_SSL");


    org.apache.kafka.clients.producer.Producer<String, String> producer = new org.apache.kafka.clients.producer.KafkaProducer(properties);
    

    for (int key = 101; key < 120; key++)
    {
      org.apache.kafka.clients.producer.ProducerRecord<String, String> producerRecord = new org.apache.kafka.clients.producer.ProducerRecord(topicName, Integer.toString(key), "message that has key: " + Integer.toString(key));
      producer.send(producerRecord);
      try {
        Thread.sleep(100);
      }
      catch(Exception e){
        e.printStackTrace();
    }
    }
    
    producer.close();
  }
}
