package com.mcligeyo.rsmhub.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.adapters.RequisitionDataExpensesAdapter;
import com.mcligeyo.rsmhub.constants.Constants;
import com.mcligeyo.rsmhub.model.RequisitionData;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingExpensesFragment extends Fragment {
    private LinearLayout linearLayoutErrorPending;
    private RecyclerView recyclerViewPending;
    private RequisitionDataExpensesAdapter requisitionDataExpensesAdapter;
    private List<RequisitionData> requisitionDatas = new ArrayList<>();
    private int reqSstatus;


    public PendingExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pending_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewPending = view.findViewById(R.id.recycler_expenses_pending);
        linearLayoutErrorPending = view.findViewById(R.id.linear_layout_error_pending);

        getRequisitionDataDisplayInRecycView();
        // getReqWithReqItem();


    }


    private void getRequisitionDataDisplayInRecycView() {


        // get token from shared pref saved in in login activity when you click login button
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
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

        AndroidNetworking.initialize(getContext(), okHttpClient);
        //AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        //only shows loging details when in development and not in production

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);


        //store required fields in requisitionitem class in shared pref
        //requisition_item goes in with requisitin_id


//


        AndroidNetworking.get(Constants.GET_REQUISITION_URL_2)
                .setTag("Get Requisitons")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response.length() > 0) { //if there is a response and staus is 1.1 will only show pending items
                            linearLayoutErrorPending.setVisibility(View.GONE);
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    String requistionName = jsonObject.getString("name");
                                    String requistionDescription = jsonObject.getString("description");
                                    String requistionDate = jsonObject.getString("date");
                                    int requisitonStatus = jsonObject.getInt("status");
                                    int requisitonId = jsonObject.getInt("id");

                                    SharedPreferences sharedPreferences1 = getActivity().getSharedPreferences(Constants.PENDING_STATUS, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                                    editor.putInt("reqStatus", requisitonStatus);
                                    editor.apply();

                                    RequisitionData requisitionData = new RequisitionData(requistionName, requistionDate, requisitonId, requisitonStatus);
                                    requisitionDatas.add(requisitionData);

                                    recyclerViewPending.setLayoutManager(new LinearLayoutManager(getActivity()));
                                    requisitionDataExpensesAdapter = new RequisitionDataExpensesAdapter(getContext(), requisitionDatas);
                                    recyclerViewPending.setAdapter(requisitionDataExpensesAdapter);
                                    requisitionDataExpensesAdapter.loadNewData(requisitionDatas);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toasty.error(Objects.requireNonNull(getContext()), Objects.requireNonNull(e.getMessage()), Toasty.LENGTH_SHORT).show();
                                }

                            }

//                            recyclerViewPending.setLayoutManager(new LinearLayoutManager(getActivity()));
//                            requisitionDataExpensesAdapter = new RequisitionDataExpensesAdapter(getContext(),requisitionDatas);
//                            recyclerViewPending.setAdapter(requisitionDataExpensesAdapter);
//                            requisitionDataExpensesAdapter.loadNewData(requisitionDatas);


                        } else {
                            //Toasty.error(Objects.requireNonNull(getContext()), "No Requisition",Toasty.LENGTH_SHORT).show();
                            linearLayoutErrorPending.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Toasty.error(Objects.requireNonNull(getContext()), "Network Err: Retry", Toasty.LENGTH_SHORT).show();
                    }
                });
    }
}
