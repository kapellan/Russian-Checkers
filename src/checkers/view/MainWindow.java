package checkers.view;

import checkers.controller.ActionChessBoard;
import checkers.model.ChessBoardData;
import checkers.model.Logic;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.*;

/**
 * This class provides GUI
 *
 * @author Kapellan
 */
public class MainWindow {

    private ChessBoardData data;
    private final ChessBoard cBoard;
    private JTextArea tArea = new JTextArea(26, 12);
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
    //
    private JFrame frameModal;
    private JLabel labelModal;
    private JButton buttonModalExit = new JButton();
    private JButton buttonModalNewGame = new JButton();

    public void updateGui() {
        labelUser.setText(data.labelWhiteTitle + data.whiteCheckers);
        labelComp.setText(data.labelBlackTitle + data.blackCheckers);
        tArea.append(data.resultBuf);
        tArea.setCaretPosition(tArea.getDocument().getLength());
    }

    public void updateTextGuiLanguageInfo() {
        setGuiText();
    }

    public void updateTextGuiLabels() {
        labelUser.setText(data.labelWhiteTitle + data.whiteCheckers);
        labelComp.setText(data.labelBlackTitle + data.blackCheckers);
    }

    public void clearTextArea() {
        tArea.setText("");
    }

    private void setGuiText() {
        labelUser.setText(data.labelWhiteTitle + data.whiteCheckers);
        labelComp.setText(data.labelBlackTitle + data.blackCheckers);
        frame.setTitle(data.frameTitle);
        menuGame.setText(data.gameTitle);
        menuSettings.setText(data.settingsTitle);
        itemLanguage.setText(data.languageTitle);
        itemGameActors.setText(data.gameActorsTitle);
        rbUserVScomp.setText(data.userVScompTitle);
        rbCompVSuser.setText(data.compVSuserTitle);
        rbUserVSuser.setText(data.userVSuserTitle);
        rbCompVScomp.setText(data.compVScompTitle);
        menuHelp.setText(data.helpTitle);
        itemNewGame.setText(data.newGameTitle);
        itemExit.setText(data.exitTitle);
        itemRules.setText(data.rulesTitle);
        itemAbout.setText(data.aboutTitle);
        labelComp.setText(data.labelBlackTitle + data.blackCheckers);
        labelUser.setText(data.labelWhiteTitle + data.whiteCheckers);
    }

    public void createShowModalFrame(String frameModalTitle, String labelText, String bNewGame, String bExit) {
        frameModal = new JFrame(frameModalTitle);
        frameModal.setLayout(new BorderLayout());
        buttonModalNewGame.setText(bNewGame);
        buttonModalExit.setText(bExit);
        labelModal = new JLabel(labelText);

        JPanel frameModalPanel = new JPanel(new FlowLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JPanel labelPanel = new JPanel(new BorderLayout());

        labelPanel.add(labelModal);
        frameModalPanel.add(BorderLayout.NORTH, labelPanel);
        frameModalPanel.add(BorderLayout.SOUTH, buttonPanel);

        buttonPanel.add(buttonModalNewGame);
        buttonPanel.add(buttonModalExit);

        frameModal.getContentPane().add(frameModalPanel);
        frameModal.setResizable(false);
        frameModal.setPreferredSize(new Dimension(300, 100));
        frameModal.setFocusable(true);
        frameModal.getRootPane().setOpaque(true);
        frameModal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frameModal.pack();
        frameModal.setLocationRelativeTo(null);
        frameModal.setVisible(true);
        frame.setEnabled(false);
    }

    public void noBlackCkeckersLeft() {
        createShowModalFrame(data.whiteWon, data.noBlackCheckersText, data.newGameTitle, data.exitTitle);
    }

    public void noWhiteCkeckersLeft() {
        createShowModalFrame(data.blackWon, data.noWhiteCheckersText, data.newGameTitle, data.exitTitle);
    }

    public void blackIsBlocked() {
        createShowModalFrame(data.whiteWon, data.blackIsBlockedText, data.newGameTitle, data.exitTitle);
    }

    public void whiteIsBlocked() {
        createShowModalFrame(data.blackWon, data.whiteIsBlockedText, data.newGameTitle, data.exitTitle);
    }

    public void modalFrameDispose() {
        if (frameModal != null) {
            frameModal.dispose();
            frameModal = null;
        }
        frame.setEnabled(true);
        frame.requestFocus();
    }

    public void showAbout() {
        JOptionPane.showMessageDialog(null, data.aboutDeveloperText, data.aboutTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    MainWindow(ChessBoardData data) {
        this.data = data;
        cBoard = new ChessBoard(data);
        setGuiText();

        rbUserVScomp.setSelected(true);

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

        //Image icon = Toolkit.getDefaultToolkit().getImage("logo.png");
        //frame.setIconImage(icon);
        frame.setIconImage(new ImageIcon("./logo.png").getImage());
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
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            Logic logic = new Logic();
            logic.data.setRussianLang();
            logic.data.setUSERvsCOMP();

            MainWindow mainW = new MainWindow(logic.data);
            ActionChessBoard act = new ActionChessBoard(logic.data, logic, mainW);
            logic.data.addDataListener(act);

            mainW.cBoard.addMouseListener(act);
            mainW.frame.addKeyListener(act);

            mainW.itemExit.addActionListener(act);
            mainW.itemAbout.addActionListener(act);
            mainW.itemRules.addActionListener(act);
            mainW.itemNewGame.addActionListener(act);

            mainW.rbRusLang.addActionListener(act);
            mainW.rbEngLang.addActionListener(act);
            mainW.rbUkrLang.addActionListener(act);

            mainW.rbUserVScomp.addActionListener(act);
            mainW.rbCompVSuser.addActionListener(act);
            mainW.rbUserVSuser.addActionListener(act);
            mainW.rbCompVScomp.addActionListener(act);

            mainW.buttonModalNewGame.addActionListener(act);
            mainW.buttonModalExit.addActionListener(act);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
