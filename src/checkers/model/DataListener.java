/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package checkers.model;

import java.util.EventListener;

/**
 *
 * @author Kapellan
 */
public interface DataListener extends EventListener {

    void updateTextGuiLanguageInfo(UpdateGuiEvent e);

    void updateGUI(UpdateGuiEvent e);

    void noBlackCkeckersLeft(UpdateGuiEvent e);

    void noWhiteCkeckersLeft(UpdateGuiEvent e);

    void blackIsBlocked(UpdateGuiEvent e);

    void whiteIsBlocked(UpdateGuiEvent e);
}
