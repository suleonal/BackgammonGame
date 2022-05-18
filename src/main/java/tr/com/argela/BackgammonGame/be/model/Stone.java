package tr.com.argela.BackgammonGame.be.model;

import lombok.Getter;
import lombok.Setter;
import tr.com.argela.BackgammonGame.be.constant.Player;

@Getter
@Setter
public class Stone {
    Player player;
    boolean insideZone;


    public Stone(Player player){
        this.player=player;
        insideZone=false;
    }

    public Stone(Player player,boolean insideZone){
        this.player=player;
        this.insideZone = insideZone;
    }
    
}
