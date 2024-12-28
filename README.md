
# ItsApex E-Commerce Platform

## Overview

ItsApex is an innovative e-commerce platform designed to address the challenges faced by retailers. Built using Spring Boot for the backend and Angular for the frontend, the platform operates on two different ports to deliver a seamless and scalable experience.

This platform empowers retailers to:
* Sell products online efficiently.
* Track inventory and monitor stock levels.
* Register new products dynamically.
* Handle regulatory compliance with a DB-driven workflow.

Additionally, delivery personnel can register on the platform, define delivery zones, and receive tasks based on shop location and delivery areas. Payments are calculated based on delivery distance and time.

## Key Featues
### 1. Retailer Management
* Register retailers with a dynamic DB-driven form based on government regulations and business mandates.
* Workflow adapts to the type of merchant for a tailored onboarding experience.
* Inventory management to track stock levels and product details.

### 2. Delivery Management
* Delivery agents can register and select their preferred delivery zones.
* Tasks are automatically assigned based on proximity to the store and delivery location.
* Payments are calculated based on distance and delivery time.

### 3. Secure Transactions
* Allows buyers to purchase from trusted retailers.
* Provides options for custom modifications as per buyer requests.

### 4. Dynamic DB-Driven Workflow
* The registration process adapts dynamically to changing government and business regulations.
* Supports customizable forms that can evolve without major code changes.

## Technology Stack
- Frontend: Angular (Runs on port 4200)
- Backend: Spring Boot (Runs on port 8080)
- Database: PostgreSQL/ MongoDB (Configurable based on requirements)
- REST APIs: For seamless communication between frontend and backend
- Authentication: Spring Security for user authentication and authorization
- Build Tools: Maven for backend and Angular CLI for frontend



