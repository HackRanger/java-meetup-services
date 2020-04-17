
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.config.SslConfigs;

public class SSLKafkaConsumerDemo
{
  public SSLKafkaConsumerDemo() {}
  
  public void consume(String kafkaBrokers, String topicName)
  {
    Properties properties = new Properties();
    System.out.println("Kafka Consumer for..." + topicName);
    
    properties.setProperty("bootstrap.servers", kafkaBrokers);
    properties.setProperty("key.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class.getName());
    properties.setProperty("value.deserializer", org.apache.kafka.common.serialization.StringDeserializer.class.getName());
    properties.setProperty("security.protocol", "PLAINTEXT");
    properties.setProperty("auto.offset.reset", "latest");
    
    org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer(properties);
    kafkaConsumer.subscribe(java.util.Arrays.asList(new String[] { topicName }));
    for (;;)
    {
      org.apache.kafka.clients.consumer.ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(100L);
      for (ConsumerRecord<String, String> consumerRecord : consumerRecords)
      {
          System.out.println("Partition: " + consumerRecord.partition() + ", Offset: " + consumerRecord
          .offset() + ", Key: " + 
          (String)consumerRecord.key() + ", Value: " + 
          (String)consumerRecord.value());
      }
      
      kafkaConsumer.commitSync();
    }
  }
}
