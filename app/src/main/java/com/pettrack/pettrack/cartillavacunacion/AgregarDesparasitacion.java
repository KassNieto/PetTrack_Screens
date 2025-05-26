package com.pettrack.pettrack.cartillavacunacion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.models.cartillavacunacion.Desparasitacion;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarDesparasitacion extends AppCompatActivity {

    private static final String TAG = "AgregarDesparasitacion";

    private EditText addFAplicacionDes, addPrxAplicacionDes, addDosis, addPesoDes;
    private Button btnGuardarDes;
    private int mascotaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.desparasitacion);

        setupToolbar();
        initViews();
        getMascotaIdFromIntent();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarDes);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Agregar Desparasitación");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initViews() {
        addFAplicacionDes = findViewById(R.id.addFAplicacionDes);
        addPrxAplicacionDes = findViewById(R.id.addPrxAplicacionDes);
        addDosis = findViewById(R.id.addDosis);
        addPesoDes = findViewById(R.id.addPesoDes);
        btnGuardarDes = findViewById(R.id.btnGuardarDes);
    }

    private void getMascotaIdFromIntent() {
        mascotaId = getIntent().getIntExtra("mascotaId", -1);
        if (mascotaId == -1) {
            Toast.makeText(this, "Error: No se recibió ID de mascota", Toast.LENGTH_SHORT).show();
            finish();
        }
        Log.d(TAG, "ID Mascota recibido: " + mascotaId);
    }

    private void setupListeners() {
        // Mostrar DatePicker al tocar campos de fecha
        addFAplicacionDes.setOnClickListener(v -> showDatePickerDialog(addFAplicacionDes));
        addPrxAplicacionDes.setOnClickListener(v -> showDatePickerDialog(addPrxAplicacionDes));

        // Guardar desparasitación al tocar el botón
        btnGuardarDes.setOnClickListener(v -> validateAndSaveDesparasitacion());
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, day) -> {
                    String selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day);
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void validateAndSaveDesparasitacion() {
        String fechaAplicacion = addFAplicacionDes.getText().toString().trim();
        String fechaProximaAplicacion = addPrxAplicacionDes.getText().toString().trim();
        String dosis = addDosis.getText().toString().trim();
        String pesoStr = addPesoDes.getText().toString().trim();

        if (fechaAplicacion.isEmpty()) {
            addFAplicacionDes.setError("Ingrese la fecha de aplicación");
            return;
        }

        if (pesoStr.isEmpty()) {
            addPesoDes.setError("Ingrese el peso de la mascota");
            return;
        }

        double pesoKg;
        try {
            pesoKg = Double.parseDouble(pesoStr);
        } catch (NumberFormatException e) {
            addPesoDes.setError("Ingrese un peso válido");
            return;
        }

        if (fechaProximaAplicacion.isEmpty()) {
            fechaProximaAplicacion = fechaAplicacion;
        }

        Desparasitacion desparasitacion = createDesparasitacionObject(fechaAplicacion, fechaProximaAplicacion, dosis, pesoKg);

        Log.d(TAG, "Enviando desparasitación: " + new Gson().toJson(desparasitacion));

        saveDesparasitacionToApi(desparasitacion);
    }

    private Desparasitacion createDesparasitacionObject(String fechaAplicacion, String fechaProximaAplicacion, String dosis, double pesoKg) {
        Desparasitacion desparasitacion = new Desparasitacion();
        desparasitacion.setFechaAplicacion(fechaAplicacion);
        desparasitacion.setFechaProximaAplicacion(fechaProximaAplicacion);
        desparasitacion.setDosis(dosis);
        desparasitacion.setPesoKg(pesoKg);

        Mascota mascota = new Mascota();
        mascota.setId(mascotaId);
        desparasitacion.setMascota(mascota);

        return desparasitacion;
    }

    private void saveDesparasitacionToApi(Desparasitacion desparasitacion) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Desparasitacion> call = apiService.agregarDesparasitacion(mascotaId, desparasitacion);

        call.enqueue(new Callback<Desparasitacion>() {
            @Override
            public void onResponse(Call<Desparasitacion> call, Response<Desparasitacion> response) {
                if (response.isSuccessful()) {
                    handleSuccessResponse(response);
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<Desparasitacion> call, Throwable t) {
                handleFailure(t);
            }
        });
    }

    private void handleSuccessResponse(Response<Desparasitacion> response) {
        Log.d(TAG, "Desparasitación guardada exitosamente");
        Toast.makeText(AgregarDesparasitacion.this, "Desparasitación registrada correctamente", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void handleErrorResponse(Response<Desparasitacion> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
            Log.e(TAG, "Error al guardar desparasitación: " + response.code() + " - " + errorBody);
            Toast.makeText(AgregarDesparasitacion.this, "Error al registrar: " + errorBody, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error al leer errorBody", e);
            Toast.makeText(AgregarDesparasitacion.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFailure(Throwable t) {
        Log.e(TAG, "Error de conexión: " + t.getMessage(), t);
        Toast.makeText(AgregarDesparasitacion.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
