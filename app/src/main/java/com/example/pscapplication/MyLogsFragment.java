package com.example.pscapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyLogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyLogsFragment extends Fragment {

    String login, ipaddress;
    View v;
    GridView gridList;
    static ArrayList<LogString> logs = new ArrayList<LogString>();
    JSONArray jsonArray;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyLogsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyLogsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyLogsFragment newInstance(String param1, String param2) {
        MyLogsFragment fragment = new MyLogsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_my_logs, container, false);
        login = MainClientActivity.getUsername();
        gridList = v.findViewById(R.id.gridLogs);
        LogStringListAdapter logStringListAdapter = new LogStringListAdapter(getActivity(), R.layout.grid_log, logs);
        gridList.setAdapter(logStringListAdapter);
        logStringListAdapter.clear();
        new MyLogsFragment.GetLogsList(getActivity()).execute();
        return v;
    }

    public class GetLogsList extends AsyncTask<String, Void, String> {
        Context context;
        private GetLogsList(Context context) {
            this.context = context.getApplicationContext();
        }
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                ipaddress = BackHelper.getServerIpAddress();

                URL url = new URL("http://" + ipaddress + "/PSC/user-operations/getlogs.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("login", login);
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

                int responseCode=conn.getResponseCode();

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

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONArray ja = null;
            JSONObject jsonObject = null;

            try {
                jsonObject = new JSONObject(result);
                Integer responseCode = Integer.parseInt(jsonObject.getString("code"));
                Log.e("RESPONSE CODE", String.valueOf(responseCode));

                ja = jsonObject.getJSONArray("logs");
                Log.e("LOGS", String.valueOf(ja));
                Log.e("RESULT", result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject singleString = null;
                    try {
                        singleString = ja.getJSONObject(i);
                        Log.e("STRING", String.valueOf(singleString));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LogString temp = new LogString(
                            singleString.getString("text")
                    );
                    logs.add(temp);
                    jsonArray = ja;
                    LogStringListAdapter stateAdapter = new LogStringListAdapter(context, R.layout.grid_log, logs);
                    GridView gridList = v.findViewById(R.id.gridLogs);
                    gridList.setAdapter(stateAdapter);
                    stateAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
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

    public static ArrayList<LogString> getLogs() {
        return logs;
    }

}