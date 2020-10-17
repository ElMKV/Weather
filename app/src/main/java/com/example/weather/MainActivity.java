package com.example.weather;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LocationListener {
    LineGraphSeries<DataPoint> series;
    private static final int REQUEST_LOCATION = 1;
    String KEY = "14fcc3726972a2ee7e67a8a0a98c87d8";
    String UNITS = "metric";
    String LANG = "ru";

    String codeWeather;
    Double lat, lon;

    TextView nameTextView, sunSetTextView, sunRiseTextView, feelsLikeTextView, cloudsTextView, textViewPower, textViewHumidity,
            textViewCloudsPer, textViewVisibility, textViewUviIndex, textViewWindSpeed, textViewWindDeg;
    RecyclerView recyclerView;
    SeekBar seekBarUvi;
    ImageView imageViewIcon;
    WeatherWeekAdapter adapter;
    GraphView graph;

    final String TAG = "GPS";
    private final static int ALL_PERMISSIONS_RESULT = 101;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    LocationManager locationManager;
    Location loc;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    boolean isGPS = false;
    boolean isNetwork = false;
    boolean canGetLocation = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getConectionCheck();
        getView();

    }

    private void getConectionCheck() {
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);
        isGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        if (!isGPS && !isNetwork) {
            Log.d(TAG, "Connection off");
            showSettingsAlert();
            getLastLocation();
        } else {
            Log.d(TAG, "Connection on");
            // check permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (permissionsToRequest.size() > 0) {
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                            ALL_PERMISSIONS_RESULT);
                    Log.d(TAG, "Permission requests");
                    canGetLocation = false;
                }
            }

            // get location
            getLocation();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged");
        updateUI(location);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}

    @Override
    public void onProviderEnabled(String s) {
        getLocation();
    }

    @Override
    public void onProviderDisabled(String s) {
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getLocation() {
        try {
            if (canGetLocation) {
                Log.d(TAG, "Can get location");
                if (isGPS) {
                    // from GPS
                    Log.d("LOC", "GPS on");
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (loc != null)
                            Log.d("LOC", "loc != null");

                        updateUI(loc);
                    }
                } else if (isNetwork) {
                    // from Network Provider
                    Log.d("LOC", "NETWORK_PROVIDER on");
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (loc != null)
                            updateUI(loc);
                    }
                } else {
                    loc.setLatitude(0);
                    loc.setLongitude(0);
                    updateUI(loc);
                }
            } else {
                Log.d("LOC", "Can't get location");
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getLastLocation() {
        try {
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, false);
            Location location = locationManager.getLastKnownLocation(provider);
            Log.d(TAG, provider);
            Log.d(TAG, location == null ? "NO LastLocation" : location.toString());
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case ALL_PERMISSIONS_RESULT:
                Log.d(TAG, "onRequestPermissionsResult");
                for (String perms : permissionsToRequest) {
                    if (!hasPermission(perms)) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                            showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(
                                                new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                }
                            });
                            return;
                        }
                    }
                } else {
                    Log.d(TAG, "No rejected permissions.");
                    canGetLocation = true;
                    getLocation();
                }
                break;
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS is not Enabled!");
        alertDialog.setMessage("Do you want to turn on GPS?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void updateUI(Location loc) {

        Log.d("LOC", "updateUI");
        lat = loc.getLatitude();
        lon = loc.getLongitude();
        loadInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    private void getView() {
        Log.d("LOGG", "getView");
        nameTextView = findViewById(R.id.name);
        feelsLikeTextView = findViewById(R.id.feelsLikeTextView);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        sunSetTextView = findViewById(R.id.sunSetTextView);
        sunRiseTextView = findViewById(R.id.sunRiseTextView);
        cloudsTextView = findViewById(R.id.cloudsTextView);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        recyclerView = findViewById(R.id.recyclerView);
        textViewPower = findViewById(R.id.textViewPower);
        textViewHumidity = findViewById(R.id.textViewHumidity);
        textViewCloudsPer = findViewById(R.id.textViewCloudsPer);
        textViewVisibility = findViewById(R.id.textViewVisibility);
        seekBarUvi = findViewById(R.id.seekBarUvi);
        textViewWindSpeed = findViewById(R.id.textViewWindSpeed);
        textViewWindDeg = findViewById(R.id.textViewWindDeg);
        textViewUviIndex = findViewById(R.id.textViewUviIndex);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

    }



    @Override
    protected void onRestart() {
        Log.d("LOGG", "onRestart");
        super.onRestart();
        getConectionCheck();


    }




    private void loadInfo() {
        NetworkService.getInstance()
                .getCityWithName()
                .GetSityInf(lat, lon, LANG, KEY, UNITS)
                .enqueue(new Callback<Info>() {
                    @Override
                    public void onResponse(Call<Info> call, Response<Info> response) {

                        Info info = response.body();
                        getInfoAndInitView(info);

                    }

                    @Override
                    public void onFailure(Call<Info> call, Throwable t) {
                        Log.d("LOG", "Ошибка " + t.toString());
                        feelsLikeTextView.setText("-");
                        Toast.makeText(getApplicationContext(), "Проверьте подключение к интернету.", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void getInfoAndInitView(Info info) {

        Log.d("log", info.getTimezone());
        Log.d("LOGG", "getInfoAndInitView");


        nameTextView.setText("Регион - " + info.getTimezone());

        DecimalFormat df = new DecimalFormat("###");

        feelsLikeTextView.setText(df.format(info.getCurrent().getFeelsLike()) + "\u2103");
        cloudsTextView.setText(info.getCurrent().getWeather().get(0).getDescription());
        codeWeather = info.getCurrent().getWeather().get(0).getIcon();

        Integer unixSunRise = info.getCurrent().getSunrise();
        Integer unixSunSet = info.getCurrent().getSunset();
        Date dateSunRise = new Date(unixSunRise * 1000L);
        Date dateSunSet = new Date(unixSunSet * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");


        sunRiseTextView.setText("Восход: " + sdf.format(dateSunRise));
        sunSetTextView.setText("Закат: " + sdf.format(dateSunSet));

        Picasso.with(getApplicationContext())
                .load("http://openweathermap.org/img/wn/" + codeWeather + "@2x.png")
                .into(imageViewIcon);
        ArrayList<Daily> dailies = (ArrayList<Daily>) info.getDaily();
        adapter = new WeatherWeekAdapter(MainActivity.this, dailies);
        recyclerView.setAdapter(adapter);

        textViewPower.setText("Давление - " + Math.ceil( info.getCurrent().getPressure()/1.333) + " мм.рт.ст.");//получаем мм.рт.ст.
        textViewHumidity.setText("Влажность - " + info.getCurrent().getHumidity().toString());
        textViewCloudsPer.setText(info.getCurrent().getClouds().toString() + " % облачности");
        textViewVisibility.setText(info.getCurrent().getVisibility().toString() + " метров видимость" );
        textViewWindSpeed.setText("Скорость ветра - " + info.getCurrent().getWindSpeed().toString() + " м/с");
        Log.d("LOGG" ,info.getCurrent().getWindSpeed().toString());
        double uvi = Math.ceil(info.getCurrent().getUvi());
        int valueUvi = (int) uvi;
        textViewUviIndex.setText("Интенсивность УФ излучения - " + valueUvi);
        seekBarUvi.setProgress(valueUvi);
        seekBarUvi.setEnabled(false);
        textViewWindDeg.setText("Направление ветра - " + GetComapWing(info.getCurrent().getWindDeg()));

        initGraphView(info);

    }

    static String GetComapWing(int index) {
        String wing = null;

        if (index == 360 || index == 0){
            wing = "С";
        }
        if (index>0 & index<90){
            wing = "СВ";
        }
        if (index==90){
            wing = "Юг";
        }
        if (index>90 & index<180){
            wing = "ЮВ";
        }
        if (index == 180){
            wing = "Ю";
        }
        if (index>180 & index<270){
            wing = "ЮЗ";
        }
        if (index > 270 & index <360){
            wing = "СЗ";
        }

        return wing;
    }

    private void initGraphView(Info info) {
        graph = (GraphView) findViewById(R.id.graph);

        if (info.getHourly().size() > 0) {
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
            for (int i = 0; i < info.getHourly().size(); i++) {
                DataPoint point = new DataPoint(i, info.getHourly().get(i).getTemp());
                series.appendData(point, true, info.getHourly().size());
            }
            graph.getViewport().setScrollable(true); // enables horizontal scrolling
            graph.getViewport().setScrollableY(true); // enables vertical scrolling
            graph.getViewport().setScalable(true); // enables horizontal zooming and scrolling
            graph.getViewport().setScalableY(true); // enables vertical zooming and scrolling
            series.setColor(Color.WHITE);
            graph.getGridLabelRenderer().setGridColor(Color.WHITE);

            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.WHITE);
            graph.getGridLabelRenderer().reloadStyles();

            DateFormat dateFormat = new SimpleDateFormat("HH");
            Date date = new Date();

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(Double.parseDouble(dateFormat.format(date)));
            graph.getViewport().setMaxX(Double.parseDouble(dateFormat.format(date)) + 4.0);

            graph.addSeries(series);
        }

    }

}