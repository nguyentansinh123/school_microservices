package com.caffein.analyticservice.kafka.consumer;

import com.caffein.analyticservice.kafka.AttendanceRecordedEvent;
import com.caffein.analyticservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "attendance-events", groupId = "analytics-group")
    public void handleAttendanceEvent(AttendanceRecordedEvent event) {
        log.info("Received attendance event: {}", event);
        try {
            analyticsService.processAttendanceEvent(event);
        } catch (Exception e) {
            log.error("Error processing attendance event: {}", e.getMessage(), e);
        }
    }
}