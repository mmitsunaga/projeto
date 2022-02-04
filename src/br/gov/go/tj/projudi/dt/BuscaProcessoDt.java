package br.gov.go.tj.projudi.dt;

import br.gov.go.tj.utils.Funcoes;

public class BuscaProcessoDt extends Dados {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7319202590188745124L;
	
	public static final int CONSULTA_DEFAULT = 0;
	public static final int CONSULTA_PROCESSOS_ADVOGADO = 1;
	public static final int CONSULTA_USUARIO_INTERNO = 2;
	public static final int CONSULTA_USUARIO_EXTERNO = 4;
	public static final int CONSULTA_PROCESSOS_CLASSIFICAR = 5;
	public static final int CONSULTA_PROCESSOS_ARQUIVADOS_SEM_MOTIVO = 6;
	public static final int CONSULTA_PROCESSOS_INCONSISTENCIA_POLO_PASSIVO = 7;
	public static final int CONSULTA_PROCESSOS_PRISAO_FORA_PRAZO = 8;
	public static final int CONSULTA_PROCESSOS_PRESCRITOS = 9;
	public static final int CONSULTA_PROCESSOS_SEM_ASSUNTO = 10;
	public static final int CONSULTA_PROCESSOS_COM_ASSUNTO_PAI = 11;
	public static final int CONSULTA_PROCESSOS_COM_CLASSE_PAI = 12;
	public static final int CONSULTA_PROCESSOS_INQUERITO = 13;
	
	
	private String Id_ProcessoStatus;
	private String Id_Serventia;
	private String Id_Comarca;
	private String Id_Classificador;
	private String Id_ServentiaSubTipo;
	private String Id_Assunto;
	private String Id_ProcessoTipo;
	private String Id_Relator;
	private String Id_UsuarioServentia;
	private String Id_ServentiaCargo;
	private String Id_ClassificadorAlteracao;
	private String Id;
	
	private String NomeParte;
	private String ProcessoNumero;
	private String ProcessoStatus;
	private String ProcessoStatusCodigo;	
	private String Serventia;
	private String Comarca;
	private String Classificador;
	private String ServentiaSubTipo;
	private String TcoNumero;
	private String OabNumero;
	private String MinValor;
	private String MaxValor;	
	private String Assunto;
	private String ProcessoTipo;
	private String PoloPassivoNull;
	private String CpfPoloPassivo;
	private String PoloAtivoNull;
	private String CpfPoloAtivo;
	private String PesquisarNomeExato;
	private String OabUf;
	private String OabComplemento;
	private String UsuarioServentia;
	private String Relator;
	private String ServentiaCargoRelator;
	private String ProcessoTipoCodigo;
	private String Promovente;	
	private String SituacaoAdvogadoProcesso;
	private String ProcessoNumeroSimples;
	private String ServentiaCargo;
	private String ServentiaCargoUsuario;
	private String ClassificadorAlteracao;
	private String SegredoJustica;
	private String CpfCnpjParte;
	private String Inquerito;


	private String Id_ServentiaUsuario;

	private int inProcessoNumero=-1;
	private int inDigito=-1;
	private int inAno=-1;
	private long loCpfCnpjParte=-1;
	
	public BuscaProcessoDt() {
		limpar();
	}
	
	@Override
	public void setId(String id) {
		Id = id;		
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return Id;
	}

	/**
	 * Limpa as variavéis de consulta de processos
	 */
	public void limpar() {
		super.limpar();
		Id="";		
		NomeParte="";
		ProcessoNumero="";
		Id_ProcessoStatus="";
		ProcessoStatus="";
		ProcessoStatusCodigo="";	
		CpfCnpjParte="";
		Id_Serventia="";
		Serventia="";
		Id_Comarca="";
		Comarca="";
		Id_Classificador="";
		Classificador="";
		Id_ServentiaSubTipo="";
		ServentiaSubTipo="";
		TcoNumero="";
		OabNumero="";
		MinValor="";
		MaxValor="";	
		Id_Assunto="";
		Assunto="";
		Id_ProcessoTipo="";
		ProcessoTipo="";
		PoloPassivoNull="";
		CpfPoloPassivo="";
		PoloAtivoNull="";
		CpfPoloAtivo="";
		PesquisarNomeExato="";
		OabUf="";
		OabComplemento="";
		UsuarioServentia="";
		Id_Relator="";
		Relator="";
		ServentiaCargoRelator="";
		ProcessoTipoCodigo="";
		Promovente="";
		SituacaoAdvogadoProcesso="";
		SituacaoAdvogadoProcesso="";
		ProcessoNumeroSimples="";
		Id_UsuarioServentia="";
		Id_ServentiaCargo="";
		ServentiaCargo="";
		ServentiaCargoUsuario="";
		ClassificadorAlteracao="";
		Id_ClassificadorAlteracao="";
		Id_ServentiaUsuario="";
		inProcessoNumero=-1;
		inDigito=-1;
		inAno=-1;
		loCpfCnpjParte=-1;
		Inquerito="";

	}

