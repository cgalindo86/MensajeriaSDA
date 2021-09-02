package com.ingenio.mensajeriasda.controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AdapterEventos extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Evento> items;

    public AdapterEventos(Activity activity, ArrayList arrayList) {
        this.activity = activity;
        this.items = arrayList;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (convertView==null){
            LayoutInflater ly = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = ly.inflate(R.layout.items2,null);
        }

        final Evento evento = items.get(position);

        Button btn = (Button) v.findViewById(R.id.asistire);
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Alumno alumno = new Alumno();
                String nalumno = alumno.getAlumnoElegido(activity);
                String nombre[] = alumno.getAlumnoData(nalumno,activity).split("&");

                String mensajeria="Estimado CAMILO GALINDO"
                        +". Soy apoderado de "+nombre[1]
                        +". Confirmo mi asistencia al evento:\n"+evento.getDetalle()
                        +"\n";

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("plain/text");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"cgalindo@sda.edu.pe"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Confirmar asistencia a evento SDA");
                intent.putExtra(Intent.EXTRA_TEXT, mensajeria);
                activity.startActivity(Intent.createChooser(intent, "Confirmar asistencia a evento SDA"));
            }
        });

        RelativeLayout relativeLayout = (RelativeLayout) v.findViewById(R.id.tipo);
        if(evento.getTipo().equals("1")){
            relativeLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.bordetareas));
            btn.setVisibility(View.GONE);
        } else if(evento.getTipo().equals("2")){
            relativeLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.bordemateriales));
            btn.setVisibility(View.GONE);
        } else if(evento.getTipo().equals("3")){
            relativeLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.bordeactividades));
            btn.setVisibility(View.GONE);
        } else if(evento.getTipo().equals("4")){
            relativeLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.bordeevaluaciones));
            btn.setVisibility(View.GONE);
        } else if(evento.getTipo().equals("5")){
            relativeLayout.setBackground(ContextCompat.getDrawable(activity, R.drawable.bordeactividades));
            btn.setVisibility(View.VISIBLE);
        }

        /*LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.responder);
        if(evento.getTipo().equals("3")){
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }*/

        TextView t = (TextView) v.findViewById(R.id.hora);
        t.setText(evento.getHora());

        TextView t2 = (TextView) v.findViewById(R.id.detalle);
        String res = evento.getDetalle();
        t2.setText(""+res);

        TextView t3 = (TextView) v.findViewById(R.id.curso);
        if(evento.getCurso().equals("N")){
            t3.setText("");
        } else {
            t3.setText(""+evento.getCurso());
        }

        TextView t4 = (TextView) v.findViewById(R.id.nota);
        t4.setText(evento.getNota());



        return v;
    }

    public Bitmap redimensionar(Bitmap mBitmap, float newWidth, float newHeigth){
        //Redimensionamos
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeigth) / height;
        // create a matrix for the manipulation
        Matrix matrix = new Matrix();
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false);
    }

    private class MyHttpPostRequest extends AsyncTask<String, Integer, String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(activity);
            progressDoalog.setMax(100);
            progressDoalog.setMessage("Enviando confirmación");
            progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDoalog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        protected String doInBackground(String... params) {

            BufferedReader in = null;
            String url = params[0];
            Log.e("url",url);

            HttpClient httpClient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost(url);
            HttpGet httpGet = new HttpGet(url);

            try {
                //List<BasicNameValuePair> postValues = new ArrayList<>(2);
                List<BasicNameValuePair> getValues = new ArrayList<>(2);
                //postValues.add(new BasicNameValuePair("accion", "mailAcad"));
                getValues.add(new BasicNameValuePair("funcion","envioConfirmacion"));
                getValues.add(new BasicNameValuePair("responsable","cgalindo@sda.edu.pe"));
                getValues.add(new BasicNameValuePair("padre","Padre_de_prueba"));
                getValues.add(new BasicNameValuePair("mensaje","Mensaje_de_prueba"));
                //httpGet
                //httppost.setEntity(new UrlEncodedFormEntity(getValues,"UTF-8"));

                //Hace la petición
                HttpResponse response = httpClient.execute(httpGet);
                Log.e("eee3", response.getStatusLine().toString());

                //Obtengo el contenido de la respuesta en formato InputStream Buffer y la paso a formato String
                in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer sb = new StringBuffer("");
                String line = "";
                String NL = System.getProperty("line.separator");
                while ((line = in.readLine()) != null) {
                    sb.append(line + NL);
                }
                in.close();
                return sb.toString();

            } catch (Exception e) {
                return "Exception happened: " + e.getMessage();
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

        protected void onProgressUpdate(Integer... progress) {
            //Se obtiene el progreso de la peticion
            Log.e("eee4","Indicador de pregreso " + progress[0].toString());
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("result mensaje 2",result);
            progressDoalog.dismiss();
        }
    }


}

