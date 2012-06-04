package checkers;

import static  checkers.ChessBoard.Status.*;
import checkers.ChessBoard.Status;

/** 
 * This class provides cell's properties
 * 
 * @author Kapellan
 */
class Cell {

    String index;
    int cX;
    int cY;
    private ChessBoard.Status status;

    Status getStatus() {
        return this.status;
    }


    void setStatus(Status status) {
        this.status = status;
    }

    boolean isExist() {
        return (status != NILL);
    }

    boolean isWhite() {
        return (status == WHITE_CH || status == WHITE_ACH || status == WHITE_Q || status == WHITE_AQ);
    }

    boolean isBlack() {
        return (status == BLACK_CH || status == BLACK_Q || status == BLACK_ACH || status == BLACK_AQ);
    }

    void resetActive() {
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

    void setActive() {
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

    boolean isBlackCell() {
        return (status == BC);
    }

    boolean isBlackQueen() {
        return (status == BLACK_Q);
    }

    boolean isBlackChecker() {
        return (status == BLACK_CH);
    }

    boolean isBlackActiveQueen() {
        return (status == BLACK_AQ);
    }

    boolean isBlackActiveChecker() {
        return (status == BLACK_ACH);
    }

    boolean isWhiteQueen() {
        return (status == WHITE_Q);
    }

    boolean isWhiteActiveQueen() {
        return (status == WHITE_AQ);
    }

    boolean isWhiteChecker() {        
        return (status == WHITE_CH);
    }

    boolean isWhiteActiveChecker() {
        return (status == WHITE_ACH);
    }

    boolean isActive() {        
        return (status == WHITE_ACH   || status == WHITE_AQ || status == BLACK_ACH || status == BLACK_AQ);
    }

    boolean isChecker() {
        return (status == WHITE_CH || status == WHITE_ACH || status == BLACK_CH || status == BLACK_ACH);
    }

    boolean isQueen() {
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

    Cell(String index, int cX, int cY, ChessBoard.Status status) {
        this.index = index;
        this.cX = cX;
        this.cY = cY;
        this.status = status;
    }

    public Cell(ChessBoard.Status status) {
        this.status = status;
    }
    public Cell() {
        this.status = NILL;
    }
}