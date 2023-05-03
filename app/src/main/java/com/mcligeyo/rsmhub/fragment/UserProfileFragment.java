package com.mcligeyo.rsmhub.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.mcligeyo.rsmhub.LoginActivity;
import com.mcligeyo.rsmhub.R;
import com.mcligeyo.rsmhub.constants.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment {
    private TextView fullName, userName, userProfilePendingAmount, userProfilePendinText, userProfileApprovedAmount, userProfileApprovedText, fragmentUserProfileSideNameText, fragmentUserProfileSideEmailText, fragmentUserProfileSidePhoneNumText;

    private MaterialButton logoutButton;

    public UserProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullName = view.findViewById(R.id.fullname_field);
        userName = view.findViewById(R.id.username_field);
        userProfilePendingAmount = view.findViewById(R.id.userProfilePendingAmountTextView);
        userProfilePendinText = view.findViewById(R.id.userProfilePendinTextTextView);
        userProfileApprovedAmount = view.findViewById(R.id.userProfileApprovedAmountTextView);
        userProfileApprovedText = view.findViewById(R.id.userProfileApprovedTextTextView);
        fragmentUserProfileSideNameText = view.findViewById(R.id.fragmentUserProfileSideNameTextTextView);
        fragmentUserProfileSideEmailText = view.findViewById(R.id.fragmentUserProfileSideEmailTextTextView);
        fragmentUserProfileSidePhoneNumText = view.findViewById(R.id.fragmentUserProfileSidePhoneNumTextTextView);

        logoutButton = view.findViewById(R.id.ReqDetailsSubmitButton);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        String name_user = sharedPreferences.getString("user_name", null);
        String name_email = sharedPreferences.getString("user_email", null);
        String phone_number = sharedPreferences.getString("user_phone_number", null);

        fullName.setText(name_user);
        userName.setText(name_user);
        fragmentUserProfileSideNameText.setText(name_user);
        fragmentUserProfileSideEmailText.setText(name_email);
        fragmentUserProfileSidePhoneNumText.setText(phone_number);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogout();
            }
        });
    }


    public void userLogout() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear()
                .apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

    }


}
