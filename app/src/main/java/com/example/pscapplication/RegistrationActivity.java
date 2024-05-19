package com.example.pscapplication;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class RegistrationActivity extends AppCompatActivity {

    String username, password, company_name, contact_person, email, phone_num, ipaddress, toastText;
    Integer responseCode, errorCode;
    FirstRegFragment firstRegFragment = new FirstRegFragment();
    SecondRegFragment secondRegFragment = new SecondRegFragment();
    @Override
    public void onBackPressed() {
        showCancelDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ipaddress = BackHelper.getServerIpAddress();
        getSupportFragmentManager().beginTransaction().replace(R.id.flRegFrameLayout, firstRegFragment).commit();
    }

    public void showCancelDialog() {
        CancelRegDialogFragment dialog = new CancelRegDialogFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void successfullyRegistered() {
        SuccessfulRegDialogFragment successfulRegDialogFragment = new SuccessfulRegDialogFragment();
        successfulRegDialogFragment.show(getSupportFragmentManager(), "custom");
    }

    public void notAllFieldsAreFilledDialog() {
        NotAllFieldsAreFilledFragment dialog = new NotAllFieldsAreFilledFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void regPasswordsNotEqualDialog() {
        RegPasswordsNotEqualFragment dialog = new RegPasswordsNotEqualFragment();
        dialog.show(getSupportFragmentManager(), "custom");
    }

    public void goNextStep() {
        getSupportFragmentManager().beginTransaction().replace(R.id.flRegFrameLayout, secondRegFragment).commit();
    }

    public void goReg() {
        new RegistrationTask().execute();
    }

    public String getLogin() {
        return username;
    }

    public void setLogin(String regUsername) {
        username = regUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String regPassword) {
        password = regPassword;
    }

    public void setCompName(String regCompanyName) {
        company_name = regCompanyName;
    }

    public void setContactPerson(String regContactPerson) {
        contact_person = regContactPerson;
    }

    public void setEmail(String regEmail) {
        email = regEmail;
    }

    public void setPhoneNum(String regPhoneNum) {
        phone_num = regPhoneNum;
    }

    public class RegistrationTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                String ipaddress = BackHelper.getServerIpAddress();

                URL url = new URL("http://" + ipaddress + "/PSC/user-operations/clients/registration.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
                postDataParams.put("password", password);
                postDataParams.put("company_name", company_name);
                postDataParams.put("contact_person", contact_person);
                postDataParams.put("email", email);
                postDataParams.put("phone_num", phone_num);
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
                                toastText = getResources().getString(R.string.reg_400_1);
                                break;
                            case 2:
                                toastText = getResources().getString(R.string.reg_400_2);
                                break;
                            case 3:
                                toastText = getResources().getString(R.string.reg_400_3);
                                notAllFieldsAreFilledDialog();
                                break;
                        }
                    }
                    else if (responseCode == 500) {
                            errorCode = Integer.parseInt(ja.getString("error_code"));
                            switch (errorCode) {
                                case 1:
                                    toastText = getResources().getString(R.string.reg_500_1);
                                    break;
                                case 2:
                                    toastText = getResources().getString(R.string.reg_500_2);
                                    break;
                                case 3:
                                    toastText = getResources().getString(R.string.reg_500_3);
                                    break;
                            }
                        }
                    else if (responseCode == 200) {
                        toastText = getResources().getString(R.string.reg_200);
                        successfullyRegistered();
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