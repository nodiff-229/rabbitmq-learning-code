package com.atguigu.rabbitmq.eight;

import com.atguigu.rabbitmq.utils.RabbitMQUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class Consumer01 {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static final String DEAD_EXCHANGE = "dead_exchange";

    public static final String NORMAL_QUEUE = "normal_queue";

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();

        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", "lisi");
        arguments.put("x-max-length", 6);


        channel.queueDeclare(NORMAL_QUEUE, false, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, false, false, false, null);

        // 绑定交换机与队列
        channel.queueBind(NORMAL_QUEUE, NORMAL_EXCHANGE, "zhangsan");
        channel.queueBind(DEAD_QUEUE, DEAD_EXCHANGE, "lisi");
        System.out.println("等待接收消息......");

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String msg = new String(message.getBody(), "UTF-8");
            if (msg.equals("info5")) {
                System.out.println("Consumer01控制台打印接受到的消息：" + msg + ":此消息是被C1拒绝的");
                channel.basicReject(message.getEnvelope().getDeliveryTag(), false);

            } else {
                System.out.println("Consumer01控制台打印接受到的消息：" + msg);
                channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
            }

        };



        channel.basicConsume(NORMAL_QUEUE, false, deliverCallback, consumerTag -> {
        });


    }
}
