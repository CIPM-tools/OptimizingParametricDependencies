package tools.vitruv.applications.pcmjava.modelrefinement.parameters.optimization.genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import io.jenetics.Genotype;
import io.jenetics.Mutator;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.EvolutionStart;
import io.jenetics.engine.Limits;
import io.jenetics.ext.SingleNodeCrossover;
import io.jenetics.ext.util.TreeFormatter;
import io.jenetics.ext.util.TreeNode;
import io.jenetics.prog.ProgramChromosome;
import io.jenetics.prog.ProgramGene;
import io.jenetics.prog.op.EphemeralConst;
import io.jenetics.prog.op.MathExpr;
import io.jenetics.prog.op.MathOp;
import io.jenetics.prog.op.Op;
import io.jenetics.prog.op.Var;
import io.jenetics.prog.regression.Error;
import io.jenetics.prog.regression.LossFunction;
import io.jenetics.prog.regression.Regression;
import io.jenetics.prog.regression.Sample;
import io.jenetics.util.ISeq;
import io.jenetics.util.RandomRegistry;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl.ServiceParameterToOptimize;

/**
 * Optimization entry point class
 * 
 * @author SonyaV
 *
 */
public class Optimization {
	private ServiceParameterToOptimize parameter;
	private OptimizationMode mode;
	private ProgramGene<Double> expression;
	private EvolutionResult<ProgramGene<Double>, Double> result;
	private Regression<Double> regression;
	private TreeNode<Op<Double>> tree;
	private int generations;

	/**
	 * Constructor for the optimization class
	 * 
	 * @param param       subject of the optimization
	 * @param mode        optimization mode
	 * @param generations number of evolutions/generations
	 */
	public Optimization(ServiceParameterToOptimize param, OptimizationMode mode, int generations) {
		this.parameter = param;
		this.mode = mode;
		this.regression = Regression.of(Regression.codecOf(getOperations(), getTerminals(), 5),
				Error.of(LossFunction::mse), getSample());
		this.generations = generations;
	}

	/**
	 * Get the allowed operations according to the optimization mode
	 * 
	 * @return sequence of operations
	 */
	private ISeq<Op<Double>> getOperations() {
		ISeq<Op<Double>> operations = ISeq.of(MathOp.ADD, MathOp.MUL, MathOp.POW, MathOp.SUB, MathOp.DIV);
		if (this.mode == OptimizationMode.LogExp) {
			operations = operations.append(MathOp.LOG, MathOp.LOG10, MathOp.EXP, MathOp.SQRT);
		} else if (this.mode == OptimizationMode.Trignometric) {
			operations.append(MathOp.SIN, MathOp.COS, MathOp.TAN);
		}
		return operations;
	}

	/**
	 * Get the terminal symbols (variables) for this optimization according to the
	 * database of the service parameter
	 * 
	 * @return sequence of terminals
	 */
	private ISeq<Op<Double>> getTerminals() {
		List<Op<Double>> variables = new ArrayList<Op<Double>>();
		
		// add the names of all numeric variables	
		Map<String, List<Object>> attributes = parameter.getNumericAttr();
		//in reverse order; without the class attribute
		int counter = attributes.keySet().size()-2;
		for(Entry<String, List<Object>> attr : attributes.entrySet()) {
			if(!attr.getKey().equals("class")) {
				variables.add(Var.of(attr.getKey(), counter));
				counter--;
			}
		}
		ISeq<Op<Double>> terminals = ISeq.of(variables)
				.append(ISeq.of(EphemeralConst.of(() -> (double) RandomRegistry.getRandom().nextInt(10))));
		return terminals;
	}

	/**
	 * Get the data set values for the service parameter
	 * 
	 * @return collection of samples
	 */
	private Iterable<Sample<Double>> getSample() {
		List<Sample<Double>> samples = new ArrayList<Sample<Double>>();
		List<String> instances = this.parameter.getModel().getDataSet().getInstances(true);
		for (int i = 0; i < instances.size(); i++) {
			String[] splitInstance = instances.get(i).split(",");
			Double[] doubles = new Double[splitInstance.length];

			// transform the instance values to double values
			for (int j = 0; j < splitInstance.length; j++) {
				doubles[j] = Double.valueOf(splitInstance[j]);
			}
			samples.add(Sample.of(doubles));
		}
		return samples;
	}

	/**
	 * Create individual for the genetic programming from StoEx String
	 * @param stoEx initial stochastic expression
	 * @return EvolutionStart object - first individual of the optimization
	 */
	private EvolutionStart<ProgramGene<Double>, Double> getInitialIndividual(String stoEx) {
		MathExpr expr = MathExpr.parse(stoEx);

		ProgramChromosome<Double> ch = ProgramChromosome.of(expr.toTree(), getOperations(), getTerminals());
		Genotype<ProgramGene<Double>> ge = Genotype.of(ch);
		Phenotype<ProgramGene<Double>, Double> ph = Phenotype.of(ge, 1);
		return EvolutionStart.of(ISeq.of(ph), 1);
	}

	
	/**
	 * Start optimization
	 */
	public void start() {
		Engine<ProgramGene<Double>, Double> engine = Engine.builder(regression)
				.minimizing()
				.alterers(new SingleNodeCrossover<>(0.15), new Mutator<>(0.15))
				.build();

		result = engine.stream(getInitialIndividual(this.parameter.getModel().getArgumentStochasticExpression()))
				.limit(Limits.byFitnessThreshold(0.01))
				.limit(generations)
				.collect(EvolutionResult.toBestEvolutionResult());

		expression = result.getBestPhenotype().getGenotype().getGene();

		// Simplify result program
		this.tree = Utils.shorten(expression.toTreeNode());
		MathExpr.rewrite(this.tree);
		//test();
	}

	private void test() {
		MathExpr e = MathExpr.parse("(y_VALUE*x_VALUE)*neg(neg(x_VALUE))");
		System.out.println(e.eval(2.0, 3.0));
	}
	/** 
	 * Get mathematical expression from tree
	 * @return mathematical expression
	 */
	public MathExpr getMathExpr() {
		return new MathExpr(this.tree);
	}

	
	/**
	 * Print statistics for the optimization
	 */
	public void printStats() {
		System.out.println("Generations: " + result.getTotalGenerations());
		System.out.println("Error:       " + regression.error(this.tree));
	}

	/**
	 * @return double value of error
	 */
	public double getError() {
		return this.regression.error(this.tree);
	}

	/**
	 * Print formatted tree
	 */
	public void printTree() {
		TreeFormatter formatter = TreeFormatter.TREE;
		System.out.println(formatter.format(this.tree));
	}

	/**
	 * Get the optimized StoEx
	 * @return optimized stochastic expression string
	 */
	public String getOptimizedStochasticExpression() {
		return new MathExpr(this.tree).toString();
	}
}
