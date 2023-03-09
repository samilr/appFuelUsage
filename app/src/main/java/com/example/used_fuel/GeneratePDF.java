package com.example.used_fuel;

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratePDF {

    private String _consumoKMG;
    private String _consumoMIG;
    private String _consumoKML;
    private String _combustibleUsado;
    private String _dineroUsado;
    private Date _currentDate;
    private String pdfFileName;

    String currrentDate;
    public GeneratePDF(String consumoKM_G, String consumoMI_G, String consumoKM_L, String combustibleUsado, String dineroUsado){
        this._consumoKMG =  consumoKM_G;
        this._consumoMIG = consumoMI_G;
        this._consumoKML = consumoKM_L;
        this._combustibleUsado = combustibleUsado;
        this._dineroUsado = dineroUsado;
        this._currentDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        currrentDate = format.format(_currentDate);
    }

    public void generatePDF(){
        Document document = new Document();
        try {
            this.pdfFileName = "CONSUMO_DE_COMBUSTIBLE_" + currrentDate;
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.used_fuel/files/" + this.pdfFileName + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            document.open();

            // Agrega el título "REPORTE"
            Paragraph title = new Paragraph("Reporte de Consumo " + currrentDate);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Agrega los datos de las variables
            document.add(new Paragraph("Fecha: " + currrentDate));
            document.add(new Paragraph("Consumo KM/G: " +  _consumoKMG));
            document.add(new Paragraph("Consumo MI/G: " + _consumoMIG));
            document.add(new Paragraph("Consumo KM/L: " + _consumoKML));
            document.add(new Paragraph("Combustible usado: " + _combustibleUsado));
            document.add(new Paragraph("Dinero usado: $" + _dineroUsado));

            document.close();

          this.pdfFileName = "CONSUMO_DE_COMBUSTIBLE_" + currrentDate;

        }catch (SecurityException | FileNotFoundException e){
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPdfFileName() {
        return pdfFileName;
    }
}
