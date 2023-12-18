package com.samplecompany.service;

import com.samplecompany.entities.Computer;
import com.samplecompany.exception.ComputerNotFoundException;
import com.samplecompany.repository.ComputerRepository;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Service
public class ComputerService {

    private final ComputerRepository computerRepository;

    private final RestTemplate template;

    public ComputerService(ComputerRepository computerRepository, RestTemplate template) {
        this.computerRepository = computerRepository;
        this.template = template;
    }

    public Computer addComputerToEmployee(Computer computer) {
        Computer addedComputer = computerRepository.save(computer);
        checkAndNotifyForMultipleAssignment(computer.getEmployeeAlias());
        return addedComputer;
    }

    public Computer readComputerById(Long id) {
        return computerRepository.findById(id).orElseThrow(() -> new ComputerNotFoundException("Computer id - "+id+" not found "));
    }

    public Computer updateComputer(Long id, Computer computerDetails) {
        Computer existingComputer = computerRepository.findById(id).orElseThrow(() -> new ComputerNotFoundException("Computer id - "+id+" not found "));

        //updating computer details
        if (computerDetails.getComputerName() != null) {
            existingComputer.setComputerName(computerDetails.getComputerName());
        }
        if (computerDetails.getDescription() != null) {
            existingComputer.setDescription(computerDetails.getDescription());
        }
        if (computerDetails.getIpAddress() != null) {
            existingComputer.setIpAddress(computerDetails.getIpAddress());
        }
        if (computerDetails.getMacAddress() != null) {
            existingComputer.setMacAddress(computerDetails.getMacAddress());
        }
        if (computerDetails.getEmployeeAlias() != null) {
            existingComputer.setEmployeeAlias(computerDetails.getEmployeeAlias());
        }
        return computerRepository.save(existingComputer);

    }

    public List<Computer> readAll() {
        return computerRepository.findAll();
    }

    public void unAssignComputer(Long id) {
        Computer computer = readComputerById(id);
        computer.setEmployeeAlias(null);
        computerRepository.save(computer);
    }

    public List<Computer> getComputersByEmployee(String employeeAlias) {
        return computerRepository.findByEmployeeAlias(employeeAlias);
    }

    public void reAssignComputerToEmployee(Long computerId, String newEmployeeAlias) {
        Computer computer = readComputerById(computerId);
        computer.setEmployeeAlias(newEmployeeAlias);
        computerRepository.save(computer);
        checkAndNotifyForMultipleAssignment(newEmployeeAlias);
    }

    private void checkAndNotifyForMultipleAssignment(String employeeAlias) {
        long count = computerRepository.countByEmployeeAlias(employeeAlias);
        if (count >= 3) {
            sendNotification(employeeAlias);
        }
    }

    void sendNotification(String employeeAlias) {
        String notificationUrl = "http://localhost:8080/api/notify";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Create the request body
        String jsonBody = String.format("{\"level\":\"warning\",\"employeeAbbreviation\":\"%s\",\"message\":\"Employee %s is assigned 3 or more computers\"}", employeeAlias, employeeAlias);
        HttpEntity<String> request = new HttpEntity<>(jsonBody, headers);

        // Sending the POST request
        try {
            String response = template.postForObject(notificationUrl, request, String.class);
            System.out.println("Notification sent successfully: " + response);
        } catch (Exception e) {
            System.out.println("Error sending notification: " + e.getMessage());
        }
    }
}