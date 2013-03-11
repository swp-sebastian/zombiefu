package zombiefu.items;

import jade.util.datatype.ColoredChar;

public class Nahrung extends Item {
    
    private int heilkraft;

    public Nahrung(ColoredChar face, String name, int h) {
        super(face, name);
        heilkraft = h;
    }
}
