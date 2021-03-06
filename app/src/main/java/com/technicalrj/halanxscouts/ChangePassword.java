package com.technicalrj.halanxscouts;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.technicalrj.halanxscouts.LoginActivity.halanxScout;
import static com.technicalrj.halanxscouts.RegisterActivity.JSON;

public class ChangePassword extends AppCompatActivity {

    private EditText passwordTv;
    private EditText confirmPasswordTv;
    private EditText oldPasswordTv;

    private String key;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);







        SharedPreferences prefs = getSharedPreferences("login_user_halanx_scouts", MODE_PRIVATE);
        key = prefs.getString("login_key", null);

        passwordTv = findViewById(R.id.password);
        confirmPasswordTv = findViewById(R.id.confirm_password);
        oldPasswordTv = findViewById(R.id.old_password);

    }

    public void login(View view) {


        String password  = passwordTv.getText().toString().trim();
        String confirmPassword  = confirmPasswordTv.getText().toString().trim();
        String oldPassword  = oldPasswordTv.getText().toString().trim();

        if(password.isEmpty()){
            passwordTv.setError("Password can't be empty");
        }else if(confirmPassword.isEmpty()){
            confirmPasswordTv.setError("Confirm Password can't be empty");
        }else if(oldPassword.isEmpty()) {
            oldPasswordTv.setError("Old Password can't be empty");
        }else{

            // Not validate the informatono entered

            if(!password.equals(confirmPassword)){
                confirmPasswordTv.setError("Password Not matching");
            } else {

                //Everyting is correct sending otp

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading...");
                progressDialog.show();

                OkHttpClient client = new OkHttpClient();



                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.put("otp",JSONObject.NULL);
                    jsonObject.put("old_password",oldPassword);
                    jsonObject.put("new_password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }





                RequestBody body = RequestBody.create(JSON, jsonObject.toString());
                Log.i("InfoText","key:"+key + " json:"+jsonObject.toString());

                final Request request = new Request.Builder()
                        .url(halanxScout+"/rest-auth/password/change/")
                        .patch(body)
                        .addHeader("Authorization","Token "+key)
                        .build();





                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        Log.i("InfoText","Details :"+response.body().string());

                        if(response.isSuccessful()){


                            progressDialog.dismiss();

                            ChangePassword.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ChangePassword.this,"Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                }
                            });


                            finish();









                        }else {
                            ChangePassword.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressDialog.dismiss();

                                    Log.i("InfoText","password not succ.");
                                    Toast.makeText(ChangePassword.this,"Unable to change password", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                });




            }

        }


    }
}
