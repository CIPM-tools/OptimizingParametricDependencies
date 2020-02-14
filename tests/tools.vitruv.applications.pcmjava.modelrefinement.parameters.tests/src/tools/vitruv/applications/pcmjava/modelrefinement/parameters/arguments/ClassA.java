package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;

public class ClassA {
    ClassB b  = new ClassB();
    
    public void methodA(int x,int y, boolean z){
        ServiceParameters parameters = new ServiceParameters();
        parameters.addInt("x", x);
        parameters.addInt("y", y);
        parameters.addBoolean("z", z);
        
        ThreadMonitoringController.getInstance().enterService("_MxoYQEP4Eeqlibq1qvJ3LA", parameters);
        try {
            ThreadMonitoringController.getInstance().setCurrentCallerId("_MxoYQEP4Eeqlibq1qvJ3LA");
            b.getPrimes(2*x+ Math.log(y));
            b.foo(!z);
            
        } finally {
            ThreadMonitoringController.getInstance().exitService();
        }
        
        
    }
}
