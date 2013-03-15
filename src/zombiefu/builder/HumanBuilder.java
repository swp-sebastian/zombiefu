package zombiefu.builder;

import jade.core.Actor;
import java.util.Set;
import zombiefu.human.Human;
import zombiefu.items.Waffe;
import zombiefu.monster.Monster;

public class HumanBuilder {
    
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private int ects;
    private Waffe w;
    private Set<Actor> dropOnDeath;
    
    public HumanBuilder(String name,int hp,int attack,int defense, Waffe w, int ects, Set<Actor> dropOnDeath){
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.w = w;
        this.dropOnDeath = dropOnDeath;
        this.ects = ects;
    }
    
    public Human buildHuman(){
        return null;
    }
}
