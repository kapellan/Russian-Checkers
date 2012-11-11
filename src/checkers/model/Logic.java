package checkers.model;

import java.util.ArrayList;
import java.awt.HeadlessException;
import java.util.Random;
import static checkers.model.Logic.Action.*;
import static checkers.model.Logic.Player.*;
import static checkers.model.Logic.Direction.*;

/**
 * This class provides game logic - move and fight.
 *
 * @author Kapellan
 */
public class Logic {

    public ChessBoardData data;
    boolean steelFighterFlag = false;
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
            if ((!data.whiteIsHuman) && data.whiteSessionContinue) {
                computer = WHITE;
            } else if ((!data.blackIsHuman) && data.blackSessionContinue) {
                computer = BLACK;
            } else {
                throw new PlayerErrorException();
            }
            if (data.whiteSessionContinue) {
                data.resultBuf = data.stepWhiteText + "\n";
            } else {
                data.resultBuf = data.stepBlackText + "\n";
            }

            Cell activeCell;
            Cell targetCell;
            Cell victimCell;
            // First we must fight */
            if (getSome(this.data, FIGHT, computer).isExist()) {
                do {
                    Thread.currentThread().sleep(500);
                    Cell actCells[] = getBestEnemy(computer);
                    activeCell = actCells[0];
                    targetCell = actCells[1];
                    victimCell = getVictim(this.data, activeCell, targetCell, getDbyTarget(this.data, activeCell, targetCell));
                    turkishArr.add(victimCell);
                    victimCell.setTurkichChecker();
                    activeCell.resetActive();
                    targetCell.setStatus(activeCell.getStatus());
                    activeCell.setBlackCell();
                    checkSetQeen(targetCell);
                    activeCell = targetCell;
                    if (!steelFighterFlag) {
                        data.resultBuf += actCells[0].index + ":" + actCells[1].index;
                    } else {
                        data.resultBuf += ":" + activeCell.index;
                    }
                    if (isSome(this.data, activeCell, FIGHT)) {
                        activeCell.setActive();
                        steelFighterFlag = true;
                    } else {
                        activeCell.resetActive();
                        steelFighterFlag = false;
                        resetTurkishArr();
                        changeSession();
                        data.resultBuf += "\n";
                        customResult();
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
                checkSetQeen(targetCell);
                activeCell.setBlackCell();
                data.resultBuf += activeCell.index + ":" + targetCell.index + "\n";
                customResult();
                changeSession();
                return;
            }
        } catch (PlayerErrorException e) {
            e.printStackTrace();
            return;
        } catch (NoSuchDirectionException e) {
            e.printStackTrace();
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (HeadlessException e) {
            e.printStackTrace();
        }

    }

    public void userStep(Cell activeCell, Cell targetCell) {
        try {
            Player player;
            if (activeCell.isWhite()) {
                player = WHITE;
                if (!steelFighterFlag) {
                    data.resultBuf = data.stepWhiteText + "\n";
                }
            } else if (activeCell.isBlack()) {
                player = BLACK;
                if (!steelFighterFlag) {
                    data.resultBuf = data.stepBlackText + "\n";
                }
            } else {
                throw new PlayerErrorException();
            }

            Direction d = getDbyTarget(this.data, activeCell, targetCell);

            if (getSome(this.data, FIGHT, player).isExist() && !isSome(this.data, activeCell, FIGHT)) {
                data.resultBuf += data.userHasFighterText + " " + getSome(this.data, FIGHT, player).index + "\n";
                customResult();
                return;
            }
            //First we must fight /
            if (isRightActByD(this.data, activeCell, targetCell, d, FIGHT)) {
                Cell victimCell = getVictim(this.data, activeCell, targetCell, d);
                turkishArr.add(victimCell);
                victimCell.setTurkichChecker();
                activeCell.resetActive();
                targetCell.setStatus(activeCell.getStatus());
                activeCell.setBlackCell();
                checkSetQeen(targetCell);
                if (!steelFighterFlag) {
                    userResultCheckersNum = activeCell.index;
                }
                activeCell = targetCell;
                if (isSome(this.data, activeCell, FIGHT)) {
                    steelFighterFlag = true;
                    activeCell.setActive();
                    userResultCheckersNum += ":" + targetCell.index;
                    return;
                } else {
                    activeCell.resetActive();
                    resetTurkishArr();
                    data.resultBuf += userResultCheckersNum + ":" + targetCell.index + "\n";
                    userResultCheckersNum = "";
                    customResult();
                    steelFighterFlag = false;
                    changeSession();
                    return;
                }
            }

            // Check for a fighter 
            if (isSome(this.data, activeCell, FIGHT)) {
                data.resultBuf += data.userMustFightText + "\n";
                return;
            }

            // If there is no fighter checker we move 
            if (isRightActByD(this.data, activeCell, targetCell, d, MOVE)) {
                targetCell.setStatus(activeCell.getStatus());
                targetCell.resetActive();
                checkSetQeen(targetCell);
                activeCell.setBlackCell();
                data.resultBuf += activeCell.index + ":" + targetCell.index + "\n";
                customResult();
                changeSession();
                return;
            }

        } catch (PlayerErrorException e) {
            e.printStackTrace();
            return;
        } catch (NoSuchDirectionException e) {
            e.printStackTrace();
            return;
        }
    }

    private void changeSession() {
        data.whiteSessionContinue = !data.whiteSessionContinue;
        data.blackSessionContinue = !data.blackSessionContinue;
    }

    private Cell getVictim(ChessBoardData xData, Cell activeCell, Cell targetCell, Direction d) {
        if (activeCell.isChecker()) {
            if (targetCell.equals(getCellByD(xData, activeCell, d, 2))) {
                return getCellByD(xData, activeCell, d, 1);
            }
        }
        if (activeCell.isQueen()) {
            int deep = 1;
            while (getCellByD(xData, activeCell, d, deep).isBlackCell()) {
                deep++;
            }
            if (getCellByD(xData, activeCell, d, deep).isOpposite(activeCell)) {
                if (getCellByD(xData, activeCell, d, deep + 1).isBlackCell()) {
                    return getCellByD(xData, activeCell, d, deep);
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
            for (int i = 0; i < data.cells.length; i++) {
                if (data.cells[i].isWhiteActiveChecker() || data.cells[i].isWhiteActiveQueen()) {
                    activeCell = data.cells[i];
                }
            }
        } else if (computer.isBlack()) {
            for (int i = 0; i < data.cells.length; i++) {
                if (data.cells[i].isBlackActiveChecker() || data.cells[i].isBlackActiveQueen()) {
                    activeCell = data.cells[i];
                }
            }
        }
        if (activeCell.isExist()) {
            act = FIGHT;
        } else {
            if (getSome(this.data, FIGHT, computer).isExist()) {
                act = FIGHT;
            } else {
                act = MOVE;
            }
        }

        if (activeCell.isExist()) {
            variants.add(activeCell);
        } else {
            if (computer.isWhite()) {
                for (int i = 0; i < data.cells.length; i++) {
                    if (isSome(this.data, data.cells[i], act) && data.cells[i].isWhite()) {
                        variants.add(data.cells[i]);
                    }
                }
            } else {
                for (int i = 0; i < data.cells.length; i++) {
                    if (isSome(this.data, data.cells[i], act) && data.cells[i].isBlack()) {
                        variants.add(data.cells[i]);
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
                while (getCellByD(this.data, variants.get(v), d, deep).isExist()) {
                    Cell targetCell = getCellByD(this.data, variants.get(v), d, deep);
                    if (isRightActByD(this.data, variants.get(v), targetCell, d, act)) {
                        tmpTargets.add(targetCell);
                    }
                    deep++;
                }
            }
            targets[v] = tmpTargets.toArray(new Cell[tmpTargets.size()]);
            points[v] = new int[tmpTargets.size()];
        }

        // get checkers with max/min index num for white/black checkers
        int minIndex = -1;
        int maxIndex = 9;
        for (int v = 0; v < variants.size(); v++) {
            if (variants.get(v).isWhiteChecker() && Integer.parseInt(variants.get(v).index.substring(1)) > minIndex) {
                minIndex = Integer.parseInt(variants.get(v).index.substring(1));
            }
            if (variants.get(v).isBlackChecker() && Integer.parseInt(variants.get(v).index.substring(1)) < maxIndex) {
                maxIndex = Integer.parseInt(variants.get(v).index.substring(1));
            }
        }

        // set points of any target
        System.out.println("__________________");
        for (int v = 0; v < targets.length; v++) {
            System.out.println();
            System.out.println("variant: " + variants.get(v).index);
            for (int t = 0; t < targets[v].length; t++) {
                System.out.println("  target: " + targets[v][t].index);
                if (willBeLessUnderAttack(variants.get(v), targets[v][t])) {
                    points[v][t] += 4;
                    System.out.println("   willBeLessUnderAttack");
                }
                if (willBeMoreUnderAttack(variants.get(v), targets[v][t])) {
                    points[v][t] += -4;
                    System.out.println("   willBeMoreUnderAttack");
                }
                if (willBeUnderAtackAfterStep(variants.get(v), targets[v][t])) {
                    System.out.println("   willBeUnderAtackAfterStep");
                    if (act.isMove()) {
                        points[v][t] += -1;
                    }
                    if (act.isFight() && willBeFighterAfter(variants.get(v), targets[v][t])) {
                        points[v][t] += 1;
                        System.out.println("   willBeFighterAfter AND FIGHT");
                        points[v][t] += commonCheckPoints(variants.get(v), targets[v][t], maxIndex, minIndex);
                    }
                } else {
                    if (willBeFighterAfter(variants.get(v), targets[v][t])) {
                        points[v][t] += 3;
                        System.out.println("   willBeFighterAfter");
                        points[v][t] += commonCheckPoints(variants.get(v), targets[v][t], maxIndex, minIndex);

                    } else {
                        points[v][t] += 1;
                        System.out.println("   NOT willBeFighterAfter");
                        points[v][t] += commonCheckPoints(variants.get(v), targets[v][t], maxIndex, minIndex);
                    }
                }
                System.out.println("   POINTS: " + points[v][t]);
            }
        }


        // get max value
        int min = -100;
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

    private int commonCheckPoints(Cell variant, Cell target, int maxIndex, int minIndex) {
        int pointSummary = 0;
        if (isQueenIndex(target)) {
            pointSummary += 2;
            System.out.println("   isQueenIndex");
        }
        if (isCentralField(target)) {
            pointSummary += 1;
            System.out.println("   isCentralField");
        }
        if (variant.isWhiteChecker() && Integer.parseInt(target.index.substring(1)) == minIndex) {
            pointSummary += 1;
            System.out.println("min index: " + target.index + " " + minIndex);
        }
        if (variant.isBlackChecker() && Integer.parseInt(target.index.substring(1)) == maxIndex) {
            pointSummary += 1;
            System.out.println("max index: " + target.index + " " + maxIndex);
        }
        System.out.println("   Points in common step: " + pointSummary);
        return pointSummary;
    }

    private boolean willBeFighterAfter(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        // Make xBoard - temp ChessBoard, what we use to prognosis which checkers will be fighter after some step
        ChessBoardData xData = new ChessBoardData();
        Cell activeCellTmp = new Cell();
        Cell targetCellTmp = new Cell();
        for (int i = 0; i < xData.cells.length; i++) {
            xData.cells[i].setStatus(data.cells[i].getStatus());
            if (activeCell.index.equals(xData.cells[i].index)) {
                activeCellTmp = xData.cells[i];
            }
            if (targetCell.index.equals(xData.cells[i].index)) {
                targetCellTmp = xData.cells[i];
            }
        }

        Direction d = getDbyTarget(xData, activeCellTmp, targetCellTmp);
        Cell victimCell = getVictim(xData, activeCellTmp, targetCellTmp, d);
        targetCellTmp.setStatus(activeCellTmp.getStatus());
        activeCellTmp.setBlackCell();
        if (victimCell.isExist()) {
            victimCell.setTurkichChecker();
        }

        return isSome(xData, targetCellTmp, FIGHT);
    }

    private boolean willBeLessUnderAttack(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        ChessBoardData xData = new ChessBoardData();
        Cell activeCellTmp = new Cell();
        Cell targetCellTmp = new Cell();
        for (int i = 0; i < xData.cells.length; i++) {
            //
            xData.cells[i].setStatus(data.cells[i].getStatus());
            if (activeCell.index.equals(xData.cells[i].index)) {
                activeCellTmp = xData.cells[i];
            }
            if (targetCell.index.equals(xData.cells[i].index)) {
                targetCellTmp = xData.cells[i];
            }
        }
        Player computer = activeCellTmp.isWhite() ? WHITE : BLACK;
        int ownUnderAttackBefore = 0;
        int ownUnderAttackAfter = 0;
        for (int i = 0; i < xData.cells.length; i++) {
            if (xData.cells[i].isOwn(activeCellTmp)) {
                if (isCellUnderAtack(xData, xData.cells[i], computer)) {
                    ownUnderAttackBefore++;
                }
            }
        }

        targetCellTmp.setStatus(activeCellTmp.getStatus());
        activeCellTmp.setBlackCell();
        for (int i = 0; i < xData.cells.length; i++) {
            if (xData.cells[i].isOwn(targetCellTmp)) {
                if (isCellUnderAtack(xData, xData.cells[i], computer)) {
                    ownUnderAttackAfter++;
                }
            }
        }
        return (ownUnderAttackAfter < ownUnderAttackBefore);
    }

    private boolean willBeMoreUnderAttack(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        ChessBoardData xData = new ChessBoardData();
        Cell activeCellTmp = new Cell();
        Cell targetCellTmp = new Cell();
        for (int i = 0; i < xData.cells.length; i++) {
            //
            xData.cells[i].setStatus(data.cells[i].getStatus());
            if (activeCell.index.equals(xData.cells[i].index)) {
                activeCellTmp = xData.cells[i];
            }
            if (targetCell.index.equals(xData.cells[i].index)) {
                targetCellTmp = xData.cells[i];
            }
        }
        Player computer = activeCellTmp.isWhite() ? WHITE : BLACK;
        int ownUnderAttackBefore = 0;
        int ownUnderAttackAfter = 0;
        for (int i = 0; i < xData.cells.length; i++) {
            if (xData.cells[i].isOwn(activeCellTmp)) {
                if (isCellUnderAtack(xData, xData.cells[i], computer)) {
                    ownUnderAttackBefore++;
                }
            }
        }

        targetCellTmp.setStatus(activeCellTmp.getStatus());
        activeCellTmp.setBlackCell();
        for (int i = 0; i < xData.cells.length; i++) {
            if (xData.cells[i].isOwn(targetCellTmp)) {
                if (isCellUnderAtack(xData, xData.cells[i], computer)) {
                    ownUnderAttackAfter++;
                }
            }
        }
        return (ownUnderAttackAfter > ownUnderAttackBefore);
    }

    private boolean willBeUnderAtackAfterStep(Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
//    |o| | |  Будет ли шашка под атакой ПОСЛЕ ХОДА на ближней дистанции(одна клетка) Рассматриваем вариант по диагонали: наша шашка, пустая клетка(куда собираемся походить), вражеская шашка.
//    | |x| |  После хода клетка, занятая нашей шашкой освободиться, и наша шашка станет под атаку. Чтобы этого избежать проверяем эту ситуацию.
//    | | |*|  Для обычных шашек проверяются ихние направления - черные - вниз, белые - вверх. Дамки проверяются по всем направлениям.
        for (Direction d : Direction.values()) {
            if (getCellByD(this.data, targetCell, d, 1).isOpposite(activeCell) && getCellByD(this.data, targetCell, getOppositeD(d), 1).equals(activeCell)) {
                return true;
            }
        }
// Проверяем нахрест по диагонали наличие шашки противника, нашей шашки, пустой клетки.
        for (Direction d : Direction.values()) {
            if (getCellByD(this.data, targetCell, d, 1).isOpposite(activeCell) && getCellByD(this.data, targetCell, getOppositeD(d), 1).isBlackCell()) {
                return true;
            }
        }
// Ешем дамку во всех направлениях до конца диагонали или наличия шашки на диагонали, если находим, проверяем будет ли позади пустая клетка, или та 
// с которой мы будем ходить (соответсвенно она будет тоже пустая после хода)
        for (Direction d : Direction.values()) {
            int i = 1;
            while (getCellByD(this.data, targetCell, d, i).isBlackCell() || (getCellByD(this.data, targetCell, d, i).isOwn(activeCell) && getCellByD(this.data, targetCell, d, i).equals(activeCell))) {
                i++;
            }
            if (getCellByD(this.data, targetCell, d, i).isOpposite(activeCell) && getCellByD(this.data, targetCell, d, i).isQueen()) {
                if (getCellByD(this.data, targetCell, getOppositeD(d), 1).isBlackCell() || getCellByD(this.data, targetCell, getOppositeD(d), 1).equals(activeCell)) {
                    return true;
                }
            }

        }
        return false;
    }

//   |*| |*|   |x| |x| Проверяем по диагоналям под атакой ли любая шашка(клетка) НА ДАННЫЙ МОМЕНТ.
//   | |o| |   | |o| |
//   |x| |x|   |*| |*|  
    private boolean isCellUnderAtack(ChessBoardData xData, Cell cell, Player player) throws NoSuchDirectionException {
        Cell tmpCell = new Cell();
        if (player.isWhite()) {
            tmpCell.setWhiteChecker();
        } else {
            tmpCell.setBlackChecker();
        }
//find enemy checker near us
        for (Direction d : Direction.values()) {
            if (getCellByD(xData, cell, d, 1).isOpposite(tmpCell) && getCellByD(xData, cell, getOppositeD(d), 1).isBlackCell()) {
                return true;
            }
        }
//find enemy queen far from       
        for (Direction d : Direction.values()) {
            int i = 1;
            while (getCellByD(xData, cell, d, i).isBlackCell()) {
                i++;
            }
            if (getCellByD(xData, cell, d, i).isOpposite(tmpCell) && getCellByD(xData, cell, d, i).isQueen()) {
                if (getCellByD(xData, cell, getOppositeD(d), 1).isBlackCell()) {
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

    private boolean isSomeByD(ChessBoardData xData, Cell cell, Action act, Direction d) {
        int i = 1;
        while (getCellByD(xData, cell, d, i).isExist()) {
            if (isRightActByD(xData, cell, getCellByD(xData, cell, d, i), d, act)) {
                return true;
            }
            i++;
        }
        return false;
    }

    private boolean isSome(ChessBoardData xData, Cell cell, Action act) {
        for (Direction d : Direction.values()) {
            if (isSomeByD(xData, cell, act, d)) {
                return true;
            }

        }
        return false;
    }

    Cell getSome(ChessBoardData xData, Action act, Player pl) {
        for (int i = 0; i < xData.cells.length; i++) {
            if (xData.cells[i].isWhite() && isSome(xData, xData.cells[i], act) && pl.isWhite()) {
                return xData.cells[i];
            }
            if (xData.cells[i].isBlack() && isSome(xData, xData.cells[i], act) && pl.isBlack()) {
                return xData.cells[i];
            }
        }
        return new Cell();
    }

//  return:
//  move    - [targetCell, new Cell()]
//  fight   - [targetCell, victimCell]
//  nothing - [new Cell(), new Cell()]
    private Cell[] checkCells(ChessBoardData xData, Cell activeCell, Cell targetCell, Direction d, Action act) {
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
                if (getCellByD(xData, activeCell, d, 1).isBlackCell()) {
                    if (getCellByD(xData, activeCell, d, 1).equals(targetCell)) {
                        return new Cell[]{targetCell, new Cell()};
                    }
                }
            }
            // queen
            if (activeCell.isQueen()) {
                int deep = 1;
                while (getCellByD(xData, activeCell, d, deep).isBlackCell()) {
                    if (getCellByD(xData, activeCell, d, deep).equals(targetCell)) {
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
                if (getCellByD(xData, activeCell, d, 1).isOpposite(activeCell)) {
                    if (getCellByD(xData, activeCell, d, 2).isBlackCell() && getCellByD(xData, activeCell, d, 2).equals(targetCell)) {
                        return new Cell[]{targetCell, getCellByD(xData, activeCell, d, 1)};
                    }
                }
            }
            //queen
            if (activeCell.isQueen()) {
                int deep = 1;
                while (getCellByD(xData, activeCell, d, deep).isBlackCell()) {
                    deep++;
                }
                victimCell = getCellByD(xData, activeCell, d, deep);
                if (victimCell.isOpposite(activeCell)) {
                    deep = 1;
                    while (getCellByD(xData, victimCell, d, deep).isBlackCell()) {
                        if (getCellByD(xData, victimCell, d, deep).equals(targetCell)) {
                            return new Cell[]{targetCell, victimCell};
                        }
                        deep++;
                    }
                }
            }
        }
        return new Cell[]{new Cell(), new Cell()};
    }

    private boolean isRightActByD(ChessBoardData xData, Cell activeCell, Cell targetCell, Direction d, Action act) {
        Cell actCells[] = checkCells(xData, activeCell, targetCell, d, act);
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

    private Direction getDbyTarget(ChessBoardData xData, Cell activeCell, Cell targetCell) throws NoSuchDirectionException {
        for (Direction d : Direction.values()) {
            int i = 1;
            while (getCellByD(xData, activeCell, d, i).isExist()) {
                if (targetCell.equals(getCellByD(xData, activeCell, d, i))) {
                    return d;
                }
                i++;
            }
        }
        throw new NoSuchDirectionException();
    }

    /**
     * Search cell in array of cells. If cell with such coordinates exists,
     * return it. If not - create new cell and set it's status like "NILL"
     */
    Cell getCellByXY(ChessBoardData xData, int clickedX, int clickedY) {
        for (Cell cell : xData.cells) {
            if ((clickedX >= (cell.cX))
                    && (clickedX < (cell.cX + xData.CELL_SIZE))
                    && (clickedY >= (cell.cY))
                    && (clickedY < (cell.cY + xData.CELL_SIZE))) {
                return cell;
            }
        }
        return new Cell();
    }

    /**
     * Search cell in array of cells. If cell with such coordinates exists,
     * return it. If not - create new cell and set it's status like "NILL"
     */
    public Cell getCellByXY(int clickedX, int clickedY) {
        for (Cell cell : data.cells) {
            if ((clickedX >= (cell.cX))
                    && (clickedX < (cell.cX + data.CELL_SIZE))
                    && (clickedY >= (cell.cY))
                    && (clickedY < (cell.cY + data.CELL_SIZE))) {
                return cell;
            }
        }
        return new Cell();
    }

    private Cell getCellByD(ChessBoardData xData, Cell cell, Direction d, int deep) {
        return getCellByXY(xData, cell.cX + xData.CELL_SIZE * d.kX * deep, cell.cY + xData.CELL_SIZE * d.kY * deep);
    }

    private boolean checkSetQeen(Cell cell) {
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

    private boolean isQueenIndex(Cell cell) {
        String whiteIndexQueen[] = {"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8"};
        String blackIndexQueen[] = {"a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"};
        if (cell.isBlackChecker()) {
            for (String bIndex : blackIndexQueen) {
                if (bIndex.equals(cell.index)) {
                    return true;
                }
            }
        }
        if (cell.isWhiteChecker()) {
            for (String wIndex : whiteIndexQueen) {
                if (wIndex.equals(cell.index)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCentralField(Cell cell) {
        String centralFields[] = {"b2", "b4", "b6", "c3", "c5", "c7", "d2", "d4", "d6", "e3", "e3", "e7", "f2", "f4", "f6", "g3", "g5", "g7"};

        for (String cetralField : centralFields) {
            if (cetralField.equals(cell.index)) {
                return true;
            }
        }
        return false;
    }

    void customResult() {
        data.setCheckersNum();
        data.notifyUpdateGUI();

        if (data.blackCheckers == 0) {
            data.gameOwer = true;
            data.notifyNoBlackCkeckersLeft();
            return;
        }
        if (data.whiteCheckers == 0) {
            data.gameOwer = true;
            data.notifyNoWhiteCkeckersLeft();
            return;
        }

        if (!getSome(this.data, Logic.Action.FIGHT, Logic.Player.BLACK).isExist() && !getSome(this.data, Logic.Action.MOVE, Logic.Player.BLACK).isExist() && data.blackCheckers != 0) {
            data.gameOwer = true;
            data.notifyBlackIsBlocked();
            return;
        }
        if (!getSome(this.data, Logic.Action.FIGHT, Logic.Player.WHITE).isExist() && !getSome(this.data, Logic.Action.MOVE, Logic.Player.WHITE).isExist() && data.whiteCheckers != 0) {
            data.gameOwer = true;
            data.notifyWhiteIsBlocked();
            return;
        }
        data.resultBuf = "";
    }

    Cell getCellByIndex(String index) {
        for (int i = 0; i < data.cells.length; i++) {
            if (index.equals(data.cells[i].index)) {
                return data.cells[i];
            }
        }
        return new Cell();
    }

    public Logic() {
        this.data = new ChessBoardData();
        (new Thread(new ObservePlayerQueue(this))).start();
        /*
         for (int i = 0; i < data.cells.length; i++) {
         if (data.cells[i].isChecker() || data.cells[i].isQueen()) {
         data.cells[i].setBlackCell();
         }
         }
         getCellByIndex("f2").setBlackChecker();
         //getCellByIndex("e1").setBlackQueen();
        
         getCellByIndex("g1").setWhiteChecker();
         */
    }
}