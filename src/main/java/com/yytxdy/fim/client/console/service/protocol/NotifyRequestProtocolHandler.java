package com.yytxdy.fim.client.console.service.protocol;

import com.yytxdy.fim.client.console.utils.ProtocolHelper;
import com.yytxdy.fim.protocol.Fim;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class NotifyRequestProtocolHandler implements ProtocolHandler {
    private static final Logger logger = LoggerFactory.getLogger(NotifyRequestProtocolHandler.class);

    @Override
    public boolean support(Fim.Protocol.DataType dataType) {
        return Fim.Protocol.DataType.NotifyRequestType.equals(dataType);
    }

    @Override
    public void handler(Fim.Protocol protocol, ChannelHandlerContext ctx) {
        Fim.NotifyRequest request = protocol.getNotifyRequest();
        logger.info("receive message: " + request.getContent());
        // 通知服务端删除离线信息
        Fim.NotifyResponse response = Fim.NotifyResponse.newBuilder().setReceiverId(request.getReceiverId()).setMessageId(request.getMessageId()).build();
        ctx.channel().writeAndFlush(ProtocolHelper.notifyResponse(response));
    }
}
