package com.ingenio.mensajeriasda.controler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
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

public class AdapterAlumnos extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Alumnos> items;

    public AdapterAlumnos(Activity activity, ArrayList arrayList) {
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
            v = ly.inflate(R.layout.itemspinner2,null);
        }

        final Alumnos alumnos = items.get(position);

        TextView t = (TextView) v.findViewById(android.R.id.text1);
        t.setText(alumnos.getNombre());
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String elegido = alumnos.getDni();
                String ruta = "http://sdavirtualroom.dyndns.org/sda/ingresoApp.php?mail="+elegido;

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(ruta));
                activity.startActivity(intent);

            }
        });

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

