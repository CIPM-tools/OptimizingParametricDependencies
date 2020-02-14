package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import java.util.StringJoiner;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instances;

/**
 * Class for the argument model of numeric service parameter
 * 
 * @author SonyaV
 *
 */
public class WekaNumericArgumentModel implements ArgumentModel {

	private final LinearRegression linRegClassifier;
	private final WekaDataSet<Long> dataset;
	private final AttributeSelection filter;
	private final ClassifierSubsetEval evaluator;
	private final GreedyStepwise search;
	private final Instances filteredData;

	public WekaNumericArgumentModel(final WekaDataSet<Long> dataset) throws Exception {
		this.dataset = dataset;
		this.linRegClassifier = new LinearRegression();

		// System.out.println(this.dataset.getDataSet().toString());

		// feature selection
		this.filter = new AttributeSelection();
		this.evaluator = new ClassifierSubsetEval();
		evaluator.setClassifier(linRegClassifier);
		this.search = new GreedyStepwise();
		search.setSearchBackwards(true);
		filter.setEvaluator(evaluator);
		filter.setSearch(search);
		filter.setInputFormat(this.dataset.getDataSet());
		filteredData = Filter.useFilter(this.dataset.getDataSet(), filter);

		this.linRegClassifier.buildClassifier(filteredData);
	}

	/**
	 * "Translates" the classifier output into stochastic expression
	 *
	 * @return argument stochastic expression string
	 */
	@Override
	public String getArgumentStochasticExpression() {
		StringJoiner result = new StringJoiner(" + (");
		double[] coefficients = this.linRegClassifier.coefficients();
		int braces = 0;
		for (int i = 0; i < coefficients.length - 2; i++) {
			double coefficient = Utils.round(coefficients[i], 3);
			if (coefficient == 0) {
				continue;
			}
			StringBuilder coefficientPart = new StringBuilder();
			String paramStoEx = this.dataset.getStochasticExpressionForIndex(i);

			coefficientPart.append(coefficient).append(" * ").append(paramStoEx);
			result.add(coefficientPart.toString());
			braces++;
		}
		result.add(String.valueOf(Utils.round(coefficients[coefficients.length - 1], 3)));
		StringBuilder strBuilder = new StringBuilder().append(result.toString());
		for (int i = 0; i < braces; i++) {
			strBuilder.append(")");
		}
		return strBuilder.toString();
	}

	/**
	 * Gets the error of the classifier
	 * 
	 * @return relative absolute error(in %)
	 */
	@Override
	public double getError() {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(filteredData);
			evaluation.evaluateModel(this.linRegClassifier, filteredData);
			return Utils.round(evaluation.relativeAbsoluteError(), 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public LinearRegression getClassifier() {
		return linRegClassifier;
	}

	public WekaDataSet getDataSet() {
		return this.dataset;
	}

}
