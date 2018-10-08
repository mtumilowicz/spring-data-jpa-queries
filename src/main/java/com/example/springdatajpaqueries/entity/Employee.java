package com.example.springdatajpaqueries.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by mtumilowicz on 2018-10-03.
 */
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@ToString
@NamedQuery(name = "Employee.employeesByIssueDescription",
        query = "select e from Employee e join fetch e.issues i where i.description = ?1")
public class Employee {
    @Id
    Integer id;

    String name;

    @Embedded
    Address address;

    @OneToMany
    @JoinColumn
    Collection<Issue> issues;
}
