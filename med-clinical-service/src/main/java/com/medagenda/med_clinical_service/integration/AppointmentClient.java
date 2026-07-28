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

    private record AppointmentStatusUpdateDTO(String status) {}

    public AppointmentClient(
            RestClient.Builder restClientBuilder,
            @Value("${appointment.service.url}") String appointmentServiceUrl) {

        this.restClient = restClientBuilder
                .baseUrl(appointmentServiceUrl)
                .build();
    }

    public List<DailyAgendaResponse> getDailyAgenda(Long doctorId) {
        return restClient.get()
                .uri("/api/v1/appointments/calendar/today")
                .header("X-User-Id", String.valueOf(doctorId))
                .header("X-User-Role", "ROLE_DOCTOR")
                .retrieve()
                .body(new ParameterizedTypeReference<List<DailyAgendaResponse>>() {});
    }

    public void updateAppointmentStatus(Long appointmentId, String status, Long doctorId) {

        AppointmentStatusUpdateDTO body = new AppointmentStatusUpdateDTO(status);

        restClient.patch()
                .uri("/api/v1/appointments/{id}/status", appointmentId)
                .header("X-User-Id", String.valueOf(doctorId))
                .header("X-User-Role", "ROLE_DOCTOR")
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}