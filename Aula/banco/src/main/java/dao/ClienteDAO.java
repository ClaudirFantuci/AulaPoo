package dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Cliente;

public class ClienteDAO {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

    public Cliente inserir(Cliente cliente) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(cliente);
        em.getTransaction().commit();
        em.close();
        return cliente;
    }

    public List<Cliente> buscarPorCpf(String cpf) {
        EntityManager em = emf.createEntityManager();
        List<Cliente> clientes = null;
        try {
            clientes = em.createQuery("from Cliente where cpf='" + cpf + "'", Cliente.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.close();
        }
        return clientes;
    }
}
