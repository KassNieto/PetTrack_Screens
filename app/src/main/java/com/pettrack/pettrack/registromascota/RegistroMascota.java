package com.pettrack.pettrack.registromascota;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.Mascota;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroMascota extends AppCompatActivity {

    private EditText editNombre, editRaza, editEdad, editColor, editPeso, editSenyas;
    private RadioGroup radioGroupSexo;
    private ImageView imgMascota;
    private Button btnSeleccionarFoto, btnAgregar;
    private Uri imagenUri;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_mascota);

        // Inicializar vistas
        editNombre = findViewById(R.id.edit_nombre_mascota);
        editRaza = findViewById(R.id.edit_raza_mascota);
        editEdad = findViewById(R.id.edit_edad_mascota);
        editColor = findViewById(R.id.edit_color_mascota);
        editPeso = findViewById(R.id.edit_peso_mascota);
        editSenyas = findViewById(R.id.edit_senyas_mascota);
        radioGroupSexo = findViewById(R.id.radioGroupSexo);
        imgMascota = findViewById(R.id.img_mascota);
        btnSeleccionarFoto = findViewById(R.id.btn_seleccionar_foto);
        btnAgregar = findViewById(R.id.btn_agregar_mascota);

        // Configurar listeners
        btnSeleccionarFoto.setOnClickListener(v -> abrirSelectorImagen());
        btnAgregar.setOnClickListener(v -> registrarMascota());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void abrirSelectorImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imagenUri = data.getData();
            imgMascota.setImageURI(imagenUri);
        }
    }

    private void registrarMascota() {
        // Obtener datos del formulario
        String nombre = editNombre.getText().toString().trim();
        String raza = editRaza.getText().toString().trim();
        String edad = editEdad.getText().toString().trim();
        String color = editColor.getText().toString().trim();
        String peso = editPeso.getText().toString().trim();
        String senyas = editSenyas.getText().toString().trim();

        // Obtener sexo
        int selectedId = radioGroupSexo.getCheckedRadioButtonId();
        String sexo = "";
        if (selectedId == R.id.radioMacho) {
            sexo = "Macho";
        } else if (selectedId == R.id.radioHembra) {
            sexo = "Hembra";
        }

        // Validaciones básicas
        if (nombre.isEmpty() || sexo.isEmpty() || imagenUri == null) {
            Toast.makeText(this, "Nombre, sexo y foto son obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Mascota
        Mascota mascota = new Mascota();
        mascota.setNombre(nombre);
        mascota.setSexo(sexo);
        mascota.setRaza(raza);
        mascota.setEdad(edad);
        mascota.setColor(color);
        mascota.setPeso(peso);
        mascota.setSeniasParticulares(senyas);

        try {
            // Copiar el contenido de la URI a un archivo temporal
            File file = getFileFromUri(imagenUri);
            RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part fotoPart = MultipartBody.Part.createFormData("foto", file.getName(), requestFile);

            // Convertir mascota a JSON
            RequestBody mascotaPart = RequestBody.create(
                    MediaType.parse("application/json"),
                    new Gson().toJson(mascota)
            );

            // Obtener user_id
            int userId = getIntent().getIntExtra("user_id", -1);
            if (userId == -1) {
                Toast.makeText(this, "Error: usuario no identificado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Llamar al API
            ApiService apiService = ApiClient.getClient().create(ApiService.class);
            Call<Mascota> call = apiService.crearMascotaConImagen(
                    userId,
                    mascotaPart,
                    fotoPart
            );

            call.enqueue(new Callback<Mascota>() {
                @Override
                public void onResponse(Call<Mascota> call, Response<Mascota> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegistroMascota.this, "Mascota registrada!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    } else {
                        try {
                            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Error desconocido";
                            Log.e("API_ERROR", "Código: " + response.code() + " - " + errorBody);
                            Toast.makeText(RegistroMascota.this, "Error al registrar: " + response.code(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(RegistroMascota.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Mascota> call, Throwable t) {
                    Log.e("API_FAILURE", "Error de conexión", t);
                    Toast.makeText(RegistroMascota.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "Error al procesar la imagen", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private File getFileFromUri(Uri uri) throws IOException {
        File tempFile = new File(getCacheDir(), "temp_image.jpg");
        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             OutputStream outputStream = new FileOutputStream(tempFile)) {

            if (inputStream == null) throw new IOException("No se pudo abrir InputStream");

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return tempFile;
    }
}
