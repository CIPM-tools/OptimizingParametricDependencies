package evaluation.casestudy2;

import java.util.List;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class A {

    private final B externalB;
    private final Common c;

    public A(final B externalB, Common common) {
        this.externalB = externalB;
        this.c = common;
    }

    public int methodA(final int x, final int y, final boolean b, final List<String> mylist) {
            ServiceParameters serviceParameters = new ServiceParameters();
            serviceParameters.addInt("x", x);
            serviceParameters.addInt("y", y);
            serviceParameters.addBoolean("b", b);
            serviceParameters.addList("mylist", mylist.size());
            
            ThreadMonitoringController.getInstance()
                    .enterService("_NjSLcGUlEeqsxIbJMvutiA", serviceParameters);
        
        try {
            return this.methodAIntern(x,y,b,mylist);
        } finally {
                ThreadMonitoringController.getInstance().exitService();
           
        }
    }

    private int methodAIntern(final int x, final int y, final boolean b, final List<String> mylist) {
        c.logInternalAction("_Ai9TQGUmEeqsxIbJMvutiA",
                () -> {
                    Common.fibonacci(x);
                });

        // Monitoring actions start
        long ___loopIterationsCount_1 = 0;
        // Monitoring actions end

        int result = 0;
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

            // Monitoring actions start
            String ___executedBranchId_1 = null;
            // Monitoring actions end

            if (b) {
                // Monitoring actions start
                ___executedBranchId_1 = "_I_JccGUmEeqsxIbJMvutiA";
                // Monitoring actions end

                c.setCallerId("_XvCYUGUmEeqsxIbJMvutiA");
                result += this.externalB.methodB1(Math.pow(x, 2) + 4*y).size();
            }

            c.logBranch("_Hpxk8GUmEeqsxIbJMvutiA", ___executedBranchId_1);
        }

        c.logIteration("_FSbTEGUmEeqsxIbJMvutiA", ___loopIterationsCount_1);

   

            // Monitoring actions start
            String ___executedBranchId_2 = null;
            // Monitoring actions end

            if (x < 7) {

                // Monitoring actions start
                ___executedBranchId_2 = "_p1wtIGUmEeqsxIbJMvutiA";
                // Monitoring actions end

                c.setCallerId("_4RyR0GUmEeqsxIbJMvutiA");
                this.externalB.methodB2(!b);
            }

            c.logBranch("_oCVAoGUmEeqsxIbJMvutiA", ___executedBranchId_2);
        
        c.setCallerId("_88KWQGUmEeqsxIbJMvutiA");
        this.externalB.methodB3((int)Math.pow(mylist.size(), 2), mylist);
        return result;
    }

}
