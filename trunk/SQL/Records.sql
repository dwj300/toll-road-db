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
(mile_marker, exit_number, nearest_town)
values (161, '161', 'Bellefonte' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (100, '100B', 'Altoona' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (161, '161', 'Bellefonte' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (99, '42C', 'Milton' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (90, '50', 'Dubois' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (162, '162', 'Boalsburg' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (199, '20D', 'Tyrone' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (115, '115', 'Williamsport' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (369, '369', 'Philadelphia' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (400, '400B', 'Bloomsburg' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (350, '350', 'Harrisburg' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (325, '325', 'Pittsburg' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (15, '15A', 'Erie' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (400, '400', 'Reading' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (222, '222', 'York' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (333, '333', 'Carlisle' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (444, '444', 'Scranton' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (111, '111', 'Bald Eagle' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (117, '117', 'Penns Valley' );

insert into exits
(mile_marker, exit_number, nearest_town)
values (353, '353', 'Indiana' );

insert into trips
(trip_id, start_exit, end_exit, date, payment_type, transmitter_id, status, class)
values (0, '161', '100B', '2009-12-02', 'Transmitter', 1, 'Paid', 'Car');

insert into trips
(trip_id, start_exit, end_exit, date, payment_type, transmitter_id, status, class)
values (1, '99', '117', '2009-12-05', 'Ticket', NULL, 'Completed But Not Paid', 'Car');

insert into trips
(trip_id, start_exit, end_exit, date, payment_type, transmitter_id, status, class)
values (2, '400', NULL, '2009-11-22', 'Transmitter', 2, 'Underway', 'Car');

insert into trips
(trip_id, start_exit, end_exit, date, payment_type, transmitter_id, status, class)
values (3, '50', '192', '2009-11-29', 'Ticket', NULL, 'Paid', 'Truck');

insert into trips
(trip_id, start_exit, end_exit, date, payment_type, transmitter_id, status, class)
values (4, '162', '42C', '2009-12-03', 'Transmitter', 0, 'Paid', 'Truck');