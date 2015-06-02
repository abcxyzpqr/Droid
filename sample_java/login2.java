package in.co.leaf.air.connection.server;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import in.co.leaf.air.AirApp;
import in.co.leaf.air.AppConstant;
import in.co.leaf.air.connection.ApiParameterConstant;
import in.co.leaf.air.utility.Utility;

public class AirServerApi {

    public JSONObject performSignIn(List<NameValuePair> params) {
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlPOST
                (AppConstant.BASE_URL + "users/login", params);
        return response;
    }

    public JSONObject performSignUp(List<NameValuePair> params) {
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlPOST
                (AppConstant.BASE_URL + "users/create", params);
        return response;
    }

    public JSONObject deviceActionAnalytic(List<NameValuePair> params) {
        params = addExtraParameter(params);
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlPOST
                (AppConstant.DEV_BASE_API_URL +
                        "hubs/adddeviceaction", params);
        return response;
    }

    public JSONObject forgotPassword(List<NameValuePair> params) {
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlPOST(AppConstant.BASE_URL +
                "users/reset", params);
        return response;
    }

    public JSONObject signOut(List<NameValuePair> params){
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlPOST(AppConstant.BASE_URL +
                "users/logout", params);
        return response;
    }

    public JSONObject shareControls(List<NameValuePair> params) {
        params = addExtraParameter(params);
        JSONObject json =  new HTTPRequestHandler().getJSONFromUrlPOST(AppConstant.BASE_API_URL + "hubs/inviteuser", params);
        return json;
    }

    public JSONObject deleteSharedUser(List<NameValuePair> params) {
        params = addExtraParameter(params);
        JSONObject json =  new HTTPRequestHandler().getJSONFromUrlPOST(AppConstant.BASE_API_URL + "hubs/removeusers", params);
        return json;
    }

    private List<NameValuePair> addExtraParameter(List<NameValuePair> params) {
        if (params == null) {
            params = new ArrayList<NameValuePair>();
        }
        params.add(new BasicNameValuePair(ApiParameterConstant.SESSION_ID,
                AirApp.getInstance().getSessionId()));
        params.add(new BasicNameValuePair(ApiParameterConstant.CSRF_TOKEN,
                AirApp.getInstance().getCsrfToken()));
        return params;
    }

    //Get Method

    public JSONObject testServer(){
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlGET
            (AppConstant.DEV_BASE_API_URL + "/test", "");
        return response;
    }

    public JSONObject getAllData(HashMap<String, String> paramList) {
        paramList = addExtraParameter(paramList);
        String params = Utility.getUrlParamString(paramList);
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlGET
                (AppConstant.DEV_BASE_API_URL + "init", params);
        return response;
    }

    public JSONObject getAppNotification(HashMap<String, String> paramList) {
        paramList = addExtraParameter(paramList);
        String params = Utility.getUrlParamString(paramList);
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlGET
                (AppConstant.BASE_API_URL + "notifications/get", params);
        return response;
    }

    public JSONObject getRoomDevices(HashMap<String, String> paramList) {
        paramList = addExtraParameter(paramList);
        String params = Utility.getUrlParamString(paramList);
        JSONObject response = new HTTPRequestHandler().getJSONFromUrlGET
                (AppConstant.BASE_API_URL + "devices/getbyroom", params);
        return response;
    }

    private HashMap<String, String> addExtraParameter(HashMap<String, String> paramMap) {
        if (paramMap == null) {
            paramMap = new HashMap<String, String>();
        }
        paramMap.put(ApiParameterConstant.SESSION_ID, AirApp.getInstance().getSessionId());
        paramMap.put(ApiParameterConstant.CSRF_TOKEN, AirApp.getInstance().getCsrfToken());
        return paramMap;
    }
}
