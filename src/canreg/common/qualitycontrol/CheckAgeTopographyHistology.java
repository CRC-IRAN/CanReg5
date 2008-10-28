package canreg.common.qualitycontrol;

import canreg.common.Globals;
import canreg.common.qualitycontrol.Checker.CheckNames;
import java.util.Map;

/**
 *
 * @author ervikm
 */
public class CheckAgeTopographyHistology implements CheckInterface {

    public Checker.CheckNames checkName = Checker.CheckNames.AgeTopographyHistology;
    
    public static Globals.StandardVariableNames[] variablesNeeded = new Globals.StandardVariableNames[]{
        Globals.StandardVariableNames.Age,
        Globals.StandardVariableNames.Topography,
        Globals.StandardVariableNames.Morphology,
    };

    public Globals.StandardVariableNames[] getVariablesNeeded() {
        return variablesNeeded;
    }

    public CheckResult performCheck(Map<Globals.StandardVariableNames, Object> variables) {
        
        CheckResult result = new CheckResult();
        result.setCheckName(checkName.toString());

        String ageCode = null;
        String topographyCode = null;
        String morphologyCode = null;

        int morphologyNumber = 0;
        int ageNumber = 0;
        int topographyNumber = 0;

        try {
            ageCode = variables.get(Globals.StandardVariableNames.Age).toString();
            ageNumber = Integer.parseInt(ageCode);
            topographyCode = variables.get(Globals.StandardVariableNames.Topography).toString();
            topographyNumber = Integer.parseInt(topographyCode);
            morphologyCode = variables.get(Globals.StandardVariableNames.Morphology).toString();
            morphologyNumber = Integer.parseInt(morphologyCode);
        } catch (NumberFormatException numberFormatException) {
            result.setResultCode(CheckResult.ResultCode.Invalid);
            result.setMessage("Not a number");
            return result;
        } catch (NullPointerException nullPointerException) {
            result.setResultCode(CheckResult.ResultCode.Missing);
            result.setMessage("Missing variable(s) needed.");
            return result;
        }

    	int topographyGroup = topographyNumber/10;
    	boolean ok = true;

    	if (ageNumber < 40 && topographyGroup==61 && (morphologyNumber/10)==814)
    		ok = false;

    	if (ageNumber < 20 && topographyGroup==17 && morphologyNumber < 9590)
    		ok = false;

    	if (ageNumber < 20
    	 && (topographyGroup==33 || topographyGroup==34 || topographyGroup==18)
    	 && (morphologyNumber/10 != 824))
    		ok = false;

    	if (ageNumber > 5 && (morphologyNumber==9510 || morphologyNumber==9512)
    	 && (topographyGroup==69))
    		ok = false;

    	if ((ageNumber < 15 || ageNumber > 45) && topographyGroup==58 && morphologyNumber==9100)
    		ok = false;

    	if (ok)
    	{
    		result.setMessage("");
                result.setResultCode(CheckResult.ResultCode.OK);
                return result;
    	}
    	else
    	{
    		result.setMessage(ageCode+", "+topographyCode+", "+ morphologyCode);
                result.setResultCode(CheckResult.ResultCode.Rare);
                return result;
    	}
        

    }

    public CheckNames getCheckName() {
        return checkName;
    }
}