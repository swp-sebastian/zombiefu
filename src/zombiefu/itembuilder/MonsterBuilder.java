package zombiefu.itembuilder;

import zombiefu.items.Item;
import zombiefu.items.Waffe;
import zombiefu.monster.Monster;

public class MonsterBuilder {
    
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private int ects;
    private Waffe w;
    private Item item;
    
    public MonsterBuilder(String name,int hp,int attack,int defense, Waffe w, int ects, Item item){
        this.name = name;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.w = w;
        this.item = item;
        this.ects = ects;
    }
    
    public Monster buildMonster(){
        return new Monster(name,hp,attack,defense,w,ects,item);
    }
}
