package com.zzangse.attendance_check.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// DB에 저장한 6자리 난수를 삭제하는 코드 ID.VER
public class FindAccountDeleteRequest extends StringRequest {
    private static final String URL_PW_VER = "http://zzangse.store/find_account/DeleteCodePW_VER.php";
    private static final String URL_ID_VER = "http://zzangse.store/find_account/DeleteCodeID_VER.php";
    private Map<String, String> map;

    public enum RequestType {
        PASSWORD_VERIFICATION,
        ID_VERIFICATION
    }

    public FindAccountDeleteRequest(RequestType requestType, String identifier, String userEmail, Response.Listener<String> listener) {
        super(Method.POST, getUrl(requestType), listener, null);
        map = new HashMap<>();
        if (requestType == RequestType.PASSWORD_VERIFICATION) {
            map.put("userID", identifier);
        } else if (requestType == RequestType.ID_VERIFICATION) {
            map.put("userName", identifier);
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