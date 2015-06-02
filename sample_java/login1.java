package in.co.leaf.air.task.thread;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import in.co.leaf.air.AirApp;
import in.co.leaf.air.connection.ApiParameterConstant;
import in.co.leaf.air.connection.server.AirServerApi;
import in.co.leaf.air.database.query.BaseQuery;
import in.co.leaf.air.database.query.UserQuery;
import in.co.leaf.air.utility.SharedPreferencesManager;

public class BaseSignInTask extends AsyncTask<String, Integer, JSONObject> {

    protected boolean error = true;
    private String emailId;
    private String password;

    public BaseSignInTask(String emailId, String password) {
        this.emailId = emailId;
        this.password = password;
    }

    @Override
    protected JSONObject doInBackground(String[] params) {
        ArrayList<NameValuePair> paramList = new ArrayList<NameValuePair>();
        paramList.add(new BasicNameValuePair(ApiParameterConstant.User.EMAIL, emailId));
        paramList.add(new BasicNameValuePair(ApiParameterConstant.User.PASSWORD, password));
        JSONObject response = getResponse(paramList);
        storeLoginInfo(response);
        return response;
    }

    protected JSONObject getResponse(ArrayList<NameValuePair> paramList) {
        return new AirServerApi().performSignIn(paramList);
    }

    private void storeLoginInfo(JSONObject response) {
        try {
            if (response != null && response.getBoolean(ApiParameterConstant.STATUS)) {
                storeEmailId();
                BaseQuery.resetTables();
                JSONObject jsonUser = response.getJSONObject(ApiParameterConstant.DATA);
                UserQuery.syncUserDetail(jsonUser, AirApp.getContext());
                error = false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void storeEmailId() {
        SharedPreferencesManager spm = SharedPreferencesManager.getInstance();
        spm.writeEmail(emailId);
    }
}
