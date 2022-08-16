package com.ingenio.mensajeriasda.controler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.model.Alumnos;

import java.util.ArrayList;

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
                Alumno alumno = new Alumno();
                if(alumno.getAlumnoOpcion(activity.getApplicationContext()).equals("virtual")){
                    String elegido = alumnos.getDni();
                    String ruta = "http://sdavirtualroom.dyndns.org/sda/ingresoApp2.php?mail="+elegido;

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(ruta));
                    activity.startActivity(intent);
                } else if(alumno.getAlumnoOpcion(activity.getApplicationContext()).equals("declaracion")){
                    String elegido = alumnos.getDni();
                    String ruta = "http://sdavirtualroom.dyndns.org/sda/view/ficha_medica.php?mail="+elegido;

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(ruta));
                    activity.startActivity(intent);
                } else if(alumno.getAlumnoOpcion(activity.getApplicationContext()).equals("covid")){
                    String elegido = alumnos.getDni();
                    String ruta = "http://sdavirtualroom.dyndns.org/sda/view/ficha_sintomatologia.php?mail="+elegido;

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(ruta));
                    activity.startActivity(intent);
                }


            }
        });

        return v;
    }

}

