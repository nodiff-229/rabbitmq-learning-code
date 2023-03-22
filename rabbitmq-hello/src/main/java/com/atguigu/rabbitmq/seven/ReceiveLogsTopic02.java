package com.atguigu.rabbitmq.seven;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsTopic02 {

    public static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = "Q2";
        channel.queueDeclare(queueName, false, false, false, null);
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*.rabbit");
        channel.queueBind(queueName, EXCHANGE_NAME, "lazy.#");
        System.out.println("ReceiveLogsTopic02等待接受消息......");

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("ReceiveLogsTopic02控制台打印接受到的消息：" + new String(message.getBody(), "UTF-8"));
            System.out.println("接受队列：" + queueName + " 绑定键：" + message.getEnvelope().getRoutingKey());
        };

        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {});

    }
}
