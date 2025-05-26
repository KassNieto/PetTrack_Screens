package com.pettrack.pettrack.activitypettrack.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pettrack.pettrack.databinding.FragmentCalendarioBinding;

public class CalendarioFragment extends Fragment {

    private FragmentCalendarioBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CalendarioViewModel calendarioViewModel =
                new ViewModelProvider(this).get(CalendarioViewModel.class);

        binding = FragmentCalendarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        CalendarView calendar = binding.calendarView;
        calendar.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Toast.makeText(getContext(), dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_SHORT).show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}