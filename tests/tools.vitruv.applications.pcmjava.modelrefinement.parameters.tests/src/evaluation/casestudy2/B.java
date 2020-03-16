package evaluation.casestudy2;

import java.util.ArrayList;
import java.util.List;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class B {

    private final Common c;

    public B(Common c) {
        this.c = c;
    }

    public ArrayList<Integer> methodB1(final double d) {
        ArrayList<Integer> primes = new ArrayList<Integer>();
            ServiceParameters parameters = new ServiceParameters();
            parameters.addFloat("d", d);

            ThreadMonitoringController.getInstance()
                    .enterService("_tzMBcGUlEeqsxIbJMvutiA", parameters);

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

    public boolean methodB2(final boolean bool) {
            ServiceParameters parameters = new ServiceParameters();
            parameters.addBoolean("bool", bool);
            ThreadMonitoringController.getInstance().enterService("_tzOdsGUlEeqsxIbJMvutiA", parameters);
            //do something
            ServiceParameters returnValue = new ServiceParameters();
            returnValue.addBoolean("NOTbool", !bool);
            ThreadMonitoringController.getInstance().exitService();
        return !bool;
    }
    
    public void methodB3(int size, List<String> listB3) {
            ServiceParameters parameters = new ServiceParameters();
            parameters.addInt("size", size);
            parameters.addList("listB3", listB3.size());
            ThreadMonitoringController.getInstance().enterService("_tzRhAGUlEeqsxIbJMvutiA", parameters);
            
            //do something
            ThreadMonitoringController.getInstance().exitService();
    }
}
