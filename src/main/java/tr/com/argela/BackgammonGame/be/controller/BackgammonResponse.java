package tr.com.argela.BackgammonGame.be.controller;

import lombok.Getter;
import lombok.Setter;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;

@Getter
@Setter
public class BackgammonResponse {

    String sessionId;
    BackgammonBoard backgammonBoard;
    
    String message;
    Class errorClass;
    boolean failed;

    public BackgammonResponse(String sessionId){
        this.sessionId = sessionId;
        failed = false;
    }

    public BackgammonResponse(String sessionId, BackgammonBoard backgammonBoard) {
        this.sessionId = sessionId;
        this.backgammonBoard = backgammonBoard;
        failed = false;
    }

    public BackgammonResponse(String sessionId, Exception exception) {
        this.sessionId = sessionId;
        this.message = exception.getMessage();
        this.errorClass = exception.getClass();
        failed = true;
    }

    
}
