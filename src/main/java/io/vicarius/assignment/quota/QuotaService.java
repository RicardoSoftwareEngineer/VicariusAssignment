package io.vicarius.assignment.quota;

import io.vicarius.assignment.user.UserDTO;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface QuotaService {

    String consume(String id) throws ResponseStatusException;

    List<UserDTO> getUsersQuota() throws ResponseStatusException;
}
