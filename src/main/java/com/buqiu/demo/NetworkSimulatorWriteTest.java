package com.buqiu.demo;

import com.buqiu.demo.*;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * <p>
 * Test class for NetworkSimulator: Encapsulates data and writes to a binary file.
 * </p>
 *
 * Author: 不秋
 * Since: 2024-12-04
 */
public class NetworkSimulatorWriteTest {

    public static void main(String[] args) {
        try {
            // ------------------- 创建应用层数据 -------------------
            String message = "这是一个测试消息，用于封装到二进制文件中。";
            byte[] appData = message.getBytes("UTF-8");
            System.out.println("应用层数据: " + message);

            // ------------------- 创建传输层头部 -------------------
            TransportHeader transportHeader = new TransportHeader();
            transportHeader.setSourcePort((short) 54321);
            transportHeader.setDestPort((short) 8080);
            transportHeader.setSequenceNumber(1001);
            transportHeader.setAcknowledgmentNumber(2002);
            transportHeader.setDataOffsetAndFlags((short) ((5 << 12) | 0x0010)); // 数据偏移和标志位
            transportHeader.setWindowSize((short) 8192);
            transportHeader.setChecksum((short) 0xABCD);
            transportHeader.setUrgentPointer((short) 0);

            // ------------------- 创建网络层头部 -------------------
            NetworkHeader networkHeader = new NetworkHeader();
            networkHeader.setVersion((byte) 4);
            networkHeader.setIhl((byte) 5);
            networkHeader.setTypeOfService((byte) 0);
            // networkHeader.setTotalLength((short) 0); // 将在封装过程中设置
            networkHeader.setIdentification((short) 0x1A2B);
            networkHeader.setFlagsAndFragmentOffset((short) 0x4000); // 不分片
            networkHeader.setTtl((byte) 128);
            networkHeader.setProtocol((byte) 6); // TCP
            networkHeader.setHeaderChecksum((short) 0x1234);
            networkHeader.setSourceIp(ipToInt("10.0.0.1"));
            networkHeader.setDestinationIp(ipToInt("10.0.0.2"));

            // ------------------- 创建数据链路层头部 -------------------
            DataLinkHeader dataLinkHeader = new DataLinkHeader();
            dataLinkHeader.setDestinationMac(new byte[]{0x00, 0x1A, 0x2B, 0x3C, 0x4D, 0x5E});
            dataLinkHeader.setSourceMac(new byte[]{0x00, 0x1B, 0x2C, 0x3D, 0x4E, 0x5F});
            dataLinkHeader.setType((short) 0x0800); // IPv4
            dataLinkHeader.setFcs(new byte[]{0x12, 0x34, 0x56, 0x78}); // 模拟的 FCS

            // ------------------- 创建数据包 -------------------
            Packet packet = new Packet(appData, transportHeader, networkHeader, dataLinkHeader);
            System.out.println("\n创建的数据包已准备好。");

            // ------------------- 数据封装 -------------------
            byte[] physicalData = NetworkSimulator.encapsulate(packet);
            System.out.println("\n封装后的物理层数据: " + NetworkSimulator.bytesToHex(physicalData));

            // ------------------- 写入二进制文件 -------------------
            String filePath = "encapsulatedData.bin";
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(physicalData);
                System.out.println("\n封装后的数据已写入文件: " + filePath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工具方法：将 IP 地址字符串转换为整数。
     *
     * @param ip IP 地址字符串，例如 "192.168.1.1"
     * @return 转换后的整数表示
     */
    private static int ipToInt(String ip) {
        String[] parts = ip.split("\\.");
        int result = 0;
        for(String part : parts){
            result = (result << 8) | Integer.parseInt(part);
        }
        return result;
    }
}
