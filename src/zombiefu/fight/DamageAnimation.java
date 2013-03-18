/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.fight;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import java.awt.Color;


public class DamageAnimation extends Actor {

    public DamageAnimation() {
        super(ColoredChar.create('\u2605', Color.RED));
    }

    @Override
    public void act() {
        throw new IllegalArgumentException("Interner Fehler: DamageAnimation sollte nie nach Ende einer Runde noch existieren.");
    }
    
}
