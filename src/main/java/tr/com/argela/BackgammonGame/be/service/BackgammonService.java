package tr.com.argela.BackgammonGame.be.service;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tr.com.argela.BackgammonGame.be.config.GameConfig;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.CurrentPlayerIsChangedException;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.exception.PlayerNotAuthorized;
import tr.com.argela.BackgammonGame.be.exception.PunishZoneHasStoneException;
import tr.com.argela.BackgammonGame.be.exception.WrongMoveException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.model.PlayerInfo;
import tr.com.argela.BackgammonGame.be.repository.BackgommonRepository;

@Service
public class BackgammonService {

    Logger logger = LoggerFactory.getLogger(BackgammonService.class);

    Player player;

    @Autowired
    BackgommonRepository backgammonRepository;

    @Autowired
    GameConfig gameConfig;

    public String createNewGame(String name, String ip) {
        String sessionId = createSessionId();
        String playerOneSessionId = createSessionId();
        PlayerInfo playerOneSession = new PlayerInfo(playerOneSessionId, name, ip);
        BackgammonBoard backgammonBoard = new BackgammonBoard(sessionId, gameConfig.getPitSize(),playerOneSession);
        backgammonRepository.save(sessionId, backgammonBoard);
        if (logger.isInfoEnabled()) {
            logger.debug("[NewGame] sessionId:" + sessionId + " , pitSize:" + gameConfig.getPitSize());
        }
        return sessionId;
    }

    public void join(String sessionId, String name, String ip) throws GameException{
        String playerTwoSessionId = createSessionId();
        BackgammonBoard backgammonBoard = getBackgammonBoard(sessionId);
        PlayerInfo playerTwoSession = new PlayerInfo(playerTwoSessionId, name, ip);
        backgammonBoard.setPlayerInfo(playerTwoSession);
    }

    // generate et id yi
    private String createSessionId() {
        return UUID.randomUUID().toString();
    }
    

    public BackgammonBoard getBackgammonBoard(String sessionId) throws GameException {
        return backgammonRepository.getBySessionId(sessionId);
    }

    public void rollDice(String sessionId) throws GameException {
        BackgammonBoard board = getBackgammonBoard(sessionId);
        board.rollDice();
    }

    public void validateMove(BackgammonBoard backgammonBoard, int requestedMove, int source, int dest)
            throws GameException {
        if (!(Player.isPlayerZone(source) || Player.isPlayerZone(dest)
                || ((source >= 0 && source < gameConfig.getPitSize())
                        && (dest >= 0 && dest < gameConfig.getPitSize())))) {
            throw new WrongMoveException(player, source, dest);
        }

        if (!backgammonBoard.isPunishmentZoneHasStone(source, dest, requestedMove)) {
            throw new PunishZoneHasStoneException();
        }

        if (!backgammonBoard.hasStone(source)) {
            throw new WrongMoveException(player, source, dest, " has not stone on source pit");
        }

        if (!backgammonBoard.isDestinationValid(dest)) {
            throw new WrongMoveException(player, source, dest, " not valid action");
        }

    }

    public void move(String sessionId, PlayerInfo playerSessionId, int source, int dest) throws GameException {

        BackgammonBoard board = getBackgammonBoard(sessionId);

        int requestedMove = board.validateDice(source, dest);
  
        try {
            if(board.getCurrentPlayer()==Player.ONE){
                //if(playerSessionId == board.getPlayerInfo()|| board.getPlayerInfo().playerTwoSessionId){
                validateMove(board, requestedMove, source, dest);
                board.addStone(board.getCurrentPlayer(), dest, 1);
                board.removeStone(source, 1);
                } else {
                    throw new PlayerNotAuthorized();
                
            }
        } catch (CurrentPlayerIsChangedException e) {
            System.out.println("User is changed due to no more valid moves");
        } catch (GameException e) {
            if (requestedMove > -1) {
                board.getMoves().add(requestedMove);
            }
            throw e;
        }
        if (board.getMoves().size() == 0) {
            board.setCurrentPlayer(board.getCurrentPlayer().getOtherPlayer());
        }

    }
 
}
