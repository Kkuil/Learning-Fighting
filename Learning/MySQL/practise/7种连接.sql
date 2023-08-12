# 内连接
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp JOIN departments dept
ON emp.`department_id` = dept.`department_id`;


# 左外连接
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp LEFT JOIN departments dept
ON emp.`department_id` = dept.`department_id`;

# 右外连接
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp RIGHT JOIN departments dept
ON emp.`department_id` = dept.`department_id`;

# 在左外连接的基础上去除公共部分
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp LEFT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE dept.`department_id` IS NULL;

# 在右外连接的基础上去除公共部分
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp RIGHT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE emp.`department_id` IS NULL;

# 全连接
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp LEFT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
UNION ALL
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp RIGHT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE emp.`department_id` IS NULL;

# 全连接的基础上去除内连接部分
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp LEFT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE dept.`department_id` IS NULL
UNION ALL
SELECT emp.employee_id, emp.last_name, dept.department_id, dept.department_name
FROM employees emp RIGHT JOIN departments dept
ON emp.`department_id` = dept.`department_id`
WHERE emp.`department_id` IS NULL;



