package com.mcligeyo.rsmhub;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcligeyo.rsmhub.adapters.RequisitionItemsExpensesAdapter;
import com.mcligeyo.rsmhub.adapters.RequisitionItemsExpensesAdapter2;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionData;
import com.mcligeyo.rsmhub.model.RequisitionDataViewGet;
import com.mcligeyo.rsmhub.model.RequisitionItem;
import com.mcligeyo.rsmhub.model.RequisitionItemViewGetId;
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

public class RequisitionDetailsActivity extends AppCompatActivity {
    private MaterialButton addItemButton;
    private MaterialButton submitItemButton;
    private TextView reqDetsProfileTextView, reqDetsTitleTextView, dateReqDetsSideTextView, ReqIdReqDetsSideTextView, statusReqDateSideTextView;
    private RecyclerView recyclerViewRequisitionItems;
    private List<RequisitionItem> requisitionItems = new ArrayList<>();
    private ArrayList<RequisitionItem> requisitionItemList = new ArrayList<>();
    private RequisitionItemsExpensesAdapter requisitionItemsExpensesAdapter;
    private List<RequisitionItemViewGetId> requisitionItemViewGetIds;
    private RequisitionItemsExpensesAdapter2 requisitionItemsExpensesAdapter2;
    private LinearLayout linearLayoutWithButtons;
    private RotateLoading rotateLoading;


    private Parcelable savedRecyclerLayoutState; //used to mantain scroll position in recyclerview wen phone turned
    private Parcelable mListState;
    private RecyclerView.LayoutManager mlayoutManager;

    //dialog edittext and textinpt layouts
    private TextInputLayout customAddRequisitionItemName, customAddRequisitionItemDialogSupplier, customAddRequisitionItemDialogUnitCost, customAddRequisitionItemsDialogQauntity;
    private TextInputEditText customAddRequisitionItemNameText, customAddRequisitionItemDialogSupplierText, customAddRequisitionItemDialogUnitCostText, customAddRequisitionItemsDialogQauntityText;
    private MaterialButton customAddReqItemsDialogAddItemButton;
    private MaterialButton customAddReqItemsDialogCancelButton;

    //dialog
    private Dialog addReqItemsDialog;

    private String customAddRequisitionItemNameC;
    private String customAddRequisitionItemDialogSupplierC;
    private String customAddRequisitionItemDialogUnitCostC;
    private String customAddRequisitionItemsDialogQauntityC;

    private String sname;
    private String sdescription;
    private String sdate;
    private String sid;
    private String sstatus;


    private RotateLoading customAddRequisitionItemsDialogRotateDiaolog;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_details);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Requisition Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addItemButton = findViewById(R.id.ReqDetailsAddItemButton);
        reqDetsProfileTextView = findViewById(R.id.reqDetsProfileTextView);
        reqDetsTitleTextView = findViewById(R.id.reqDetsTitleTextView);
        dateReqDetsSideTextView = findViewById(R.id.dateReqSideTextView);
        ReqIdReqDetsSideTextView = findViewById(R.id.ReqIdReqDetailsSideTextView);
        statusReqDateSideTextView = findViewById(R.id.StatusReqDetailsSideTextView);
        linearLayoutWithButtons = findViewById(R.id.activity_requisition_details_linear_layout_with_buttons);
        recyclerViewRequisitionItems = findViewById(R.id.ReqDetailsRecyclerView);
        submitItemButton = findViewById(R.id.ReqDetailsSubmitButton);
        rotateLoading = findViewById(R.id.ReqDetailsRotateLoaderLoader);


        //get intentitems in MAkeRequisition activity dispay i text view in card.

