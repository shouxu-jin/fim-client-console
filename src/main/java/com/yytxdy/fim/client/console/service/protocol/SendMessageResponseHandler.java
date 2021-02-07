package com.yytxdy.fim.client.console.service.protocol;

import com.yytxdy.fim.protocol.Fim;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SendMessageResponseHandler implements ProtocolHandler {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageResponseHandler.class);

    @Override
    public boolean support(Fim.Protocol.DataType dataType) {
        return Fim.Protocol.DataType.SendMessageResponseType.equals(dataType);
    }

    @Override
    public void handler(Fim.Protocol protocol, ChannelHandlerContext ctx) {
        Fim.SendMessageResponse response = protocol.getSendMessageResponse();
        if (response.getSuccess()) {
            logger.info("send message success: " + response.getMessageId());
        } else {
            logger.error("send message failed: " + response.getErrorMessage());
        }
    }
}
