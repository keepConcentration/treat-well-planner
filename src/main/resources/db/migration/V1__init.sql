-- ============================
-- Create members Table
-- ============================
CREATE TABLE IF NOT EXISTS members (
id UUID PRIMARY KEY,                                -- UUID 고유 ID
email VARCHAR(255),                                 -- 이메일
name VARCHAR(100),                                  -- 이름
insert_id UUID NOT NULL,                            -- 생성자 ID
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 생성일시
update_id UUID NOT NULL,                            -- 수정자 ID
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  -- 수정일시
);

-- ============================
-- Create member_tokens Table
-- ============================
CREATE TABLE IF NOT EXISTS member_tokens (
member_id UUID NOT NULL PRIMARY KEY,
refresh_token TEXT NOT NULL,
expiration TIMESTAMP NOT NULL,
insert_id UUID NOT NULL,                            -- 생성자 ID
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 생성일시
update_id UUID NOT NULL,                            -- 수정자 ID
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  -- 수정일시
);

-- ============================
-- Create social_account Table
-- ============================
CREATE TABLE IF NOT EXISTS social_accounts (
id UUID NOT NULL PRIMARY KEY,        -- 고유 식별자 (UUID)
social_id VARCHAR(255) NOT NULL,    -- 소셜 서비스 제공자의 ID
provider VARCHAR(50) NOT NULL,      -- 소셜 제공사 (KAKAO, GOOGLE 등)
member_id UUID NOT NULL,            -- 회원 테이블의 외래 키 (member.id)
insert_id UUID NOT NULL,                            -- 생성자 ID
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 생성일시
update_id UUID NOT NULL,                            -- 수정자 ID
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,  -- 수정일시
CONSTRAINT unique_social_account UNIQUE (social_id, provider), -- 소셜 ID와 제공사 조합은 중복 불가능
CONSTRAINT fk_social_account_member FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE -- 회원 삭제 시 함께 삭제
);

-- ===========================
-- Create categories Table
-- ===========================
CREATE TABLE IF NOT EXISTS categories (
id UUID PRIMARY KEY,                                 -- UUID 고유 ID
name VARCHAR(50) NOT NULL UNIQUE,                   -- 카테고리 이름 (유니크)
deleted BOOLEAN DEFAULT FALSE NOT NULL,             -- 논리 삭제 여부 (기본값 FALSE)
insert_id UUID NOT NULL,                    -- 생성자 ID
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 생성일시
update_id UUID NOT NULL,                    -- 수정자 ID
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  -- 수정일시
);

-- =========================
-- Create tags Table
-- =========================
CREATE TABLE IF NOT EXISTS tags (
id UUID PRIMARY KEY,          -- UUID 고유 ID
name VARCHAR(50) NOT NULL UNIQUE, -- 태그 이름, 유니크
insert_id UUID NOT NULL, -- 생성자 ID
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 생성일시
update_id UUID NOT NULL, -- 수정자 ID
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL  -- 수정일시
);

-- =============================
-- Create recurrence_rule Table
-- =============================
CREATE TABLE IF NOT EXISTS recurrence_rule (
id UUID PRIMARY KEY,
recurrence_interval INTEGER NOT NULL,
rule_type VARCHAR(50) NOT NULL,
insert_id UUID NOT NULL,
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
update_id UUID NOT NULL,
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- ========================
-- Create plans Table
-- ========================
CREATE TABLE IF NOT EXISTS plans (
id UUID PRIMARY KEY,
title VARCHAR(100) NOT NULL,
description VARCHAR(500) NULL,
start_date DATE NULL,
end_date DATE NULL,
recurrence_rule_id UUID,
category_id UUID,
insert_id UUID NOT NULL,
insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
update_id UUID NOT NULL,
update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
CONSTRAINT fk_recurrence_rule FOREIGN KEY (recurrence_rule_id) REFERENCES recurrence_rule(id) ON DELETE CASCADE
);

-- ====================================
-- Create plan_tags (Many-to-Many Table)
-- ====================================
CREATE TABLE IF NOT EXISTS plan_tags (
plan_id UUID NOT NULL,        -- Plan ID 다대다 관계
tag_id UUID NOT NULL,         -- Tag ID 다대다 관계
PRIMARY KEY (plan_id, tag_id), -- 다대다 관계 Primary Key
CONSTRAINT fk_plan FOREIGN KEY (plan_id) REFERENCES plans(id) ON DELETE CASCADE,
CONSTRAINT fk_tag FOREIGN KEY (tag_id) REFERENCES tags(id) ON DELETE CASCADE
);

-- ==============================
-- Create recurrence_rule_days
-- ==============================
CREATE TABLE IF NOT EXISTS recurrence_rule_days (
recurrence_rule_id UUID NOT NULL,
day_of_week VARCHAR(10) NOT NULL, -- 요일 저장 (SUNDAY, MONDAY 등)
PRIMARY KEY (recurrence_rule_id, day_of_week),
CONSTRAINT fk_recurrence_rule_days FOREIGN KEY (recurrence_rule_id) REFERENCES recurrence_rule(id) ON DELETE CASCADE
);

-- =================================
-- Create recurrence_rule_days_of_month
-- =================================
CREATE TABLE IF NOT EXISTS recurrence_rule_days_of_month (
recurrence_rule_id UUID NOT NULL,
day_of_month INTEGER NOT NULL, -- 월간 날짜
PRIMARY KEY (recurrence_rule_id, day_of_month),
CONSTRAINT fk_recurrence_rule_days_of_month FOREIGN KEY (recurrence_rule_id) REFERENCES recurrence_rule(id) ON DELETE CASCADE
);

-- ==================================
-- Create recurrence_rule_months
-- ==================================
CREATE TABLE IF NOT EXISTS recurrence_rule_months (
recurrence_rule_id UUID NOT NULL,
month_of_year INTEGER NOT NULL, -- 연간 월 정보 (1 ~ 12)
PRIMARY KEY (recurrence_rule_id, month_of_year),
CONSTRAINT fk_recurrence_rule_months FOREIGN KEY (recurrence_rule_id) REFERENCES recurrence_rule(id) ON DELETE CASCADE
);

-- ==========================
-- Create Indexes for Plans
-- ==========================
CREATE INDEX IF NOT EXISTS idx_tags_name ON tags(name); -- 태그 이름 검색용
CREATE INDEX IF NOT EXISTS idx_plans_category ON plans(category_id); -- 플랜별 카테고리 인덱스
CREATE INDEX IF NOT EXISTS idx_plans_date_range ON plans(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_recurrence_rule_type ON recurrence_rule(rule_type);
CREATE INDEX IF NOT EXISTS idx_members_email ON members(email); -- 이메일 검색 속도 향상