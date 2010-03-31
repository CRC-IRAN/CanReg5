/*
 * RecordEditorPanel.java
 *
 * Created on 16 July 2008, 14:53
 */
package canreg.client.gui.dataentry;

import canreg.client.CanRegClientApp;
import canreg.client.gui.components.DateVariableEditorPanel;
import canreg.client.gui.components.TextFieldVariableEditorPanel;
import canreg.client.gui.components.VariableEditorGroupPanel;
import canreg.client.gui.components.VariableEditorPanel;
import canreg.common.DatabaseGroupsListElement;
import canreg.common.DatabaseVariablesListElement;
import canreg.common.DatabaseVariablesListElementPositionSorter;
import canreg.common.GlobalToolBox;
import canreg.common.Globals;
import canreg.common.GregorianCalendarCanReg;
import canreg.common.Tools;
import canreg.common.qualitycontrol.CheckResult;
import canreg.common.qualitycontrol.CheckResult.ResultCode;
import canreg.server.database.DatabaseRecord;
import canreg.server.database.Dictionary;
import canreg.server.database.DictionaryEntry;
import canreg.server.database.Patient;
import canreg.server.database.Source;
import canreg.server.database.Tumour;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.application.Action;
import org.w3c.dom.Document;

/**
 *
 * @author  ervikm
 */
public class RecordEditorPanel extends javax.swing.JPanel implements ActionListener, Cloneable, PropertyChangeListener {

    private DatabaseRecord databaseRecord;
    private Document doc;
    private panelTypes panelType;
    private DatabaseVariablesListElement[] variablesInTable;
    private Map<String, VariableEditorPanel> variableEditorPanels;
    private Map<Integer, Dictionary> dictionary;
    private DatabaseGroupsListElement[] groupListElements;
    private GlobalToolBox globalToolBox;
    private boolean hasChanged = false;
    private ActionListener actionListener;
    private DatabaseVariablesListElement recordStatusVariableListElement;
    private DatabaseVariablesListElement unduplicationVariableListElement;
    private DatabaseVariablesListElement checkVariableListElement;
    private Map<String, DictionaryEntry> recStatusDictMap;
    private DictionaryEntry[] recStatusDictWithConfirmArray;
    private DictionaryEntry[] recStatusDictWithoutConfirmArray;
    private ResultCode resultCode = null;
    private DatabaseVariablesListElement patientIDVariableListElement;
    private DatabaseVariablesListElement patientRecordIDVariableListElement;
    private DatabaseVariablesListElement obsoleteFlagVariableListElement;
    private DatabaseVariablesListElement updatedByVariableListElement;
    private DatabaseVariablesListElement updateDateVariableListElement;
    private DatabaseVariablesListElement tumourSequenceNumberVariableListElement;
    private DatabaseVariablesListElement tumourSequenceTotalVariableListElement;
    private SourcesPanel sourcesPanel;
    private final SimpleDateFormat dateFormat;
    private LinkedList<DatabaseVariablesListElement> autoFillList;

    boolean areAllVariablesPresent() {
        boolean allPresent = true;
        for (DatabaseVariablesListElement databaseVariablesListElement : variablesInTable) {
            VariableEditorPanel panel = variableEditorPanels.get(databaseVariablesListElement.getDatabaseVariableName());
            if (panel != null) {
                boolean filledOK = panel.isFilledOK();
                if (!filledOK) {
                    panel.updateFilledInStatusColor();
                }
                allPresent = allPresent & filledOK;
            }
        }
        return allPresent;
    }

    void refreshDatabaseRecord(DatabaseRecord record) {
        setRecord(record);
        setSaveNeeded(false);

        // get record status and check status

        buildPanel();

        // set record status and check status

        refreshCheckStatus(record);
        refreshRecordStatus(record);
        refreshUpdatedBy();
    }

    void setActionListener(ActionListener listener) {
        this.actionListener = listener;
    }

    /**
     * 
     * @return
     */
    public boolean isSaveNeeded() {
        // hasChanged = false;

        for (DatabaseVariablesListElement databaseVariablesListElement : variablesInTable) {
            VariableEditorPanel panel = variableEditorPanels.get(databaseVariablesListElement.getDatabaseVariableName());
            if (panel != null) {
                hasChanged = hasChanged || panel.hasChanged();
            }
        }

        return hasChanged;
    }

    /**
     * 
     * @param saveNeeded
     */
    public void setSaveNeeded(boolean saveNeeded) {
        this.hasChanged = saveNeeded;
    }


    /*
     * Set the resultcode of individual variables.
     *
     */
    public void setResultCodeOfVariable(String databaseVariableName, ResultCode resultCode) {
        VariableEditorPanel panel = variableEditorPanels.get(databaseVariableName);
        panel.setResultCode(resultCode);
    }

