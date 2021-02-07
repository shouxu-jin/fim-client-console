package com.yytxdy.fim.client.console.netty;

import com.yytxdy.fim.client.console.utils.ProtocolHelper;
import com.yytxdy.fim.protocol.Fim;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.ScheduledFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@ChannelHandler.Sharable
public class HeartbeatHandler extends ChannelInboundHandlerAdapter {
    private Channel channel;
    private boolean sync;
    @Value("${fim.client.telephone}")
    private long telephone;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        channel = ctx.channel();
        sendHeartbeat(channel);
    }

    private void sendHeartbeat(Channel channel) {
        ScheduledFuture<?> future = channel.eventLoop().schedule(() -> {
            if (channel.isActive()) {
                Fim.HeartbeatRequest request = Fim.HeartbeatRequest.newBuilder().setUserId(telephone).setSyncMessage(sync).build();
                sync = !sync;
                channel.writeAndFlush(ProtocolHelper.heartbeatRequest(request));
            } else {
                channel.closeFuture();
                throw new RuntimeException();
            }
        }, 10, TimeUnit.SECONDS);

        future.addListener((GenericFutureListener) future1 -> {
            if (future1.isSuccess()) {
                sendHeartbeat(channel);
            }
        });
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
