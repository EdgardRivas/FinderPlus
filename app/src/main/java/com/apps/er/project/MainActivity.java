package com.apps.er.project;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener
{
    private double longitude;
    private double latitude;
    private LatLng loc;
    private LocationManager locationManager;
    private Location location;
    private Criteria criteria;
    private GoogleMap googleMap;
    private FloatingActionsMenu options;
    private FloatingActionButton search, locate, run;
    private View background;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        background = findViewById(R.id.background);
        options = (FloatingActionsMenu) findViewById(R.id.options);
        search = (FloatingActionButton) findViewById(R.id.search);
        locate = (FloatingActionButton) findViewById(R.id.locate);
        run = (FloatingActionButton) findViewById(R.id.run);

        options.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                background.setVisibility(View.VISIBLE);
                background.setEnabled(false);
            }

            @Override
            public void onMenuCollapsed() {
                background.setVisibility(View.GONE);
                background.setEnabled(true);
            }
        });
        search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle("Please Wait");
                dialog.setMessage("Searching");
                dialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, Connection.getPoints, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        dialog.dismiss();
                        try
                        {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("idSucursal");

                            if(code.equals("0"))
                            {
                                dialog.setTitle("Outputs");
                                dialog.setMessage("No matchets found");
                                dialog.show();
                            }
                            else
                            {
                                for (int i = 0; i < jsonArray.length(); i++)
                                {
                                    try
                                    {
                                        JSONObject object = jsonArray.getJSONObject(i);
                                        Integer iId = object.getInt("idSucursal");
                                        String iSuc = object.getString("sucursal");
                                        Double iLat = object.getDouble("lat");
                                        Double iLon = object.getDouble("lon");
                                        drawPoint(iSuc, iLat, iLon);
                                    }
                                    catch (JSONException e)
                                    {

                                    }
                                }
                            }
                        }
                        catch (JSONException e)
                        {

                        }
                    }
                }, new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        dialog.dismiss();
                        Toast.makeText(MainActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                Singleton.getInstance(MainActivity.this).addToRequest(stringRequest);
            }
        });
        locate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getCurrentLocation(googleMap);
            }
        });

        run.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                run(view);
            }
        });
        boolean splash = getPreferences(getApplicationContext());
        if(splash)
        {
            Intent iS = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(iS);
            setPreferences(getApplicationContext(), false);
        }
    }

    private void run(View view)
    {
        final View v = view;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = this.getLayoutInflater();
        final View dialogView = layoutInflater.inflate(R.layout.recover_account, null);
        final EditText editText = dialogView.findViewById(R.id.txtEmail);
        alertDialog.setView(dialogView);
        alertDialog.setTitle(getResources().getString(R.string.oRecover));
        alertDialog.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                String loc = editText.getText().toString();
                String[] parts = loc.split(",");
                String p1 = parts[1-1];
                String p2 = parts[2-1];
                double l1 = Double.parseDouble(p1);
                double l2 = Double.parseDouble(p2);
                drawPoint(loc, l1, l2);
                Snackbar.make(v, "Ok", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
        alertDialog.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                //Nothing
            }
        });

        AlertDialog dialog = alertDialog.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    public void setPreferences(Context context, Boolean splash)
    {
        SharedPreferences spSplash = context.getSharedPreferences("SPLASH_SCREEN", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = spSplash.edit();
        editor.putBoolean("FIRST_TIME", splash);
        editor.apply();
    }

    public boolean getPreferences(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SPLASH_SCREEN", MODE_PRIVATE);
        return sharedPreferences.getBoolean("FIRST_TIME", true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.settings:
                //Intent iS = new Intent(this, Settings.class);
                //startActivity(iS);
                return true;
            case R.id.login:
                Intent iL = new Intent(this, Login.class);
                startActivity(iL);
                return true;
            case R.id.contact:
                //Intent iC = new Intent(this, Contact.class);
                //startActivity(iC);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void getCurrentLocation(GoogleMap map)
    {
        map.clear();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            //map.setMyLocationEnabled(true);
            //map.getUiSettings().setMyLocationButtonEnabled(true);
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.ACCURACY_COARSE);
            String provider = locationManager.getBestProvider(criteria, true);
            location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 10000, 15, this);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String title = "YOU ARE HERE!";
            drawPoint(title, latitude, longitude);
        }
    }

    @Override
    public void onMapReady(GoogleMap map)
    {
        googleMap = map;
        //googleMap.setMyLocationEnabled(true);
        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        String title = "New York City";
        double lat = 40.730610;
        double lon = -73.935242;
        //String title = "UNICAES";
        //double lat = 13.984288016871906;
        //double lon = -89.54727340841293;
        drawPoint(title, lat, lon);
    }

    private void drawPoint(String title, double lat, double lon)
    {
        loc = new LatLng(lat,lon);
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.addMarker(new MarkerOptions().position(loc).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerDragListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        getCurrentLocation(googleMap);
    }

    @Override
    public void onConnectionSuspended(int i)
    {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    public void onMapLongClick(LatLng latLng)
    {
        googleMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker)
    {
        Toast.makeText(this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker)
    {
        Toast.makeText(this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker)
    {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
    }

    @Override
    public void onLocationChanged(Location location)
    {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle)
    {

    }

    @Override
    public void onProviderEnabled(String s)
    {

    }

    @Override
    public void onProviderDisabled(String s)
    {

    }
}
