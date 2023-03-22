package com.atguigu.rabbitmq.two;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

/**
 * 这是一个工作线程，相当于之前的消费者
 */
public class Worker01 {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        // 消费者未成功接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("接收到的消息：" + new String(message.getBody()));

        };

        // 消费者取消消费的回调
        CancelCallback cancelCallback = consumerTag -> {

            System.out.println("消费者取消消费接口回调逻辑" + consumerTag);

        };

    //     消息的接收
        System.out.println("C2等待接受消息......");
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);

    }
}
