package checkers.controller;

import checkers.model.Cell;
import checkers.model.ChessBoardData;
import checkers.model.DataListener;
import checkers.model.Logic;
import checkers.model.UpdateGuiEvent;
import checkers.view.MainWindow;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Desktop;
import java.net.URI;

/**
 * This class provides user's actions on chess board
 *
 * @author Kapellan
 */
public class ActionChessBoard implements MouseListener, KeyListener, ActionListener, DataListener {

    private Logic logic;
    private ChessBoardData data;
    private MainWindow mainW;

    public ActionChessBoard(ChessBoardData data, Logic logic, MainWindow mainW) {
        this.data = data;
        this.logic = logic;
        this.mainW = mainW;
    }

    /**
     * User's click actions
     */
    public void mousePressed(MouseEvent e) {
        if ((!data.whiteIsHuman) && (!data.blackIsHuman)) {
            return;
        }
        if (e.getButton() == 1) {
            Cell clickedCell = logic.getCellByXY(e.getX(), e.getY());

            /* Во время произведения сессии белых клик по черным ни к чему не приведет */
            if (logic.data.whiteSessionContinue) {
                if (data.whiteIsHuman) {
                    if (!(clickedCell.isWhite() || clickedCell.isBlackCell())) {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (logic.data.blackSessionContinue) {
                if (data.blackIsHuman) {
                    if (!(clickedCell.isBlack() || clickedCell.isBlackCell())) {
                        return;
                    }
                } else {
                    return;
                }
            }

            /* Select checker - paint it red - make it active */
            if (!existActiveChecker()) {
                clickedCell.setActive();
                return;
            }

            /* If was click on active checker - it becomes deactive */
            if (existActiveChecker()) {
                if (getActiveChecker().equals(clickedCell)) {
                    clickedCell.resetActive();
                    return;
                }
            }

            /* If was click on deactive checker, but we hawe active checker, active becomes deactive, and deactive becomes active */
            if (existActiveChecker() && (clickedCell.isChecker() || clickedCell.isQueen())) {
                getActiveChecker().resetActive();
                clickedCell.setActive();
                return;
            }

            /* We activated checker, so second click selects target cell */
            if (existActiveChecker()) {
                Cell activeCell = getActiveChecker();
                Cell targetCell = clickedCell;
                logic.userStep(activeCell, targetCell);
                return;
            }
        }

        /* Right-button mouse click - deactivates any active checker */
        if (e.getButton() == 3) {
            getActiveChecker().resetActive();
            return;
        }


    }

    public void actionPerformed(ActionEvent ev) {
        try {
            if (ev.getActionCommand().equals(data.exitTitle)) {
                data.setGameExit();
                System.exit(0);
            }

            if (ev.getActionCommand().equals(data.aboutTitle)) {
                mainW.showAbout();
            }

            if (ev.getActionCommand().equals(data.rulesTitle)) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(URI.create(data.rulesLink));
            }

            if (ev.getActionCommand().equals(data.newGameTitle)) {
                data.restartGame();
                mainW.modalFrameDispose();
                mainW.clearTextArea();
                mainW.updateTextGuiLabels();
            }

            if (ev.getActionCommand().equals("Русский")) {
                data.setRussianLang();
            }

            if (ev.getActionCommand().equals("English")) {
                data.setEnglishLang();
            }

            if (ev.getActionCommand().equals("Українська")) {
                data.setUkrainianLang();
            }
            if (ev.getActionCommand().equals(data.userVScompTitle)) {
                data.setUSERvsCOMP();
            }
            if (ev.getActionCommand().equals(data.compVSuserTitle)) {
                data.setCOMPvsUSER();
            }
            if (ev.getActionCommand().equals(data.compVScompTitle)) {
                data.setCOMPvsCOMP();
            }
            if (ev.getActionCommand().equals(data.userVSuserTitle)) {
                data.setUSERvsUSER();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void noBlackCkeckersLeft(UpdateGuiEvent e) {
        mainW.noBlackCkeckersLeft();
    }

    public void noWhiteCkeckersLeft(UpdateGuiEvent e) {
        mainW.noWhiteCkeckersLeft();
    }

    public void blackIsBlocked(UpdateGuiEvent e) {
        mainW.blackIsBlocked();
    }

    public void whiteIsBlocked(UpdateGuiEvent e) {
        mainW.whiteIsBlocked();
    }

    public void updateGUI(UpdateGuiEvent e) {
        mainW.updateGui();
    }

    public void updateTextGuiLanguageInfo(UpdateGuiEvent e) {
        mainW.updateTextGuiLanguageInfo();
    }

    private Cell getActiveChecker() {
        for (Cell cell : data.cells) {
            if (data.whiteIsHuman) {
                if (cell.isWhiteActiveChecker() || cell.isWhiteActiveQueen()) {
                    return cell;
                }
            }
            if (data.blackIsHuman) {
                if (cell.isBlackActiveChecker() || cell.isBlackActiveQueen()) {
                    return cell;
                }
            }
        }
        return new Cell();
    }

    private boolean existActiveChecker() {
        if (getActiveChecker().isExist()) {
            return true;
        }
        return false;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
