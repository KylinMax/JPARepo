package com.kylin.jap.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;

import com.kylin.jpa.entities.Department;
import com.kylin.jpa.entities.Manager;
import com.kylin.jpa.entities.Order;
import com.kylin.jpa.entities.Person;
import com.kylin.jpa.entities.School;
import com.kylin.jpa.entities.Student;
import com.sun.org.apache.xalan.internal.xsltc.dom.SAXImpl.NamespaceWildcardIterator;

public class Test {
	// 1.创建EntityManagerFactory
	EntityManagerFactory entityManagerFactory;
	// 2.创建EntityManager
	EntityManager entityManager;
	// 3.开启事务
	EntityTransaction transaction;

	@Before
	public void init() {
		// persistence.xml文件中的<persistence-unit>标签的值
		String persistenceUnitName = "JPAStudy";
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@After
	public void destroy() {
		// 5.提交事务
		transaction.commit();
		// 6.关闭EntityManager
		entityManager.close();
		// 7.关闭EntityManagerFactory
		entityManagerFactory.close();
	}

	/******************** one2one ********************************/

	// 默认情况下，若获取不维护关联关系的一方，则会通过左外连接获取关联对象
	//可以通过@OneToOne的fetch属性来修改加密策略，但依然会再发送SQL语句来初始化其关联的对象
	// 说明在不维护关联关系的一方， 不建议修改fetch属性
	@org.junit.Test
	public void testOne2OneFind2() {
		Manager manager = entityManager.find(Manager.class, 1);
		System.out.println(manager.getDepartment().getClass().getName());
	}

	// 默认情况下，若获取维护关联关系的一方，则会通过左外连接获取关联对象
	// 可以通过OneToOne的fetch属性修改加载方式
	@org.junit.Test
	public void testOne2OneFind() {
		Department department = entityManager.find(Department.class, 1);
		System.out.println(department.getManager().getClass().getName());
	}

	// 双向one2one的关联关系，建议先保存不维护关联关系的一方， 这样不会多出update语句
	@org.junit.Test
	public void testOne2OnePersist() {
		Manager manager = new Manager();
		manager.setName("kylin");
		Department department = new Department();
		department.setDepartmentName("finicial");

		// 设置关联属性
		manager.setDepartment(department);
		department.setManager(manager);

		// 执行插入操作
		entityManager.persist(manager);
		entityManager.persist(department);

	}

	/******************** 双向1对多 *********************************/
	// 若是双向1对多的关联关系， 执行保存时
	// 若先保存n的一端， 在保存1的一端， 默认情况下， 会多出n调Update语句
	// 若先报存1的一端， 再保存多的一端， 则会多出n调Update语句
	// 在进行双向1对多关联关系时， 建议使用多的一方来维护关联关系， 而1的一方不维护关联关系，这样会有效减少sql语句

	@org.junit.Test
	public void testBiDirectionalOneToMany() {
		Set<Student> students = new HashSet<>();
		Student student = new Student("aaa", 33);
		Student student2 = new Student("vvv", 44);
		students.add(student);
		students.add(student2);
		School school = new School();
		school.setSchoolName("ustc");
		school.setStudents(students);
		student.setSchool(school);
		student2.setSchool(school);
		entityManager.persist(school);
		entityManager.persist(student);
		entityManager.persist(student2);
	}

	/********************* OneToMany ******************************/
	@org.junit.Test
	public void testOneToManyUpdate() {
		School school = entityManager.find(School.class, 3);
		school.getStudents().iterator().next().setName("sb");
	}

	// 默认情况下， 若删除1的一端， 则会先把关联的n的一端置空
	// 可以通过@OneToMany的cascade属性来修改默认的删除策略。
	@org.junit.Test
	public void testOneToManyRemove() {
		School school = entityManager.find(School.class, 2);
		System.out.println(school);
		entityManager.remove(school);
	}

	// 默认对关联多的一方使用懒加载的加载策略
	// 可以使用@OneToMany的fetch属性来修改默认的加载策略
	@org.junit.Test
	public void testOneToManyFind() {
		School school = entityManager.find(School.class, 2);
		System.out.println(school);
		Iterator<Student> iterator = (Iterator<Student>) school.getStudents().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	// 单项一对多关联关系执行保存时， 一定会多出Update语句
	// 因为n的一点在插入时不会同时插入外键列
	@org.junit.Test
	public void testOneToManyPersist() {
		Set<Student> students = new HashSet<>();
		Student student1 = new Student("kylin", 33);
		Student student2 = new Student("john", 22);
		students.add(student1);
		students.add(student2);
		School school = new School("uestc", students);

		entityManager.persist(student1);
		entityManager.persist(student2);
		entityManager.persist(school);

	}

	/********************** ManyToOne ***************************/
	@org.junit.Test
	public void tsetManyToOneUpdate() {
		Order order = entityManager.find(Order.class, 2);
		order.getPerson().setLastName("alan");
	}

	@org.junit.Test
	public void testManyToOneRemove() {
		// 可以删除n的一端
		// Order order = entityManager.find(Order.class, 1);
		// entityManager.remove(order);

		// 不能直接删除1的一端，因为有外键约束
		Person person = entityManager.find(Person.class, 5);
		entityManager.remove(person);
	}

	// 默认情况下，使用左外链接的方式来获取n的一端的对象和其关联的1的一端的对象
	// 可以使用@ManyToOne的fetch属性来修改默认的关联属性的加载策略
	@org.junit.Test
	public void testManyToOneFind() {
		Order order = entityManager.find(Order.class, 1);
		System.err.println(order.getOrderName());
		System.err.println(order.getPerson().getLastName());
	}

	/**
	 * 保存多对一时，建议先保存1的一端，然后保存n的一端，这样不会多出额外的Update语句
	 */
	@org.junit.Test
	public void testManyToOnePersist() {
		Person person = new Person("customer", 33, "customer@gmail.com", new Date(), new Date());
		Order order1 = new Order("food-order", person);
		Order order2 = new Order("drink-order", person);

		// 执行保存操作
		entityManager.persist(person);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}

	/*********************************** refresh ******************************/
	/**
	 * jpa 中的reflush 同 hibernate 中 Session 的 refresh 方法.
	 * 
	 * reflush 会强制发送sql查询（select）语句，使缓存中的数据和数据库中的数据保持一致，数据由数据库到缓存 flush
	 * 会强制发送sql更新（update）语句，使数据库中的数据和缓存中的数据保持一致，数据由缓存到数据库
	 * 
	 * 注意：当对缓存中的数据进行一系列操作后，一般提交事务时，会调用flush方法，把数据库更新一下
	 * 但在commit或flush之前调用reflush，那么缓存中的数据又变成了和数据中的数据一样的了，你原先修改的数据白费了
	 */
	@org.junit.Test
	public void testRefresh() {
		Person person = entityManager.find(Person.class, 4);
		person.setLastName("gina");
		// 发送select将数据库中的数据同步到缓存中的对象，等于上一步操作没有修改
		entityManager.refresh(person);
	}

	/************************************ flush *******************************/
	/**
	 * jpa中的flush 同 hibernate 中 Session 的 flush 方法.
	 * 默认情况下，在提交事务的时候会刷新缓存（即调用flush方法）
	 * 
	 * 手动调用，会立刻强制发送sql更新（update）语句，使数据库中的数据和缓存中的数据保持一致 但数据库中的记录还没有变，因为还没有提交事务
	 * 
	 */
	@org.junit.Test
	public void testFlush() {
		Person person = entityManager.find(Person.class, 4);
		person.setLastName("Bob");
		entityManager.flush();
	}

	/***************************** merge ****************************************/

	/*
	 * 总的来说：类似于hibernate中Session的saveOrUpdate方法
	 * 
	 */

	// 若传入的是一个游离对象， 即传入的对象由OID
	// 1. 若在EntityManager缓存中有对象
	// 2. JPA会把游离对象属性复制到查询到的EntityManager缓存的对象中。
	// 3. 对EntityManager缓存中的对象进行Update
	@org.junit.Test
	public void testMerge4() {
		Person person = new Person("aaa", 33, "aaa@gmail.com", new Date(), new Date());
		person.setId(4);

		Person person2 = entityManager.find(Person.class, 1);
		entityManager.merge(person);
	}

	// 若传入的是一个游离对象， 即传入的对象由OID
	// 1. 若在EntityManager缓存中没有对象
	// 2. 若在数据库中也有对应的记录
	// 3. JPA会查询对应的记录，然后返回记录对应的对象， 然后再把游离对象的属性复制到查询到的对象中。
	// 4. 对查询到的对象执行update操作
	@org.junit.Test
	public void testMerge3() {
		Person person = new Person("kobe", 33, "kobe@gmail.com", new Date(), new Date());
		person.setId(4);

		Person person2 = entityManager.merge(person);
		System.out.println(person);
		System.out.println(person2);
		System.out.println(person == person2);
	}

	// 若传入的是一个游离对象， 即传入的对象由OID
	// 1. 若在EntityManager缓存中没有对象
	// 2. 若在数据库中也没有对应的记录
	// 3. JPA会创建一个新的对象， 然后把当前游离对象的属性复制到新的对象中
	// 4. 对新创建的对象执行insert 操作
	@org.junit.Test
	public void testMerge2() {
		Person person = new Person("tomas", 33, "tomas@gmail.com", new Date(), new Date());
		person.setId(100);

		Person person2 = entityManager.merge(person);
		System.out.println("person#id:" + person.getId());
		System.out.println("person2#id" + person2.getId());
	}

	// 1. 若传入的是一个临时对象
	// 会创建一个新的对象， 会把临时对象的属性复制到新的对象中， 然后对新的对象执行持久化操作，所以新的对象中有id，以前的临时对象中没有id
	@org.junit.Test
	public void testMerge1() {
		Person person = new Person("smith", 22, "smith@gmail.com", new Date(), new Date());
		Person person2 = entityManager.merge(person);
		System.out.println("person#id:" + person.getId());
		System.out.println("person2#id:" + person2.getId());
	}

	/***************************** remove ****************************************/
	// 类似于hibernate的delete方法，把对象对应的记录从数据库中移除
	// 注意：该方法只能移除持久化对象，而hibernate的delete还可以移除游离对象。
	@org.junit.Test
	public void testRemove() {
		// Person person = new Person();
		// person.setId(2);
		// entityManager.remove(person);

		Person person = entityManager.find(Person.class, 2);
		System.out.println(person);
		entityManager.remove(person);
	}

	/***************************** persist ****************************************/
	// 类似于hibernate类的save方法， 使对象由临时状态变为持久化状态
	// 和hibernate的save方法的不同之处： 若对象由id，则不能执行insert操作，而会抛出异常。
	@org.junit.Test
	public void testPersistence() {
		Person person = new Person("john", 22, "aa@email.com", new Date(), new Date());

		entityManager.persist(person);
		System.out.println(person.getId());
	}

	/***************************** getReference ****************************************/
	// 类似于hibernate中session的load方法
	@org.junit.Test
	public void testGetReference() {
		Person person = entityManager.getReference(Person.class, 1);
		System.out.println(person.getClass().getName());
		System.out.println(person);
	}

	/***************************** find ****************************************/
	@org.junit.Test
	public void testFind() {
		Person person = entityManager.find(Person.class, 1);
		System.out.println(person);

	}
}
