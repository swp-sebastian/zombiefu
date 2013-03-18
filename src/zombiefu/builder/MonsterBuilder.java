package zombiefu.builder;

import jade.core.Actor;
import jade.util.datatype.ColoredChar;
import java.util.Set;
import zombiefu.items.Weapon;
import zombiefu.monster.Monster;

public class MonsterBuilder {
    
    private ColoredChar face;
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private int ects;
    private Weapon w;
    private Set<Actor> dropOnDeath;
    
    public MonsterBuilder(ColoredChar face, String name,int hp,int attack,int defense, Weapon w, int ects, Set<Actor> dropOnDeath){
        this.name = name;
        this.face = face;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.w = w;
        this.dropOnDeath = dropOnDeath;
        this.ects = ects;
    }
    
    public Monster buildMonster(){
        return new Monster(face, name,hp,attack,defense,w,ects,dropOnDeath);
    }
}
