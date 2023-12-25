package io.vicarius.assignment;

import io.vicarius.assignment.user.UserDTO;
import io.vicarius.assignment.user.UserServiceImpl;
import io.vicarius.assignment.util.Util;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserServiceIntegrationTests {
    @Autowired
    private UserServiceImpl userService;
    private Util util = new Util();

    @Test
    void testCreateUser(){
        UserDTO user = new UserDTO("Ricardo", "Ribeiro");
        if(util.isDayTime()){
            assertNull(user.getId());
            user = userService.create(user);
            assertNotNull(user.getId());
        }
        if(!util.isDayTime()){
            UserDTO finalUser = user;
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.create(finalUser);
            });
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        }
    }

    @Test
    void testRetrieveUsers() {
        UserDTO user1 = new UserDTO("Ricardo", "Ribeiro");
        UserDTO user2 = new UserDTO("Ricardo", "Rib");
        if(util.isDayTime()){
            userService.create(user1);
            userService.create(user2);
            List<UserDTO> users = userService.retrieveAll();

            assertEquals(2, users.size());
            assertEquals(user1.getFirstName(), users.get(0).getFirstName());
            assertEquals(user1.getLastName(), users.get(0).getLastName());
            assertEquals(user2.getFirstName(), users.get(1).getFirstName());
            assertEquals(user2.getLastName(), users.get(1).getLastName());
        }
        if(!util.isDayTime()){
            UserDTO finalUser = user1;
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.create(finalUser);
            });
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            List<UserDTO> users = userService.retrieveAll();
            assertNotNull(users);
        }
    }

    @Test
    void testRetrieveUserById(){
        UserDTO user = new UserDTO("Ricardo", "Ribeiro");
        if(util.isDayTime()){
            user = userService.create(user);
            UserDTO userFromDB = userService.retrieveById(user.getId());
            assertNotNull(userFromDB);
            assertEquals(user.getId(), userFromDB.getId());
            assertEquals(user.getFirstName(), userFromDB.getFirstName());
            assertEquals(user.getLastName(), userFromDB.getLastName());
        }
        if(!util.isDayTime()){
            UserDTO finalUser = user;
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.create(finalUser);
            });
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
            exception = assertThrows(ResponseStatusException.class, () -> {
                userService.retrieveById("userId");
            });
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
    }

    @Test
    void testUpdateUser() throws Exception {
        UserDTO user = new UserDTO("Ricardo", "Ribeiro");
        if(util.isDayTime()){
            user = userService.create(user);
            user.setFirstName("Ric");
            user.setLastName("Rib");
            userService.update(user, user.getId());
            UserDTO userFromDB = userService.retrieveById(user.getId());
            assertEquals(userFromDB.getId(), user.getId());
            assertEquals(userFromDB.getFirstName(), user.getFirstName());
            assertEquals(userFromDB.getLastName(), user.getLastName());
        }
        if(!util.isDayTime()){
            UserDTO finalUser = user;
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.update(finalUser, "userId");
            });
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        }
    }

    @Test
    void testDeleteUser() throws Exception {
        UserDTO user = new UserDTO("Ricardo", "Ribeiro");
        if(util.isDayTime()){
            user = userService.create(user);
            user = userService.retrieveById(user.getId());
            assertNotNull(user);
            userService.delete(user.getId());
            UserDTO finalUser = user;
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.retrieveById(finalUser.getId());
            });
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        }
        if(!util.isDayTime()){
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.delete("userId");
            });
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        }
    }

    @Test
    void testUserIdempotency() throws Exception {
        UserDTO user = new UserDTO("Ricardo", "Ribeiro");
        if(util.isDayTime()){
            user = userService.create(user);
            user = userService.retrieveById(user.getId());
            assertNotNull(user);
            UserDTO finalUser = user;
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                userService.create(finalUser);
            });
            assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());
        }
    }

    @Test
    void testUserMissingField() throws Exception {
        if(util.isDayTime()){
            ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
                UserDTO user = new UserDTO("Ricardo", "Ribeiro");
                user.setFirstName(null);
                userService.create(user);
            });
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

            exception = assertThrows(ResponseStatusException.class, () -> {
                UserDTO user = new UserDTO("Ricardo", "Ribeiro");
                user.setLastName(null);
                userService.create(user);
            });
            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        }
    }
}
