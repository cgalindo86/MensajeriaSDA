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
import com.ingenio.mensajeriasda.model.Pago;

import java.util.ArrayList;

public class AdapterPagos extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Pago> items;

    public AdapterPagos(Activity activity, ArrayList arrayList) {
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
            v = ly.inflate(R.layout.pagos,null);
        }

        final Pago pago = items.get(position);


        // formattedDate have current date/time
        TextView t = (TextView) v.findViewById(R.id.mes);
        t.setText(pago.getMes());

        TextView t2 = (TextView) v.findViewById(R.id.estado);
        t2.setText(pago.getEstado());

        TextView t3 = (TextView) v.findViewById(R.id.importe);
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.pagos);
        if(pago.getEstado().equals("PENDIENTE")){
            linearLayout.setBackgroundColor(Color.parseColor("#F5D1D1"));
            t3.setVisibility(View.VISIBLE);
            t3.setText(pago.getImporte()+" Soles");
        } else {
            linearLayout.setBackgroundColor(Color.parseColor("#D2F5D1"));
            t3.setVisibility(View.GONE);
            t3.setText("");
        }

        return v;
    }




}

