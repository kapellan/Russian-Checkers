package checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.RenderingHints;
import java.awt.Dimension;
import java.awt.Font;
import static checkers.ChessBoard.Status.*;

/**
 * This class draws chessboard and sets each cell's attributes - index, status, coordinates on X and Y
 *  
 * @author Kapellan
 */
class ChessBoard extends JPanel {

    /* The number of own and enemy's checkers */
    int whiteCheckers = 12;
    int blackCheckers = 12;
    /* CellSize is a size of chess board cell, it is a square so all sides are same */
    final int CELL_SIZE = 55;
    /* cellSideNumber is using for different kinds of checkers. Russian checkers use chess board 8x8 cells.  */
    private final int CELL_SIDE_NUM = 8;
    final int CELL_NUM = CELL_SIDE_NUM * CELL_SIDE_NUM;
    Cell cells[] = new Cell[CELL_NUM];

    /* Each cell's coordinates (left upper corner) */
    int cX;
    int cY;
    /* Each checker's diameter */
    private int checkerX;
    private int checkerY;
    /* The offset from left and top frame bounds  */
    private final int OFFSET_LEFT_BOUND = -30;
    private final int OFFSET_TOP_BOUND = -30;
    /* Diameter of checkers, inner round of queen */
    private final int CHECKER_DIAMETER = 50;
    private final int QUEEN_INNER_DIAMETER = 40;
    private final int QUEEN_INNER_OFFSET = (CHECKER_DIAMETER - QUEEN_INNER_DIAMETER) / 2;
    private final Dimension PREFERRED_SIZE = new Dimension(CELL_SIDE_NUM * CELL_SIZE + CELL_SIZE, CELL_SIDE_NUM * CELL_SIZE + CELL_SIZE);

    enum Status {

        NILL,
        WC,
        BC,
        WHITE_CH,
        BLACK_CH,
        WHITE_Q,
        BLACK_Q,
        WHITE_ACH,
        BLACK_ACH,
        WHITE_AQ,
        BLACK_AQ,
        TBCH;
    }
    /* Those arrays we use in  makeIndex() method and in painting numbers of chess board in method paint()*/
    private final String LITERALS[] = {"NULL", "a", "b", "c", "d", "e", "f", "g", "h"};//
    private final int REVERS_NUMBERS[] = {0, 8, 7, 6, 5, 4, 3, 2, 1};

