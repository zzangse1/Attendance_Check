package com.zzangse.attendance_check.request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertMemberRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/insert_member.php";
    private Map<String, String> map;


    public InsertMemberRequest(String userID, String groupName, String name, String number, String number2,
                               String address, String memo, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID", userID);
        map.put("groupName", groupName);
        map.put("name", name);
        map.put("number", number);
        map.put("number2", number2);
        map.put("address", address);
        map.put("memo", memo);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
