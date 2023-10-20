package com.example.used_fuel;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
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
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.used_fuel/files/consumption_history.json";
            File file = new File(path);
            boolean fileExists = file.exists();
            FileWriter fileWriter = new FileWriter(path, fileExists);

            if (!fileExists) {
                fileWriter.write("[\n");
            } else {
                fileWriter.write(",\n");
            }

            fileWriter.write(jsonData);
            fileWriter.close();
            if (fileExists) {
                FileWriter closingBracketWriter = new FileWriter(path, true);
                closingBracketWriter.write("]");
                closingBracketWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
