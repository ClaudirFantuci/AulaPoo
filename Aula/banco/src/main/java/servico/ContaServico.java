package servico;

import java.util.Date;
import java.util.List;
import util.ValidarCpf;
import dao.ContaDAO;
import entidade.Conta;

public class ContaServico {
	ContaDAO dao = new ContaDAO();

	public Conta inserir(Conta conta) {
		if (validarOperacao(conta) == false) {
			return null;
		}
		conta.setDescricao("Operação de " + conta.getTipoTransacao());
		conta.setDataTransacao(new Date());
		Conta contaBanco = dao.inserir(conta);
		return contaBanco;
	}

	public boolean validarOperacao(Conta conta) {
		if (limiteSaque(conta) == false) {
			return false;
		}
		if (conta.getTipoTransacao() == "saque") {
			if (limiteSaques(conta) >= 5000) {
				return false;
			}
		}
		if (validarLimiteOperacoes(conta) == false) {
			return false;
		}

		if (ValidarCpf.validarCpf(conta) == false) {
			return false;
		}
		if (conta.getTipoTransacao() != "deposito") {
			if (virificarSaldo(conta) - conta.getValorOperacao() < 0) {
				return false;
			}
		}
		if (limitePix(conta) == false) {
			return false;
		}
		if (horarioPix(conta) == false) {
			return false;
		}
		return true;
	}

	public List<Conta> extratoMes(Conta conta) {
		String mes = mesTransacao(conta);
		List<Conta> contas = dao.buscarPorCpfMes(conta.getCpfCorrentista(), mes);
		for (Conta conta2 : contas) {
			System.out.println(conta2.getId());
		}
		return contas;
	}

	public boolean horarioPix(Conta conta) {
		if (conta.getTipoTransacao() == "pix") {
			if (conta.getHorarioMovimentacao() < 6 && conta.getHorarioMovimentacao() > 22) {
				return false;
			}
		}
		return true;
	}

	public boolean limitePix(Conta conta) {
		if (conta.getTipoTransacao() == "pix") {
			if (conta.getValorOperacao() > 300) {
				return false;
			}
		}
		return true;
	}

	public boolean limiteSaque(Conta conta) {
		if (conta.getTipoTransacao() == "saque") {
			if (conta.getValorOperacao() > 5000) {
				return false;
			}
		}
		return true;
	}

	public double limiteSaques(Conta conta) {
		double totalSaques = 0;
		String dia = diaTransacao(conta);
		List<Conta> saques = dao.buscarPorCpfDiaTipoTransacao(conta.getCpfCorrentista(), dia, "saque");
		for (Conta conta2 : saques) {
			totalSaques += conta2.getValorOperacao();
		}
		return totalSaques;
	}

	public boolean validarLimiteOperacoes(Conta conta) {
		String dia = diaTransacao(conta);
		List<Conta> transacoes = dao.buscarPorCpfDia(conta.getCpfCorrentista(), dia);
		System.out.println(transacoes.size());
		if (transacoes.size() >= 30) {
			return false;
		}
		return true;
	}

	public double virificarSaldo(Conta conta) {
		double totalDeposito = 0;
		double totalSaida = 0;

		List<Conta> deposito = dao.buscarPorCpfTipoTransacao(conta.getCpfCorrentista(), "deposito");
		List<Conta> saque = dao.buscarPorCpfTipoTransacao(conta.getCpfCorrentista(), "saque");
		List<Conta> pagamento = dao.buscarPorCpfTipoTransacao(conta.getCpfCorrentista(), "pagamento ");
		List<Conta> pix = dao.buscarPorCpfTipoTransacao(conta.getCpfCorrentista(), "pix ");

		for (Conta entrada : deposito) {
			totalDeposito += conta.getValorOperacao();
		}
		for (Conta saida : pix) {
			totalSaida += conta.getValorOperacao();
		}

		for (Conta saida : saque) {
			totalSaida += conta.getValorOperacao();
		}

		for (Conta saida : pagamento) {
			totalSaida += conta.getValorOperacao();
		}

		return totalDeposito - totalSaida;
	}

	public String diaTransacao(Conta conta) {
		Date dataTransacao = conta.getDataTransacao();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dia = sdf.format(dataTransacao);
		return dia;
	}

	public String mesTransacao(Conta conta) {
		Date dataTransacao = conta.getDataTransacao();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/yyyy");
		String dia = sdf.format(dataTransacao);
		return dia;
	}
}
