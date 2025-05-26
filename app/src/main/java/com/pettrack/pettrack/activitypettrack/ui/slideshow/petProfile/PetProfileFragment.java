
package com.pettrack.pettrack.activitypettrack.ui.slideshow.petProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.api.ApiClient;
import com.pettrack.pettrack.api.ApiService;
import com.pettrack.pettrack.cartillavacunacion.CartillaVacunacion;
import com.pettrack.pettrack.databinding.FragmentPetProfilerragmentBinding;
import com.pettrack.pettrack.models.Mascota;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PetProfileFragment extends Fragment {
    private static final String ARG_USER_ID = "user_id";
    private static final String ARG_MASCOTA_ID = "mascota_id";

    private FragmentPetProfilerragmentBinding binding;
    private int userId;
    private int mascotaId;
    private Mascota mascota;

    public static PetProfileFragment newInstance(int userId, int mascotaId) {
        PetProfileFragment fragment = new PetProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putInt(ARG_MASCOTA_ID, mascotaId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getInt(ARG_USER_ID, -1);
            mascotaId = getArguments().getInt(ARG_MASCOTA_ID, -1);
            Log.d("PetProfile", "ID de usuario recibido: " + userId);
            Log.d("PetProfile", "ID de mascota recibido: " + mascotaId);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPetProfilerragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarDatosMascota();

        // Acción del botón + Añadir en Cartilla de vacunación
        binding.btnAddVaccine.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CartillaVacunacion.class);
            intent.putExtra("mascotaId", mascotaId); // opcional, por si lo necesitas en CartillaVacunacion
            Log.d("PetProfile", "Abriendo CartillaVacunacion con mascotaId = " + mascotaId);
            startActivity(intent);
        });
    }

    private void cargarDatosMascota() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<Mascota> call = apiService.getMascotaById(userId, mascotaId);

        call.enqueue(new Callback<Mascota>() {
            @Override
            public void onResponse(Call<Mascota> call, Response<Mascota> response) {
                if (response.isSuccessful()) {
                    mascota = response.body();
                    if (mascota != null) {
                        Log.d("PetProfile", "Datos de mascota recibidos: " + mascota.getNombre());
                        mostrarDatosMascota();
                    } else {
                        Log.e("PetProfile", "Respuesta exitosa pero cuerpo vacío");
                        Toast.makeText(requireContext(), "No se encontraron datos de la mascota", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        Log.e("PetProfile", "Error en la respuesta: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("PetProfile", "Error al leer el cuerpo de error", e);
                    }
                    Toast.makeText(requireContext(), "Error al cargar datos de la mascota", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Mascota> call, Throwable t) {
                Log.e("PetProfile", "Error de conexión: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

            if (mascota.getFoto() != null && !mascota.getFoto().isEmpty()) {
                Glide.with(requireContext())
                        .load(mascota.getFoto())
                        .into(binding.ivPetPhoto);
            } else {
                binding.ivPetPhoto.setImageResource(R.drawable.ic_launcher_background);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
