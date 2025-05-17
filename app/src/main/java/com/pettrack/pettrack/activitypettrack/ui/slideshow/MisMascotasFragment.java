package com.pettrack.pettrack.activitypettrack.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter.MascotasAdapter;
import com.pettrack.pettrack.activitypettrack.ui.slideshow.petProfile.PetProfileFragment;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.databinding.FragmentMisMascotasBinding;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.registromascota.RegistroMascota;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisMascotasFragment extends Fragment implements MascotasAdapter.OnMascotaClickListener {
    private RecyclerView recyclerMascotas;
    private TextView txtNoMascotas;
    private MascotasAdapter mascotasAdapter;
    private FragmentMisMascotasBinding binding;
    private List<Mascota> mascotasList = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMisMascotasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar vistas
        recyclerMascotas = binding.recyclerViewMascotas;
        txtNoMascotas = binding.txtNoMascotas;

        // Configurar RecyclerView con el listener
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(getContext()));
        mascotasAdapter = new MascotasAdapter(mascotasList, getActivity(), this);
        recyclerMascotas.setAdapter(mascotasAdapter);

        // Configurar el botón de agregar mascota
        binding.btnAgregarMascota.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RegistroMascota.class);
            intent.putExtra("user_id", obtenerUserId());
            startActivity(intent);
        });

        // Cargar mascotas
        cargarMascotas();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        cargarMascotas();
    }

    @Override
    public void onMascotaClick(Mascota mascota) {
        // Navegar al fragmento de perfil de mascota con el ID
        Fragment fragment = PetProfileFragment.newInstance(mascota.getId());
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_activity_pet_track, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void cargarMascotas() {
        int userId = obtenerUserId();
        Log.d("MascotasAPI", "Obteniendo mascotas para user_id: " + userId);
        if (userId == -1) {
            Toast.makeText(getContext(), "No se encontró user_id", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Mascota>> call = apiService.getMascotasByUsuarioId(obtenerUserId());

        call.enqueue(new Callback<List<Mascota>>()  {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                Log.d("MascotasDebug", "Código de respuesta: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<Mascota> nuevasMascotas = response.body(); // ← corregido aquí
                    Log.d("MascotasDebug", "Número de mascotas recibidas: " + nuevasMascotas.size());
                    mascotasAdapter.actualizarMascotas(nuevasMascotas);  // ← usar la lista recibida
                    mostrarListaMascotas();
                } else {
                    try {
                        Log.e("MascotasDebug", "Error en la respuesta: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
                Log.e("MascotasAPI", "Error en la llamada: " + t.getMessage());
                mostrarMensajeSinMascotas();
                Toast.makeText(getContext(), "Error al cargar mascotas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarMensajeSinMascotas() {
        recyclerMascotas.setVisibility(View.GONE);
        txtNoMascotas.setVisibility(View.VISIBLE);
    }

    private void mostrarListaMascotas() {
        recyclerMascotas.setVisibility(View.VISIBLE);
        txtNoMascotas.setVisibility(View.GONE);
    }

    private int obtenerUserId() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPref.getInt("user_id", -1);
    }
}