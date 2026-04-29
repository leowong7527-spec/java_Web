package ict.bean;

import java.time.LocalTime;

public class ClinicService {
    private final int id;
    private final String clinicName;
    private final String serviceName;
    private final int dailyQuota;
    private final int slotCapacity;
    private final LocalTime openingTime;
    private final LocalTime closingTime;

    public ClinicService(int id, String clinicName, String serviceName, int dailyQuota) {
        this(id, clinicName, serviceName, dailyQuota, 1, LocalTime.of(9, 0), LocalTime.of(17, 0));
    }

    public ClinicService(int id, String clinicName, String serviceName, int dailyQuota, int slotCapacity, LocalTime openingTime, LocalTime closingTime) {
        this.id = id;
        this.clinicName = clinicName;
        this.serviceName = serviceName;
        this.dailyQuota = dailyQuota;
        this.slotCapacity = slotCapacity;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public int getId() {
        return id;
    }

    public String getClinicName() {
        return clinicName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public int getDailyQuota() {
        return dailyQuota;
    }

    public int getSlotCapacity() {
        return slotCapacity;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public LocalTime getClosingTime() {
        return closingTime;
    }
}


