package ict.bean;

import java.time.LocalDateTime;

public class Appointment {
    private final int id;
    private final int patientId;
    private final int serviceId;
    private LocalDateTime slotTime;
    private String status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Appointment(int id, int patientId, int serviceId, LocalDateTime slotTime, String status) {
        this.id = id;
        this.patientId = patientId;
        this.serviceId = serviceId;
        this.slotTime = slotTime;
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    public Appointment(int id, int patientId, int serviceId, LocalDateTime slotTime, String status,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.patientId = patientId;
        this.serviceId = serviceId;
        this.slotTime = slotTime;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public int getPatientId() {
        return patientId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public LocalDateTime getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(LocalDateTime slotTime) {
        this.slotTime = slotTime;
        this.updatedAt = LocalDateTime.now();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}


