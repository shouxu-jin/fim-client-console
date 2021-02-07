package com.yytxdy.fim.client.console.service.protocol;

import com.yytxdy.fim.protocol.Fim;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginResponseProtocolHandler implements ProtocolHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginResponseProtocolHandler.class);

    @Override
    public boolean support(Fim.Protocol.DataType dataType) {
        return Fim.Protocol.DataType.LoginResponseType.equals(dataType);
    }

    @Override
    public void handler(Fim.Protocol protocol, ChannelHandlerContext ctx) {
        Fim.LoginResponse response = protocol.getLoginResponse();
        if (!response.getSuccess()) {
            logger.error("登陆失败: " + response.getErrorMessage());
            System.exit(0);
        }
        logger.info("登陆成功");
    }
}
