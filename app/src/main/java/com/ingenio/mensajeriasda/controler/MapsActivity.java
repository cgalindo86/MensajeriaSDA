package com.ingenio.mensajeriasda.controler;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentActivity;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Coordenadas;
import com.ingenio.mensajeriasda.service.Conexion;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker marcador,marcador2,marcador3,marcador4,marcador5,marcadorInicio,marcadorFinal;
    double lat = 0.0;
    double lng = 0.0;
    double nLat = 0.0;
    double nLng = 0.0;
    String mensaje1="", ruta="", pagolink="", medio="";
    String direccion = "", rutaSeleccionada = "";
    private Vibrator vibrator;
    LinearLayout caja,caja0,caja2,caja3,caja4;
    double nDistan = 0.0;
    Boolean valor = false;
    Button btn,rutaA,rutaB,rutaC,rutaD, rutaE,volver1,volver2,volver3,volver4,selRuta,selSaldo,recarga;
    Button recarga3,recarga12,recarga24,niubiz,plin,yape,pagar;
    com.github.nkzawa.socketio.client.Socket socket;
    Context context;
    TextView saldo;
    int cuenta=0,cuentaA=0;
    double[][] matrizA = {
            {-12.084160, -77.089899},{-12.084116, -77.089859},{-12.084149, -77.089803},
            {-12.084175, -77.089752},{-12.084260, -77.089556},{-12.084300, -77.089482},
            {-12.084327, -77.089431},{-12.084394, -77.089300}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contenedor);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        saldo = (TextView) findViewById(R.id.saldo);
        Conexion conexion = new Conexion();
        Alumno alumno = new Alumno();
        ruta = conexion.getUrl(getApplicationContext());
        ruta = ruta + "/controler/consultaAlumno.php?accionget=8&alumnoget="+alumno.getAlumnoElegido(getApplicationContext());
        Log.e("rutasaldo",ruta);
        Lee lee = new Lee();
        lee.execute(ruta);
