package com.example.used_fuel;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
public class MainActivity extends AppCompatActivity {
    EditText txbDistance, txbKmByLitros, txbPrice;
    TextView txbMillesByGal, txbKilometerByGal, txbfuelUsed, txbMoneyUsed, txbKilometerByLiters;
    Spinner spMesureUnity, spDistanceUnity, spGasUnity;
    Button btnCalcular, btnPdf;
    double averageFuelUsed, usedFuelLiters, millesByGal, distance, distanceMI, moneyUsed, usedFuelGal, kilometerByGal, kilometerByLiters;
    double litersKilometersToGalon = 3.785411784, kilometerToMilles = 0.621371, millesToKilometer = 1.60934, gasPriceByGal, gasPriceByLiter = gasPriceByGal / litersKilometersToGalon;
    List<String> mesureUnity = Arrays.asList("(KM/L)", "(KM/G)", "(MI/G)", "(GAL)");
    List<String> distanceUnity = Arrays.asList("(KM)", "(MI)");
    List<String> gasUnity = Arrays.asList("(GAL)", "(LI)");
    String mesureUnityChoseen, distanceUnityChoosen, gasUnityChoseen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appComponets();
    }

    public void appComponets(){
        txbMillesByGal = findViewById(R.id.txbMPG);
        txbKilometerByGal = findViewById(R.id.txbKMG);
        txbDistance = findViewById(R.id.txbDistancia);
        txbKmByLitros = findViewById(R.id.txbKmLitro);
        btnCalcular = findViewById(R.id.btnCalcular);
        txbfuelUsed = findViewById(R.id.txbCombustibleUsado);
        txbMoneyUsed = findViewById(R.id.txbDineroGastado);
        spMesureUnity = findViewById(R.id.spUnit);
        spDistanceUnity = findViewById(R.id.spDistancia);
        btnPdf = findViewById(R.id.btnPdf);
        txbPrice = findViewById(R.id.txbPrice);
        spGasUnity = findViewById(R.id.spGasUnity);
        txbPrice.setText(String.valueOf(gasPriceByGal));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, mesureUnity);
        spMesureUnity.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                R.layout.spinner_item, distanceUnity);
        spDistanceUnity.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this,
                R.layout.spinner_item, gasUnity);
        spGasUnity.setAdapter(adapter3);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_layout);

        txbKilometerByLiters = findViewById(R.id.txbKPL);
        txbKilometerByGal.setEnabled(false);
        txbMillesByGal.setEnabled(false);
        txbfuelUsed.setEnabled(false);
        txbMoneyUsed.setEnabled(false);
        txbKilometerByLiters.setEnabled(false);
        txbPrice.setText("293.10");
    }
    public void calculateFuelUsed(View view){
        if (isInputEmpty()){
            Toast.makeText(this, "Complete los campos para calcular", Toast.LENGTH_SHORT).show();
        }
        else {
            distance = Double.parseDouble(txbDistance.getText().toString());
            averageFuelUsed = Double.parseDouble(txbKmByLitros.getText().toString());
            mesureUnityChoseen = spMesureUnity.getSelectedItem().toString();
            distanceUnityChoosen = spDistanceUnity.getSelectedItem().toString();
            gasUnityChoseen = spGasUnity.getSelectedItem().toString();

            if (Objects.equals(distanceUnityChoosen, "(MI)")){
                distance *= millesToKilometer;
            }
            switch (mesureUnityChoseen){
                case "(KM/L)":
                    getData(distance, averageFuelUsed);
                    break;
                case "(KM/G)":
                    averageFuelUsed /= litersKilometersToGalon;
                    getData(distance, averageFuelUsed);
                    break;
                case "(MI/G)":
                    averageFuelUsed *= millesToKilometer;
                    averageFuelUsed /= litersKilometersToGalon;
                    getData(distance, averageFuelUsed);
                    break;
                case "(GAL)":
                    averageFuelUsed = distance / averageFuelUsed;
                    averageFuelUsed /= litersKilometersToGalon;
                    getData(distance, averageFuelUsed);
                    break;
            }
            saveData(this.getCurrentFocus());
        }
    }
    public void getData(double distanceAverage, double fuelUsedAverage){
        averageFuelUsed = fuelUsedAverage;
        distance = distanceAverage;
        kilometerByLiters = averageFuelUsed;
        kilometerByGal = averageFuelUsed * litersKilometersToGalon;
        millesByGal = kilometerByGal * kilometerToMilles;
        usedFuelGal = distance / kilometerByGal;
        usedFuelLiters = distance / kilometerByLiters;
        formatValues();

        switch (gasUnityChoseen){
            case "(GAL)":
                txbfuelUsed.setText(usedFuelGal + " GALONES");
                break;
            case "(LI)":
                txbfuelUsed.setText(usedFuelLiters + " LITROS");
                break;
        }
        txbKilometerByGal.setText(kilometerByGal+ " KM/G");
        txbMillesByGal.setText(millesByGal + " MI/G");
        txbKilometerByLiters.setText(kilometerByLiters + " KM/L");
        txbMoneyUsed.setText("$" + moneyUsed + " DOP");
    }
    public void saveData(View view){
        if (isDataEmpty()){
            Toast.makeText(this, "Complete los campos para continuar", Toast.LENGTH_SHORT).show();
        }else{
            formatValues();
            SaveFuelDataRecord saveFuelDataRecord = new SaveFuelDataRecord(
                    String.valueOf(distance),
                    String.valueOf(distanceMI),
                    String.valueOf(kilometerByGal),
                    String.valueOf(millesByGal),
                    String.valueOf(kilometerByLiters),
                    String.valueOf(usedFuelGal),
                    String.valueOf(usedFuelLiters),
                    String.valueOf(moneyUsed),
                    String.valueOf(gasPriceByGal),
                    String.valueOf(gasPriceByLiter));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                saveFuelDataRecord.saveDataAsJSON();
                Toast.makeText(this, "Guardado correctamente.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void restartApp(View view){
        txbKilometerByGal.setText("");
        txbMillesByGal.setText("");
        txbKilometerByLiters.setText("");
        txbfuelUsed.setText("");
        txbMoneyUsed.setText("");
        txbDistance.setText("");
        txbKmByLitros.setText("");
        txbPrice.setText("293.10");
    }
    public void makePdf(View view){
        if (isDataEmpty()){
            Toast.makeText(this, "Complete los campos para continuar", Toast.LENGTH_SHORT).show();
        }else{
            formatValues();
            GeneratePDF classGeneratePdf = new GeneratePDF(
                    String.valueOf(distance),
                    String.valueOf(distanceMI),
                    String.valueOf(kilometerByGal),
                    String.valueOf(millesByGal),
                    String.valueOf(kilometerByLiters),
                    String.valueOf(usedFuelGal),
                    String.valueOf(usedFuelLiters),
                    String.valueOf(moneyUsed),
                    String.valueOf(gasPriceByGal),
                    String.valueOf(gasPriceByLiter));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager()) {
                classGeneratePdf.generatePDF();
                sharePdf(classGeneratePdf);
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        }
    }
    public void formatValues(){
        distanceMI = distance * kilometerToMilles;

        if ("(GAL)".equals(gasUnityChoseen)) {
            gasPriceByGal = Double.parseDouble(txbPrice.getText().toString());
            gasPriceByLiter = gasPriceByGal / litersKilometersToGalon;
            moneyUsed = usedFuelGal * gasPriceByGal;
            txbfuelUsed.setText(usedFuelGal + " GALONES");
        } else {
            gasPriceByLiter = Double.parseDouble(txbPrice.getText().toString());
            gasPriceByGal = gasPriceByLiter * litersKilometersToGalon;
            moneyUsed = usedFuelLiters * gasPriceByLiter;
            txbfuelUsed.setText(usedFuelLiters + " LITROS");
        }

        distanceMI = Double.parseDouble(String.format("%.2f", distanceMI));
        usedFuelLiters = Double.parseDouble(String.format("%.2f", usedFuelLiters));
        kilometerByGal = Double.parseDouble(String.format("%.2f", kilometerByGal));
        millesByGal = Double.parseDouble(String.format("%.2f", millesByGal));
        kilometerByLiters = Double.parseDouble(String.format("%.2f", kilometerByLiters));
        usedFuelGal = Double.parseDouble(String.format("%.2f", usedFuelGal));
        usedFuelLiters = Double.parseDouble(String.format("%.2f", usedFuelLiters));
        moneyUsed = Double.parseDouble(String.format("%.2f", moneyUsed));
    }
    public void sharePdf(GeneratePDF generatePDF) {
        Intent compartirIntent = new Intent(Intent.ACTION_SEND);
        compartirIntent.setType("application/pdf");
        File directorio = getExternalFilesDir(null);
        File archivoPDF = new File(directorio, generatePDF.getPdfFileName() + ".pdf");
        Uri uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", archivoPDF);
        compartirIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(compartirIntent, "Compartir archivo PDF"));
    }
    public boolean isInputEmpty(){
        return txbDistance.getText().toString().isEmpty()
                || txbKmByLitros.getText().toString().isEmpty()
                || txbPrice.getText().toString().isEmpty();
    }
    public boolean isDataEmpty(){
            return txbMillesByGal.getText().toString().isEmpty()
                    || txbKilometerByGal.getText().toString().isEmpty()
                    || txbfuelUsed.getText().toString().isEmpty()
                    || txbMoneyUsed.getText().toString().isEmpty()
                    || txbKilometerByLiters.getText().toString().isEmpty();
    }
}