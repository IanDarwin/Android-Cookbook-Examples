-- Minimal data for testing.
-- ID/fkey values are chosen to avoid colliding with sequence-generated values

insert into Task (id, name, creationDate, project, context) values(-10, 'Get the lead out!', '2011-01-01', 'Plumbing', 'Home');
insert into Task (id, name, creationDate, project, context) values(-11, 'Pay off loan', '2011-01-01', 'Accounting', 'Home');
insert into Task (id, name, creationDate, project, context) values(-12, 'Call Home', '2011-01-01', null, 'Work');
