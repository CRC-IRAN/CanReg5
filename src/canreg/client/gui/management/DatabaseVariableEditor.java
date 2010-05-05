/*
 * DatabaseVariableEditor.java
 *
 * Created on 20-Jan-2010, 16:09:34
 */
package canreg.client.gui.management;

import canreg.common.TranslatableListElement;
import canreg.common.DatabaseDictionaryListElement;
import canreg.common.DatabaseGroupsListElement;
import canreg.common.DatabaseVariablesListElement;
import canreg.common.Globals;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;

/**
 *
 * @author ervikm
 */
public class DatabaseVariableEditor extends javax.swing.JPanel {

    private DatabaseVariablesListElement databaseVariablesListElement;

    /** Creates new form DatabaseVariableEditor */
    public DatabaseVariableEditor() {
        initComponents();
        int numberOfStandardVarableNames = Globals.StandardVariableNames.values().length;
        String[] standardVariableNames = new String[numberOfStandardVarableNames + 1];
        standardVariableNames[0] = "";
        for (int i = 0; i < numberOfStandardVarableNames; i++) {
            standardVariableNames[i + 1] = Globals.StandardVariableNames.values()[i].toString();
        }
        standardVariableNameComboBox.setModel(new DefaultComboBoxModel(standardVariableNames));
        fillInStatusComboBox.setModel(new DefaultComboBoxModel(
                new TranslatableListElement[]{
                    new TranslatableListElement(Globals.FILL_IN_STATUS_MANDATORY_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("MANDATORY")),
                    new TranslatableListElement(Globals.FILL_IN_STATUS_OPTIONAL_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("OPTIONAL")),
                    new TranslatableListElement(Globals.FILL_IN_STATUS_AUTOMATIC_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("AUTOMATIC")),
                    new TranslatableListElement(Globals.FILL_IN_STATUS_SYSTEM_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("SYSTEM"))}));
        multiplePrimaryComboBox.setModel(new DefaultComboBoxModel(
                new TranslatableListElement[]{
                    new TranslatableListElement(Globals.MULTIPLEPRIMARY_COPY_OTHER_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("OTHER")),
                    new TranslatableListElement(Globals.MULTIPLEPRIMARY_COPY_INTERESTING_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("INTERESTING")),
                    new TranslatableListElement(Globals.MULTIPLEPRIMARY_COPY_PROBABLY_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("PROBABLY")),
                    new TranslatableListElement(Globals.MULTIPLEPRIMARY_COPY_MUST_STRING, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("MUST"))}));
        variableTypeComboBox.setModel(new DefaultComboBoxModel(
                new TranslatableListElement[]{
                    new TranslatableListElement(Globals.VARIABLE_TYPE_ALPHA_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("ALPHABETIC")),
                    new TranslatableListElement(Globals.VARIABLE_TYPE_ASIAN_TEXT_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("ASIAN TEXT")),
                    new TranslatableListElement(Globals.VARIABLE_TYPE_DATE_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("DATE")),
                    new TranslatableListElement(Globals.VARIABLE_TYPE_DICTIONARY_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("DICTIONARY")),
                    new TranslatableListElement(Globals.VARIABLE_TYPE_NUMBER_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("NUMBER")),
                    new TranslatableListElement(Globals.VARIABLE_TYPE_TEXT_AREA_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("TEXT AREA"))}));
        tableComboBox.setModel(new DefaultComboBoxModel(
                new TranslatableListElement[]{
                    new TranslatableListElement(Globals.PATIENT_TABLE_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("PATIENT")),
                    new TranslatableListElement(Globals.TUMOUR_TABLE_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("TUMOUR")),
                    new TranslatableListElement(Globals.SOURCE_TABLE_NAME, java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("SOURCE"))
                }));
        dictionaryComboBox.setModel(new DefaultComboBoxModel(new DatabaseDictionaryListElement[]{
                    new DatabaseDictionaryListElement()
                }));
        groupComboBox.setModel(new DefaultComboBoxModel(new DatabaseGroupsListElement[]{
                    new DatabaseGroupsListElement(java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("DEFAULT"), 1, 1)
                }));
    }

    public void setGroups(DatabaseGroupsListElement[] groups) {
        groupComboBox.setModel(new DefaultComboBoxModel(groups));
    }

