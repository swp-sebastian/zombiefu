package zombiefu.itembuilder;

import jade.core.Actor;
import java.util.Set;
import zombiefu.items.Waffe;
import zombiefu.monster.Monster;

public class MonsterBuilder {
    
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private int ects;
    private Waffe w;
    private Set<Actor> dropOnDeath;
    
    public MonsterBuilder(String name,int hp,int attack,int defense, Waffe w, int ects, Set<Actor> dropOnDeath){
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.w = w;
        this.dropOnDeath = dropOnDeath;
        this.ects = ects;
    }
    
    public Monster buildMonster(){
        return new Monster(name,hp,attack,defense,w,ects,dropOnDeath);
    }
}
