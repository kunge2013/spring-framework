package com.simple.mq.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author fangkun
 * @date 2020/9/19 12:39
 * @description:
 */
public class KafkaProducerSimple {
	static final Properties props = new Properties();
	static {

		String groupId= "001";
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");

		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringSerializer.class);
	}
	public static void main(String[] args) {
		KafkaProducer<String, String > producer = new KafkaProducer(props);
		String topic = "procude" ;
		Integer partition = 100;
		ProducerRecord producerRecord = new ProducerRecord(topic, partition, "key", "val");
		producer.send(producerRecord, new Callback() {
			@Override
			public void onCompletion(RecordMetadata metadata, Exception exception) {
				System.out.println("metadata = " + metadata);
				System.out.println("exception = " + exception);

			}
		});
	}
}
