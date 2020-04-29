package evaluation.casestudy2;

import java.util.ArrayList;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class B {

    private final Common c;

    public B(Common c) {
        this.c = c;
    }

    public ArrayList<Integer> methodB1(final double b1arg) {
        ArrayList<Integer> primes = new ArrayList<Integer>();
        ServiceParameters parameters = new ServiceParameters();
        parameters.addFloat("b1arg", b1arg);

        ThreadMonitoringController.getInstance()
                .enterService("_kJWHAGz0Eeq3FqIU_QUKjQ", parameters);
        
        c.logInternalAction("_7MNJMGz1Eeq3FqIU_QUKjQ", () -> {
            for (int i = 2; i <= b1arg; i++) {
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
       });
        
        ServiceParameters returnValue = new ServiceParameters();
        returnValue.addList("primes", primes.size());
        ThreadMonitoringController.getInstance().exitService(returnValue);

        return primes;
    }

    public void methodB2(int b2arg) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addInt("b2arg", b2arg);
        ThreadMonitoringController.getInstance().enterService("_kJfQ8Gz0Eeq3FqIU_QUKjQ", parameters);
        
        c.logInternalAction("_bpq6EGz2Eeq3FqIU_QUKjQ", ()->{
            try {
                Common.computation(b2arg/2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
       });
        
        ThreadMonitoringController.getInstance().exitService();
    }
    
    public void methodB3(int b3arg) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addInt("b3arg", b3arg);
        ThreadMonitoringController.getInstance().enterService("_kJc0sGz0Eeq3FqIU_QUKjQ", parameters);
        
        c.logInternalAction("_RI9UYGz2Eeq3FqIU_QUKjQ", ()->{
            try {
                Common.computation(2);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
       
        ThreadMonitoringController.getInstance().exitService();
    }
    
    public boolean methodB4(final boolean b4arg) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addBoolean("b4arg", b4arg);
        ThreadMonitoringController.getInstance().enterService("_kJaYcGz0Eeq3FqIU_QUKjQ", parameters);
        
        //do something
        
        ServiceParameters returnValue = new ServiceParameters();
        returnValue.addBoolean("NOTbool", !b4arg);
        ThreadMonitoringController.getInstance().exitService(returnValue);
        return !b4arg;
    }

    public void methodB5(final int b5arg) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addInt("b5arg", b5arg);
        ThreadMonitoringController.getInstance().enterService("_kJhGIGz0Eeq3FqIU_QUKjQ", parameters);
        
        c.logInternalAction("_ldfkkGz2Eeq3FqIU_QUKjQ", ()->{
            try {
                Common.computation(b5arg);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        
        ThreadMonitoringController.getInstance().exitService();
    }
    
    public void methodB6(final double b6arg) {
        ServiceParameters parameters = new ServiceParameters();
        parameters.addFloat("b6arg", b6arg);
        ThreadMonitoringController.getInstance().enterService("_kJi7UGz0Eeq3FqIU_QUKjQ", parameters);
        
        c.logInternalAction("_EFKRcGz3Eeq3FqIU_QUKjQ", () -> {
            try {
                Common.computation((int) (2*b6arg));
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
        ThreadMonitoringController.getInstance().exitService();
    }
    
}
