-- 아래 계정들의 비밀번호는 asdf1234! 임

insert into users (birth_date, damage_count, grade, rental_count, report_count, restriction_count, created_at,
                          professor_id, updated_at, department, email, gender, "group", major, name, nickname,
                          password, phone_number, profile_picture, role, student_number)
values  ('2001-03-15', 0, 3, 5, 2, 1,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'hong@example.com', 'M', 'A조', '세부전공',
         '조경준', '조교', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-1234-5678', 'profile1.jpg', 'ROLE_SUPER_ADMIN', '202300003'),
        ('2002-06-22', 1, 2, 2, 1, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'kim@example.com', 'F', 'B조', '세부전공',
         '김영희', '김김', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-2345-6789', null, 'ROLE_SUPER_ADMIN', '20230002'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', 'M', 'A조', '세부전공',
         '박철수', '박박', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-3456-7890', 'profile3.png', 'ROLE_SUPER_ADMIN', '202300004'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', 'M', 'A조', '세부전공',
         '김나은', '나나', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-3456-7890', 'profile3.png', 'ROLE_USER', '202300005'),
        ('2001-07-20', 0, 1, 1, 0, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '컴퓨터공학과', 'lee@example.com', 'F', 'C조', '인공지능',
         '이서연', '서연이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-4567-8901', 'profile4.jpg', 'ROLE_USER', '202300006'),
        ('1999-09-09', 2, 3, 3, 1, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '전자공학과', 'choi@example.com', 'M', 'D조', '반도체',
         '최민수', '민수', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-5678-9012', 'profile5.png', 'ROLE_USER', '202300007'),
        ('2003-02-28', 0, 1, 0, 0, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '기계공학과', 'jang@example.com', 'F', 'E조', '자동차',
         '장지우', '지우지우', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-6789-0123', 'profile6.jpg', 'ROLE_USER', '202300008'),
        ('2002-11-11', 1, 2, 4, 0, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '건축학과', 'kang@example.com', 'M', 'F조', '도시계획',
         '강한결', '한결이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '010-7890-1234', 'profile7.png', 'ROLE_USER', '202300009'),
        ('2001-03-15', 1, 1, 2, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '컴퓨터공학과', 'lee@example.com', 'F', 'A조', 'AI',
            '이미영', '미영미영', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-1234-5678', 'profile8.jpg', 'ROLE_USER', '202300010'),
        ('2000-07-22', 2, 3, 1, 1, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '전자공학과', 'park@example.com', 'M', 'B조', '로봇',
            '박지훈', '지훈이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-2345-6789', 'profile9.png', 'ROLE_USER', '202300011'),
        ('2002-01-10', 0, 0, 0, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '산업디자인과', 'yoon@example.com', 'F', 'C조', 'UX디자인',
            '윤서연', '서연서연', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-3456-7890', 'profile10.jpg', 'ROLE_USER', '202300012'),
        ('1998-12-05', 3, 2, 3, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '화학공학과', 'seo@example.com', 'M', 'D조', '신소재',
            '서강민', '강민이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-4567-8901', 'profile11.png', 'ROLE_USER', '202300013'),
        ('2003-06-19', 0, 1, 1, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '물리학과', 'han@example.com', 'F', 'E조', '양자역학',
            '한소영', '소영이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-5678-9012', 'profile12.jpg', 'ROLE_USER', '202300014'),
        ('2001-09-01', 2, 2, 2, 1, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '경영학과', 'kim@example.com', 'M', 'A조', '회계',
            '김태훈', '태훈이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-6789-0123', 'profile13.png', 'ROLE_USER', '202300015'),
        ('1999-10-30', 1, 0, 0, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '경제학과', 'cho@example.com', 'F', 'B조', '금융',
            '조수빈', '수빈이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-7890-1234', 'profile14.jpg', 'ROLE_USER', '202300016'),
        ('2000-04-25', 3, 2, 1, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '생명과학과', 'jung@example.com', 'M', 'C조', '분자생물학',
            '정우성', '우성이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-8901-2345', 'profile15.png', 'ROLE_USER', '202300017'),
        ('2004-08-08', 0, 0, 2, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '문헌정보학과', 'oh@example.com', 'F', 'D조', '기록관리',
            '오지현', '지현지현', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-9012-3456', 'profile16.jpg', 'ROLE_USER', '202300018'),
        ('2002-02-14', 1, 3, 3, 1, 1,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '소프트웨어학과', 'bae@example.com', 'M', 'E조', '백엔드',
            '배진수', '진수배', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '010-0123-4567', 'profile17.png', 'ROLE_USER', '202300019');

-- 강의실 INSERT
insert into public.classroom (end_time, start_time, created_at, manager_id, renter_id, updated_at, attachment, location, name, status)
values
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '101', '101호', 'AVAILABLE'),
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '102', '102호', 'AVAILABLE'),
    ('18:00:00', '09:00:00', now(), 1, 1, now(), null, '103', '103호', 'RENTAL_PENDING'),
    ('18:00:00', '09:00:00', now(), 1, 1, now(), null, '105', '105호', 'RENTAL_PENDING'),
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '201', '201호', 'AVAILABLE');

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

-- 장비
insert into public.equipment (broken_count, category_id, created_at, end_rent_date, manager_id, model_id, rental_count, renter_semester_schdule_id, renter_user_id, repair_count, start_rent_date, updated_at, description, image_url, restriction_grade, serial_number, status)
values  (0, 1, '2025-05-28 17:44:19.441547', null, 10, 1, 0, null, null, 0, null, '2025-05-28 17:44:19.441547', '최신 맥북 프로 16인치', 'images/macbook_pro.jpg', '3', 'CAMCAN250501', 'AVAILABLE'),
        (0, 1, '2025-05-28 17:44:19.504248', null, 10, 1, 0, null, null, 0, null, '2025-05-28 17:44:19.504248', '최신 맥북 프로 16인치', 'images/macbook_pro.jpg', '3', 'CAMCAN250502', 'AVAILABLE'),
        (0, 1, '2025-05-28 17:44:19.513947', null, 10, 1, 0, null, null, 0, null, '2025-05-28 17:44:19.513947', '최신 맥북 프로 16인치', 'images/macbook_pro.jpg', '3', 'CAMCAN250503', 'AVAILABLE'),
        (0, 1, '2025-05-28 17:44:19.520992', null, 10, 1, 0, null, null, 0, null, '2025-05-28 17:44:19.520992', '최신 맥북 프로 16인치', 'images/macbook_pro.jpg', '3', 'CAMCAN250504', 'AVAILABLE'),
        (0, 1, '2025-05-28 17:44:19.526169', null, 10, 1, 0, null, null, 0, null, '2025-05-28 17:44:19.526169', '최신 맥북 프로 16인치', 'images/macbook_pro.jpg', '3', 'CAMCAN250505', 'AVAILABLE');

-- 공지사항
 insert into public.notice (view, title, content, important, attachment_url, author_id, created_at, updated_at)
       values
          (0,'시스템 점검 안내',
           '안녕하세요. 서버 시스템 점검으로 인해 2025년 5월 25일 오전 2시~4시까지 서비스 이용이 일시 중단됩니다. 이용에 불편을 드려 죄송합니다.',
           true, null, 3, '2025-05-20 09:00:00', '2025-05-20 09:00:00'),
          (0,'신규 기능 업데이트 소식',
           '안녕하세요! 많은 분들이 요청해주신 댓글 기능이 추가되었습니다. 이제 게시글에 댓글을 작성하고 다른 사용자들과 소통할 수 있습니다. 많은 이용 부탁드립니다.',
           false, 'https://example.com/update-guide.pdf', 2, '2025-05-18 14:30:00', '2025-05-18 14:30:00'),
          (0,'개인정보 처리방침 변경 안내',
           '개인정보보호법 개정에 따라 개인정보 처리방침이 일부 변경되었습니다. 주요 변경사항을 확인하시고 동의 절차를 진행해주시기 바랍니다. 미동의 시 서비스 이용에 제한이 있을 수 있습니다.',
           true, 'https://example.com/privacy-policy.pdf', 1, '2025-05-15 11:00:00', '2025-05-15 11:00:00'),
          (0,'5월 정기 점검 및 이벤트 안내',
           '5월 정기 점검이 예정되어 있습니다. 점검 기간: 2025년 5월 30일 오전 1시~3시. 또한 점검 완료 후 감사 이벤트로 포인트 지급 이벤트를 진행할 예정이니 많은 참여 바랍니다.',
           false, null, 3, '2025-05-22 16:45:00', '2025-05-22 16:45:00');

-- 연간 일정
insert into public.year_schedule (date, end_at, is_holiday, start_at, classroom_id, created_at, updated_at, description)
values  ('2025-05-26', null, true, null, null, '2025-05-26 10:52:46.130416', '2025-05-26 10:52:46.130416', 'string'),
        ('2025-05-26', '14:00:00', false, '13:00:00', 1, '2025-05-26 10:53:50.759813', '2025-05-26 10:53:50.759813', 'string');

-- 학기
insert into public.semester (end_date, start_date, year, created_at, updated_at, name)
values  ('2025-06-26', '2025-02-26', 2025, '2025-05-26 11:08:49.943975', '2025-05-26 11:08:49.943975', '1학기');

-- 수업 시간표
insert into public.semester_schedule (day, end_at, start_at, year, classroom_id, created_at, professor_id, semester_id, updated_at, name, color)
values  (3, '14:00:00', '13:00:00', null, 2, '2025-05-28 18:28:36.555480', 1, 1, '2025-05-28 18:28:36.555480', 'string', '#112233'),
        (5, '14:00:00', '13:00:00', null, 1, '2025-05-28 18:28:00.099899', 1, 1, '2025-05-28 18:28:00.099899', 'string', '#998877');

-- 게시판 카테고리
insert into public.board_category (created_at, updated_at, description, name)
values
    (now(), now(), '자유로운 대화가 가능합니다.', '자유게시판'),
    (now(), now(), '사용자끼리 자유로운 질문 답변이 가능합니다.', '질문게시판'),
    (now(), now(), '관리자 공지사항을 안내합니다.', '공지사항');

-- 게시판
insert into public.board (author_id, board_category_id, created_at, updated_at, attachment_url, title, content)
values
    (1, 1, now(), now(), null, '제목1', '내용1'),
    (3, 2, now(), now(), null, '제목2', '내용2'),
    (2, 1, now(), now(), null, '이번 주말에 뭐하세요?', '같이 농구할 사람 모집합니다.'),
    (4, 2, now(), now(), null, 'MySQL과 PostgreSQL 차이점?', '두 DBMS의 차이를 정리해주실 수 있나요?'),
    (1, 3, now(), now(), null, '서비스 점검 안내', '이번 주 토요일 오전 2시부터 4시까지 점검이 있을 예정입니다.');

-- 문의
insert into public.inquiry (id, author_id, created_at, updated_at, attachment_url, status, title, type, content)
values
    (1, 1, now(), now(), null, 'WAITING', '문의1', 'RENTAL', '문의내용1'),
    (2, 3, now(), now(), null, 'ANSWERED', '문의2', 'RETURN', '문의내용2'),
    (3, 3, now(), now(), null, 'WAITING', '문의3', 'ETC', '문의내용3'),
    (4, 2, now(), now(), null, 'WAITING', '로그인이 안돼요', 'SYSTEM_ERROR', '로그인 시도 시 계속 실패합니다. 원인을 모르겠어요.'),
    (5, 4, now(), now(), null, 'WAITING', '비밀번호 변경 문의', 'EQUIPMENT_DAMAGE', '비밀번호 변경 이메일이 오지 않습니다.'),
    (6, 1, now(), now(), null, 'WAITING', '기능 제안', 'PENALTY', '장비 대여 시 반납 알림 기능이 있었으면 좋겠습니다.');

-- 문의 답변
insert into public.inquiry_answer (created_at, inquiry_id, responder_id, updated_at, content)
values
    (now(), 2, 2, now(), '문의2에 대한 답변'),
    (now(), 5, 3, now(), '기능 제안에 대한 답변 감사합니다. 검토해보겠습니다.'),
    (now(), 3, 1, now(), '로그인 문제는 현재 해결되었습니다. 다시 시도해 주세요.'),
    (now(), 4, 2, now(), '비밀번호 변경 메일이 발송되지 않는 문제는 현재 확인 중입니다.');

-- 유저 4명 (id : 5,6,7,8) 제재 시키기
insert into rental_restriction (created_at, end_at, updated_at, user_id, reason, type)
values
    (now(), now() + (random() * interval '5 days'), now(), 5, 'OVERDUE', 'EQUIPMENT'),
    (now(), now() + (random() * interval '5 days'), now(), 6, 'DAMAGED', 'EQUIPMENT'),
    (now(), now() + (random() * interval '5 days'), now(), 7, 'DAMAGED', 'CLASSROOM'),
    (now(), now() + (random() * interval '5 days'), now(), 8, 'LOST', 'CLASSROOM');