	public String getCpfCnpjParte() {		
		return CpfCnpjParte;
	}
	
	public long getCpfCnpjParteToLong() {		
		return loCpfCnpjParte;
	}
	
	public void setCpfCnpjParte(long cpfCnpjParte) {
		CpfCnpjParte = Funcoes.LongToString(cpfCnpjParte);
		loCpfCnpjParte = cpfCnpjParte;				
	}
	
	public void setCpfCnpjParte(String cpfCnpjParte) {
		if (cpfCnpjParte!=null && !cpfCnpjParte.isEmpty() ) {
			loCpfCnpjParte =Funcoes.StringToLong( cpfCnpjParte.replaceAll("[/.-]", ""),-1);
		}		
	}

	public String getNomeParte() {
		return NomeParte;
	}

	public void setNomeParte(String nomeParte) {
		NomeParte = nomeParte;
	}

	public String getProcessoNumero() {
		return ProcessoNumero;
	}

	public void setProcessoNumero(String processoNumero) {		
		ProcessoNumero = processoNumero;
		
		if (ProcessoNumero!=null && !ProcessoNumero.isEmpty()) {
			processoNumero = ProcessoNumero;
			processoNumero = processoNumero.replaceAll("-", ".").replaceAll(",", ".");
			String[] stTemp = processoNumero.split("\\.");
			if (stTemp.length >= 1) { 
				inProcessoNumero =  Funcoes.StringToInt(stTemp[0],-1);
			}else {
				processoNumero = "";
			}
			if (stTemp.length >= 2) {
				inDigito = Funcoes.StringToInt(stTemp[1],(int)-1);
			}
			if (stTemp.length >= 3) {
				inAno = Funcoes.StringToInt(stTemp[2],-1);
			}
		}
	}
	
	public int getDigito() {
		return inDigito;
	}
	public int getAno() {
		return inAno;
	}	
	public int getProcessoNumeroToInt() {
		return inProcessoNumero;
	}

	public String getProcessoStatusCodigo() {
		return ProcessoStatusCodigo;
	}

	public void setProcessoStatusCodigo(String processoStatusCodigo) {
		if (processoStatusCodigo == null) {
			//o padrão é consulta de processos ativos
			ProcessoStatus="Ativo";
			processoStatusCodigo= String.valueOf(ProcessoStatusDt.ATIVO);
		}else if (processoStatusCodigo != null && !processoStatusCodigo.equalsIgnoreCase("null"))
			ProcessoStatusCodigo = processoStatusCodigo;
	}

	public String getId_Serventia() {
		return Id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		Id_Serventia = id_Serventia;
	}

	public String getId_Comarca() {
		return Id_Comarca;
	}

	public void setId_Comarca(String id_Comarca) {
		Id_Comarca = id_Comarca;
	}

	public String getId_Classificador() {
		return Id_Classificador;
	}

	public void setId_Classificador(String id_Classificador) {
		Id_Classificador = id_Classificador;
	}

	public String getId_ServentiaSubTipo() {
		return Id_ServentiaSubTipo;
	}

	public void setId_ServentiaSubTipo(String id_ServentiaSubTipo) {
		Id_ServentiaSubTipo = id_ServentiaSubTipo;
	}

	public String getTcoNumero() {
		return TcoNumero;
	}

	public void setTcoNumero(String tcoNumero) {
		TcoNumero = tcoNumero;
	}

	public String getOabNumero() {
		return OabNumero;
	}

