package com.mcligeyo.rsmhub.model;

public class RequisitionDataViewGet {
    private int id;
    private String name;
    private String description;
    private String date;
    private int status;
    private int user_id;
    private int requisition_code;

    public RequisitionDataViewGet() {
    }

    public RequisitionDataViewGet(String name, String description, String date, int status, int user_id, int requisition_code) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;
        this.user_id = user_id;
        this.requisition_code = requisition_code;
    }

    public RequisitionDataViewGet(int id, String name, String description, String date, int status, int user_id, int requisition_code) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;
        this.user_id = user_id;
        this.requisition_code = requisition_code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRequisition_code() {
        return requisition_code;
    }

    public void setRequisition_code(int requisition_code) {
        this.requisition_code = requisition_code;
    }
}
