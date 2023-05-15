package hr.fer.oprpp2.hw05.dao.jpa;

import javax.persistence.EntityManagerFactory;

/**
 * Provider for Entity Manager Factory
 *
 */
public class JPAEMFProvider {
	
	public static EntityManagerFactory emf;
	
	public static EntityManagerFactory getEmf() {
		return emf;
	}
	
	public static void setEmf(EntityManagerFactory emf) {
		JPAEMFProvider.emf = emf;
	}

}