	public void setOabNumero(String oabNumero) {
		OabNumero = oabNumero;
	}

	public String getMinValor() {
		return MinValor;
	}

	public void setMinValor(String minValor) {
		this.MinValor = minValor;
	}

	public String getMaxValor() {
		return MaxValor;
	}

	public void setMaxValor(String maxValor) {
		this.MaxValor = maxValor;
	}

	public String getId_Assunto() {
		return Id_Assunto;
	}

	public void setId_Assunto(String id_assunto) {
		this.Id_Assunto = id_assunto;
	}

	public String getId_ProcessoTipo() {
		return Id_ProcessoTipo;
	}

	public void setId_ProcessoTipo(String id_proc_tipo) {
		this.Id_ProcessoTipo = id_proc_tipo;
	}

	public String getStPoloPassivoNull() {
		return PoloPassivoNull;
	}

	public void setStPoloPassivoNull(String stPoloPassivoNull) {
		this.PoloPassivoNull = stPoloPassivoNull;
	}

	public void setCpfPoloPassivo(String cpfPoloPassivo) {
		if (cpfPoloPassivo!=null && !cpfPoloPassivo.isEmpty() ) {
			CpfPoloPassivo = cpfPoloPassivo.replaceAll("[/.-]", "");
		}		 
	}

	public String getPoloAtivoNull() {
		return PoloAtivoNull;
	}

	public void setPoloAtivoNull(String poloAtivoNull) {
		PoloAtivoNull = poloAtivoNull;
	}

	public String getCpfPoloAtivo() {
		return CpfPoloAtivo;
	}

	public void setCpfPoloAtivo(String cpfPoloAtivo) {
		if (cpfPoloAtivo!=null && !cpfPoloAtivo.isEmpty() ) {
			CpfPoloAtivo = cpfPoloAtivo.replaceAll("[/.-]", "");
		}			 
	}

	public String getPesquisarNomeExato() {
		return PesquisarNomeExato;
	}

	public void setPesquisarNomeExato(String pesquisarNomeExato) {
		PesquisarNomeExato = pesquisarNomeExato;
	}

	public String getOabUf() {
		return OabUf;
	}

	public void setOabUf(String oabUf) {
		OabUf = oabUf;
	}

	public String getOabComplemento() {
		return OabComplemento;
	}

	public void setOabComplemento(String oabComplemento) {
		OabComplemento = oabComplemento;
	}

	public void setServentia(String serventia) {
		Serventia = serventia;	
	}

	public void setProcessoStatus(String processoStatus) {
		ProcessoStatus = processoStatus;		
	}

	public void setId_ProcessoStatus(String id_processoStatus) {
		Id_ProcessoStatus = id_processoStatus;		
	}

	public void setId_UsuarioServentia(String id_usuarioServentia) {
		Id_UsuarioServentia = id_usuarioServentia;		
	}

	public void setId_Relator(String id_relator) {
		Id_Relator = id_relator;		
	}

	public void setRelator(String relator) {
		Relator = relator;		
	}

	public void setServentiaCargoRelator(String serventiaCargoRelator) {
		ServentiaCargoRelator = serventiaCargoRelator;
		
	}

	public void setAssunto(String assunto) {
		Assunto = assunto;
		
	}

	public void setClassificador(String classificador) {
		Classificador=classificador;		
	}

	public String getComarca() {
		return Comarca;
	}

	public void setComarca(String comarca) {
		Comarca = comarca;
	}

	public String getServentiaSubTipo() {
		return ServentiaSubTipo;
	}

	public void setServentiaSubTipo(String serventiaSubTipo) {
		ServentiaSubTipo = serventiaSubTipo;
	}

	public String getProcessoTipo() {
		return ProcessoTipo;
	}

	public void setProcessoTipo(String processoTipo) {
		ProcessoTipo = processoTipo;
	}

	public String getPoloPassivoNull() {
		return PoloPassivoNull;
	}

	public void setPoloPassivoNull(String poloPassivoNull) {
		PoloPassivoNull = poloPassivoNull;
	}

	public String getCpfPoloPassivo() {
		return CpfPoloPassivo;
	}

	public Object getUsuarioServentia() {
		return UsuarioServentia;
	}

