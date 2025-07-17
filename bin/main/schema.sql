-- Schema für Evasio-Datenbank
-- Erstellt Tabellen mit korrekter Auto-Increment-Konfiguration

-- Topics Tabelle
CREATE TABLE IF NOT EXISTS topic (
    topic_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(100),
    difficulty VARCHAR(50)
);

-- Modules Tabelle
CREATE TABLE IF NOT EXISTS module (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    topic_id BIGINT,
    FOREIGN KEY (topic_id) REFERENCES topic(topic_id)
);

-- Quizzes Tabelle
CREATE TABLE IF NOT EXISTS quiz (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(500) NOT NULL,
    correct_answer VARCHAR(255) NOT NULL,
    question_type VARCHAR(50),
    module_id BIGINT,
    FOREIGN KEY (module_id) REFERENCES module(id)
);

-- Quiz Options Tabelle
CREATE TABLE IF NOT EXISTS quiz_options (
    quiz_id BIGINT,
    options VARCHAR(255),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

-- Quiz Matching Options Tabelle
CREATE TABLE IF NOT EXISTS quiz_matching_options (
    quiz_id BIGINT,
    matching_options VARCHAR(255),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

-- Roles Tabelle
CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) UNIQUE NOT NULL
);

-- Users Tabelle
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(255) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    password VARCHAR(500) NOT NULL,
    email VARCHAR(50) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

-- Users Roles Verknüpfungstabelle
CREATE TABLE IF NOT EXISTS users_roles (
    user_id VARCHAR(255),
    roles_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (roles_id) REFERENCES role(id)
);

-- User Topics Tabelle
CREATE TABLE IF NOT EXISTS user_topic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    topic_id BIGINT NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    unlocked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (topic_id) REFERENCES topic(topic_id)
);

-- User Modules Tabelle
CREATE TABLE IF NOT EXISTS user_modules (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    user_topic_id BIGINT,
    module_id BIGINT,
    completed BOOLEAN DEFAULT FALSE,
    unlocked BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_topic_id) REFERENCES user_topic(id),
    FOREIGN KEY (module_id) REFERENCES module(id)
);

-- Tokens Tabelle
CREATE TABLE IF NOT EXISTS token (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) DEFAULT 'BEARER',
    revoked BOOLEAN DEFAULT FALSE,
    expired BOOLEAN DEFAULT FALSE,
    user_id VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Prices Tabelle (für Stripe)
CREATE TABLE IF NOT EXISTS price (
    id VARCHAR(255) PRIMARY KEY,
    product_id VARCHAR(255),
    unit_amount BIGINT,
    currency VARCHAR(10),
    recurring_interval VARCHAR(20),
    recurring_interval_count INT,
    active BOOLEAN DEFAULT TRUE
);

-- Price Metadata Tabelle
CREATE TABLE IF NOT EXISTS price_metadata (
    price_id VARCHAR(255),
    metadata_key VARCHAR(255),
    metadata_value TEXT,
    FOREIGN KEY (price_id) REFERENCES price(id)
); 