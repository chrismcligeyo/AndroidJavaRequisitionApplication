package com.mcligeyo.rsmhub.model;

public class RequisitionData {
    private String name;
    private String description;
    private String date;
    private int user_id;
    private int status;
    //    private int requisition_code;
//    private int activation_id;
    private int id;

    public RequisitionData() {
    }


    public RequisitionData(int id, String name, String description, String date, int status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.status = status;

    }


    public RequisitionData(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }


    public RequisitionData(String name, String description, String date, int user_id, int id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.user_id = user_id;
        this.id = id;
    }

    public RequisitionData(String name, String description, String date, int user_id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.user_id = user_id;
//        this.status = status;
//        this.requisition_code = requisition_code;
//        this.activation_id = activation_id;
    }

    public RequisitionData(String name, String date, int id, int status) {
        this.name = name;
        this.date = date;
        this.id = id;
        this.status = status;

    }

    public RequisitionData(String name, String description, String date, int user_id, int status, int id) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.user_id = user_id;
        this.status = status;
        this.id = id;
    }

    public RequisitionData(int user_id) {
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}


