package canreg.common;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author ervikm
 */
public class DatabaseFilter implements Serializable {

    static public enum QueryType {
        BROWSER,
        FREQUENCIES_BY_YEAR
    }
    private String filterString;
    private Set<DatabaseVariablesListElement> databaseVariables;
    private QueryType queryType;

    public Set<DatabaseVariablesListElement> getDatabaseVariables() {
        return databaseVariables;
    }

    public void setDatabaseVariables(Set<DatabaseVariablesListElement> databaseVariables) {
        this.databaseVariables = databaseVariables;
    }
    
    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }
}
