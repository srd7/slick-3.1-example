# --- !Ups

INSERT INTO USER (ID, NAME) VALUES (1, '田中太郎');

# --- !Downs
DELETE FROM USER WHERE 1;
