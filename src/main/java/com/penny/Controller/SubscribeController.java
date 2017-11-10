package com.penny.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2017/11/10.
 */
@RestController
public class SubscribeController {

    private final static Logger logger = LoggerFactory.getLogger(SubscribeController.class);


    @GetMapping(value = "/pubMessage")
    public String pubMessage(){
        String sMsg = null;

        MessageHandler messageHandler = new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
//               sMsg = message.getPayload().toString();
            }
        };

        return sMsg;

    }

}
