package com.buqiu.test;

/**
 * <p>
 * 可以从二进制文件中解封装，然后得到数据
 * </p>
 *
 * @author: 不秋
 * @since: 2024-12-04 02:47:49
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Decapsulation {
    public static void main(String[] args) {
        try {
            // 从二进制文件读取封装后的数据
            byte[] receivedData;
            try (FileInputStream fis = new FileInputStream("complex_physical_layer_data.bin")) {
                receivedData = fis.readAllBytes();
            }

            // 解封装过程
            byte[] appLayerData = NetworkSimulator.decapsulate(receivedData);

            // 输出解封装后的应用层数据
            String receivedAppData = new String(appLayerData, StandardCharsets.UTF_8);
            System.out.println("解封装后的应用层数据：" + receivedAppData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
