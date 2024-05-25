package com.zzangse.attendance_check.request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class InsertCheckRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/insert_check.php";
    private Map<String, String> map;

//    public InsertCheckRequest(int priNum,String infoCheck,String infoDate, Response.Listener<String> listener) {
//        super(Method.POST, URL, listener, null);
//
//        map = new HashMap<>();
//        map.put("priNum", String.valueOf(priNum));
//        map.put("infoCheck", infoCheck);
//        map.put("infoDate", infoDate);
//
//    }
    public InsertCheckRequest(int priNum,String infoCheck , Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("priNum", String.valueOf(priNum));
        //map.put("priNum", priNum+"");
        map.put("infoCheck", infoCheck);
    //    map.put("infoDate", infoDate);

    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
