-- 아래 계정들의 비밀번호는 asdf1234! 임

insert into users (birth_date, broken_count, grade, rental_count, report_count, restriction_count, created_at,
                          professor_id, updated_at, department, email, gender, "group", major, name, nickname,
                          password, phone_number, profile_picture, role, student_number)
values  ('2001-03-15', 0, 3, 5, 2, 1,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'hong@example.com', 'M', 'A조', '세부전공',
         '조경준', '조교', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01012345678', 'profile1.jpg', 'ROLE_SUPER_ADMIN', '202300003'),
        ('2002-06-22', 1, 2, 2, 1, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'kim@example.com', 'F', 'B조', '세부전공',
         '김영희', '김김', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01023456789', null, 'ROLE_SUPER_ADMIN', '20230002'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', 'M', 'A조', '세부전공',
         '박철수', '박박', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01034567890', 'profile3.png', 'ROLE_SUPER_ADMIN', '202300004'),
        ('2000-12-05', 3, 4, 10, 5, 2,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '학과', 'park@example.com', 'M', 'A조', '세부전공',
         '김나은', '나나', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01034567890', 'profile3.png', 'ROLE_USER', '202300005'),
        ('2001-07-20', 0, 1, 1, 0, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '컴퓨터공학과', 'lee@example.com', 'F', 'C조', '인공지능',
         '이서연', '서연이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01045678901', 'profile4.jpg', 'ROLE_USER', '202300006'),
        ('1999-09-09', 2, 3, 3, 1, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '전자공학과', 'choi@example.com', 'M', 'D조', '반도체',
         '최민수', '민수', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01056789012', 'profile5.png', 'ROLE_USER', '202300007'),
        ('2003-02-28', 0, 1, 0, 0, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '기계공학과', 'jang@example.com', 'F', 'E조', '자동차',
         '장지우', '지우지우', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01067890123', 'profile6.jpg', 'ROLE_USER', '202300008'),
        ('2002-11-11', 1, 2, 4, 0, 0,
         '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
         '건축학과', 'kang@example.com', 'M', 'F조', '도시계획',
         '강한결', '한결이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
         '01078901234', 'profile7.png', 'ROLE_USER', '202300009'),
        ('2001-03-15', 1, 1, 2, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '컴퓨터공학과', 'lee@example.com', 'F', 'A조', 'AI',
            '이미영', '미영미영', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01012345678', 'profile8.jpg', 'ROLE_USER', '202300010'),
        ('2000-07-22', 2, 3, 1, 1, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '전자공학과', 'park@example.com', 'M', 'B조', '로봇',
            '박지훈', '지훈이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01023456789', 'profile9.png', 'ROLE_USER', '202300011'),
        ('2002-01-10', 0, 0, 0, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '산업디자인과', 'yoon@example.com', 'F', 'C조', 'UX디자인',
            '윤서연', '서연서연', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01034567890', 'profile10.jpg', 'ROLE_USER', '202300012'),
        ('1998-12-05', 3, 2, 3, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '화학공학과', 'seo@example.com', 'M', 'D조', '신소재',
            '서강민', '강민이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01045678901', 'profile11.png', 'ROLE_USER', '202300013'),
        ('2003-06-19', 0, 1, 1, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '물리학과', 'han@example.com', 'F', 'E조', '양자역학',
            '한소영', '소영이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01056789012', 'profile12.jpg', 'ROLE_USER', '202300014'),
        ('2001-09-01', 2, 2, 2, 1, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '경영학과', 'kim@example.com', 'M', 'A조', '회계',
            '김태훈', '태훈이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01067890123', 'profile13.png', 'ROLE_USER', '202300015'),
        ('1999-10-30', 1, 0, 0, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '경제학과', 'cho@example.com', 'F', 'B조', '금융',
            '조수빈', '수빈이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01078901234', 'profile14.jpg', 'ROLE_USER', '202300016'),
        ('2000-04-25', 3, 2, 1, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '생명과학과', 'jung@example.com', 'M', 'C조', '분자생물학',
            '정우성', '우성이', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01089012345', 'profile15.png', 'ROLE_USER', '202300017'),
        ('2004-08-08', 0, 0, 2, 0, 0,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '문헌정보학과', 'oh@example.com', 'F', 'D조', '기록관리',
            '오지현', '지현지현', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01090123456', 'profile16.jpg', 'ROLE_USER', '202300018'),
        ('2002-02-14', 1, 3, 3, 1, 1,
            '2025-05-16 13:40:26.060319', 1, '2025-05-16 13:40:26.060319',
            '소프트웨어학과', 'bae@example.com', 'M', 'E조', '백엔드',
            '배진수', '진수배', '$2a$10$2/ZUDynpz0BDpGUt/FLJ.OumPW6INKT.Hotj65QofSY5896MQmzW2',
            '01001234567', 'profile17.png', 'ROLE_USER', '202300019');

-- 강의실 INSERT
insert into public.classroom (end_time, start_time, created_at, manager_id, renter_id, updated_at, attachment, location, name, status, requested_at, start_rent_time, end_rent_time)
values
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '101', '101호', 'AVAILABLE', null, null, null),
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '102', '102호', 'AVAILABLE', null, null, null),
    ('18:00:00', '09:00:00', now(), 1, 1, now(), null, '103', '103호', 'RENTAL_PENDING', date_trunc('day', now()) + interval '11 hours', '13:00:00', '15:00:00'),
    ('18:00:00', '09:00:00', now(), 1, 1, now(), null, '105', '105호', 'RENTAL_PENDING', date_trunc('day', now()) + interval '10 hours', '14:00:00', '16:00:00'),
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '201', '201호', 'AVAILABLE', null, null, null),
    ('18:00:00', '09:00:00', now(), 1, 1, now(), null, '202', '202호', 'IN_USE', date_trunc('day', now()) + interval '9 hours', '15:00:00', '18:00:00'),
    ('18:00:00', '09:00:00', now(), 1, null, now(), null, '203', '203호', 'BROKEN', null, null, null);

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
        (0, 1, '2025-05-28 17:44:19.526169', null, 10, 1, 0, null, null, 0, null, '2025-05-28 17:44:19.526169', '최신 맥북 프로 16인치', 'images/macbook_pro.jpg', '3', 'CAMCAN250505', 'AVAILABLE'),
        (0, 1, '2025-06-04 10:00:00', '2025-06-10 10:00:00', 10, 1, 0, null, 3, 0, '2025-06-05 10:00:00', '2025-06-04 10:00:00', '대여 신청된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250506', 'RENTAL_PENDING'),
        (0, 1, '2025-06-04 10:01:00', '2025-06-10 10:01:00', 10, 1, 0, null, 3, 0, '2025-06-05 10:01:00', '2025-06-04 10:01:00', '대여 신청된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250507', 'RENTAL_PENDING'),
        (0, 1, '2025-06-04 10:02:00', '2025-06-10 10:02:00', 10, 1, 0, null, 3, 0, '2025-06-05 10:02:00', '2025-06-04 10:02:00', '대여 신청된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250508', 'RENTAL_PENDING'),
        (0, 1, '2025-06-04 10:03:00', '2025-06-10 10:03:00', 10, 1, 0, null, 3, 0, '2025-06-05 10:03:00', '2025-06-04 10:03:00', '대여 신청된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250509', 'RENTAL_PENDING'),
        (0, 1, '2025-06-04 10:04:00', '2025-06-10 10:04:00', 10, 1, 0, null, 3, 0, '2025-06-05 10:04:00', '2025-06-04 10:04:00', '대여 신청된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250510', 'RENTAL_PENDING'),
        (0, 1, '2025-06-05 10:10:00', '2025-06-11 10:10:00', 10, 1, 1, null, 3, 0, '2025-06-05 10:10:00', '2025-06-05 10:10:00', '사용 중인 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250511', 'IN_USE'),
        (0, 1, '2025-06-05 10:11:00', '2025-06-11 10:11:00', 10, 1, 2, null, 3, 0, '2025-06-05 10:11:00', '2025-06-05 10:11:00', '사용 중인 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250512', 'IN_USE'),
        (0, 1, '2025-06-05 10:12:00', '2025-06-11 10:12:00', 10, 1, 3, null, 3, 0, '2025-06-05 10:12:00', '2025-06-05 10:12:00', '사용 중인 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250513', 'IN_USE'),
        (1, 1, '2025-06-05 10:20:00', null, 10, 1, 5, null, null, 0, null, '2025-06-05 10:20:00', '파손된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250514', 'BROKEN'),
        (2, 1, '2025-06-05 10:21:00', null, 10, 1, 6, null, null, 1, null, '2025-06-05 10:21:00', '파손된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250515', 'BROKEN'),
        (1, 1, '2025-06-05 10:22:00', null, 10, 1, 7, null, null, 0, null, '2025-06-05 10:22:00', '파손된 맥북 프로', 'images/macbook_pro.jpg', '3', 'CAMCAN250516', 'BROKEN');
-- 공지사항
insert into public.notice (view, title, content, important, attachment_url, author_id, created_at, updated_at)
values  (0,'시스템 점검 안내',
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
values  ('2025-05-26', null, true, null, null, '2025-05-26 10:52:46.130416', '2025-05-26 10:52:46.130416', '첫번째 연간 일정(휴일)'),
        ('2025-05-27', '14:00:00', false, '13:00:00', 1, '2025-05-26 10:53:50.759813', '2025-05-26 10:53:50.759813', '두번째 연간 일정(특강)'),
        ('2025-06-10', null, true, null, null, '2025-05-26 10:52:46.130416', '2025-05-26 10:52:46.130416', '세번째 연간 일정(휴일)'),
        ('2025-06-11', '14:00:00', false, '13:00:00', 1, '2025-05-26 10:53:50.759813', '2025-05-26 10:53:50.759813', '네번째 연간 일정(특강)');

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
INSERT INTO public.community (author_id, board_category_id, created_at, updated_at, recommend,
    view, type, type_id, title, nickname, content
)
VALUES
    (1, 1, now(), now(), 0, 0, 'general', null, '제목1', '닉네임1', '내용1'),
    (3, 2, now(), now(), 0, 0, 'general', null, '제목2', '닉네임2', '내용2'),
    (2, 1, now(), now(), 0, 0, 'general', null, '이번 주말에 뭐하세요?', '농구좋아', '같이 농구할 사람 모집합니다.'),
    (4, 2, now(), now(), 0, 0, 'question', null, 'MySQL과 PostgreSQL 차이점?', '데이터베이스마스터', '두 DBMS의 차이를 정리해주실 수 있나요?'),
    (1, 3, now(), now(), 0, 0, 'notice', null, '서비스 점검 안내', '관리자', '이번 주 토요일 오전 2시부터 4시까지 점검이 있을 예정입니다.');

-- 문의
insert into public.inquiry (id, author_id, created_at, updated_at, attachment_url, status, title, type, content)
values
    (1, 1, now(), now(), null, 'NOT_ANSWERED', '문의1', 'RENTAL', '문의내용1'),
    (2, 3, now(), now(), null, 'ANSWERED', '문의2', 'RETURN', '문의내용2'),
    (3, 3, now(), now(), null, 'NOT_ANSWERED', '문의3', 'ETC', '문의내용3'),
    (4, 2, now(), now(), null, 'NOT_ANSWERED', '로그인이 안돼요', 'SYSTEM_ERROR', '로그인 시도 시 계속 실패합니다. 원인을 모르겠어요.'),
    (5, 4, now(), now(), null, 'NOT_ANSWERED', '비밀번호 변경 문의', 'EQUIPMENT_BROKEN', '비밀번호 변경 이메일이 오지 않습니다.'),
    (6, 1, now(), now(), null, 'NOT_ANSWERED', '기능 제안', 'PENALTY', '장비 대여 시 반납 알림 기능이 있었으면 좋겠습니다.');

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
    (now(), now() + (random() * interval '5 days'), now(), 6, 'BROKEN', 'EQUIPMENT'),
    (now(), now() + (random() * interval '5 days'), now(), 7, 'BROKEN', 'CLASSROOM'),
    (now(), now() + (random() * interval '5 days'), now(), 8, 'LOST', 'CLASSROOM');


--파손 수리 히스토리
INSERT INTO broken_repair_history (
    target_type, history_type, equipment_id, classroom_id, detail,
    broken_type, broken_by_id, broken_by_name, related_broken_id,
    created_at, updated_at
)
VALUES
-- 장비 파손 1~5
('EQUIPMENT', 'BROKEN', 1, null, '장비 파손 테스트 1', 'ADMIN_BROKEN', 1, '관리자1', null, '2025-04-01', '2025-04-01'),
('EQUIPMENT', 'BROKEN', 2, null, '장비 파손 테스트 2', 'RETURN_BROKEN', 2, '사용자2', null, '2025-04-05', '2025-04-05'),
('EQUIPMENT', 'BROKEN', 3, null, '장비 파손 테스트 3', 'ADMIN_BROKEN', 1, '관리자1', null, '2025-04-10', '2025-04-10'),
('EQUIPMENT', 'BROKEN', 4, null, '장비 파손 테스트 4', 'RETURN_BROKEN', 2, '사용자2', null, '2025-04-15', '2025-04-15'),
('EQUIPMENT', 'BROKEN', 5, null, '장비 파손 테스트 5', 'ADMIN_BROKEN', 1, '관리자1', null, '2025-04-20', '2025-04-20'),

-- 장비 수리 6~10 (related to 위의 ID 1~5)
('EQUIPMENT', 'REPAIR', 1, null, '장비 수리 테스트 1', null, 2, '사용자2', 1, '2025-04-25', '2025-04-25'),
('EQUIPMENT', 'REPAIR', 2, null, '장비 수리 테스트 2', null, 1, '관리자1', 2, '2025-04-27', '2025-04-27'),
('EQUIPMENT', 'REPAIR', 3, null, '장비 수리 테스트 3', null, 2, '사용자2', 3, '2025-05-01', '2025-05-01'),
('EQUIPMENT', 'REPAIR', 4, null, '장비 수리 테스트 4', null, 1, '관리자1', 4, '2025-05-05', '2025-05-05'),
('EQUIPMENT', 'REPAIR', 5, null, '장비 수리 테스트 5', null, 2, '사용자2', 5, '2025-05-10', '2025-05-10'),

-- 강의실 파손 11~15
('CLASSROOM', 'BROKEN', null, 1, '강의실 파손 테스트 1', 'ADMIN_BROKEN', 1, '관리자1', null, '2025-04-03', '2025-04-03'),
('CLASSROOM', 'BROKEN', null, 2, '강의실 파손 테스트 2', 'RETURN_BROKEN', 2, '사용자2', null, '2025-04-07', '2025-04-07'),
('CLASSROOM', 'BROKEN', null, 3, '강의실 파손 테스트 3', 'ADMIN_BROKEN', 1, '관리자1', null, '2025-04-12', '2025-04-12'),
('CLASSROOM', 'BROKEN', null, 4, '강의실 파손 테스트 4', 'RETURN_BROKEN', 2, '사용자2', null, '2025-04-17', '2025-04-17'),
('CLASSROOM', 'BROKEN', null, 5, '강의실 파손 테스트 5', 'ADMIN_BROKEN', 1, '관리자1', null, '2025-04-22', '2025-04-22'),

-- 강의실 수리 16~20 (related to ID 11~15)
('CLASSROOM', 'REPAIR', null, 1, '강의실 수리 테스트 1', null, 2, '사용자2', 11, '2025-04-26', '2025-04-26'),
('CLASSROOM', 'REPAIR', null, 2, '강의실 수리 테스트 2', null, 1, '관리자1', 12, '2025-04-28', '2025-04-28'),
('CLASSROOM', 'REPAIR', null, 3, '강의실 수리 테스트 3', null, 2, '사용자2', 13, '2025-05-02', '2025-05-02'),
('CLASSROOM', 'REPAIR', null, 4, '강의실 수리 테스트 4', null, 1, '관리자1', 14, '2025-05-06', '2025-05-06'),
('CLASSROOM', 'REPAIR', null, 5, '강의실 수리 테스트 5', null, 2, '사용자2', 15, '2025-05-11', '2025-05-11');

--장비 장바구니
INSERT INTO equipment_carts (user_id, equipment_id, created_at, updated_at)
VALUES
-- user_id = 1
(1, 1, '2025-05-01 10:00:00', '2025-05-01 10:00:00'),
(1, 2, '2025-05-01 10:05:00', '2025-05-01 10:05:00'),
(1, 3, '2025-05-01 10:10:00', '2025-05-01 10:10:00'),

-- user_id = 2
(2, 1, '2025-05-01 11:00:00', '2025-05-01 11:00:00'),
(2, 2, '2025-05-01 11:05:00', '2025-05-01 11:05:00'),
(2, 3, '2025-05-01 11:10:00', '2025-05-01 11:10:00'),

-- user_id = 3
(3, 1, '2025-05-01 12:00:00', '2025-05-01 12:00:00'),
(3, 2, '2025-05-01 12:05:00', '2025-05-01 12:05:00'),
(3, 3, '2025-05-01 12:10:00', '2025-05-01 12:10:00'),

-- user_id = 4
(4, 1, '2025-05-02 10:00:00', '2025-05-02 10:00:00'),
(4, 2, '2025-05-02 10:05:00', '2025-05-02 10:05:00'),
(4, 3, '2025-05-02 10:10:00', '2025-05-02 10:10:00'),

-- user_id = 5
(5, 1, '2025-05-02 11:00:00', '2025-05-02 11:00:00'),
(5, 2, '2025-05-02 11:05:00', '2025-05-02 11:05:00'),
(5, 3, '2025-05-02 11:10:00', '2025-05-02 11:10:00'),

-- user_id = 6
(6, 1, '2025-05-02 12:00:00', '2025-05-02 12:00:00'),
(6, 2, '2025-05-02 12:05:00', '2025-05-02 12:05:00'),
(6, 3, '2025-05-02 12:10:00', '2025-05-02 12:10:00'),

-- user_id = 7
(7, 1, '2025-05-03 10:00:00', '2025-05-03 10:00:00'),
(7, 2, '2025-05-03 10:05:00', '2025-05-03 10:05:00'),
(7, 3, '2025-05-03 10:10:00', '2025-05-03 10:10:00'),

-- user_id = 8
(8, 1, '2025-05-03 11:00:00', '2025-05-03 11:00:00'),
(8, 2, '2025-05-03 11:05:00', '2025-05-03 11:05:00'),
(8, 3, '2025-05-03 11:10:00', '2025-05-03 11:10:00'),

-- user_id = 9
(9, 1, '2025-05-03 12:00:00', '2025-05-03 12:00:00'),
(9, 2, '2025-05-03 12:05:00', '2025-05-03 12:05:00'),
(9, 3, '2025-05-03 12:10:00', '2025-05-03 12:10:00'),

-- user_id = 10
(10, 1, '2025-05-04 10:00:00', '2025-05-04 10:00:00'),
(10, 2, '2025-05-04 10:05:00', '2025-05-04 10:05:00'),
(10, 3, '2025-05-04 10:10:00', '2025-05-04 10:10:00'),

-- user_id = 11
(11, 1, '2025-05-04 11:00:00', '2025-05-04 11:00:00'),
(11, 2, '2025-05-04 11:05:00', '2025-05-04 11:05:00'),
(11, 3, '2025-05-04 11:10:00', '2025-05-04 11:10:00'),

-- user_id = 12
(12, 1, '2025-05-04 12:00:00', '2025-05-04 12:00:00'),
(12, 2, '2025-05-04 12:05:00', '2025-05-04 12:05:00'),
(12, 3, '2025-05-04 12:10:00', '2025-05-04 12:10:00'),

-- user_id = 13
(13, 1, '2025-05-05 10:00:00', '2025-05-05 10:00:00'),
(13, 2, '2025-05-05 10:05:00', '2025-05-05 10:05:00'),
(13, 3, '2025-05-05 10:10:00', '2025-05-05 10:10:00'),

-- user_id = 14
(14, 1, '2025-05-05 11:00:00', '2025-05-05 11:00:00'),
(14, 2, '2025-05-05 11:05:00', '2025-05-05 11:05:00'),
(14, 3, '2025-05-05 11:10:00', '2025-05-05 11:10:00'),

-- user_id = 15
(15, 1, '2025-05-05 12:00:00', '2025-05-05 12:00:00'),
(15, 2, '2025-05-05 12:05:00', '2025-05-05 12:05:00'),
(15, 3, '2025-05-05 12:10:00', '2025-05-05 12:10:00');
