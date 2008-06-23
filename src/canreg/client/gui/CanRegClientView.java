/*
 * CanRegClientView.java
 */
package canreg.client.gui;

import canreg.client.gui.dataentry.BrowseInternalFrame;
import canreg.client.gui.management.FirstNameSexInternalFrame;
import canreg.client.gui.management.BackUpInternalFrame;
import canreg.client.gui.analysis.ExportFrame;
import canreg.client.gui.dataentry.EditDictionaryInternalFrame;
import canreg.client.gui.dataentry.ImportView;
import canreg.client.*;
import canreg.client.gui.StandardDialog;
import canreg.client.gui.WelcomeInternalFrame;
import canreg.*;
import canreg.client.gui.tools.BareBonesBrowserLaunch;
import canreg.common.Globals;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.rmi.RemoteException;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 * The application's main frame.
 */
public class CanRegClientView extends FrameView {

    private static boolean debug = true;
    LocalSettings localSettings;

    public CanRegClientView(SingleFrameApplication app) {
        super(app);

        initComponents();

        setUserRightsLevel(userRightsLevel);

        // To speed up moving of Frames... 
        // To be moved to a config option?
        // desktopPane.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
        applyPreferences();



        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        // Show the welcome frame...
        if (!CanRegClientApp.getApplication().isLoggedIn()) {
            showWelcomeFrame(this);
        }
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = CanRegClientApp.getApplication().getMainFrame();
            aboutBox = new CanRegClientAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        CanRegClientApp.getApplication().show(aboutBox);
    }

