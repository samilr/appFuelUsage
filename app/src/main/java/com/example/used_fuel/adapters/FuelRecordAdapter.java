package com.example.used_fuel.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.used_fuel.FuelRecord;
import com.example.used_fuel.R;

import java.util.List;

public class FuelRecordAdapter extends RecyclerView.Adapter<FuelRecordAdapter.ViewHolder> {
    private List<FuelRecord> fuelRecords;

    public FuelRecordAdapter(List<FuelRecord> fuelRecords) {
        this.fuelRecords = fuelRecords;
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

        holder.txtDate.setText(fuelRecord.getDate().toString());
        holder.txtDistancia.setText(fuelRecord.getDistanceKM().toString() + " KM");
        holder.txtKMG.setText(fuelRecord.getAverageKmByGal().toString() + " KM/G");
        holder.txtMPG.setText(fuelRecord.getAverageMiByGal().toString() + " MI/G");
        holder.txtKPL.setText(fuelRecord.getAverageKmByLI().toString() + " KM/L");
        holder.txtGalonesUsado.setText(fuelRecord.getFuelUsedGal().toString() + " GALONES");
        holder.txtLitrosUsado.setText(fuelRecord.getFuelUsedLiters().toString() + " LITROS");
        holder.txtDineroGastado.setText("$" + fuelRecord.getMoneyUsed().toString() + " DOP");

        // Configura los demás campos de FuelRecord de la misma manera
    }

    @Override
    public int getItemCount() {
        return fuelRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtKMG;
        TextView txtMPG;
        TextView txtKPL;
        TextView txtGalonesUsado;
        TextView txtLitrosUsado;
        TextView txtDineroGastado;
        TextView txtDistancia;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtKMG = itemView.findViewById(R.id.txtKMG);
            txtMPG = itemView.findViewById(R.id.txtMPG);
            txtKPL = itemView.findViewById(R.id.txtKPL);
            txtGalonesUsado = itemView.findViewById(R.id.txtGalonesUsado);
            txtLitrosUsado = itemView.findViewById(R.id.txtLitrosUsados);
            txtDineroGastado = itemView.findViewById(R.id.txtDineroGastado);
            txtDistancia = itemView.findViewById(R.id.txtDistancia);
        }
    }

    public void setData(List<FuelRecord> fuelRecords) {
        this.fuelRecords = fuelRecords;
    }
}
