-- 아래 계정들의 비밀번호는 asdf1234! 임

insert into public.users (birth_date, broken_count, grade, rental_count, report_count, restriction_count, created_at,
                          professor_id, updated_at, department, email, gender, "group", major, name, nickname,
                          password, phone_number, profile_picture, role, student_number)
values  ('2001-03-15', 0, 3, 5, 2, 1,
         '2025-05-16 13:40:26.060319', null, '2025-05-16 13:40:26.060319',
         '학과', 'hong@example.com', '남', 'A조', '세부전공',
         '슈퍼어드민', '길동이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01012345678', 'profile1.jpg', 'ROLE_SUPER_ADMIN', '000'),
        ('2002-06-22', 1, 2, 2, 1, 0,
         '2025-05-16 13:40:26.060319', null, '2025-05-16 13:40:26.060319',
         '학과', 'kim@example.com', '여', 'B조', '세부전공',
         '김영희', '김영희', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01023456789', null, 'ROLE_SUPER_ADMIN', '20230002'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', null, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', '남', 'A조', '세부전공',
         '박철수', '철수짱', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01034567890', 'profile3.png', 'ROLE_SUPER_ADMIN', '202300003');