    public void applyPreferences() {
        localSettings = CanRegClientApp.getApplication().getLocalSettings();
        // Apply the outline drag mode
        if (localSettings.isOutlineDragMode()) {
            desktopPane.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
        } else {
            desktopPane.setDragMode(javax.swing.JDesktopPane.LIVE_DRAG_MODE);
        }
        // Apply the settings to main program
        CanRegClientApp.getApplication().applyPreferences();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        browseEditButton = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        startDatabaseServerButton = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        optionsButton = new javax.swing.JButton();
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        logInMenuItem = new javax.swing.JMenuItem();
        logOutMenuItem = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JSeparator();
        dataEntryMenu = new javax.swing.JMenu();
        browseEditMenuItem = new javax.swing.JMenuItem();
        editDictionaryMenuItem = new javax.swing.JMenuItem();
        importDataMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        viewWorkFilesMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        analysisMenu = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JSeparator();
        jMenuItem7 = new javax.swing.JMenuItem();
        managementMenu = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        jMenuItem13 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JSeparator();
        jMenuItem15 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        userLevelLabel = new javax.swing.JLabel();

        mainPanel.setName("mainPanel"); // NOI18N

        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(CanRegClientView.class, this);
        browseEditButton.setAction(actionMap.get("browseEditAction")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(CanRegClientView.class);
        browseEditButton.setText(resourceMap.getString("browseEditButton.text")); // NOI18N
        browseEditButton.setFocusable(false);
        browseEditButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        browseEditButton.setName("browseEditButton"); // NOI18N
        browseEditButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(browseEditButton);

        jSeparator12.setName("jSeparator12"); // NOI18N
        toolBar.add(jSeparator12);

        startDatabaseServerButton.setAction(actionMap.get("startDatabaseServer")); // NOI18N
        startDatabaseServerButton.setFocusable(false);
        startDatabaseServerButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        startDatabaseServerButton.setName("startDatabaseServerButton"); // NOI18N
        startDatabaseServerButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(startDatabaseServerButton);

        jSeparator13.setName("jSeparator13"); // NOI18N
        toolBar.add(jSeparator13);

        optionsButton.setAction(actionMap.get("showOptionFrame")); // NOI18N
        optionsButton.setFocusable(false);
        optionsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        optionsButton.setName("optionsButton"); // NOI18N
        optionsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toolBar.add(optionsButton);

        desktopPane.setAutoscrolls(true);
        desktopPane.setDoubleBuffered(true);
        desktopPane.setMinimumSize(new java.awt.Dimension(400, 300));
        desktopPane.setName("desktopPane"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toolBar, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 859, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(toolBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 652, Short.MAX_VALUE))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        logInMenuItem.setAction(actionMap.get("showLoginFrame")); // NOI18N
        logInMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/canreg/client/gui/resources/Vista-Inspirate_1.0/16x16/actions/forward.png"))); // NOI18N
        logInMenuItem.setName("logInMenuItem"); // NOI18N
        fileMenu.add(logInMenuItem);

        logOutMenuItem.setAction(actionMap.get("logOutaction")); // NOI18N
        logOutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/canreg/client/gui/resources/Vista-Inspirate_1.0/16x16/actions/back.png"))); // NOI18N
        logOutMenuItem.setName("logOutMenuItem"); // NOI18N
        fileMenu.add(logOutMenuItem);

        jSeparator11.setName("jSeparator11"); // NOI18N
        fileMenu.add(jSeparator11);

        dataEntryMenu.setText(resourceMap.getString("dataEntryMenu.text")); // NOI18N
        dataEntryMenu.setName("dataEntryMenu"); // NOI18N

        browseEditMenuItem.setAction(actionMap.get("browseEditAction")); // NOI18N
        browseEditMenuItem.setText(resourceMap.getString("browseMenuItem.text")); // NOI18N
        browseEditMenuItem.setName("browseMenuItem"); // NOI18N
        dataEntryMenu.add(browseEditMenuItem);

        editDictionaryMenuItem.setAction(actionMap.get("editDictionaryAction")); // NOI18N
        editDictionaryMenuItem.setText(resourceMap.getString("dictionaryMenuItem.text")); // NOI18N
        editDictionaryMenuItem.setName("dictionaryMenuItem"); // NOI18N
        dataEntryMenu.add(editDictionaryMenuItem);

        importDataMenuItem.setAction(actionMap.get("importData")); // NOI18N
        importDataMenuItem.setName("importMenuItem"); // NOI18N
        dataEntryMenu.add(importDataMenuItem);

        fileMenu.add(dataEntryMenu);

        jSeparator4.setName("jSeparator4"); // NOI18N
        fileMenu.add(jSeparator4);

        viewWorkFilesMenuItem.setAction(actionMap.get("viewWorkFiles")); // NOI18N
        viewWorkFilesMenuItem.setText(resourceMap.getString("viewWorkFilesMenuItem.text")); // NOI18N
        viewWorkFilesMenuItem.setName("viewWorkFilesMenuItem"); // NOI18N
        fileMenu.add(viewWorkFilesMenuItem);

        jSeparator3.setName("jSeparator3"); // NOI18N
        fileMenu.add(jSeparator3);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        analysisMenu.setText(resourceMap.getString("analysisMenu.text")); // NOI18N
        analysisMenu.setName("analysisMenu"); // NOI18N

        jMenuItem4.setText(resourceMap.getString("frequencyDistributionsMenuItem.text")); // NOI18N
        jMenuItem4.setName("frequencyDistributionsMenuItem"); // NOI18N
        analysisMenu.add(jMenuItem4);

        jMenuItem5.setText(resourceMap.getString("incidenceTablesMenuItem.text")); // NOI18N
        jMenuItem5.setName("incidenceTablesMenuItem"); // NOI18N
        analysisMenu.add(jMenuItem5);

        jSeparator8.setName("jSeparator8"); // NOI18N
        analysisMenu.add(jSeparator8);

        jMenuItem7.setAction(actionMap.get("showExportFrame")); // NOI18N
        jMenuItem7.setText(resourceMap.getString("exportReportsMenuItem.text")); // NOI18N
        jMenuItem7.setName("exportReportsMenuItem"); // NOI18N
        analysisMenu.add(jMenuItem7);

        menuBar.add(analysisMenu);

        managementMenu.setText(resourceMap.getString("managementMenu.text")); // NOI18N
        managementMenu.setName("managementMenu"); // NOI18N

        jMenuItem9.setAction(actionMap.get("backupAction")); // NOI18N
        jMenuItem9.setText(resourceMap.getString("backupMenuItem.text")); // NOI18N
        jMenuItem9.setName("backupMenuItem"); // NOI18N
        managementMenu.add(jMenuItem9);

        jSeparator7.setName("jSeparator7"); // NOI18N
        managementMenu.add(jSeparator7);

        jMenuItem10.setAction(actionMap.get("showNameSexAction")); // NOI18N
        jMenuItem10.setText(resourceMap.getString("nameSexMenuItem.text")); // NOI18N
        jMenuItem10.setName("nameSexMenuItem"); // NOI18N
        managementMenu.add(jMenuItem10);

        jMenuItem11.setText(resourceMap.getString("duplicateSearchMenuItem.text")); // NOI18N
        jMenuItem11.setName("duplicateSearchMenuItem"); // NOI18N
        managementMenu.add(jMenuItem11);

        jSeparator6.setName("jSeparator6"); // NOI18N
        managementMenu.add(jSeparator6);

        jMenuItem12.setText(resourceMap.getString("usersMenuItem.text")); // NOI18N
        jMenuItem12.setName("usersMenuItem"); // NOI18N
        managementMenu.add(jMenuItem12);

        jSeparator10.setName("jSeparator10"); // NOI18N
        managementMenu.add(jSeparator10);

        jMenu1.setText(resourceMap.getString("advancedMenu.text")); // NOI18N
        jMenu1.setName("advancedMenu"); // NOI18N

        jMenuItem18.setAction(actionMap.get("showUsersLoggedIn")); // NOI18N
        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/canreg/client/gui/resources/Vista-Inspirate_1.0/16x16/actions/kontact_contacts.png"))); // NOI18N
        jMenuItem18.setName("showUsersLoggedInMenuItem"); // NOI18N
        jMenu1.add(jMenuItem18);

        jMenuItem16.setAction(actionMap.get("startDatabaseServer")); // NOI18N
        jMenuItem16.setText(resourceMap.getString("startDBMenuItem.text")); // NOI18N
        jMenuItem16.setName("startDBMenuItem"); // NOI18N
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem16);

        jMenuItem17.setAction(actionMap.get("stopDatabaseServer")); // NOI18N
        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/canreg/client/gui/resources/Vista-Inspirate_1.0/16x16/actions/stop.png"))); // NOI18N
        jMenuItem17.setText(resourceMap.getString("stopDBMenuItem.text")); // NOI18N
        jMenuItem17.setName("stopDBMenuItem"); // NOI18N
        jMenu1.add(jMenuItem17);

        managementMenu.add(jMenu1);

        jSeparator5.setName("jSeparator5"); // NOI18N
        managementMenu.add(jSeparator5);

        jMenuItem13.setAction(actionMap.get("showOptionFrame")); // NOI18N
        jMenuItem13.setName("optionsMenuItem"); // NOI18N
        managementMenu.add(jMenuItem13);

        menuBar.add(managementMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        jMenuItem14.setAction(actionMap.get("showCanRegHelpFile")); // NOI18N
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/canreg/client/gui/resources/Vista-Inspirate_1.0/16x16/actions/help.png"))); // NOI18N
        jMenuItem14.setText(resourceMap.getString("helpMenuItem.text")); // NOI18N
        jMenuItem14.setName("helpMenuItem"); // NOI18N
        helpMenu.add(jMenuItem14);

        jSeparator1.setName("jSeparator1"); // NOI18N
        helpMenu.add(jSeparator1);

        jMenu4.setText(resourceMap.getString("linksMenu.text")); // NOI18N
        jMenu4.setName("linksMenu"); // NOI18N

        jMenuItem6.setAction(actionMap.get("openIacrWebsite")); // NOI18N
        jMenuItem6.setText(resourceMap.getString("iacrWebsiteMenuItem.text")); // NOI18N
        jMenuItem6.setName("iacrWebsiteMenuItem"); // NOI18N
        jMenu4.add(jMenuItem6);

        jMenuItem1.setAction(actionMap.get("openENCRweb")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenu4.add(jMenuItem1);

        jMenuItem19.setAction(actionMap.get("openICDO3web")); // NOI18N
        jMenuItem19.setName("icdo3MenuItem"); // NOI18N
        jMenu4.add(jMenuItem19);

        helpMenu.add(jMenu4);

        jSeparator9.setName("jSeparator9"); // NOI18N
        helpMenu.add(jSeparator9);

        jMenuItem15.setAction(actionMap.get("openICDO3Manual")); // NOI18N
        jMenuItem15.setText(resourceMap.getString("jMenuItem15.text")); // NOI18N
        jMenuItem15.setName("jMenuItem15"); // NOI18N
        helpMenu.add(jMenuItem15);

        jSeparator2.setName("jSeparator2"); // NOI18N
        helpMenu.add(jSeparator2);

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/canreg/client/gui/resources/LogoBetaNew16x16.png"))); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        userLevelLabel.setText(resourceMap.getString("userLevelLabel.text")); // NOI18N
        userLevelLabel.setName("userLevelLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userLevelLabel)
                .addGap(91, 91, 91)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addComponent(statusMessageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 531, Short.MAX_VALUE)
                        .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusAnimationLabel)
                        .addContainerGap())))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11))
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userLevelLabel)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
        setToolBar(toolBar);
    }// </editor-fold>//GEN-END:initComponents
    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
    // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void showWelcomeFrame(FrameView fv) {
        WelcomeInternalFrame welcomeInternalFrame = new WelcomeInternalFrame(fv);

        desktopPane.add(welcomeInternalFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
        // System.out.println("coucou");
        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        welcomeInternalFrame.setVisible(true);
        welcomeInternalFrame.setLocation(scr.width / 2 - welcomeInternalFrame.getWidth() / 2, scr.height / 2 - welcomeInternalFrame.getHeight() / 2 - 142);
        // debugOut(mainFrame.getWidth() / 2 + " " + mainFrame.getHeight() / 2);
        
        welcomeInternalFrame.setDesktopPane(desktopPane);

    }

    private class OpenICDO3ManualTask extends org.jdesktop.application.Task<Object, Void> {

        OpenICDO3ManualTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to OpenICDO3ManualTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() throws IOException {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            if (System.getProperty("os.name").toString().substring(0, 3).equalsIgnoreCase("win")) {
                File file = new File("c:\\admtools.chm");
                Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
            }

            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
        }
    }

    @Action
    public void showLoginFrame() {
        if (CanRegClientApp.getApplication().isLoggedIn()) {
            int i = JOptionPane.showInternalConfirmDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Do want to log out of the current CanReg system?", "Already logged in.", JOptionPane.YES_NO_OPTION);
            if (i == 0) {
                logOut();
            }
        }
        // If still logged in
        if (CanRegClientApp.getApplication().isLoggedIn()) {
            JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Already logged in.", "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LoginInternalFrame loginInternalFrame = new LoginInternalFrame(this, desktopPane);
            showAndCenterInternalFrame(desktopPane, loginInternalFrame);


        }
    }

    @Action
    public void logOutaction() {
        int i = JOptionPane.showInternalConfirmDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), "Do you really want to log out?", "Log out?", JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            logOut();
        }
    }

    @Action
    public Task viewWorkFiles() {
        return new ViewWorkFilesTask(getApplication());
    }

    private class ViewWorkFilesTask extends org.jdesktop.application.Task<Object, Void> {

        ViewWorkFilesTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to ViewWorkFilesTask fields, here.
            super(app);
        }

        @Override
        protected Object doInBackground() throws IOException {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            if (System.getProperty("os.name").toString().substring(0, 3).equalsIgnoreCase("win")) {
                File file = new File(localSettings.getProperty(localSettings.workingDirPathKey));
                Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
            }

            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
        }
    }

    private static void debugOut(String msg) {
        if (debug) {
            System.out.println("\t[CanRegClientView] " + msg);
        }
    }

    @Action
    public void showUsersLoggedIn() {
        try {
            // Get list of users...
            String[] list = CanRegClientApp.getApplication().listUsersLoggedIn();

            // format the list
            String users = "";
            for (int i = 0; i < list.length; i++) {
                users += list[i] + "\n";
            }
            // send some debug out
            debugOut(users);

            // show the dialog box
            StandardDialog sd = new StandardDialog(getFrame(), true, "Users logged in", users);
            sd.setLocationRelativeTo(null);
            sd.setVisible(true);

        } catch (RemoteException re) {

        } catch (SecurityException se) {

        }
    }

    @Action
    public Task startDatabaseServer() {
        return new StartDatabaseServerTask(getApplication());
    }

    private class StartDatabaseServerTask extends org.jdesktop.application.Task<Object, Void> {

        StartDatabaseServerTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to StartDatabaseServerTask fields, here.
            super(app);
            try {
                CanRegClientApp.getApplication().startDatabaseServer();
                JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/resources/CanRegClientView").getString("Database_server_started."), "Message", JOptionPane.INFORMATION_MESSAGE);

            } catch (RemoteException ex) {
                Logger.getLogger(CanRegClientView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(CanRegClientView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        protected Object doInBackground() {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.
            return null;  // return your result
        }

        @Override
        protected void succeeded(Object result) {
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
        }
    }

    @Action
    public void stopDatabaseServer() {
        try {
            CanRegClientApp.getApplication().stopDatabaseServer();
            JOptionPane.showInternalMessageDialog(CanRegClientApp.getApplication().getMainFrame().getContentPane(), java.util.ResourceBundle.getBundle("canreg/client/gui/resources/CanRegClientView").getString("Database_server_stopped."), "Message", JOptionPane.INFORMATION_MESSAGE);

        } catch (RemoteException ex) {
            Logger.getLogger(CanRegClientView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(CanRegClientView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setUserRightsLevel(int userRightsLevel) {
        this.userRightsLevel = userRightsLevel;
        userLevelLabel.setText(Globals.USER_RIGHT_LEVELS[userRightsLevel]);

        boolean analysis = false;
        boolean mangement = false;
        boolean dataEntry = false;
        boolean loggedIn = false;

        // Hide/reveal menus
        if (userRightsLevel == 0) {
            analysis = false;
            mangement = false;
            dataEntry = false;
            loggedIn = false;
        } else {
            loggedIn = true;
            if (userRightsLevel < 2) {
                mangement = true;
            }
            if (userRightsLevel < 3) {
                dataEntry = true;
            }
            if (userRightsLevel < 4) {
                analysis = true;
            }
        }
        toolBar.setVisible(loggedIn);
        analysisMenu.setEnabled(analysis);
        managementMenu.setEnabled(mangement);
        dataEntryMenu.setEnabled(dataEntry);
        logOutMenuItem.setEnabled(loggedIn);
    }

    @Action
    public void ShowPatient() {
    // PatientFrame1.setVisible(!PatientFrame1.isVisible());
    }

    @Action
    public Task openICDO3Manual() {
        return new OpenICDO3ManualTask(getApplication());
    }

    private void logOut() {
        CanRegClientApp.getApplication().logOut();
        getFrame().setTitle("CanReg5 - Not logged in.");
        userLevelLabel.setText("Not logged in.");
    }

    @Action
    public void openIacrWebsite() throws IOException {
        BareBonesBrowserLaunch.openURL(java.util.ResourceBundle.getBundle("canreg/client/gui/resources/CanRegClientView").getString("http://www.iacr.com.fr/"));
    }

    @Action
    public void openICDO3web() {
        BareBonesBrowserLaunch.openURL(java.util.ResourceBundle.getBundle("canreg/client/gui/resources/CanRegClientView").getString("http://training.seer.cancer.gov/module_icdo3/icdo3_home.html"));
    }

    @Action
    public void showCanRegHelpFile() {
        File file = new File("doc");
        BareBonesBrowserLaunch.openURL("file:" + file.getAbsolutePath() + "/CanReg5-functionality.htm");
    }

    @Action
    public void openENCRweb() {
        BareBonesBrowserLaunch.openURL(java.util.ResourceBundle.getBundle("canreg/client/gui/resources/CanRegClientView").getString("http://www.encr.com.fr/"));
    }

    @Action
    public void importData() {
        ImportView importInternalFrame = new ImportView();
        showAndCenterInternalFrame(desktopPane, importInternalFrame);
    }

    @Action
    public void showExportFrame() {
        JInternalFrame internalFrame = new ExportFrame(desktopPane);
        showAndCenterInternalFrame(desktopPane, internalFrame);
    }

    @Action
    public void browseEditAction() {
        JInternalFrame internalFrame = new BrowseInternalFrame(desktopPane);
        showAndCenterInternalFrame(desktopPane, internalFrame);
    }

    @Action
    public void backupAction() {
        JInternalFrame internalFrame = new BackUpInternalFrame();
        showAndCenterInternalFrame(desktopPane, internalFrame);
    }

    @Action
    public void editDictionaryAction() {
        JInternalFrame internalFrame = new EditDictionaryInternalFrame();
        showAndCenterInternalFrame(desktopPane, internalFrame);
    }

    @Action
    public void showOptionFrame() {
        JInternalFrame internalFrame = new OptionsFrame(this);
        showAndCenterInternalFrame(desktopPane, internalFrame);
    }

    @Action
    public void showNameSexAction() {
        JInternalFrame internalFrame = new FirstNameSexInternalFrame();
        showAndCenterInternalFrame(desktopPane, internalFrame);
    }

    public static void showAndCenterInternalFrame(JDesktopPane desktopPane, JInternalFrame internalFrame) {
        desktopPane.add(internalFrame, javax.swing.JLayeredPane.DEFAULT_LAYER);
        internalFrame.setVisible(true);
        int posX = Math.max(desktopPane.getWidth() / 2 - internalFrame.getWidth() / 2 ,0);
        int posY = Math.max(desktopPane.getHeight() / 2 - internalFrame.getHeight() / 2,0);
        internalFrame.setLocation(posX,posY);
        //CanRegClientApp.getApplication().getMainFrame();
        internalFrame.setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu analysisMenu;
    private javax.swing.JButton browseEditButton;
    private javax.swing.JMenuItem browseEditMenuItem;
    private javax.swing.JMenu dataEntryMenu;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem editDictionaryMenuItem;
    private javax.swing.JMenuItem importDataMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JMenuItem logInMenuItem;
    private javax.swing.JMenuItem logOutMenuItem;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenu managementMenu;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JButton optionsButton;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JButton startDatabaseServerButton;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JLabel userLevelLabel;
    private javax.swing.JMenuItem viewWorkFilesMenuItem;
    // End of variables declaration//GEN-END:variables

    // private javax.swing.JInternalFrame internalFrame;
    // private javax.swing.JInternalFrame welcomeInternalFrame;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private int userRightsLevel = 0;
}
