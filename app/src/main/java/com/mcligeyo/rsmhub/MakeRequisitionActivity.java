package com.mcligeyo.rsmhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionData;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MakeRequisitionActivity extends AppCompatActivity {

    //    private TextView textView;
    private TextInputLayout requisitionItemNameTextInputLayout, requisitionDescriptionTextInputLayout, datePickerTextInputLayout;
    private TextInputEditText requisitionItemNameTextInputEditText, requisitionDescriptionTextInputEditText, datePickerTextInputEditText;
    private RotateLoading rotateLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_requisition);

        getSupportActionBar().setTitle("Make Requisition");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        textView = findViewById(R.id.textView2);

        MaterialButton materialButton = findViewById(R.id.makeReqButton);
        rotateLoading = findViewById(R.id.rotateLoadingProgessDialog);
        requisitionItemNameTextInputLayout = (TextInputLayout) findViewById(R.id.requisNameTextInputLay);
        requisitionDescriptionTextInputLayout = (TextInputLayout) findViewById(R.id.makeRequisDescriptionTextInputLay);
        datePickerTextInputLayout = (TextInputLayout) findViewById(R.id.makeRequisDateTextInputLay);
        requisitionItemNameTextInputEditText = (TextInputEditText) findViewById(R.id.requisNameTextInputEditText);
        requisitionDescriptionTextInputEditText = (TextInputEditText) findViewById(R.id.makeRequisDescriptionEditText);
        datePickerTextInputEditText = (TextInputEditText) findViewById(R.id.makeRequisDateEditText);
        rotateLoading = findViewById(R.id.rotateLoadingProgessDialog);


        // Use Calender class instance to get current date , month and year from calender

        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        //get todays date from materialDatePicker.(naturally uses a long, if we got date from calnder and try put in setselection, we will get error ecause it expects a long, and calender dates in int)
        long today = MaterialDatePicker.todayInUtcMilliseconds();
        //set todays date in calender using set time in milliseconds which accepts long
        calendar.setTimeInMillis(today);

        //set month to jan and store in  january long variable
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        long january = calendar.getTimeInMillis();

        //set month to december and store in december variable
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        long december = calendar.getTimeInMillis();

        //set monthto march. to show that you can start calendar on march instead of jan
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        long march = calendar.getTimeInMillis();

        //set calendar constraints using CalenderConstraintsBulder
        final CalendarConstraints.Builder calenderConstraintsBuilder = new CalendarConstraints.Builder();
        calenderConstraintsBuilder.setStart(january); //takes in a long
        calenderConstraintsBuilder.setEnd(december);
        //calenderConstraintsBuilder.setOpenAt(march);

        //set validator for constraints. dates before current date will be disabled
        //if today is 16th, from 15 to 1st will be disabled
        calenderConstraintsBuilder.setValidator(DateValidatorPointForward.now());


        MaterialDatePicker.Builder materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();
        materialDatePickerBuilder.setTitleText("PICK DATE");
        materialDatePickerBuilder.setSelection(today);
        materialDatePickerBuilder.setCalendarConstraints(calenderConstraintsBuilder.build());

        final MaterialDatePicker materialDatePicker = materialDatePickerBuilder.build();

//        datePickerTextInputEditText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                materialDatePicker.show(getSupportFragmentManager(), "MAKE REQUISITION ACT MATDATEPICKER");
//            }
//        });
        //ontouch preferrable on date picker than onclick listener. faster
        datePickerTextInputEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    //this code below solves error of (materialdatepicker fragment already addedd)when you click edit text twice,
                    if (materialDatePicker != null && materialDatePicker.isAdded()) {
                        //do nothing
                    } else {
                        //show date picker
                        materialDatePicker.show(getSupportFragmentManager(), "MAKE REQUISITION ACT MATDATEPICKER");

                    }
                    //how it was before if with isadded
//                    materialDatePicker.show(getSupportFragmentManager(), "MAKE REQUISITION ACT MATDATEPICKER");

                }
                return false;
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //selection gives the date in millisections. convert to string when using selection
                //materialDatePicker.getHeaderText() displays time as string(April 16, 2020)
                //textView.setText("Date Selected : " + selection.toString()); //displays 1586995200000.Convert the string timestamp to date format. then convert the date format to string to be placed in textview

                String x = selection.toString();
                //chnge currentdate milliseconds in stringabve to long
                long x2 = Long.parseLong(x);
                //change currentdatemilliseconds in long above to date
                Date date = new Date(x2);
                //format date to your liking use date formatter
                DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                String dateNew = formater.format(date);
