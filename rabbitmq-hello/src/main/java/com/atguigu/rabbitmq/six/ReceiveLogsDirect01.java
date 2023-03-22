package com.atguigu.rabbitmq.six;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogsDirect01 {
    public static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        /**
         * 生成一个临时队列，队列的名称是随机的
         * 当消费者断开与队列的连接时，队列就自动删除
         */
        channel.queueDeclare("console", false, false, false, null);
        channel.queueBind("console", EXCHANGE_NAME, "info");
        channel.queueBind("console", EXCHANGE_NAME, "warning");

        System.out.println("ReceiveLogsDirect01等待接受消息......");

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("ReceiveLogsDirect01控制台打印接受到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume("console", true, deliverCallback, consumerTag -> {
        });


    }
}
