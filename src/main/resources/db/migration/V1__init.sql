-- users table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL
);

-- tasks table
CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    description TEXT,
    creation_date TIMESTAMP,
    target_date TIMESTAMP,
    completed BOOLEAN DEFAULT FALSE,
    deleted BOOLEAN DEFAULT FALSE
);

-- notifications table
CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT,
    read BOOLEAN DEFAULT FALSE
);
