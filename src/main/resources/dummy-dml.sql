-- 아래 계정들의 비밀번호는 asdf1234! 임

insert into users (birth_date, damage_count, grade, rental_count, report_count, restriction_count, created_at,
                          professor_id, updated_at, department, email, gender, "group", major, name, nickname,
                          password, phone_number, profile_picture, role, student_number)
values  ('2001-03-15', 0, 3, 5, 2, 1,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'hong@example.com', '남', 'A조', '세부전공',
         '슈퍼어드민', '길동이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-1234-5678', 'profile1.jpg', 'ROLE_SUPER_ADMIN', '000'),
        ('2002-06-22', 1, 2, 2, 1, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'kim@example.com', '여', 'B조', '세부전공',
         '김영희', '김영희', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-2345-6789', null, 'ROLE_SUPER_ADMIN', '20230002'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', '남', 'A조', '세부전공',
         '박철수', '철수짱', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-3456-7890', 'profile3.png', 'ROLE_SUPER_ADMIN', '202300003'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', '남', 'A조', '세부전공',
         '유저유저', '유저유저', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-3456-7890', 'profile3.png', 'ROLE_USER', '202300004');

-- 강의실 INSERT
insert into public.classroom (available, end_time, start_time, created_at, manager_id, renter_id, updated_at, attachment, location, name, status)
values
    (true, '18:00:00', '09:00:00', now(), 1, null, now(), null, '101', '101호', 'AVAILABLE'),
    (true, '18:00:00', '09:00:00', now(), 1, null, now(), null, '102', '102호', 'AVAILABLE'),
    (true, '18:00:00', '09:00:00', now(), 1, null, now(), null, '103', '103호', 'AVAILABLE'),
    (true, '18:00:00', '09:00:00', now(), 1, null, now(), null, '105', '105호', 'AVAILABLE'),
    (true, '18:00:00', '09:00:00', now(), 1, null, now(), null, '201', '201호', 'AVAILABLE');

-- 교수 INSERT
insert into public.professor (created_at, updated_at, description, name)
values
    (now(), now(), '학과장', '최일묵'),
    (now(), now(), '', '이상봉'),
    (now(), now(), '', '박규준'),
    (now(), now(), '', '오강선'),
    (now(), now(), '', '송현선'),
    (now(), now(), '', '이정한');

-- 장비 카테고리
insert into public.equipment_category (max_rental_count, english_code, name)
values  (10, 'CAMERA', '카메라'),
        (5, 'MIC', '마이크'),
        (7, 'TRIPOD', '삼각대'),
        (3, 'LIGHT', '조명');

-- 장비 모델명
insert into public.equipment_model (available, category_id, english_code, name)
values  (true, 1, 'CANNON-EOS-R6', '캐논 EOS R6'),
        (true, 2, 'SHURE-SM7B', '슈어 SM7B'),
        (true, 3, 'MANFROTTO-190X', '만프로토 190X'),
        (false, 4, 'APUTURE-AL-M9', '아푸처 AL-M9');

-- 공지사항
 insert into notice (title, content, important, attachment_url, author_id, created_at, updated_at)
       values
          ('시스템 점검 안내',
           '안녕하세요. 서버 시스템 점검으로 인해 2025년 5월 25일 오전 2시~4시까지 서비스 이용이 일시 중단됩니다. 이용에 불편을 드려 죄송합니다.',
           true, null, 3, '2025-05-20 09:00:00', '2025-05-20 09:00:00'),
          ('신규 기능 업데이트 소식',
           '안녕하세요! 많은 분들이 요청해주신 댓글 기능이 추가되었습니다. 이제 게시글에 댓글을 작성하고 다른 사용자들과 소통할 수 있습니다. 많은 이용 부탁드립니다.',
           false, 'https://example.com/update-guide.pdf', 2, '2025-05-18 14:30:00', '2025-05-18 14:30:00'),
          ('개인정보 처리방침 변경 안내',
           '개인정보보호법 개정에 따라 개인정보 처리방침이 일부 변경되었습니다. 주요 변경사항을 확인하시고 동의 절차를 진행해주시기 바랍니다. 미동의 시 서비스 이용에 제한이 있을 수 있습니다.',
           true, 'https://example.com/privacy-policy.pdf', 1, '2025-05-15 11:00:00', '2025-05-15 11:00:00'),
          ('5월 정기 점검 및 이벤트 안내',
           '5월 정기 점검이 예정되어 있습니다. 점검 기간: 2025년 5월 30일 오전 1시~3시. 또한 점검 완료 후 감사 이벤트로 포인트 지급 이벤트를 진행할 예정이니 많은 참여 바랍니다.',
           false, null, 3, '2025-05-22 16:45:00', '2025-05-22 16:45:00');