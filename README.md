# Smart-Menu

A full-stack web application designed to modernize the restaurant dining experience. It allows customers to browse the menu and place orders directly from their table, which are then displayed on a real-time dashboard for the kitchen staff.

## Table of Contents

- [About The Project](#about-the-project)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Installation](#installation)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## About The Project

This application aims to automate and streamline the customer ordering process in a restaurant. By providing a digital menu and ordering platform, it reduces the need for manual order-taking by waiters, minimizes errors, and improves efficiency. Customers can place orders at their own pace, and the kitchen receives them instantly on a dedicated dashboard for processing.

## Key Features

-   **User Authentication**: Secure registration and login for customers and administrative staff.
-   **Dynamic Menu Management**: Admins can easily create, update, and delete food items and categories.
-   **Seamless Ordering System**: An intuitive interface for customers to browse the menu, add items to their cart, and place an order.
-   **Real-Time Order Tracking**: A live kitchen dashboard displays incoming orders and allows staff to update the order status (e.g., Preparing, Ready, Delivered).
-   **Payment Integration**: Facilitates payment processing, suitable for modern restaurant and cloud kitchen models.

## Tech Stack

The application is built with a modern, robust technology stack:

| Category              | Technology                                                                                             |
| --------------------- | ------------------------------------------------------------------------------------------------------ |
| **Frontend**          | Next.js, React, TypeScript                                                                             |
| **Backend**           | Spring Boot, Spring Data JPA, Java                                                                     |
| **Database**          | PostgreSQL                                                                                             |
| **ORM & Auditing**    | Hibernate, Hibernate Envers                                                                            |
| **Database Migration**| Liquibase                                                                                              |
| **Build Tool**        | Gradle                                                                                                 |

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites

Make sure you have the following software installed on your machine:
-   JDK 17 or later
-   Node.js v18 or later
-   PostgreSQL 14 or later
-   Gradle 7.0 or later

### Installation

1.  **Clone the repository**
    ```sh
    git clone https://github.com/your_username/smart-menu.git
    cd smart-menu
    ```

2.  **Configure the Backend (Spring Boot)**
    -   Navigate to the backend project directory.
    -   Open the `src/main/resources/application.properties` (or `.yml`) file.
    -   Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties with your local PostgreSQL database credentials.
    -   Run the application:
        ```sh
        ./gradlew bootRun
        ```
    -   The backend server will start, typically on port 8080. Liquibase will automatically run the database migrations on startup.

3.  **Configure the Frontend (Next.js)**
    -   Navigate to the frontend project directory.
    -   Install NPM packages:
        ```sh
        npm install
        ```
    -   Create a `.env.local` file in the frontend root directory and add the backend API URL:
        ```
        NEXT_PUBLIC_API_BASE_URL=http://localhost:8080
        ```
    -   Start the development server:
        ```sh
        npm run dev
        ```
    -   Open your browser and navigate to `http://localhost:3000`.

## Usage

Once the application is running:
-   **Customers** can register, log in, browse the menu, add items to their cart, and place an order.
-   **Admin/Kitchen Staff** can log in to a separate dashboard to manage menu items and view/update incoming orders in real-time.

## Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

## License

Distributed under the MIT License. See `LICENSE` for more information.