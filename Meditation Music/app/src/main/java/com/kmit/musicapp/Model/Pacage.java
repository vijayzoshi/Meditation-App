package com.kmit.musicapp.Model;

public class Pacage {
    String package_id;
    String package_name;
    String package_duration;
    String package_price;
    String total_package_price;

    public String getPackage_id() {
        return package_id;
    }

    public void setPackage_id(String package_id) {
        this.package_id = package_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getPackage_duration() {
        return package_duration;
    }

    public void setPackage_duration(String package_duration) {
        this.package_duration = package_duration;
    }

    public String getPackage_price() {
        return package_price;
    }

    public void setPackage_price(String package_price) {
        this.package_price = package_price;
    }

    public String getTotal_package_price() {
        return total_package_price;
    }

    public void setTotal_package_price(String total_package_price) {
        this.total_package_price = total_package_price;
    }

    public Pacage(String package_id, String package_name, String package_duration, String package_price, String total_package_price) {
        this.package_id = package_id;
        this.package_name = package_name;
        this.package_duration = package_duration;
        this.package_price = package_price;
        this.total_package_price = total_package_price;


    }
}

