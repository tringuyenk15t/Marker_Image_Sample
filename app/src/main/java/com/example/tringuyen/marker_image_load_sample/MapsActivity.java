package com.example.tringuyen.marker_image_load_sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.tringuyen.marker_image_load_sample.models.DirectionResponse;
import com.example.tringuyen.marker_image_load_sample.network.Google_direction;
import com.example.tringuyen.marker_image_load_sample.network.ServiceGenerator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Source: https://github.com/bumptech/glide/issues/290
 * Create by Tri Nguyen
 */
public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private final Map<Marker, Bitmap> images = new HashMap<>();

    private String name = "";
    private String url;
    private String id;
    private LatLng sushi1;
    private LatLng sushi2;
    private Google_direction google_direction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.fpoli
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        ImageButton fab = (ImageButton) findViewById(R.id.fab);
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        sushi1 = new LatLng(10.7718488,106.6576495);
        sushi2 = new LatLng(10.775727,106.704895);
        //TODO customize marker icon here

//        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_light_green3);

        mMap.addMarker(new MarkerOptions()
                .position(sushi1).title("1")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));
        mMap.addMarker(new MarkerOptions()
                .position(sushi2).title("2")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        builder.include(sushi1);
        builder.include(sushi2);

        LatLngBounds bounds = builder.build();

//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sushi1, 12));

        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowClickListener(this);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPolyline();
            }
        });
    }

    /**
     * Draw direction line from start location to destination location
     */
    private void setPolyline()
    {
        google_direction = ServiceGenerator.createService(Google_direction.class);
        Call<DirectionResponse> call = google_direction.getDirections("10.7718488,106.6576495","10.775727,106.704895");
        call.enqueue(new Callback<DirectionResponse>() {
            @Override
            public void onResponse(Call<DirectionResponse> call, Response<DirectionResponse> response) {
                DirectionResponse result = response.body();
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(sushi1);

                List<LatLng> directionCoordinates =  decodePolyLine(result.getRoutes().get(0).getOverviewPolyline().getPoints());
                for (LatLng latLng : directionCoordinates)
                {
                    polylineOptions.add(latLng);
                }

                polylineOptions.add(sushi2);
                mMap.addPolyline(polylineOptions.width(15).color(getApplicationContext().getResources().getColor(R.color.colorPolyline)));

                //set camera zoom fit with start and end location
                LatLngBounds.Builder latLngBounds = new LatLngBounds.Builder();
                latLngBounds.include(sushi1);
                latLngBounds.include(sushi2);

                LatLngBounds bounds = latLngBounds.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,50)); // the second param is the offset from edges of the map in pixels

                //Disable Map Toolbar
                mMap.getUiSettings().setMapToolbarEnabled(false);
    //          //dissable zoom control
    //          mMap.getUiSettings().setZoomControlsEnabled(false);
            }

            @Override
            public void onFailure(Call<DirectionResponse> call, Throwable t) {
                Log.e("Error", t.toString());
            }
        });
    }

    /**
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    /**
     *load window info when marker was clicked
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(final Marker marker) {

        View v = getLayoutInflater().inflate(R.layout.customzied_marker,null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        final ImageView iv = (ImageView) v.findViewById(R.id.photo);
        switch (marker.getTitle())
        {
            case "1":
                name = "Sushi 1";
                url = "https://lh4.googleusercontent.com/-sNgDHSsXQq0/Vs8EPkWn2MI/AAAAAAAAdmw/_3nab6vIYkotFCYs4GA0-E6ilMNFtGonw/s1600-w300-h300/";
                id = "3a12b660341076e2d5910d065908da5c78319fa3";
                break;
            case "2":
                name = "Sushi 2";
                url  = "https://lh4.googleusercontent.com/-mAK_87HKD4k/VyMyJMw825I/AAAAAAABfNU/WtJ5sGGnHagyRto3t69R2BvXs1CdTOvVgCLIB/s1600-w300-h300/";
                id = "73216385848be6411ac509ff5eb53ef4f7a5923d";
                break;

            default:
                name = "";
                url = "";
                id = "";
                break;
        }
        tv.setText(name);
        Bitmap image = images.get(marker);
        if (image == null)
        {
            Glide.with(this).load(url).asBitmap().dontAnimate().into(new MarkerImageTarget(marker));
            iv.setImageResource(R.drawable.image);
            return v;
        }
        else
        {
            iv.setImageBitmap(image);
            return v;
        }
    }

    /**
     * Start detail info activity and pass location id into it
     * @param marker
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(this,Marker_detail.class);
        intent.putExtra("ID",id);
        this.startActivity(intent);
    }

    /**
     * Show windowinfo when image was completely loaded
     */
    private class MarkerImageTarget extends SimpleTarget<Bitmap>
    {
        Marker marker;
        MarkerImageTarget (Marker marker)
        {
            super(250,150);// otherwise Glide will load original sized bitmap which is huge
            this.marker = marker;
        }
        @Override
        public void onLoadCleared(Drawable placeholder) {
            images.remove(marker); // clean up previous image, it became invalid
            // don't call marker.showInfoWindow() to update because this is most likely called from Glide.into()
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            images.put(marker,resource);
            marker.showInfoWindow();
        }
    }

    /**
     * Decode overview_polyline to a list of LatLng.
     * @param poly
     * @return decodeds
     */
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }

}
