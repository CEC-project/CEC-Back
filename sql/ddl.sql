-- categories
CREATE TABLE categories (
    category_id BIGSERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

-- equipment
CREATE TABLE equipment (
    equipment_id BIGSERIAL PRIMARY KEY,
    name VARCHAR,
    category VARCHAR,
    asset_number VARCHAR,
    model VARCHAR,
    description TEXT,
    related_items TEXT,
    status VARCHAR,
    available BOOLEAN,
    rental_start_time TIMESTAMP,
    rental_end_time TIMESTAMP,
    rented_by_user_id BIGINT
);

-- users
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    student_number VARCHAR(255) NOT NULL,
    nickname VARCHAR(255),
    grade VARCHAR(255) NOT NULL,
    group VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL,
    professor VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_picture VARCHAR(255),
    birthdate DATE NOT NULL,
    restriction_count INTEGER NOT NULL DEFAULT 0,
    report_count INTEGER NOT NULL DEFAULT 0,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    equipment_id VARCHAR(255),
    bookmark_id VARCHAR(255),
    complaint_id VARCHAR(255),
    rental_history_id VARCHAR(255)
);

-- bookmarks
CREATE TABLE bookmarks (
    bookmark_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    equipment_id BIGINT
);

-- classrooms
CREATE TABLE classrooms (
    classroom_id BIGSERIAL PRIMARY KEY,
    name VARCHAR,
    location VARCHAR,
    status VARCHAR,
    rental_start_time TIMESTAMP,
    rental_end_time TIMESTAMP,
    admin_id BIGINT
);

-- rental_history
CREATE TABLE rental_history (
    rental_id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT,
    user_id BIGINT,
    rental_start_time TIMESTAMP,
    rental_end_time TIMESTAMP,
    status VARCHAR
);

-- complaints
CREATE TABLE complaints (
    complaint_id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    year VARCHAR,
    type VARCHAR,
    title VARCHAR,
    content TEXT,
    attachment VARCHAR,
    status VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- notices
CREATE TABLE notices (
    notice_id BIGSERIAL PRIMARY KEY,
    title VARCHAR,
    content TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    author_id BIGINT
);