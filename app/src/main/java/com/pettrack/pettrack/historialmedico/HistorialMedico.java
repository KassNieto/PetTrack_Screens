package com.pettrack.pettrack.historialmedico;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.pettrack.pettrack.R;

public class HistorialMedico extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private Button btnSubirArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_medico2);

        btnSubirArchivo = findViewById(R.id.btnSubirArchivo);

        btnSubirArchivo.setOnClickListener(v -> {
            // Intent para seleccionar un archivo
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, "Selecciona un archivo"), PICK_FILE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedFileUri = data.getData();
            if (selectedFileUri != null) {
                // Aquí se puede subir al servidor o a la base de datos en la nube
                Toast.makeText(this, "Archivo seleccionado: " + selectedFileUri.getPath(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

