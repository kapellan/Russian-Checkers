package checkers;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.FlowLayout;
import java.net.URI;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;
import static checkers.Menu.GameActors.*;
import static checkers.Menu.Lang.*;

/**
 * This class provides GUI
 * 
 * @author Kapellan
 */
public class Menu implements MouseListener {

    boolean gameOwer = false;
    ChessBoard cBoard = new ChessBoard();
    Logic logic = new Logic(cBoard, this);
    ActionChessBoard act = new ActionChessBoard(cBoard, logic);
    ObservePlayerQueue observer = new ObservePlayerQueue(logic);
    String resultBuf;
    JTextArea tArea = new JTextArea(26, 12);
    String stepWhiteText;
    String stepBlackText;
    String userHasFighterText;
    String userMustFightText;
    String wrongNextCellText;
    private NodeList menuValues;
    private String frameTitle;
    private String gameTitle;
    private String settingsTitle;
    private String languageTitle;
    private String gameActorsTitle;
    private String compVSuserTitle;
    private String userVScompTitle;
    private String userVSuserTitle;
    private String compVScompTitle;
    private String helpTitle;
    private String newGameTitle;
    private String exitTitle;
    private String rulesTitle;
    private String rulesLink;
    private String aboutTitle;
    private String aboutDeveloperText;
    private String labelBlackTitle;
    private String labelWhiteTitle;
    private String noWhiteCheckersText;
    private String noBlackCheckersText;
    private String whiteIsBlockedText;
    private String blackIsBlockedText;
    private String whiteWon;
    private String blackWon;
    private String dialogNewGame;
    private String dialogExit;
    private Lang LANG = Lang.RU;
    private JFrame frame = new JFrame();
    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuGame = new JMenu();
    private JMenu menuSettings = new JMenu();
    private JMenu itemLanguage = new JMenu();
    private ButtonGroup langButtonGroup = new ButtonGroup();
    private JRadioButtonMenuItem rbRusLang = new JRadioButtonMenuItem("Русский", true);
    private JRadioButtonMenuItem rbEngLang = new JRadioButtonMenuItem("English");
    private JRadioButtonMenuItem rbUkrLang = new JRadioButtonMenuItem("Українська");
    private JMenu menuHelp = new JMenu();
    private JMenuItem itemNewGame = new JMenuItem();
    private JMenuItem itemExit = new JMenuItem();
    private JMenuItem itemRules = new JMenuItem();
    private JMenuItem itemAbout = new JMenuItem();
    private JMenu itemGameActors = new JMenu();
    private ButtonGroup actorButtonGroup = new ButtonGroup();
    private JRadioButtonMenuItem rbCompVSuser = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem rbUserVScomp = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem rbUserVSuser = new JRadioButtonMenuItem();
    private JRadioButtonMenuItem rbCompVScomp = new JRadioButtonMenuItem();
    private JLabel labelComp = new JLabel();
    private JLabel labelUser = new JLabel();
    private JScrollPane scrollPane = new JScrollPane(tArea);
    private JPanel resultPanel = new JPanel();
    private BoxLayout boxL = new BoxLayout(resultPanel, BoxLayout.Y_AXIS);
    private JPanel mainPanel = new JPanel(new FlowLayout());

    enum Lang {

        RU, UA, ENG;
    }

    enum GameActors {

        USERvsCOMP, COMPvsUSER, USERvsUSER, COMPvsCOMP;
    }

    private void startObserver() {
        Thread thread = new Thread(new ObservePlayerQueue(logic));
        thread.start();
    }

