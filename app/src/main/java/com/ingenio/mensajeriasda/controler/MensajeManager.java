package com.ingenio.mensajeriasda.controler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.github.nkzawa.socketio.client.Socket;
import com.ingenio.mensajeriasda.BootReceiver;
import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.MensajeModel;

import java.util.ArrayList;

public class MensajeManager extends AppCompatActivity {

    ImageView atras;
    String elegido="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mensaje_manager);
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();
        final MensajeModel mensajeModel = new MensajeModel();

        final Alumno alumno = new Alumno();
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String[] valores = alumno.getAlumnosNombre(getApplicationContext()).split("_");
        final String[] valores2 = alumno.getAlumnos(getApplicationContext()).split("_");
        spinner.setAdapter(new ArrayAdapter<String>(this, R.layout.itemspinner, valores));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id)
            {
                elegido = valores2[position];
                alumno.setAlumnoElegido(elegido,getApplicationContext());
                Lista2(mensajeModel.getMensaje(getApplicationContext()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //elegido = valores2[0];
            }
        });

        atras = (ImageView) findViewById(R.id.atras);
        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MensajeManager.this,MainActivity.class);
                startActivity(i2);
            }
        });



        Lista(mensajeModel.getMensaje(getApplicationContext()));

    }

    private void Lista(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista);
        Log.d("ingreso lista",mensaje);
        Alumno alumno = new Alumno();
        String elegido =  alumno.getAlumnoElegido(getApplicationContext());

        final ArrayList<Mensaje> arrayList = new ArrayList<Mensaje>();
        final String d[] = mensaje.split("#");

        if(mensaje!=""){

            int n = d.length;
            int i;
            for(i=0; i<d.length; i++){
                String e[] = d[i].split("%");
                //Log.e("d",e[4]+e[5]+elegido);
                Mensaje mensaje1 = new Mensaje(e[0],e[1],e[2],e[3],e[4],e[5],e[6],e[7],e[8],e[9]);
                arrayList.add(mensaje1);

            }
        }

        AdapterMensajes adapterMensajes = new AdapterMensajes(this,arrayList);
        lista.setAdapter(adapterMensajes);

    }

    private void Lista2(String mensaje){
        final ListView lista = (ListView) findViewById(R.id.milista);
        Log.d("ingreso lista",mensaje);
        Alumno alumno = new Alumno();
        String elegido =  alumno.getAlumnoElegido(getApplicationContext());

        final ArrayList<Mensaje> arrayList = new ArrayList<Mensaje>();
        final String d[] = mensaje.split("#");

        if(mensaje!=""){

            int n = d.length;
            int i;
            for(i=0; i<d.length; i++){
                String e[] = d[i].split("%");
                //Log.e("d",e[4]+e[5]+elegido);
                if(e[4].equals(elegido)){

                    Mensaje mensaje1 = new Mensaje(e[0],e[1],e[2],e[3],e[4],e[5],e[6],e[7],e[8],e[9]);
                    arrayList.add(mensaje1);
                }

            }
        }

        AdapterMensajes adapterMensajes = new AdapterMensajes(this,arrayList);
        lista.setAdapter(adapterMensajes);

    }

}
