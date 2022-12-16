package com.example.lostbutfound;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.IconCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.GeoObjectCollection;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Circle;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.GeoObjectTapEvent;
import com.yandex.mapkit.layers.GeoObjectTapListener;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CircleMapObject;
import com.yandex.mapkit.map.GeoObjectSelectionMetadata;
import com.yandex.mapkit.map.InputListener;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.image.ImageProvider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class Yandex extends AppCompatActivity implements GeoObjectTapListener, InputListener {

    private MapView mapView;
    private MapObjectCollection mapObjects;

    private Point CIRCLE_CENTER = new Point(49.8019, 73.1021);


    private List<Point> clusterCenters = Arrays.asList(
            new Point(49.8019, 73.1021),
            new Point(51.1801, 71.446),
            new Point(43.2567, 76.9286)
    );

    GeoObjectTapEvent geoObjectTapEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.setApiKey("5a69f91a-a3b1-46fd-b0dc-ba5a13d750ad");
        MapKitFactory.initialize(this);

        setContentView(R.layout.activity_yandex);
        mapView = (MapView)findViewById(R.id.mapView);
        mapView.getMap().move(
                new CameraPosition(new Point(49.8019,
                        73.1021), 11.0f,
                        0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 0),
                null);

        mapObjects = mapView.getMap().getMapObjects();

//        PlacemarkMapObject karaganda = mapObjects
//                .addPlacemark(new Point(49.8019, 73.1021));

        PlacemarkMapObject astana = mapObjects
                .addPlacemark(new Point(51.1801, 71.446));
        astana.setText("aaaaaaaaaaaaaaaa");

        PlacemarkMapObject almata = mapObjects
                .addPlacemark(new Point(43.2567, 76.9286));

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(27.525773, 53.89079, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder(
                        "Адрес:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                                    "\n");
                }
                Utility.showToast(this, strReturnedAddress.toString());
            } else {
                Utility.showToast(this, "Нет адресов!");
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Utility.showToast(this, "Не могу получить адрес!");
        }
        String address = "Москва, ул. Льва Толстого, 16";

        createTappableCircle();
        mapView.getMap().addTapListener(this);
        mapView.getMap().addInputListener(this);
    }

    private MapObjectTapListener circleMapObjectTapListener = new MapObjectTapListener() {
        @Override
        public boolean onMapObjectTap(MapObject mapObject, Point point) {
            if (mapObject instanceof CircleMapObject) {
                CircleMapObject circle = (CircleMapObject)mapObject;

                float randomRadius = 100.0f + 50.0f * new Random().nextFloat();

                Circle curGeometry = circle.getGeometry();
                Circle newGeometry = new Circle(curGeometry.getCenter(), randomRadius);
                circle.setGeometry(newGeometry);

                Object userData = circle.getUserData();
                if (userData instanceof CircleMapObjectUserData) {
                    CircleMapObjectUserData circleUserData = (CircleMapObjectUserData)userData;

//                    Utility.showToast(Yandex.this, "aaaaaaaf" + 1);
                    Toast toast = Toast.makeText(
                            getApplicationContext(),
                            "Circle with id " + circleUserData.id + " and description '"
                                    + circleUserData.description + "' tapped",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            return true;
        }
    };

    private class CircleMapObjectUserData {
        final int id;
        final String description;

        CircleMapObjectUserData(int id, String description) {
            this.id = id;
            this.description = description;
        }
    }

    private void createTappableCircle() {
        CircleMapObject circle = mapObjects.addCircle(
                new Circle(CIRCLE_CENTER, 20), Color.GREEN, 2, Color.RED);
        circle.setZIndex(10.0f);
        circle.setUserData(new CircleMapObjectUserData(42, "Tappable circle"));

        circle.addTapListener(circleMapObjectTapListener);

        CIRCLE_CENTER = new Point(51.1801, 71.446);
        circle = mapObjects.addCircle(
                new Circle(CIRCLE_CENTER, 10), Color.YELLOW, 2, Color.BLACK);
        circle.setZIndex(10.0f);
        circle.setUserData(new CircleMapObjectUserData(2, "lol"));

        // Client code must retain strong reference to the listener.
        circle.addTapListener(circleMapObjectTapListener);
    }

    @Override
    protected void onStop() {
        mapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }

    @Override
    public boolean onObjectTap(@NonNull GeoObjectTapEvent geoObjectTapEvent) {
        final GeoObjectSelectionMetadata selectionMetadata = geoObjectTapEvent
                .getGeoObject()
                .getMetadataContainer()
                .getItem(GeoObjectSelectionMetadata.class);

        if (selectionMetadata != null) {
            mapView.getMap().selectGeoObject(selectionMetadata.getId(), selectionMetadata.getLayerId());
        }

        return selectionMetadata != null;
    }

    @Override
    public void onMapTap(@NonNull Map map, @NonNull Point point) {

        mapView.getMap().deselectGeoObject();
    }

    @Override
    public void onMapLongTap(@NonNull Map map, @NonNull Point point) {

    }
}