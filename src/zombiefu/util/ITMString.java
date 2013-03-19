/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.util;

import jade.core.Actor;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.Coordinate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.print.attribute.AttributeSet;
import zombiefu.actor.Teleporter;
import zombiefu.items.Item;
import zombiefu.items.RandomItemGenerator;
import zombiefu.items.MensaCard;
import static zombiefu.util.ConfigHelper.getKeyCardByName;
import static zombiefu.util.ConfigHelper.newFoodByName;
import static zombiefu.util.ConfigHelper.newHumanByName;
import static zombiefu.util.ConfigHelper.newMonsterByName;
import static zombiefu.util.ConfigHelper.newWeaponByName;

/**
 *
 * @author tomas
 */
public class ITMString {

    private String itmString;

    public ITMString(String s) {
        itmString = s;
    }

    public Set<Actor> getActorSet() {
        Set<Actor> ret = new HashSet<Actor>();
        if(itmString == null || itmString.isEmpty()) {
            return ret;
        }
        String[] strings = itmString.split(" ");
        for (String s : strings) {
            Matcher m = Pattern.compile("^(\\w+)\\((.+)\\)x?([0-9]*)$").matcher(s);
            Guard.verifyState(m.matches());
            String key = m.group(1);
            String[] arguments = m.group(2).split("\\s?,\\s?");
            int anzahl = m.group(3).isEmpty() ? 1 : Integer.decode(m.group(3));
            for (int i = 1; i <= anzahl; i++) {
            if (key.equals("food"))
                ret.add(ConfigHelper.newFoodByName(arguments[0]));
            else if (key.equals("weapon"))
                ret.add(ConfigHelper.newWeaponByName(arguments[0]));
            else if (key.equals("door"))
                ret.add(ConfigHelper.getDoorByName(arguments[0]));
            else if (key.equals("key"))
                ret.add(ConfigHelper.getKeyCardByName(arguments[0]));
            else if (key.equals("mensacard"))
                ret.add(new MensaCard(Integer.decode(arguments[0])));
            else if (key.equals("shop"))
                ret.add(ConfigHelper.newShopByName(arguments[0]));
            else if (key.equals("human"))
                ret.add(ConfigHelper.newHumanByName(arguments[0]));
            else if (key.equals("monster"))
                ret.add(ConfigHelper.newMonsterByName(arguments[0]));
            else if (key.equals("random"))
                ret.add(RandomItemGenerator.fromString(arguments[0]).getRandomItem());
            else if (key.equals("teleporter"))
                ret.add(new Teleporter(arguments[0], new Coordinate(Integer.decode(arguments[1]), Integer.decode(arguments[2]))));
            else
                throw new IllegalArgumentException("Invalid ITM String");
            }
        }
        return ret;
    }
    
    public Item getSingleItem() {
        Set<Actor> actorSet = getActorSet();
        Guard.verifyState(actorSet.size() == 1);
        Actor actor = actorSet.iterator().next();
        Guard.verifyState(actor instanceof Item);
        return (Item) actor;        
    }
}
