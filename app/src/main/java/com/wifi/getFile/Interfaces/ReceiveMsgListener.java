package com.wifi.getFile.Interfaces;

import com.wifi.getFile.Data.ChatMessage;

/**
 * 接收消息监听的listener接口
 *
 *
 */
public interface ReceiveMsgListener {
    public boolean receive(ChatMessage msg);

}
