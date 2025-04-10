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
    user_id BIGSERIAL PRIMARY KEY,
    student_id VARCHAR UNIQUE,
    name VARCHAR,
    nickname VARCHAR,
    department VARCHAR,
    major VARCHAR,
    year INTEGER,
    gender VARCHAR,
    professor VARCHAR,
    phone_number VARCHAR,
    email VARCHAR,
    password VARCHAR,
    profile_picture VARCHAR,
    birth_date DATE,
    restriction_count INTEGER,
    report_count INTEGER,
    role VARCHAR,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
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