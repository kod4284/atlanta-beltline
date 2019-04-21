
#1
CREATE TABLE `user` (
	`username` varchar(20) NOT NULL,
	`password` char(64) NOT NULL,
	`status` ENUM ('Approved', 'Pending', 'Declined'),
`firstname` varchar(20) NOT NULL,
	`lastname` varchar(20) NOT NULL,
	`user_type` ENUM ('Employee', 'Visitor', 'Employee, Visitor', 'User'),
	PRIMARY KEY (`username`)
)
ENGINE=INNODB;

#2
CREATE TABLE `user_email` (
	`username` varchar(20) NOT NULL,
	`email` varchar(30) NOT NULL,
	PRIMARY KEY (`email`),
	KEY `fk1` (`username`),
	CONSTRAINT `fk1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#3
CREATE TABLE `visitor` (
	`username` varchar(20) NOT NULL,
	PRIMARY KEY (`username`),
	CONSTRAINT `fk2` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#4
CREATE TABLE `employee` (
	`username` varchar(20) NOT NULL,
	`employee_id` decimal(9, 0) NOT NULL,
	`phone` decimal(10, 0) NOT NULL,
	`employee_address` varchar(50) NOT NULL,
	`employee_city` varchar(10) NOT NULL,
	`employee_state` char(2) NOT NULL,
	`employee_zipcode` decimal(5, 0) NOT NULL,
	`employee_type` ENUM ('Admin', 'Staff', 'Manager'),
	PRIMARY KEY (`username`),
	UNIQUE KEY `phone` (`phone`),
	UNIQUE KEY `fk3` (`username`),
	CONSTRAINT `fk3` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#5
CREATE TABLE `administrator` (
	`username` varchar(20) NOT NULL, 
	PRIMARY KEY (`username`),
	CONSTRAINT `fk4` FOREIGN KEY (`username`) REFERENCES `employee` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#6
CREATE TABLE `staff`(
	`username` varchar(20) NOT NULL,
	PRIMARY KEY (`username`),
	CONSTRAINT `fk5` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#7
CREATE TABLE `manager` (
	`username` varchar(20) NOT NULL,
	PRIMARY KEY (`username`),
	CONSTRAINT `fk6` FOREIGN KEY (`username`) REFERENCES `employee` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;


#8
CREATE TABLE `site`(
`site_name` varchar(50) NOT NULL,
	`site_address` varchar(50),
	`site_zipcode` decimal (5,0) NOT NULL,
	`open_everyday` ENUM ('No', 'Yes') NOT NULL,
	`manager_username` varchar(20) NOT NULL,
	PRIMARY KEY (`site_name`),
	KEY `fk7` (`manager_username`),
	CONSTRAINT `fk7` FOREIGN KEY (`manager_username`) REFERENCES `manager` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#9
CREATE TABLE `event`(
	`event_name` varchar(50) NOT NULL,
	`start_date` date NOT NULL,
	`site_name` varchar(50) NOT NULL,
	`end_date` date NOT NULL,
	`event_price` decimal(5, 2) NOT NULL,
	`capacity` decimal (7, 0) NOT NULL,
`min_staff_required` decimal(3, 0) NOT NULL,
`description` varchar(1000) NOT NULL,
PRIMARY KEY (`event_name`, `start_date`, `site_name`),
	CONSTRAINT `fk8` FOREIGN KEY (`site_name`) REFERENCES `site` (`site_name`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#10
CREATE TABLE `assign_to` (
	`staff_username` varchar(20) NOT NULL,
	`event_name` varchar(50) NOT NULL,
	`start_date` date NOT NULL,
	`site_name` varchar(50) NOT NULL,
	PRIMARY KEY (`staff_username`, `event_name`, `start_date`, `site_name`),
	CONSTRAINT `fk9` FOREIGN KEY (`staff_username`) REFERENCES `staff` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk10` FOREIGN KEY (`event_name`, `start_date`, `site_name`) REFERENCES `event` (`event_name`, `start_date`, `site_name`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#11
CREATE TABLE `transit`(
	`transit_type` ENUM('MARTA', 'Bus', 'Bike') NOT NULL,
	`transit_route` varchar(20) NOT NULL,
	`transit_price` decimal(5,2) NOT NULL,
	PRIMARY KEY (`transit_type`, `transit_route`)
)
ENGINE=INNODB;

#12
CREATE TABLE `connect`(
	`site_name` varchar(50) NOT NULL,
	`transit_type` ENUM ('MARTA', 'Bus', 'Bike') NOT NULL,
	`transit_route` varchar(20) NOT NULL,
	PRIMARY KEY (`transit_type`, `transit_route`,`site_name`),
	CONSTRAINT `fk11` FOREIGN KEY (`site_name`) REFERENCES `site` (`site_name`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk12` FOREIGN KEY (`transit_type`, `transit_route`) REFERENCES `transit` (`transit_type`, `transit_route`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#13
CREATE TABLE `take_transit` (
	`username` varchar(20) NOT NULL,
	`transit_type` ENUM ('MARTA', 'Bus', 'Bike') NOT NULL,
	`transit_route` varchar(20) NOT NULL,
	`transit_date` date NOT NULL,
	PRIMARY KEY (`username`, `transit_date`, `transit_type`, `transit_route`),
	CONSTRAINT `fk13` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk16\4` FOREIGN KEY (`transit_type`, `transit_route`) REFERENCES `transit` (`transit_type`, `transit_route`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#14
CREATE TABLE `visit_site`(
	`visitor_username` varchar(20) NOT NULL,
`site_name` varchar(50) NOT NULL,
	`visit_site_date` date NOT NULL,
	PRIMARY KEY (`visitor_username`, `site_name`, `visit_site_date`),
	CONSTRAINT `fk15` FOREIGN KEY (`visitor_username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk16` FOREIGN KEY (`site_name`) REFERENCES `site` (`site_name`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;

#15
CREATE TABLE `visit_event`(
	`visitor_username` varchar(20) NOT NULL,
	`event_name` varchar(50) NOT NULL,
	`start_date` date NOT NULL,
	`site_name` varchar(50) NOT NULL,
	`visit_event_date` date NOT NULL,
	PRIMARY KEY (`visitor_username`, `start_date`, `event_name`,`visit_event_date`, `site_name`),
	CONSTRAINT `fk17` FOREIGN KEY (`visitor_username`) REFERENCES `user` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT `fk18` FOREIGN KEY (`event_name`, `start_date`, `site_name`) REFERENCES `event` (`event_name`, `start_date`, `site_name`) ON DELETE CASCADE ON UPDATE CASCADE
)
ENGINE=INNODB;


 INSERT INTO user values
('ray.cho','7cf2e5f72d3e144cad58f95214f2dd20ad8f9979f34d561433a31dacbc16071b','Approved','Ray','Cho','Employee, Visitor'),
('james.smith','7cf2e5f72d3e144cad58f95214f2dd20ad8f9979f34d561433a31dacbc16071b','Approved','James','Smith','Employee'),
('michael.smith','6d3a26d88ea77a9b07d79a48307644cd88976173f49f279fed04b681d713a541','Approved','Michael','Smith','Employee, Visitor'),
('robert.smith','232c98d6f01474e874341b78d28064ac6c318763dbf80b057e0ea116905c7fcc','Approved','Robert ','Smith','Employee'),
('maria.garcia','ddbdea14aecce91cd12172bce09e9b402a29ea0c2813dc35935095ead340cc35','Approved','Maria','Garcia','Employee, Visitor'),
('david.smith','f79704e124b997b32bd83c014b05c20413c6a3e928ec8083bf1872c82c025672','Approved','David','Smith','Employee'),
('manager1','380f9771d2df8566ce2bd5b8ed772b0bb74fd6457fb803ab2d267c394d89c750','Pending','Manager','One','Employee'),
('manager2','9d05b6092d975b0884c6ba7fadb283ced03da9822ebbd13cc6b6d1855a6495ec','Approved','Manager','Two','Employee, Visitor'),
('manager3','42385b24804a6609a2744d414e0bf945704427b256ab79144b9ba93f278dbea7','Approved','Manager','Three','Employee'),
('manager4','e3c0f6e574f2e758a4d9d271fea62894230126062d74fd6d474e2046837f9bce','Approved','Manager','Four','Employee, Visitor'),
('manager5','60c6fc387428b43201be7da60da59934acb080b254e4eebead657b54154fbeb1','Approved','Manager','Five','Employee, Visitor'),
('maria.rodriguez','c50218388d572cbe6aac09b33ceb5189608d5b9ede429b5a17562a17fdd547c4','Declined','Maria','Rodriguez','Visitor'),
('mary.smith','9ddbd60268ae6987437511066a2000f1f0017c23728700f9794628a9d3d33034','Approved','Mary','Smith','Visitor'),
('maria.hernandez','600d2690306308866676b4229d51e04857876021705362bf3b26b08a1f78f9cb','Approved','Maria','Hernandez','User'),
('staff1','02defbfb8190f9d0719ef7a23da2049bd2e61442bc14021a6d8a4ae35ca334b7','Approved','Staff','One','Employee'),
('staff2','6bd0987c664d5e7551004d30656ae1d12b9d262e2d128ba4200934b4116d96cd','Approved','Staff','Two','Employee, Visitor'),
('staff3','8857a879cbea64f2d20c6c1bfab505f4b23c06d28decb3b9ddc5426b75f469f1','Approved','Staff','Three','Employee, Visitor'),
('user1','90aae915da86d3b3a4da7a996bc264bfbaf50a953cbbe8cd3478a2a6ccc7b900','Pending','User','One','User'),
('visitor1','5c1e1b5c8936669bfe844210fb7ae7d3411dd9f41614d09ce9732dfc17c266bc','Approved','Visitor','One','Visitor');

INSERT INTO user_email VALUES
    ('ray.cho','woonglae@gmail.com'),
    ('james.smith','jsmith@gmail.com'),
    ('james.smith','jsmith@hotmail.com'),
    ('james.smith','jsmith@gatech.edu'),
    ('james.smith','jsmith@outlook.com'),
    ('michael.smith','msmith@gmail.com'),
    ('robert.smith','rsmith@hotmail.com'),
    ('maria.garcia','mgarcia@yahoo.com'),
    ('maria.garcia','mgarcia@gatech.edu'),
    ('david.smith','dsmith@outlook.com'),
    ('maria.rodriguez','mrodriguez@gmail.com'),
    ('mary.smith','mary@outlook.com'),
    ('maria.hernandez','mh@gatech.edu'),
    ('maria.hernandez','mh123@gmail.com'),
    ('manager1','m1@beltline.com'),
    ('manager2','m2@beltline.com'),
    ('manager3','m3@beltline.com'),
    ('manager4','m4@beltline.com'),
    ('manager5','m5@beltline.com'),
    ('staff1','s1@beltline.com'),
    ('staff2','s2@beltline.com'),
    ('staff3','s3@beltline.com'),
    ('user1','u1@beltline.com'),
    ('visitor1','v1@beltline.com');


INSERT INTO employee VALUES
    ('ray.cho',000000014,8018955159,'1032 Hemphill ave','Atlanta','GA',30318,'Admin'),
    ('james.smith',000000001,4043721234,'123 East Main Street','Rochester','NY',14604,'Admin'),
    ('michael.smith',000000002,4043726789,'350 Ferst Drive','Atlanta','GA',30332,'Staff'),
    ('robert.smith',000000003,1234567890,'123 East Main Street','Columbus','OH',43215,'Staff'),
    ('maria.garcia',000000004,7890123456,'123 East Main Street','Richland','PA',17987,'Manager'),
    ('david.smith',000000005,5124776435,'350 Ferst Drive','Atlanta','GA',30332,'Manager'),
    ('manager1',000000006,8045126767,'123 East Main Street','Rochester','NY',14604,'Manager'),
    ('manager2',000000007,9876543210,'123 East Main Street','Rochester','NY',14604,'Manager'),
    ('manager3',000000008,5432167890,'350 Ferst Drive','Atlanta','GA',30332,'Manager'),
    ('manager4',000000009,8053467565,'123 East Main Street','Columbus','OH',43215,'Manager'),
	('manager5',000000010,8031446782,'801 Atlantic Drive', 'Atlanta', 'GA', 30332, 'Manager'),
	('staff1', 000000011, 8024456765, '266 Ferst Drive Northwest', 'Atlanta', 'GA', 30332, 'Staff'),
	('staff2', 000000012, 8888888888, '266 Ferst Drive Northwest', 'Atlanta', 'GA', 30332, 'Staff'),
	('staff3', 000000013, 3333333333, '801 Atlantic Drive', 'Atlanta', 'GA', 30332, 'Staff');

INSERT INTO administrator VALUES
    ('ray.cho'),
	('james.smith');

INSERT INTO manager VALUES
	('maria.garcia'),
	('david.smith'),
	('manager1'),
	('manager2'),
	('manager3'),
	('manager4'),
    ('manager5');

INSERT INTO staff VALUE
	('staff1'),
	('staff2'),
	('staff3'),
    ('robert.smith'),
    ('michael.smith');

INSERT INTO visitor VALUES
    ('ray.cho'),
	('michael.smith'),
    ('maria.garcia'),
    ('manager2'),
	('manager4'),
	('manager5'),
	('maria.rodriguez'),
	('mary.smith'),    
	('staff2'),
	('staff3'),
	('visitor1');

INSERT INTO site VALUES
    ('Piedmont Park','400 Park Drive Northeast',30306,'Yes','manager2'),
    ('Atlanta Beltline Center','112 Krog Street Northeast',30307,'No','manager3'),
    ('Historic Fourth Ward Park','680 Dallas Street Northeast',30308,'Yes','manager4'),
    ('Westview Cemetery','1680 Westview Drive Southwest',30310,'No','manager5'),
    ('Inman Park',NULL,30307,'Yes','david.smith');

INSERT INTO event VALUES
    ('Eastside Trail','2019-02-04 00:00:00','Piedmont Park','2019-02-05 00:00:00',0,99999,1,'A combination of multi-use trail and linear greenspace, the Eastside Trail was the first finished section of the Atlanta BeltLine trail in the old rail corridor. The Eastside Trail, which was funded by a combination of public and private philanthropic sources, runs from the tip of Piedmont Park to Reynoldstown. More details at https://beltline.org/explore-atlanta-beltline-trails/eastside-trail/'),
    ('Eastside Trail','2019-02-04 00:00:00','Inman Park','2019-02-05 00:00:00',0,99999,1,'A combination of multi-use trail and linear greenspace, the Eastside Trail was the first finished section of the Atlanta BeltLine trail in the old rail corridor. The Eastside Trail, which was funded by a combination of public and private philanthropic sources, runs from the tip of Piedmont Park to Reynoldstown. More details at https://beltline.org/explore-atlanta-beltline-trails/eastside-trail/'),
    ('Eastside Trail','2019-03-01 00:00:00','Inman Park','2019-03-02 00:00:00',0,99999,1,'A combination of multi-use trail and linear greenspace, the Eastside Trail was the first finished section of the Atlanta BeltLine trail in the old rail corridor. The Eastside Trail, which was funded by a combination of public and private philanthropic sources, runs from the tip of Piedmont Park to Reynoldstown. More details at https://beltline.org/explore-atlanta-beltline-trails/eastside-trail/'),
    ('Eastside Trail','2019-02-13 00:00:00','Historic Fourth Ward Park','2019-02-14 00:00:00',0,99999,1,'A combination of multi-use trail and linear greenspace, the Eastside Trail was the first finished section of the Atlanta BeltLine trail in the old rail corridor. The Eastside Trail, which was funded by a combination of public and private philanthropic sources, runs from the tip of Piedmont Park to Reynoldstown. More details at https://beltline.org/explore-atlanta-beltline-trails/eastside-trail/'),
    ('Westside Trail','2019-02-18 00:00:00','Westview Cemetery','2019-02-21 00:00:00',0,99999,1,'The Westside Trail is a free amenity that offers a bicycle and pedestrian-safe corridor with a 14-foot-wide multi-use trail surrounded by mature trees and grasses thanks to Trees Atlanta’s Arboretum. With 16 points of entry, 14 of which will be ADA-accessible with ramp and stair systems, the trail provides numerous access points for people of all abilities. More details at: https://beltline.org/explore-atlanta-beltline-trails/westside-trail/'),
    ('Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00',25,6,2,'The Atlanta BeltLine Partnership’s tour program operates with a natural gas-powered, ADA accessible tour bus funded through contributions from 10th & Monroe, LLC, SunTrust Bank Trusteed Foundations – Florence C. and Harry L. English Memorial Fund and Thomas Guy Woolford Charitable Trust, and AGL Resources'),
    ('Bus Tour','2019-02-08 00:00:00','Inman Park','2019-02-10 00:00:00',25,6,2,'The Atlanta BeltLine Partnership’s tour program operates with a natural gas-powered, ADA accessible tour bus funded through contributions from 10th & Monroe, LLC, SunTrust Bank Trusteed Foundations – Florence C. and Harry L. English Memorial Fund and Thomas Guy Woolford Charitable Trust, and AGL Resources'),
    ('Private Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00',40,4,1,'Private tours are available most days, pending bus and tour guide availability. Private tours can accommodate up to 4 guests per tour, and are subject to a tour fee (nonprofit rates are available). As a nonprofit organization with limited resources, we are unable to offer free private tours. We thank you for your support and your understanding as we try to provide as many private group tours as possible. The Atlanta BeltLine Partnership’s tour program operates with a natural gas-powered, ADA accessible tour bus funded through contributions from 10th & Monroe, LLC, SunTrust Bank Trusteed Foundations – Florence C. and Harry L. English Memorial Fund and Thomas Guy Woolford Charitable Trust, and AGL Resources'),
    ('Arboretum Walking Tour','2019-02-08 00:00:00','Inman Park','2019-02-11 00:00:00',5,5,1,'Official Atlanta BeltLine Arboretum Walking Tours provide an up-close view of the Westside Trail and the Atlanta BeltLine Arboretum led by Trees Atlanta Docents. The one and a half hour tours step off at at 10am (Oct thru May), and 9am (June thru September). Departure for all tours is from Rose Circle Park near Brown Middle School. More details at: https://beltline.org/visit/atlanta-beltline-tours/#arboretum-walking'),
    ('Official Atlanta BeltLine Bike Tour','2019-02-09 00:00:00','Atlanta BeltLine Center','2019-02-14 00:00:00',5,5,1,'These tours will include rest stops highlighting assets and points of interest along the Atlanta BeltLine. Staff will lead the rides, and each group will have a ride sweep to help with any unexpected mechanical difficulties.');

INSERT INTO transit VALUES
    ('MARTA','Blue',2),
    ('Bus','152',2),
    ('Bike','Relay',1);

INSERT INTO connect VALUES
    ('Inman Park','MARTA','Blue'),
    ('Piedmont Park','MARTA','Blue'),
    ('Historic Fourth Ward Park','MARTA','Blue'),
    ('Westview Cemetery','MARTA','Blue'),
    ('Inman Park','Bus','152'),
    ('Piedmont Park','Bus','152'),
    ('Historic Fourth Ward Park','Bus','152'),
    ('Piedmont Park','Bike','Relay'),
    ('Historic Fourth Ward Park','Bike','Relay');

INSERT INTO take_transit VALUES
    ('manager2','MARTA','Blue','2019-03-20 00:00:00'),
    ('manager2','Bus','152','2019-03-20 00:00:00'),
    ('manager3','Bike','Relay','2019-03-20 00:00:00'),
    ('manager2','MARTA','Blue','2019-03-21 00:00:00'),
    ('maria.hernandez','Bus','152','2019-03-20 00:00:00'),
    ('maria.hernandez','Bike','Relay','2019-03-20 00:00:00'),
    ('manager2','MARTA','Blue','2019-03-22 00:00:00'),
    ('maria.hernandez','Bus','152','2019-03-22 00:00:00'),
    ('mary.smith','Bike','Relay','2019-03-23 00:00:00'),
    ('visitor1','MARTA','Blue','2019-03-21 00:00:00');

INSERT INTO assign_to VALUES
    ('michael.smith','Eastside Trail','2019-02-04 00:00:00','Piedmont Park'),
    ('staff1','Eastside Trail','2019-02-04 00:00:00','Piedmont Park'),
    ('robert.smith','Eastside Trail','2019-02-04 00:00:00','Inman Park'),
    ('staff2','Eastside Trail','2019-02-04 00:00:00','Inman Park'),
    ('staff1','Eastside Trail','2019-03-01 00:00:00','Inman Park'),
    ('michael.smith','Eastside Trail','2019-02-13 00:00:00','Historic Fourth Ward Park'),
    ('staff1','Westside Trail','2019-02-18 00:00:00','Westview Cemetery'),
    ('staff3','Westside Trail','2019-02-18 00:00:00','Westview Cemetery'),
    ('michael.smith','Bus Tour','2019-02-01 00:00:00','Inman Park'),
    ('staff2','Bus Tour','2019-02-01 00:00:00','Inman Park'),
    ('robert.smith','Bus Tour','2019-02-08 00:00:00','Inman Park'),
    ('michael.smith','Bus Tour','2019-02-08 00:00:00','Inman Park'),
    ('robert.smith','Private Bus Tour','2019-02-01 00:00:00','Inman Park'),
    ('staff3','Arboretum Walking Tour','2019-02-08 00:00:00','Inman Park'),
    ('staff1','Official Atlanta BeltLine Bike Tour','2019-02-09 00:00:00','Atlanta BeltLine Center');


INSERT INTO visit_event VALUES
    ('mary.smith','Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-01 00:00:00'),
    ('maria.garcia','Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00'),
    ('manager2','Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00'),
    ('manager4','Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-01 00:00:00'),
    ('manager5','Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00'),
    ('staff2','Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00'),
    ('mary.smith','Westside Trail','2019-02-18 00:00:00','Westview Cemetery','2019-02-19 00:00:00'),
    ('mary.smith','Private Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-01 00:00:00'),
    ('mary.smith','Private Bus Tour','2019-02-01 00:00:00','Inman Park','2019-02-02 00:00:00'),
    ('mary.smith','Official Atlanta BeltLine Bike Tour','2019-02-09 00:00:00','Atlanta BeltLine Center','2019-02-10 00:00:00'),
    ('mary.smith','Arboretum Walking Tour','2019-02-08 00:00:00','Inman Park','2019-02-10 00:00:00'),
    ('mary.smith','Eastside Trail','2019-02-04 00:00:00','Piedmont Park','2019-02-04 00:00:00'),
    ('mary.smith','Eastside Trail','2019-02-13 00:00:00','Historic Fourth Ward Park','2019-02-13 00:00:00'),
    ('mary.smith','Eastside Trail','2019-02-13 00:00:00','Historic Fourth Ward Park','2019-02-14 00:00:00'),
    ('visitor1','Eastside Trail','2019-02-13 00:00:00','Historic Fourth Ward Park','2019-02-14 00:00:00'),
    ('visitor1','Official Atlanta BeltLine Bike Tour','2019-02-09 00:00:00','Atlanta BeltLine Center','2019-02-10 00:00:00'),
    ('visitor1','Westside Trail','2019-02-18 00:00:00','Westview Cemetery','2019-02-19 00:00:00');


INSERT INTO visit_site VALUES
    ('mary.smith','Inman Park','2019-02-01 00:00:00'),
    ('mary.smith','Inman Park','2019-02-02 00:00:00'),
    ('mary.smith','Inman Park','2019-02-03 00:00:00'),
    ('mary.smith','Atlanta Beltline Center','2019-02-01 00:00:00'),
    ('mary.smith','Atlanta Beltline Center','2019-02-10 00:00:00'),
    ('mary.smith','Historic Fourth Ward Park','2019-02-02 00:00:00'),
    ('mary.smith','Piedmont Park','2019-02-02 00:00:00'),
    ('visitor1','Piedmont Park','2019-02-11 00:00:00'),
    ('visitor1','Atlanta Beltline Center','2019-02-13 00:00:00'),
    ('visitor1','Historic Fourth Ward Park','2019-02-11 00:00:00'),
    ('visitor1','Westview Cemetery','2019-02-06 00:00:00'),
    ('visitor1','Inman Park','2019-02-01 00:00:00'),
    ('visitor1','Piedmont Park','2019-02-01 00:00:00'),
    ('visitor1','Atlanta Beltline Center','2019-02-09 00:00:00');