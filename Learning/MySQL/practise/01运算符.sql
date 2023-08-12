# 选择工资不在5000-12000的员工姓名和工资
SELECT last_name, salary from employees where salary NOT BETWEEN 5000 AND 12000;

# 选择在20或50号部门工作的员工姓名和部门号
SELECT first_name, department_id from employees where department_id IN (20, 50);

# 选择公司中没有管理者的员工姓名及job_id
SELECT first_name, job_id from employees where manager_id IS NULL;

# 选择公司中有奖金的员工姓名，工资和奖金级别
SELECT last_name, salary, commission_pct from employees WHERE commission_pct IS NOT NULL;

# 选择公司中员工姓名的第三个字母是a的员工姓名
SELECT first_name, last_name from employees WHERE first_name LIKE "__a%" or last_name LIKE "__a%";

# 选择姓名中含有a和k的员工姓名
SELECT first_name, last_name 
from employees 
WHERE first_name LIKE "%a%k%" OR first_name LIKE "%k%a%"
OR last_name LIKE "%a%k%" OR last_name LIKE "%k%a%";

# 显示出表employees表中first_name以“e”结尾的员工姓名
SELECT first_name, last_name
FROM employees
WHERE first_name LIKE "%e"
OR last_name LIKE "%e";

# 查询员工的部门编号在80-100之间的姓名和工种
SELECT first_name, last_name, job_id
FROM employees
WHERE department_id BETWEEN 80 AND 100;

# 查询manager_id为100,101,110的员工姓名，工资和管理者id
SELECT first_name, last_name, salary, manager_id
FROM employees
WHERE manager_id IN (100, 101, 110);






