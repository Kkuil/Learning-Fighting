# 1.显示所有员工的姓名，部门号和部门名称。
SELECT emp.last_name, emp.department_id, dept.department_name
FROM employees emp LEFT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE dept.`department_id` IS NULL
UNION ALL
SELECT emp.last_name, emp.department_id, dept.department_name
FROM employees emp RIGHT JOIN departments dept
ON emp.`department_id` = dept.`department_id`;

# 2.查询90号部门员工的job_id和90号部门的location_id
SELECT emp.job_id, locations.location_id
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
JOIN locations
ON dept.`location_id` = locations.`location_id`
JOIN jobs
ON emp.`job_id` = jobs.`job_id`
WHERE dept.`department_id` = 90;

# 3.选择所有有奖金的员工的 last_name , department_name , location_id , city
SELECT emp.last_name, dept.department_name, locations.location_id, locations.city
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
JOIN locations
ON dept.`location_id` = locations.`location_id`
WHERE emp.`commission_pct` IS NOT NULL;

# 4.选择city在Toronto工作的员工的 last_name , job_id , department_id , department_name
SELECT emp.last_name, emp.job_id, dept.department_id, dept.department_name
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
JOIN locations
ON dept.`location_id` = locations.`location_id`
WHERE locations.city = "Toronto";

# 5.查询员工所在的部门名称、部门地址、姓名、工作、工资，其中员工所在部门的部门名称为’Executive’
SELECT dept.department_name, locations.street_address, emp.last_name, jobs.job_title, emp.salary
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
JOIN locations
ON dept.`location_id` = locations.`location_id`
JOIN jobs
ON emp.`job_id` = jobs.`job_id`
WHERE dept.`department_name` = "Executive"; 

# 6.查询指定员工的姓名，员工号，以及他的管理者的姓名和员工号，结果类似于下面的格式
employees Emp# manager Mgr#
kochhar 101 
king 100
SELECT emp.last_name, emp.employee_id, mgr.last_name mgr_name, mgr.job_id mgr_id
FROM employees emp JOIN employees mgr
ON emp.`manager_id` = mgr.`employee_id`
-- WHERE emp.last_name = "king";

# 7.查询哪些部门没有员工
SELECT emp.last_name, dept.department_name 
FROM employees emp RIGHT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE emp.`department_id` IS NULL;

# 8. 查询哪个城市没有部门
SELECT locations.city, dept.`department_id`
FROM locations LEFT JOIN departments dept
ON locations.`location_id` = dept.`location_id`
WHERE dept.`department_id` IS NULL;

# 9. 查询部门名为 Sales 或 IT 的员工信息
SELECT emp.last_name, emp.department_id, dept.department_name
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE dept.`department_name`  IN ("Sales", "IT");

