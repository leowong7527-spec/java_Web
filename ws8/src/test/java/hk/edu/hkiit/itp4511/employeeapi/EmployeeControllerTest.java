package hk.edu.hkiit.itp4511.employeeapi;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import hk.edu.hkiit.itp4511.employeeapi.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crudWorkflowShouldWork() throws Exception {
        Employee request = new Employee(null, "Alice", "Smith", "alice.smith@example.com");

        String createdJson = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Employee createdEmployee = objectMapper.readValue(createdJson, Employee.class);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(org.hamcrest.Matchers.greaterThanOrEqualTo(1))));

        mockMvc.perform(get("/api/employees/{id}", createdEmployee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice.smith@example.com"));

        Employee updateRequest = new Employee(null, "Alice", "Johnson", "alice.johnson@example.com");
        mockMvc.perform(put("/api/employees/{id}", createdEmployee.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Johnson"));

        mockMvc.perform(delete("/api/employees/{id}", createdEmployee.getId()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }
}
