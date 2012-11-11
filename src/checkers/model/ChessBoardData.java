package checkers.model;

import checkers.model.ChessBoardData.GameActors;
import java.awt.Dimension;
import static checkers.model.ChessBoardData.Status.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import static checkers.model.ChessBoardData.GameActors.*;
import static checkers.model.ChessBoardData.Lang;

/**
 * This class paints chessboard
 *
 * @author Kapellan
 */
public class ChessBoardData {

    public boolean whiteIsHuman = true;
    public boolean blackIsHuman = true;
    public boolean whiteSessionContinue = true;
    public boolean blackSessionContinue = false;
    public String resultBuf;
    /* The number of own and enemy's checkers */
    public int whiteCheckers = 12;
    public int blackCheckers = 12;
    /* CellSize is a size of chess board cell, it is a square so all sides are same */
    public final int CELL_SIZE = 55;
    /* cellSideNumber is using for different kinds of checkers. Russian checkers use chess board 8x8 cells.  */
    public final int CELL_SIDE_NUM = 8;
    public final int CELL_NUM = CELL_SIDE_NUM * CELL_SIDE_NUM;
    public Cell cells[] = new Cell[CELL_NUM];
    /* Each checker's diameter */
    public int checkerX;
    public int checkerY;
    /* The offset from left and top frame bounds  */
    public final int OFFSET_LEFT_BOUND = -30;
    public final int OFFSET_TOP_BOUND = -30;
    /* Diameter of checkers, inner round of queen */
    public final int CHECKER_DIAMETER = 50;
    public final int QUEEN_INNER_DIAMETER = 40;
    public final int QUEEN_INNER_OFFSET = (CHECKER_DIAMETER - QUEEN_INNER_DIAMETER) / 2;
    public final Dimension PREFERRED_SIZE = new Dimension(CELL_SIDE_NUM * CELL_SIZE + CELL_SIZE, CELL_SIDE_NUM * CELL_SIZE + CELL_SIZE);
    /* Those arrays we use in  makeIndex() method and in painting numbers of chess board in method paint()*/
    public final String LITERALS[] = {"NULL", "a", "b", "c", "d", "e", "f", "g", "h"};//
    public final int REVERS_NUMBERS[] = {0, 8, 7, 6, 5, 4, 3, 2, 1};
    public String stepWhiteText;
    public String stepBlackText;
    public String userHasFighterText;
    public String userMustFightText;
    public String wrongNextCellText;
    public String frameTitle;
    public String gameTitle;
    public String settingsTitle;
    public String languageTitle;
    public String gameActorsTitle;
    public String compVSuserTitle;
    public String userVScompTitle;
    public String userVSuserTitle;
    public String compVScompTitle;
    public String helpTitle;
    public String newGameTitle;
    public String exitTitle;
    public String rulesTitle;
    public String rulesLink;
    public String aboutTitle;
    public String aboutDeveloperText;
    public String labelBlackTitle;
    public String labelWhiteTitle;
    public String noWhiteCheckersText;
    public String noBlackCheckersText;
    public String whiteIsBlockedText;
    public String blackIsBlockedText;
    public String whiteWon;
    public String blackWon;
    public String dialogNewGame;
    public String dialogExit;
    /* Each cell's coordinates (left upper corner) */
    int cX;
    int cY;
    boolean gameExit = false;
    boolean gameOwer = false;
    Lang LANG = Lang.RU;
    private NodeList menuValues;
    private DataListener dataListener;

    public enum Status {

        NILL, WC, BC, WHITE_CH, BLACK_CH, WHITE_Q, BLACK_Q, WHITE_ACH, BLACK_ACH, WHITE_AQ, BLACK_AQ, TBCH;
    }

    enum Lang {

        RU, UA, ENG;
    }

    enum GameActors {

        USERvsCOMP, COMPvsUSER, USERvsUSER, COMPvsCOMP;
    }

    public void setUSERvsCOMP() {
        setGameActors(USERvsCOMP);
    }

    public void setCOMPvsUSER() {
        setGameActors(COMPvsUSER);

    }

    public void setCOMPvsCOMP() {
        setGameActors(COMPvsCOMP);
    }

    public void setUSERvsUSER() {
        setGameActors(USERvsUSER);
    }

    void setGameActors(GameActors GA) {
        if (GA == USERvsCOMP) {
            whiteIsHuman = true;
            blackIsHuman = false;
        }
        if (GA == COMPvsUSER) {
            whiteIsHuman = false;
            blackIsHuman = true;
        }
        if (GA == COMPvsCOMP) {
            whiteIsHuman = false;
            blackIsHuman = false;
        }
        if (GA == USERvsUSER) {
            whiteIsHuman = true;
            blackIsHuman = true;
        }
    }

