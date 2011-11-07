/**
 * CanReg5 - a tool to input, store, check and analyse cancer registry data.
 * Copyright (C) 2008-2011  International Agency for Research on Cancer
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
 * @author Morten Johannes Ervik, CIN/IARC, ervikm@iarc.fr
 */

/*
 * DatabaseElementsPanel.java
 *
 * Created on 21-Jan-2010, 17:51:08
 */
package canreg.client.gui.management.systemeditor;

import canreg.common.DatabaseElement;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.TreeSet;
import org.jdesktop.application.Action;

/**
 *
 * @author ervikm
 */
public abstract class DatabaseElementsPanel extends javax.swing.JPanel implements ActionListener {

    private ActionListener listener;
    public static String UPDATED = "database_elements_panel_updated";
    protected TreeSet<DatabaseElementPanel> elementPanelsSet;
    // private List<DatabaseElement> databaseElementsList;

    /** Creates new form DatabaseElementsPanel */
    public DatabaseElementsPanel() {
        initComponents();
        elementPanelsSet = new TreeSet<DatabaseElementPanel>(new Comparator<DatabaseElementPanel>() {

            @Override
            public int compare(DatabaseElementPanel o1, DatabaseElementPanel o2) {
                return o1.getPosition() - o2.getPosition();
            }
        });
    }

    public void setActionListener(ActionListener listener) {
        this.listener = listener;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        elementsPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();

        setName("Form"); // NOI18N

        elementsPanel.setName("elementsPanel"); // NOI18N
        elementsPanel.setLayout(new java.awt.GridLayout(0, 1));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(canreg.client.CanRegClientApp.class).getContext().getActionMap(DatabaseElementsPanel.class, this);
        addButton.setAction(actionMap.get("addAction")); // NOI18N
        addButton.setName("addButton"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(addButton))
            .addComponent(elementsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(elementsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(addButton))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Action
    public abstract void addAction();

    public abstract boolean removable(DatabaseElement dbe);

    public void add(DatabaseElement element) {
        DatabaseElementPanel elementPanel = new DatabaseElementPanel(element);
        // elementPanel.setRemovable(removable(element));
        elementPanel.setPosition(elementPanelsSet.size());
        elementPanel.setActionListener(this);
        if (visible(element)) {
            elementsPanel.add(elementPanel);
            elementsPanel.revalidate();
            elementsPanel.repaint();
            elementPanel.setVisible(true);
            elementPanel.setColorSignal(colorize(element));
        } else {
            elementPanel.setVisible(false);
        }
        elementPanelsSet.add(elementPanel);
        listener.actionPerformed(new ActionEvent(this, 0, UPDATED));
        elementPanel.editAction();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JPanel elementsPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(DatabaseElementPanel.REMOVE_ACTION)) {
            elementsPanel.remove((Component) e.getSource());
            elementPanelsSet.remove((DatabaseElementPanel) e.getSource());
            elementsPanel.revalidate();
            elementsPanel.repaint();
        } else if (e.getActionCommand().equals(DatabaseElementPanel.MOVE_UP_ACTION)) {
            DatabaseElementPanel source = (DatabaseElementPanel) e.getSource();
            DatabaseElementPanel lower = elementPanelsSet.lower(source);
            // skip all invisible elements
            while (lower != null && !lower.getDatabaseElement().userVariable()) {
                lower = elementPanelsSet.lower(lower);
            }
            if (lower != null) {
                elementPanelsSet.remove(source);
                elementPanelsSet.remove(lower);
                int tmpPos = source.getPosition();
                source.setPosition(lower.getPosition());
                lower.setPosition(tmpPos);
                elementPanelsSet.add(source);
                elementPanelsSet.add(lower);
            }
            redrawTable();
        } else if (e.getActionCommand().equals(DatabaseElementPanel.MOVE_DOWN_ACTION)) {
            DatabaseElementPanel source = (DatabaseElementPanel) e.getSource();
            DatabaseElementPanel higher = elementPanelsSet.higher(source);
            while (higher != null && !higher.getDatabaseElement().userVariable()) {
                higher = elementPanelsSet.higher(higher);
            }
            if (higher != null) {
                elementPanelsSet.remove(source);
                elementPanelsSet.remove(higher);
                int tmpPos = source.getPosition();
                source.setPosition(higher.getPosition());
                higher.setPosition(tmpPos);
                elementPanelsSet.add(source);
                elementPanelsSet.add(higher);
            }
            redrawTable();
        } else if (e.getActionCommand().equals(DatabaseElementPanel.EDIT_ACTION)) {
            // pass it on
            if (listener != null) {
                listener.actionPerformed(e);
            }
        }
    }

    void setElements(DatabaseElement[] databaseElementsArray) {
        // this.databaseElementsList = java.util.Arrays.asList(databaseElementsArray);
        elementPanelsSet.clear();
        for (DatabaseElement dbe : databaseElementsArray) {
            DatabaseElementPanel elementPanel = new DatabaseElementPanel(dbe);
            elementPanel.setColorSignal(colorize(dbe));
            elementPanel.setPosition(elementPanelsSet.size());
            elementPanel.setActionListener(this);
            elementPanelsSet.add(elementPanel);
        }
        redrawTable();
    }

    public DatabaseElement[] getDatabaseElements() {
        DatabaseElement[] elements = new DatabaseElement[elementPanelsSet.size()];
        int i = 0;
        for (DatabaseElementPanel elementPanel : elementPanelsSet) {
            elements[i] = elementPanel.getDatabaseElement();
            i++;
        }
        return elements;
    }

    public void redrawTable() {
        elementsPanel.removeAll();
        for (DatabaseElementPanel elementPanel : elementPanelsSet) {
            elementPanel.refresh();
            if (visible(elementPanel.getDatabaseElement())) {
                elementsPanel.add(elementPanel);
                elementPanel.setVisible(true);
                elementPanel.setColorSignal(colorize(elementPanel.getDatabaseElement()));
            }
        }
        elementsPanel.revalidate();
        elementsPanel.repaint();
    }

    public abstract Color colorize(DatabaseElement element);

    public abstract boolean visible(DatabaseElement element);
}
