package com.buqiu.demo;

import com.buqiu.demo.DataLinkHeader;
import com.buqiu.demo.NetworkHeader;
import com.buqiu.demo.TransportHeader;

/**
 * Represents a complete network packet with all layer headers and application data.
 */
public class Packet {
    private byte[] appData;
    private TransportHeader transportHeader;
    private NetworkHeader networkHeader;
    private DataLinkHeader dataLinkHeader;

    public Packet(byte[] appData, TransportHeader transportHeader, NetworkHeader networkHeader, DataLinkHeader dataLinkHeader) {
        this.appData = appData;
        this.transportHeader = transportHeader;
        this.networkHeader = networkHeader;
        this.dataLinkHeader = dataLinkHeader;
    }

    // Getters and Setters
    public byte[] getAppData() {
        return appData;
    }

    public void setAppData(byte[] appData) {
        this.appData = appData;
    }

    public TransportHeader getTransportHeader() {
        return transportHeader;
    }

    public void setTransportHeader(TransportHeader transportHeader) {
        this.transportHeader = transportHeader;
    }

    public NetworkHeader getNetworkHeader() {
        return networkHeader;
    }

    public void setNetworkHeader(NetworkHeader networkHeader) {
        this.networkHeader = networkHeader;
    }

    public DataLinkHeader getDataLinkHeader() {
        return dataLinkHeader;
    }

    public void setDataLinkHeader(DataLinkHeader dataLinkHeader) {
        this.dataLinkHeader = dataLinkHeader;
    }
}
