package ma.xproce.stocksmicroservice;


import ma.xproce.stocksmicroservice.Controllers.StockController;
import ma.xproce.stocksmicroservice.Services.StockManager;
import ma.xproce.stocksmicroservice.Services.TokenValidator;
import ma.xproce.stocksmicroservice.dao.entities.Stock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StockManager stockManager;

    @Mock
    private TokenValidator tokenValidator;

    @InjectMocks
    private StockController stockController;

    @BeforeEach
    public void setup() {
        // Setup standalone MockMvc with controller
        mockMvc = MockMvcBuilders.standaloneSetup(stockController)
                .build();

        // Default token validation setup
        Mockito.when(tokenValidator.validateToken(anyString())).thenReturn(true);
    }

    @Test
    public void testGetAllStocks() throws Exception {
        Mockito.when(stockManager.getAllStocks()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/stocks")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    public void testUnauthorizedAccess() throws Exception {
        Mockito.when(tokenValidator.validateToken(anyString())).thenReturn(false);

        mockMvc.perform(get("/stocks")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid or missing token."));
    }
    @Test
    public void testAuthorizedAccess() throws Exception {
        mockMvc.perform(get("/stocks")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(status().isOk());

        Mockito.verify(tokenValidator).validateToken("Bearer valid_token");
    }
}
