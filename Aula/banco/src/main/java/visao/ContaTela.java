package visao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import controle.ContaControle;
import entidade.Conta;
import servico.ContaServico;

public class ContaTela {

	public static void main(String[] args) {
		ContaControle controle = new ContaControle();

		Conta conta = new Conta();
		conta.setCpfCorrentista("10174068980");
		conta.setDataTransacao(new Date());
		conta.setDescricao("Depósito de 500,00 no dia 03/10/24");
		conta.setNomeCorrentista("José");
		conta.setTipoTransacao("deposito");
		conta.setValorOperacao(5000.);
		conta.setHorarioMovimentacao(LocalDateTime.now().getHour());
		controle.inserir(conta);

		controle.extrato(conta);
	}

}