	public void setUsuarioServentia(String usuarioServentia) {
		UsuarioServentia = usuarioServentia;
	}

	public String getId_ProcessoStatus() {
		return Id_ProcessoStatus;
	}

	public String getProcessoStatus() {
		return ProcessoStatus;
	}

	public String getServentia() {
		return Serventia;
	}

	public String getClassificador() {
		return Classificador;
	}

	public String getAssunto() {
		return Assunto;
	}

	public String getId_Relator() {
		return Id_Relator;
	}

	public String getRelator() {
		return Relator;
	}

	public String getServentiaCargoRelator() {
		return ServentiaCargoRelator;
	}

	public void setProcessoTipoCodigo(String processoTipoCodigo) {
		ProcessoTipoCodigo = processoTipoCodigo;
		
	}

	public void setPromovente(String promovente) {
		Promovente = promovente;
		
	}

	public void setSituacaoAdvogadoProcesso(String situacaoAdvogadoProcesso) {
		SituacaoAdvogadoProcesso = situacaoAdvogadoProcesso;
		
	}

	public String getProcessoTipoCodigo() {
		return ProcessoTipoCodigo;
	}

	public String getPromovente() {
		return Promovente;
	}

	public String getSituacaoAdvogadoProcesso() {
		return SituacaoAdvogadoProcesso;
	}

	public boolean temRelator() {		// 
		return Relator!= null && !Relator.isEmpty();
	}

	public boolean isSigiloso() {
		return Funcoes.StringToLong(getProcessoStatusCodigo(),-1)==ProcessoStatusDt.SIGILOSO;
	}

	public String getProcessoNumeroSimples() {
		return ProcessoNumeroSimples;
	}

	public String getId_UsuarioServentia() {
		return Id_UsuarioServentia;
	}
	
	public boolean temId_UsuarioServentia() {
		return Id_UsuarioServentia!=null && !Id_UsuarioServentia.isEmpty();
		                                                                  
	}

	public boolean temId_Serventia() {
		return Id_Serventia!=null && !Id_Serventia.isEmpty();
	}

	public void setId_ServentiaCargo(String id_ServentiaCargo) {
		Id_ServentiaCargo = id_ServentiaCargo;
		
	}

	public void setServentiaCargo(String serventiaCargo) {
		ServentiaCargo = serventiaCargo;
		
	}

	public void setServentiaCargoUsuario(String serventiaCargoUsuario) {
		ServentiaCargoUsuario = serventiaCargoUsuario;
		
	}

	public String getId_ServentiaCargo() {
		return Id_ServentiaCargo;
	}

	public String getServentiaCargo() {
		return ServentiaCargo;
	}

	public String getServentiaCargoUsuario() {
		return ServentiaCargoUsuario;
	}

	public void setProcessoNumeroSimples(String processoNumeroSimples) {
		ProcessoNumeroSimples = processoNumeroSimples;
	}
	public String getClassificadorAlteracao() {
		return ClassificadorAlteracao;
	}
	
	public String getId_ClassificadorAlteracao() {
		return Id_ClassificadorAlteracao;
	}

	public void setClassificadorAlteracao(String classificadorAlteracao) {
		ClassificadorAlteracao = classificadorAlteracao;
	}

	public void setId_ClassificadorAlteracao(String id_ClassificadorAlteracao) {
		Id_ClassificadorAlteracao = id_ClassificadorAlteracao;
	}

	public boolean temId() {		
		return Id!= null && !Id.isEmpty();
	}

	public String getId_Processo() {
		return Id;
	}
	
	public void setInquerito(String inquerito) {
		this.Inquerito =  inquerito;
	}
	
	public String getInquerito() {
		return this.Inquerito;
	}
	
	public boolean temServentiaCargo() {		
		return ServentiaCargo!= null && !ServentiaCargo.isEmpty();
	}
	
	public boolean temServentiaCargoUsuario() {		
		return ServentiaCargoUsuario!= null && !ServentiaCargoUsuario.isEmpty();
	}
	
	public boolean temNumero() {
		return this.ProcessoNumeroSimples != null && !ProcessoNumeroSimples.isEmpty();			
	}
	
	public boolean temInquerito() {
		return this.Inquerito != null && !Inquerito.isEmpty();			
	}

