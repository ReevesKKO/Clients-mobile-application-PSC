package com.example.pscapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

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
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View v;
    String user_id, ipaddress, username;
    static ArrayList<SecObject> objects = new ArrayList<SecObject>();
    JSONArray jsonArray;
    TextView tvProfileUsername, tvProfileCompanyName, tvProfilePhoneNumber, tvProfileEmail, tvProfileContactName;
    Button btnLogOut, btnGetLogs;
    MainClientActivity mainClientActivity;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        v = inflater.inflate(R.layout.fragment_profile, container, false);
        mainClientActivity = new MainClientActivity();
        tvProfileCompanyName = v.findViewById(R.id.tvProfileCompanyName);
        tvProfileEmail = v.findViewById(R.id.tvProfileEmail);
        tvProfileContactName = v.findViewById(R.id.tvProfileContactPerson);
        tvProfilePhoneNumber = v.findViewById(R.id.tvProfilePhoneNumber);
        tvProfileUsername = v.findViewById(R.id.tvProfileUsername);
        btnLogOut = v.findViewById(R.id.btnLogOut);
        btnGetLogs = v.findViewById(R.id.btnGetLogs);

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog();
            }
        });

        btnGetLogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLogsFragment nextFrag = new MyLogsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.flMainClientFrameLayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        username = MainClientActivity.getUsername();

        new ProfileFragment.GetProfileInfo(getActivity()).execute();
        return v;
    }

    public void exitDialog() {
        LogOutFragment dialog = new LogOutFragment();
        dialog.show(getParentFragmentManager(), "custom");
    }

    public class GetProfileInfo extends AsyncTask<String, Void, String> {
        Context context;
        private GetProfileInfo(Context context) {
            this.context = context.getApplicationContext();
        }
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                ipaddress = BackHelper.getServerIpAddress();

                URL url = new URL("http://" + ipaddress + "/PSC/user-operations/clients/get_info.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("username", username);
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
                ja = jsonObject.getJSONArray("client_info");
                Log.e("client_info", String.valueOf(ja));
                Log.e("RESULT", result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject singleObject = null;
                    try {
                        singleObject = ja.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        tvProfileEmail.setText(singleObject.getString("email"));
                        tvProfileCompanyName.setText(singleObject.getString("company_name"));
                        tvProfilePhoneNumber.setText(singleObject.getString("phone_num"));
                        tvProfileContactName.setText(singleObject.getString("contact_person"));
                        tvProfileUsername.setText(username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray = ja;
                    Log.e("JA", jsonArray.toString());
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
}