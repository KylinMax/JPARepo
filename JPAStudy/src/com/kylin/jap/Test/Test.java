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
	// 1.����EntityManagerFactory
	EntityManagerFactory entityManagerFactory;
	// 2.����EntityManager
	EntityManager entityManager;
	// 3.��������
	EntityTransaction transaction;

	@Before
	public void init() {
		// persistence.xml�ļ��е�<persistence-unit>��ǩ��ֵ
		String persistenceUnitName = "JPAStudy";
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
		transaction = entityManager.getTransaction();
		transaction.begin();
	}

	@After
	public void destroy() {
		// 5.�ύ����
		transaction.commit();
		// 6.�ر�EntityManager
		entityManager.close();
		// 7.�ر�EntityManagerFactory
		entityManagerFactory.close();
	}

	/******************** one2one ********************************/

	// Ĭ������£�����ȡ��ά��������ϵ��һ�������ͨ���������ӻ�ȡ��������
	//����ͨ��@OneToOne��fetch�������޸ļ��ܲ��ԣ�����Ȼ���ٷ���SQL�������ʼ��������Ķ���
	// ˵���ڲ�ά��������ϵ��һ���� �������޸�fetch����
	@org.junit.Test
	public void testOne2OneFind2() {
		Manager manager = entityManager.find(Manager.class, 1);
		System.out.println(manager.getDepartment().getClass().getName());
	}

	// Ĭ������£�����ȡά��������ϵ��һ�������ͨ���������ӻ�ȡ��������
	// ����ͨ��OneToOne��fetch�����޸ļ��ط�ʽ
	@org.junit.Test
	public void testOne2OneFind() {
		Department department = entityManager.find(Department.class, 1);
		System.out.println(department.getManager().getClass().getName());
	}

	// ˫��one2one�Ĺ�����ϵ�������ȱ��治ά��������ϵ��һ���� ����������update���
	@org.junit.Test
	public void testOne2OnePersist() {
		Manager manager = new Manager();
		manager.setName("kylin");
		Department department = new Department();
		department.setDepartmentName("finicial");

		// ���ù�������
		manager.setDepartment(department);
		department.setManager(manager);

		// ִ�в������
		entityManager.persist(manager);
		entityManager.persist(department);

	}

	/******************** ˫��1�Զ� *********************************/
	// ����˫��1�Զ�Ĺ�����ϵ�� ִ�б���ʱ
	// ���ȱ���n��һ�ˣ� �ڱ���1��һ�ˣ� Ĭ������£� ����n��Update���
	// ���ȱ���1��һ�ˣ� �ٱ�����һ�ˣ� �����n��Update���
	// �ڽ���˫��1�Զ������ϵʱ�� ����ʹ�ö��һ����ά��������ϵ�� ��1��һ����ά��������ϵ����������Ч����sql���

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

	// Ĭ������£� ��ɾ��1��һ�ˣ� ����Ȱѹ�����n��һ���ÿ�
	// ����ͨ��@OneToMany��cascade�������޸�Ĭ�ϵ�ɾ�����ԡ�
	@org.junit.Test
	public void testOneToManyRemove() {
		School school = entityManager.find(School.class, 2);
		System.out.println(school);
		entityManager.remove(school);
	}

	// Ĭ�϶Թ������һ��ʹ�������صļ��ز���
	// ����ʹ��@OneToMany��fetch�������޸�Ĭ�ϵļ��ز���
	@org.junit.Test
	public void testOneToManyFind() {
		School school = entityManager.find(School.class, 2);
		System.out.println(school);
		Iterator<Student> iterator = (Iterator<Student>) school.getStudents().iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
		}
	}

	// ����һ�Զ������ϵִ�б���ʱ�� һ������Update���
	// ��Ϊn��һ���ڲ���ʱ����ͬʱ���������
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
		// ����ɾ��n��һ��
		// Order order = entityManager.find(Order.class, 1);
		// entityManager.remove(order);

		// ����ֱ��ɾ��1��һ�ˣ���Ϊ�����Լ��
		Person person = entityManager.find(Person.class, 5);
		entityManager.remove(person);
	}

	// Ĭ������£�ʹ���������ӵķ�ʽ����ȡn��һ�˵Ķ�����������1��һ�˵Ķ���
	// ����ʹ��@ManyToOne��fetch�������޸�Ĭ�ϵĹ������Եļ��ز���
	@org.junit.Test
	public void testManyToOneFind() {
		Order order = entityManager.find(Order.class, 1);
		System.err.println(order.getOrderName());
		System.err.println(order.getPerson().getLastName());
	}

	/**
	 * ������һʱ�������ȱ���1��һ�ˣ�Ȼ�󱣴�n��һ�ˣ����������������Update���
	 */
	@org.junit.Test
	public void testManyToOnePersist() {
		Person person = new Person("customer", 33, "customer@gmail.com", new Date(), new Date());
		Order order1 = new Order("food-order", person);
		Order order2 = new Order("drink-order", person);

		// ִ�б������
		entityManager.persist(person);
		entityManager.persist(order1);
		entityManager.persist(order2);
	}

	/*********************************** refresh ******************************/
	/**
	 * jpa �е�reflush ͬ hibernate �� Session �� refresh ����.
	 * 
	 * reflush ��ǿ�Ʒ���sql��ѯ��select����䣬ʹ�����е����ݺ����ݿ��е����ݱ���һ�£����������ݿ⵽���� flush
	 * ��ǿ�Ʒ���sql���£�update����䣬ʹ���ݿ��е����ݺͻ����е����ݱ���һ�£������ɻ��浽���ݿ�
	 * 
	 * ע�⣺���Ի����е����ݽ���һϵ�в�����һ���ύ����ʱ�������flush�����������ݿ����һ��
	 * ����commit��flush֮ǰ����reflush����ô�����е������ֱ���˺������е�����һ�����ˣ���ԭ���޸ĵ����ݰ׷���
	 */
	@org.junit.Test
	public void testRefresh() {
		Person person = entityManager.find(Person.class, 4);
		person.setLastName("gina");
		// ����select�����ݿ��е�����ͬ���������еĶ��󣬵�����һ������û���޸�
		entityManager.refresh(person);
	}

	/************************************ flush *******************************/
	/**
	 * jpa�е�flush ͬ hibernate �� Session �� flush ����.
	 * Ĭ������£����ύ�����ʱ���ˢ�»��棨������flush������
	 * 
	 * �ֶ����ã�������ǿ�Ʒ���sql���£�update����䣬ʹ���ݿ��е����ݺͻ����е����ݱ���һ�� �����ݿ��еļ�¼��û�б䣬��Ϊ��û���ύ����
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
	 * �ܵ���˵��������hibernate��Session��saveOrUpdate����
	 * 
	 */

	// ���������һ��������� ������Ķ�����OID
	// 1. ����EntityManager�������ж���
	// 2. JPA�������������Ը��Ƶ���ѯ����EntityManager����Ķ����С�
	// 3. ��EntityManager�����еĶ������Update
	@org.junit.Test
	public void testMerge4() {
		Person person = new Person("aaa", 33, "aaa@gmail.com", new Date(), new Date());
		person.setId(4);

		Person person2 = entityManager.find(Person.class, 1);
		entityManager.merge(person);
	}

	// ���������һ��������� ������Ķ�����OID
	// 1. ����EntityManager������û�ж���
	// 2. �������ݿ���Ҳ�ж�Ӧ�ļ�¼
	// 3. JPA���ѯ��Ӧ�ļ�¼��Ȼ�󷵻ؼ�¼��Ӧ�Ķ��� Ȼ���ٰ������������Ը��Ƶ���ѯ���Ķ����С�
	// 4. �Բ�ѯ���Ķ���ִ��update����
	@org.junit.Test
	public void testMerge3() {
		Person person = new Person("kobe", 33, "kobe@gmail.com", new Date(), new Date());
		person.setId(4);

		Person person2 = entityManager.merge(person);
		System.out.println(person);
		System.out.println(person2);
		System.out.println(person == person2);
	}

	// ���������һ��������� ������Ķ�����OID
	// 1. ����EntityManager������û�ж���
	// 2. �������ݿ���Ҳû�ж�Ӧ�ļ�¼
	// 3. JPA�ᴴ��һ���µĶ��� Ȼ��ѵ�ǰ�����������Ը��Ƶ��µĶ�����
	// 4. ���´����Ķ���ִ��insert ����
	@org.junit.Test
	public void testMerge2() {
		Person person = new Person("tomas", 33, "tomas@gmail.com", new Date(), new Date());
		person.setId(100);

		Person person2 = entityManager.merge(person);
		System.out.println("person#id:" + person.getId());
		System.out.println("person2#id" + person2.getId());
	}

	// 1. ���������һ����ʱ����
	// �ᴴ��һ���µĶ��� �����ʱ��������Ը��Ƶ��µĶ����У� Ȼ����µĶ���ִ�г־û������������µĶ�������id����ǰ����ʱ������û��id
	@org.junit.Test
	public void testMerge1() {
		Person person = new Person("smith", 22, "smith@gmail.com", new Date(), new Date());
		Person person2 = entityManager.merge(person);
		System.out.println("person#id:" + person.getId());
		System.out.println("person2#id:" + person2.getId());
	}

	/***************************** remove ****************************************/
	// ������hibernate��delete�������Ѷ����Ӧ�ļ�¼�����ݿ����Ƴ�
	// ע�⣺�÷���ֻ���Ƴ��־û����󣬶�hibernate��delete�������Ƴ��������
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
	// ������hibernate���save������ ʹ��������ʱ״̬��Ϊ�־û�״̬
	// ��hibernate��save�����Ĳ�֮ͬ���� ��������id������ִ��insert�����������׳��쳣��
	@org.junit.Test
	public void testPersistence() {
		Person person = new Person("john", 22, "aa@email.com", new Date(), new Date());

		entityManager.persist(person);
		System.out.println(person.getId());
	}

	/***************************** getReference ****************************************/
	// ������hibernate��session��load����
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
