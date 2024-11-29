package dao;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entidade.Movimentacao;

public class MovimentacaoDAO {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoPU");

	public Movimentacao inserir(Movimentacao movimentacao) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		em.persist(movimentacao);
		em.getTransaction().commit();
		em.close();
		return movimentacao;
	}

	public Movimentacao alterar(Movimentacao movimentacao) {
		Movimentacao movimentacaoBanco = null;
		if (movimentacao.getId() != null) {
			EntityManager em = emf.createEntityManager();
			em.getTransaction().begin();

			movimentacaoBanco = buscarPorId(movimentacao.getId());

			if (movimentacaoBanco != null) {
				movimentacaoBanco.setDescricao(movimentacao.getDescricao());
				em.merge(movimentacaoBanco);
			}

			em.getTransaction().commit();
			em.close();
		}
		return movimentacaoBanco;
	}

	public void excluir(Long id) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Movimentacao movimentacao = em.find(Movimentacao.class, id);
		if (movimentacao != null) {
			em.remove(movimentacao);
		}
		em.getTransaction().commit();
		em.close();
	}

	public List<Movimentacao> listarTodos() {
		EntityManager em = emf.createEntityManager();
		// hql: hibernate query language
		List<Movimentacao> movimentacaos = null;
		try {
			movimentacaos = em.createQuery("from Movimentacao", Movimentacao.class).getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return movimentacaos;
	}
	// buscar todas as movimentacaos de acordo com o CPF
	// buscar todas as movimentacaos de acordo com o tipo da transação

	public List<Movimentacao> buscarPorCpf(String cpf) {
		EntityManager em = emf.createEntityManager();
		Query query = em.createQuery("from Movimentacao where cpf_correntista='" + cpf + "'", Movimentacao.class);
		em.close();
		return query.getResultList();
	}

	public List<Movimentacao> buscarPorCpfTipoTransacao(String cpf, String tipoTransacao) {
		EntityManager em = emf.createEntityManager();
		try {
			Query query = em.createQuery(
					"from Movimentacao where cpf_correntista='" + cpf + "' and tipo_transacao='" + tipoTransacao + "'",
					Movimentacao.class);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Movimentacao> buscarPorCpfPeriodico(String cpf, String inicio, String fim) {
		EntityManager em = emf.createEntityManager();
		try {

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Date dataFinal = formato.parse(fim);
			Date dataInicio = formato.parse(inicio);

			Query query = em.createQuery(
					"from Movimentacao where cpf_correntista = :cpf and dataTransacao between :dataInicio and :dataFinal",
					Movimentacao.class);
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

	public List<Movimentacao> buscarPorCpfMes(String cpf, String dia) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat formato2 = new SimpleDateFormat("MM/yyyy");
			Date primeiroDia = formato2.parse("01/" + dia);
			Date dataTransacao = formato.parse(dia);

			Query query = em.createQuery(
					"from Movimentacao where cpf_correntista = :cpf and dataTransacao between :primeiroDia and :dataTransacao",
					Movimentacao.class);
			query.setParameter("cpf", cpf);
			query.setParameter("primeiroDia", primeiroDia);
			query.setParameter("dataTransacao", dataTransacao);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Movimentacao> buscarPorCpfDia(String cpf, String dia) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dataTransacao = dateFormat.parse(dia);

			Query query = em.createQuery(
					"from Movimentacao where cpf_correntista = :cpf and dataTransacao = :dataTransacao", Movimentacao.class);
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
	public List<Movimentacao> buscarTipoTransacaoTipoConta(String cpf, String tipoTransacao, String contaTipo) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dataTransacao = dateFormat.parse(dia);

			Query query = em.createQuery(
					"from Movimentacao where cpf_correntista = :cpf and dataTransacao = :dataTransacao and tipo_transacao = :tipoTransacao and conta_tipo = :tipoconta",
					Movimentacao.class);
			query.setParameter("cpf", cpf);
			query.setParameter("tipoTransacao", tipoTransacao);
			query.setParameter("tipoconta", contaTipo);

			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			em.close();
		}
	}

	public List<Movimentacao> buscarPorCpfDiaTipoTransacao(String cpf, String dia, String tipoTransacao) {
		EntityManager em = emf.createEntityManager();
		try {
			// Converte a string de data para o tipo Date
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date dataTransacao = dateFormat.parse(dia);

			Query query = em.createQuery(
					"from Movimentacao where cpf_correntista = :cpf and dataTransacao = :dataTransacao and tipo_transacao = :tipoTransacao",
					Movimentacao.class);
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

	public Movimentacao buscarPorId(Long id) {
		EntityManager em = emf.createEntityManager();
		Movimentacao movimentacao = em.find(Movimentacao.class, id);
		em.close();
		return movimentacao;
		// return em.find(movimentacao.class, id);
	}

	public List<Movimentacao> buscarMovimentacoesTresMeses(String cpf) {
	    EntityManager em = emf.createEntityManager();
	    try {
	        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
	        Calendar calendar = Calendar.getInstance();
	        calendar.add(Calendar.MONTH, -3);
	        Date dataInicio = calendar.getTime();
	        Date dataFinal = new Date();

	        Query query = em.createQuery(
	            "from Movimentacao where cpf_correntista = :cpf and dataTransacao between :dataInicio and :dataFinal",
	            Movimentacao.class
	        );
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
}
