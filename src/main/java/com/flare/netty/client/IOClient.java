package com.flare.netty.client;

import java.net.Socket;
import java.util.Date;

/**
 * @ClassName IOClient
 * @Description 客户端
 * @Author xieyi
 * @Data 2019/1/11
 */
public class IOClient {
    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8000);
                while (true) {
                    socket.getOutputStream().write((new Date() + ":hello world").getBytes());
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
