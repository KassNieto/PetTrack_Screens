package com.pettrack.pettrack.activitypettrack.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pettrack.pettrack.R;
import com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter.Mascotas;
import com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter.MascotasAdapter;
import com.pettrack.pettrack.databinding.FragmentMisMascotasBinding;

import java.util.ArrayList;
import java.util.List;

public class MisMascotasFragment extends Fragment {
    RecyclerView recyclerMascotas;
    MascotasAdapter mascotasAdapter;
    private FragmentMisMascotasBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MisMascotasViewModel misMascotasViewModel =
                new ViewModelProvider(this).get(MisMascotasViewModel.class);

        binding = FragmentMisMascotasBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        inicializarElementos(root); // Pasar la vista root como parámetro
        final TextView textView = binding.txtMisMascotas;
        misMascotasViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void inicializarElementos(View root) {
        // Usar root.findViewById en lugar de findViewById directo
        recyclerMascotas = root.findViewById(R.id.recycler_view_mascotas);
        recyclerMascotas.setLayoutManager(new LinearLayoutManager(getContext())); // Usar getContext() en lugar de this

        List<Mascotas> mascotasList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mascotasList.add(new Mascotas(i,"pet", "Darketa", "2 años", "Perro"));
        }
        mascotasAdapter = new MascotasAdapter(mascotasList, getActivity()); // Usar getActivity() para el contexto
        recyclerMascotas.setAdapter(mascotasAdapter); // No olvides establecer el adaptador
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}