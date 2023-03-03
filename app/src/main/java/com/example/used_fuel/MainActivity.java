package com.example.used_fuel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    EditText txbDistance, txbKmByLitros;
    TextView txbMillesByGal, txbKilometerByGal, txbfuelUsed, txbMoneyUsed, txbKilometerByLiters;
    Spinner spMesureUnity, spDistanceUnity;
    Button btnCalcular;
    double averageFuelUsed, millesByGal, distance, moneyUsedByGas, usedFuel, kilometerByGal, litersKilometersToGalon = 3.785411784, kilometerToMilles = 0.621371, millesToKilometer = 1.60934, kilometerByLiters;
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
        if (txbDistance.getText().toString().isEmpty() && txbKmByLitros.getText().toString().isEmpty()
                || txbKmByLitros.getText().toString().isEmpty() || txbDistance.getText().toString().isEmpty()){
            Toast.makeText(this, "Complete los campos para calcular", Toast.LENGTH_SHORT).show();
        }else {
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
        moneyUsedByGas = usedFuel * 293.60;

        txbKilometerByGal.setText(String.format("%.2f", kilometerByGal)+ " KM/G");
        txbMillesByGal.setText(String.format("%.2f", millesByGal) + " MI/G");
        txbKilometerByLiters.setText(String.format("%.2f", kilometerByLiters) + " KM/L");
        txbfuelUsed.setText(String.format("%.2f", usedFuel) + " GAL");
        txbMoneyUsed.setText("$"+String.format("%.2f", moneyUsedByGas));
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

}