package com.example.root.test_app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.egl.EGLDisplay;

public class MainActivity extends Activity {

    public HttpRequestHandler RQ;
    public EditText number , password;
    public Button button;

    AsyncLogin loginTask = new AsyncLogin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        number = (EditText)findViewById(R.id.login_number);
        password = (EditText)findViewById(R.id.password);
        button = (Button) findViewById(R.id.login);
        Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_id));
        RQ = new HttpRequestHandler();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginTask.execute();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class AsyncLogin extends AsyncTask<String, Integer, JSONObject> {
        boolean error = false;

        @Override
        protected JSONObject doInBackground(String... params) {
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("email" , number.getText().toString()));
            param.add(new BasicNameValuePair("password" , password.getText().toString()));
            JSONObject response = RQ.getJSONfromurlPost("http://leafair.co.in/users/login", param);
            return  response;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            try {
                if (result.getBoolean("status")) {
                    Toast.makeText(MainActivity.this , result.getString("message") , Toast.LENGTH_SHORT).show();
                    //Open New Activity
                    //Finish this activity
                }else {
                    Toast.makeText(MainActivity.this , result.getString("message") , Toast.LENGTH_SHORT).show();
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

        protected void onProgressUpdate(Integer progress) {
        }

        protected void onPreExecute() {
        }
    }
}
