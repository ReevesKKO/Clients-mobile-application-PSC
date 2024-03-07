package com.example.pscapplication;

public class SecObject {
    private Integer id;
    private String name;
    private String address;
    private String description;
    private String security_mode;
    private Integer client_id;

    public SecObject(Integer id, String name, String address, String description, String security_mode, Integer client_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.description = description;
        this.security_mode = security_mode;
        this.client_id = client_id;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSecurityMode() {
        return this.security_mode;
    }

    public void setSecurityMode(String security_mode) {
        this.security_mode = security_mode;
    }

    public Integer getClientId() {
        return this.client_id;
    }

    public void setClientId(Integer client_id) {
        this.client_id = client_id;
    }
}
