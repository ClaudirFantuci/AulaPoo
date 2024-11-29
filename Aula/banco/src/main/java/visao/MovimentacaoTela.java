package visao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import controle.ClienteControle;
import controle.ContaControle;
import controle.MovimentacaoControle;
import dao.ClienteDAO;
import dao.ContaDAO;
import controle.MovimentacaoControle;
import entidade.Cliente;
import entidade.Conta;
import entidade.ContaTipo;
import entidade.Movimentacao;
import entidade.Movimentacao;
import servico.MovimentacaoServico;

public class MovimentacaoTela {

	public static void main(String[] args) {
		ClienteDAO clienteDAO = new ClienteDAO();
		ContaDAO contaDao = new ContaDAO();

		ClienteControle controleCliente = new ClienteControle();
		Cliente cliente = new Cliente();
		cliente.setCpf("10174068980");
		cliente.setNome("José Antônio da Silva");
		controleCliente.inserir(cliente);

		ContaControle controleConta = new ContaControle();
		Conta conta = new Conta();
		conta.setDataAbertura(new Date());
		conta.setCliente(cliente);
		conta.setContaTipo(ContaTipo.CONTA_POUPANCA);
		controleConta.inserir(conta);
		
		Conta conta1 = new Conta();
		conta1.setDataAbertura(new Date());
		conta1.setCliente(cliente);
		conta1.setContaTipo(ContaTipo.CONTA_CORRENTE);
		controleConta.inserir(conta1);
		

		
		MovimentacaoControle controleMovimentacao = new MovimentacaoControle();
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setCpfCorrentista("10174068980");
		movimentacao.setDataTransacao(new Date());
		movimentacao.setDescricao("Depósito de 500,00 no dia 03/10/24");
		movimentacao.setNomeCorrentista("José");
		movimentacao.setTipoTransacao("deposito");
		movimentacao.setValorOperacao(8.);
		movimentacao.setHorarioMovimentacao(LocalDateTime.now().getHour());
		controleMovimentacao.inserir(movimentacao);

		String notificar = controleMovimentacao.VerificarNotificarSaldo(movimentacao);
		if (notificar != null) {
			System.out.println(notificar);
		}
		// Scanner sc = new Scanner(System.in);
		// String inicio = sc.nextLine();
		// String fim = sc.nextLine();
		// List<Movimentacao> movimentacoes = controle.extratoPeriodico(movimentacao,
		// inicio, fim);
		// System.out.println("Extrato entre" + inicio + "-" + fim + ":");
		// for (Movimentacao movimentacao2 : movimentacoes) {
		// System.out.println("id" + movimentacao2.getId() + "nome: " +
		// movimentacao2.getNomeCorrentista() + " cpf: "
		// + movimentacao2.getCpfCorrentista()
		// + " tipo transação: " + movimentacao2.getTipoTransacao() + " valor operação:
		// " + movimentacao2.getValorOperacao()
		// + " data transação: " + movimentacao2.getDataTransacao());
		// }
		// }
	}
}