    public void setChecksResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
        String recStatus = null;
        boolean canBeConfirmed = false;
        if (resultCode == null) {
            checksLabel.setText(java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("NOT_DONE"));
            canBeConfirmed = false;
        } else if (resultCode == ResultCode.NotDone) {
            checksLabel.setText(java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("NOT_DONE"));
            canBeConfirmed = false;
        } else {
            checksLabel.setText(java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("DONE:_") + resultCode.toString());
            if (resultCode == ResultCode.OK || resultCode == ResultCode.Query) {
                canBeConfirmed = true;
            } else if (resultCode == resultCode.Rare) {
                if (CanRegClientApp.getApplication().getUserRightLevel() == Globals.UserRightLevels.SUPERVISOR) {
                    canBeConfirmed = true;
                }
            }
        }
        // Set record status
        if (recordStatusVariableListElement != null && recordStatusVariableListElement.getUseDictionary() != null) {
            recStatus = "0";
            if (hasChanged) {
                recStatus = "0";
            } else {
                recStatus = (String) databaseRecord.getVariable(recordStatusVariableListElement.getDatabaseVariableName());
            }
            if (canBeConfirmed) {
                recordStatusComboBox.setModel(new DefaultComboBoxModel(recStatusDictWithConfirmArray));
                if (recStatus != null) {
                    recordStatusComboBox.setSelectedItem(recStatusDictMap.get(recStatus));
                }
            } else {
                recordStatusComboBox.setModel(new DefaultComboBoxModel(recStatusDictWithoutConfirmArray));
                if (recStatus != null) {
                    recordStatusComboBox.setSelectedItem(recStatusDictMap.get(recStatus));
                }
            }
            databaseRecord.setVariable(recordStatusVariableListElement.getDatabaseVariableName(), recStatus);
            databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), CheckResult.toDatabaseVariable(resultCode));
        }
    }

    void toggleObsolete(boolean confirmed) {
        if (confirmed) {
            DatabaseVariablesListElement dbvle = obsoleteFlagVariableListElement;
            if (dbvle != null) {
                boolean obsolete = obsoleteToggleButton.isSelected();
                if (obsolete) {
                    databaseRecord.setVariable(dbvle.getDatabaseVariableName(), Globals.OBSOLETE_VALUE);
                } else {
                    databaseRecord.setVariable(dbvle.getDatabaseVariableName(), Globals.NOT_OBSOLETE_VALUE);
                }
            }
        } else {
            obsoleteToggleButton.setSelected(!obsoleteToggleButton.isSelected());
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("Changed")) {
            if (e.getSource().equals(saveButton)) {
                // do nothing...
            } else {
                changesDone();
                actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.CHANGED));
            }
        }
    }

    private void changesDone() {
        setSaveNeeded(true);
        setChecksResultCode(ResultCode.NotDone);
    }

    private void refreshRecordStatus(DatabaseRecord record) {
        /*
         * Set the record status.
         */
        if (recordStatusVariableListElement != null && recordStatusVariableListElement.getUseDictionary() != null) {
            recordStatusComboBox.setModel(new DefaultComboBoxModel(recStatusDictWithConfirmArray));
            String recStatus = (String) record.getVariable(recordStatusVariableListElement.getDatabaseVariableName());
            if (recStatus != null) {
                recordStatusComboBox.setSelectedItem(recStatusDictMap.get(recStatus));
            } else {
                recordStatusComboBox.setSelectedItem(recStatusDictMap.get("0"));
            }
        } else {
            recordStatusPanel.setVisible(false);
        }
    }

    private void refreshCheckStatus(DatabaseRecord record) {
        /*
         * Set the check status
         */
        if (checkVariableListElement != null) {
            Object checkStatus = record.getVariable(checkVariableListElement.getDatabaseVariableName());
            if (checkStatus != null) {
                String checkStatusString = (String) checkStatus;
                resultCode = CheckResult.toResultCode(checkStatusString);
                setSaveNeeded(false);
                setChecksResultCode(resultCode);
            } else {
                // String checkStatusString = (String) checkStatus;
                // resultCode = CheckResult.toResultCode(checkStatusString);
                setSaveNeeded(true);
                setChecksResultCode(ResultCode.NotDone);
            }
        }
    }

    private void refreshObsoleteStatus(DatabaseRecord record) {
        /*
         * Set the obsolete status
         */
        String obsoleteStatus = (String) record.getVariable(obsoleteFlagVariableListElement.getDatabaseVariableName());
        if (obsoleteStatus != null && obsoleteStatus.equalsIgnoreCase(Globals.OBSOLETE_VALUE)) {
            obsoleteToggleButton.setSelected(true);
        } else {
            obsoleteToggleButton.setSelected(false);
        }
    }

    public void maximizeSize() {
        int heightToGrowBy = this.getHeight() - dataScrollPane.getHeight() + dataPanel.getHeight();
        int widthToGrowBy = this.getWidth() - dataScrollPane.getWidth() + dataPanel.getWidth();
        this.setSize(this.getHeight() + heightToGrowBy, this.getWidth() + widthToGrowBy);
        this.revalidate();
    }

    private void refreshUpdatedBy() {
        String updatedBy = java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("UNKNOWN_BY");
        String updateDateToolTip = java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("UNKNOWN_DATE");
        /*
         * Set the updatedBy
         */
        if (updatedByVariableListElement != null) {
            String updatedByString = (String) databaseRecord.getVariable(updatedByVariableListElement.getDatabaseVariableName());
            if (updatedByString != null && updatedByString.trim().length() > 0) {
                updatedBy = updatedByString;
            }
        }
        userLabel.setText(updatedBy);
        /*
         * Set the update date
         */
        if (updateDateVariableListElement != null) {
            String updateDate = (String) databaseRecord.getVariable(updateDateVariableListElement.getDatabaseVariableName());
            String updateDateString = "";
            if (updateDate != null && updateDate.length() > 0) {
                Date date;
                try {
                    date = dateFormat.parse(updateDate);
                } catch (ParseException ex) {
                    date = null;
                    Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.INFO, null, ex);
                }
                if (date != null) {
                    Calendar todayCal = new GregorianCalendarCanReg();
                    Calendar recordCal = new GregorianCalendarCanReg();
                    recordCal.setTime(date);
                    if (todayCal.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR)
                            && todayCal.get(Calendar.DAY_OF_YEAR) == recordCal.get(Calendar.DAY_OF_YEAR)) {
                        updateDateString = java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("TODAY");
                        updateDateToolTip = DateFormat.getDateInstance().format(date);
                    } else if (todayCal.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR)
                            && todayCal.get(Calendar.DAY_OF_YEAR) - recordCal.get(Calendar.DAY_OF_YEAR) == 1) {
                        updateDateString = java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("YESTERDAY");
                        updateDateToolTip = DateFormat.getDateInstance().format(date);
                    } else if (todayCal.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR)
                            && todayCal.get(Calendar.WEEK_OF_YEAR) == recordCal.get(Calendar.WEEK_OF_YEAR)) {
                        updateDateString = java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("THIS_WEEK");
                        updateDateToolTip = DateFormat.getDateInstance().format(date);
                    } else if (todayCal.get(Calendar.YEAR) == recordCal.get(Calendar.YEAR)
                            && todayCal.get(Calendar.WEEK_OF_YEAR) - recordCal.get(Calendar.WEEK_OF_YEAR) == 1) {
                        updateDateString = java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("LAST_WEEK");
                        updateDateToolTip = DateFormat.getDateInstance().format(date);
                    } else {
                        updateDateString = DateFormat.getDateInstance().format(date);
                        updateDateToolTip = DateFormat.getDateInstance().format(date);
                    }
                }
            }
            dateLabel.setText(updateDateString);
        }
        updatedByPanel.setToolTipText(java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/RecordEditorPanel").getString("RECORD_UPDATED_BY_") + updatedBy + ", " + updateDateToolTip);
    }

    private void refreshSequence() {
        if (tumourSequenceNumberVariableListElement != null) {
            String tumourSequenceNumberString = (String) databaseRecord.getVariable(tumourSequenceNumberVariableListElement.getDatabaseVariableName());
            sequenceNumberValueLabel.setText(tumourSequenceNumberString);
        }
        if (tumourSequenceTotalVariableListElement != null) {
            String tumourSequenceTotalString = (String) databaseRecord.getVariable(tumourSequenceTotalVariableListElement.getDatabaseVariableName());
            sequenceTotalValueLabel.setText(tumourSequenceTotalString);
        }
    }

    private enum panelTypes {

        PATIENT, TUMOUR, SOURCE
    }

    /** Creates new form RecordEditorPanel */
    public RecordEditorPanel(ActionListener listener) {
        initComponents();

        this.actionListener = listener;

        globalToolBox =
                CanRegClientApp.getApplication().getGlobalToolBox();
        saveButton.setEnabled(true);
        // setChecksResultCode(resultCode);
        // Remove this for now?
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener(this);

        // TODO: update the label of person search and MP search
        // for now - hide this untill it is used...
        searchLabel.setVisible(false);
        mpLabel.setVisible(false);

        dateFormat = new SimpleDateFormat("yyyyMMdd");

        autoFillList = new LinkedList<DatabaseVariablesListElement>();
    }

    /**
     *
     * @param doc
     */
    public void setDocument(Document doc) {
        this.doc = doc;
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
    public void setRecordAndBuildPanel(DatabaseRecord dbr) {
        setChecksResultCode(ResultCode.NotDone);
        setRecord(dbr);
        buildPanel();
    }

    private void setRecord(DatabaseRecord dbr) {
        this.databaseRecord = dbr;
        setSaveNeeded(false);
        groupListElements = Tools.getGroupsListElements(doc, Globals.NAMESPACE);
        if (databaseRecord.getClass().isInstance(new Patient())) {
            panelType = panelTypes.PATIENT;
            recordStatusVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordStatus.toString());
            unduplicationVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PersonSearch.toString());
            patientIDVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientID.toString());
            patientRecordIDVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientRecordID.toString());
            obsoleteFlagVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.ObsoleteFlagPatientTable.toString());
            updateDateVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientUpdateDate.toString());
            updatedByVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.PatientUpdatedBy.toString());

        } else if (databaseRecord.getClass().isInstance(new Tumour())) {
            panelType = panelTypes.TUMOUR;
            recordStatusVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.TumourRecordStatus.toString());
            obsoleteFlagVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.ObsoleteFlagTumourTable.toString());
            checkVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.CheckStatus.toString());
            updateDateVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.TumourUpdateDate.toString());
            updatedByVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.TumourUpdatedBy.toString());
            tumourSequenceNumberVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.MultPrimSeq.toString());
            tumourSequenceTotalVariableListElement =
                    globalToolBox.translateStandardVariableNameToDatabaseListElement(Globals.StandardVariableNames.MultPrimTot.toString());
        } else if (databaseRecord.getClass().isInstance(new Source())) {
            panelType = panelTypes.SOURCE;
            recordStatusVariableListElement = null;
            unduplicationVariableListElement = null;
            obsoleteFlagVariableListElement = null;
            checkVariableListElement = null;

        }

        /*
         * Build the record status map.
         */

        if (recordStatusVariableListElement != null && recordStatusVariableListElement.getUseDictionary() != null) {
            recStatusDictMap = dictionary.get(canreg.client.dataentry.DictionaryHelper.getDictionaryIDbyName(doc, recordStatusVariableListElement.getUseDictionary())).getDictionaryEntries();

            Collection<DictionaryEntry> recStatusDictCollection = recStatusDictMap.values();
            recStatusDictWithConfirmArray =
                    recStatusDictCollection.toArray(new DictionaryEntry[0]);

            Vector<DictionaryEntry> recStatusDictWithoutConfirmVector = new Vector<DictionaryEntry>();
            for (DictionaryEntry entry : recStatusDictCollection) {
                // "1" is the code for confirmed... TODO: change to dynamic code...
                if (!entry.getCode().equalsIgnoreCase("1")) {
                    recStatusDictWithoutConfirmVector.add(entry);
                }
            }
            recStatusDictWithoutConfirmArray = recStatusDictWithoutConfirmVector.toArray(new DictionaryEntry[0]);
        }

        String tableName = null;

        if (panelType == panelTypes.PATIENT) {
            tableName = Globals.PATIENT_TABLE_NAME;
            mpPanel.setVisible(false);
            changePatientRecordMenuItem.setVisible(false);
            checksPanel.setVisible(false);
            sequencePanel.setVisible(false);
        } else if (panelType == panelTypes.TUMOUR) {
            tableName = Globals.TUMOUR_TABLE_NAME;
            personSearchPanel.setVisible(false);
        } else if (panelType == panelTypes.SOURCE) {
            tableName = Globals.SOURCE_TABLE_NAME;
            systemPanel.setVisible(false);
        }
        variablesInTable =
                canreg.common.Tools.getVariableListElements(doc, Globals.NAMESPACE, tableName);
        Arrays.sort(variablesInTable, new DatabaseVariablesListElementPositionSorter());
    }

    /**
     *
     * @return
     */
    public DatabaseRecord getDatabaseRecord() {
        buildDatabaseRecord();
        return databaseRecord;
    }

    private void buildPanel() {
        dataPanel.removeAll();

        if (variableEditorPanels != null) {
            for (VariableEditorPanel vep : variableEditorPanels.values()) {
                vep.removeListener();
            }
        }
        variableEditorPanels =
                new LinkedHashMap();

        Map<Integer, VariableEditorGroupPanel> groupIDtoPanelMap = new LinkedHashMap<Integer, VariableEditorGroupPanel>();

        Map<String, DictionaryEntry> possibleValues;

        for (int i = 0; i
                < variablesInTable.length; i++) {
            DatabaseVariablesListElement currentVariable = variablesInTable[i];
            VariableEditorPanel vep;

            String variableType = currentVariable.getVariableType();

            if (Globals.VARIABLE_TYPE_DATE_NAME.equalsIgnoreCase(variableType)) {
                vep = new DateVariableEditorPanel(this);
            } else if (Globals.VARIABLE_TYPE_TEXT_AREA_NAME.equalsIgnoreCase(variableType)) {
                vep = new TextFieldVariableEditorPanel(this);
            } else {
                vep = new VariableEditorPanel(this);
            }

            vep.setDatabaseVariablesListElement(currentVariable);

            int dictionaryID = currentVariable.getDictionaryID();

            if (dictionaryID >= 0) {
                Dictionary dic = dictionary.get(dictionaryID);

                if (dic != null) {
                    vep.setDictionary(dic);
                }

            } else {
                vep.setDictionary(null);
            }

            String variableName = currentVariable.getDatabaseVariableName();
            Object variableValue = databaseRecord.getVariable(variableName);
            if (variableValue != null) {
                vep.setInitialValue(variableValue.toString());
            }

            String variableFillStatus = currentVariable.getFillInStatus();
            if (Globals.FILL_IN_STATUS_AUTOMATIC_STRING.equalsIgnoreCase(variableFillStatus)) {
                autoFillList.add(currentVariable);
            }

            Integer groupID = currentVariable.getGroupID();
            //Skip 0 and -1 - System groups
            if (groupID > 0) {
                VariableEditorGroupPanel panel = groupIDtoPanelMap.get(groupID);
                if (panel == null) {
                    panel = new VariableEditorGroupPanel();
                    panel.setGroupName(globalToolBox.translateGroupIDToDatabaseGroupListElement(groupID).getGroupName());
                    groupIDtoPanelMap.put(currentVariable.getGroupID(), panel);
                }

                panel.add(vep);
            }

            // vep.setPropertyChangeListener(this);
            variableEditorPanels.put(currentVariable.getDatabaseVariableName(), vep);
        }

        // Iterate trough groups

        // Iterator<Integer> iterator = groupIDtoPanelMap.keySet().iterator();
        for (DatabaseGroupsListElement groupListElement : groupListElements) {
            int groupID = groupListElement.getGroupIndex();
            JPanel panel = groupIDtoPanelMap.get(groupID);
            if (panel != null) {
                dataPanel.add(panel);
                panel.setVisible(true);
            }
        }

        // If this is the tumour part we add the source table
        if (panelType == panelTypes.TUMOUR) {
            sourcesPanel = new SourcesPanel(this);
            sourcesPanel.setDictionary(dictionary);
            sourcesPanel.setDoc(doc);
            sourcesPanel.setVisible(true);
            Tumour tumour = (Tumour) databaseRecord;
            sourcesPanel.setSources(tumour.getSources());
            dataPanel.add(sourcesPanel);
            refreshSequence();
        }

        if (panelType != panelTypes.SOURCE) {
            refreshObsoleteStatus(databaseRecord);
            refreshRecordStatus(databaseRecord);
            refreshCheckStatus(databaseRecord);
        }
        refreshUpdatedBy();
        dataPanel.revalidate();
        dataPanel.repaint();
    }

    public void propertyChange(PropertyChangeEvent e) {
        String propName = e.getPropertyName();
        if ("focusOwner".equals(propName)) {
            if (e.getNewValue() instanceof JTextField) {
                JTextField textField = (JTextField) e.getNewValue();
                textField.selectAll();
            }

        } /** Called when a field's "value" property changes. */
        else if ("value".equals(propName)) {
            setSaveNeeded(true);
            //Temporarily disabled
            actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.CHANGED));
            // saveButton.setEnabled(saveNeeded);
        } else {
            // Do nothing.
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        deleteMenuItem = new javax.swing.JMenuItem();
        changePatientRecordMenuItem = new javax.swing.JMenuItem();
        systemPanel = new javax.swing.JPanel();
        checksPanel = new javax.swing.JPanel();
        checksButton = new javax.swing.JButton();
        checksLabel = new javax.swing.JLabel();
        personSearchPanel = new javax.swing.JPanel();
        searchButton = new javax.swing.JButton();
        searchLabel = new javax.swing.JLabel();
        mpPanel = new javax.swing.JPanel();
        mpButton = new javax.swing.JButton();
        mpLabel = new javax.swing.JLabel();
        recordStatusPanel = new javax.swing.JPanel();
        recordStatusComboBox = new javax.swing.JComboBox();
        obsoleteToggleButton = new javax.swing.JToggleButton();
        controlPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        menuButton = new javax.swing.JButton();
        updatedByPanel = new javax.swing.JPanel();
        byLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        sequencePanel = new javax.swing.JPanel();
        sequenceNumberDescriptionLabel = new javax.swing.JLabel();
        sequenceNumberValueLabel = new javax.swing.JLabel();
        sequenceTotalValueLabel = new javax.swing.JLabel();
        sequenceTotalDescriptionLabel = new javax.swing.JLabel();
        dataScrollPane = new javax.swing.JScrollPane();
        dataPanel = new javax.swing.JPanel();

        popupMenu.setName("popupMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(RecordEditorPanel.class, this);
        deleteMenuItem.setAction(actionMap.get("deleteRecord")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(RecordEditorPanel.class);
        deleteMenuItem.setText(resourceMap.getString("deleteMenuItem.text")); // NOI18N
        deleteMenuItem.setName("deleteMenuItem"); // NOI18N
        popupMenu.add(deleteMenuItem);

        changePatientRecordMenuItem.setAction(actionMap.get("changePatientRecord")); // NOI18N
        changePatientRecordMenuItem.setText(resourceMap.getString("changePatientRecordMenuItem.text")); // NOI18N
        changePatientRecordMenuItem.setName("changePatientRecordMenuItem"); // NOI18N
        popupMenu.add(changePatientRecordMenuItem);

        setName("Form"); // NOI18N

        systemPanel.setName("systemPanel"); // NOI18N

        checksPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("checksPanel.border.title"))); // NOI18N
        checksPanel.setName("checksPanel"); // NOI18N

        checksButton.setAction(actionMap.get("runChecksAction")); // NOI18N
        checksButton.setText(resourceMap.getString("checksButton.text")); // NOI18N
        checksButton.setName("checksButton"); // NOI18N

        checksLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        checksLabel.setText(resourceMap.getString("checksLabel.text")); // NOI18N
        checksLabel.setName("checksLabel"); // NOI18N

        javax.swing.GroupLayout checksPanelLayout = new javax.swing.GroupLayout(checksPanel);
        checksPanel.setLayout(checksPanelLayout);
        checksPanelLayout.setHorizontalGroup(
            checksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(checksButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(checksLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
        );
        checksPanelLayout.setVerticalGroup(
            checksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(checksPanelLayout.createSequentialGroup()
                .addComponent(checksButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(checksLabel))
        );

        personSearchPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("personSearchPanel.border.title"))); // NOI18N
        personSearchPanel.setName("personSearchPanel"); // NOI18N

        searchButton.setAction(actionMap.get("runPersonSearch")); // NOI18N
        searchButton.setText(resourceMap.getString("searchButton.text")); // NOI18N
        searchButton.setToolTipText(resourceMap.getString("searchButton.toolTipText")); // NOI18N
        searchButton.setName("searchButton"); // NOI18N

        searchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        searchLabel.setText(resourceMap.getString("searchLabel.text")); // NOI18N
        searchLabel.setName("searchLabel"); // NOI18N

        javax.swing.GroupLayout personSearchPanelLayout = new javax.swing.GroupLayout(personSearchPanel);
        personSearchPanel.setLayout(personSearchPanelLayout);
        personSearchPanelLayout.setHorizontalGroup(
            personSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
            .addComponent(searchLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
        );
        personSearchPanelLayout.setVerticalGroup(
            personSearchPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personSearchPanelLayout.createSequentialGroup()
                .addComponent(searchButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchLabel))
        );

        mpPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("mpPanel.border.title"))); // NOI18N
        mpPanel.setName("mpPanel"); // NOI18N

        mpButton.setAction(actionMap.get("runMultiplePrimarySearch")); // NOI18N
        mpButton.setText(resourceMap.getString("mpButton.text")); // NOI18N
        mpButton.setToolTipText(resourceMap.getString("mpButton.toolTipText")); // NOI18N
        mpButton.setName("mpButton"); // NOI18N

        mpLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        mpLabel.setText(resourceMap.getString("mpLabel.text")); // NOI18N
        mpLabel.setName("mpLabel"); // NOI18N

        javax.swing.GroupLayout mpPanelLayout = new javax.swing.GroupLayout(mpPanel);
        mpPanel.setLayout(mpPanelLayout);
        mpPanelLayout.setHorizontalGroup(
            mpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpPanelLayout.createSequentialGroup()
                .addGroup(mpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                    .addComponent(mpButton))
                .addContainerGap())
        );
        mpPanelLayout.setVerticalGroup(
            mpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mpPanelLayout.createSequentialGroup()
                .addComponent(mpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mpLabel))
        );

        recordStatusPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("recordStatusPanel.border.title"))); // NOI18N
        recordStatusPanel.setName("recordStatusPanel"); // NOI18N

        recordStatusComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        recordStatusComboBox.setName("recordStatusComboBox"); // NOI18N

        obsoleteToggleButton.setAction(actionMap.get("setObsoleteFlag")); // NOI18N
        obsoleteToggleButton.setText(resourceMap.getString("obsoleteToggleButton.text")); // NOI18N
        obsoleteToggleButton.setName("obsoleteToggleButton"); // NOI18N

        javax.swing.GroupLayout recordStatusPanelLayout = new javax.swing.GroupLayout(recordStatusPanel);
        recordStatusPanel.setLayout(recordStatusPanelLayout);
        recordStatusPanelLayout.setHorizontalGroup(
            recordStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, recordStatusPanelLayout.createSequentialGroup()
                .addGroup(recordStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(obsoleteToggleButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 157, Short.MAX_VALUE)
                    .addComponent(recordStatusComboBox, 0, 157, Short.MAX_VALUE))
                .addContainerGap())
        );
        recordStatusPanelLayout.setVerticalGroup(
            recordStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recordStatusPanelLayout.createSequentialGroup()
                .addComponent(recordStatusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(obsoleteToggleButton)
                .addContainerGap())
        );

        controlPanel.setName("controlPanel"); // NOI18N

        saveButton.setAction(actionMap.get("saveRecord")); // NOI18N
        saveButton.setText(resourceMap.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        menuButton.setAction(actionMap.get("menuAction")); // NOI18N
        menuButton.setText(resourceMap.getString("menuButton.text")); // NOI18N
        menuButton.setName("menuButton"); // NOI18N

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(menuButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
                    .addComponent(saveButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE))
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(menuButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        updatedByPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("updatedByPanel.border.title"))); // NOI18N
        updatedByPanel.setName("updatedByPanel"); // NOI18N

        byLabel.setText(resourceMap.getString("byLabel.text")); // NOI18N
        byLabel.setName("byLabel"); // NOI18N

        userLabel.setText(resourceMap.getString("userLabel.text")); // NOI18N
        userLabel.setName("userLabel"); // NOI18N

        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dateLabel.setText(resourceMap.getString("dateLabel.text")); // NOI18N
        dateLabel.setName("dateLabel"); // NOI18N

        javax.swing.GroupLayout updatedByPanelLayout = new javax.swing.GroupLayout(updatedByPanel);
        updatedByPanel.setLayout(updatedByPanelLayout);
        updatedByPanelLayout.setHorizontalGroup(
            updatedByPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatedByPanelLayout.createSequentialGroup()
                .addComponent(byLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
        );
        updatedByPanelLayout.setVerticalGroup(
            updatedByPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(updatedByPanelLayout.createSequentialGroup()
                .addGroup(updatedByPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(byLabel)
                    .addComponent(userLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        sequencePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("sequencePanel.border.title"))); // NOI18N
        sequencePanel.setName("sequencePanel"); // NOI18N

        sequenceNumberDescriptionLabel.setText(resourceMap.getString("sequenceNumberDescriptionLabel.text")); // NOI18N
        sequenceNumberDescriptionLabel.setName("sequenceNumberDescriptionLabel"); // NOI18N

        sequenceNumberValueLabel.setText(resourceMap.getString("sequenceNumberValueLabel.text")); // NOI18N
        sequenceNumberValueLabel.setName("sequenceNumberValueLabel"); // NOI18N

        sequenceTotalValueLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sequenceTotalValueLabel.setText(resourceMap.getString("sequenceTotalValueLabel.text")); // NOI18N
        sequenceTotalValueLabel.setName("sequenceTotalValueLabel"); // NOI18N

        sequenceTotalDescriptionLabel.setText(resourceMap.getString("sequenceTotalDescriptionLabel.text")); // NOI18N
        sequenceTotalDescriptionLabel.setName("sequenceTotalDescriptionLabel"); // NOI18N

        javax.swing.GroupLayout sequencePanelLayout = new javax.swing.GroupLayout(sequencePanel);
        sequencePanel.setLayout(sequencePanelLayout);
        sequencePanelLayout.setHorizontalGroup(
            sequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sequencePanelLayout.createSequentialGroup()
                .addGroup(sequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sequenceNumberDescriptionLabel)
                    .addComponent(sequenceTotalDescriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(sequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(sequenceNumberValueLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sequenceTotalValueLabel)))
        );
        sequencePanelLayout.setVerticalGroup(
            sequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sequencePanelLayout.createSequentialGroup()
                .addGroup(sequencePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(sequencePanelLayout.createSequentialGroup()
                        .addComponent(sequenceNumberDescriptionLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sequenceTotalDescriptionLabel))
                    .addGroup(sequencePanelLayout.createSequentialGroup()
                        .addComponent(sequenceNumberValueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sequenceTotalValueLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout systemPanelLayout = new javax.swing.GroupLayout(systemPanel);
        systemPanel.setLayout(systemPanelLayout);
        systemPanelLayout.setHorizontalGroup(
            systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemPanelLayout.createSequentialGroup()
                .addComponent(checksPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(personSearchPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(recordStatusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sequencePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(updatedByPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        systemPanelLayout.setVerticalGroup(
            systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(systemPanelLayout.createSequentialGroup()
                .addGroup(systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                    .addComponent(personSearchPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                    .addComponent(checksPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                    .addComponent(sequencePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(updatedByPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recordStatusPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))
                .addContainerGap())
        );

        dataScrollPane.setName("dataScrollPane"); // NOI18N

        dataPanel.setName("dataPanel"); // NOI18N
        dataPanel.setLayout(new javax.swing.BoxLayout(dataPanel, javax.swing.BoxLayout.PAGE_AXIS));
        dataScrollPane.setViewportView(dataPanel);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(systemPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(dataScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 758, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(systemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        dataScrollPane.getVerticalScrollBar().setUnitIncrement(16);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel byLabel;
    private javax.swing.JMenuItem changePatientRecordMenuItem;
    private javax.swing.JButton checksButton;
    private javax.swing.JLabel checksLabel;
    private javax.swing.JPanel checksPanel;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JPanel dataPanel;
    private javax.swing.JScrollPane dataScrollPane;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JButton menuButton;
    private javax.swing.JButton mpButton;
    private javax.swing.JLabel mpLabel;
    private javax.swing.JPanel mpPanel;
    private javax.swing.JToggleButton obsoleteToggleButton;
    private javax.swing.JPanel personSearchPanel;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JComboBox recordStatusComboBox;
    private javax.swing.JPanel recordStatusPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JLabel sequenceNumberDescriptionLabel;
    private javax.swing.JLabel sequenceNumberValueLabel;
    private javax.swing.JPanel sequencePanel;
    private javax.swing.JLabel sequenceTotalDescriptionLabel;
    private javax.swing.JLabel sequenceTotalValueLabel;
    private javax.swing.JPanel systemPanel;
    private javax.swing.JPanel updatedByPanel;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables

    @Override
    public RecordEditorPanel clone() throws CloneNotSupportedException {
        RecordEditorPanel clone = (RecordEditorPanel) super.clone();

        return clone();
    }

    /**
     * 
     */
    @Action
    public void saveRecord() {
        buildDatabaseRecord();
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.SAVE));
        Iterator<VariableEditorPanel> iterator = variableEditorPanels.values().iterator();
        while (iterator.hasNext()) {
            VariableEditorPanel vep = iterator.next();
            vep.setSaved();
        }
    }

    private void buildDatabaseRecord() {
        Iterator<VariableEditorPanel> iterator = variableEditorPanels.values().iterator();
        while (iterator.hasNext()) {
            VariableEditorPanel vep = iterator.next();
            databaseRecord.setVariable(vep.getKey(), vep.getValue());
        }

        if (panelType == panelTypes.TUMOUR) {
            Tumour tumour = (Tumour) databaseRecord;
            tumour.setSources(sourcesPanel.getSources());
        }

        if (recordStatusVariableListElement != null) {
            if (recordStatusVariableListElement != null && recordStatusVariableListElement.getUseDictionary() != null) {
                DictionaryEntry recordStatusValue = (DictionaryEntry) recordStatusComboBox.getSelectedItem();
                if (recordStatusValue != null) {
                    databaseRecord.setVariable(recordStatusVariableListElement.getDatabaseVariableName(), recordStatusValue.getCode());
                } else {
                    databaseRecord.setVariable(recordStatusVariableListElement.getDatabaseVariableName(), "0");
                    // JOptionPane.showInternalMessageDialog(this, "Record status dictionary entries missing.");
                    Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.WARNING, "Warning! Record status dictionary entries missing.");
                }
            } else {
                databaseRecord.setVariable(recordStatusVariableListElement.getDatabaseVariableName(), "0");
                // JOptionPane.showInternalMessageDialog(this, "Record status dictionary entries missing.");
                Logger.getLogger(RecordEditorPanel.class.getName()).log(Level.WARNING, "Warning! Record status dictionary entries missing.");
            }
        }
        if (obsoleteFlagVariableListElement != null) {
            if (obsoleteToggleButton.isSelected()) {
                databaseRecord.setVariable(obsoleteFlagVariableListElement.getDatabaseVariableName(), Globals.OBSOLETE_VALUE);
            } else {
                databaseRecord.setVariable(obsoleteFlagVariableListElement.getDatabaseVariableName(), Globals.NOT_OBSOLETE_VALUE);
            }
        }
        // TODO: Make dynamic result codes
        if (checkVariableListElement != null) {
            if (resultCode == null) {
                resultCode = ResultCode.NotDone;
            }
            if (resultCode.equals(ResultCode.OK)) {
                databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), "1");
            } else if (resultCode.equals(ResultCode.Invalid)) {
                databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), "3");
            } else if (resultCode.equals(ResultCode.Missing)) {
                databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), "3");
            } else if (resultCode.equals(ResultCode.Rare)) {
                databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), "2");
            } else if (resultCode.equals(ResultCode.NotDone)) {
                databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), "0");
            } else {
                databaseRecord.setVariable(checkVariableListElement.getDatabaseVariableName(), "0");
            }
        }
    }

    public LinkedList<DatabaseVariablesListElement> getAutoFillList() {
        return autoFillList;
    }

    public void setVariable(DatabaseVariablesListElement variable, String value) {
        VariableEditorPanel vep = variableEditorPanels.get(variable.getDatabaseVariableName());
        vep.setValue(value);
    }

    /**
     * 
     */
    @Action
    public void runPersonSearch() {
        autoFill();
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.PERSON_SEARCH));

    }

    /**
     * 
     */
    @Action
    public void runChecksAction() {
        autoFill();
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.CHECKS));
    }

    @Action
    public void deleteRecord() {
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.DELETE));
    }

    @Action
    public void changePatientRecord() {
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.CHANGE_PATIENT_RECORD));
    }

    @Action
    public void setObsoleteFlag() {
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.OBSOLETE));
    }

    @Action
    public void runMultiplePrimarySearch() {
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.RUN_MP));
    }

    @Action
    public void calculateAge() {
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.CALC_AGE));
    }

    @Action
    public void autoFill() {
        actionListener.actionPerformed(new ActionEvent(this, 0, RecordEditor.AUTO_FILL));
    }

    @Action
    public void menuAction() {
        popupMenu.show(menuButton, 0, 0);
    }
}
