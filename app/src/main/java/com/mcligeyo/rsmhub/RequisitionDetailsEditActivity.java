package com.mcligeyo.rsmhub;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.BuildConfig;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcligeyo.rsmhub.adapters.RequisitionItemsExpensesAdapter;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionItem;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequisitionDetailsEditActivity extends AppCompatActivity {
    private MaterialButton editDialogButton, editCancelDialogButton;
    private RotateLoading rotateLoading1, rotateLoading;
    private AlertDialog dialog;
    private List<RequisitionItem> requisitionItems = new ArrayList<>();
    private RecyclerView recyclerViewRequisitionItems;
    private RequisitionItemsExpensesAdapter requisitionItemsExpensesAdapter;
    //
    private TextInputLayout customEditRequisitionItemName, customEditRequisitionItemDialogSupplier, customEditRequisitionItemDialogUnitCost, customEditRequisitionItemsDialogQauntity;
    private TextInputEditText customEditRequisitionItemNameText, customEditRequisitionItemDialogSupplierText, customEditRequisitionItemDialogUnitCostText, customEditRequisitionItemsDialogQauntityText;

    //

    //
    private String requisitionItemName, requisitionItemSupplier, requisitionItemUnitCost, requisitionItemUnitQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_details_edit);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        TextView reqDetailsEditItemprofile = findViewById(R.id.reqDetailsEditprofile);
        TextView requisitionDetailsEditItemNameText = findViewById(R.id.reqDetailsEditItemName);
        TextView reqDetailsEditItemSupplier = findViewById(R.id.requisition_details_edit_supplier);
        TextView reqDetailsEditItemUnitCost = findViewById(R.id.requisition_details_edit_unit_cost);
        TextView reqDetailsEditItemQuantity = findViewById(R.id.requisition_details_edit_quantity);
        TextView reqDetailsEditItemTotal = findViewById(R.id.requisiton_details_edit_total);
        //


        //
//        final MaterialButton editButton = findViewById(R.id.ReqDetailsEditEditButton);
//        MaterialButton deleteButton = findViewById(R.id.ReqDetailsEditDeleteButton);
//        rotateLoading1 = findViewById(R.id.ReqDetailsRotateLoader);

        recyclerViewRequisitionItems = findViewById(R.id.ReqDetailsRecyclerView);

        Bundle bundles = getIntent().getExtras();
        assert bundles != null;
        final String req_item_name = bundles.getString("reqItemName", null);
        Objects.requireNonNull(getSupportActionBar()).setTitle(req_item_name.toUpperCase());
        final String req_item_supplier = bundles.getString("reqItemSupplier", null);
        final int req_item_unit_cost = bundles.getInt("reqItemUnitCost", 0);
        final String req_item_quantity = bundles.getString("reqItemQuantity", null);
        int req_item_total = bundles.getInt("reqItemTotal", 0);
//        int req_item_req_id = bundles.getInt("requisition_id",0);
//        int req_item_id = bundles.getInt("id",0);

        String reqdetname = bundles.getString("name2", null);
        String reqdetdescription = bundles.getString("description2", null);
        String reqdetdate = bundles.getString("date2", null);
        int reqdetid = bundles.getInt("id2", 0);
        int reqdetstatus = bundles.getInt("status2", 0);


        requisitionDetailsEditItemNameText.setText(req_item_name.toUpperCase());
        reqDetailsEditItemSupplier.setText(req_item_supplier.toUpperCase());
        reqDetailsEditItemUnitCost.setText(String.valueOf(req_item_unit_cost));
        reqDetailsEditItemQuantity.setText(req_item_quantity);
        reqDetailsEditItemTotal.setText(String.valueOf(req_item_total));


        //get randomcolor to be se in profile textview
        int[] colorArrs = getResources().getIntArray(R.array.color_array_1);
        int ranDomColor = createRandomColor(colorArrs);

        String reqDataTitle = String.valueOf(req_item_name.toUpperCase().charAt(0));

        reqDetailsEditItemprofile.setBackgroundColor(ranDomColor);
        reqDetailsEditItemprofile.setText(reqDataTitle);

