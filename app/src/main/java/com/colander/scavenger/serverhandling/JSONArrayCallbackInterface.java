package com.colander.scavenger.serverhandling;

import org.json.JSONArray;

/**
 * Created by colander on 1/1/16.
 */
public interface JSONArrayCallbackInterface {
    void onJSONResponse(JSONArray array, int id);
}
