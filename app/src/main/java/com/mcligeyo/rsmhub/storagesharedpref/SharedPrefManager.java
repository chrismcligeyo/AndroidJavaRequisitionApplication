package com.mcligeyo.rsmhub.storagesharedpref;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.Datla;
import com.mcligeyo.rsmhub.model.RequisitionData;

public class SharedPrefManager {
    private static SharedPrefManager sharedPrefManagerInstance;
    private Context context; //because shared preference requires context
    private Gson gson;
    private String json;

    //    private RequisitionData requisitionData;
    private SharedPrefManager(Context context) {
        this.context = context;
    }


    public static synchronized SharedPrefManager getInstance(Context context) {
        if (sharedPrefManagerInstance == null) {
            sharedPrefManagerInstance = new SharedPrefManager(context);
        }
        return sharedPrefManagerInstance;
    }


//save Data in shared prefer. Data Object in LoginResponse containing token, id, activated

    public void saveDatla(Datla datla) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("token", datla.getToken());
        sharedPreferencesEditor.putInt("id", datla.getId());
        sharedPreferencesEditor.putInt("activated", datla.getActivated());

        sharedPreferencesEditor.apply();
    }

    //get data from Datla object in resonse.

    public Datla getDatla() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return new Datla(
                sharedPreferences.getString("token", null),
                sharedPreferences.getInt("id", -1),
                sharedPreferences.getInt("activated", 0)
        );


    }

    public Datla getDatlaToken() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        Datla datlas = new Datla();
        datlas.setToken(sharedPreferences.getString("token", null));
        return datlas;
//        return new Datla(
//
//                sharedPreferences.getString("token", null)
//        );


    }

    public boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id", -1) != -1; //shortform for below if code

        //            if(sharedPreferences.getInt("id", -1) != -1){//if id stored not equal to -1. means if user exist(user logged in). sql does not store -ves
//
//                return true;
//            }
//            return false;


    }


    //store user Data inside shared pref
//
//    public void saveUserData(Data data){
//
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        gson = new Gson();
//        json = gson.toJson(data);
//
//        editor.putString("Data", json);
//        editor.putInt("id", data.getId());
//        editor.putString("token", data.getToken());
//        editor.putInt("activated", data.getActivated());
//
//
//
//        editor.apply();
//
//    }

    //check if user is logged in


//    public Data getUserData(){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME,Context.MODE_PRIVATE);
//        gson = new Gson();
//
////        MyObject obj = gson.fromJson(json, MyObject.class);
//        return new Data(
//                json = sharedPreferences.getString("Data", ""),
//                data =gson.fromJson(json, Data.class),
//                sharedPreferences.getInt("id", -1),
//                sharedPreferences.getString("token",null),
//                sharedPreferences.getInt("activated",0)
//
//        );
//
//    }


    //save requisition data in shared pref
//    public RequisitionResponse saveRequisitionData(RequisitionData requisitionData){
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CREATE_REQUISITION_PREF_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor sharedPrefEditior = sharedPreferences.edit();
//        gson = new Gson();
//        json = gson.toJson(requisitionData);
//
//        sharedPrefEditior.putString("Requisition Data", json);
//        sharedPrefEditior.putInt("id", requisitionData.getRequisition().getId());
//        sharedPrefEditior.putString("name", requisitionData.getRequisition().getName());
//        sharedPrefEditior.putString("description", requisitionData.getRequisition().getDescription());
//        sharedPrefEditior.putString("date", requisitionData.getRequisition().getDate());
//        sharedPrefEditior.putInt("user_id", requisitionData.getRequisition().getUserId());
//        sharedPrefEditior.putInt("status", requisitionData.getRequisition().getStatus());
//        sharedPrefEditior.putInt("requisition_code", requisitionData.getRequisition().getRequisitionCode());
//        sharedPrefEditior.putInt("activation_id", requisitionData.getRequisition().getActivationId());
//        sharedPrefEditior.apply();
//
//        return null;
//    }
//
//
//    //get shared pref requisition user_id value
//    public RequisitionData getRequisitionData(){
//        assert context != null;
//        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CREATE_REQUISITION_PREF_NAME,Context.MODE_PRIVATE);
//        gson = new Gson();
//
//        //if the function belongs to a class, the return type must be of that class
//        return new RequisitionData(
//                json = sharedPreferences.getString("Requisition Data", ""),
//                requisitionData =gson.fromJson(json, RequisitionData.class),
//                sharedPreferences.getInt("id",-1),
//                sharedPreferences.getString("name", "John"),
//                sharedPreferences.getString("description", "described"),
//                sharedPreferences.getString("date","2020-01-01"),
//                sharedPreferences.getInt("user_id",0),
//                sharedPreferences.getInt("status",0),
//                sharedPreferences.getInt("requisition_code",1000),
//                sharedPreferences.getInt("activation_id", 0)
//
//
//
//
//
//        );
//    }

    //logout. clear shared preff editor

    public void userLogout() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear()
                .apply();

    }

    //save requisitin data in shared pref
    public void saveRequisitionData(RequisitionData requisitionData) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CREATE_REQUISITION_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("name", requisitionData.getName());
        sharedPreferencesEditor.putString("description", requisitionData.getDescription());
        sharedPreferencesEditor.putString("date", requisitionData.getDate());
        sharedPreferencesEditor.putInt("user_id", requisitionData.getUser_id());

        sharedPreferencesEditor.apply();
    }

    //get requisition data
    public RequisitionData getRequisitionData() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CREATE_REQUISITION_PREF_NAME, Context.MODE_PRIVATE);
        return new RequisitionData(
                sharedPreferences.getString("name", null),
                sharedPreferences.getString("description", null),
                sharedPreferences.getString("date", null),
                sharedPreferences.getInt("user_id", 0)
        );
    }

    //get requisitiondata id
    public RequisitionData getRequiDataId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.CREATE_REQUISITION_PREF_NAME, Context.MODE_PRIVATE);
        return new RequisitionData(
                sharedPreferences.getInt("user_id", 0)
        );
    }


}

