package com.pettrack.pettrack.historialmedico;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.pettrack.pettrack.R;

public class PantallaPrincipal extends AppCompatActivity {

    private Button btnMas;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_medico);

        // Vincula los elementos del layout con el código Java
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnMas = findViewById(R.id.btnMas);

        // Acción del botón "Más"
        btnMas.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaPrincipal.this, HistorialMedico.class);
            startActivity(intent);
        });
    }
}
