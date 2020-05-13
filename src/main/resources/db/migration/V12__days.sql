ALTER TABLE routes ADD COLUMN monday BOOL DEFAULT false AFTER days;
ALTER TABLE routes ADD COLUMN tuesday BOOL DEFAULT false AFTER monday;
ALTER TABLE routes ADD COLUMN wednesday BOOL DEFAULT false AFTER tuesday;
ALTER TABLE routes ADD COLUMN thursday BOOL DEFAULT false AFTER wednesday;
ALTER TABLE routes ADD COLUMN friday BOOL DEFAULT false AFTER thursday;
ALTER TABLE routes ADD COLUMN saturday BOOL DEFAULT false AFTER friday;
ALTER TABLE routes ADD COLUMN sunday BOOL DEFAULT false AFTER saturday;