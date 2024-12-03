package com.buqiu.demo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * <p>
 * Refactored Network Simulator that supports dynamic data input for various layers.
 * </p>
 *
 * Author: 不秋
 * Since: 2024-12-04
 */
public class NetworkSimulator {

    /**
     * Encapsulates the application data into physical layer data using the provided headers.
     *
     * @param packet The packet containing all layer headers and application data.
     * @return The byte array representing the physical layer data.
     * @throws IOException If an I/O error occurs.
     */
    public static byte[] encapsulate(Packet packet) throws IOException {
        // Application layer to Transport layer
        byte[] transportData = transportLayerEncapsulate(packet.getAppData(), packet.getTransportHeader());

        // Transport layer to Network layer
        byte[] networkData = networkLayerEncapsulate(transportData, packet.getNetworkHeader());

        // Network layer to Data Link layer
        byte[] dataLinkData = dataLinkLayerEncapsulate(networkData, packet.getDataLinkHeader());

        // Data Link layer to Physical layer
        byte[] physicalData = physicalLayerEncapsulate(dataLinkData);

        return physicalData;
    }

    /**
     * Decapsulates the physical layer data back to application data and headers.
     *
     * @param physicalData The byte array representing the physical layer data.
     * @return The packet containing all layer headers and application data.
     * @throws IOException If an I/O error occurs.
     */
    public static Packet decapsulate(byte[] physicalData) throws IOException {
        // Physical layer to Data Link layer
        byte[] dataLinkDataWithFcs = physicalLayerDecapsulate(physicalData);

        // 数据链路层数据长度应至少为14（头部）+4（FCS）=18字节
        if (dataLinkDataWithFcs.length < 18) {
            throw new IOException("数据链路层数据长度不足。");
        }

        // 分割数据链路层数据为头部、有效载荷和尾部FCS
        int headerLength = 14; // 以太网头部14字节
        int trailerLength = 4; // FCS 4字节

        byte[] headerBytes = Arrays.copyOfRange(dataLinkDataWithFcs, 0, headerLength);
        byte[] fcsBytes = Arrays.copyOfRange(dataLinkDataWithFcs, dataLinkDataWithFcs.length - trailerLength, dataLinkDataWithFcs.length);
        byte[] networkData = Arrays.copyOfRange(dataLinkDataWithFcs, headerLength, dataLinkDataWithFcs.length - trailerLength);

        // 提取数据链路层头部并设置FCS
        DataLinkHeader dataLinkHeader = extractDataLinkHeader(headerBytes);
        dataLinkHeader.setFcs(fcsBytes);

        // Network layer to Transport layer
        byte[] transportData = networkLayerDecapsulate(networkData);

        // Transport layer to Application layer
        byte[] appData = transportLayerDecapsulate(transportData);

        // Extract headers from the respective layers
        TransportHeader transportHeader = extractTransportHeader(transportData);
        NetworkHeader networkHeader = extractNetworkHeader(networkData);

        return new Packet(appData, transportHeader, networkHeader, dataLinkHeader);
    }

    // ------------------- Transport Layer Methods -------------------

    /**
     * Encapsulates data at the transport layer by adding the transport header.
     *
     * @param data            The application data.
     * @param transportHeader The transport layer header.
     * @return The byte array representing transport layer data.
     * @throws IOException If an I/O error occurs.
     */
    private static byte[] transportLayerEncapsulate(byte[] data, TransportHeader transportHeader) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Construct transport layer header
        ByteBuffer buffer = ByteBuffer.allocate(20); // TCP header is typically 20 bytes
        buffer.putShort(transportHeader.getSourcePort());
        buffer.putShort(transportHeader.getDestPort());
        buffer.putInt(transportHeader.getSequenceNumber());
        buffer.putInt(transportHeader.getAcknowledgmentNumber());
        buffer.putShort(transportHeader.getDataOffsetAndFlags());
        buffer.putShort(transportHeader.getWindowSize());
        buffer.putShort(transportHeader.getChecksum());
        buffer.putShort(transportHeader.getUrgentPointer());

