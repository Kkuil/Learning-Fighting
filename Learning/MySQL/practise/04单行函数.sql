# 1.显示系统时间(注：日期+时间)
SELECT NOW() FROM DUAL;

# 2.查询员工号，姓名，工资，以及工资提高百分之20%后的结果（new salary）
SELECT employee_id, last_name, salary, salary * 1.2
FROM employees;

# 3.将员工的姓名按首字母排序，并写出姓名的长度（length）
SELECT last_name, LENGTH(last_name)
FROM employees
ORDER BY LENGTH(last_name) DESC;

# 4.查询员工id,last_name,salary，并作为一个列输出，别名为OUT_PUT
SELECT CONCAT(employee_id, last_name, salary)
FROM employees;

# 5.查询公司各员工工作的年数、工作的天数，并按工作年数的降序排序
SELECT 
emp.employee_id, 
emp.last_name, 
DATEDIFF(IF(ISNULL(jh.end_date), CURDATE(), jh.end_date), emp.hire_date) work_days, 
ROUND(DATEDIFF(IF(ISNULL(jh.end_date), CURDATE(), jh.end_date), emp.hire_date) / 365) work_years 
FROM employees emp LEFT JOIN job_history jh 
ON emp.`employee_id` = jh.`employee_id` 
ORDER BY work_years DESC;

# 6.查询员工姓名，hire_date , department_id，满足以下条件：雇用时间在1997年之后，department_id为80 或 90 或110, commission_pct不为空
SELECT emp.last_name, emp.hire_date, emp.department_id
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE YEAR(emp.`hire_date`) > 1997 AND emp.department_id IN (80, 90, 110) AND emp.commission_pct IS NOT NULL;

# 7.查询公司中入职超过10000天的员工姓名、入职时间
SELECT emp.last_name, emp.hire_date, IF(ISNULL(jh.end_date), CURDATE(), jh.end_date) end_date,  DATEDIFF(IF(ISNULL(jh.end_date), CURDATE(), jh.end_date), emp.hire_date) work_days
FROM employees emp JOIN job_history jh
ON emp.`employee_id` = jh.`employee_id`
WHERE DATEDIFF(IF(ISNULL(jh.end_date), CURDATE(), jh.end_date), emp.hire_date) > 1000;
 

# 8.做一个查询，产生下面的结果
SELECT last_name, salary, salary * 3 trible_sal
FROM employees;








