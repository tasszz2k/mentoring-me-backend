CREATE TABLE addresses
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    name        INT                                  NOT NULL,
    type        TINYINT                              NULL,
    ward_id     INT                                  NULL,
    district_id INT                                  NULL,
    province_id INT                                  NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (ward_id) REFERENCES addresses (id),
    CONSTRAINT
        FOREIGN KEY (district_id) REFERENCES addresses (id),
    CONSTRAINT
        FOREIGN KEY (province_id) REFERENCES addresses (id)
);

CREATE INDEX district_id
    ON addresses (district_id);

CREATE INDEX province_id
    ON addresses (province_id);

CREATE INDEX ward_id
    ON addresses (ward_id);

CREATE TABLE categories
(
    id                 INT AUTO_INCREMENT
        PRIMARY KEY,
    parent_category_id INT                                  NOT NULL,
    name               VARCHAR(50)                          NULL,
    code               VARCHAR(10)                          NULL,
    is_deleted         TINYINT(1) DEFAULT 0                 NOT NULL,
    created            DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified           DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (parent_category_id) REFERENCES categories (id)
);

CREATE TABLE roles
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    name       INT                                  NOT NULL,
    code       INT                                  NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE shifts
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    code       VARCHAR(50)                          NULL,
    day        TINYINT                              NULL,
    shift      TINYINT                              NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE user_profiles
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
#     full_name  VARCHAR(255)                         NULL,
    gender     TINYINT                              NULL,
    dob        DATE                                 NULL,
    rating     FLOAT                                NULL,
    address    VARCHAR(500)                         NULL,
    address_id INT                                  NULL,
    status     TINYINT(1)                           NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (address_id) REFERENCES addresses (id)
);

CREATE INDEX address_id
    ON user_profiles (address_id);

CREATE INDEX id
    ON user_profiles (id);

CREATE TABLE users
(
    id               INT AUTO_INCREMENT
        PRIMARY KEY,
    profile_id       INT                                  NOT NULL,
    full_name        VARCHAR(255)                         NOT NULL,
    password         VARCHAR(100)                         NULL,
    hash             VARCHAR(255)                         NULL,
    email            VARCHAR(100)                         NULL,
    phone_number     VARCHAR(15)                          NULL,
    image_url        VARCHAR(255)                         NULL,
    provider         VARCHAR(50)                          NULL,
    provider_user_id VARCHAR(255)                         NULL,
    status           TINYINT(1)                           NULL,
    enabled          TINYINT(1) DEFAULT 0                 NOT NULL,
    created          DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified         DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (profile_id) REFERENCES user_profiles (id)

);

CREATE TABLE classes
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    mentor_id   INT                                  NULL,
    category_id INT                                  NOT NULL,
    created_by  INT                                  NOT NULL,
    title       VARCHAR(255)                         NULL,
    start_date  DATETIME                             NULL,
    end_date    DATETIME                             NULL,
    duration    INT                                  NULL,
    type        TINYINT                              NULL,
    status      TINYINT                              NULL,
    price       FLOAT                                NULL,
    address     VARCHAR(500)                         NULL,
    address_id  INT                                  NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (address_id) REFERENCES addresses (id),
    CONSTRAINT
        FOREIGN KEY (mentor_id) REFERENCES users (id),
    CONSTRAINT
        FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT
        FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE INDEX address_id
    ON classes (address_id);

CREATE TABLE classes_shifts
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    course_id  INT                                  NOT NULL,
    shift_id   INT                                  NOT NULL,
    type       TINYINT                              NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (shift_id) REFERENCES shifts (id),
    CONSTRAINT
        FOREIGN KEY (course_id) REFERENCES classes (id)
);

CREATE TABLE feedback_users
(
    id           INT AUTO_INCREMENT
        PRIMARY KEY,
    from_user_id INT                                  NOT NULL,
    to_user_id   INT                                  NOT NULL,
    rating       INT                                  NULL,
    comment      TEXT                                 NULL,
    is_deleted   TINYINT(1) DEFAULT 0                 NOT NULL,
    created      DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (from_user_id) REFERENCES users (id),
    CONSTRAINT
        FOREIGN KEY (to_user_id) REFERENCES users (id)
);

CREATE TABLE login_histories
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    last_login DATETIME                             NULL,
    ip_address VARCHAR(20)                          NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX id
    ON login_histories (id);

CREATE INDEX user_id
    ON login_histories (user_id);

CREATE TABLE pending_users
(
    id               INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id          INT                                  NOT NULL,
    status           TINYINT(1)                           NULL,
    verified_date    DATETIME   DEFAULT CURRENT_TIMESTAMP NULL,
    verified_user_id INT                                  NULL,
    is_deleted       TINYINT(1) DEFAULT 0                 NULL,
    created          DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified         DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX id
    ON pending_users (id);

CREATE INDEX user_id
    ON pending_users (user_id);

CREATE TABLE quizzes
(
    id                 INT AUTO_INCREMENT
        PRIMARY KEY,
    title              VARCHAR(255)                         NULL,
    description        TEXT                                 NULL,
    number_of_question INT                                  NULL,
    visible_status     TINYINT                              NULL,
    editable_status    TINYINT                              NULL,
    type               TINYINT                              NULL,
    created_by         INT                                  NOT NULL,
    modified_by        INT                                  NOT NULL,
    is_deleted         TINYINT(1) DEFAULT 0                 NOT NULL,
    created            DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified           DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT
        FOREIGN KEY (modified_by) REFERENCES users (id)
);

CREATE TABLE favorite_quizzes
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    quiz_id    INT                                  NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE questions
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    quiz_id     INT                                  NOT NULL,
    question    TEXT                                 NULL,
    description TEXT                                 NULL,
    type        TINYINT                              NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id)
);

CREATE TABLE answers
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    question_id INT                                  NOT NULL,
    answer      VARCHAR(500)                         NULL,
    is_correct  TINYINT(1)                           NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (question_id) REFERENCES questions (id)
);

CREATE TABLE quiz_results
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    quiz_id    INT                                  NOT NULL,
    score      FLOAT                                NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE quizzes_categories
(
    quiz_id     INT                                  NOT NULL,
    category_id INT                                  NOT NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT
        FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE students_classes
(
    user_id     INT                                  NOT NULL,
    course_id   INT                                  NOT NULL,
    enroll_date DATETIME                             NULL,
    status      TINYINT                              NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT
        FOREIGN KEY (course_id) REFERENCES classes (id)
);

CREATE TABLE timetables
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    name       VARCHAR(255)                         NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE events
(
    id           INT AUTO_INCREMENT
        PRIMARY KEY,
    timetable_id INT                                  NOT NULL,
    title        VARCHAR(255)                         NULL,
    content      TEXT                                 NULL,
    from_time    DATETIME                             NULL,
    to_time      DATETIME                             NULL,
    type         TINYINT                              NULL,
    is_deleted   TINYINT(1) DEFAULT 0                 NOT NULL,
    created      DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT
        FOREIGN KEY (timetable_id) REFERENCES timetables (id)
);

CREATE INDEX id
    ON users (id);

CREATE TABLE users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT
        FOREIGN KEY (role_id) REFERENCES roles (id)
);