/*
 * PDSEditorInternalFrame.java
 *
 * Created on 29 September 2008, 15:59
 */
package canreg.client.gui.dataentry;

import canreg.client.CanRegClientApp;
import canreg.client.gui.CanRegClientView;
import canreg.client.gui.components.FastFilterInternalFrame;
import canreg.client.gui.tools.ExcelAdapter;
import canreg.common.Globals;
import canreg.server.database.AgeGroupStructure;
import canreg.server.database.PopulationDataset;
import canreg.server.database.PopulationDatasetsEntry;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.w3c.dom.Document;

/**
 *
 * @author  ervikm
 */
public final class PDSEditorInternalFrame extends javax.swing.JInternalFrame implements ActionListener {

    private FastFilterInternalFrame filterWizardInternalFrame;
    private JDesktopPane dtp;
    private Document doc;
    private PopulationDataset pds;
    private JTextField dateTextField;
    private PopulationDataset[] worldPopulations;
    JFreeChart chart;

    /** Creates new form PDSEditorInternalFrame
     * @param dtp
     * @param worldPopulations 
     */
    public PDSEditorInternalFrame(JDesktopPane dtp, PopulationDataset[] worldPopulations) {
        this.dtp = dtp;
        this.worldPopulations = worldPopulations;
        initComponents();
        initValues();
    }

    /**
     * 
     * @param pds
     */
    public void setPopulationDataset(PopulationDataset pds) {
        this.pds = pds;
        if (pds != null) {
            nameTextField.setText(pds.getPopulationDatasetName());
            filterTextField.setText(pds.getFilter());
            sourceTextField.setText(pds.getSource());
            descriptionTextArea.setText(pds.getDescription());
            ageGroupStructureComboBox.setSelectedItem(pds.getAgeGroupStructure());
            if (ageGroupStructureComboBox.getSelectedItem() == null && pds.getAgeGroupStructure() != null) {
                ageGroupStructureComboBox.addItem(pds.getAgeGroupStructure());
                ageGroupStructureComboBox.setSelectedItem(pds.getAgeGroupStructure());
            }
            dateTextField.setText(pds.getDate());
            refreshPopulationDataSetTable();
            lockedToggleButton.setSelected(true);
            if (pds.isWorldPopulationBool()) {
                standardPopulationComboBox.setVisible(false);
                editStandardPopulationButton.setVisible(false);
                standardPopulationLabel.setVisible(false);
            } else {
                boolean found = false;
                PopulationDataset worldPopulation = null;
                int i = 0;
                while (!found && i < worldPopulations.length) {
                    worldPopulation = worldPopulations[i++];
                    if (worldPopulation != null) {
                        found = worldPopulation.getPopulationDatasetID() == pds.getWorldPopulationID();
                    }
                }
                if (worldPopulation != null) {
                    standardPopulationComboBox.setSelectedItem(worldPopulation);
                }
            }
            lockTheFields();
        }
    }

