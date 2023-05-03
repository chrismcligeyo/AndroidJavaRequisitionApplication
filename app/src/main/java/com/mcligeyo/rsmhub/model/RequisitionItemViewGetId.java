package com.mcligeyo.rsmhub.model;

public class RequisitionItemViewGetId {
    private int id;
    private String itemname;
    private String supplier;
    private int unit_cost;
    private String quantity;
    private int total;
    private int requisition_id;
    private RequisitionDataViewGet requisitionDataViewGet;

    public RequisitionItemViewGetId() {
    }

    public RequisitionItemViewGetId(String itemname, String supplier, int unit_cost, String quantity, int total, int requisition_id, RequisitionDataViewGet requisitionDataViewGet) {
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
        this.total = total;
        this.requisition_id = requisition_id;
        this.requisitionDataViewGet = requisitionDataViewGet;
    }

    public RequisitionItemViewGetId(int id, String itemname, String supplier, int unit_cost, String quantity, int total, int requisition_id, RequisitionDataViewGet requisitionDataViewGet) {
        this.id = id;
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
        this.total = total;
        this.requisition_id = requisition_id;
        this.requisitionDataViewGet = requisitionDataViewGet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public int getUnit_cost() {
        return unit_cost;
    }

    public void setUnit_cost(int unit_cost) {
        this.unit_cost = unit_cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getRequisition_id() {
        return requisition_id;
    }

    public void setRequisition_id(int requisition_id) {
        this.requisition_id = requisition_id;
    }

    public RequisitionDataViewGet getRequisitionDataViewGet() {
        return requisitionDataViewGet;
    }

    public void setRequisitionDataViewGet(RequisitionDataViewGet requisitionDataViewGet) {
        this.requisitionDataViewGet = requisitionDataViewGet;
    }
}
