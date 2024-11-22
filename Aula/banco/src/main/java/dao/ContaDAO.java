package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Cliente;
import entidade.Conta;
import entidade.Movimentacao;

public class ContaDAO {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

	public Conta inserir(Conta conta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(conta);
		em.getTransaction().commit();
		em.close();
		return conta;
	}

	public static List<Conta> buscarPorIdCliente(Conta conta) {
		EntityManager em = emf.createEntityManager();
		Cliente cliente = conta.getCliente();
		long id = cliente.getId();
		List<Conta> contas = null;
		try {
			contas = em.createQuery("from Conta where id_cliente='" + id + "'", Conta.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return contas;
	}
}