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

}

