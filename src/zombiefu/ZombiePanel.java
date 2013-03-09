package zombiefu;

import jade.ui.TiledTermPanel;

public class ZombiePanel extends TiledTermPanel {
    
    public ZombiePanel(int i, int j, int k) {
		super(i,j,k);
	}

	public static ZombiePanel getFramedTerminal(String title)
    {
        ZombiePanel term = new ZombiePanel(80,60,14);
        frameTermPanel(term, title);
        return term;
    }
}
