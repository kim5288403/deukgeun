INSERT INTO applicant(
created_date,
updated_date,
job_posting_id,
trainer_id,
support_amount
)
values
(
null,
 null,
(select job_posting_id from job_posting ORDER BY job_posting_id DESC LIMIT 1),
1,
30000
 );