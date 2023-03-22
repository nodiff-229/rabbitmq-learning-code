package com.atguigu.rabbitmq.three;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.atguigu.rabbitmq.utils.SleepUtils;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

public class Work03 {

    public static final String TASK_QUEUE_NAME = "ack_queue";





    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        System.out.println("C1等待接收消息处理时间较短");

        boolean autoAck = false;

        // 消费者未成功接收消息的回调
        DeliverCallback deliverCallback = (consumerTag, message)->{
            SleepUtils.sleep(1);
            System.out.println("接收到的消息：" + new String(message.getBody(), "UTF-8"));
            //     手动应答

            channel.basicAck(message.getEnvelope().getDeliveryTag(), false);

        };

        // 消费者取消消费的回调
        CancelCallback cancelCallback = consumerTag -> {

            System.out.println("消费者取消消费接口回调逻辑" + consumerTag);

        };

        //采用手动应答
        channel.basicConsume(TASK_QUEUE_NAME, autoAck, deliverCallback, cancelCallback);
    }
}
