CREATE TABLE addresses
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    name        VARCHAR(500)                         NOT NULL,
    type        TINYINT                              NULL,
    ward_id     INT                                  NULL,
    district_id INT                                  NULL,
    province_id INT                                  NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT addresses_ibfk_1
        FOREIGN KEY (ward_id) REFERENCES addresses (id),
    CONSTRAINT addresses_ibfk_2
        FOREIGN KEY (district_id) REFERENCES addresses (id),
    CONSTRAINT addresses_ibfk_3
        FOREIGN KEY (province_id) REFERENCES addresses (id)
)
    AUTO_INCREMENT = 29072;

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
    parent_category_id INT                                  NULL,
    name               VARCHAR(150)                         NOT NULL,
    code               VARCHAR(50)                          NOT NULL,
    is_deleted         TINYINT(1) DEFAULT 0                 NOT NULL,
    created            DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified           DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT categories_code_uindex
        UNIQUE (code),
    CONSTRAINT categories_ibfk_1
        FOREIGN KEY (parent_category_id) REFERENCES categories (id)
)
    AUTO_INCREMENT = 114;

CREATE INDEX parent_category_id
    ON categories (parent_category_id);

CREATE TABLE cometchat_tokens
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    token      VARCHAR(500)                         NOT NULL,
    user_id    INT                                  NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL
)
    AUTO_INCREMENT = 66;

CREATE TABLE notifications
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    title       VARCHAR(500)                         NULL,
    body        TEXT                                 NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    object_type TINYINT(1)                           NULL,
    object_id   INT                                  NULL,
    CONSTRAINT notifications_id_uindex
        UNIQUE (id)
)
    AUTO_INCREMENT = 130;

CREATE TABLE roles
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    name       VARCHAR(50)                          NOT NULL,
    code       VARCHAR(50)                          NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT roles_code_uindex
        UNIQUE (code),
    CONSTRAINT roles_name_uindex
        UNIQUE (name)
)
    AUTO_INCREMENT = 5;

CREATE TABLE user_profiles
(
    id               INT AUTO_INCREMENT
        PRIMARY KEY,
    gender           TINYINT                              NULL,
    dob              DATE                                 NULL,
    rating           FLOAT                                NULL,
    detail_address   VARCHAR(500)                         NULL,
    address_id       INT                                  NULL,
    bio              TEXT                                 NULL,
    school           VARCHAR(500)                         NULL,
    price            FLOAT                                NULL,
    status           TINYINT(1)                           NULL,
    is_online_study  BIT        DEFAULT b'0'              NULL,
    is_offline_study BIT        DEFAULT b'0'              NULL,
    is_deleted       TINYINT(1) DEFAULT 0                 NOT NULL,
    created          DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified         DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT user_profiles_ibfk_1
        FOREIGN KEY (address_id) REFERENCES addresses (id)
)
    AUTO_INCREMENT = 81;

CREATE TABLE profiles_categories
(
    profile_id  INT NOT NULL,
    category_id INT NOT NULL,
    PRIMARY KEY (profile_id, category_id),
    CONSTRAINT users_categories_categories_id_fk
        FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT users_categories_user_profiles_id_fk
        FOREIGN KEY (profile_id) REFERENCES user_profiles (id)
)
    COMMENT 'specialization';

CREATE INDEX address_id
    ON user_profiles (address_id);

CREATE INDEX id
    ON user_profiles (id);

CREATE INDEX user_profiles_price_index
    ON user_profiles (price);

CREATE INDEX user_profiles_rating_index
    ON user_profiles (rating DESC);

CREATE TABLE users
(
    id                    INT AUTO_INCREMENT
        PRIMARY KEY,
    profile_id            INT                                  NOT NULL,
    full_name             VARCHAR(255)                         NOT NULL,
    email                 VARCHAR(100)                         NULL,
    phone_number          VARCHAR(15)                          NULL,
    hash                  VARCHAR(255)                         NULL,
    password              VARCHAR(100)                         NULL,
    image_url             VARCHAR(750)                         NULL,
    provider              VARCHAR(50)                          NULL,
    provider_user_id      VARCHAR(255)                         NULL,
    verified_email        TINYINT(1) DEFAULT 0                 NOT NULL,
    verified_phone_number TINYINT(1) DEFAULT 0                 NOT NULL,
    created               DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified              DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    status                TINYINT(1)                           NULL,
    enabled               TINYINT(1) DEFAULT 0                 NOT NULL,
    auth_chat_token       VARCHAR(500)                         NULL,
    CONSTRAINT users_email_uindex
        UNIQUE (email),
    CONSTRAINT users_ibfk_1
        FOREIGN KEY (profile_id) REFERENCES user_profiles (id)
)
    AUTO_INCREMENT = 81;

