package com.example.springdatajpaqueries;

import com.example.springdatajpaqueries.entity.Employee;
import com.example.springdatajpaqueries.repository.EmployeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * Created by mtumilowicz on 2018-10-08.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class SpringDataJpaQueriesTest {

    @Autowired
    EmployeeRepository repository;
    
    @Test
    @Transactional
    public void stream() {
        try (Stream<Employee> all = repository.findAllStream()) {
            assertThat(all.count(), is(4L));
        }
    }
    
    @Test
    public void async() {
        CompletableFuture<Employee> employeeFuture = repository.findByName("Hemingway");
        
        assertThat(employeeFuture.join().getName(), is("Hemingway"));
    }
    
    @Test
    public void namedQuery() {
        List<Employee> employees = repository.employeesByIssueDescription("stay sharp!");
        
        assertThat(employees, hasSize(1));
        assertThat(employees.get(0).getName(), is("Tumilowicz"));
    }
    
    @Test
    public void nativeQuery() {
        List<Employee> employeesFromKrakow = repository.findByCity("Krakow");

        assertThat(employeesFromKrakow, hasSize(2));
        assertThat(employeesFromKrakow.get(0).getName(), is("Mrozek"));
        assertThat(employeesFromKrakow.get(1).getName(), is("Lem"));
    }
}
