package zombiefu.monster;

import jade.fov.RayCaster;
import zombiefu.items.MensaCard;
import zombiefu.items.Waffe;
import jade.util.Dice;
import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import jade.util.datatype.Coordinate;
import jade.util.datatype.Direction;

import java.awt.Color;
import java.util.Collections;
import java.util.List;
import zombiefu.actor.Creature;
import zombiefu.exception.CannotMoveToIllegalFieldException;
import zombiefu.exception.NoPlaceToMoveException;
import zombiefu.exception.WeaponHasNoMunitionException;
import zombiefu.exception.TargetNotFoundException;
import zombiefu.exception.NoDirectionGivenException;
import zombiefu.exception.TargetIsNotInThisWorldException;
import zombiefu.items.Item;
import zombiefu.ki.StupidMover;
import zombiefu.ki.MoveAlgorithm;
import zombiefu.player.Player;
import zombiefu.util.ZombieGame;
import zombiefu.util.ZombieTools;

public class Monster extends Creature {

    protected MoveAlgorithm movealg;
    private Waffe waffe;
    protected int ectsYield;
    private Item item;

    public Monster(String name, int h, int a, int d, Waffe w, int ects,
            Item item) {
        this(ColoredChar.create('\u265E', Color.RED), name, h, a, d, w, ects);
        this.item = item;
    }

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w,
            int ects, int s, MoveAlgorithm m) {
        super(face, n, h, a, d);
        waffe = w;
        movealg = m;
        fov = new RayCaster();
        sichtweite = s;
        ectsYield = ects;
        if (item != null) {
            if (Dice.global.chance(85)) {
                this.item = new MensaCard(Dice.global.nextInt(1, 100));
            }
        }
    }

    public Monster(ColoredChar face, String n, int h, int a, int d, Waffe w,
            int ects) {
        this(face, n, h, a, d, w, ects, 10, new StupidMover());
    }

    private void moveRandomly() throws NoPlaceToMoveException {
        List<Direction> dirs = ZombieTools.getAllowedDirections();
        Collections.shuffle(dirs);
        for (Direction d : dirs) {
            try {
                tryToMove(d);
                return;
            } catch (CannotMoveToIllegalFieldException ex) {
            } catch (WeaponHasNoMunitionException ex) {
            }
        }
        throw new NoPlaceToMoveException();
    }

    private Coordinate getPlayerPosition()
            throws TargetIsNotInThisWorldException {
        Guard.argumentIsNotNull(world());
        Player player = world().getActor(Player.class);

        if (player == null) {
            throw new TargetIsNotInThisWorldException();
        }

        return player.pos();
    }

    protected boolean positionIsVisible(Coordinate pos)
            throws TargetIsNotInThisWorldException {
        return fov.getViewField(world(), pos(), sichtweite).contains(pos);
    }

    protected Direction directionToPlayer() throws TargetNotFoundException,
            TargetIsNotInThisWorldException {
        return movealg.directionTo(world(), pos(), getPlayerPosition());
    }

    protected void moveToPlayer() throws TargetIsNotInThisWorldException,
            TargetNotFoundException, WeaponHasNoMunitionException {
        try {
            tryToMove(directionToPlayer());
        } catch (CannotMoveToIllegalFieldException ex) {
            // Logger.getLogger(Monster.class.getName()).log(Level.SEVERE, null,
            // ex);
        }
    }

    @Override
    public void act() {
        try {
            if (positionIsVisible(getPlayerPosition())) {
                moveToPlayer();
                return;
            }
        } catch (TargetIsNotInThisWorldException ex) {
        } catch (TargetNotFoundException ex) {
        } catch (WeaponHasNoMunitionException ex) {
        }
        try {
            moveRandomly();
        } catch (NoPlaceToMoveException ex) {
            ZombieTools.log(getName() + ": Cannot move - doing nothing");
            return;
        }
    }

    @Override
    public Waffe getActiveWeapon() {
        return waffe;
    }

    protected Item itemDroppedOnKill() {
        return item;
    }

    @Override
    protected void killed(Creature killer) {
        Item it = itemDroppedOnKill();
        if (it != null) {
            world().addActor(it, pos());
        } else if (ZombieGame.getPlayer() == killer) {
            ZombieGame.getPlayer().giveECTS(ectsYield);
        }
        expire();
        ZombieGame.newMessage(killer.getName() + " hat " + getName()
                + " getötet.");
    }

    @Override
    protected Direction getAttackDirection() throws NoDirectionGivenException {
        // TODO: Überprüfen, ob Gegner wirklich in einer Linie ist
        try {
            return directionToPlayer();
        } catch (TargetNotFoundException e) {
            throw new NoDirectionGivenException();
        } catch (TargetIsNotInThisWorldException ex) {
            throw new NoDirectionGivenException();
        }
    }
}
