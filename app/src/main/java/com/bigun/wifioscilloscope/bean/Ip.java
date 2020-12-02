package com.bigun.wifioscilloscope.bean;

public class Ip {
    public String ip;
    public String name;

    public Ip (String ip) {
        this.ip = ip;
    }
    public Ip (String ip, String name) {
        this.ip = ip;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
