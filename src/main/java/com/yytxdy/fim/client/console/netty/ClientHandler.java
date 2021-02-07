package com.yytxdy.fim.client.console.netty;

import com.yytxdy.fim.client.console.service.protocol.ProtocolHandler;
import com.yytxdy.fim.protocol.Fim;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ChannelHandler.Sharable
public class ClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandler.class);
    @Autowired
    private List<ProtocolHandler> protocolHandlers;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        Fim.Protocol protocol = (Fim.Protocol) msg;
        logger.info(protocol.toString());
        Fim.Protocol.DataType dataType = protocol.getDataType();
        for (ProtocolHandler handler : protocolHandlers) {
            if (handler.support(dataType)) {
                handler.handler(protocol, ctx);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("客户端异常");
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel != null) {
            channel.close();
        }
        ctx.close();
    }
}
