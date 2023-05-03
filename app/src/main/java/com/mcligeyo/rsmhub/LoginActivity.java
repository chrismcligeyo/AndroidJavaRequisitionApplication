package com.mcligeyo.rsmhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.User;
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


public class LoginActivity extends AppCompatActivity {

    private TextView welcomeTextview;
    private EditText loginEmailEditText;
    private EditText loginPasswordEditText;
    private Button loginButton;
    private RotateLoading rotateLoadingProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rotateLoadingProgressDialog = (RotateLoading) findViewById(R.id.rotateLoadingProgessDialog);
        welcomeTextview = (TextView) findViewById(R.id.WelcomeLoginTextView);
        loginEmailEditText = (EditText) findViewById(R.id.LoginEmailEditText);
        loginPasswordEditText = (EditText) findViewById(R.id.PasswordEditText);
        loginButton = (Button) findViewById(R.id.loginButtonID);

        //welcomeTextview animation in login page
        YoYo.with(Techniques.Tada)
                .duration(6000)
//               .repeat(3)
                .playOn(welcomeTextview);

//        Resources res = getResources();
//        String[] welcome = res.getStringArray(R.array.welcomeText);
//
//        for(int i = 0; i < welcome.length; i++){
//            fadingTextView.setText(welcome[i]);
//        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();


            }
        });

    }

    //overide on start. when user logins in first, and gets out of app, when she/he gets into app again,dont have to login again but will be directed to home page
    // will use id of user stored in sharedpreff.
    @Override
    protected void onStart() {
        super.onStart();
        //check if user is logged in, if so direct to home page(MainActivity) via intent
//        if(SharedPrefManager.getInstance(LoginActivity.this).isUserLoggedIn()){
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);

        if (sharedPreferences.getInt("id", -1) != -1) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void userLogin() {
        rotateLoadingProgressDialog.start();
        String email = loginEmailEditText.getText().toString().trim();
        String password = loginPasswordEditText.getText().toString().trim();

        if (email.isEmpty()) { //is empty returns a boolean
            loginEmailEditText.setError("Email is required");
            loginEmailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmailEditText.setError("Enter valid Mail");
            loginEmailEditText.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPasswordEditText.setError("Password Required");
            loginPasswordEditText.requestFocus();
            return;
        }

        //rsm cointains 4 character passwords that are hashed.
//        if(password.length() < 6 ){
//            loginPasswordEditText.setError("Password should be at least six characters");
//            loginPasswordEditText.requestFocus();
//            return;
//        }

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("Authorization", Constants.AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }
                )
                .build();

        AndroidNetworking.initialize(LoginActivity.this, okHttpClient);
        // AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


//        AndroidNetworkingServerCalls androidNetworkingServerCalls =new AndroidNetworkingServerCalls(LoginActivity.this);
//
//        androidNetworkingServerCalls.login(email, password);

//        Datla token = SharedPrefManager.getInstance(LoginActivity.this).getDatlaToken();
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        int id = sharedPreferences.getInt("id", 0);

        User user = new User(email, password, id);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("id", user.getId());
            // jsonObject.put("token", token); //added as body parameter in post below

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AndroidNetworking.post(Constants.LOG_IN_URL2)
//                .addBodyParameter(user)
                .addJSONObjectBody(jsonObject)
//                .addBodyParameter("token", token)
//                .addBodyParameter("id",id)
                .setTag("login")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        String status = "";
//                        Log.d("Success Here", "Result " + response.toString());
//                        String token = response.getString("token");
//                        int id = response.getInt("id");
//                        int activated = response.getInt("activated");
//
//                        Datla datla = new Datla(token,id,activated);
                        try {
                            status = response.getString("status");

                            if (status.equals("success")) {
                                //fetch user details through response
                                JSONObject jsonObject = null;
                                jsonObject = new JSONObject(response.getString("data"));

                                String token = jsonObject.getString("token");
                                int id = jsonObject.getInt("id");
                                int activated = jsonObject.getInt("activated");
                                String user_email = jsonObject.getString("user_email");
                                String user_name = jsonObject.getString("user_name");
                                String user_phone_number = jsonObject.getString("user_phone_number");
                                Log.d("JSONOBJCTID", token);
                                // Datla datla = new Datla(token,id,activated);
                                Log.d("JSONOBJCTIDDDD", String.valueOf(id));

                                //savedata fromresponse toshared pref
                                SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE);
                                SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
                                sharedPreferencesEditor.putString("token", token);
                                sharedPreferencesEditor.putInt("id", id);
                                sharedPreferencesEditor.putInt("activated", activated);
                                sharedPreferencesEditor.putString("user_email", user_email);
                                sharedPreferencesEditor.putString("user_name", user_name);
                                sharedPreferencesEditor.putString("user_phone_number", user_phone_number);

                                sharedPreferencesEditor.apply();
//                                //savedata fromresponse toshared pref
//                                SharedPrefManager.getInstance(LoginActivity.this).saveDatla(datla);
////                                Log.d("JSONOBJCTIDD", String.valueOf(SharedPrefManager.getInstance(context).saveDatla(datla)));
//
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toasty.success(LoginActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

                            } else {

                                rotateLoadingProgressDialog.stop();
                                Toasty.error(LoginActivity.this, response.getString("message"), Toasty.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Toasty.error(LoginActivity.this, Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Toasty.error(LoginActivity.this, "Net Err: Retry", Toasty.LENGTH_SHORT).show();

                    }
                });


    }


}
