package com.zzangse.attendance_check.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class FindAccountSendRequest extends StringRequest {
    private final static String URL_PW_VER = "http://zzangse.store/find_account/CheckRandCodePW_VER.php";
    private final static String URL_ID_VER = "http://zzangse.store/find_account/CheckRandCodeID_VER.php";
    private Map<String, String> map;

    public enum RequestType {
        PASSWORD_VERIFICATION,
        ID_VERIFICATION
    }

    public FindAccountSendRequest(RequestType requestType, String identifier, String userEmail, String randomCode, Response.Listener<String> listener) {
        super(Method.POST, getUrl(requestType), listener, null);
        map = new HashMap<>();
        if (requestType == RequestType.PASSWORD_VERIFICATION) {
            map.put("userID", identifier);
            map.put("userEmail", userEmail);
            map.put("randCode", randomCode);
        } else if (requestType == RequestType.ID_VERIFICATION) {
            map.put("userName", identifier);
            map.put("userEmail", userEmail);
            map.put("randCode", randomCode);
        }
    }

    private static String getUrl(RequestType requestType) {
        switch (requestType) {
            case PASSWORD_VERIFICATION:
                return URL_PW_VER;
            case ID_VERIFICATION:
                return URL_ID_VER;
            default:
                throw new IllegalArgumentException("Invalid request type");
        }
    }


    @Override
    protected Map<String, String> getParams() {
        return map;
    }

}
