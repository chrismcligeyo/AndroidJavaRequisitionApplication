package com.mcligeyo.rsmhub.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RequisitionItem implements Parcelable {
    private String itemname;
    private String supplier;
    private int unit_cost;
    private String quantity;
    private int requisition_id;
    private int total;
    private int id;


    public RequisitionItem() {
    }

    public RequisitionItem(int id) {
        this.id = id;
    }


    public RequisitionItem(String itemname, int total) {
        this.itemname = itemname;
        this.total = total;
    }

    public RequisitionItem(String itemname, String supplier, int unit_cost, String quantity) {
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
    }

    public RequisitionItem(String itemname, String supplier, int unit_cost, String quantity, int requisition_id) {
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
        this.requisition_id = requisition_id;

    }

    public RequisitionItem(String itemname, String supplier, int unit_cost, String quantity, int requisition_id, int total) {
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
        this.requisition_id = requisition_id;
        this.total = total;
    }

    public RequisitionItem(int id, String itemname, String supplier, int unit_cost, String quantity, int requisition_id, int total) {
        this.id = id;
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
        this.requisition_id = requisition_id;
        this.total = total;

    }

    public RequisitionItem(String itemname, String supplier, int unit_cost, String quantity, int requisition_id, int total, int id) {
        this.id = id;
        this.itemname = itemname;
        this.supplier = supplier;
        this.unit_cost = unit_cost;
        this.quantity = quantity;
        this.requisition_id = requisition_id;
        this.total = total;

    }


    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public RequisitionItem createFromParcel(Parcel in) {
            return new RequisitionItem(in);
        }

        public RequisitionItem[] newArray(int size) {
            return new RequisitionItem[size];
        }
    };


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

    // Parcelling part
    public RequisitionItem(Parcel in) {
        this.id = in.readInt();
        this.itemname = in.readString();
        this.supplier = in.readString();
        this.unit_cost = in.readInt();
        this.quantity = in.readString();
        this.requisition_id = in.readInt();
        this.total = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.itemname);
        dest.writeString(this.supplier);
        dest.writeInt(this.unit_cost);
        dest.writeString(this.quantity);
        dest.writeInt(this.requisition_id);
        dest.writeInt(this.total);


    }
}
