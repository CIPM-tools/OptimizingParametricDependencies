package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import java.util.ArrayList;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class ClassB {

    public final ArrayList<Integer> getPrimes(final double d) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addFloat("a", d);

        ThreadMonitoringController.getInstance()
                .enterService("_CSBlkFM9EeqgKcfP5vloLg", parameters);
        ArrayList<Integer> primes = new ArrayList<Integer>();

        for (int i = 2; i <= d; i++) {
            boolean isPrime = true;
            for (int j = 2; j <= i / 2; j++) {
                if (i % j == 0) {
                    isPrime = false;
                    break;
                }
            }
            if (isPrime) {
                primes.add(i);
            }
        }
        ServiceParameters returnValue = new ServiceParameters();
        returnValue.addList("primes", primes.size());
        ThreadMonitoringController.getInstance().exitService(returnValue);
        return primes;
    }

    public void foo(final boolean b) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addBoolean("b", b);

        ThreadMonitoringController.getInstance()
                .enterService("_CSGeEFM9EeqgKcfP5vloLg", parameters);
        // do something
        ThreadMonitoringController.getInstance().exitService();

    }

    public void bar(final double c) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addFloat("c", c);

        ThreadMonitoringController.getInstance()
                .enterService("_CSKIcFM9EeqgKcfP5vloLg", parameters);
        // do something
        ThreadMonitoringController.getInstance().exitService();
    }
}
