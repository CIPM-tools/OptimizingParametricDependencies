package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EReference;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.parameter.ParameterFactory;
import org.palladiosimulator.pcm.parameter.VariableCharacterisation;
import org.palladiosimulator.pcm.parameter.VariableCharacterisationType;
import org.palladiosimulator.pcm.parameter.VariableUsage;
import org.palladiosimulator.pcm.parameter.impl.VariableUsageImpl;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ExternalCallAction;
import org.palladiosimulator.pcm.seff.SeffPackage;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.ArgumentEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.optimization.genetic.Optimization;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.optimization.genetic.OptimizationMode;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;

/**
 * Class for estimating parametric dependencies for external call arguments
 * and updating the PCM
 * @author SonyaV
 *
 */
public class ArgumentEstimationImpl implements ArgumentEstimation{

	private static final Logger LOGGER = Logger.getLogger(ArgumentEstimationImpl.class);

	// service ID to <parameter name, parameter model>
	private final Map<String, Map<String, ArgumentModel>> modelCache; 

	public ArgumentEstimationImpl() {
		this(ThreadLocalRandom.current());
	}

	public ArgumentEstimationImpl(final Random random) {
		this.modelCache = new HashMap<>();
	}

	/**
	 * creates argument models for every external call from serviceCalls
	 * @param pcmModel Palladio Component Model to update
	 * @param serviceCalls the external calls from this model
	 */
	@Override
	public void update(Repository pcmModel, ServiceCallDataSet serviceCalls) {
		WekaArgumentsModelEstimation estimation = new WekaArgumentsModelEstimation(serviceCalls);

		Map<String, Map<String, ArgumentModel>> argumentModels = estimation.estimateAll();

		for (ServiceCall record : serviceCalls.getServiceCalls())
			this.modelCache.put(record.getServiceId(), argumentModels.get(record.getServiceId()));
		this.applyEstimations(pcmModel);
	}

	/**
	 * applies the corresponding model for every external call from pcmModel
	 * @param pcmModel Palladio Component Model
	 */
	private void applyEstimations(final Repository pcmModel) {
		List<ExternalCallAction> externalCalls = PcmUtils.getObjects(pcmModel, ExternalCallAction.class);
		for (ExternalCallAction externalCall : externalCalls) {
			this.applyModel(externalCall);
		}
	}

	/**
	 * applies the corresponding model for the specific external call
	 * @param externalCall external call to apply the estimation model to
	 */
	private void applyModel(final ExternalCallAction externalCall) {
		//get the corresponding estimation model
		Map<String, ArgumentModel> parameterModels = this.modelCache
				.get(externalCall.getCalledService_ExternalService().getId());
		if (parameterModels == null) {
			LOGGER.warn("A estimation for the parameters of external call with id " + externalCall.getId()
					+ " was not found.");
			return;
		}
		for (Entry<String, ArgumentModel> model : parameterModels.entrySet()) {
			String parameterName = model.getKey();
			String stoEx = model.getValue().getArgumentStochasticExpression();

			System.out.println("Initial StoEx for parameter " + parameterName + ": " 
					+ Utils.replaceUnderscoreWithDot(stoEx));

			//for now only numeric models with error >= 10% are optimized
			if (model.getValue().getClass().getSimpleName().equals("WekaNumericArgumentModel")
					&& model.getValue().getError() >= 10) {
				optimize(model.getKey(), model.getValue());
				
			}
			else {		
		       VariableUsage varUsage = PcmUtils.createVariableUsage(parameterName, VariableCharacterisationType.VALUE, stoEx);
		       externalCall.getInputVariableUsages__CallAction().add(varUsage);
			}
		}
	}

	/**
	 * starts the genetic programming optimization algorithm
	 * @param parameterName
	 * @param model model as base of the optimization
	 */
	private void optimize(String parameterName, ArgumentModel model) {
		ServiceParameterToOptimize sp = new ServiceParameterToOptimize(parameterName, model);
		Optimization op = new Optimization(sp, OptimizationMode.LogExp, 10000);
		op.start();
		System.out.println("Optimized StoEx for parameter: " + parameterName + ": " + 
		Utils.replaceUnderscoreWithDot(op.getOptimizedStochasticExpression()));
		op.printTree();
		op.printStats();
	}
}
