# 1.where子句可否使用组函数进行过滤?
# 不可以

# 2.查询公司员工工资的最大值，最小值，平均值，总和
SELECT MAX(salary), MIN(salary), AVG(salary), SUM(salary)
FROM employees;

# 3.查询各job_id的员工工资的最大值，最小值，平均值，总和
SELECT emp.job_id, MAX(salary), MIN(salary), AVG(salary), SUM(salary)
FROM employees emp JOIN jobs j
ON emp.`job_id` = j.`job_id`
GROUP BY j.`job_id`;

# 4.选择具有各个job_id的员工人数
SELECT j.job_id, COUNT(j.job_id) 
FROM employees emp JOIN jobs j
ON emp.`job_id` = j.`job_id`
GROUP BY j.`job_id`;

# 5.查询员工最高工资和最低工资的差距（DIFFERENCE）
SELECT  MAX(salary), MIN(salary), MAX(salary) - MIN(salary) AS difference
FROM employees;

# 6.查询各个管理者手下员工的最低工资，其中最低工资不能低于6000，没有管理者的员工不计算在内
SELECT mgr.`employee_id`, MIN(emp.salary) AS min_sal
FROM employees emp JOIN employees mgr
ON emp.`manager_id` = mgr.`employee_id`
WHERE emp.`manager_id` IS NOT NULL
GROUP BY mgr.`employee_id`
HAVING min_sal >= 6000;

# 7.查询所有部门的名字，location_id，员工数量和平均工资，并按平均工资降序
SELECT dept.department_name, dept.location_id, COUNT(*), AVG(emp.salary) AS dept_avg_sal
FROM departments dept JOIN employees emp
ON emp.department_id = dept.department_id
GROUP BY dept.department_id
ORDER BY dept_avg_sal DESC;

# 8.查询每个工种、每个部门的部门名、工种名和最低工资
SELECT dept.department_name, emp.job_id, MIN(emp.salary) AS min_sal_in_job
FROM employees emp JOIN departments dept
ON emp.department_id = dept.department_id
GROUP BY dept.department_name, job_id;

