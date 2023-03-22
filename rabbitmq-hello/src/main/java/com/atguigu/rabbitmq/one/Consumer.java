package com.atguigu.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    //    队列名称
    public static final String QUEUE_NAME = "hello";

    // 接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();

        // 连接rabbitMQ的队列
        factory.setHost("43.138.23.37");
        factory.setUsername("admin");
        factory.setPassword("123456");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // 消费者未成功接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println(new String(message.getBody()));

        };

        // 消费者取消消费的回调
        CancelCallback cancelCallback = consumerTag -> {

            System.out.println("消息消费被中断了");

        };


        /**
         * autoACK表示是否自动答
         * 消费者未成功接收消息的回调
         * 消费者取消消费的回调
         */
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);




    }
}
