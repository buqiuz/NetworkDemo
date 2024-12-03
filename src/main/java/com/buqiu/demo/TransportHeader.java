package com.buqiu.demo;

/**
 * Represents the Transport Layer Header (e.g., TCP/UDP).
 */
public class TransportHeader {
    private short sourcePort;
    private short destPort;
    private int sequenceNumber;
    private int acknowledgmentNumber;
    private short dataOffsetAndFlags;
    private short windowSize;
    private short checksum;
    private short urgentPointer;

    // Getters and Setters
    public short getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(short sourcePort) {
        this.sourcePort = sourcePort;
    }

    public short getDestPort() {
        return destPort;
    }

    public void setDestPort(short destPort) {
        this.destPort = destPort;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public void setAcknowledgmentNumber(int acknowledgmentNumber) {
        this.acknowledgmentNumber = acknowledgmentNumber;
    }

    public short getDataOffsetAndFlags() {
        return dataOffsetAndFlags;
    }

    public void setDataOffsetAndFlags(short dataOffsetAndFlags) {
        this.dataOffsetAndFlags = dataOffsetAndFlags;
    }

    public short getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(short windowSize) {
        this.windowSize = windowSize;
    }

    public short getChecksum() {
        return checksum;
    }

    public void setChecksum(short checksum) {
        this.checksum = checksum;
    }

    public short getUrgentPointer() {
        return urgentPointer;
    }

    public void setUrgentPointer(short urgentPointer) {
        this.urgentPointer = urgentPointer;
    }

    @Override
    public String toString() {
        return "TransportHeader{" +
                "sourcePort=" + sourcePort +
                ", destPort=" + destPort +
                ", sequenceNumber=" + sequenceNumber +
                ", acknowledgmentNumber=" + acknowledgmentNumber +
                ", dataOffsetAndFlags=" + dataOffsetAndFlags +
                ", windowSize=" + windowSize +
                ", checksum=0x" + String.format("%04X", checksum) +
                ", urgentPointer=" + urgentPointer +
                '}';
    }
}