    ChessBoard() {

        int cellCount = 0;
        /* The cycle of vertical rows painting */
        for (int vert = 1; vert < CELL_SIDE_NUM + 1; vert++) {
            /* unpaired rows **/
            if (vert % 2 != 0) {
                /* The cycle of horizontal painting. We change black and white squares, also left bottom korner must be grey,
                 *  so we use conditions which check it */
                for (int hor = 1; hor < (CELL_SIDE_NUM + 1); hor++) {
                    cX = OFFSET_LEFT_BOUND + (hor * CELL_SIZE);
                    cY = OFFSET_TOP_BOUND + (vert * CELL_SIZE);
                    /* unpaired cols in unpaired rows are white */
                    if (hor % 2 != 0) {
                        cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, WC);
                        cellCount++;
                    }
                    /* paired cols in paired rows are grey **/
                    if (hor % 2 == 0) {
                        /* black cells we arrange by checkers. Cells upper 5 row - white checkers (enemy), under 4 row - black checkers (own) **/
                        if (vert > (CELL_SIDE_NUM / 2 + 1)) {
                            cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, WHITE_CH);
                            cellCount++;
                        } else if (vert < (CELL_SIDE_NUM / 2)) {
                            cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, BLACK_CH);
                            cellCount++;
                        } else {
                            cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, BC);
                            cellCount++;
                        }
                    }
                }
            } else { /* double rows */
                for (int hor = 1; hor < (CELL_SIDE_NUM + 1); hor++) {
                    cX = OFFSET_LEFT_BOUND + (hor * CELL_SIZE);
                    cY = OFFSET_TOP_BOUND + (vert * CELL_SIZE);
                    /* unpaired cols in paired rows are grey */
                    if (hor % 2 != 0) {
                        /* black cells we arrange by checkers. Cells upper 5 row - white checkers (enemy), under 4 row - black checkers (own) **/
                        if (vert > (CELL_SIDE_NUM / 2 + 1)) {
                            cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, WHITE_CH);
                            cellCount++;
                        } else if (vert < (CELL_SIDE_NUM / 2)) {
                            cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, BLACK_CH);
                            cellCount++;
                        } else {
                            cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, BC);
                            cellCount++;
                        }
                    }
                    /* paired cols in paired rows are white */
                    if (hor % 2 == 0) {
                        cells[cellCount] = new Cell(makeIndex(hor, vert), cX, cY, WC);
                        cellCount++;
                    }
                }
            }
        }
        this.setMinimumSize(PREFERRED_SIZE);
        this.setPreferredSize(PREFERRED_SIZE);
    }// End of constructor

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        Font font = new Font("Dialog", Font.PLAIN, 14);
        g2d.setFont(font);

        /* This cycle make's numbers and literals near chess board bounds */
        for (int i = 1; i < (CELL_SIDE_NUM + 1); i++) {
            /* horizontal number */
            g2d.drawString(Integer.toString(REVERS_NUMBERS[i]), OFFSET_LEFT_BOUND + 40, OFFSET_TOP_BOUND + (i * CELL_SIZE) + 30);
            /* vertical literal */
            g2d.drawString(LITERALS[i], OFFSET_LEFT_BOUND + (i * CELL_SIZE) + 20, OFFSET_TOP_BOUND + 50);
        }

        for (int cCount = 0; cCount < CELL_NUM; cCount++) {
            Cell cell = cells[cCount];
            checkerX = cell.cX + CELL_SIZE / 2 - CHECKER_DIAMETER / 2;
            checkerY = cell.cY + CELL_SIZE / 2 - CHECKER_DIAMETER / 2;

            switch (cell.getStatus()) {
                case WC:
                    paintCell(Color.WHITE, g2d, cell);
                    break;
                case BC:
                    paintCell(Color.GRAY, g2d, cell);
                    break;
                case WHITE_CH:
                    paintChecker(Color.WHITE, g2d, cell);
                    break;
                case BLACK_CH:
                    paintChecker(Color.BLACK, g2d, cell);
                    break;
                case WHITE_ACH:
                    paintChecker(Color.RED, g2d, cell);
                    break;
                case BLACK_ACH:
                    paintChecker(Color.BLUE, g2d, cell);
                    break;
                case WHITE_Q:
                    paintQueen(Color.WHITE, Color.LIGHT_GRAY, g2d, cell);
                    break;
                case BLACK_Q:
                    paintQueen(Color.BLACK, Color.LIGHT_GRAY, g2d, cell);
                    break;
                case WHITE_AQ:
                    paintQueen(Color.WHITE, Color.RED, g2d, cell);
                    break;
                case BLACK_AQ:
                    paintQueen(Color.BLACK, Color.RED, g2d, cell);
                    break;
                case TBCH:
                    paintChecker(Color.GREEN, g2d, cell);
                    break;
            } // end of switch

        }// end of for
        this.setPreferredSize(PREFERRED_SIZE);
        repaint();
    }

    private void paintCell(Color color, Graphics2D g2d, Cell cell) {
        g2d.setPaint(color);
        g2d.fillRect(cell.cX, cell.cY, CELL_SIZE, CELL_SIZE);
    }

    private void paintChecker(Color color, Graphics2D g2d, Cell cell) {
        g2d.setPaint(Color.GRAY);
        g2d.fillRect(cell.cX, cell.cY, CELL_SIZE, CELL_SIZE);
        g2d.setPaint(color);
        g2d.fillOval(checkerX, checkerY, CHECKER_DIAMETER, CHECKER_DIAMETER);
    }

    private void paintQueen(Color color, Color colorInner, Graphics2D g2d, Cell cell) {
        g2d.setPaint(Color.GRAY);
        g2d.fillRect(cell.cX, cell.cY, CELL_SIZE, CELL_SIZE);
        g2d.setPaint(color);
        g2d.fillOval(checkerX, checkerY, CHECKER_DIAMETER, CHECKER_DIAMETER);
        g2d.setPaint(colorInner);
        g2d.fillOval(checkerX + QUEEN_INNER_OFFSET, checkerY + QUEEN_INNER_OFFSET, QUEEN_INNER_DIAMETER, QUEEN_INNER_DIAMETER);
    }

    /** This method make's alphabet-digital index of any cell */
    private String makeIndex(int indexLiteralX, int indexDigitY) {
        return LITERALS[indexLiteralX] + (Integer.toString(REVERS_NUMBERS[indexDigitY]));
    }

    void setCheckersNum() {
        int compNum = 0;
        int userNum = 0;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i].isBlack()) {
                compNum++;
            }
            if (cells[i].isWhite()) {
                userNum++;
            }
        }
        blackCheckers = compNum;
        whiteCheckers = userNum;
    }
}
