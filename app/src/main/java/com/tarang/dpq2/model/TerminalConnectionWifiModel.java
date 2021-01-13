package com.tarang.dpq2.model;

public class TerminalConnectionWifiModel {
    private String priority;
    private String communicationType;
    private String networkIpAddress;
    private String networkTcpPort;
    private String countAccessRetries;
    private String responseTimeout;
    private String sslCirtificateFile;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public String getNetworkIpAddress() {
        return networkIpAddress;
    }

    public void setNetworkIpAddress(String networkIpAddress) {
        this.networkIpAddress = networkIpAddress;
    }

    public String getNetworkTcpPort() {
        return networkTcpPort;
    }

    public void setNetworkTcpPort(String networkTcpPort) {
        this.networkTcpPort = networkTcpPort;
    }

    public String getCountAccessRetries() {
        return countAccessRetries;
    }

    public void setCountAccessRetries(String countAccessRetries) {
        this.countAccessRetries = countAccessRetries;
    }

    public String getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(String responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public String getSslCirtificateFile() {
        return sslCirtificateFile;
    }

    public void setSslCirtificateFile(String sslCirtificateFile) {
        this.sslCirtificateFile = sslCirtificateFile;
    }

    @Override
    public String toString() {
        return "TerminalConnectionWifiModel{" +
                "priority='" + priority + '\'' +
                ", communicationType='" + communicationType + '\'' +
                ", networkIpAddress='" + networkIpAddress + '\'' +
                ", networkTcpPort='" + networkTcpPort + '\'' +
                ", countAccessRetries='" + countAccessRetries + '\'' +
                ", responseTimeout='" + responseTimeout + '\'' +
                ", sslCirtificateFile='" + sslCirtificateFile + '\'' +
                '}';
    }
}
