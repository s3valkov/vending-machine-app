# Vending Machine API

This API provides functionality for managing a vending machine, including inventory management, coin insertion, and product purchasing.


## Prerequisites

- Java Development Kit (JDK) version 8 or later
- Gradle build tool


## Run the application
- Go to the folder, where the repo is cloned
- Execute ```gradlew bootRun```

## Run tests
- Execute ```gradlew test```

### 1. Get Inventory

**Endpoint:**
GET /api/products

**Description:**
Retrieve the current inventory of products.

```console
curl http://localhost:8080/api/products
```

### 2. Add Product

**Endpoint:**
POST /api/add


**Request:**
```json
{
  "id": 1,
  "name": "Fanta",
  "price": 1.75,
  "quantity": 8
}
``` 

**Description:**
Add a new product to the inventory.

```console
curl -X POST -H "Content-Type: application/json" -d '{"id": 1, "name": "Fanta", "price": 1.75, "quantity": 8}' http://localhost:8080/api/add
```

### 3. Update Product
**Endpoint:**
PUT /api/update/


**Request:**
```json
{
  "id": 1,
  "name": "Fanta Lemon",
  "price": 2.00,
  "quantity": 20
}
```

**Description:**
Update an existing product in the inventory.

```console
curl -X POST -H "Content-Type: application/json" -d '{"id": 1, "name": "Fanta Lemon", "price": 2.25, "quantity": 12}' http://localhost:8080/api/update
```

### 4. Remove Product
**Endpoint:**
DELETE /api/remove/{productId}

**Description:**
Remove a product from the inventory.

```console
curl -X DELETE http://localhost:8080/api/remove/1
```

### 5. Insert Coins
**Endpoint:**
POST /api/insert-coin/{coin}

**Description:**
Insert a coin into the vending machine where valid coin is (10, 20, 50, 100, 200).

```console
curl -X POST http://localhost:8080/api/insert-coin/50
```

### 6. Reset Inserted Coins
**Endpoint:**
POST /api/reset

**Description:**
Reset the inserted coins and return the total.

```console
curl -X POST http://localhost:8080/api/reset
```

### 7. Buy Product
**Endpoint:**
POST /api/buy/{productId}

**Description:**
Purchase a product from the vending machine.

```console
curl -X POST http://localhost:8080/api/buy/1
```