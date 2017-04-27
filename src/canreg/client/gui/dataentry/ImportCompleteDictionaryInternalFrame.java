/**
 * CanReg5 - a tool to input, store, check and analyse cancer registry data.
 * Copyright (C) 2008-2017  International Agency for Research on Cancer
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @author Morten Johannes Ervik, CSU/IARC, ervikm@iarc.fr
 */

/*
 * ImportCompleteDictionaryInternalFrame.java
 *
 * Created on 26 September 2008, 14:46
 */
package canreg.client.gui.dataentry;

import canreg.client.CanRegClientApp;
import canreg.client.LocalSettings;
import canreg.client.gui.tools.globalpopup.MyPopUpMenu;
import canreg.common.GlobalToolBox;
import canreg.common.Globals;
import canreg.common.Tools;
import java.awt.Cursor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.jdesktop.application.Task;

/**
 *
 * @author ervikm
 */
public class ImportCompleteDictionaryInternalFrame extends javax.swing.JInternalFrame {

    private final LocalSettings localSettings;
    private final String path;
    private final JFileChooser chooser;
    private final GlobalToolBox globalToolBox;

    /**
     * Creates new form ImportCompleteDictionaryInternalFrame
     */
    public ImportCompleteDictionaryInternalFrame() {
        initComponents();
        localSettings = CanRegClientApp.getApplication().getLocalSettings();
        globalToolBox = CanRegClientApp.getApplication().getGlobalToolBox();
        path = localSettings.getProperty("dictionary_import_path");

        if (path == null) {
            chooser = new JFileChooser();
        } else {
            chooser = new JFileChooser(path);
        }

        // get the available charsets
        SortedMap<String, Charset> charsets = Charset.availableCharsets();
        charsetsComboBox.setModel(new javax.swing.DefaultComboBoxModel(charsets.values().toArray()));
        // set the default mapping
        charsetsComboBox.setSelectedItem(Charset.defaultCharset());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        chooseFilePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        fileNameTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        previewPanel = new javax.swing.JPanel();
        previewScrollPane = new javax.swing.JScrollPane();
        previewTextArea = new javax.swing.JTextArea();
        previewButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cr4dictionaryCheckBox = new javax.swing.JCheckBox();
        importButton = new javax.swing.JButton();
        charsetsComboBox = new javax.swing.JComboBox();
        fileEncodingLabel = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(ImportCompleteDictionaryInternalFrame.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N

        chooseFilePanel.setName("chooseFilePanel"); // NOI18N

        jLabel1.setName("jLabel1"); // NOI18N

        fileNameTextField.setName("fileNameTextField"); // NOI18N
        fileNameTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                fileNameTextFieldMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                fileNameTextFieldMouseReleased(evt);
            }
        });
        fileNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                fileNameTextFieldFocusLost(evt);
            }
        });

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(ImportCompleteDictionaryInternalFrame.class, this);
        browseButton.setAction(actionMap.get("browseAction")); // NOI18N
        browseButton.setName("browseButton"); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        previewPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("previewPanel.border.title"))); // NOI18N
        previewPanel.setEnabled(false);
        previewPanel.setName("previewPanel"); // NOI18N

        previewScrollPane.setFocusable(false);
        previewScrollPane.setName("previewScrollPane"); // NOI18N

        previewTextArea.setColumns(20);
        previewTextArea.setEditable(false);
        previewTextArea.setRows(5);
        previewTextArea.setName("previewTextArea"); // NOI18N
        previewTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                previewTextAreaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                previewTextAreaMouseReleased(evt);
            }
        });
        previewScrollPane.setViewportView(previewTextArea);

        javax.swing.GroupLayout previewPanelLayout = new javax.swing.GroupLayout(previewPanel);
        previewPanel.setLayout(previewPanelLayout);
        previewPanelLayout.setHorizontalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(previewScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
        );
        previewPanelLayout.setVerticalGroup(
            previewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(previewScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
        );

        previewButton.setAction(actionMap.get("previewAction")); // NOI18N
        previewButton.setName("previewButton"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        cr4dictionaryCheckBox.setAction(actionMap.get("canreg4FormatTicked")); // NOI18N
        cr4dictionaryCheckBox.setText(resourceMap.getString("cr4dictionaryCheckBox.text")); // NOI18N
        cr4dictionaryCheckBox.setToolTipText(resourceMap.getString("cr4dictionaryCheckBox.toolTipText")); // NOI18N
        cr4dictionaryCheckBox.setName("cr4dictionaryCheckBox"); // NOI18N

        importButton.setAction(actionMap.get("importAction")); // NOI18N
        importButton.setName("importButton"); // NOI18N

        charsetsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        charsetsComboBox.setName("charsetsComboBox"); // NOI18N

        fileEncodingLabel.setText(resourceMap.getString("fileEncodingLabel.text")); // NOI18N
        fileEncodingLabel.setName("fileEncodingLabel"); // NOI18N

        jButton1.setAction(actionMap.get("autoDetectFileCoding")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N

        javax.swing.GroupLayout chooseFilePanelLayout = new javax.swing.GroupLayout(chooseFilePanel);
        chooseFilePanel.setLayout(chooseFilePanelLayout);
        chooseFilePanelLayout.setHorizontalGroup(
            chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chooseFilePanelLayout.createSequentialGroup()
                        .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(fileNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(browseButton))
                            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                                .addComponent(fileEncodingLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(charsetsComboBox, 0, 96, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addGap(6, 6, 6)
                                .addComponent(cr4dictionaryCheckBox)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(importButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(previewButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        chooseFilePanelLayout.setVerticalGroup(
            chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chooseFilePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3)
                    .addComponent(fileNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton)
                    .addComponent(previewButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(chooseFilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(charsetsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(fileEncodingLabel)
                    .addComponent(importButton)
                    .addComponent(cr4dictionaryCheckBox)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(previewPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooseFilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(chooseFilePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void fileNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_fileNameTextFieldFocusLost
// changeFile();
}//GEN-LAST:event_fileNameTextFieldFocusLost

private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
// TODO add your handling code here:
}//GEN-LAST:event_browseButtonActionPerformed

private void fileNameTextFieldMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileNameTextFieldMousePressed
    MyPopUpMenu.potentiallyShowPopUpMenuTextComponent(fileNameTextField, evt);
}//GEN-LAST:event_fileNameTextFieldMousePressed

private void fileNameTextFieldMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileNameTextFieldMouseReleased
    MyPopUpMenu.potentiallyShowPopUpMenuTextComponent(fileNameTextField, evt);
}//GEN-LAST:event_fileNameTextFieldMouseReleased

private void previewTextAreaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewTextAreaMousePressed
    MyPopUpMenu.potentiallyShowPopUpMenuTextComponent(previewTextArea, evt);
}//GEN-LAST:event_previewTextAreaMousePressed

private void previewTextAreaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_previewTextAreaMouseReleased
    MyPopUpMenu.potentiallyShowPopUpMenuTextComponent(previewTextArea, evt);
}//GEN-LAST:event_previewTextAreaMouseReleased

    /**
     *
     */
    @Action
    public void browseAction() {
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                //set the file name
                fileNameTextField.setText(chooser.getSelectedFile().getCanonicalPath());
                autoDetectFileCoding();
            } catch (IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     */
    @Action
    public void previewAction() {
        // show the contents of the file
        BufferedReader br = null;
        try {
            FileInputStream fis = new FileInputStream(fileNameTextField.getText().trim());
            InputStreamReader isr = new InputStreamReader(fis, (Charset) charsetsComboBox.getSelectedItem());
            br = new BufferedReader(isr);
            int i = 0;
            String text = new String();
            String line = br.readLine();

            while (line != null && i < Globals.NUMBER_OF_LINES_IN_IMPORT_PREVIEW) {
                text += line + "\n";
                line = br.readLine();
                i++;
            }
            previewTextArea.setText(text);
            previewTextArea.setCaretPosition(0);
            previewPanel.setVisible(true);
        } catch (FileNotFoundException fileNotFoundException) {
            JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("COULD_NOT_PREVIEW_THE_FILE:_") + "\'" + fileNameTextField.getText().trim() + "\'.", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("ERROR"), JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, fileNotFoundException);
        } catch (IOException ex) {
            Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     *
     * @return
     */
    @Action
    public Task importAction() {
        // this.dispose();
        return new ImportActionTask(org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class));
    }

    private class ImportActionTask extends org.jdesktop.application.Task<Object, Void> {

        boolean cr4dictionary;
        String fileName;

        ImportActionTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ImportActionTask fields, here.
            super(app);
            browseButton.setEnabled(false);
            importButton.setEnabled(false);
            previewButton.setEnabled(false);
            cr4dictionary = cr4dictionaryCheckBox.isSelected();
            fileName = fileNameTextField.getText().trim();
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            BufferedReader br = null;
            Map<Integer, Map<Integer, String>> allErrors = new TreeMap<Integer, Map<Integer, String>>();
            try {
                FileInputStream fis = new FileInputStream(fileName);
                InputStreamReader isr = new InputStreamReader(fis, (Charset) charsetsComboBox.getSelectedItem());
                br = new BufferedReader(isr);
                String dictionaryString = new String();
                String line = br.readLine();
                int dictionaryID;
                while (line != null) {

                    while (line != null && line.trim().length() == 0) {
                        // skip empty lines
                        line = br.readLine();
                    }
                    if (line == null) {
                        JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("FILE_IS_NOT_CORRECT_FORMAT:_") + "\'" + fileNameTextField.getText().trim() + "\'.", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("ERROR"), JOptionPane.ERROR_MESSAGE);
                        return ("Error");
                    }
                    // first line contains the dictionary id
                    // remove leading #
                    if (line.startsWith("#")) {
                        line = line.substring(1);
                    }

                    line = line.replace(' ', '\t');
                    String[] lineElements = line.split("\t");
                    dictionaryID = Integer.parseInt(lineElements[0]);
                    if (cr4dictionary) {
                        dictionaryID--;
                    }

                    // read next line;
                    line = br.readLine();

                    // read untill blank line
                    while (line != null && line.trim().length() > 0) {
                        if (cr4dictionary) {
                            line = line.replaceFirst("  ", "\t");
                        }
                        dictionaryString += line + "\n";
                        line = br.readLine();
                    }

                    if (dictionaryString.trim().length() > 0) {
                        try {
                            Map<Integer, String> errors = canreg.client.dataentry.DictionaryHelper.testDictionary(null, dictionaryString);
                            if (errors.size() > 0) {
                                allErrors.put(dictionaryID, errors);
                                Logger.getLogger(EditDictionaryInternalFrame.class.getName()).log(Level.WARNING, errors.size() + " errors in dictionary: " + dictionaryID, new Exception());
                            } else {
                                canreg.client.dataentry.DictionaryHelper.replaceDictionary(dictionaryID, dictionaryString, CanRegClientApp.getApplication());
                            }
                            dictionaryString = new String();
                        } catch (RemoteException ex) {
                            Logger.getLogger(EditDictionaryInternalFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    // Read next line 
                    line = br.readLine();
                    // Skip trailing blank lines
                    while (line != null && line.trim().length() == 0) {
                        line = br.readLine();
                    }
                }

                CanRegClientApp.getApplication().refreshDictionary();

            } catch (FileNotFoundException fileNotFoundException) {
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("COULD_NOT_PREVIEW_THE_FILE:_") + fileNameTextField.getText().trim() + "\'.", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("ERROR"), JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(ImportView.class.getName()).log(Level.WARNING, null, fileNotFoundException);
            } catch (IOException ex) {
                Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NumberFormatException nfe) {
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("SOMETHING_WRONG_WITH_THE_DICTIONARY:_") + "\'" + fileNameTextField.getText().trim() + "\'.", java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("ERROR"), JOptionPane.ERROR_MESSAGE);
                Logger.getLogger(ImportView.class.getName()).log(Level.WARNING, null, nfe);
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ImportView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return allErrors;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().
            Map<Integer, Map<Integer, String>> allErrors = (Map<Integer, Map<Integer, String>>) result;
            File file = new File(fileName);
            localSettings.setProperty("dictionary_import_path", file.getParent());
            localSettings.writeSettings();
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
            if (allErrors.isEmpty()) {
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("SUCCESSFULLY_IMPORTED_DICTIONARIES_FROM_FILE."), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("DICTIONARY_SUCCESSFULLY_IMPORTED"), JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), allErrors.size() + java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("_DICTIONARIES_WERE_NOT_IMPORTED..."), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("DICTIONARY_NOT_SUCCESSFULLY_IMPORTED."), JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    @Action
    public void canreg4FormatTicked() {
        if (cr4dictionaryCheckBox.isSelected()) {
            charsetsComboBox.setSelectedItem(globalToolBox.getStandardCharset());
        } else {
            charsetsComboBox.setSelectedItem(Charset.defaultCharset());
        }
    }

    @Action
    public void autoDetectFileCoding() throws IOException {
        String encoding = Tools.detectCharacterCodingOfFile(fileNameTextField.getText());
        if (encoding != null) {
            Charset charset = Charset.forName(encoding);
            charsetsComboBox.setSelectedItem(charset);
            // System.out.println("Detected encoding = " + encoding);
        } else {
            // JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("NO_ENCODING_DETECTED."), java.util.ResourceBundle.getBundle("canreg/client/gui/dataentry/resources/ImportCompleteDictionaryInternalFrame").getString("WARNING"), JOptionPane.WARNING_MESSAGE);
            System.out.println("No encoding detected.");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JComboBox charsetsComboBox;
    private javax.swing.JPanel chooseFilePanel;
    private javax.swing.JCheckBox cr4dictionaryCheckBox;
    private javax.swing.JLabel fileEncodingLabel;
    private javax.swing.JTextField fileNameTextField;
    private javax.swing.JButton importButton;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton previewButton;
    private javax.swing.JPanel previewPanel;
    private javax.swing.JScrollPane previewScrollPane;
    private javax.swing.JTextArea previewTextArea;
    // End of variables declaration//GEN-END:variables
}
