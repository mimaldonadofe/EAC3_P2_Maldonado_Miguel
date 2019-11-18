package miguelmaldonado.ioc.eac3_p2_maldonado_miguel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1 ;
    private static final double LONGITUD_MARCADOR = 2.008749;
    private static final double LATITUD_MARCADOR = 41.398310;
    private GoogleMap mMap;

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
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        inicialitzaMapa();

        // Crea el botó de centrar a la posició de l'usuari i el cercle blau a la ubicació.
        enableMyLocation(mMap);

        // Aprofitem el cercle blau amb la ubicació de l'usuari per afegir-hi un onclicklistener
        // que mostrarà per pantalla un missatge amb la latitud i longitud.
        mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
            @Override
            public void onMyLocationClick(@NonNull Location location) {
                String missatge = "La meva localització és (" + location.getLatitude()+
                        ", " + location.getLongitude() + ")";
                Toast.makeText(getApplicationContext(), missatge,Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Afegeix un marcador a una posició al iniciar-se el mapa.
     */
    private void inicialitzaMapa() {
        LatLng poble = new LatLng(LATITUD_MARCADOR,LONGITUD_MARCADOR);
        mMap.addMarker(new MarkerOptions().position(poble).title(getString(R.string.marcador)));
    }

    /**
     * Comprova si existeixen permissos per a poder habilitar la ubicació de l'usuari. Si
     * existeix s'habilita el location layer. Al mapa apareix el botó de centrar en
     * la ubicació actual de l'usuari. Si no existeix el permís es demana a l'usuari.
     * @param map
     */
    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * Comprova si l'usuari dona permís. EN aquest cas si l'usuari accepta donar permís de
     * localització aleshores s'habilita el location layer.
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation(mMap);
                    break;
                }
        }
    }
}
