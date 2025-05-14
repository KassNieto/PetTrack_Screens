package com.pettrack.pettrack.cartillavacunacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pettrack.pettrack.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AgregarVacuna extends AppCompatActivity {

    private EditText addFAplicacionVac, addPrxAplicacionVac, addTipoVac;
    private Button btnGuardarVac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacunacion);

        // Toolbar con flecha de regreso
        Toolbar toolbar = findViewById(R.id.toolbarVac);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Vacunación");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());

        // Referencias a los campos
        addFAplicacionVac = findViewById(R.id.addFAplicacionVac);
        addPrxAplicacionVac = findViewById(R.id.addPrxAplicacionVac);
        addTipoVac = findViewById(R.id.addTipoVac);
        btnGuardarVac = findViewById(R.id.btnGuardarVac);


        btnGuardarVac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechaAplicacion = addFAplicacionVac.getText().toString().trim();
                String proximaAplicacion = addPrxAplicacionVac.getText().toString().trim();
                String tipoVacuna = addTipoVac.getText().toString().trim();

                if (fechaAplicacion.isEmpty() || proximaAplicacion.isEmpty() || tipoVacuna.isEmpty()) {
                    Toast.makeText(AgregarVacuna.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(AgregarVacuna.this, "Vacunación registrada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}
