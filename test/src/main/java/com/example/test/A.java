package com.example.test;

/**
 * Created by wumin on 2018/8/18.
 */
public class A {

    private  String port;

    private  String url;

    private  String name;

    @Override
    public String toString() {
        return "A{" +
                "port='" + port + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
