package com.caffein.analyticservice.kafka.consumer;

import com.caffein.analyticservice.kafka.EnrollmentEvent;
import com.caffein.analyticservice.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentEventConsumer {

    private final AnalyticsService analyticsService;

    @KafkaListener(topics = "enrollment-events", groupId = "analytics-group")
    public void handleEnrollmentEvent(EnrollmentEvent event) {
        log.info("Received enrollment event: {}", event);
        try {
            analyticsService.processEnrollmentEvent(event);
        } catch (Exception e) {
            log.error("Error processing enrollment event: {}", e.getMessage(), e);
        }
    }
}