insert into doctor (first_name, last_name, date_of_birth, cpf, email, pwd, phone_number, specialty) values ('João', 'Castro', '1980-10-03', '10120230311', 'joaocastro_med@email.com', '$2a$10$WNdFMwST6ZObyOv8NIYjoeRcqeAc54wvsl2X11gypfFhxbFNNYqj2', '2132326565', 'Cardiologia');
insert into doctor (first_name, last_name, date_of_birth, cpf, email, pwd, phone_number, specialty) values ('Fernanda', 'Vasconcelos', '1974-09-15', '84858293289', 'fervasconcelos_med@email.com', '$2a$10$GPTSzL.gsRgLN/ZhhnIaWOAqrT6Gn0oiFNmpHH6cSHbp119efv.EW', '31979892310', 'Neurologia');
insert into doctor (first_name, last_name, date_of_birth, cpf, email, pwd, phone_number, specialty) values ('Paulo', 'Carvalho', '1969-06-11', '54501175400', 'paulacarvalho_med@email.com', '$2a$10$/iIEzoeoeYug6b.K8yd10ea5ZPbakBZ.6Xbks86.q3X9c.5ZilOPy', '61982620207', 'Clínica geral');
insert into doctor (first_name, last_name, date_of_birth, cpf, email, pwd, phone_number, specialty) values ('Maria', 'Eduarda Nunes', '1966-06-21', '79668316983', 'dudanunes_med@email.com', '$2a$10$QWFAT5h3gVTMMkKRnSJjNuZgS4ty2lY8g4yaIoNmvSpaRf4ZoYA4q', '1130544184', 'Ortopedia');
insert into doctor (first_name, last_name, date_of_birth, cpf, email, pwd, phone_number, specialty) values ('Gabriela', 'Campos', '1984-09-25', '20485272644', 'gabrielacampos_med@email.com', '$2a$10$d9rVt56T9YlRu02.XMzt.uKQFM6twyFgwkxow0gmUp0Kj7MXCuKBS', '7136084495', 'Psiquiatria');
insert into doctor (first_name, last_name, date_of_birth, cpf, email, pwd, phone_number, specialty) values ('Antônio', 'Moura', '1988-06-03', '64558808704', 'antoniomoura_med@email.com', '$2a$10$c4aZRjr0s7FdbvvQtL4/c.LGzr83Tu8EyVOjOKOLaNGY8R70A1o3y', '21986961560', 'Pediatria');

insert into patient (patient_name, cpf) values ('Pedro Lima', '38919723001');
insert into patient (patient_name, cpf) values ('Raul Matos', '74655546751');
insert into patient (patient_name, cpf) values ('Isabele Costa', '34732141161');
insert into patient (patient_name, cpf) values ('Felipe Barbosa', '45252167354');
insert into patient (patient_name, cpf) values ('Lucas Melo', '27815419070');
insert into patient (patient_name, cpf) values ('Carolina Peixoto', '76618580617');

insert into attendance (date_time, doctor_id, patient_id) values ('2020-01-01 15:20:00', '1', '1');
insert into attendance (date_time, doctor_id, patient_id) values ('2020-02-27 11:40:00', '1', '4');
insert into attendance (date_time, doctor_id, patient_id) values ('2020-02-12 13:10:00', '1', '3');
insert into attendance (date_time, doctor_id, patient_id) values ('2020-02-12 13:45:00', '1', '5');
insert into attendance (date_time, doctor_id, patient_id) values ('2020-06-19 17:20:00', '2', '3');
insert into attendance (date_time, doctor_id, patient_id) values ('2020-12-04 18:10:00', '2', '4');
insert into attendance (date_time, doctor_id, patient_id) values ('2020-12-04 13:50:00', '3', '1');