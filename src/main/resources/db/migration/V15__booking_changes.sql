DROP TABLE bookings;
DROP TABLE logbook;

CREATE TABLE bookings(
    id BIGINT(21) UNSIGNED AUTO_INCREMENT,
    user_id BIGINT(21) UNSIGNED,
    airline VARCHAR(4),
    flight_number VARCHAR(12),
    atcident VARCHAR(12),
    departure VARCHAR(4),
    arrival VARCHAR(4),
    equipment VARCHAR(4),
    departure_time VARCHAR(5),
    arrival_time VARCHAR(5),
    duration VARCHAR(5),
    planned_departure TIMESTAMP,

    PRIMARY KEY(id)
);

ALTER TABLE bookings ADD CONSTRAINT user_bookings FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE logbook(
  id BIGINT(21) UNSIGNED AUTO_INCREMENT,
  user_id BIGINT(21) UNSIGNED,
  airline VARCHAR(4),
  flight_number VARCHAR(4),
  planned_departure VARCHAR(4),
  actual_departure VARCHAR(4),
  planned_arrival VARCHAR(4),
  actual_arrival VARCHAR(4),
  equipment VARCHAR(4),
  offblock_time TIMESTAMP,
  onblock_time TIMESTAMP,
  duration VARCHAR(5),
  status VARCHAR(128),
  simulator VARCHAR(128),
  fuel_start FLOAT(8, 2),
  fuel_end FLOAT(8, 2),
  distance_flown FLOAT(10, 1),
  positions MEDIUMTEXT DEFAULT '[]',

  PRIMARY KEY(id)
);

ALTER TABLE logbook ADD CONSTRAINT user_logbook FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE;

CREATE TABLE logbook_details(
  id BIGINT(21) UNSIGNED AUTO_INCREMENT,
  logbook_id BIGINT(21) UNSIGNED,
  latitude FLOAT(10, 8),
  longitude FLOAT(10, 7),
  altitude INT(6),
  groundspeed INT(5),
  entry VARCHAR(255),

  PRIMARY KEY(id)
);

ALTER TABLE logbook_details ADD CONSTRAINT logbook_link FOREIGN KEY (logbook_id) REFERENCES logbook(id) ON DELETE CASCADE ON UPDATE CASCADE;
