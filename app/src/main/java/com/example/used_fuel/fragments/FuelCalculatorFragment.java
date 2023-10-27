package com.example.used_fuel.fragments;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.used_fuel.BuildConfig;
import com.example.used_fuel.GeneratePDF;
import com.example.used_fuel.R;
import com.example.used_fuel.SaveFuelDataRecord;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FuelCalculatorFragment extends Fragment {
    EditText txbDistance, txbKmByLitros, txbPrice;
    TextView txbMillesByGal, txbKilometerByGal, txbfuelUsed, txbMoneyUsed, txbKilometerByLiters;
    CardView cardViewResult;
    Spinner spMesureUnity, spDistanceUnity, spGasUnity;
    Button btnCalcular, btnPdf, btnReiniciar, btnShareImage;
    double averageFuelUsed, usedFuelLiters, millesByGal, distance, distanceMI, moneyUsed, usedFuelGal, kilometerByGal, kilometerByLiters;
    double litersKilometersToGalon = 3.785411784, kilometerToMilles = 0.621371, millesToKilometer = 1.60934, gasPriceByGal, gasPriceByLiter = gasPriceByGal / litersKilometersToGalon;
    List<String> mesureUnity = Arrays.asList("(KM/L)", "(KM/G)", "(MI/G)", "(GAL)");
    List<String> distanceUnity = Arrays.asList("(KM)", "(MI)");
    List<String> gasUnity = Arrays.asList("(GAL)", "(LI)");
    String mesureUnityChoseen, distanceUnityChoosen, gasUnityChoseen;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el diseño del fragmento
        View rootView = inflater.inflate(R.layout.fuel_calculator, container, false);

        // Llama al método appComponets() después de inflar el diseño
        appComponets(rootView);
        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateFuelUsed();
            }
        });

        btnReiniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartApp();
            }
        });

        btnPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePdf();
            }
        });
        btnShareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDataEmpty()){
                    captureAndShareCardView(cardViewResult);
                }else {
                    Toast.makeText(requireContext(), "Primero debe realizar un calculo.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }
    public void appComponets(View rootView){
        txbMillesByGal = rootView.findViewById(R.id.txbMPG);
        txbKilometerByGal = rootView.findViewById(R.id.txbKMG);
        txbDistance = rootView.findViewById(R.id.txbDistancia);
        txbKmByLitros = rootView.findViewById(R.id.txbKmLitro);
        btnCalcular = rootView.findViewById(R.id.btnCalcular);
        txbfuelUsed = rootView.findViewById(R.id.txbCombustibleUsado);
        txbMoneyUsed = rootView.findViewById(R.id.txbDineroGastado);
        spMesureUnity = rootView.findViewById(R.id.spUnit);
        spDistanceUnity = rootView.findViewById(R.id.spDistancia);
        btnPdf = rootView.findViewById(R.id.btnPdf);
        btnReiniciar = rootView.findViewById(R.id.btnReiniciar);
        txbPrice = rootView.findViewById(R.id.txbPrice);
        spGasUnity = rootView.findViewById(R.id.spGasUnity);
        txbKilometerByLiters = rootView.findViewById(R.id.txbKPL);
        btnShareImage = rootView.findViewById(R.id.btnSaveImage);
        cardViewResult = rootView.findViewById(R.id.cardViewResult);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.spinner_item, mesureUnity);
        spMesureUnity.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(),
                R.layout.spinner_item, distanceUnity);
        spDistanceUnity.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(requireContext(),
                R.layout.spinner_item, gasUnity);
        spGasUnity.setAdapter(adapter3);

        txbKilometerByGal.setEnabled(false);
        txbMillesByGal.setEnabled(false);
        txbfuelUsed.setEnabled(false);
        txbMoneyUsed.setEnabled(false);
        txbKilometerByLiters.setEnabled(false);
        txbPrice.setText("293.10");
    }
    public void calculateFuelUsed(){
        if (isInputEmpty()){
            Toast.makeText(requireContext(), "Complete los campos para calcular", Toast.LENGTH_SHORT).show();
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
            saveData();
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
    public void saveData(){
        if (isDataEmpty()){
            Toast.makeText(requireContext(), "Complete los campos para continuar", Toast.LENGTH_SHORT).show();
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
            if (hasPermision()) {
                saveFuelDataRecord.saveDataAsJSON();
                Toast.makeText(requireContext(), "Guardado correctamente.", Toast.LENGTH_SHORT).show();
            }else {
                askPermision();
            }
        }
    }
    public void restartApp(){
        txbKilometerByGal.setText("");
        txbMillesByGal.setText("");
        txbKilometerByLiters.setText("");
        txbfuelUsed.setText("");
        txbMoneyUsed.setText("");
        txbDistance.setText("");
        txbKmByLitros.setText("");
        txbPrice.setText("293.10");
    }
    public void makePdf(){
        if (isDataEmpty()){
            Toast.makeText(getContext(), "Complete los campos para continuar", Toast.LENGTH_SHORT).show();
        }else{
            formatValues();
            if (hasPermision()) {
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
                classGeneratePdf.generatePDF();
                sharePdf(classGeneratePdf);
            } else {
                askPermision();
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
        File directorio = requireContext().getExternalFilesDir(null);
        File archivoPDF = new File(directorio, generatePDF.getPdfFileName() + ".pdf");
        Uri uri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".fileprovider", archivoPDF);
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
    public boolean hasPermision(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && Environment.isExternalStorageManager();
    }
    public void askPermision(){
        if (!hasPermision()){
            Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(intent);
        }
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
            File file = new File(getContext().getExternalCacheDir(), "cardview_capture.png");
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
        Uri imageUri = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", imageFile);

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Compartir captura de pantalla"));
    }
    private void captureAndShareCardView(View viewToCapture) {
        btnShareImage.setVisibility(View.GONE);
        // Captura la vista como una imagen
        Bitmap bitmap = viewToBitmap(viewToCapture);

        // Guarda la imagen en la memoria
        String imagePath = saveBitmapToStorage(bitmap);

        // Comparte la imagen
        shareImage(imagePath);
        btnShareImage.setVisibility(View.VISIBLE);
    }



}