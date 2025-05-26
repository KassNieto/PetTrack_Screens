
package com.pettrack.pettrack.cartillavacunacion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.models.cartillavacunacion.Vacuna;

import java.io.IOException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarVacuna extends AppCompatActivity {

    private static final String TAG = "AgregarVacuna";

    private EditText addFAplicacionVac, addPrxAplicacionVac, addTipoVac;
    private Button btnGuardarVac;
    private int mascotaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacunacion);

        setupToolbar();
        initViews();
        getMascotaIdFromIntent();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarVac);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Agregar Vacuna");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());
    }

    private void initViews() {
        addFAplicacionVac = findViewById(R.id.addFAplicacionVac);
        addPrxAplicacionVac = findViewById(R.id.addPrxAplicacionVac);
        addTipoVac = findViewById(R.id.addTipoVac);
        btnGuardarVac = findViewById(R.id.btnGuardarVac);
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
        addFAplicacionVac.setOnClickListener(v -> showDatePickerDialog(addFAplicacionVac));
        addPrxAplicacionVac.setOnClickListener(v -> showDatePickerDialog(addPrxAplicacionVac));

        btnGuardarVac.setOnClickListener(v -> validateAndSaveVaccine());
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

    private void validateAndSaveVaccine() {
        String fechaAplicacion = addFAplicacionVac.getText().toString().trim();
        String proximaAplicacion = addPrxAplicacionVac.getText().toString().trim();
        String tipoVacuna = addTipoVac.getText().toString().trim();

        if (fechaAplicacion.isEmpty()) {
            addFAplicacionVac.setError("Ingrese la fecha de aplicación");
            return;
        }

        if (tipoVacuna.isEmpty()) {
            addTipoVac.setError("Ingrese el tipo de vacuna");
            return;
        }

        if (proximaAplicacion.isEmpty()) {
            proximaAplicacion = fechaAplicacion;
        }

        Mascota mascota = new Mascota();
        mascota.setId(mascotaId);

        Vacuna nuevaVacuna = new Vacuna();
        nuevaVacuna.setFechaAplicacion(fechaAplicacion);
        nuevaVacuna.setProximaAplicacion(proximaAplicacion);
        nuevaVacuna.setTipoVacuna(tipoVacuna);
        nuevaVacuna.setMascota(mascota);

        Log.d(TAG, "Enviando vacuna: " + new Gson().toJson(nuevaVacuna));
        saveVaccineToApi(nuevaVacuna);
    }

    private void saveVaccineToApi(Vacuna vacuna) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Vacuna> call = apiService.agregarVacuna(mascotaId, vacuna);

        call.enqueue(new Callback<Vacuna>() {
            @Override
            public void onResponse(Call<Vacuna> call, Response<Vacuna> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Vacuna guardada exitosamente");
                    Toast.makeText(AgregarVacuna.this, "Vacuna registrada correctamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(Call<Vacuna> call, Throwable t) {
                Log.e(TAG, "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(AgregarVacuna.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleErrorResponse(Response<Vacuna> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
            Log.e(TAG, "Error al guardar vacuna: " + response.code() + " - " + errorBody);
            Toast.makeText(AgregarVacuna.this, "Error al registrar: " + errorBody, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e(TAG, "Error al leer errorBody", e);
            Toast.makeText(AgregarVacuna.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
        }
    }
}
