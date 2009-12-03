create table exits
(
   mile_marker smallint, 			        			/* Mile Marker, primary key  */
   exit_number varchar(5),  							/* Exit number, e.g. 18A, 19 */
   nearest_town	varchar(50) 							/* Name of the nearest town  */   
);

create table customers
(
   customer_id smallint not null,			            /* Customer id, primary key    */
   name varchar(20),                                    /* Name of the customer        */
   address varchar (50),                                /* Customer's stree address    */
   city varchar(20),									/* City of the customer        */ 
   state varchar(20),                                   /* State the Customer lives in */
   zip_code smallint,                                    /* Zip code of the city        */   
   constraint pk_customer_id primary key (customer_id)  
);

create table transmitters
(
   transmitter_id smallint not null,					/* Transmitter id, primary key			 		 */
   customer_id smallint not null,						/* Customer id, foreign key to customers	 	 */
   account_balance double,								/* Account balance on the transmitter		 	 */
   license_plate varchar(7),                   		 	/* License Plate of the car with the transmitter */
   constraint pk_transmitter_id primary key (transmitter_id),
   constraint fk_customer_id foreign key (customer_id) references customers (customer_id)
);

create table trips
(
   trip_id smallint not null,							/* Trip id, primary key	             */
   start_exit varchar(5),								/* Exit in which the trip starts     */
   end_exit varchar(5),                                 /* Exit in which the trip ends       */
   date date,                                           /* Date in which the trip occurred   */
   payment_type varchar(20),						    /* Payment, ticket or transmitter    */
   transmitter_id smallint,                             /* Id of the transmitter used to pay */
   status varchar(25),                                  /* Current state of the trip         */
   class varchar(10),
   constraint pk_trip_id primary key (trip_id),
   constraint fk_transmitter_id foreign key (transmitter_id) references transmitters (transmitter_id)
);