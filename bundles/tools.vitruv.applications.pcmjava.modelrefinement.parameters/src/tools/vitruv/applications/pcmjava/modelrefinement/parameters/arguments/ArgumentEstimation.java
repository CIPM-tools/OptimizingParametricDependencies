package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;

public interface ArgumentEstimation {

    public void update(Repository pcmModel, ServiceCallDataSet serviceCalls);
}