package tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments;

import java.io.IOException;
import java.util.Collections;
import java.nio.file.Paths;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.seff.ExternalCallAction;

import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.sampler.sigar.ISigarSamplerFactory;
import kieker.monitoring.sampler.sigar.SigarSamplerFactory;
import kieker.monitoring.sampler.sigar.samplers.CPUsDetailedPercSampler;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.SeffParameterEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceCallDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.ServiceParameters;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.arguments.impl.ArgumentEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.loop.impl.LoopEstimationImpl;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.monitoring.ThreadMonitoringController;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;

public class Main {
    final static ISigarSamplerFactory sigarFactory = SigarSamplerFactory.INSTANCE;
    final static CPUsDetailedPercSampler cpuSampler = sigarFactory.createSensorCPUsDetailedPerc();
    static ClassA a = new ClassA();
    static SeffParameterEstimation est = new SeffParameterEstimation(true);
    
    public static void main(String[] args) throws IOException {
        
        MonitoringController.getInstance().schedulePeriodicSampler(
                cpuSampler, 0, 1, TimeUnit.SECONDS);
        System.out.flush();
        System.out.println("Set processor affinity to CPU 0 and press enter.");
        System.in.read(); 
        
        ThreadMonitoringController.setSessionId("session-1");
        boolean arg = true;
        String str = "spaghetti";
        
        for(int i=1; i<10; i++) {
            a.methodA(2*i,i,arg,str);
            arg = arg^true;
            str+="i";
        }
        
        System.out.println("Finished performance monitoring");
        
        Repository pcm = PcmUtils.loadModel("test-data\\arguments_test\\default.repository");
        KiekerMonitoringReader reader = new KiekerMonitoringReader("test-data\\arguments_test\\kieker", "session-1");
        est.update(pcm, reader);
        PcmUtils.saveModel("test-data\\arguments_test\\res\\result.repository", pcm);
    }
}
