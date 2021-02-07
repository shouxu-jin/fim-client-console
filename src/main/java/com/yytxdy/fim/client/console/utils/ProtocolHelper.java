package com.yytxdy.fim.client.console.utils;

import com.yytxdy.fim.protocol.Fim;

public class ProtocolHelper {
    public static final Fim.Protocol loginRequest(Fim.LoginRequest loginRequest) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.LoginRequestType).setLoginRequest(loginRequest).build();
    }

    public static final Fim.Protocol loginResponse(Fim.LoginResponse loginResponse) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.LoginResponseType).setLoginResponse(loginResponse).build();
    }

    public static final Fim.Protocol sendMessageRequest(Fim.SendMessageRequest sendMessageRequest) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.SendMessageRequestType).setSendMessageRequest(sendMessageRequest).build();
    }

    public static final Fim.Protocol sendMessageResponse(Fim.SendMessageResponse sendMessageResponse) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.SendMessageResponseType).setSendMessageResponse(sendMessageResponse).build();
    }

    public static final Fim.Protocol heartbeatRequest(Fim.HeartbeatRequest heartbeatRequest) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.HeartbeatRequestType).setHeartbeatRequest(heartbeatRequest).build();
    }

    public static final Fim.Protocol heartbeatResponse(Fim.HeartbeatResponse heartbeatResponse) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.HeartbeatResponseType).setHeartbeatResponse(heartbeatResponse).build();
    }

    public static final Fim.Protocol notifyRequest(Fim.NotifyRequest notifyRequest) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.NotifyRequestType).setNotifyRequest(notifyRequest).build();
    }

    public static final Fim.Protocol notifyResponse(Fim.NotifyResponse notifyResponse) {
        return Fim.Protocol.newBuilder().setDataType(Fim.Protocol.DataType.NotifyResponseType).setNotifyResponse(notifyResponse).build();
    }
}
