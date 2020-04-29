package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import weka.core.Instances;

public interface ParameterModel {

	public Instances getInstancesDataSet();
	public WekaDataSet<?> getWekaDataSet();
	public String getStochasticExpression();
}
