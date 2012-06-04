package checkers;

import java.util.ArrayList;
import java.awt.HeadlessException;
import java.util.Random;
import static checkers.Logic.Action.*;
import static checkers.Logic.Player.*;
import static checkers.Logic.Direction.*;

/** 
 * This class provides game logic - move and fight. 
 * 
 * @author Kapellan
 */
class Logic {

    Menu menu;
    boolean whiteIsHuman = true;
    boolean blackIsHuman = true;
    boolean whiteSessionContinue = true;
    boolean blackSessionContinue = false;
    boolean steelFighterFlag = false;
    private ChessBoard cBoard;
    private String userResultCheckersNum;
    private ArrayList<Cell> turkishArr = new ArrayList<Cell>();

    enum Direction {

        RU(1, -1),
        RB(1, 1),
        LB(-1, 1),
        LU(-1, -1);
        private final int kX;
        private final int kY;

        boolean isWhiteD() {
            return (this == RU || this == LU);
        }

        boolean isBlackD() {
            return (this == RB || this == LB);
        }

        boolean isExist() {
            return (this == RU || this == LU || this == RB || this == LB);
        }

        Direction(int kX, int kY) {
            this.kX = kX;
            this.kY = kY;
        }
    }

    enum Action {

        MOVE, FIGHT;

        boolean isFight() {
            return this == FIGHT;
        }

        boolean isMove() {
            return this == MOVE;
        }
    }

    enum Player {

        WHITE, BLACK;

        boolean isWhite() {
            return this == WHITE;
        }

        boolean isBlack() {
            return this == BLACK;
        }
    }

