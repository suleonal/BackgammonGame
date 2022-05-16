package tr.com.argela.BackgammonGame.be.exception;

import tr.com.argela.BackgammonGame.be.constant.Player;

public class TreasureZoneException extends GameException{
    public TreasureZoneException(Player player ){
        super("Player : "+player+ " has not allowed to get stone from treasure");
    }
}
