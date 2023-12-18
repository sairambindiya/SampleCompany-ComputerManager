package com.samplecompany.controller;

import com.samplecompany.entities.Computer;
import com.samplecompany.service.ComputerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/computer")
public class ComputerController {

    private final ComputerService service;

    public ComputerController(ComputerService service) {
        this.service = service;
    }

    //create a computer with the details provided and assigns it to respective employee and returns '201' status code upon successful creation
    @PostMapping
    public ResponseEntity<Computer> createComputer(@RequestBody @Validated Computer computer){
        Computer createdComputer = service.addComputerToEmployee(computer);
        URI location= ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/id}").buildAndExpand(createdComputer.getComputerId()).toUri();
        return ResponseEntity.created(location).body(createdComputer);
    }

    //read computer by it's id and returns '200' status code for successful retrieval
    @GetMapping("/{id}")
    public ResponseEntity<Computer> readComputerById(@PathVariable Long id){
        return ResponseEntity.ok((Computer) service.readComputerById(id));
    }

    //update the existing computer by fetching the computer with it's id and updates the existing
    // computer with the values provided in the request body and returns '200' status code for successful
    // updation.
    @PutMapping("/{id}")
    public ResponseEntity<Computer> updateComputer(@PathVariable Long id,@RequestBody @Validated Computer computer){
        return ResponseEntity.ok((Computer) service.updateComputer(id,computer));
    }



    //removing the computer from the assigned employee and returns '204 No Content' upon removing
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComputer(@PathVariable Long id){
        service.unAssignComputer(id);
        return ResponseEntity.noContent().build();
    }

    //read all information about the existing computers in the database.
    @GetMapping
    public List<Computer> readAll(){
        return service.readAll();

    }
}
