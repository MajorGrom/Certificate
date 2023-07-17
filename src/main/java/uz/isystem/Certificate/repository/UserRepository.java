package uz.isystem.Certificate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.isystem.Certificate.model.User;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Integer> {
}
