package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.ArgumentModel;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

/**
 * Class for the argument model of numeric service parameter
 * 
 * @author SonyaV
 *
 */
public class WekaNumericArgumentModel implements ArgumentModel {

	private final LinearRegression linRegClassifier;
	private final WekaDataSet<?> dataset;
	private final AttributeSelection filter;
	private final ClassifierSubsetEval evaluator;
	private final BestFirst search;
	private final Instances filteredData;
	private final boolean integerOnly;

	public WekaNumericArgumentModel(final WekaDataSet<?> dataset, boolean intOnly) throws Exception {
		this.dataset = dataset;
		this.integerOnly = intOnly;
		this.linRegClassifier = new LinearRegression();

		// feature selection
		this.filter = new AttributeSelection();
		this.evaluator = new ClassifierSubsetEval();
		evaluator.setClassifier(linRegClassifier);
		this.search = new BestFirst();
		search.setDirection(new SelectedTag(2, this.search.TAGS_SELECTION));
		filter.setEvaluator(evaluator);
		filter.setSearch(search);
		filter.setInputFormat(this.dataset.getDataSet());
		filteredData = Filter.useFilter(this.dataset.getDataSet(), filter);
		this.linRegClassifier.buildClassifier(filteredData);
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

	public WekaDataSet<?> getDataSet() {
		return this.dataset;
	}

	@Override
	public String getStochasticExpression() {
		String stoEx = Utils.getStoExLinReg(this.linRegClassifier, this.filteredData);
		if(integerOnly) return Utils.replaceDoubles(stoEx);
		else return stoEx;
	}

}
