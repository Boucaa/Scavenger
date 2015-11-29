package com.colander.scavenger.serverhandling;

import com.android.volley.RequestQueue;

/**
 * Created by colander on 11/29/15.
 */
public class RequestManager {
    private String serverIP = "http://10.0.0.34:8081/";
    private RequestQueue queue;

    public  RequestManager(RequestQueue queue){
        this.queue = queue;
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
