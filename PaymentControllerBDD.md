# Payment Controller BDD Documentation

## Overview
This document describes the Behavior-Driven Development (BDD) scenarios for the Payment Controller in the Payment Management System. The tests follow a unit testing approach using JUnit 5 and Mockito, focusing on controller layer validation with Given-When-Then structure.

## Test Structure
- **Framework**: JUnit 5 with Mockito Extension
- **Approach**: Unit tests focusing on controller methods
- **Pattern**: Given-When-Then with descriptive scenario names
- **Coverage**: CREATE, RETRIEVE, DELETE operations

## Scenarios

### CREATE PAYMENT TESTS

#### Scenario: Create payment QR code PIX successfully
**Given** a valid payment request with CPF and order ID  
**When** creating payment QR code PIX  
**Then** return payment response with OK status and verify service calls  

#### Scenario: Create payment fails when order not found
**Given** an invalid order ID in payment request  
**When** creating payment QR code PIX  
**Then** throw RuntimeException for order not found  

### RETRIEVE PAYMENT TESTS

#### Scenario: Retrieve payment by ID successfully
**Given** an existing payment ID  
**When** finding payment by ID  
**Then** return payment response with OK status  

#### Scenario: Retrieve payment by ID fails when not found
**Given** a non-existing payment ID  
**When** finding payment by ID  
**Then** throw RuntimeException for payment not found  

#### Scenario: Retrieve all payments with pagination
**Given** a pageable request  
**When** getting payments  
**Then** return paged payment responses with OK status  

#### Scenario: Retrieve payment options
**Given** no specific conditions  
**When** listing payment options  
**Then** return list of payment options with OK status  

### DELETE PAYMENT TESTS

#### Scenario: Delete payment by ID successfully
**Given** an existing payment ID  
**When** deleting payment by ID  
**Then** return NO_CONTENT status  

#### Scenario: Delete payment fails when not found
**Given** a non-existing payment ID  
**When** deleting payment by ID  
**Then** throw RuntimeException for payment not found  

## Test Implementation Details

### Mocks Used
- `PaymentUseCase`: Business logic interface
- `PaymentMapper`: Data transformation interface

### Helper Methods
- `createPaymentOrder()`: Creates a sample PaymentOrder domain object
- `createPaymentOrderResponse()`: Creates a sample PaymentOrderResponse DTO

### Verification Patterns
- Service method calls are verified using `verify()`
- ResponseEntity status codes are asserted
- Exception throwing is tested with `assertThrows()`

## Dependencies
- JUnit 5 (jupiter)
- Mockito Core
- Spring Framework (for ResponseEntity)
- Project domain models and DTOs

## Running Tests
Execute tests using your IDE or Maven:
```bash
mvn test -Dtest=PaymentControllerTest
```