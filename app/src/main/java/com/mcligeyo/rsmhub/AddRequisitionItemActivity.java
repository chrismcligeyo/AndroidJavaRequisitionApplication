package com.mcligeyo.rsmhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionItem;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddRequisitionItemActivity extends AppCompatActivity {
    private TextInputLayout addRequisitionItemNameTextInputLayout1, addRequisitionItemSupplierTextInputLayout1, addRequisitionItemUnitCostTextInputLayout1, addRequisitionItemQuantityTextInputLayout1;
    private TextInputEditText addRequisitionItemNameTextInputEditText1, addRequisitionItemNameSupplierTextInputEditText1, addRequisitionItemUnitCostTextInputEditText1, addRequisitionItemQuantityTextInputEditText1;
    private MaterialButton makeReqItemButton1;
    private RotateLoading rotateLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_requisition_item);
        //getSupportActionBar().setTitle("Make Requisition");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Objects.requireNonNull(getSupportActionBar()).setTitle("Requisition Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addRequisitionItemNameTextInputLayout1 = findViewById(R.id.addRequisitionItemNameTextInputLayout);
        addRequisitionItemSupplierTextInputLayout1 = findViewById(R.id.addRequisitionItemSupplierTextInputLayout);
        addRequisitionItemUnitCostTextInputLayout1 = findViewById(R.id.addRequisitionItemUnitCostTextInputLayout);
        addRequisitionItemQuantityTextInputLayout1 = findViewById(R.id.addRequisitionItemQuantityTextInputLayout);
        addRequisitionItemNameTextInputEditText1 = findViewById(R.id.addRequisitionItemNameTextInputEditText);
        addRequisitionItemNameSupplierTextInputEditText1 = findViewById(R.id.addRequisitionItemNameSupplierTextInputEditText);
        addRequisitionItemUnitCostTextInputEditText1 = findViewById(R.id.addRequisitionItemUnitCostTextInputEditText);
        addRequisitionItemQuantityTextInputEditText1 = findViewById(R.id.addRequisitionItemQuantityTextInputEditText);
        rotateLoading = (RotateLoading) findViewById(R.id.rotateLoadingProgessDialogAddReqItem);
        makeReqItemButton1 = findViewById(R.id.makeReqItemButton);


        makeReqItemButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeRequisition();
            }
        });

    }

    private void makeRequisition() {
        rotateLoading.start();
        String addRequisitionItemName = String.valueOf(addRequisitionItemNameTextInputEditText1.getText());
        String addRequisitionItemNameSupplier = String.valueOf(addRequisitionItemNameSupplierTextInputEditText1.getText());
        String addRequisitionItemUnitCost = String.valueOf(addRequisitionItemUnitCostTextInputEditText1.getText());
        String addRequisitionItemQuantity = String.valueOf(addRequisitionItemQuantityTextInputEditText1.getText());

        if (addRequisitionItemName.isEmpty()) {
            addRequisitionItemNameTextInputLayout1.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
            addRequisitionItemNameTextInputEditText1.setError("Name Required");
            addRequisitionItemNameTextInputEditText1.requestFocus();
            return;
        }

        if (addRequisitionItemNameSupplier.isEmpty()) {
            addRequisitionItemSupplierTextInputLayout1.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
            addRequisitionItemNameSupplierTextInputEditText1.setError("Supplier Required");
            addRequisitionItemNameSupplierTextInputEditText1.requestFocus();
            return;
        }

        if (addRequisitionItemUnitCost.isEmpty()) {
            addRequisitionItemUnitCostTextInputLayout1.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
            addRequisitionItemUnitCostTextInputEditText1.setError("Cost Required");
            addRequisitionItemUnitCostTextInputEditText1.requestFocus();
            return;
        }

        if (addRequisitionItemQuantity.isEmpty()) {
            addRequisitionItemQuantityTextInputLayout1.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
            addRequisitionItemQuantityTextInputEditText1.setError("Quantity Required");
            addRequisitionItemQuantityTextInputEditText1.requestFocus();
            return;
        }

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

        AndroidNetworking.initialize(AddRequisitionItemActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //get reqId. because when we addreq it goes with the reqid
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
        String req_id = sharedPreferences1.getString("req_id", null);
        //will send fields as jsonobject
        Log.d("req_idd", req_id);

        RequisitionItem requisitionItem = new RequisitionItem(addRequisitionItemName, addRequisitionItemNameSupplier, Integer.parseInt(addRequisitionItemUnitCost), addRequisitionItemQuantity, Integer.parseInt(req_id));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("itemname", requisitionItem.getItemname());
            jsonObject.put("supplier", requisitionItem.getSupplier());
            jsonObject.put("unit_cost", requisitionItem.getUnit_cost());
            jsonObject.put("quantity", requisitionItem.getQuantity());
            jsonObject.put("requisition_id", requisitionItem.getRequisition_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(Constants.MAKE_REQUISITION_ITEM_URL2)
                .addJSONObjectBody(jsonObject)
//                .addBodyParameter(token) problem is here Expected BEGIN_OBJECT but was STRING at line 1 column 2 path $
//                .addBodyParameter("addRequisitionItemName", addRequisitionItemName)
//                .addBodyParameter("addRequisitionItemNameSupplier", addRequisitionItemNameSupplier)
//                .addBodyParameter("addRequisitionItemUnitCost", addRequisitionItemUnitCost)
//                .addBodyParameter("addRequisitionItemQuantity", addRequisitionItemQuantity)
                .setTag("Make Requisition Item")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";

                        Log.d("RESPONSE::", response.toString());
                        try {

                            status = response.getString("status");
                            if (status.equals("success")) {
                                rotateLoading.stop();

                                //get requisition item values from response
                                JSONObject jsonObject = null;
                                jsonObject = new JSONObject(response.getString("data"));

                                String requisitionItemName = jsonObject.getString("itemname");
                                String requisitionItemSupplier = jsonObject.getString("supplier");
                                String requisitionItemUnitCost = jsonObject.getString("unit_cost");
                                String requisitionItemQuantity = jsonObject.getString("quantity");
                                String requisitionId = jsonObject.getString("requisition_id");
                                int reqItemTotal = jsonObject.getInt("total");
                                int requisitionItemId = jsonObject.getInt("id");

                                //store in model, then sav indiviual model items inshared pref,if you want to access values letter
                                RequisitionItem requisitionItem1 = new RequisitionItem(requisitionItemName, requisitionItemSupplier, Integer.parseInt(requisitionItemUnitCost), requisitionItemQuantity, Integer.parseInt(requisitionId), reqItemTotal, requisitionItemId);

                                SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putString("req_item_name", requisitionItem1.getItemname());
                                editor.putString("req_item_supplier", requisitionItem1.getSupplier());
                                editor.putInt("req_item_unit_cost", requisitionItem1.getUnit_cost());
                                editor.putString("req_item_quantity", requisitionItem1.getQuantity());
                                editor.putInt("requisition_id", requisitionItem1.getRequisition_id());
                                editor.putInt("req_item_total", requisitionItem1.getTotal());
                                editor.putInt("req_item_id", requisitionItem1.getId());

                                editor.apply();


                                Toasty.success(AddRequisitionItemActivity.this, response.getString("message"), Toasty.LENGTH_LONG).show();
                                Intent intent = new Intent(AddRequisitionItemActivity.this, RequisitionExpensesActivity.class);
                                startActivity(intent);


                            }
                        } catch (JSONException e) {
                            Toasty.error(AddRequisitionItemActivity.this, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED. " + e.getMessage(), Toasty.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(AddRequisitionItemActivity.this, "UNSUCCESSFUL " + anError.getMessage(), Toasty.LENGTH_LONG).show();

                    }
                });

    }


}
