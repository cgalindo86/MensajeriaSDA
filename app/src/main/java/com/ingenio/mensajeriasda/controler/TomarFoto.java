package com.ingenio.mensajeriasda.controler;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.*;
import net.gotev.uploadservice.UploadService;

import com.ingenio.mensajeriasda.BuildConfig;
import com.ingenio.mensajeriasda.R;
import com.ingenio.mensajeriasda.model.Alumno;

import static android.os.Environment.DIRECTORY_PICTURES;

import org.json.JSONObject;

/**
 * Created by cgali on 21/12/2017.
 */

public class TomarFoto extends AppCompatActivity {

    private static int REQUEST_CODE_TAKE_PHOTO = 1;
    String name="", nombreFoto="", nombre="", ruta_fotos="";
    String alumno="", grado="", seccion="";
    Bitmap bitmap = null, bitmap2 = null;
    int num=0,cfoto;
    private String mCurrentPhotoPath;
    Uri photoURI;
    File image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tomar_foto);

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        UploadService.NAMESPACE = "com.ingenio.mensajeriasda";


        if(Build.VERSION.SDK_INT>=24){
            try{
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        Permisos();

        Alumno mialumno = new Alumno();
        String elegido = mialumno.getAlumnoElegido(getApplicationContext());
        //alumno.setAlumnoElegido(elegido,getApplicationContext());
        Log.e("eee",mialumno.getAlumnoData(elegido,getApplicationContext()));
        String gr[] = mialumno.getAlumnoData(elegido,getApplicationContext()).split("&");
        alumno = gr[0];
        grado = gr[2]; seccion = gr[3];

        Button btnAction = (Button)findViewById(R.id.btn);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permisos2();

                nombre = alumno+".png";
                ruta_fotos = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/Fotos/";
                name = ruta_fotos + nombre;
                dispatchTakePictureIntent();

            }
        });

        Button btn2 = (Button)findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*nombre = alumno+".png";
                ruta_fotos = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/Fotos/";
                name = ruta_fotos + nombre;
                File file2 = new File(ruta_fotos);

                file2.mkdirs();
                String file = ruta_fotos+nombre;
                File mi_foto = new File(file);
                FileOutputStream out;

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                    bitmap = rotateBitmap(bitmap,-90);
                    out = new FileOutputStream(mi_foto);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //Se guarda el bitmap en memoria, en la ruta almacenada


                Guardar();

            }
        });

        Button btn3 = (Button)findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bitmap = rotateBitmap(bitmap,-90);
                ImageView iv = (ImageView)findViewById(R.id.foto);
                iv.setImageBitmap(bitmap);
            }
        });
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();

            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                Log.e("nofoto",ex+"");
            }

            if (photoFile != null) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                photoURI = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                Log.e("photofile",""+photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        Log.e("sifoto",""+name);
        File file2 = new File(ruta_fotos);
        file2.mkdirs();
        String file = name;
        image = new File(file);
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {

            //Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoURI);
                bitmap = rotateBitmap(bitmap,-90);
                FileOutputStream out;
                out = new FileOutputStream(image);
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, out);
                out.flush();
                out.close();
                ImageView iv = (ImageView)findViewById(R.id.foto);
                iv.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                Log.e("exc1",""+e);
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("exc2",""+e);
                e.printStackTrace();
            }

        }
    }

    public Bitmap rotateBitmap(Bitmap original, float degrees) {
        int width = original.getWidth();
        int height = original.getHeight();

        Matrix matrix = new Matrix();
        matrix.preRotate(degrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(original, 0, 0, width, height, matrix, true);

        return rotatedBitmap;
    }

    public void Guardar(){

        try {

            String rutaFoto = Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)+"/Fotos/"+alumno+".png";
            //Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES)
            String URL_SUBIRPICTURE="";
            URL_SUBIRPICTURE="http://sdavirtualroom.dyndns.org/sda/uploadFoto.php?alumno="+alumno+"&grado="+grado+"&seccion="+seccion;
            Log.e("ruta",URL_SUBIRPICTURE);

            Log.e("ruta foto",rutaFoto);
            String uploadId = UUID.randomUUID().toString();
            new MultipartUploadRequest(getApplicationContext(), uploadId, URL_SUBIRPICTURE)
                    .addFileToUpload(rutaFoto, "picture")
                    .addParameter("name", alumno+".png")
                    .setMaxRetries(2)
                    .startUpload();

        } catch (Exception exc) {
            Log.e("exc",exc.getMessage()+" NO "+exc.getLocalizedMessage());
            System.out.println(exc.getMessage()+" NO "+exc.getLocalizedMessage());

        }
        //return true;
    }

    private final UploadServiceBroadcastReceiver uploadReceiver;

    {
        uploadReceiver = new UploadServiceBroadcastReceiver() {

            @Override
            public void onProgress(String uploadId, int progress) {
                Log.e("n1",progress+"");
            }

            @Override
            public void onProgress(final String uploadId,
                                   final long uploadedBytes,
                                   final long totalBytes) {
                //Toast.makeText(getApplicationContext(), "Enviando...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(String uploadId, Exception exception) {
                Toast.makeText(getApplicationContext(), "No se puede enviar archivo al servidor", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted(String uploadId, int serverResponseCode, byte[] serverResponseBody){
                Log.e("n1",uploadId);
                Log.e("n2",serverResponseCode+"");
                Log.e("n3",serverResponseBody+"");
                Toast.makeText(getApplicationContext(), "Listo !!! ", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(TomarFoto.this,MiQr.class);
                startActivity(i);
            }
        };
    }


    private void Permisos(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},101);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
    }

    private void Permisos2(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);

    }

}
