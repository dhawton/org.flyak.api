CREATE TABLE users(
  id BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(128) NOT NULL,
  password VARCHAR(128),
  verified TINYINT(1) DEFAULT 0,
  verification_token INT(6) DEFAULT 0,
  last_airport VARCHAR(4) DEFAULT "PAFA",
  hours FLOAT(10,2) DEFAULT 0.0,
  rank INT(2),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `users` ADD UNIQUE(`email`);

CREATE TABLE airports(
  icao VARCHAR(4),
  name VARCHAR(128),
  lat FLOAT(10,8),
  lon FLOAT(10,8),

  PRIMARY KEY(icao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE airlines(
  icao VARCHAR(3),
  name VARCHAR(128),
  base VARCHAR(4),

  PRIMARY KEY(icao)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE equipment(
  icao VARCHAR(3),
  name VARCHAR(255),
  airline_id VARCHAR(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE roles(
  id BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id BIGINT(21) UNSIGNED,
  role VARCHAR(255),
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `roles` ADD CONSTRAINT `user_roles` FOREIGN KEY (`user_id`) REFERENCES `users`(`id`) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE `roles` ADD INDEX (`user_id`);

CREATE TABLE routes(
  id BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT,
  airline VARCHAR(3) NOT NULL,
  flight_number VARCHAR(12) NOT NULL,
  departure VARCHAR(4),
  arrival VARCHAR(4),
  days VARCHAR(14),
  equipment VARCHAR(4),
  departure_time TIME,
  duration TIME,
  routing TEXT,
  altitude INT(7) UNSIGNED,
  notes TEXT,
  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE logbook(
  id BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id BIGINT(21) UNSIGNED,
  flight VARCHAR(12),
  equipment VARCHAR(128),
  simulator VARCHAR(128),
  duration TIME,
  fuel_burnt FLOAT(10,2),
  passengers INT(5),
  distance FLOAT(10,2),
  cargo FLOAT(8,2),
  departure VARCHAR(4),
  arrival VARCHAR(4),

  PRIMARY KEY(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `logbook` ADD CONSTRAINT user_logbook FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE logbook ADD INDEX (user_id);