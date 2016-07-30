package com.example.tringuyen.marker_image_load_sample;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

/**
 * Source: https://github.com/bumptech/glide/issues/290
 * Create by Tri Nguyen
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.InfoWindowAdapter {

    private GoogleMap mMap;
    private final Map<Marker, Bitmap> images = new HashMap<>();

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
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng sushi1 = new LatLng(10.7718488,106.6576495);
        LatLng sushi2 = new LatLng(10.7786628,106.662548);
        mMap.addMarker(new MarkerOptions().position(sushi1).title("1"));
        mMap.addMarker(new MarkerOptions().position(sushi2).title("2"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(sushi1);
        builder.include(sushi2);

        LatLngBounds bounds = builder.build();
        mMap.setInfoWindowAdapter(this);
//        mMap.moveCamera(new CameraUpdate());
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sushi1, 15));
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
     *
     * @param marker
     * @return
     */
    @Override
    public View getInfoContents(final Marker marker) {

        View v = getLayoutInflater().inflate(R.layout.customzied_marker,null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        final ImageView iv = (ImageView) v.findViewById(R.id.photo);
        String name = "";
        String url;

        switch (marker.getTitle())
        {
            case "1":
                name = "Sushi 1";
                url = "https://lh4.googleusercontent.com/-sNgDHSsXQq0/Vs8EPkWn2MI/AAAAAAAAdmw/_3nab6vIYkotFCYs4GA0-E6ilMNFtGonw/s1600-w300-h300/";
                break;
            case "2":
                name = "Sushi 2";
                url  = "https://lh4.googleusercontent.com/-mAK_87HKD4k/VyMyJMw825I/AAAAAAABfNU/WtJ5sGGnHagyRto3t69R2BvXs1CdTOvVgCLIB/s1600-w300-h300/";
                break;

            default:
                name = "";
                url = "";
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
}
