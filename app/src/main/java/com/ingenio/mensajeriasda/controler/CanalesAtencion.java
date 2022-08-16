package com.ingenio.mensajeriasda.controler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.MensajeModel;

public class CanalesAtencion extends AppCompatActivity {

    Button correoSec,correoTes,correoAten,wpSec,wpTes,wpAten;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.canales_atencion);

        correoAten = (Button) findViewById(R.id.correoAtencion);
        correoSec = (Button) findViewById(R.id.correoSecretaria);
        correoTes = (Button) findViewById(R.id.correoTesoreria);
        wpAten = (Button) findViewById(R.id.wpAtencion);
        wpSec = (Button) findViewById(R.id.wpSecretaria);
        wpTes = (Button) findViewById(R.id.wpTesoreria);

        correoAten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alumno alumno = new Alumno();
                String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                String mensajeria="Estimado personal de Atención al cliente"
                        +". Soy familiar de "+nombre[1]
                        +". Tengo la siguiente consulta:\n";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"atencionalcliente@sda.edu.pe"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
                intent.putExtra(Intent.EXTRA_TEXT, mensajeria);
                startActivity(Intent.createChooser(intent, "Consulta"));
            }
        });

        correoSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alumno alumno = new Alumno();
                String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                String mensajeria="Estimado personal de Secretaría"
                        +". Soy familiar de "+nombre[1]
                        +". Tengo la siguiente consulta:\n";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"secretaria@sda.edu.pe"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
                intent.putExtra(Intent.EXTRA_TEXT, mensajeria);
                startActivity(Intent.createChooser(intent, "Consulta"));
            }
        });

        correoTes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alumno alumno = new Alumno();
                String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                String mensajeria="Estimado personal de Tesorería"
                        +". Soy familiar de "+nombre[1]
                        +". Tengo la siguiente consulta:\n";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tesoreria@sda.edu.pe"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Consulta");
                intent.putExtra(Intent.EXTRA_TEXT, mensajeria);
                startActivity(Intent.createChooser(intent, "Consulta"));
            }
        });

        wpAten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(estaInstaladaAplicacion("com.whatsapp.w4b", getApplicationContext())){
                    Alumno alumno = new Alumno();
                    String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                    String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                    String mensajeria="https://api.whatsapp.com/send?phone=51990653388"
                            +"&text=Estimado%20personal%20de%20Atención%20al%20cliente%20"
                            +"%20Soy%20familiar%20de%20"+nombre[1].replace(" ","%20")
                            +".%20Tengo%20la%20siguiente%20consulta:%0A";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setPackage("com.whatsapp.w4b");
                    startActivity(i);
                } else {
                    Alumno alumno = new Alumno();
                    String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                    String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                    String mensajeria="https://api.whatsapp.com/send?phone=51990653388"
                            +"&text=Estimado%20personal%20de%20Atención%20al%20cliente%20"
                            +"%20Soy%20familiar%20de%20"+nombre[1].replace(" ","%20")
                            +".%20Tengo%20la%20siguiente%20consulta:%0A";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    //i.setPackage("com.whatsapp");
                    startActivity(i);

                }


            }
        });

        wpSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(estaInstaladaAplicacion("com.whatsapp.w4b", getApplicationContext())){
                    Alumno alumno = new Alumno();
                    String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                    String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                    String mensajeria="https://api.whatsapp.com/send?phone=51976304634"
                            +"&text=Estimado%20personal%20de%20Secretaría"
                            +"%20Soy%20familiar%20de%20"+nombre[1].replace(" ","%20")
                            +".%20Tengo%20la%20siguiente%20consulta:%0A";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setPackage("com.whatsapp.w4b");
                    startActivity(i);
                } else {
                    Alumno alumno = new Alumno();
                    String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                    String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                    String mensajeria="https://api.whatsapp.com/send?phone=51976304634"
                            +"&text=Estimado%20personal%20de%20Secretaría"
                            +"%20Soy%20familiar%20de%20"+nombre[1].replace(" ","%20")
                            +".%20Tengo%20la%20siguiente%20consulta:%0A";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    //i.setPackage("com.whatsapp");
                    startActivity(i);

                }


            }
        });

        wpTes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(estaInstaladaAplicacion("com.whatsapp.w4b", getApplicationContext())){
                    Alumno alumno = new Alumno();
                    String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                    String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                    String mensajeria="https://api.whatsapp.com/send?phone=51995558868"
                            +"&text=Estimado%20personal%20de%20Tesorería"
                            +"%20Soy%20familiar%20de%20"+nombre[1].replace(" ","%20")
                            +".%20Tengo%20la%20siguiente%20consulta:%0A";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setPackage("com.whatsapp.w4b");
                    startActivity(i);
                } else {
                    Alumno alumno = new Alumno();
                    String nalumno = alumno.getAlumnoElegido(getApplicationContext());
                    String nombre[] = alumno.getAlumnoData(nalumno,getApplicationContext()).split("&");

                    String mensajeria="https://api.whatsapp.com/send?phone=51995558868"
                            +"&text=Estimado%20personal%20de%20Tesorería"
                            +"%20Soy%20familiar%20de%20"+nombre[1].replace(" ","%20")
                            +".%20Tengo%20la%20siguiente%20consulta:%0A";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    //i.setPackage("com.whatsapp");
                    startActivity(i);

                }


            }
        });

    }

    private boolean estaInstaladaAplicacion(String nombrePaquete, Context context) {

        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(nombrePaquete, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
