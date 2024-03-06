package dev.sigit.springbootjwt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.sigit.springbootjwt.dto.JwtAuthenticationResponse;
import dev.sigit.springbootjwt.dto.SigninRequest;
import dev.sigit.springbootjwt.services.JWTService;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String JWT_MALFORMED = "eyJhI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDk1MjM2OTAsImV4cCI6MTcwOTUyNTEzMH0.gHaPTXe35OUBs21CdtDTifc6BN3-whtstCumITvHgLU";

    private static final String JWT_EXPIRED = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbkBnbWFpbC5jb20iLCJpYXQiOjE3MDk2OTY3MzgsImV4cCI6MTcwOTY5ODE3OH0.MRPxlEvTlNWxTkKxlHrgPiJuzuja58ZEWHShovDsjZE";

    private static final StringBuilder JWT_ADMIN = new StringBuilder();


    @Test
    @Order(1)
    void testLoginAdminShouldReturnOk() throws Exception {
        SigninRequest signinRequest = new SigninRequest();
        signinRequest.setEmail("admin@gmail.com");
        signinRequest.setPassword("admin");

        String requestBody = objectMapper.writeValueAsString(signinRequest);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/signin")
                .contentType("application/json")
                .content(requestBody))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        String respBody = result.getResponse().getContentAsString();
        JwtAuthenticationResponse jwtAuthenticationResponse = objectMapper.readValue(respBody, JwtAuthenticationResponse.class);
        JWT_ADMIN.append(jwtAuthenticationResponse.getToken());
    }

    @Test
    @Order(2)
    void testJwtAdminShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin")
                .contentType("application/json")
                .header("Authorization", "Bearer " + JWT_ADMIN.toString()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(3)
    void testJwtAdminShouldReturnForbidden() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + JWT_ADMIN.toString()))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @Order(4)
    void testJwtMalformed(){
        Exception exception = assertThrows(MalformedJwtException.class, () -> {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user")
                            .contentType("application/json")
                            .header("Authorization", "Bearer " + JWT_MALFORMED))
                    .andExpect(status().isForbidden())
                    .andDo(print());
        });

        System.out.println(exception.getMessage());

        String expectedMessage = "Malformed JWT";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
