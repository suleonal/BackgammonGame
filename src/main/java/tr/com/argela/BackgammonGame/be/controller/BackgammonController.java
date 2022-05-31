package tr.com.argela.BackgammonGame.be.controller;

import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import tr.com.argela.BackgammonGame.be.exception.GameException;
import tr.com.argela.BackgammonGame.be.model.BackgammonBoard;
import tr.com.argela.BackgammonGame.be.service.BackgammonService;
import tr.com.argela.BackgammonGame.be.service.RequestService;

@RestController
@RequestMapping("/backgammon")
public class BackgammonController {

    @Autowired
    BackgammonService backgammonService;
    @Autowired
    RequestService requestService;

    Logger logger = LoggerFactory.getLogger(BackgammonController.class);
    
    @GetMapping("/start/{name}")
    public ResponseEntity<BackgammonResponse> createNewGame(HttpServletRequest request,String name){
        String clientIpAddress = requestService.getClientIpAddress(request);
        String sessionId = backgammonService.createNewGame();
        return new ResponseEntity<>(new BackgammonResponse(sessionId), HttpStatus.OK);
    }
    //join gamesessionid name
    

    @GetMapping("/board/{sessionId}")
    public ResponseEntity<BackgammonResponse> getBoard(@PathVariable String sessionId) {
        try {
            BackgammonBoard BackgammonBoard = backgammonService.getBackgammonBoard(sessionId);
            return new ResponseEntity<>(new BackgammonResponse(sessionId, BackgammonBoard), HttpStatus.OK);
        } catch (GameException ex) {
            logger.error("[getBoard][FAIL] sessionId:" + sessionId + ", msg:" + ex.getMessage(),ex);
            return new ResponseEntity<>(new BackgammonResponse(sessionId, ex), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/roll/{sessionId}")
    public ResponseEntity<BackgammonResponse> rollDice(@PathVariable String sessionId) {
        try {
            backgammonService.rollDice(sessionId);
            BackgammonBoard BackgammonBoard = backgammonService.getBackgammonBoard(sessionId);
            return new ResponseEntity<>(new BackgammonResponse(sessionId, BackgammonBoard), HttpStatus.OK);
        } catch (GameException ex) {
            logger.error("[getBoard][FAIL] sessionId:" + sessionId + ", msg:" + ex.getMessage(),ex);
            return new ResponseEntity<>(new BackgammonResponse(sessionId, ex), HttpStatus.UNAUTHORIZED);
        }
    }
    @GetMapping("/move/{sessionId}/{source}/{dest}")
    public ResponseEntity<BackgammonResponse> move(@PathVariable String sessionId,@PathVariable int source,@PathVariable int dest) {
        try {
            backgammonService.move(sessionId,source,dest);
            BackgammonBoard BackgammonBoard = backgammonService.getBackgammonBoard(sessionId);
            return new ResponseEntity<>(new BackgammonResponse(sessionId, BackgammonBoard), HttpStatus.OK);
        } catch (GameException ex) {
            logger.error("[getBoard][FAIL] sessionId:" + sessionId + ", msg:" + ex.getMessage(),ex);
            return new ResponseEntity<>(new BackgammonResponse(sessionId, ex), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
        String clientIpAddress = requestService.getClientIpAddress(request);
        model.addAttribute("clientIPAddress", clientIpAddress);
        return "index";
    }

}
