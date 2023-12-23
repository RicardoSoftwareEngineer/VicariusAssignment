package io.vicarius.assignment.user;

import org.springframework.data.repository.CrudRepository;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, String> {
    Optional<UserEntity> findById(String id);
    List<UserEntity> findAll();
    Optional<UserEntity> findByFirstNameAndLastName(String firstName, String lastName);
    void deleteById(String id);
}
