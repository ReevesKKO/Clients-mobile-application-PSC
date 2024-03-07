package com.example.pscapplication;

import static com.example.pscapplication.JWTHelper.decode;

import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {

    TextView tvForgotPassword;
    EditText etLogin, etPassword;
    Button btnLogIn, btnGoReg;
    String login, password, toastText, jwt;
    Integer errorCode, responseCode;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvForgotPassword.setPaintFlags(tvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnGoReg = findViewById(R.id.btnGoReg);
        etLogin = findViewById(R.id.etAuthLogin);
        etPassword = findViewById(R.id.etAuthPassword);

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login = String.valueOf(etLogin.getText());
                password = String.valueOf(etPassword.getText());
                if (!login.isEmpty() && !password.isEmpty()) {
                    new LoginTask().execute();
                }
                else {
                    notAllFieldsAreFilledDialog();
                }
            }
        });
    }

    public void notAllFieldsAreFilledDialog() {
        NotAllFieldsAreFilledFragment dialog = new NotAllFieldsAreFilledFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public class LoginTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                String ipaddress = BackHelper.getServerIpAddress();

                URL url = new URL("http://" + ipaddress + "/PSC/user-operations/login.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("login", login);
                postDataParams.put("password", password);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, StandardCharsets.UTF_8));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer();
                    String line;

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();

                }
                else {
                    return "false : " + responseCode;
                }
            }
            catch(Exception e){
                return "Exception: " + e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.e("Result", result);

            JSONObject ja = null;

            try {
                ja = new JSONObject(result);
                try {

                    responseCode = Integer.parseInt(ja.getString("code"));

                    if (responseCode == 400) {
                        errorCode = Integer.parseInt(ja.getString("error_code"));
                        switch (errorCode) {
                            case 1:
                                toastText = getResources().getString(R.string.wrong_login_or_password);
                                break;
                            case 2:
                                toastText = getResources().getString(R.string.no_login_found);
                                break;
                            case 3:
                                toastText = getResources().getString(R.string.not_all_fields_are_filled);
                                notAllFieldsAreFilledDialog();
                                break;
                        }
                    }
                    else if (responseCode == 200) {
                        jwt = ja.getString("JWT");
                        Log.e("JWT", jwt);
                        String[] parts = JWTHelper.splitJWT(jwt);
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            JSONObject header = new JSONObject(decode(parts[0]));
                            JSONObject payload = new JSONObject(decode(parts[1]));

                            Log.e("HEADER", String.valueOf(header));
                            Log.e("PAYLOAD", String.valueOf(payload));

                            String user_id = payload.getString("user_id");
                            String account_type = payload.getString("account_type");
                            Log.e("USER_ID", user_id);
                            Log.e("ACCOUNT_TYPE", account_type);

                            if (account_type.equals("client")) {
                                toastText = getResources().getString(R.string.success_login);
                                Intent intent = new Intent(getApplicationContext(), MainClientActivity.class);
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);
                            }
                            else {
                                toastText = getResources().getString(R.string.not_a_client);
                            }
                        }
                    }
                    else {
                        toastText = getResources().getString(R.string.unknown_error);
                    }
                    Toast.makeText(getApplicationContext(), toastText, Toast.LENGTH_LONG).show();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}