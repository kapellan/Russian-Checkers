package checkers.model;

/**
 *
 * @author Kapellan
 */
public class ObservePlayerQueue implements Runnable {

    private Logic logic;

    public void run() {
        try {
            while (!logic.data.gameExit) {
                Thread.sleep(1000);
                if ((!logic.data.whiteIsHuman && logic.data.whiteSessionContinue && !logic.data.gameOwer)
                        || (!logic.data.blackIsHuman && logic.data.blackSessionContinue && !logic.data.gameOwer)) {
                    logic.compStep();
                }
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

    public ObservePlayerQueue(Logic logic) {
        this.logic = logic;
    }
}
