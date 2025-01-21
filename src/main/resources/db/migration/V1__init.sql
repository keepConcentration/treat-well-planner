-- =============================
-- Create recurrence_rule Table
-- =============================
CREATE TABLE IF NOT EXISTS recurrence_rule (
  id UUID PRIMARY KEY,
  recurrence_interval INTEGER NOT NULL,
  rule_type VARCHAR(50) NOT NULL,
  insert_id VARCHAR(255) NOT NULL,
  insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  update_id VARCHAR(255) NOT NULL,
  update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- ========================
-- Create plans Table
-- ========================
CREATE TABLE IF NOT EXISTS plans (
  id UUID PRIMARY KEY,
  title VARCHAR(100) NOT NULL,
  description VARCHAR(500) NOT NULL,
  start_date DATE NULL,
  end_date DATE NULL,
  recurrence_rule_id UUID,
  insert_id VARCHAR(255) NOT NULL,
  insert_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  update_id VARCHAR(255) NOT NULL,
  update_date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  CONSTRAINT fk_recurrence_rule FOREIGN KEY (recurrence_rule_id) REFERENCES recurrence_rule(id) ON DELETE CASCADE
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
CREATE INDEX IF NOT EXISTS idx_plans_date_range ON plans(start_date, end_date);
CREATE INDEX IF NOT EXISTS idx_recurrence_rule_type ON recurrence_rule(rule_type);