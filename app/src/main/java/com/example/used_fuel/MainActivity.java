package com.example.used_fuel;

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
    EditText txbDistance, txbKmByLitros;
    TextView txbMillesByGal, txbKilometerByGal, txbfuelUsed, txbMoneyUsed, txbKilometerByLiters;
    Spinner spMesureUnity, spDistanceUnity;
    Button btnCalcular, btnPdf;
    double averageFuelUsed, millesByGal, distance, moneyUsed, usedFuel, kilometerByGal, kilometerByLiters, litersKilometersToGalon = 3.785411784, kilometerToMilles = 0.621371, millesToKilometer = 1.60934;
    List<String> mesureUnity = Arrays.asList("(KM/L)", "(KM/G)", "(MI/G)", "(GAL)");
    List<String> distanceUnity = Arrays.asList("(KM)", "(MI)");
    String mesureUnityChoseen, distanceUnityChoosen;

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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, mesureUnity);
        spMesureUnity.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                R.layout.spinner_item, distanceUnity);
        spDistanceUnity.setAdapter(adapter2);

        txbKilometerByLiters = findViewById(R.id.txbKPL);
        txbKilometerByGal.setEnabled(false);
        txbMillesByGal.setEnabled(false);
        txbfuelUsed.setEnabled(false);
        txbMoneyUsed.setEnabled(false);
        txbKilometerByLiters.setEnabled(false);
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
        }
    }
    public void getData(double distanceAverage, double fuelUsedAverage){
        averageFuelUsed = fuelUsedAverage;
        distance = distanceAverage;
        kilometerByLiters = averageFuelUsed;

        kilometerByGal = averageFuelUsed * litersKilometersToGalon;
        millesByGal = kilometerByGal * kilometerToMilles;
        usedFuel = distance / kilometerByGal;
        moneyUsed = usedFuel * 293.60;

        formatValues();

        txbKilometerByGal.setText(kilometerByGal+ " KM/G");
        txbMillesByGal.setText(millesByGal + " MI/G");
        txbKilometerByLiters.setText(kilometerByLiters + " KM/L");
        txbfuelUsed.setText(usedFuel + " GAL");
        txbMoneyUsed.setText("$"+moneyUsed);
    }
    public void restartApp(View view){
        txbKilometerByGal.setText("");
        txbMillesByGal.setText("");
        txbKilometerByLiters.setText("");
        txbfuelUsed.setText("");
        txbMoneyUsed.setText("");
        txbDistance.setText("");
        txbKmByLitros.setText("");
    }
    public void makePdf(View view){
        if (isDataEmpty()){
            Toast.makeText(this, "Complete los campos para continuar", Toast.LENGTH_SHORT).show();
        }else{
            formatValues();
            GeneratePDF classGeneratePdf = new GeneratePDF(String.valueOf(kilometerByGal), String.valueOf(millesByGal), String.valueOf(kilometerByLiters), String.valueOf(usedFuel), String.valueOf(moneyUsed));

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
        kilometerByGal = Double.parseDouble(String.format("%.2f", kilometerByGal));
        millesByGal = Double.parseDouble(String.format("%.2f", millesByGal));
        kilometerByLiters = Double.parseDouble(String.format("%.2f", kilometerByLiters));
        usedFuel = Double.parseDouble(String.format("%.2f", usedFuel));
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
        if (txbDistance.getText().toString().isEmpty() && txbKmByLitros.getText().toString().isEmpty()
                || txbKmByLitros.getText().toString().isEmpty() || txbDistance.getText().toString().isEmpty()){
            return true;
        }else {
            return false;
        }
    }
    public boolean isDataEmpty(){
        if (txbMillesByGal.getText().toString().isEmpty() && txbKilometerByGal.getText().toString().isEmpty()
                && txbfuelUsed.getText().toString().isEmpty() && txbMoneyUsed.getText().toString().isEmpty() && txbKilometerByLiters.getText().toString().isEmpty()){
            return true;
        }else{
            return false;
        }
    }
}