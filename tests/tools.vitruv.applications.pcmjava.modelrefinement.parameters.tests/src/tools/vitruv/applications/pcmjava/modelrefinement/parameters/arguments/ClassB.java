package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import java.util.ArrayList;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class ClassB {

    
    public ArrayList<Integer> getPrimes(double d) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addFloat("a", d);
        
        ThreadMonitoringController.getInstance().enterService("_DNqHoEP4Eeqlibq1qvJ3LA",parameters);
        ArrayList<Integer> primes = new ArrayList<Integer>();
        
        for(int i=2; i<=d; i++) {
            boolean isPrime = true;
            for (int j=2; j <= i/2; j++){
                if ( i % j == 0){
                    isPrime = false;
                    break;
                }
            }
            if(isPrime) {
             primes.add(i);   
            }
        }
        ThreadMonitoringController.getInstance().exitService();
        return primes;
        }
    
        public void foo(boolean b) {
            ServiceParameters parameters = new ServiceParameters();
            parameters.addBoolean("b", b);
            
            ThreadMonitoringController.getInstance().enterService("_922bEEWkEeqLzaiRqTsXpA",parameters);
            //do something
            ThreadMonitoringController.getInstance().exitService();
            
        }
    }

