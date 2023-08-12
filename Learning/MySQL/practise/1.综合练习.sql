# 0 前置工作
# 1. 创建emp表
CREATE TABLE
IF
	NOT EXISTS emp (
		empno INT ( 10 ) NOT NULL UNIQUE AUTO_INCREMENT COMMENT "员工号",
		ename VARCHAR ( 20 ) NOT NULL COMMENT "员工姓名",
		job VARCHAR ( 20 ) NOT NULL COMMENT "员工职位",
		mgr INT ( 10 ) NOT NULL COMMENT "员工所属管理者",
		sal DECIMAL ( 10, 2 ) NOT NULL COMMENT "员工薪水",
		deptno INT ( 10 ) NOT NULL COMMENT "员工所属部门号",
		PRIMARY KEY ( empno ),
#		FOREIGN KEY ( mgr ) REFERENCES emp ( empno ),
		FOREIGN KEY ( deptno ) REFERENCES dept ( deptno ) 
	);
CREATE TABLE
IF
	NOT EXISTS dept (
		deptno INT ( 10 ) NOT NULL UNIQUE AUTO_INCREMENT COMMENT "部门号",
		dname VARCHAR ( 20 ) NOT NULL COMMENT "部门名称",
		loc VARCHAR ( 20 ) NOT NULL COMMENT "部门所处地理位置",
		PRIMARY KEY ( deptno ) 
	);
	
# 2. 插入假数据
-- 插入"dept"表的SQL假数据
INSERT INTO dept ( dname, loc )
VALUES
	( '财务部', '北京' ),
	( '人力资源部', '上海' ),
	( '销售部', '深圳' ),
	( '研发部', '广州' ),
	( '市场部', '南京' ),
	( '客服部', '成都' ),
	( '物流部', '杭州' ),
	( '品控部', '武汉' ),
	( '采购部', '重庆' ),
	( '设计部', '天津' );
-- 插入"emp"表的SQL假数据
INSERT INTO emp ( ename, job, mgr, sal, deptno )
VALUES
	( '张三', '经理', NULL, 12000.50, 1 ),
	( '李四', '副经理', 1, 8000.00, 1 ),
	( '王五', '人事经理', 1, 9000.00, 2 ),
	( '赵六', '招聘专员', 3, 5000.00, 2 ),
	( '钱七', '薪资福利专员', 3, 7000.00, 2 ),
	( '孙八', '销售总监', 1, 15000.00, 3 ),
	( '周九', '销售经理', 6, 11000.00, 3 ),
	( '吴十', '销售代表', 7, 6000.00, 3 ),
	( '郑十一', '技术总监', 1, 18000.00, 4 ),
	( '王十二', '技术经理', 9, 12000.00, 4 ),
	( '赵十三', '架构师', 10, 15000.00, 4 ),
	( '钱十四', '程序员', 10, 8000.00, 4 ),
	( '孙十五', '测试工程师', 10, 7000.00, 4 ),
	( '周十六', '市场总监', 1, 17000.00, 5 ),
	( '吴十七', '市场经理', 14, 10000.00, 5 ),
	( '郑十八', '市场专员', 17, 6000.00, 5 ),
	( '李十九', '客服经理', 1, 11000.00, 6 ),
	( '刘二十', '客服代表', 18, 5000.00, 6 ),
	( '陈二十一', '物流经理', 1, 10000.00, 7 ),
	( '张二十二', '货运专员', 20, 5000.00, 7 ),
	( '王二十三', '品控经理', 1, 13000.00, 8 ),
	( '赵二十四', '品控专员', 23, 6000.00, 8 ),
	( '钱二十五', '采购经理', 1, 14000.00, 9 ),
	( '孙二十六', '采购专员', 25, 6000.00, 9 ),
	( '周二十七', '设计总监', 1, 16000.00, 10 ),
	( '吴二十八', '设计经理', 27, 11000.00, 10 ),
	( '郑二十九', '美工设计师', 28, 8000.00, 10 );
	
	
# 1. 列出emp表中各部门的部门号，最高工资，最低工资
SELECT deptno, MAX(sal), MIN(sal)
FROM emp
GROUP BY deptno;

# 2. 列出emp表中各部门job 含’经理’的员工的部门号，最低工资，最高工资
SELECT deptno, MAX(sal) AS max_sal, MIN(sal) AS min_sal
FROM emp
WHERE job LIKE "%经理%"
GROUP BY deptno;

# 3. 查询emp中最低工资小于7000的部门中job为'品控专员'的员工的部门号，最低工资，最高工资
SELECT deptno, MAX(sal) AS max_sal, MIN(sal) AS min_sal
FROM emp
WHERE job = "品控专员"
GROUP BY deptno
HAVING min_sal < 7000;

