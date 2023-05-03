package com.mcligeyo.rsmhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mcligeyo.rsmhub.adapters.RequisitionItemsExpensesAdapter2;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class RequisitionDataEditActivity extends AppCompatActivity {
    private TextView reqDataEditProfileText, reqDataEditTitleText, dateReqDataEditSideText, ReqIdReqDataEditSideText, StatusReqDataEditSideText;
    private RecyclerView requisitionDataEditRecyclerView;
    private List<RequisitionItem> requisitionItems = new ArrayList<>();
    private RequisitionItemsExpensesAdapter2 requisitionItemsExpensesAdapter2;
    private RequisitionItem requisitionItem;

    //private ImageView reqItemDataEditImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_data_edit);

        reqDataEditProfileText = findViewById(R.id.reqDataEditProfileTextView);
        reqDataEditTitleText = findViewById(R.id.reqDataEditTitleTextView);
        dateReqDataEditSideText = findViewById(R.id.dateReqDataEditSideTextView);
        ReqIdReqDataEditSideText = findViewById(R.id.ReqIdReqDataEditSideTextView);
        StatusReqDataEditSideText = findViewById(R.id.StatusReqDataEditSideTextView);

        //reqItemDataEditImageView = findViewById(R.id.list_view_req_expenses_img_delete);

        requisitionDataEditRecyclerView = findViewById(R.id.ReqDataEditRecyclerView);
        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        String requisitionName = bundle.getString("requisitonName");
        String requisitionDate = bundle.getString("requisitionDate");
        int requisitionId = bundle.getInt("requisitionId");
        int requistionStatus = bundle.getInt("requisitionStatus");


        Objects.requireNonNull(getSupportActionBar()).setTitle(requisitionName.toUpperCase());


        int[] colorsArray = getResources().getIntArray(R.array.color_array_1);
        int ranDomColor = RequisitionDetailsActivity.createRandomColor(colorsArray);

        String singleLetter = String.valueOf(requisitionName.toUpperCase().charAt(0));
        reqDataEditProfileText.setText(singleLetter);
        reqDataEditProfileText.setBackgroundColor(ranDomColor);

        reqDataEditTitleText.setText(requisitionName.toUpperCase());
        dateReqDataEditSideText.setText(requisitionDate);
        ReqIdReqDataEditSideText.setText(String.valueOf(requisitionId));


        switch (requistionStatus) {
            case 1:  //1 is submitted
                StatusReqDataEditSideText.setText(R.string.req_data_edit_submitted);
                break;
            case 2:  //2 is approved
                StatusReqDataEditSideText.setText(R.string.req_data_edit_approved);
                break;
            case 3:  //3 is declined
                StatusReqDataEditSideText.setText(R.string.req_data_edit_declined);
                break;
        }


        // StatusReqDataEditSideText.setText(String.valueOf(requistionStatus));
        getReqItems();
        //getRequisitionsWithReqItems();
        //getReqWithReqItem();


    }


