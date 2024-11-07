package controle;

import java.util.List;

import entidade.Conta;
import servico.ContaServico;

public class ContaControle {

	ContaServico servico = new ContaServico();

	public Conta inserir(Conta conta) {
		return servico.inserir(conta);
	}

	public void extrato(Conta conta) {
		servico.extratoMes(conta);
	}

	public void extratoPeriodico(Conta conta) {
		servico.extratoPeriodico(conta);

	}
}