    private void refreshPopulationDataSetTable() {
        AgeGroupStructure ags = (AgeGroupStructure) ageGroupStructureComboBox.getSelectedItem();
        String[] ageGroupNames = ags.getAgeGroupNames();
        String[][] ageGroupLabels = new String[ageGroupNames.length][];

        for (int i = 0; i < ageGroupNames.length; i++) {
            ageGroupLabels[i] = new String[]{ageGroupNames[i]};
        }

        ageGroupLabelsTable.setModel(new DefaultTableModel(ageGroupLabels, new String[]{java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("AGE_GROUP")}) {

            Class[] types = new Class[]{
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        PopulationDatasetsEntry[] ageGroups;
        Object[][] pdsTableData = new Object[ageGroupNames.length][2];

        for (int i = 0; i < ageGroupNames.length; i++) {
            // pdsTableData[i][0] = ageGroupNames[i];
            pdsTableData[i][0] = new Integer(0);
            pdsTableData[i][0] = new Integer(0);
            /// pdsTableData[i][3] = ageGroupNames[i];
        }

        if (pds != null) {
            ageGroups = pds.getAgeGroups();
            for (PopulationDatasetsEntry pdse : ageGroups) {
                if (pdse.getAgeGroup() < pdsTableData.length && pdse.getSex() <= 2) {
                    pdsTableData[pdse.getAgeGroup()][pdse.getSex() - 1] = pdse.getCount();
                } else {
                    Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.WARNING, "{0}{1} {2}", new Object[]{java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("OUTSIDE_SKOPE:_"), pdse.getAgeGroup(), pdse.getSex()});
                }
            }
        }

        pdsTable.setModel(new javax.swing.table.DefaultTableModel(
                pdsTableData,
                new String[]{
                    java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("MALE"), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("FEMALE")
                }) {

            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean[]{
                true, true
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        updateTotals();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        detailsPanel = new javax.swing.JPanel();
        dateLabel = new javax.swing.JLabel();
        dateChooser = new com.toedter.calendar.JDateChooser();
        ageGroupStructureComboBox = new javax.swing.JComboBox();
        ageGroupStructureLabel = new javax.swing.JLabel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        descriptionTextArea = new javax.swing.JTextArea();
        descriptionLabel = new javax.swing.JLabel();
        sourceLabel = new javax.swing.JLabel();
        sourceTextField = new javax.swing.JTextField();
        filterTextField = new javax.swing.JTextField();
        filterWizardButton = new javax.swing.JButton();
        nameTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        filterLabel = new javax.swing.JLabel();
        standardPopulationLabel = new javax.swing.JLabel();
        standardPopulationComboBox = new javax.swing.JComboBox();
        editStandardPopulationButton = new javax.swing.JButton();
        otherAgeGroupStructureButton = new javax.swing.JButton();
        dataSetPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        pdsTable = new javax.swing.JTable();
        totalsTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        ageGroupLabelsTable = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        pyramidPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        pyramidLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        lockedToggleButton = new javax.swing.JToggleButton();
        deleteButton = new javax.swing.JButton();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        detailsPanel1 = new javax.swing.JPanel();
        dateLabel1 = new javax.swing.JLabel();
        dateChooser1 = new com.toedter.calendar.JDateChooser();
        ageGroupStructureComboBox1 = new javax.swing.JComboBox();
        ageGroupStructureLabel1 = new javax.swing.JLabel();
        descriptionScrollPane1 = new javax.swing.JScrollPane();
        descriptionTextArea1 = new javax.swing.JTextArea();
        descriptionLabel1 = new javax.swing.JLabel();
        sourceLabel1 = new javax.swing.JLabel();
        sourceTextField1 = new javax.swing.JTextField();
        filterTextField1 = new javax.swing.JTextField();
        filterWizardButton1 = new javax.swing.JButton();
        nameTextField1 = new javax.swing.JTextField();
        nameLabel1 = new javax.swing.JLabel();
        filterLabel1 = new javax.swing.JLabel();
        standardPopulationLabel1 = new javax.swing.JLabel();
        standardPopulationComboBox1 = new javax.swing.JComboBox();
        editStandardPopulationButton1 = new javax.swing.JButton();
        otherAgeGroupStructureButton1 = new javax.swing.JButton();
        dataSetPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        pdsTable1 = new javax.swing.JTable();
        totalsTable1 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        ageGroupLabelsTable1 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        pyramidLabel1 = new javax.swing.JLabel();
        saveButton1 = new javax.swing.JButton();
        cancelButton1 = new javax.swing.JButton();
        lockedToggleButton1 = new javax.swing.JToggleButton();
        deleteButton1 = new javax.swing.JButton();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(PDSEditorInternalFrame.class, this);
        jMenuItem1.setAction(actionMap.get("saveGraphicsAction")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jPopupMenu1.add(jMenuItem1);

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(PDSEditorInternalFrame.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N
        jTabbedPane1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane1FocusGained(evt);
            }
        });

        detailsPanel.setName("detailsPanel"); // NOI18N

        dateLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dateLabel.setText(resourceMap.getString("dateLabel.text")); // NOI18N
        dateLabel.setName("dateLabel"); // NOI18N

        dateChooser.setName("dateChooser"); // NOI18N

        ageGroupStructureComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ageGroupStructureComboBox.setName("ageGroupStructureComboBox"); // NOI18N
        ageGroupStructureComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ageGroupStructureChanged(evt);
            }
        });

        ageGroupStructureLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ageGroupStructureLabel.setText(resourceMap.getString("ageGroupStructureLabel.text")); // NOI18N
        ageGroupStructureLabel.setName("ageGroupStructureLabel"); // NOI18N

        descriptionScrollPane.setName("descriptionScrollPane"); // NOI18N

        descriptionTextArea.setColumns(20);
        descriptionTextArea.setRows(5);
        descriptionTextArea.setName("descriptionTextArea"); // NOI18N
        descriptionScrollPane.setViewportView(descriptionTextArea);

        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        descriptionLabel.setText(resourceMap.getString("descriptionLabel.text")); // NOI18N
        descriptionLabel.setName("descriptionLabel"); // NOI18N

        sourceLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sourceLabel.setText(resourceMap.getString("sourceLabel.text")); // NOI18N
        sourceLabel.setName("sourceLabel"); // NOI18N

        sourceTextField.setText(resourceMap.getString("sourceTextField.text")); // NOI18N
        sourceTextField.setName("sourceTextField"); // NOI18N

        filterTextField.setText(resourceMap.getString("filterTextField.text")); // NOI18N
        filterTextField.setName("filterTextField"); // NOI18N

        filterWizardButton.setAction(actionMap.get("filterWizardAction")); // NOI18N
        filterWizardButton.setName("filterWizardButton"); // NOI18N

        nameTextField.setText(resourceMap.getString("nameTextField.text")); // NOI18N
        nameTextField.setToolTipText(resourceMap.getString("nameTextField.toolTipText")+Globals.PDS_DATABASE_NAME_LENGTH);
        nameTextField.setName("nameTextField"); // NOI18N

        nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel.setText(resourceMap.getString("nameLabel.text")); // NOI18N
        nameLabel.setName("nameLabel"); // NOI18N

        filterLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        filterLabel.setText(resourceMap.getString("filterLabel.text")); // NOI18N
        filterLabel.setName("filterLabel"); // NOI18N

        standardPopulationLabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        standardPopulationLabel.setText(resourceMap.getString("standardPopulationLabel.text")); // NOI18N
        standardPopulationLabel.setName("standardPopulationLabel"); // NOI18N

        standardPopulationComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        standardPopulationComboBox.setName("standardPopulationComboBox"); // NOI18N

        editStandardPopulationButton.setAction(actionMap.get("editWorldPopulation")); // NOI18N
        editStandardPopulationButton.setName("editStandardPopulationButton"); // NOI18N

        otherAgeGroupStructureButton.setAction(actionMap.get("otherAction")); // NOI18N
        otherAgeGroupStructureButton.setName("otherAgeGroupStructureButton"); // NOI18N

        javax.swing.GroupLayout detailsPanelLayout = new javax.swing.GroupLayout(detailsPanel);
        detailsPanel.setLayout(detailsPanelLayout);
        detailsPanelLayout.setHorizontalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(filterLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sourceLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ageGroupStructureLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(standardPopulationLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailsPanelLayout.createSequentialGroup()
                        .addComponent(standardPopulationComboBox, 0, 389, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editStandardPopulationButton))
                    .addComponent(dateChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(descriptionScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addComponent(sourceTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addComponent(filterTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterWizardButton))
                    .addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 446, Short.MAX_VALUE)
                    .addGroup(detailsPanelLayout.createSequentialGroup()
                        .addComponent(ageGroupStructureComboBox, 0, 379, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherAgeGroupStructureButton)))
                .addContainerGap())
        );
        detailsPanelLayout.setVerticalGroup(
            detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanelLayout.createSequentialGroup()
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterWizardButton)
                    .addComponent(filterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ageGroupStructureComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ageGroupStructureLabel)
                    .addComponent(otherAgeGroupStructureButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editStandardPopulationButton)
                    .addComponent(standardPopulationComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(standardPopulationLabel))
                .addContainerGap(172, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("detailsPanel.TabConstraints.tabTitle"), detailsPanel); // NOI18N

        dataSetPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("dataSetPanel.border.title"))); // NOI18N
        dataSetPanel.setName("dataSetPanel"); // NOI18N
        dataSetPanel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                dataSetPanelFocusLost(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jSplitPane1.setDividerLocation(60);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        jSplitPane3.setBorder(null);
        jSplitPane3.setDividerLocation(250);
        jSplitPane3.setDividerSize(0);
        jSplitPane3.setResizeWeight(0.5);
        jSplitPane3.setName("jSplitPane3"); // NOI18N

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jSplitPane3.setLeftComponent(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jSplitPane3.setRightComponent(jLabel2);

        pdsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)}
            },
            new String [] {
                "Male", "Female"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        pdsTable.setColumnSelectionAllowed(true);
        pdsTable.setName("pdsTable"); // NOI18N
        pdsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        pdsTable.getTableHeader().setReorderingAllowed(false);
        pdsTable.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pdsTablePropertyChange(evt);
            }
        });

        totalsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)}
            },
            new String [] {
                "Male", "Female"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        totalsTable.setEnabled(false);
        totalsTable.setFocusable(false);
        totalsTable.setName("totalsTable"); // NOI18N
        totalsTable.getTableHeader().setReorderingAllowed(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(totalsTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pdsTable, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jSplitPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pdsTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalsTable, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pdsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        pdsTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title1")); // NOI18N
        pdsTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title2")); // NOI18N
        ExcelAdapter myAd = new ExcelAdapter(pdsTable);
        totalsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        totalsTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title1")); // NOI18N
        totalsTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title2")); // NOI18N

        jSplitPane1.setRightComponent(jPanel1);

        jPanel2.setName("jPanel2"); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        ageGroupLabelsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Age group"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ageGroupLabelsTable.setEnabled(false);
        ageGroupLabelsTable.setFocusable(false);
        ageGroupLabelsTable.setName("ageGroupLabelsTable"); // NOI18N
        ageGroupLabelsTable.setRequestFocusEnabled(false);
        ageGroupLabelsTable.getTableHeader().setReorderingAllowed(false);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
            .addComponent(ageGroupLabelsTable, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageGroupLabelsTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(202, Short.MAX_VALUE))
        );

        ageGroupLabelsTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ageGroupLabelsTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title0")); // NOI18N

        jSplitPane1.setLeftComponent(jPanel2);

        jScrollPane1.setViewportView(jSplitPane1);

        javax.swing.GroupLayout dataSetPanelLayout = new javax.swing.GroupLayout(dataSetPanel);
        dataSetPanel.setLayout(dataSetPanelLayout);
        dataSetPanelLayout.setHorizontalGroup(
            dataSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 594, Short.MAX_VALUE)
        );
        dataSetPanelLayout.setVerticalGroup(
            dataSetPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab(resourceMap.getString("dataSetPanel.TabConstraints.tabTitle"), dataSetPanel); // NOI18N

        pyramidPanel.setName("pyramidPanel"); // NOI18N
        pyramidPanel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                pyramidPanelFocusGained(evt);
            }
        });

        jButton1.setAction(actionMap.get("updatePyramid")); // NOI18N
        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        pyramidLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pyramidLabel.setText(resourceMap.getString("pyramidLabel.text")); // NOI18N
        pyramidLabel.setName("pyramidLabel"); // NOI18N
        pyramidLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pyramidLabelMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pyramidPanelLayout = new javax.swing.GroupLayout(pyramidPanel);
        pyramidPanel.setLayout(pyramidPanelLayout);
        pyramidPanelLayout.setHorizontalGroup(
            pyramidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
            .addComponent(pyramidLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
        );
        pyramidPanelLayout.setVerticalGroup(
            pyramidPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pyramidPanelLayout.createSequentialGroup()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pyramidLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("pyramidPanel.TabConstraints.tabTitle"), pyramidPanel); // NOI18N

        saveButton.setAction(actionMap.get("saveAction")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N

        cancelButton.setAction(actionMap.get("cancelAction")); // NOI18N
        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        lockedToggleButton.setAction(actionMap.get("lockedAction")); // NOI18N
        lockedToggleButton.setName("lockedToggleButton"); // NOI18N
        lockedToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockedToggleButtonActionPerformed(evt);
            }
        });

        deleteButton.setAction(actionMap.get("deletePopulationDataSetAction")); // NOI18N
        deleteButton.setName("deleteButton"); // NOI18N

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setMaximizable(true);
        jInternalFrame1.setResizable(true);
        jInternalFrame1.setTitle(resourceMap.getString("jInternalFrame1.title")); // NOI18N
        jInternalFrame1.setFrameIcon(null);
        jInternalFrame1.setName("jInternalFrame1"); // NOI18N

        jTabbedPane2.setName("jTabbedPane2"); // NOI18N
        jTabbedPane2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTabbedPane2FocusGained(evt);
            }
        });

        detailsPanel1.setName("detailsPanel1"); // NOI18N

        dateLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dateLabel1.setText(resourceMap.getString("dateLabel1.text")); // NOI18N
        dateLabel1.setName("dateLabel1"); // NOI18N

        dateChooser1.setName("dateChooser1"); // NOI18N

        ageGroupStructureComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        ageGroupStructureComboBox1.setName("ageGroupStructureComboBox1"); // NOI18N
        ageGroupStructureComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ageGroupStructureComboBox1ageGroupStructureChanged(evt);
            }
        });

        ageGroupStructureLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ageGroupStructureLabel1.setText(resourceMap.getString("ageGroupStructureLabel1.text")); // NOI18N
        ageGroupStructureLabel1.setName("ageGroupStructureLabel1"); // NOI18N

        descriptionScrollPane1.setName("descriptionScrollPane1"); // NOI18N

        descriptionTextArea1.setColumns(20);
        descriptionTextArea1.setRows(5);
        descriptionTextArea1.setName("descriptionTextArea1"); // NOI18N
        descriptionScrollPane1.setViewportView(descriptionTextArea1);

        descriptionLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        descriptionLabel1.setText(resourceMap.getString("descriptionLabel1.text")); // NOI18N
        descriptionLabel1.setName("descriptionLabel1"); // NOI18N

        sourceLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sourceLabel1.setText(resourceMap.getString("sourceLabel1.text")); // NOI18N
        sourceLabel1.setName("sourceLabel1"); // NOI18N

        sourceTextField1.setName("sourceTextField1"); // NOI18N

        filterTextField1.setName("filterTextField1"); // NOI18N

        filterWizardButton1.setAction(actionMap.get("filterWizardAction")); // NOI18N
        filterWizardButton1.setName("filterWizardButton1"); // NOI18N

        nameTextField1.setToolTipText(resourceMap.getString("nameTextField.toolTipText")+Globals.PDS_DATABASE_NAME_LENGTH);
        nameTextField1.setName("nameTextField1"); // NOI18N

        nameLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        nameLabel1.setText(resourceMap.getString("nameLabel1.text")); // NOI18N
        nameLabel1.setName("nameLabel1"); // NOI18N

        filterLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        filterLabel1.setText(resourceMap.getString("filterLabel1.text")); // NOI18N
        filterLabel1.setName("filterLabel1"); // NOI18N

        standardPopulationLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        standardPopulationLabel1.setText(resourceMap.getString("standardPopulationLabel1.text")); // NOI18N
        standardPopulationLabel1.setName("standardPopulationLabel1"); // NOI18N

        standardPopulationComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        standardPopulationComboBox1.setName("standardPopulationComboBox1"); // NOI18N

        editStandardPopulationButton1.setAction(actionMap.get("editWorldPopulation")); // NOI18N
        editStandardPopulationButton1.setName("editStandardPopulationButton1"); // NOI18N

        otherAgeGroupStructureButton1.setAction(actionMap.get("otherAction")); // NOI18N
        otherAgeGroupStructureButton1.setName("otherAgeGroupStructureButton1"); // NOI18N

        javax.swing.GroupLayout detailsPanel1Layout = new javax.swing.GroupLayout(detailsPanel1);
        detailsPanel1.setLayout(detailsPanel1Layout);
        detailsPanel1Layout.setHorizontalGroup(
            detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nameLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                    .addComponent(filterLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(sourceLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(descriptionLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ageGroupStructureLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dateLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(standardPopulationLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailsPanel1Layout.createSequentialGroup()
                        .addComponent(standardPopulationComboBox1, 0, 62, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editStandardPopulationButton1))
                    .addComponent(dateChooser1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(descriptionScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addComponent(sourceTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addGroup(detailsPanel1Layout.createSequentialGroup()
                        .addComponent(filterTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 20, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterWizardButton1))
                    .addComponent(nameTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                    .addGroup(detailsPanel1Layout.createSequentialGroup()
                        .addComponent(ageGroupStructureComboBox1, 0, 52, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(otherAgeGroupStructureButton1)))
                .addContainerGap())
        );
        detailsPanel1Layout.setVerticalGroup(
            detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailsPanel1Layout.createSequentialGroup()
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterWizardButton1)
                    .addComponent(filterTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sourceTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sourceLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(descriptionScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(descriptionLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ageGroupStructureComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ageGroupStructureLabel1)
                    .addComponent(otherAgeGroupStructureButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dateChooser1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailsPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(editStandardPopulationButton1)
                    .addComponent(standardPopulationComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(standardPopulationLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("detailsPanel1.TabConstraints.tabTitle"), detailsPanel1); // NOI18N

        dataSetPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("dataSetPanel1.border.title"))); // NOI18N
        dataSetPanel1.setName("dataSetPanel1"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jSplitPane2.setDividerLocation(60);
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jSplitPane4.setBorder(null);
        jSplitPane4.setDividerLocation(250);
        jSplitPane4.setDividerSize(0);
        jSplitPane4.setResizeWeight(0.5);
        jSplitPane4.setName("jSplitPane4"); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N
        jSplitPane4.setLeftComponent(jLabel5);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText(resourceMap.getString("jLabel6.text")); // NOI18N
        jLabel6.setName("jLabel6"); // NOI18N
        jSplitPane4.setRightComponent(jLabel6);

        pdsTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)}
            },
            new String [] {
                "Male", "Female"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        pdsTable1.setName("pdsTable1"); // NOI18N
        pdsTable1.getTableHeader().setReorderingAllowed(false);
        pdsTable1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                pdsTable1PropertyChange(evt);
            }
        });

        totalsTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)},
                {new Integer(0), new Integer(0)}
            },
            new String [] {
                "Male", "Female"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        totalsTable1.setFocusable(false);
        totalsTable1.setName("totalsTable1"); // NOI18N
        totalsTable1.getTableHeader().setReorderingAllowed(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(totalsTable1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pdsTable1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSplitPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 491, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jSplitPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pdsTable1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalsTable1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pdsTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        pdsTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title1")); // NOI18N
        pdsTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title2")); // NOI18N
        totalsTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        totalsTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title1")); // NOI18N
        totalsTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title2")); // NOI18N

        jSplitPane2.setRightComponent(jPanel4);

        jPanel5.setName("jPanel5"); // NOI18N

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(resourceMap.getString("jLabel7.text")); // NOI18N
        jLabel7.setName("jLabel7"); // NOI18N

        ageGroupLabelsTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Age group"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ageGroupLabelsTable1.setFocusable(false);
        ageGroupLabelsTable1.setName("ageGroupLabelsTable1"); // NOI18N
        ageGroupLabelsTable1.getTableHeader().setReorderingAllowed(false);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText(resourceMap.getString("jLabel8.text")); // NOI18N
        jLabel8.setName("jLabel8"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
            .addComponent(ageGroupLabelsTable1, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ageGroupLabelsTable1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(202, Short.MAX_VALUE))
        );

        ageGroupLabelsTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        ageGroupLabelsTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("pdsTable.columnModel.title0")); // NOI18N

        jSplitPane2.setLeftComponent(jPanel5);

        jScrollPane2.setViewportView(jSplitPane2);

        javax.swing.GroupLayout dataSetPanel1Layout = new javax.swing.GroupLayout(dataSetPanel1);
        dataSetPanel1.setLayout(dataSetPanel1Layout);
        dataSetPanel1Layout.setHorizontalGroup(
            dataSetPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
        );
        dataSetPanel1Layout.setVerticalGroup(
            dataSetPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab(resourceMap.getString("dataSetPanel1.TabConstraints.tabTitle"), dataSetPanel1); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N

        jButton2.setAction(actionMap.get("updatePyramid")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N

        pyramidLabel1.setName("pyramidLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
            .addComponent(pyramidLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pyramidLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 248, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        saveButton1.setAction(actionMap.get("saveAction")); // NOI18N
        saveButton1.setName("saveButton1"); // NOI18N

        cancelButton1.setAction(actionMap.get("cancelAction")); // NOI18N
        cancelButton1.setName("cancelButton1"); // NOI18N

        lockedToggleButton1.setAction(actionMap.get("lockedAction")); // NOI18N
        lockedToggleButton1.setName("lockedToggleButton1"); // NOI18N
        lockedToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockedToggleButton1ActionPerformed(evt);
            }
        });

        deleteButton1.setAction(actionMap.get("deletePopulationDataSetAction")); // NOI18N
        deleteButton1.setName("deleteButton1"); // NOI18N

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jInternalFrame1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deleteButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lockedToggleButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton1)
                .addContainerGap())
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 288, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jInternalFrame1Layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton1)
                    .addComponent(cancelButton1)
                    .addComponent(lockedToggleButton1)
                    .addComponent(deleteButton1))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deleteButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 333, Short.MAX_VALUE)
                .addComponent(lockedToggleButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveButton)
                .addContainerGap())
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 615, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 307, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 308, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveButton)
                    .addComponent(cancelButton)
                    .addComponent(lockedToggleButton)
                    .addComponent(deleteButton))
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 253, Short.MAX_VALUE)
                    .addComponent(jInternalFrame1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 253, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void ageGroupStructureChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ageGroupStructureChanged
    // buildPDSfromTable();
    refreshPopulationDataSetTable();
}//GEN-LAST:event_ageGroupStructureChanged

