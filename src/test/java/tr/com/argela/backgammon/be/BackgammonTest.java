
package tr.com.argela.backgammon.be;

import static org.junit.Assert.assertThrows;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import tr.com.argela.BackgammonGame.be.BeApplication;
import tr.com.argela.BackgammonGame.be.constant.Player;
import tr.com.argela.BackgammonGame.be.exception.AllStoneNotIsPlayerZoneException;
import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.exception.PitIsBlokedByComponentException;
import tr.com.argela.BackgammonGame.be.exception.CurrentPlayerIsChangedException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.model.Stone;
import tr.com.argela.BackgammonGame.be.service.BackgammonService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = BeApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class BackgammonTest {

	@Autowired
	BackgammonService backgammonService;

	@Test
	void contextLoads() {
	}

	/*@Test
	public void test_start() {
		String token = createGame();
		assert token != null;
	}*/

	/*@Test
	public void test_get_board() throws GameException {
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);
		Map<Integer, Integer> pits = new LinkedHashMap<>();
		pits.put(0, 2);
		pits.put(5, 5);
		pits.put(7, 3);
		pits.put(11, 5);
		pits.put(12, 5);
		pits.put(16, 3);
		pits.put(18, 5);
		pits.put(23, 2);

		for (int i = 0; i < 24; i++) {
			int expectedStoneSize = pits.getOrDefault(i, 0);
			assert board.getPits().get(i).size() == expectedStoneSize;
		}
	}

	@Test
	public void test_success_move() throws GameException {
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);
		board.getMoves().add(5);
		board.getMoves().add(3);
		backgammonService.move(sessionId, 16, 21);
		backgammonService.move(sessionId, 18, 21);

		Map<Integer, Integer> pits = new LinkedHashMap<>();
		pits.put(0, 2);
		pits.put(5, 5);
		pits.put(7, 3);
		pits.put(11, 5);
		pits.put(12, 5);
		pits.put(16, 2);
		pits.put(18, 4);
		pits.put(21, 2);
		pits.put(23, 2);
		for (int i = 0; i < 24; i++) {
			int expectedStoneSize = pits.getOrDefault(i, 0);
			assert board.getPits().get(i).size() == expectedStoneSize;
		}
		assert board.getCurrentPlayer() == Player.TWO;
	}

	@Test
	public void test_block_test() throws GameException {
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);
		board.getMoves().add(5);
		board.getMoves().add(3);

		assertThrows(PitIsBlokedByComponentException.class, () -> {
			backgammonService.move(sessionId, 18, 23);
		});
	}

	@Test
	public void test_send_stone_to_punishment() throws GameException {
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);
		board.getMoves().add(2);
		board.getMoves().add(3);

		board.getPits().get(20).add(new Stone(Player.TWO));
		board.getPits().get(21).add(new Stone(Player.TWO));

		backgammonService.move(sessionId, 18, 20);
		backgammonService.move(sessionId, 18, 21);
		System.out.println("Punish:" + board.getPunishZone().get(Player.TWO));
		assert board.getPunishZone().get(Player.TWO) == 2;
	}

	@Test
	public void test_send_stone_to_treasure() throws GameException {
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);
		board.getMoves().add(6);
		board.getMoves().add(6);
		board.getMoves().add(6);
		board.getMoves().add(6);

		for (int i = 0; i <= 23; i++) {
			board.getPits().get(i).clear();
		}
		board.getTreasureZone().put(Player.ONE, 11);
		board.addStone(Player.ONE, 18, 4);

		backgammonService.move(sessionId, 18, Player.ONE.getTreasureZoneId());
		backgammonService.move(sessionId, 18, Player.ONE.getTreasureZoneId());

		
		assert board.getTreasureZone().get(Player.ONE) == 13;
	}

	@Test
	public void test_send_stone_to_treasure_failure() throws GameException {
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);
		board.getMoves().add(6);
		board.getMoves().add(6);

		assertThrows(AllStoneNotIsPlayerZoneException.class, () -> {
			backgammonService.move(sessionId, 18, Player.ONE.getTreasureZoneId());
		});
	}*/

	/*String createGame() {
		return backgammonService.createNewGame();
	}

	@Test
	public void test_current_player_changed() throws GameException{
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);

		board.getMoves().add(2);
		board.getMoves().add(3);

		board.getPits().get(1).add(new Stone(Player.TWO));
		board.getPits().get(1).add(new Stone(Player.TWO));
		board.getPits().get(2).add(new Stone(Player.TWO));

		board.getPunishZone().put(Player.ONE,2);

		backgammonService.move(sessionId, -1, 2);
		
		assert board.getCurrentPlayer() == Player.TWO;
		assert board.getPunishZone().get(Player.ONE)==1;
		
	}

	@Test
	public void test_treasure_zone() throws GameException{
		String sessionId = createGame();
		BackgammonBoard board = backgammonService.getBackgammonBoard(sessionId);

		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(18).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		board.getPits().get(21).add(new Stone(Player.ONE));
		

		board.getMoves().add(6);
		board.getMoves().add(3);

		assert board.getTreasureZone().get(Player.ONE)==2;
	}*/
}