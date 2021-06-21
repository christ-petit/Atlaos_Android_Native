package org.atlaos.app.ui.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.atlaos.app.R;
import org.atlaos.app.model.DescribeRecordModel;
import org.jetbrains.annotations.NotNull;

public class LocationPickerFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {
    private static final String TAG="LocationPickerFragment";
    private GoogleMap googleMap;
    private Marker marker=null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_location_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
     this.googleMap=googleMap;
     if (marker == null) marker= googleMap.addMarker(new MarkerOptions().draggable(true).position(new LatLng(18.098621982995702, 104.18878304227917)));

        LatLng sw=new LatLng(13.98888603252886d,99.76288703671828d);
        LatLng ne=new LatLng(22.401588208878795d,107.73631432314828d);
        LatLngBounds laosBoundary=new LatLngBounds(sw,ne);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(laosBoundary,10));
        googleMap.setOnMarkerDragListener(this);
         DescribeRecordModel model=new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class);
         if (model.getLatLng() != null) {
             marker.setPosition(model.getLatLng());
         }

    }


    @Override
    public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

    }

    @Override
    public void onMarkerDrag(@NonNull @NotNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
      new ViewModelProvider(requireActivity()).get(DescribeRecordModel.class).setLatLng(marker.getPosition());

    }
}