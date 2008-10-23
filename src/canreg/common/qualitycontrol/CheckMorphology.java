package canreg.common.qualitycontrol;

import canreg.common.Globals;
import canreg.common.LookUpFileDescription;
import canreg.common.LookUpLoader;
import canreg.common.qualitycontrol.Checker.CheckNames;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ervikm
 */
public class CheckMorphology implements CheckInterface {

    public static Checker.CheckNames checkName = Checker.CheckNames.Morphology;
    
    public static Globals.StandardVariableNames[] variablesNeeded = new Globals.StandardVariableNames[]{
        Globals.StandardVariableNames.Morphology,
    };
    
    Map<String,String> morphologicalFamiliesMap;
    private int codeLength = 4;
    private String lookUpFileResource = "/canreg/common/resources/lookup/MorphFam.txt";

    public Globals.StandardVariableNames[] getVariablesNeeded() {
        return variablesNeeded;
    }

    public CheckMorphology() {
        URL resourceURL = this.getClass().getResource(lookUpFileResource);       
        LookUpFileDescription description = new LookUpFileDescription(resourceURL, codeLength);
        try {
            morphologicalFamiliesMap = LookUpLoader.load(description);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CheckMorphology.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(CheckMorphology.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(CheckMorphology.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CheckResult performCheck(Map<Globals.StandardVariableNames, Object> variables) {

        CheckResult result = new CheckResult();
        result.setCheckName(checkName.toString());

        String morphologyCode = null;

        try {
            morphologyCode = variables.get(Globals.StandardVariableNames.Morphology).toString();
        } catch (NullPointerException nullPointerException) {
            result.setResultCode(CheckResult.ResultCode.Missing);
            result.setMessage("Missing variable(s) needed.");
            return result;
        }

        String morphologyFamilyString = morphologicalFamiliesMap.get(morphologyCode);

        if (morphologyFamilyString == null) {
            result.setMessage(morphologyCode);
            result.setResultCode(CheckResult.ResultCode.Invalid);
            System.out.println("not a valid morph code? " + morphologyCode);
            return result;
        } else {
            result.setMessage("");
            result.setResultCode(CheckResult.ResultCode.OK);
            return result;
        }
    }

    public CheckNames getCheckName() {
        return checkName;
    }
}
