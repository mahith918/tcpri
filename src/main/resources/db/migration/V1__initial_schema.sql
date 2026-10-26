CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(190) NOT NULL UNIQUE,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tasks (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(180) NOT NULL,
  description TEXT,
  due_date DATE,
  status VARCHAR(20) NOT NULL,
  assignee_id BIGINT NOT NULL,
  created_by_id BIGINT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_task_assignee FOREIGN KEY (assignee_id) REFERENCES users(id),
  CONSTRAINT fk_task_creator FOREIGN KEY (created_by_id) REFERENCES users(id)
);

CREATE TABLE social_posts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(180) NOT NULL,
  caption TEXT,
  platform VARCHAR(30) NOT NULL,
  planned_at DATETIME,
  status VARCHAR(30) NOT NULL,
  created_by_id BIGINT NOT NULL,
  approved_by_id BIGINT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_post_creator FOREIGN KEY (created_by_id) REFERENCES users(id),
  CONSTRAINT fk_post_approver FOREIGN KEY (approved_by_id) REFERENCES users(id)
);
