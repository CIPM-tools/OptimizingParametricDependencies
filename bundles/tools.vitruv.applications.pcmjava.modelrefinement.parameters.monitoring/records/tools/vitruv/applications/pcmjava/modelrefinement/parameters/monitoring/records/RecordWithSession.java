package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public interface RecordWithSession extends IMonitoringRecord {
	public String getSessionId();
	
}
