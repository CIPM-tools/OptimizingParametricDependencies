package evaluation.casestudy2;

import java.util.ArrayList;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class A {

    private final B externalB;
    private final Common c;

    public A(final B externalB, Common common) {
        this.externalB = externalB;
        this.c = common;
    }

    public void methodA(final int x, final int y, final boolean b) {
        ServiceParameters serviceParameters = new ServiceParameters();
        serviceParameters.addInt("x", x);
        serviceParameters.addInt("y", y);
        serviceParameters.addBoolean("b", b);

        ThreadMonitoringController.getInstance()
                .enterService("_NjSLcGUlEeqsxIbJMvutiA",serviceParameters);

        try {
            this.methodAIntern(x, y, b);
        } finally {
            ThreadMonitoringController.getInstance().exitService();

        }
    }

    private void methodAIntern(final int x, final int y, final boolean b) {
        c.logInternalAction("_Ai9TQGUmEeqsxIbJMvutiA",
                () -> {
                    try {
                        Common.computation(x);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });

        // Monitoring actions start
       long ___loopIterationsCount_1 = 0;
        // Monitoring actions end

        for (int i = 0; i < y; i++) {

            // Monitoring actions start
            ___loopIterationsCount_1++;
            // Monitoring actions end

            c.logInternalAction("_rOL4oGUmEeqsxIbJMvutiA",
            () -> {
                try {
                    Common.computation(Common.ComputationConst1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
                
            c.setCallerId("_XvCYUGUmEeqsxIbJMvutiA");
            ArrayList<Integer> result = this.externalB.methodB1(4 * x);

            c.setCallerId("_sUu0EGz0Eeq3FqIU_QUKjQ");
            this.externalB.methodB2((int) Math.pow(x, 2) + y);

            c.setCallerId("_thOcYGz0Eeq3FqIU_QUKjQ");
            this.externalB.methodB5(result.size());

        }

        c.logIteration("_FSbTEGUmEeqsxIbJMvutiA", ___loopIterationsCount_1);

        // Monitoring actions start
        String ___executedBranchId_2 = null;
        // Monitoring actions end

        if (b) {

            // Monitoring actions start
            ___executedBranchId_2 = "_p1wtIGUmEeqsxIbJMvutiA";
            // Monitoring actions end

            c.setCallerId("_4mPpAGz0Eeq3FqIU_QUKjQ");
            this.externalB.methodB3((int) Math.pow(x, 3));

            c.setCallerId("_69X4cGz0Eeq3FqIU_QUKjQ");
            this.externalB.methodB6(Math.pow(y, 0.5));
        }

            c.setCallerId("_5-IpAGz0Eeq3FqIU_QUKjQ");
        this.externalB.methodB4(!b);

        c.logBranch("_oCVAoGUmEeqsxIbJMvutiA", ___executedBranchId_2);

    }

}
