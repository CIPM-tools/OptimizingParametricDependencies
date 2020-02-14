package tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl;

import java.util.StringJoiner;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCall;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.WekaDataSet;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;

public class WekaLoopModel implements LoopModel {

    private final LinearRegression classifier;

    private final WekaDataSet<Long> dataset;

    public WekaLoopModel(final WekaDataSet<Long> dataset)
            throws Exception {
        this.dataset = dataset;

        this.classifier = new LinearRegression();
        this.classifier.buildClassifier(this.dataset.getDataSet());
    }

    public LinearRegression getClassifier() {
        return classifier;
    }

    public Instances getDataSet() {
        return dataset.getDataSet();
    }

    @Override
    public double predictIterations(final ServiceCall serviceCall) {
        Instance parametersInstance = this.dataset.buildTestInstance(serviceCall.getParameters());
        try {
            return this.classifier.classifyInstance(parametersInstance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getIterationsStochasticExpression() {
        StringJoiner result = new StringJoiner(" + (");
        double[] coefficients = this.classifier.coefficients();
        int braces = 0;
        for (int i = 0; i < coefficients.length - 2; i++) {
            double coefficient = Utils.round(coefficients[i],3);
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

    public double getError() {
		Evaluation evaluation = null;
		try {
			evaluation = new Evaluation(this.dataset.getDataSet());
			evaluation.evaluateModel(this.classifier, this.dataset.getDataSet());
			return Utils.round(evaluation.relativeAbsoluteError(),3);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
}