package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.gov.go.tj.utils.Funcoes;

public class GuiaCertidaoGeralDt extends Dados {
	
	private static final long serialVersionUID = 2982814366576233619L;

	public static final int CodigoPermissaoNarrativa = 264;
	public static final int CodigoPermissaoPraticaForense = 524;//447; // TESTAR ESSA PERMISSÃO 524
	
	public static final int CodigoPermissaoInterdicao = 907; //ALTERAR ESTA PERMISSÃO, COLOCANDO A PERMISSÃO CORRETA
	public static final int CodigoPermissaoLicitacao = 907; //ALTERAR ESTA PERMISSÃO, COLOCANDO A PERMISSÃO CORRETA
	public static final int CodigoPermissaoAutoria = 907; //ALTERAR ESTA PERMISSÃO, COLOCANDO A PERMISSÃO CORRETA
	
	private String nome;
	private String cpf;
	private String tipoPessoa;
	private String id_Naturalidade;
	private String naturalidade;	
	private String sexo;
	private String dataNascimento;
	private String rg;
	private String id_RgOrgaoExpedidor;
	private String rgOrgaoExpedidor;
	private String rgOrgaoExpedidorSigla;
	private String rgDataExpedicao;
	private String id_EstadoCivil;
	private String estadoCivil;
	private String id_Profissao;
	private String profissao;
	private String domicilio;
	
	//interdicao
	private String nomePai;
	private String nomeMae;
	private String registroGeral;
	private String patente;
	private String id_Nacionalidade;
	private String nacionalidade;
	
	//pratica forense
	private String oab;
	private String oabComplemento;
	private String oabUf;
	private String oabUfCodigo;
	private String textoForense;
	private String anoInicial;
	private String anoFinal;
	private String mesInicial;
	private String mesFinal;
	private List listaProcesso;
	private int quantidade;
	private int modeloCodigo;
	private String[] vetorMes = {"0", "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro" };
	private String tipo;
	
	private String id_Comarca;
	private String comarca;
	private String id_Serventia;
	private String serventia;
	
	// Custas da certidão (IDs das custas = 191 e 9025)
	private String custaCertidao;
	private String custaTaxaJudiciaria;
	private String custaTotal;
	
	public GuiaCertidaoGeralDt() {
		limpar();
	}
	//--------------------------------------------------------------------------------------------
	public String getNome() {		
		return nome;
	}
	
