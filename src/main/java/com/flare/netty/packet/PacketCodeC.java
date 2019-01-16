package com.flare.netty.packet;

import com.flare.netty.packet.command.Command;
import com.flare.netty.packet.request.LoginRequestPacket;
import com.flare.netty.packet.request.MessageRequestPacket;
import com.flare.netty.packet.response.LoginResponsePacket;
import com.flare.netty.packet.response.MessageResponsePacket;
import com.flare.netty.serialize.Serializer;
import com.flare.netty.serialize.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName PacketCodeC
 * @Description 封装成二进制
 * @Author xieyi
 * @Data 2019/1/15
 */
public class PacketCodeC {
    private static final int MAGIC_NUMBER = 0x12345678;

    private static class SingletonHandler {
        public static PacketCodeC INSTANCE = new PacketCodeC();
    }

    public static PacketCodeC getInstance(){
        return SingletonHandler.INSTANCE;
    }

    /**
     * 数据包类型
      */
    private static final Map<Byte, Class<? extends Packet>> packetTypeMap;
    /**
     * 序列化类型
     */
    private static final Map<Byte, Serializer> serializerMap;

    /**
     * 初始化
     */
    static {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Command.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Command.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Command.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Command.MESSAGE_RESPONSE, MessageResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * 编码
     * @param packet
     * @return
     */
    public ByteBuf encode(ByteBufAllocator byteBufAllocator,Packet packet){
        //1.创建 ByteBuf对象
        ByteBuf byteBuf = byteBufAllocator.ioBuffer();
        //2.序列化java对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        //3.实际编码过程
        //魔数 4
        byteBuf.writeInt(MAGIC_NUMBER);
        //版本号 1
        byteBuf.writeByte(packet.getVersion());
        //序列化算法 1
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        //指令 1
        byteBuf.writeByte(packet.getCommand());
        //数据长度
        byteBuf.writeInt(bytes.length);
        //数据
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 解码
     * @param byteBuf
     * @return
     */
    public Packet decode(ByteBuf byteBuf){
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识，读取一个字节
        byte serializeAlgorithm  = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度 4个字节
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        // 获取请求类
        Class<? extends Packet> requestType = getRequestType(command);

        // 获取序列化类型
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            // 解码
            return serializer.deserialize(requestType, bytes);
        }
        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
