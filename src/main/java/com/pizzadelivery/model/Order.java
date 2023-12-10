package com.pizzadelivery.model;

import java.time.LocalDateTime;

public record Order(int id, GPS location, LocalDateTime dateTime) {

    @Override
    public String toString() {
        return "\n\tOrder\n\t{" +
                "\n\tid=" + id + ',' +
                "\n\t location=" + location + ',' +
                "\n\t dateTime=" + dateTime + ',' +
                "\n\t}";
    }
}
