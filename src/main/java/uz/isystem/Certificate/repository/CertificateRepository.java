package uz.isystem.Certificate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.isystem.Certificate.model.Certificate;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Integer> {

    Optional<Certificate> findByTokenAndDeletedAtIsNull(String token);
}
