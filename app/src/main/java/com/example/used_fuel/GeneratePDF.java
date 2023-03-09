package com.example.used_fuel;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Environment;
import android.text.TextPaint;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
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
            PdfPTable table = new PdfPTable(2);
           // Image image = Image.getInstance("ruta/de/tu/imagen.png");

            document.open();

            Paragraph title = new Paragraph("CONSUMO DE COMBUSTIBLE SG " + currrentDate);
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

            title.setAlignment(Element.ALIGN_CENTER);
            title.getFont().setSize(20f);
            title.setFont(boldFont);

            document.add(title);
            document.add(new Paragraph(" "));

            Font tableFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLACK);

            table.addCell("FECHA:");
            table.addCell(currrentDate);

            table.addCell("CONSUMO KM/G:");
            table.addCell(_consumoKMG + " KM/G");

            table.addCell("CONSUMO MI/G:");
            table.addCell(_consumoMIG + " MI/G");

            table.addCell("CONSUMO KM/L:");
            table.addCell(_consumoKML + " KM/L");

            table.addCell("COMBUSTIBLE USADO:");
            table.addCell(_combustibleUsado + " GALONES");

            table.addCell("DINERO USADO:");
            table.addCell("RD$ " + _dineroUsado + " DOP");

            document.add(table);
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
