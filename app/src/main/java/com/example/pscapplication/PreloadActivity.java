package com.example.pscapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class PreloadActivity extends AppCompatActivity {

    Boolean state;
    String result, link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckConnection().execute();
            }
        }, 500);
    }

    public class CheckConnection extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {
                String ipaddress = BackHelper.getServerIpAddress();
                link = new URL("http://" + ipaddress).getHost();
                state = InetAddress.getByName(link).isReachable(15000);
                if (state == Boolean.TRUE) {
                    result = "Success";
                }
                else {
                    result = "Error: server is unavailable";
                }
            } catch (UnknownHostException e) {
                result = "Error: UnknownHost";
            } catch (IOException e) {
                result = "Error";
            }
            return result;
        }

        protected void onPostExecute(String result) {
            if (result == "Success") {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
            else {
                showErrorDialog();
            }
        }

        public void showErrorDialog() {
            CheckInternetConnectionFragment dialog = new CheckInternetConnectionFragment();
            dialog.show(getSupportFragmentManager(), "custom");
        }
    }
}