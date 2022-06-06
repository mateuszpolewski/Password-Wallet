package pl.application.passwordwallet.configuration.authhandlers;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.application.passwordwallet.model.User;
import pl.application.passwordwallet.service.MyUserDetailsService;
import pl.application.passwordwallet.service.UserAuthenticationService;
import pl.application.passwordwallet.service.UserService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static pl.application.passwordwallet.service.MyUserDetailsService.constants;

@Service
public class LoginAttemptService {
    private boolean isAccountBlocked;
    private boolean isLongerCache = false;
    private final int MAX_ATTEMPT = 2;
    private LoadingCache<String, Integer> attemptsCache;
    private LoadingCache<String, Boolean> cache5s = createCache(5, TimeUnit.SECONDS);
    private LoadingCache<String, Boolean> cache10s = createCache(10, TimeUnit.SECONDS);

    @Autowired
    private UserAuthenticationService userAuthenticationService;
    @Autowired
    private UserService userService;

    public LoadingCache<String, Boolean> createCache(int duration, TimeUnit timeUnit) {
        System.out.println("createCache = executed");
        return CacheBuilder.newBuilder().
                expireAfterWrite(duration, timeUnit).build(new CacheLoader<String, Boolean>() {
                    public Boolean load(String key) {
                        return true;
                    }
                });
    }

    public void loginFailed(String key) {
        User user = userService.findUserByEmail(constants.getEmail());
        if(!isLongerCache && user.getActive())
            cache5s.put(key, true);
        else if(isLongerCache && user.getActive())
            cache10s.put(key,true);
        else
            System.out.println("ACCOUNT BLOCKED");
    }

    public void loginSucceeded(String key) {
        attemptsCache.invalidate(key);
    }

    public boolean isBlocked(String key) {
        return (cache5s.getIfPresent(key) != null) || (cache10s.getIfPresent(key) != null);
    }

    public void setLongerCache(boolean longerCache) {
        isLongerCache = longerCache;
    }

    public LoadingCache<String, Boolean> getCache5s() {
        return cache5s;
    }

    public LoadingCache<String, Boolean> getCache10s() {
        return cache10s;
    }

    public boolean isLongerCache() {
        return isLongerCache;
    }
}