# 4. 写出对上题的另一解决方法
# （请补充）
# 5. 根据部门号由高而低，工资由低而高列出每个员工的姓名，部门号，工资
SELECT ename, deptno, sal
FROM emp
ORDER BY deptno DESC, sal ASC;

# 6. 列出'张三'所在部门中每个员工的姓名与部门号
SELECT ename, deptno
FROM emp
WHERE deptno IN (
	SELECT deptno
	FROM emp
	WHERE ename = "张三"
);

# 7. 列出每个员工的姓名，工作，部门号，部门名
SELECT emp.ename, emp.job, emp.deptno, dept.dname
FROM emp JOIN dept
ON emp.deptno = dept.deptno;

# 8. 列出emp中工作为'美工设计师'的员工的姓名，工作，部门号，部门名
SELECT emp.ename, emp.job, emp.deptno, dept.dname
FROM emp JOIN dept
ON emp.deptno = dept.deptno
WHERE job = "美工设计师"; 

# 9. 对于emp中有管理者的员工，列出姓名，管理者姓名（管理者外键为mgr）
SELECT e.ename AS "员工姓名", m.ename AS "管理员姓名"
FROM emp AS e JOIN emp m
ON e.mgr = m.empno;

# 10. 对于dept表中，列出所有部门名，部门号，同时列出各部门工作为'产品经理'的员工名与工作
SELECT emp.ename, emp.job, dept.dname, dept.deptno
FROM emp JOIN dept
ON emp.deptno = dept.deptno
WHERE job = "美工设计师"

# 11. 对于工资高于本部门平均水平的员工，列出部门号，姓名，工资，按部门号排序
SELECT dept.deptno, AVG(sal)
FROM emp JOIN dept
ON emp.deptno = dept.deptno
GROUP BY dept.deptno;

SELECT emp_outer.deptno, emp_outer.ename, emp_outer.sal
FROM emp AS emp_outer
WHERE sal > (
	SELECT AVG(emp_inner.sal)
	FROM emp AS emp_inner
	WHERE emp_inner.deptno = emp_outer.deptno
	GROUP BY emp_inner.deptno
)

# 12. 对于emp，列出各个部门中工资高于本部门平均水平的员工数和部门号，按部门号排序
SELECT emp_outer.deptno, emp_outer.ename, emp_outer.sal
FROM emp AS emp_outer
WHERE sal > (
	SELECT AVG(emp_inner.sal)
	FROM emp AS emp_inner
	WHERE emp_inner.deptno = emp_outer.deptno
	GROUP BY emp_inner.deptno
)
ORDER BY deptno;

# 13. 对于emp中工资高于本部门平均水平，人数多于1人的，列出其部门号及高于部门平均工资的人数，并按部门号排序
SELECT dept.deptno AS "部门号", dept.dname AS "部门名", COUNT(*) AS "人数"
FROM emp AS emp_outer JOIN dept
ON emp_outer.deptno = dept.deptno
WHERE sal > (
	SELECT AVG(emp_inner.sal)
	FROM emp AS emp_inner
	WHERE emp_inner.deptno = emp_outer.deptno
	GROUP BY emp_inner.deptno
)
GROUP BY emp_outer.deptno
HAVING COUNT(*) > 1;

# 14. 对于emp中工资高于本部门平均水平，且其多于平均工资人数多于3人的，列出部门号，部门总人数，按部门号排序
SELECT dept.deptno AS "部门号", COUNT(*) AS "总人数"
FROM emp AS emp_outer JOIN dept
ON emp_outer.deptno = dept.deptno
WHERE emp_outer.deptno IN (
	SELECT emp_inner.deptno
	FROM emp AS emp_inner JOIN dept
	ON emp_inner.deptno = dept.deptno
	WHERE emp_inner.sal > (
		SELECT AVG(sal)
		FROM emp AS emp_inner_inner
		WHERE emp_inner_inner.deptno = emp_inner.deptno
		GROUP BY dept.deptno
	)
	GROUP BY dept.deptno
	HAVING COUNT(*) >= 2
)
GROUP BY dept.deptno
ORDER BY emp_outer.deptno;

# 15. 对于emp中低于自己工资至少5人的员工，列出其部门号，姓名，工资，以及工资少于自己的人数
SELECT emp_outer.deptno AS "部门号", emp_outer.ename AS "员工名", emp_outer.sal AS "员工工资", (
	SELECT COUNT(*) AS cnt
	FROM (
		SELECT emp_inner.empno
		FROM emp AS emp_inner
		WHERE emp_outer.sal > emp_inner.sal
	) AS lt_sal_cnt
) AS "工资少于自己的人数"
FROM emp AS emp_outer
WHERE (
	SELECT COUNT(*)
	FROM (
		SELECT emp_inner.empno
		FROM emp AS emp_inner
		WHERE emp_outer.sal > emp_inner.sal
	) AS lt_sal_cnt
) > 5;

