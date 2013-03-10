/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.util;

import jade.core.World;
import jade.util.datatype.Coordinate;
import zombiefu.creature.Player;
import zombiefu.items.Teleporter;
import zombiefu.level.Level;

/**
 *
 * @author tomas
 */
public class ZombieTools {
    
    public static void createBidirectionalTeleporter(World world1, Coordinate coord1, World world2, Coordinate coord2) {
        Teleporter tel1 = new Teleporter(world2, coord2);
        Teleporter tel2 = new Teleporter(world1, coord1);
        world1.addActor(tel1, coord1);
        world2.addActor(tel2, coord2);
    }

    public static void createStoryForPlayer(Player player) {
        
        // Welten und Teleporter
        Level world1 = Level.levelFromFile("src/sources/Testraum.txt");
        Level world2 = Level.levelFromFile("src/sources/TestraumZ.txt");
        createBidirectionalTeleporter(world1, new Coordinate(33, 19), world2, new Coordinate(33, 1));
        
        // Spieler auf erste Welt setzen
        world1.addActor(player);
        
    }
}
