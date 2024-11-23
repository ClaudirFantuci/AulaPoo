package servico;

import java.util.List;

import dao.ContaDAO;
import entidade.Conta;

public class ContaServico {
    ContaDAO dao = new ContaDAO();

    public Conta inserir(Conta conta) {
        if (validarOperacao(conta) == false) {
            System.out.println("Operação inválida");
            return null;
        }
        return dao.inserir(conta);
    }

    public boolean validarOperacao(Conta conta) {
        List<Conta> contas = dao.buscarPorIdCliente(conta);
        if (contas.size() >= 3) {
            System.out.println("Cliente já possui mais de 3 contas");
            return false;
        }
        return true;
    }
    
    public 
}
