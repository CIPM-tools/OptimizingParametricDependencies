package tools.vitruv.applications.pcmjava.modelrefinement.parameters;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;

public interface ParameterModel {

	public WekaDataSet<?> getDataSet();
	
	public String getStochasticExpression();
}
