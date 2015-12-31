package com.colander.scavenger;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

/**
 * Created by colander on 12/30/15.
 */
public class AccountContainer {
    private static GoogleSignInAccount mAccount;

    public static void setAccount(GoogleSignInAccount account) {
        mAccount = account;
    }

    public static GoogleSignInAccount getAccount() {
        return mAccount;
    }
}
