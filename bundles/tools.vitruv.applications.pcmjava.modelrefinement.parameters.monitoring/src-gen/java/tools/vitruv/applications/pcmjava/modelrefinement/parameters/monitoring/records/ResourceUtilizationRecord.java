package tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.records.RecordWithSession;

/**
 * @author Generic Kieker
 * API compatibility: Kieker 1.10.0
 * 
 * @since 1.10
 */
public class ResourceUtilizationRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, RecordWithSession {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // RecordWithSession.sessionId
			 + TYPE_SIZE_STRING // ResourceUtilizationRecord.resourceId
			 + TYPE_SIZE_DOUBLE // ResourceUtilizationRecord.utilization
			 + TYPE_SIZE_LONG; // ResourceUtilizationRecord.timestamp
	
	public static final Class<?>[] TYPES = {
		String.class, // RecordWithSession.sessionId
		String.class, // ResourceUtilizationRecord.resourceId
		double.class, // ResourceUtilizationRecord.utilization
		long.class, // ResourceUtilizationRecord.timestamp
	};
	
	/** default constants. */
	public static final String SESSION_ID = "<not set>";
	public static final String RESOURCE_ID = "<not set>";
	private static final long serialVersionUID = 937906745133014588L;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"sessionId",
		"resourceId",
		"utilization",
		"timestamp",
	};
	
	/** property declarations. */
	private final String sessionId;
	private final String resourceId;
	private final double utilization;
	private final long timestamp;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param sessionId
	 *            sessionId
	 * @param resourceId
	 *            resourceId
	 * @param utilization
	 *            utilization
	 * @param timestamp
	 *            timestamp
	 */
	public ResourceUtilizationRecord(final String sessionId, final String resourceId, final double utilization, final long timestamp) {
		this.sessionId = sessionId == null?SESSION_ID:sessionId;
		this.resourceId = resourceId == null?RESOURCE_ID:resourceId;
		this.utilization = utilization;
		this.timestamp = timestamp;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	public ResourceUtilizationRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.sessionId = (String) values[0];
		this.resourceId = (String) values[1];
		this.utilization = (Double) values[2];
		this.timestamp = (Long) values[3];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated to be removed 1.15
	 */
	@Deprecated
	protected ResourceUtilizationRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.sessionId = (String) values[0];
		this.resourceId = (String) values[1];
		this.utilization = (Double) values[2];
		this.timestamp = (Long) values[3];
	}

	/**
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 *
	 * @deprecated to be removed in 1.15
	 */
	@Deprecated
	public ResourceUtilizationRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.sessionId = stringRegistry.get(buffer.getInt());
		this.resourceId = stringRegistry.get(buffer.getInt());
		this.utilization = buffer.getDouble();
		this.timestamp = buffer.getLong();
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated to be removed in 1.15
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getSessionId(),
			this.getResourceId(),
			this.getUtilization(),
			this.getTimestamp(),
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getSessionId());
		stringRegistry.get(this.getResourceId());
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13 to be removed in 1.15
	 */
	@Override
	@Deprecated
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getSessionId()));
		buffer.putInt(stringRegistry.get(this.getResourceId()));
		buffer.putDouble(this.getUtilization());
		buffer.putLong(this.getTimestamp());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
		return PROPERTY_NAMES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated to be rmeoved in 1.15
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated to be rmeoved in 1.15
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		
		final ResourceUtilizationRecord castedRecord = (ResourceUtilizationRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getSessionId().equals(castedRecord.getSessionId())) {
			return false;
		}
		if (!this.getResourceId().equals(castedRecord.getResourceId())) {
			return false;
		}
		if (isNotEqual(this.getUtilization(), castedRecord.getUtilization())) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		
		return true;
	}
	
	public final String getSessionId() {
		return this.sessionId;
	}
	
	
	public final String getResourceId() {
		return this.resourceId;
	}
	
	
	public final double getUtilization() {
		return this.utilization;
	}
	
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
}
