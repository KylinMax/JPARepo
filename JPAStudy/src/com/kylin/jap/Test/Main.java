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
		// 1.����EntityManagerFactory

		// persistence.xml�ļ��е�<persistence-unit>��ǩ��ֵ
		String persistenceUnitName = "JPAStudy";
		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);

		// 2.����EntityManager

		EntityManager entityManager = entityManagerFactory.createEntityManager();

		// 3.��������

		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		// 4.���г־û�����
		Person person = new Person("kylin", 22, "kylin@gmail.com", new Date(), new Date());
		entityManager.persist(person);
		
//		PkGenerator  pkGenerator = new PkGenerator("PERSON_ID", 11);
//		entityManager.persist(pkGenerator);

		// 5.�ύ����
		entityTransaction.commit();

		// 6.�ر�EntityManager
		entityManager.close();

		// 7.�ر�EntityManagerFactory
		entityManagerFactory.close();
	}
}
