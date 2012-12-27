/**
 * CanReg5 - a tool to input, store, check and analyse cancer registry data.
 * Copyright (C) 2008-2013  International Agency for Research on Cancer
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

package canreg.client.gui.tools.globalpopup;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 *
 * @author ervikm
 */
public class MyPopUpMenu extends JPopupMenu {

    public MyPopUpMenu(JTextComponent tc) {
        super();
        add(new CutAction(tc));
        add(new CopyAction(tc));
        add(new PasteAction(tc));
        add(new DeleteAction(tc));
        addSeparator();
        add(new SelectAllAction(tc));
    }

    public static void potentiallyShowPopUpMenuTextComponent(JTextComponent textComponent, MouseEvent evt) {
        if (evt.isPopupTrigger()) {
            Point pt = SwingUtilities.convertPoint(evt.getComponent(), evt.getPoint(), textComponent);
            JPopupMenu menu = new MyPopUpMenu(textComponent);
            menu.show(textComponent, pt.x, pt.y);
        }
    }
}