////-12.084032, -77.089987


        caja0 = (LinearLayout) findViewById(R.id.caja0);
        caja = (LinearLayout) findViewById(R.id.caja);
        caja2 = (LinearLayout) findViewById(R.id.caja2);
        caja3 = (LinearLayout) findViewById(R.id.caja3);
        caja4 = (LinearLayout) findViewById(R.id.caja4);
        caja0.setVisibility(View.VISIBLE);
        caja.setVisibility(View.GONE);
        caja2.setVisibility(View.GONE);
        caja3.setVisibility(View.GONE);
        caja4.setVisibility(View.GONE);

        btn = (Button) findViewById(R.id.cancelar);
        volver1 = (Button) findViewById(R.id.volver1);
        volver2 = (Button) findViewById(R.id.volver2);
        volver3 = (Button) findViewById(R.id.volver3);
        volver4 = (Button) findViewById(R.id.volver4);
        selSaldo = (Button) findViewById(R.id.selSaldo);
        selRuta = (Button) findViewById(R.id.selRuta);
        recarga = (Button) findViewById(R.id.recarga);
        recarga3 = (Button) findViewById(R.id.recarga3);
        recarga12 = (Button) findViewById(R.id.recarga12);
        recarga24 = (Button) findViewById(R.id.recarga24);

        yape = (Button) findViewById(R.id.yape);
        plin = (Button) findViewById(R.id.plin);
        niubiz = (Button) findViewById(R.id.niubiz);
        pagar = (Button) findViewById(R.id.pagar);

        recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.VISIBLE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.GONE);
            }
        });

        recarga3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagolink="3";
                Limpiar2();
                recarga3.setBackgroundResource(R.drawable.bordec1);
            }
        });

        recarga12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagolink="12";
                Limpiar2();
                recarga12.setBackgroundResource(R.drawable.bordec1);
            }
        });

        recarga24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagolink="24";
                Limpiar2();
                recarga24.setBackgroundResource(R.drawable.bordec1);
            }
        });

        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Limpiar2();

                ruta = conexion.getUrl(getApplicationContext());
                ruta = ruta + "/controler/consultaAlumno.php?accionget=9&saldoget="+pagolink+"&alumnoget="+alumno.getAlumnoElegido(getApplicationContext())+"&ppffget="+alumno.getPPFFDni(getApplicationContext())+"&medioget="+medio+"&rutaget="+rutaSeleccionada;
                Log.e("rutasaldo",ruta);
                Lee2 lee2 = new Lee2();
                lee2.execute(ruta);
            }
        });



        selSaldo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.VISIBLE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.GONE);
                Alumno alumno = new Alumno();
                ruta = conexion.getUrl(getApplicationContext());
                ruta = ruta + "/controler/consultaAlumno.php?accionget=8&alumnoget="+alumno.getAlumnoElegido(getApplicationContext());
                Log.e("rutasaldo",ruta);
                Lee lee = new Lee();
                lee.execute(ruta);
            }
        });

        selRuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.VISIBLE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.GONE);
            }
        });

        volver1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.VISIBLE);
                caja4.setVisibility(View.GONE);
            }
        });

        volver2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.VISIBLE);
                caja4.setVisibility(View.GONE);
            }
        });

        volver3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.VISIBLE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.GONE);
            }
        });

        volver4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.VISIBLE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.GONE);
            }
        });

        niubiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medio="link";
                pagar.setText("PAGAR");
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.VISIBLE);
            }
        });

        yape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medio="yape";
                pagar.setText("YAPEAR");
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.VISIBLE);
            }
        });

        plin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medio="plin";
                pagar.setText("PLINEAR");
                caja.setVisibility(View.GONE);
                caja2.setVisibility(View.GONE);
                caja3.setVisibility(View.GONE);
                caja0.setVisibility(View.GONE);
                caja4.setVisibility(View.VISIBLE);
            }
        });

        rutaA = (Button) findViewById(R.id.rutaA);
        rutaB = (Button) findViewById(R.id.rutaB);
        rutaC = (Button) findViewById(R.id.rutaC);
        rutaD = (Button) findViewById(R.id.rutaD);
        rutaE = (Button) findViewById(R.id.rutaE);
        btn.setVisibility(View.GONE);

        Limpiar();

        rutaA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rutaSeleccionada="A";
                Limpiar();
                rutaA.setBackgroundResource(R.drawable.bordec1);
                ConsultaEstadoMovilidad();
                MarcadoresRutaA();
            }
        });

        rutaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rutaSeleccionada="B";
                Limpiar();
                rutaB.setBackgroundResource(R.drawable.bordec1);
                ConsultaEstadoMovilidad();
                MarcadoresRutaB();
            }
        });

        rutaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rutaSeleccionada="C";
                Limpiar();
                rutaC.setBackgroundResource(R.drawable.bordec1);
                ConsultaEstadoMovilidad();
                MarcadoresRutaC();
            }
        });

        rutaD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rutaSeleccionada="D";
                Limpiar();
                rutaD.setBackgroundResource(R.drawable.bordec1);
                ConsultaEstadoMovilidad();
                MarcadoresRutaD();
            }
        });

        rutaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rutaSeleccionada="E";
                Limpiar();
                rutaE.setBackgroundResource(R.drawable.bordec1);
                ConsultaEstadoMovilidad();
                MarcadoresRutaE();
            }
        });

        context = getApplicationContext();

        IO.Options opts = new IO.Options();
        opts.forceNew = true;
        opts.reconnection = false;
        try {

            socket = IO.socket("http://sdavirtualroom.dyndns.org:8013");

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        socket.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Log.e("Uno", "Ingreso");
        mapFragment.getMapAsync(this);

        Button btn2 = (Button) findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miUbicacion();
            }
        });

        socket.on("movilidad estado notifica", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Handler nHandler = new Handler(getMainLooper());
                nHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"recibe info 01",Toast.LENGTH_LONG).show();
                        JSONObject data = (JSONObject) args[0];
                        String ruta2,asunto2,estado2;
                        try {
                            ruta2 = data.getString("ruta");
                            asunto2 = data.getString("asunto");
                            estado2 = data.getString("estado");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                        String texto = "";
                        if(ruta2.equals(rutaSeleccionada)){
                            if(estado2.equals("Finalizado")){
                                texto = "La ruta "+ruta2+" no está operativa en estos momentos";
                            } else if(estado2.equals("cancelada")){
                                texto = "La ruta "+ruta2+" ha sido cancelada el día de hoy";
                            } else {
                                texto = "La ruta "+ruta2+" se encuentra en marcha";
                            }
                        }


                        Toast.makeText(getApplicationContext(),texto,Toast.LENGTH_LONG).show();


                    }
                });
            }
        });

        socket.on("coordenadas2", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Handler nHandler = new Handler(getMainLooper());
                nHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(getApplicationContext(),"recibe info 01",Toast.LENGTH_LONG).show();
                        JSONObject data = (JSONObject) args[0];
                        String laruta;
                        double lat2, lng2;
                        try {
                            lat2 = Double.parseDouble(data.getString("lat"));
                            lng2 = Double.parseDouble(data.getString("lng"));
                            laruta = data.getString("ruta");
                            //lng2 = Double.parseDouble("-77.0260600");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            return;
                        }
                        Log.e("antes",lat2+" - "+lng2);
                        //Toast.makeText(context,lat2+" - "+lng2+" - "+laruta+" - "+rutaSeleccionada,Toast.LENGTH_LONG).show();
                        if(laruta.equals("A") && laruta.equals(rutaSeleccionada)){
                            AgregarMarcador2(lat2,lng2);
                        } else if(laruta.equals("B") && laruta.equals(rutaSeleccionada)){
                            AgregarMarcador3(lat2,lng2);
                        } else if(laruta.equals("C") && laruta.equals(rutaSeleccionada)){
                            AgregarMarcador4(lat2,lng2);
                        } else if(laruta.equals("D") && laruta.equals(rutaSeleccionada)){
                            AgregarMarcador5(lat2,lng2);
                        } else if(laruta.equals("E") && laruta.equals(rutaSeleccionada)){
                            AgregarMarcador5(lat2,lng2);
                        }


                    }
                });
            }
        });

        //miUbicacion();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.e("Dos", "Ingreso");
        miUbicacion();

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

                mMap.addMarker(new MarkerOptions().position(point).title(
                        point.toString()));

                nLat = point.latitude;
                nLng = point.longitude;

            }
        });
    }

    //activar los servicios del gps cuando esten apagados
    public void locationStart() {
        Log.e("Tres", "Ingreso");
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Log.e("Cuatro", "Ingreso");
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                    direccion = (DirCalle.getAddressLine(0));
                }
                Log.e("direccion",direccion);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //agregar el marcador en el mapa
    private void AgregarMarcador(double lat, double lng) {
        Log.e("Cinco", "Ingreso");
        Log.e("Marcador", "Lat:" + lat + " Lng:" + lng);
        LatLng coordenadas = new LatLng(lat, lng);
        CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLngZoom(coordenadas, 19);
        if (marcador != null) marcador.remove();
        marcador = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.nino1)));
        cuenta++;
        if(cuenta==1){
            mMap.animateCamera(MiUbicacion);
        }

    }

    private void AgregarMarcador2(double lat, double lng) {
        Log.e("Cinco", "Ingreso");
        Log.e("Marcador", "Lat:" + lat + " Lng:" + lng);
        LatLng coordenadas2 = new LatLng(lat, lng);
        CameraUpdate MiUbicacion2 = CameraUpdateFactory.newLatLngZoom(coordenadas2, 19);
        if (marcador2 != null) marcador2.remove();
        marcador2 = mMap.addMarker(new MarkerOptions()
                .position(coordenadas2)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.autobus33)));
        mMap.animateCamera(MiUbicacion2);
    }

    private void AgregarMarcador3(double lat, double lng) {
        Log.e("Cinco", "Ingreso");
        Log.e("Marcador", "Lat:" + lat + " Lng:" + lng);
        LatLng coordenadas3 = new LatLng(lat, lng);
        CameraUpdate MiUbicacion3 = CameraUpdateFactory.newLatLngZoom(coordenadas3, 19);
        if (marcador3 != null) marcador3.remove();
        marcador3 = mMap.addMarker(new MarkerOptions()
                .position(coordenadas3)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.autobus33)));
        mMap.animateCamera(MiUbicacion3);

    }

    private void AgregarMarcador4(double lat, double lng) {
        Log.e("Cinco", "Ingreso");
        Log.e("Marcador", "Lat:" + lat + " Lng:" + lng);
        LatLng coordenadas3 = new LatLng(lat, lng);
        CameraUpdate MiUbicacion3 = CameraUpdateFactory.newLatLngZoom(coordenadas3, 19);
        if (marcador4 != null) marcador4.remove();
        marcador4 = mMap.addMarker(new MarkerOptions()
                .position(coordenadas3)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.autobus33)));
        mMap.animateCamera(MiUbicacion3);

    }

    private void AgregarMarcador5(double lat, double lng) {
        Log.e("Cinco", "Ingreso");
        Log.e("Marcador", "Lat:" + lat + " Lng:" + lng);
        LatLng coordenadas3 = new LatLng(lat, lng);
        CameraUpdate MiUbicacion3 = CameraUpdateFactory.newLatLngZoom(coordenadas3, 19);
        if (marcador5 != null) marcador5.remove();
        marcador5 = mMap.addMarker(new MarkerOptions()
                .position(coordenadas3)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.autobus33)));
        mMap.animateCamera(MiUbicacion3);

    }

    //actualizar la ubicacion
    private void ActualizarUbicacion(Location location) {
        Log.e("Seis 0", "Ingreso");
        if (location != null) {
            Log.e("Seis 1", "Ingreso");
            lat = location.getLatitude();
            lng = location.getLongitude();
            AgregarMarcador(lat, lng);

        } else {
            Log.e("ubicacion desactivada", "");
            /*double lat2 = Double.parseDouble("-12.0811734") ;
            double lng2 = Double.parseDouble("-77.1039348");
            AgregarMarcador(lat2,lng2);*/
        }

    }

    //control del gps
    LocationListener locListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.e("Siete", "Ingreso");
            ActualizarUbicacion(location);
            setLocation(location);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {
            mensaje1 = ("GPS Activado");
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String s) {
            mensaje1 = ("GPS Desactivado");
            locationStart();
            Mensaje();
        }
    };
    private static int PETICION_PERMISO_LOCALIZACION = 101;

    private void miUbicacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e("Ocho 1", "Ingreso");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PETICION_PERMISO_LOCALIZACION);
            return;
        } else {
            Log.e("Ocho 2", "Ingreso");
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            ActualizarUbicacion(location);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,0,locListener);
        }

    }

    public void Mensaje() {
        Log.e("Nueve","Ingreso");
        //Toast toast = Toast.makeText(this, mensaje1, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        //toast.show();
    }

    public class Lee extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MapsActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String empresa2 = getDatos(params[0]);
            return empresa2;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            String[] data = result.split("___");
            saldo.setText(data[0]);
            rutaSeleccionada = data[1];

        }
    }

    public class Lee2 extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MapsActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String empresa2 = getDatos(params[0]);
            return empresa2;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            saldo.setText(result);
            String ruta="";
            if(medio.equals("link")){
                if(pagolink.equals("3")){
                    ruta = "https://pagolink.niubiz.com.pe/pagoseguro/AsociacionEducativaSantoDomingoelApostol/1918768/info";

                } else if(pagolink.equals("12")){
                    ruta = "https://pagolink.niubiz.com.pe/pagoseguro/AsociacionEducativaSantoDomingoelApostol/1890895/info";

                } else if(pagolink.equals("24")){
                    ruta = "https://pagolink.niubiz.com.pe/pagoseguro/AsociacionEducativaSantoDomingoelApostol/1890902/info";

                }

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta));
                startActivity(intent);
            } else if(medio.equals("yape")){
                if(estaInstaladaAplicacion("com.bcp.innovacxion.yapeapp", getApplicationContext())){
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.bcp.innovacxion.yapeapp");
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"La app no está instalada",Toast.LENGTH_LONG).show();
                    Conexion conexion = new Conexion();
                    Alumno alumno = new Alumno();
                    ruta = conexion.getUrl(getApplicationContext());
                    ruta = ruta + "/controler/consultaAlumno.php?accionget=91&saldoget="+pagolink+"&alumnoget="+alumno.getAlumnoElegido(getApplicationContext())+"&ppffget="+alumno.getPPFFDni(getApplicationContext())+"&medioget="+medio+"&rutaget="+rutaSeleccionada;

                    Log.e("rutasaldo",ruta);
                    Lee lee = new Lee();
                    lee.execute(ruta);
                }
            } else {
                if(estaInstaladaAplicacion("pe.com.interbank.mobilebanking", getApplicationContext())){
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.bcp.innovacxion.yapeapp");
                    startActivity(launchIntent);
                } else if(estaInstaladaAplicacion("com.bbva.nxt_peru", getApplicationContext())){
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.bcp.innovacxion.yapeapp");
                    startActivity(launchIntent);
                } else if(estaInstaladaAplicacion("pe.com.scotiabank.blpm.android.client", getApplicationContext())){
                    Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.bcp.innovacxion.yapeapp");
                    startActivity(launchIntent);
                } else {
                    Toast.makeText(getApplicationContext(),"La app no está instalada",Toast.LENGTH_LONG).show();
                    Conexion conexion = new Conexion();
                    Alumno alumno = new Alumno();
                    ruta = conexion.getUrl(getApplicationContext());
                    ruta = ruta + "/controler/consultaAlumno.php?accionget=91&saldoget="+pagolink+"&alumnoget="+alumno.getAlumnoElegido(getApplicationContext())+"&ppffget="+alumno.getPPFFDni(getApplicationContext())+"&medioget="+medio+"&rutaget="+rutaSeleccionada;

                    Log.e("rutasaldo",ruta);
                    Lee lee = new Lee();
                    lee.execute(ruta);
                }
            }

        }
    }

    public class Lee3 extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(MapsActivity.this);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Leyendo....");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String empresa2 = getDatos(params[0]);
            return empresa2;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            saldo.setText(result);

            if(estaInstaladaAplicacion("com.bcp.innovacxion.yapeapp", getApplicationContext())){
                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.bcp.innovacxion.yapeapp");
                startActivity(launchIntent);
            } else {
                Toast.makeText(getApplicationContext(),"La app no está instalada",Toast.LENGTH_LONG).show();
            }

        }
    }

    public String getDatos(String entrada) {
        Log.e("entrada",entrada);
        URL alumUrl = null;
        String url2="";
        try{
            alumUrl = new URL(entrada);
            HttpURLConnection conn = (HttpURLConnection) alumUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            byte[] array = new byte[1000]; // buffer temporal de lectura.
            StringBuffer out = new StringBuffer();
            byte[] b = new byte[4096];
            for (int n; (n = is.read(b)) != -1;) {
                out.append(new String(b, 0, n, "UTF-8"));
            }
            String pot=new String(out.toString().getBytes("UTF-8"));
            url2=pot;
        }catch(IOException ex){
            ex.printStackTrace();
        }
        Log.e("Consulta",url2);
        return url2;
    }

    private boolean estaInstaladaAplicacion(String nombrePaquete, Context context) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_META_DATA);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("e",e+"");
            return false;
        }
    }

    void Limpiar(){
        rutaA.setBackgroundResource(R.drawable.bordec2);
        rutaB.setBackgroundResource(R.drawable.bordec2);
        rutaC.setBackgroundResource(R.drawable.bordec2);
        rutaD.setBackgroundResource(R.drawable.bordec2);
        rutaE.setBackgroundResource(R.drawable.bordec2);
    }

    void Limpiar2(){
        recarga3.setBackgroundResource(R.drawable.bordec2);
        recarga12.setBackgroundResource(R.drawable.bordec2);
        recarga24.setBackgroundResource(R.drawable.bordec2);
    }

    void ConsultaEstadoMovilidad(){
        String json = "{'ruta':'"+rutaSeleccionada+"','asunto':'"+"Estado movilidad "+rutaSeleccionada+"','estado':'"+""+"'}";

        try {

            JSONObject obj = new JSONObject(json);
            socket.emit("movilidad estado consulta",obj);
            Log.e("movilidad estado", obj.toString());

        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
        }
    }

    void MarcadoresRutaA(){
        mMap.clear();
        LatLng coordenadas = new LatLng(-12.084160, -77.089899);
        CameraUpdate MiUbicacionX = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        mMap.animateCamera(MiUbicacionX);
        marcadorInicio = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084160, -77.089899), new LatLng(-12.084124, -77.089859))
                .width(5)
                .color(Color.RED));
        Polyline line2 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084124, -77.089859), new LatLng(-12.088036, -77.081563))
                .width(5)
                .color(Color.RED));
        Polyline line3 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088036, -77.081563), new LatLng(-12.090567, -77.076633))
                .width(5)
                .color(Color.RED));
        Polyline line4 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090567, -77.076633), new LatLng(-12.090326, -77.076429))
                .width(5)
                .color(Color.RED));
        Polyline line5 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090326, -77.076429), new LatLng(-12.092019, -77.071828))
                .width(5)
                .color(Color.RED));
        Polyline line6 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.092019, -77.071828), new LatLng(-12.089069, -77.070677))
                .width(5)
                .color(Color.RED));

        Polyline line7 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089069, -77.070677), new LatLng(-12.087498, -77.074932))
                .width(5)
                .color(Color.RED));
        /*Polyline line7a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088333, -77.072729), new LatLng(-12.087498, -77.074932))
                .width(5)
                .color(Color.RED));*/
        Polyline line8 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087498, -77.074932), new LatLng(-12.087490, -77.074950))
                .width(5)
                .color(Color.RED));
        Polyline line8a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087490, -77.074950), new LatLng(-12.085827, -77.078185))
                .width(5)
                .color(Color.RED));
        Polyline line8b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.085827, -77.078185), new LatLng(-12.084949, -77.079923))
                .width(5)
                .color(Color.RED));
        Polyline line8c = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084949, -77.079923), new LatLng(-12.083760, -77.082198))
                .width(5)
                .color(Color.RED));
        Polyline line8d = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083760, -77.082198), new LatLng(-12.083538, -77.082774))
                .width(5)
                .color(Color.RED));
        Polyline line8e = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083538, -77.082774), new LatLng(-12.081487, -77.088575))
                .width(5)
                .color(Color.RED));

        Polyline line9 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081487, -77.088575), new LatLng(-12.080882, -77.088213))
                .width(5)
                .color(Color.RED));
        Polyline line10 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080882, -77.088213), new LatLng(-12.081307, -77.087235))
                .width(5)
                .color(Color.RED));

        Polyline line11 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081307, -77.087235), new LatLng(-12.081648, -77.085784))
                .width(5)
                .color(Color.RED));
        Polyline line11a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081648, -77.085784), new LatLng(-12.081692, -77.085011))
                .width(5)
                .color(Color.RED));
        Polyline line11b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081692, -77.085011), new LatLng(-12.081898, -77.082404))
                .width(5)
                .color(Color.RED));
        //-12.083538, -77.082774

        Polyline line12 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081898, -77.082404), new LatLng(-12.082293, -77.081037))
                .width(5)
                .color(Color.RED));
        Polyline line12a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082293, -77.081037), new LatLng(-12.082731, -77.080179))
                .width(5)
                .color(Color.RED));
        Polyline line12b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082731, -77.080179), new LatLng(-12.085817, -77.074078))
                .width(5)
                .color(Color.RED));

        Polyline line13 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.085817, -77.074078), new LatLng(-12.086562, -77.074464))
                .width(5)
                .color(Color.RED));
        Polyline line14 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.086562, -77.074464), new LatLng(-12.087329, -77.072366))
                .width(5)
                .color(Color.RED));
        Polyline line15 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087329, -77.072366), new LatLng(-12.081871, -77.070292))
                .width(5)
                .color(Color.RED));
        Polyline line16 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081871, -77.070292), new LatLng(-12.078190, -77.080983))
                .width(5)
                .color(Color.RED));
        Polyline line17 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078190, -77.080983), new LatLng(-12.078008, -77.084506))
                .width(5)
                .color(Color.RED));
        Polyline line18 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078008, -77.084506), new LatLng(-12.074631, -77.083787))
                .width(5)
                .color(Color.RED));
        Polyline line19 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074631, -77.083787), new LatLng(-12.073584, -77.087023))
                .width(5)
                .color(Color.RED));
        Polyline line20 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.073584, -77.087023), new LatLng(-12.072947, -77.086758))
                .width(5)
                .color(Color.RED));
        Polyline line21 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072947, -77.086758), new LatLng(-12.070856, -77.090895))
                .width(5)
                .color(Color.RED));
        Polyline line22 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070856, -77.090895), new LatLng(-12.070258, -77.091582))
                .width(5)
                .color(Color.RED));
        Polyline line23 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070258, -77.091582), new LatLng(-12.074052, -77.092161))
                .width(5)
                .color(Color.RED));
        Polyline line24 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074052, -77.092161), new LatLng(-12.078800, -77.094103))
                .width(5)
                .color(Color.RED));
        Polyline line25 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078800, -77.094103), new LatLng(-12.076025, -77.099960))
                .width(5)
                .color(Color.RED));
        Polyline line26 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.076025, -77.099960), new LatLng(-12.080150, -77.102130))
                .width(5)
                .color(Color.RED));
        Polyline line27 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080150, -77.102130), new LatLng(-12.079838, -77.102915))
                .width(5)
                .color(Color.RED));
        Polyline line28 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079838, -77.102915), new LatLng(-12.081352, -77.103662))
                .width(5)
                .color(Color.RED));
        LatLng coordenadas2 = new LatLng(-12.081352, -77.103662);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadas2)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));
        /*Polyline line29 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074631, -77.083787), new LatLng(-12.073584, -77.087023))
                .width(5)
                .color(Color.RED));
        Polyline line30 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.073584, -77.087023), new LatLng(-12.072947, -77.086758))
                .width(5)
                .color(Color.RED));*/

    }

    void MarcadoresRutaB(){
        mMap.clear();
        LatLng coordenadas = new LatLng(-12.084215, -77.096470);
        CameraUpdate MiUbicacionX = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        mMap.animateCamera(MiUbicacionX);
        marcadorInicio = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084215, -77.096470), new LatLng(-12.077494, -77.093451))
                .width(5)
                .color(Color.RED));
        Polyline line2 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077494, -77.093451), new LatLng(-12.077954, -77.091979))
                .width(5)
                .color(Color.RED));
        Polyline line3 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077954, -77.091979), new LatLng(-12.078117, -77.088747))
                .width(5)
                .color(Color.RED));
        Polyline line4 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078117, -77.088747), new LatLng(-12.072999, -77.086790))
                .width(5)
                .color(Color.RED));
        Polyline line5 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072999, -77.086790), new LatLng(-12.070773, -77.090978))
                .width(5)
                .color(Color.RED));
        Polyline line6 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070773, -77.090978), new LatLng(-12.070046, -77.092025))
                .width(5)
                .color(Color.RED));

        Polyline line7 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070046, -77.092025), new LatLng(-12.069705, -77.093132))
                .width(5)
                .color(Color.RED));

        Polyline line8 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.069705, -77.093132), new LatLng(-12.068473, -77.104104))
                .width(5)
                .color(Color.RED));
        Polyline line9 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.068473, -77.104104), new LatLng(-12.067151, -77.103955))
                .width(5)
                .color(Color.RED));
        Polyline line10 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067151, -77.103955), new LatLng(-12.064764, -77.104305))
                .width(5)
                .color(Color.RED));
        Polyline line11 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064764, -77.104305), new LatLng(-12.064333, -77.100694))
                .width(5)
                .color(Color.RED));
        Polyline line12 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064333, -77.100694), new LatLng(-12.064347, -77.097352))
                .width(5)
                .color(Color.RED));
        Polyline line13 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064347, -77.097352), new LatLng(-12.066252, -77.097409))
                .width(5)
                .color(Color.RED));
        Polyline line14 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.066252, -77.097409), new LatLng(-12.069152, -77.098084))
                .width(5)
                .color(Color.RED));
        Polyline line15 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.069152, -77.098084), new LatLng(-12.068841, -77.101217))
                .width(5)
                .color(Color.RED));
        Polyline line16 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.068841, -77.101217), new LatLng(-12.070322, -77.101437))
                .width(5)
                .color(Color.RED));
        Polyline line17 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070322, -77.101437), new LatLng(-12.072855, -77.102585))
                .width(5)
                .color(Color.RED));
        Polyline line18 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072855, -77.102585), new LatLng(-12.067301, -77.116435))
                .width(5)
                .color(Color.RED));
        Polyline line19 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067301, -77.116435), new LatLng(-12.065484, -77.118749))
                .width(5)
                .color(Color.RED));
        Polyline line20 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.065484, -77.118749), new LatLng(-12.064950, -77.119037))
                .width(5)
                .color(Color.RED));
        Polyline line21 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064950, -77.119037), new LatLng(-12.064831, -77.119766))
                .width(5)
                .color(Color.RED));
        Polyline line22 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064831, -77.119766), new LatLng(-12.065387, -77.119910))
                .width(5)
                .color(Color.RED));
        Polyline line23 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.065387, -77.119910), new LatLng(-12.065914, -77.119568))
                .width(5)
                .color(Color.RED));
        Polyline line24 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.065914, -77.119568), new LatLng(-12.067446, -77.120412))
                .width(5)
                .color(Color.RED));
        Polyline line25 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067446, -77.120412), new LatLng(-12.069833, -77.115974))
                .width(5)
                .color(Color.RED));
        Polyline line26 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.069833, -77.115974), new LatLng(-12.071254, -77.116543))
                .width(5)
                .color(Color.RED));
        Polyline line27 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071254, -77.116543), new LatLng(-12.072915, -77.111964))
                .width(5)
                .color(Color.RED));
        Polyline line28 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072915, -77.111964), new LatLng(-12.072982, -77.111790))
                .width(5)
                .color(Color.RED));
        Polyline line29 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072982, -77.111790), new LatLng(-12.072366, -77.111501))
                .width(5)
                .color(Color.RED));
        Polyline line30 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072366, -77.111501), new LatLng(-12.071769, -77.110750))
                .width(5)
                .color(Color.RED));
        Polyline line31 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071769, -77.110750), new LatLng(-12.071214, -77.109913))
                .width(5)
                .color(Color.RED));
        Polyline line32 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071214, -77.109913), new LatLng(-12.070655, -77.109583))
                .width(5)
                .color(Color.RED));
        Polyline line33 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070655, -77.109583), new LatLng(-12.071067, -77.108526))
                .width(5)
                .color(Color.RED));
        Polyline line34 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071067, -77.108526), new LatLng(-12.071681, -77.108840))
                .width(5)
                .color(Color.RED));
        Polyline line35 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071681, -77.108840), new LatLng(-12.072670, -77.109835))
                .width(5)
                .color(Color.RED));
        Polyline line36 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072670, -77.109835), new LatLng(-12.073034, -77.110726))
                .width(5)
                .color(Color.RED));

        Polyline line37 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.073034, -77.110726), new LatLng(-12.072759, -77.111941))
                .width(5)
                .color(Color.RED));

        Polyline line38 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072759, -77.111941), new LatLng(-12.076226, -77.113237))
                .width(5)
                .color(Color.RED));
        Polyline line39 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.076226, -77.113237), new LatLng(-12.077429, -77.110107))
                .width(5)
                .color(Color.RED));
        Polyline line40 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077429, -77.110107), new LatLng(-12.080089, -77.105351))
                .width(5)
                .color(Color.RED));
        Polyline line40a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080089, -77.105351), new LatLng(-12.078993, -77.104640))
                .width(5)
                .color(Color.RED));
        Polyline line40b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078993, -77.104640), new LatLng(-12.079856, -77.102934))
                .width(5)
                .color(Color.RED));

        Polyline line41 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079856, -77.102934), new LatLng(-12.081283, -77.103603))
                .width(5)
                .color(Color.RED));
        LatLng coordenadas2 = new LatLng(-12.081283, -77.103603);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadas2)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));

    }

    void MarcadoresRutaC(){
        mMap.clear();
        LatLng coordenadas = new LatLng(-12.064815, -77.119676);
        CameraUpdate MiUbicacionX = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        mMap.animateCamera(MiUbicacionX);
        marcadorInicio = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064815, -77.119676), new LatLng(-12.062938, -77.122445))
                .width(5)
                .color(Color.RED));
        Polyline line2 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062938, -77.122445), new LatLng(-12.060756, -77.121307))
                .width(5)
                .color(Color.RED));
        Polyline line3 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.060756, -77.121307), new LatLng(-12.059421, -77.121262))
                .width(5)
                .color(Color.RED));
        Polyline line4 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.059421, -77.121262), new LatLng(-12.058193, -77.121551))
                .width(5)
                .color(Color.RED));
        Polyline line5 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.058193, -77.121551), new LatLng(-12.055708, -77.121915))
                .width(5)
                .color(Color.RED));
        Polyline line6 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055708, -77.121915), new LatLng(-12.054647, -77.120853))
                .width(5)
                .color(Color.RED));
        Polyline line7 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.054647, -77.120853), new LatLng(-12.054087, -77.119901))
                .width(5)
                .color(Color.RED));
        Polyline line8 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.054087, -77.119901), new LatLng(-12.053838, -77.117902))
                .width(5)
                .color(Color.RED));
        Polyline line9 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.053838, -77.117902), new LatLng(-12.053538, -77.115273))
                .width(5)
                .color(Color.RED));
        Polyline line10 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.053538, -77.115273), new LatLng(-12.053092, -77.109026))
                .width(5)
                .color(Color.RED));
        Polyline line11 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.053092, -77.109026), new LatLng(-12.052907, -77.108988))
                .width(5)
                .color(Color.RED));
        Polyline line12 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.052907, -77.108988), new LatLng(-12.052406, -77.108980))
                .width(5)
                .color(Color.RED));
        Polyline line13 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.052406, -77.108980), new LatLng(-12.049299, -77.109269))
                .width(5)
                .color(Color.RED));
        Polyline line14 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.049299, -77.109269), new LatLng(-12.050719, -77.125484))
                .width(5)
                .color(Color.RED));
        Polyline line15 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.050719, -77.125484), new LatLng(-12.050470, -77.125982))
                .width(5)
                .color(Color.RED));
        Polyline line16 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.050470, -77.125982), new LatLng(-12.050857, -77.126396))
                .width(5)
                .color(Color.RED));
        LatLng coordenadasx = new LatLng(-12.050857, -77.126396);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadasx)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.point)));

        Polyline line17 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.050857, -77.126396), new LatLng(-12.051459, -77.133538))
                .width(5)
                .color(Color.RED));
        Polyline line18 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051459, -77.133538), new LatLng(-12.051170, -77.134024))
                .width(5)
                .color(Color.RED));


        Polyline line19 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051170, -77.134024), new LatLng(-12.051251, -77.134411))
                .width(5)
                .color(Color.RED));
        Polyline line20 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051251, -77.134411), new LatLng(-12.051556, -77.134669))
                .width(5)
                .color(Color.RED));
        Polyline line21 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051556, -77.134669), new LatLng(-12.051734, -77.138341))
                .width(5)
                .color(Color.RED));
        /*Polyline line22 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051734, -77.138341), new LatLng(-12.052527, -77.138388))
                .width(5)
                .color(Color.RED));*/
        Polyline line23 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051734, -77.138341), new LatLng(-12.051848, -77.138634))
                .width(5)
                .color(Color.RED));
        Polyline line24 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051848, -77.138634), new LatLng(-12.051763, -77.139355))
                .width(5)
                .color(Color.RED));

        Polyline line25 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051763, -77.139355), new LatLng(-12.052237, -77.139822))
                .width(5)
                .color(Color.RED));
        Polyline line26 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.052237, -77.139822), new LatLng(-12.052619, -77.139871))
                .width(5)
                .color(Color.RED));
        Polyline line27 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.052619, -77.139871), new LatLng(-12.055442, -77.142136))
                .width(5)
                .color(Color.RED));
        Polyline line28 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055442, -77.142136), new LatLng(-12.056533, -77.142579))
                .width(5)
                .color(Color.RED));
        Polyline line29 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.056533, -77.142579), new LatLng(-12.059283, -77.143004))
                .width(5)
                .color(Color.RED));
        Polyline line30 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.059283, -77.143004), new LatLng(-12.061056, -77.143089))
                .width(5)
                .color(Color.RED));
        Polyline line31 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061056, -77.143089), new LatLng(-12.063574, -77.142668))
                .width(5)
                .color(Color.RED));
        Polyline line32 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.063574, -77.142668), new LatLng(-12.061215, -77.133590))
                .width(5)
                .color(Color.RED));
        Polyline line33 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061215, -77.133590), new LatLng(-12.062744, -77.129212))
                .width(5)
                .color(Color.RED));
        Polyline line34 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062744, -77.129212), new LatLng(-12.067556, -77.131013))
                .width(5)
                .color(Color.RED));
        Polyline line35 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067556, -77.131013), new LatLng(-12.066048, -77.122853))
                .width(5)
                .color(Color.RED));
        Polyline line36 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.066048, -77.122853), new LatLng(-12.067458, -77.120441))
                .width(5)
                .color(Color.RED));
        Polyline line37 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067458, -77.120441), new LatLng(-12.072556, -77.123353))
                .width(5)
                .color(Color.RED));
        Polyline line38 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072556, -77.123353), new LatLng(-12.076277, -77.113263))
                .width(5)
                .color(Color.RED));
        Polyline line39 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.076277, -77.113263), new LatLng(-12.077429, -77.110107))
                .width(5)
                .color(Color.RED));
        Polyline line40 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077429, -77.110107), new LatLng(-12.080089, -77.105351))
                .width(5)
                .color(Color.RED));
        Polyline line40a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080089, -77.105351), new LatLng(-12.078993, -77.104640))
                .width(5)
                .color(Color.RED));
        Polyline line40b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078993, -77.104640), new LatLng(-12.079856, -77.102934))
                .width(5)
                .color(Color.RED));

        Polyline line41 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079856, -77.102934), new LatLng(-12.081283, -77.103603))
                .width(5)
                .color(Color.RED));

        LatLng coordenadas2 = new LatLng(-12.081283, -77.103603);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadas2)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));

    }


    void MarcadoresRutaD(){
        mMap.clear();
        LatLng coordenadas = new LatLng(-12.070362, -77.109471);
        CameraUpdate MiUbicacionX = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        mMap.animateCamera(MiUbicacionX);
        marcadorInicio = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070362, -77.109471), new LatLng(-12.069794, -77.109179))
                .width(5)
                .color(Color.RED));

        Polyline line2 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.069794, -77.109179), new LatLng(-12.067381, -77.108630))
                .width(5)
                .color(Color.RED));
        Polyline line3 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067381, -77.108630), new LatLng(-12.066809, -77.108539))
                .width(5)
                .color(Color.RED));
        Polyline line4 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.066809, -77.108539), new LatLng(-12.064480, -77.108400))
                .width(5)
                .color(Color.RED));
        Polyline line5 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064480, -77.108400), new LatLng(-12.064343, -77.108421))
                .width(5)
                .color(Color.RED));
        Polyline line6 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064343, -77.108421), new LatLng(-12.062832, -77.108743))
                .width(5)
                .color(Color.RED));
        Polyline line7 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062832, -77.108743), new LatLng(-12.062030, -77.108786))
                .width(5)
                .color(Color.RED));
        Polyline line8 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062030, -77.108786), new LatLng(-12.061096, -77.108475))
                .width(5)
                .color(Color.RED));
        Polyline line9 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061096, -77.108475), new LatLng(-12.058751, -77.107466))
                .width(5)
                .color(Color.RED));
        Polyline line10 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.058751, -77.107466), new LatLng(-12.058100, -77.107252))
                .width(5)
                .color(Color.RED));
        Polyline line11 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.058100, -77.107252), new LatLng(-12.057597, -77.107209))
                .width(5)
                .color(Color.RED));
        Polyline line12 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.057597, -77.107209), new LatLng(-12.057062, -77.107300))
                .width(5)
                .color(Color.RED));
        Polyline line13 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.057062, -77.107300), new LatLng(-12.057754, -77.110696))
                .width(5)
                .color(Color.RED));
        Polyline line14 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.057754, -77.110696), new LatLng(-12.059281, -77.110422))
                .width(5)
                .color(Color.RED));
        Polyline line15 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.059281, -77.110422), new LatLng(-12.061914, -77.111801))
                .width(5)
                .color(Color.RED));
        Polyline line16 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061914, -77.111801), new LatLng(-12.062533, -77.111763))
                .width(5)
                .color(Color.RED));
        /*LatLng coordenadasx = new LatLng(-12.050857, -77.126396);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadasx)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.point)));*/

        Polyline line17 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062533, -77.111763), new LatLng(-12.062643, -77.112648))
                .width(5)
                .color(Color.RED));
        Polyline line18 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062643, -77.112648), new LatLng(-12.061091, -77.115341))
                .width(5)
                .color(Color.RED));


        Polyline line19 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061091, -77.115341), new LatLng(-12.063920, -77.117415))
                .width(5)
                .color(Color.RED));
        Polyline line20 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.063920, -77.117415), new LatLng(-12.063617, -77.117839))
                .width(5)
                .color(Color.RED));
        Polyline line21 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.063617, -77.117839), new LatLng(-12.062235, -77.116780))
                .width(5)
                .color(Color.RED));
        /*Polyline line22 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051734, -77.138341), new LatLng(-12.052527, -77.138388))
                .width(5)
                .color(Color.RED));*/
        Polyline line23 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062235, -77.116780), new LatLng(-12.060822, -77.115887))
                .width(5)
                .color(Color.RED));
        Polyline line24 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.060822, -77.115887), new LatLng(-12.060149, -77.115351))
                .width(5)
                .color(Color.RED));

        Polyline line25 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.060149, -77.115351), new LatLng(-12.059182, -77.114732))
                .width(5)
                .color(Color.RED));
        Polyline line26 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.059182, -77.114732), new LatLng(-12.058461, -77.114598))
                .width(5)
                .color(Color.RED));
        Polyline line27 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.058461, -77.114598), new LatLng(-12.057575, -77.114617))
                .width(5)
                .color(Color.RED));
        Polyline line28 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.057575, -77.114617), new LatLng(-12.056771, -77.114727))
                .width(5)
                .color(Color.RED));
        Polyline line29 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.056771, -77.114727), new LatLng(-12.054062, -77.114939))
                .width(5)
                .color(Color.RED));
        Polyline line30 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.054062, -77.114939), new LatLng(-12.053469, -77.114966))
                .width(5)
                .color(Color.RED));
        Polyline line31 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.053469, -77.114966), new LatLng(-12.052942, -77.114946))
                .width(5)
                .color(Color.RED));
        Polyline line32 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.052942, -77.114946), new LatLng(-12.051713, -77.115058))
                .width(5)
                .color(Color.RED));
        Polyline line33 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051713, -77.115058), new LatLng(-12.050986, -77.115112))
                .width(5)
                .color(Color.RED));
        Polyline line34 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.050986, -77.115112), new LatLng(-12.049969, -77.115170))
                .width(5)
                .color(Color.RED));
        Polyline line35 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.049969, -77.115170), new LatLng(-12.049367, -77.109246))
                .width(5)
                .color(Color.RED));
        Polyline line36 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.049367, -77.109246), new LatLng(-12.049015, -77.105253))
                .width(5)
                .color(Color.RED));
        Polyline line37 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.049015, -77.105253), new LatLng(-12.048846, -77.102714))
                .width(5)
                .color(Color.RED));
        Polyline line38 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.048846, -77.102714), new LatLng(-12.055343, -77.106996))
                .width(5)
                .color(Color.RED));
        Polyline line39 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055343, -77.106996), new LatLng(-12.055435, -77.106871))
                .width(5)
                .color(Color.RED));
        Polyline line40 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055435, -77.106871), new LatLng(-12.055335, -77.106381))
                .width(5)
                .color(Color.RED));
        Polyline line40a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055335, -77.106381), new LatLng(-12.055346, -77.106146))
                .width(5)
                .color(Color.RED));
        Polyline line40b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055346, -77.106146), new LatLng(-12.055465, -77.105957))
                .width(5)
                .color(Color.RED));

        Polyline line41 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.055465, -77.105957), new LatLng(-12.056726, -77.105638))
                .width(5)
                .color(Color.RED));
        Polyline line42 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.056726, -77.105638), new LatLng(-12.056448, -77.104170))
                .width(5)
                .color(Color.RED));
        Polyline line43 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.056448, -77.104170), new LatLng(-12.056511, -77.103961))
                .width(5)
                .color(Color.RED));
        Polyline line44 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.056511, -77.103961), new LatLng(-12.057544, -77.101998))
                .width(5)
                .color(Color.RED));
        Polyline line45 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.057544, -77.101998), new LatLng(-12.061315, -77.104002))
                .width(5)
                .color(Color.RED));
        Polyline line46 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061315, -77.104002), new LatLng(-12.061498, -77.104937))
                .width(5)
                .color(Color.RED));
        Polyline line47 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.061498, -77.104937), new LatLng(-12.062747, -77.104795))
                .width(5)
                .color(Color.RED));
        Polyline line48 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062747, -77.104795), new LatLng(-12.063327, -77.104729))
                .width(5)
                .color(Color.RED));
        Polyline line49 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.063327, -77.104729), new LatLng(-12.064277, -77.104509))
                .width(5)
                .color(Color.RED));
        Polyline line50 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064277, -77.104509), new LatLng(-12.064754, -77.104435))
                .width(5)
                .color(Color.RED));

        Polyline line51 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064754, -77.104435), new LatLng(-12.066364, -77.104112))
                .width(5)
                .color(Color.RED));
        Polyline line52 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.066364, -77.104112), new LatLng(-12.067122, -77.104071))
                .width(5)
                .color(Color.RED));
        Polyline line53 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067122, -77.104071), new LatLng(-12.067931, -77.104124))
                .width(5)
                .color(Color.RED));
        Polyline line54 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067931, -77.104124), new LatLng(-12.068857, -77.104289))
                .width(5)
                .color(Color.RED));
        Polyline line55 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.068857, -77.104289), new LatLng(-12.070566, -77.104707))
                .width(5)
                .color(Color.RED));
        Polyline line56 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070566, -77.104707), new LatLng(-12.071280, -77.104870))
                .width(5)
                .color(Color.RED));
        Polyline line57 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071280, -77.104870), new LatLng(-12.071754, -77.105108))
                .width(5)
                .color(Color.RED));
        Polyline line58 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071754, -77.105108), new LatLng(-12.072685, -77.105521))
                .width(5)
                .color(Color.RED));
        Polyline line59 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.072685, -77.105521), new LatLng(-12.074138, -77.106106))
                .width(5)
                .color(Color.RED));
        Polyline line60 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074138, -77.106106), new LatLng(-12.074858, -77.106377))
                .width(5)
                .color(Color.RED));

        Polyline line61 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074858, -77.106377), new LatLng(-12.077301, -77.107647))
                .width(5)
                .color(Color.RED));
        Polyline line62 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077301, -77.107647), new LatLng(-12.078731, -77.105075))
                .width(5)
                .color(Color.RED));
        Polyline line63 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078731, -77.105075), new LatLng(-12.078981, -77.104639))
                .width(5)
                .color(Color.RED));
        Polyline line64 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078981, -77.104639), new LatLng(-12.079848, -77.102911))
                .width(5)
                .color(Color.RED));
        Polyline line65 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079848, -77.102911), new LatLng(-12.081282, -77.103607))
                .width(5)
                .color(Color.RED));



        LatLng coordenadas2 = new LatLng(-12.081282, -77.103607);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadas2)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));

    }

    void MarcadoresRutaE(){
        mMap.clear();
        LatLng coordenadas = new LatLng(-12.082273, -77.104637);
        CameraUpdate MiUbicacionX = CameraUpdateFactory.newLatLngZoom(coordenadas, 15);
        mMap.animateCamera(MiUbicacionX);
        marcadorInicio = mMap.addMarker(new MarkerOptions()
                .position(coordenadas)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.start)));
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082273, -77.104637), new LatLng(-12.082791, -77.103712))
                .width(5)
                .color(Color.RED));
        Polyline line2 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082791, -77.103712), new LatLng(-12.083159, -77.103033))
                .width(5)
                .color(Color.RED));
        Polyline line3 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083159, -77.103033), new LatLng(-12.083281, -77.102408))
                .width(5)
                .color(Color.RED));
        Polyline line4 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083281, -77.102408), new LatLng(-12.083359, -77.101636))
                .width(5)
                .color(Color.RED));
        Polyline line5 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083359, -77.101636), new LatLng(-12.084092, -77.099897))
                .width(5)
                .color(Color.RED));
        Polyline line6 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084092, -77.099897), new LatLng(-12.084569, -77.098762))
                .width(5)
                .color(Color.RED));
        Polyline line7 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084569, -77.098762), new LatLng(-12.085299, -77.097028))
                .width(5)
                .color(Color.RED));
        Polyline line8 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.085299, -77.097028), new LatLng(-12.085844, -77.095950))
                .width(5)
                .color(Color.RED));
        Polyline line9 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.085844, -77.095950), new LatLng(-12.086119, -77.095430))
                .width(5)
                .color(Color.RED));
        Polyline line10 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.086119, -77.095430), new LatLng(-12.086643, -77.094221))
                .width(5)
                .color(Color.RED));
        Polyline line11 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.086643, -77.094221), new LatLng(-12.086871, -77.093564))
                .width(5)
                .color(Color.RED));
        Polyline line12 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.086871, -77.093564), new LatLng(-12.087008, -77.093223))
                .width(5)
                .color(Color.RED));
        Polyline line13 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087008, -77.093223), new LatLng(-12.087451, -77.092553))
                .width(5)
                .color(Color.RED));
        Polyline line14 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087451, -77.092553), new LatLng(-12.087728, -77.091877))
                .width(5)
                .color(Color.RED));
        Polyline line15 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087728, -77.091877), new LatLng(-12.087984, -77.091261))
                .width(5)
                .color(Color.RED));
        Polyline line16 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.087984, -77.091261), new LatLng(-12.088274, -77.090687))
                .width(5)
                .color(Color.RED));
        /*LatLng coordenadasx = new LatLng(-12.050857, -77.126396);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadasx)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.point)));*/

        Polyline line17 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088274, -77.090687), new LatLng(-12.088464, -77.090097))
                .width(5)
                .color(Color.RED));
        Polyline line18 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088464, -77.090097), new LatLng(-12.088648, -77.089759))
                .width(5)
                .color(Color.RED));


        Polyline line19 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088648, -77.089759), new LatLng(-12.089007, -77.089252))
                .width(5)
                .color(Color.RED));
        Polyline line20 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089007, -77.089252), new LatLng(-12.089475, -77.088320))
                .width(5)
                .color(Color.RED));
        Polyline line21 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089475, -77.088320), new LatLng(-12.089699, -77.087835))
                .width(5)
                .color(Color.RED));
        /*Polyline line22 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.051734, -77.138341), new LatLng(-12.052527, -77.138388))
                .width(5)
                .color(Color.RED));*/
        Polyline line23 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089699, -77.087835), new LatLng(-12.089793, -77.087417))
                .width(5)
                .color(Color.RED));
        Polyline line24 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089793, -77.087417), new LatLng(-12.089908, -77.087012))
                .width(5)
                .color(Color.RED));

        Polyline line25 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089908, -77.087012), new LatLng(-12.090417, -77.086110))
                .width(5)
                .color(Color.RED));
        Polyline line26 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090417, -77.086110), new LatLng(-12.090676, -77.085618))
                .width(5)
                .color(Color.RED));
        Polyline line27 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090676, -77.085618), new LatLng(-12.090676, -77.084831))
                .width(5)
                .color(Color.RED));
        Polyline line28 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090676, -77.084831), new LatLng(-12.091384, -77.083382))
                .width(5)
                .color(Color.RED));
        Polyline line29 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.091384, -77.083382), new LatLng(-12.092167, -77.081858))
                .width(5)
                .color(Color.RED));
        Polyline line30 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.092167, -77.081858), new LatLng(-12.093039, -77.080112))
                .width(5)
                .color(Color.RED));
        Polyline line31 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.093039, -77.080112), new LatLng(-12.093800, -77.078648))
                .width(5)
                .color(Color.RED));
        Polyline line32 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.093800, -77.078648), new LatLng(-12.094489, -77.077327))
                .width(5)
                .color(Color.RED));
        Polyline line33 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.094489, -77.077327), new LatLng(-12.095116, -77.075736))
                .width(5)
                .color(Color.RED));
        Polyline line34 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.095116, -77.075736), new LatLng(-12.095996, -77.073248))
                .width(5)
                .color(Color.RED));
        Polyline line35 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.095996, -77.073248), new LatLng(-12.096367, -77.072115))
                .width(5)
                .color(Color.RED));
        Polyline line36 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.096367, -77.072115), new LatLng(-12.093418, -77.071066))
                .width(5)
                .color(Color.RED));
        Polyline line37 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.093418, -77.071066), new LatLng(-12.093261, -77.071442))
                .width(5)
                .color(Color.RED));
        Polyline line38 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.093261, -77.071442), new LatLng(-12.092877, -77.072510))
                .width(5)
                .color(Color.RED));
        Polyline line39 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.092877, -77.072510), new LatLng(-12.092612, -77.073182))
                .width(5)
                .color(Color.RED));
        Polyline line40 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.092612, -77.073182), new LatLng(-12.091686, -77.075825))
                .width(5)
                .color(Color.RED));
        Polyline line40a = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.091686, -77.075825), new LatLng(-12.091421, -77.076502))
                .width(5)
                .color(Color.RED));
        Polyline line40b = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.091421, -77.076502), new LatLng(-12.091373, -77.076935))
                .width(5)
                .color(Color.RED));

        Polyline line41 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.091373, -77.076935), new LatLng(-12.091364, -77.077097))
                .width(5)
                .color(Color.RED));
        Polyline line42 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.091364, -77.077097), new LatLng(-12.090522, -77.078739))
                .width(5)
                .color(Color.RED));
        Polyline line43 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090522, -77.078739), new LatLng(-12.090001, -77.079759))
                .width(5)
                .color(Color.RED));
        Polyline line44 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.090001, -77.079759), new LatLng(-12.089295, -77.081121))
                .width(5)
                .color(Color.RED));
        Polyline line45 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.089295, -77.081121), new LatLng(-12.088404, -77.082853))
                .width(5)
                .color(Color.RED));
        Polyline line46 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088404, -77.082853), new LatLng(-12.088118, -77.083436))
                .width(5)
                .color(Color.RED));
        Polyline line47 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.088118, -77.083436), new LatLng(-12.086334, -77.082669))
                .width(5)
                .color(Color.RED));
        Polyline line48 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.086334, -77.082669), new LatLng(-12.086082, -77.083291))
                .width(5)
                .color(Color.RED));
        Polyline line49 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.086082, -77.083291), new LatLng(-12.084776, -77.082889))
                .width(5)
                .color(Color.RED));
        Polyline line50 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084776, -77.082889), new LatLng(-12.081991, -77.082315))
                .width(5)
                .color(Color.RED));

        Polyline line51 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081991, -77.082315), new LatLng(-12.080186, -77.081934))
                .width(5)
                .color(Color.RED));
        Polyline line52 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080186, -77.081934), new LatLng(-12.079284, -77.081634))
                .width(5)
                .color(Color.RED));
        Polyline line53 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079284, -77.081634), new LatLng(-12.078660, -77.081210))
                .width(5)
                .color(Color.RED));
        Polyline line54 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078660, -77.081210), new LatLng(-12.077852, -77.080979))
                .width(5)
                .color(Color.RED));
        Polyline line55 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077852, -77.080979), new LatLng(-12.077013, -77.080824))
                .width(5)
                .color(Color.RED));
        Polyline line56 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077013, -77.080824), new LatLng(-12.075958, -77.080325))
                .width(5)
                .color(Color.RED));
        Polyline line57 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.075958, -77.080325), new LatLng(-12.074615, -77.079343))
                .width(5)
                .color(Color.RED));
        Polyline line58 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074615, -77.079343), new LatLng(-12.073760, -77.078683))
                .width(5)
                .color(Color.RED));
        Polyline line59 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.073760, -77.078683), new LatLng(-12.073236, -77.078415))
                .width(5)
                .color(Color.RED));
        Polyline line60 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.073236, -77.078415), new LatLng(-12.071898, -77.078276))
                .width(5)
                .color(Color.RED));

        Polyline line61 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.071898, -77.078276), new LatLng(-12.070057, -77.077814))
                .width(5)
                .color(Color.RED));
        Polyline line62 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070057, -77.077814), new LatLng(-12.068047, -77.077702))
                .width(5)
                .color(Color.RED));
        Polyline line63 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.068047, -77.077702), new LatLng(-12.066081, -77.077838))
                .width(5)
                .color(Color.RED));
        Polyline line64 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.066081, -77.077838), new LatLng(-12.064174, -77.078157))
                .width(5)
                .color(Color.RED));
        Polyline line65 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.064174, -77.078157), new LatLng(-12.062104, -77.078650))
                .width(5)
                .color(Color.RED));

        Polyline line66 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.062104, -77.078650), new LatLng(-12.063037, -77.082466))
                .width(5)
                .color(Color.RED));
        Polyline line67 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.063037, -77.082466), new LatLng(-12.063891, -77.082223))
                .width(5)
                .color(Color.RED));
        Polyline line68 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.063891, -77.082223), new LatLng(-12.065850, -77.081853))
                .width(5)
                .color(Color.RED));
        Polyline line69 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.065850, -77.081853), new LatLng(-12.066774, -77.081705))
                .width(5)
                .color(Color.RED));
        Polyline line70 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.066774, -77.081705), new LatLng(-12.067616, -77.081705))
                .width(5)
                .color(Color.RED));

        Polyline line71 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.067616, -77.081705), new LatLng(-12.068887, -77.081851))
                .width(5)
                .color(Color.RED));
        Polyline line72 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.068887, -77.081851), new LatLng(-12.070541, -77.082379))
                .width(5)
                .color(Color.RED));
        Polyline line73 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.070541, -77.082379), new LatLng(-12.073006, -77.083214))
                .width(5)
                .color(Color.RED));
        Polyline line74 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.073006, -77.083214), new LatLng(-12.074343, -77.083684))
                .width(5)
                .color(Color.RED));
        Polyline line75 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074343, -77.083684), new LatLng(-12.074408, -77.083760))
                .width(5)
                .color(Color.RED));
        Polyline line76 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074408, -77.083760), new LatLng(-12.074839, -77.083904))
                .width(5)
                .color(Color.RED));
        Polyline line77 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.074839, -77.083904), new LatLng(-12.075673, -77.084218))
                .width(5)
                .color(Color.RED));
        Polyline line78 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.075673, -77.084218), new LatLng(-12.076349, -77.084409))
                .width(5)
                .color(Color.RED));
        Polyline line79 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.076349, -77.084409), new LatLng(-12.077382, -77.084511))
                .width(5)
                .color(Color.RED));
        Polyline line80 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077382, -77.084511), new LatLng(-12.078029, -77.084539))
                .width(5)
                .color(Color.RED));

        Polyline line81 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078029, -77.084539), new LatLng(-12.077963, -77.087154))
                .width(5)
                .color(Color.RED));
        Polyline line82 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.077963, -77.087154), new LatLng(-12.078481, -77.087268))
                .width(5)
                .color(Color.RED));
        Polyline line83 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.078481, -77.087268), new LatLng(-12.079718, -77.087813))
                .width(5)
                .color(Color.RED));
        Polyline line84 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079718, -77.087813), new LatLng(-12.080200, -77.088015))
                .width(5)
                .color(Color.RED));
        Polyline line85 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080200, -77.088015), new LatLng(-12.081403, -77.088735))
                .width(5)
                .color(Color.RED));

        Polyline line86 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081403, -77.088735), new LatLng(-12.082108, -77.089102))
                .width(5)
                .color(Color.RED));
        Polyline line87 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082108, -77.089102), new LatLng(-12.083985, -77.090006))
                .width(5)
                .color(Color.RED));
        Polyline line88 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083985, -77.090006), new LatLng(-12.084339, -77.090156))
                .width(5)
                .color(Color.RED));
        Polyline line89 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084339, -77.090156), new LatLng(-12.084690, -77.090574))
                .width(5)
                .color(Color.RED));
        Polyline line90 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084690, -77.090574), new LatLng(-12.085251, -77.090840))
                .width(5)
                .color(Color.RED));

        Polyline line91 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.085251, -77.090840), new LatLng(-12.084907, -77.091626))
                .width(5)
                .color(Color.RED));
        Polyline line92 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084907, -77.091626), new LatLng(-12.084446, -77.092616))
                .width(5)
                .color(Color.RED));
        Polyline line93 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.084446, -77.092616), new LatLng(-12.083748, -77.094131))
                .width(5)
                .color(Color.RED));
        Polyline line94 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.083748, -77.094131), new LatLng(-12.082925, -77.095909))
                .width(5)
                .color(Color.RED));
        Polyline line95 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082925, -77.095909), new LatLng(-12.082206, -77.097540))
                .width(5)
                .color(Color.RED));
        Polyline line96 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.082206, -77.097540), new LatLng(-12.081558, -77.099032))
                .width(5)
                .color(Color.RED));
        Polyline line97 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081558, -77.099032), new LatLng(-12.081072, -77.100071))
                .width(5)
                .color(Color.RED));
        Polyline line98 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.081072, -77.100071), new LatLng(-12.080195, -77.102093))
                .width(5)
                .color(Color.RED));
        Polyline line99 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.080195, -77.102093), new LatLng(-12.079840, -77.102903))
                .width(5)
                .color(Color.RED));
        Polyline line100 = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(-12.079840, -77.102903), new LatLng(-12.081295, -77.103620))
                .width(5)
                .color(Color.RED));

        LatLng coordenadas2 = new LatLng(-12.081283, -77.103603);
        marcadorFinal = mMap.addMarker(new MarkerOptions()
                .position(coordenadas2)
                .title("Dirección2:" + direccion)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home)));

    }

    @Override
    protected void onPause(){
        super.onPause();
        Conexion conexion = new Conexion();
        Alumno alumno = new Alumno();
        ruta = conexion.getUrl(getApplicationContext());
        ruta = ruta + "/controler/consultaAlumno.php?accionget=8&alumnoget="+alumno.getAlumnoElegido(getApplicationContext());
        Log.e("rutasaldo",ruta);
        Lee lee = new Lee();
        lee.execute(ruta);
    }

    @Override
    protected void onResume(){
        super.onResume();
        Conexion conexion = new Conexion();
        Alumno alumno = new Alumno();
        ruta = conexion.getUrl(getApplicationContext());
        ruta = ruta + "/controler/consultaAlumno.php?accionget=8&alumnoget="+alumno.getAlumnoElegido(getApplicationContext());
        Log.e("rutasaldo",ruta);
        Lee lee = new Lee();
        lee.execute(ruta);
    }

}
