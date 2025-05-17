package com.pettrack.pettrack.signup;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.pettrack.pettrack.MainActivity;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.activitypettrack.PetTrackActivity;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.User;
import com.pettrack.pettrack.models.signup.RegisterRequest;
import com.pettrack.pettrack.models.signup.ApiResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private TextInputEditText etNombre, etApellido, etFechaNacimiento,
            etTelefono, etEmail, etContrasena;
    private ApiService apiService;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Formateador de fecha
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        calendar = Calendar.getInstance();

        // Inicializar ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registrando...");
        progressDialog.setCancelable(false);

        // Inicializar Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        // Referencias a los views
        etNombre = findViewById(R.id.addNombre);
        etApellido = findViewById(R.id.addApelido);
        etFechaNacimiento = findViewById(R.id.addFNacimiento);
        etTelefono = findViewById(R.id.addCelular);
        etEmail = findViewById(R.id.addCorreoSign);
        etContrasena = findViewById(R.id.addContraSign);

        // Configurar el selector de fecha
        setupDatePicker();

        // Configurar el botón de registro
        findViewById(R.id.registerButton).setOnClickListener(v -> registrarUsuario());

        // Manejo de insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setupDatePicker() {
        etFechaNacimiento.setShowSoftInputOnFocus(false);

        etFechaNacimiento.setOnClickListener(v -> showDatePicker());

        etFechaNacimiento.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String fecha = dateFormatter.format(calendar.getTime());
                    etFechaNacimiento.setText(fecha);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void registrarUsuario() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellido.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String numero = etTelefono.getText().toString().trim();
        String correoElectronico = etEmail.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();

        if (!validarCampos(nombre, apellidos, fechaNacimiento, numero, correoElectronico, contrasena)) {
            return;
        }

        RegisterRequest registerRequest = new RegisterRequest(
                nombre,
                apellidos,
                fechaNacimiento,
                numero,
                correoElectronico,
                contrasena);

        mostrarCargando(true);

        apiService.registerUser(registerRequest).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                mostrarCargando(false);

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                        try {
                            // Obtener el ID directamente del objeto ApiResponse
                            int userId = apiResponse.getId(); // Asumiendo que ApiResponse tiene un campo id

                            // Guardar el ID del usuario en SharedPreferences
                            SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("user_id", userId);
                            editor.apply();

                            // Redirigir a PetTrackActivity
                            Toast.makeText(SignUp.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, PetTrackActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            Log.e("REGISTRO", "Error al procesar respuesta", e);
                            mostrarError("Error al procesar los datos del usuario");
                        }

                } else {
                    try {
                        String errorBody = response.errorBody() != null ?
                                response.errorBody().string() : "Error desconocido";
                        mostrarError("Error: " + errorBody);
                        Log.e("API_ERROR", "Error en la respuesta: " + errorBody);
                    } catch (IOException e) {
                        mostrarError("Error al procesar la respuesta");
                        Log.e("API_ERROR", "Error procesando respuesta", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mostrarCargando(false);
                mostrarError("Error de conexión: " + t.getMessage());
                Log.e("API_CALL", "Error en la llamada API", t);
            }
        });
    }

    private boolean validarCampos(String... campos) {
        if (campos[0].isEmpty()) {
            etNombre.setError("Nombre requerido");
            return false;
        }
        if (campos[1].isEmpty()) {
            etApellido.setError("Apellido requerido");
            return false;
        }
        if (campos[2].isEmpty()) {
            etFechaNacimiento.setError("Fecha de nacimiento requerida");
            return false;
        }
        if (campos[3].isEmpty()) {
            etTelefono.setError("Teléfono requerido");
            return false;
        }
        if (campos[4].isEmpty()) {
            etEmail.setError("Email requerido");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(campos[4]).matches()) {
            etEmail.setError("Email inválido");
            return false;
        }
        if (campos[5].isEmpty()) {
            etContrasena.setError("Contraseña requerida");
            return false;
        }
        if (campos[5].length() < 6) {
            etContrasena.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        return true;
    }

    private void mostrarCargando(boolean mostrar) {
        if (mostrar) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}