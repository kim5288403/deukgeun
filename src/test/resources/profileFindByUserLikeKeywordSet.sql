INSERT INTO trainer_member(
created_date,
updated_date,
detail_address,
email,
extra_address,
gender,
group_name,
group_status,
introduction,
jibun_address,
name,
password,
postcode,
price,
road_address
)
values
(null,
 null,
'test',
'test',
'test',
'M',
'test',
'Y',
'test',
'test',
'test',
'test',
'test',
0,
'test'
 );

INSERT INTO trainer_profile(created_date, updated_date, member_id, path)
values
(null, null, (select member_id from trainer_member where name = 'test'), 'test1');

INSERT INTO trainer_profile(created_date, updated_date, member_id, path)
values
(null, null, (select member_id from trainer_member where name = 'test'), 'test2');