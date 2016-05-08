package com.colander.scavenger;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.zxing.Result;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ZXingScannerView.ResultHandler {

    private DrawerLayout drawerLayout;
    private ImageLoader imageLoader;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount userAccount;
    private int RC_SIGN_IN = 9001;
    private int SCAN_CODE = 9002;

    MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, (Toolbar) findViewById(R.id.toolbar), R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setTitle("Scavenger");
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationDrawerListener(this));
        navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.user_portrait);
                if (AccountContainer.getGoogleAccount() != null) {
                    networkImageView.setImageUrl(AccountContainer.getGoogleAccount().getPhotoUrl().toString(), imageLoader);
                    navigationView.removeOnLayoutChangeListener(this);
                }

            }
        });

        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        imageLoader = new ImageLoader(Volley.newRequestQueue(this), imageCache);
        System.out.println(imageLoader + " " + imageCache);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.server_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        signIn();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            if (result.getSignInAccount() != null) {
                AccountContainer.setGoogleAccount(result.getSignInAccount());
                System.out.println("hard login successful");
                onSuccessfulLogin();
            } else {
                Toast.makeText(this, "Login unsuccessful", Toast.LENGTH_LONG).show();
                signIn();
            }
        } else if (resultCode == ScanActivity.SCAN_CODE) {
            System.out.println("EUREKA");
            mapFragment.scanResult(intent.getExtras().getString("BARCODE"));
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            if (scanningResult != null) {
                String scanContent = scanningResult.getContents();
                TextView text = (TextView) findViewById(R.id.scanned_text);
                text.setText(scanContent);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No scan data received!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void signIn() {
        OptionalPendingResult<GoogleSignInResult> pendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (pendingResult.isDone()) {
            System.out.println("silent login successful");
            AccountContainer.setGoogleAccount(pendingResult.get().getSignInAccount());
            onSuccessfulLogin();
        } else {
            pendingResult.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult result) {
                    if (result.getSignInAccount() == null) {
                        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                        startActivityForResult(signInIntent, RC_SIGN_IN);
                    } else {
                        System.out.println("silent login successful");
                        AccountContainer.setGoogleAccount(result.getSignInAccount());
                        onSuccessfulLogin();
                    }
                }
            });
        }
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            userAccount = null;
                            signIn();
                        }
                    }
                });
    }

    public void scan(MapFragment fragment) {
        this.mapFragment = fragment;
        Intent i = new Intent(this, ScanActivity.class);
        startActivityForResult(i, SCAN_CODE);
    }

    //API SETUP ERROR
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.out.println("API CONNECTION FAILED");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                signOut();
                return true;
            default:
                return true;
        }
    }

    @Override
    public void handleResult(Result result) {
    }

    public void onSuccessfulLogin() {
        System.out.println("LOGIN SUCCESSFUL");
        NetworkImageView networkImageView = (NetworkImageView) findViewById(R.id.user_portrait);
        //((NetworkImageView) findViewById(R.id.user_portrait)).setImageUrl(AccountContainer.getGoogleAccount().getPhotoUrl().toString(), imageLoader);
    }
}
