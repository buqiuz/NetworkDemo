package com.buqiu.demo;

/**
 * Represents the Data Link Layer Header (e.g., Ethernet).
 */
public class DataLinkHeader {
    private byte[] destinationMac; // 6 bytes
    private byte[] sourceMac;      // 6 bytes
    private short type;
    private byte[] fcs;            // 4 bytes (simulated)

    // Getters and Setters
    public byte[] getDestinationMac() {
        return destinationMac;
    }

    public void setDestinationMac(byte[] destinationMac) {
        this.destinationMac = destinationMac;
    }

    public byte[] getSourceMac() {
        return sourceMac;
    }

    public void setSourceMac(byte[] sourceMac) {
        this.sourceMac = sourceMac;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public byte[] getFcs() {
        return fcs;
    }

    public void setFcs(byte[] fcs) {
        this.fcs = fcs;
    }

    @Override
    public String toString() {
        return "DataLinkHeader{" +
                "destinationMac=" + macToString(destinationMac) +
                ", sourceMac=" + macToString(sourceMac) +
                ", type=0x" + String.format("%04X", type) +
                ", fcs=" + (fcs != null ? bytesToHex(fcs) : "null") +
                '}';
    }

    /**
     * Utility method to convert MAC address bytes to human-readable string.
     *
     * @param mac The MAC address byte array.
     * @return The MAC address in "XX:XX:XX:XX:XX:XX" format.
     */
    private String macToString(byte[] mac) {
        if(mac == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X", mac[i]));
            if(i < mac.length - 1) sb.append(":");
        }
        return sb.toString();
    }

    /**
     * Utility method to convert byte array to hex string.
     *
     * @param bytes The byte array.
     * @return Hexadecimal representation of the byte array.
     */
    private String bytesToHex(byte[] bytes) {
        if(bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
