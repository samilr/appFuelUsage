package com.example.used_fuel;

public class FuelRecord implements Comparable<FuelRecord> {
    private String averageKmByGal;
    private String averageKmByLI;
    private String averageMiByGal;
    private String date;
    private String distanceKM;
    private String distanceMI;
    private String fuelUsedGal;
    private String fuelUsedLiters;
    private String moneyUsed;
    private String priceByGal;
    private String priceByLiter;

    public String getAverageKmByGal() {
        return averageKmByGal;
    }

    public void setAverageKmByGal(String averageKmByGal) {
        this.averageKmByGal = averageKmByGal;
    }

    public String getAverageKmByLI() {
        return averageKmByLI;
    }

    public void setAverageKmByLI(String averageKmByLI) {
        this.averageKmByLI = averageKmByLI;
    }

    public String getAverageMiByGal() {
        return averageMiByGal;
    }

    public void setAverageMiByGal(String averageMiByGal) {
        this.averageMiByGal = averageMiByGal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistanceKM() {
        return distanceKM;
    }

    public void setDistanceKM(String distanceKM) {
        this.distanceKM = distanceKM;
    }

    public String getDistanceMI() {
        return distanceMI;
    }

    public void setDistanceMI(String distanceMI) {
        this.distanceMI = distanceMI;
    }

    public String getFuelUsedGal() {
        return fuelUsedGal;
    }

    public void setFuelUsedGal(String fuelUsedGal) {
        this.fuelUsedGal = fuelUsedGal;
    }

    public String getFuelUsedLiters() {
        return fuelUsedLiters;
    }

    public void setFuelUsedLiters(String fuelUsedLiters) {
        this.fuelUsedLiters = fuelUsedLiters;
    }

    public String getMoneyUsed() {
        return moneyUsed;
    }

    public void setMoneyUsed(String moneyUsed) {
        this.moneyUsed = moneyUsed;
    }

    public String getPriceByGal() {
        return priceByGal;
    }

    public void setPriceByGal(String priceByGal) {
        this.priceByGal = priceByGal;
    }

    public String getPriceByLiter() {
        return priceByLiter;
    }

    public void setPriceByLiter(String priceByLiter) {
        this.priceByLiter = priceByLiter;
    }

    @Override
    public int compareTo(FuelRecord o) {
        return o.getDate().compareTo(this.getDate());
    }
// Agrega getters y setters
}
