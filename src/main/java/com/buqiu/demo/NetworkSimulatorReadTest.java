package com.buqiu.demo;

import com.buqiu.demo.NetworkSimulator;
import com.buqiu.demo.Packet;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * <p>
 * Test class for NetworkSimulator: Reads encapsulated data from a binary file and decapsulates it.
 * </p>
 *
 * Author: 不秋
 * Since: 2024-12-04
 */
public class NetworkSimulatorReadTest {

    public static void main(String[] args) {
        try {
            // ------------------- 读取二进制文件 -------------------
            String filePath = "encapsulatedData.bin";
            byte[] physicalData = readFileToByteArray(filePath);
            System.out.println("读取的物理层数据: " + NetworkSimulator.bytesToHex(physicalData));

            // ------------------- 数据解封装 -------------------
            Packet decapsulatedPacket = NetworkSimulator.decapsulate(physicalData);
            String decapsulatedMessage = new String(decapsulatedPacket.getAppData(), "UTF-8");
            System.out.println("\n解封装后的应用层数据: " + decapsulatedMessage);

            // ------------------- 数据验证 -------------------
            // 假设原始消息是已知的，可以在实际测试中进行比对
            // 这里仅打印出来，您可以根据需要进行比对
            System.out.println("\n解封装后的传输层头部: " + decapsulatedPacket.getTransportHeader());
            System.out.println("解封装后的网络层头部: " + decapsulatedPacket.getNetworkHeader());
            System.out.println("解封装后的数据链路层头部: " + decapsulatedPacket.getDataLinkHeader());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 工具方法：从文件读取所有字节。
     *
     * @param filePath 文件路径
     * @return 文件内容的字节数组
     * @throws IOException 如果发生 I/O 错误
     */
    private static byte[] readFileToByteArray(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        byte[] data = new byte[fis.available()];
        int bytesRead = fis.read(data);
        fis.close();
        if (bytesRead != data.length) {
            throw new IOException("未能完整读取文件: " + filePath);
        }
        return data;
    }
}
