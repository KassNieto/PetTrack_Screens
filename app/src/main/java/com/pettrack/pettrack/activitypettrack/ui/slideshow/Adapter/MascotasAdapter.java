package com.pettrack.pettrack.activitypettrack.ui.slideshow.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.models.Mascota;

import java.util.List;

public class MascotasAdapter extends RecyclerView.Adapter<MascotasAdapter.ViewHolder> {
    private List<Mascota> mascotaList;
    private Context context;
    private OnMascotaClickListener listener;

    // Interfaz para manejar clicks
    public interface OnMascotaClickListener {
        void onMascotaClick(Mascota mascota);
    }

    public MascotasAdapter(List<Mascota> mascotaList, Context context, OnMascotaClickListener listener) {
        this.mascotaList = mascotaList;
        this.context = context;
        this.listener = listener;
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

        // 1️⃣ Primero verifica que la URL no sea nula o vacía
        if (mascota.getFoto() != null && !mascota.getFoto().isEmpty()) {
            // 2️⃣ Usa Glide con las configuraciones que mencionaste
            Glide.with(context)
                    .load(mascota.getFoto())
                    .override(500, 500)  // Fuerza el tamaño de carga
                    .transform(new CenterCrop(), new RoundedCorners(16))  // Recorta y redondea esquinas
                    .placeholder(R.drawable.ic_launcher_background)  // Imagen mientras carga
                    .error(R.drawable.ic_launcher_background)  // Imagen si falla
                    .into(holder.cardImg);

            // 3️⃣ Opcional: Agrega logs para depuración
            Log.d("GLIDE_DEBUG", "Cargando imagen: " + mascota.getFoto());
        } else {
            // Si no hay URL, coloca una imagen por defecto
            holder.cardImg.setImageResource(R.drawable.ic_launcher_background);
            Log.e("GLIDE_DEBUG", "URL de imagen vacía para: " + mascota.getNombre());
        }

        // Configura los demás views (nombre, edad, etc.)
        holder.txtNombre.setText(mascota.getNombre());
        // ... (otros campos)
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
