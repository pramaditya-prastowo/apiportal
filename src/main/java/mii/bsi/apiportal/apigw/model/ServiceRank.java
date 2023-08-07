package mii.bsi.apiportal.apigw.model;

import lombok.Data;

public class ServiceRank {
    private String service;
    private String jumlah;

    public String getService(){
        return this.service.split(":")[1];
    }
    public int getJumlah(){
        return Integer.parseInt(this.jumlah);
    }
}
