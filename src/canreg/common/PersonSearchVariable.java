/**
 * CanReg5 - a tool to input, store, check and analyse cancer registry data.
 * Copyright (C) 2008-2011  International Agency for Research on Cancer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Morten Johannes Ervik, CIN/IARC, ervikm@iarc.fr
 */

package canreg.common;

import canreg.common.qualitycontrol.PersonSearcher.CompareAlgorithms;
import java.io.Serializable;

/**
 *
 * @author ervikm
 */
public class PersonSearchVariable implements Serializable {

    private float weight;
    private DatabaseVariablesListElement databaseVariablesListElement;
    private CompareAlgorithms compareAlgorithm;

    /**
     * 
     * @return
     */
    public String getName() {
        return databaseVariablesListElement.getDatabaseVariableName();
    }

    /**
     * 
     * @return
     */
    public float getWeight() {
        return weight;
    }

    /**
     * 
     * @param weight
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setVariable(DatabaseVariablesListElement databaseVariablesListElement) {
        this.databaseVariablesListElement = databaseVariablesListElement;
        setDefaultAlgorithmName();
    }

    /**
     * @return the algorithmName
     */
    public CompareAlgorithms getCompareAlgorithm() {
        return compareAlgorithm;
    }

    /**
     * @param compareAlgorithm the algorithmName to set
     */
    public void setAlgorithm(CompareAlgorithms compareAlgorithm) {
        if (compareAlgorithm != null) {
            this.compareAlgorithm = compareAlgorithm;
        }
    }

    private void setDefaultAlgorithmName() {
        String variableType = databaseVariablesListElement.getVariableType();
        if (variableType.equalsIgnoreCase(Globals.VARIABLE_TYPE_DICTIONARY_NAME)) {
            this.compareAlgorithm = CompareAlgorithms.code;
        } else if (variableType.equalsIgnoreCase(Globals.VARIABLE_TYPE_ALPHA_NAME)) {
            this.compareAlgorithm = CompareAlgorithms.alpha;
        } else if (variableType.equalsIgnoreCase(Globals.VARIABLE_TYPE_DATE_NAME)) {
            this.compareAlgorithm = CompareAlgorithms.date;
        } else if (variableType.equalsIgnoreCase(Globals.VARIABLE_TYPE_NUMBER_NAME)) {
            this.compareAlgorithm = CompareAlgorithms.number;
        } else {
            this.compareAlgorithm = CompareAlgorithms.alpha;
        }
    }
}
