package com.atguigu.rabbitmq.one;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

//    队列名称
    public static final String QUEUE_NAME = "hello";

    // 发消息
    public static void main(String[] args) throws IOException, TimeoutException {
    //     创建一个连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        // 连接rabbitMQ的队列
        factory.setHost("166.111.82.35");
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 创建连接
        Connection connection = factory.newConnection();

        // 创建信道
        Channel channel = connection.createChannel();

        /**
         * 生成一个队列
         * 1. 队列名称
         * 2. 队列里面的消息是否持久化（磁盘），默认情况消息存储在内存中
         * 3. 该队列是否只供一个消费者进行消费，是否进行消息共享，true可以由多个消费者消费，false整一个消费者消费
         * 4. 是否自动删除 最后一个消费者断开连接以后，true自动删除，false不自动删除
         * 5. 其它参数
         */
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        // 发消息
        String message = "hello world";


        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");

    }
}
