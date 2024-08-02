package com.zzangse.attendance_check.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// DB에 등록된 정보가 있는지 확인하는 코드
public class FindAccountRequest extends StringRequest {
    private final static String URL_ID_VER = "http://zzangse.store/find_account/FindAccountID.php";
    private final static String URL_PW_VER = "http://zzangse.store/find_account/FindAccountPW.php";
    private Map<String, String> map;
    public enum RequestType {
        PASSWORD_VERIFICATION,
        ID_VERIFICATION
    }


    public FindAccountRequest(RequestType requestType,String userID,String userName, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, getUrl(requestType), listener, null);
        map = new HashMap<>();
        if (requestType == RequestType.PASSWORD_VERIFICATION) {
            map.put("userID", userID);
            map.put("userName", userName);
            map.put("userEmail", userEmail);
        } else if (requestType == RequestType.ID_VERIFICATION) {
            map.put("userName", userName);
            map.put("userEmail", userEmail);
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
