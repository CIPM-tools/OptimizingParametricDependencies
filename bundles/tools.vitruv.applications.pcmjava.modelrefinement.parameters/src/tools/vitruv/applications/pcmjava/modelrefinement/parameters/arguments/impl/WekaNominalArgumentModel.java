package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.ArgumentModel;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.branch.impl.WekaBranchModel.StochasticExpressionJ48;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Class for the argument model of nominal service parameter
 * 
 * @author SonyaV
 *
 */
public class WekaNominalArgumentModel implements ArgumentModel {

	private final StochasticExpressionJ48 j48Tree;
	private final WekaDataSet<?> dataset;
	private final AttributeSelection filter;
	private final ClassifierSubsetEval evaluator;
	private final BestFirst search;
	private final Instances filteredData;

	public WekaNominalArgumentModel(final WekaDataSet<?> dataset) throws Exception {
		this.dataset = dataset;
		this.j48Tree = new StochasticExpressionJ48();

		// System.out.println(this.dataset.getDataSet().toString());

		// feature selection
		this.filter = new AttributeSelection();
		this.evaluator = new ClassifierSubsetEval();
		evaluator.setClassifier(j48Tree);
		this.search = new BestFirst();
		search.setDirection(new SelectedTag(2, this.search.TAGS_SELECTION));
		filter.setEvaluator(evaluator);
		filter.setSearch(search);
		filter.setInputFormat(this.dataset.getDataSet());
		filteredData = Filter.useFilter(this.dataset.getDataSet(), filter);

		this.j48Tree.buildClassifier(filteredData);
		//System.out.println(filteredData.toString());
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
			evaluation.evaluateModel(this.j48Tree, filteredData);
			return Utils.round(evaluation.relativeAbsoluteError(), 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public WekaDataSet<?> getDataSet() {
		return this.dataset;
	}

	@Override
	public String getStochasticExpression() {
		 return Utils.getStoExTree(this.j48Tree);
//		String[] attrExpr = new String[this.filteredData.numAttributes()];
//		for (int i = 0; i < this.filteredData.numAttributes(); i++) {
//			Attribute attr = this.filteredData.attribute(i);
//			attrExpr[i] = attr.name();
//		}
//		return j48Tree.getBranchStochasticExpression(0, attrExpr);
	}
}
