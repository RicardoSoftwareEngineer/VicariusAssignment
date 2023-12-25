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
	void testCreateUser() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isString())
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()));
	}

	@Test
	void testRetrieveUsers() throws Exception {
		UserDTO user1 = new UserDTO("Ricardo", "Ribeiro");
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user1)))
				.andExpect(status().isOk());

		UserDTO user2 = new UserDTO("Ricardo", "Rib");
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user2)))
				.andExpect(status().isOk());

		mockMvc.perform(get("/user/v1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].firstName").value("Ricardo"))
				.andExpect(jsonPath("$[0].lastName").value("Ribeiro"))
				.andExpect(jsonPath("$[1].firstName").value("Ricardo"))
				.andExpect(jsonPath("$[1].lastName").value("Rib"));
	}

	@Test
	void testRetrieveUserById() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		String contentAsString = mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isString())
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()))
				.andReturn().getResponse().getContentAsString();

		String userId = objectMapper.readTree(contentAsString).get("id").asText();

		mockMvc.perform(get("/user/v1/{id}", userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()));
	}

	@Test
	void testUpdateUser() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		String contentAsString = mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isString())
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()))
				.andReturn().getResponse().getContentAsString();

		String userId = objectMapper.readTree(contentAsString).get("id").asText();
		user.setFirstName("Ric");
		user.setLastName("Rib");

		mockMvc.perform(put("/user/v1/{id}", userId)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()));

		mockMvc.perform(get("/user/v1/{id}", userId))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(userId))
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()));
	}

	@Test
	void testDeleteUser() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		String contentAsString = mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isString())
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()))
				.andReturn().getResponse().getContentAsString();

		String userId = objectMapper.readTree(contentAsString).get("id").asText();

		mockMvc.perform(delete("/user/v1/{id}", userId))
				.andExpect(status().isOk());

		mockMvc.perform(get("/user/v1/{id}", userId))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUserIdempotency() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isString())
				.andExpect(jsonPath("$.firstName").value(user.getFirstName()))
				.andExpect(jsonPath("$.lastName").value(user.getLastName()));

		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isForbidden());
	}

	@Test
	void testUserMissingField() throws Exception {
		UserDTO user = new UserDTO("Ricardo", "Ribeiro");
		user.setFirstName(null);
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isBadRequest());

		user = new UserDTO("Ricardo", "Ribeiro");
		user.setLastName(null);
		mockMvc.perform(post("/user/v1")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(user)))
				.andExpect(status().isBadRequest());
	}
}
