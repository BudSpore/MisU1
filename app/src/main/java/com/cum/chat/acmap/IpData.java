package com.cum.chat.acmap;

/**
 * Created by 2-1Ping on 2017/2/10.
 */

public class IpData {
    private String country;
    private String country_id;
    private String area;
    private String area_id;
    private String region;
    private String region_id;
    private String city;
    private String city_id;
    private String county;
    private String county_id;
    private String isp;
    private String isp_id;
    private String ip;
    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return this.country;
    }
    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
