package tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.dataset;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;

public class MockServiceCallDataSet extends ServiceCallDataSetImpl {

    public String add(Map<String, Object> serviceArguments) {
        return this.add(serviceArguments, Common.DEFAULT_SERVICE_ID, Common.TIME_NOT_SET, Common.TIME_NOT_SET,
                Common.FIELD_NOT_SET, Common.FIELD_NOT_SET);
    }

    public String add(Map<String, Object> serviceArguments, String serviceId, long entryTime, long exitTime,
            String callerId, String callerExecutionId) {
        String executionId = UUID.randomUUID().toString();
        Map<String,String> serviceArgumentsTypes = new HashMap<>();
        for(Entry<String, Object> arg : serviceArguments.entrySet()) {
        serviceArgumentsTypes.put(arg.getKey(), arg.getValue().getClass().getName());
        }
        ServiceCallImpl serviceCall = new ServiceCallImpl(Optional.ofNullable(callerId), callerExecutionId, entryTime,
                exitTime, ServiceParameters.build(serviceArguments), serviceId, executionId);
        super.add(serviceCall);
        return executionId;
    }
}
