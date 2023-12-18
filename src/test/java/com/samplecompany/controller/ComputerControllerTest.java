package com.samplecompany.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samplecompany.entities.Computer;
import com.samplecompany.service.ComputerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ComputerController.class)
public class ComputerControllerTest {

    @MockBean
    private ComputerService computerService;

    @InjectMocks
    private ComputerController computerController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(computerController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenPostComputer_thenCreateComputer() throws Exception {
        Computer computer = new Computer(); // Populate with test data
        given(computerService.addComputerToEmployee(computer)).willReturn(computer);

        mockMvc.perform(post("/api/v1/computer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(computer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.computerName").value(computer.getComputerName()));
    }

    @Test
    void whenGetComputers_thenReturnComputers() throws Exception {
        given(computerService.readAll()).willReturn(Collections.singletonList(new Computer())); // Populate with test data

        mockMvc.perform(get("/api/v1/computer"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void whenUpdateComputer_thenComputerShouldBeUpdated() throws Exception {
        Long computerId = 1L;
        Computer updatedComputer = new Computer();
        given(computerService.updateComputer(eq(computerId), any(Computer.class))).willReturn(updatedComputer);

        mockMvc.perform(put("/api/v1/computer/" + computerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedComputer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.computerName").value(updatedComputer.getComputerName()));
    }

    @Test
    void whenDeleteComputer_thenComputerShouldBeRemoved() throws Exception {
        Long computerId = 1L;
        doNothing().when(computerService).unAssignComputer(computerId);

        mockMvc.perform(delete("/api/v1/computer/" + computerId))
                .andExpect(status().isNoContent());

        verify(computerService).unAssignComputer(computerId);
    }

}
