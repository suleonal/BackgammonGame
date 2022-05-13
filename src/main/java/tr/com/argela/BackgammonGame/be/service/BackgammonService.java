package tr.com.argela.BackgammonGame.be.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.argela.BackgammonGame.be.config.GameConfig;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.repository.BackgommonRepository;

@Service
public class BackgammonService {

    Logger logger = LoggerFactory.getLogger(BackgammonService.class);

    Player player;

    @Autowired
    BackgommonRepository backgammonRepository;

    @Autowired
    GameConfig gameConfig;

    int zar = (int) Math.random() * 5 + 1;

    public String createNewGame() {
        String sessionId = createSessionId();
        BackgammonBoard backgammonBoard = new BackgammonBoard(sessionId, gameConfig.getPitSize());
        backgammonRepository.save(sessionId, backgammonBoard);
        if (logger.isInfoEnabled()) {
            logger.debug("[NewGame] sessionId:" + sessionId + " , pitSize:" + gameConfig.getPitSize());
        }
    
        return sessionId;
      
    }

    private String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public BackgammonBoard getBackgammonBoard(String sessionId) throws GameException {
        return backgammonRepository.getBySessionId(sessionId);

    }
    public void rollDice(String sessionId) throws GameException{
        BackgammonBoard board= getBackgammonBoard(sessionId);
        board.rollDice();
    }

    public void move(String sessionId) throws GameException{
        BackgammonBoard board = getBackgammonBoard(sessionId);
        //board.move();
    }

}
