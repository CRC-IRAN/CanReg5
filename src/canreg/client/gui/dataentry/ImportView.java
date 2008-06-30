/*
 * ImportView.java
 *
 * Created on 22 February 2008, 09:30
 */
package canreg.client.gui.dataentry;

import canreg.client.LocalSettings;
import canreg.client.gui.dataentry.VariableMappingPanel;
import canreg.client.CanRegClientApp;
import canreg.common.DatabaseVariablesListElement;
import canreg.client.dataentry.Import;
import canreg.client.dataentry.ImportOptions;
import canreg.client.dataentry.Relation;
import canreg.common.Globals;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;
import org.w3c.dom.Document;

/**
 *
 * @author  morten
 * 
 * This module 
 * 
 * TODO: Accept various date formats?
 * Implement the various options
 * 
 * 
 */
public class ImportView extends javax.swing.JInternalFrame {

    private boolean needToRebuildVariableMap = true;
    private File inFile;
    private Document doc;
    private List<VariableMappingPanel> panelList;
    private DatabaseVariablesListElement[] variablesInDB;
    private JFileChooser chooser;
    private String path;
    private LocalSettings localSettings;

    /** Creates new form ImportView */
    public ImportView() {

        initComponents();
        previewPanel.setVisible(false);

        changeTab(0);

        // Add a listener for changing the active tab
        ChangeListener tabbedPaneChangeListener = new ChangeListener() {

            public void stateChanged(ChangeEvent e) {
                initializeVariableMappingTab();
                changeTab(tabbedPane.getSelectedIndex());
            }
        };
        // And add the listener to the tabbedPane
        tabbedPane.addChangeListener(tabbedPaneChangeListener);

        localSettings = CanRegClientApp.getApplication().getLocalSettings();
        path = localSettings.getProperty("import_path");

        if (path == null) {
            chooser = new JFileChooser();
        } else {
            chooser = new JFileChooser(path);
        }
        // Group the radiobuttons
        ButtonGroup discrepanciesButtonGroup = new ButtonGroup();
        // Add to the button group
        discrepanciesButtonGroup.add(rejectRadioButton);
        discrepanciesButtonGroup.add(updateRadioButton);
        discrepanciesButtonGroup.add(overwriteRadioButton);

        // Get the system description
        doc = CanRegClientApp.getApplication().getDatabseDescription();
        variablesInDB = canreg.common.Tools.getVariableListElements(doc, Globals.NAMESPACE);

    // initializeVariableMappingTab();
    }

    private void changeFile() {
        inFile = new File(fileNameTextField.getText().trim());
        path = inFile.getPath();
        needToRebuildVariableMap = true;
    }

