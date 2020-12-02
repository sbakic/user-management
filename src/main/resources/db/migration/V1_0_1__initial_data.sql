INSERT INTO authority
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

INSERT INTO application_user
values (1,
        'Administrator',
        '',
        'admin@example.com',
        '$2y$10$HPlo7YqwPYWHr.zJlrYKmeKGkUn5poKZvlRtKrS6Kz7r/1Jn5uHIa',
        '',
        '');

INSERT INTO application_user_authority
VALUES (1, 'ROLE_ADMIN');
