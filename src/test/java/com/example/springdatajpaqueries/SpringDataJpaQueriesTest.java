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
import static org.hamcrest.Matchers.empty;
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
    
    @Test
    public void SpEL_namedParams() {
        List<Employee> byNames = repository.findByNames("a", "b");
        
        assertThat(byNames, is(empty()));
    }
    
    @Test
    @Transactional
    public void update() {
        int i = repository.updateNameForEmployeesFromCity("Tumilowicz updated", "Warsaw");
        
        assertThat(i, is(1));
        assertThat(repository.findById(1).get().getName(), is("Tumilowicz updated"));
    }
    
    @Test
    @Transactional
    public void delete() {
        repository.deleteByAddress_Street("Plac Zbawiciela");
        
        assertThat(repository.findAll(), hasSize(3));
    }

    @Test
    @Transactional
    public void deleteBulk() {
        repository.deleteInBulkByStreet("907 Whitehead St");
        
        System.out.println(repository.findAll());
        
        assertThat(repository.findAll(), hasSize(3));
    }
}
