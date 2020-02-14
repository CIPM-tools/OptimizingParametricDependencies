package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSetBuilder;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSetMode;

public class WekaArgumentsModelEstimation {
	private final ServiceCallDataSet serviceCalls;

	public WekaArgumentsModelEstimation(final ServiceCallDataSet serviceCalls) {
		this.serviceCalls = serviceCalls;
	}

	
	/**
	 * Create estimation model for service with id serviceId
	 * @param serviceId
	 * @return
	 */
	public Map<String, ArgumentModel> estimate(final String serviceId) {
		try {
			return this.internEstimate(serviceId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Estimate the parameters of all external calls 
	 * @return Map <serviceId,<parameterName, ArgumentModel>>
	 */
	public Map<String, Map<String, ArgumentModel>> estimateAll() {
		HashMap<String, Map<String, ArgumentModel>> returnValue = new HashMap<String, Map<String, ArgumentModel>>();
		for (String serviceId : this.serviceCalls.getExternalServiceCallsIds()) {
			returnValue.put(serviceId, this.estimate(serviceId));
		}
		return returnValue;
	}

	private Map<String, ArgumentModel> internEstimate(final String serviceId) throws Exception {
		List<ServiceCall> records = this.serviceCalls.getServiceCalls(serviceId);
		Map<String, ArgumentModel> models = new HashMap<String, ArgumentModel>(); // parameter names to models
		Map<String, WekaDataSetBuilder> parameterNameToDataSetBuilder = new HashMap<String, WekaDataSetBuilder>();

		if (records.size() == 0) {
			throw new IllegalStateException("No records for service id " + serviceId + " found.");
		}

		for (ServiceCall record : records) {
			for (Entry<String, Object> calleeParameter : record.getParameters().getParameters().entrySet()) {

				WekaDataSetBuilder builder = null;
				if (!parameterNameToDataSetBuilder.containsKey(calleeParameter.getKey())) {
					builder = whichBuilder(calleeParameter.getValue(), this.serviceCalls);
					parameterNameToDataSetBuilder.put(calleeParameter.getKey(), builder);
				} else {
					builder = parameterNameToDataSetBuilder.get(calleeParameter.getKey());
				}

				builder.addInstance(record.getCallerServiceExecutionId(), calleeParameter.getValue());

			}
		}

		parameterNameToDataSetBuilder.forEach((k, v) -> {
			try {
				if (v.getMode() == WekaDataSetMode.NumericOnly || v.getMode() == WekaDataSetMode.IntegerOnly) {
					models.put(k, new WekaNumericArgumentModel(v.build()));
				} else {
					models.put(k, new WekaNominalArgumentModel(v.build()));
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return models;
	}

	private WekaDataSetBuilder whichBuilder(Object parameter, ServiceCallDataSet serviceCalls) {
		if (parameter instanceof Double) {
			return new WekaDataSetBuilder<Double>(serviceCalls, WekaDataSetMode.NumericOnly);
		} else if (parameter instanceof String) {
			return new WekaDataSetBuilder<String>(serviceCalls, WekaDataSetMode.NoTransformations);
		} else if (parameter instanceof Integer) {
			return new WekaDataSetBuilder<Integer>(serviceCalls, WekaDataSetMode.IntegerOnly);
		} else if (parameter instanceof Long) {
			return new WekaDataSetBuilder<Long>(serviceCalls, WekaDataSetMode.NumericOnly);
		} else if (parameter instanceof Float) {
			return new WekaDataSetBuilder<Float>(serviceCalls, WekaDataSetMode.NumericOnly);
		} else if (parameter instanceof Boolean) {
			return new WekaDataSetBuilder<String>(serviceCalls, WekaDataSetMode.NoTransformations);
		} else if (parameter instanceof Iterable) { // not sure for this one yet
			return new WekaDataSetBuilder<Integer>(serviceCalls, WekaDataSetMode.NumericOnly);
		} 
		return null;
	}
}
