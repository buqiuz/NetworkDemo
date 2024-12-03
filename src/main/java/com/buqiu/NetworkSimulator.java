package com.buqiu;

/**
 * <p>
 * 类描述
 * </p>
 *
 * @author: 不秋
 * @since: 2024-12-04 02:46:51
 */
import java.io.*;
import java.nio.ByteBuffer;

public class NetworkSimulator {

    // 封装过程，从应用层到物理层
    public static byte[] encapsulate(byte[] appData) throws IOException {
        // 应用层到传输层
        byte[] transportData = transportLayerEncapsulate(appData);

        // 传输层到网络层
        byte[] networkData = networkLayerEncapsulate(transportData);

        // 网络层到数据链路层
        byte[] dataLinkData = dataLinkLayerEncapsulate(networkData);

        // 数据链路层到物理层
        byte[] physicalData = physicalLayerEncapsulate(dataLinkData);

        return physicalData;
    }

    // 解封装过程，从物理层到应用层
    public static byte[] decapsulate(byte[] physicalData) throws IOException {
        // 物理层到数据链路层
        byte[] dataLinkData = physicalLayerDecapsulate(physicalData);

        // 数据链路层到网络层
        byte[] networkData = dataLinkLayerDecapsulate(dataLinkData);

        // 网络层到传输层
        byte[] transportData = networkLayerDecapsulate(networkData);

        // 传输层到应用层
        byte[] appData = transportLayerDecapsulate(transportData);

        return appData;
    }

    // 传输层封装，加上传输层头部（模拟 TCP）
    private static byte[] transportLayerEncapsulate(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 模拟 TCP 头部（源端口、目的端口、序列号、确认号等）
        ByteBuffer buffer = ByteBuffer.allocate(20); // TCP 头部最小20字节
        buffer.putShort((short) 12345); // 源端口
        buffer.putShort((short) 80);    // 目的端口
        buffer.putInt(1);               // 序列号
        buffer.putInt(0);               // 确认号
        buffer.putShort((short) ((5 << 12) | 0)); // 数据偏移（5）和保留位、标志位
        buffer.putShort((short) 0);     // 窗口大小
        buffer.putShort((short) 0);     // 校验和
        buffer.putShort((short) 0);     // 紧急指针

        baos.write(buffer.array());
        baos.write(data);
        return baos.toByteArray();
    }

    // 传输层解封装，去掉传输层头部
    private static byte[] transportLayerDecapsulate(byte[] data) {
        int headerLength = 20; // TCP 头部最小20字节
        byte[] result = new byte[data.length - headerLength];
        System.arraycopy(data, headerLength, result, 0, result.length);
        return result;
    }

    // 网络层封装，加上网络层头部（模拟 IPv4）
    private static byte[] networkLayerEncapsulate(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 模拟 IPv4 头部
        ByteBuffer buffer = ByteBuffer.allocate(20); // IPv4 头部最小20字节
        buffer.put((byte) ((4 << 4) | 5)); // 版本（4）和头部长度（5）
        buffer.put((byte) 0); // 服务类型
        buffer.putShort((short) (20 + data.length)); // 总长度
        buffer.putShort((short) 0); // 标识符
        buffer.putShort((short) 0); // 标志位和片偏移
        buffer.put((byte) 64); // 生存时间
        buffer.put((byte) 6);  // 协议（TCP 为6）
        buffer.putShort((short) 0); // 头部校验和
        buffer.putInt(0xC0A80001); // 源 IP 地址（192.168.0.1）
        buffer.putInt(0xC0A80002); // 目的 IP 地址（192.168.0.2）

        baos.write(buffer.array());
        baos.write(data);
        return baos.toByteArray();
    }

    // 网络层解封装，去掉网络层头部
    private static byte[] networkLayerDecapsulate(byte[] data) {
        int headerLength = (data[0] & 0x0F) * 4; // 头部长度
        byte[] result = new byte[data.length - headerLength];
        System.arraycopy(data, headerLength, result, 0, result.length);
        return result;
    }

    // 数据链路层封装，加上头部和尾部（模拟以太网帧）
    private static byte[] dataLinkLayerEncapsulate(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 以太网头部
        ByteBuffer buffer = ByteBuffer.allocate(14);
        buffer.put(new byte[]{(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF}); // 目的 MAC 地址
        buffer.put(new byte[]{0x00, 0x0C, 0x29, 0x3E, 0x7A, 0x6B}); // 源 MAC 地址
        buffer.putShort((short) 0x0800); // 类型字段（0x0800 表示 IPv4）

        baos.write(buffer.array());
        baos.write(data);

        // 以太网尾部（FCS），这里为了简单模拟，填充4个字节的0
        baos.write(new byte[4]);

        return baos.toByteArray();
    }

    // 数据链路层解封装，去掉头部和尾部
    private static byte[] dataLinkLayerDecapsulate(byte[] data) {
        int headerLength = 14; // 以太网头部14字节
        int trailerLength = 4; // FCS 4字节
        byte[] result = new byte[data.length - headerLength - trailerLength];
        System.arraycopy(data, headerLength, result, 0, result.length);
        return result;
    }

    // 物理层封装，这里可以模拟信道编码等处理
    public static byte[] physicalLayerEncapsulate(byte[] data) {
        // 模拟物理层编码，例如曼彻斯特编码，这里简单地做一个按位取反
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte b : data) {
            baos.write(~b); // 按位取反，模拟物理层编码
        }
        return baos.toByteArray();
    }

    // 物理层解封装，还原数据
    public static byte[] physicalLayerDecapsulate(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte b : data) {
            baos.write(~b); // 按位取反，恢复原始数据
        }
        return baos.toByteArray();
    }
}
