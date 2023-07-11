INSERT INTO match_info(created_date, updated_date, applicant_id, job_posting_id, status)
values
(null, null, (select applicant_id from applicant where job_posting_id = 123), 123, 1);