package evaluation.utils;

import java.io.File;

import org.pcm.headless.api.client.PCMHeadlessClient;
import org.pcm.headless.api.util.PCMUtil;

public class ParameterizedEvaluation {
    private static File validationBaseFolder = new File("test-data/casestudy/results-data-2/kieker");

    private static File nonOptimizedBaseFolder = new File(
            "test-data/casestudy/non-optimized");
    private static File optimizedBaseFolder = new File(
            "test-data/casestudy/optimized");
    private static File resultsBaseFolder = new File("test-data/casestudy/results-sonya/raw");

    public static void main(String[] argv) {
        PCMUtil.loadPCMModels();

        execute20UserParameterized();
        execute20UserNonParameterized();

        //execute40UserParameterized();
        //execute40UserNonParameterized();
    }

//    private static void execute40UserNonParameterized() {
//        executeExperiment(nonOptimizedBaseFolder, validationBaseFolder, resultsBaseFolder, "teastore40user.usagemodel",
//                "non_parameterized_40user.json");
//    }

    private static void execute20UserNonParameterized() {
        executeExperiment(nonOptimizedBaseFolder, validationBaseFolder, resultsBaseFolder, "casestudy20user.usagemodel",
                "non_optimized_20user.json");
    }

//    private static void execute40UserParameterized() {
//        File validationFolder = new File(validationBaseFolder, "40user/");
//        executeExperiment(optimizedBaseFolder, validationFolder, resultsBaseFolder, "teastore40user.usagemodel",
//                "parameterized_40user.json");
//    }

    private static void execute20UserParameterized() {
        File validationFolder = new File(validationBaseFolder, "20user/");
        executeExperiment(optimizedBaseFolder, validationFolder, resultsBaseFolder, "casestudy20user.usagemodel",
                "optimized_20user.json");
    }

    private static void executeExperiment(File pcmBaseFolder, File validationFolder, File resultsFolder,
            String usageProfile, String resultName) {
        File repo = new File(pcmBaseFolder, "casestudy.repository");
        File system = new File(pcmBaseFolder, "casestudy.system");
        File usage = new File(pcmBaseFolder, usageProfile);
        File resenv = new File(pcmBaseFolder, "casestudy.resourceenvironment");
        File alloc = new File(pcmBaseFolder, "casestudy.allocation");

        File resultFile = new File(resultsFolder, resultName);

        EvaluationData data = new EvaluationData();
        data.setRepository(repo);
        data.setSysten(system);
        data.setResourceenv(resenv);
        data.setAllocation(alloc);
        data.setUsagemodel(usage);
        data.setValidationFolder(validationFolder);
        data.setTargetService("_fgN6Z2BTEem3FetPjQjq2g");
        data.setOutputJsonFile(resultFile);

        PCMHeadlessClient client = new PCMHeadlessClient(EvaluationConfig.SIMULATION_REST_URL);
        EvaluationAutomizer automizer = new EvaluationAutomizer(client, data);

        automizer.execute(25, false, EvaluationConfig.defaultConfig);
    }
}
