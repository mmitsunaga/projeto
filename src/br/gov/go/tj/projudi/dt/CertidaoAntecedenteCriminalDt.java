package br.gov.go.tj.projudi.dt;


import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.ne.UsuarioNe;
import br.gov.go.tj.utils.Funcoes;

public class CertidaoAntecedenteCriminalDt extends Dados {
	
	public static final int ANTECEDENTE_CRIMINAL_NEGATIVO_MODELO_CODIGO = 139;
	public static final int ANTECEDENTE_CRIMINAL_POSITIVO_MODELO_CODIGO = 138;
	
	public static final int INFORMACAO_CRIMINAL_POSITIVA_MODELO_CODIGO = 155;
	public static final int INFORMACAO_CRIMINAL_NEGATIVA_MODELO_CODIGO = 156;
	
	public static final int INFORMACAO_MENOR_MENOR_NEGATIVO_MODELO_CODIGO = 157;
	public static final int INFORMACAO_MENOR_INFRATOR_POSITIVO_MODELO_CODIGO = 158;
	
	private static final long serialVersionUID = 5212514624842503553L;
	public static final int CodigoPermissao = 447;
	private String nome;
	private String nomePai;
	private String nomeMae;
	private String naturalidade;
	private String id_Naturalidade;
	private String profissao;
	private String estadoCivil;
	private String dataNascimento;
	private String sexo;
	private String identidade;
	private String cpfCnpj;
	private String domicilio;
	private String nacionalidade;
	private String texto;
	private List listaProcesso;
	private String id_estadoCivil;
	private String id_Profissao;
	private String comarcaCodigo;
	private String comarca;
	private List listaProcessosImprimirCertidao;
	private String chkMenorInfrator;
	private String rg;
	
	public String getComarca() {
		return Funcoes.retirarAcentos(comarca).toUpperCase();
	}

	public void setComarca(String comarca) {
		this.comarca = comarca;
	}
	
	public List getListaProcessoComarca() {
		List listaAux = new ArrayList();
		for (int i = 0; i < listaProcessosImprimirCertidao.size(); i++) {
			if (Funcoes.retirarAcentos(((ProcessoAntecedenteCriminalDt) listaProcessosImprimirCertidao.get(i)).getComarca()).toUpperCase() != null){
				if (Funcoes.retirarAcentos(((ProcessoAntecedenteCriminalDt) listaProcessosImprimirCertidao.get(i)).getComarca()).toUpperCase().equals(this.getComarca())) {
					listaAux.add(listaProcessosImprimirCertidao.get(i));
				}
			}
		}
		
		return listaAux;
	}
	
	public List getListProcessoInformativo() {
		List listaAux = new ArrayList();
		for (int i = 0; i < listaProcessosImprimirCertidao.size(); i++) {
			if (((ProcessoAntecedenteCriminalDt) listaProcessosImprimirCertidao.get(i)).getComarca() != null){
				if (!((ProcessoAntecedenteCriminalDt) listaProcessosImprimirCertidao.get(i)).getComarca().equals(this.comarca)) {
					listaAux.add(listaProcessosImprimirCertidao.get(i));
				}
			}
		}
		
		return listaAux;
	}

	public CertidaoAntecedenteCriminalDt() {
		super();
		limpar();
		listaProcesso = new ArrayList();
		listaProcessosImprimirCertidao = new ArrayList();
	}

	public void limpar() {
		nome = "";
		nomePai = "";
		nomeMae = "";
		naturalidade = "";
		profissao = "";
		estadoCivil = "";
		dataNascimento = "";
		sexo = "";
		identidade = "";
		cpfCnpj = "";
		domicilio = "";
		nacionalidade = "";
		texto = "";
		comarcaCodigo = "";
		chkMenorInfrator = "false";
		rg = "";
		id_Naturalidade = "";
	}
	
	public void setNome(String parameter) {
		if(parameter != null)
		nome = parameter;
	}

	public String getNome() {
		return nome;
	}
	
	public String getComarcaCodigo() {
		return comarcaCodigo;
	}

	public String getNomePai() {
		return nomePai;
	}

	public void setNomePai(String nomePai) {
		if (nomePai != null)
		this.nomePai = nomePai;
	}

	public String getNomeMae() {
		return nomeMae;
	}

	public void setNomeMae(String nomeMae) {
		if (nomeMae != null)
		this.nomeMae = nomeMae;
	}

	public String getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(String naturalidade) {
		if (naturalidade != null)
		this.naturalidade = naturalidade;
	}

