INSERT INTO public.users (
    user_id, created_at, updated_at, birth_date, damage_count, department, email,
    gender, grade, "group", major, name, nickname, password, phone_number,
    professor_id, profile_picture, rental_count, report_count, restriction_count,
    role, student_number
)
VALUES
-- 홍길동
(1, '2025-04-30 09:03:58.943824', '2025-04-30 09:03:58.943824', '2001-03-15', 0, '학과', 'hong@example.com',
 '남', 3, 'A조', '세부전공', '홍길동', '길동이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
 '010-1234-5678', null, 'profile1.jpg', 5, 2, 1, 'ROLE_USER', '20230001'),

-- 김영희
(2, '2025-04-30 09:03:58.943824', '2025-04-30 09:03:58.943824', '2002-06-22', 1, '학과', 'kim@example.com',
 '여', 2, 'B조', '세부전공', '김영희', '김영희', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
 '010-2345-6789', null, null, 2, 1, 0, 'ROLE_USER', '20230002'),

-- string
(5, '2025-04-30 18:35:35.303256', '2025-04-30 18:35:49.079072', '2024-01-01', 0, 'string', null,
 'string', 0, null, null, 'string', 'string', '$2a$10$ygqz9EsG93pjjE4mphRwBuSpp.uEhqOx/a7ITpNpXhRYirtN/SyZq',
 '01012341234', null, 'string', 0, 0, 0, 'ROLE_USER', '202301235'),

-- 박철수
(3, '2025-04-30 09:03:58.943824', '2025-04-30 09:03:58.943824', '2000-12-05', 3, '학과', 'park@example.com',
 '남', 4, 'A조', '세부전공', '박철수', '철수짱', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
 '010-3456-7890', null, 'profile3.png', 10, 5, 2, 'ROLE_USER', '202300003');