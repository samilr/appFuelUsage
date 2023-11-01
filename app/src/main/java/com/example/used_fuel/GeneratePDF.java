package com.example.used_fuel;

import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeneratePDF {
    private String _distanciaRecorridaKM;
    private String _distanciaRecorridaMI;
    private String _consumoKMG;
    private String _consumoMIG;
    private String _consumoKML;
    private String _combustibleUsadoGalones;
    private String _combustibleUsadoLitros;
    private String _dineroUsado;
    private Date _currentDate;
    private String pdfFileName;
    private String _priceByGal;
    private String _priceByLiter;

    String currrentDate;
    public GeneratePDF(String distanciaRecorridaKM, String distanciaRecorridaMI, String consumoKM_G, String consumoMI_G, String consumoKM_L, String combustibleUsado, String usedFuelLiters, String dineroUsado, String priceByGal, String priceByLiter){
        this._distanciaRecorridaKM = distanciaRecorridaKM;
        this._distanciaRecorridaMI = distanciaRecorridaMI;
        this._consumoKMG =  consumoKM_G;
        this._consumoMIG = consumoMI_G;
        this._consumoKML = consumoKM_L;
        this._combustibleUsadoGalones = combustibleUsado;
        this._combustibleUsadoLitros = usedFuelLiters;
        this._dineroUsado = dineroUsado;
        this._priceByGal = priceByGal;
        this._priceByLiter = priceByLiter;
        this._currentDate = new Date();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        currrentDate = format.format(_currentDate);
    }

    public void generatePDF(){
        Document document = new Document();
        try {
            this.pdfFileName = "REPORTE_DE_CONSUMO";
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/Android/data/com.example.used_fuel/files/" + this.pdfFileName + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(path));
            PdfPTable table = new PdfPTable(2);
            document.open();

            Paragraph title = new Paragraph("CONSUMO DE COMBUSTIBLE SG " + currrentDate);
            Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);

            title.setAlignment(Element.ALIGN_CENTER);
            title.getFont().setSize(20f);
            title.setFont(boldFont);

            document.add(title);
            document.add(new Paragraph(" "));

            table.addCell("FECHA:");
            table.addCell(currrentDate);

            table.addCell("DISTANCIA RECORRIDA (KM):");
            table.addCell(_distanciaRecorridaKM + " Kilometros");

            table.addCell("DISTANCIA RECORRIDA (MI):");
            table.addCell(_distanciaRecorridaMI + " Millas");

            table.addCell("CONSUMO KM/G:");
            table.addCell(_consumoKMG + " Kilometros por Galon");

            table.addCell("CONSUMO MI/G:");
            table.addCell(_consumoMIG + " Millas por Galon");

            table.addCell("CONSUMO KM/L:");
            table.addCell(_consumoKML + " Kilometros por Litros");

            table.addCell("COMBUSTIBLE USADO (GAL):");
            table.addCell(_combustibleUsadoGalones + " GALONES");

            table.addCell("COMBUSTIBLE USADO (LI):");
            table.addCell(_combustibleUsadoLitros + " LITROS");

            table.addCell("DINERO USADO:");
            table.addCell("RD$ " + _dineroUsado + " DOP");

            table.addCell("PRECIO POR GALON:");
            table.addCell("RD$ " + _priceByGal + " DOP");

            table.addCell("PRECIO POR LITRO:");
            table.addCell("RD$ " + _priceByLiter + " DOP");

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
