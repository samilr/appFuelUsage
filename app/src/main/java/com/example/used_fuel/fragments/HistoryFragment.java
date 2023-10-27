package com.example.used_fuel.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.used_fuel.FuelRecord;
import com.example.used_fuel.adapters.FuelRecordAdapter;
import com.example.used_fuel.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends Fragment {
    List<FuelRecord> fuelRecords;
    List<FuelRecord> filteredFuelRecords;
    RecyclerView recyclerView;
    FuelRecordAdapter adapter;
    TextView txtNotFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history, container, false);
        fuelRecords = readJsonFile();
        Collections.sort(fuelRecords);

        recyclerView = rootView.findViewById(R.id.rvHistory);
        txtNotFound = rootView.findViewById(R.id.txtNotFound);

        adapter = new FuelRecordAdapter(fuelRecords, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SwipeRefreshLayout swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            List<FuelRecord> refreshedData = readJsonFile();
            Collections.sort(refreshedData);
            if (refreshedData.isEmpty()) {
                txtNotFound.setVisibility(View.VISIBLE);
            } else {
                txtNotFound.setVisibility(View.GONE);
            }
            Toast.makeText(requireContext(), "Datos actualizados.", Toast.LENGTH_SHORT).show();
            adapter.setData(refreshedData);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });

        Button btnDatePicker = rootView.findViewById(R.id.btnDatePicker);
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        if (fuelRecords.isEmpty()) {
            txtNotFound.setVisibility(View.VISIBLE);
        } else {
            txtNotFound.setVisibility(View.GONE);
        }
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

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, monthOfYear, dayOfMonth);
                        filterRecyclerView(selectedDate);
                    }
                },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void filterRecyclerView(Calendar selectedDate) {
        filteredFuelRecords = new ArrayList<>();

        for (FuelRecord record : fuelRecords) {
            Calendar recordDate = Calendar.getInstance();
            recordDate.setTime(record.getDate());

            if (recordDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                    recordDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                    recordDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH)) {
                filteredFuelRecords.add(record);
            }
        }
        if (filteredFuelRecords.isEmpty()) {
            txtNotFound.setVisibility(View.VISIBLE);
        } else {
            txtNotFound.setVisibility(View.GONE);
        }
        adapter.setData(filteredFuelRecords);
        adapter.notifyDataSetChanged();
    }

}
