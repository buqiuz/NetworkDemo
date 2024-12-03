package com.buqiu;

/**
 * <p>
 * 可以将应用层数据封装到物理层数据，并写入二进制文件
 * </p>
 *
 * @author: 不秋
 * @since: 2024-12-04 02:47:13
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Encapsulation {
    public static void main(String[] args) {
        try {
            // 原始应用层数据
            String appData = "模拟数据";

            // 封装过程
            byte[] physicalData = NetworkSimulator.encapsulate(appData.getBytes(StandardCharsets.UTF_8));

            // 将封装后的数据写入二进制文件
            try (FileOutputStream fos = new FileOutputStream("complex_physical_layer_data.bin")) {
                fos.write(physicalData);
            }

            System.out.println("数据已封装并写入 'complex_physical_layer_data.bin' 文件。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
