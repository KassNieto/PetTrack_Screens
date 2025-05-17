package com.pettrack.pettrack.activitypettrack.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter.MascotasAdapter;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.databinding.FragmentMisMascotasBinding;
import com.pettrack.pettrack.models.Mascota;
import com.pettrack.pettrack.registromascota.RegistroMascota;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MisMascotasFragment extends Fragment {
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

        // Configurar RecyclerView
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(getContext()));
        mascotasAdapter = new MascotasAdapter(mascotasList, getActivity());
        recyclerMascotas.setAdapter(mascotasAdapter);

        // Configurar el botón de agregar mascota
        binding.btnAgregarMascota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad de registro de mascota
                Intent intent = new Intent(getActivity(), RegistroMascota.class);
                startActivity(intent);
            }
        });

        // Cargar mascotas
        cargarMascotas();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar las mascotas cuando el fragment vuelva a ser visible
        cargarMascotas();
    }

    private void cargarMascotas() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Mascota>> call = apiService.getMascotasByUsuarioId(obtenerUserId());

        call.enqueue(new Callback<List<Mascota>>() {
            @Override
            public void onResponse(Call<List<Mascota>> call, Response<List<Mascota>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mascotasList.clear();
                    mascotasList.addAll(response.body());
                    mascotasAdapter.notifyDataSetChanged();

                    // Mostrar u ocultar vistas según si hay mascotas
                    if (mascotasList.isEmpty()) {
                        mostrarMensajeSinMascotas();
                    } else {
                        mostrarListaMascotas();
                    }
                } else {
                    mostrarMensajeSinMascotas();
                }
            }

            @Override
            public void onFailure(Call<List<Mascota>> call, Throwable t) {
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