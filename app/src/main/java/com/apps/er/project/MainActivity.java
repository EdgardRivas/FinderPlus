package com.apps.er.project;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Permission;

import toan.android.floatingactionmenu.FloatingActionButton;
import toan.android.floatingactionmenu.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerClickListener
{
    // APIKey que conseguimos en la consola de desarrolladores de Google.
    private String key = "AIzaSyB2ga0iCOSfLHNc9tEQM7qmcs7h9Zfr26s";
    // Variables para el modal.
    private AlertDialog.Builder builder;
    private LayoutInflater layoutInflater;
    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    // Variables que va a usar el mapa.
    private double longitude;
    private double latitude;
    private float zoomLevel;
    private GoogleMap map;
    // Variables para la solicitud de lugares cercanos con la Api de Google
    GoogleApiClient client;
    LocationRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        zoomLevel = 15.0f;

        // Instanciamos los botones.
        final FloatingActionsMenu options = findViewById(R.id.options);
        FloatingActionButton locate = findViewById(R.id.locate);
        FloatingActionButton search = findViewById(R.id.search);

        // Evento al hacer click en el botón localizar.
        locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCurrentLocation(map);
                options.collapse();
            }
        });
        // Evento al hacer click en el botón buscar.
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
                options.collapse();
            }
        });
        boolean splash = getPreferences(getApplicationContext());
        if(splash) {
            Intent iS = new Intent(MainActivity.this, SplashScreen.class);
            startActivity(iS);
            setPreferences(getApplicationContext(), false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent iS = new Intent(this, Settings.class);
                startActivity(iS);
                return true;
            case R.id.about:
                Intent iA = new Intent(this, About.class);
                startActivity(iA);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMinZoomPreference(12.0f);
        map.setMaxZoomPreference(18.0f);
        //buildGoogleClient(); // Otra forma de obtener la ubicación actual.
        getCurrentLocation(map);
    }

    private void buildGoogleClient(){
        if(client != null ){
            client = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            client.connect();
        }
    }

    // Método para obtener la ubicación actual (localizar).
    private void getCurrentLocation(GoogleMap map) {
        map.clear();
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_COARSE);
            criteria.setPowerRequirement(Criteria.ACCURACY_COARSE);
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            locationManager.requestLocationUpdates(provider, 10000, 15, this);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            String title = "YOU ARE HERE!";
            drawPoint(title, latitude, longitude);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION}, ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION));
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_COARSE_LOCATION}, ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION));
        }
    }

    // M+etodo para crear un puntero en el mapa.
    private void drawPoint(String title, double lat, double lon) {
        LatLng loc = new LatLng(lat, lon);
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.addMarker(new MarkerOptions().position(loc).title(title));
        map.moveCamera(CameraUpdateFactory.newLatLng(loc));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, zoomLevel));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerDragListener(this);
    }

    // Método para buscar.
    private void search() {
        // Instanciamos el modal.
        builder = new AlertDialog.Builder(this);
        layoutInflater = this.getLayoutInflater();
        @SuppressLint("InflateParams")
        final View dialogView = layoutInflater.inflate(R.layout.modal_search, null);
        final TextView txtSearch = dialogView.findViewById(R.id.txtSearch);
        // Añadimos la vista, el título, y los botonoes al modal.
        builder.setView(dialogView);
        builder.setTitle("Search nearby places");
        builder.setPositiveButton("OK", null);
        builder.setNegativeButton("Cancel", null);
        alertDialog = this.builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                // Botón que genera la consuita dependiendo del query que el usuario haya ingresado.
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String query = txtSearch.getText().toString().trim();
                        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        stringBuilder.append("location="+latitude+","+longitude);
                        stringBuilder.append("&radius="+28000);
                        //stringBuilder.append("&rankby="+"distance");
                        //stringBuilder.append("&type="+"car_repair");
                        stringBuilder.append("&keyword="+query);
                        stringBuilder.append("&key="+key);
                        String url = stringBuilder.toString();
                        Snackbar.make(view, "Fetching results from query: " + query, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        Object[] dataTransfer;
                        dataTransfer = new Object[2];
                        dataTransfer[0] = map;
                        dataTransfer[1] = url;
                        // Tarea Asíncrona que nos permite ejecutar una petición a los servidores de Google
                        // en segundo plano mientras estamos en la actividad principal con el modal
                        // y que devuelve un JSON de los resultados.
                        new GetNearbyPlaces().execute(dataTransfer);
                    }
                });
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation(map);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //map.addMarker(new MarkerOptions().position(latLng).draggable(true));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        //Toast.makeText(this, "onMarkerDragStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        //Toast.makeText(this, "onMarkerDrag", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //latitude = marker.getPosition().latitude;
        //longitude = marker.getPosition().longitude;
    }

    @Override
    public void onLocationChanged(Location location) {
        //getCurrentLocation(map);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void setPreferences(Context context, Boolean splash) {
        SharedPreferences spSplash = context.getSharedPreferences("SPLASH_SCREEN", MODE_PRIVATE);
        SharedPreferences.Editor editor;
        editor = spSplash.edit();
        editor.putBoolean("FIRST_TIME", splash);
        editor.apply();
    }

    public boolean getPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SPLASH_SCREEN", MODE_PRIVATE);
        return sharedPreferences.getBoolean("FIRST_TIME", true);
    }

    // Clase que ejecuta la petición a los servidores de Google.
    @SuppressLint("StaticFieldLeak")
    class GetNearbyPlaces extends AsyncTask<Object, String, String> {
        // Variables para un nuevo mapa.
        GoogleMap nMap;
        String url;
        InputStream is;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String data;
        // Método que se ejcuta en segundo plano que recibe el query del usuario y lo envía por http para la consulta.
        @Override
        protected String doInBackground(Object... objects) {
            nMap = (GoogleMap) objects[0];
            url = (String) objects[1];
            try {
                URL url_work = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url_work.openConnection();
                httpURLConnection.connect();
                is = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(is));
                String line = "";
                stringBuilder = new StringBuilder();
                while((line = bufferedReader.readLine())!=null) {
                    stringBuilder.append(line);
                }
                data = stringBuilder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        // Método que al obtener los resultados los interpreta y los muestra convirtiéndolos en puntos en el mapa.
        @Override
        protected void onPostExecute(String s) {
            // Limpiamos el mapa por si habían puntos anteriores.
            nMap.clear();
            // Agregar nuestra ubicación actual nuevamente.
            getCurrentLocation(nMap);
            // Se cierra el modal de búsqueda.
            alertDialog.dismiss();
            try {
                // Variables para almacenar el objeto JSON que es devuelto por Google.
                JSONObject parenObject = new JSONObject(s);
                JSONArray resultsArray = parenObject.getJSONArray("results");

                // Instrucción "for" que itera el objeto para obtener en variables los campos necesarios a mostrar al usuario.
                for (int i = 0; i < resultsArray.length(); i++) {

                    JSONObject jsonObject = resultsArray.getJSONObject(i);
                    JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");
                    String lat = locationObj.getString("lat");
                    String lng = locationObj.getString("lng");

                    JSONObject nameObject = resultsArray.getJSONObject(i);
                    String name = nameObject.getString("name");

                    // Variables de longitud y latitud que se le pasan al nuevo mapa
                    // para crear los puntos de las ubicaciones encontradas
                    LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.title(name);
                    markerOptions.position(latLng);

                    // Se añaden las ubicaciones y sus datos al mapa
                    // y se hace un zoom inverso para mayor comodidad.
                    nMap.addMarker(markerOptions);
                    zoomLevel = 12.0f;
                    nMap.animateCamera(CameraUpdateFactory.zoomTo(zoomLevel));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
