package pl.application.passwordwallet.service;

import org.springframework.stereotype.Service;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.model.WalletPasswordRequest;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletPasswordRequestService {
    public List<User> generateDistinctUserList(List<WalletPasswordRequest> requests) {
        if(requests.isEmpty())
            return null;
        List<User> users = new ArrayList<>();
        users.add(requests.get(0).getUserFrom());
        for(int i = 1; i < requests.size(); i++) {
            if(!requests.get(i - 1).getUserFrom().getEmail().equals(requests.get(i).getUserFrom().getEmail()))
                users.add(requests.get(i).getUserFrom());
        }
        return users;
    }
}
