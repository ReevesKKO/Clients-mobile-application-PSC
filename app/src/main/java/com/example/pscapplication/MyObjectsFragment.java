package com.example.pscapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * Use the {@link MyObjectsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyObjectsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    View v;
    String user_id, ipaddress;
    GridView gridList;
    static ArrayList<SecObject> objects = new ArrayList<SecObject>();
    JSONArray jsonArray;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyObjectsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyObjectsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyObjectsFragment newInstance(String param1, String param2) {
        MyObjectsFragment fragment = new MyObjectsFragment();
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
        v = inflater.inflate(R.layout.fragment_my_objects, container, false);
        user_id = MainClientActivity.getUserId();
        gridList = v.findViewById(R.id.gridObjects);
        SecObjectListAdapter secObjectListAdapter = new SecObjectListAdapter(getActivity(), R.layout.grid_object, objects);
        gridList.setAdapter(secObjectListAdapter);
        secObjectListAdapter.clear();
        new MyObjectsFragment.GetAllObjectsList(getActivity()).execute();

        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Integer objectId = objects.get(i).getId();
                String objectName  = objects.get(i).getName();
                String objectAddress = objects.get(i).getAddress();
                String objectDescription = objects.get(i).getDescription();
                String objectSecurityMode = objects.get(i).getSecurityMode();
                Integer objectUserId = objects.get(i).getClientId();

                Intent detailObjectIntent = new Intent(getContext(), ObjectDetailedActivity.class);
                detailObjectIntent.putExtra("id", String.valueOf(objectId));
                detailObjectIntent.putExtra("name", objectName);
                detailObjectIntent.putExtra("address", objectAddress);
                detailObjectIntent.putExtra("description", objectDescription);
                detailObjectIntent.putExtra("security_mode", objectSecurityMode);
                detailObjectIntent.putExtra("user_id", String.valueOf(objectUserId));
                startActivity(detailObjectIntent);
            }
        });

        return v;
    }

    public class GetAllObjectsList extends AsyncTask<String, Void, String> {
        Context context;
        private GetAllObjectsList(Context context) {
            this.context = context.getApplicationContext();
        }
        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                ipaddress = BackHelper.getServerIpAddress();

                URL url = new URL("http://" + ipaddress + "/PSC/objects-operations/get/all_by_client.php");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("user_id", user_id);
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
                ja = jsonObject.getJSONArray("objects");
                Log.e("OBJECTS", String.valueOf(ja));
                Log.e("RESULT", result);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject singleObject = null;
                    try {
                        singleObject = ja.getJSONObject(i);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        SecObject temp = new SecObject(
                                Integer.parseInt(singleObject.getString("id")),
                                singleObject.getString("name"),
                                singleObject.getString("address"),
                                singleObject.getString("description"),
                                singleObject.getString("security_mode"),
                                Integer.parseInt(singleObject.getString("client_id"))
                        );
                        objects.add(temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray = ja;
                    SecObjectListAdapter stateAdapter = new SecObjectListAdapter(context, R.layout.grid_object, objects);
                    GridView gridList = v.findViewById(R.id.gridObjects);
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

    public static ArrayList<SecObject> getObjects() {
        return objects;
    }
}