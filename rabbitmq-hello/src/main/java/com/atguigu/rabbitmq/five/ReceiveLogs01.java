package com.atguigu.rabbitmq.five;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class ReceiveLogs01 {

    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 生成一个临时队列，队列的名称是随机的
         * 当消费者断开与队列的连接时，队列就自动删除
         */
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, EXCHANGE_NAME, "");
        System.out.println("ReceiveLogs01等待接受消息......");

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("ReceiveLogs01控制台打印接受到的消息：" + new String(message.getBody(), "UTF-8"));
        };
        channel.basicConsume(queue, true, deliverCallback, consumerTag -> {
        });


    }
}
