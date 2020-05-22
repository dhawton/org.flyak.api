CREATE TABLE files(
  id BIGINT(21) UNSIGNED AUTO_INCREMENT,
  path VARCHAR(255),
  name VARCHAR(255),
  created_at datetime default current_timestamp,
  updated_at datetime default current_timestamp,

  primary key(id)
);

alter table files add index path (path);