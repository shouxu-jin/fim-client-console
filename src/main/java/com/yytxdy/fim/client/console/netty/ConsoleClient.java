package com.yytxdy.fim.client.console.netty;

import com.yytxdy.fim.client.console.utils.ProtocolHelper;
import com.yytxdy.fim.protocol.Fim;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Scanner;

@Component
public class ConsoleClient {
    private static final Logger logger = LoggerFactory.getLogger(ConsoleClient.class);
    @Value("${fim.client.telephone}")
    private long telephone;
    @Value("${fim.client.token}")
    private String token;
    @Value("${fim.server.ip}")
    private String ip;
    @Value("${fim.server.port}")
    private int port;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;
    @Autowired
    private HeartbeatHandler heartbeatHandler;
    @Autowired
    private ClientHandler clientHandler;
    private Channel channel = null;

    public void startClient() {
        EventLoopGroup loopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(loopGroup).channel(NioSocketChannel.class);
        // 设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // 设置禁用nagle算法
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        // 设置连接超时时长
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10 * 1000);
        // 设置初始化Channel
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new ProtobufVarint32FrameDecoder());
                pipeline.addLast(new ProtobufDecoder(Fim.Protocol.getDefaultInstance()));
                pipeline.addLast(new ProtobufVarint32LengthFieldPrepender());
                pipeline.addLast(new ProtobufEncoder());
                pipeline.addLast(clientHandler);
                pipeline.addLast(heartbeatHandler);
            }
        });

        try {
            channel = bootstrap.connect(ip, port).sync().channel();
        } catch (Exception e) {
            logger.error("连接失败", e);
            System.exit(0);
        }
        setLoginParam();
        login();
        startConsoleMessageListener();
    }

    private void setLoginParam() {
        redisTemplate.boundValueOps(String.valueOf(telephone)).set(token);
        redisTemplate.boundValueOps(telephone + "-login").set(ip);
    }

    private void login() {
        Fim.LoginRequest loginRequest = Fim.LoginRequest.newBuilder().setUserId(telephone).setToken(token).build();
        channel.writeAndFlush(ProtocolHelper.loginRequest(loginRequest));
    }

    private void startConsoleMessageListener() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String line = scanner.nextLine();
            if (StringUtils.hasLength(line)) {
                long receiverId = Long.valueOf(line.split(":")[0]);
                String content = line.split(":")[1];
                Fim.SendMessageRequest request = Fim.SendMessageRequest.newBuilder()
                        .setSenderId(telephone)
                        .setReceiverId(receiverId)
                        .setMessageType(Fim.MessageType.text)
                        .setContent(content).build();
                channel.writeAndFlush(ProtocolHelper.sendMessageRequest(request));
            }
        }
    }
}
