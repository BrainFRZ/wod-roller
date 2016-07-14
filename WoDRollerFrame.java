/**************************************************************************************************
 * Program Name   :
 * Author         : Terry Weiss
 * Date           : May 11, 2016
 * Course/Section :
 * Program Description:
 **************************************************************************************************/

package wodroller;

import java.awt.Dimension;
import javax.swing.JFrame;

public class WoDRollerFrame extends JFrame {
    private WoDRollerFrame() {
        JFrame frame = new JFrame("WoD Roller");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        WoDRollerPanel rollerPanel = new WoDRollerPanel();
        frame.getContentPane().add(rollerPanel);
        frame.getRootPane().setDefaultButton(rollerPanel.getDefaultButton());

        Dimension minSize = new Dimension(650, 675);
        frame.setMinimumSize(minSize);
        frame.setPreferredSize(minSize);
        frame.pack();
        frame.setVisible(true);
    }

    public static WoDRollerFrame launchWoDRoller() {
        return new WoDRollerFrame();
    }
}
