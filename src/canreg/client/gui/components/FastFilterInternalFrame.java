/*
 * FastFilterInternalFrame.java
 *
 * Created on 29 February 2008, 14:44
 */
package canreg.client.gui.components;

import canreg.client.dataentry.DictionaryHelper;
import canreg.common.DatabaseVariablesListElement;
import canreg.common.Globals;
import canreg.server.database.Dictionary;
import canreg.server.database.DictionaryEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.jdesktop.application.Action;
import org.w3c.dom.Document;

/**
 *
 * @author  morten
 */
public class FastFilterInternalFrame extends javax.swing.JInternalFrame {

    private DatabaseVariablesListElement[] variablesInTable;
    private Document doc;
    private String tableName = "both";
    private Map<Integer, Dictionary> dictionaries;
    Dictionary dictionary;
    private Map<String, DictionaryEntry> possibleValuesMap;
    private ActionListener actionListener;
    private int maxLength;
    private boolean dictionaryPopUp = true;
    private boolean currentSelectionAdded = false;

    /** Creates new form FastFilterInternalFrame */
    public FastFilterInternalFrame() {
        initComponents();
        initValues();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        instructionLabel1 = new javax.swing.JLabel();
        instructionLabel2 = new javax.swing.JLabel();
        variableComboBox = new javax.swing.JComboBox();
        operationComboBox = new javax.swing.JComboBox();
        logicalOperatorComboBox = new javax.swing.JComboBox();
        variableLabel = new javax.swing.JLabel();
        operationLabel = new javax.swing.JLabel();
        valueLabel = new javax.swing.JLabel();
        valuesSplitPane = new javax.swing.JSplitPane();
        valueTextField = new javax.swing.JTextField();
        valueTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        filterPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        textPane = new javax.swing.JTextPane();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(FastFilterInternalFrame.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel1.setName("jPanel1"); // NOI18N

        instructionLabel1.setText(resourceMap.getString("instructionLabel1.text")); // NOI18N
        instructionLabel1.setName("instructionLabel1"); // NOI18N

        instructionLabel2.setText(resourceMap.getString("instructionLabel2.text")); // NOI18N
        instructionLabel2.setName("instructionLabel2"); // NOI18N

        variableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(FastFilterInternalFrame.class, this);
        variableComboBox.setAction(actionMap.get("varibleChosenAction")); // NOI18N
        variableComboBox.setName("variableComboBox"); // NOI18N

        operationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        operationComboBox.setAction(actionMap.get("operatorSelected")); // NOI18N
        operationComboBox.setName("operationComboBox"); // NOI18N

        logicalOperatorComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        logicalOperatorComboBox.setAction(actionMap.get("operatorAction")); // NOI18N
        logicalOperatorComboBox.setName("logicalOperatorComboBox"); // NOI18N

        variableLabel.setText(resourceMap.getString("variableLabel.text")); // NOI18N
        variableLabel.setName("variableLabel"); // NOI18N

        operationLabel.setText(resourceMap.getString("operationLabel.text")); // NOI18N
        operationLabel.setName("operationLabel"); // NOI18N

        valueLabel.setText(resourceMap.getString("valueLabel.text")); // NOI18N
        valueLabel.setName("valueLabel"); // NOI18N

        valuesSplitPane.setResizeWeight(0.5);
        valuesSplitPane.setName("valuesSplitPane"); // NOI18N

        valueTextField.setText(resourceMap.getString("valueTextField.text")); // NOI18N
        valueTextField.setAction(actionMap.get("addAction")); // NOI18N
        valueTextField.setName("valueTextField"); // NOI18N
        valueTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClickHandler(evt);
            }
        });
        valuesSplitPane.setLeftComponent(valueTextField);

        valueTextField2.setAction(actionMap.get("addAction")); // NOI18N
        valueTextField2.setName("valueTextField2"); // NOI18N
        valueTextField2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                valueTextField2mouseClickHandler(evt);
            }
        });
        valuesSplitPane.setRightComponent(valueTextField2);

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(instructionLabel1)
            .addComponent(instructionLabel2)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(variableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(variableLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(operationLabel)
                    .addComponent(operationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(valuesSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(valueLabel)
                        .addGap(246, 246, 246)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logicalOperatorComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(instructionLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(instructionLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valueLabel)
                    .addComponent(operationLabel)
                    .addComponent(jLabel1)
                    .addComponent(variableLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(logicalOperatorComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(variableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(operationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(valuesSplitPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("filterPanel.border.title"))); // NOI18N
        filterPanel.setName("filterPanel"); // NOI18N

        scrollPane.setName("scrollPane"); // NOI18N

        textPane.setName("textPane"); // NOI18N
        scrollPane.setViewportView(textPane);

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
        );

        cancelButton.setAction(actionMap.get("cancelAction")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        okButton.setAction(actionMap.get("okAction")); // NOI18N
        okButton.setName("okButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(okButton))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void mouseClickHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClickHandler
    DatabaseVariablesListElement dbvle = (DatabaseVariablesListElement) variableComboBox.getSelectedItem();
    if (dictionaryPopUp && (valueTextField.equals(evt.getSource()) || valueTextField2.equals(evt.getSource())) && dbvle.getVariableType().equalsIgnoreCase("dict")) {
        if (possibleValuesMap == null) {
            JOptionPane.showInternalMessageDialog(this, "Empty dictionary.", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            DictionaryEntry[] possibleValuesArray;

            Vector<DictionaryEntry> possibleValuesVector = new Vector<DictionaryEntry>();
            Vector<DictionaryEntry> allValuesVector = new Vector<DictionaryEntry>();

            Iterator<String> it = possibleValuesMap.keySet().iterator();
            DictionaryEntry tempentry;

            while (it.hasNext()) {
                tempentry = possibleValuesMap.get(it.next());

                allValuesVector.add(tempentry);

                if (dictionary.isCompoundDictionary()) {
                    if (tempentry.getCode().length() < maxLength) {
                        possibleValuesVector.add(tempentry);
                    }
                } else {
                    possibleValuesVector.add(tempentry);
                }
            }
            possibleValuesArray = possibleValuesVector.toArray(new DictionaryEntry[0]);

            DictionaryEntry selectedValue = (DictionaryEntry) JOptionPane.showInternalInputDialog(this,
                    java.util.ResourceBundle.getBundle("canreg/client/gui/components/resources/VariableEditorPanel").getString("Choose_one"), java.util.ResourceBundle.getBundle("canreg/client/gui/components/resources/VariableEditorPanel").getString("Input"),
                    JOptionPane.INFORMATION_MESSAGE, null,
                    possibleValuesArray, possibleValuesArray[0]);

            if (selectedValue != null) {
                String value = selectedValue.getCode();
                while (dictionary.isCompoundDictionary() && value.length() < maxLength) {
                    possibleValuesArray = DictionaryHelper.getDictionaryEntriesStartingWith(value, allValuesVector.toArray(new DictionaryEntry[0]));
                    if (possibleValuesArray.length > 0) {
                        selectedValue = (DictionaryEntry) JOptionPane.showInternalInputDialog(this,
                                java.util.ResourceBundle.getBundle("canreg/client/gui/components/resources/VariableEditorPanel").getString("Choose_one"), java.util.ResourceBundle.getBundle("canreg/client/gui/components/resources/VariableEditorPanel").getString("Input"),
                                JOptionPane.INFORMATION_MESSAGE, null,
                                possibleValuesArray, possibleValuesArray[0]);
                        value = selectedValue.getCode();
                    } else {
                        value = value + "9";
                    }
                }
                // setValue(value);
                JTextField field = (JTextField) evt.getSource();
                field.setText(value);
            }
        }
    } else {
        // Do nothing
    }
    currentSelectionAdded = false;
}//GEN-LAST:event_mouseClickHandler

private void valueTextField2mouseClickHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_valueTextField2mouseClickHandler
    this.mouseClickHandler(evt);
}//GEN-LAST:event_valueTextField2mouseClickHandler
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JLabel instructionLabel1;
    private javax.swing.JLabel instructionLabel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JComboBox logicalOperatorComboBox;
    private javax.swing.JButton okButton;
    private javax.swing.JComboBox operationComboBox;
    private javax.swing.JLabel operationLabel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextPane textPane;
    private javax.swing.JLabel valueLabel;
    private javax.swing.JTextField valueTextField;
    private javax.swing.JTextField valueTextField2;
    private javax.swing.JSplitPane valuesSplitPane;
    private javax.swing.JComboBox variableComboBox;
    private javax.swing.JLabel variableLabel;
    // End of variables declaration//GEN-END:variables

    private void initValues() {
        String[] operators = {"=", "<>", ">", "<", ">=", "<=", "BETWEEN", "LIKE", "IN"};
        operationComboBox.setModel(new DefaultComboBoxModel(operators));
        operationComboBox.setSelectedIndex(0);
        // Get the system description
        doc = canreg.client.CanRegClientApp.getApplication().getDatabseDescription();
        refreshVariableList();

        String[] logicalOperator = {"", "AND", "OR"};
        logicalOperatorComboBox.setModel(new DefaultComboBoxModel(logicalOperator));
        dictionaries = canreg.client.CanRegClientApp.getApplication().getDictionary();
        updatePossibleValues();

    }

    private void refreshVariableList() {
        variablesInTable = canreg.common.Tools.getVariableListElements(doc, Globals.NAMESPACE);
        if (!tableName.equalsIgnoreCase("both")) {
            LinkedList<DatabaseVariablesListElement> tempVariablesInTable = new LinkedList<DatabaseVariablesListElement>();
            for (int i = 0; i <
                    variablesInTable.length; i++) {
                if (variablesInTable[i].getDatabaseTableName().equalsIgnoreCase(tableName)) {
                    tempVariablesInTable.add(variablesInTable[i]);
                }
            }
            variablesInTable = new DatabaseVariablesListElement[tempVariablesInTable.size()];
            for (int i = 0; i <
                    variablesInTable.length; i++) {
                variablesInTable[i] = tempVariablesInTable.get(i);
            }

        }
        variableComboBox.setModel(new DefaultComboBoxModel(variablesInTable));
    }

    /**
     * 
     * @param str
     */
    public void setTextPane(String str) {
        textPane.setText(str);
    }

    /**
     * 
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
        refreshVariableList();
    }

    /**
     * 
     * @param al
     */
    public void setActionListener(ActionListener al) {
        this.actionListener = al;
    }

    /**
     * 
     */
    @Action
    public void cancelAction() {
        this.setVisible(false);
    }

    /**
     * 
     */
    @Action
    public void okAction() {
        if (currentSelectionIsNotAdded()) {
            addAction();
        }
        actionListener.actionPerformed(new ActionEvent(this, 0, textPane.getText().trim()));
        this.setVisible(false);
    }

    /**
     * 
     */
    @Action
    public void addAction() {
        String newFilterPart = "";
        newFilterPart +=
                variableComboBox.getSelectedItem().toString();
        newFilterPart +=
                " ";
        newFilterPart +=
                operationComboBox.getSelectedItem().toString();
        newFilterPart +=
                " ";
        DatabaseVariablesListElement dvle = (DatabaseVariablesListElement) variableComboBox.getSelectedItem();
        if (!dvle.getVariableType().equalsIgnoreCase("Number")) {
            newFilterPart += "'";
        }

        newFilterPart += valueTextField.getText();
        if (!dvle.getVariableType().equalsIgnoreCase("Number")) {
            newFilterPart += "'";
        }

        if (operationComboBox.getSelectedItem().toString().equalsIgnoreCase("BETWEEN")) {
            newFilterPart += " AND ";

            if (!dvle.getVariableType().equalsIgnoreCase("Number")) {
                newFilterPart += "'";
            }

            newFilterPart += valueTextField2.getText();
            if (!dvle.getVariableType().equalsIgnoreCase("Number")) {
                newFilterPart += "'";
            }
        }

        newFilterPart += " ";
        newFilterPart +=
                logicalOperatorComboBox.getSelectedItem().toString();
        newFilterPart +=
                " ";
        textPane.setText(textPane.getText() + newFilterPart);

        // reset things
        valueTextField.setText("");
        valueTextField2.setText("");
        logicalOperatorComboBox.setSelectedIndex(0);

        currentSelectionAdded = true;
    }

    /**
     * 
     */
    @Action
    public void varibleChosenAction() {
        valueTextField.setText("");
        valueTextField2.setText("");
        updatePossibleValues();
        currentSelectionAdded = false;
    }

    @SuppressWarnings("empty-statement")
    private void updatePossibleValues() {
        DatabaseVariablesListElement dbvle = (DatabaseVariablesListElement) variableComboBox.getSelectedItem();
        maxLength = dbvle.getVariableLength();
        int id = dbvle.getDictionaryID();
        if (id >= 0) {
            dictionary = dictionaries.get(id);
            if (dictionary != null) {
                // Map sortedmap = new TreeMap(map);
                possibleValuesMap = dictionary.getDictionaryEntries();
            } else {
                possibleValuesMap = null;
            }
        } else {
            possibleValuesMap = null;
        }
    }

    @Action
    public void operatorSelected() {
        String operator = operationComboBox.getSelectedItem().toString();
        if ("BETWEEN".equalsIgnoreCase(operator)) {
            valueTextField2.setVisible(true);
            valuesSplitPane.setDividerLocation(0.5);
            dictionaryPopUp = true;
        } else if ("LIKE".equalsIgnoreCase(operator)) {
            valueTextField2.setVisible(false);
            valuesSplitPane.setDividerLocation(1);
            dictionaryPopUp = false;
        } else {
            valueTextField2.setVisible(false);
            valuesSplitPane.setDividerLocation(1);
            dictionaryPopUp = true;
        }
        currentSelectionAdded = false;
    }

    private boolean currentSelectionIsNotAdded() {
        boolean isAdded = false;
        if (valueTextField.getText().trim().length() == 0) {
            isAdded = true;
        } else {
            isAdded = currentSelectionAdded;
        }
        return !isAdded;
    }

    @Action
    public void operatorAction() {
        if (currentSelectionIsNotAdded()) {
            addAction();
        }
    }
}
