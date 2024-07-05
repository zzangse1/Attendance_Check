package com.zzangse.attendance_check.request;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ModifyMemberRequest extends StringRequest {
    private final static String URL = "http://zzangse.store/modify_member.php";
    private Map<String, String> map;

    public ModifyMemberRequest(int priNum, String modifyGroupName, String modifyName, String modifyNumber,
                               String modifyNumber2, String modifyAddress,String modifyAddress2, String modifyMemo, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("priNum", String.valueOf(priNum));
        map.put("modifyGroupName", modifyGroupName);
        map.put("modifyName", modifyName);
        map.put("modifyNumber", modifyNumber);
        map.put("modifyNumber2", modifyNumber2);
        map.put("modifyAddress", modifyAddress);
        map.put("modifyAddress2", modifyAddress2);
        map.put("modifyMemo", modifyMemo);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
