package com.ingenio.mensajeriasda.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.MensajeModel;

public class DetalleMensaje extends Activity {

    ImageView atras;
    Button wp, mail;
    TextView fecha,alumno,asunto,responsable,curso;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_mensaje);
/*
*
String info = mensajes.getSupervisor()+"%"+mensajes.getSupervisorMail()+"%"+mensajes.getSupervisorCelular()+"%"
        +mensajes.getId_ppff()+"%"+mensajes.getAlumno()+"%"+mensajes.getAlumnoNombre()+"%"
        +mensajes.getFecha()+"%"+mensajes.getHora()+"%"+mensajes.getAsunto()+"%"
        +mensajes.getCurso()+"%";
* */

        fecha = (TextView) findViewById(R.id.fecha);
        alumno = (TextView) findViewById(R.id.alumno);
        asunto = (TextView) findViewById(R.id.asunto);
        curso = (TextView) findViewById(R.id.asignatura);
        responsable = (TextView) findViewById(R.id.responsable);

        MensajeModel mensajeModel = new MensajeModel();
        String info[] = mensajeModel.getMensajeElegido(getApplicationContext()).split("%");
        fecha.setText(info[6]+"\n"+info[7]);
        alumno.setText(info[5]);
        asunto.setText(info[8]);
        curso.setText(info[9]);
        responsable.setText(info[0]);

        atras = (ImageView) findViewById(R.id.atras);
        wp = (Button) findViewById(R.id.wp);
        mail = (Button) findViewById(R.id.mail);

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(DetalleMensaje.this, MensajeManager.class);
                startActivity(i2);
            }
        });

        wp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if(estaInstaladaAplicacion("com.whatsapp", getApplicationContext())){
                    MensajeModel mensajeModel = new MensajeModel();
                    String info = mensajeModel.getMensajeElegido(getApplicationContext());
                    String info2[] = info.split("%");

                    String mensajeria="https://api.whatsapp.com/send?phone=51"+info2[2]
                            +"&text=Estimado%20"+info2[0].replace(" ","%20")
                            +"%20Soy%20apoderado%20de%20"+info2[5].replace(" ","%20")
                            +".%20He%20recibido%20el%20siguiente%20mensaje%20:%0A"+info2[8].replace(" ","%20")
                            +"%0AAl%20respecto%20le%20comunico%20que%20";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setPackage("com.whatsapp");
                    startActivity(i);
                } else*/ if(estaInstaladaAplicacion("com.whatsapp.w4b", getApplicationContext())){
                    MensajeModel mensajeModel = new MensajeModel();
                    String info = mensajeModel.getMensajeElegido(getApplicationContext());
                    String info2[] = info.split("%");

                    String mensajeria="https://api.whatsapp.com/send?phone=51"+info2[2]
                            +"&text=Estimado%20"+info2[0].replace(" ","%20")
                            +"%20Soy%20apoderado%20de%20"+info2[5].replace(" ","%20")
                            +".%20He%20recibido%20el%20siguiente%20mensaje%20:%0A"+info2[8].replace(" ","%20")
                            +"%0AAl%20respecto%20le%20comunico%20que%20";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    i.setPackage("com.whatsapp.w4b");
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(),"Usted no tiene instaladada la app WhatsApp",Toast.LENGTH_LONG).show();
                    MensajeModel mensajeModel = new MensajeModel();
                    String info = mensajeModel.getMensajeElegido(getApplicationContext());
                    String info2[] = info.split("%");

                    String mensajeria="https://api.whatsapp.com/send?phone=51"+info2[2]
                            +"&text=Estimado%20"+info2[0].replace(" ","%20")
                            +"%20Soy%20apoderado%20de%20"+info2[5].replace(" ","%20")
                            +".%20He%20recibido%20el%20siguiente%20mensaje%20:%0A"+info2[8].replace(" ","%20")
                            +"%0AAl%20respecto%20le%20comunico%20que%20";

                    Uri uri = Uri.parse(mensajeria);
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    //i.setPackage("com.whatsapp");
                    startActivity(i);

                }


            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MensajeModel mensajeModel = new MensajeModel();
                String info = mensajeModel.getMensajeElegido(getApplicationContext());
                String info2[] = info.split("%");

                String mensajeria="Estimado "+info2[0]
                        +". Soy apoderado de "+info2[5]
                        +". He recibido el siguiente mensaje:\n"+info2[8]
                        +"\nAl respecto le comunico que: ";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{info2[1]});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Respuesta Instant Messaging");
                intent.putExtra(Intent.EXTRA_TEXT, mensajeria);
                startActivity(Intent.createChooser(intent, "Respuesta Instant Messaging"));
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

    @Override
    public void onBackPressed (){
        Intent i = new Intent(DetalleMensaje.this, MensajeManager.class);
        startActivity(i);
    }
}
