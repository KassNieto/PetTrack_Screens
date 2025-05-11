package com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.models.Mascota;

import java.util.List;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.ViewHolder> {
    private List<Mascota> mascotaList;
    private Context context;

    public MascotasAdapter(List<Mascota> mascotaList, Context context) {
        this.mascotaList = mascotaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarjeta_mascota, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mascota mascota = mascotaList.get(position);

        // Cargar imagen desde URL (si existe)
        if (mascota.getFoto() != null && !mascota.getFoto().isEmpty()) {
            Glide.with(context)
                    .load(mascota.getFoto())
                    .placeholder(R.drawable.ic_launcher_background) // Imagen por defecto
                    .error(R.drawable.ic_launcher_background) // Imagen si hay error
                    .into(holder.cardImg);
        } else {
            holder.cardImg.setImageResource(R.drawable.ic_launcher_background);
        }

        // Configurar textos (manejando posibles valores nulos)
        holder.txtNombre.setText(mascota.getNombre() != null ? mascota.getNombre() : "Sin nombre");
        holder.txtEdadValue.setText(mascota.getEdad() != null ? mascota.getEdad() : "Edad no especificada");

        // Usar raza como tipo (o puedes agregar un campo tipo en el modelo si es diferente)
        holder.txtTipoValue.setText(mascota.getRaza() != null ? mascota.getRaza() : "Raza no especificada");
    }

    @Override
    public int getItemCount() {
        return mascotaList.size();
    }

    // Método para actualizar la lista de mascotas
    public void actualizarMascotas(List<Mascota> nuevasMascotas) {
        mascotaList.clear();
        mascotaList.addAll(nuevasMascotas);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cardImg;
        TextView txtNombre;
        TextView txtEdadValue;
        TextView txtTipoValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImg = itemView.findViewById(R.id.card_img);
            txtNombre = itemView.findViewById(R.id.txt_nombre);
            txtEdadValue = itemView.findViewById(R.id.txt_edad_value);
            txtTipoValue = itemView.findViewById(R.id.txt_tipo_value);
        }
    }
}