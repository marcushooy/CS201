package com.reviews.Models;
public interface ReviewRecord {
    String getName(); //airline name or airport name    
    String getDate(); //date of the review
    double getOverallRating(); //overall rating
    String getCountry(); //country of the airline/airport
    String getContent(); //content of the review
    String getAirline(); // airline name
}
