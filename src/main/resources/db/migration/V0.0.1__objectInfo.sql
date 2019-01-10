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
    worth　INT,
    name TEXT,
    FOREIGN KEY(type_id) REFERENCES objectinfo_types(id),
    PRIMARY KEY(id, type_id)
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

INSERT INTO objectinfo_types
(id, name)
VALUES
(1,'armor'),
(2,'ring');

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
(1,8,'plate mail',5,150);

