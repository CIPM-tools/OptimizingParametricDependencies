package evaluation.casestudy2;

import java.io.File;

import org.pcm.headless.api.client.PCMHeadlessClient;
import org.pcm.headless.api.client.measure.MonitorRepositoryBuilderUtil;
import org.pcm.headless.api.util.PCMUtil;

import evaluation.utils.EvaluationAutomizer;
import evaluation.utils.EvaluationConfig;
import evaluation.utils.EvaluationData;

public class Casestudy2Evaluation {
    private static File validationBaseFolder = new File("test-data//casestudy2//kieker_coarsegrained//10user//");

    // only for result.repository
    private static File dfBaseFolder = new File(
            "test-data//casestudy2//df//");
    private static File nonOptimizedBaseFolder = new File(
            "test-data//casestudy2//nonOptimized//");
    private static File optimizedBaseFolder = new File(
            "test-data//casestudy2//optimized//");

    private static File resultsBaseFolder = new File("test-data//casestudy2//results//");

    public static void main(String[] argv) {
        PCMUtil.loadPCMModels();
       
        //executeDF();
        //executeNonOptimized();
        executeOptimized();

    }

    private static void executeDF() {
        executeExperiment(dfBaseFolder,
                "df.json");
    }

    private static void executeNonOptimized() {
        executeExperiment(nonOptimizedBaseFolder, 
                "non_optimized.json");
    }

    private static void executeOptimized() {
        executeExperiment(optimizedBaseFolder, 
                "optimized.json");
    }

    private static void executeExperiment(File repoBaseFolder, String resultName) {
        File repo = new File(repoBaseFolder, "result.repository");
        File system = new File(repoBaseFolder, "default.system");
        File usage = new File(repoBaseFolder, "default.usagemodel");
        File resenv = new File(repoBaseFolder, "default.resourceenvironment");
        File alloc = new File(repoBaseFolder, "default.allocation");
        File monitorRepo = new File(repoBaseFolder, "default.monitorrepository");
        
        MonitorRepositoryBuilderUtil monitorBuilder = new MonitorRepositoryBuilderUtil(repo, system, usage);
        monitorBuilder.monitorExternalCalls().monitorUsageScenarios();
        monitorBuilder.saveToFile(monitorRepo, new File(repoBaseFolder, "default.measuringpoint"));
        
        File measuringPoint = new File(repoBaseFolder, "default.measuringpoint");

        File resultFile = new File(resultsBaseFolder, resultName);

        EvaluationData data = new EvaluationData();
        data.setRepository(repo);
        data.setSysten(system);
        data.setResourceenv(resenv);
        data.setAllocation(alloc);
        data.setUsagemodel(usage);
        data.setMonitorRepository(monitorRepo);
        data.setMeasuringPoint(measuringPoint);
        data.setValidationFolder(validationBaseFolder);
        data.setTargetService("_NjSLcGUlEeqsxIbJMvutiA");
        data.setOutputJsonFile(resultFile);

        PCMHeadlessClient client = new PCMHeadlessClient(EvaluationConfig.SIMULATION_REST_URL);
        EvaluationAutomizer automizer = new EvaluationAutomizer(client, data);

        automizer.execute(10, false, EvaluationConfig.defaultConfig);
    }
}
