package tr.com.argela.BackgammonGame.be.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

import tr.com.argela.BackgammonGame.be.repository.BackgommonRepository;
import tr.com.argela.BackgammonGame.be.repository.BackgammonRepositoryImpl;

import org.springframework.beans.factory.annotation.Value;

@Configuration
@Getter
public class GameConfig {

    @Value("#{new Integer('${backgammon.general.pitSize}')}")
    int pitSize;

    @Bean
    public BackgommonRepository getBackgammonRepository() {
        return new BackgammonRepositoryImpl();
    }

}
