package com.colander.scavenger.serverhandling;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.colander.scavenger.AccountContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by colander on 11/29/15.
 */
public class RequestManager {
    private String serverIP = "http://192.168.24.252:8081/";
    private String MAP_REQUEST = "MAP";
    private String CLAIM_REQUEST = "CLAIM";
    private RequestQueue queue;
    public final static int NODES_ID = 1;

    public static String NETWORK_ERROR = "NET_ERR";
    final String TAG = "RequestManager";

    public RequestManager(RequestQueue queue) {
        this.queue = queue;
    }

    public <T extends JSONCallbackInterface> void claimNode(String id, String qr, final T callback) {
        System.out.printf("JSON request sent");
        Log.d(TAG, "JSON request sent");
        JSONObject request = new JSONObject();
        try {
            request.put("token", AccountContainer.getGoogleAccount().getIdToken());
            request.put("nodeID", id);
            request.put("qr", qr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonPostRequest(request, serverIP + CLAIM_REQUEST, callback);
    }

    public <T extends JSONCallbackInterface> void getMap(final T callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverIP + MAP_REQUEST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("GOT RESPONSE");
                        Log.d(TAG, "GOT RESPONSE");
                        try {
                            callback.onJSONResponse(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("REQUEST ERROR " + error.getMessage());
                Log.d(TAG, "REQUEST ERROR");
                try {
                    callback.onJSONResponse(new JSONObject().put("result", NETWORK_ERROR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        queue.add(stringRequest);
    }

    public <T extends JSONCallbackInterface> void JsonPostRequest(JSONObject jsonObject, String url, final T callback) {
        queue.add(new JsonObjectRequest(url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("JSON request successful");
                Log.d(TAG, "JSON request successful");
                callback.onJSONResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("JSON request unsuccessful " + error.getMessage());
                Log.d(TAG, "JSON request unsuccessful");
                try {
                    callback.onJSONResponse(new JSONObject().put("result", NETWORK_ERROR));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
