package com.pettrack.pettrack.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.pettrack.pettrack.MainActivity;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.activitypettrack.PetTrackActivity;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.LoginRequest;
import com.pettrack.pettrack.models.LoginResponse;
import com.pettrack.pettrack.signup.SignUp;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login);

        // Configura el listener para los insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar vistas
        emailEditText = findViewById(R.id.addCorreoLog);
        passwordEditText = findViewById(R.id.addContra);
        loginButton = findViewById(R.id.btnIngresar);
        registerButton = findViewById(R.id.btnRegistrar); // Asegúrate de que este ID coincida con tu XML

        // Inicializar Retrofit
        apiService = ApiClient.getClient().create(ApiService.class);

        // Configurar el click listener para el botón de login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });

        // Configurar el click listener para el botón de registrar
        registerButton = findViewById(R.id.btnRegistrar);
        registerButton = findViewById(R.id.btnRegistrar);

        if (registerButton == null) {
            Toast.makeText(this, "btnRegistrar es null. Verifica el layout", Toast.LENGTH_LONG).show();
        } else {
            registerButton.setOnClickListener(v -> {
                Toast.makeText(Login.this, "Click detectado", Toast.LENGTH_SHORT).show();
                goToSignUp();
            });
        }


    }

    private void goToSignUp() {
        Intent intent = new Intent(Login.this, SignUp.class);
        startActivity(intent);
        finish();
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(Login.this, "Por favor ingresa email y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el objeto de solicitud
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Realizar la llamada a la API
        Call<LoginResponse> call = apiService.loginUser(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Login exitoso
                    LoginResponse loginResponse = response.body();
                    handleSuccessfulLogin(loginResponse);
                } else {
                    Toast.makeText(Login.this, "Error en el login: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // Fallo en la conexión
                Toast.makeText(Login.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity", "Error en la llamada API", t);
            }
        });
    }

    private void handleSuccessfulLogin(LoginResponse loginResponse) {
        // Guardar el ID y token usando SharedPreferences
        SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Asume que loginResponse tiene métodos como getUserId() y getToken()
        editor.putString("user_id", loginResponse.getUserId()); // Guarda el ID del usuario
        editor.apply(); // ¡Aplicar los cambios!

        // Redirigir al MainActivity (o pantalla principal)
        Toast.makeText(this, "Login exitoso!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, PetTrackActivity.class);
        startActivity(intent);
        finish(); // Cierra la actividad de login para no volver atrás
    }
}