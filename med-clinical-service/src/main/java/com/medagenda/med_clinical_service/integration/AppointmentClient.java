package com.medagenda.med_clinical_service.integration;

import com.medagenda.med_clinical_service.dtos.DailyAgendaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class AppointmentClient {

    private final RestClient restClient;

    public AppointmentClient(
            RestClient.Builder restClientBuilder,
            @Value("${appointment.service.url:http://localhost:8081}") String appointmentServiceUrl) {

        this.restClient = restClientBuilder
                .baseUrl(appointmentServiceUrl)
                .build();
    }

    public List<DailyAgendaResponse> getDailyAgenda(Long doctorId) {
        return restClient.get()
                .uri("/api/v1/appointments/calendar/today")
                .header("X-User-Id", String.valueOf(doctorId))
                .retrieve()
                .body(new ParameterizedTypeReference<List<DailyAgendaResponse>>() {});
    }

    public void updateAppointmentStatus(Long appointmentId, String status) {
        restClient.patch()
                .uri("/api/v1/appointments/{id}/status?status={status}", appointmentId, status)
                .retrieve()
                .toBodilessEntity();
    }
}