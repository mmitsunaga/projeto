package br.gov.go.tj.projudi.dt;

import java.util.List;

import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.ResultSetTJGO;

// ---------------------------------------------------------
public class PendenciaArquivoDt extends PendenciaArquivoDtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2344181409081440735L;

	public static final int CodigoPermissao = 114;

	// Variáveis para controlar possíveis status de PendenciaArquivo
	public static final int NORMAL = 0;
	public static final int BLOQUEADO_POR_VIRUS = 1;
	public static final int RESTRICAO_DOWNLOAD = 2;
	public static final int AGUARDANDO_ASSINATURA = -3;

	private ArquivoDt arquivoDt;
	private PendenciaDt pendenciaDt;
	private boolean valido; // Define se arquivo de uma pendência é válido

	// Variáveis para guardar dados da pré-análise
	private String assistenteResponsavel;
	private String juizResponsavel;
	private boolean multiplo; // Variável para tratamento de arquivo múltiplo
	private String processoNumero; // Variável para guardar o processo envolvido
	private String dataPreAnalise;

	private String hash;
	private String idPendenciaRelacionada;

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getDataPreAnalise() {
		return dataPreAnalise;
	}

	public void setDataPreAnalise(String dataPreAnalise) {
		this.dataPreAnalise = dataPreAnalise;
	}

	public String getProcessoNumero() {
		return processoNumero;
	}

	public void setProcessoNumero(String processoNumero) {
		this.processoNumero = processoNumero;
	}

	public void limpar() {
		super.limpar();
		assistenteResponsavel = "";
		juizResponsavel = "";
		processoNumero = "";
		multiplo = false;
		valido = true;
	}

	public String getAssistenteResponsavel() {
		return assistenteResponsavel;
	}

	public void setAssistenteResponsavel(String assistenteResponsavel) {
		if (assistenteResponsavel != null) this.assistenteResponsavel = assistenteResponsavel;
	}

	public String getJuizResponsavel() {
		return juizResponsavel;
	}

	public void setJuizResponsavel(String juizResponsavel) {
		if (juizResponsavel != null) this.juizResponsavel = juizResponsavel;
	}

	public ArquivoDt getArquivoDt() {
		return arquivoDt;
	}

	public void setArquivoDt(ArquivoDt arquivoDt) {
		this.arquivoDt = arquivoDt;
	}

	public PendenciaArquivoDt() {
		super();
	}

	public PendenciaArquivoDt(String id_Pendencia, String id_Arquivo) {
		setId_Pendencia(id_Pendencia);
		setId_Arquivo(id_Arquivo);
	}

	/**
	 * Retorna em literal se o arquivo e uma resposta
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 24/11/2008 10:19
	 * @return String
	 */
	public String getRespostaLiteral() {
		if (this.getResposta().equals("true")) return "Sim";

		return "Não";
	}

	/*
	 * public boolean isResposta(){return this.getResposta().equals("1"); }
	 */

	public boolean isMultiplo() {
		return multiplo;
	}

	public void setMultiplo(boolean multiplo) {
		this.multiplo = multiplo;
	}

	public PendenciaDt getPendenciaDt() {
		return pendenciaDt;
	}

	public void setPendenciaDt(PendenciaDt pendenciaDt) {
		this.pendenciaDt = pendenciaDt;
	}

	public void setResponsavelPreAnalise(List responsaveis) {
		
		if(responsaveis==null) 
			return;
		
		for (int j = 0; j < responsaveis.size(); j++) {
			PendenciaResponsavelDt responsavel = (PendenciaResponsavelDt) responsaveis.get(j);
			if (responsavel.getId_UsuarioResponsavel() != null && !responsavel.getId_UsuarioResponsavel().equals("")) {
				this.setAssistenteResponsavel(responsavel.getNomeUsuarioResponsavel());
			} else {
				if(responsavel.getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.ASSISTENTE_GABINETE))){
					this.setAssistenteResponsavel(responsavel.getNomeUsuarioServentiaCargo());
				} else {
					this.setJuizResponsavel(responsavel.getNomeUsuarioServentiaCargo());
				}
			}
		}
	}

	public boolean isValido() {
		return valido;
	}

	public void setValido(boolean valido) {
		this.valido = valido;
	}
	
	protected void associarDt( PendenciaArquivoDt Dados, ResultSetTJGO rs )  throws Exception {
		Dados.setId(rs.getString("ID_PEND_ARQ"));
		Dados.setNomeArquivo(rs.getString("NOME_ARQ"));
		Dados.setId_Pendencia( rs.getString("ID_PEND"));
		Dados.setId_Arquivo( rs.getString("ID_ARQ"));
		Dados.setResposta( Funcoes.FormatarLogico(rs.getString("RESPOSTA")));
		Dados.setCodigoTemp( rs.getString("CODIGO_TEMP"));		
		Dados.setPendenciaTipo( rs.getString("PEND_TIPO"));
	}

	public String getIdPendenciaRelacionada() {
		return idPendenciaRelacionada;
	}

	public void setIdPendenciaRelacionada(String idPendenciaRelacionada) {
		this.idPendenciaRelacionada = idPendenciaRelacionada;
	}
}
