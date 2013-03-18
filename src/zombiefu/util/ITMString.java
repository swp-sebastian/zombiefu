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
import zombiefu.actor.Teleporter;
import zombiefu.items.Item;
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
        Set<Actor> ret = new HashSet<>();
        String[] strings = itmString.split(" ");
        for (String s : strings) {
            Matcher m = Pattern.compile("^(\\w+)\\((.+)\\)x?([0-9]*)$").matcher(s);
            Guard.verifyState(m.matches());
            String key = m.group(1);
            String[] arguments = m.group(2).split("\\s?,\\s?");
            int anzahl = m.group(3).isEmpty() ? 1 : Integer.decode(m.group(3));
            // Tomas: Ich möchte hier eigentlich switch benutzen, aber ich 
            // darf nicht, weil Java 6 das nicht kann. Grrrrrr!
            for (int i = 1; i <= anzahl; i++) {
                switch (key) {
                    case "food":
                        ret.add(ConfigHelper.newFoodByName(arguments[0]));
                        break;
                    case "weapon":
                        ret.add(ConfigHelper.newWeaponByName(arguments[0]));
                        break;
                    case "door":
                        ret.add(ConfigHelper.getDoorByName(arguments[0]));
                        break;
                    case "key":
                        ret.add(ConfigHelper.getKeyCardByName(arguments[0]));
                        break;
                    case "mensacard":
                        ret.add(new MensaCard(arguments.length > 1 ? Dice.global.nextInt(Integer.decode(arguments[0]), Integer.decode(arguments[1])) : Integer.decode(arguments[0])));
                        break;
                    case "shop":
                        ret.add(ConfigHelper.newShopByName(arguments[0]));
                        break;
                    case "human":
                        ret.add(ConfigHelper.newHumanByName(arguments[0]));
                        break;
                    case "monster":
                        ret.add(ConfigHelper.newMonsterByName(arguments[0]));
                        break;
                    case "teleporter":
                        ret.add(new Teleporter(arguments[0], new Coordinate(Integer.decode(arguments[1]), Integer.decode(arguments[2]))));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid ITM String");
                }
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
