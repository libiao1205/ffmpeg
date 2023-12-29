package com.example.demo.manager;

import com.example.demo.controller.KafkaController;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author libiao
 * @date 2023/12/18
 */
@Component
public class KafkaConsumerCustom {

    @KafkaListener(topics = {"topic-2"}, groupId = "group-test1")
    public void onMessage(List<ConsumerRecord<String, Object>> records, Acknowledgment acknowledgment) {
        for (ConsumerRecord<String, Object> record : records) {
            System.out.println("消费者1---offset=" + record.offset() + "  " + record.topic() + "-" + record.partition() + "=" + record.value() + "   时间=" + (System.currentTimeMillis() - KafkaController.startTime));
        }
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = {"topic-2"}, groupId = "group-test1")
    public void onMessage1(List<ConsumerRecord<String, Object>> records, Acknowledgment acknowledgment) {
        for (ConsumerRecord<String, Object> record : records) {
            System.out.println("消费者2---offset=" + record.offset() + "  " + record.topic() + "-" + record.partition() + "=" + record.value() + "   时间=" + (System.currentTimeMillis() - KafkaController.startTime));
        }
        acknowledgment.acknowledge();
    }

    @KafkaListener(topics = {"topic-2"}, groupId = "group-test1")
    public void onMessage2(List<ConsumerRecord<String, Object>> records, Acknowledgment acknowledgment) {
        for (ConsumerRecord<String, Object> record : records) {
            System.out.println("消费者3---offset=" + record.offset() + "  " + record.topic() + "-" + record.partition() + "=" + record.value() + "   时间=" + (System.currentTimeMillis() - KafkaController.startTime));
        }
        acknowledgment.acknowledge();
    }
}
