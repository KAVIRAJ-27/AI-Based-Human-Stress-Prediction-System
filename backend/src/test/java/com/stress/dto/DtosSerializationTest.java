package com.stress.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
class DtosSerializationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeLoginRequestFromJson() throws Exception {
        Dtos.LoginRequest request = objectMapper.readValue(
                "{\"username\":\"admin\",\"password\":\"admin123\"}",
                Dtos.LoginRequest.class
        );

        assertEquals("admin", request.getUsername());
        assertEquals("admin123", request.getPassword());
    }
}
