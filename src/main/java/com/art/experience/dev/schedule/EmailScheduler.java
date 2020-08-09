package com.art.experience.dev.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@ConditionalOnProperty(name = "reports-reserves.scheduler.enabled", havingValue = "true")
public class EmailScheduler {

    private static final Logger LOGGER = LogManager.getLogger(EmailScheduler.class);
    //private final ReportScanService reportScanService;
    //private final CommercialReportService commercialReportService;

    @Autowired
    public EmailScheduler(/*final ReportScanService reportScanService,
                          final CommercialReportService commercialReportService*/) {
      //  this.reportScanService = reportScanService;
      //  this.commercialReportService = commercialReportService;
    }

    @Scheduled(cron = "${reports-reserves.scheduler.cron}")
    public void retrieveScansReports() {
        LOGGER.info("INITIALIZING RETRIEVE SCAN REPORTS " + Instant.now());
       // reportScanService.getReportsOfRunningScans();
    }

  /*  @Scheduled(cron = "${reports-reserves.scheduler.cron_monthly}")
    public void generateAndSendCommercialReport() {
        LOGGER.info("SENDING COMMERCIAL REPORT " + Instant.now());
        //commercialReportService.generateMonthlyReport();
    }*/
}