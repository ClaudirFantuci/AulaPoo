package dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Conta;

public class ContaDAO {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

	public Conta inserir(Conta conta) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(conta);
		em.getTransaction().commit();
		em.close();
		return conta;
	}

	public Conta alterar(Conta conta) {
		Conta contaBanco = null;
		if (conta.getId() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			contaBanco = buscarPorId(conta.getId());

			if (contaBanco != null) {
				contaBanco.setDescricao(conta.getDescricao());
				em.merge(contaBanco);
			}

			em.getTransaction().commit();
			em.close();
		}
		return contaBanco;
	}

	public void excluir(Long id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Conta conta = em.find(Conta.class, id);
		if (conta != null) {
			em.remove(conta);
		}
		em.getTransaction().commit();
		em.close();
	}

	public List<Conta> listarTodos() {
		EntityManager em = emf.createEntityManager();
		// hql: hibernate query language
		List<Conta> contas = null;
		try {
			contas = em.createQuery("from Conta", Conta.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return contas;
	}
	// buscar todas as contas de acordo com o CPF
	// buscar todas as contas de acordo com o tipo da transação

	public List<Conta> buscarPorCpf(String cpf) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Conta where cpf_correntista='" + cpf + "'", Conta.class);
		em.close();
		return query.getResultList();
	}

	public List<Conta> buscarPorCpfTipoTransacao(String cpf, String tipoTransacao) {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createQuery(
					"from Conta where cpf_correntista='" + cpf + "' and tipo_transacao='" + tipoTransacao + "'",
					Conta.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Conta> buscarPorCpfPeriodico(String cpf, String inicio, String fim) {
		EntityManager em = emf.createEntityManager();
		try {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date dataFinal = formato.parse(fim);
			Date dataInicio = formato.parse(inicio);

			Query query = em.createQuery(
					"from Conta where cpf_correntista = :cpf and dataTransacao between :dataInicio and :dataFinal",
					Conta.class);
			query.setParameter("cpf", cpf);
			query.setParameter("dataInicio", dataInicio);
			query.setParameter("dataFinal", dataFinal);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Conta> buscarPorCpfMes(String cpf, String dia) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formato2 = new SimpleDateFormat("MM/yyyy");
			Date primeiroDia = formato2.parse("1/" + dia);
			Date dataTransacao = formato.parse(dia);

			Query query = em.createQuery(
					"from Conta where cpf_correntista = :cpf and dataTransacao LIKE :dataTransacao", Conta.class);
			query.setParameter("cpf", cpf);
			query.setParameter("dataTransacao", dataTransacao);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Conta> buscarPorCpfDia(String cpf, String dia) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dataTransacao = dateFormat.parse(dia);

			Query query = em.createQuery(
					"from Conta where cpf_correntista = :cpf and dataTransacao = :dataTransacao", Conta.class);
			query.setParameter("cpf", cpf);
			query.setParameter("dataTransacao", dataTransacao);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Conta> buscarPorCpfDiaTipoTransacao(String cpf, String dia, String tipoTransacao) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dataTransacao = dateFormat.parse(dia);

			Query query = em.createQuery(
					"from Conta where cpf_correntista = :cpf and dataTransacao = :dataTransacao and tipo_transacao = :tipoTransacao",
					Conta.class);
			query.setParameter("cpf", cpf);
			query.setParameter("dataTransacao", dataTransacao);
			query.setParameter("tipoTransacao", tipoTransacao);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public Conta buscarPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		Conta conta = em.find(Conta.class, id);
		em.close();
		return conta;
		// return em.find(Conta.class, id);
	}

	public List<Conta> buscarPorCpfData(String cpfCorrentista, Date date) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorCpfData'");
	}
}
