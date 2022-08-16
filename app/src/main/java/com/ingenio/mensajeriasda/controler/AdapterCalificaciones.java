package com.ingenio.mensajeriasda.controler;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Calificaciones;

import java.util.ArrayList;

public class AdapterCalificaciones extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Calificaciones> items;

    public AdapterCalificaciones(Activity activity, ArrayList arrayList) {
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
            v = ly.inflate(R.layout.item_calificaciones,null);
        }

        final Calificaciones calificaciones = items.get(position);

        TextView t = (TextView) v.findViewById(R.id.miembro);
        t.setText(calificaciones.getItem());

        TextView t2 = (TextView) v.findViewById(R.id.nota);

        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.califica);
        linearLayout.setBackgroundColor(Color.parseColor("#"+calificaciones.getcolor()));
        /*if(calificaciones.getTipo().equals("1")){
            linearLayout.setBackgroundColor(Color.parseColor("#F2C9A4"));
        } else {
            linearLayout.setBackgroundColor(Color.parseColor("#DAF1CC"));
        }*/

        if(calificaciones.getNota().equals("AD")){
            t2.setTextColor(Color.parseColor("#335BF1"));
            t2.setText(calificaciones.getNota());
        } else if(calificaciones.getNota().equals("A")){
            t2.setTextColor(Color.parseColor("#335BF1"));
            t2.setText(calificaciones.getNota());
        } else if(calificaciones.getNota().equals("B")){
            t2.setTextColor(Color.parseColor("#335BF1"));
            t2.setText(calificaciones.getNota());
        } else if(calificaciones.getNota().equals("C")){
            t2.setTextColor(Color.parseColor("#F13333"));
            t2.setText("*");
            //t2.setText();
        } else if(calificaciones.getNota().equals(" ")){
            t2.setText(" ");
        } else {
            int lanota = Integer.parseInt(calificaciones.getNota());
            if(lanota>11){
                t2.setTextColor(Color.parseColor("#335BF1"));
            } else {
                t2.setTextColor(Color.parseColor("#F13333"));
            }
        }


        return v;
    }

    public static boolean esMayuscula(String s) {
        // Regresa el resultado de comparar la original con su versión mayúscula
        return s.equals(s.toUpperCase());
    }

}

