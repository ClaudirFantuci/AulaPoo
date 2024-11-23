package dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class DAOGenerico<T> {
	private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");
	private final Class<T> classeEntidade;
	
	public DAOGenerico(Class<T> classe) {
		this.classeEntidade = classe;
	}
	
	protected EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
	
	public T inserir(T objeto) {
		EntityManager em = getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(objeto);
			em.getTransaction().commit();
			return objeto;
		}finally {
			em.close();
		}
	}
}