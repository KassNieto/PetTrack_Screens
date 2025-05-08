package com.pettrack.pettrack.signup;

import android.app.DatePickerDialog;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Formateador de fecha
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Formato ISO para la API
        calendar = Calendar.getInstance();

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
        // Evitar que el teclado aparezca para el campo de fecha
        etFechaNacimiento.setShowSoftInputOnFocus(false);

        etFechaNacimiento.setOnClickListener(v -> showDatePicker());

        // Esto permite que el campo reciba clicks pero no entrada de teclado
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

                    // Formatear la fecha como string y mostrarla
                    String fecha = dateFormatter.format(calendar.getTime());
                    etFechaNacimiento.setText(fecha);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        // Establecer fecha máxima (hoy)
        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void registrarUsuario() {
        // Obtener los valores de los campos
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellido.getText().toString().trim();
        String fechaNacimiento = etFechaNacimiento.getText().toString().trim();
        String numero = etTelefono.getText().toString().trim();
        String correoElectronico = etEmail.getText().toString().trim();
        String contrasena = etContrasena.getText().toString().trim();

        // Debug: Verificar valores antes de enviar
        Log.d("FORM_DATA", "Email: " + correoElectronico);
        Log.d("FORM_DATA", "Fecha: " + fechaNacimiento);

        // Validar campos
        if (!validarCampos(nombre, apellidos, fechaNacimiento, numero, correoElectronico, contrasena)) {
            return;
        }

        // Crear objeto RegisterRequest
        RegisterRequest registerRequest = new RegisterRequest(
                nombre,
                apellidos,
                fechaNacimiento,
                numero,
                correoElectronico, // camelCase
                contrasena);

        // Mostrar progreso
        mostrarCargando(true);

        // Debug: Ver el JSON que se enviará
        Gson gson = new Gson();
        Log.d("REQUEST_JSON", gson.toJson(registerRequest));

        // Realizar la llamada a la API
        apiService.registerUser(registerRequest).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                mostrarCargando(false);
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().isSuccess()) {
                            registroExitoso(response.body()); // <---- CORREGIDO AQUÍ
                        } else {
                            String errorBody = response.errorBody() != null ?
                                    response.errorBody().string() : "Error desconocido";
                            mostrarError("Error en el registro: " + errorBody);
                            Log.e("API_ERROR", errorBody);
                        }
                    } else {
                        mostrarError("Error HTTP: " + response.code());
                    }
                } catch (IOException e) {
                    mostrarError("Error procesando respuesta: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mostrarCargando(false);
                mostrarError("Error de conexión: " + t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private boolean validarCampos(String... campos) {
        for (String campo : campos) {
            if (campo.isEmpty()) {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Validación adicional del email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(campos[4]).matches()) {
            etEmail.setError("Email inválido");
            return false;
        }

        // Validación de contraseña (mínimo 6 caracteres)
        if (campos[5].length() < 6) {
            etContrasena.setError("La contraseña debe tener al menos 6 caracteres");
            return false;
        }

        return true;
    }

    private void mostrarCargando(boolean mostrar) {
        // Implementa un ProgressDialog o similar
    }

    private void registroExitoso(ApiResponse response) {
        // Verificar que la respuesta y los datos sean válidos
        if (response != null && response.isSuccess() && response.getData() != null) {
            User user = response.getData();

            // Guardar solo el user_id en SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            if (user.getId() != null) {  // Asume que User tiene un método getId()
                editor.putString("user_id", user.getId());
                editor.apply(); // ¡Importante aplicar los cambios!

                // Redirigir a MainActivity
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Error: ID de usuario no recibido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Error en el registro: " + (response != null ? response.getMessage() : ""), Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarError(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
