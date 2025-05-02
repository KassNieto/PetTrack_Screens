package com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pettrack.pettrack.R;

import java.util.List;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.ViewHolder>{
    private List<Mascotas> mascotaList;
    private Context context;

    public MascotasAdapter(List<Mascotas> mascotaList, Context context) {
        this.mascotaList = mascotaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tarjeta_mascota, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mascotas mascota = mascotaList.get(position);

        // Cargar imagen si existe
        if (mascota.getImagen() != null && !mascota.getImagen().isEmpty()) {
            try {
                int drawableId = context.getResources().getIdentifier(
                        mascota.getImagen(),
                        "drawable",
                        context.getPackageName());

                if (drawableId != 0) {
                    Glide.with(context)
                            .load(drawableId)
                            .into(holder.cardImg); // Asume que tienes un ImageView llamado cardIcon
                }
            } catch (Exception e) {
                // Manejar error si es necesario
            }
        }

        holder.txtNombre.setText(mascota.getNombre() != null ? mascota.getNombre() : "");
        holder.txtEdadValue.setText(mascota.getEdad() != null ? mascota.getEdad() : "");
        holder.txtTipoValue.setText(mascota.getTipo() != null ? mascota.getTipo() : "");
    }
    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView cardImg;
        private TextView txtNombre;
        private TextView txtEdadValue;
        private TextView txtTipoValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImg = itemView.findViewById(R.id.card_img);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtEdadValue = itemView.findViewById(R.id.txt_edad_value);
            txtTipoValue = itemView.findViewById(R.id.txt_tipo_value);
        }
    }
}
