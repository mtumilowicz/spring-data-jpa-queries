package com.example.springdatajpaqueries.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Embeddable;

/**
 * Created by mtumilowicz on 2018-10-03.
 */
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@ToString
@Getter
public class Address {
    String street;
    String city;
}
