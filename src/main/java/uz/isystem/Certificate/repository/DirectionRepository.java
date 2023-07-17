package uz.isystem.Certificate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.isystem.Certificate.model.Direction;

public interface DirectionRepository extends JpaRepository<Direction, Integer > {
}
