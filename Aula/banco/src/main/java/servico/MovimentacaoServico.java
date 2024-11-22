package servico;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import util.ValidarCpf;
import dao.MovimentacaoDAO;
import entidade.Movimentacao;

public class MovimentacaoServico {
	MovimentacaoDAO dao = new MovimentacaoDAO();

	public Movimentacao inserir(Movimentacao movimentacao) {
		if (validarOperacao(movimentacao) == false) {
			System.out.println("Operação inválida");
			return null;
		}
		movimentacao.setDescricao("Operação de " + movimentacao.getTipoTransacao());
		movimentacao.setDataTransacao(new Date());
		Movimentacao movimentacaoBanco = dao.inserir(movimentacao);
		return movimentacaoBanco;
	}

	public boolean validarOperacao(Movimentacao movimentacao) {
		tarifaPix(movimentacao);
		tarifaSaque(movimentacao);
		if (limiteSaque(movimentacao) == false) {
			System.out.println("Limite de saque diário atingido");
			return false;
		}
		if (movimentacao.getTipoTransacao() == "saque") {
			if (limiteSaques(movimentacao) >= 5000) {
				System.out.println("Limite de saques diários atingido");
				return false;
			}
		}
		if (validarLimiteOperacoes(movimentacao) == false) {
			System.out.println("Limite de operações diárias atingido");
			return false;
		}
		String cpf = movimentacao.getCpfCorrentista();
		if (ValidarCpf.validarCpf(cpf) == false) {
			System.out.println("CPF inválido");
			return false;
		}
		if (movimentacao.getTipoTransacao() != "deposito") {
			if (virificarSaldo(movimentacao) - movimentacao.getValorOperacao() < 0) {
				System.out.println("Saldo insuficiente");

				return false;
			}
		}
		if (limitePix(movimentacao) == false) {
			System.out.println("Limite de pix diário atingido");
			return false;
		}
		if (horarioPix(movimentacao) == false) {
			System.out.println("Horário de pix inválido");
			return false;
		}
		return true;
	}

	public void tarifaSaque(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == "saque") {
			double valor = movimentacao.getValorOperacao();
			movimentacao.setValorOperacao(valor + 2.);
		}
	}

	public void tarifaPix(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == "pagamento" || movimentacao.getTipoTransacao() == "pix") {
			double valor = movimentacao.getValorOperacao();
			movimentacao.setValorOperacao(valor + 5.);
		}
	}

	public List<Movimentacao> extratoMes(Movimentacao movimentacao) {
		String dia = diaTransacao(movimentacao);
		List<Movimentacao> movimentacaos = dao.buscarPorCpfMes(movimentacao.getCpfCorrentista(), dia);
		System.out.println("Extrato do mês");
		for (Movimentacao movimentacao2 : movimentacaos) {
			System.out.println(
					"nome: " + movimentacao2.getNomeCorrentista() + " cpf: " + movimentacao2.getCpfCorrentista()
							+ " tipo transação: " + movimentacao2.getTipoTransacao() + " valor operação: "
							+ movimentacao2.getValorOperacao()
							+ " data transação: " + movimentacao2.getDataTransacao());
		}
		return movimentacaos;
	}

	public List<Movimentacao> extratoPeriodico(Movimentacao movimentacao, String inicio, String fim) {

		List<Movimentacao> movimentacaos = dao.buscarPorCpfPeriodico(movimentacao.getCpfCorrentista(), inicio, fim);
		return movimentacaos;
	}

	public boolean horarioPix(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == "pix") {
			if (movimentacao.getHorarioMovimentacao() < 6 && movimentacao.getHorarioMovimentacao() > 22) {
				return false;
			}
		}
		return true;
	}

	public boolean limitePix(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == "pix") {
			if (movimentacao.getValorOperacao() > 300) {
				return false;
			}
		}
		return true;
	}

	public boolean limiteSaque(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() == "saque") {
			if (movimentacao.getValorOperacao() > 5000) {
				return false;
			}
		}
		return true;
	}

	public double limiteSaques(Movimentacao movimentacao) {
		double totalSaques = 0;
		String dia = diaTransacao(movimentacao);
		List<Movimentacao> saques = dao.buscarPorCpfDiaTipoTransacao(movimentacao.getCpfCorrentista(), dia, "saque");
		for (Movimentacao movimentacao2 : saques) {
			totalSaques += movimentacao2.getValorOperacao();
		}
		return totalSaques;
	}

	public boolean validarLimiteOperacoes(Movimentacao movimentacao) {
		String dia = diaTransacao(movimentacao);
		List<Movimentacao> transacoes = dao.buscarPorCpfDia(movimentacao.getCpfCorrentista(), dia);
		if (transacoes.size() >= 40) {
			return false;
		}
		return true;
	}

	public double virificarSaldo(Movimentacao movimentacao) {
		double totalDeposito = 0;
		double totalSaida = 0;

		List<Movimentacao> deposito = dao.buscarPorCpfTipoTransacao(movimentacao.getCpfCorrentista(), "deposito");
		List<Movimentacao> saque = dao.buscarPorCpfTipoTransacao(movimentacao.getCpfCorrentista(), "saque");
		List<Movimentacao> pagamento = dao.buscarPorCpfTipoTransacao(movimentacao.getCpfCorrentista(), "pagamento");
		List<Movimentacao> pix = dao.buscarPorCpfTipoTransacao(movimentacao.getCpfCorrentista(), "pix ");

		for (Movimentacao entrada : deposito) {
			totalDeposito += entrada.getValorOperacao();
		}
		for (Movimentacao saida : pix) {
			totalSaida += saida.getValorOperacao();
		}

		for (Movimentacao saida : saque) {
			totalSaida += saida.getValorOperacao();
		}

		for (Movimentacao saida : pagamento) {
			totalSaida += saida.getValorOperacao();
		}
		System.out.println("Saldo: " + (totalDeposito - totalSaida));
		return totalDeposito - totalSaida;
	}

	public String diaTransacao(Movimentacao movimentacao) {
		Date dataTransacao = movimentacao.getDataTransacao();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dia = sdf.format(dataTransacao);
		return dia;
	}

	public String VerificarNotificarSaldo(Movimentacao movimentacao) {
		if (movimentacao.getTipoTransacao() != "deposito") {
			if (virificarSaldo(movimentacao) - movimentacao.getValorOperacao() > 0.
					&& virificarSaldo(movimentacao) - movimentacao.getValorOperacao() < 100.) {
				return "Saldo abaixo de R$ 100,00";
			}

		}
		return null;
	}
}
