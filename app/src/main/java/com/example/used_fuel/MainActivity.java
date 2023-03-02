package com.example.used_fuel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText txbMpg, txbKpg, txbDistanciaRecorrida, txbKMLitros, txbCombustibleUsado, txbDineroGastado, txbKPL;
    Button btnCalcular;
    double KMPG, KMPL, MPG, distancia, dineroGastado, combustibleUsado, litrosToKM = 3.785411784, kmToMillas = 0.621371;
    List<String> mesureUnity = Arrays.asList("Desayuno", "Almuerzo", "Merienda", "Cena", "Chatarra");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String foodChoosen;
        inicialiced();

    }

    public void inicialiced(){
        txbMpg = findViewById(R.id.txbMPG);
        txbKpg = findViewById(R.id.txbKMG);
        txbDistanciaRecorrida = findViewById(R.id.txbDistancia);
        txbKMLitros = findViewById(R.id.txbKmLitro);
        btnCalcular = findViewById(R.id.btnCalcular);
        txbCombustibleUsado = findViewById(R.id.txbCombustibleUsado);
        txbDineroGastado = findViewById(R.id.txbDineroGastado);
        txbKPL = findViewById(R.id.txbKPL);
        txbKpg.setEnabled(false);
        txbMpg.setEnabled(false);
        txbCombustibleUsado.setEnabled(false);
        txbDineroGastado.setEnabled(false);
        txbKPL.setEnabled(false);
    }
    public void calcularConsumo(View view){
        if (txbDistanciaRecorrida.getText().toString().isEmpty() && txbKMLitros.getText().toString().isEmpty() || txbKMLitros.getText().toString().isEmpty() || txbDistanciaRecorrida.getText().toString().isEmpty()){
            Toast.makeText(this, "Complete los campos para calcular", Toast.LENGTH_SHORT).show();
        }else {
            distancia = Double.parseDouble(txbDistanciaRecorrida.getText().toString());
            KMPL = Double.parseDouble(txbKMLitros.getText().toString());
            KMPG = KMPL * litrosToKM;
            MPG = KMPG * kmToMillas;
            combustibleUsado = distancia / KMPG;
            dineroGastado = combustibleUsado * 293.60;

            txbKpg.setText(String.format("%.2f", KMPG)+ " KM/G");
            txbMpg.setText(String.format("%.2f", MPG) + " M/G");
            txbKPL.setText(String.format("%.2f", KMPL) + " KM/L");
            txbCombustibleUsado.setText(String.format("%.2f", combustibleUsado) + " GALONES");
            txbDineroGastado.setText("$"+String.format("%.2f", dineroGastado) + " DOP");

            txbDistanciaRecorrida.setText("");
            txbDistanciaRecorrida.setHint(distancia + " KM RECORRIDOS");

            txbKMLitros.setText("");
            txbKMLitros.setHint(KMPL + " KM/L");
        }
    }
    public void reiniciar(View view){
        txbKpg.setText("");
        txbMpg.setText("");
        txbKPL.setText("");
        txbCombustibleUsado.setText("");
        txbDineroGastado.setText("");
        txbDistanciaRecorrida.setText("");
        txbKMLitros.setText("");
        txbKMLitros.setHint("CONSUMO KM/L (KM)");
        txbDistanciaRecorrida.setHint("DISTANCIA RECORRIDA (KM)");
    }


}