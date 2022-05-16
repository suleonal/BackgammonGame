package tr.com.argela.BackgammonGame.be.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.Session;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import tr.com.argela.BackgammonGame.be.constant.GameState;
import tr.com.argela.BackgammonGame.be.constant.Player;

@Getter
@Setter
public class BackgammonBoard {

    String sessionId;
    int pitSize;
    @JsonIgnore
    Player nextPlayer;
    Player currentPlayer;
    GameState gameState;
    Map<Integer, List<Stone>> pits;

    List<Integer> moves = new ArrayList<>();

    public BackgammonBoard(String sessionId, int pitSize) {
        this.sessionId = sessionId;
        initializedBoard(pitSize);
    }

    public void initializedBoard(int pitSize) {
        this.pitSize = pitSize;

        currentPlayer = Player.ONE;
        nextPlayer = Player.TWO;
        gameState = GameState.ACTIVE;

        pits = new LinkedHashMap<>();
        for (int pitId = 0; pitId < pitSize; pitId++) {
            pits.put(pitId, new ArrayList<>());
        }

        addStone(Player.ONE, 0, 2);
        addStone(Player.ONE, 11, 5);
        addStone(Player.ONE, 16, 3);
        addStone(Player.ONE, 18, 5);

        addStone(Player.TWO, 23, 2);
        addStone(Player.TWO, 12, 5);
        addStone(Player.TWO, 7, 3);
        addStone(Player.TWO, 5, 5);

    }

    public void addStone(Player player, int pitId, int size) {
        for (int i = 0; i < size; i++)
            pits.get(pitId).add(new Stone(player));
    }

    public void removeStone(int pitId, int size) {
        for (int i = size - 1; i >= 0; i--)
            pits.get(pitId).remove(i);
    }

    public void rollDice() {
        int dice1;
        int dice2;
        dice1 = (int) (Math.random() * 6);
        dice2 = (int) (Math.random() * 6);

        moves.clear();
        if (dice1 == dice2) {
            for (int i = 0; i < 4; i++) {
                moves.add(dice1);
            }
        } else {
            moves.add(dice1);
            moves.add(dice2);
        }
    }

  

    public void diceControl(Player player) {
        int playerOneDice=0;
        int playerTwoDice=0;
        boolean notEqualDice = true;

        while (notEqualDice) {
            playerOneDice = (int) (Math.random() * 6);
            playerTwoDice = (int) (Math.random() * 6);

            if (playerOneDice != playerTwoDice) {
                notEqualDice = false;
                break;
            }
        }
        if (playerOneDice < playerTwoDice) {
            currentPlayer = Player.TWO;
            nextPlayer = Player.ONE;
        } else {
            currentPlayer = Player.ONE;
            nextPlayer = Player.TWO;
        }
    }

}
