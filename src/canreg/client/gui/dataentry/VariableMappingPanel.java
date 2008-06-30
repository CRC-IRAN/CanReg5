/*
 * VariableMappingPanel.java
 *
 * Created on 27 May 2008, 16:58
 */
package canreg.client.gui.dataentry;

import canreg.common.DatabaseVariablesListElement;
import org.jdesktop.application.Action;

/**
 *
 * @author  ervikm
 */
public class VariableMappingPanel extends javax.swing.JPanel {

    DatabaseVariablesListElement[] variablesInDB;
    String fileVariable;

    /** Creates new form VariableMappingPanel */
    public VariableMappingPanel() {
        initComponents();
    }

    void setDBVariables(DatabaseVariablesListElement[] variablesInDB) {
        this.variablesInDB = variablesInDB;
        DatabaseVariablesListElement[] entriesInComboBox = new DatabaseVariablesListElement[variablesInDB.length+1];
        entriesInComboBox[0]=null;
        for (int i = 0; i<variablesInDB.length; i++){
            entriesInComboBox[i+1]=variablesInDB[i];
        }
        dbVariableComboBox.setModel(new javax.swing.DefaultComboBoxModel(entriesInComboBox));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        fileVariableLabel = new javax.swing.JLabel();
        dbVariableComboBox = new javax.swing.JComboBox();

        setName("Form"); // NOI18N

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        fileVariableLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(VariableMappingPanel.class);
        fileVariableLabel.setText(resourceMap.getString("fileVariableLabel.text")); // NOI18N
        fileVariableLabel.setName("fileVariableLabel"); // NOI18N
        jSplitPane1.setLeftComponent(fileVariableLabel);

        dbVariableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DB Variable 1", "DB Variable 2" }));
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(VariableMappingPanel.class, this);
        dbVariableComboBox.setAction(actionMap.get("changeComboBoxAction")); // NOI18N
        dbVariableComboBox.setName("dbVariableComboBox"); // NOI18N
        jSplitPane1.setRightComponent(dbVariableComboBox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
  
    public int getDBVariableIndex() {
        DatabaseVariablesListElement dbVLE = (DatabaseVariablesListElement) dbVariableComboBox.getSelectedItem();
        return dbVLE.getDatabaseTableVariableID();
    }
    
    public DatabaseVariablesListElement getSelectedDBVariableObject(){
        return (DatabaseVariablesListElement) dbVariableComboBox.getSelectedObjects()[0];
    }

    public void setSelectedDBVariableObject(DatabaseVariablesListElement dbVLE){
        dbVariableComboBox.setSelectedItem(dbVLE);
    }
    
    public void setSelectedDBIndex(int i){
        boolean found = false;
        int j = 0;
        DatabaseVariablesListElement dbVLE = null;
        while (!found && j<variablesInDB.length){
            dbVLE = variablesInDB[j++];
            found = dbVLE.getDatabaseTableVariableID()==i;
        }
        if (found) {
            dbVariableComboBox.setSelectedItem(dbVLE);
            fileVariableLabel.setText("<html>"+fileVariable+"<html>");
        }
        else {
            dbVariableComboBox.setSelectedItem(null);
            fileVariableLabel.setText("<html><b>"+fileVariable+"</b><html>");
            fileVariableLabel.repaint();
        }
    }
    
    public void setFileVariableName(String variable){
        fileVariable = variable;
        fileVariableLabel.setText(variable);
    }
    
    public String getFileVariableName(){
        return fileVariableLabel.getText();
    }

    @Action
    public void changeComboBoxAction() {
        DatabaseVariablesListElement dbvle = (DatabaseVariablesListElement) dbVariableComboBox.getSelectedItem();
        if (dbvle!=null)
            setSelectedDBIndex(dbvle.getDatabaseTableVariableID());
        else {
            setSelectedDBIndex(-1);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dbVariableComboBox;
    private javax.swing.JLabel fileVariableLabel;
    private javax.swing.JSplitPane jSplitPane1;
    // End of variables declaration//GEN-END:variables
    
}