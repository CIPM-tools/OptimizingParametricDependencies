package evaluation.casestudy2;

import org.junit.BeforeClass;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.Repository;

import tools.vitruv.applications.pcmjava.modelrefinement.parameters.LoggingUtil;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.MonitoringDataSet;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.SeffParameterEstimation;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.impl.KiekerMonitoringReader;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.optimization.genetic.OptimizationConfig;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.optimization.genetic.OptimizationMode;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.PcmUtils;
import tools.vitruv.applications.pcmjava.modelrefinement.parameters.util.Utils;

public class SeffParameterEstimationTest {

    public static KiekerMonitoringReader getReader(Common c) {
        return new KiekerMonitoringReader(Common.DataRootPath + "kieker/");
    }

    public static Repository loadPcmModel(Common c) {
        return PcmUtils.loadModel(Common.DataRootPath + "/default.repository");
    }
    

    public static void savePcmModel(Common c, Repository repository) {
        PcmUtils.saveModel(Common.DataRootPath + c.mode + "/result.repository", repository);
    }

    @BeforeClass
    public static void setUp() {
        LoggingUtil.InitConsoleLogger();
    }    
    
    @Test
    public void evaluation() throws Exception {
        evaluationStep(Mode.jp);
        evaluationStep(Mode.nonOptimized);
        evaluationStep(Mode.optimized);   
        //evaluationStep(Mode.noInitInd);
        //evaluationStep(Mode.noFS); 
        //evaluationStep(Mode.noRV);
    }

    public static void evaluationStep(Mode mode) throws Exception {
        Common c = new Common("session-1", mode);
        
        SeffParameterEstimation estimation = null;
        
        if(mode.equals(Mode.jp)) {
            estimation = new SeffParameterEstimation(false,false,false,false, OptimizationConfig.EMPTY);
        }
        else if(mode.equals(Mode.nonOptimized)) {
            estimation = new SeffParameterEstimation(false,true,true,true, OptimizationConfig.EMPTY);
        }
        else if (mode.equals(Mode.optimized)) {
            estimation = new SeffParameterEstimation(true,true,true,true,OptimizationConfig.STANDARD);
        }
        
        else if (mode.equals(Mode.noInitInd)) {
            OptimizationConfig config = new OptimizationConfig(10000, 2, 10, OptimizationMode.Basic, 5,
                    0.01, false);
            estimation = new SeffParameterEstimation(true,true,true,true,config);
        }
        else if (mode.equals(Mode.noFS)) {
            estimation = new SeffParameterEstimation(false,true,false,true,OptimizationConfig.EMPTY);
        }
        
        else if (mode.equals(Mode.noRV)) {
            estimation = new SeffParameterEstimation(false,true,true,false,OptimizationConfig.EMPTY);
        }
        
        
        MonitoringDataSet reader = getReader(c);
        Repository pcmModel = loadPcmModel(c);
        estimation.update(pcmModel, reader);
        
        savePcmModel(c, pcmModel);
        System.out.println("estimated mode: " + mode.toString());
    }
}
