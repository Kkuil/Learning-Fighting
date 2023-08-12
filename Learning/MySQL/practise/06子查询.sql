#1.查询和Zlotkey相同部门的员工姓名和工资
SELECT last_name, salary
FROM employees
WHERE department_id = (
	SELECT department_id
	FROM employees
	WHERE last_name = "Zlotkey"
);

#2.查询工资比公司平均工资高的员工的员工号，姓名和工资。
SELECT employee_id, last_name, salary
FROM employees
WHERE salary > (
	SELECT AVG(salary)
	FROM employees
)
ORDER BY salary;

#3.选择工资大于所有JOB_ID = 'SA_MAN'的员工的工资的员工的last_name, job_id, salary
SELECT last_name, job_id, salary
FROM employees
WHERE salary > ALL (
	SELECT salary 
	FROM employees
	WHERE job_id = "SA_MAN"
);

#4.查询和姓名中包含字母u的员工在相同部门的员工的员工号和姓名
SELECT employee_id, last_name 
FROM employees 
WHERE department_id IN (
	SELECT department_id
	FROM employees
	WHERE last_name LIKE "%u%"
);

#5.查询在部门的location_id为1700的部门工作的员工的员工号
SELECT employee_id, last_name
FROM employees
WHERE department_id IN (
	SELECT department_id
	FROM departments
	WHERE location_id = 1700
);

#6.查询管理者是King的员工姓名和工资
-- SELECT * FROM employees WHERE last_name = "King";
SELECT last_name, salary
FROM employees
WHERE manager_id IN (
	SELECT manager_id 
	FROM employees
	WHERE last_name = "King"
);

#7.查询工资最低的员工信息: last_name, salary
SELECT last_name, salary
FROM employees
WHERE salary <= ALL (
	SELECT MIN(salary)
	FROM employees
);

#8.查询平均工资最低的部门信息
SELECT dept.department_id dept_id, dept.department_name dept_name, AVG(emp.salary) AS dept_avg
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
GROUP BY dept.`department_id`
HAVING dept_avg <= ALL (
	SELECT AVG(salary)
	FROM employees emp JOIN departments dept
	ON emp.`department_id` = dept.`department_id`
	GROUP BY dept.`department_id`
);



#9.查询平均工资最低的部门信息和该部门的平均工资（相关子查询）
SELECT dept.department_id dept_id, dept.department_name dept_name, AVG(emp.salary) AS dept_avg
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
GROUP BY dept.`department_id`
HAVING dept_avg <= ALL (
	SELECT AVG(salary)
	FROM employees emp JOIN departments dept
	ON emp.`department_id` = dept.`department_id`
	GROUP BY dept.`department_id`
); 

#10.查询平均工资最高的 job 信息
SELECT jobs.job_id, jobs.job_title, AVG(emp.salary) AS job_min_avg
FROM employees emp JOIN jobs
ON emp.job_id = jobs.job_id
GROUP BY jobs.job_id
HAVING job_min_avg <= ALL (
	SELECT AVG(emp.salary) AS job_avg
	FROM employees emp JOIN jobs
	ON emp.job_id = jobs.job_id
	GROUP BY jobs.job_id
	ORDER BY job_avg
);

#11.查询平均工资高于公司平均工资的部门有哪些?
SELECT dept.department_id, dept.department_name, AVG(emp.salary) dept_avg
FROM employees emp JOIN departments dept
ON emp.department_id = dept.department_id
GROUP BY dept.department_id
HAVING dept_avg > (
	SELECT AVG(salary)
	FROM employees
);

#12.查询出公司中所有 manager 的详细信息
SELECT employee_id, last_name
FROM employees
WHERE employee_id in (
	SELECT DISTINCT(manager_id) 
	FROM employees 
	WHERE manager_id IS NOT NULL
);

#13.各个部门中 最高工资中最低的那个部门的 最低工资是多少?
SELECT MIN(dept_avg)
FROM (
	SELECT AVG(emp.salary) AS dept_avg
	FROM employees emp JOIN departments dept
	ON emp.department_id = dept.department_id
	GROUP BY dept.department_id
) AS t_dept_avg;

#14.查询平均工资最高的部门的 manager 的详细信息: last_name, department_id, email, salary
SELECT emp.last_name, emp.department_id, emp.email, emp.salary
FROM employees emp JOIN departments dept
ON emp.department_id = dept.department_id
WHERE dept.department_id = (
	SELECT dept.department_id
	FROM employees emp JOIN departments dept
	ON emp.department_id = dept.department_id
	GROUP BY dept.department_id
	HAVING AVG(emp.salary) = (
		SELECT MAX(dept_avg)
		FROM (
			SELECT dept.department_id, AVG(emp.salary) AS dept_avg
			FROM employees emp JOIN departments dept
			ON emp.department_id = dept.department_id
			GROUP BY dept.department_id
		) AS t_dept_avg
	)
);


#15. 查询部门的部门号，其中不包括job_id是"ST_CLERK"的部门号
SELECT DISTINCT(dept.department_id)
FROM departments dept JOIN employees emp
ON dept.department_id = emp.department_id
JOIN jobs
ON emp.job_id = jobs.job_id
WHERE jobs.job_id <> "ST_CLEAK"
ORDER BY dept.department_id;


#16. 选择所有没有管理者的员工的last_name
SELECT last_name
FROM employees
WHERE manager_id is NULL;

#17．查询员工号、姓名、雇用时间、工资，其中员工的管理者为 'De Haan'
SELECT employee_id, last_name, hire_date, salary
FROM employees
WHERE manager_id = (
	SELECT employee_id
	FROM employees
	WHERE last_name = "De Haan"
);

#18.查询各部门中工资比本部门平均工资高的员工的员工号, 姓名和工资（相关子查询）
SELECT employee_id, last_name, salary, department_id
FROM employees AS emp_outer
WHERE salary >= ALL (
	SELECT AVG(emp_inner.salary)
	FROM employees emp_inner JOIN departments dept
	ON emp_inner.department_id = dept.department_id
	WHERE dept.department_id = emp_outer.department_id
	GROUP BY dept.department_id
);

#19.查询每个部门下的部门人数大于 5 的部门名称（相关子查询）
SELECT department_name, COUNT(*) AS dept_count
FROM employees emp JOIN departments dept
ON emp.department_id = dept.department_id
GROUP BY dept.department_id
HAVING dept_count > 5;

#20.查询每个国家下的部门个数大于 2 的国家编号（相关子查询）
SELECT r.region_id, COUNT(*) AS reg_count
FROM departments dept JOIN locations l
ON dept.location_id = l.location_id
JOIN countries c
ON l.country_id = c.country_id
JOIN regions r
ON c.region_id = r.region_id
GROUP BY r.region_id
HAVING reg_count > 2;
