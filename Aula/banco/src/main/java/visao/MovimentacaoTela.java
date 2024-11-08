package visao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import controle.MovimentacaoControle;
import controle.MovimentacaoControle;
import entidade.Movimentacao;
import entidade.Movimentacao;
import servico.MovimentacaoServico;

public class MovimentacaoTela {

	public static void main(String[] args) {
		MovimentacaoControle controle = new MovimentacaoControle();

		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setCpfCorrentista("10174068980");
		movimentacao.setDataTransacao(new Date());
		movimentacao.setDescricao("Depósito de 500,00 no dia 03/10/24");
		movimentacao.setNomeCorrentista("José");
		movimentacao.setTipoTransacao("deposito");
		movimentacao.setValorOperacao(8.);
		movimentacao.setHorarioMovimentacao(LocalDateTime.now().getHour());
		controle.inserir(movimentacao);
		String notificar = controle.VerificarNotificarSaldo(movimentacao);
		if(notificar != null) {
			System.out.println(notificar);
		}
		Scanner sc = new Scanner(System.in);
		String inicio = sc.nextLine();
		String fim = sc.nextLine();
		 List<Movimentacao> movimentacoes = controle.extratoPeriodico(movimentacao, inicio, fim);
		 System.out.println("Extrato entre" + inicio + "-" + fim + ":");
			for (Movimentacao movimentacao2 : movimentacoes) {
				System.out.println("id" + movimentacao2.getId() + "nome: " + movimentacao2.getNomeCorrentista() + " cpf: "
						+ movimentacao2.getCpfCorrentista()
						+ " tipo transação: " + movimentacao2.getTipoTransacao() + " valor operação: " + movimentacao2.getValorOperacao()
						+ " data transação: " + movimentacao2.getDataTransacao());
			}
	}

}