    public void restartGame() {
        gameOwer = false;
        resetData();
        initCellsArr();
    }

    public void setGameExit() {
        gameExit = true;
    }

    void resetData() {
        gameOwer = true;
        gameOwer = false;
        whiteCheckers = 12;
        blackCheckers = 12;
        whiteSessionContinue = true;
        blackSessionContinue = false;
    }

    public void setEnglishLang() {
        setLanguage(Lang.ENG);
    }

    public void setRussianLang() {
        setLanguage(Lang.RU);

    }

    public void setUkrainianLang() {
        setLanguage(Lang.UA);
    }

    private void setLanguage(Lang lang) {
        initMenuValbyXML(lang);
        LANG = lang;
        stepWhiteText = getFromXML("stepWhiteText");
        stepBlackText = getFromXML("stepBlackText");
        userHasFighterText = getFromXML("userHasFighterText");
        userMustFightText = getFromXML("userMustFightText");
        wrongNextCellText = getFromXML("wrongNextCellText");
        frameTitle = getFromXML("frameTitle");
        gameTitle = getFromXML("gameTitle");
        settingsTitle = getFromXML("settingsTitle");
        gameActorsTitle = getFromXML("gameActorsTitle");
        userVScompTitle = getFromXML("userVScompTitle");
        compVSuserTitle = getFromXML("compVSuserTitle");
        userVSuserTitle = getFromXML("userVSuserTitle");
        compVScompTitle = getFromXML("compVScompTitle");
        languageTitle = getFromXML("languageTitle");
        helpTitle = getFromXML("helpTitle");
        newGameTitle = getFromXML("newGameTitle");
        exitTitle = getFromXML("exitTitle");
        rulesTitle = getFromXML("rulesTitle");
        rulesLink = getFromXML("rulesLink");
        aboutTitle = getFromXML("aboutTitle");
        aboutDeveloperText = getFromXML("aboutDeveloperText");
        labelBlackTitle = getFromXML("labelBlackTitle");
        labelWhiteTitle = getFromXML("labelWhiteTitle");
        noWhiteCheckersText = getFromXML("noWhiteCheckersText");
        noBlackCheckersText = getFromXML("noBlackCheckersText");
        whiteIsBlockedText = getFromXML("whiteIsBlockedText");
        blackIsBlockedText = getFromXML("blackIsBlockedText");
        whiteWon = getFromXML("whiteWon");
        blackWon = getFromXML("blackWon");
        dialogNewGame = getFromXML("dialogNewGame");
        dialogExit = getFromXML("dialogExit");
        if (dataListener != null) {
            dataListener.updateTextGuiLanguageInfo(new UpdateGuiEvent(this));
        }
    }

    private void initMenuValbyXML(Lang lang) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("./menu.xml");
            Element rootElementLang = document.getDocumentElement();
            NodeList langElements = rootElementLang.getChildNodes();
            for (int i = 0; i < langElements.getLength(); i++) {
                if (langElements.item(i).getNodeName().equals(lang.toString())) {
                    menuValues = langElements.item(i).getChildNodes();
                }
            }
        } catch (Exception exception) {
            System.out.println("XML parsing error!");
            exception.printStackTrace();
        }
    }

    private String getFromXML(String elementName) {
        for (int i = 0; i < menuValues.getLength(); i++) {
            if (menuValues.item(i).getNodeName().equals(elementName)) {
                return menuValues.item(i).getTextContent();
            }
        }
        return "Some problem in XML language file";
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

    /**
     * This method make's alphabet-digital index of any cell
     */
    private String makeIndex(int indexLiteralX, int indexDigitY) {
        return LITERALS[indexLiteralX] + (Integer.toString(REVERS_NUMBERS[indexDigitY]));
    }

    void notifyNoBlackCkeckersLeft() {
        dataListener.noBlackCkeckersLeft(new UpdateGuiEvent(this));
    }

    void notifyNoWhiteCkeckersLeft() {
        dataListener.noWhiteCkeckersLeft(new UpdateGuiEvent(this));
    }

    void notifyBlackIsBlocked() {
        dataListener.blackIsBlocked(new UpdateGuiEvent(this));
    }

    void notifyWhiteIsBlocked() {
        dataListener.whiteIsBlocked(new UpdateGuiEvent(this));
    }

    void notifyUpdateGUI() {
        dataListener.updateGUI(new UpdateGuiEvent(this));
    }

    private void initCellsArr() {
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
    }

    public void addDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }

    public ChessBoardData() {
        initCellsArr();
    }
}
