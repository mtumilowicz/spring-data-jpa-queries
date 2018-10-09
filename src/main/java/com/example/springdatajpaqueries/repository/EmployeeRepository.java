package com.example.springdatajpaqueries.repository;

import com.example.springdatajpaqueries.entity.Employee;
import com.example.springdatajpaqueries.entity.EmployeeNameCityProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * Created by mtumilowicz on 2018-10-03.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e")
    Stream<Employee> findAllStream();

    @Async
    CompletableFuture<Employee> findByName(String name);

    List<Employee> employeesByIssueDescription(String description);

    @Query(value = "SELECT * FROM EMPLOYEE WHERE CITY = ?1", nativeQuery = true)
    List<Employee> findByCity(String city);

    @Query("select e from #{#entityName} e where e.name = :name1 or e.name = :name2")
    List<Employee> findByNames(@Param("name1") String name1,
                               @Param("name2") String name2);
    @Modifying
    @Query("update Employee e set e.name = :name where e.address.city = :city")
    int updateNameForEmployeesFromCity(@Param("name") String name,
                                       @Param("city") String city);

    void deleteByAddress_Street(String street);

    @Modifying
    @Query("delete from Employee e where e.address.street = ?1")
    void deleteInBulkByStreet(String street);
    
    @Query("select e from Employee e")
    List<EmployeeNameCityProjection> findAll_NameCityProjection();
}
