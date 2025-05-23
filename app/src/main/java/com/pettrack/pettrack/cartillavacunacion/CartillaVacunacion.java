package com.pettrack.pettrack.cartillavacunacion;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.cartillavacunacion.Vacuna;
import com.pettrack.pettrack.models.cartillavacunacion.Desparasitacion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartillaVacunacion extends AppCompatActivity {

    private Button btnVacunacion;
    private Button btnDesparasitacion;
    private TableLayout tableVacunas;
    private TableLayout tableDesparasitacion;

    private int idMascota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartilla_vacunacion);

        btnVacunacion = findViewById(R.id.btnVacunacion);
        btnDesparasitacion = findViewById(R.id.btnDesparasitacion);
        tableVacunas = findViewById(R.id.tableVacunas);
        tableDesparasitacion = findViewById(R.id.tableDesparasitacion);

        idMascota = getIntent().getIntExtra("idMascota", -1);
        if (idMascota == -1) {
            idMascota = 6;  // Cambia por un ID válido en tu backend
        }

        btnVacunacion.setOnClickListener(v -> {
            Intent intent = new Intent(CartillaVacunacion.this, AgregarVacuna.class);
            intent.putExtra("idMascota", idMascota);
            startActivity(intent);
        });

        btnDesparasitacion.setOnClickListener(v -> {
            Intent intent = new Intent(CartillaVacunacion.this, AgregarDesparasitacion.class);
            intent.putExtra("idMascota", idMascota);
            startActivity(intent);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        tableVacunas.removeAllViews();
        tableDesparasitacion.removeAllViews();

        agregarEncabezados();  // Pon encabezados después de limpiar

        cargarVacunas();
        cargarDesparasitaciones();
    }

    private void agregarEncabezados() {
        TableRow encabezadoVacunas = new TableRow(this);
        encabezadoVacunas.addView(crearTextoEncabezado("Fecha Aplicación"));
        encabezadoVacunas.addView(crearTextoEncabezado("Tipo Vacuna"));
        encabezadoVacunas.addView(crearTextoEncabezado("Próxima Aplicación"));
        tableVacunas.addView(encabezadoVacunas);

        TableRow encabezadoDesparasitacion = new TableRow(this);
        encabezadoDesparasitacion.addView(crearTextoEncabezado("Fecha    Aplicación"));
        encabezadoDesparasitacion.addView(crearTextoEncabezado("Peso (kg)"));
        encabezadoDesparasitacion.addView(crearTextoEncabezado("Dosis (ml)"));
        encabezadoDesparasitacion.addView(crearTextoEncabezado("Próxima Aplicación"));
        tableDesparasitacion.addView(encabezadoDesparasitacion);
    }

    private TextView crearTextoEncabezado(String texto) {
        TextView textView = new TextView(this);
        textView.setText(texto);
        textView.setTextSize(16);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(16, 8, 16, 8); // Espaciado más amplio
        textView.setGravity(Gravity.CENTER);

        // Permitir múltiples líneas y evitar que se corte el texto
        textView.setSingleLine(false);
        textView.setMaxLines(3); // Puedes ajustar este número si hace falta
        textView.setEllipsize(null);

        // Usar LayoutParams con pesos para que las columnas se distribuyan equitativamente
        TableRow.LayoutParams params;

        if (texto.equals("Próxima Aplicación")) {
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f); // más espacio
        } else {
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        }

        textView.setLayoutParams(params);

        return textView;
    }


    private void cargarVacunas() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Vacuna>> call = apiService.getVacunasByMascotaId(idMascota);
        call.enqueue(new Callback<List<Vacuna>>() {
            @Override
            public void onResponse(Call<List<Vacuna>> call, Response<List<Vacuna>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vacuna> vacunas = response.body();
                    if (vacunas.isEmpty()) {
                        Toast.makeText(CartillaVacunacion.this, "No hay vacunas registradas", Toast.LENGTH_SHORT).show();
                    } else {
                        for (Vacuna vacuna : vacunas) {
                            String fechaFormateada = formatearFechaSoloDia(vacuna.getFechaAplicacion());
                            String proximaFormateada = formatearFechaSoloDia(vacuna.getProximaAplicacion());

                            Log.d("CartillaVacunacion", "Próxima aplicación vacuna: " + vacuna.getProximaAplicacion());

                            agregarFilaVacuna(fechaFormateada, vacuna.getTipoVacuna(), proximaFormateada);
                        }
                    }
                } else {
                    Toast.makeText(CartillaVacunacion.this, "No se pudieron cargar las vacunas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Vacuna>> call, Throwable t) {
                Toast.makeText(CartillaVacunacion.this, "Error de red al obtener vacunas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarDesparasitaciones() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Desparasitacion>> call = apiService.getDesparasitacionesByMascotaId(idMascota);
        call.enqueue(new Callback<List<Desparasitacion>>() {
            @Override
            public void onResponse(Call<List<Desparasitacion>> call, Response<List<Desparasitacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Desparasitacion> desparasitaciones = response.body();
                    if (desparasitaciones.isEmpty()) {
                        Toast.makeText(CartillaVacunacion.this, "No hay desparasitaciones registradas", Toast.LENGTH_SHORT).show();
                    } else {
                        for (Desparasitacion d : desparasitaciones) {
                            String fechaFormateada = formatearFechaSoloDia(d.getFechaAplicacion());
                            String proximaFormateada = formatearFechaSoloDia(d.getProximaAplicacion());

                            Log.d("CartillaVacunacion", "Próxima aplicación desparasitacion: " + d.getProximaAplicacion());

                            agregarFilaDesparasitacion(fechaFormateada, d.getPeso(), d.getDosis(), proximaFormateada);
                        }
                    }
                } else {
                    Toast.makeText(CartillaVacunacion.this, "No se pudieron cargar las desparasitaciones", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Desparasitacion>> call, Throwable t) {
                Toast.makeText(CartillaVacunacion.this, "Error de red al obtener desparasitaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarFilaVacuna(String fecha, String vacuna, String proxima) {
        TableRow fila = new TableRow(this);
        fila.addView(crearTextoFila(fecha));
        fila.addView(crearTextoFila(vacuna));
        fila.addView(crearTextoFila(proxima));
        tableVacunas.addView(fila);
    }

    private void agregarFilaDesparasitacion(String fecha, String peso, String dosis, String proxima) {
        TableRow fila = new TableRow(this);
        fila.addView(crearTextoFila(fecha));
        fila.addView(crearTextoFila(peso));
        fila.addView(crearTextoFila(dosis));
        fila.addView(crearTextoFila(proxima));
        tableDesparasitacion.addView(fila);
    }

    private TextView crearTextoFila(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto);
        tv.setPadding(8, 8, 8, 8);
        return tv;
    }

    // Formatea fecha "yyyy-MM-dd'T'HH:mm:ss" a "dd/MM/yyyy"
    private String formatearFechaSoloDia(String fechaOriginal) {
        if (fechaOriginal == null || fechaOriginal.isEmpty()) return "";

        try {
            SimpleDateFormat formatoOriginal = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            Date fecha = formatoOriginal.parse(fechaOriginal);

            SimpleDateFormat formatoNuevo = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return formatoNuevo.format(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
            return fechaOriginal; // Si no pudo parsear, devuelve la original
        }
    }
}
