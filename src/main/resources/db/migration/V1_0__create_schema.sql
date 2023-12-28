CREATE TABLE post
(
    id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name    VARCHAR(255),
    body    VARCHAR(255),
    blogger_id BIGINT                                  NOT NULL,
    CONSTRAINT pk_post PRIMARY KEY (id)
);
CREATE TABLE blogger
(
    blogger_id BIGINT NOT NULL,
    user_name  VARCHAR(255),
    password   VARCHAR(255),
    CONSTRAINT pk_blogger PRIMARY KEY (blogger_id)
);
CREATE TABLE comment
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(255),
    comment  VARCHAR(255),
    CONSTRAINT pk_comment PRIMARY KEY (id)
);
ALTER TABLE post
    ADD CONSTRAINT FK_POST_ON_USER FOREIGN KEY (blogger_id) REFERENCES blogger (blogger_id);
