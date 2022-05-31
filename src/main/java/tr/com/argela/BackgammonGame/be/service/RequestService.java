package tr.com.argela.BackgammonGame.be.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

@Service
public interface RequestService {
    String getClientIpAddress(HttpServletRequest request);
}
