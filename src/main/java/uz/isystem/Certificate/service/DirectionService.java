package uz.isystem.Certificate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import uz.isystem.Certificate.exception.BadRequest;
import uz.isystem.Certificate.model.Direction;
import uz.isystem.Certificate.repository.DirectionRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DirectionService {
    private DirectionRepository directionRepository;

    public boolean create(Direction direction) {
        directionRepository.save(direction);
        return true;
    }

    public List<Direction> getAll() {
        return directionRepository.findAll();
    }

    public Direction getById(Integer id){
        Optional<Direction> optional = directionRepository.findById(id);
        if(optional.isEmpty()){
            throw new BadRequest("Direction not found");
        }
        return optional.get();
    }
}