	public String getProfissao() {
		return profissao;
	}
	
	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		if (rg!= null)
		this.rg = rg;
	}

	public void setProfissao(String profissao) {
		if (profissao!= null)
		this.profissao = profissao;
	}

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		if(estadoCivil != null)
		this.estadoCivil = estadoCivil;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		if (dataNascimento != null)
		this.dataNascimento = dataNascimento;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		if (sexo != null)
		this.sexo = sexo;
	}

	public String getIdentidade() {
		return identidade;
	}

	public void setIdentidade(String identidade) {
		if (identidade != null)
		this.identidade = identidade;
	}

	public String getCpfCnpj() {
		return cpfCnpj;
	}

	public void setCpfCnpj(String cpfCnpj) {
		if (cpfCnpj != null)
		this.cpfCnpj = cpfCnpj;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		if (domicilio != null)
		this.domicilio = domicilio;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		if (texto != null)
		this.texto = texto;
	}

	public void removeProcesso(int posicao) {
		if (posicao < listaProcesso.size())
			listaProcesso.remove(posicao);
	}

	public List getListaProcesso() {
		return listaProcesso;
	}

	public void setListaProcesso(List processoLista) {
		listaProcesso = processoLista;
		
	}
	
	public String getNacionalidade() {
		return nacionalidade;
	}

	public void setNacionalidade(String nacionalidade) {
		if (nacionalidade != null)
		this.nacionalidade = nacionalidade;
	}

	
	@Override
	public String getId() {
		return null;
	}

	@Override
	public void setId(String id) {
	}

	public void setId_EstadoCivil(String parameter) {
		if (parameter != null)
		id_estadoCivil = parameter;
	}

	public void setId_Profissao(String parameter) {
		if (parameter != null)
		id_Profissao = parameter;
	}

	public String getId_estadoCivil() {
		return id_estadoCivil;
	}

	public void setId_estadoCivil(String idEstadoCivil) {
		if (idEstadoCivil != null)
		id_estadoCivil = idEstadoCivil;
	}

	public void setId_Naturalidade(String idNarturalidade) {
		if (idNarturalidade != null && !idNarturalidade.isEmpty() && !idNarturalidade.equalsIgnoreCase("null")) {
			id_Naturalidade = idNarturalidade;
		}
	}
	
	public String getId_Naturalidade() {
		return id_Naturalidade;
	}

	public String getId_Profissao() {
		return id_Profissao;
	}
	
	public String getChkMenorInfrator() {
		return chkMenorInfrator;
	}

	public void setChkMenorInfrator(String chkMenorInfrator) {
		if(chkMenorInfrator != null)
			this.chkMenorInfrator = chkMenorInfrator;
	}

	/**
	 * Retorna o código do modelo de certidão/informação de antecedentes baseado no perfil do usuário da sessão.
	 * @param usuarioSessao - usuárioNe 
	 * @param chkMenorInfrator - informa se a certidão é para menor infrator
	 * @return código do modelo
	 * @author jcpresa, hmgodinho
	 */
	public String getModeloCodigo(UsuarioNe usuarioSessao, boolean chkMenorInfrator) {
		//Se a certidão for de menor infrator, consultará o modelo correspondente
		if(chkMenorInfrator){
			return Integer.toString(listaProcessosImprimirCertidao.size() == 0 ? INFORMACAO_MENOR_MENOR_NEGATIVO_MODELO_CODIGO : INFORMACAO_MENOR_INFRATOR_POSITIVO_MODELO_CODIGO);
		}
		//Se não for certidão de menor infrator, consultará o modelo segundo o perfil do usuário
		if(usuarioSessao.getUsuarioDt().getGrupoTipoCodigo().equals(String.valueOf(GrupoTipoDt.DISTRIBUIDOR)))
			return Integer.toString(listaProcessosImprimirCertidao.size() == 0 ? ANTECEDENTE_CRIMINAL_NEGATIVO_MODELO_CODIGO : ANTECEDENTE_CRIMINAL_POSITIVO_MODELO_CODIGO);
		else {
			return Integer.toString(listaProcessosImprimirCertidao.size() == 0 ? INFORMACAO_CRIMINAL_NEGATIVA_MODELO_CODIGO : INFORMACAO_CRIMINAL_POSITIVA_MODELO_CODIGO);
		}
	}

	public void setComarcaCodigo(String comarcaCodigo2) {
		this.comarcaCodigo = comarcaCodigo2;		
	}

	public List getListaProcessosImprimirCertidao() {
		return listaProcessosImprimirCertidao;
	}

	public void setListaProcessosImprimirCertidao(List listaProcessosImprimirCertidao) {
		this.listaProcessosImprimirCertidao = listaProcessosImprimirCertidao;
	}

}
