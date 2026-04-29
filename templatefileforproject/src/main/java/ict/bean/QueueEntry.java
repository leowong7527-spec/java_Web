package ict.bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class QueueEntry {
    private final int id;
    private final int patientId;
    private final int serviceId;
    private final LocalDate queueDate;
    private final int queueNumber;
    private String status;
    private final LocalDateTime joinedAt;

    public QueueEntry(int id, int patientId, int serviceId, LocalDate queueDate, int queueNumber, String status) {
        this.id = id;
        this.patientId = patientId;
        this.serviceId = serviceId;
        this.queueDate = queueDate;
        this.queueNumber = queueNumber;
        this.status = status;
        this.joinedAt = LocalDateTime.now();
    }

    public QueueEntry(int id, int patientId, int serviceId, LocalDate queueDate, int queueNumber, String status,
                      LocalDateTime joinedAt) {
        this.id = id;
        this.patientId = patientId;
        this.serviceId = serviceId;
        this.queueDate = queueDate;
        this.queueNumber = queueNumber;
        this.status = status;
        this.joinedAt = joinedAt;
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

    public LocalDate getQueueDate() {
        return queueDate;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
}


