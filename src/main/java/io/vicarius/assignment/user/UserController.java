package io.vicarius.assignment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/user/v1")
    public UserDTO create(@RequestBody UserDTO userDTO){
        return userService.create(userDTO);
    }

    @GetMapping(value = "/user/v1")
    public List<UserDTO> retrieve(){
        return userService.retrieve();
    }

    @GetMapping(value = "/user/v1/{id}")
    public UserDTO retrieveById(@PathVariable String id){
        return userService.retrieveById(id);
    }

    @PutMapping(value = "/user/v1/{id}")
    public UserDTO update(@PathVariable String id, @RequestBody UserDTO userDTO){
        return userService.update(userDTO, id);
    }

    @DeleteMapping(value = "/user/v1/{id}")
    public void delete(@PathVariable String id){
        userService.delete(id);
    }
}