//    private int reqID;
//    private String reqName;
//    private String reqDescription;
//    private String reqDate;
//    private int reqStatus;
//
//    private void getReqWithReqItem(){
//
//
//        // get token from shared pref saved in in login activity when you click login button
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
//        final String token = sharedPreferences.getString("token", null);
//
////        Log.d("tokennn",token);
//
//
//        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request originalRequest = chain.request();
//
//                        // Add authorization header with updated authorization value to intercepted request
//
//                        Request.Builder requestBuilder = originalRequest.newBuilder()
//                                .addHeader("Content-Type", "application/json")
//                                .addHeader("Authorization", "Bearer " + token)
//                                .method(originalRequest.method(), originalRequest.body());
//
//                        Request request = requestBuilder.build();
//                        return chain.proceed(request);
//
//                    }
//                }).build();
//
//        AndroidNetworking.initialize(RequisitionDataEditActivity.this, okHttpClient);
//        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
//        //only shows loging details when in development and not in production
//
//        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
//
//
//        //store required fields in requisitionitem class in shared pref
//        //requisition_item goes in with requisitin_id
//
//
//
//
//
////
//
//
//        AndroidNetworking.get(Constants.GET_REQUISITION_ITEM_URL2)
//                .setTag("GetRWRITEM")
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        String status = null;
//                        try {
//                            status = response.getString("status");
//                            if(status.equals("success")){
//                                Log.d("SUCCESSS", "Successssss");
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                for(int i = 0; i<jsonArray.length(); i++){
//                                    JSONObject requisitionItemObject = jsonArray.getJSONObject(i);
//                                    int reqItemId =  requisitionItemObject.getInt("id");
//                                    String reqITemName = requisitionItemObject.getString("itemname");
//                                    String reqITemSupplier = requisitionItemObject.getString("supplier");
//                                    int reqItemUnicost =  requisitionItemObject.getInt("unit_cost");
//                                    String reqITemQuantity = requisitionItemObject.getString("quantity");
//                                    int reqItemTotal =  requisitionItemObject.getInt("total");
//                                    int reqItemReqId =  requisitionItemObject.getInt("requisition_id");
//
//                                    JSONObject requisition = requisitionItemObject.getJSONObject("requisition");
//                                    reqID = requisition.getInt("id");
//                                    reqName = requisition.getString("name");
//                                    reqDescription = requisition.getString("description");
//                                    reqDate = requisition.getString("date");
//                                    reqStatus = requisition.getInt("status");
//
//
//                                    RequisitionItem requisitionItem = new RequisitionItem(reqItemId,reqITemName,reqITemSupplier,reqItemUnicost,reqITemQuantity,reqItemTotal,reqItemReqId);
//
//                                    requisitionItems.add(requisitionItem);
//                                    requisitionDataEditRecyclerView.setLayoutManager(new LinearLayoutManager(RequisitionDataEditActivity.this));
//                                    requisitionItemsExpensesAdapter2 = new RequisitionItemsExpensesAdapter2(RequisitionDataEditActivity.this,requisitionItems);
//                                    requisitionDataEditRecyclerView.setAdapter(requisitionItemsExpensesAdapter2);
//                                    requisitionItemsExpensesAdapter2.notifyDataSetChanged(); //used requisitionItemsExpensesAdapter2 instead of requisitionItemsExpensesAdapter because  dont want the delete imageview in list view in requisitionItemsExpensesAdapter to show when item submitted
//
//
//                                    SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.REQITPREF_REQPREF,Context.MODE_PRIVATE);
//                                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                                    editor1.putInt("reqItemId",requisitionItem.getId());
//                                    editor1.putString("reqITemName",requisitionItem.getItemname());
//                                    editor1.putString("reqITemSupplier",requisitionItem.getSupplier());
//                                    editor1.putInt("reqItemUnicost",requisitionItem.getUnit_cost());
//                                    editor1.putString("reqITemQuantity",requisitionItem.getQuantity());
//                                    editor1.putInt("reqItemTotal",requisitionItem.getTotal());
//                                    editor1.putInt("reqItemReqId",requisitionItem.getRequisition_id());
//                                    editor1.apply();
//
//
//                                }
//
//
//
//
//
//
//
//
//
//                            } else {
//                                Toasty.error(RequisitionDataEditActivity.this, Objects.requireNonNull(response.getString("message")),Toasty.LENGTH_SHORT).show();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        } {
//
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Toasty.error(RequisitionDataEditActivity.this, "Network Err: Retry",Toasty.LENGTH_SHORT).show();
//                    }
//                });
//
//    }
//


    //gets all reqitems belonging to a particular requisiton
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

        AndroidNetworking.initialize(RequisitionDataEditActivity.this, okHttpClient);
        // AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //get req Id. because when we get request it goes with req item id
        // SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.GET_REQUISITION_ITEMS_PREF_NAME,MODE_PRIVATE);
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
        String req_id = sharedPreferences1.getString("req_id", null);
//        String req_id = sharedPreferences1.getString("requisition_id", null);
        //will send fields as jsonobject
        //      assert req_id != null;
//        Log.d("req_idd", req_id);


        AndroidNetworking.get(Constants.GET_REQUISITION_URL_2 + "/" + req_id)
                .setTag("GetReqItemss")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";


                        Log.d("RESPONSEREQITEMS::", response.toString());

//                       List<RequisitionItem> requisitionItems = null;
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

                                    RequisitionItem requisitionItem = new RequisitionItem(req_itemname, req_item_supplier, req_item_unit_cost, req_item_quantity, requisition_id, req_item_total, req_item_id);
                                    // requisitionItems = new ArrayList<>();
                                    //requisitionItems.clear();
                                    requisitionItems.add(requisitionItem);

                                    //requisitionItemList.addAll was added tob used n saved intant state. b4 we only had  requisitionItems only
                                    //requisitionItemList.addAll(requisitionItems);
                                    SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.GET_REQ_ITEMS_BELONGING_TO_REQUISITION, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences2.edit();
                                    editor.putString("get_req_itemname", requisitionItem.getItemname());
                                    editor.putString("get_req_item_supplier", requisitionItem.getSupplier());
                                    editor.putInt("get_req_item_unit_cost", requisitionItem.getUnit_cost());
                                    editor.putString("get_req_item_quantity", requisitionItem.getQuantity());
                                    editor.putInt("get_requisition_id", requisitionItem.getRequisition_id());
                                    editor.putInt("get_req_item_total", requisitionItem.getTotal());
                                    editor.putInt("get_req_item_id", requisitionItem.getId());
                                    editor.apply();


                                }


                            } else {

                                Toasty.error(RequisitionDataEditActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(RequisitionDataEditActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();

                        }


                        requisitionDataEditRecyclerView.setLayoutManager(new LinearLayoutManager(RequisitionDataEditActivity.this));
                        requisitionItemsExpensesAdapter2 = new RequisitionItemsExpensesAdapter2(RequisitionDataEditActivity.this, requisitionItems);
                        requisitionDataEditRecyclerView.setAdapter(requisitionItemsExpensesAdapter2);
                        requisitionItemsExpensesAdapter2.notifyDataSetChanged();
                        //requisitionItemsExpensesAdapter.loadNewData(requisitionItems);


                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDataEditActivity.this, "Network Err: Retry", Toasty.LENGTH_SHORT).show();


                    }
                });

    }


