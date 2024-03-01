package com.example.pscapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class PreloadActivity extends AppCompatActivity {

    TextView tvStatusTest;
    Boolean state;
    String result, link;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);
        tvStatusTest = findViewById(R.id.tvStatusTest);
        new CheckConnection().execute();
    }

    public class CheckConnection extends AsyncTask<String, Void, String>
    {
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {
            try {
                link = new URL("http://10.0.2.2/").getHost();
                state = InetAddress.getByName(link).isReachable(15000);
                if (state == Boolean.TRUE) {
                    result = "Сервер доступен";
                }
                else {
                    result = "Сервер недоступен";
                }
            } catch (UnknownHostException e) {
                result = "UnknownHost";
            } catch (IOException e) {
                result = "ERR";
            }
            return result;
        }

        protected void onPostExecute(String result) {
            tvStatusTest.setText(result);
        }
    }
}