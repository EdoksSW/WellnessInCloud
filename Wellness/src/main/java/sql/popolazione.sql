INSERT INTO admin(email, password, codicefiscale, nome, cognome, datanascita) VALUES
('admin.giangi@wellness.com', 'password', 'MRSRSS80A01H501U', 'Gianluca', 'Vassallucci', '2000-05-13'),
('admin.mery@wellness.com', 'password', 'LGBNCH85B02H501V', 'Maria', 'Teresa', '2002-04-23');

INSERT INTO staff(email, password, codicefiscale, nome, cognome, telefono, datanascita, via, civico, cap, iban, ruolo, qualifica) VALUES
('train.luke@wellness.com', 'staff123', 'GNNVRD90C03H501W', 'Matteo', 'Prignone', '3331234567', '1990-03-10', 'Via Enea', 10, '80134', 'IT12A3456789012345678901234', 'PERSONAL_TRAINER', 'Laurea: Scienze Motorie'),
('train.annie@wellness.com', 'password', 'NNANRI92D04H501X', 'Anna', 'Lica', '3387785674','2002-04-23', 'Via Scarlatti', 20, '80145', 'IT98B7654321098765432109876', 'RECEPTIONIST', 'Diploma');

INSERT INTO cliente(email, password, codicefiscale, nome, cognome, telefono, datanascita, via, civico, cap, stato_account) VALUES
('cliente.marco@gmail.com', 'user123', 'MRCMAR95E05H501Y', 'Marco', 'Martini', '3401122334', '1995-05-12', 'Via Napoli', 5, '80121', 'ATTIVO'),
('cliente.sara@gmail.com', 'user123', 'SRASRT98F06H501Z', 'Sara', 'Sarti', '3405566778', '1998-06-18', 'Corso Garibaldi', 112, '80122', 'ATTIVO'),
('cliente.luca@gmail.com', 'user123', 'LCALCU01G07H501A', 'Luca', 'Lucarelli', '3409988776', '2001-07-25', 'Piazza Municipio', 1, '80132', 'SCADUTO');
