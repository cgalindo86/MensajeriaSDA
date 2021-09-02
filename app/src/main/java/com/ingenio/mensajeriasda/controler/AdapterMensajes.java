package com.ingenio.mensajeriasda.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ingenio.mensajeriasda.MainActivity;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.MensajeModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AdapterMensajes extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Mensaje> items;

    public AdapterMensajes(Activity activity, ArrayList arrayList) {
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
            v = ly.inflate(R.layout.items,null);
        }

        final Mensaje mensajes = items.get(position);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        TextView t = (TextView) v.findViewById(R.id.fecha);
        if(formattedDate.equals(mensajes.getFecha())){
            t.setText(mensajes.getHora());
        } else {
            t.setText(mensajes.getFecha());
        }


        TextView t2 = (TextView) v.findViewById(R.id.asunto);
        String res = mensajes.getAsunto();
        t2.setText(""+res);

        TextView t21 = (TextView) v.findViewById(R.id.tipo);
        if(res.contains("tarde a la clase")){
            t21.setText("T");
        } else if(res.contains("no ha ingresado")){
            t21.setText("F");
        } else if(res.contains("cámara") || res.contains("camara") || res.contains("Cámara")){
            t21.setText("C");
        } else if(res.contains("no responde")){
            t21.setText("N");
        }
        TextView t3 = (TextView) v.findViewById(R.id.responsable);
        t3.setText(""+mensajes.getSupervisor());

        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.filaitem);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String info = mensajes.getSupervisor()+"%"+mensajes.getSupervisorMail()+"%"+mensajes.getSupervisorCelular()+"%"
                        +mensajes.getId_ppff()+"%"+mensajes.getAlumno()+"%"+mensajes.getAlumnoNombre()+"%"
                        +mensajes.getFecha()+"%"+mensajes.getHora()+"%"+mensajes.getAsunto()+"%"
                        +mensajes.getCurso()+"%";
                MensajeModel mensajeModel = new MensajeModel();
                mensajeModel.setMensajeElegido(info,activity);
                Intent intent = new Intent(activity, DetalleMensaje.class);
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


}

