package com.pettrack.pettrack.historialmedico;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pettrack.pettrack.R;
import com.pettrack.pettrack.models.historialmedico.HistorialMedicoUploader;
import java.util.List;

public class HistorialMedicoAdapter extends RecyclerView.Adapter<HistorialMedicoAdapter.ViewHolder> {

    private List<HistorialMedicoUploader> archivos;
    private Context context;

    public HistorialMedicoAdapter(Context context, List<HistorialMedicoUploader> archivos) {
        this.context = context;
        this.archivos = archivos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_archivo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistorialMedicoUploader archivo = archivos.get(position);
        holder.tvNombre.setText(archivo.getNombre());

        Log.d("DEBUG", "Nombre: " + archivo.getNombre() + ", URL: " + archivo.getRuta());

        // 👇 Agrega click para abrir el PDF
        holder.tvNombre.setOnClickListener(v -> {
            String Ruta = archivo.getRuta(); // Asegúrate que este método devuelva la URL válida del PDF
            if (Ruta != null && !Ruta.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(Ruta), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(context, "No se pudo abrir el archivo", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "No se encontró la URL del archivo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return archivos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvnombrearchivo);
        }
    }

    // Método para actualizar la lista y refrescar el RecyclerView
    public void actualizarLista(List<HistorialMedicoUploader> nuevosArchivos) {
        this.archivos = nuevosArchivos;
        notifyDataSetChanged();
    }
}