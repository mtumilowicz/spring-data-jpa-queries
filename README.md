# spring-data-jpa-queries
The main goal of this project is to show basics of constructing queries
using Spring Data and its repositories.

_Reference_: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

# preface
Please refer my recent project - Spring Data Basics: 
https://github.com/mtumilowicz/spring-data-jpa-basics

# overview
* `@Query` method + returning `Stream`
    ```
    @Query("select e from Employee e")
    Stream<Employee> findAllStream();    
    ```
    _Remark_: Stream (as a I/O operation) should be closed 
    (best way is to declare it using try-with-resources):
    ```
    @Test
    @Transactional
    public void stream() {
        try (final Stream<Employee> all = repository.findAllStream()) {
            assertThat(all.count(), is(4L));
        }
    }    
    ```
    
* `@Async` method + returning `CompletableFuture`
    ```
    @Async
    CompletableFuture<Employee> findByName(String name);    
    ```
    
* `@NamedQuery` declared over entity class
    ```
    @NamedQuery(name = "Employee.employeesByIssueDescription",
            query = "select e from Employee e join fetch e.issues i where i.description = ?1")
    ```
    ```
    List<Employee> employeesByIssueDescription(String description);
    ```
    
* native query
    ```
    @Query(value = "SELECT * FROM EMPLOYEE WHERE CITY = ?1", nativeQuery = true)
    List<Employee> findByCity(String city);    
    ```
    
* SpEL Expressions (upon query execution, these expressions are evaluated 
against a predefined set of variables). Spring Data JPA supports a variable 
called `entityName`. Its usage is select x from `#{#entityName}` x. It inserts 
the `entityName` of the domain type associated with the given repository. 
The `entityName` is resolved as follows: If the domain type has set the name 
property on the `@Entity` annotation, it is used. Otherwise, the simple 
class-name of the domain type is used.
    ```
    @Query("select e from #{#entityName} e where e.name = :name1 or e.name = :name2")
    List<Employee> findByNames(@Param("name1") String name1,
                               @Param("name2") String name2);    
    ```
    
* `@Modifying` + update queries
    ```
    @Modifying
    @Query("update Employee e set e.name = :name where e.address.city = :city")
    int updateNameForEmployeesFromCity(@Param("name") String name,
                                       @Param("city") String city);    
    ```
    1. Doing so triggers the query annotated to the method as an updating query 
    instead of a selecting one.
    1. In Spring Data the annotation @Modifying must be used on repository DML 
    query methods. Without this annotation Spring cannot decide to execute 
    JPA's `Query#executeUpdate()` instead of `Query#getQueryResult()`.
    1. As the `EntityManager` might contain outdated entities after the 
    execution of the modifying query, we do not automatically clear it 
    (see the JavaDoc of `EntityManager.clear()` for details), since this 
    effectively drops all non-flushed changes still pending in the 
    `EntityManager`. If you wish the `EntityManager` to be cleared 
    automatically, you can set the `@Modifying` annotation’s 
    `clearAutomatically` attribute to true.
    
# deleting queries
* `@Modifying`
    ```
    @Modifying
    @Query("delete from Employee e where e.address.street = ?1")
    void deleteInBulkByStreet(String street);    
    ```
    
* query method
    ```
    void deleteByAddress_Street(String street);
    ```
    
**Comparison**:
1. Although the `deleteByRoleId(…)` method looks like it basically 
produces the same result as the `deleteInBulkByRoleId(…)`, there is 
an important difference between the two method declarations in 
terms of the way they get executed. As the name suggests, the 
latter method issues a single JPQL query (the one defined in the 
annotation) against the database. This means even currently loaded 
instances of User do not see lifecycle callbacks invoked.

1. To make sure lifecycle queries are actually invoked, an invocation 
of `deleteByRoleId(…)` executes a query and then deletes the returned 
instances one by one, so that the persistence provider can actually 
invoke `@PreRemove` callbacks on those entities.

1. In fact, a derived delete query is a shortcut for executing the 
query and then calling `CrudRepository.delete(Iterable<User> users)` 
on the result and keeping behavior in sync with the implementations 
of other `delete(…)` methods in `CrudRepository`.

**Summary**:
1. `deleteByAddress_Street` test
    ```
    @Test
    @Transactional
    public void delete() {
        repository.deleteByAddress_Street("Plac Zbawiciela");
        
        assertThat(repository.findAll(), hasSize(3));
    }    
    ```
    will produce:
    ```
    Hibernate: select employee0_.id as id1_0_, employee0_.city as city2_0_, employee0_.street as street3_0_, employee0_.name as name4_0_ from employee employee0_ where employee0_.street=?
    preRemove!
    Hibernate: update issue set issues_id=null where issues_id=?
    Hibernate: delete from employee where id=?
    Hibernate: select employee0_.id as id1_0_, employee0_.city as city2_0_, employee0_.street as street3_0_, employee0_.name as name4_0_ from employee employee0_
    ```
1. `deleteInBulkByStreet` test
    ```
    @Test
    @Transactional
    public void deleteBulk() {
        repository.deleteInBulkByStreet("907 Whitehead St");
        
        assertThat(repository.findAll(), hasSize(3));
    }    
    ```
    will produce:
    ```
    Hibernate: delete from employee where street=?
    Hibernate: select employee0_.id as id1_0_, employee0_.city as city2_0_, employee0_.street as street3_0_, employee0_.name as name4_0_ from employee employee0_
    ```