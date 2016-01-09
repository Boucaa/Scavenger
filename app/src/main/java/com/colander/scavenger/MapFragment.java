package com.colander.scavenger;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.colander.scavenger.serverhandling.JSONArrayCallbackInterface;
import com.colander.scavenger.serverhandling.RequestManager;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.OnSheetDismissedListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MapFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends android.support.v4.app.Fragment implements OnMapReadyCallback, JSONArrayCallbackInterface, LocationListener {
    private static GoogleMap mMap;
    private RequestManager requestManager;
    private LocationManager locationManager;
    private JSONArray nodes;
    private final String NODES_KEY = "nodes";
    private OnFragmentInteractionListener mListener;
    private BottomSheetLayout bottomSheet;
    private ImageLoader mImageLoader;
    private String currentNode = "";
    private View shadow;

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("MAP FRAGMENT CREATED");
        setUpRequestManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(Volley.newRequestQueue(getContext()), imageCache);
        if (this.requestManager == null) setUpRequestManager();
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(NODES_KEY)) {
                System.out.println("RESTORING NODES");
                try {
                    nodes = new JSONArray(savedInstanceState.getString(NODES_KEY));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("savedInstanceState didn't contain nodes");
                requestManager.getAllNodesJSON(this);
            }
        }
        bottomSheet = (BottomSheetLayout) view;
        bottomSheet.setShouldDimContentView(false);
        bottomSheet.setInterceptContentTouch(false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("MAP READY");
        this.mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        if (nodes == null) this.requestManager.getAllNodesJSON(this);
        else displayNodes();
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
        mMap.animateCamera(cameraUpdate);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showNode(marker);
                return true;
            }
        });
    }

    public void showNode(Marker marker) {
        for (int i = 0; i < nodes.length(); i++) {
            try {
                JSONObject obj = nodes.getJSONObject(i);
                if (marker.getId().equals(obj.getString("markerID"))) {
                    if (obj.getString("id").equals(this.currentNode)) return;
                    if (!bottomSheet.isSheetShowing()) {
                        bottomSheet.showWithSheetView(LayoutInflater.from(getContext()).inflate(R.layout.content_node, bottomSheet, false));
                        bottomSheet.addOnSheetDismissedListener(new OnSheetDismissedListener() {
                            @Override
                            public void onDismissed(BottomSheetLayout bottomSheetLayout) {
                                currentNode = "";
                            }
                        });
                        shadow = getActivity().findViewById(R.id.toolbar_shadow);
                        bottomSheet.addOnSheetStateChangeListener(new BottomSheetLayout.OnSheetStateChangeListener() {
                            @Override
                            public void onSheetStateChanged(BottomSheetLayout.State state) {
                                if (state == BottomSheetLayout.State.EXPANDED) {
                                    shadow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.solid_shadow));
                                    if (Build.VERSION.SDK_INT >= 21)
                                        getActivity().getActionBar().setElevation(0);
                                } else {
                                    shadow.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.toolbar_dropshadow));
                                    if (Build.VERSION.SDK_INT >= 21)
                                        getActivity().getActionBar().setElevation(4);
                                }
                            }
                        });
                        ((ImageButton)getActivity().findViewById(R.id.hide_node_button)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                bottomSheet.dismissSheet();
                            }
                        });
                    }
                    RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.node_images);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                    recyclerView.setAdapter(new NodeImgRecyclerAdapter(new String[]{"kek", "lel", "topkek", " adw", "L", " ", "AWD", " awdawda", "ls", "cd"}, mImageLoader, getContext()));
                    ((TextView) getActivity().findViewById(R.id.node_headline)).setText(obj.getString("headline"));
                    ((TextView) getActivity().findViewById(R.id.node_description)).setText(obj.getString("description"));
                    this.currentNode = obj.getString("id");
                    break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*@Override
    public void onPairResponse(Pair<Double, Double>[] pairs) {
        for (int i = 0; i < pairs.length; i++) {
            this.mMap.addMarker(new MarkerOptions().title("a loaded node!").position(new LatLng(pairs[i].first, pairs[i].second)));
        }
    }*/

    public void setUpRequestManager() {
        this.requestManager = new RequestManager(Volley.newRequestQueue(getActivity()));
    }

    public void displayNodes() {
        for (int i = 0; i < nodes.length(); i++) {
            try {
                JSONObject object = nodes.getJSONObject(i);
                Marker marker = this.mMap.addMarker(new MarkerOptions().title("a node! :)").position(new LatLng(object.getDouble("lat"), object.getDouble("lng"))));
                object.put("markerID", marker.getId());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (nodes != null) outState.putString(NODES_KEY, nodes.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onJSONResponse(JSONArray array, int id) {
        if (id == RequestManager.NODES_ID) {
            this.nodes = array;
            displayNodes();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
