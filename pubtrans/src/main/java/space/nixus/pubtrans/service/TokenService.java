package space.nixus.pubtrans.service;

import space.nixus.pubtrans.model.Token;
import space.nixus.pubtrans.repository.TokenRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    @Autowired
    TokenRepository tokenRepository;

    public boolean validate(String tokenValue) {
        var token = tokenRepository.findByValue(tokenValue);
        if(token != null) {

        }
    }
}
