package tr.com.argela.BackgammonGame.be.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.argela.BackgammonGame.be.config.GameConfig;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.repository.BackgommonRepository;

@Service
public class BackgammonService {


    Logger logger = LoggerFactory.getLogger(BackgammonService.class);
    
    @Autowired
    BackgommonRepository BackgammonRepository;
    
    @Autowired
    GameConfig gameConfig;



    public String createNewGame(){
        String sessionId = createSessionId();
        BackgammonBoard BackgammonBoard = new BackgammonBoard(sessionId, gameConfig.getPitSize());
        BackgammonRepository.save(sessionId, BackgammonBoard);
        if (logger.isInfoEnabled()) {
            logger.debug("[NewGame] sessionId:" + sessionId + " , pitSize:" + gameConfig.getPitSize());
        }
        return sessionId;
    }

    private String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public BackgammonBoard getBackgammonBoard(String sessionId) throws GameException{
        return BackgammonRepository.getBySessionId(sessionId);
        
    }
    
}
