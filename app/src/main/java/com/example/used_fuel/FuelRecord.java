package com.example.used_fuel;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.util.Date;

public class FuelRecord {
    private String _distanciaRecorridaKM;
    private String _distanciaRecorridaMI;
    private String _consumoKMG;
    private String _consumoMIG;
    private String _consumoKML;
    private String _combustibleUsadoGalones;
    private String _combustibleUsadoLitros;
    private String _dineroUsado;
    private Date _currentDate;

    public FuelRecord(String _distanciaRecorridaKM, String _distanciaRecorridaMI, String _consumoKMG, String _consumoMIG, String _consumoKML, String _combustibleUsadoGalones, String _combustibleUsadoLitros, String _dineroUsado) {
        this._distanciaRecorridaKM = _distanciaRecorridaKM;
        this._distanciaRecorridaMI = _distanciaRecorridaMI;
        this._consumoKMG = _consumoKMG;
        this._consumoMIG = _consumoMIG;
        this._consumoKML = _consumoKML;
        this._combustibleUsadoGalones = _combustibleUsadoGalones;
        this._combustibleUsadoLitros = _combustibleUsadoLitros;
        this._dineroUsado = _dineroUsado;
        this._currentDate = _currentDate;
    }


}
