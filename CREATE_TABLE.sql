CREATE TABLE "USERS"
(
  "LOGIN" text NOT NULL,
  "PASSWORD" text NOT NULL,
  "BALANCE" numeric NOT NULL,
  CONSTRAINT users_pk PRIMARY KEY ("LOGIN")
)