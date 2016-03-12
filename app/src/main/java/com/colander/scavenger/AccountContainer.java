package com.colander.scavenger;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.ArrayList;

/**
 * Created by colander on 12/30/15.
 */
public class AccountContainer {
    private static GoogleSignInAccount googleSignInAccount;
    private static ArrayList<String> visited = new ArrayList<>();

    public static void setGoogleAccount(GoogleSignInAccount googleAccount) {
        googleSignInAccount = googleAccount;
    }

    public static GoogleSignInAccount getGoogleAccount() {
        return googleSignInAccount;
    }

    public static void addVisited(ArrayList<String> nodes) {
        visited.addAll(nodes);
    }

    public static ArrayList<String> getVisited() {
        return visited;
    }
}
