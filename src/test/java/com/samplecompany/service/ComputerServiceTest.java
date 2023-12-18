package com.samplecompany.service;

import com.samplecompany.entities.Computer;
import com.samplecompany.repository.ComputerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ComputerServiceTest {

    @Mock
    private ComputerRepository computerRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ComputerService computerService;

    private Computer computer;

    @BeforeEach
    void setUp() {
        computer = new Computer();
        computer.setEmployeeAlias("abc");
        computer.setComputerName("Mac m2");
        computer.setDescription("Mac m2 is a good laptop");
        computer.setMacAddress("00.0a.0b.0i");
        computer.setIpAddress("197.08.06");
    }

    @Test
    void whenAddComputerToEmployee_thenComputerShouldBeSaved() {
        when(computerRepository.save(any(Computer.class))).thenReturn(computer);
        when(computerRepository.countByEmployeeAlias(anyString())).thenReturn(1L);

        Computer addedComputer = computerService.addComputerToEmployee(computer);

        assertNotNull(addedComputer);
        verify(computerRepository).save(computer);
    }

    @Test
    void whenReadComputerById_thenComputerShouldBeReturned() {
        when(computerRepository.findById(anyLong())).thenReturn(Optional.of(computer));

        Computer foundComputer = computerService.readComputerById(Long.valueOf(1));

        assertNotNull(foundComputer);
        assertEquals(computer, foundComputer);
        verify(computerRepository).findById(1L);
    }

    @Test
    void whenUnassignComputer_thenEmployeeAliasShouldBeNull() {
        when(computerRepository.findById(anyLong())).thenReturn(Optional.of(computer));

        computerService.unAssignComputer(1L);

        assertNull(computer.getEmployeeAlias());
        verify(computerRepository).save(computer);
    }

    @Test
    void whenUpdateComputer_thenComputerShouldBeUpdated() {
        Computer updatedDetails = new Computer();
        updatedDetails.setComputerName("abc_updated");
        when(computerRepository.findById(anyLong())).thenReturn(Optional.of(computer));
        when(computerRepository.save(any(Computer.class))).thenReturn(computer);

        Computer updatedComputer = computerService.updateComputer(1L, updatedDetails);

        assertEquals("abc_updated", updatedComputer.getComputerName());
        verify(computerRepository).save(computer);
    }

    @Test
    void whenGetAllComputers_thenAllComputersShouldBeReturned() {
        when(computerRepository.findAll()).thenReturn(Collections.singletonList(computer));

        List<Computer> computers = computerService.readAll();

        assertFalse(computers.isEmpty());
        assertEquals(1, computers.size());
        verify(computerRepository).findAll();
    }

    @Test
    void whenGetComputersByEmployee_thenRelatedComputersShouldBeReturned() {
        when(computerRepository.findByEmployeeAlias(anyString())).thenReturn(Collections.singletonList(computer));

        List<Computer> computers = computerService.getComputersByEmployee("abc");

        assertFalse(computers.isEmpty());
        assertEquals(1, computers.size());
        verify(computerRepository).findByEmployeeAlias("abc");
    }

    @Test
    void whenReAssignComputerToEmployee_thenEmployeeAliasShouldBeUpdated() {
        Long computerId = 1L;
        String newEmployeeAlias = "NEW";
        when(computerRepository.findById(computerId)).thenReturn(Optional.of(computer));
        when(computerRepository.countByEmployeeAlias(newEmployeeAlias)).thenReturn(2L); // Adjust count for testing notification logic

        computerService.reAssignComputerToEmployee(computerId, newEmployeeAlias);

        assertEquals(newEmployeeAlias, computer.getEmployeeAlias());
        verify(computerRepository).save(computer);
        verify(computerRepository).countByEmployeeAlias(newEmployeeAlias);
        // You can also verify if sendNotification is called based on the count
    }

    @Test
    void whenSendNotification_thenHttpCallShouldBeMade() {
        String employeeAlias = "ABC";
        String notificationUrl = "http://localhost:8080/api/notify";
        String expectedJsonBody = String.format("{\"level\":\"warning\",\"employeeAbbreviation\":\"%s\",\"message\":\"Employee %s is assigned 3 or more computers\"}", employeeAlias, employeeAlias);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(expectedJsonBody, headers);

        when(restTemplate.postForObject(eq(notificationUrl), eq(requestEntity), eq(String.class))).thenReturn("Notification Sent");

        computerService.sendNotification(employeeAlias);

        verify(restTemplate).postForObject(eq(notificationUrl), eq(requestEntity), eq(String.class));
    }

}

