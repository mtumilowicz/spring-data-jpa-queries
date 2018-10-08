package com.example.springdatajpaqueries.entity;

/**
 * Created by mtumilowicz on 2018-10-08.
 */
public interface EmployeeNameCityProjection {
    String getName();
    AddressCityProjection getAddress();
    
    public interface AddressCityProjection {
        String getCity();
    }
}
