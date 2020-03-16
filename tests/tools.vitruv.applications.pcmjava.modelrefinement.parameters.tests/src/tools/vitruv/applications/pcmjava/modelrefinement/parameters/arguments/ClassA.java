package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import evaluation.casestudy.Common;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class ClassA {
    ClassB bObject = new ClassB();

    public void methodA(int x, int y, boolean b, String s) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addInt("x", x);
        parameters.addInt("y", y);
        parameters.addBoolean("b", b);
        parameters.addString("s", s);

        ThreadMonitoringController.getInstance().enterService("_S_FXMFosEeqYMNt7-6dMJg", parameters);
        try {
            long ___startTime = ThreadMonitoringController.getInstance().getTime();

            Common.computation(x);
            ThreadMonitoringController.getInstance().logResponseTime("_UTYQoFosEeqYMNt7-6dMJg", Common.CpuResourceId,
                    ___startTime);

            int __loopIterationsCount = 0;
            for (int i = 0; i < y; i++) {
                ThreadMonitoringController.getInstance().setCurrentCallerId("_dWVe4FosEeqYMNt7-6dMJg");
                bObject.getPrimes(2 * y + Math.pow(x, 3)); // a
                ThreadMonitoringController.getInstance().setCurrentCallerId("_fHy1cFosEeqYMNt7-6dMJg");
                bObject.foo(!b); // b
                ThreadMonitoringController.getInstance().setCurrentCallerId("_gCbRsFosEeqYMNt7-6dMJg");
                bObject.bar(s.length()); // c
                ThreadMonitoringController.getInstance().setCurrentCallerId("_hLiO0FosEeqYMNt7-6dMJg");
                bObject.getPrimes(s.length() + 1);
                __loopIterationsCount++;
            }
            ThreadMonitoringController.getInstance().logLoopIterationCount("_ZyfksFosEeqYMNt7-6dMJg",
                    __loopIterationsCount);
            
            String __executedBranchId = null;
            if(x+y < 20) {
                __executedBranchId = "_Dra5oFsNEeqmVOZrbVCimw";
                //do something                
            }
            ThreadMonitoringController.getInstance().logBranchExecution("_--LucFsMEeqmVOZrbVCimw", __executedBranchId);
        } finally {
            ThreadMonitoringController.getInstance().exitService();
        }

    }
}