//                textView.setText("Date Selected : " + dateNew);

                datePickerTextInputEditText.setText(dateNew);

            }
        });


        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              Toast.makeText(MakeRequisitionActivity.this,"clicked", Toast.LENGTH_LONG).show();


                createRequisition();

            }
        });

    }

    private void createRequisition() {
        rotateLoading.start();
        String requisitionItemName = String.valueOf(requisitionItemNameTextInputEditText.getText());
        String requisitionDescription = String.valueOf(requisitionDescriptionTextInputEditText.getText());
        String requisitionDate = String.valueOf(datePickerTextInputEditText.getText());

        if (requisitionItemName.isEmpty()) {
            requisitionItemNameTextInputLayout.setEndIconDrawable(R.drawable.ic_error_red);
//                        requisitionItemNameTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
            requisitionItemNameTextInputEditText.setError("Name Required");
            requisitionItemNameTextInputEditText.requestFocus();
            return;
        }

        if (requisitionDescription.isEmpty()) {
            requisitionDescriptionTextInputLayout.setEndIconDrawable(R.drawable.ic_error_red);
//                    requisitionDescriptionTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
            requisitionDescriptionTextInputEditText.setError("Description Required");
            requisitionDescriptionTextInputEditText.requestFocus();
            return;
        }

        if (requisitionDate.isEmpty()) {
            datePickerTextInputLayout.setEndIconDrawable(R.drawable.ic_error_red);
//                    datePickerTextInputEditText.setError("Date Required"); //not required here. wont appera cause datepicker fills space
            datePickerTextInputEditText.requestFocus();
//                    datePickerTextInputLayout.setEndIconVisible(true); //if you want icon to always appear
//                 datePickerTextInputEditText.setError("Date Required", ContextCompat.getDrawable(MakeRequisitionActivity.this, R.drawable.ic_error_red));// does not respond wellwith materialelements. use text input layout to seticondrawable. ContextCompat works better with native android elements
        }
        // get token from shared pref saved in in login activity when you click login button
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        final String token = sharedPreferences.getString("token", null);
        int user_id = sharedPreferences.getInt("id", -1);
        Log.d("user_id_from_login", String.valueOf(user_id));

        //shared pref to get req idto be sent with
        SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
        final String requisitionid = sharedPreferences1.getString("req_id", "0");

//        final Datla token = SharedPrefManager.getInstance(MakeRequisitionActivity.this).getDatlaToken();


        OkHttpClient okHttpClient1 = new OkHttpClient().newBuilder()
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

        AndroidNetworking.initialize(MakeRequisitionActivity.this, okHttpClient1);
//        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


        RequisitionData requisitionData = new RequisitionData(requisitionItemName, requisitionDescription, requisitionDate, user_id, Integer.parseInt(requisitionid));
        Log.d("usemid", String.valueOf(requisitionData.getUser_id()));
        Log.d("usemidreq", String.valueOf(requisitionData.getId()));

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", requisitionData.getName());
            jsonObject.put("description", requisitionData.getDescription());
            jsonObject.put("date", requisitionData.getDate());
            jsonObject.put("user_id", requisitionData.getUser_id());
            jsonObject.put("id", requisitionData.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        JSONObject jsonObject2 = new JSONObject();
//        try {
//            jsonObject2.put("token", token);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        AndroidNetworking.post(Constants.MAKE_REQUISITION_URL2)
                .addJSONObjectBody(jsonObject)
//                .addBodyParameter(id)
                .setTag("Make Requisition")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status = "";
                        try {
                            status = response.getString("status");
                            if (status.equals("success")) {
                                JSONObject jsonObject1 = null;
                                jsonObject1 = new JSONObject(response.getString("data"));
                                String req_name = jsonObject1.getString("name");
                                String req_description = jsonObject1.getString("description");
                                String req_date = jsonObject1.getString("date");
                                String req_user_id = jsonObject1.getString("user_id");
                                String req_status = jsonObject1.getString("status");
                                String req_id = jsonObject1.getString("id"); //this gives req id. want to display user_id. get fromshared pref

                                RequisitionData requisitionData1 = new RequisitionData(req_name, req_description, req_date, Integer.parseInt(req_user_id), Integer.parseInt(req_status), Integer.parseInt(req_id));


                                //store response values in shared pref, to be retrieved in addreq
                                SharedPreferences sharedPreferences1 = getSharedPreferences(Constants.MAKE_REQ_PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences1.edit();
                                editor.putString("req_name", requisitionData1.getName());
                                editor.putString("req_description", requisitionData1.getDescription());
                                editor.putString("req_date", requisitionData1.getDate());
                                editor.putString("req_user_id", String.valueOf(requisitionData1.getUser_id()));
                                editor.putString("req_status", String.valueOf(requisitionData1.getStatus()));
                                editor.putString("req_id", String.valueOf(requisitionData1.getId()));
                                editor.apply();


                                Toasty.success(MakeRequisitionActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();
                                Intent intent = new Intent(MakeRequisitionActivity.this, RequisitionDetailsActivity.class);
                                //detais in respnse tobe displayed in reqdetailsactivity
                                intent.putExtra("name", req_name);
                                intent.putExtra("description", req_description);
                                intent.putExtra("date", req_date);
                                intent.putExtra("user_id", req_user_id);
                                intent.putExtra("status", req_status);
                                intent.putExtra("req_id", req_id);


                                //startActivity(intent);
                                startActivityForResult(intent, Constants.REQUEST_CODE);
                                //finish(); //when you call finish on intent,you cant go back with upbutton


                                Log.i("taged", req_description);
                            } else {

                                rotateLoading.stop();
                                Toasty.error(MakeRequisitionActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

//                               JSONObject jsonObject =null;
//                               jsonObject = new JSONObject(response.getString("data"));
//
//                               String token = jsonObject.getString("token");
//                               int id = jsonObject.getInt("id");
//                               int activated = jsonObject.getInt("activated");
//                               Log.d("JSONOBJCTID", String.valueOf(id));
//                               Datla datla = new Datla(token,id,activated);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("ERRROR", Objects.requireNonNull(anError.getMessage()));


                        Toasty.error(MakeRequisitionActivity.this, "Network Error: Retry", Toasty.LENGTH_SHORT).show();

                    }
                });


//        AndroidNetworkingServerCalls androidNetworkingServerCalls = new AndroidNetworkingServerCalls(MakeRequisitionActivity.this);
//
//        androidNetworkingServerCalls.makeRequisition(requisitionItemName,requisitionDescription,requisitionDate);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String result = data.getStringExtra("returnData");
                assert result != null;
                Log.d("Go back", result);

            }

        }
    }
}
