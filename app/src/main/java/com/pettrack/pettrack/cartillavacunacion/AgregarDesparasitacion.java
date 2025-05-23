package com.pettrack.pettrack.cartillavacunacion;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.cartillavacunacion.Desparasitacion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgregarDesparasitacion extends AppCompatActivity {

    private EditText addFAplicacionDes, addPrxAplicacionDes, addDosis, addPesoDes;
    private Button btnGuardarDes;
    private int idMascota;

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

        // Mostrar el calendario al tocar los campos de fecha
        addFAplicacionDes.setOnClickListener(v -> mostrarDatePicker(addFAplicacionDes));
        addPrxAplicacionDes.setOnClickListener(v -> mostrarDatePicker(addPrxAplicacionDes));

        // Obtener el ID de la mascota desde el Intent
        idMascota = getIntent().getIntExtra("idMascota", -1);

        btnGuardarDes.setOnClickListener(v -> {
            String fecha = addFAplicacionDes.getText().toString().trim();
            String proxima = addPrxAplicacionDes.getText().toString().trim();
            String dosis = addDosis.getText().toString().trim();
            String peso = addPesoDes.getText().toString().trim();

            if (fecha.isEmpty() || proxima.isEmpty() || dosis.isEmpty() || peso.isEmpty()) {
                Toast.makeText(AgregarDesparasitacion.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (idMascota == -1) {
                Toast.makeText(this, "Error: ID de mascota no recibido", Toast.LENGTH_SHORT).show();
            } else {
                // Crea el objeto Desparasitacion usando constructor vacío y setters
                Desparasitacion nuevaDesparasitacion = new Desparasitacion();
                nuevaDesparasitacion.setFechaAplicacion(fecha);
                nuevaDesparasitacion.setProximaAplicacion(proxima);
                nuevaDesparasitacion.setDosis(dosis);
                nuevaDesparasitacion.setPeso(peso);
                // El idMascota no está en el modelo, si tu API requiere enviar el idMascota,
                // considera agregar un campo o manejarlo en el backend.

                // Llama al servicio
                ApiService apiService = ApiClient.getApiService();
                apiService.agregarDesparasitacion(idMascota, nuevaDesparasitacion).enqueue(new Callback<Desparasitacion>() {
                    @Override
                    public void onResponse(Call<Desparasitacion> call, Response<Desparasitacion> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AgregarDesparasitacion.this, "Desparasitación guardada correctamente", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AgregarDesparasitacion.this, "Error al guardar en la API", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Desparasitacion> call, Throwable t) {
                        Toast.makeText(AgregarDesparasitacion.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    // Método para mostrar el DatePicker
    private void mostrarDatePicker(EditText editText) {
        final Calendar calendario = Calendar.getInstance();
        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    String fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                    editText.setText(fechaSeleccionada);
                }, anio, mes, dia);
        datePickerDialog.show();
    }
}
