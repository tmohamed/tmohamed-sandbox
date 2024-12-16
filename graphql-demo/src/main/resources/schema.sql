-- Create the AUTHORS table (if it doesn't already exist)
CREATE TABLE AUTHORS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    BIO VARCHAR(255),
    NAME VARCHAR(255) NOT NULL
);

-- Create the BOOKS table with PUBLISH_YEAR as an INT
CREATE TABLE BOOKS (
    ID BIGINT AUTO_INCREMENT PRIMARY KEY,
    TITLE VARCHAR(255) NOT NULL,            -- Book title
    PUBLISHED_YEAR INT,                       -- Year the book was published (as an INT)
    AUTHOR_ID BIGINT,                       -- Foreign key to the AUTHORS table
    FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHORS(ID) ON DELETE CASCADE
);
