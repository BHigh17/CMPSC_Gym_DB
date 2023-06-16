create table MEMBERSHIP
(
    MEMBERSHIPID int generated always as identity(start with 0, increment by 1)
        constraint MEMEBRSHIP_PK
            primary key,
    MEMPRICE int not null,
    MEMNAME VARCHAR(50) not null
);


create table MEMBER
(
	MEMBERID int generated always as identity(start with 1101, increment by 1)
		constraint MEMBER_PK
			primary key,
	FIRSTNAME VARCHAR(50) not null,
	LASTNAME VARCHAR(50) not null,
	STREETADDRESS VARCHAR(100) not null,
	CITY VARCHAR(50) not null,
	STATE VARCHAR(50),
	ZIP VARCHAR(5),
	PHONE CHAR(10),
	MEMBERSHIPID int

);

alter table MEMBER
    add constraint MEMBER_MEMBERSHIP_MEMBERSHIPID_FK
        foreign key (MEMBERSHIPID) references MEMBERSHIP;



create table CLASS
(
	CLASSID int not null
		constraint CLASS_PK
			primary key,
	CLNAME VARCHAR(50) not null,
	CLTIME VARCHAR(50) not null,
	CLSESSION VARCHAR(50) not null,
	CLMAXCAPACITY int
);
create table CLASSMEMBER
(
	REGISTRATIONID int not null
		constraint CLASSMEMBER_PK
			primary key,
	CLASSID int not null
		constraint CLASSMEMBER_CLASS_CLASSID_FK
			references CLASS,
	MEMBERID int not null
		constraint CLASSMEMBER_MEMBER_MEMBERID_FK
			references MEMBER,
	DATEREGISTRATION DATE not null
);


-- sample data for table population
INSERT INTO MEMBERSHIP(MEMBERSHIPID, MEMPRICE, MEMNAME) VALUES
(DEFAULT, 20,'Basic'),
(DEFAULT, 30, 'Basic+'),

(DEFAULT, 50, 'Athlete'),

(DEFAULT, 100, 'Extreme'),
(DEFAULT, 100, 'Ultimate Extreme Platinum');

insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Doralin', 'Smelley', '0675 Cherokee Court', 'San Jose', 'CA', '95160', '4086610635', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Shelton', 'Lyndon', '169 Onsgard Parkway', 'Houston', 'TX', '77212', '7137220177', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Ewen', 'Skoughman', '0732 Westridge Lane', 'El Paso', 'TX', '88589', '9151788275', 4);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Christan', 'Bone', '81 Bartillon Trail', 'Cincinnati', 'OH', '45243', '5136648634', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Preston', 'Parmley', '6995 Surrey Point', 'Birmingham', 'AL', '35244', '2059671548', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Diandra', 'Carefull', '544 Forster Junction', 'El Paso', 'TX', '79977', '9153806867', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Moria', 'Cassy', '962 Talmadge Way', 'Sacramento', 'CA', '95852', '9162481972', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Maxy', 'Dundridge', '87389 Northwestern Circle', 'Norwalk', 'CT', '06854', '2034116036', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Amitie', 'Vasilischev', '0 Comanche Junction', 'Tulsa', 'OK', '74126', '9188032479', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Modesta', 'Lintot', '038 Westport Street', 'Atlanta', 'GA', '30351', '4046962132', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Bobby', 'Rampling', '6777 Monica Avenue', 'Fresno', 'CA', '93721', '5592939470', 2);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Cyndi', 'Leaves', '7 Twin Pines Park', 'Sacramento', 'CA', '94237', '9165780351', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Titus', 'Holburn', '1 Elmside Pass', 'Pueblo', 'CO', '81005', '7195130324', 4);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Kristy', 'MacIlory', '0922 Di Loreto Terrace', 'Hartford', 'CT', '06105', '8603178289', 0);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Tarah', 'Riehm', '68 Melody Avenue', 'Charlotte', 'NC', '28247', '7047533264', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Astra', 'Veldens', '158 Sherman Place', 'Grand Forks', 'ND', '58207', '7015140788', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Noe', 'Caverhill', '12761 East Parkway', 'Houston', 'TX', '77030', '7131714226', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Donaugh', 'Earngy', '20 Merchant Road', 'Austin', 'TX', '78783', '5124186865', 0);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Menard', 'Youngman', '5791 Forest Dale Hill', 'Saint Petersburg', 'FL', '33710', '7274130212', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Mozelle', 'Fiander', '63193 Glacier Hill Trail', 'Denver', 'CO', '80223', '7204133337', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Marc', 'Gervaise', '0 Anhalt Road', 'Houston', 'TX', '77260', '7132959702', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Brittani', 'Faley', '869 Merry Drive', 'Humble', 'TX', '77346', '2813957249', 4);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Ali', 'Filip', '1797 8th Point', 'Buffalo', 'NY', '14233', '7161939362', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Delora', 'Keveren', '005 Golden Leaf Avenue', 'Hot Springs National Park', 'AR', '71914', '5011771289', 2);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Roger', 'Orlton', '3676 Manufacturers Alley', 'San Jose', 'CA', '95128', '7145245838', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Ronnica', 'Mitchinson', '63824 Express Circle', 'Fort Worth', 'TX', '76192', '6823409357', 2);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Fancie', 'Lethieulier', '47457 Twin Pines Trail', 'Shawnee Mission', 'KS', '66225', '9137337649', 2);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Curtice', 'Swaite', '550 Autumn Leaf Alley', 'Buffalo', 'NY', '14276', '7161426776', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Tabby', 'Woollons', '512 Porter Crossing', 'Cleveland', 'OH', '44111', '2164470765', 2);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Ian', 'Berge', '81 Prairieview Point', 'North Las Vegas', 'NV', '89036', '7022978857', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Trudie', 'Lattey', '8348 Ridgeway Terrace', 'Rochester', 'NY', '14646', '5854595677', 4);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Wendi', 'Cranage', '87 Sunbrook Center', 'Anchorage', 'AK', '99599', '9079316572', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Cash', 'Castelluzzi', '29124 Nevada Court', 'Houston', 'TX', '77245', '7138679050', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Tessy', 'Lett', '146 Rutledge Plaza', 'Brooklyn', 'NY', '11215', '6466114453', 0);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Sunny', 'Forbear', '95 Stang Center', 'Flushing', 'NY', '11388', '3478515002', 3);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Martino', 'Drinkale', '89507 International Junction', 'Huntington', 'WV', '25716', '3047101152', 1);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Whitman', 'McKay', '9881 Springview Parkway', 'Omaha', 'NE', '68197', '4028773781', 4);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Eddie', 'Gellion', '83 Main Trail', 'Sioux Falls', 'SD', '57188', '6058922687', 0);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Kalie', 'Sircombe', '56 Browning Center', 'Long Beach', 'CA', '90840', '5624310074', 0);
insert into MEMBER (MEMBERID, FIRSTNAME, LASTNAME, STREETADDRESS, CITY, STATE, ZIP, PHONE, MEMBERSHIPID) values (DEFAULT, 'Daryn', 'Hagwood', '558 Commercial Lane', 'Houston', 'TX', '77015', '7136860546', 2);

