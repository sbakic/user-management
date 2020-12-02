CREATE TABLE authority
(
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE application_user
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    email         VARCHAR(50) NOT NULL,
    password_hash VARCHAR(60) NOT NULL,
    country       VARCHAR(50),
    address       VARCHAR(255),
    CONSTRAINT uq_application_user_email UNIQUE (email)
);

CREATE TABLE application_user_authority
(
    user_id        BIGINT      NOT NULL,
    authority_name VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, authority_name),
    CONSTRAINT fk_application_user_authority_application_user_id FOREIGN KEY (USER_ID) REFERENCES application_user (id),
    CONSTRAINT fk_application_user_authority_authority_name FOREIGN KEY (AUTHORITY_NAME) REFERENCES authority (name)
);
