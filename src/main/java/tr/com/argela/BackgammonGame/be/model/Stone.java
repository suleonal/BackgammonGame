package tr.com.argela.BackgammonGame.be.model;

import lombok.Getter;
import tr.com.argela.BackgammonGame.be.constant.Player;

@Getter
public class Stone {
    Player player;

    public Stone(Player player){
        this.player=player;
    }
    
}
