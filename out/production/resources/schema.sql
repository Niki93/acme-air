CREATE TABLE flight (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    origin VARCHAR(50) NOT NULL,
    destination VARCHAR(50) NOT NULL,
    flight_date DATE NOT NULL,
    seats_available INT NOT NULL,
    cost INT NOT NULL,
    seat_class VARCHAR(50) NOT NULL
);