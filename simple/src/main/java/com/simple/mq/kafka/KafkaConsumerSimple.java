package com.simple.mq.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.Spliterator;

/**
 * @author fangkun
 * @date 2020/9/19 18:15
 * @description:
 */
public class KafkaConsumerSimple {
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
		String topic = "procude";
		KafkaConsumer<String, String > consumer = new KafkaConsumer(props);
		consumer.subscribe(Collections.singletonList(topic));
		while (true) {
			ConsumerRecords<String, String> poll = consumer.poll(Duration.ofSeconds(1));
			if (poll.isEmpty()) continue;
			Spliterator<ConsumerRecord<String, String>> spliterator = poll.spliterator();
			spliterator.forEachRemaining((o) -> {
				System.out.println("data ====>>>> " + o.toString());
			});

		}
	}
}
