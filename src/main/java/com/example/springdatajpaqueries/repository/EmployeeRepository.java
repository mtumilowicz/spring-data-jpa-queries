package com.example.springdatajpaqueries.repository;

import com.example.springdatajpaqueries.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Created by mtumilowicz on 2018-10-03.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e")
    Stream<Employee> findAllStream(); // remember to close the stream

//    @Async
//    CompletableFuture<Employee> findByName(String name);
//
//    List<Employee> employeesByIssueDescription(String description); // named query entity
//
//    @Query(value = "SELECT * FROM EMPLOYEE WHERE CITY = ?1", nativeQuery = true)
//    List<Employee> findByCity(String emailAddress);
//
//    @Query("select e from #{#entityName} e where e.name = :name1 or e.name = :name2") // SpEL 
//    List<Employee> findByNames(@Param("name1") String name1,
//                               @Param("name2") String name2);
//
//    @Modifying
////    Doing so triggers the query annotated to the method as an updating query instead of a selecting one. As the EntityManager might contain outdated entities after the execution of the modifying query, we do not automatically clear it (see the JavaDoc of EntityManager.clear() for details), since this effectively drops all non-flushed changes still pending in the EntityManager. If you wish the EntityManager to be cleared automatically, you can set the @Modifying annotation’s clearAutomatically attribute to true.
//    @Query("update Employee e set e.name = :name where e.address.city = :city")
//    int updateNameForEmployeesFromCity(@Param("name") String name,
//                                       @Param("city") String city);
//
//    void deleteByAddress_Street(String street);
//
//    @Modifying
//    @Query("delete from Employee e where e.address.street = ?1")
//    void deleteInBulkByStreet(String street);
//
//    /*
//     Although the deleteByRoleId(…) method looks like it basically produces the same result as the deleteInBulkByRoleId(…), there is an important difference between the two method declarations in terms of the way they get executed. As the name suggests, the latter method issues a single JPQL query (the one defined in the annotation) against the database. This means even currently loaded instances of User do not see lifecycle callbacks invoked.
//
//     To make sure lifecycle queries are actually invoked, an invocation of deleteByRoleId(…) executes a query and then deletes the returned instances one by one, so that the persistence provider can actually invoke @PreRemove callbacks on those entities.
//
//     In fact, a derived delete query is a shortcut for executing the query and then calling CrudRepository.delete(Iterable<User> users) on the result and keeping behavior in sync with the implementations of other delete(…) methods in CrudRepository.
//     */
//    
//    List<EmployeeNameCityProjection> findAllNames();
}
