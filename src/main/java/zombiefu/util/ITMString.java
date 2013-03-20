/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.util;

import jade.core.Actor;
import jade.util.Guard;
import jade.util.datatype.Coordinate;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import zombiefu.actor.Teleporter;
import zombiefu.items.Item;
import zombiefu.items.RandomItemGenerator;
import zombiefu.items.MensaCard;

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
        if (itmString == null || itmString.isEmpty()) {
            return ret;
        }
        String[] strings = itmString.split(" ");
        outerloop:
        for (String s : strings) {
            Matcher m = Pattern.compile("^(\\w+)\\((.+)\\)x?([0-9]*)$").matcher(s);
            Guard.verifyState(m.matches());
            String key = m.group(1);
            String[] arguments = m.group(2).split("\\s?,\\s?");
            int anzahl = m.group(3).isEmpty() ? 1 : Integer.decode(m.group(3));
            Actor a;
            for (int i = 1; i <= anzahl; i++) {
                switch (key) {
                    case "food":
                        a = ConfigHelper.newFoodByName(arguments[0]);
                        break;
                    case "weapon":
                        a = ConfigHelper.newWeaponByName(arguments[0]);
                        break;
                    case "door":
                        a = ConfigHelper.getDoorByName(arguments[0]);
                        break;
                    case "key":
                        a = ConfigHelper.getKeyCardByName(arguments[0]);
                        break;
                    case "mensacard":
                        a = new MensaCard(Integer.decode(arguments[0]));
                        break;
                    case "shop":
                        a = ConfigHelper.newShopByName(arguments[0]);
                        break;
                    case "human":
                        a = ConfigHelper.newHumanByName(arguments[0]);
                        break;
                    case "monster":
                        a = ConfigHelper.newMonsterByName(arguments[0]);
                        break;
                    case "random":
                        a = RandomItemGenerator.fromString(arguments[0]).getRandomItem();
                        if (a == null) {
                            continue outerloop;
                        }
                        break;
                    case "teleporter":
                        a = new Teleporter(arguments[0], new Coordinate(Integer.decode(arguments[1]), Integer.decode(arguments[2])));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid ITM String");
                }
                Guard.argumentIsNotNull(a);
                ret.add(a);
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
