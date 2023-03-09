package com.example.used_fuel;

import android.os.Environment;
import android.widget.Toast;

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

        //MainActivity mainActivity = new MainActivity();

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
            document.add(new Paragraph("CONSUMO KM/G: " +  _consumoKMG + " KM/G"));
            document.add(new Paragraph("CONSUMO MI/G: " + _consumoMIG + " MI/G"));
            document.add(new Paragraph("CONSUMO KM/L: " + _consumoKML + " KM/L"));
            document.add(new Paragraph("COMBUSTIBLE USADO: " + _combustibleUsado + " GALONES"));
            document.add(new Paragraph("DINERO USADO: RD$" + _dineroUsado + " DOP"));
            document.close();

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