    public void compStep() {
        try {
            Player computer;
            if ((!whiteIsHuman) && whiteSessionContinue) {
                computer = WHITE;
            } else if ((!blackIsHuman) && blackSessionContinue) {
                computer = BLACK;
            } else {
                throw new PlayerErrorException();
            }
            if (whiteSessionContinue) {
                menu.resultBuf = menu.stepWhiteText + "\n";
            } else {
                menu.resultBuf = menu.stepBlackText + "\n";
            }

            Cell activeCell;
            Cell targetCell;
            Cell victimCell;
            // First we must fight */
            if (getSome(this.cBoard, FIGHT, computer).isExist()) {
                do {
                    Thread.currentThread().sleep(500);
                    Cell actCells[] = getBestEnemy(computer);
                    activeCell = actCells[0];
                    targetCell = actCells[1];
                    victimCell = getVictim(this.cBoard, activeCell, targetCell, getDbyTarget(this.cBoard, activeCell, targetCell));
                    turkishArr.add(victimCell);
                    victimCell.setTurkichChecker();
                    activeCell.resetActive();
                    targetCell.setStatus(activeCell.getStatus());
                    activeCell.setBlackCell();
                    checkQeen(targetCell);
                    activeCell = targetCell;
                    if (!steelFighterFlag) {
                        menu.resultBuf += actCells[0].index + ":" + actCells[1].index;
                    } else {
                        menu.resultBuf += ":" + activeCell.index;
                    }
                    if (isSome(this.cBoard, activeCell, FIGHT)) {
                        activeCell.setActive();
                        steelFighterFlag = true;
                    } else {
                        activeCell.resetActive();
                        steelFighterFlag = false;
                        resetTurkishArr();
                        changeSession();
                        menu.resultBuf += "\n";
                        menu.customResult();
                        return;
                    }
                } while (steelFighterFlag);
            } else {
                // If there is no fighter checker we move 
                Cell actCells[] = getBestEnemy(computer);
                activeCell = actCells[0];
                targetCell = actCells[1];
                Thread.currentThread().sleep(500);
                targetCell.setStatus(activeCell.getStatus());
                targetCell.resetActive();
                checkQeen(targetCell);
                activeCell.setBlackCell();
                menu.resultBuf += activeCell.index + ":" + targetCell.index + "\n";
                menu.customResult();
                menu.tArea.setCaretPosition(menu.tArea.getDocument().getLength());
                changeSession();
                return;
            }
        } catch (PlayerErrorException e) {
            return;
        } catch (NoSuchDirectionException e) {
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (HeadlessException e) {
            e.printStackTrace();
        }

    }

    void userStep(Cell activeCell, Cell targetCell) {
        try {
            Player player;
            if (activeCell.isWhite()) {
                player = WHITE;
                menu.resultBuf = menu.stepWhiteText + "\n";
            } else if (activeCell.isBlack()) {
                player = BLACK;
                menu.resultBuf = menu.stepBlackText + "\n";
            } else {
                throw new PlayerErrorException();
            }

            Direction d = getDbyTarget(this.cBoard, activeCell, targetCell);

            if (getSome(this.cBoard, FIGHT, player).isExist() && !isSome(this.cBoard, activeCell, FIGHT)) {
                menu.resultBuf += menu.userHasFighterText + " " + getSome(this.cBoard, FIGHT, player).index + "\n";
                menu.customResult();
                return;
            }
            //First we must fight /
            if (isRightActByD(this.cBoard, activeCell, targetCell, d, FIGHT)) {
                Cell victimCell = getVictim(this.cBoard, activeCell, targetCell, d);
                turkishArr.add(victimCell);
                victimCell.setTurkichChecker();
                activeCell.resetActive();
                targetCell.setStatus(activeCell.getStatus());
                activeCell.setBlackCell();
                checkQeen(targetCell);
                if (!steelFighterFlag) {
                    userResultCheckersNum = activeCell.index;
                }
                activeCell = targetCell;
                if (isSome(this.cBoard, activeCell, FIGHT)) {
                    steelFighterFlag = true;
                    activeCell.setActive();
                    userResultCheckersNum += ":" + targetCell.index;
                    return;
                } else {
                    activeCell.resetActive();
                    resetTurkishArr();
                    menu.resultBuf += userResultCheckersNum + ":" + targetCell.index + "\n";
                    userResultCheckersNum = "";
                    menu.customResult();
                    steelFighterFlag = false;
                    changeSession();
                    return;
                }
            }

            // Check for a fighter 
            if (isSome(this.cBoard, activeCell, FIGHT)) {
                menu.resultBuf += menu.userMustFightText + "\n";
                return;
            }

            // If there is no fighter checker we move 
            if (isRightActByD(this.cBoard, activeCell, targetCell, d, MOVE)) {
                targetCell.setStatus(activeCell.getStatus());
                targetCell.resetActive();
                checkQeen(targetCell);
                activeCell.setBlackCell();
                menu.resultBuf += activeCell.index + ":" + targetCell.index + "\n";
                menu.customResult();
                menu.tArea.setCaretPosition(menu.tArea.getDocument().getLength());
                changeSession();
                return;
            }

        } catch (PlayerErrorException e) {
            return;
        } catch (NoSuchDirectionException e) {
            return;
        }
    }

    private void changeSession() {
        whiteSessionContinue = !whiteSessionContinue;
        blackSessionContinue = !blackSessionContinue;
    }

    private Cell getVictim(ChessBoard xBoard, Cell activeCell, Cell targetCell, Direction d) {
        if (activeCell.isChecker()) {
            if (targetCell.equals(getCellByD(xBoard, activeCell, d, 2))) {
                return getCellByD(xBoard, activeCell, d, 1);
            }
        }
        if (activeCell.isQueen()) {
            int deep = 1;
            while (getCellByD(xBoard, activeCell, d, deep).isBlackCell()) {
                deep++;
            }
            if (getCellByD(xBoard, activeCell, d, deep).isOpposite(activeCell)) {
                if (getCellByD(xBoard, activeCell, d, deep + 1).isBlackCell()) {
                    return getCellByD(xBoard, activeCell, d, deep);
                }
            }
        }

        return new Cell();
    }

    private Cell[] getBestEnemy(Player computer) throws NoSuchDirectionException {
        Action act;
        ArrayList<Cell> variants = new ArrayList<Cell>();
        Cell activeCell = new Cell();
        //check for active checker
        if (computer.isWhite()) {
            for (int i = 0; i < cBoard.cells.length; i++) {
                if (cBoard.cells[i].isWhiteActiveChecker() || cBoard.cells[i].isWhiteActiveQueen()) {
                    activeCell = cBoard.cells[i];
                }
            }
        } else if (computer.isBlack()) {
            for (int i = 0; i < cBoard.cells.length; i++) {
                if (cBoard.cells[i].isBlackActiveChecker() || cBoard.cells[i].isBlackActiveQueen()) {
                    activeCell = cBoard.cells[i];
                }
            }
        }
        if (activeCell.isExist()) {
            act = FIGHT;
        } else {
            if (getSome(this.cBoard, FIGHT, computer).isExist()) {
                act = FIGHT;
            } else {
                act = MOVE;
            }
        }

        if (activeCell.isExist()) {
            variants.add(activeCell);
        } else {
            if (computer.isWhite()) {
                for (int i = 0; i < cBoard.cells.length; i++) {
                    if (isSome(this.cBoard, cBoard.cells[i], act) && cBoard.cells[i].isWhite()) {
                        variants.add(cBoard.cells[i]);
                    }
                }
            } else {
                for (int i = 0; i < cBoard.cells.length; i++) {
                    if (isSome(this.cBoard, cBoard.cells[i], act) && cBoard.cells[i].isBlack()) {
                        variants.add(cBoard.cells[i]);
                    }
                }
            }
        }

        Cell targets[][] = new Cell[variants.size()][];
        int points[][] = new int[variants.size()][];

        // Fill the targets by variants 
        for (int v = 0; v < variants.size(); v++) {
            ArrayList<Cell> tmpTargets = new ArrayList<Cell>();
            for (Direction d : Direction.values()) {
                int deep = 1;
                while (getCellByD(this.cBoard, variants.get(v), d, deep).isExist()) {
                    Cell targetCell = getCellByD(this.cBoard, variants.get(v), d, deep);
                    if (isRightActByD(this.cBoard, variants.get(v), targetCell, d, act)) {
                        tmpTargets.add(targetCell);
                    }
                    deep++;
                }
            }
            targets[v] = tmpTargets.toArray(new Cell[tmpTargets.size()]);
            points[v] = new int[tmpTargets.size()];
        }

        // set points of any target
        for (int v = 0; v < targets.length; v++) {
            for (int t = 0; t < targets[v].length; t++) {
                if (willBeLessUnderAttack(variants.get(v), targets[v][t])) {
                    points[v][t] += 2;
                }
                if (willBeUnderAtackAfterStep(variants.get(v), targets[v][t])) {
                    if (act.isMove()) {
                        points[v][t] += -1;
                    }
                    if (act.isFight() && willBeFighterAfter(variants.get(v), targets[v][t])) {
                        points[v][t] += 2;
                    }
                } else {
                    if (willBeFighterAfter(variants.get(v), targets[v][t])) {
                        points[v][t] += 2;
                    } else {
                        points[v][t] += 1;
                    }
                }
            }
        }

        for (int i = 0; i < targets.length; i++) {
            for (int k = 0; k < targets[i].length; k++) {
                if (targets[i][k].isExist()) {
                }
            }
        }
        // get max value
        int min = -1;
        for (int v = 0; v < points.length; v++) {
            for (int t = 0; t < points[v].length; t++) {
                if (points[v][t] > min) {
                    min = points[v][t];
                }
            }
        }
        // if max val is not alone
        int maxValueCount = 0;
        for (int v = 0; v < points.length; v++) {
            for (int t = 0; t < points[v].length; t++) {
                if (points[v][t] == min) {
                    maxValueCount++;
                }
            }
        }
        // fill arr those max val
        int i = 0;
        Cell arrMaxVal[][] = new Cell[maxValueCount][];
        for (int v = 0; v < points.length; v++) {
            for (int t = 0; t < points[v].length; t++) {
                if (points[v][t] == min) {
                    arrMaxVal[i] = new Cell[]{variants.get(v), targets[v][t]};
                    i++;
                }
            }
        }

        // get best target 
        if (maxValueCount > 0) {
            Random rand = new Random();
            return arrMaxVal[rand.nextInt(maxValueCount)];
        } else {
            return new Cell[]{new Cell(), new Cell()};
        }
    }

    private boolean willBeFighterAfter(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        // Make xBoard - temp ChessBoard, what we use to prognosis which checkers will be fighter after some step
        ChessBoard xBoard = new ChessBoard();
        Cell activeCellTmp = new Cell();
        Cell targetCellTmp = new Cell();
        for (int i = 0; i < xBoard.cells.length; i++) {
            xBoard.cells[i].setStatus(cBoard.cells[i].getStatus());
            if (activeCell.index.equals(xBoard.cells[i].index)) {
                activeCellTmp = xBoard.cells[i];
            }
            if (targetCell.index.equals(xBoard.cells[i].index)) {
                targetCellTmp = xBoard.cells[i];
            }
        }

        Direction d = getDbyTarget(xBoard, activeCellTmp, targetCellTmp);
        Cell victimCell = getVictim(xBoard, activeCellTmp, targetCellTmp, d);
        targetCellTmp.setStatus(activeCellTmp.getStatus());
        activeCellTmp.setBlackCell();
        if (victimCell.isExist()) {
            victimCell.setTurkichChecker();
        }

        return isSome(xBoard, targetCell, FIGHT);
    }

    private boolean willBeLessUnderAttack(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        ChessBoard xBoard = new ChessBoard();
        Cell activeCellTmp = new Cell();
        Cell targetCellTmp = new Cell();
        for (int i = 0; i < xBoard.cells.length; i++) {
            xBoard.cells[i].setStatus(cBoard.cells[i].getStatus());
            if (activeCell.index.equals(xBoard.cells[i].index)) {
                activeCellTmp = xBoard.cells[i];
            }
            if (targetCell.index.equals(xBoard.cells[i].index)) {
                targetCellTmp = xBoard.cells[i];
            }
        }
        Player computer = activeCellTmp.isWhite() ? WHITE : BLACK;
        int ownUnderAttackBefore = 0;
        int ownUnderAttackAfter = 0;
        for (int i = 0; i < xBoard.cells.length; i++) {
            if (xBoard.cells[i].isOwn(activeCellTmp)) {
                if (isCellUnderAtack(xBoard, xBoard.cells[i], computer)) {
                    ownUnderAttackBefore++;
                }
            }
        }

        targetCellTmp.setStatus(activeCellTmp.getStatus());
        activeCellTmp.setBlackCell();
        for (int i = 0; i < xBoard.cells.length; i++) {
            if (xBoard.cells[i].isOwn(targetCellTmp)) {
                if (isCellUnderAtack(xBoard, xBoard.cells[i], computer)) {
                    ownUnderAttackAfter++;
                }
            }
        }
        return (ownUnderAttackAfter < ownUnderAttackBefore);
    }

    private boolean willBeUnderAtackAfterStep(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
//    |o| | |  Будет ли шашка под атакой ПОСЛЕ ХОДА на ближней дистанции(одна клетка) Рассматриваем вариант по диагонали: наша шашка, пустая клетка(куда собираемся походить), вражеская шашка.
//    | |x| |  После хода клетка, занятая нашей шашкой освободиться, и наша шашка станет под атаку. Чтобы этого избежать проверяем эту ситуацию.
//    | | |*|  Для обычных шашек проверяются ихние направления - черные - вниз, белые - вверх. Дамки проверяются по всем направлениям.
        for (Direction d : Direction.values()) {
            if (getCellByD(this.cBoard, targetCell, d, 1).isOpposite(activeCell) && getCellByD(this.cBoard, targetCell, getOppositeD(d), 1).equals(activeCell)) {
                return true;
            }
        }
// Проверяем нахрест по диагонали наличие шашки противника, нашей шашки, пустой клетки.
        for (Direction d : Direction.values()) {
            if (getCellByD(this.cBoard, targetCell, d, 1).isOpposite(activeCell) && getCellByD(this.cBoard, targetCell, getOppositeD(d), 1).isBlackCell()) {
                return true;
            }
        }
// Ешем дамку во всех направлениях до конца диагонали или наличия шашки на диагонали, если находим, проверяем будет ли позади пустая клетка, или та 
// с которой мы будем ходить (соответсвенно она будет тоже пустая после хода)
        for (Direction d : Direction.values()) {
            int i = 1;
            while (getCellByD(this.cBoard, targetCell, d, i).isBlackCell() || (getCellByD(this.cBoard, targetCell, d, i).isOwn(activeCell) && getCellByD(this.cBoard, targetCell, d, i).equals(activeCell))) {
                i++;
            }
            if (getCellByD(this.cBoard, targetCell, d, i).isOpposite(activeCell) && getCellByD(this.cBoard, targetCell, d, i).isQueen()) {
                if (getCellByD(this.cBoard, targetCell, getOppositeD(d), 1).isBlackCell() || getCellByD(this.cBoard, targetCell, getOppositeD(d), 1).equals(activeCell)) {
                    return true;
                }
            }

        }
        return false;
    }

//   |*| |*|   |x| |x| Проверяем по диагоналям под атакой ли любая шашка(клетка) НА ДАННЫЙ МОМЕНТ.
//   | |o| |   | |o| |
//   |x| |x|   |*| |*|  
    private boolean isCellUnderAtack(ChessBoard xBoard, Cell cell, Player player) throws NoSuchDirectionException {
        Cell tmpCell = new Cell();
        if (player.isWhite()) {
            tmpCell.setWhiteChecker();
        } else {
            tmpCell.setBlackChecker();
        }
//find enemy checker near us
        for (Direction d : Direction.values()) {
            if (getCellByD(xBoard, cell, d, 1).isOpposite(tmpCell) && getCellByD(xBoard, cell, getOppositeD(d), 1).isBlackCell()) {
                return true;
            }
        }
//find enemy queen far from       
        for (Direction d : Direction.values()) {
            int i = 1;
            while (getCellByD(xBoard, cell, d, i).isBlackCell()) {
                i++;
            }
            if (getCellByD(xBoard, cell, d, i).isOpposite(tmpCell) && getCellByD(xBoard, cell, d, i).isQueen()) {
                if (getCellByD(xBoard, cell, getOppositeD(d), 1).isBlackCell()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Direction getOppositeD(Direction d) throws NoSuchDirectionException {
        if (d == LU) {
            return RB;
        }
        if (d == RU) {
            return LB;
        }
        if (d == RB) {
            return LU;
        }
        if (d == LB) {
            return RU;
        }
        throw new NoSuchDirectionException();
    }

    private void resetTurkishArr() {
        for (int i = 0; i < turkishArr.size(); i++) {
            turkishArr.get(i).setBlackCell();
        }
        turkishArr.clear();
    }

    private boolean isSomeByD(ChessBoard xBoard, Cell cell, Action act, Direction d) {
        int i = 1;
        while (getCellByD(xBoard, cell, d, i).isExist()) {
            if (isRightActByD(xBoard, cell, getCellByD(xBoard, cell, d, i), d, act)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean isSome(ChessBoard xBoard, Cell cell, Action act) {
        for (Direction d : Direction.values()) {
            if (isSomeByD(xBoard, cell, act, d)) {
                return true;
            }

        }
        return false;
    }

    Cell getSome(ChessBoard xBoard, Action act, Player pl) {
        for (int i = 0; i < xBoard.cells.length; i++) {
            if (xBoard.cells[i].isWhite() && isSome(xBoard, xBoard.cells[i], act) && pl.isWhite()) {
                return xBoard.cells[i];
            }
            if (xBoard.cells[i].isBlack() && isSome(xBoard, xBoard.cells[i], act) && pl.isBlack()) {
                return xBoard.cells[i];
            }
        }
        return new Cell();
    }

//  return:
//  move    - [targetCell, new Cell()]
//  fight   - [targetCell, victimCell]
//  nothing - [new Cell(), new Cell()]
    private Cell[] checkCells(ChessBoard xBoard, Cell activeCell, Cell targetCell, Direction d, Action act) {
        Cell victimCell;

        //move
        if (act.isMove()) {
            //check right directions for checkers
            if (((activeCell.isBlackChecker() || activeCell.isBlackActiveChecker()) && !d.isBlackD())
                    || ((activeCell.isWhiteChecker() || activeCell.isWhiteActiveChecker()) && !d.isWhiteD())) {
                return new Cell[]{new Cell(), new Cell()};
            }

            // checker
            if (activeCell.isChecker()) {
                if (getCellByD(xBoard, activeCell, d, 1).isBlackCell()) {
                    if (getCellByD(xBoard, activeCell, d, 1).equals(targetCell)) {
                        return new Cell[]{targetCell, new Cell()};
                    }
                }
            }
            // queen
            if (activeCell.isQueen()) {
                int deep = 1;
                while (getCellByD(xBoard, activeCell, d, deep).isBlackCell()) {
                    if (getCellByD(xBoard, activeCell, d, deep).equals(targetCell)) {
                        return new Cell[]{targetCell, new Cell()};
                    }
                    deep++;
                }
            }
        }
        //fight        
        if (act.isFight()) {
            //checker
            if (activeCell.isChecker()) {
                if (getCellByD(xBoard, activeCell, d, 1).isOpposite(activeCell)) {
                    if (getCellByD(xBoard, activeCell, d, 2).isBlackCell() && getCellByD(xBoard, activeCell, d, 2).equals(targetCell)) {
                        return new Cell[]{targetCell, getCellByD(xBoard, activeCell, d, 1)};
                    }
                }
            }
            //queen
            if (activeCell.isQueen()) {
                int deep = 1;
                while (getCellByD(xBoard, activeCell, d, deep).isBlackCell()) {
                    deep++;
                }
                victimCell = getCellByD(xBoard, activeCell, d, deep);
                if (victimCell.isOpposite(activeCell)) {
                    deep = 1;
                    while (getCellByD(xBoard, victimCell, d, deep).isBlackCell()) {
                        if (getCellByD(xBoard, victimCell, d, deep).equals(targetCell)) {
                            return new Cell[]{targetCell, victimCell};
                        }
                        deep++;
                    }
                }
            }
        }
        return new Cell[]{new Cell(), new Cell()};
    }

    private boolean isRightActByD(ChessBoard xBoard, Cell activeCell, Cell targetCell, Direction d, Action act) {
        Cell actCells[] = checkCells(xBoard, activeCell, targetCell, d, act);
        // move
        if (act.isMove() && actCells[0].isExist() && (!actCells[1].isExist())) {
            return true;
        }
        // fight
        if (act.isFight() && actCells[0].isExist() && actCells[1].isExist()) {
            return true;
        }
        return false;
    }

    private Direction getDbyTarget(ChessBoard xBoard, Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        for (Direction d : Direction.values()) {
            int i = 1;
            while (getCellByD(xBoard, activeCell, d, i).isExist()) {
                if (targetCell.equals(getCellByD(xBoard, activeCell, d, i))) {
                    return d;
                }
                i++;
            }
        }
        throw new NoSuchDirectionException();
    }

    /** Search cell in array of cells. If cell with such coordinates exists, return it. If not - create new cell and set it's status like "NILL" */
    Cell getCellByXY(ChessBoard xBoard, int clickedX, int clickedY) {
        for (Cell cell : xBoard.cells) {
            if ((clickedX >= (cell.cX))
                    && (clickedX < (cell.cX + xBoard.CELL_SIZE))
                    && (clickedY >= (cell.cY))
                    && (clickedY < (cell.cY + xBoard.CELL_SIZE))) {
                return cell;
            }
        }
        return new Cell();
    }

    private Cell getCellByD(ChessBoard xBoard, Cell cell, Direction d, int deep) {
        return getCellByXY(xBoard, cell.cX + xBoard.CELL_SIZE * d.kX * deep, cell.cY + xBoard.CELL_SIZE * d.kY * deep);
    }

    private boolean checkQeen(Cell cell) {
        String userIndexQ[] = {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"};
        String compIndexQ[] = {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
        if (cell.isWhiteChecker()) {
            for (String uIndex : userIndexQ) {
                if (uIndex.equals(cell.index)) {
                    cell.setWhiteQueen();
                    return true;
                }
            }
        }
        if (cell.isBlackChecker()) {
            for (String cIndex : compIndexQ) {
                if (cIndex.equals(cell.index)) {
                    cell.setBlackQueen();
                    return true;
                }
            }
        }
        return false;
    }

    Logic(ChessBoard cBoard, Menu menu) {
        this.cBoard = cBoard;
        this.menu = menu;
    }
}