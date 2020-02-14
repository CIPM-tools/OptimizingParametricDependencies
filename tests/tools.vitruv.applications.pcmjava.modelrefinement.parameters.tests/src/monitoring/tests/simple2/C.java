package monitoring.tests.simple2;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class C {

    public void methodA(String s, boolean b) {
                
        // Monitoring actions start
        ServiceParameters serviceParameters = new ServiceParameters();
        serviceParameters.addString("s", s);
        serviceParameters.addBoolean("b", b);
       
        ThreadMonitoringController.getInstance().enterService("_SVoyANChEeiG9v0ZHxeEbQ", serviceParameters);
        try {
            // Monitoring actions end

            // Monitoring actions start
            long ___startTime_1 = ThreadMonitoringController.getInstance().getTime();
            // Monitoring actions end

            this.computation();

            // Monitoring actions start
            ThreadMonitoringController.getInstance().logResponseTime("_bjAggNChEeiG9v0ZHxeEbQ",
                    "_oro4gG3fEdy4YaaT-RYrLQ", ___startTime_1);
            // Monitoring actions end

            // Monitoring actions start
        } finally {
            ThreadMonitoringController.getInstance().exitService();
        }
        // Monitoring actions end
    }

    private void computation() {
        double[] array = new double[50000];
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.sqrt(i);
            for (int a = 0; a < array.length; a++) {
                array[i] += array[a];
            }
        }
    }
}
