package evaluation.casestudy2;

import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.SeffParameterEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;

public class SeffParameterEstimationTest {

    public static KiekerMonitoringReader getReader(Common c) {
        return new KiekerMonitoringReader(Common.DataRootPath + "kieker/", c.name);
    }

    public static Repository loadPcmModel(Common c) {
        return PcmUtils.loadModel(Common.DataRootPath + "/default.repository");
    }

    public static void savePcmModel(Common c, Repository repository) {
        PcmUtils.saveModel(Common.DataRootPath + "results/" + c.mode + "/result.repository", repository);
    }

    @BeforeClass
    public static void setUp() {
        LoggingUtil.InitConsoleLogger();
    }    
    
    @Test
    public void evaluation() throws Exception {
        evaluationStep(Mode.Parametrized);
        //evaluationStep(Mode.ParametrizedWithOpt);      
    }

    public static void evaluationStep(Mode mode) throws Exception {
        Common c = new Common("session-1", mode);
        
        SeffParameterEstimation estimation = null;
        
        if(mode.equals(Mode.Parametrized)) {
            estimation = new SeffParameterEstimation(false);
        }
        else if (mode.equals(Mode.ParametrizedWithOpt)) {
            estimation = new SeffParameterEstimation(true);
        }

        MonitoringDataSet reader = getReader(c);
        Repository pcmModel = loadPcmModel(c);
        estimation.update(pcmModel, reader);
        
        savePcmModel(c, pcmModel);
        System.out.println("estimated mode: " + mode.toString());
    }
}
