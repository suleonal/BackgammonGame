package tr.com.argela.BackgammonGame.be.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
        addStone(Player.ONE, 9, 5);
        addStone(Player.ONE, 13, 3);
        addStone(Player.ONE, 15, 5);


        addStone(Player.TWO, 19, 2);
        addStone(Player.TWO, 10, 5);
        addStone(Player.TWO, 6, 3);
        addStone(Player.TWO, 4, 5);

    }

    public void addStone(Player player, int pitId, int size) {
        for (int i = 0; i < size; i++)
            pits.get(pitId).add(new Stone(player));
    }

    public void removeStone(int pitId, int size) {
        for (int i = size-1; i > 0; i--)
            pits.get(pitId).remove(i);
    }

}
