package com.pettrack.pettrack.cartillavacunacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pettrack.pettrack.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AgregarDesparasitacion extends AppCompatActivity {

    private EditText addFAplicacionDes, addPrxAplicacionDes, addDosis, addPesoDes;
    private Button btnGuardarDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desparasitacion);

        Toolbar toolbar = findViewById(R.id.toolbarDes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Desparasitación");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

        addFAplicacionDes = findViewById(R.id.addFAplicacionDes);
        addPrxAplicacionDes = findViewById(R.id.addPrxAplicacionDes);
        addDosis = findViewById(R.id.addDosis);
        addPesoDes = findViewById(R.id.addPesoDes);
        btnGuardarDes = findViewById(R.id.btnGuardarDes);

        btnGuardarDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fecha = addFAplicacionDes.getText().toString().trim();
                String proxima = addPrxAplicacionDes.getText().toString().trim();
                String dosis = addDosis.getText().toString().trim();
                String peso = addPesoDes.getText().toString().trim();

                if (fecha.isEmpty() || proxima.isEmpty() || dosis.isEmpty() || peso.isEmpty()) {
                    Toast.makeText(AgregarDesparasitacion.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(AgregarDesparasitacion.this, "Desparasitación guardada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
