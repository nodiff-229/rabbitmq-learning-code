package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.util.HashMap;
import java.util.Map;

public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";



    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        for (int i = 1; i < 11; i++) {
            String message = "info" + i;

            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();

            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan", null, message.getBytes());


        }


    }

}
