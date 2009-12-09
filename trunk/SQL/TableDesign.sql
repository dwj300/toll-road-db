create table transactions
(
   transaction_id smallint not null,                 /* Transaction id, primary key     */
   exit_id smallint not null,                        /* Exit id, foreign key		    */
   amount_paid double not null, 					 /* Amount paid, e.g. 12.34		    */
   payment_type varchar(20) not null,				 /* Payment, ticket or transmitter  */
   class varchar(10) not null,						 /* Class of the vehicle. Car|Truck */
   constraint pk_transaction_id primary key (transaction_id),
   constraint fk_exit_id foreign key (exit_id) references exits (exit_id)
);

create table exits
(
   exit_id smallint,                                 /* Exit id, primary key            */
   mile_marker smallint,                             /* Mile Marker, can have mult exits*/
   exit_number varchar(5),                           /* Exit number, e.g. 18A, 19       */
   nearest_town	varchar(50),                         /* Name of the nearest town        */
   constraint pk_exit_id primary key (exit_id)
);

create table customers
(
   customer_id smallint not null,                   /* Customer id, primary key    */
   name varchar(20),                                /* Name of the customer        */
   address varchar (50),                            /* Customer's stree address    */
   city varchar(20),                                /* City of the customer        */
   state varchar(15),                               /* State the Customer lives in */
   zip_code smallint,                               /* Zip code of the city        */
   constraint pk_customer_id primary key (customer_id)  
);

create table transmitters
(
   transmitter_id smallint not null,                /* Transmitter id, primary key                   */
   customer_id smallint not null,                   /* Customer id, foreign key to customers         */
   account_balance double,                          /* Account balance on the transmitter            */
   license_plate varchar(7),                        /* License Plate of the car with the transmitter */
   constraint pk_transmitter_id primary key (transmitter_id),
   constraint fk_customer_id foreign key (customer_id) references customers (customer_id)
);

create table trips
(
   trip_id smallint not null,/* Trip id, primary key	                                  */
   start_exit_id smallint,/* Exit id in which the trip starts                          */
   end_exit_id smallint,/* Exit id in which the trip ends                            */
   date date,/* Date in which the trip occurred                        */
   payment_type varchar(20),/* Payment, ticket or transmitter                         */
   transmitter_id smallint,/* Id of the transmitter used to pay 					  */
   status varchar(25),/* State of the trip. Paid|Completed But Not Paid|Underway*/
   class varchar(10),/* Class of the vehicle. Car|Truck*/
   constraint pk_trip_id primary key (trip_id),
   constraint fk_transmitter_id foreign key (transmitter_id) references transmitters (transmitter_id),
   constraint fk_start_exit_id foreign key (start_exit_id) references exits (exit_id),
   constraint fk_end_exit_id foreign key (end_exit_id) references exits (exit_id)
);