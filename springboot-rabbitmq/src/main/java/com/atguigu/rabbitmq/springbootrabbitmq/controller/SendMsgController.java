package com.atguigu.rabbitmq.springbootrabbitmq.controller;

import com.atguigu.rabbitmq.springbootrabbitmq.config.DelayQueueConfig;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/ttl")
public class SendMsgController {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    //     开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message) {
        log.info("当前时间：{},发送一条信息给两个TTL队列：{}", new Date().toString(), message);

        rabbitTemplate.convertAndSend("X", "XA", "消息来自ttl为10s的队列" + message);
        rabbitTemplate.convertAndSend("X", "XB", "消息来自ttl为40s的队列" + message);

    }

    //     开始发带TTL的消息
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendMsg(@PathVariable String message, @PathVariable String ttlTime) {
        log.info("当前时间：{},发送一条时长{}毫秒TTl信息给队列QC：{}", new Date().toString(), ttlTime, message);
        rabbitTemplate.convertAndSend("X", "XC", message, msg -> {
        //     设置发送消息时的延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

//     开始发消息 基于插件的消息及延迟的时间
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendMsg(@PathVariable String message, @PathVariable Integer delayTime) {
        log.info("当前时间：{},发送一条时长{}毫秒TTl信息给延迟队列delay.queue：{}", new Date().toString(), delayTime, message);
        rabbitTemplate.convertAndSend(DelayQueueConfig.DELAYED_EXCHANGE_NAME, DelayQueueConfig.DELAYED_ROUTING_KEY, message, msg -> {
            //     设置发送消息时的延迟时长
            msg.getMessageProperties().setDelay(delayTime);

            return msg;
        });
}




}
