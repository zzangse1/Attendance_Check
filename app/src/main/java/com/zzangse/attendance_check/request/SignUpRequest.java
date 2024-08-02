package com.zzangse.attendance_check.request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignUpRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/attendance_check/sign/sign_up.php";
    private Map<String, String> map;


    public SignUpRequest(String userID, String userEmail, String userPassword, String userNickName,
                         String userName, String userBirth, String userSex,
                         String userPhoneNumber, String userToken, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userEmail", userEmail);
        map.put("userPassword", userPassword);
        map.put("userNickName", userNickName);
        map.put("userName", userName);
        map.put("userBirth", userBirth);
        map.put("userSex", userSex);
        map.put("userPhoneNumber", userPhoneNumber);
        map.put("userToken", userToken);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
