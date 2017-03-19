package com.kylin.jap.Test;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.kylin.jpa.entities.Person;
import com.kylin.jpa.entities.PkGenerator;

public class Main {
	public static void main(String[] args) {
		// 1.创建EntityManagerFactory

		// persistence.xml文件中的<persistence-unit>标签的值
		String persistenceUnitName = "JPAStudy";
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

		// 2.创建EntityManager

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// 3.开启事务

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		// 4.进行持久化操作
		Person person = new Person("kylin", 22, "kylin@gmail.com", new Date(), new Date());
		entityManager.persist(person);
		
//		PkGenerator  pkGenerator = new PkGenerator("PERSON_ID", 11);
//		entityManager.persist(pkGenerator);

		// 5.提交事务
		entityTransaction.commit();

		// 6.关闭EntityManager
		entityManager.close();

		// 7.关闭EntityManagerFactory
		entityManagerFactory.close();
	}
}
