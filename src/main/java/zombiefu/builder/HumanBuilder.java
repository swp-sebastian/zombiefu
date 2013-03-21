package zombiefu.builder;

import jade.util.Guard;
import jade.util.datatype.ColoredChar;
import java.util.Map;
import zombiefu.creature.AttributeSet;
import zombiefu.human.BuyingHuman;
import zombiefu.human.GivingHuman;
import zombiefu.human.Human;
import zombiefu.human.SellingHuman;
import zombiefu.human.TalkingHuman;
import zombiefu.human.TradingHuman;
import zombiefu.items.Item;

public class HumanBuilder {

    private ColoredChar face;
    private String name;
    private Map<String, String> phraseSet;
    private Item offerItem;
    private Integer offerMoney;
    private String requestItem;
    private Integer requestMoney;
    private AttributeSet attSet;

    public HumanBuilder(ColoredChar face, String name, AttributeSet attSet, Map<String, String> phraseSet, Item offerItem, Integer offerMoney, String requestItem, Integer requestMoney) {
        this.face = face;
        this.name = name;
        this.phraseSet = phraseSet;
        this.offerItem = offerItem;
        this.offerMoney = offerMoney;
        this.requestItem = requestItem;
        this.requestMoney = requestMoney;
        this.attSet = attSet;
        Guard.verifyState(offerItem == null || offerMoney == null);
        Guard.verifyState(requestItem == null || requestMoney == null);
        Guard.verifyState(offerMoney == null || requestMoney == null);
    }

    public Human buildHuman() {
        if (offerItem != null) {
            if (requestItem != null) {
                return new TradingHuman(face, name, attSet, offerItem, requestItem, phraseSet);
            } else if (requestMoney != null) {
                return new SellingHuman(face, name, attSet, offerItem, requestMoney, phraseSet);
            } else {
                return new GivingHuman(face, name, attSet, offerItem, phraseSet);
            }
        } else if (offerMoney != null && requestItem != null) {
            return new BuyingHuman(face, name, attSet, requestItem, offerMoney, phraseSet);            
        } else {
            return new TalkingHuman(face, name, attSet, phraseSet.get("default"));
        }
    }
}
