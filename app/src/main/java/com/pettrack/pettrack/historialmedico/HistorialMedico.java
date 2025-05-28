package com.pettrack.pettrack.historialmedico;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.api.ApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialMedico extends AppCompatActivity {
    private static final int REQUEST_CODE_ARCHIVO = 100;
    private ApiService apiService;
    private int mascotaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_medico2);

        mascotaId = getIntent().getIntExtra("mascotaId", 0);
        apiService = ApiClient.getClient().create(ApiService.class);

        Button btnSubirArchivo = findViewById(R.id.btnSubirArchivo);
        btnSubirArchivo.setOnClickListener(v -> seleccionarArchivo());
    }

    private void seleccionarArchivo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Selecciona un archivo"), REQUEST_CODE_ARCHIVO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ARCHIVO && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            subirArchivo(uri);
        }
    }

    private void subirArchivo(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            String nombre = obtenerNombreArchivo(uri);

            File file = new File(getCacheDir(), nombre);
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            inputStream.close();
            outputStream.close();

            String tipoMime = getContentResolver().getType(uri);
            if (tipoMime == null) {
                tipoMime = "application/octet-stream"; // Valor por defecto
            }
            RequestBody requestFile = RequestBody.create(MediaType.parse(tipoMime), file);            MultipartBody.Part body = MultipartBody.Part.createFormData("archivo", file.getName(), requestFile);

            apiService.subirArchivo(mascotaId, body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Toast.makeText(HistorialMedico.this, "Archivo subido", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(HistorialMedico.this, "Error al subir archivo", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obtenerNombreArchivo(Uri uri) {
        String nombre = "archivo";
        try (android.database.Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int nombreIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME);
                nombre = cursor.getString(nombreIndex);            }
        }
        return nombre;
    }
}