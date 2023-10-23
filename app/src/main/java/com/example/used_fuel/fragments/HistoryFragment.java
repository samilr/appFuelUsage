package com.example.used_fuel.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.used_fuel.FuelRecord;
import com.example.used_fuel.FuelRecordAdapter;
import com.example.used_fuel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class HistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);
        List<FuelRecord> fuelRecords = readJsonFile();
        Collections.sort(fuelRecords);
        RecyclerView recyclerView = rootView.findViewById(R.id.rvHistory);
        FuelRecordAdapter adapter = new FuelRecordAdapter(fuelRecords);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Realiza la operación de recarga de datos aquí, por ejemplo, volver a leer el archivo JSON
                List<FuelRecord> refreshedData = readJsonFile();
                Collections.sort(refreshedData);
                adapter.setData(refreshedData);
                adapter.notifyDataSetChanged();
                Toast.makeText(requireContext(), "Datos actualizados.", Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false); // Detener la animación de actualización
            }
        });

        return rootView;
    }
    private List<FuelRecord> readJsonFile() {
        List<FuelRecord> fuelRecords = new ArrayList<>();
        String filePath = getContext().getExternalFilesDir(null) + "/fuel_record.json";

        try {
            File file = new File(filePath);
            if (file.exists()) {
                FileInputStream inputStream = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(inputStream);
                fuelRecords = new Gson().fromJson(reader, new TypeToken<List<FuelRecord>>() {}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fuelRecords;
    }

}
