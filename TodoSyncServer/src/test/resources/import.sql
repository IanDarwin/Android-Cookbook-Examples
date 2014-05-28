-- Minimal data for testing.
-- ID/fkey values are chosen to avoid colliding with sequence-generated values

insert into Task (id, name, createdYear, createdMonth, createdDay, project, context, complete, modified) values(-10, 'Get the lead out!', 2011,01,01, 'Plumbing', 'Home', false, 0);
insert into Task (id, name, createdYear, createdMonth, createdDay, project, context, complete, modified) values(-11, 'Pay off loan', 2011,02,03, 'Accounting', 'Home', false, 0);
insert into Task (id, name, createdYear, createdMonth, createdDay, project, context, complete, modified) values(-12, 'Call Home', 2011,03,03, null, 'Work', false, 0);
