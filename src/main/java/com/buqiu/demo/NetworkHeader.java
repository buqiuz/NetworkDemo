package com.buqiu.demo;

/**
 * Represents the Network Layer Header (e.g., IPv4).
 */
public class NetworkHeader {
    private byte version;
    private byte ihl; // Internet Header Length
    private byte typeOfService;
    private short totalLength;
    private short identification;
    private short flagsAndFragmentOffset;
    private byte ttl;
    private byte protocol;
    private short headerChecksum;
    private int sourceIp;
    private int destinationIp;

    // Getters and Setters
    public byte getVersion() {
        return version;
    }

    public void setVersion(byte version) {
        this.version = version;
    }

    public byte getIhl() {
        return ihl;
    }

    public void setIhl(byte ihl) {
        this.ihl = ihl;
    }

    public byte getTypeOfService() {
        return typeOfService;
    }

    public void setTypeOfService(byte typeOfService) {
        this.typeOfService = typeOfService;
    }

    public short getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(short totalLength) {
        this.totalLength = totalLength;
    }

    public short getIdentification() {
        return identification;
    }

    public void setIdentification(short identification) {
        this.identification = identification;
    }

    public short getFlagsAndFragmentOffset() {
        return flagsAndFragmentOffset;
    }

    public void setFlagsAndFragmentOffset(short flagsAndFragmentOffset) {
        this.flagsAndFragmentOffset = flagsAndFragmentOffset;
    }

    public byte getTtl() {
        return ttl;
    }

    public void setTtl(byte ttl) {
        this.ttl = ttl;
    }

    public byte getProtocol() {
        return protocol;
    }

    public void setProtocol(byte protocol) {
        this.protocol = protocol;
    }

    public short getHeaderChecksum() {
        return headerChecksum;
    }

    public void setHeaderChecksum(short headerChecksum) {
        this.headerChecksum = headerChecksum;
    }

    public int getSourceIp() {
        return sourceIp;
    }

    public void setSourceIp(int sourceIp) {
        this.sourceIp = sourceIp;
    }

    public int getDestinationIp() {
        return destinationIp;
    }

    public void setDestinationIp(int destinationIp) {
        this.destinationIp = destinationIp;
    }

    @Override
    public String toString() {
        return "NetworkHeader{" +
                "version=" + version +
                ", ihl=" + ihl +
                ", typeOfService=" + typeOfService +
                ", totalLength=" + totalLength +
                ", identification=0x" + String.format("%04X", identification) +
                ", flagsAndFragmentOffset=0x" + String.format("%04X", flagsAndFragmentOffset) +
                ", ttl=" + ttl +
                ", protocol=" + protocol +
                ", headerChecksum=0x" + String.format("%04X", headerChecksum) +
                ", sourceIp=" + ipToString(sourceIp) +
                ", destinationIp=" + ipToString(destinationIp) +
                '}';
    }

    /**
     * 工具方法：将整数表示的 IP 转换为字符串形式。
     *
     * @param ip 整数表示的 IP 地址
     * @return 字符串形式的 IP 地址
     */
    private String ipToString(int ip) {
        return String.format("%d.%d.%d.%d",
                (ip >> 24) & 0xFF,
                (ip >> 16) & 0xFF,
                (ip >> 8) & 0xFF,
                ip & 0xFF);
    }
}
