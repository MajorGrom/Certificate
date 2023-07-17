package uz.isystem.Certificate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.isystem.Certificate.exception.BadRequest;
import uz.isystem.Certificate.model.User;
import uz.isystem.Certificate.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    public boolean create(User user) {
        userRepository.save(user);
        return true;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id){
        Optional<User> optional = userRepository.findById(id);
        if(optional.isEmpty()){
            throw new BadRequest("User not found");
        }
        return optional.get();
    }
}
