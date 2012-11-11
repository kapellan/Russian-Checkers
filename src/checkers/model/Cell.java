package checkers.model;

import static checkers.model.ChessBoardData.Status.*;
import checkers.model.ChessBoardData.Status;

/**
 * This class provides cell's properties
 *
 * @author Kapellan
 */
public class Cell {

    public String index;
    public int cX;
    public int cY;
    private ChessBoardData.Status status;

    public Status getStatus() {
        return this.status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    public boolean isExist() {
        return (status != NILL);
    }

    public boolean isWhite() {
        return (status == WHITE_CH || status == WHITE_ACH || status == WHITE_Q || status == WHITE_AQ);
    }

    public boolean isBlack() {
        return (status == BLACK_CH || status == BLACK_ACH || status == BLACK_Q || status == BLACK_AQ);
    }

    public void resetActive() {
        if (status == WHITE_ACH) {
            status = WHITE_CH;
            return;
        }
        if (status == WHITE_AQ) {
            status = WHITE_Q;
            return;
        }
        if (status == BLACK_ACH) {
            status = BLACK_CH;
            return;
        }
        if (status == BLACK_AQ) {
            status = BLACK_Q;
            return;
        }
    }

    public void setActive() {
        if (status == WHITE_CH) {
            status = WHITE_ACH;
            return;
        }
        if (status == WHITE_Q) {
            status = WHITE_AQ;
            return;
        }
        if (status == BLACK_CH) {
            status = BLACK_ACH;
            return;
        }
        if (status == BLACK_Q) {
            status = BLACK_AQ;
            return;
        }
    }

    public boolean isBlackCell() {
        return (status == BC);
    }

    boolean isBlackQueen() {
        return (status == BLACK_Q);
    }

    boolean isBlackChecker() {
        return (status == BLACK_CH);
    }

    public boolean isBlackActiveQueen() {
        return (status == BLACK_AQ);
    }

    public boolean isBlackActiveChecker() {
        return (status == BLACK_ACH);
    }

    boolean isWhiteQueen() {
        return (status == WHITE_Q);
    }

    public boolean isWhiteActiveQueen() {
        return (status == WHITE_AQ);
    }

    boolean isWhiteChecker() {
        return (status == WHITE_CH);
    }

    public boolean isWhiteActiveChecker() {
        return (status == WHITE_ACH);
    }

    boolean isActive() {
        return (status == WHITE_ACH || status == WHITE_AQ || status == BLACK_ACH || status == BLACK_AQ);
    }

    public boolean isChecker() {
        return (status == WHITE_CH || status == WHITE_ACH || status == BLACK_CH || status == BLACK_ACH);
    }

    public boolean isQueen() {
        return (status == WHITE_Q || status == WHITE_AQ || status == BLACK_Q || status == BLACK_AQ);
    }

    boolean isTurkichChecker() {
        return status == TBCH;
    }

    void setBlackCell() {
        status = BC;
    }

    void setBlackQueen() {
        status = BLACK_Q;
    }

    void setBlackChecker() {
        status = BLACK_CH;
    }

    void setWhiteQueen() {
        status = WHITE_Q;
    }

    void setWhiteChecker() {
        status = WHITE_CH;
    }

    void setTurkichChecker() {
        status = TBCH;
    }

    boolean isOpposite(Cell cell) {
        if (this.isWhite() && cell.isBlack()) {
            return true;
        }
        if (this.isBlack() && cell.isWhite()) {
            return true;
        }
        return false;
    }

    boolean isOwn(Cell cell) {
        if (this.isWhite() && cell.isWhite()) {
            return true;
        }
        if (this.isBlack() && cell.isBlack()) {
            return true;
        }
        return false;
    }

    Cell(String index, int cX, int cY, ChessBoardData.Status status) {
        this.index = index;
        this.cX = cX;
        this.cY = cY;
        this.status = status;
    }

    public Cell(ChessBoardData.Status status) {
        this.status = status;
    }

    public Cell() {
        this.status = NILL;
    }
}