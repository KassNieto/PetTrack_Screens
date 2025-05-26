package com.pettrack.pettrack.activitypettrack.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.databinding.FragmentMiPerfilBinding;
import com.pettrack.pettrack.models.User;

public class MiPerfilFragment extends Fragment {

    private FragmentMiPerfilBinding binding;
    private MiPerfilViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMiPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar ProgressBar
        progressBar = root.findViewById(R.id.progressBar); // Asegúrate de tener un ProgressBar en tu layout

        viewModel = new ViewModelProvider(this).get(MiPerfilViewModel.class);

        // Obtener el ID del usuario de SharedPreferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        int userId = sharedPref.getInt("user_id", -1);

        if (userId != -1) {
            viewModel.loadUserData(userId);
        } else {
            Toast.makeText(getContext(), "No se encontró el ID de usuario", Toast.LENGTH_SHORT).show();
        }

        setupObservers();

        return root;
    }

    private void setupObservers() {
        // Observar datos del usuario
        viewModel.getUserData().observe(getViewLifecycleOwner(), user -> {
            if (user != null) {
                updateUI(user);
            }
        });

        // Observar estado de carga
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                binding.getRoot().setAlpha(0.5f); // Opcional: atenuar el contenido
            } else {
                progressBar.setVisibility(View.GONE);
                binding.getRoot().setAlpha(1.0f);
            }
        });

        // Observar errores
        viewModel.getErrorMessage().observe(getViewLifecycleOwner(), error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(User user) {
        binding.tvNombres.setText(user.getNombre());
        binding.tvApellidos.setText(user.getApellidos());
        binding.tvTelefono.setText(user.getNumero());
        binding.tvFechaNacimiento.setText(user.getFechaNacimiento());
        binding.tvCorreo.setText(user.getCorreoElectronico());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