//    private void getRequisitionsWithReqItems() {
//        // get token from shared pref saved in in login activity when you click login button
//        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME,MODE_PRIVATE);
//        final String token = sharedPreferences.getString("token", null);
//
////        Log.d("tokennn",token);
//
//        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request originalRequest = chain.request();
//
//                        // Add authorization header with updated authorization value to intercepted request
//
//                        Request.Builder requestBuilder = originalRequest.newBuilder()
//                                .addHeader("Content-Type", "application/json")
//                                .addHeader("Authorization","Bearer " + token)
//                                .method(originalRequest.method(), originalRequest.body());
//
//                        Request request = requestBuilder.build();
//                        return chain.proceed(request);
//
//                    }
//                }).build();
//
//        AndroidNetworking.initialize(RequisitionDataEditActivity.this,okHttpClient);
//        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
//        //only shows loging details when in development and not in production
//
//            AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
//
//
//        //store required fields in requisitionitem class in shared pref
//        //requisition_item goes in with requisitin_id
//
//
//        //get req Id. because when we get request it goes with req item id
//        //SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.GET_REQUISITION_ITEMS_PREF_NAME,MODE_PRIVATE);
//        //SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.GET_REQ_ITEMS_BELONGING_TO_REQUISITION,MODE_PRIVATE);
//        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.REQITEMWITHREQ,MODE_PRIVATE);
//        int req_item_id = sharedPreferences1.getInt("reqitemID", 0);
////        String req_id = sharedPreferences1.getString("requisition_id", null);
//        //will send fields as jsonobject
//        Log.d("req_item_idd", String.valueOf(req_item_id));
//
//
//
//
//
//        AndroidNetworking.get(Constants.GET_REQUISITION_ITEM_WITH_REQUISITION_URL_2 + "/" + req_item_id)
//                .setTag("GetRequisitionWithReqItemss")
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        String status = "";
//
//                        try {
//                            status = response.getString("status");
//                            if(status.equals("success")){
//                                JSONObject jsonObject = response.getJSONObject("data");
//
//                                //get req item belonging to requisition
//                                int reqitemID = jsonObject.getInt("id");
//                                String itemNameReq = jsonObject.getString("itemname");
//                                String itemNamesupplier = jsonObject.getString("supplier");
//                                int reqitemunitCost = jsonObject.getInt("unit_cost");
//                                String itemNameQuantity = jsonObject.getString("quantity");
//                                int reqitemtotal = jsonObject.getInt("total");
//                                int reqitemrequisition_id = jsonObject.getInt("requisition_id");
//
//                                JSONObject requisition = response.getJSONObject("requisition");
//                                int req_ID = requisition.getInt("id");
//                                String NameReq = jsonObject.getString("name");
//                                String NameReqDescription = jsonObject.getString("description");
//                                String NameReqDate = jsonObject.getString("date");
//                                int req_Status = requisition.getInt("status");
//                                int req_User_ID = requisition.getInt("user_id");
//
//                                requisitionItem = new RequisitionItem(reqitemID,itemNameReq, itemNamesupplier,reqitemunitCost, itemNameQuantity,  reqitemtotal,reqitemrequisition_id);
//                                requisitionItems.add(requisitionItem);
//
//                                SharedPreferences preferences = getSharedPreferences(Constants.REQITEMWITHREQ,MODE_PRIVATE);
//                                SharedPreferences.Editor reqitemwithreq = preferences.edit();
//
//                                reqitemwithreq.putInt("reqitemID",requisitionItem.getId());
//                                reqitemwithreq.putString("itemNameReq",requisitionItem.getItemname());
//                                reqitemwithreq.putString("itemNamesupplier",requisitionItem.getSupplier());
//                                reqitemwithreq.putInt("reqitemunitCost",requisitionItem.getUnit_cost());
//                                reqitemwithreq.putString("itemNameQuantity",requisitionItem.getQuantity());
//                                reqitemwithreq.putInt("reqitemtotal",requisitionItem.getTotal());
//                                reqitemwithreq.putInt("reqitemrequisition_id",requisitionItem.getRequisition_id());
//
//
//
//                                reqitemwithreq.putInt("req_ID",req_ID);
//                                reqitemwithreq.putString("NameReq",NameReq);
//                                reqitemwithreq.putString("NameReqDescription",NameReqDescription);
//                                reqitemwithreq.putString("NameReqDate",NameReqDate);
//                                reqitemwithreq.putInt("req_Status",req_Status);
//                                reqitemwithreq.putInt("req_User_ID",req_User_ID);
//
//                                reqitemwithreq.apply();
//
//
//
//                            }
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        requisitionDataEditRecyclerView.setLayoutManager(new LinearLayoutManager(RequisitionDataEditActivity.this));
//                        requisitionItemsExpensesAdapter2 = new RequisitionItemsExpensesAdapter2(RequisitionDataEditActivity.this,requisitionItems);
//                        requisitionDataEditRecyclerView.setAdapter(requisitionItemsExpensesAdapter2);
//                        requisitionItemsExpensesAdapter2.notifyDataSetChanged(); //used requisitionItemsExpensesAdapter2 instead of requisitionItemsExpensesAdapter because  dont want the delete imageview in list view in requisitionItemsExpensesAdapter to show when item submitted
//
//                        //requisitionItemsExpensesAdapter.loadNewData(requisitionItems);
//
//
//
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
//
//
//
//
//
//
//
//
//    }


}
