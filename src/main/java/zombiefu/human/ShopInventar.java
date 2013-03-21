/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package zombiefu.human;

import jade.util.Guard;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import zombiefu.builder.ItemBuilder;
import zombiefu.util.ConfigHelper;
import zombiefu.util.ZombieTools;

/**
 *
 * @author tomas
 */
public class ShopInventar {

    HashMap<ItemBuilder, Integer> inventar;
    ArrayList<ItemBuilder> list;

    public ShopInventar(HashMap<ItemBuilder, Integer> inventar) {
        this.inventar = inventar;
    }

    public ShopInventar(List<String> list) {
        this.inventar = new HashMap<>();
        for (String s : list) {
            String[] it = s.split(" ");
            ItemBuilder itb = ConfigHelper.getItemBuilderByName(it[0]);
            Guard.argumentIsNotNull(itb);
            this.inventar.put(itb, ZombieTools.parseMoneyString(it[1]));
        }
    }

    public ArrayList<ItemBuilder> asList() {
        if (list == null) {
            this.list = new ArrayList<>();
            for (ItemBuilder it : inventar.keySet()) {
                list.add(it);
            }
            Collections.sort(list, new Comparator<ItemBuilder>() {
                @Override
                public int compare(ItemBuilder t, ItemBuilder t1) {
                    return t.getName().compareTo(t1.getName());
                }
            });
        }
        return list;
    }

    public int get(ItemBuilder item) {
        return inventar.get(item);
    }

    public boolean isEmpty() {
        return inventar.isEmpty();
    }
}
