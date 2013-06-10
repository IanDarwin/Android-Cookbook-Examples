-- Minimal data for testing, mostly the Readings section of the code.
-- The ID/fkey values are chosen to avoid colliding with sequence-generated values

insert into person(id,login,first_name,last_name) values(100, '300000', 'Sample', 'Patient');
insert into patient(id) values(100);

-- Sorry about this long line, DO NOT WRAP, hibernate import.sql processing is line-at-a-time, *shrug*.
insert into readings(id, created_at, measured_at, measurementcontext, relationship, rid, serial_number, timesource, patient_id) values(-1, '2012-05-31 12:37:04', '2012-05-31 12:37:04', null, null, 1338482224562, null, 1, 100);

insert into weight_readings(id, measurement, units) values (-1, 151.42, 'lbs');

-- Ditto for a BG Reading
insert into readings(id, created_at, measured_at, measurementcontext, relationship, rid, serial_number, timesource, patient_id) values(-2, '2012-05-31 12:37:04', '2012-05-31 12:37:04', null, null, 1338482224562, null, 1, 100);

insert into bg_readings (measurement, units, id ) values (12.3, 'mmol/l',  -2);



