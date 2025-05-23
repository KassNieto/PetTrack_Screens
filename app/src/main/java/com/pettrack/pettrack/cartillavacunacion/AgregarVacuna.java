package com.pettrack.pettrack.cartillavacunacion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.cartillavacunacion.Vacuna;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

public class AgregarVacuna extends AppCompatActivity {

    private EditText addFAplicacionVac, addPrxAplicacionVac, addTipoVac;
    private Button btnGuardarVac;
    private int idMascota;

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

        // Obtener el ID de la mascota desde el Intent
        idMascota = getIntent().getIntExtra("idMascota", -1);
        System.out.println("ID Mascota recibido: " + idMascota);

        // Abrir DatePickerDialog al tocar campos de fecha
        addFAplicacionVac.setOnClickListener(v -> showDatePickerDialog(addFAplicacionVac));
        addPrxAplicacionVac.setOnClickListener(v -> showDatePickerDialog(addPrxAplicacionVac));

        btnGuardarVac.setOnClickListener(v -> {
            String fechaAplicacion = addFAplicacionVac.getText().toString().trim();
            String proximaAplicacion = addPrxAplicacionVac.getText().toString().trim();
            String tipoVacuna = addTipoVac.getText().toString().trim();

            Log.d("AgregarVacuna", "Enviar - Fecha Aplicacion: " + fechaAplicacion);
            Log.d("AgregarVacuna", "Enviar - Proxima Aplicacion: " + proximaAplicacion);
            Log.d("AgregarVacuna", "Enviar - Tipo Vacuna: " + tipoVacuna);

            if (fechaAplicacion.isEmpty() || proximaAplicacion.isEmpty() || tipoVacuna.isEmpty()) {
                Toast.makeText(AgregarVacuna.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Vacuna nuevaVacuna = new Vacuna(fechaAplicacion, proximaAplicacion, tipoVacuna, idMascota);

            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Vacuna> call = apiService.agregarVacuna(idMascota, nuevaVacuna);
            call.enqueue(new Callback<Vacuna>() {
                @Override
                public void onResponse(Call<Vacuna> call, Response<Vacuna> response) {
                    if (response.isSuccessful()) {
                        Vacuna v = response.body();
                        if (v != null) {
                            Log.d("AgregarVacuna", "Recibido - Fecha Aplicacion: " + v.getFechaAplicacion());
                            Log.d("AgregarVacuna", "Recibido - Proxima Aplicacion: " + v.getProximaAplicacion());
                            Log.d("AgregarVacuna", "Recibido - Tipo Vacuna: " + v.getTipoVacuna());
                        }
                        Toast.makeText(AgregarVacuna.this, "Vacunación registrada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "No hay mensaje de error";
                            Toast.makeText(AgregarVacuna.this, "Error al registrar vacunación: " + response.code(), Toast.LENGTH_LONG).show();
                            Log.e("AgregarVacuna", "Error API: " + errorBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Vacuna> call, Throwable t) {
                    Toast.makeText(AgregarVacuna.this, "Fallo en la conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("AgregarVacuna", "Fallo en la conexión", t);
                }
            });
        });
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
                    editText.setText(fechaSeleccionada);
                }, year, month, day);
        datePickerDialog.show();
    }
}
