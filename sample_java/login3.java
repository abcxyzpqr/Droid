package in.co.leaf.air.connection.server;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import in.co.leaf.air.AirApp;
import in.co.leaf.air.AppConstant;
import in.co.leaf.air.task.thread.BaseSignOutTask;


public class HTTPRequestHandler {

    private InputStream is = null;
    public JSONObject jObj = null;
    private String json = "";
    private boolean http_success = true;
    private StringBuilder sb;
    private static final int AUTH_FAILED = 401;

    public JSONObject getJSONFromUrlPOST(String url, List<NameValuePair> params) {
        http_success = true;
        try {
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
            DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setHeader("X-API-VERSION", AppConstant.API_VERSION);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            http_success = false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            http_success = false;
        } catch (IOException e) {
            e.printStackTrace();
            http_success = false;
        }

        if (http_success) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                AppConstant.log("airhttp ", "row response:" + json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
                try {
                    jObj = new JSONObject();
                    jObj.put("status", "false");

                    jObj.put("message", "Error converting result");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }

            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
                if (jObj.getInt("code") == AUTH_FAILED) {
                    onAuthenticationFailed();
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                try {
                    jObj = new JSONObject();
                    jObj.put("status", "false");
                    jObj.put("message", "Error parsing data");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                jObj = new JSONObject();
                jObj.put("status", "false");
                jObj.put("connection", "false");
                jObj.put("message", "Cannot Connect to Server");
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        AppConstant.log("airhttp ", "post response:" + jObj);
        return jObj;
    }

    public JSONObject getJSONFromUrlGET(String url, String params) {
        http_success = true;
        try {
            url += "?" + params;
            URL targetURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) targetURL.openConnection();
            conn.setRequestProperty("X-API-VERSION", AppConstant.API_VERSION);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            conn.setUseCaches(false);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String currentLine;
            sb = new StringBuilder();
            while ((currentLine = in.readLine()) != null) {
                sb.append(currentLine + "\n");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            http_success = false;
        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            http_success = false;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            http_success = false;
        } catch (IOException e) {
            e.printStackTrace();
            http_success = false;
        } catch (Exception e) {
            e.printStackTrace();
            http_success = false;
        }

        if (http_success) {
            json = sb.toString();
            AppConstant.log("airhttp ", "row response:" + json);
            // try parse the string to a JSON object
            try {
                jObj = new JSONObject(json);
                if (jObj.getInt("code") == AUTH_FAILED) {
                    onAuthenticationFailed();
                }
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
                try {
                    jObj = new JSONObject();
                    jObj.put("status", "false");
                    jObj.put("message", "Error parsing data");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
            }
        } else {
            try {
                jObj = new JSONObject();
                jObj.put("status", "false");
                jObj.put("connection", "false");
                jObj.put("message", "Cannot Connect to Server");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        AppConstant.log("airhttp ", "post response:" + jObj);
        return jObj;
    }

    private void onAuthenticationFailed() {
        AppConstant.showToast("Authentication failed.");
        BaseSignOutTask.onUserLogout();
    }
}
