/*
 * RecordEditor.java
 *
 * Created on 16 July 2008, 14:46
 */
package canreg.client.gui.dataentry;

import canreg.client.CanRegClientApp;
import canreg.client.gui.CanRegClientView;
import canreg.client.gui.tools.PrintUtilities;
import canreg.common.DatabaseVariablesListElement;
import canreg.common.GlobalToolBox;
import canreg.common.Globals;
import canreg.common.conversions.ConversionResult;
import canreg.common.conversions.Converter;
import canreg.common.qualitycontrol.CheckResult;
import canreg.common.qualitycontrol.DefaultMultiplePrimaryTester;
import canreg.common.qualitycontrol.MultiplePrimaryTesterInterface;
import canreg.server.database.DatabaseRecord;
import canreg.server.database.Dictionary;
import canreg.server.database.Patient;
import canreg.server.database.Tumour;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import org.jdesktop.application.Action;
import org.w3c.dom.Document;

/**
 *
 * @author  ervikm
 */
public class RecordEditor extends javax.swing.JInternalFrame implements ActionListener {

    public static final String CHANGED = "changed";
    public static final String CHECKS = "checks";
    public static final String DELETE = "delete";
    public static final String SAVE = "save";
    public static final String RUN_MP = "runMP";
    public static final String OBSOLETE = "obsolete";
    public static final String CHANGE_PATIENT_RECORD = "changePatientRecord";
    public static final String CALC_AGE = "calcAge";
    private Document doc;
    private Map<Integer, Dictionary> dictionary;
    private LinkedList<DatabaseRecord> patientRecords;
    private TreeMap<Object, RecordEditorPanel> patientRecordsMap;
    private LinkedList<DatabaseRecord> tumourRecords;
    private boolean changesDone = false;
    private JDesktopPane desktopPane;
    private GlobalToolBox globalToolBox;
    private boolean titleSet = false;
    String tumourObsoleteVariableName = null;
    String patientObsoleteVariableName = null;

    /** Creates new form RecordEditor
     * @param desktopPane 
     */
    public RecordEditor(JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;

        initComponents();
        patientRecords = new LinkedList<DatabaseRecord>();
        tumourRecords = new LinkedList<DatabaseRecord>();
        patientRecordsMap = new TreeMap<Object, RecordEditorPanel>();

        addInternalFrameListener(new InternalFrameAdapter() {

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                int option = JOptionPane.NO_OPTION;
                // Go through all panels and ask if any changes has been done
                boolean changesDone = false;
                for (Component component : patientTabbedPane.getComponents()) {
                    RecordEditorPanel panel = (RecordEditorPanel) component;
                    changesDone = changesDone || panel.isSaveNeeded();
                }
                for (Component component : tumourTabbedPane.getComponents()) {
                    RecordEditorPanel panel = (RecordEditorPanel) component;
                    changesDone = changesDone || panel.isSaveNeeded();
                }
                if (changesDone) { // TODO: Implement "If changes has been made" check...
                    option = JOptionPane.showConfirmDialog(null, "Really close?\nChanges made will be lost.");
                    if (option == JOptionPane.YES_OPTION) {
                        close();
                    }
                } else {
                    close();
                }
            }
        });

