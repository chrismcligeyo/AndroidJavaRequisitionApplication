package com.mcligeyo.rsmhub.model;

public class Datla {
    private String token;
    private int id;
    private int activated;

    public Datla() {
    }

    public Datla(String token, int id, int activated) {
        this.token = token;
        this.id = id;
        this.activated = activated;
    }

    public Datla(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActivated() {
        return activated;
    }

    public void setActivated(int activated) {
        this.activated = activated;
    }
}
