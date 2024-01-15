# Pizza Delivery App

## Overview

The Pizza Delivery App is a Java-based application designed to manage and optimize pizza delivery operations for a
pizzeria. The system employs multiple algorithms to efficiently allocate delivery personnel, optimize delivery routes,
and enhance the overall customer experience.

## Table of Contents

1. [Running the app](#running-the-app)
2. [Purpose & Usage](#purpose--usage)
3. [Important points & Criteria](#important-points--criteria)
4. [Project Structure](#project-structure)
5. [Classes Overview](#classes-overview)
6. [Algorithms](#algorithms)
7. [File Persistence](#file-persistence)

## Running the app

To run the Pizza Delivery App, you have a `test`package and inside it `PizzeriaTest` class provided to you in order to
test the application. You can run it in your IDE and even the other test classes inside the package:
- `AlgorithmTest`
- `PierreAlgorithmTest`
- `SamuelAlgorithmTest`
- `PizzeriaTest`

## Purpose & Usage

This project goal of this project is to understand and learn when / how to use different algorithmic approaches. The
Pizza Delivery App provides a console-based interface for delivering pizza orders and optimizing delivery
routes.

## Important points & Criteria

Our main constraint is that each delivery person can only deliver 5 orders at a time maximum.
- The `Pizzeria` class is the main class of the application
  - An order in our list of order waits for too long in the pizzeria.
  - It triggers a method that will select the algorithm to apply to our list of order.
  - We select the type of algorithm based on the number of elements in the list (refer to constants).
  - Once all the algorithm send their result it compares their grades.
  - The best combination is selected and then sorted with the brute force algorithm.
  - The order is then assigned to the delivery person.
- Algorithm types
  - **Brute force**
  - **Greedy**
  - **Dynamic**
  - **Genetic**
- Algorithm criteria
  - **Number of discount tickets** : The algorithm will select the orders with the least discount tickets.
  - **Time of delivery** : The algorithm will select the orders with the smallest delivery time.
  - **Total distance during delivery** : The algorithm will select the orders with the shortest distance during delivery.


## Project Structure

The project follows the Maven directory structure:

- `src/main/java`: Contains the main Java source code.
  - `com.pizzadelivery.model`: Contains classes representing the core model of the application, including `Pizzeria`,
    `Order`, and various algorithm implementations.
  - `com.pizzadelivery.util`: Includes utility classes such as `GPS` for geographical calculations and `SaveableHashMap`
    for persistent data storage.
- `src/test/java`: Contains test classes.
- `src/main/resources`: Contains configuration files and other resources.
- `pom.xml`: The Maven Project Object Model file, defining project dependencies and build configurations.


## Classes Overview

1. `Pizzeria`: Represents the main control center of the pizza delivery operations. Manages delivery personnel, orders,
   and implements algorithms for order assignment.

2. `DeliveryPerson`: Represents a delivery person and extends the `Thread` class for concurrent order delivery.

3. `SailorManAlgorithm`: Represents a delivery person and extends the `Thread` class for concurrent order delivery.

4. `Order`: Represents a pizza order, including location, timestamp, and details.

5. `GPS`: Provides utility methods for geographical calculations, such as distance between two points. In this class we 
    are logging API errors.

6. `SaveableHashMap`: Extends the `HashMap` class to provide persistent storage of key-value pairs.

7. `Main`: Entry point for the Pizza Delivery App. Initializes and runs the application.

8. `SamuelAlgorithm`: Implements Samuel's algorithms for order assignment.

9. `PierreAlgorithm`: Implements Pierre's algorithms for order assignment.

10. `RemiAlgorithm`: Implements Remi's algorithms for order assignment.

11. `Grades`:The Grades interface defines constants representing the maximum delivery times.


## Algorithms

The Pizza Delivery App employs various algorithms to optimize order assignment and delivery routes.

The class `Pizzeria` is the point of entry of the application, when an order waits for too long, we search the best 4 other orders that combine the best by using the class `SailorManAlgorithm`. This class uses the algorithm implemented in `SamuelAlgorithm`, `PierreAlgorithm`, and `RemiAlgorithm`.

Each of these classes implements 4 algorithms for order assignment with different criteria. Each algorithm is called according to the number of orders criteria : **number of discount tickets | time of delivery | total distance during delivery.** Here are the type of algorithms implemented in each class :
- Brute force **nb orders < 5**
- Greedy **nb orders > 50**
- Dynamic **nb orders < 20 & nb orders >5**
- Genetic **nb orders >20 & nb orders < 50**

When `SailorManAlgorithm` gets the result of each algorithm classes, it compares the grades of each array of orders and returns the best one. Then the `Pizzeria` class calls the method `sortOrders`, this method uses brute force algorithm to sort an array of 5 orders.

## File Persistence

The `SaveableHashMap` class enables the persistent storage of data to a file. It ensures that data is loaded from the file during application startup and saved automatically when modifications occur.

