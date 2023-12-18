package com.samplecompany.repository;

import com.samplecompany.entities.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComputerRepository extends JpaRepository<Computer,Long> {

    //retrieves information about the computer by using employee abbreviation
    List<Computer> findByEmployeeAlias(String employeeAlias);

    // retrieves the count of employee when the 3 or more computers are issued
    long countByEmployeeAlias(String employeeAlias);

}
