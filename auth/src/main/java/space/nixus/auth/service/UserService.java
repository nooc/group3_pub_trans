package space.nixus.auth.service;

import space.nixus.auth.error.UserExistsError;
import space.nixus.auth.error.UserNotFoundError;
import space.nixus.auth.model.User;
import space.nixus.auth.model.UserParams;
import space.nixus.auth.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getUsers() {
        var users = new ArrayList<User>();
        userRepository.findAll(Sort.unsorted()).forEach(users::add);
        return users;
    }

    public List<String> getRoles() {
        var roles = new ArrayList<String>();
        //userRepository.findAll(Sort.unsorted()).forEach(users::add);
        return roles;
    }

    public boolean exists(long id) {
        return userRepository.existsById(id);
    }

    public User create(UserParams newUser) {
        if(! userRepository.existsByEmail(newUser.getEmail())) {
            var user = new User();
            user.update(newUser);
            return userRepository.save(user);
        }
        throw new UserExistsError(newUser.getEmail());
    }

    public void delete(long id) {
        userRepository.deleteById(id);
    }

    public User update(long id, UserParams params) {
        var userOpt = userRepository.findById(id);
        if(userOpt.isPresent()) {
            return update(userOpt.get().update(params));
        }
        throw new UserNotFoundError(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByEmail(username);
    }

    public User findById(long id) {
        return userRepository.findById(id).get();
    }
}
