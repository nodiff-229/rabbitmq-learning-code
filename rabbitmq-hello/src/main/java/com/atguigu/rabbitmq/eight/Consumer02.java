package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer02 {


    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            System.out.println("Consumer02控制台打印接受到的消息：" + new String(message.getBody(), "UTF-8"));
        };


        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> {
        });


    }
}
