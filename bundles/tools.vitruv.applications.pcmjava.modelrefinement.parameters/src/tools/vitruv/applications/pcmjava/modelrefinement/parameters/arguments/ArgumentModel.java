package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ParameterModel;
import weka.core.Instances;

/**
 * Interface for the arguments estimation model (can be numeric or nominal)
 * @author SonyaV
 *
 */
public interface ArgumentModel extends ParameterModel {
  
    public double getError();
    
    public boolean isIntegerOnly();
}