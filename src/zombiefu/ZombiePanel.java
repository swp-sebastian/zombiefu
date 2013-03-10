package zombiefu;

import jade.ui.TermPanel;

public class ZombiePanel extends TermPanel {

    public ZombiePanel(int i, int j, int k) {
        super(i,j,k);
    }

    public ZombiePanel() {
        super();
    }

    public static ZombiePanel getFramedTerminal(String title)
    {
        ZombiePanel term = new ZombiePanel(80,60,14);
        frameTermPanel(term, title);
        return term;
    }
}
