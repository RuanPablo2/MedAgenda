package com.medagenda.med_clinical_service.entities;

import com.medagenda.med_clinical_service.dtos.CertificateDTO;
import com.medagenda.med_clinical_service.dtos.PrescriptionDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_clinical_history")
public class ClinicalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long appointmentId;

    @Column(nullable = false)
    private Long patientId;

    @Column(nullable = false)
    private Long doctorId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String symptoms;

    @Column(columnDefinition = "TEXT")
    private String diagnosis;

    @Column(columnDefinition = "TEXT")
    private String internalNotes;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<PrescriptionDTO> prescriptions;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<CertificateDTO> certificates;

    public ClinicalHistory() {
    }

    public ClinicalHistory(Long appointmentId, Long patientId, Long doctorId, LocalDateTime createdAt,
                           String symptoms, String diagnosis, String internalNotes,
                           List<PrescriptionDTO> prescriptions, List<CertificateDTO> certificates) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.createdAt = createdAt;
        this.symptoms = symptoms;
        this.diagnosis = diagnosis;
        this.internalNotes = internalNotes;
        this.prescriptions = prescriptions;
        this.certificates = certificates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getInternalNotes() {
        return internalNotes;
    }

    public void setInternalNotes(String internalNotes) {
        this.internalNotes = internalNotes;
    }

    public List<PrescriptionDTO> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<PrescriptionDTO> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<CertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateDTO> certificates) {
        this.certificates = certificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClinicalHistory that = (ClinicalHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}