CREATE TABLE documents
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    title       VARCHAR(255)                         NULL,
    category_id INT                                  NULL,
    created_by  INT                                  NULL,
    content     TEXT                                 NULL,
    type        TINYINT                              NULL,
    status      TINYINT                              NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    like_count  INT        DEFAULT 0                 NULL,
    CONSTRAINT document_posts_categories_id_fk
        FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT document_posts_users_id_fk
        FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE document_attachments
(
    id          INT AUTO_INCREMENT
        PRIMARY KEY,
    document_id INT                                  NULL,
    file_src    VARCHAR(255)                         NOT NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT document_attachments_document_posts_id_fk
        FOREIGN KEY (document_id) REFERENCES documents (id)
);

CREATE INDEX document_posts_status_index
    ON documents (status);

CREATE TABLE favorite_mentors
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    student_id INT                                  NOT NULL,
    mentor_id  INT                                  NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT mentor_id
        FOREIGN KEY (mentor_id) REFERENCES users (id),
    CONSTRAINT student_id
        FOREIGN KEY (student_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 35;

CREATE INDEX mentor_id_idx
    ON favorite_mentors (mentor_id);

CREATE INDEX student_id_idx
    ON favorite_mentors (student_id);

CREATE TABLE fcm_tokens
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    token      VARCHAR(255)                         NOT NULL,
    user_id    INT                                  NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    CONSTRAINT fcm_tokens_user_id_token_uindex
        UNIQUE (user_id, token),
    CONSTRAINT fcm_tokens_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 117;

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
    CONSTRAINT feedback_users_ibfk_1
        FOREIGN KEY (from_user_id) REFERENCES users (id),
    CONSTRAINT feedback_users_ibfk_2
        FOREIGN KEY (to_user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 13;

CREATE INDEX from_user_id
    ON feedback_users (from_user_id);

CREATE INDEX to_user_id
    ON feedback_users (to_user_id);

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
    CONSTRAINT login_histories_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX id
    ON login_histories (id);

CREATE INDEX user_id
    ON login_histories (user_id);

CREATE TABLE mentor_verifications
(
    id            INT AUTO_INCREMENT
        PRIMARY KEY,
    mentor_id     INT                                  NOT NULL,
    message       TEXT                                 NULL,
    status        TINYINT(1)                           NULL,
    verified_date DATETIME                             NULL,
    moderator_id  INT                                  NULL,
    is_deleted    TINYINT(1) DEFAULT 0                 NULL,
    created       DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified      DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT user_id
        UNIQUE (mentor_id),
    CONSTRAINT mentor_verifications_users_id_fk
        FOREIGN KEY (mentor_id) REFERENCES users (id),
    CONSTRAINT mentor_verifications_users_id_fk_2
        FOREIGN KEY (moderator_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 37;

CREATE INDEX id
    ON mentor_verifications (id);

CREATE TABLE mentorship
(
    id             INT AUTO_INCREMENT
        PRIMARY KEY,
    mentor_id      INT                                  NULL,
    category_id    INT                                  NOT NULL,
    created_by     INT                                  NOT NULL,
    title          VARCHAR(255)                         NULL,
    start_date     DATETIME                             NULL,
    end_date       DATETIME                             NULL,
    duration       INT                                  NULL,
    type           TINYINT                              NULL,
    status         TINYINT                              NULL,
    price          FLOAT                                NULL,
    detail_address VARCHAR(500)                         NULL,
    address_id     INT                                  NULL,
    is_deleted     TINYINT(1) DEFAULT 0                 NOT NULL,
    created        DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified       DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT mentorship_ibfk_1
        FOREIGN KEY (address_id) REFERENCES addresses (id),
    CONSTRAINT mentorship_ibfk_2
        FOREIGN KEY (mentor_id) REFERENCES users (id),
    CONSTRAINT mentorship_ibfk_3
        FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT mentorship_ibfk_4
        FOREIGN KEY (created_by) REFERENCES users (id)
)
    AUTO_INCREMENT = 34;

CREATE INDEX address_id
    ON mentorship (address_id);

CREATE INDEX category_id
    ON mentorship (category_id);

CREATE INDEX created_by
    ON mentorship (created_by);

CREATE INDEX mentor_id
    ON mentorship (mentor_id);

CREATE INDEX mentorship_price_index
    ON mentorship (price);

CREATE INDEX mentorship_status_index
    ON mentorship (status);

CREATE TABLE mentorship_requests
(
    id               INT AUTO_INCREMENT
        PRIMARY KEY,
    mentorship_id    INT                                  NOT NULL,
    assignee_id      INT                                  NOT NULL,
    approver_id      INT                                  NOT NULL,
    assignee_role_id INT                                  NOT NULL,
    enroll_date      DATETIME                             NULL,
    status           TINYINT                              NULL,
    content          TEXT                                 NULL,
    message          TEXT                                 NULL,
    is_deleted       TINYINT(1) DEFAULT 0                 NOT NULL,
    created          DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified         DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT class_enrollments_pk_2
        UNIQUE (mentorship_id, approver_id),
    CONSTRAINT class_enrollments_roles_id_fk
        FOREIGN KEY (assignee_role_id) REFERENCES roles (id),
    CONSTRAINT class_enrollments_users_id_fk
        FOREIGN KEY (assignee_id) REFERENCES users (id),
    CONSTRAINT mentorship_requests_ibfk_1
        FOREIGN KEY (approver_id) REFERENCES users (id),
    CONSTRAINT mentorship_requests_ibfk_2
        FOREIGN KEY (mentorship_id) REFERENCES mentorship (id)
)
    AUTO_INCREMENT = 34;

CREATE INDEX course_id
    ON mentorship_requests (mentorship_id);

CREATE INDEX mentorship_requests_status_index
    ON mentorship_requests (status);

CREATE INDEX user_id
    ON mentorship_requests (approver_id);

CREATE TABLE posts
(
    id             INT AUTO_INCREMENT
        PRIMARY KEY,
    title          VARCHAR(255)                         NULL,
    category_id    INT                                  NULL,
    created_by     INT                                  NULL,
    content        TEXT                                 NULL,
    start_date     DATETIME                             NULL,
    end_date       DATETIME                             NULL,
    type           TINYINT                              NULL,
    status         TINYINT                              NULL,
    price          FLOAT                                NULL,
    detail_address VARCHAR(500)                         NULL,
    address_id     INT                                  NULL,
    is_deleted     TINYINT(1) DEFAULT 0                 NOT NULL,
    created        DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified       DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    like_count     INT        DEFAULT 0                 NULL,
    comment_count  INT        DEFAULT 0                 NULL,
    CONSTRAINT posts_addresses_id_fk
        FOREIGN KEY (address_id) REFERENCES addresses (id),
    CONSTRAINT posts_categories_id_fk
        FOREIGN KEY (category_id) REFERENCES categories (id),
    CONSTRAINT posts_users_id_fk
        FOREIGN KEY (created_by) REFERENCES users (id)
)
    AUTO_INCREMENT = 15;

CREATE TABLE comments
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    post_id    INT                                NOT NULL,
    content    TEXT                               NULL,
    created_by INT                                NOT NULL,
    created    DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    is_deleted TINYINT  DEFAULT 0                 NOT NULL,
    CONSTRAINT comments_posts_id_fk
        FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT comments_users_id_fk
        FOREIGN KEY (created_by) REFERENCES users (id)
)
    AUTO_INCREMENT = 40;

CREATE INDEX posts_price_index
    ON posts (price);

CREATE INDEX posts_status_index
    ON posts (status);

CREATE TABLE quizzes
(
    id                 INT AUTO_INCREMENT
        PRIMARY KEY,
    title              VARCHAR(255)                         NULL,
    author             VARCHAR(255)                         NULL,
    number_of_question INT                                  NOT NULL,
    created_by         INT                                  NOT NULL,
    modified_by        INT                                  NULL,
    is_deleted         TINYINT(1) DEFAULT 0                 NOT NULL,
    created            DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified           DATETIME   DEFAULT CURRENT_TIMESTAMP NULL,
    time               INT                                  NOT NULL,
    is_draft           TINYINT(1)                           NULL,
    CONSTRAINT quizzes_ibfk_1
        FOREIGN KEY (created_by) REFERENCES users (id),
    CONSTRAINT quizzes_ibfk_2
        FOREIGN KEY (modified_by) REFERENCES users (id)
)
    AUTO_INCREMENT = 18;

CREATE TABLE favorite_quizzes
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    quiz_id    INT                                  NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT quiz_id
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT user_id
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 12;

CREATE INDEX quizId_idx
    ON favorite_quizzes (quiz_id);

CREATE INDEX userId_idx
    ON favorite_quizzes (user_id);

CREATE TABLE questions
(
    id                 INT AUTO_INCREMENT
        PRIMARY KEY,
    quiz_id            INT                                  NOT NULL,
    question           TEXT                                 NULL,
    description        TEXT                                 NULL,
    type               TINYINT                              NULL,
    is_deleted         TINYINT(1) DEFAULT 0                 NOT NULL,
    created            DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified           DATETIME   DEFAULT CURRENT_TIMESTAMP NULL,
    is_multiple_choice TINYINT(1)                           NULL,
    CONSTRAINT questions_ibfk_1
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id)
)
    AUTO_INCREMENT = 98;

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
    CONSTRAINT answers_ibfk_1
        FOREIGN KEY (question_id) REFERENCES questions (id)
)
    AUTO_INCREMENT = 312;

CREATE INDEX question_id
    ON answers (question_id);

CREATE INDEX quiz_id
    ON questions (quiz_id);

CREATE TABLE quiz_results
(
    id              INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id         INT                                  NOT NULL,
    quiz_id         INT                                  NOT NULL,
    score           INT                                  NULL,
    is_deleted      TINYINT(1) DEFAULT 0                 NOT NULL,
    created         DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified        DATETIME   DEFAULT CURRENT_TIMESTAMP NULL,
    number_of_true  INT                                  NULL,
    number_of_false INT                                  NULL,
    CONSTRAINT quiz_results_ibfk_1
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT quiz_results_ibfk_2
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 42;

CREATE INDEX quiz_id
    ON quiz_results (quiz_id);

CREATE INDEX user_id
    ON quiz_results (user_id);

CREATE INDEX created_by
    ON quizzes (created_by);

CREATE INDEX modified_by
    ON quizzes (modified_by);

CREATE INDEX quizzes_author_index
    ON quizzes (author);

CREATE TABLE quizzes_categories
(
    quiz_id     INT                                  NOT NULL,
    category_id INT                                  NOT NULL,
    is_deleted  TINYINT(1) DEFAULT 0                 NOT NULL,
    created     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT quizzes_categories_ibfk_1
        FOREIGN KEY (quiz_id) REFERENCES quizzes (id),
    CONSTRAINT quizzes_categories_ibfk_2
        FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE INDEX category_id
    ON quizzes_categories (category_id);

CREATE INDEX quiz_id
    ON quizzes_categories (quiz_id);

CREATE TABLE reports_users
(
    id           INT AUTO_INCREMENT
        PRIMARY KEY,
    from_user_id INT                                    NOT NULL,
    to_user_id   INT                                    NOT NULL,
    reason       VARCHAR(255)                           NOT NULL,
    description  TEXT                                   NULL,
    img_url1     VARCHAR(750) DEFAULT 'null'            NULL,
    img_url2     VARCHAR(750) DEFAULT 'null'            NULL,
    img_url3     VARCHAR(750) DEFAULT 'null'            NULL,
    is_deleted   TINYINT(1)   DEFAULT 0                 NOT NULL,
    created      DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified     DATETIME     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT from_student_id
        FOREIGN KEY (from_user_id) REFERENCES users (id),
    CONSTRAINT to_mentor_id
        FOREIGN KEY (to_user_id) REFERENCES users (id)
);

CREATE INDEX mentor_id_idx
    ON reports_users (to_user_id);

CREATE INDEX student_id_idx
    ON reports_users (from_user_id);

CREATE TABLE secure_tokens
(
    id         BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    token      VARCHAR(255)                        NOT NULL,
    time_stamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
    expire_at  DATETIME                            NULL,
    user_id    INT                                 NOT NULL,
    CONSTRAINT secure_tokens_id_uindex
        UNIQUE (id),
    CONSTRAINT secure_tokens_token_uindex
        UNIQUE (token),
    CONSTRAINT secure_tokens_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 8;

CREATE TABLE shifts
(
    id            INT AUTO_INCREMENT
        PRIMARY KEY,
    mentorship_id INT                                  NULL,
    day_of_week   VARCHAR(15)                          NOT NULL,
    start_time    TIME                                 NOT NULL,
    end_time      TIME                                 NOT NULL,
    `repeat`      INT        DEFAULT 1                 NOT NULL,
    is_deleted    TINYINT(1) DEFAULT 0                 NOT NULL,
    created       DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified      DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    created_by    INT                                  NULL,
    CONSTRAINT shifts_mentorship_id_fk
        FOREIGN KEY (mentorship_id) REFERENCES mentorship (id)
)
    AUTO_INCREMENT = 117;

CREATE TABLE timetables
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    name       VARCHAR(255)                         NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT timetables_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 77;

CREATE TABLE events
(
    id            INT AUTO_INCREMENT
        PRIMARY KEY,
    timetable_id  INT                                  NOT NULL,
    mentorship_id INT                                  NULL,
    shift_id      INT                                  NULL,
    title         VARCHAR(255)                         NULL,
    content       TEXT                                 NULL,
    start_time    DATETIME                             NULL,
    end_time      DATETIME                             NULL,
    type          TINYINT                              NULL,
    is_deleted    TINYINT(1) DEFAULT 0                 NOT NULL,
    created       DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified      DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT events_ibfk_1
        FOREIGN KEY (timetable_id) REFERENCES timetables (id),
    CONSTRAINT events_mentorship_id_fk
        FOREIGN KEY (mentorship_id) REFERENCES mentorship (id),
    CONSTRAINT events_shifts_id_fk
        FOREIGN KEY (shift_id) REFERENCES shifts (id)
)
    AUTO_INCREMENT = 1233;

CREATE INDEX events_mentorship_id_index
    ON events (mentorship_id);

CREATE INDEX timetable_id
    ON events (timetable_id);

CREATE INDEX user_id
    ON timetables (user_id);

CREATE TABLE unread_notifications_counters
(
    id                           INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id                      INT                                  NOT NULL,
    unread_notifications_counter INT        DEFAULT 0                 NOT NULL,
    is_deleted                   TINYINT(1) DEFAULT 0                 NOT NULL,
    created                      DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified                     DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT unread_notification_counter_user_id_uindex
        UNIQUE (user_id),
    CONSTRAINT unread_notification_counter_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 56;

CREATE INDEX id
    ON users (id);

CREATE INDEX profile_id
    ON users (profile_id);

CREATE INDEX users_password_index
    ON users (password);

CREATE TABLE users_like_posts
(
    id         INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id    INT                                  NOT NULL,
    post_id    INT                                  NOT NULL,
    is_deleted TINYINT(1) DEFAULT 0                 NOT NULL,
    created    DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified   DATETIME   DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT users_like_posts_user_id_post_id_uindex
        UNIQUE (user_id, post_id),
    CONSTRAINT users_like_posts_posts_id_fk
        FOREIGN KEY (post_id) REFERENCES posts (id),
    CONSTRAINT users_like_posts_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 42;

CREATE TABLE users_notifications
(
    id              INT AUTO_INCREMENT
        PRIMARY KEY,
    user_id         INT               NOT NULL,
    notification_id INT               NOT NULL,
    is_read         TINYINT DEFAULT 0 NOT NULL,
    CONSTRAINT users_notifications_id_uindex
        UNIQUE (id),
    CONSTRAINT users_notifications_notifications_id_fk
        FOREIGN KEY (notification_id) REFERENCES notifications (id),
    CONSTRAINT users_notifications_users_id_fk
        FOREIGN KEY (user_id) REFERENCES users (id)
)
    AUTO_INCREMENT = 130;

CREATE TABLE users_roles
(
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT users_roles_user_id_uindex
        UNIQUE (user_id),
    CONSTRAINT users_roles_ibfk_1
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT users_roles_ibfk_2
        FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE INDEX role_id
    ON users_roles (role_id);


