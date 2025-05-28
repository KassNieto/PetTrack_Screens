package com.pettrack.pettrack.cartillavacunacion;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.pettrack.pettrack.R;

import androidx.appcompat.app.AppCompatActivity;

public class CartillaVacunacion extends AppCompatActivity {

    private Button btnVacunacion;
    private Button btnDesparasitacion;

    private TableLayout tableVacunas;
    private TableLayout tableDesparasitacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartilla_vacunacion);

        // Enlazamos los botones
        btnVacunacion = findViewById(R.id.btnVacunacion);
        btnDesparasitacion = findViewById(R.id.btnDesparasitacion);

        // Enlazamos las tablas
        tableVacunas = findViewById(R.id.tableVacunas);
        tableDesparasitacion = findViewById(R.id.tableDesparasitacion);

        // Acciones de los botones
        btnVacunacion.setOnClickListener(v -> {
            Intent intent = new Intent(CartillaVacunacion.this, AgregarVacuna.class);
            startActivity(intent);
        });

        btnDesparasitacion.setOnClickListener(v -> {
            Intent intent = new Intent(CartillaVacunacion.this, AgregarDesparasitacion.class);
            startActivity(intent);
        });

        //EJEMPLOS PARA VISUALIZACION
        agregarFilaVacuna("10/05/2025", "Rabia", "10/11/2025");
        agregarFilaDesparasitacion("10/05/2025", "12.5", "2 ml", "10/11/2025");
    }

    private void agregarFilaVacuna(String fecha, String vacuna, String proxima) {
        TableRow fila = new TableRow(this);
        fila.addView(crearTexto(fecha));
        fila.addView(crearTexto(vacuna));
        fila.addView(crearTexto(proxima));
        tableVacunas.addView(fila);
    }

    private void agregarFilaDesparasitacion(String fecha, String peso, String dosis, String proxima) {
        TableRow fila = new TableRow(this);
        fila.addView(crearTexto(fecha));
        fila.addView(crearTexto(peso));
        fila.addView(crearTexto(dosis));
        fila.addView(crearTexto(proxima));
        tableDesparasitacion.addView(fila);
    }

    private TextView crearTexto(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }
}