        // Add a listener for changing the active tab
        ChangeListener tabbedPaneChangeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                JTabbedPane pane = (JTabbedPane) e.getSource();
                RecordEditorPanel rep = (RecordEditorPanel) pane.getSelectedComponent();
                if (rep != null) {
                    setActiveRecord(rep);
                }
            }
        };
        // And add the listener to the tabbedPane
        patientTabbedPane.addChangeListener(tabbedPaneChangeListener);
        tumourTabbedPane.addChangeListener(tabbedPaneChangeListener);

    }

    private void addToPatientMap(RecordEditorPanel recordEditorPanel, DatabaseRecord dbr) {
        Object regno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString()).getDatabaseVariableName());
        if (regno != null) {
            patientRecordsMap.put(regno, recordEditorPanel);
        }
    }

    private void changesDone() {
        changesDone = true;
    }

    private void setActiveRecord(RecordEditorPanel rep) {
        DatabaseRecord dbr = rep.getDatabaseRecord();
        if (dbr != null && dbr instanceof Tumour) {
            Object patientRecordID = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordIDTumourTable.toString()).getDatabaseVariableName());
            Component comp = patientRecordsMap.get(patientRecordID);
            if (comp != null) {
                patientTabbedPane.setSelectedComponent(comp);
            }
        }
    }

    /**
     * 
     * @param doc
     */
    public void setGlobalToolBox(GlobalToolBox globalToolBox) {
        this.globalToolBox = globalToolBox;
        this.doc = globalToolBox.getDocument();

        patientObsoleteVariableName = globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.ObsoleteFlagPatientTable.toString()).getDatabaseVariableName();
        tumourObsoleteVariableName = globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.ObsoleteFlagTumourTable.toString()).getDatabaseVariableName();

    }

    /**
     * 
     */
    public void closing() {
    }

    /**
     * 
     */
    public void close() {
        this.dispose();
    }

    /**
     * 
     * @param dictionary
     */
    public void setDictionary(Map<Integer, Dictionary> dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * 
     * @param dbr
     */
    public void addRecord(DatabaseRecord dbr) {
        RecordEditorPanel rePanel = new RecordEditorPanel(this);
        rePanel.setDictionary(dictionary);
        rePanel.setDocument(doc);
        rePanel.setRecord(dbr);
        rePanel.repaint();
        if (dbr instanceof Patient) {
            patientRecords.add(dbr);
            Object regno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString()).getDatabaseVariableName());
            String regnoString = "n/a";
            if (regno != null) {
                regnoString = regno.toString();
                if (regnoString.length() == 0) {
                    regnoString = "n/a";
                } else {
                    patientRecordsMap.put(regno, rePanel);
                }
            }
            Object patientObsoleteStatus = dbr.getVariable(patientObsoleteVariableName);
            if (patientObsoleteStatus != null && patientObsoleteStatus.equals(1)) {
                regnoString += " (obsolete)";
            }
            patientTabbedPane.addTab(dbr.toString() + ": " + regnoString + " ", rePanel);
            if (!titleSet) {
                Object patno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientID.toString()).getDatabaseVariableName());
                String patnoString = "n/a";
                if (patno != null) {
                    patnoString = patno.toString();
                    if (patnoString.length() > 0) {
                        this.setTitle("Patient ID:" + patnoString);
                        titleSet = true;
                    }
                }
            }
        } else if (dbr instanceof Tumour) {
            tumourRecords.add(dbr);
            Object regno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.TumourID.toString()).getDatabaseVariableName());
            String regnoString = "n/a";
            if (regno != null) {
                regnoString = regno.toString();
                if (regnoString.length() == 0) {
                    regnoString = "n/a";
                }
            }
            Object tumourObsoleteStatus = dbr.getVariable(tumourObsoleteVariableName);
            if (tumourObsoleteStatus != null && tumourObsoleteStatus.equals(1)) {
                regnoString += " (obsolete)";
            }
            tumourTabbedPane.addTab(dbr.toString() + ": " + regnoString + " ", rePanel);
        }
        refreshShowObsolete();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        recordSplitPane = new javax.swing.JSplitPane();
        patientTabbedPane = new javax.swing.JTabbedPane();
        tumourTabbedPane = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        addTumourRecordButton = new javax.swing.JButton();
        addpatientRecordButton = new javax.swing.JButton();
        saveAllButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        showObsoleteRecordsCheckBox = new javax.swing.JCheckBox();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(RecordEditor.class);
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N

        recordSplitPane.setDividerSize(10);
        recordSplitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        recordSplitPane.setResizeWeight(0.5);
        recordSplitPane.setName("recordSplitPane"); // NOI18N
        recordSplitPane.setOneTouchExpandable(true);

        patientTabbedPane.setName("patientTabbedPane"); // NOI18N
        recordSplitPane.setTopComponent(patientTabbedPane);

        tumourTabbedPane.setName("tumourTabbedPane"); // NOI18N
        recordSplitPane.setRightComponent(tumourTabbedPane);

        jPanel1.setName("jPanel1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(RecordEditor.class, this);
        addTumourRecordButton.setAction(actionMap.get("addTumourAction")); // NOI18N
        addTumourRecordButton.setName("addTumourRecordButton"); // NOI18N
        addTumourRecordButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTumourRecordButtonActionPerformed(evt);
            }
        });

        addpatientRecordButton.setAction(actionMap.get("addPatientAction")); // NOI18N
        addpatientRecordButton.setName("addpatientRecordButton"); // NOI18N

        saveAllButton.setAction(actionMap.get("saveAllAction")); // NOI18N
        saveAllButton.setName("saveAllButton"); // NOI18N

        printButton.setAction(actionMap.get("printAction")); // NOI18N
        printButton.setText(resourceMap.getString("printButton.text")); // NOI18N
        printButton.setName("printButton"); // NOI18N

        showObsoleteRecordsCheckBox.setAction(actionMap.get("toggleShowObsoleteRecords")); // NOI18N
        showObsoleteRecordsCheckBox.setText(resourceMap.getString("showObsoleteRecordsCheckBox.text")); // NOI18N
        showObsoleteRecordsCheckBox.setName("showObsoleteRecordsCheckBox"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(addTumourRecordButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addpatientRecordButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showObsoleteRecordsCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                .addComponent(printButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveAllButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(addTumourRecordButton)
                .addComponent(addpatientRecordButton)
                .addComponent(saveAllButton)
                .addComponent(printButton)
                .addComponent(showObsoleteRecordsCheckBox))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(recordSplitPane, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 603, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addTumourRecordButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTumourRecordButtonActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_addTumourRecordButtonActionPerformed

    /**
     *
     */
    @Action
    public void addTumourAction() {
        Tumour tumour = new Tumour();
        populateNewRecord(tumour, doc);
        addRecord(tumour);
    }

    /**
     * 
     */
    @Action
    public void addPatientAction() {
        Patient patient = new Patient();
        populateNewRecord(patient, doc);
        addRecord(patient);
    }

    private DatabaseRecord populateNewRecord(DatabaseRecord dbr, Document doc) {
        String tableName = "";
        if (dbr instanceof Tumour) {
            tableName = Globals.TUMOUR_TABLE_NAME;
        } else if (dbr instanceof Patient) {
            tableName = Globals.PATIENT_TABLE_NAME;
        }
        RecordEditorPanel activePatientPanel = (RecordEditorPanel) patientTabbedPane.getSelectedComponent();
        Patient activePatient = (Patient) activePatientPanel.getDatabaseRecord();

        DatabaseVariablesListElement[] variablesInTable = canreg.common.Tools.getVariableListElements(doc, Globals.NAMESPACE, tableName);
        for (DatabaseVariablesListElement dbvle : variablesInTable) {
            String type = dbvle.getVariableType();
            if (type.equalsIgnoreCase("date") || type.equalsIgnoreCase("number")) {
                dbr.setVariable(dbvle.getDatabaseVariableName(), -1);
            } else {
                dbr.setVariable(dbvle.getDatabaseVariableName(), "");
            }
        }

        if (dbr instanceof Patient) {
            // copy all information
            for (String variableName : dbr.getVariableNames()) {
                if (variableName.equalsIgnoreCase(Globals.PATIENT_TABLE_RECORD_ID_VARIABLE_NAME) || variableName.equalsIgnoreCase(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString()).getDatabaseVariableName())) {
                } else {
                    dbr.setVariable(variableName, activePatient.getVariable(variableName));
                }
            }
            // except the database record ID and the patientTable ID
            dbr.setVariable(Globals.PATIENT_TABLE_RECORD_ID_VARIABLE_NAME, null);
            dbr.setVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString()).getDatabaseVariableName(), null);
        }
        return dbr;
    }

    /**
     * 
     */
    @Action
    public void saveAllAction() {
        LinkedList<RecordEditorPanel> reps = new LinkedList<RecordEditorPanel>();

        for (Component comp : patientTabbedPane.getComponents()) {
            reps.add((RecordEditorPanel) comp);
        }

        for (Component comp : tumourTabbedPane.getComponents()) {
            reps.add((RecordEditorPanel) comp);
        }

        for (RecordEditorPanel rep : reps) {
            try {
                DatabaseRecord dbr = saveRecord(rep.getDatabaseRecord());
                rep.refreshDatabaseRecord(dbr);
            } catch (SecurityException ex) {
                Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void refreshTitles(RecordEditorPanel recordEditorPanel, DatabaseRecord dbr) {

        if (dbr instanceof Patient) {
            // patientRecords.add(dbr);
            Object regno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString()).getDatabaseVariableName());
            String regnoString = "n/a";
            if (regno != null) {
                regnoString = regno.toString();
                if (regnoString.length() == 0) {
                    regnoString = "n/a";
                } else {
                    // patientRecordsMap.put(regno, recordEditorPanel);
                }
            }
            int index = 0;
            for (Component comp : patientTabbedPane.getComponents()) {
                if (comp.equals(recordEditorPanel)) {
                    patientTabbedPane.setTitleAt(index, dbr.toString() + ": " + regnoString);
                }
                index++;
            }
            if (!titleSet) {
                Object patno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientID.toString()).getDatabaseVariableName());
                String patnoString = "n/a";
                if (patno != null) {
                    patnoString = patno.toString();
                    if (patnoString.length() > 0) {
                        this.setTitle("Patient ID:" + patnoString);
                        titleSet = true;
                    }
                }
            }
        } else if (dbr instanceof Tumour) {
            // tumourRecords.add(dbr);
            Object regno = dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.TumourID.toString()).getDatabaseVariableName());
            String regnoString = "n/a";
            if (regno != null) {
                regnoString = regno.toString();
                if (regnoString.length() == 0) {
                    regnoString = "n/a";
                }
            }
            int index = 0;
            for (Component comp : tumourTabbedPane.getComponents()) {
                if (comp.equals(recordEditorPanel)) {
                    tumourTabbedPane.setTitleAt(index, dbr.toString() + ": " + regnoString);
                }
                index++;
            }
        }
    }

    /**
     * 
     */
    @Action
    public void printAction() {
        PrintUtilities.printComponent(patientTabbedPane.getSelectedComponent());
        PrintUtilities.printComponent(tumourTabbedPane.getSelectedComponent());
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addTumourRecordButton;
    private javax.swing.JButton addpatientRecordButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane patientTabbedPane;
    private javax.swing.JButton printButton;
    private javax.swing.JSplitPane recordSplitPane;
    private javax.swing.JButton saveAllButton;
    private javax.swing.JCheckBox showObsoleteRecordsCheckBox;
    private javax.swing.JTabbedPane tumourTabbedPane;
    // End of variables declaration//GEN-END:variables

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();


        if (source instanceof RecordEditorPanel) {
            if (e.getActionCommand().equalsIgnoreCase(CHANGED)) {
                changesDone();
            } else if (e.getActionCommand().equalsIgnoreCase(DELETE)) {
                int option = JOptionPane.NO_OPTION;
                option = JOptionPane.showConfirmDialog(null, "Permanently delete record?");
                if (option == JOptionPane.YES_OPTION) {
                    boolean success = false;
                    RecordEditorPanel recordEditorPanel = (RecordEditorPanel) source;
                    DatabaseRecord record = recordEditorPanel.getDatabaseRecord();
                    int id = -1;
                    String tableName = null;
                    JTabbedPane tabbedPane = null;
                    if (record instanceof Patient) {
                        Object idObject = record.getVariable(Globals.PATIENT_TABLE_RECORD_ID_VARIABLE_NAME);
                        if (idObject != null) {
                            id = (Integer) idObject;
                        }
                        tableName = Globals.PATIENT_TABLE_NAME;
                        tabbedPane = patientTabbedPane;
                    } else if (record instanceof Tumour) {
                        Object idObject = record.getVariable(Globals.TUMOUR_TABLE_RECORD_ID_VARIABLE_NAME);
                        if (idObject != null) {
                            id = (Integer) idObject;
                        }
                        tableName = Globals.TUMOUR_TABLE_NAME;
                        tabbedPane = tumourTabbedPane;
                    }
                    if (id > 0) {
                        try {
                            success = canreg.client.CanRegClientApp.getApplication().deleteRecord(id, tableName);
                        } catch (SecurityException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RemoteException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (success) {
                        tabbedPane.remove(recordEditorPanel);
                        JOptionPane.showInternalMessageDialog(this, "Record deleted.");
                    } else {
                        JOptionPane.showInternalMessageDialog(this, "Record not deleted.\nError occured...");
                    }
                }
            } else if (e.getActionCommand().equalsIgnoreCase(CHECKS)) {
                RecordEditorPanel recordEditorPanel = (RecordEditorPanel) source;
                RecordEditorPanel tumourRecordEditorPanel;
                RecordEditorPanel patientRecordEditorPanel;
                DatabaseRecord record = recordEditorPanel.getDatabaseRecord();
                CheckResult.ResultCode worstResultCodeFound = CheckResult.ResultCode.OK;
                String message = "";
                Patient patient;
                Tumour tumour;
                if (record instanceof Patient) {
                    patient = (Patient) record;
                    patientRecordEditorPanel = recordEditorPanel;
                    tumourRecordEditorPanel = (RecordEditorPanel) tumourTabbedPane.getSelectedComponent();
                    tumour = (Tumour) tumourRecordEditorPanel.getDatabaseRecord();
                } else {
                    tumour = (Tumour) record;
                    tumourRecordEditorPanel = recordEditorPanel;
                    patientRecordEditorPanel = (RecordEditorPanel) patientTabbedPane.getSelectedComponent();
                    patient = (Patient) patientRecordEditorPanel.getDatabaseRecord();
                }

                EditChecksInternalFrame editChecksInternalFrame = new EditChecksInternalFrame();

                // Check to see if all mandatory variables are there
                boolean allPresent = patientRecordEditorPanel.areAllVariablesPresent();
                allPresent = allPresent & tumourRecordEditorPanel.areAllVariablesPresent();

                if (!allPresent) {
                    editChecksInternalFrame.setMandatoryVariablesTextAreaText("Mandatory variables missing.");
                    worstResultCodeFound = CheckResult.ResultCode.Missing;
                    message += "Not performed.";
                } else {
                    editChecksInternalFrame.setMandatoryVariablesTextAreaText("All mandatory variables present.");
                    // Run the checks on the data
                    LinkedList<CheckResult> checkResults = canreg.client.CanRegClientApp.getApplication().performChecks(patient, tumour);

                    Map<Globals.StandardVariableNames, CheckResult.ResultCode> mapOfVariablesAndWorstResultCodes = new TreeMap<Globals.StandardVariableNames, CheckResult.ResultCode>();
                    worstResultCodeFound = CheckResult.ResultCode.OK;
                    for (CheckResult result : checkResults) {
                        if (result.getResultCode() != CheckResult.ResultCode.OK) {
                            message += result + "\n";
                            worstResultCodeFound = CheckResult.decideWorstResultCode(result.getResultCode(), worstResultCodeFound);
                            for (Globals.StandardVariableNames standardVariableName : result.getVariablesInvolved()) {
                                CheckResult.ResultCode worstResultCodeFoundForThisVariable = mapOfVariablesAndWorstResultCodes.get(standardVariableName);
                                if (worstResultCodeFoundForThisVariable == null) {
                                    mapOfVariablesAndWorstResultCodes.put(standardVariableName, result.getResultCode());
                                } else if (CheckResult.compareResultSets(result.getResultCode(), worstResultCodeFoundForThisVariable) > 0) {
                                    mapOfVariablesAndWorstResultCodes.put(standardVariableName, result.getResultCode());
                                }
                            }
                        }
                        Logger.getLogger(RecordEditor.class.getName()).log(Level.INFO, result.toString());
                    }

                    if (worstResultCodeFound != CheckResult.ResultCode.Invalid && worstResultCodeFound != CheckResult.ResultCode.Missing) {
                        // If no errors were found we generate ICD10 code
                        ConversionResult[] conversionResult = canreg.client.CanRegClientApp.getApplication().performConversions(Converter.ConversionName.ICDO3toICD10, patient, tumour);
                        if (conversionResult != null) {
                            if (conversionResult[0].getResultCode() != ConversionResult.ResultCode.Invalid) {
                                editChecksInternalFrame.setICD10TextFieldText(conversionResult[0].getValue() + "");
                                DatabaseVariablesListElement ICD10databaseVariablesElement = globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.ICD10.toString());
                                if (ICD10databaseVariablesElement != null) {
                                    tumour.setVariable(ICD10databaseVariablesElement.getDatabaseVariableName(), conversionResult[0].getValue());
                                }
                            }
                        }
                    }

                    tumourRecordEditorPanel.refreshDatabaseRecord(tumour);

                    if (worstResultCodeFound == CheckResult.ResultCode.OK) {
                        message += "Cross-check conclusion: Valid";
                    } else {
                        // set the various variable panels to respective warnings
                        for (Globals.StandardVariableNames standardVariableName : mapOfVariablesAndWorstResultCodes.keySet()) {
                            DatabaseVariablesListElement dbvle = globalToolBox.translateStandardVariableNameToDatabaseListElement(standardVariableName.toString());

                            if (dbvle.getDatabaseTableName().equalsIgnoreCase(Globals.TUMOUR_TABLE_NAME)) {
                                tumourRecordEditorPanel.setResultCodeOfVariable(dbvle.getDatabaseVariableName(), mapOfVariablesAndWorstResultCodes.get(standardVariableName));
                            } else if (dbvle.getDatabaseTableName().equalsIgnoreCase(Globals.PATIENT_TABLE_NAME)) {
                                patientRecordEditorPanel.setResultCodeOfVariable(dbvle.getDatabaseVariableName(), mapOfVariablesAndWorstResultCodes.get(standardVariableName));
                            }
                        }
                    }
                }
                tumourRecordEditorPanel.setChecksResultCode(worstResultCodeFound);

                editChecksInternalFrame.setCrossChecksTextAreaText(message);
                editChecksInternalFrame.setResultTextFieldText(worstResultCodeFound.toString());

                CanRegClientView.showAndCenterInternalFrame(desktopPane, editChecksInternalFrame);
            } else if (e.getActionCommand().equalsIgnoreCase(SAVE)) {
                RecordEditorPanel recordEditorPanel = (RecordEditorPanel) source;
                DatabaseRecord databaseRecord = recordEditorPanel.getDatabaseRecord();
                if (databaseRecord instanceof Tumour) {
                    // set the patient id to the active patient number
                    RecordEditorPanel patientRecordEditorPanel = (RecordEditorPanel) patientTabbedPane.getSelectedComponent();
                    DatabaseRecord patientDatabaseRecord = patientRecordEditorPanel.getDatabaseRecord();
                    associateTumourRecordToPatientRecord(databaseRecord, patientDatabaseRecord);
                }
                try {
                    DatabaseRecord dbr = saveRecord(databaseRecord);
                    recordEditorPanel.refreshDatabaseRecord(dbr);
                    refreshTitles(recordEditorPanel, dbr);
                    if (dbr instanceof Patient) {
                        addToPatientMap(recordEditorPanel, dbr);
                    }
                } catch (SecurityException ex) {
                    Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (RemoteException ex) {
                    Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (e.getActionCommand().equalsIgnoreCase(CHANGE_PATIENT_RECORD)) {
                RecordEditorPanel tumourRecordEditorPanel = (RecordEditorPanel) source;
                Tumour tumourDatabaseRecord = (Tumour) tumourRecordEditorPanel.getDatabaseRecord();
                String requestedPatientRecordID = JOptionPane.showInputDialog(null, "Please enter Patient record ID:", "Move tumour to which Patient Record?", JOptionPane.QUESTION_MESSAGE);
                if (requestedPatientRecordID != null) {
                    // First see if it is one of the records shown
                    RecordEditorPanel patientRecordEditorPanel = patientRecordsMap.get(requestedPatientRecordID);
                    Patient patientDatabaseRecord = null;
                    if (patientRecordEditorPanel != null) {
                        patientDatabaseRecord = (Patient) patientRecordEditorPanel.getDatabaseRecord();
                    } else {
                        try {
                            patientDatabaseRecord = CanRegClientApp.getApplication().getPatientRecord(requestedPatientRecordID);
                        } catch (SQLException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RemoteException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SecurityException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (Exception ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (patientDatabaseRecord != null) {
                        associateTumourRecordToPatientRecord(tumourDatabaseRecord, patientDatabaseRecord);
                        try {
                            saveRecord(tumourDatabaseRecord);
                            tumourTabbedPane.remove(tumourRecordEditorPanel);
                            JOptionPane.showInternalMessageDialog(this, "Record moved.");
                        } catch (SecurityException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RemoteException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(RecordEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        tumourRecordEditorPanel.refreshDatabaseRecord(tumourDatabaseRecord);
                    } else {
                        JOptionPane.showInternalMessageDialog(this, "No such patient record.", "Failed", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (e.getActionCommand().equalsIgnoreCase(OBSOLETE)) {
                RecordEditorPanel recordEditorPanel = (RecordEditorPanel) source;
                int option = JOptionPane.NO_OPTION;
                option = JOptionPane.showConfirmDialog(null, "Really change obsolete-status?");
                boolean toggle = (option == JOptionPane.YES_OPTION);
                recordEditorPanel.toggleObsolete(toggle);
                if (toggle) {
                    refreshShowObsolete();
                }
            } else if (e.getActionCommand().equalsIgnoreCase(RUN_MP)) {
                RecordEditorPanel recordEditorPanel = (RecordEditorPanel) source;
                DatabaseRecord databaseRecordA = recordEditorPanel.getDatabaseRecord();
                String topographyA = (String) databaseRecordA.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.Topography.toString()).getDatabaseVariableName());
                String morphologyA = (String) databaseRecordA.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.Morphology.toString()).getDatabaseVariableName());

                MultiplePrimaryTesterInterface multiplePrimaryTester = new DefaultMultiplePrimaryTester();

                for (Component tumourPanelComponent : tumourTabbedPane.getComponents()) {
                    RecordEditorPanel tumourPanel = (RecordEditorPanel) tumourPanelComponent;
                    if (!source.equals(tumourPanel)) {
                        DatabaseRecord dbr = tumourPanel.getDatabaseRecord();
                        String topographyB = (String) dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.Topography.toString()).getDatabaseVariableName());
                        String morphologyB = (String) dbr.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.Morphology.toString()).getDatabaseVariableName());
                        int result = multiplePrimaryTester.multiplePrimaryTest(topographyA, morphologyA, topographyB, morphologyB);
                        JOptionPane.showInternalMessageDialog(this, "Result: " + multiplePrimaryTester.mptCodes[result], "Result", JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (e.getActionCommand().equalsIgnoreCase(CALC_AGE)) {
                // this should be called at any time any of the fields birth date or incidence date gets changed
                RecordEditorPanel recordEditorPanel = (RecordEditorPanel) source;
                DatabaseRecord sourceDatabaseRecord = recordEditorPanel.getDatabaseRecord();
                DatabaseRecord patientDatabaseRecord;
                // TODO: implement calculate age
                if (sourceDatabaseRecord instanceof Tumour) {
                    RecordEditorPanel patientRecordEditorPanel = (RecordEditorPanel) patientTabbedPane.getSelectedComponent();
                    patientDatabaseRecord = patientRecordEditorPanel.getDatabaseRecord();
                } else {
                    // get all the tumour records
                }
            }
        }
    }

    private DatabaseRecord saveRecord(DatabaseRecord databaseRecord) throws SecurityException, RemoteException, SQLException {
        // id is the internal database id
        if (databaseRecord.getVariable(Globals.PATIENT_TABLE_RECORD_ID_VARIABLE_NAME) == null &&
                databaseRecord.getVariable(Globals.TUMOUR_TABLE_RECORD_ID_VARIABLE_NAME) == null) {
            int id = canreg.client.CanRegClientApp.getApplication().saveRecord(databaseRecord);
            if (databaseRecord instanceof Patient) {
                // databaseRecord.setVariable(Globals.PATIENT_TABLE_RECORD_ID_VARIABLE_NAME, id);
                databaseRecord = canreg.client.CanRegClientApp.getApplication().getRecord(id, Globals.PATIENT_TABLE_NAME);
            } else if (databaseRecord instanceof Tumour) {
                // databaseRecord.setVariable(Globals.TUMOUR_TABLE_RECORD_ID_VARIABLE_NAME, id);
                databaseRecord = canreg.client.CanRegClientApp.getApplication().getRecord(id, Globals.TUMOUR_TABLE_NAME);
            }
            JOptionPane.showInternalMessageDialog(this, "New record saved.");
        } else {
            canreg.client.CanRegClientApp.getApplication().editRecord(databaseRecord);
            // TODO: Retrieve updated data if not data can be lost. Get the patient/tumour?
            int id = -1;
            if (databaseRecord instanceof Patient) {
                id = (Integer) databaseRecord.getVariable(Globals.PATIENT_TABLE_RECORD_ID_VARIABLE_NAME);
                databaseRecord = canreg.client.CanRegClientApp.getApplication().getRecord(id, Globals.PATIENT_TABLE_NAME);
            } else if (databaseRecord instanceof Tumour) {
                id = (Integer) databaseRecord.getVariable(Globals.TUMOUR_TABLE_RECORD_ID_VARIABLE_NAME);
                databaseRecord = canreg.client.CanRegClientApp.getApplication().getRecord(id, Globals.TUMOUR_TABLE_NAME);
            }
            JOptionPane.showInternalMessageDialog(this, "Record saved.");
        }
        return databaseRecord;
    }

    private DatabaseRecord associateTumourRecordToPatientRecord(DatabaseRecord tumourDatabaseRecord, DatabaseRecord patientDatabaseRecord) {
        tumourDatabaseRecord.setVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientIDTumourTable.toString()).getDatabaseVariableName(),
                patientDatabaseRecord.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientID.toString()).getDatabaseVariableName()));
        tumourDatabaseRecord.setVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordIDTumourTable.toString()).getDatabaseVariableName(),
                patientDatabaseRecord.getVariable(globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString()).getDatabaseVariableName()));
        return tumourDatabaseRecord;
    }

    private void refreshShowObsolete() {
        boolean showObsolete = showObsoleteRecordsCheckBox.isSelected();
        for (Component comp : patientTabbedPane.getComponents()) {
            RecordEditorPanel rep = (RecordEditorPanel) comp;
            DatabaseRecord dbr = rep.getDatabaseRecord();
            int obsoleteFlag = (Integer) dbr.getVariable(patientObsoleteVariableName);
            if (!showObsolete && obsoleteFlag == 1) {
                patientTabbedPane.setEnabledAt(patientTabbedPane.indexOfComponent(rep), false);
            } else {
                patientTabbedPane.setEnabledAt(patientTabbedPane.indexOfComponent(rep), true);
            }
        }
        for (Component comp : tumourTabbedPane.getComponents()) {
            RecordEditorPanel rep = (RecordEditorPanel) comp;
            DatabaseRecord dbr = rep.getDatabaseRecord();
            int obsoleteFlag = (Integer) dbr.getVariable(tumourObsoleteVariableName);
            if (!showObsolete && obsoleteFlag == 1) {
                tumourTabbedPane.setEnabledAt(tumourTabbedPane.indexOfComponent(rep), false);
            } else {
                tumourTabbedPane.setEnabledAt(tumourTabbedPane.indexOfComponent(rep), true);
            }
        }
    }

    @Action
    public void toggleShowObsoleteRecords() {
        refreshShowObsolete();
    }
}
