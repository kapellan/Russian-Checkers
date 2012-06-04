/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers;

import javax.swing.SwingUtilities;

/**
 *
 * @author Kapellan
 */
public class ObservePlayerQueue implements Runnable {

    private Logic logic;

    public void run() {
        try {
            while (!logic.menu.gameOwer) {
                Thread.sleep(1000);
                if ((!logic.whiteIsHuman && logic.whiteSessionContinue)
                        || (!logic.blackIsHuman && logic.blackSessionContinue)) {
                    logic.compStep();
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public ObservePlayerQueue(Logic logic) {
        this.logic = logic;
    }
}