private void lockedToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockedToggleButtonActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_lockedToggleButtonActionPerformed

private void pdsTablePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pdsTablePropertyChange
    updateTotals();
}//GEN-LAST:event_pdsTablePropertyChange

private void jTabbedPane1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane1FocusGained
    // updatePyramid();
}//GEN-LAST:event_jTabbedPane1FocusGained

private void ageGroupStructureComboBox1ageGroupStructureChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ageGroupStructureComboBox1ageGroupStructureChanged
    // TODO add your handling code here:
}//GEN-LAST:event_ageGroupStructureComboBox1ageGroupStructureChanged

private void pdsTable1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_pdsTable1PropertyChange
    // TODO add your handling code here:
}//GEN-LAST:event_pdsTable1PropertyChange

private void jTabbedPane2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane2FocusGained
    // TODO add your handling code here:
}//GEN-LAST:event_jTabbedPane2FocusGained

private void lockedToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockedToggleButton1ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_lockedToggleButton1ActionPerformed

private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
    updatePyramid();
}//GEN-LAST:event_formComponentResized

private void pyramidPanelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_pyramidPanelFocusGained
    updatePyramid();
}//GEN-LAST:event_pyramidPanelFocusGained

private void dataSetPanelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_dataSetPanelFocusLost
    updatePyramid();
}//GEN-LAST:event_dataSetPanelFocusLost