    public void setDictionaries(DatabaseDictionaryListElement[] dictionaries) {
        dictionaryComboBox.setModel(new DefaultComboBoxModel(dictionaries));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fullNameLabel = new javax.swing.JLabel();
        fullNameTextField = new javax.swing.JTextField();
        shortNameLabel = new javax.swing.JLabel();
        englishNameLabel = new javax.swing.JLabel();
        groupLabel = new javax.swing.JLabel();
        groupComboBox = new javax.swing.JComboBox();
        fillInStatusLabel = new javax.swing.JLabel();
        fillInStatusComboBox = new javax.swing.JComboBox();
        shortNameTextField = new javax.swing.JTextField();
        englishNameTextField = new javax.swing.JTextField();
        mpCopyLabel = new javax.swing.JLabel();
        multiplePrimaryComboBox = new javax.swing.JComboBox();
        variableTypeLabel = new javax.swing.JLabel();
        variableTypeComboBox = new javax.swing.JComboBox();
        variableLengthLabel = new javax.swing.JLabel();
        variableLengthTextField = new javax.swing.JTextField();
        dictionaryLabel = new javax.swing.JLabel();
        dictionaryComboBox = new javax.swing.JComboBox();
        tableLabel = new javax.swing.JLabel();
        tableComboBox = new javax.swing.JComboBox();
        standardVariableNameLabel = new javax.swing.JLabel();
        standardVariableNameComboBox = new javax.swing.JComboBox();
        unknownCodeLabel = new javax.swing.JLabel();
        unknownCodeTextField = new javax.swing.JTextField();

        setMinimumSize(new java.awt.Dimension(311, 312));
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(DatabaseVariableEditor.class);
        fullNameLabel.setText(resourceMap.getString("fullNameLabel.text")); // NOI18N
        fullNameLabel.setName("fullNameLabel"); // NOI18N

        fullNameTextField.setText(resourceMap.getString("fullNameTextField.text")); // NOI18N
        fullNameTextField.setName("fullNameTextField"); // NOI18N
        fullNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fullNameTextFieldFocusLost(evt);
            }
        });

        shortNameLabel.setText(resourceMap.getString("shortNameLabel.text")); // NOI18N
        shortNameLabel.setName("shortNameLabel"); // NOI18N

        englishNameLabel.setText(resourceMap.getString("englishNameLabel.text")); // NOI18N
        englishNameLabel.setName("englishNameLabel"); // NOI18N

        groupLabel.setText(resourceMap.getString("groupLabel.text")); // NOI18N
        groupLabel.setName("groupLabel"); // NOI18N

        groupComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        groupComboBox.setName("groupComboBox"); // NOI18N

        fillInStatusLabel.setText(resourceMap.getString("fillInStatusLabel.text")); // NOI18N
        fillInStatusLabel.setName("fillInStatusLabel"); // NOI18N

        fillInStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        fillInStatusComboBox.setName("fillInStatusComboBox"); // NOI18N

        shortNameTextField.setText(resourceMap.getString("shortNameTextField.text")); // NOI18N
        shortNameTextField.setName("shortNameTextField"); // NOI18N

        englishNameTextField.setText(resourceMap.getString("englishNameTextField.text")); // NOI18N
        englishNameTextField.setName("englishNameTextField"); // NOI18N

        mpCopyLabel.setText(resourceMap.getString("mpCopyLabel.text")); // NOI18N
        mpCopyLabel.setName("mpCopyLabel"); // NOI18N

        multiplePrimaryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        multiplePrimaryComboBox.setName("multiplePrimaryComboBox"); // NOI18N

        variableTypeLabel.setText(resourceMap.getString("variableTypeLabel.text")); // NOI18N
        variableTypeLabel.setName("variableTypeLabel"); // NOI18N

        variableTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(DatabaseVariableEditor.class, this);
        variableTypeComboBox.setAction(actionMap.get("variableTypeChosenAction")); // NOI18N
        variableTypeComboBox.setName("variableTypeComboBox"); // NOI18N

        variableLengthLabel.setText(resourceMap.getString("variableLengthLabel.text")); // NOI18N
        variableLengthLabel.setName("variableLengthLabel"); // NOI18N

        variableLengthTextField.setText(resourceMap.getString("variableLengthTextField.text")); // NOI18N
        variableLengthTextField.setName("variableLengthTextField"); // NOI18N
        variableLengthTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                variableLengthTextFieldFocusLost(evt);
            }
        });

        dictionaryLabel.setText(resourceMap.getString("dictionaryLabel.text")); // NOI18N
        dictionaryLabel.setEnabled(false);
        dictionaryLabel.setName("dictionaryLabel"); // NOI18N

        dictionaryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        dictionaryComboBox.setAction(actionMap.get("dictionaryChosen")); // NOI18N
        dictionaryComboBox.setName("dictionaryComboBox"); // NOI18N

        tableLabel.setText(resourceMap.getString("tableLabel.text")); // NOI18N
        tableLabel.setName("tableLabel"); // NOI18N

        tableComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        tableComboBox.setName("tableComboBox"); // NOI18N

        standardVariableNameLabel.setText(resourceMap.getString("standardVariableNameLabel.text")); // NOI18N
        standardVariableNameLabel.setName("standardVariableNameLabel"); // NOI18N

        standardVariableNameComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        standardVariableNameComboBox.setName("standardVariableNameComboBox"); // NOI18N

        unknownCodeLabel.setText(resourceMap.getString("unknownCodeLabel.text")); // NOI18N
        unknownCodeLabel.setName("unknownCodeLabel"); // NOI18N

        unknownCodeTextField.setText(resourceMap.getString("unknownCodeTextField.text")); // NOI18N
        unknownCodeTextField.setName("unknownCodeTextField"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(variableLengthLabel)
                            .addComponent(variableTypeLabel)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(38, 38, 38)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(englishNameLabel)
                                        .addComponent(groupLabel)
                                        .addComponent(fillInStatusLabel)
                                        .addComponent(mpCopyLabel)))
                                .addComponent(shortNameLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(fullNameLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(standardVariableNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(standardVariableNameComboBox, 0, 152, Short.MAX_VALUE)
                            .addComponent(fullNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(shortNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(englishNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(multiplePrimaryComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 152, Short.MAX_VALUE)
                            .addComponent(variableTypeComboBox, 0, 152, Short.MAX_VALUE)
                            .addComponent(variableLengthTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
                            .addComponent(groupComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 152, Short.MAX_VALUE)
                            .addComponent(fillInStatusComboBox, javax.swing.GroupLayout.Alignment.TRAILING, 0, 152, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(dictionaryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dictionaryComboBox, 0, 152, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(unknownCodeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(unknownCodeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(tableLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tableComboBox, 0, 152, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fullNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fullNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(shortNameLabel)
                    .addComponent(shortNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(englishNameLabel)
                    .addComponent(englishNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(standardVariableNameComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(standardVariableNameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(groupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(groupLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fillInStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fillInStatusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mpCopyLabel)
                    .addComponent(multiplePrimaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variableTypeLabel)
                    .addComponent(variableTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(variableLengthLabel)
                    .addComponent(variableLengthTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dictionaryLabel)
                    .addComponent(dictionaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(unknownCodeLabel)
                    .addComponent(unknownCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tableLabel)
                    .addComponent(tableComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void fullNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fullNameTextFieldFocusLost
        if (englishNameTextField.getText().trim().length() == 0) {
            englishNameTextField.setText(fullNameTextField.getText());
        }
    }//GEN-LAST:event_fullNameTextFieldFocusLost

    private void variableLengthTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_variableLengthTextFieldFocusLost
        try {
            Integer.parseInt(variableLengthTextField.getText());

            int length = Integer.parseInt(variableLengthTextField.getText());
            if (length <= 0) {
                JOptionPane.showInternalMessageDialog(this,
                        "Variable length should greater than 0.");
            }
 
        } catch (NumberFormatException nfe) {
            JOptionPane.showInternalMessageDialog(this, nfe);
        }
    }//GEN-LAST:event_variableLengthTextFieldFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox dictionaryComboBox;
    private javax.swing.JLabel dictionaryLabel;
    private javax.swing.JLabel englishNameLabel;
    private javax.swing.JTextField englishNameTextField;
    private javax.swing.JComboBox fillInStatusComboBox;
    private javax.swing.JLabel fillInStatusLabel;
    private javax.swing.JLabel fullNameLabel;
    private javax.swing.JTextField fullNameTextField;
    private javax.swing.JComboBox groupComboBox;
    private javax.swing.JLabel groupLabel;
    private javax.swing.JLabel mpCopyLabel;
    private javax.swing.JComboBox multiplePrimaryComboBox;
    private javax.swing.JLabel shortNameLabel;
    private javax.swing.JTextField shortNameTextField;
    private javax.swing.JComboBox standardVariableNameComboBox;
    private javax.swing.JLabel standardVariableNameLabel;
    private javax.swing.JComboBox tableComboBox;
    private javax.swing.JLabel tableLabel;
    private javax.swing.JLabel unknownCodeLabel;
    private javax.swing.JTextField unknownCodeTextField;
    private javax.swing.JLabel variableLengthLabel;
    private javax.swing.JTextField variableLengthTextField;
    private javax.swing.JComboBox variableTypeComboBox;
    private javax.swing.JLabel variableTypeLabel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the databaseVariablesListElement
     */
    public DatabaseVariablesListElement refreshDatabaseVariablesListElement() throws DatabaseVariablesListException {
        TranslatableListElement tleTable = (TranslatableListElement) tableComboBox.getSelectedItem();
        databaseVariablesListElement.setTable(tleTable.getOriginalName());

        TranslatableListElement tleType = (TranslatableListElement) variableTypeComboBox.getSelectedItem();
        databaseVariablesListElement.setVariableType(tleType.getOriginalName());

        databaseVariablesListElement.setShortName(shortNameTextField.getText());

        // databaseVariablesListElement = new DatabaseVariablesListElement(tleTable.getOriginalName(), id, shortNameTextField.getText(), tleType.getOriginalName());

        if (tleType.equals(new TranslatableListElement(Globals.VARIABLE_TYPE_DICTIONARY_NAME, Globals.VARIABLE_TYPE_DICTIONARY_NAME))) {
            DatabaseDictionaryListElement dictionary = (DatabaseDictionaryListElement) dictionaryComboBox.getSelectedItem();
            databaseVariablesListElement.setDictionary(dictionary);
        }
        if (standardVariableNameComboBox.getSelectedIndex() > 0) {
            databaseVariablesListElement.setStandardVariableName(standardVariableNameComboBox.getSelectedItem().toString());
        }
        databaseVariablesListElement.setFullName(fullNameTextField.getText());
        databaseVariablesListElement.setEnglishName(englishNameTextField.getText());

        if (standardVariableNameComboBox.getSelectedItem() != null) {
            databaseVariablesListElement.setStandardVariableName(standardVariableNameComboBox.getSelectedItem().toString());
        }

        // X pos
        // databaseVariablesListElement.setXPos(0);
        // Y pos
        // databaseVariablesListElement.setYPos(id*100);

        try {
            int length = Integer.parseInt(variableLengthTextField.getText());
            if (!Globals.VARIABLE_TYPE_NUMBER_NAME.equalsIgnoreCase(databaseVariablesListElement.getVariableType()) && length<=0){
                throw new DatabaseVariablesListException(databaseVariablesListElement,java.util.ResourceBundle.getBundle("canreg/client/gui/management/resources/DatabaseVariableEditor").getString("VARIABLE_LENGHT_LESS_THAN_0."));
            }
            databaseVariablesListElement.setVariableLength(length);
        } catch (NumberFormatException nfe) {
            throw nfe;
        }

        TranslatableListElement tleFIS = (TranslatableListElement) fillInStatusComboBox.getSelectedItem();
        databaseVariablesListElement.setFillInStatus(tleFIS.getOriginalName());

        DatabaseGroupsListElement dbgle = (DatabaseGroupsListElement) groupComboBox.getSelectedItem();
        databaseVariablesListElement.setGroup(dbgle);

        if (unknownCodeTextField.getText().length() > 0) {
            databaseVariablesListElement.setUnknownCode(unknownCodeTextField.getText());
        } else {
            databaseVariablesListElement.setUnknownCode(null);
        }
        TranslatableListElement tleMPC = (TranslatableListElement) multiplePrimaryComboBox.getSelectedItem();
        databaseVariablesListElement.setMultiplePrimaryCopy(tleMPC.getOriginalName());

        return databaseVariablesListElement;
    }

    /**
     * @param databaseVariablesListElement the databaseVariablesListElement to set
     */
    public void setDatabaseVariablesListElement(DatabaseVariablesListElement databaseVariablesListElement) {
        this.databaseVariablesListElement = databaseVariablesListElement;

        int dictID = databaseVariablesListElement.getDictionaryID();
        // find the dictionary
        for (int i = 0; i < dictionaryComboBox.getItemCount(); i++) {
            DatabaseDictionaryListElement ddle = (DatabaseDictionaryListElement) dictionaryComboBox.getItemAt(i);
            if (ddle.getDictionaryID() == dictID) {
                dictionaryComboBox.setSelectedIndex(i);
                dictionaryChosen();
            }
        }

        int groupID = databaseVariablesListElement.getGroupID();
        // find the dictionary
        for (int i = 0; i < groupComboBox.getItemCount(); i++) {
            DatabaseGroupsListElement dgle = (DatabaseGroupsListElement) groupComboBox.getItemAt(i);
            if (dgle.getGroupIndex() == groupID) {
                groupComboBox.setSelectedIndex(i);
            }
        }

        String tableName = databaseVariablesListElement.getTable();
        tableComboBox.setSelectedItem(new TranslatableListElement(tableName, tableName));

        // String multiplePrimaryCopy =
        String fillInStatus = databaseVariablesListElement.getFillInStatus();
        fillInStatusComboBox.setSelectedItem(new TranslatableListElement(fillInStatus, fillInStatus));

        String standardVariableName = databaseVariablesListElement.getStandardVariableName();
        standardVariableNameComboBox.setSelectedItem(standardVariableName);

        String variableType = databaseVariablesListElement.getVariableType();
        variableTypeComboBox.setSelectedItem(new TranslatableListElement(variableType, variableType));

        String mpCopy = databaseVariablesListElement.getMultiplePrimaryCopy();
        multiplePrimaryComboBox.setSelectedItem(new TranslatableListElement(mpCopy, mpCopy));

        fullNameTextField.setText(databaseVariablesListElement.getFullName());
        shortNameTextField.setText(databaseVariablesListElement.getShortName());
        englishNameTextField.setText(databaseVariablesListElement.getEnglishName());
        fullNameTextField.setText(databaseVariablesListElement.getFullName());
        fullNameTextField.setText(databaseVariablesListElement.getFullName());
        variableLengthTextField.setText(databaseVariablesListElement.getVariableLength() + "");

        variableTypeChosenAction();
    }

    @Action
    public void variableTypeChosenAction() {
        boolean enableDictionarySelection = false;
        boolean enableVariableLengthChange = true;
        if (variableTypeComboBox.getSelectedItem().equals(new TranslatableListElement(Globals.VARIABLE_TYPE_DICTIONARY_NAME, Globals.VARIABLE_TYPE_DICTIONARY_NAME))) {
            enableDictionarySelection = true;
            enableVariableLengthChange = false;
        } else if (variableTypeComboBox.getSelectedItem().equals(new TranslatableListElement(Globals.VARIABLE_TYPE_DATE_NAME, Globals.VARIABLE_TYPE_DATE_NAME))) {
            // if this is date we have to set the length to 8
            enableVariableLengthChange = false;
            enableDictionarySelection = false;
            variableLengthTextField.setText(Globals.DATE_FORMAT_STRING.length() + "");
        }
        dictionaryComboBox.setEnabled(enableDictionarySelection);
        dictionaryLabel.setEnabled(enableDictionarySelection);
        unknownCodeLabel.setEnabled(enableVariableLengthChange);
        unknownCodeTextField.setEnabled(enableVariableLengthChange);
        variableLengthLabel.setEnabled(enableVariableLengthChange);
        variableLengthTextField.setEnabled(enableVariableLengthChange);
    }

    @Action
    public void dictionaryChosen() {
        DatabaseDictionaryListElement ddle = (DatabaseDictionaryListElement) dictionaryComboBox.getSelectedItem();
        if (ddle != null) {
            variableLengthTextField.setText(ddle.getFullDictionaryCodeLength() + "");
            if (ddle.getUnkownCode() != null) {
                unknownCodeTextField.setText(ddle.getUnkownCode());
            }
        }
    }
}
