/*
 * BackUpInternalFrame.java
 *
 * Created on 29 February 2008, 15:08
 */

package canreg.client.gui;

/**
 *
 * @author  morten
 */
public class BackUpInternalFrame extends javax.swing.JInternalFrame {
    
    /** Creates new form BackUpInternalFrame */
    public BackUpInternalFrame() {
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        performBackupButton = new javax.swing.JButton();
        copyElsewhereButton = new javax.swing.JButton();

        setClosable(true);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getResourceMap(BackUpInternalFrame.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setFrameIcon(resourceMap.getIcon("Form.frameIcon")); // NOI18N
        setName("Form"); // NOI18N

        mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        mainPanel.setName("mainPanel"); // NOI18N

        performBackupButton.setText(resourceMap.getString("performBackupButton.text")); // NOI18N
        performBackupButton.setName("performBackupButton"); // NOI18N

        copyElsewhereButton.setText(resourceMap.getString("copyElsewhereButton.text")); // NOI18N
        copyElsewhereButton.setName("copyElsewhereButton"); // NOI18N

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(performBackupButton, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
            .addComponent(copyElsewhereButton, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(performBackupButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(copyElsewhereButton))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton copyElsewhereButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton performBackupButton;
    // End of variables declaration//GEN-END:variables
    
}