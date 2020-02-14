package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.ServiceCallRecord;

public class KiekerMonitoringRecordsWriter implements MonitoringRecordsWriter {

    private final IMonitoringController monitoringController;

    public KiekerMonitoringRecordsWriter(IMonitoringController monitoringController) {
        this.monitoringController = monitoringController;
    }

    @Override
    public synchronized void write(IMonitoringRecord monitoringRecord) {
        this.monitoringController.newMonitoringRecord(monitoringRecord);
    }
}
