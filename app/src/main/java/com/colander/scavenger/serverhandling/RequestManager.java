package com.colander.scavenger.serverhandling;

import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by colander on 11/29/15.
 */
public class RequestManager {
    private String serverIP = "http://10.0.0.34:8081/";
    private String mapRequest = "MAP";
    private RequestQueue queue;

    public  RequestManager(RequestQueue queue){
        this.queue = queue;
    }

    public <T extends CallbackInterface>void getAllNodes(final T callback){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverIP + mapRequest,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray json = new JSONObject(response).getJSONArray("result");
                            Pair<Double,Double>[] pairs = new Pair[json.length()];
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject obj = json.getJSONObject(i);
                                pairs[i] = new Pair<>(obj.getDouble("lat"),obj.getDouble("lng"));
                            }
                            callback.onPairResponse(pairs);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("REQUEST ERROR " + error.networkResponse.statusCode);

            }
        });
        queue.add(stringRequest);
    }
    //TODO: method which returns all nodes on the map

    /*RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://10.0.0.34:8081/TESTTTT";

    // Request a string response from the provided URL.
    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    //text2.setText("Server response is: " + response);
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            text2.setText("That didn't work!");
        }
    });
    // Add the request to the RequestQueue.
    queue.add(stringRequest);*/
}
