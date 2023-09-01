INSERT INTO applicant(
created_date,
updated_date,
job_id,
trainer_id,
support_amount
)
values
(
null,
 null,
(select job_id from job ORDER BY job_id DESC LIMIT 1),
1,
30000
 );