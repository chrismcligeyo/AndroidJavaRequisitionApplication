package com.mcligeyo.rsmhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;
import java.util.Random;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RequisitionDetailsEditedItemActivity extends AppCompatActivity {

    private TextView reqDetailsEditedItemprofileText, reqDetailsEditedItemItemNameText, requisition_details_EditedItem_supplierText, requisition_details_EditedItem_unit_costText, requisition_details_EditedItem_quantityText, requisiton_details_EditedItem_totalText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_details_edited_item2);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        reqDetailsEditedItemprofileText = findViewById(R.id.reqDetailsEditedItemprofile);

        reqDetailsEditedItemItemNameText = findViewById(R.id.reqDetailsEditedItemItemName);
        requisition_details_EditedItem_supplierText = findViewById(R.id.requisition_details_EditedItem_supplier);
        requisition_details_EditedItem_unit_costText = findViewById(R.id.requisition_details_EditedItem_unit_cost);
        requisition_details_EditedItem_quantityText = findViewById(R.id.requisition_details_EditedItem_quantity);
        requisiton_details_EditedItem_totalText = findViewById(R.id.requisiton_details_EditedItem_total);

        getEditedDataFromDbaseDisplayINRDEditedIAct();

    }

    private void getEditedDataFromDbaseDisplayINRDEditedIAct() {
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

        AndroidNetworking.initialize(RequisitionDetailsEditedItemActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //will gte req item id, placed in put url at end
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.EDITED_REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
        final int edited_req_item_id = sharedPreferences1.getInt("req_item_editedid", 0);
        //will send fields as jsonobject
        Log.d("req_idd", String.valueOf(edited_req_item_id));


//


        AndroidNetworking.get(Constants.GET_REQUISITION_ITEM_URL2 + "/" + edited_req_item_id)
                .addPathParameter("editReqItemId", String.valueOf(edited_req_item_id))
                .setTag("Get Editedeq item")
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
                                Toasty.success(RequisitionDetailsEditedItemActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                //data is from shared pref
                                JSONObject jsonObject = new JSONObject(response.getString("data"));
                                int id = jsonObject.getInt("id");
                                String reqdetsEditedItemitemname = jsonObject.getString("itemname");
                                String reqdetsEditedItemisupplier = jsonObject.getString("supplier");
                                String reqdetsEditedItemunit_cost = jsonObject.getString("unit_cost");
                                String reqdetsEditedItemquantity = jsonObject.getString("quantity");
                                int reqdetsEditedItemtotal = jsonObject.getInt("total");
                                int reqdetsEditedItemrequisition_id = jsonObject.getInt("requisition_id");

                                //you get data of req items from response in dbase and place in edit texts
                                RequisitionItem requisitionItem3 = new RequisitionItem(reqdetsEditedItemitemname, reqdetsEditedItemisupplier, Integer.parseInt(reqdetsEditedItemunit_cost), reqdetsEditedItemquantity, reqdetsEditedItemtotal);

                                // set response to edittexts


                                reqDetailsEditedItemItemNameText.setText(requisitionItem3.getItemname().toUpperCase());
                                requisition_details_EditedItem_supplierText.setText(requisitionItem3.getSupplier().toUpperCase());
                                requisition_details_EditedItem_unit_costText.setText(String.valueOf(requisitionItem3.getUnit_cost()));
                                requisition_details_EditedItem_quantityText.setText(requisitionItem3.getQuantity());
                                int item_amount = (requisitionItem3.getUnit_cost()) * Integer.parseInt(requisitionItem3.getQuantity());
                                requisiton_details_EditedItem_totalText.setText(String.valueOf(item_amount));

                                //get randomcolor to be se in profile textview
                                int[] colorArrs = getResources().getIntArray(R.array.color_array_1);
                                int ranDomColor = createRandomColor(colorArrs);


                                String reqDataTitle = String.valueOf(requisitionItem3.getItemname().toUpperCase().charAt(0));

                                reqDetailsEditedItemprofileText.setBackgroundColor(ranDomColor);
                                reqDetailsEditedItemprofileText.setText(reqDataTitle);


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(RequisitionDetailsEditedItemActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();
                        }

//                if(){
//
//                }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDetailsEditedItemActivity.this, "Netwok Error: Retry", Toasty.LENGTH_SHORT).show();
                    }
                });
    }

    private int createRandomColor(int[] colorArrs) {
        int rand = new Random().nextInt(colorArrs.length);
        return colorArrs[rand];
    }
}