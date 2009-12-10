insert into customers
(customer_id, name, address, city, state, zip_code)
   values (0, 'Jeff Farkas', '547 Oakwood Avenue', 'State College', 'PA', 16803);

insert into customers
(customer_id, name, address, city, state, zip_code)
   values (1, 'Mike Barr', '547 Oakwood Avenue', 'State College', 'PA', 16803);

insert into customers
(customer_id, name, address, city, state, zip_code)
   values (2, 'Kyle Dodson', '547 Oakwood Avenue', 'State College', 'PA', 16803);
   
insert into transmitters
(transmitter_id, customer_id, account_balance, license_plate)
   values (0, 0,  75.00, 'FLA1766');

insert into transmitters
(transmitter_id, customer_id, account_balance, license_plate)
   values (1, 0, 50.00, 'LOLROFL');

insert into transmitters
(transmitter_id, customer_id, account_balance, license_plate)
   values (2, 1, 150.75, 'CIVENG');

insert into transmitters
(transmitter_id, customer_id, account_balance, license_plate)
   values (3, 2, 40.25, 'BATMAN');
   
insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (0, 161, '161', 'Bellefonte' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (1, 100, '100B', 'Altoona' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (2, 162, '162', 'Pinecroft' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (3, 99, '42C', 'Milton' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (4, 90, '50', 'Dubois' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (5, 162, '162', 'Boalsburg' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (6, 199, '20D', 'Tyrone' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (7, 115, '115', 'Williamsport' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (8, 369, '369', 'Philadelphia' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (9, 400, '400B', 'Bloomsburg' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (10, 350, '350', 'Harrisburg' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (11, 325, '325', 'Pittsburg' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (12, 15, '15A', 'Erie' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (13, 400, '400', 'Reading' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (14, 222, '222', 'York' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (15, 333, '333', 'Carlisle' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (16, 444, '444', 'Scranton' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (17, 111, '111', 'Bald Eagle' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (18, 117, '117', 'Penns Valley' );

insert into exits
(exit_id, mile_marker, exit_number, nearest_town)
values (19, 353, '353', 'Indiana' );

insert into trips
(trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class)
values (0, 0, 1, '2009-12-02', 'Transmitter', 1, 'Completed But Not Paid', 'Car');

insert into trips
(trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class)
values (1, 3, 18, '2009-12-05', 'Ticket', NULL, 'Completed But Not Paid', 'Car');

insert into trips
(trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class)
values (2, 13, NULL, '2009-11-22', 'Transmitter', 2, 'Underway', 'Car');

insert into trips
(trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class)
values (3, 4, 16, '2009-11-29', 'Ticket', NULL, 'Completed But Not Paid', 'Truck');

insert into trips
(trip_id, start_exit_id, end_exit_id, date, payment_type, transmitter_id, status, class)
values (4, 5, 3, '2009-12-03', 'Transmitter', 0, 'Completed But Not Paid', 'Truck');