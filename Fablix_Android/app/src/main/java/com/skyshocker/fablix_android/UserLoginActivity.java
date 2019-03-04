package com.skyshocker.fablix_android;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A login screen that offers login via email/password.
 */
public class UserLoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);

        mPasswordView = (EditText) findViewById(R.id.password);
//        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
//                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
//                    return true;
//                }
//                return false;
//            }
//        });

        Button mEmailSignInButton = (Button) findViewById(R.id.login);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        System.out.println("login in");

        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "https://3.17.195.187:8443/fablix/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);

                        Log.d("username.reponse", response);
                        try {
                            JSONObject result = new JSONObject(response);
                            if(result.get("status").equals("fail")){
                                Toast.makeText(UserLoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                            } else if (result.get("status").equals("success")){
                                System.out.println("succeed");
                                Intent intent = new Intent(UserLoginActivity.this, MovieListActivity.class);
                                startActivity(intent);
                            } else {
                                System.err.println("response: " + response);
                            }
                        } catch (JSONException e) {
                            System.out.println(e);
                            System.out.println(response);
                            // Oops
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        System.out.println(error);
                        // error
                        Log.d("username.error", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                params.put("username", ((EditText)findViewById(R.id.email)).getText().toString());
                params.put("password", ((EditText)findViewById(R.id.password)).getText().toString());
                System.out.println(params);
                return params;
            }
        };

        queue.add(loginRequest);

    }
}

