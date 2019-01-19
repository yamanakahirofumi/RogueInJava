CREATE TABLE objectinfo_types(
    id INT PRIMARY KEY,
    name TEXT
);



CREATE TABLE player(
    id INT AUTO_INCREMENT PRIMARY KEY,
    name TEXT
);

CREATE TABLE objectinfo (
    id INT,
    type_id INT,
    probability INT,
    worth INT,
    name TEXT,
    PRIMARY KEY (id, type_id),
    FOREIGN KEY (type_id) REFERENCES objectinfo_types(id)
);

CREATE TABLE objectinfo_knowledge(
    info_id INT,
    player_id INT,
    knowledge BOOLEAN,
    guessed TEXT,
    FOREIGN KEY (info_id) REFERENCES objectinfo(id),
    FOREIGN KEY (player_id) REFERENCES player(id),
    PRIMARY KEY(info_id, player_id)
);

CREATE TABLE armors (
    id INTEGER,
    info_id INT,
    name TEXT,
    FOREIGN KEY (info_id) REFERENCES objectinfo(id),
    PRIMARY KEY(id)
);

-- 武器の初期データ
CREATE TABLE init_weapons (
    id INTEGER,
    damage TEXT,
    hurling_damage TEXT,
    launch INT,
    flags INT,
    PRIMARY KEY(id)
);


--
INSERT INTO objectinfo_types
(id, name)
VALUES
(1,'armor'),
(2,'ring'),
(3,'scroll'),
(4,'weapon'),
(5,'stick');

INSERT INTO objectinfo
(type_id, id, name, probability, worth)
VALUES
(1,1,'leather armor',20,20),
(1,2,'ring mail',15,25),
(1,3,'studded leather armor',15,20),
(1,4,'scale mail',13,30),
(1,5,'chain mail',12,75),
(1,6,'splint mail',10,80),
(1,7,'banded mail',10,90),
(1,8,'plate mail',5,150),
(2,9,'protection',9,400),
(2,10,'add strength',9,400),
(2,11,'sustain strength',5,280),
(2,12,'searching',10,420),
(2,13,'see invisible',10,310),
(2,14,'adornment',1,10),
(2,15,'aggravate monster',10,10),
(2,16,'dexterity',8,440),
(2,17,'increase damage',8,400),
(2,18,'regeneration',4,460),
(2,19,'slow digestion',9,240),
(2,20,'teleportation',5,30),
(2,21,'stealth',7,470),
(2,22,'maintain armor',5, 80);

INSERT INTO init_weapons
(id, damage, hurling_damage, launch, flags)
VALUES
(0,'2x4','1x3',-1,0),
(1,'3x4','1x2',-1,0),
(2,'1x1','1x1',-1,0),
(3,'1x1','2x3',2,12),
(4,'1x6','1x4',-1,12),
(5,'4x4','1x2',-1,0),
(6,'1x1','1x3',-1,12),
(7,'1x2','2x4',-1,12),
(8,'2x3','1x6',-1,4);
