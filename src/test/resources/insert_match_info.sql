INSERT INTO match_info(created_date, updated_date, applicant_id, job_id, status)
values
(
null,
null,
(select applicant_id from applicant ORDER BY applicant_id DESC LIMIT 1),
(select job_id from job ORDER BY job_id DESC LIMIT 1),
1);