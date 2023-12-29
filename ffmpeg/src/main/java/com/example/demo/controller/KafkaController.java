package com.example.demo.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CountDownLatch;

/**
 * @author libiao
 * @date 2023/12/18
 */
@RequiredArgsConstructor
@RestController()
@RequestMapping("/kafka")
@Api(value = "/test", tags = "kafka相关接口")
public class KafkaController {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final ThreadPoolTaskExecutor taskExecutor;

    public static long startTime = 0;

    /**
     * 调用send方法 1：进入拦截器序列化ProducerRecord对象为字符串，2：进入分区器，3：进入缓冲区（同一分区的消息会被放到同一批次中），4：sender线程从缓冲区拉取消息发送到kafka broker
     */
    @GetMapping("/sendMessageBatch")
    public String sendMessageBatch(@RequestParam("content") String content) {
        startTime = System.currentTimeMillis();
        System.out.println("开始时间=" + startTime);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            taskExecutor.execute(() -> {
                for (int j = 0; j < 10; j++) {
                    kafkaTemplate.send("test", content + "-" + Thread.currentThread().getName() + "   " + finalI + ":" + j);
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
            System.out.println("耗时=" + (System.currentTimeMillis() - startTime));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "成功";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@RequestParam("content") String content, @RequestParam("key") String key, @RequestParam("partition") int partition) {
        kafkaTemplate.send("test", partition, key, content);
        return "成功";
    }

}