	public void setNome(String nome) {
		if(nome != null){
			this.nome = nome;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getTipoPessoa() {		
		return tipoPessoa;
	}
	
	public void setTipoPessoa(String tipoPessoa) {
		if(tipoPessoa != null){
			this.tipoPessoa = tipoPessoa;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getRg() {		
		return rg;
	}
	
	public void setRg(String rg) {
		if(rg != null){
			this.rg = rg;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_RgOrgaoExpedidor() {
		 return id_RgOrgaoExpedidor;
	}
	
	public void setId_RgOrgaoExpedidor(String id_RgOrgaoExpedidor) {		
		if(id_RgOrgaoExpedidor != null){
			if (id_RgOrgaoExpedidor.equalsIgnoreCase("null")){				
				id_RgOrgaoExpedidor = "";
				rgOrgaoExpedidor = "";
			}
			else if (!id_RgOrgaoExpedidor.equalsIgnoreCase("")){			
				this.id_RgOrgaoExpedidor = id_RgOrgaoExpedidor;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getRgOrgaoExpedidor() {		
		return rgOrgaoExpedidor;
	}
	
	public void setRgOrgaoExpedidor(String rgOrgaoExpedidor) {
		if(rgOrgaoExpedidor != null){
			this.rgOrgaoExpedidor = rgOrgaoExpedidor;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getRgOrgaoExpedidorSigla() {		
		return rgOrgaoExpedidorSigla;
	}
	
	public void setRgOrgaoExpedidorSigla(String rgOrgaoExpedidorSigla) {
		if(rgOrgaoExpedidorSigla != null){
			this.rgOrgaoExpedidorSigla = rgOrgaoExpedidorSigla;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getRgDataExpedicao() {		
		return rgDataExpedicao;
	}
	
	public void setRgDataExpedicao(String rgDataExpedicao) {
		if(rgDataExpedicao != null){
			this.rgDataExpedicao = rgDataExpedicao;
		}
	}
	//--------------------------------------------------------------------------------------------	
	public String getCpf() {		
		return cpf;
	}
	
	public void setCpf(String cpf) {
		if(cpf != null){
			this.cpf = cpf;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_Comarca() {
		 return id_Comarca;
	}
	
	public void setId_Comarca(String id_Comarca) {		
		if(id_Comarca != null){
			if (id_Comarca.equalsIgnoreCase("null")){				
				id_Comarca = "";
				comarca = "";
			}
			else if (!id_Comarca.equalsIgnoreCase("")){			
				this.id_Comarca = id_Comarca;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getComarca() {		
		return comarca;
	}
	
	public void setComarca(String comarca) {
		if(comarca != null){
			this.comarca = comarca;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_Serventia() {
		 return id_Serventia;
	}
	
	public void setId_Serventia(String id_Serventia) {		
		if(id_Serventia != null){
			if (id_Serventia.equalsIgnoreCase("null")){				
				id_Serventia = "";
				serventia = "";
			}
			else if (!id_Serventia.equalsIgnoreCase("")){			
				this.id_Serventia = id_Serventia;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getServentia() {		
		return serventia;
	}
	
	public void setServentia(String serventia) {
		if(serventia != null){
			this.serventia = serventia;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getSexo() {		
		return sexo;
	}
	
	public void setSexo(String sexo) {
		if(sexo != null){
			this.sexo = sexo;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_EstadoCivil() {
		 return id_EstadoCivil;
	}
	
	public void setId_EstadoCivil(String id_EstadoCivil) {		
		if(id_EstadoCivil != null){
			if (id_EstadoCivil.equalsIgnoreCase("null")){				
				id_EstadoCivil = "";
				estadoCivil = "";
			}
			else if (!id_EstadoCivil.equalsIgnoreCase("")){			
				this.id_EstadoCivil = id_EstadoCivil;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getEstadoCivil() {
		return estadoCivil;
	}
	
	public void setEstadoCivil(String estadoCivil) {
		if(estadoCivil != null){
			this.estadoCivil = estadoCivil;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getDataNascimento() {		
		return dataNascimento;
	}
	
	public void setDataNascimento(String dataNascimento) {
		if(dataNascimento != null){
			this.dataNascimento = dataNascimento;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_Naturalidade() {
		 return id_Naturalidade;
	}
	
	public void setId_Naturalidade(String id_Naturalidade) {		
		if(id_Naturalidade != null){
			if (id_Naturalidade.equalsIgnoreCase("null")){				
				id_Naturalidade = "";
				naturalidade = "";
			}
			else if (!id_Naturalidade.equalsIgnoreCase("")){			
				this.id_Naturalidade = id_Naturalidade;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getNaturalidade() {		
		return naturalidade;
	}
	
	public void setNaturalidade(String naturalidade) {
		if(naturalidade != null){
			this.naturalidade = naturalidade;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_Profissao() {
		return id_Profissao;
	}
	
	public void setId_Profissao(String id_Profissao) {		
		if(id_Profissao != null){
			if (id_Profissao.equalsIgnoreCase("null")){				
				id_Profissao = "";
				profissao = "";
			}
			else if (!id_Profissao.equalsIgnoreCase("")){			
				this.id_Profissao = id_Profissao;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getProfissao() {		
		return profissao;
	}
	
	public void setProfissao(String profissao) {
		if(profissao != null){
			this.profissao = profissao;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getDomicilio() {		
		return domicilio;
	}
	
	public void setDomicilio(String domicilio) {
		if(domicilio != null){
			this.domicilio = domicilio;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getNomePai() {		
		return nomePai;
	}
	
	public void setNomePai(String nomePai) {
		if(nomePai != null){
			this.nomePai = nomePai;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getNomeMae() {		
		return nomeMae;
	}
	
	public void setNomeMae(String nomeMae) {
		if(nomeMae != null){
			this.nomeMae = nomeMae;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getRegistroGeral() {		
		return registroGeral;
	}
	
	public void setRegistroGeral(String registroGeral) {
		if(registroGeral != null){
			this.registroGeral = registroGeral;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getPatente() {		
		return patente;
	}
	
	public void setPatente(String patente) {
		if(patente != null){
			this.patente = patente;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getId_Nacionalidade() {
		return id_Nacionalidade;
	}
	
	public void setId_Nacionalidade(String id_Nacionalidade) {		
		if(id_Nacionalidade != null){
			if (id_Nacionalidade.equalsIgnoreCase("null")){				
				id_Nacionalidade = "";
				nacionalidade = "";
			}
			else if (!id_Nacionalidade.equalsIgnoreCase("")){			
				this.id_Nacionalidade = id_Nacionalidade;
			}			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getNacionalidade() {		
		return nacionalidade;
	}
	
	public void setNacionalidade(String nacionalidade) {
		if(nacionalidade != null){
			this.nacionalidade = nacionalidade;
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getOab() {
		return oab;
	}
	
	public String getOabNumero() {
		return oab;
	}

	public void setOab(String oab) {
		if(oab != null){
			this.oab = oab;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getOabComplemento() {
		return oabComplemento;
	}
	
	public void setOabComplemento(String oabComplemento) {
		if (oabComplemento != null){
			this.oabComplemento = oabComplemento;			
		}
	}	
	//--------------------------------------------------------------------------------------------
	public String getOabUf() {
		return oabUf;
	}
	
	public void setOabUf(String oabUf) {
		if(oabUf != null){
			this.oabUf = oabUf;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getOabUfCodigo() {
		return oabUfCodigo;
	}
	
	public void setOabUfCodigo(String oabUfCodigo) {
		if(oabUfCodigo != null){
			this.oabUfCodigo = oabUfCodigo;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getTextoForense() {
		return textoForense;
	}

	public void setTextoForense(String texto) {
		if (texto != null){
			this.textoForense = texto;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getAnoInicial() {
		return anoInicial;
	}
	
	public void setAnoInicial(String anoInicial) {
		if (anoInicial != null){
			this.anoInicial = anoInicial;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getAnoFinal() {
		return anoFinal;
	}
	
	public void setAnoFinal(String anoFinal) {
		if (anoFinal != null){
			this.anoFinal = anoFinal;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getMesInicial() {
		return mesInicial;
	}

	public void setMesInicial(String mesInicial) {
		if(mesInicial != null){
			this.mesInicial = mesInicial;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getMesFinal() {
		return mesFinal;
	}

	public void setMesFinal(String mesFinal) {
		if(mesFinal != null){
			this.mesFinal = mesFinal;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public List getListaProcesso() {
		return this.listaProcesso;
	}

	public void setListaProcesso(List listaProcesso) {
		if(listaProcesso != null){
			this.listaProcesso = listaProcesso;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;			
	}
	//--------------------------------------------------------------------------------------------
	public int getModeloCodigo() {
		return modeloCodigo;
	}

	public void setModeloCodigo(int praticaForenseQuantitativaModeloCodigo) {
			this.modeloCodigo = praticaForenseQuantitativaModeloCodigo;
	}
	//--------------------------------------------------------------------------------------------
	public String getMesTexto(String mesNumero) {
		int i = Funcoes.StringToInt(mesNumero);
		return this.vetorMes[i];
	}
	//--------------------------------------------------------------------------------------------
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		if(tipo != null)
		this.tipo = tipo;
	}
	//--------------------------------------------------------------------------------------------
	public String getDataTimeFinal() {
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(Funcoes.getUltimoDiaMes(this.mesFinal, this.anoFinal));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return Funcoes.DataHora(calendar.getTime());
	}
	//--------------------------------------------------------------------------------------------
	public String getDataTimeInicial() {
		
		Calendar calendar = Calendar.getInstance(); //O mês no objeto Calendar inicia de 0, portando Jan = 0 e Dez = 11
		calendar.set(Funcoes.StringToInt(this.anoInicial), (Funcoes.StringToInt(this.mesInicial) - 1), 1,0,0,0);	
		return Funcoes.DataHora(calendar.getTime());
	}
	//--------------------------------------------------------------------------------------------
	public String getCustaCertidao() {
		return this.custaCertidao;
	}

	public void setCustaCertidao(String custaCertidao) {
		if(custaCertidao != null){
			this.custaCertidao = custaCertidao;			
		}
	}
	//--------------------------------------------------------------------------------------------
	public String getCustaTaxaJudiciaria() {
		return custaTaxaJudiciaria;
	}

	public void setCustaTaxaJudiciaria(String custaTaxaJudiciaria) {
		this.custaTaxaJudiciaria = custaTaxaJudiciaria;			
	}	
	//--------------------------------------------------------------------------------------------
	public String getCustaTotal() {
		return custaTotal;
	}

	public void setCustaTotal(String custaTotal) {
		this.custaTotal = custaTotal;			
	}	
	//--------------------------------------------------------------------------------------------

	public void limpar(){
		
		nome = "";
		cpf = "";
		tipoPessoa = "";
		id_Naturalidade = "";
		naturalidade = "";
		sexo = "";
		dataNascimento = "";
		rg = "";
		id_RgOrgaoExpedidor = "";
		rgOrgaoExpedidor = "";
		rgOrgaoExpedidorSigla = "";
		rgDataExpedicao = "";
		id_EstadoCivil = "";
		estadoCivil = "";
		id_Profissao = "";
		profissao = "";
		domicilio = "";
		nomePai = "";
		nomeMae = "";
		registroGeral = "";
		patente = "";
		id_Nacionalidade = "";
		nacionalidade = "";
		
		oab = "";
		oabComplemento = "";
		oabUf = "";
		oabUfCodigo = "";
		textoForense = "";
		Calendar cal = Calendar.getInstance();  
        int year = cal.get(Calendar.YEAR);  
		anoFinal = String.valueOf(year);
		anoInicial = String.valueOf(year);
		mesFinal = "2";
		mesInicial = "1";
		listaProcesso = new ArrayList(5);
		quantidade = 0;
		modeloCodigo = 0;
		tipo = "Quantitativa";
		
		id_Comarca = "";
		comarca = "";
		id_Serventia = "";
		serventia = "";
		
		custaCertidao = "";
		custaTaxaJudiciaria = "";
		custaTotal = "";
	}
	
	public void copiar(GuiaCertidaoGeralDt objeto){
		
		if (objeto == null){
			return;
		}		
		nome = objeto.getNome();
		cpf = objeto.getCpf();
		tipoPessoa = objeto.getTipoPessoa();
		id_Naturalidade = objeto.getId_Naturalidade();
		naturalidade = objeto.getNaturalidade();
		sexo = objeto.getSexo();
		dataNascimento = objeto.getDataNascimento();
		rg = objeto.getRg();
		id_RgOrgaoExpedidor = objeto.getId_RgOrgaoExpedidor();
		rgOrgaoExpedidor = objeto.getRgOrgaoExpedidor();
		rgOrgaoExpedidorSigla = objeto.getRgOrgaoExpedidorSigla();
		rgDataExpedicao = objeto.getRgDataExpedicao();
		id_EstadoCivil = objeto.getId_EstadoCivil();
		estadoCivil = objeto.getEstadoCivil();
		id_Profissao = objeto.getId_Profissao();
		profissao = objeto.getProfissao();
		domicilio = objeto.getDomicilio();
		nomePai = objeto.getNomePai();
		nomeMae = objeto.getNomeMae();
		registroGeral = objeto.getRegistroGeral();
		patente = objeto.getPatente();
		id_Nacionalidade = objeto.getId_Nacionalidade();
		nacionalidade = objeto.getNacionalidade();
		
		oab = objeto.getOab();
		oabComplemento = objeto.getOabComplemento();
		oabUf = objeto.getOabUf();
		oabUfCodigo = objeto.getOabUfCodigo();
		textoForense = objeto.getTextoForense();
		anoFinal = objeto.getAnoFinal();
		anoInicial = objeto.getAnoInicial();
		mesFinal = objeto.getMesFinal();
		mesInicial = objeto.getMesInicial();
		listaProcesso = objeto.getListaProcesso();
		quantidade = objeto.getQuantidade();
		modeloCodigo = objeto.getModeloCodigo();
		tipo = objeto.getTipo();
		
		id_Comarca = objeto.getId_Comarca();
		comarca = objeto.getComarca();
		id_Serventia = objeto.getId_Serventia();
		serventia = objeto.getServentia();
		
		custaCertidao = objeto.getCustaCertidao();
		custaTaxaJudiciaria = objeto.getCustaTaxaJudiciaria();
		custaTotal = objeto.getCustaTotal();
	}	

	public String getPropriedades(){

		return "[Nome:" + nome + ";Cpf:" + cpf + ";TipoPessoa:" + tipoPessoa + ";Id_Naturalidade:" + id_Naturalidade + ";Naturalidade:" + naturalidade + ";Sexo:" + sexo + ";DataNascimento:" + dataNascimento + ";Rg:" + rg + 
				";Id_RgOrgaoExpedidor:" + id_RgOrgaoExpedidor + ";RgOrgaoExpedidor:" + rgOrgaoExpedidor + ";RgOrgaoExpedidorSigla:" + rgOrgaoExpedidorSigla + ";" + "RgDataExpedicao:" + rgDataExpedicao + ";Id_EstadoCivil:" + id_EstadoCivil + 
				";EstadoCivil:" + estadoCivil + ";Id_Profissao:" + id_Profissao + ";Profissao:" + profissao + ";Domicilio:" + domicilio + ";NomePai:" + nomePai + ";NomeMae:" + nomeMae + ";RegistroGeral:" + registroGeral + 
				";Patente:" + patente + ";Id_Nacionalidade:" + id_Nacionalidade + ";Nacionalidade:" + nacionalidade + ";CustaCertidao:" + custaCertidao + ";CustaTaxaJudiciaria:" + custaTaxaJudiciaria + ";CustaTotal:" + custaTotal +
				";Id_Comarca:" + id_Comarca + ";Comarca:" + comarca + ";id_Serventia:" + id_Serventia + ";Serventia:" + serventia + "]";
	}	
	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub		
	}	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