//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                final Dialog editDialog = new Dialog(RequisitionDetailsEditActivity.this);
////                editDialog.setTitle("EDIT REQUISITION ITEMS");
////                editDialog.setCancelable(false);
////                editDialog.setContentView(R.layout.custom_edit_requisition_items_dialog);
//                //use alert dialog, bet for perfomance. required not touse diaog class
//                final AlertDialog.Builder editDialog = new AlertDialog.Builder(RequisitionDetailsEditActivity.this);
//                View customEditXml = getLayoutInflater().inflate(R.layout.custom_edit_requisition_items_dialog, null); //avoid passin null as view group but did so coz, its a custom diaog,has no parent i guess
//                editDialog.setTitle("Edit Requisition Items");
//                editDialog.setCancelable(false);
//                editDialog.setView(customEditXml);
//
//
//              dialog = editDialog.create();
//                dialog.show();
//
//                customEditRequisitionItemName = dialog.findViewById(R.id.customEditRequisitionItemNameTextInputLayout);
//                customEditRequisitionItemDialogSupplier = dialog.findViewById(R.id.customEditRequisitionItemDialogSupplierTextInputLayout);
//                customEditRequisitionItemDialogUnitCost = dialog.findViewById(R.id.customEditRequisitionItemDialogUnitCostTextInputLayout);
//                customEditRequisitionItemsDialogQauntity = dialog.findViewById(R.id.customEditRequisitionItemsDialogQauntityTextInputLayout);
//
//                customEditRequisitionItemNameText = dialog.findViewById(R.id.customEditRequisitionItemNameTextInputEditText);
//                customEditRequisitionItemDialogSupplierText = dialog.findViewById(R.id.customEditRequisitionItemDialogSupplierTextInputEditText);
//                customEditRequisitionItemDialogUnitCostText = dialog.findViewById(R.id.customEditRequisitionItemDialogUnitCostTextInputEditText);
//                customEditRequisitionItemsDialogQauntityText = dialog.findViewById(R.id.customEditRequisitionItemsDialogQauntityTextInputEditText);
//               rotateLoading = dialog.findViewById(R.id.customEditRequisitionItemsDialogRotateLoader);
//
//               //method 1. use intent from prev activity(reqdetails) and set them in edittexts below
//                customEditRequisitionItemNameText.setText(req_item_name);
//                customEditRequisitionItemDialogSupplierText.setText(req_item_supplier);
//                customEditRequisitionItemDialogUnitCostText.setText(String.valueOf(req_item_unit_cost));
//                customEditRequisitionItemsDialogQauntityText.setText(req_item_quantity);
//               //method 2.get data  from database and put in edit texts, such that when you open dialog ,it displays. both method1 above and 2 work, i prefer 2
//              //getReqItemDataAndPlaceInEditDialog();
//
//
//                editDialogButton = dialog.findViewById(R.id.customEditRequisitionItemsDiaologAddButtonId);
//                editCancelDialogButton = dialog.findViewById(R.id.customEditRequisitionItemsCancelButtonId);
//
//
//
//
//                editCancelDialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//
//                //when you click on edit button you first fetch data from dbase and place in edit texts
//                editDialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        requisitionItemName = String.valueOf(customEditRequisitionItemNameText.getText());
//                        requisitionItemSupplier = String.valueOf(customEditRequisitionItemDialogSupplierText.getText());
//                        requisitionItemUnitCost = String.valueOf(customEditRequisitionItemDialogUnitCostText.getText());
//                        requisitionItemUnitQuantity = String.valueOf(customEditRequisitionItemsDialogQauntityText.getText());
//
//                        if (requisitionItemName.isEmpty()) {
//                            customEditRequisitionItemName.setEndIconDrawable(R.drawable.ic_error_red);
////                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
//                            customEditRequisitionItemNameText.setError("Name Required");
//                            customEditRequisitionItemNameText.requestFocus();
//                            return;
//                        }
//
//                        if (requisitionItemSupplier.isEmpty()) {
//                            customEditRequisitionItemDialogSupplier.setEndIconDrawable(R.drawable.ic_error_red);
////                    requisitionDescriptionTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
//                            customEditRequisitionItemDialogSupplierText.setError("Description Required");
//                            customEditRequisitionItemDialogSupplierText.requestFocus();
//                            return;
//                        }
//
//                        if (requisitionItemUnitCost.isEmpty()) {
//                            customEditRequisitionItemDialogUnitCost.setEndIconDrawable(R.drawable.ic_error_red);
//                            customEditRequisitionItemDialogUnitCostText.setError("Date Required"); //not required here. wont appera cause datepicker fills space
//                            customEditRequisitionItemDialogUnitCostText.requestFocus();
////                    datePickerTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
////                 datePickerTextInputEditText.setError("Date Required", ContextCompat.getDrawable(MakeRequisitionActivity.this, R.drawable.ic_error_red));// does not respond wellwith materialelements. use text input layout to seticondrawable. ContextCompat works better with native android elements
//                        return;
//                        }
//
//                        if (requisitionItemUnitQuantity.isEmpty()) {
//                            customEditRequisitionItemsDialogQauntity.setEndIconDrawable(R.drawable.ic_error_red);
////                    requisitionDescriptionTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
//                            customEditRequisitionItemsDialogQauntityText.setError("Description Required");
//                            customEditRequisitionItemsDialogQauntityText.requestFocus();
//                            //return; when i commit, android vsc says unnecessary to pace return here
//                            return;
//                        }
//
//                        //fetch data and place in edit text
//                        rotateLoading.start();
//                        editCustomDialogData();
////                        Intent intent = new Intent(RequisitionDetailsEditActivity.this, RequisitionDetailsActivity.class);
////                        startActivity(intent);
////                        finish();
//                    }
//                });
//
//                //editDialog.show();
//
//
//            }
//        });
        //delete button

