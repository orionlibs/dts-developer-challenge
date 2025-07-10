DROP DATABASE IF EXISTS cases;
CREATE DATABASE cases CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


CREATE TABLE cases.cases
(
    id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    case_number VARCHAR(50) NOT NULL,
    title VARCHAR(700) NOT NULL,
    description TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_date_time DATETIME NOT NULL,
    updated_date_time DATETIME NOT NULL,
    due_date_time DATETIME NOT NULL,
    INDEX(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE utf8mb4_unicode_ci;