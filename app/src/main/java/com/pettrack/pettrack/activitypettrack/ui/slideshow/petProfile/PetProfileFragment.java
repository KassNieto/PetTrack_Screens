package com.pettrack.pettrack.activitypettrack.ui.slideshow.petProfile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.pettrack.pettrack.databinding.FragmentPetProfilerragmentBinding;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.models.Mascota;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetProfileFragment extends Fragment {
    private static final String ARG_MASCOTA_ID = "mascota_id";

    private FragmentPetProfilerragmentBinding binding;
    private int mascotaId;
    private Mascota mascota;

    public static PetProfileFragment newInstance(int mascotaId) {
        PetProfileFragment fragment = new PetProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MASCOTA_ID, mascotaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mascotaId = getArguments().getInt(ARG_MASCOTA_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Cambiado a FragmentPetProfilerragmentBinding
        binding = FragmentPetProfilerragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cargarDatosMascota();

        // Configurar listeners para los botones
        binding.btnAddVaccine.setOnClickListener(v -> {
            // Lógica para añadir vacuna
        });

        binding.btnAddMedicalRecord.setOnClickListener(v -> {
            // Lógica para añadir historial médico
        });
    }

    private void cargarDatosMascota() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Mascota> call = apiService.getMascotaById(mascotaId);

        call.enqueue(new Callback<Mascota>() {
            @Override
            public void onResponse(Call<Mascota> call, Response<Mascota> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mascota = response.body();
                    mostrarDatosMascota();
                } else {
                    Toast.makeText(requireContext(), "Error al cargar datos de la mascota", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mascota> call, Throwable t) {
                Toast.makeText(requireContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarDatosMascota() {
        if (mascota != null) {
            binding.tvPetName.setText(mascota.getNombre());
            binding.tvPetBreed.setText(mascota.getRaza());
            binding.tvPetAge.setText(mascota.getEdad());
            binding.tvPetSex.setText(mascota.getSexo());
            binding.tvPetColor.setText(mascota.getColor());
            binding.tvPetWeight.setText(mascota.getPeso());
            binding.tvPetDistinctiveFeatures.setText(mascota.getSeniasParticulares());

            // Cargar foto si existe
            if (mascota.getFoto() != null && !mascota.getFoto().isEmpty()) {
                Glide.with(requireContext())
                        .load(mascota.getFoto())
                        .into(binding.ivPetPhoto);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}