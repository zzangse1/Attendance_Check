package com.zzangse.attendance_check.request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class InsertCheckRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/insert_check.php";
    private Map<String, String> map;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public InsertCheckRequest(int priNum, String infoCheck, java.sql.Date infoDate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("priNum", String.valueOf(priNum));
        map.put("infoCheck", infoCheck);
        map.put("infoDate", simpleDateFormat.format(infoDate)); // Date를 문자열로 변환하여 저장

        Log.d("InsertCheckRequest", "priNum: " + priNum);
        Log.d("InsertCheckRequest", "infoCheck: " + infoCheck);
        Log.d("InsertCheckRequest", "infoDate: " + simpleDateFormat.format(infoDate));


    }

    @Override
    protected Map<String, String> getParams() {
        return map;
    }

    @Override
    public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }
}