package com.pizzadelivery.model;

import java.time.LocalDateTime;

public record Order(int id, GPS location, LocalDateTime orderDate) {

}
