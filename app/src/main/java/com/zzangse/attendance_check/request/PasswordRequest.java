package com.zzangse.attendance_check.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PasswordRequest extends StringRequest {
    private final static String URL_CHANGE_PW = "http://www.zzangse.store/change_password.php";
    private final static String URL_FIND_PW = "http://www.zzangse.store/find_account/NewPassword.php";
    private Map<String, String> map;

    public enum RequestType {
        CHANGE_PW,
        FIND_PW
    }

    public PasswordRequest(RequestType requestType, String userID, String userPassword, String newPassword,
                           Response.Listener<String> listener) {
        super(Method.POST, getUrl(requestType), listener, null);
        map = new HashMap<>();
        if (requestType == RequestType.CHANGE_PW) {
            map.put("userID", userID);
            map.put("userPassword", userPassword);
            map.put("newPassword", newPassword);
        } else if (requestType == RequestType.FIND_PW) {
            map.put("userID", userID);
            map.put("newPassword", newPassword);
        }

    }

    private static String getUrl(RequestType requestType) {
        switch (requestType) {
            case FIND_PW:
                return URL_FIND_PW;
            case CHANGE_PW:
                return URL_CHANGE_PW;
            default:
                throw new IllegalArgumentException("inValid request Type");
        }
    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }
}
