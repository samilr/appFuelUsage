package com.example.used_fuel;

import android.os.Environment;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class SaveFuelDataRecord {
    private Date date;
    private String distanceKM;
    private String distanceMI;
    private String averageKmByGal;
    private String averageMiByGal;
    private String averageKmByLI;
    private String fuelUsedGal;
    private String fuelUsedLiters;
    private String moneyUsed;
    private String priceByGal;
    private String priceByLiter;

    public SaveFuelDataRecord(String distanceKM, String distanceMI, String averageKmByGal, String averageMiByGal, String averageKmByLI, String fuelUsedGal, String fuelUsedLiters, String moneyUsed, String priceByGal, String priceByLiter) {
        this.date = new Date();;
        this.distanceKM = distanceKM;
        this.distanceMI = distanceMI;
        this.averageKmByGal = averageKmByGal;
        this.averageMiByGal = averageMiByGal;
        this.averageKmByLI = averageKmByLI;
        this.fuelUsedGal = fuelUsedGal;
        this.fuelUsedLiters = fuelUsedLiters;
        this.moneyUsed = moneyUsed;
        this.priceByGal = priceByGal;
        this.priceByLiter = priceByLiter;
    }
    public void saveDataAsJSON() {
        Gson gson = new Gson();
        String jsonData = gson.toJson(this);

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.used_fuel/files/fuel_record.json";
            File file = new File(path);
            boolean fileExists = file.exists();

            if (!fileExists) {
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(new JSONObject(jsonData));
                FileWriter fileWriter = new FileWriter(path);
                fileWriter.write(jsonArray.toString(4));  // 4 es el número de espacios para la indentación
                fileWriter.close();
            } else {
                // Lee el archivo JSON existente y agrega el nuevo JSON al array existente
                BufferedReader br = new BufferedReader(new FileReader(path));
                StringBuilder content = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                }

                JSONArray jsonArray = new JSONArray(content.toString());
                jsonArray.put(new JSONObject(jsonData));

                FileWriter fileWriter = new FileWriter(path);
                fileWriter.write(jsonArray.toString(4));  // 4 es el número de espacios para la indentación
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
