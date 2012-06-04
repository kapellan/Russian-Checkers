package checkers;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/** 
 * This class provides user's actions on chess board
 * 
 * @author Kapellan
 */
class ActionChessBoard implements MouseListener, KeyListener {

    private ChessBoard cBoard;
    private Logic logic;

    ActionChessBoard(ChessBoard cBoard, Logic logic) {
        this.cBoard = cBoard;
        this.logic = logic;
    }

    /** User's click actions */
    public void mousePressed(MouseEvent e) {
        if ((!logic.whiteIsHuman) && (!logic.blackIsHuman)) {
            return;
        }
        if (e.getButton() == 1) {
            Cell clickedCell = logic.getCellByXY(this.cBoard, e.getX(), e.getY());

            /* Во время произведения сессии белых клик по черным ни к чему не приведет */
            if (logic.whiteSessionContinue) {
                if (logic.whiteIsHuman) {
                    if (!(clickedCell.isWhite() || clickedCell.isBlackCell())) {
                        return;
                    }
                }
            }
            if (logic.blackSessionContinue) {
                if (logic.blackIsHuman) {
                    if (!(clickedCell.isBlack() || clickedCell.isBlackCell())) {
                        return;
                    }
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

    private Cell getActiveChecker() {
        for (Cell cell : cBoard.cells) {
            if (logic.whiteIsHuman) {
                if (cell.isWhiteActiveChecker() || cell.isWhiteActiveQueen()) {
                    return cell;
                }
            }
            if (logic.blackIsHuman) {
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