//        Bundle extras = getIntent().getExtras();
//        assert extras != null;
//        String title = extras.getString("name");
//        String date = extras.getString("date");
//        String req_id = extras.getString("req_id");
//        String status = extras.getString("status");

        //use shared pref to display req details from make req instead of bundles, with bundles when in next activity and you come back to this activity, it displays, null bundle error

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
        String title = sharedPreferences.getString("req_name", null);
        String date = sharedPreferences.getString("req_date", null);
        String req_id = sharedPreferences.getString("req_id", null);
        String status = sharedPreferences.getString("req_status", null);

        assert title != null;
        reqDetsTitleTextView.setText(title.toUpperCase());
        dateReqDetsSideTextView.setText(date);
        ReqIdReqDetsSideTextView.setText(req_id);


        //get randomcolor to be se in profile textview
        int[] colorArrs = getResources().getIntArray(R.array.color_array_1);
        int ranDomColor = createRandomColor(colorArrs);


        String reqDataTitle = String.valueOf(title.toUpperCase().charAt(0));

        reqDetsProfileTextView.setBackgroundColor(ranDomColor);
        reqDetsProfileTextView.setText(reqDataTitle);

        assert status != null;
        if (status.equals("0")) {
            statusReqDateSideTextView.setText(getResources().getString(R.string.not_submitted));
        }


        //recyclerViewRequisitionItems.setHasFixedSize(true);
        // recyclerViewRequisitionItems.setLayoutManager(new LinearLayoutManager(RequisitionDetailsActivity.this));
        //recyclerViewRequisitionItems.setItemAnimator(new DefaultItemAnimator());

//       requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsActivity.this,requisitionItems);


        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open cstom add reqisition items dialog
                addReqItemsDialog = new Dialog(RequisitionDetailsActivity.this);
                addReqItemsDialog.setCancelable(false);
                addReqItemsDialog.setTitle(getResources().getString(R.string.add_requisition_item));
                addReqItemsDialog.setContentView(R.layout.custom_add_requisition_items_dialog);

                //dialog text input layout
                customAddRequisitionItemName = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemNameTextInputLayout);
                customAddRequisitionItemDialogSupplier = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemDialogSupplierTextInputLayout);
                customAddRequisitionItemDialogUnitCost = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemDialogUnitCostTextInputLayout);
                customAddRequisitionItemsDialogQauntity = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemsDialogQauntityTextInputLayout);

                //dialog edit texts
                customAddRequisitionItemNameText = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemNameTextInputEditText);
                customAddRequisitionItemDialogSupplierText = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemDialogSupplierTextInputEditText);
                customAddRequisitionItemDialogUnitCostText = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemDialogUnitCostTextInputEditText);
                customAddRequisitionItemsDialogQauntityText = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemsDialogQauntityTextInputEditText);

                //dailog buttons
                customAddReqItemsDialogAddItemButton = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemsDiaologAddButtonId);
                customAddReqItemsDialogCancelButton = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemsCancelButtonId);
                customAddRequisitionItemsDialogRotateDiaolog = addReqItemsDialog.findViewById(R.id.customAddRequisitionItemsDialogRotateLoader);


                customAddReqItemsDialogCancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addReqItemsDialog.dismiss();
                    }
                });

                customAddReqItemsDialogAddItemButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customAddRequisitionItemsDialogRotateDiaolog.start();
                        customAddRequisitionItemNameC = String.valueOf(customAddRequisitionItemNameText.getText());
                        customAddRequisitionItemDialogSupplierC = String.valueOf(customAddRequisitionItemDialogSupplierText.getText());
                        customAddRequisitionItemDialogUnitCostC = String.valueOf(customAddRequisitionItemDialogUnitCostText.getText());
                        customAddRequisitionItemsDialogQauntityC = String.valueOf(customAddRequisitionItemsDialogQauntityText.getText());

                        if (TextUtils.isEmpty(customAddRequisitionItemNameC)) {
                            customAddRequisitionItemName.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
                            customAddRequisitionItemNameText.setError("Name Required");
                            customAddRequisitionItemNameText.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(customAddRequisitionItemDialogSupplierC)) {
                            customAddRequisitionItemDialogSupplier.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
                            customAddRequisitionItemDialogSupplierText.setError("Supplier Required");
                            customAddRequisitionItemDialogSupplierText.requestFocus();
                            return;
                        }

                        if (TextUtils.isEmpty(customAddRequisitionItemDialogUnitCostC)) {
                            customAddRequisitionItemDialogUnitCost.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
                            customAddRequisitionItemDialogUnitCostText.setError("Cost Required");
                            customAddRequisitionItemDialogUnitCostText.requestFocus();
                        }

                        if (TextUtils.isEmpty(customAddRequisitionItemsDialogQauntityC)) {
                            customAddRequisitionItemsDialogQauntity.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
                            customAddRequisitionItemsDialogQauntityText.setError("Quantity Required");
                            customAddRequisitionItemsDialogQauntityText.requestFocus();
                        }

                        postToReqDetailsRecyclerView();


                    }
                });

                addReqItemsDialog.show();

            }
            //may place get here
        });

