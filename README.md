# Pizza Delivery App
Made and thought by <a href="https://github.com/Pierrafrom" target="_blank">Pierre Fromont Boissel</a>, <a href="https://github.com/Samuelito78" target="_blank">Samuel Boix-Segura</a> and <a href="https://github.com/Gayar78" target="_blank">Rémi Thibault</a> . <br>

## Overview

The Pizza Delivery App is a Java-based application designed to manage and optimize pizza delivery operations for a
pizzeria. The system employs multiple algorithms to efficiently allocate delivery personnel, optimize delivery routes,
and enhance the overall customer experience.

## Table of Contents

1. [Running the app](#running-the-app)
2. [Testing with JUnit](#testing-with-junit)
    1. [Overview](#overview)
    2. [Test Classes](#test-classes)
    3. [Test Suite Generation](#test-suite-generation)
    4. [API Calls and Real-time Data Handling](#api-calls-and-real-time-data-handling)
3. [Purpose & Usage](#purpose--usage)
4. [Important points & Criteria](#important-points--criteria)
5. [Project Structure](#project-structure)
6. [Classes Overview](#classes-overview)
7. [Algorithms](#algorithms)
8. [OpenRouteService API Integration and Caching](#openrouteservice-api-integration-and-caching)
    1. [API Usage for Distance and Travel Time Calculations](#api-usage-for-distance-and-travel-time-calculations)
    2. [Handling API Limitations](#handling-api-limitations)
    3. [Backup Solution](#backup-solution)
    4. [Caching Mechanism](#caching-mechanism)
    5. [Continuous Improvement](#continuous-improvement)
    6. [Code Snippet](#code-snippet)
9. [File Persistence](#file-persistence)

## Running the app

To run the Pizza Delivery App, you have a `test`package and inside it `PizzeriaTest` class provided to you in order to
test the application. You can run it in your IDE and even the other test classes inside the package:

- `AlgorithmTest`
- `PierreAlgorithmTest`
- `SamuelAlgorithmTest`
- `PizzeriaTest`

## Testing with JUnit

### Overview

Our application utilizes JUnit for conducting automated tests to ensure the reliability and performance of various
algorithms used for pizza delivery. The tests are designed to cover all functional aspects of the system, including
delivery algorithms, order management, and the accuracy of GPS calculations.

### Test Classes

The following test classes are available in the `src/test/java` package of the Maven structure:

- `AlgorithmTest`: Tests the validity of the results returned by different algorithms based on criteria such as delivery
   time, distance, and the number of discount tickets.
- `PierreAlgorithmTest`, `SamuelAlgorithmTest`: These classes specifically test the algorithms developed by Pierre and
   Samuel, assessing their efficiency and accuracy in various scenarios.
- `PizzeriaTest`: Ensures that the `Pizzeria` class properly manages the assignment of orders to delivery personnel and
   the execution of delivery algorithms.
- `TestSuiteGenerator`: Generates dynamic test suites with varied orders to test the robustness of algorithms under
   different conditions.

### Test Suite Generation

`TestSuiteGenerator` is a key class in our testing system. It automatically creates test orders with varied GPS
coordinates. These orders are then used to simulate different delivery scenarios and test the effectiveness of the
algorithms.

### API Calls and Real-time Data Handling

Our tests incorporate API calls to validate GPS coordinates and simulate real-world conditions. Additionally, the times
of the orders are generated randomly at each program launch to ensure dynamic and representative testing of real-world
conditions.

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
    - **Total distance during delivery** : The algorithm will select the orders with the shortest distance during
      delivery.

## Project Structure

The project follows the Maven directory structure:

- `src/main/java`: Contains the main Java source code.
    - `com.pizzadelivery.model`: Contains classes representing the core model of the application, including `Pizzeria`,
      `Order`, and various algorithm implementations.
    - `com.pizzadelivery.util`: Includes utility classes such as `GPS` for geographical calculations
      and `SaveableHashMap`
      for persistent data storage.
- `src/test/java`: Contains test classes.
- `src/main/resources`: Contains configuration files and other resources.
- `pom.xml`: The Maven Project Object Model file, defining project dependencies and build configurations.

### Maven Project Structure

#### Overview

The project follows the standard Maven structure, facilitating dependency management and build automation.

#### Structure Details

- `src/main/java`: Contains the main source code of the application.
- `src/test/java`: Contains the JUnit test classes.
- `src/main/resources`: Includes configuration files and other resources necessary for the application.
- `pom.xml`: Maven configuration file defining project dependencies and build configurations.

#### Dependency Management

With Maven, dependency management is streamlined. All necessary libraries, including JUnit for testing, are defined in
the `pom.xml` file and are automatically managed by Maven.

#### Build and Test Automation

Maven also facilitates the automation of the build and test process. Developers can easily compile the project and run
the entire suite of tests using simple Maven commands.

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

10. `RemiAlgorithm`: Implements Remi's algorithms for order assignment. For now, we are not using Remi's algorithm because
    it is not working properly to make our app work.     

11. `Grades`:The Grades interface defines constants representing the maximum delivery times.

## Algorithms

The Pizza Delivery App employs various algorithms to optimize order assignment and delivery routes.

The class `Pizzeria` is the point of entry of the application, when an order waits for too long, we search the best 4
other orders that combine the best by using the class `SailorManAlgorithm`. This class uses the algorithm implemented
in `SamuelAlgorithm`, `PierreAlgorithm`, and `RemiAlgorithm`.

Each of these classes implements 4 algorithms for order assignment with different criteria. Each algorithm is called
according to the number of orders criteria : **number of discount tickets | time of delivery | total distance during
delivery.** Here are the type of algorithms implemented in each class :

- Brute force **nb orders < 5**
- Greedy **nb orders > 50**
- Dynamic **nb orders < 20 & nb orders >5**
- Genetic **nb orders >20 & nb orders < 50**

When `SailorManAlgorithm` gets the result of each algorithm classes, it compares the grades of each array of orders and
returns the best one. Then the `Pizzeria` class calls the method `sortOrders`, this method uses brute force algorithm to
sort an array of 5 orders.

## OpenRouteService API Integration and Caching

### API Usage for Distance and Travel Time Calculations

The `GPS` class in our application integrates with the OpenRouteService API to calculate the distance and travel time
between two GPS locations. This external API provides accurate and up-to-date information which is crucial for
optimizing our delivery routes.

### Handling API Limitations

To manage API limitations and potential downtimes, our system implements an API cooldown strategy. If the API rate limit
is exceeded or an error occurs, our application enters a cooldown period, during which it switches to backup
calculations.

### Backup Solution

As a backup, we use the Haversine formula to calculate "as-the-crow-flies" distances and estimate travel times based on
a fixed scooter speed. This ensures that our application remains functional and continues to provide route calculations
even when the external API is unavailable.

### Caching Mechanism

To optimize performance and reduce unnecessary API calls, we have implemented a caching system using `SaveableHashMap`,
a custom extension of Java's `HashMap`. This class automatically saves its content to a file upon any modification,
ensuring data persistence between application runs.

- **Time and Distance Caching**: Separate caches are maintained for travel time and distance calculations. When a route
  calculation is requested, the application first checks the cache. If the information is available, it is used
  directly; otherwise, an API call is made.

- **Thread-Safe File Operations**: To ensure data integrity, file operations in `SaveableHashMap` are thread-safe. This
  is achieved using lock mechanisms, allowing concurrent access and modifications by multiple threads.

- **Separate Thread for API Cooldown**: The cooldown mechanism for the API is managed in a separate thread. This
  approach allows the main application to continue functioning while the cooldown timer runs in the background.

### Continuous Improvement

Our application continuously updates its cache with the latest data from the API. As we accumulate more route
information, our reliance on the API decreases, leading to faster response times and reduced API usage.

### Code Snippet

Here is a snippet from the `GPS` class showing the API call and caching mechanism:

```java
public static String callOpenRouteServiceApi(GPS source, GPS destination) throws Exception {
    // API call logic
}

public double timeTravel(GPS otherGPS) {
    // Caching and calculation logic
}

private static void startApiCooldown() {
    // API cooldown logic in a separate thread
}
```

## File Persistence

The SaveableHashMap class ensures that our cache is persistently stored and updated:

```java

public class SaveableHashMap<K, V> extends HashMap<K, V> {
// Custom HashMap logic with file persistence
}
```

This robust system enhances the efficiency and reliability of our pizza delivery operations, ensuring quick response
times and reduced dependence on external APIs.

