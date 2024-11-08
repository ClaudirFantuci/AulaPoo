package controle;

import java.util.List;

import entidade.Movimentacao;
import servico.MovimentacaoServico;

public class MovimentacaoControle {

	MovimentacaoServico servico = new MovimentacaoServico();

	public Movimentacao inserir(Movimentacao movimentacao) {
		return servico.inserir(movimentacao);
	}

	public void extrato(Movimentacao movimentacao) {
		servico.extratoMes(movimentacao);
	}

	public List<Movimentacao> extratoPeriodico(Movimentacao movimentacao, String inicio, String fim) {
		return servico.extratoPeriodico(movimentacao, inicio, fim);

	}
	
	public String VerificarNotificarSaldo(Movimentacao movimentacao) {
		return servico.VerificarNotificarSaldo(movimentacao);
	}
}