//        if (savedInstanceState != null) {
//            requisitionItemList = savedInstanceState.getParcelableArrayList(Constants.LIST_STATE);
//            savedRecyclerLayoutState = savedInstanceState.getParcelable(Constants.BUNDLE_RECYCLER_LAYOUT);
//            requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsActivity.this,requisitionItemList);
//            recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);
//            restoreLayoutManagerPosition();
//            requisitionItemsExpensesAdapter.notifyDataSetChanged();
//
//        }

        //submit bttom comes here

        //getReqItems();

        submitItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rotateLoading.start();
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

                AndroidNetworking.initialize(RequisitionDetailsActivity.this, okHttpClient);
                //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
                //only shows loging details when in development and not in production

                AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


                //store required fields in requisitionitem class in shared pref
                //requisition_item goes in with requisitin_id


                //will gte req item id, placed in put url at end
                SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
                String submit_req_item_name = sharedPreferences1.getString("req_name", null);
                String submit_req_description = sharedPreferences1.getString("req_description", null);
                String submit_req_date = sharedPreferences1.getString("req_date", null);
                String submit_req_user_id = sharedPreferences1.getString("req_user_id", null);
                String submit_req_status = sharedPreferences1.getString("req_status", null);
                String submit_req_id = sharedPreferences1.getString("req_id", null);

                //will send fields as jsonobject
                assert submit_req_id != null;
                Log.d("req_idsssdddd", submit_req_id);

                AndroidNetworking.post(Constants.SUBMIT_REQUISITION_ITEM_URL2 + "/" + submit_req_id)
                        .addPathParameter("submit_req_id", submit_req_id)
                        .setTag("Submit Requisition")
                        .setPriority(Priority.HIGH)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("submittresponseee", response.toString());
                                String status = "";
                                try {
                                    status = response.getString("status");

                                    if (status.equals("success")) {
                                        rotateLoading.stop();

                                        Toasty.success(RequisitionDetailsActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

//                                        linearLayoutWithButtons.setVisibility(View.GONE);

                                        //get response, place
                                        JSONObject jsonObject1 = new JSONObject(response.getString("data"));
                                        int submited_req_id = jsonObject1.getInt("id");
                                        String submited_req_name = jsonObject1.getString("name");
                                        String submited_req_description = jsonObject1.getString("description");
                                        String submited_req_date = jsonObject1.getString("date");
                                        int submited_req_status = jsonObject1.getInt("status");
                                        int submited_req_user_id = jsonObject1.getInt("user_id");

                                        RequisitionData requisitionData = new RequisitionData(submited_req_name, submited_req_description, submited_req_date, submited_req_user_id, submited_req_status, submited_req_id);
                                        SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.SUBMITED_REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences2.edit();
                                        editor.putString("req_submitted_name", requisitionData.getName());
                                        editor.putString("req_submitted_description", requisitionData.getDescription());
                                        editor.putString("req_submitted_date", requisitionData.getDate());
                                        editor.putInt("req_submitted_user_id", requisitionData.getUser_id());
                                        editor.putInt("req_submitted_status", requisitionData.getStatus());
                                        editor.putInt("req_submitted_req_id", requisitionData.getId());

                                        editor.apply();

                                        //Toasty.success(RequisitionDetailsActivity.this,response.getString("message"),Toasty.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RequisitionDetailsActivity.this, RequisitionExpensesActivity.class);
                                        startActivity(intent);


                                    } else {
                                        Toasty.error(RequisitionDetailsActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toasty.error(RequisitionDetailsActivity.this, e.getMessage(), Toasty.LENGTH_SHORT).show();
                                }

//                if(){
//
//                }

                            }

                            @Override
                            public void onError(ANError anError) {

                                Toasty.error(RequisitionDetailsActivity.this, "Network Err:Retry", Toasty.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        getReqItems();
        //getRequisitionItemsDisplayInRecycViewRda();

    }


//
//
//    private void restoreLayoutManagerPosition() {
//        if (savedRecyclerLayoutState != null) {
//            recyclerViewRequisitionItems.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
//        }
//    }
//
//
//    @Override
//    public void onSaveInstanceState(final Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
//       //mListState = Objects.requireNonNull(recyclerViewRequisitionItems.getLayoutManager()).onSaveInstanceState();//method 1
//       // savedInstanceState.putParcelable(Constants.LIST_STATE,mListState);//method 1
//       savedInstanceState.putParcelableArrayList(Constants.LIST_STATE, requisitionItemList);//method 2 with ParcelableArrayList requisitionItemList. method 2 requires you convert your class Reqitem To a parceable. i made REQITEM class implement parcelable
//        savedInstanceState.putParcelable(Constants.BUNDLE_RECYCLER_LAYOUT, recyclerViewRequisitionItems.getLayoutManager().onSaveInstanceState());
//
//
//    }
//
//    @Override
//    protected void onRestoreInstanceState(final Bundle savedInstanceState) {//wil only be called if saved instance state not == nul, so dont have to check if not null
//        super.onRestoreInstanceState(savedInstanceState);
//
//       // mListState = savedInstanceState.getParcelable(Constants.LIST_STATE); //method 1, getting whoe recyclevew using layoutmanager
//        requisitionItemList = savedInstanceState.getParcelableArrayList(Constants.LIST_STATE);//method 2 with ParcelableArrayList requisitionItemList //method 2 requires you convert your class Reqitem To a parceable. i made REQITEM class implement parcelable
//            savedRecyclerLayoutState = savedInstanceState.getParcelable(Constants.BUNDLE_RECYCLER_LAYOUT);
//
//
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//
//       getReqItems();
//
//
//
//
//    }


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

        AndroidNetworking.initialize(RequisitionDetailsActivity.this, okHttpClient);
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
        assert req_id != null;
        Log.d("req_idd", req_id);


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

                                Toasty.error(RequisitionDetailsActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(RequisitionDetailsActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();

                        }


                        recyclerViewRequisitionItems.setLayoutManager(new LinearLayoutManager(RequisitionDetailsActivity.this));
                        requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsActivity.this, requisitionItems);
                        recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);
                        requisitionItemsExpensesAdapter.notifyDataSetChanged();
                        //requisitionItemsExpensesAdapter.loadNewData(requisitionItems);


                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDetailsActivity.this, "Network Err: Retry", Toasty.LENGTH_SHORT).show();


                    }
                });

    }

    private void postToReqDetailsRecyclerView() {
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

        AndroidNetworking.initialize(RequisitionDetailsActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


        //get reqId. because when we addreq it goes with the reqid
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
        String req_id = sharedPreferences1.getString("req_id", null);
        //will send fields as jsonobject
        Log.d("req_idd", req_id);


        final RequisitionItem requisitionItem = new RequisitionItem(customAddRequisitionItemNameC, customAddRequisitionItemDialogSupplierC, Integer.parseInt(customAddRequisitionItemDialogUnitCostC), customAddRequisitionItemsDialogQauntityC, Integer.parseInt(req_id));

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
                                customAddRequisitionItemsDialogRotateDiaolog.stop();
                                Toasty.success(RequisitionDetailsActivity.this, response.getString("message"), Toasty.LENGTH_LONG).show();

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

                                requisitionItems.add(requisitionItem1); //if we used this then add all below, it creates same item twice in recyclerview
                                //requisitionItemList.add was added tob used n saved intant state. b4 we only had  requisitionItems only.previously add all, but add all used for colections, like lists.requisiionitem1 is a single objet
                                requisitionItemList.add(requisitionItem1);
//
                                //requisitionItemsExpensesAdapter.addItem1(requisitionItem1);//if we used this with  requisitionItems.add(requisitionItem1); , it creates same item twice in recyclerview
//                                recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);

                                SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences2.edit();
                                editor.putString("req_name", requisitionItem1.getItemname());
                                editor.putString("req_item_supplier", requisitionItem1.getSupplier());
                                editor.putInt("req_item_unit_cost", requisitionItem1.getUnit_cost());
                                editor.putString("req_item_quantity", requisitionItem1.getQuantity());
                                editor.putInt("requisition_id", requisitionItem1.getRequisition_id());
                                editor.putInt("req_item_total", requisitionItem1.getTotal());
                                editor.putInt("req_item_id", requisitionItem1.getId());

                                editor.apply();
                                //getReqItems();
                                //also works, but decided to use post only, so as to maintin allitems added in savedinstance state. get below will display the single item only when you move from next activity to previous
                                //below gets a single req item belonging to a requisition
                                //getFromRequisitionItemViewGetIdPostToReqDetailsRecyclerView();

                                addReqItemsDialog.dismiss();


                            } else {
                                Toasty.error(RequisitionDetailsActivity.this, response.getString("message"), Toasty.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Toasty.error(RequisitionDetailsActivity.this, "GOOD RESPONSE BUT JAVA CAN'T PARSE JSON IT RECEIVED. " + e.getMessage(), Toasty.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                        //below works too aslong as you dont use // getFromRequisitionItemViewGetIdPostToReqDetailsRecyclerView();
                        recyclerViewRequisitionItems.setLayoutManager(new LinearLayoutManager(RequisitionDetailsActivity.this));
                        requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsActivity.this, requisitionItems);
                        recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDetailsActivity.this, "UNSUCCESSFUL " + anError.getMessage(), Toasty.LENGTH_LONG).show();

                    }
                });

    }


    private void getRequisitionItemsDisplayInRecycViewRda() {

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

        AndroidNetworking.initialize(RequisitionDetailsActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


//        SharedPreferences preferences = getSharedPreferences(Constants.REQUISITION_ITEM_WITH_REQUISITION, MODE_PRIVATE);
//        final int reqStatus = preferences.getInt("requisitionStatus",0);


        AndroidNetworking.get(Constants.GET_REQUISITION_ITEM_URL2)
                .setTag("Get RequisitonsItem WithRequisition")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RequisitionItem Req: ", response.toString());
                        String status = "";
                        JSONArray jsonArray = null;
                        try {
                            status = response.getString("status");
                            if (status.equals("success")) {
                                jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject requisitionItemJsonObject = jsonArray.getJSONObject(i);
                                    int reqItemId = requisitionItemJsonObject.getInt("id");
                                    String reqItemName = requisitionItemJsonObject.getString("itemname");
                                    String reqItemSupplier = requisitionItemJsonObject.getString("supplier");
                                    int reqItemUnitCost = requisitionItemJsonObject.getInt("unit_cost");
                                    String reqItemQuantity = requisitionItemJsonObject.getString("quantity");
                                    int reqItemTotal = requisitionItemJsonObject.getInt("total");
                                    int reqItemRequisitionId = requisitionItemJsonObject.getInt("requisition_id");

                                    JSONObject requisition = requisitionItemJsonObject.getJSONObject("requisition");
                                    int requisitionId = requisition.getInt("id");
                                    String requisitionName = requisition.getString("name");
                                    String requisitonDescription = requisition.getString("description");
                                    String requisitionDate = requisition.getString("date");
                                    int requisitionStatus = requisition.getInt("status");
                                    int requistionUserId = requisition.getInt("user_id");


                                    SharedPreferences sharedPreferences13 = getSharedPreferences(Constants.REQUISITION_ITEM_WITH_REQUISITION, MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences13.edit();
                                    //requisitionItems
                                    editor.putInt("reqItemId", reqItemId);
                                    editor.putString("reqItemName", reqItemName);
                                    editor.putString("reqItemSupplier", reqItemSupplier);
                                    editor.putInt("reqItemUnitCost", reqItemUnitCost);
                                    editor.putString("reqItemQuantity", reqItemQuantity);
                                    editor.putInt("reqItemTotal", reqItemTotal);
                                    editor.putInt("reqItemRequisitionId", reqItemRequisitionId);

                                    //requisition
                                    editor.putInt("requisitionId", requisitionId);
                                    editor.putString("requisitionName", requisitionName);
                                    editor.putString("requisitonDescription", requisitonDescription);
                                    editor.putString("requisitionDate", requisitionDate);
                                    editor.putInt("requisitionStatus", requisitionStatus);
                                    editor.putInt("requistionUserId", requistionUserId);
                                    editor.apply();


                                    RequisitionItem requisitionItem = new RequisitionItem(reqItemName, reqItemSupplier, reqItemUnitCost, reqItemQuantity, reqItemRequisitionId, reqItemTotal, reqItemId);
                                    // requisitionItems = new ArrayList<>();
                                    //requisitionItems.clear();
                                    requisitionItems.add(requisitionItem);

                                    recyclerViewRequisitionItems.setLayoutManager(new LinearLayoutManager(RequisitionDetailsActivity.this));
                                    requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsActivity.this, requisitionItems);
                                    recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);
                                    requisitionItemsExpensesAdapter.notifyDataSetChanged(); //used requisitionItemsExpensesAdapter2 instead of requisitionItemsExpensesAdapter because  dont want the delete imageview in list view in requisitionItemsExpensesAdapter to show when item submitted

                                    //requisitionItemsExpensesAdapter.loadNewData(requisitionItems);

                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toasty.error(RequisitionDetailsActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onError(ANError anError) {

                        Toasty.error(RequisitionDetailsActivity.this, "Net Err: Retry", Toasty.LENGTH_SHORT).show();
                    }
                });

    }


    private void getFromRequisitionItemViewGetIdPostToReqDetailsRecyclerView() {
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

        AndroidNetworking.initialize(RequisitionDetailsActivity.this, okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


        //get req item Id. because when we get request it goes with req item id
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.REQUISITION_ITEM_PREF_NAME, MODE_PRIVATE);
        int req_item_id = sharedPreferences1.getInt("req_item_id", 0);
        //will send fields as jsonobject
        Log.d("req_item_idd", String.valueOf(req_item_id));


        AndroidNetworking.get(Constants.GET_REQUISITION_ITEM_URL2 + "/" + req_item_id)
                .addPathParameter("id", String.valueOf(req_item_id))
                .setTag("GetReqItems")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String status = "";
//                        requisitionItemViewGetIds = new ArrayList<>();

                        Log.d("RESPONSEITEM::", response.toString());

                        try {
                            status = response.getString("status");

                            if (status.equals("success")) {

                                JSONObject jsonObject = null;
                                jsonObject = new JSONObject(response.getString("data"));

                                int reqItemId = jsonObject.getInt("id");
                                String reqitemname = jsonObject.getString("itemname");
                                String reqitemSupplier = jsonObject.getString("supplier");
                                int reqitemUnitCost = jsonObject.getInt("unit_cost");
                                String reqitemQuantity = jsonObject.getString("quantity");
                                int reqitemTotal = jsonObject.getInt("total");
                                int reqItemReqId = jsonObject.getInt("requisition_id");


                                JSONObject requistion = jsonObject.getJSONObject("requisition");
                                int req_id = requistion.getInt("id");
                                String req_name = requistion.getString("name");
                                String req_description = requistion.getString("description");
                                String req_date = requistion.getString("date");
                                int req_status = requistion.getInt("status");
                                int req_user_id = requistion.getInt("user_id");
                                int req_code = requistion.getInt("requisition_code");

                                Log.d("reqCOdeeee:::", String.valueOf(req_code));
                                RequisitionDataViewGet requisitionDataViewGet = new RequisitionDataViewGet(req_id, req_name, req_description, req_date, req_status, req_user_id, req_code);
                                RequisitionItemViewGetId requisitionItemViewGetId = new RequisitionItemViewGetId(reqItemId, reqitemname, reqitemSupplier, reqitemUnitCost, reqitemQuantity, reqitemTotal, reqItemReqId, requisitionDataViewGet);

                                //evertime user adds item by clicking  on additem dialog button, it will add reqItem to reqitems arraylist
//                                requisitionItemViewGetIds.add(requisitionItemViewGetId);


                                //store in model, then sav indiviual model items inshared pref,if you want to access values letter
                                RequisitionItem requisitionItem1 = new RequisitionItem(reqitemname.toUpperCase(), reqitemSupplier.toUpperCase(), reqitemUnitCost, reqitemQuantity, reqItemReqId, reqitemTotal, reqItemId);


                                requisitionItems.add(requisitionItem1);
                                //requisitionItemList.addAll was added tob used n saved intant state. b4 we only had  requisitionItems only
                                requisitionItemList.addAll(requisitionItems);// using list to save state instead of parceableAraayist

                                //get in shred pref, can b gotten later
//                                SharedPreferences sharedPreferences2 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME,MODE_PRIVATE);
//                                 sname = sharedPreferences2.getString("req_name",null);
//                                 sdescription = sharedPreferences2.getString("req_description", null);
//                                 sdate = sharedPreferences2.getString("req_date", null);
//                                 sid = sharedPreferences2.getString("req_id",null);
//                                 sstatus = sharedPreferences2.getString("req_status",null);


                            } else {

                                Toasty.error(RequisitionDetailsActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //you can initiate adapter here or at top, if you initiate at top, make sure you set it ere as below. reason i set it at top  is to taste adapter methods like load new data on start. if i initited adapter here,and called method loadNewData belonging to adapter, it will give me a null pinter exception on adapter, because adapter is initiated here and cant be acccessed from on start when you call it
                        recyclerViewRequisitionItems.setLayoutManager(new LinearLayoutManager(RequisitionDetailsActivity.this));
                        requisitionItemsExpensesAdapter = new RequisitionItemsExpensesAdapter(RequisitionDetailsActivity.this, requisitionItems);
                        recyclerViewRequisitionItems.setAdapter(requisitionItemsExpensesAdapter);
                        requisitionItemsExpensesAdapter.loadNewData(requisitionItems);
                        //used itemview in adapter to make item clickable
//                        recyclerViewRequisitionItems.addOnItemTouchListener(new MERecyclerItemClickListener(RequisitionDetailsActivity.this, new MERecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//
//                                RequisitionData requisitionData = new RequisitionData(sname,sdescription,sdate,Integer.parseInt(sid),Integer.parseInt(sstatus));
//                                RequisitionItem requisitionItem = requisitionItems.get(position);
//
//                                Intent intent = new Intent(RequisitionDetailsActivity.this, RequisitionDetailsEditActivity.class);
//                                intent.putExtra("reqItemName",requisitionItem.getItemname());
//                                intent.putExtra("reqItemSupplier",requisitionItem.getSupplier());
//                                intent.putExtra("reqItemUnitCost",requisitionItem.getUnit_cost());
//                                intent.putExtra("reqItemQuantity",requisitionItem.getQuantity());
//                                intent.putExtra("requisition_id",requisitionItem.getRequisition_id());
//                                intent.putExtra("reqItemTotal",requisitionItem.getTotal());
//                                intent.putExtra("id",requisitionItem.getId());
//                                intent.putExtra("name2",requisitionData.getName());
//                                intent.putExtra("description2", requisitionData.getDescription());
//                                intent.putExtra("date2",requisitionData.getDate());
//                                intent.putExtra("id2",requisitionData.getId());
//                                intent.putExtra("status2", requisitionData.getStatus());
//                                startActivity(intent);
//                                //startActivityForResult(intent,Constants.REQUEST_CODE1);
//                               // startActivity(intent,extras);
//
//                            }
//                        })
//                        );


                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(RequisitionDetailsActivity.this, Objects.requireNonNull(anError.getMessage()), Toasty.LENGTH_SHORT).show();


                    }
                });
    }


//get requisition detils and post to req details cardview. you can use intent, but coming back to activity gives empty bundle error


    public static int createRandomColor(int[] array) {
        //generate random number
        int randNum = new Random().nextInt(array.length);
        return array[randNum];

        //above shorter
//        int max = 39;
//        int min = 1;
//        int range = max - min + 1;
//
//        for(int i = 0; i < array.length; i++){
//           int rand = (int) ((Math.random()*range) + min);
//            return array[rand];
//        }


    }

    //The up button in the action bar is treated as a menu item with ID android.R.id.home, as you can read in the docs. There you can find that you can handle clicks on it using this code:
    //the second activity, one with bundle, is the one that will have id of back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {// Respond to the action bar's Up/Home button
            Intent returnIntent = getIntent();
            returnIntent.putExtra("returnData", "From ReqDetsAct To MakeReqActi");
            setResult(RESULT_OK, returnIntent);
            // finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //here req details is first activity, we want to move from reqdetailsedit activity to reqdetailsactivity. reqdetailsedit activity has bundle data from intent in this activity to
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String result = data.getStringExtra("returnDataaa");
                assert result != null;
                Log.d("Go back", result);

            }

        }
    }


}
