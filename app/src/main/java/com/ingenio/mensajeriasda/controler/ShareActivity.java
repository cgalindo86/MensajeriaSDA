package com.ingenio.mensajeriasda.controler;

import static android.os.Environment.DIRECTORY_PICTURES;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ingenio.mensajeriasda.BuildConfig;
import com.ingenio.mensajeriasda.model.Alumno;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.service.Conexion;
import com.ingenio.mensajeriasda.service.ConsultasBD;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ShareActivity extends Activity {

    ImageView guardar;
    Bitmap bitmap = null, bitmap2 = null;
    String nombre="",cursoElegido="",curso="",libro="",mediotxt="";
    Spinner medioPago;
    String[] lista = {"NIUBIZ","YAPE","PLIN"};

    EditText beneficiario,monto,celular,operacion;

    public final Calendar c = Calendar.getInstance();

    private static final String DOS_PUNTOS = ":";

    //Variables para obtener la hora hora
    final int hora = c.get(Calendar.HOUR_OF_DAY);
    final int minuto = c.get(Calendar.MINUTE);
    int mes = c.get(Calendar.MONTH);
    int dia = c.get(Calendar.DAY_OF_MONTH);
    int anio = c.get(Calendar.YEAR);
    int ubicar=1,contA=0,contP=0,contT=0;

    EditText etHora,etFecha;
    ImageButton ibObtenerHora,ibObtenerFecha;
    private static final String CERO = "0";
    private static final String BARRA = "/";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shared_activity);

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.ingenio.mensajeriasda";

        etFecha = (EditText) findViewById(R.id.et_mostrar_fecha_picker);
        etHora = (EditText) findViewById(R.id.et_mostrar_hora_picker);
        ibObtenerFecha = (ImageButton) findViewById(R.id.ib_obtener_fecha);
        ibObtenerHora = (ImageButton) findViewById(R.id.ib_obtener_hora);

        ibObtenerFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ib_obtener_fecha:
                        obtenerFecha();
                        break;
                }
            }
        });

        ibObtenerHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ib_obtener_hora:
                        obtenerHora();
                        break;
                }
            }
        });

        medioPago = (Spinner) findViewById(R.id.medioPago);
        ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lista);
        medioPago.setAdapter(adapter3);
        medioPago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id)
            {
                mediotxt = lista[pos];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                mediotxt = lista[0];
            }
        });

        Alumno alumno = new Alumno();

        beneficiario = (EditText) findViewById(R.id.beneficiario);
        String al[] = alumno.getAlumnoData(alumno.getAlumnoElegido(getApplicationContext()),getApplicationContext()).split("&");
        beneficiario.setText("DNI Alumno: "+al[0]);
        monto = (EditText) findViewById(R.id.monto);
        celular = (EditText) findViewById(R.id.celular);
        operacion = (EditText) findViewById(R.id.operacion);

        guardar = (ImageView) findViewById(R.id.guardar);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else if(Intent.ACTION_VIEW.equals(action) && type != null){
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Guardar(bitmap);
            }
        });

    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {

            String ruta = sharedText;
            Log.e("text",sharedText+"");
            Intent intent1 = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(ruta));
            startActivity(intent);
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            Log.e("imagen uri",imageUri+"");
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ImageView imagen = (ImageView) findViewById(R.id.img);
                imagen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    void handleSendMultipleImages(Intent intent) {
        ArrayList<Uri> imageUris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        if (imageUris != null) {
            // Update UI to reflect multiple images being shared
        }
    }

    private void obtenerFecha(){
        DatePickerDialog recogerFecha = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                //Esta variable lo que realiza es aumentar en uno el mes ya que comienza desde 0 = enero
                final int mesActual = month + 1;
                //Formateo el día obtenido: antepone el 0 si son menores de 10
                String diaFormateado = (dayOfMonth < 10)? CERO + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                //Formateo el mes obtenido: antepone el 0 si son menores de 10
                String mesFormateado = (mesActual < 10)? CERO + String.valueOf(mesActual):String.valueOf(mesActual);
                //Muestro la fecha con el formato deseado
                //etFecha.setText(diaFormateado + BARRA + mesFormateado + BARRA + year);
                etFecha.setText(year + "-" + mesFormateado + "-" + diaFormateado);

            }
            //Estos valores deben ir en ese orden, de lo contrario no mostrara la fecha actual
            /**
             *También puede cargar los valores que usted desee
             */
        },anio, mes, dia);

        //Muestro el widget
        recogerFecha.show();

    }

    private void obtenerHora(){
        TimePickerDialog recogerHora = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //Formateo el hora obtenido: antepone el 0 si son menores de 10
                String horaFormateada =  (hourOfDay < 10)? String.valueOf(CERO + hourOfDay) : String.valueOf(hourOfDay);
                //Formateo el minuto obtenido: antepone el 0 si son menores de 10
                String minutoFormateado = (minute < 10)? String.valueOf(CERO + minute):String.valueOf(minute);
                //Obtengo el valor a.m. o p.m., dependiendo de la selección del usuario
                String AM_PM;
                if(hourOfDay < 12) {
                    AM_PM = "a.m.";
                } else {
                    AM_PM = "p.m.";
                }
                etHora.setText(horaFormateada + DOS_PUNTOS + minutoFormateado);
            }

        }, hora, minuto, false);

        recogerHora.show();
    }

    public void Guardar(Bitmap bitmap){

        Date date = new Date();
        DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        String nhora = hourFormat.format(date)+"";
        Log.e("hora",nhora);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String nfecha = dateFormat.format(date)+"";
        Log.e("fecha",nfecha);

        String beneficiariotxt = beneficiario.getText().toString();
        String montotxt = monto.getText().toString();
        montotxt = montotxt.replace(" ","");


        String operaciontxt = operacion.getText().toString();
        operaciontxt = operaciontxt.replace(" ","");
        String celulartxt = celular.getText().toString();
        celulartxt = celulartxt.replace(" ","");
        String fechatxt = etFecha.getText().toString();
        String horatxt = etHora.getText().toString();

        String ruta_fotos = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/Comprobantes/";
        File file2 = new File(ruta_fotos);

        file2.mkdirs();

        Log.e("data",mediotxt);

        if(!beneficiariotxt.equals("") && !montotxt.equals("") && !celulartxt.equals("") && !fechatxt.equals("") && !horatxt.equals("")){
            Alumno al = new Alumno();
            String ppff = al.getPPFFDni(getApplicationContext());
            String alumno = al.getAlumnoElegido(getApplicationContext());



            nombre = beneficiariotxt.replace(" ","_");
            nombre = nombre.replace("DNI Alumno: ","");
            nombre = nombre + "_"+celulartxt+"_"+fechatxt+"_"+horatxt+".png";


            String file = ruta_fotos+nombre;
            File mi_foto = new File(file);

            try {
                FileOutputStream out;
                out = new FileOutputStream(mi_foto);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                Toast.makeText(getApplicationContext(),"GUARDADO",Toast.LENGTH_LONG).show();
                SubirFoto(ppff,alumno,nombre);
                Toast.makeText(getApplicationContext(), "Enviando archivo", Toast.LENGTH_SHORT).show();

                Alumno alumno1 = new Alumno();
                String rutaSeleccionada = alumno1.getAlumnoRuta(getApplicationContext());

                Conexion conexion = new Conexion();
                String ruta = conexion.getUrl(getApplicationContext());
                ruta = ruta + "/controler/consultaAlumno.php?accionget=9&saldoget="+montotxt+"&alumnoget="+alumno1.getAlumnoElegido(getApplicationContext())+
                        "&ppffget="+alumno1.getPPFFDni(getApplicationContext())+"&medioget="+mediotxt+"&rutaget="+rutaSeleccionada+
                        "&comprobanteget="+nombre+"&operacionget="+operaciontxt+"&celularget="+celulartxt;
                Log.e("rutasaldo",ruta);
                Lee2 lee2 = new Lee2();
                lee2.execute(ruta);

            } catch (IOException ex) {
                Log.e("ERROR ", "Error:" + ex);
            }
        } else {
            Toast.makeText(getApplicationContext(),"VERIFIQUE QUE INGRESÓ LOS DATOS SOLICITADOS",Toast.LENGTH_LONG).show();
        }

    }

    public class Lee2 extends AsyncTask<String,Void,String> {

        ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            progressDoalog = new ProgressDialog(ShareActivity.this);
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
            ConsultasBD bd = new ConsultasBD();
            String datCursos = bd.Consulta(params[0]);
            return datCursos;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDoalog.dismiss();
            Toast.makeText(getApplicationContext(),"PAGO REGISTRADO. SE PROCEDERÁ A VERIFICAR Y LE INFORMAREMOS AL RESPECTO ",Toast.LENGTH_LONG).show();
        }
    }

    public class Insertar extends AsyncTask<String,Void,String> {

        //ProgressDialog progressDoalog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            ConsultasBD bd = new ConsultasBD();
            String datCursos = bd.Consulta(params[0]);
            Log.d("env",params[0]);
            return datCursos;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Log.d("dato result",result);
        }

    }

    public Boolean SubirFoto(String ppff, String alumno, String nombre){
        try {
            String rutaFoto = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/Comprobantes/"+nombre;
            Conexion cn = new Conexion();
            String URL_SUBIRPICTURE=cn.getUrl(getApplicationContext())+"/uploadComprobante.php?ppff="+ppff+"&alumno="+alumno+"&nombre="+nombre;
            Log.d("ruta",URL_SUBIRPICTURE);

            //String codigo_pag2=dni+"#"+curso+"#"+nombre;
            Log.d("ruta foto",rutaFoto); //Log.d("cod2",codigo_pag2);
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getApplicationContext(), uploadId, URL_SUBIRPICTURE)
                    .addFileToUpload(rutaFoto, "picture")
                    .addParameter("name", nombre)
                    .setMaxRetries(2)
                    //.setNotificationConfig(new UploadNotificationConfig())
                    .startUpload();

        } catch (Exception exc) {
            System.out.println(exc.getMessage()+" NO "+exc.getLocalizedMessage());

        }
        return true;
    }

    private final UploadServiceBroadcastReceiver uploadReceiver;

    {
        uploadReceiver = new UploadServiceBroadcastReceiver() {

            @Override
            public void onProgress(String uploadId, int progress) {

            }

            @Override
            public void onProgress(final String uploadId,
                                   final long uploadedBytes,
                                   final long totalBytes) {
            }

            @Override
            public void onError(String uploadId, Exception exception) {
                Toast.makeText(getApplicationContext(), "Error en carga... almacenando en memoria\nEl archivo se enviará automáticamente", Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), "No se puede enviar archivo al servidor", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody){
                Toast.makeText(getApplicationContext(), "Listo! Códgigo: "+serverResponseCode, Toast.LENGTH_SHORT).show();

            }
        };
    }

}
