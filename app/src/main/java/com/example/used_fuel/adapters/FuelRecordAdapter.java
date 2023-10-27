package com.example.used_fuel.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.used_fuel.BuildConfig;
import com.example.used_fuel.FuelRecord;
import com.example.used_fuel.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class FuelRecordAdapter extends RecyclerView.Adapter<FuelRecordAdapter.ViewHolder> {
    private List<FuelRecord> fuelRecords;
    private Context context;

    public FuelRecordAdapter(List<FuelRecord> fuelRecords, Context context) {
        this.fuelRecords = fuelRecords;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_cards, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FuelRecord fuelRecord = fuelRecords.get(position);
        holder.txtDate.setText(formatDate(fuelRecord.getDate()));
        holder.txtDistancia.setText(fuelRecord.getDistanceKM().toString() + " KM");
        holder.txtKMG.setText(fuelRecord.getAverageKmByGal().toString() + " KM/G");
        holder.txtMPG.setText(fuelRecord.getAverageMiByGal().toString() + " MI/G");
        holder.txtKPL.setText(fuelRecord.getAverageKmByLI().toString() + " KM/L");
        holder.txtGalonesUsado.setText(fuelRecord.getFuelUsedGal().toString() + " GAL");
        holder.txtLitrosUsado.setText(fuelRecord.getFuelUsedLiters().toString() + " LI");
        holder.txtDineroGastado.setText("$" + fuelRecord.getMoneyUsed().toString() + " DOP");

        // Modifica la lógica del botón "Compartir" para capturar y compartir el CardView
        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btnShare.setVisibility(View.GONE);
                captureAndShareCardView(holder.cardView);
                holder.btnShare.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fuelRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Declaración de tus vistas
        TextView txtDate;
        TextView txtKMG;
        TextView txtMPG;
        TextView txtKPL;
        TextView txtGalonesUsado;
        TextView txtLitrosUsado;
        TextView txtDineroGastado;
        TextView txtDistancia;
        Button btnShare;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializa tus vistas
            txtDate = itemView.findViewById(R.id.txtDate);
            txtKMG = itemView.findViewById(R.id.txtKMG);
            txtMPG = itemView.findViewById(R.id.txtMPG);
            txtKPL = itemView.findViewById(R.id.txtKPL);
            txtGalonesUsado = itemView.findViewById(R.id.txtGalonesUsado);
            txtLitrosUsado = itemView.findViewById(R.id.txtLitrosUsados);
            txtDineroGastado = itemView.findViewById(R.id.txtDineroGastado);
            txtDistancia = itemView.findViewById(R.id.txtDistancia);
            btnShare = itemView.findViewById(R.id.btnShare);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }

    private void captureAndShareCardView(View viewToCapture) {
        // Captura la vista como una imagen
        Bitmap bitmap = viewToBitmap(viewToCapture);

        // Guarda la imagen en la memoria
        String imagePath = saveBitmapToStorage(bitmap);

        // Comparte la imagen
        shareImage(imagePath);
    }

    private Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private String saveBitmapToStorage(Bitmap bitmap) {
        String imagePath = "";
        try {
            File file = new File(context.getExternalCacheDir(), "cardview_capture.png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            imagePath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    private void shareImage(String imagePath) {
        File imageFile = new File(imagePath);
        Uri imageUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", imageFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Compartir captura de pantalla"));
    }

    public void setData(List<FuelRecord> fuelRecords) {
        this.fuelRecords = fuelRecords;
    }
    public String formatDate(Date fecha){
        SimpleDateFormat formato =new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.ENGLISH);
        return formato.format(fecha);
    }
}
