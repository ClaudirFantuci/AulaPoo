package controle;

import java.util.List;

import entidade.Conta;
import servico.ContaServico;

public class ContaControle {

	ContaServico servico = new ContaServico();

	public Conta inserir(Conta conta) {
		return servico.inserir(conta);
	}

	public List<Conta> extrato(Conta conta) {
		return servico.extratoMes(conta);
	}

}
