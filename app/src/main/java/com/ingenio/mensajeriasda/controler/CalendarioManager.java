package com.ingenio.mensajeriasda.controler;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Evento;
import com.ingenio.mensajeriasda.model.Eventos;
import com.ingenio.mensajeriasda.model.MensajeModel;
import com.ingenio.mensajeriasda.service.Conexion;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalendarioManager extends AppCompatActivity {

    ImageView atras;
    CompactCalendarView compactCalendarView;
    ArrayList ListEvento;
    String ruta="http://sdavirtualroom.dyndns.org", anio="", mes="", dia="", fechaActual="", mesActual="";
    long epoch3;
    int val, cont=0, cmes, canio;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    TextView t;
    String elegido="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendario);

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
                Calendario();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                //elegido = valores2[0];
            }
        });
        //Toast.makeText(getApplicationContext(),isConnected+"01",Toast.LENGTH_LONG).show();

    }

    void Calendario(){
        compactCalendarView = (CompactCalendarView) findViewById(R.id.calendar1);
        compactCalendarView.setUseThreeLetterAbbreviation(true);

        ListEvento = new ArrayList<String>();

        Conexion conexion = new Conexion();
        ruta = conexion.getUrl(getApplicationContext());


        Date date = new Date();
        SimpleDateFormat formatterc = new SimpleDateFormat("dd/MM/yyyy");
        String fechac[] = (formatterc.format(date)).split("/");

        mes = fechac[1]; anio = fechac[2]; dia = fechac[0];
        fechaActual = mes+"/"+dia+"/"+anio;
        mesActual = mes;
        cmes = Integer.parseInt(mes);
        canio = Integer.parseInt(fechac[2]);
        Log.e("nmes-canio",mes+"-"+canio);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
        String uu = (sdf.format(date.getTime())).toUpperCase();
        t = (TextView) findViewById(R.id.textoMes);
        t.setText(uu);

        Alumno alumno = new Alumno();
        String dalumno = alumno.getAlumnoElegido(getApplicationContext());
        String dalumno1[] = alumno.getAlumnoData(dalumno,getApplicationContext()).split("&");
        String grado = dalumno1[2];
        String seccion = dalumno1[3];
        String nivel="";
        if(grado.contains("I")){
            nivel = "I";
        } else if(grado.contains("P")){
            nivel = "P";
        } else {
            nivel = "S";
        }
        ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=2&alumnoget="+dalumno
                +"&gradoget="+grado+"&seccionget="+seccion+"&nivelget="+nivel+"&anioget="+anio
                +"&mesget="+mes;
        Log.e("ruta",ruta);
        LeeDatos leeDatos = new LeeDatos();
        leeDatos.execute(ruta);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                String dd[] = (dateClicked+"").split(" ");
                Log.e("dia cambio2", dd[1]+" "+dd[2]+" "+dd[5]);
                String nmes="";
                if(dd[1].equals("Jan")){ nmes = "01"; cmes=1; }
                if(dd[1].equals("Feb")){ nmes = "02"; cmes=2; }
                if(dd[1].equals("Mar")){ nmes = "03"; cmes=3; }
                if(dd[1].equals("Apr")){ nmes = "04"; cmes=4; }
                if(dd[1].equals("May")){ nmes = "05"; cmes=5; }
                if(dd[1].equals("Jun")){ nmes = "06"; cmes=6; }
                if(dd[1].equals("Jul")){ nmes = "07"; cmes=7; }
                if(dd[1].equals("Aug")){ nmes = "08"; cmes=8; }
                if(dd[1].equals("Sep")){ nmes = "09"; cmes=9; }
                if(dd[1].equals("Oct")){ nmes = "10"; cmes=10; }
                if(dd[1].equals("Nov")){ nmes = "11"; cmes=11; }
                if(dd[1].equals("Dec")){ nmes = "12"; cmes=12; }
                //calendarioo.setVisibility(View.VISIBLE);
                anio = dd[5];
                Log.e("dia cambio4", nmes+"/"+dd[2]+"/"+dd[5]);
                String lafecha = nmes+"/"+dd[2]+"/"+dd[5];

                if(nmes.equals(mes)){
                    if(dd[2].equals(dia)){
                        fechaActual = mes+"/"+dia+"/"+anio;
                    } else {
                        fechaActual = mes+"/"+dd[2]+"/"+anio;
                    }

                    mesActual = mes;
                    Log.e("cambioif11",fechaActual);
                } else {
                    if(dd[2].equals("01")){
                        fechaActual = nmes+"/01/"+anio;
                    } else {
                        fechaActual = nmes+"/"+dd[2]+"/"+anio;
                    }
                    //fechaActual = nmes+"/01/"+anio;
                    mesActual = nmes;
                    Log.e("cambioif21",fechaActual);
                }
                //Extraccion(fechaActual,mesActual+"_"+anio,elegido);
                Log.e("extraccion1",fechaActual+"-"+mesActual+"-"+anio+"-"+elegido);
                Extraccion(fechaActual,mesActual+"_"+anio,elegido);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.e("mes cambio", firstDayOfNewMonth+"");
                String dd[] = (firstDayOfNewMonth+"").split(" ");
                Log.e("mes cambio2", dd[0]+" "+dd[1]);
                canio = Integer.parseInt(dd[5]);
                String nmes="";
                if(dd[1].equals("Jan")){ nmes = "01"; cmes=1; }
                if(dd[1].equals("Feb")){ nmes = "02"; cmes=2; }
                if(dd[1].equals("Mar")){ nmes = "03"; cmes=3; }
                if(dd[1].equals("Apr")){ nmes = "04"; cmes=4; }
                if(dd[1].equals("May")){ nmes = "05"; cmes=5; }
                if(dd[1].equals("Jun")){ nmes = "06"; cmes=6; }
                if(dd[1].equals("Jul")){ nmes = "07"; cmes=7; }
                if(dd[1].equals("Aug")){ nmes = "08"; cmes=8; }
                if(dd[1].equals("Sep")){ nmes = "09"; cmes=9; }
                if(dd[1].equals("Oct")){ nmes = "10"; cmes=10; }
                if(dd[1].equals("Nov")){ nmes = "11"; cmes=11; }
                if(dd[1].equals("Dec")){ nmes = "12"; cmes=12; }
                Log.e("mmes__", nmes+"");
                //Lee1("1",nmes);
                String uu = (dateFormat.format(firstDayOfNewMonth)).toUpperCase();
                t.setText(uu);

                Alumno alumno = new Alumno();
                String dalumno = alumno.getAlumnoElegido(getApplicationContext());
                String dalumno1[] = alumno.getAlumnoData(dalumno,getApplicationContext()).split("&");
                String grado = dalumno1[2];
                String seccion = dalumno1[3];
                String nivel="";
                if(grado.contains("I")){
                    nivel = "I";
                } else if(grado.contains("P")){
                    nivel = "P";
                } else {
                    nivel = "S";
                }


                ruta = "http://sdavirtualroom.dyndns.org/sda/controler/consultaAlumno.php?accionget=2&alumnoget="+dalumno
                        +"&gradoget="+grado+"&seccionget="+seccion+"&nivelget="+nivel+"&anioget="+dd[5]
                        +"&mesget="+nmes;
                Log.e("ruta",ruta);

                LeeDatos leeDatos = new LeeDatos();
                leeDatos.execute(ruta);

                if(nmes.equals(mes)){
                    fechaActual = mes+"/"+dia+"/"+anio;
                    mesActual = mes;
                    Log.e("cambioif12",fechaActual);
                } else {
                    fechaActual = nmes+"/01/"+anio;
                    mesActual = nmes;
                    Log.e("cambioif22",fechaActual);
                }
                //pedir info a controler/consultaAlumno.php?accionget=2&alumnoget=12345678&mesget=nmes;
                //EventosEnCalendario("",nmes+"");
            }
        });
    }

    public class LeeDatos extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(CalendarioManager.this);
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
            String dni = params[0];
            String ladata = getDatos(dni);
            return ladata;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            Log.e("rutares",mesActual+"-"+elegido);
            Log.e("rutares",result);
            Eventos eventos = new Eventos();
            eventos.setDatosMes(result,mesActual+"_"+anio,elegido,getApplicationContext());
            //Log.e("resultado", result);
            EventosEnCalendario(result);

        }

    }

    void EventosEnCalendario(String eventos){

        //String ev[] = {"12/10/2018 20:00:00","12/11/2018 20:00:00","12/12/2018 20:00:00","12/14/2018 20:00:00"};
        String valu[] = eventos.split("_##");
        compactCalendarView.removeAllEvents();

        int val = valu.length;
        //Log.d("valu length", val+"");
        for (int i = 0; i < val; i++) {
            //Log.e("ev",valu[i]);
            String matriz[] = valu[i].split("_");
            //Log.e("matriz",valu[i]);

            //String col = Colores(i);
            try {
                epoch3 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(matriz[0]+" 00:00:00").getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }


            ListEvento.add(epoch3+"");
            Log.e("color0",matriz[0]);
            Log.e("color1",matriz[1]);
            Log.e("color2",matriz[2]);
            Log.e("color3",matriz[3]);
            Log.e("color4",matriz[4]);
            Log.e("color5",matriz[5]);
            Log.e("color6",matriz[6]);
            Event event = new Event(Color.parseColor(Colores(matriz[1])), epoch3, matriz[2]);

            compactCalendarView.addEvent(event);

        }
        Log.e("extraccion2",fechaActual+"-"+mesActual+"-"+anio+"-"+elegido);
        Extraccion(fechaActual,mesActual+"_"+anio,elegido);

    }

    public String Colores(String i){
        String micorazon="";
        //Log.e("color2",i);
        if(i.equals("1")){
            //naranja - tareas
            micorazon = "#FA9B19";
        } else
        if(i.equals("2")){
            //morado - materiales
            micorazon = "#800040";//E45D50
        }
        else
        if(i.equals("3")){
            //azul - actividades
            micorazon = "#1919FA";//9E4C6E
        } else
        if(i.equals("4")){
            //rojo - evaluaciones
            micorazon = "#FF0000";//EE9D26
        } else
        if(i.equals("5")){
            //azul - actividades
            micorazon = "#1919FA";//9E4C6E
        } else {
            micorazon = "#000000";
        }
            /*else
        if(i.equals("5")){
            micorazon = "#ff80ff";//4CCEDE
        } else
        if(i.equals("6")){
            micorazon = "#AA60BF";//AA60BF
        } else
        if(i.equals("7")){
            micorazon = "#800000";//717ACF
        } else
        if(i.equals("8")){
            micorazon = "#000000";
        }else
        if(i.equals("9")){
            micorazon = "#FF0000";//AA60BF
        } else {
            micorazon = "#800040";//E45D50
        }*/


        return micorazon;
    }

    void Extraccion(String fecha, String mes, String alumno){
        String datos="";
        Log.e("fechaActual",fecha);
        Log.e("mesActual",mes);
        Eventos eventos = new Eventos();
        String eventos1 = eventos.getDatosMes(mes,alumno,getApplicationContext());
        String valu[] = eventos1.split("#");

        final ArrayList<Evento> arrayList = new ArrayList<Evento>();

        int evaluaciones = 0,tareas = 0,materiales = 0,actividades = 0;
        int val = valu.length;
        //Log.d("valu length", val+"");
        for (int i = 0; i < val-1; i++) {

            String matriz[] = valu[i].split("_");
            Log.e("extra valu",valu[i]);
            //Log.e("mesActual",mes);
            if(fecha.equals(matriz[0])){
                datos = datos + valu[i] + "#";
                if(matriz.length>5){
                    Evento evento = new Evento(matriz[0],matriz[1],matriz[2],matriz[3],matriz[4],matriz[5],matriz[6]);
                    arrayList.add(evento);
                }


                Log.e("extraccion",valu[i]);

                if(matriz[1].equals("1")){
                    tareas++;
                } else if(matriz[1].equals("2")){
                    materiales++;
                } else if(matriz[1].equals("3")){
                    actividades++;
                } else if(matriz[1].equals("4")){
                    evaluaciones++;
                } else if(matriz[1].equals("5")){
                    actividades++;
                }
            }
        }

        final ListView lista = (ListView) findViewById(R.id.listaeventos);
        AdapterEventos adapterEventos = new AdapterEventos(this,arrayList);
        lista.setAdapter(adapterEventos);

        TextView t1,t2,t3,t4;
        t1 = (TextView) findViewById(R.id.restareas);
        if(tareas==1){
            t1.setText(tareas + " resultado");
        } else {
            t1.setText(tareas + " resultados");
        }

        t2 = (TextView) findViewById(R.id.resmateriales);
        if(materiales==1){
            t2.setText(materiales + " resultado");
        } else {
            t2.setText(materiales + " resultados");
        }

        t3 = (TextView) findViewById(R.id.resactividades);
        if(actividades==1){
            t3.setText(actividades + " resultado");
        } else {
            t3.setText(actividades + " resultados");
        }

        t4 = (TextView) findViewById(R.id.resevaluaciones);
        if(evaluaciones==1){
            t4.setText(evaluaciones + " resultado");
        } else {
            t4.setText(evaluaciones + " resultados");
        }

        final Button detalle = (Button) findViewById(R.id.detalle);
        final LinearLayout bloque = (LinearLayout) findViewById(R.id.bloque);

        lista.setVisibility(View.GONE);
        bloque.setVisibility(View.VISIBLE);

        int total = tareas+actividades+materiales+evaluaciones;
        if(total>0){
            detalle.setVisibility(View.VISIBLE);

        } else {
            detalle.setVisibility(View.GONE);

        }

        final String finalDatos = datos;
        detalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bloque.setVisibility(View.GONE);
                detalle.setVisibility(View.GONE);
                lista.setVisibility(View.VISIBLE);
                Log.e("datos", finalDatos);
            }
        });


    }

    public String getDatos(String entrada) {
        URL alumUrl = null;
        //Class<java.net.URL> aClass = java.net.URL.class;
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
        Log.d("Consulta",url2);
        return url2;
    }

}
