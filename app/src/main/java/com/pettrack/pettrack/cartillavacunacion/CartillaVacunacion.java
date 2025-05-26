
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

import androidx.appcompat.app.AppCompatActivity;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.cartillavacunacion.Desparasitacion;
import com.pettrack.pettrack.models.cartillavacunacion.Vacuna;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartillaVacunacion extends AppCompatActivity {

    private static final int REQUEST_ADD_VACUNA = 1;
    private static final int REQUEST_ADD_DESPARASITACION = 2;

    private Button btnVacunacion, btnDesparasitacion;
    private TableLayout tableVacunas, tableDesparasitacion;
    private int mascotaId;
    private final SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartilla_vacunacion);

        // Obtener el ID de la mascota enviado desde la actividad anterior
        mascotaId = getIntent().getIntExtra("mascotaId", -1);
        if (mascotaId == -1) {
            Toast.makeText(this, "Error: No se recibió el ID de mascota", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Log.d("CartillaVacunacion", "mascotaId recibido: " + mascotaId);

        initViews();
        setupButtons();

        // Carga inicial de datos
        agregarEncabezados();
        cargarVacunasDesdeAPI();
        cargarDesparasitacionesDesdeAPI();
    }

    private void initViews() {
        btnVacunacion = findViewById(R.id.btnVacunacion);
        btnDesparasitacion = findViewById(R.id.btnDesparasitacion);
        tableVacunas = findViewById(R.id.tableVacunas);
        tableDesparasitacion = findViewById(R.id.tableDesparasitacion);
    }

    private void setupButtons() {
        btnVacunacion.setOnClickListener(v -> {
            Intent intent = new Intent(CartillaVacunacion.this, AgregarVacuna.class);
            intent.putExtra("mascotaId", mascotaId);
            startActivityForResult(intent, REQUEST_ADD_VACUNA);
        });

        btnDesparasitacion.setOnClickListener(v -> {
            Intent intent = new Intent(CartillaVacunacion.this, AgregarDesparasitacion.class);
            intent.putExtra("mascotaId", mascotaId);
            startActivityForResult(intent, REQUEST_ADD_DESPARASITACION);
        });
    }

    // Cuando se regresa de agregar vacuna o desparasitacion, recarga datos
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_ADD_VACUNA) {
                limpiarTablaVacunas();
                agregarEncabezadosVacunas();
                cargarVacunasDesdeAPI();
            } else if (requestCode == REQUEST_ADD_DESPARASITACION) {
                limpiarTablaDesparasitacion();
                agregarEncabezadosDesparasitacion();
                cargarDesparasitacionesDesdeAPI();
            }
        }
    }

    private void limpiarTablaVacunas() {
        if (tableVacunas.getChildCount() > 1) {
            tableVacunas.removeViews(1, tableVacunas.getChildCount() - 1);
        }
    }

    private void limpiarTablaDesparasitacion() {
        if (tableDesparasitacion.getChildCount() > 1) {
            tableDesparasitacion.removeViews(1, tableDesparasitacion.getChildCount() - 1);
        }
    }

    private void agregarEncabezados() {
        agregarEncabezadosVacunas();
        agregarEncabezadosDesparasitacion();
    }

    private void agregarEncabezadosVacunas() {
        TableRow encabezadoVacunas = new TableRow(this);
        encabezadoVacunas.addView(crearTextoEncabezado("Fecha Aplicación"));
        encabezadoVacunas.addView(crearTextoEncabezado("Tipo Vacuna"));
        encabezadoVacunas.addView(crearTextoEncabezado("Próxima Aplicación"));
        tableVacunas.addView(encabezadoVacunas);
    }

    private void agregarEncabezadosDesparasitacion() {
        TableRow encabezadoDesparasitacion = new TableRow(this);
        encabezadoDesparasitacion.addView(crearTextoEncabezado("Fecha Aplicación"));
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
        textView.setPadding(16, 8, 16, 8);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine(false);
        textView.setMaxLines(3);
        textView.setEllipsize(null);

        TableRow.LayoutParams params;
        if ("Próxima Aplicación".equals(texto)) {
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f);
        } else {
            params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
        }
        textView.setLayoutParams(params);
        return textView;
    }

    private void cargarVacunasDesdeAPI() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Vacuna>> call = apiService.getVacunasByMascotaId(mascotaId);
        call.enqueue(new Callback<List<Vacuna>>() {
            @Override
            public void onResponse(Call<List<Vacuna>> call, Response<List<Vacuna>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Vacuna> listaVacunas = response.body();
                    if (listaVacunas.isEmpty()) {
                        agregarMensajeNoHayDatos(tableVacunas, "No hay registros de vacunación", 3);
                    } else {
                        for (Vacuna vacuna : listaVacunas) {
                            try {
                                String fecha = formatDate(vacuna.getFechaAplicacion());
                                String proxima = formatDate(vacuna.getProximaAplicacion());
                                agregarFilaVacuna(fecha, vacuna.getTipoVacuna(), proxima);
                            } catch (Exception e) {
                                Log.e("CartillaVacunacion", "Error parseando fecha vacuna", e);
                                agregarFilaVacuna(vacuna.getFechaAplicacion(), vacuna.getTipoVacuna(), vacuna.getProximaAplicacion());
                            }
                        }
                    }
                } else {
                    agregarMensajeNoHayDatos(tableVacunas, "Error al cargar vacunas", 3);
                    Log.e("CartillaVacunacion", "Respuesta no exitosa vacunas");
                }
            }

            @Override
            public void onFailure(Call<List<Vacuna>> call, Throwable t) {
                agregarMensajeNoHayDatos(tableVacunas, "Error de conexión", 3);
                Log.e("CartillaVacunacion", "Fallo en llamada API vacunas: " + t.getMessage());
            }
        });
    }

    private void cargarDesparasitacionesDesdeAPI() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Desparasitacion>> call = apiService.getDesparasitacionesByMascotaId(mascotaId);
        call.enqueue(new Callback<List<Desparasitacion>>() {
            @Override
            public void onResponse(Call<List<Desparasitacion>> call, Response<List<Desparasitacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Desparasitacion> listaDesparasitaciones = response.body();
                    if (listaDesparasitaciones.isEmpty()) {
                        agregarMensajeNoHayDatos(tableDesparasitacion, "No hay registros de desparasitación", 4);
                    } else {
                        for (Desparasitacion d : listaDesparasitaciones) {
                            try {
                                String fecha = formatDate(d.getFechaAplicacion());
                                String proxima = formatDate(d.getFechaProximaAplicacion());
                                agregarFilaDesparasitacion(fecha, String.valueOf(d.getPesoKg()), d.getDosis(), proxima);
                            } catch (Exception e) {
                                Log.e("CartillaVacunacion", "Error parseando fecha desparasitacion", e);
                                agregarFilaDesparasitacion(d.getFechaAplicacion(), String.valueOf(d.getPesoKg()), d.getDosis(), d.getFechaProximaAplicacion());
                            }
                        }
                    }
                } else {
                    agregarMensajeNoHayDatos(tableDesparasitacion, "Error al cargar desparasitaciones", 4);
                    Log.e("CartillaVacunacion", "Respuesta no exitosa desparasitaciones");
                }
            }

            @Override
            public void onFailure(Call<List<Desparasitacion>> call, Throwable t) {
                agregarMensajeNoHayDatos(tableDesparasitacion, "Error de conexión", 4);
                Log.e("CartillaVacunacion", "Fallo en llamada API desparasitaciones: " + t.getMessage());
            }
        });
    }

    private String formatDate(String fechaOriginal) throws ParseException {
        if (fechaOriginal == null || fechaOriginal.isEmpty()) {
            return "-";
        }
        return displayDateFormat.format(apiDateFormat.parse(fechaOriginal));
    }

    private void agregarFilaVacuna(String fechaAplicacion, String tipoVacuna, String proximaAplicacion) {
        TableRow fila = new TableRow(this);
        fila.addView(crearTextoFila(fechaAplicacion));
        fila.addView(crearTextoFila(tipoVacuna));
        fila.addView(crearTextoFila(proximaAplicacion));
        tableVacunas.addView(fila);
    }

    private void agregarFilaDesparasitacion(String fechaAplicacion, String peso, String dosis, String proximaAplicacion) {
        TableRow fila = new TableRow(this);
        fila.addView(crearTextoFila(fechaAplicacion));
        fila.addView(crearTextoFila(peso));
        fila.addView(crearTextoFila(dosis));
        fila.addView(crearTextoFila(proximaAplicacion));
        tableDesparasitacion.addView(fila);
    }

    private TextView crearTextoFila(String texto) {
        TextView textView = new TextView(this);
        textView.setText(texto != null && !texto.isEmpty() ? texto : "-");
        textView.setTextSize(14);
        textView.setPadding(16, 8, 16, 8);
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    private void agregarMensajeNoHayDatos(TableLayout tabla, String mensaje, int columnas) {
        TableRow fila = new TableRow(this);
        TextView mensajeTexto = new TextView(this);
        mensajeTexto.setText(mensaje);
        mensajeTexto.setGravity(Gravity.CENTER);
        mensajeTexto.setPadding(16, 16, 16, 16);
        TableRow.LayoutParams params = new TableRow.LayoutParams();
        params.span = columnas;
        mensajeTexto.setLayoutParams(params);
        fila.addView(mensajeTexto);
        tabla.addView(fila);
    }
}
