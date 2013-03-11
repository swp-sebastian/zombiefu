package zombiefu.ui;

import jade.ui.TermPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;

// Erweitert ein JFrame und hält drei dazugehörige TermPanels.
// topTerm() für die Statusanzeige oben.
// mainTerm() für die Karte
// bottomTerm() für die Statusanzeige unten.
public class ZombieFrame extends JFrame {

    private TermPanel mainTerm, topTerm, bottomTerm;
    private JFrame frame;

    // Ordnet die TermPanels manuell ohne LayoutManageruntereinander an.
    // Besser nicht anfassen, fragil!
    private Dimension addTerminals(Container pane) {
        pane.setLayout(null);
        pane.add(topTerm.panel());
        pane.add(mainTerm.panel());
        pane.add(bottomTerm.panel());

        Insets insets = pane.getInsets();
        Dimension sizeTop = topTerm.panel().getPreferredSize();
        topTerm.panel().setBounds(insets.left, insets.top,
                                  sizeTop.width, sizeTop.height);

        Dimension sizeMain = mainTerm.panel().getPreferredSize();
        mainTerm.panel().setBounds(insets.left, insets.top+sizeTop.height,
                                   sizeMain.width, sizeMain.height);

        Dimension sizeBottom = bottomTerm.panel().getPreferredSize();
        bottomTerm.panel().setBounds(insets.left, insets.top+sizeTop.height+sizeMain.height,
                                   sizeBottom.width, sizeBottom.height);

        return new Dimension(insets.left+sizeMain.width, insets.top+sizeTop.height + sizeMain.height + sizeBottom.height+100); //+ 100?
    }

    // Default Constructor
    public ZombieFrame(String title) {
        this(title, 80, 35, 15);
    }

    public ZombieFrame(String title, int columns, int rows, int tilesize) {
        super(title);

        // Schwarzer Hintergrund
        this.getContentPane().setBackground(Color.BLACK);

        // Statuspanels sind harcoded eine Reihe hoch und so breit wie die Karte.
        topTerm    = new TermPanel(columns, 1,    tilesize);
        mainTerm   = new TermPanel(columns, rows, tilesize);
        bottomTerm = new TermPanel(columns, 1,   tilesize);

        Dimension sizeAll = addTerminals(this.getContentPane());
        this.setSize(sizeAll);

        // Marked for removal.
        topTerm.bufferString(0,0,"Oben TEST TEST TEST");
        topTerm.refreshScreen();
        bottomTerm.bufferString(0,0,"Unten TEST TEST TEST");
        bottomTerm.refreshScreen();

        // Fenster anzeigen
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        // Fokus an die Karte.
        mainTerm.panel().requestFocusInWindow();
    }

    public TermPanel topTerm() {
        return topTerm;
    }

    public TermPanel mainTerm() {
        return mainTerm;
    }

    public TermPanel bottomTerm() {
        return bottomTerm;
    }
}
