package zombiefu.ui;

import jade.ui.TermPanel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;

// Erweitert ein JFrame und hält drei dazugehörige TermPanels.
// topTerm() für die Statusanzeige oben.
// mainTerm() für die Karte.
// bottomTerm() für die Statusanzeige unten.
public class ZombieFrame extends JFrame {

    private TermPanel mainTerm, topTerm, bottomTerm;

    // Ordnet die TermPanels manuell ohne LayoutManager untereinander an.
    // Besser nicht anfassen, fragil!
    private void addTerminals(JPanel panel) {

        // Wir geben dem Panel einen schmalen Rand. Sieht besser aus
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));

        // Kein LayoutManager
        panel.setLayout(null);

        // TermPanels zu Panel hinzufügen - ggf. Fokus entfernen.
        panel.add(topTerm.panel());
        topTerm.panel().setFocusable(false);

        panel.add(mainTerm.panel());

        panel.add(bottomTerm.panel());
        bottomTerm.panel().setFocusable(false);

        // Insets des Panels
        Insets insets = panel.getInsets();

        // Positionierung TopZeile
        Dimension sizeTop = topTerm.panel().getPreferredSize();
        topTerm.panel().setBounds(insets.left, insets.top,
                                  sizeTop.width, sizeTop.height);

        // Positionierung Karte
        Dimension sizeMain = mainTerm.panel().getPreferredSize();
        mainTerm.panel().setBounds(insets.left, insets.top+sizeTop.height,
                                   sizeMain.width, sizeMain.height);

        // Positionierung BottomZeile
        Dimension sizeBottom = bottomTerm.panel().getPreferredSize();
        bottomTerm.panel().setBounds(insets.left, insets.top+sizeTop.height+sizeMain.height,
                                   sizeBottom.width, sizeBottom.height);

        // Setzen der Panelgröße.
        panel.setPreferredSize(new Dimension(sizeMain.width + insets.left + insets.right,
                                             sizeTop.height + sizeMain.height + sizeBottom.height + insets.top + insets.bottom));
    }

    // Default Constructor
    public ZombieFrame(String title) {
        this(title, 80, 35, 15);
    }

    public ZombieFrame(String title, int columns, int rows, int tilesize) {
        super(title);

        // Schwarzer Hintergrund
        this.getContentPane().setBackground(Color.BLACK);

        // Das die TermPanels umschließende Panel
        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);

        // Statuspanels sind harcoded eine Reihe hoch und so breit wie die Karte.
        topTerm    = new TermPanel(columns, 1,    tilesize);
        mainTerm   = new TermPanel(columns, rows, tilesize);
        bottomTerm = new TermPanel(columns, 2,   tilesize);

        addTerminals(panel);
        this.getContentPane().add(panel);
        this.pack();

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
