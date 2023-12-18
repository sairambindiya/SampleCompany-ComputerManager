package com.samplecompany.repository;

import com.samplecompany.entities.Computer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ComputerRepositoryTest {

    @Autowired
    private ComputerRepository computerRepository;

    private Computer computer;

    @BeforeEach
    void setUp() {
        // setup data for each test
        computer = new Computer();
        computerRepository.save(computer);
    }

    @Test
    void whenFindByEmployeeAlias_thenReturnComputers() {
        // Assuming computer employeeAlias set
        List<Computer> foundComputers = computerRepository.findByEmployeeAlias(computer.getEmployeeAlias());

        assertFalse(foundComputers.isEmpty());
        assertEquals(computer.getEmployeeAlias(), foundComputers.get(0).getEmployeeAlias());
    }

    @Test
    void whenCountByEmployeeAlias_thenReturnCount() {
        long count = computerRepository.countByEmployeeAlias(computer.getEmployeeAlias());

        assertEquals(1, count);
    }

}
