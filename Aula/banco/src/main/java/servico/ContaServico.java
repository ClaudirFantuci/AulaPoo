package servico;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

import util.ValidarCpf;
import dao.ContaDAO;
import entidade.Conta;

public class ContaServico {
	ContaDAO dao = new ContaDAO();

	public Conta inserir(Conta conta) {
		if (validarOperacao(conta) == false) {
			System.out.println("Operação inválida");
			return null;
		}
		conta.setDescricao("Operação de " + conta.getTipoTransacao());
		conta.setDataTransacao(new Date());
		Conta contaBanco = dao.inserir(conta);
		VerificarNotificarSaldo(conta);
		return contaBanco;
	}

	public boolean validarOperacao(Conta conta) {
		if (limiteSaque(conta) == false) {
			System.out.println("Limite de saque diário atingido");
			return false;
		}
		if (conta.getTipoTransacao() == "saque") {
			if (limiteSaques(conta) >= 15000) {
				System.out.println("Limite de saques diários atingido");
				return false;
			}
		}
		if (validarLimiteOperacoes(conta) == false) {
			System.out.println("Limite de operações diárias atingido");
			return false;
		}

		if (ValidarCpf.validarCpf(conta) == false) {
			System.out.println("CPF inválido");
			return false;
		}
		if (conta.getTipoTransacao() != "deposito") {
			if (virificarSaldo(conta) - conta.getValorOperacao() < 0) {
				System.out.println("Saldo insuficiente");

				return false;
			}
		}
		if (limitePix(conta) == false) {
			System.out.println("Limite de pix diário atingido");
			return false;
		}
		if (horarioPix(conta) == false) {
			System.out.println("Horário de pix inválido");
			return false;
		}
		return true;
	}

	public List<Conta> extratoMes(Conta conta) {
		String dia = diaTransacao(conta);
		List<Conta> contas = dao.buscarPorCpfMes(conta.getCpfCorrentista(), dia);
		System.out.println("Extrato do mês");
		for (Conta conta2 : contas) {
			System.out.println("nome: " + conta2.getNomeCorrentista() + " cpf: " + conta2.getCpfCorrentista()
					+ " tipo transação: " + conta2.getTipoTransacao() + " valor operação: " + conta2.getValorOperacao()
					+ " data transação: " + conta2.getDataTransacao());
		}
		return contas;
	}

	public List<Conta> extratoPeriodico(Conta conta) {
		Scanner sc = new Scanner(System.in);
		String inicio = sc.nextLine();
		String fim = sc.nextLine();
		List<Conta> contas = dao.buscarPorCpfPeriodico(conta.getCpfCorrentista(), inicio, fim);
		System.out.println("Extrato entre" + inicio + "-" + fim + ":");
		for (Conta conta2 : contas) {
			System.out.println("id" + conta2.getId() + "nome: " + conta2.getNomeCorrentista() + " cpf: "
					+ conta2.getCpfCorrentista()
					+ " tipo transação: " + conta2.getTipoTransacao() + " valor operação: " + conta2.getValorOperacao()
					+ " data transação: " + conta2.getDataTransacao());
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
			if (conta.getValorOperacao() > 15000) {
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
		List<Conta> pagamento = dao.buscarPorCpfTipoTransacao(conta.getCpfCorrentista(), "pagamento");
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
		System.out.println("Saldo: " + (totalDeposito - totalSaida));
		return totalDeposito - totalSaida;
	}

	public String diaTransacao(Conta conta) {
		Date dataTransacao = conta.getDataTransacao();
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
		String dia = sdf.format(dataTransacao);
		return dia;
	}

	public void VerificarNotificarSaldo(Conta conta) {
		if (conta.getTipoTransacao() != "deposito") {
			if (virificarSaldo(conta) - conta.getValorOperacao() > 0
					&& virificarSaldo(conta) - conta.getValorOperacao() < 100) {
				throw new Error("Saldo abaixo de R$ 100,00");
			}

		}
	}
}
