package com.tarang.dpq2.model;

public class TerminalConnectionGPRSModel {
    private String priority;
    private String communicationType;
    private String gprsDailNumber;
    private String gprsAccessPointName;
    private String connectTimeForGprsPhone;
    private String networkIpAddress;
    private String networkTcpPort;
    private String dailAttemptsToNetwork;
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

    public String getGprsDailNumber() {
        return gprsDailNumber;
    }

    public void setGprsDailNumber(String gprsDailNumber) {
        this.gprsDailNumber = gprsDailNumber;
    }

    public String getGprsAccessPointName() {
        return gprsAccessPointName;
    }

    public void setGprsAccessPointName(String gprsAccessPointName) {
        this.gprsAccessPointName = gprsAccessPointName;
    }

    public String getConnectTimeForGprsPhone() {
        return connectTimeForGprsPhone;
    }

    public void setConnectTimeForGprsPhone(String connectTimeForGprsPhone) {
        this.connectTimeForGprsPhone = connectTimeForGprsPhone;
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

    public String getDailAttemptsToNetwork() {
        return dailAttemptsToNetwork;
    }

    public void setDailAttemptsToNetwork(String dailAttemptsToNetwork) {
        this.dailAttemptsToNetwork = dailAttemptsToNetwork;
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
        return "TerminalConnectionGPRSModel{" +
                "priority='" + priority + '\'' +
                ", communicationType='" + communicationType + '\'' +
                ", gprsDailNumber='" + gprsDailNumber + '\'' +
                ", gprsAccessPointName='" + gprsAccessPointName + '\'' +
                ", connectTimeForGprsPhone='" + connectTimeForGprsPhone + '\'' +
                ", networkIpAddress='" + networkIpAddress + '\'' +
                ", networkTcpPort='" + networkTcpPort + '\'' +
                ", dailAttemptsToNetwork='" + dailAttemptsToNetwork + '\'' +
                ", responseTimeout='" + responseTimeout + '\'' +
                ", sslCirtificateFile='" + sslCirtificateFile + '\'' +
                '}';
    }
}
