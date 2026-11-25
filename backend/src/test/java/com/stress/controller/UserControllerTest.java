package com.stress.controller;

import com.stress.security.JwtUtil;
import com.stress.service.CustomUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private String adminToken;

    @BeforeEach
    void setUp() {
        UserDetails adminDetails = userDetailsService.loadUserByUsername("admin");
        adminToken = jwtUtil.generateToken(adminDetails, "ADMIN");
    }

    @Test
    void getProfile_whenUnauthenticated_rejectsRequest() throws Exception {
        mockMvc.perform(get("/user/profile")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 403, "Expected 401 or 403, but got: " + status);
                });
    }

    @Test
    void updateProfile_whenUnauthenticated_rejectsRequest() throws Exception {
        mockMvc.perform(put("/user/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Hacker\",\"email\":\"hack@hack.com\"}"))
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 401 || status == 403, "Expected 401 or 403, but got: " + status);
                });
    }

    @Test
    void getProfile_whenAuthenticated_returnsProfile() throws Exception {
        mockMvc.perform(get("/user/profile")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email", notNullValue()));
    }

    @Test
    void updateProfile_whenValid_updatesProfileFields() throws Exception {
        mockMvc.perform(put("/user/profile")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"System Administrator\",\"age\":38,\"gender\":\"Other\",\"phone\":\"+1234567890\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.fullName").value("System Administrator"))
                .andExpect(jsonPath("$.age").value(38))
                .andExpect(jsonPath("$.gender").value("Other"))
                .andExpect(jsonPath("$.phone").value("+1234567890"));
    }

    @Test
    void updateProfile_whenInvalidEmail_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/user/profile")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"not-a-valid-email\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void updateProfile_whenInvalidAge_returnsBadRequest() throws Exception {
        mockMvc.perform(put("/user/profile")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"age\":150}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
