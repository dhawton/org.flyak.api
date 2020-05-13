ALTER TABLE routes ADD COLUMN arrival_time VARCHAR(5) NOT NULL AFTER departure_time;
ALTER TABLE routes CHANGE COLUMN days days VARCHAR(128);
