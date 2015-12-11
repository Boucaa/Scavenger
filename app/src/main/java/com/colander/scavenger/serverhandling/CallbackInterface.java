package com.colander.scavenger.serverhandling;

import android.util.Pair;

/**
 * Created by colander on 11/29/15.
 */
public interface CallbackInterface {
    void onPairResponse(Pair<Double,Double>[] pairs);
}
