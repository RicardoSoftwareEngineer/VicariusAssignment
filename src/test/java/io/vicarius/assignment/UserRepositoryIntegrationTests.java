package io.vicarius.assignment;

import io.vicarius.assignment.user.UserEntity;
import io.vicarius.assignment.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserRepositoryIntegrationTests {
    @Autowired
    private UserRepository userRepository;

    @Test
    void testCreateUser(){
        UserEntity user = new UserEntity("Ricardo", "Ribeiro");
        assertNull(user.getId());
        userRepository.save(user);
        assertNotNull(user.getId());
    }

    @Test
    void testRetrieveUsers() {
        UserEntity user1 = new UserEntity("Ricardo", "Ribeiro");
        UserEntity user2 = new UserEntity("Ricardo", "Rib");
        userRepository.save(user1);
        userRepository.save(user2);
        List<UserEntity> users = userRepository.findAll();

        assertEquals(2, users.size());
        assertEquals(user1.getFirstName(), users.get(0).getFirstName());
        assertEquals(user1.getLastName(), users.get(0).getLastName());
        assertEquals(user2.getFirstName(), users.get(1).getFirstName());
        assertEquals(user2.getLastName(), users.get(1).getLastName());
    }

    @Test
    void testRetrieveUserById(){
        UserEntity user = new UserEntity("Ricardo", "Ribeiro");
        assertNull(user.getId());
        userRepository.save(user);
        assertNotNull(user.getId());
        Optional<UserEntity> userFromDatabase = userRepository.findById(user.getId());
        assertTrue(userFromDatabase.isPresent());
    }

    @Test
    void testUpdateUser() {
        UserEntity user = new UserEntity("Ricardo", "Ribeiro");
        assertNull(user.getId());
        userRepository.save(user);
        assertNotNull(user.getId());

        Optional<UserEntity> userFromDatabase = userRepository.findById(user.getId());
        assertTrue(userFromDatabase.isPresent());
        userFromDatabase.get().getFirstName().equals(user.getFirstName());
        userFromDatabase.get().getLastName().equals(user.getLastName());
        user.setFirstName("Ric");
        user.setLastName("Rib");
        userRepository.save(user);
        assertNotNull(user.getId());

        userFromDatabase = userRepository.findById(user.getId());
        assertTrue(userFromDatabase.isPresent());
        user = userFromDatabase.get();
        userFromDatabase.get().getFirstName().equals(user.getFirstName());
        userFromDatabase.get().getLastName().equals(user.getLastName());
        assertNotNull(user.getId());
    }

    @Test
    void testDeleteUser() throws Exception {
        UserEntity user = new UserEntity("Ricardo", "Ribeiro");
        assertNull(user.getId());
        userRepository.save(user);
        assertNotNull(user.getId());
        assertTrue(userRepository.findById(user.getId()).isPresent());
        userRepository.deleteById(user.getId());
        assertTrue(userRepository.findById(user.getId()).isEmpty());
    }
}