    private void changeTab(int tabNumber) {
        tabbedPane.setSelectedIndex(tabNumber);
        nextButton.setEnabled(tabNumber < tabbedPane.getTabCount() - 1);
        backButton.setEnabled(tabNumber > 0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        chooseFilePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        previewPanel = new javax.swing.JPanel();
        previewScrollPane = new javax.swing.JScrollPane();
        previewTextArea = new javax.swing.JTextArea();
        previewButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        separatingCharacterComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        associateVariablesPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        variablesScrollPane = new javax.swing.JScrollPane();
        variablesPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        importFilePanel = new javax.swing.JPanel();
        importButton = new javax.swing.JButton();
        discrepanciesPanel = new javax.swing.JPanel();
        rejectRadioButton = new javax.swing.JRadioButton();
        updateRadioButton = new javax.swing.JRadioButton();
        overwriteRadioButton = new javax.swing.JRadioButton();
        jPanel7 = new javax.swing.JPanel();
        doChecksCheckBox = new javax.swing.JCheckBox();
        personSearchCheckBox = new javax.swing.JCheckBox();
        queryNewNameCheckBox = new javax.swing.JCheckBox();
        maxLinesPanel = new javax.swing.JPanel();
        maxLinesTextField = new javax.swing.JTextField();
        testOnlyCheckBox = new javax.swing.JCheckBox();
        nextButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(ImportView.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setName("Form"); // NOI18N
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        tabbedPane.setName("tabbedPane"); // NOI18N

        chooseFilePanel.setName("chooseFilePanel"); // NOI18N

        jLabel1.setName("jLabel1"); // NOI18N

        fileNameTextField.setName("fileNameTextField"); // NOI18N
        fileNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fileNameTextFieldFocusLost(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(ImportView.class, this);
        browseButton.setAction(actionMap.get("browseFiles")); // NOI18N
        browseButton.setName("browseButton"); // NOI18N

        previewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Preview"));
        previewPanel.setEnabled(false);
        previewPanel.setName("previewPanel"); // NOI18N

        previewScrollPane.setFocusable(false);
        previewScrollPane.setName("previewScrollPane"); // NOI18N

        previewTextArea.setColumns(20);
        previewTextArea.setEditable(false);
        previewTextArea.setRows(5);
        previewTextArea.setName("previewTextArea"); // NOI18N
        previewScrollPane.setViewportView(previewTextArea);

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(previewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(previewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
        );

        previewButton.setAction(actionMap.get("previewAction")); // NOI18N
        previewButton.setName("previewButton"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        separatingCharacterComboBox.setEditable(true);
        separatingCharacterComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Tab", "Comma" }));
        separatingCharacterComboBox.setAction(actionMap.get("comboBoxChanged")); // NOI18N
        separatingCharacterComboBox.setName("separatingCharacterComboBox"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        jButton1.setAction(actionMap.get("autodetectSeparatingCharacterAction")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout chooseFilePanelLayout = new javax.swing.GroupLayout(chooseFilePanel);
        chooseFilePanel.setLayout(chooseFilePanelLayout);
        chooseFilePanelLayout.setHorizontalGroup(
            chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chooseFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(previewPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(chooseFilePanelLayout.createSequentialGroup()
                        .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, chooseFilePanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, chooseFilePanelLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(separatingCharacterComboBox, 0, 355, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                                .addComponent(browseButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(previewButton))
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        chooseFilePanelLayout.setVerticalGroup(
            chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(previewButton)
                    .addComponent(browseButton)
                    .addComponent(jLabel3)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(separatingCharacterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Choose File", chooseFilePanel);

        associateVariablesPanel.setName("associateVariablesPanel"); // NOI18N

        jLabel8.setName("jLabel8"); // NOI18N

        variablesScrollPane.setName("variablesScrollPane"); // NOI18N

        variablesPanel.setName("variablesPanel"); // NOI18N
        variablesPanel.setLayout(new java.awt.GridLayout(0, 1));
        variablesScrollPane.setViewportView(variablesPanel);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setResizeWeight(0.5);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jSplitPane1.setLeftComponent(jLabel2);

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setMaximumSize(new java.awt.Dimension(139, 14));
        jLabel4.setMinimumSize(new java.awt.Dimension(139, 14));
        jLabel4.setName("jLabel4"); // NOI18N
        jSplitPane1.setRightComponent(jLabel4);

        javax.swing.GroupLayout associateVariablesPanelLayout = new javax.swing.GroupLayout(associateVariablesPanel);
        associateVariablesPanel.setLayout(associateVariablesPanelLayout);
        associateVariablesPanelLayout.setHorizontalGroup(
            associateVariablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(associateVariablesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(associateVariablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(variablesScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 614, Short.MAX_VALUE))
                .addContainerGap())
        );
        associateVariablesPanelLayout.setVerticalGroup(
            associateVariablesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(associateVariablesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(variablesScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 609, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Associate Variables", associateVariablesPanel);

        importFilePanel.setName("importFilePanel"); // NOI18N

        importButton.setAction(actionMap.get("importAction")); // NOI18N
        importButton.setName("importButton"); // NOI18N

        discrepanciesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Discrepancies"));
        discrepanciesPanel.setName("discrepanciesPanel"); // NOI18N

        rejectRadioButton.setText(resourceMap.getString("rejectRadioButton.text")); // NOI18N
        rejectRadioButton.setName("rejectRadioButton"); // NOI18N

        updateRadioButton.setSelected(true);
        updateRadioButton.setText(resourceMap.getString("updateRadioButton.text")); // NOI18N
        updateRadioButton.setName("updateRadioButton"); // NOI18N

        overwriteRadioButton.setText(resourceMap.getString("overwriteRadioButton.text")); // NOI18N
        overwriteRadioButton.setName("overwriteRadioButton"); // NOI18N

        javax.swing.GroupLayout discrepanciesPanelLayout = new javax.swing.GroupLayout(discrepanciesPanel);
        discrepanciesPanel.setLayout(discrepanciesPanelLayout);
        discrepanciesPanelLayout.setHorizontalGroup(
            discrepanciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discrepanciesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(discrepanciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rejectRadioButton)
                    .addComponent(updateRadioButton)
                    .addComponent(overwriteRadioButton))
                .addContainerGap(519, Short.MAX_VALUE))
        );
        discrepanciesPanelLayout.setVerticalGroup(
            discrepanciesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(discrepanciesPanelLayout.createSequentialGroup()
                .addComponent(rejectRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(updateRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(overwriteRadioButton))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("CanReg data"));
        jPanel7.setName("jPanel7"); // NOI18N

        doChecksCheckBox.setText(resourceMap.getString("doChecksCheckBox.text")); // NOI18N
        doChecksCheckBox.setName("doChecksCheckBox"); // NOI18N

        personSearchCheckBox.setSelected(true);
        personSearchCheckBox.setText(resourceMap.getString("personSearchCheckBox.text")); // NOI18N
        personSearchCheckBox.setName("personSearchCheckBox"); // NOI18N

        queryNewNameCheckBox.setSelected(true);
        queryNewNameCheckBox.setText(resourceMap.getString("queryNewNameCheckBox.text")); // NOI18N
        queryNewNameCheckBox.setName("queryNewNameCheckBox"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(doChecksCheckBox)
                    .addComponent(personSearchCheckBox)
                    .addComponent(queryNewNameCheckBox))
                .addContainerGap(483, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addComponent(doChecksCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(personSearchCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(queryNewNameCheckBox)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        maxLinesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Max Lines"));
        maxLinesPanel.setName("maxLinesPanel"); // NOI18N

        maxLinesTextField.setName("maxLinesTextField"); // NOI18N

        testOnlyCheckBox.setSelected(true);
        testOnlyCheckBox.setText(resourceMap.getString("testOnlyCheckBox.text")); // NOI18N
        testOnlyCheckBox.setName("testOnlyCheckBox"); // NOI18N

        javax.swing.GroupLayout maxLinesPanelLayout = new javax.swing.GroupLayout(maxLinesPanel);
        maxLinesPanel.setLayout(maxLinesPanelLayout);
        maxLinesPanelLayout.setHorizontalGroup(
            maxLinesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maxLinesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(maxLinesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(maxLinesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 578, Short.MAX_VALUE)
                    .addComponent(testOnlyCheckBox))
                .addContainerGap())
        );
        maxLinesPanelLayout.setVerticalGroup(
            maxLinesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(maxLinesPanelLayout.createSequentialGroup()
                .addComponent(maxLinesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(testOnlyCheckBox))
        );

        javax.swing.GroupLayout importFilePanelLayout = new javax.swing.GroupLayout(importFilePanel);
        importFilePanel.setLayout(importFilePanelLayout);
        importFilePanelLayout.setHorizontalGroup(
            importFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(importFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(importFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(maxLinesPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(discrepanciesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importButton))
                .addContainerGap())
        );
        importFilePanelLayout.setVerticalGroup(
            importFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, importFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(discrepanciesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(maxLinesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 297, Short.MAX_VALUE)
                .addComponent(importButton)
                .addContainerGap())
        );

        tabbedPane.addTab("Import File", importFilePanel);

        nextButton.setAction(actionMap.get("jumpToNextTabAction")); // NOI18N
        nextButton.setName("nextButton"); // NOI18N

        cancelButton.setAction(actionMap.get("cancelAction")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        backButton.setAction(actionMap.get("jumpToPreviousTabAction")); // NOI18N
        backButton.setName("backButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(backButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nextButton))
                    .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 687, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextButton)
                    .addComponent(cancelButton)
                    .addComponent(backButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void fileNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fileNameTextFieldFocusLost
        changeFile();
    }//GEN-LAST:event_fileNameTextFieldFocusLost

    @Action
    public void browseFiles() {

        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                //set the file name
                fileNameTextField.setText(chooser.getSelectedFile().getCanonicalPath());
                changeFile();
            } catch (IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Action
    public void jumpToNextTabAction() {
        initializeVariableMappingTab();
        int tabNumber = tabbedPane.getSelectedIndex();
        if (tabNumber < tabbedPane.getTabCount()) {
            tabbedPane.setSelectedIndex(++tabNumber);
            changeTab(tabNumber);
        }
    }

    @Action
    public void jumpToPreviousTabAction() {
        initializeVariableMappingTab();
        int tabNumber = tabbedPane.getSelectedIndex();
        if (tabNumber >= 1) {
            tabbedPane.setSelectedIndex(--tabNumber);
            changeTab(tabNumber);
        }
    }

    @Action
    public void cancelAction() {
        this.dispose();
    }

    @Action(block = Task.BlockingScope.APPLICATION)
    public Task importAction() {
        localSettings.setProperty("import_path", path);
        localSettings.writeSettings();
        this.dispose();
        return new ImportActionTask(org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class));
    }

    private class ImportActionTask extends org.jdesktop.application.Task<Object, Void> {

        ImportActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ImportActionTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            try {
                // Calls the client app import action with the file parameters provided,
                CanRegClientApp.getApplication().importFile(doc, buildMap(), inFile, buildImportOptions());
            } catch (RemoteException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Successfully imported file " + inFile.getAbsolutePath() + ".", "File successfully imported", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void initializeVariableMappingTab() {
        if (needToRebuildVariableMap && fileNameTextField.getText().trim().length() > 0) {
            BufferedReader br = null;
            List<Relation> map = null;
            panelList = new LinkedList();
            try {
                // Remove all variable mappings
                variablesPanel.removeAll();

                // Read the first line of the file
                br = new BufferedReader(new FileReader(inFile));
                String line = br.readLine();
//                String[] lineElements = canreg.common.Tools.breakDownLine('\t', line);
                String[] lineElements = line.split(getSeparator());
                // Build variable mapping
                map = Import.constructRelations(doc, lineElements);

                // Add the panels
                for (Relation rel : map) {
                    VariableMappingPanel vmp = new VariableMappingPanel();
                    panelList.add(vmp);
                    vmp.setDBVariables(variablesInDB);
                    vmp.setFileVariableName(rel.getFileVariableName());
                    vmp.setSelectedDBIndex(rel.getDatabaseTableVariableID());
                    variablesPanel.add(vmp);
                    vmp.setVisible(true);
                }

                variablesPanel.revalidate();
                variablesPanel.repaint();

            } catch (RemoteException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Could not open file: \'" + fileNameTextField.getText().trim() + "\'.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                needToRebuildVariableMap = false;
                try {
                    br.close();
                } catch (IOException ex) {
                    Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
    }

    private List<Relation> buildMap() {
        List<Relation> map = new LinkedList();
        int i = 0;
        for (VariableMappingPanel vmp : panelList) {
            Relation rel = new Relation();

            DatabaseVariablesListElement dbVLE = vmp.getSelectedDBVariableObject();

            rel.setDatabaseTableName(dbVLE.getDatabaseTableName());
            rel.setDatabaseTableVariableID(vmp.getDBVariableIndex());
            rel.setDatabaseVariableName(dbVLE.getDatabaseVariableName());
            rel.setFileColumnNumber(i);
            rel.setFileVariableName(vmp.getFileVariableName());
            rel.setVariableType(dbVLE.getVariableType());

            map.add(rel);
            i++;
        }
        return map;
    }

    private ImportOptions buildImportOptions() {
        ImportOptions io = new ImportOptions();

        // Discrepencies
        if (updateRadioButton.isSelected()) {
            io.setDiscrepancies(ImportOptions.UPDATE);
        } else if (rejectRadioButton.isSelected()) {
            io.setDiscrepancies(ImportOptions.REJECT);
        } else if (overwriteRadioButton.isSelected()) {
            io.setDiscrepancies(ImportOptions.OVERWRITE);
        }
        // Max Lines
        if (maxLinesTextField.getText().trim().length() > 0) {
            io.setMaxLines(Integer.parseInt(maxLinesTextField.getText().trim()));
        } else {
            io.setMaxLines(-1);
        }
        io.setTestOnly(testOnlyCheckBox.isSelected());

        // separator
        io.setSeparator(getSeparator());

        // CanReg data
        io.setDoChecks(doChecksCheckBox.isSelected());
        io.setDoPersonSearch(personSearchCheckBox.isSelected());
        io.setQueryNewNames(queryNewNameCheckBox.isSelected());

        return io;
    }

    private String getSeparator() {
        String sc = separatingCharacterComboBox.getSelectedItem().toString();
        if (sc.equalsIgnoreCase("Tab")) {
            sc = new String("\t");
        } else if (sc.equalsIgnoreCase("Comma")) {
            sc = new String(",");
        }
        return sc;
    }

    @Action
    public void previewAction() {
        // show the contents of the file
        BufferedReader br = null;
        try {
            changeFile();
            br = new BufferedReader(new FileReader(inFile));
            // Read the entire file into the preview area... 
            // Change this to just a part of the file?
            previewTextArea.read(br, null);
            previewPanel.setVisible(true);
        } catch (FileNotFoundException fileNotFoundException) {
            JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Could not preview file: \'" + fileNameTextField.getText().trim() + "\'.", "Error", JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, fileNotFoundException);
        } catch (IOException ex) {
            Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Action
    public void comboBoxChanged() {
        needToRebuildVariableMap = true;
    }

    @Action
    public void autodetectSeparatingCharacterAction() {
        JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Not yet implemented.", "Error", JOptionPane.ERROR_MESSAGE);    
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel associateVariablesPanel;
    private javax.swing.JButton backButton;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel chooseFilePanel;
    private javax.swing.JPanel discrepanciesPanel;
    private javax.swing.JCheckBox doChecksCheckBox;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JButton importButton;
    private javax.swing.JPanel importFilePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel maxLinesPanel;
    private javax.swing.JTextField maxLinesTextField;
    private javax.swing.JButton nextButton;
    private javax.swing.JRadioButton overwriteRadioButton;
    private javax.swing.JCheckBox personSearchCheckBox;
    private javax.swing.JButton previewButton;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JScrollPane previewScrollPane;
    private javax.swing.JTextArea previewTextArea;
    private javax.swing.JCheckBox queryNewNameCheckBox;
    private javax.swing.JRadioButton rejectRadioButton;
    private javax.swing.JComboBox separatingCharacterComboBox;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JCheckBox testOnlyCheckBox;
    private javax.swing.JRadioButton updateRadioButton;
    private javax.swing.JPanel variablesPanel;
    private javax.swing.JScrollPane variablesScrollPane;
    // End of variables declaration//GEN-END:variables
}