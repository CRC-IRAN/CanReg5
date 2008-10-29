package canreg.common;

/**
 *
 * @author ervikm
 */
public class DatabaseGroupsListElement {
    private String groupName;
    private int groupIndex;
    private int groupPosition;

    DatabaseGroupsListElement(String groupName, int index) {
        this.groupName = groupName;
        this.groupIndex = index;
    }

    /**
     * 
     * @return
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     * 
     * @return
     */
    public int getGroupIndex() {
        return groupIndex;
    }

    /**
     * 
     * @param groupIndex
     */
    public void setGroupIndex(int groupIndex) {
        this.groupIndex = groupIndex;
    }

    /**
     * 
     * @return
     */
    public int getGroupPosition() {
        return groupPosition;
    }

    /**
     * 
     * @param groupPosition
     */
    public void setGroupPosition(int groupPosition) {
        this.groupPosition = groupPosition;
    }
}
