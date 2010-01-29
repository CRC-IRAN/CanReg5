package canreg.common;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author ervikm
 */
public class DatabaseIndexesListElement  implements Serializable, DatabaseElement {

    private String indexName;
    private String databaseTableName;
    private LinkedList<String> variableNamesInIndex;
    private String mainVariable = null;
    private DatabaseVariablesListElement[] variableListElementsInIndex;

    public DatabaseIndexesListElement(String indexName) {
        this.indexName = indexName;
    }

    /**
     * 
     * @return
     */
    public String getIndexName() {
        return indexName;
    }

    /**
     * 
     * @param indexName
     */
    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public void setMainVariable(String mainVariable) {
       this.mainVariable = mainVariable;
    }

    @Override
    public String toString() {
        return getMainVariable()+" ("+databaseTableName+")";
    }

    /**
     * @return the tableName
     */
    public String getDatabaseTableName() {
        return databaseTableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setDatabaseTableName(String tableName) {
        this.databaseTableName = tableName;
    }

    /**
     * @return the mainVariable
     */
    public String getMainVariable() {
        if (getVariableNamesInIndex() != null) {
            return getVariableNamesInIndex().getFirst();
        } else {
            return mainVariable;
        }
    }

    /**
     * @return the variablesInIndex
     */
    public LinkedList<String> getVariableNamesInIndex() {
        return variableNamesInIndex;
    }


    public void setVariablesInIndex(DatabaseVariablesListElement[] variableListElementsInIndex) {
        variableNamesInIndex = new LinkedList<String>();
        for (DatabaseVariablesListElement dvle:variableListElementsInIndex){
            variableNamesInIndex.add(dvle.getDatabaseVariableName());
        }
        this.variableListElementsInIndex = variableListElementsInIndex;
    }

    public DatabaseVariablesListElement[] getVariableListElementsInIndex(){
        return variableListElementsInIndex;
    }
}
