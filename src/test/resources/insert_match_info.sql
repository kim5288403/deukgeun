INSERT INTO match_info(created_date, updated_date, applicant_id, job_posting_id, status)
values
(
null,
null,
(select applicant_id from applicant ORDER BY applicant_id DESC LIMIT 1),
(select job_posting_id from job_posting ORDER BY job_posting_id DESC LIMIT 1),
1);