        baos.write(buffer.array());
        baos.write(data);
        return baos.toByteArray();
    }

    /**
     * Decapsulates data at the transport layer by removing the transport header.
     *
     * @param data The transport layer data.
     * @return The application data without the transport header.
     */
    private static byte[] transportLayerDecapsulate(byte[] data) {
        int headerLength = 20; // TCP header is typically 20 bytes
        if (data.length < headerLength) {
            throw new IllegalArgumentException("Transport layer data长度不足。");
        }
        return Arrays.copyOfRange(data, headerLength, data.length);
    }

    /**
     * Extracts the transport header from the transport layer data.
     *
     * @param data The transport layer data.
     * @return The extracted TransportHeader.
     */
    private static TransportHeader extractTransportHeader(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        TransportHeader header = new TransportHeader();
        header.setSourcePort(buffer.getShort());
        header.setDestPort(buffer.getShort());
        header.setSequenceNumber(buffer.getInt());
        header.setAcknowledgmentNumber(buffer.getInt());
        header.setDataOffsetAndFlags(buffer.getShort());
        header.setWindowSize(buffer.getShort());
        header.setChecksum(buffer.getShort());
        header.setUrgentPointer(buffer.getShort());
        return header;
    }

    // ------------------- Network Layer Methods -------------------

    /**
     * Encapsulates data at the network layer by adding the network header.
     *
     * @param data          The transport layer data.
     * @param networkHeader The network layer header.
     * @return The byte array representing network layer data.
     * @throws IOException If an I/O error occurs.
     */
    private static byte[] networkLayerEncapsulate(byte[] data, NetworkHeader networkHeader) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Calculate total length: network header + transport data
        networkHeader.setTotalLength((short) (20 + data.length)); // IPv4 header is typically 20 bytes

        // Construct network layer header
        ByteBuffer buffer = ByteBuffer.allocate(20); // IPv4 header is typically 20 bytes
        buffer.put((byte) ((networkHeader.getVersion() << 4) | (networkHeader.getIhl() & 0x0F)));
        buffer.put(networkHeader.getTypeOfService());
        buffer.putShort(networkHeader.getTotalLength());
        buffer.putShort(networkHeader.getIdentification());
        buffer.putShort(networkHeader.getFlagsAndFragmentOffset());
        buffer.put(networkHeader.getTtl());
        buffer.put(networkHeader.getProtocol());
        buffer.putShort(networkHeader.getHeaderChecksum());
        buffer.putInt(networkHeader.getSourceIp());
        buffer.putInt(networkHeader.getDestinationIp());

        baos.write(buffer.array());
        baos.write(data);
        return baos.toByteArray();
    }

    /**
     * Decapsulates data at the network layer by removing the network header.
     *
     * @param data The network layer data.
     * @return The transport layer data without the network header.
     */
    private static byte[] networkLayerDecapsulate(byte[] data) {
        if (data.length < 20) { // IPv4 header is at least 20 bytes
            throw new IllegalArgumentException("Network layer data长度不足。");
        }
        int headerLength = (data[0] & 0x0F) * 4; // IHL field specifies header length in 32-bit words
        if (headerLength < 20 || headerLength > data.length) {
            throw new IllegalArgumentException("无效的网络层头部长度。");
        }
        return Arrays.copyOfRange(data, headerLength, data.length);
    }

    /**
     * Extracts the network header from the network layer data.
     *
     * @param data The network layer data.
     * @return The extracted NetworkHeader.
     */
    private static NetworkHeader extractNetworkHeader(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        NetworkHeader header = new NetworkHeader();
        byte versionIhl = buffer.get();
        header.setVersion((byte) ((versionIhl >> 4) & 0x0F));
        header.setIhl((byte) (versionIhl & 0x0F));
        header.setTypeOfService(buffer.get());
        header.setTotalLength(buffer.getShort());
        header.setIdentification(buffer.getShort());
        header.setFlagsAndFragmentOffset(buffer.getShort());
        header.setTtl(buffer.get());
        header.setProtocol(buffer.get());
        header.setHeaderChecksum(buffer.getShort());
        header.setSourceIp(buffer.getInt());
        header.setDestinationIp(buffer.getInt());
        return header;
    }

    // ------------------- Data Link Layer Methods -------------------

    /**
     * Encapsulates data at the data link layer by adding the data link header and trailer.
     *
     * @param data             The network layer data.
     * @param dataLinkHeader The data link layer header.
     * @return The byte array representing data link layer data.
     * @throws IOException If an I/O error occurs.
     */
    private static byte[] dataLinkLayerEncapsulate(byte[] data, DataLinkHeader dataLinkHeader) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // Construct data link layer header
        ByteBuffer buffer = ByteBuffer.allocate(14); // Ethernet header is typically 14 bytes
        buffer.put(dataLinkHeader.getDestinationMac());
        buffer.put(dataLinkHeader.getSourceMac());
        buffer.putShort(dataLinkHeader.getType());

        baos.write(buffer.array());
        baos.write(data);

        // Data Link layer trailer (FCS) - simulated as 4 bytes
        baos.write(dataLinkHeader.getFcs());
        return baos.toByteArray();
    }

    /**
     * Decapsulates data at the data link layer by removing the data link header and trailer.
     *
     * @param data The data link layer data including header and trailer.
     * @return The network layer data without the data link header and trailer.
     */
    private static byte[] dataLinkLayerDecapsulate(byte[] data) {
        int headerLength = 14; // Ethernet header is typically 14 bytes
        int trailerLength = 4; // FCS is typically 4 bytes
        if (data.length < headerLength + trailerLength) {
            throw new IllegalArgumentException("Data link layer data长度不足。");
        }
        return data;
    }

    /**
     * Extracts the data link header from the data link layer header bytes.
     *
     * @param headerBytes The first 14 bytes of data link layer data.
     * @return The extracted DataLinkHeader.
     */
    private static DataLinkHeader extractDataLinkHeader(byte[] headerBytes) {
        ByteBuffer buffer = ByteBuffer.wrap(headerBytes);
        DataLinkHeader header = new DataLinkHeader();
        byte[] destMac = new byte[6];
        buffer.get(destMac);
        header.setDestinationMac(destMac);

        byte[] srcMac = new byte[6];
        buffer.get(srcMac);
        header.setSourceMac(srcMac);

        header.setType(buffer.getShort());

        // fcs将在调用者处设置
        return header;
    }

    // ------------------- Physical Layer Methods -------------------

    /**
     * Encapsulates data at the physical layer by applying encoding (e.g., bit inversion).
     *
     * @param data The data link layer data.
     * @return The byte array representing physical layer data.
     */
    public static byte[] physicalLayerEncapsulate(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte b : data) {
            baos.write(~b); // Bit inversion as a simple encoding
        }
        return baos.toByteArray();
    }

    /**
     * Decapsulates data at the physical layer by reversing the encoding.
     *
     * @param data The physical layer data.
     * @return The data link layer data without physical layer encoding.
     */
    public static byte[] physicalLayerDecapsulate(byte[] data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (byte b : data) {
            baos.write(~b); // Reverse bit inversion
        }
        return baos.toByteArray();
    }

    // ------------------- Helper Methods -------------------

    /**
     * Utility method to convert byte array to hex string for display.
     *
     * @param bytes The byte array.
     * @return Hexadecimal representation of the byte array.
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }

    // ------------------- Example Usage -------------------

    public static void main(String[] args) {
        // 这个 main 方法可以保留用于现有的测试
        // 您现在可以运行 NetworkSimulatorWriteTest 和 NetworkSimulatorReadTest 来进行封装和解封装测试
    }
}
