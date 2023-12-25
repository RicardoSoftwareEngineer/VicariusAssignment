package io.vicarius.assignment.user;

import org.springframework.web.server.ResponseStatusException;
import java.util.List;

public interface UserService {
    UserDTO create(UserDTO userDTO) throws ResponseStatusException;
    List<UserDTO> retrieveAll() throws ResponseStatusException;
    UserDTO retrieveById(String id) throws ResponseStatusException;
    UserDTO update(UserDTO userDTO, String id) throws ResponseStatusException;
    void delete(String id) throws ResponseStatusException;
}
