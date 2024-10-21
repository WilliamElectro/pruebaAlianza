package com.wbohorquez.pruebaAlianza.testController;

import com.wbohorquez.pruebaAlianza.model.Client;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author WI-LL
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    public void setUp() throws Exception {
        
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/clients"))
            .andExpect(status().isOk())
            .andExpect(content().string("All clients have been deleted."));
        
        // Crear algunos clientes para las pruebas
        String clientJson1 = "{\"sharedKey\": \"johndoe\", \"name\": \"John Doe\", \"email\": \"johndoe@example.com\", \"phone\": \"123456789\", \"dateAdded\": \"" + dateFormat.format(new Date()) + "\"}";
        String clientJson2 = "{\"sharedKey\": \"janedoe\", \"name\": \"Jane Doe\", \"email\": \"janedoe@example.com\", \"phone\": \"987654321\", \"dateAdded\": \"2023-01-15\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson1))
                .andExpect(status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson2))
                .andExpect(status().isCreated());
    }

    // Prueba para crear un nuevo cliente
    @Test
    public void testCreateClient() throws Exception {
        String clientJson = createClientJson("lpalomino", "Laura Palomino", "lpalomino@example.com", "23232323");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laura Palomino"))
                .andExpect(jsonPath("$.email").value("lpalomino@example.com"));
    }

    @Test
    public void createClient_Conflict() throws Exception {
        String clientJson = createClientJson("duplicateKey123", "Luis Martinez", "lmartinez@example.com", "767676");

        // Crear el primer cliente con el mismo sharedKey
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isCreated());

        // Intentar crear el mismo cliente de nuevo
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isBadRequest());
    }

    // Prueba para listar todos los clientes
    @Test
    public void testGetAllClients() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    // Prueba para buscar cliente por Shared Key
    @Test
    public void testGetClientBySharedKey() throws Exception {
        createClient("wbohorquez", "William Bohorquez", "wbohorquez@example.com", "12121212");

        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/search")
                .param("sharedKey", "wbohorquez")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("William Bohorquez"))
                .andExpect(jsonPath("$.email").value("wbohorquez@example.com"));
    }

    // Prueba para buscar por nombre
    @Test
    public void testSearchByName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/searchByFilters")
                .param("name", "John Doe")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"));
    }

    // Prueba para buscar por teléfono
    @Test
    public void testSearchByPhone() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/searchByFilters")
                .param("phone", "987654321")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].phone").value("987654321"))
                .andExpect(jsonPath("$[0].name").value("Jane Doe"));
    }

    // Prueba para buscar por email
    @Test
    public void testSearchByEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/searchByFilters")
                .param("email", "johndoe@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"))
                .andExpect(jsonPath("$[0].name").value("John Doe"));
    }

    // Prueba para buscar por rango de fechas
    @Test
    public void testSearchByDateRange() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/searchByFilters")
                .param("startDate", "2023-01-01")
                .param("endDate", "2023-12-31")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Jane Doe"))
                .andExpect(jsonPath("$[0].dateAdded").value("2023-01-15"));
    }

    // Prueba para búsqueda con múltiples criterios (nombre y email)
    @Test
    public void testSearchByMultipleCriteria() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/clients/searchByFilters")
                .param("name", "John Doe")
                .param("email", "johndoe@example.com")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].email").value("johndoe@example.com"));
    }

    private String createClientJson(String sharedKey, String name, String email, String phone) {
        return String.format("{\"sharedKey\": \"%s\", \"name\": \"%s\", \"email\": \"%s\", \"phone\": \"%s\"}",
                sharedKey, name, email, phone);
    }

    private void createClient(String sharedKey, String name, String email, String phone) throws Exception {
        String clientJson = createClientJson(sharedKey, name, email, phone);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clientJson))
                .andExpect(status().isCreated());
    }
}
