package tr.com.argela.BackgammonGame.be.exception;



public class PitIsBlokedByComponentException extends GameException{
    public PitIsBlokedByComponentException(int pitId){
        super("Pit is blocked by other user . PitID :"+pitId);
    }
}
