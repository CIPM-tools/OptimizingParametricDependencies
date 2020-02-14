package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import weka.attributeSelection.ClassifierSubsetEval;
import weka.attributeSelection.GreedyStepwise;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Class for the argument model of nominal service parameter
 * 
 * @author SonyaV
 *
 */
public class WekaNominalArgumentModel implements ArgumentModel {

	private final J48 j48Tree;
	private final WekaDataSet<String> dataset;
	private final AttributeSelection filter;
	private final ClassifierSubsetEval evaluator;
	private final GreedyStepwise search;
	private final Instances filteredData;

	public WekaNominalArgumentModel(final WekaDataSet<String> dataset) throws Exception {
		this.dataset = dataset;
		this.j48Tree = new J48();

		// System.out.println(this.dataset.getDataSet().toString());

		// feature selection
		this.filter = new AttributeSelection();
		this.evaluator = new ClassifierSubsetEval();
		evaluator.setClassifier(j48Tree);
		this.search = new GreedyStepwise();
		search.setSearchBackwards(true);
		filter.setEvaluator(evaluator);
		filter.setSearch(search);
		filter.setInputFormat(this.dataset.getDataSet());
		filteredData = Filter.useFilter(this.dataset.getDataSet(), filter);

		this.j48Tree.buildClassifier(filteredData);
		// System.out.println(filteredData.toString());
	}

	/**
	 * "Translates" the J48 Tree into string
	 * 
	 * @return stochastic expression string
	 */
	private String stoExIntern() {
		String[] lines = this.j48Tree.toString().split("\n");
		if (lines.length == 6) { // constant value (6 is the min size ot the tree)
			return lines[3].replace(":", "").replaceAll("\\s+", "");
		}

		StringBuilder builder = new StringBuilder();
		int numPipes = 0;
		for (int currentLine = 3; !lines[currentLine].isEmpty(); currentLine++) {
			String line = "|" + lines[currentLine];
			
			if (!line.contains(":")) { // line contains condition (no assignment)
				if (countPipes(line) == numPipes) { // closing
					builder.append(":");
				} else { // opening
					builder.append("(IF(" + line.replaceAll("\\s+", "") + ")?");
					numPipes++;
				}

			} else {
				if (countPipes(line) == numPipes) { // closing
					String[] lineParts = line.split(":");
					builder.append(":" + lineParts[1].replaceAll("\\s+", "") + ")");
					numPipes--;
				} else { // opening
					String[] lineParts = lines[currentLine].split(":");
					builder.append(
							"(IF(" + lineParts[0].replaceAll("\\s+", "") + ")?" + lineParts[1].replaceAll("\\s+", ""));
					numPipes++;
				}
			}

		}
//		for (int i = 0; i < numPipes-1; i++) {
//			builder.append(")");
//		}
		return builder.toString().substring(1,builder.toString().length()-1); // remove the most outter parenthesis
	}

	private int countPipes(String str) {
		return (int) str.chars().filter(ch -> ch == '|').count();
	}

	/**
	 * Gets the "cleaned" stochastic expression
	 * 
	 * @return argument stochastic expression
	 */
	@Override
	public String getArgumentStochasticExpression() {
		String stoEx = stoExIntern();
		String cleanStoEx = stoEx.replaceAll("\\|", "");
		cleanStoEx = cleanStoEx.replaceAll("(?<!!)=", "==");
		cleanStoEx = cleanStoEx.replaceAll("IF", "");
		cleanStoEx = cleanStoEx.replaceAll("[(]\\d+\\.\\d+[)]", "");
		return cleanStoEx;
	}

	
	/**
	 * Gets the error of the classifier
	 *@return relative absolute error(in %)
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

	public WekaDataSet getDataSet() {
		return this.dataset;
	}
}
