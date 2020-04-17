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
    properties.setProperty("security.protocol", "SASL_SSL"); //On-prem Only
    properties.setProperty("sasl.kerberos.service.name", "kafka");  //On-prem Only
    properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "C:\\Streaming\\server.truststore.jks");
    properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  "Welcome@123");
//    properties.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, "JKS"); //AWS Only
//    properties.setProperty("security.protocol", "SSL");  //AWS Only
//    properties.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "C:\\Streaming\\mm-devtest.jks");
//    properties.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG,  "Welcome@123");
    properties.setProperty("acks", "1");
    properties.setProperty("retries", "3");
    properties.setProperty("linger.ms", "1");


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
