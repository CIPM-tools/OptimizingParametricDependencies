package evaluation.casestudy2;

import java.io.File;

import evaluation.dependencies.Action;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class Common {

    public static final int ComputationConst1 = 3;

    public static final int ComputationConst2 = 10;

    public static String DataRootPath = "./test-data/casestudy2/";

    public static String ResultsPath = DataRootPath + "results/";

    public static class EvaluationData {
        public static final String Default = "default";
        public static final String Threaded = "threaded";
        public static final String Threaded2 = "threaded-2";
    }

    public final String name;
    public final Mode mode;

    public Common(String name, Mode mode) {
        this.name = name;
        this.mode = mode;
    }

    public void createDirectory(String dirPath) {
        File file = new File(dirPath);
        file.getParentFile().mkdirs();
    }

    public static void computation(final int param) throws InterruptedException {
        double[] array = new double[1000 * param];
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.sqrt(i);
            for (int a = 0; a < array.length; a++) {
                array[i] += array[a];
            }
        }
    }
    public static int fibonacci(int n) {
        int f = 0, g = 1;

        for (int i = 1; i <= n; i++) {
            f = f + g;
            g = f - g;
        }
        return f;
    }
    public static final String CpuResourceId = "_oro4gG3fEdy4YaaT-RYrLQ";

    public void setCallerId(String callerId) {
        ThreadMonitoringController.getInstance().setCurrentCallerId(callerId);

    }

    public void logBranch(String branchId, String branchTransitionId) {
        // Monitoring actions start
        ThreadMonitoringController.getInstance().logBranchExecution(branchId, branchTransitionId);
        // Monitoring actions end
    }

    public void logIteration(String loopId, long iterations) {
        // Monitoring actions start
        ThreadMonitoringController.getInstance().logLoopIterationCount(loopId, iterations);
        // Monitoring actions end
    }

    public void logInternalAction(String internalActionId, Action action) {
        long ___startTime_2 = 0;
        // Monitoring actions start
        ___startTime_2 = ThreadMonitoringController.getInstance().getTime();
        // Monitoring actions end

        action.execute();

        // Monitoring actions start
        ThreadMonitoringController.getInstance().logResponseTime(internalActionId, CpuResourceId, ___startTime_2);
        // Monitoring actions end

    }
}