private void pyramidLabelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pyramidLabelMouseClicked
    jPopupMenu1.show(evt.getComponent(),
            evt.getX(), evt.getY());
}//GEN-LAST:event_pyramidLabelMouseClicked

    /**
     *
     */
    @Action
    public void filterWizardAction() {

        if (filterWizardInternalFrame.getParent() == null) {
            dtp.add(filterWizardInternalFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
            filterWizardInternalFrame.setLocation(dtp.getWidth() / 2 - filterWizardInternalFrame.getWidth() / 2, dtp.getHeight() / 2 - filterWizardInternalFrame.getHeight() / 2);
            filterWizardInternalFrame.setVisible(false);
        }
        if (filterWizardInternalFrame.isVisible()) {
            filterWizardInternalFrame.toFront();
            try {
                filterWizardInternalFrame.setSelected(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            filterWizardInternalFrame.setTextPane("");
            filterWizardInternalFrame.setVisible(true);
        }
    }

    /**
     * 
     */
    public void initValues() {
        // Get the system description
        doc = CanRegClientApp.getApplication().getDatabseDescription();

        // variablesInDB = canreg.common.Tools.getVariableListElements(doc, Globals.NAMESPACE);

        filterWizardInternalFrame = new FastFilterInternalFrame();
        filterWizardInternalFrame.setTableName(Globals.TUMOUR_AND_PATIENT_JOIN_TABLE_NAME);
        filterWizardInternalFrame.setActionListener(this);

        ageGroupStructureComboBox.setModel(new javax.swing.DefaultComboBoxModel(Globals.defaultAgeGroupStructures));
        dateTextField = (JTextField) dateChooser.getDateEditor().getUiComponent();
        dateChooser.setDateFormatString(Globals.DATE_FORMAT_STRING);
        dateChooser.setDate(new Date());
        standardPopulationComboBox.setModel(new javax.swing.DefaultComboBoxModel(worldPopulations));
        refreshPopulationDataSetTable();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass() == FastFilterInternalFrame.class) {
            filterTextField.setText(e.getActionCommand());
        }
    }

    /**
     * 
     */
    @Action
    public void saveAction() {
        lockedToggleButton.setSelected(true);
        lockTheFields();
        buildPDSfromTable();
        try {
            if (pds.getPopulationDatasetID() < 0) {
                CanRegClientApp.getApplication().saveNewPopulationDataset(pds);
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("SUCCESSFULLY_SAVED_PDS:_") + pds.getPopulationDatasetName() + ".", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("PDS_SAVED."), JOptionPane.INFORMATION_MESSAGE);
            } else {
                try {
                    CanRegClientApp.getApplication().deletePopulationDataset(pds.getPopulationDatasetID());
                } catch (SQLException ex) {
                    Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
                CanRegClientApp.getApplication().saveNewPopulationDataset(pds);
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("SUCCESSFULLY_UPDATED_PDS:_") + pds.getPopulationDatasetName() + ".", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("PDS_SAVED."), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SecurityException ex) {
            Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException ex) {
            Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buildPDSfromTable() {
        if (pds == null) {
            pds = new PopulationDataset();
        } else {
            pds.flushAgeGroups();
        }
        pds.setPopulationDatasetName(nameTextField.getText().trim());
        pds.setSource(sourceTextField.getText().trim());
        pds.setFilter(filterTextField.getText().trim());
        pds.setDescription(descriptionTextArea.getText().trim());
        pds.setAgeGroupStructure((AgeGroupStructure) ageGroupStructureComboBox.getSelectedItem());
        pds.setDate(dateTextField.getText());
        pds.setWorldPopulationBool(false);
        PopulationDataset wpds = (PopulationDataset) standardPopulationComboBox.getSelectedItem();
        pds.setWorldPopulationID(wpds.getWorldPopulationID());

        int numberOfAgeGroups = pds.getAgeGroupStructure().getNumberOfAgeGroups();

        if (pdsTable.isEditing()) {
            pdsTable.getCellEditor().stopCellEditing();
        }

        for (int ageGroup = 0; ageGroup < numberOfAgeGroups; ageGroup++) {
            for (int sex = 0; sex <= 1; sex++) {
                Integer count;
                try {

                    count = Integer.parseInt(pdsTable.getValueAt(ageGroup, sex).toString());
                } catch (java.lang.NullPointerException npe) {
                    count = new Integer(0);
                    Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.WARNING, "Missing value in the pds...");
                } catch (java.lang.NumberFormatException nfe) {
                    count = new Integer(0);
                    Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.WARNING, "Error in the pds...");
                }
                pds.addAgeGroup(new PopulationDatasetsEntry(ageGroup, sex + 1, count));
                System.out.println((sex + 1) + " - " + ageGroup + ": " + count);
            }
        }
    }

    /**
     * 
     */
    @Action
    public void editWorldPopulation() {
        PDSEditorInternalFrame populationDatasetEditorInternalFrame = new PDSEditorInternalFrame(dtp, worldPopulations);
        populationDatasetEditorInternalFrame.setPopulationDataset((PopulationDataset) standardPopulationComboBox.getSelectedItem());
        CanRegClientView.showAndPositionInternalFrame(dtp, populationDatasetEditorInternalFrame);
    }

    /**
     * 
     */
    @Action
    public void lockedAction() {
        lockTheFields();
    }

    /**
     * 
     */
    @Action
    public void cancelAction() {
        this.dispose();
    }

    /**
     * 
     */
    @Action
    public void otherAction() {
    }

    private void lockTheFields() {
        ageGroupStructureComboBox.setEnabled(!lockedToggleButton.isSelected());
        dateTextField.setEnabled(!lockedToggleButton.isSelected());
        detailsPanel.setEnabled(!lockedToggleButton.isSelected());
        editStandardPopulationButton.setEnabled(!lockedToggleButton.isSelected());
        filterTextField.setEnabled(!lockedToggleButton.isSelected());
        filterWizardButton.setEnabled(!lockedToggleButton.isSelected());
        ageGroupStructureComboBox.setEnabled(!lockedToggleButton.isSelected());
        editStandardPopulationButton.setEnabled(!lockedToggleButton.isSelected());
        nameTextField.setEnabled(!lockedToggleButton.isSelected());
        pdsTable.setEnabled(!lockedToggleButton.isSelected());
        saveButton.setEnabled(!lockedToggleButton.isSelected());
        sourceTextField.setEnabled(!lockedToggleButton.isSelected());
        standardPopulationComboBox.setEnabled(!lockedToggleButton.isSelected());
        otherAgeGroupStructureButton.setEnabled(!lockedToggleButton.isSelected());
        descriptionTextArea.setEnabled(!lockedToggleButton.isSelected());
        dateChooser.setEnabled(!lockedToggleButton.isSelected());
    }

    @Action
    public void deletePopulationDataSetAction() {
        int result = JOptionPane.showInternalConfirmDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("REALLY_DELETE:_") + pds.getPopulationDatasetName() + ".", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("REALLY_DELETE?"), JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            try {
                CanRegClientApp.getApplication().deletePopulationDataset(pds.getPopulationDatasetID());
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("SUCCESSFULLY_SAVED_PDS:_") + pds.getPopulationDatasetName() + ".", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/PDSEditorInternalFrame").getString("PDS_SAVED."), JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RemoteException ex) {
                Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void buildPyramid() {
        DefaultKeyedValues2DDataset dataset = getDataset();
        chart = ChartFactory.createStackedBarChart(
                null,
                "Age Group", // domain axis label
                "Population", // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, //orientation
                true, // include legend
                true, // tooltips
                false // urls
                );
        BufferedImage image = chart.createBufferedImage(
                //pyramidLabel.getWidth(),
                //pyramidLabel.getHeight()
                this.getWidth() - 30,
                this.getHeight() - 150);
        pyramidLabel.setIcon(new ImageIcon(image));
    }

    private void updateTotals() {
        int maleTotals = 0;
        int femaleTotals = 0;

        for (int i = 0; i < pdsTable.getRowCount(); i++) {
            Object male = pdsTable.getValueAt(i, 0);
            int maleNumber = 0;
            if (male != null) {
                if (male instanceof Integer) {
                    maleNumber = (Integer) male;
                } else {
                    maleNumber = Integer.parseInt(male.toString());
                }
            }
            maleTotals += maleNumber;
            Object female = pdsTable.getValueAt(i, 1);
            int femaleNumber = 0;
            if (female != null) {
                if (female instanceof Integer) {
                    femaleNumber = (Integer) female;
                } else {
                    femaleNumber = Integer.parseInt(female.toString());
                }
            }
            femaleTotals += femaleNumber;

        }
        totalsTable.setValueAt(maleTotals, 0, 0);
        totalsTable.setValueAt(femaleTotals, 0, 1);
    }

    @Action
    public void updatePyramid() {
        buildPyramid();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable ageGroupLabelsTable;
    private javax.swing.JTable ageGroupLabelsTable1;
    private javax.swing.JComboBox ageGroupStructureComboBox;
    private javax.swing.JComboBox ageGroupStructureComboBox1;
    private javax.swing.JLabel ageGroupStructureLabel;
    private javax.swing.JLabel ageGroupStructureLabel1;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cancelButton1;
    private javax.swing.JPanel dataSetPanel;
    private javax.swing.JPanel dataSetPanel1;
    private com.toedter.calendar.JDateChooser dateChooser;
    private com.toedter.calendar.JDateChooser dateChooser1;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dateLabel1;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton deleteButton1;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel descriptionLabel1;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JScrollPane descriptionScrollPane1;
    private javax.swing.JTextArea descriptionTextArea;
    private javax.swing.JTextArea descriptionTextArea1;
    private javax.swing.JPanel detailsPanel;
    private javax.swing.JPanel detailsPanel1;
    private javax.swing.JButton editStandardPopulationButton;
    private javax.swing.JButton editStandardPopulationButton1;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JLabel filterLabel1;
    private javax.swing.JTextField filterTextField;
    private javax.swing.JTextField filterTextField1;
    private javax.swing.JButton filterWizardButton;
    private javax.swing.JButton filterWizardButton1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToggleButton lockedToggleButton;
    private javax.swing.JToggleButton lockedToggleButton1;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel nameLabel1;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField nameTextField1;
    private javax.swing.JButton otherAgeGroupStructureButton;
    private javax.swing.JButton otherAgeGroupStructureButton1;
    private javax.swing.JTable pdsTable;
    private javax.swing.JTable pdsTable1;
    private javax.swing.JLabel pyramidLabel;
    private javax.swing.JLabel pyramidLabel1;
    private javax.swing.JPanel pyramidPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton saveButton1;
    private javax.swing.JLabel sourceLabel;
    private javax.swing.JLabel sourceLabel1;
    private javax.swing.JTextField sourceTextField;
    private javax.swing.JTextField sourceTextField1;
    private javax.swing.JComboBox standardPopulationComboBox;
    private javax.swing.JComboBox standardPopulationComboBox1;
    private javax.swing.JLabel standardPopulationLabel;
    private javax.swing.JLabel standardPopulationLabel1;
    private javax.swing.JTable totalsTable;
    private javax.swing.JTable totalsTable1;
    // End of variables declaration//GEN-END:variables

    private DefaultKeyedValues2DDataset getDataset() {
        DefaultKeyedValues2DDataset dataset = new DefaultKeyedValues2DDataset();

        for (int i = pdsTable.getRowCount() - 1; i >= 0; i--) {

            Object male = pdsTable.getValueAt(i, 0);
            int maleNumber = 0;
            if (male != null) {
                if (male instanceof Integer) {
                    maleNumber = (Integer) male;
                } else {
                    maleNumber = Integer.parseInt(male.toString());
                }
            }
            dataset.addValue(-maleNumber, "Male", ageGroupLabelsTable.getValueAt(i, 0).toString());


            Object female = pdsTable.getValueAt(i, 1);
            int femaleNumber = 0;
            if (female != null) {
                if (female instanceof Integer) {
                    femaleNumber = (Integer) female;
                } else {
                    femaleNumber = Integer.parseInt(female.toString());
                }
            }
            dataset.addValue(femaleNumber, "Female", ageGroupLabelsTable.getValueAt(i, 0).toString());

        }
        return dataset;
    }

    @Action
    public void saveGraphicsAction() {
        if (chart != null) {
            JFileChooser chooser = new JFileChooser();
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith("png");
                }

                @Override
                public String getDescription() {
                    return "PNG graphics files";
                }
            };
            chooser.setFileFilter(filter);
            int result = chooser.showDialog(this, "Choose filename");
            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = chooser.getSelectedFile();
                    if (!file.getName().toLowerCase().endsWith("png")){
                        file = new File(file.getAbsolutePath()+".png");
                    }
                    ChartUtilities.saveChartAsPNG(file, chart, pyramidLabel.getWidth(), pyramidLabel.getHeight());
                } catch (IOException ex) {
                    Logger.getLogger(PDSEditorInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
