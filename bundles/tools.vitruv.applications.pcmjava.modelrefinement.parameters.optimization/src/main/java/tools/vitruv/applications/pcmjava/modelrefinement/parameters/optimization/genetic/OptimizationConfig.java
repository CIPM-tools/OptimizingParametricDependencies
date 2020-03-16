package tools.vitruv.applications.pcmjava.modelrefinement.parameters.optimization.genetic;

/**
 * Class for specifying the configuration of an optimization.
 * 
 * @author SonyaV
 *
 */
public class OptimizationConfig {	
	/**
	 * The evolution stream will be limited by this number of generations.
	 */
	public int generations;
	
	/**
	 * Time limit of the evolution in minutes.
	 */
	public int timeLimit;
	/**
	 * Max number of nodes of the generated mathematical expression.
	 */
	public int complexity;
	
	/**
	 * Defines which mathematical operations are allowed in the 
	 * optimization
	 */
	public OptimizationMode optimizationMode;
	
	/**
	 * Maximal depth (of the trees) of newly created expressions
	 */
	public int exprDepth;
	
	/**
	 * Limit the evolution stream by the condition: 
	 * if the best fitness of the current population 
	 * becomes less than the specified threshold and the objective is 
	 * set to minimize the fitness
	 */
	public double fitnessThreshold;

	public boolean integerOnly;
	/**
	 * Public constructor
	 * @param gen
	 * @param time
	 * @param compl
	 * @param mode
	 * @param depth
	 * @param threshold
	 */
	public OptimizationConfig(int gen, int time, int compl, OptimizationMode mode, int depth, double threshold, boolean integerOnly) {
		this.generations = gen;
		this.timeLimit = time;
		this.complexity = compl;
		this.optimizationMode = mode;
		this.exprDepth = depth;
		this.fitnessThreshold = threshold;
		this.integerOnly = integerOnly;
	
}
}
