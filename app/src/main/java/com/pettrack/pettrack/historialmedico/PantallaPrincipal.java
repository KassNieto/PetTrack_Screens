package com.pettrack.pettrack.historialmedico;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.historialmedico.HistorialMedicoUploader;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PantallaPrincipal extends AppCompatActivity {

    private RecyclerView rvArchivos;
    private Button btnMas;
    private Toolbar toolbar;
    private HistorialMedicoAdapter adapter;
    private List<HistorialMedicoUploader> listaArchivos = new ArrayList<>();
    private ApiService apiService;
    private int mascotaId = 1;  // Ejemplo, pásalo dinámicamente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.historial_medico);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rvArchivos = findViewById(R.id.rvArchivos);
        btnMas = findViewById(R.id.btnMas);

        adapter = new HistorialMedicoAdapter(this, listaArchivos);
        rvArchivos.setLayoutManager(new LinearLayoutManager(this));
        rvArchivos.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnMas.setOnClickListener(v -> {
            Intent intent = new Intent(PantallaPrincipal.this, HistorialMedico.class);
            intent.putExtra("mascotaId", mascotaId);
            startActivity(intent);
        });

        cargarArchivos();
    }

    private void cargarArchivos() {
        Call<List<HistorialMedicoUploader>> call = apiService.obtenerArchivos(mascotaId);
        call.enqueue(new Callback<List<HistorialMedicoUploader>>() {
            @Override
            public void onResponse(Call<List<HistorialMedicoUploader>> call, Response<List<HistorialMedicoUploader>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaArchivos = response.body();
                    adapter.actualizarLista(listaArchivos);
                }
            }

            @Override
            public void onFailure(Call<List<HistorialMedicoUploader>> call, Throwable t) {
                Toast.makeText(PantallaPrincipal.this, "Error al cargar archivos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarArchivos(); // Refrescar lista al regresar de subir archivo
    }
}