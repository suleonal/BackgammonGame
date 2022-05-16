package tr.com.argela.BackgammonGame.be.exception;

import tr.com.argela.BackgammonGame.be.constant.Player;

public class PunishZoneHasNoStoneException extends GameException{
    public PunishZoneHasNoStoneException(Player player ){
        super("Player : "+player+ " has no stone in its punish zone");
    }
}
