package entidade;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "movimentacao")
public class Movimentacao {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "nome_correntista")
	private String nomeCorrentista;
	@Column(name = "cpf_correntista")
	private String cpfCorrentista;
	@Column(name = "tipo_transacao")
	private String tipoTransacao;
	@Column(unique = false, name = "descricao", length = 150, nullable = true)
	private String descricao;
	@Temporal(TemporalType.DATE)
	private Date dataTransacao;

	private int horarioMovimentacao;

	@Column(name = "valor_operacao")
	private Double valorOperacao;

	public int getHorarioMovimentacao() {
		return horarioMovimentacao;
	}

	public void setHorarioMovimentacao(int horarioMovimentacao) {
		this.horarioMovimentacao = horarioMovimentacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeCorrentista() {
		return nomeCorrentista;
	}

	public void setNomeCorrentista(String nomeCorrentista) {
		this.nomeCorrentista = nomeCorrentista;
	}

	public String getCpfCorrentista() {
		return cpfCorrentista;
	}

	public void setCpfCorrentista(String cpfCorrentista) {
		this.cpfCorrentista = cpfCorrentista;
	}

	public String getTipoTransacao() {
		return tipoTransacao;
	}

	public void setTipoTransacao(String tipoTransacao) {
		this.tipoTransacao = tipoTransacao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDataTransacao() {
		return dataTransacao;
	}

	public void setDataTransacao(Date dataTransacao) {
		this.dataTransacao = dataTransacao;
	}

	public Double getValorOperacao() {
		return valorOperacao;
	}

	public void setValorOperacao(Double valorOperacao) {
		this.valorOperacao = valorOperacao;
	}

}
