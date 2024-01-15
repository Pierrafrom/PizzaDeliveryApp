# Pizza Delivery App

## Overview

The Pizza Delivery App is a Java-based application designed to manage and optimize pizza delivery operations for a
pizzeria. The system employs multiple algorithms to efficiently allocate delivery personnel, optimize delivery routes,
and enhance the overall customer experience.

## Table of Contents

1. [Installation](#installation)
2. [Purpose & Usage](#purpose--usage)
3. [Project Structure](#project-structure)
4. [Classes Overview](#classes-overview)
5. [Algorithms](#algorithms)
6. [File Persistence](#file-persistence)

## Installation

To run the Pizza Delivery App, you have a `PizzeriaTest` class provided to you in order to test the application. You can
run it in your IDE.

## Purpose & Usage

This project goal of this project is to understand and learn when/how to use different algorithmic approaches. The Pizza
Delivery App provides a console-based interface for delivering pizza orders, delivery personnel, and optimizing delivery
routes.

## Project Structure

The project follows the Maven directory structure:

- `src/main/java`: Contains the main Java source code.
    - `com.pizzadelivery.model`: Contains classes representing the core model of the application,
      including `Pizzeria`, `Order`, and various algorithm implementations.
    - `com.pizzadelivery.util`: Includes utility classes such as `GPS` for geographical calculations
      and `SaveableHashMap` for persistent data storage.
- `src/test/java`: Contains test classes.
- `src/main/resources`: Contains configuration files and other resources.
- `pom.xml`: The Maven Project Object Model file, defining project dependencies and build configurations.

## Classes Overview

1. `Pizzeria`: Represents the main control center of the pizza delivery operations. Manages delivery personnel, orders,
   and implements algorithms for order assignment.

2. `DeliveryPerson`: Represents a delivery person and extends the `Thread` class for concurrent order delivery.

3. `SailorMan`: Represents a delivery person and extends the `Thread` class for concurrent order delivery.

4. `Order`: Represents a pizza order, including location, timestamp, and details.

5. `GPS`: Provides utility methods for geographical calculations, such as distance between two points.

6. `SaveableHashMap`: Extends the `HashMap` class to provide persistent storage of key-value pairs.

7. `Main`: Entry point for the Pizza Delivery App. Initializes and runs the application.

8. `SamuelAlgorithm`: Implements Samuel's algorithms for order assignment.

9. `PierreAlgorithm`: Implements Pierre's algorithms for order assignment.

10. `RemiAlgorithm`: Implements Remi's algorithms for order assignment.

11. `Grades`:The Grades interface defines constants representing the maximum delivery times.

## Algorithms

The Pizza Delivery App employs various algorithms to optimize order assignment and delivery routes.

The class `Pizzeria` is the point of entry of the application, when an order waits for too long, we search the best 4
other orders that combine the best by using the class `SailorManAlgorithm`. This class uses the algorithm implemented
in `SamuelAlgorithm`, `PierreAlgorithm`, and `RemiAlgorithm`. Each of these classes implements 4 algorithms for order
assignment with different criteria  : **number of discount tickets | time of delivery | total distance during delivery.
** Here are the type of algorithms implemented in each class :

- Brute force
- Greedy
- Dynamic
- Genetic

## File Persistence

The `SaveableHashMap` class enables the persistent storage of data to a file. It ensures that data is loaded from the
file during application startup and saved automatically when modifications occur.

