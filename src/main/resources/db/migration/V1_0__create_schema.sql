CREATE TABLE post
(
    id         UUID NOT NULL,
    name       VARCHAR(255),
    body       TEXT,
    blogger_id UUID NOT NULL,
    count      INT,
    CONSTRAINT pk_post PRIMARY KEY (id)
);
CREATE TABLE blogger
(
    blogger_id UUID NOT NULL,
    user_name  VARCHAR(255),
    password   VARCHAR(255),
    CONSTRAINT pk_blogger PRIMARY KEY (blogger_id)
);
CREATE TABLE comment
(
    id      UUID NOT NULL,
    content VARCHAR(255),
    post_id UUID NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);
ALTER TABLE post
    ADD CONSTRAINT FK_POST_ON_BLOGGER FOREIGN KEY (blogger_id) REFERENCES blogger (blogger_id);
ALTER TABLE comment
    ADD CONSTRAINT FK_COMMENT_ON_POST FOREIGN KEY (post_id) REFERENCES post (id);