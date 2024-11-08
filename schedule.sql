-- schedule 테이블
CREATE TABLE schedules (
    id INT,
    todo STRING,
    writer STRING,
    password STRING,
    createdAt DATETIME,
    updatedAt DATETIME

);

-- 전체 일정 조회 query
SELECT id, todo, writer, createdAt, updatedAt
FROM schedules;

-- 선택 일정 조회 query
SELECT id, todo, writer, createdAt, updatedAt
FROM schedules
WHERE id = 1;

-- 일정 생성 query
INSERT INTO schedules (id, todo, writer, createdAt)
VALUES (1, '장보기', '김르탄','1234','2024-10-31 19:51:00');

-- 일정 수정 query
UPDATE schedules
SET todo = '식사하기'
WHERE id = 1;

-- 일정 삭제 query
DELETE FROM schedules
WHERE id = 1;


