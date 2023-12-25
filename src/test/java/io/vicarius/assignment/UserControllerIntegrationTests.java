package io.vicarius.assignment;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vicarius.assignment.user.UserDTO;
import io.vicarius.assignment.user.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class UserControllerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private UserServiceImpl userService;

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void testCreateAndDeleteUser() throws Exception {
		UserDTO userDTO = new UserDTO("Ricardo", "Ribeiro");
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(userDTO)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isString())
				.andExpect(jsonPath("$.firstName").value("Ricardo"))
				.andExpect(jsonPath("$.lastName").value("Ribeiro"));
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
	void testGetUserById() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		user = userService.create(user);

		mockMvc.perform(get("/user/v1/{id}", user.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.firstName").value("Ricardo"))
				.andExpect(jsonPath("$.lastName").value("Ribeiro"));
	}

	@Test
	void testGetUsers() throws Exception {
		UserDTO user1 = new UserDTO("Ricardo", "Ribeiro");
		userService.create(user1);

		UserDTO user2 = new UserDTO("Ricardo", "Rib");
		userService.create(user2);

		mockMvc.perform(get("/user/v1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstName").value("Ricardo"))
				.andExpect(jsonPath("$[0].lastName").value("Ribeiro"))
				.andExpect(jsonPath("$[1].firstName").value("Ricardo"))
				.andExpect(jsonPath("$[1].lastName").value("Rib"));
	}

	@Test
	void testUpdateUser() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		userService.create(user);
		user.setFirstName("Ric");
		user.setLastName("Rib");

		mockMvc.perform(put("/user/v1/{id}", user.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(user.getId()))
				.andExpect(jsonPath("$.firstName").value("Ric"))
				.andExpect(jsonPath("$.lastName").value("Rib"));
	}

	@Test
	void testDeleteUser() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		userService.create(user);

		mockMvc.perform(delete("/user/v1/{id}", user.getId()))
				.andExpect(status().isOk());

		mockMvc.perform(get("/user/v1/{id}", user.getId()))
				.andExpect(status().isNotFound());
	}
}
