package tr.com.argela.BackgammonGame.be.exception;

public class PunishZoneHasStoneException extends GameException{
    public PunishZoneHasStoneException(){
        super(" Play punishment stone first ");
    }
}