	public void validarEntradas(BuscaProcessoDt objBuscaProcessoDt) {
		if(objBuscaProcessoDt != null) {
			if (getCpfPoloAtivo() == null ) 			setCpfPoloAtivo( objBuscaProcessoDt.getCpfPoloAtivo());
			if (getPoloAtivoNull() == null) 			setPoloAtivoNull(objBuscaProcessoDt.getPoloAtivoNull());  
			if (getCpfPoloPassivo() == null ) 			setCpfPoloPassivo(objBuscaProcessoDt.getCpfPoloPassivo());  
			if (getStPoloPassivoNull() == null )		setStPoloPassivoNull( objBuscaProcessoDt.getStPoloPassivoNull());  
			if (getId_ProcessoTipo() == null ) 			setId_ProcessoTipo( objBuscaProcessoDt.getId_ProcessoTipo());
			if (getId_Assunto() == null ) 				setId_Assunto( objBuscaProcessoDt.getId_Assunto());  
			if (getId_Classificador() == null ) 		setId_Classificador( objBuscaProcessoDt.getId_Classificador());  
			if (getMaxValor() == null ) 				setMaxValor( objBuscaProcessoDt.getMaxValor());  
			if (getMinValor() == null ) 				setMinValor( objBuscaProcessoDt.getMinValor());
			if (getId_Serventia() == null ) 			setId_Serventia(objBuscaProcessoDt.getId_Serventia());		
			if (getNomeParte() == null ) 				setNomeParte( objBuscaProcessoDt.getNomeParte());
			if (getPesquisarNomeExato()==null) 			setPesquisarNomeExato( objBuscaProcessoDt.getPesquisarNomeExato());
			if (getCpfCnpjParteToLong() == -1)			setCpfCnpjParte( objBuscaProcessoDt.getCpfCnpjParte());
			if (getProcessoNumero() == null) 			setProcessoNumero( objBuscaProcessoDt.getProcessoNumero());
			if (getProcessoStatusCodigo()==null)		setProcessoStatusCodigo(objBuscaProcessoDt.getProcessoStatusCodigo());			
			if (getId_Comarca()== null ) 				setId_Comarca(objBuscaProcessoDt.getId_Comarca());					
			if (getOabNumero() == null ) 				setOabNumero( objBuscaProcessoDt.getOabNumero( ));
			if (getOabComplemento() == null ) 			setOabComplemento(objBuscaProcessoDt.getOabComplemento());
			if (getOabUf() == null ) 					setOabUf(objBuscaProcessoDt.getOabUf());
			if (getSituacaoAdvogadoProcesso() == null ) setSituacaoAdvogadoProcesso(objBuscaProcessoDt.getSituacaoAdvogadoProcesso());
			if (getId_ServentiaSubTipo() == null ) 		setId_ServentiaSubTipo(objBuscaProcessoDt.getId_ServentiaSubTipo());
			if (getTcoNumero() == null ) 				setTcoNumero(objBuscaProcessoDt.getTcoNumero());
			if (getId_ServentiaUsuario() == null )		setId_ServentiaUsuario(objBuscaProcessoDt.getId_ServentiaUsuario());
			if (getInquerito() == null )				setInquerito(objBuscaProcessoDt.getInquerito());
		}
	}

	public String getSegredoJustica() {
		return SegredoJustica;
	}

	public void setSegredoJustica(String segredoJustica) {
		SegredoJustica = segredoJustica;
	}

	public boolean isMesmaServentia(String id_Serventia2) {
		return id_Serventia2!=null && !id_Serventia2.isEmpty() && id_Serventia2.equalsIgnoreCase(id_Serventia2);
	}

	public boolean temOabNumero() {		
		return OabNumero!=null && !OabNumero.isEmpty();
	}

	public boolean temComplementoOab() {
		return OabComplemento!=null && !OabComplemento.isEmpty();
	}

	public boolean temUfOab() {
		return OabUf!=null && !OabUf.isEmpty();
	}

	public void setId_ServentiaUsuario(String id_Serventia) {
		Id_ServentiaUsuario = id_Serventia;
		
	}
	
	public String getId_ServentiaUsuario() {
		return Id_ServentiaUsuario;
		
	}
}
