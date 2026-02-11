package com.awscapstone.crypto_tracker_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.cloudwatch.model.*;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudWatchService {

    private final CloudWatchClient cloudWatchClient;

    @Value("${spring.application.name}")
    private String applicationName;

    public void publishMetric(String metricName, double value, String unit) {
        try {
            MetricDatum datum = MetricDatum.builder()
                    .metricName(metricName)
                    .value(value)
                    .unit(StandardUnit.fromValue(unit))
                    .timestamp(Instant.now())
                    .build();

            Dimension dimension = Dimension.builder()
                    .name("Application")
                    .value(applicationName)
                    .build();

            PutMetricDataRequest request = PutMetricDataRequest.builder()
                    .namespace("CryptoTracker/Application")
                    .metricData(datum.toBuilder().dimensions(dimension).build())
                    .build();

            cloudWatchClient.putMetricData(request);
            log.debug("Published metric: {} = {}", metricName, value);
        } catch (Exception e) {
            log.error("Failed to publish metric {}: {}", metricName, e.getMessage());
        }
    }

    public void publishApiCallMetric(String apiName, long responseTime, boolean success) {
        publishMetric("ApiResponseTime", responseTime, "Milliseconds");
        publishMetric("ApiCallCount", 1, "Count");
        publishMetric("ApiSuccessRate", success ? 1 : 0, "Count");
    }

    public void publishDatabaseMetric(String operation, long responseTime, boolean success) {
        publishMetric("DatabaseResponseTime", responseTime, "Milliseconds");
        publishMetric("DatabaseOperationCount", 1, "Count");
        publishMetric("DatabaseSuccessRate", success ? 1 : 0, "Count");
    }

    public void createAlarm(String alarmName, String metricName, double threshold, ComparisonOperator operator) {
        try {
            PutMetricAlarmRequest request = PutMetricAlarmRequest.builder()
                    .alarmName(alarmName)
                    .comparisonOperator(operator)
                    .evaluationPeriods(2)
                    .metricName(metricName)
                    .namespace("CryptoTracker/Application")
                    .period(300)
                    .statistic(Statistic.AVERAGE)
                    .threshold(threshold)
                    .actionsEnabled(false)
                    .alarmDescription("Alarm for " + metricName)
                    .dimensions(Dimension.builder()
                            .name("Application")
                            .value(applicationName)
                            .build())
                    .unit(StandardUnit.COUNT)
                    .build();

            cloudWatchClient.putMetricAlarm(request);
            log.info("Created CloudWatch alarm: {}", alarmName);
        } catch (Exception e) {
            log.error("Failed to create alarm {}: {}", alarmName, e.getMessage());
        }
    }
}