//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // get token from shared pref saved in in login activity when you click login button
//                rotateLoading1.start();
//                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
//                final String token = sharedPreferences.getString("token", null);
//
////        Log.d("tokennn",token);
//
//
//                OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                        .addInterceptor(new Interceptor() {
//                            @Override
//                            public Response intercept(Chain chain) throws IOException {
//                                Request originalRequest = chain.request();
//
//                                // Add authorization header with updated authorization value to intercepted request
//
//                                Request.Builder requestBuilder = originalRequest.newBuilder()
//                                        .addHeader("Content-Type", "application/json")
//                                        .addHeader("Authorization", "Bearer " + token)
//                                        .method(originalRequest.method(), originalRequest.body());
//
//                                Request request = requestBuilder.build();
//                                return chain.proceed(request);
//
//                            }
//                        }).build();
//
//                AndroidNetworking.initialize(RequisitionDetailsEditActivity.this, okHttpClient);
//                AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
//
//                //store required fields in requisitionitem class in shared pref
//                //requisition_item goes in with requisitin_id
//
//
//                //will gte req item id, placed in put url at end
//                SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.REQUISITION_ITEM_PREF_NAME,0);
//                int deleted_req_item_id = sharedPreferences1.getInt("req_item_id", 0);
//                //will send fields as jsonobject
//                Log.d("deleted_req_idd", String.valueOf(deleted_req_item_id));
//
//
////
//
//
//                AndroidNetworking.delete(Constants.DELETE_REQUISITION_ITEM_URL + "/" + deleted_req_item_id)
//                        .addPathParameter("editReqItemId", String.valueOf(deleted_req_item_id))
//                        .setTag("deleted rRequest item")
//                        .setPriority(Priority.HIGH)
//                        .build()
//                        .getAsJSONObject(new JSONObjectRequestListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.d("responsesese", response.toString());
//
//                                String status = "";
//                                try {
//                                    status = response.getString("status");
//                                    if(status.equals("success")){
//                                        Toasty.success(RequisitionDetailsEditActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(RequisitionDetailsEditActivity.this, RequisitionDetailsActivity.class);
//                                        startActivity(intent);
//                                        finish();
//
//
//                                        rotateLoading1.stop();
//
//
//
//                                    }
//
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                    Toasty.error(RequisitionDetailsEditActivity.this, Objects.requireNonNull(e.getMessage()),Toasty.LENGTH_SHORT).show();
//                                }
//
////                if(){
////
////                }
//
//                            }
//
//                            @Override
//                            public void onError(ANError anError) {
//                                Toasty.error(RequisitionDetailsEditActivity.this, "Netwok Error: Retry",Toasty.LENGTH_SHORT).show();
//                            }
//                        });
//
//            }
//        });


    }

    private void getReqItemDataAndPlaceInEditDialog() {
        // get token from shared pref saved in in login activity when you click login button
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", null);

//        Log.d("tokennn",token);


        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        // Add authorization header with updated authorization value to intercepted request

                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "Bearer " + token)
                                .method(originalRequest.method(), originalRequest.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);

                    }
                }).build();

        AndroidNetworking.initialize(RequisitionDetailsEditActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //will gte req item id, placed in put url at end
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
        String edit_req_item_name = sharedPreferences1.getString("req_item_name", null);
        String edit_req_item_supplier = sharedPreferences1.getString("req_item_supplier", null);
        int edit_req_item_unit_cost = sharedPreferences1.getInt("req_item_unit_cost", 0);
        String edit_req_item_quantity = sharedPreferences1.getString("req_item_quantity", null);
        int edit_req_item_requisition_id = sharedPreferences1.getInt("requisition_id", 0);
        int edit_req_item_id = sharedPreferences1.getInt("req_item_id", 0);
        //will send fields as jsonobject
        Log.d("req_idd", String.valueOf(edit_req_item_id));


//


        AndroidNetworking.get(Constants.GET_REQUISITION_ITEM_URL2 + "/" + edit_req_item_id)
                .addPathParameter("editReqItemId", String.valueOf(edit_req_item_id))
                .setTag("Get rRequest item")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("responsesese", response.toString());

                        String status = "";
                        try {
                            status = response.getString("status");
                            if (status.equals("success")) {
                                Toasty.success(RequisitionDetailsEditActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                JSONObject jsonObject = new JSONObject(response.getString("data"));
                                String itemName = jsonObject.getString("itemname");
                                String itemSupplier = jsonObject.getString("supplier");
                                int itemUnitCost = jsonObject.getInt("unit_cost");
                                String itemQuantity = jsonObject.getString("quantity");

                                //you get data of req items from response in dbase and place in edit texts
                                RequisitionItem requisitionItem3 = new RequisitionItem(itemName, itemSupplier, itemUnitCost, itemQuantity);

                                // set response to edittexts
                                customEditRequisitionItemNameText.setText(requisitionItem3.getItemname());
                                customEditRequisitionItemDialogSupplierText.setText(requisitionItem3.getSupplier());
                                customEditRequisitionItemDialogUnitCostText.setText(String.valueOf(requisitionItem3.getUnit_cost()));
                                customEditRequisitionItemsDialogQauntityText.setText(requisitionItem3.getQuantity());
                                rotateLoading1.stop();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(RequisitionDetailsEditActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();
                        }

//                if(){
//
//                }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDetailsEditActivity.this, "Netwok Error: Retry", Toasty.LENGTH_SHORT).show();
                    }
                });
    }

    public void editCustomDialogData() {
        // get token from shared pref saved in in login activity when you click login button
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", null);

//        Log.d("tokennn",token);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        // Add authorization header with updated authorization value to intercepted request

                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "Bearer " + token)
                                .method(originalRequest.method(), originalRequest.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);

                    }
                }).build();

        AndroidNetworking.initialize(RequisitionDetailsEditActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }
        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //will gte req item id, placed in put url at end
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
        String edit_req_item_name = sharedPreferences1.getString("req_item_name", null);
        String edit_req_item_supplier = sharedPreferences1.getString("req_item_supplier", null);
        int edit_req_item_unit_cost = sharedPreferences1.getInt("req_item_unit_cost", 0);
        String edit_req_item_quantity = sharedPreferences1.getString("req_item_quantity", null);
        int edit_req_item_req_id = sharedPreferences1.getInt("requisition_id", 0);
        int edit_req_item_total = sharedPreferences1.getInt("req_item_total", 0);
        int edit_req_item_id = sharedPreferences1.getInt("req_item_id", 0);
        //will send fields as jsonobject
        Log.d("req_idsssdddd", String.valueOf(edit_req_item_id));

        final RequisitionItem requisitionItem = new RequisitionItem(requisitionItemName, requisitionItemSupplier, Integer.parseInt(requisitionItemUnitCost), requisitionItemUnitQuantity);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemname", requisitionItem.getItemname());
            jsonObject.put("supplier", requisitionItem.getSupplier());
            jsonObject.put("unit_cost", requisitionItem.getUnit_cost());
            jsonObject.put("quantity", requisitionItem.getQuantity());
            jsonObject.put("id", edit_req_item_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.put(Constants.PUT_REQUISITION_ITEM_URL2 + "/" + edit_req_item_id)
                .addPathParameter("req_item_id", String.valueOf(edit_req_item_id))
                .addJSONObjectBody(jsonObject)
                .setTag("Put edit RequisitionItem")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("responssssssedddd", response.toString());
                        String status = "";
                        try {
                            status = response.getString("status");

                            if (status.equals("success")) {

                                //get response, place
                                JSONObject jsonObject1 = new JSONObject(response.getString("data"));
                                int req_item_editedid = jsonObject1.getInt("id");
                                String req_item_editeditemname = jsonObject1.getString("itemname");
                                String req_item_editedsupplier = jsonObject1.getString("supplier");
                                String req_item_editedunit_cost = jsonObject1.getString("unit_cost");
                                String req_item_editedquantity = jsonObject1.getString("quantity");
                                int req_item_editedtotal = jsonObject1.getInt("total");
                                int req_item_editedrequisition_id = jsonObject1.getInt("requisition_id");

                                RequisitionItem requisitionItem1 = new RequisitionItem(req_item_editedid, req_item_editeditemname, req_item_editedsupplier, Integer.parseInt(req_item_editedunit_cost), req_item_editedquantity, req_item_editedrequisition_id, req_item_editedtotal);

                                SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.EDITED_REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putInt("req_item_editedid", requisitionItem1.getId());
                                editor.putString("req_item_editeditemname", requisitionItem1.getItemname());
                                editor.putString("req_item_editedsupplier", requisitionItem1.getSupplier());
                                editor.putInt("req_item_editedunit_cost", requisitionItem1.getUnit_cost());
                                editor.putString("req_item_editedquantity", requisitionItem1.getQuantity());
                                editor.putInt("req_item_editedtotal", requisitionItem1.getTotal());
                                editor.putInt("req_item_editedrequisition_id ", requisitionItem1.getRequisition_id());
                                editor.apply();


                                Toasty.success(RequisitionDetailsEditActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                rotateLoading.stop();
                                //getReqItems();
                                Intent intent = new Intent(RequisitionDetailsEditActivity.this, RequisitionDetailsActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toasty.error(RequisitionDetailsEditActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//                if(){
//
//                }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void getReqItems() {
        // get token from shared pref saved in in login activity when you click login button
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", null);

//        Log.d("tokennn",token);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        // Add authorization header with updated authorization value to intercepted request

                        Request.Builder requestBuilder = originalRequest.newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("Authorization", "Bearer " + token)
                                .method(originalRequest.method(), originalRequest.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);

                    }
                }).build();

        AndroidNetworking.initialize(RequisitionDetailsEditActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production
        if (BuildConfig.DEBUG) {
            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        }

        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //get req Id. because when we get request it goes with req item id
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
        String req_id = sharedPreferences1.getString("req_id", null);
        //will send fields as jsonobject
        assert req_id != null;
        Log.d("req_idd", req_id);


        AndroidNetworking.get(Constants.GET_REQUISITION_URL_2 + "/" + req_id)
                .addPathParameter("id", req_id)
                .setTag("GetReqItemss")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";


                        Log.d("RESPONSEREQITEMS::", response.toString());

//                        List<RequisitionItem> requisitionItems = null;
                        RequisitionItem requisitionItem = null;
                        try {
                            status = response.getString("status");

                            if (status.equals("success")) {
                                JSONArray jsonArray;
                                jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int req_item_id = jsonObject.getInt("id");
                                    String req_itemname = jsonObject.getString("itemname");
                                    String req_item_supplier = jsonObject.getString("supplier");
                                    int req_item_unit_cost = jsonObject.getInt("unit_cost");
                                    String req_item_quantity = jsonObject.getString("quantity");
                                    int req_item_total = jsonObject.getInt("total");
                                    int requisition_id = jsonObject.getInt("requisition_id");

                                    requisitionItem = new RequisitionItem(req_item_id, req_itemname, req_item_supplier, req_item_unit_cost, req_item_quantity, requisition_id, req_item_total);
                                    requisitionItems.add(requisitionItem);


                                }

                                SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.GET_REQUISITION_ITEMS_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putInt("req_item_id", requisitionItem.getId());
                                editor.putString("req_itemname", requisitionItem.getItemname());
                                editor.putString("req_item_supplier", requisitionItem.getSupplier());
                                editor.putInt("req_item_unit_cost", requisitionItem.getUnit_cost());
                                editor.putString("req_item_quantity", requisitionItem.getQuantity());
                                editor.putInt(" req_item_total", requisitionItem.getTotal());
                                editor.putInt("requisition_id", requisitionItem.getRequisition_id());
                                editor.apply();


                            } else {

                                Toasty.error(RequisitionDetailsEditActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(RequisitionDetailsEditActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();
                        }

                        //  you can initiate adapter here or at top, if you initiate at top, make sure you set it ere as below. reason i set it at top  is to taste adapter methods like load new data on start. if i initited adapter here,and called method loadNewData belonging to adapter, it will give me a null pinter exception on adapter, because adapter is initiated here and cant be acccessed from on start when you call it

//                        recyclerViewRequisitionItems.setLayoutManager(new LinearLayoutManager(RequisitionDetailsEditActivity.this));
//
                        requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsEditActivity.this, requisitionItems);
                        //recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);
//                        recyclerViewRequisitionItems.setHasFixedSize(true);
                        requisitionItemsExpensesAdapter.loadNewData(requisitionItems);


                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDetailsEditActivity.this, Objects.requireNonNull(anError.getMessage()), Toasty.LENGTH_SHORT).show();


                    }
                });


    }

    private int createRandomColor(int[] colorArrs) {

        int rand = new Random().nextInt(colorArrs.length);
        return colorArrs[rand];
    }


    //The up button in the action bar is treated as a menu item with ID android.R.id.home, as you can read in the docs. There you can find that you can handle clicks on it using this code:
    //the second activity, one with bundle, is the one that will have id of back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Respond to the action bar's Up/Home button
            Intent returnIntent1 = getIntent();
            returnIntent1.putExtra("returnDataaa", "From ReqDetsEditActi To ReqDetsAct");
            setResult(RESULT_OK, returnIntent1);

        }
        return super.onOptionsItemSelected(item);
    }


}
