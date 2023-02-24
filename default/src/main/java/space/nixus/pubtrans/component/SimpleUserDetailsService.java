package space.nixus.pubtrans.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import space.nixus.pubtrans.service.UserService;

@Component
public class SimpleUserDetailsService implements UserDetailsService{

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userService.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("username");
        }
        return user;
    }
}
