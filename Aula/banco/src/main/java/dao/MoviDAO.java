package dao;

import java.util.List;

import javax.persistence.EntityManager;

import javax.persistence.Query;

import entidade.Movimentacao;

public class MoviDAO extends DAOGenerico<Movimentacao> {

	public MoviDAO() {
		super(Movimentacao.class);
	}
	
public List<Movimentacao> buscarPorCPF(String cpf){
	EntityManager em = getEntityManager();
	try {
		Query query = em.createQuery("from Movimentacao where conta.cliente.cpf='"+cpf+"'");
		return query.getResultList();
	}finally {
		em.clear();
	}
}
}
