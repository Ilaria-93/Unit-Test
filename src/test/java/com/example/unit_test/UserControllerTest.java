package com.example.unit_test;

import com.example.unit_test.controllers.UserController;
import com.example.unit_test.entities.User;
import com.example.unit_test.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    void createUser() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setEmail("ila.8@gmail.com");
        user.setFirstName("Ilaria");
        user.setLastName("Faleschini");

        String userJASON = objectMapper.writeValueAsString(user);

        MvcResult resultActions = this.mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJASON)).andDo(print())
                .andExpect(status().isOk()).andReturn();
    }

    @Test
    @Order(2)
    void getAllUsers() throws Exception {
        MvcResult result = this.mockMvc.perform(get("/api/users"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        List<User> users = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        assertThat(users.size()).isNotZero();
    }

    @Test
    @Order(3)
    void getUserById() throws Exception {
        Long userId = 1L;
        MvcResult resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        String responseContent = resultActions.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);
        Assertions.assertNotNull(user);
    }


    @Test
    @Order(4)
    void updateUser() throws Exception {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setEmail("updated.email@gmail.com");
        updatedUser.setFirstName("UpdatedName");
        updatedUser.setLastName("UpdatedLastName");

        String userJSON = objectMapper.writeValueAsString(updatedUser);

        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJSON)).andDo(print())
                .andExpect(status().isOk()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        User user = objectMapper.readValue(responseContent, User.class);
        Assertions.assertEquals("updated.email@gmail.com", user.getEmail());
        Assertions.assertEquals("UpdatedName", user.getFirstName());
    }


    @Test
    @Order(5)
    void deleteUser() throws Exception {
        Long userId = 1L;
        mockMvc.perform(delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<User> deletedUser = userRepository.findById(Math.toIntExact(userId));
        Assertions.assertTrue(deletedUser.isEmpty());
    }

    @Test
    @Order(6)
    void contextLoads() {
        assertThat(userController).isNotNull();
    }
}