    private void setGui() {
        setLanguage(LANG);
        setGameActors(USERvsCOMP);

        rbUserVScomp.setSelected(true);

        cBoard.addMouseListener(act);
        itemExit.addMouseListener(this);
        itemAbout.addMouseListener(this);
        itemRules.addMouseListener(this);
        itemNewGame.addMouseListener(this);

        rbRusLang.addMouseListener(this);
        rbEngLang.addMouseListener(this);
        rbUkrLang.addMouseListener(this);

        rbUserVScomp.addMouseListener(this);
        rbCompVSuser.addMouseListener(this);
        rbUserVSuser.addMouseListener(this);
        rbCompVScomp.addMouseListener(this);

        menuGame.add(itemNewGame);
        menuGame.add(itemExit);

        langButtonGroup.add(rbRusLang);
        langButtonGroup.add(rbEngLang);
        langButtonGroup.add(rbUkrLang);

        itemLanguage.add(rbRusLang);
        itemLanguage.add(rbEngLang);
        itemLanguage.add(rbUkrLang);

        actorButtonGroup.add(rbUserVScomp);
        actorButtonGroup.add(rbCompVSuser);
        actorButtonGroup.add(rbUserVSuser);
        actorButtonGroup.add(rbCompVScomp);

        itemGameActors.add(rbUserVScomp);
        itemGameActors.add(rbCompVSuser);
        itemGameActors.add(rbUserVSuser);
        itemGameActors.add(rbCompVScomp);

        menuSettings.add(itemGameActors);
        menuSettings.add(itemLanguage);

        menuHelp.add(itemRules);
        menuHelp.add(itemAbout);

        menuBar.add(menuGame);
        menuBar.add(menuSettings);
        menuBar.add(menuHelp);

        tArea.setFont(new Font("Dialog", Font.PLAIN, 12));
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        tArea.setEditable(false);

        labelUser.setText(labelWhiteTitle + cBoard.whiteCheckers);
        labelComp.setText(labelBlackTitle + cBoard.blackCheckers);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
        labelUser.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        labelComp.setAlignmentX(JLabel.LEFT_ALIGNMENT);

        resultPanel.setLayout(boxL);
        resultPanel.add(labelUser);
        resultPanel.add(labelComp);
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(scrollPane);
        resultPanel.add(Box.createVerticalStrut(20));

        mainPanel.add(cBoard);
        mainPanel.add(resultPanel);

        frame.addKeyListener(act);
        frame.setFocusable(true);
        frame.getRootPane().setOpaque(true);
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(menuBar, BorderLayout.NORTH);
        frame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(700, 500));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        startObserver();
    }

    public void mousePressed(MouseEvent e) {
        try {
            if (e.getComponent() == itemExit) {
                System.exit(0);
            }

            if (e.getComponent() == itemAbout) {
                JOptionPane.showMessageDialog(null, aboutDeveloperText, aboutTitle, JOptionPane.INFORMATION_MESSAGE);
            }

            if (e.getComponent() == itemRules) {
                Desktop desktop = Desktop.getDesktop();
                desktop.browse(URI.create(rulesLink));
            }

            if (e.getComponent() == itemNewGame) {
                restartGame();
            }

            if (e.getComponent() == rbRusLang) {
                setLanguage(RU);
            }

            if (e.getComponent() == rbEngLang) {
                setLanguage(ENG);
            }

            if (e.getComponent() == rbUkrLang) {
                setLanguage(UA);
            }
            if (e.getComponent() == rbUserVScomp) {
                setGameActors(USERvsCOMP);
            }
            if (e.getComponent() == rbCompVSuser) {
                setGameActors(COMPvsUSER);
            }
            if (e.getComponent() == rbCompVScomp) {
                setGameActors(COMPvsCOMP);
            }
            if (e.getComponent() == rbUserVSuser) {
                setGameActors(USERvsUSER);
            }

        } catch (Exception ex) {
        }
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

        frame.setTitle(frameTitle);
        menuGame.setText(gameTitle);
        menuSettings.setText(settingsTitle);
        itemLanguage.setText(languageTitle);
        itemGameActors.setText(gameActorsTitle);
        rbUserVScomp.setText(userVScompTitle);
        rbCompVSuser.setText(compVSuserTitle);
        rbUserVSuser.setText(userVSuserTitle);
        rbCompVScomp.setText(compVScompTitle);
        menuHelp.setText(helpTitle);
        itemNewGame.setText(newGameTitle);
        itemExit.setText(exitTitle);
        itemRules.setText(rulesTitle);
        itemAbout.setText(aboutTitle);
        labelComp.setText(labelBlackTitle + cBoard.blackCheckers);
        labelUser.setText(labelWhiteTitle + cBoard.whiteCheckers);
    }

    void initMenuValbyXML(Lang lang) {
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

    String getFromXML(String elementName) {
        for (int i = 0; i < menuValues.getLength(); i++) {
            if (menuValues.item(i).getNodeName().equals(elementName)) {
                return menuValues.item(i).getTextContent();
            }
        }
        return "Some problem in XML language file";
    }

    private void setGameActors(GameActors GA) {
        if (GA == USERvsCOMP) {
            logic.whiteIsHuman = true;
            logic.blackIsHuman = false;
        }
        if (GA == COMPvsUSER) {
            logic.whiteIsHuman = false;
            logic.blackIsHuman = true;
        }
        if (GA == COMPvsCOMP) {
            logic.whiteIsHuman = false;
            logic.blackIsHuman = false;
        }
        if (GA == USERvsUSER) {
            logic.whiteIsHuman = true;
            logic.blackIsHuman = true;
        }
    }

    void customResult() {
        cBoard.setCheckersNum();
        labelUser.setText(labelWhiteTitle + cBoard.whiteCheckers);
        labelComp.setText(labelBlackTitle + cBoard.blackCheckers);
        String optionsDialog[] = {dialogNewGame, dialogExit};

        if (cBoard.blackCheckers == 0) {
            gameOwer = true;
            int userChoice = JOptionPane.showOptionDialog(null, noBlackCheckersText, whiteWon, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsDialog, optionsDialog[0]);
            if (userChoice == JOptionPane.YES_OPTION) {
                restartGame();
            }
            if (userChoice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            return;
        }
        if (cBoard.whiteCheckers == 0) {
            gameOwer = true;
            int userChoice = JOptionPane.showOptionDialog(null, noWhiteCheckersText, blackWon, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsDialog, optionsDialog[0]);
            if (userChoice == JOptionPane.YES_OPTION) {
                restartGame();
            }
            if (userChoice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            return;
        }

        if (!logic.getSome(this.cBoard, Logic.Action.FIGHT, Logic.Player.BLACK).isExist() && !logic.getSome(this.cBoard, Logic.Action.MOVE, Logic.Player.BLACK).isExist() && cBoard.blackCheckers != 0) {
            gameOwer = true;
            int userChoice = JOptionPane.showOptionDialog(null, blackIsBlockedText, whiteWon, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsDialog, optionsDialog[0]);
            if (userChoice == JOptionPane.YES_OPTION) {
                restartGame();
            }
            if (userChoice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            return;
        }
        if (!logic.getSome(this.cBoard, Logic.Action.FIGHT, Logic.Player.WHITE).isExist() && !logic.getSome(this.cBoard, Logic.Action.MOVE, Logic.Player.WHITE).isExist() && cBoard.whiteCheckers != 0) {
            gameOwer = true;
            int userChoice = JOptionPane.showOptionDialog(null, whiteIsBlockedText, blackWon, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, optionsDialog, optionsDialog[0]);
            if (userChoice == JOptionPane.YES_OPTION) {
                restartGame();
            }
            if (userChoice == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
            return;
        }


        tArea.append(resultBuf);
        resultBuf = "";
        tArea.setCaretPosition(tArea.getDocument().getLength());
    }

    private void restartGame() {
        gameOwer = true;
        String langArr[] = {LANG.toString()};
        main(langArr);
        frame.dispose();
    }

    private Menu(Lang lang) {
        this.LANG = lang;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            if (args.length > 0) {
                if (args[0].equals(Lang.RU.toString())) {
                    Menu menu = new Menu(Lang.RU);
                    menu.setGui();
                    return;
                }
                if (args[0].equals(Lang.ENG.toString())) {
                    Menu menu = new Menu(Lang.ENG);
                    menu.setGui();
                    return;
                }
                if (args[0].equals(Lang.UA.toString())) {
                    Menu menu = new Menu(Lang.UA);
                    menu.setGui();
                    return;
                }
            } else {
                new Menu(Lang.RU).setGui();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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