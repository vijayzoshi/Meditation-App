package com.kmit.musicapp.Model;

public class Home_order {
    String home_components_id;
    String home_components_name;
    String home_components_order;
    String home_components_status;

    public Home_order(String home_components_id, String home_components_name, String home_components_order, String home_components_status) {
        this.home_components_id = home_components_id;
        this.home_components_name = home_components_name;
        this.home_components_order = home_components_order;
        this.home_components_status = home_components_status;
    }

    public String getHome_components_id() {
        return home_components_id;
    }

    public void setHome_components_id(String home_components_id) {
        this.home_components_id = home_components_id;
    }

    public String getHome_components_name() {
        return home_components_name;
    }

    public void setHome_components_name(String home_components_name) {
        this.home_components_name = home_components_name;
    }

    public String getHome_components_order() {
        return home_components_order;
    }

    public void setHome_components_order(String home_components_order) {
        this.home_components_order = home_components_order;
    }

    public String getHome_components_status() {
        return home_components_status;
    }

    public void setHome_components_status(String home_components_status) {
        this.home_components_status = home_components_status;
    }
}
