package io.vicarius.assignment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping(value = "/user/v1")
    public Object create(@RequestBody UserDTO userDTO){
        return userService.create(userDTO);
    }

    @GetMapping(value = "/user/v1")
    public Object retrieve(){
        return userService.retrieve();
    }

    @GetMapping(value = "/user/v1/{id}")
    public Object retrieveById(@PathVariable String id){
        return userService.retrieveById(id);
    }

    @PutMapping(value = "/user/v1/{id}")
    public Object update(@PathVariable String id, @RequestBody UserDTO userDTO){
        return userService.update(userDTO, id);
    }

    @DeleteMapping(value = "/user/v1/{id}")
    public void delete(@PathVariable String id){
        userService.delete(id);
    }
}
