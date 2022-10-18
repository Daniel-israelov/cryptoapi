package com.example.cryptoapi.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.mockito.Mockito.verify;

@SpringBootTest
@ExtendWith({MockitoExtension.class})
class ExternalEndpointServiceTest {

    @Mock
    private ExternalEndpointService ees;


    @Test
    @DisplayName("Ping testing - should NOT throw exception")
    void shouldReturnStatusCode200() throws IOException {
        ees.ping();
        verify(ees).ping();
    }
}