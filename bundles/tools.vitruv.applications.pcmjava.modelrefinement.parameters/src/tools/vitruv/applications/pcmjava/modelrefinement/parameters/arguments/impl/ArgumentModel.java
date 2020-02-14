package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;

/**
 * Interface for the arguments estimation model (can be numeric or nominal)
 * @author SonyaV
 *
 */
public interface ArgumentModel {

    String getArgumentStochasticExpression();

    WekaDataSet getDataSet();
    
    double getError();
}