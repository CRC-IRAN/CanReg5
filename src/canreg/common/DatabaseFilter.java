package canreg.common;

import java.io.Serializable;

/**
 *
 * @author ervikm
 */
public class DatabaseFilter implements Serializable {
    private String filterString;
    
    public String getFilterString() {
        return filterString;
    }

    public void setFilterString(String filterString) {
        this.filterString = filterString;
    }

}
