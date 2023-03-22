package com.atguigu.rabbitmq.four;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class ConfirmMessage {

    // 批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
    //     单个确认
    //     ConfirmMessage.publishMessageIndividually();

    //     批量确认
    //     ConfirmMessage.publishMessageBatch();

    //     异步批量确认
        ConfirmMessage.publishMessageAsync();
        return;
    }

    public static void publishMessageIndividually() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
    //     开启发布确认
        channel.confirmSelect();
    //     开始时间
        long begin = System.currentTimeMillis();

    //     批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
            boolean flag = channel.waitForConfirms();
            if (flag) {
                System.out.println("消息发送成功");
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息，耗时" + (end - begin) + "ms");

    }

//         批量确认发布
public static void publishMessageBatch() throws Exception {
    Channel channel = RabbitMQUtils.getChannel();

    String queueName = UUID.randomUUID().toString();
    channel.queueDeclare(queueName, true, false, false, null);
    //     开启发布确认
    channel.confirmSelect();
    //     开始时间
    long begin = System.currentTimeMillis();

    // 批量确认消息大小
    int batchSize = 100;



    //     批量发消息
    for (int i = 0; i < MESSAGE_COUNT; i++) {
        String message = i + "";
        channel.basicPublish("", queueName, null, message.getBytes());
    //     判断达到100条消息的时候，批量确认一次
        if (i % batchSize == 0) {
            channel.waitForConfirms();
        }

    }

    long end = System.currentTimeMillis();
    System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息，耗时" + (end - begin) + "ms");

}

    // 异步发布确认
    public static void publishMessageAsync() throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //     开启发布确认
        channel.confirmSelect();
        //     开始时间


        /**
         * 线程安全有序的一个哈希表，适用于高并发的情况下
         * 1. 轻松地将序号与消息进行关联
         * 2. 轻松地批量删除条目 只要给到序号
         * 3. 支持高并发（多线程）
         */
        ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

        // 准备消息监听器，监听哪些消息成功了/失败了
        channel.addConfirmListener((deliveryTag, multiple) -> {
            if (multiple) {
                // 删除已经确认的消息
                ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
                confirmed.clear();
            } else {
                outstandingConfirms.remove(deliveryTag);
            }

            System.out.println("已确认的消息:" + deliveryTag);
        }, ((deliveryTag, multiple) -> {
            String message = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是:" + message + "，序号是:" + deliveryTag);

        }));


        long begin = System.currentTimeMillis();

        //     批量发消息
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i + "";
            channel.basicPublish("", queueName, null, message.getBytes());
        //     此处记录下所有要发送的消息
            outstandingConfirms.put(channel.getNextPublishSeqNo(), message);


        }

        long end = System.currentTimeMillis();
        System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息，耗时" + (end - begin) + "ms");
    }
}
