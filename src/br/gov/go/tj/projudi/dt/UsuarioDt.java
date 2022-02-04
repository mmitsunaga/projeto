package br.gov.go.tj.projudi.dt;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.utils.Funcoes;

//---------------------------------------------------------
public class UsuarioDt extends UsuarioDtGen {

	/**
	 * 
	 */
	private static final long serialVersionUID = -608845774058127706L;

	// retirado por solicitaçao da karla
	// public static String liCumprimentos = "";
	// public static String liConclusoes = "";

	private Map<String, Boolean> mapPermissoes = new HashMap<String, Boolean>();

	private List<String> liConsultar = new ArrayList<String>();

	public static final int CodigoPermissao = 216;
	public static final int CodigoPermissaoServidorJudiciario = 157;

	public static final String SistemaProjudi = "1";
	public static final String USUARIO_PUBLICO = "-1";

	// Variáveis para controlar possíveis status de Usuario
	public static final int INATIVO = 0;
	public static final int ATIVO = 1;
	
	private BitSet Permissoes = new BitSet(10000);

	private Map mapPermissaoEspecial = new HashMap();

	private CertificadoDt Certificado =null;
	private String SenhaCertificado=null;
	
	public String getSenhaCertificado() {
		return SenhaCertificado;
	}

	public void setSenhaCertificado(String senhaCertificado) {
		SenhaCertificado = senhaCertificado;
	}

	private String Id_UsuarioServentia;
	private String UsuarioServentiaAtivo;
	private String OabNumero;
	private String OabComplemento;
	private String Id_OabUf;
	private String OabEstado;
	private String Id_Serventia;
	private String Serventia;
	private String ServentiaCodigo;
	private String ServentiaUf;
	private String ServentiaIdArea;
	private String RgOrgaoExpedidorUf;
	private String Id_UsuarioServentiaGrupo;
	private String UsuarioServentiaGrupoAtivo;
	private String Id_Grupo;
	private String Grupo;
	private String GrupoCodigo;
	private String Id_GrupoTipo;
	private String GrupoTipo;
	private String GrupoTipoCodigo;
	private String Id_ServentiaTipo;
	private String ServentiaTipo;
	private String ServentiaTipoCodigo;
	private String ServentiaTipoExterna;
	private String Id_ServentiaCargo;
	private String ServentiaCargo;
	private String CargoTipoCodigo;
	private String CargoTipo;
	private String Usu_Serventia_Oab_Master;

	// dados para mostrar na tela de administracao de usuários logados
	private String DataEntrada;
	private long DataUlitmoAcesso;

	// Dados referentes a serventia
	private String Id_Comarca;
	private String ComarcaCodigo;
	private String Id_Cidade;
	private String Cidade;
	private String Estado;
	private String Id_AudienciaTipo;
	private String EstadoRepresentacao;

	// Dados referentes ao usuário chefe
	private String Id_UsuarioServentiaChefe;
	private String UsuarioServentiaChefe;
	private String Id_ServentiaCargoUsuarioChefe;
	private String GrupoUsuarioChefe;
	private String GrupoTipoUsuarioChefe;
	private String CargoTipoCodigoUsuarioChefe;
	private String CargoTipoUsuarioChefe;
	private String PodeGuardarAssinarUsuarioServentiaChefe;
	private String ServentiaCargoUsuarioChefe;

	private String UsuarioCidade;
	private String UsuarioEstado;
	private String ServentiaSubtipoCodigo;

	// Lista de documentos do usuário
	private List listaArquivosUsuario;
	private List listaUsuariosServentias;
	private EnderecoDt enderecoUsuario;
	private UsuarioServentiaOabDt usuarioServentiaOab;

	// variável criada para controlar a consulta de processo por código de
	// acesso	
	private String Id_ProcessoCodigoAcesso;

	private boolean boReCaptcha = false;

	private String Id_ServentiaSubtipo;


	public void limpar() {
		super.limpar();
		Id_UsuarioServentia = "";
		UsuarioServentiaAtivo = "";
		Id_UsuarioServentiaGrupo = "";
		UsuarioServentiaGrupoAtivo = "";
		Usu_Serventia_Oab_Master = "";
		Id_Serventia = "";
		Serventia = "";
		ServentiaCodigo = "";
		ServentiaIdArea = "";
		ServentiaUf = "";
		RgOrgaoExpedidorUf = "";
		Id_Grupo = "";
		Grupo = "";
		GrupoCodigo = "";
		Id_GrupoTipo = "";
		GrupoTipo = "";
		GrupoTipoCodigo = "";
		Id_ServentiaTipo = "";
		ServentiaTipo = "";
		ServentiaTipoCodigo = "";
		Id_Comarca = "";
		Id_Cidade = "";
		Cidade = "";
		Estado = "";
		Id_ServentiaCargo = "";
		ServentiaCargo = "";
		CargoTipoCodigo = "";
		CargoTipo = "";
		Id_UsuarioServentiaChefe = "";
		UsuarioServentiaChefe = "";
		Id_ServentiaCargoUsuarioChefe = "";
		GrupoUsuarioChefe = "";
		GrupoTipoUsuarioChefe = "";
		CargoTipoCodigoUsuarioChefe = "";
		CargoTipoUsuarioChefe = "";
		ServentiaCargoUsuarioChefe = "";
		PodeGuardarAssinarUsuarioServentiaChefe = "";
		enderecoUsuario = new EnderecoDt();
		usuarioServentiaOab = new UsuarioServentiaOabDt();
		listaUsuariosServentias = null;
		OabNumero = "";
		OabComplemento = "";
		Id_OabUf = "";
		OabEstado = "";
		Id_ServentiaSubtipo="";
	}
	
	public List<String> getLiConsultar() {
		return liConsultar;
	}
	
	public void setLiConsultar(List<String> liConsultar) {
		this.liConsultar = liConsultar;
	}

	public UsuarioServentiaOabDt getUsuarioServentiaOab() {
		return usuarioServentiaOab;
	}

	public void setUsuarioServentiaOab(UsuarioServentiaOabDt usuarioServentiaOab) {
		this.usuarioServentiaOab = usuarioServentiaOab;
	}

	public EnderecoDt getEnderecoUsuario() {
		return enderecoUsuario;
	}

	public void setEnderecoUsuario(EnderecoDt enderecoUsuario) {
		this.enderecoUsuario = enderecoUsuario;
	}

	public String getUsuarioServentiaAtivo() {
		return UsuarioServentiaAtivo;
	}

	public void setUsuarioServentiaAtivo(String usuarioServentiaAtivo) {
		if (usuarioServentiaAtivo != null)
			UsuarioServentiaAtivo = usuarioServentiaAtivo;
	}

	public List getListaUsuarioServentias() {
		return listaUsuariosServentias;
	}

	public void setListaUsuarioServentias(List listaUsuariosServentias) {
		if (listaUsuariosServentias != null)
			this.listaUsuariosServentias = listaUsuariosServentias;
	}

	public String getCidade() {
		if(enderecoUsuario != null) {
			return enderecoUsuario.getCidade();
		}
		return "";
	}

	public String getEstado() {
		if(enderecoUsuario != null) {
			return enderecoUsuario.getEstado();
		}
		return "";
	}

	public String getGrupo() {
		return Grupo;
	}

	public void setGrupo(String grupo) {
		if (grupo != null)
			Grupo = grupo;
	}

	public String getGrupoCodigo() {
		return GrupoCodigo;
	}

	public void setGrupoCodigo(String grupoCodigo) {
		GrupoCodigo = grupoCodigo;
	}

	public String getId_Cidade() {
		return Id_Cidade;
	}

	public void setId_Cidade(String id_Cidade) {
		Id_Cidade = id_Cidade;
	}

	public String getId_Comarca() {
		return Id_Comarca;
	}

	public void setId_Comarca(String id_Comarca) {
		Id_Comarca = id_Comarca;
	}

	public String getId_Serventia() {
		return Id_Serventia;
	}

	public void setId_Serventia(String id_Serventia) {
		if (id_Serventia != null)
			if (id_Serventia.equalsIgnoreCase("null")) {
				Id_Serventia = "";
				Serventia = "";
			} else if (!id_Serventia.equalsIgnoreCase(""))
				Id_Serventia = id_Serventia;
	}

	public String getId_UsuarioServentiaChefe() {
		return Id_UsuarioServentiaChefe;
	}

	public void setId_UsuarioServentiaChefe(String id_UsuarioServentiaChefe) {
		if (id_UsuarioServentiaChefe != null)
			Id_UsuarioServentiaChefe = id_UsuarioServentiaChefe;
	}

	public String getUsuarioServentiaChefe() {
		return UsuarioServentiaChefe;
	}

	public void setUsuarioServentiaChefe(String usuarioServentiaChefe) {
		if (usuarioServentiaChefe != null)
			UsuarioServentiaChefe = usuarioServentiaChefe;
	}

	public String getEstadoRepresentacao() {
		return EstadoRepresentacao;
	}

	public void setEstadoRepresentacao(String estadoRepresentacao) {
		if (estadoRepresentacao != null)
			EstadoRepresentacao = estadoRepresentacao;
	}

	public String getId_ServentiaTipo() {
		return Id_ServentiaTipo;
	}

	public void setId_ServentiaTipo(String id_ServentiaTipo) {
		Id_ServentiaTipo = id_ServentiaTipo;
	}

	public String getId_UsuarioServentia() {
		return Id_UsuarioServentia;
	}

	public void setId_UsuarioServentia(String id_UsuarioServentia) {
		Id_UsuarioServentia = id_UsuarioServentia;
	}

	public String getRgOrgaoExpedidorUf() {
		return RgOrgaoExpedidorUf;
	}

	public void setRgOrgaoExpedidorUf(String rgOrgaoExpedidorUf) {
		if (rgOrgaoExpedidorUf != null)
			RgOrgaoExpedidorUf = rgOrgaoExpedidorUf;
	}

	public String getServentia() {
		return Serventia;
	}

	public void setServentia(String serventia) {
		if (serventia != null)
			Serventia = serventia;
	}

	public String getServentiaCodigo() {
		return ServentiaCodigo;
	}

	public void setServentiaCodigo(String serventiaCodigo) {
		ServentiaCodigo = serventiaCodigo;
	}

	public String getServentiaUf() {
		return ServentiaUf;
	}

	public void setServentiaUf(String serventiaUf) {
		if (serventiaUf != null)
			ServentiaUf = serventiaUf;
	}

	public String getId_AudienciaTipo() {
		return Id_AudienciaTipo;
	}

	public void setId_AudienciaTipo(String id_AudienciaTipo) {
		Id_AudienciaTipo = id_AudienciaTipo;
	}

	public String getServentiaTipo() {
		return ServentiaTipo;
	}

	public void setServentiaTipo(String serventiaTipo) {
		ServentiaTipo = serventiaTipo;
	}

	public String getServentiaTipoCodigo() {
		return ServentiaTipoCodigo;
	}

	public void setServentiaTipoCodigo(String serventiaTipoCodigo) {
		if (serventiaTipoCodigo != null)
			ServentiaTipoCodigo = serventiaTipoCodigo;
	}

	public String getServentiaTipoExterna() {
		return ServentiaTipoExterna;
	}

	public void setServentiaTipoExterna(String serventiaTipoExterna) {
		ServentiaTipoExterna = serventiaTipoExterna;
	}

	public void setPermissao(int index) {

		Permissoes.set(index, true);

	}

	public boolean getPermissao(int index) {

		// System.out.print("{");
		// for (int i=0; i<8000; i++)
		// System.out.print(Permissoes.get(i) + ",");
		// //System.out.println("}");

		return Permissoes.get(index);

	}

	public void setPermissoes(BitSet permissoes) {

		this.Permissoes = permissoes;

	}

	/*
	 * public BitSet getPermissoes1() {
	 * 
	 * return this.Permissoes;
	 * 
	 * }
	 */

	public String getId_Grupo() {
		return Id_Grupo;
	}

	public void setId_Grupo(String id_Grupo) {
		if (id_Grupo != null)
			Id_Grupo = id_Grupo;
	}

	public String getServentiaSubtipoCodigo() {
		return ServentiaSubtipoCodigo;
	}

	public void setServentiaSubtipoCodigo(String serventiaSubtipoCodigo) {
		this.ServentiaSubtipoCodigo = serventiaSubtipoCodigo;
	}

	public String getUsuarioCidade() {
		return UsuarioCidade;
	}

	public void setUsuarioCidade(String usuarioCidade) {
		UsuarioCidade = usuarioCidade;
	}

	public String getUsuarioEstado() {
		return UsuarioEstado;
	}

	public void setUsuarioEstado(String usuarioEstado) {
		UsuarioEstado = usuarioEstado;
	}

	public String getComarcaCodigo() {
		return ComarcaCodigo;
	}

	public void setComarcaCodigo(String comarcaCodigo) {
		ComarcaCodigo = comarcaCodigo;
	}

	public String getId_UsuarioServentiaGrupo() {
		return Id_UsuarioServentiaGrupo;
	}

	public void setId_UsuarioServentiaGrupo(String id_UsuarioServentiaGrupo) {
		if (id_UsuarioServentiaGrupo != null)
			Id_UsuarioServentiaGrupo = id_UsuarioServentiaGrupo;
	}

	public Map getMapPermissaoEspecial() {
		return mapPermissaoEspecial;
	}

	public void addPermissaoEspecial(int permissaoEspecialCodigo, String Menu) {
		mapPermissaoEspecial.put(permissaoEspecialCodigo, Menu);
	}

	public String getDataEntrada() {
		return DataEntrada;
	}

	public void setDataEntrada(String dataEntrada) {
		DataEntrada = dataEntrada;
	}
	
	public long getTimeaUlitmoAcesso() {
		return DataUlitmoAcesso;
	}
	
	public String getDataUlitmoAcesso() {
		return Funcoes.DataHora(DataUlitmoAcesso);
	}

	public void setDataUlitmoAcesso(long dataUlitmoAcesso) {
		DataUlitmoAcesso = dataUlitmoAcesso;
	}

	public String getId_OabUf() {
		return Id_OabUf;
	}

	public void setId_OabUf(String valor) {
		if (valor != null)
			Id_OabUf = valor;
	}

	public String getOabEstado() {
		return OabEstado;
	}

	public void setOabEstado(String valor) {
		if (valor != null)
			OabEstado = valor;
	}

	public String getOabNumero() {
		return OabNumero;
	}

	public void setOabNumero(String valor) {
		if (valor != null)
			OabNumero = valor;
	}

	public String getOabComplemento() {
		return OabComplemento;
	}

	public void setOabComplemento(String valor) {
		if (valor != null)
			OabComplemento = valor;
	}

	public String getId_ServentiaCargo() {
		return Id_ServentiaCargo;
	}

	public void setId_ServentiaCargo(String id_ServentiCargo) {
		if (id_ServentiCargo != null)
			Id_ServentiaCargo = id_ServentiCargo;
	}
	
	public String getServentiaCargo() {
		return ServentiaCargo;
	}

	public void setServentiaCargo(String serventiCargo) {
		if (serventiCargo != null)
			ServentiaCargo = serventiCargo;
	}

	public String getId_ServentiaCargoUsuarioChefe() {
		return Id_ServentiaCargoUsuarioChefe;
	}

	public void setId_ServentiaCargoUsuarioChefe(
			String id_ServentiaCargoUsuarioChefe) {
		if (id_ServentiaCargoUsuarioChefe != null)
			Id_ServentiaCargoUsuarioChefe = id_ServentiaCargoUsuarioChefe;
	}

	public String getCargoTipo() {
		return CargoTipo;
	}

	public void setCargoTipo(String cargoTipo) {
		if (cargoTipo != null)
			CargoTipo = cargoTipo;
	}

	public String getGrupoUsuarioChefe() {
		return GrupoUsuarioChefe;
	}

	public void setGrupoUsuarioChefe(String grupoUsuarioChefe) {
		if (grupoUsuarioChefe != null)
			GrupoUsuarioChefe = grupoUsuarioChefe;
	}

	public String getGrupoTipoUsuarioChefe() {
		return GrupoTipoUsuarioChefe;
	}

	public void setGrupoTipoUsuarioChefe(String grupoTipoUsuarioChefe) {
		if (grupoTipoUsuarioChefe != null)
			GrupoTipoUsuarioChefe = grupoTipoUsuarioChefe;
	}

	public List getListaArquivosUsuario() {
		return listaArquivosUsuario;
	}

	public void setListaArquivosUsuario(List listaArquivosUsuario) {
		this.listaArquivosUsuario = listaArquivosUsuario;
	}

	public String getCargoTipoCodigo() {
		return CargoTipoCodigo;
	}

	public void setCargoTipoCodigo(String cargoTipoCodigo) {
		if (cargoTipoCodigo != null)
			CargoTipoCodigo = cargoTipoCodigo;
	}

	public String getCargoTipoCodigoUsuarioChefe() {
		return CargoTipoCodigoUsuarioChefe;
	}

	public void setCargoTipoCodigoUsuarioChefe(
			String cargoTipoCodigoUsuarioChefe) {
		if (cargoTipoCodigoUsuarioChefe != null)
			CargoTipoCodigoUsuarioChefe = cargoTipoCodigoUsuarioChefe;
	}

	public String getCargoTipoUsuarioChefe() {
		return CargoTipoUsuarioChefe;
	}

	public void setCargoTipoUsuarioChefe(String cargoTipoUsuarioChefe) {
		if (cargoTipoUsuarioChefe != null)
			CargoTipoUsuarioChefe = cargoTipoUsuarioChefe;
	}
	
	public String getServentiaCargoUsuarioChefe() {
		return ServentiaCargoUsuarioChefe;
	}

	public void setServentiaCargoUsuarioChefe(String serventiaCargoUsuarioChefe) {
		if (ServentiaCargoUsuarioChefe != null)
		ServentiaCargoUsuarioChefe = serventiaCargoUsuarioChefe;
	}

	public String getPodeGuardarAssinarUsuarioServentiaChefe() {
		return PodeGuardarAssinarUsuarioServentiaChefe;
	}

	public void setPodeGuardarAssinarUsuarioServentiaChefe(String podeGuardarAssinarUsuarioServentiaChefe) {
		if (podeGuardarAssinarUsuarioServentiaChefe != null) {
			PodeGuardarAssinarUsuarioServentiaChefe = podeGuardarAssinarUsuarioServentiaChefe;
		}
	}

	public boolean isPodeGuardarParaAssinar() {
		if (this.getPodeGuardarAssinarUsuarioServentiaChefe() == null)
			return false;
		return (this.getPodeGuardarAssinarUsuarioServentiaChefe().trim().equalsIgnoreCase("true"));
	}

	public String getUsuarioServentiaGrupoAtivo() {
		return UsuarioServentiaGrupoAtivo;
	}

	public void setUsuarioServentiaGrupoAtivo(String usuarioServentiaGrupoAtivo) {
		if (usuarioServentiaGrupoAtivo != null)
			UsuarioServentiaGrupoAtivo = usuarioServentiaGrupoAtivo;
	}
	
	public String getUsu_Serventia_Oab_Master() {
		return Usu_Serventia_Oab_Master;
	}

	public void setUsu_Serventia_Oab_Master(String usu_Serventia_Oab_Master) {
		if (usu_Serventia_Oab_Master != null)
			Usu_Serventia_Oab_Master = usu_Serventia_Oab_Master;
	}

	public String getId_GrupoTipo() {
		return Id_GrupoTipo;
	}

	public void setId_GrupoTipo(String id_GrupoTipo) {
		if (id_GrupoTipo != null)
			Id_GrupoTipo = id_GrupoTipo;
	}

	public String getGrupoTipo() {
		return GrupoTipo;
	}

	public void setGrupoTipo(String grupoTipo) {
		if (grupoTipo != null)
			GrupoTipo = grupoTipo;
	}

	public String getGrupoTipoCodigo() {
		return GrupoTipoCodigo;
	}

	public void setGrupoTipoCodigo(String grupoTipoCodigo) {
		if (grupoTipoCodigo != null)
			GrupoTipoCodigo = grupoTipoCodigo;
	}

	//será turma julgadora quando o subtipo serventia for de uma das turmas
	public boolean isTurmaJulgadora() {				
		return ServentiaSubtipoDt.isTurma(getServentiaSubtipoCodigo());
	}

	//será segundo grau quando o subtipo serventia for de um dos tipos de segundo grau
	public boolean isSegundoGrau() {
		return ServentiaSubtipoDt.isSegundoGrau(getServentiaSubtipoCodigo());
	}

	//será gabinete de segundo grau quando o subtipo serventia for de um dos tipos de gabinetes de segundo grau
	public boolean isGabineteSegundoGrau() {		
		return ServentiaSubtipoDt.isGabineteSegundoGrau(getServentiaSubtipoCodigo());
	}
		
	public boolean isUPJ() {
		return ServentiaSubtipoDt.isUPJs(getServentiaSubtipoCodigo());
	}
	
	public boolean isUPJSegundoGrau() {
		return (ServentiaTipoDt.isSegundoGrau(getServentiaTipoCodigo()) && isUPJ());
	}
	
	public boolean isUPJTurmaRecursal() {				
		return ServentiaSubtipoDt.isUPJTurmaRecursal(getServentiaSubtipoCodigo());
	}
	
	public boolean isUpjJuizadoEspecialFazendaPublica() {
		return ServentiaSubtipoDt.isUPJJuizadoEspecialFazendaPublica(getServentiaSubtipoCodigo());
	}

	public int getGrupoTipoCodigoToInt() {

		return Funcoes.StringToInt(GrupoTipoCodigo);
	}

	public int getGrupoCodigoToInt() {

		return Funcoes.StringToInt(GrupoCodigo);
	}

	public boolean isAdvogadoParticular() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		
		if (grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR|| grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO)	{											
			return true;
		}

		return false;
	}

	public boolean isMp() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if ((grupo == GrupoDt.MINISTERIO_PUBLICO ) || (grupo == GrupoDt.MP_TCE) )
			return true;

		return false;
	}
	
	public boolean isMagistrado() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupo == GrupoDt.JUIZ_EXECUCAO_PENAL ||
			grupo == GrupoDt.JUIZES_TURMA_RECURSAL ||
			grupo == GrupoDt.JUIZES_VARA ||
			grupo == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU ||
			grupo == GrupoDt.MAGISTRADO_UPJ_SEGUNDO_GRAU ||
			grupo == GrupoDt.JUIZ_CORREGEDOR ||
			grupo == GrupoDt.JUIZ_AUXILIAR_PRESIDENCIA ||
			grupo == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL ||
			grupo == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL ||
			grupo == GrupoDt.DESEMBARGADOR ||
			grupo == GrupoDt.PRESIDENTE_SEGUNDO_GRAU ||
			grupo == GrupoDt.PRESIDENTE_TJGO) {
			return true;
		}

		return false;
	}

	public boolean isAdvogado() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_ESTADUAL
				|| grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO
				|| grupoCodigo == GrupoDt.ADVOGADO_DEFENSOR_PUBLICO
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_UNIAO)
			return true;

		return false;
	}

	public boolean isContador() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupoCodigo == GrupoDt.CONTADORES_VARA)
			return true;

		return false;
	}

	public boolean isAnalistaJudiciario() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupoCodigo == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL
				|| grupoCodigo == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL
				|| grupoCodigo == GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL
				|| grupoCodigo == GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL)
			return true;

		return false;
	}
	
	public boolean isTecnicoJudiciario() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupoCodigo == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL
				|| grupoCodigo == GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL
				|| grupoCodigo == GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL
				|| grupoCodigo == GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL
				|| grupoCodigo ==  GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL)
			return true;

		return false;
	}

	public boolean hasServentiaCargo() {
		String id_serventiaCargo = getId_ServentiaCargo();
		if (id_serventiaCargo != null && id_serventiaCargo.length() > 0)
			return true;

		return false;
	}

	public boolean isAtivo() {
		if (getAtivo().equals("true") || getAtivo().equals("1")) {
			return true;
		}
		return false;
	}

	public boolean isCoordenadorJuridico() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		int grupoTipoCodigo = Funcoes.StringToInt(getGrupoTipoCodigo());
		if (grupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL
				|| grupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL
				|| grupoCodigo == GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL
				|| grupoCodigo == GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA
				|| grupoCodigo == GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO
				|| grupoCodigo == GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA
				|| grupoTipoCodigo == GrupoTipoDt.COORDENADOR_PROCURADORIA
				|| grupoTipoCodigo == GrupoTipoDt.COORDENADOR_ESCRITORIO_JURIDICO
				|| grupoTipoCodigo == GrupoTipoDt.COORDENADOR_ADVOCACIA_PUBLICA
				|| grupoTipoCodigo == GrupoTipoDt.COORDENADOR_DEFENSORIA_PUBLICA)
			return true;
		return false;
	}

	public boolean isCoordenadorPromotoria() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupoCodigo == GrupoDt.COORDENADOR_PROMOTORIA)
			return true;
		return false;
	}

	public boolean isCoordenadorCentralMandado() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if(grupoCodigo == GrupoDt.COORDENADOR_CENTRAL_MANDADO)
			return true;
		return false;
	}

	public boolean isAssessorMagistrado() {

		int grupoCodigo = this.getGrupoCodigoToInt();
		return 	grupoCodigo == GrupoDt.ASSESSOR_DESEMBARGADOR				
				|| grupoCodigo == GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU
				|| grupoCodigo == GrupoDt.ASSESSOR_JUIZES_VARA
				|| grupoCodigo == GrupoDt.JUIZ_LEIGO
				|| grupoCodigo == GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU;
	}
	
	public boolean isAssistenteGabinete() {
		int grupoCodigo = this.getGrupoCodigoToInt();
		return grupoCodigo == GrupoDt.ASSISTENTE_GABINETE;				
	}
	
	public boolean isEstagiarioGabinete() {
		int grupoCodigo = this.getGrupoCodigoToInt();
		return grupoCodigo == GrupoDt.ESTAGIARIO_GABINETE;				
	}
	
	public boolean isGerenciamentoSegundoGrau() {
		int grupoCodigo = this.getGrupoCodigoToInt();
		return grupoCodigo == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU;				
	}

	public boolean isAdvogadoUsuarioChefe() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoUsuarioChefe());
		if (grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_ESTADUAL
				|| grupoCodigo == GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO
				|| grupoCodigo == GrupoDt.ADVOGADO_DEFENSOR_PUBLICO
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO
				|| grupoCodigo == GrupoDt.ADVOGADO_PUBLICO_UNIAO)
			return true;

		return false;
	}	

	public void setServentiaIdArea(String serventiaIdArea) {

		if (serventiaIdArea != null) {
			ServentiaIdArea = serventiaIdArea;
		}
	}

	public String getServentiaIdArea() {
		return ServentiaIdArea;
	}

	public boolean isServentiaTipo2Grau() {
		if (Funcoes.StringToInt(getServentiaTipoCodigo()) == ServentiaTipoDt.SEGUNDO_GRAU) {
			return true;
		}

		return false;
	}

	public boolean isGrupoCodigoDeAutoridade() {
		int grupoCodigo = Funcoes.StringToInt(getGrupoCodigo());
		if ((grupoCodigo == GrupoDt.JUIZES_VARA
				|| grupoCodigo == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU
				|| grupoCodigo == GrupoDt.JUIZES_TURMA_RECURSAL
				|| grupoCodigo == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL
				|| grupoCodigo == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL
				|| grupoCodigo == GrupoDt.JUIZ_EXECUCAO_PENAL
				|| grupoCodigo == GrupoDt.DESEMBARGADOR)) {
			return true;
		}

		return false;
	}

	public boolean isGrupoTipoCodigoDeAutoridade() {
		int grupoTipoCodigo = Funcoes.StringToInt(getGrupoTipoCodigo());

		if ((grupoTipoCodigo == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU
				|| grupoTipoCodigo == GrupoTipoDt.ASSISTENTE_GABINETE
				|| grupoTipoCodigo == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO
				|| grupoTipoCodigo == GrupoTipoDt.ASSESSOR_DESEMBARGADOR
				|| grupoTipoCodigo == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU)) {
			return true;
		}

		return false;
	}

	public void setId_ProcessoCodigoAcesso(String stId) {
		Id_ProcessoCodigoAcesso = stId;		
	}
	
	public boolean isValidoAcessoProcesso(String id){
		if(Id_ProcessoCodigoAcesso!=null){
			return Id_ProcessoCodigoAcesso.equals(id);
		} else {
			return false;
		}
	}
	
	public String getPropriedades(){
		
		return super.getPropriedades().replace("]", "") + enderecoUsuario.getPropriedades().replace("[", ";");		
	}

	public Map<String, Boolean> getMapPermissoes() {
		return mapPermissoes;
	}

	public void setMapPermissoes(Map<String, Boolean> mapPermissoes) {
		this.mapPermissoes = mapPermissoes;
	}

	public void copiar(UsuarioDt objeto){
		
		super.copiar(objeto);		
		
		EnderecoDt enderecoDt = new EnderecoDt();		
		enderecoDt.copiar(objeto.getEnderecoUsuario());
		this.setEnderecoUsuario(enderecoDt);
	}

	public boolean temListaUsuarioServentia() {
		if(listaUsuariosServentias != null && !listaUsuariosServentias.isEmpty() && listaUsuariosServentias.size() > 0){
			return true;
		}
		return false;
	}

	public boolean isAnalistaPreProcessual() {
		if(Funcoes.StringToInt(getGrupoCodigo(),-1) == GrupoDt.ANALISTA_PRE_PROCESSUAL){
			return true;
		}
		return false;
	}
	
	public String getDescricaoApresentacaoSelecaoServentiaGrupo() {
		String descricaoGrupo = "";
		
		if (isAssessorAdvogado() || isAssessorMP()) {
			descricaoGrupo = GrupoDt.getAtividadeUsuario(this.getGrupoCodigo());
		} else if (this.getGrupo() != null) {
			descricaoGrupo = this.getGrupo();
		}
		
		if (this.getUsuarioServentiaChefe() != null && this.getUsuarioServentiaChefe().length() > 0) {
			descricaoGrupo += " - "+ this.getUsuarioServentiaChefe();		
		}
		if (this.getCargoTipoCodigoUsuarioChefe() != null && this.getCargoTipoCodigoUsuarioChefe().length() > 0) {
			descricaoGrupo += " (" + this.getCargoTipoUsuarioChefe() + ")";
		}
			
		//Tratando caso de Relator, Presidente e seus assistentes para poder diferenciar o cargo
		if (this.getCargoTipo() != null &&
			this.getCargoTipo().length() > 0 &&
			(Funcoes.StringToInt(this.getCargoTipoCodigo()) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || 
			 Funcoes.StringToInt(this.getCargoTipoCodigo()) == CargoTipoDt.PRESIDENTE_TURMA)) {
				descricaoGrupo += " - " + this.getCargoTipo();
		} else if (this.getCargoTipoCodigoUsuarioChefe() != null &&
				   this.getCargoTipoCodigoUsuarioChefe().length() > 0 && 
				   (Funcoes.StringToInt(this.getCargoTipoCodigoUsuarioChefe()) == CargoTipoDt.RELATOR_TURMA_SEGUNDO_GRAU || 
				    Funcoes.StringToInt(this.getCargoTipoCodigoUsuarioChefe()) == CargoTipoDt.PRESIDENTE_TURMA)) {
			descricaoGrupo += " - " + this.getCargoTipoUsuarioChefe();
		}
		
		if (this.getServentiaCargo() != null && this.getServentiaCargo().trim().length() > 0) {
			descricaoGrupo += " - " + this.getServentiaCargo();
		}
		
		if (this.OabNumero != null && this.OabNumero.trim().length() > 0 && 
			this.OabComplemento != null && this.OabComplemento.trim().length() > 0 && 
			this.OabEstado != null && this.OabEstado.trim().length() > 0) {
			descricaoGrupo += " - OAB/Matrícula: " + this.OabNumero.trim() + "-" +  this.OabComplemento.trim() + "-" + this.OabEstado.trim();
		}
		
		return descricaoGrupo;
	}

	public boolean isUsuarioInterno() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		
		if (grupo == GrupoDt.CONSULTORES ||		
			//grupo == GrupoDt.CONSULTOR_SISTEMAS_EXTERNOS ||
			grupo == GrupoDt.JUIZ_EXECUCAO_PENAL ||
			grupo == GrupoDt.JUIZES_TURMA_RECURSAL ||
			grupo == GrupoDt.JUIZES_VARA ||
			grupo == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU ||
			grupo == GrupoDt.JUIZ_CORREGEDOR ||
			grupo == GrupoDt.JUIZ_AUXILIAR_PRESIDENCIA ||
			grupo == GrupoDt.DESEMBARGADOR ||	
			grupo == GrupoDt.PRESIDENTE_SEGUNDO_GRAU ||
			grupo == GrupoDt.PRESIDENTE_TJGO ||	
			grupo == GrupoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS ||		
			grupo == GrupoDt.TECNICO_EXECUCAO_PENAL ||
			grupo == GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL ||
			grupo == GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL ||	
			grupo == GrupoDt.PRESIDENCIA ||
			grupo == GrupoDt.ESTATISTICA ||	
			grupo == GrupoDt.ANALISTA_TI ||
			grupo == GrupoDt.GERENTE_TI ||	
			grupo == GrupoDt.ADMINISTRADORES ||
			grupo == GrupoDt.GERENCIAMENTO_TABELAS ||
			grupo == GrupoDt.ADMINISTRADOR_LOG ||
			grupo == GrupoDt.CADASTRADORES_TABELA ||
			grupo == GrupoDt.CADASTRADORES ||
			grupo == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU ||
			grupo == GrupoDt.CADASTRADOR_MASTER ||
			grupo == GrupoDt.CENTRAL_MANDADOS ||	
			grupo == GrupoDt.DISTRIBUIDOR_PRIMEIRO_GRAU ||
			grupo == GrupoDt.DISTRIBUIDOR_GABINETE ||
			grupo == GrupoDt.DISTRIBUIDOR_CRIMINAL ||
			grupo == GrupoDt.DISTRIBUIDOR_CAMARA ||	
			grupo == GrupoDt.ASSISTENTE_GABINETE ||
			grupo == GrupoDt.ASSISTENTE_GABINETE_FLUXO ||	
			grupo == GrupoDt.ASSESSOR_GABINETE_PRESIDENTE ||	
			grupo == GrupoDt.OUVIDOR_PRESIDENCIA ||	
			grupo == GrupoDt.CONTADORES_VARA ||	
			grupo == GrupoDt.CONCILIADORES_VARA ||
			grupo == GrupoDt.OFICIAL_JUSTICA ||	
			grupo == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL ||
			grupo == GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL ||
			grupo == GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL ||		
			grupo == GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU ||
			grupo == GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU ||
			grupo == GrupoDt.ASSESSOR_DESEMBARGADOR ||
			grupo == GrupoDt.ASSESSOR ||
			grupo == GrupoDt.ASSESSOR_JUIZES_VARA ||	
			grupo == GrupoDt.JUIZ_LEIGO ||	
			grupo == GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU ||	
			grupo == GrupoDt.ESTAGIARIO_SEGUNDO_GRAU ||	
			grupo == GrupoDt.ESTAGIARIO_GABINETE ||
			grupo == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL ||
			grupo == GrupoDt.ANALISTAS_EXECUCAO_PENAL ||
			grupo == GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL ||
			grupo == GrupoDt.ANALISTA_FORENSE ||
			grupo == GrupoDt.ANALISTA_FORENSE_2_GRAU ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_PRESIDENCIA ||
			grupo == GrupoDt.ANALISTA_PROCURADORIA ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL ||
			grupo == GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO ||
			grupo == GrupoDt.ANALISTA_COORDENARODIRA_JUDICIARIA ||
			grupo == GrupoDt.ANALISTA_FINANCEIRO ||
			grupo == GrupoDt.ANALISTA_PRE_PROCESSUAL ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL ||
			grupo == GrupoDt.ANALISTAS_CEJUSC ||		
			grupo == GrupoDt.COORDENADOR_CENTRAL_MANDADO  ||									
			grupo == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL  ||
			grupo == GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL  ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL  ||
			grupo == GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL	) {
			return true;
		}

		return false;		
	}
	
	public boolean isDesembargador() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (
			grupo == GrupoDt.DESEMBARGADOR ) {
			return true;
		}

		return false;
	}
	
	public boolean isDistribuidorGabinete() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (
			grupo == GrupoDt.DISTRIBUIDOR_GABINETE ) {
			return true;
		}
		return false;
	}
	
	public boolean isDistribuidorCamara() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (
			grupo == GrupoDt.DISTRIBUIDOR_CAMARA ) {
			return true;
		}
		return false;
	}
	
	public boolean isDistribuidor() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (
			grupo == GrupoDt.DISTRIBUIDOR_PRIMEIRO_GRAU ||
			grupo == GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU) {
			return true;
		}
		return false;
	}
	
	public boolean isPresidenteSegundoGrau() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if ( grupo == GrupoDt.PRESIDENTE_SEGUNDO_GRAU ) {
			return true;
		}

		return false;
	}

	public boolean isMesmaServentia(String id_serv) {
		if (id_serv!=null && id_serv.equals(getId_Serventia())){
			return true;
		}
		return false;
	}

	public boolean isAnalistaVara() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if ( 		
			grupo == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL ||
					grupo == GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL ||
					grupo == GrupoDt.ANALISTAS_EXECUCAO_PENAL ||
					grupo == GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL ||					
					grupo == GrupoDt.ANALISTA_JUDICIARIO_PRESIDENCIA ||					
					grupo == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL ||													
					grupo == GrupoDt.ANALISTA_PRE_PROCESSUAL  ){
			return true;
		}

		return false;
	}
	
	public boolean isAnalistaFinanceiro() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (grupo == GrupoDt.ANALISTA_FINANCEIRO){
			return true;
		}

		return false;
	}

	public boolean isAssessorDesembargador() {
		int grupoCodigo = this.getGrupoCodigoToInt();
		return 	grupoCodigo == GrupoDt.ASSESSOR_DESEMBARGADOR;				
		
	}

	public boolean isCriminal() {
		int subTipoCodigo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		if ( subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL ||
			 subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL_CRIMINAL ||
			 subTipoCodigo == ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL_FAZENDA_PUBLICA ||
			 subTipoCodigo == ServentiaSubtipoDt.VARA_CRIMINAL ||
			 subTipoCodigo == ServentiaSubtipoDt.UPJ_CRIMINAL ||
			 subTipoCodigo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA ||			 
			 subTipoCodigo == ServentiaSubtipoDt.UPJ_CUSTODIA ||
			 subTipoCodigo == ServentiaSubtipoDt.SECAO_CRIMINAL ||
			 subTipoCodigo == ServentiaSubtipoDt.CAMARA_CRIMINAL 					 
			 ){
			return true;
		}
		return false;
	}
	
	public boolean isAutoridadePolicial() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.AUTORIDADES_POLICIAIS){
			return true;
		}
		return false;
	}

	public boolean isAssessorMP() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.ASSESSOR_MP){
			return true;
		}
		return false;
	}

	public boolean isAssessor() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.ASSESSOR){
			return true;
		}
		return false;
	}
			
	public boolean isAssessorAdvogado() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.ASSESSOR_ADVOGADOS){
			return true;
		}
		return false;
	}

	public boolean isAssessoJuizVaraTurma() {
		int grupoTipo = Funcoes.StringToInt(getGrupoTipoCodigo());		
		if (grupoTipo == GrupoTipoDt.ASSESSOR_JUIZ_VARA_TURMA){
			return true;
		}
		return false;
	}
	
	public boolean isJuizTurma() {
		int grupoTipo = Funcoes.StringToInt(getGrupoTipoCodigo());		
		if (grupoTipo == GrupoTipoDt.JUIZ_TURMA){
			return true;
		}
		return false;
	}

	public boolean isAssessorPresidenteSegundoGrau() {
		int grupoTipo = Funcoes.StringToInt(getGrupoTipoCodigo());		
		if (grupoTipo == GrupoTipoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU){
			return true;
		}
		return false;				
	}

	public boolean isCoordenadorOab() {
		int grupo =getGrupoCodigoToInt();		
		if (grupo == GrupoDt.COORDENADOR_OAB){
			return true;
		}
		return false;			
	}
	
	
	public boolean isJuventudeInfracional() {
		if (ServentiaSubtipoCodigo != null && ServentiaSubtipoCodigo.equals(String.valueOf(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL))){
			return true;
		}
		return false;
	}
	
	public boolean isJuizLeigo() {
		int grupoTipo = Funcoes.StringToInt(getGrupoTipoCodigo());		
		if (grupoTipo == GrupoTipoDt.JUIZ_LEIGO){
			return true;
		}
		return false;				
	}
	public boolean isOficial() {
		int grupo =getGrupoCodigoToInt();		
		if (grupo == GrupoDt.OFICIAL_JUSTICA){
			return true;
		}
		return false;			
	}
	
	public boolean isValidadoGooogle() {
		return boReCaptcha ;
	}
	
	public void setGoogleOK() {
		boReCaptcha = true;
	}

	public boolean isInteligenciaN0() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.INTELIGENCIA_N0){
			return true;
		}
		return false;
	}
	
	public boolean isInteligenciaN1() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.INTELIGENCIA_N1){
			return true;
		}
		return false;
	}
	
	public boolean isInteligenciaN2() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.INTELIGENCIA_N2){
			return true;
		}
		return false;
	}
	
	public boolean isInteligenciaN3() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.INTELIGENCIA_N3){
			return true;
		}
		return false;
	}
	
	public boolean isInteligenciaN4() {
		int grupo = getGrupoCodigoToInt();
		if (grupo == GrupoDt.INTELIGENCIA_N4){
			return true;
		}
		return false;
	}
	
	public boolean isSalvo() {
		if (getId()!=null && !getId().isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean isGabinetePresidenciaTjgo() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return subtipo == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO;
	}

	public boolean isGabineteVicePresidenciaTjgo() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return subtipo == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO;
	}
	
	public boolean isGabineteUPJ() {		
		return ServentiaSubtipoDt.isGabineteFluxoUPJ(getServentiaSubtipoCodigo());
	}
	
	public boolean isServentiaPreprocessual() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return subtipo == ServentiaSubtipoDt.PREPROCESSUAL;
	}
	
	public boolean isServentiaNupemec() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return subtipo == ServentiaSubtipoDt.NUPEMEC;
	}

	public boolean isAssistenteGabineteComFluxo() {
		int grupoCodigo = this.getGrupoCodigoToInt();
		return grupoCodigo == GrupoDt.ASSISTENTE_GABINETE_FLUXO;	
	}

	public boolean isGabinete() {
		int subtipo = Funcoes.StringToInt(getServentiaTipoCodigo()); 
		return subtipo == ServentiaTipoDt.GABINETE;
	}

	public boolean isJuizUPJ() {
		int grupo = getGrupoCodigoToInt();
		return (grupo == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU || grupo == GrupoDt.MAGISTRADO_UPJ_SEGUNDO_GRAU);		
	}

	public boolean isServentiaUPJFamilia() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		if ( subtipo == ServentiaSubtipoDt.UPJ_FAMILIA ){
			return true;
		}
		return false;
	}
	
	public boolean isServentiaUPJSucessoes() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return subtipo == ServentiaSubtipoDt.UPJ_SUCESSOES;
	}
	
	public boolean isServentiaUPJCriminal() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return  (subtipo == ServentiaSubtipoDt.UPJ_CRIMINAL 
				|| subtipo == ServentiaSubtipoDt.UPJ_VIOLENCIA_DOMESTICA
				|| subtipo == ServentiaSubtipoDt.UPJ_CUSTODIA);
	}

	public boolean isServentiaUPJCustodia() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return  subtipo == ServentiaSubtipoDt.UPJ_CUSTODIA;
	}
	
	public boolean isServentiaUPJJuizadoEspecialFazendaPublica() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return  subtipo == ServentiaSubtipoDt.UPJ_JUIZADO_ESPECIAL_FAZENDA_PUBLICA;
	}
	
	public boolean isServentiaInfanciaJuventudeInfracional() {
		int subtipo = Funcoes.StringToInt(getServentiaSubtipoCodigo()); 
		return  subtipo == ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL;
	}
	
	public boolean isConciliador() {
		int grupo =getGrupoCodigoToInt();		
		if (grupo == GrupoDt.CONCILIADORES_VARA){
			return true;
		}
		return false;		
	}

	public void setId_ServentiaSubtipo(String id_ServentiaSubtipo) {
		Id_ServentiaSubtipo= id_ServentiaSubtipo;
	}

	public String getId_ServentiaSubtipo() {
		return Id_ServentiaSubtipo;
	}

	public boolean isCertificadoCarregado() {		
		return Certificado!=null;
	}

	public void setCertificado(CertificadoDt certDt) {
		Certificado=certDt;		
	}

	public boolean isSenhaCertificado() {
		return SenhaCertificado!=null && !SenhaCertificado.isEmpty();
	}

	public byte[] getCertificadoConteudo() {
		return Certificado.getConteudo();
	}

	public boolean isCoordenadorHabilitacaoMp() {
		int grupo = getGrupoCodigoToInt();
		return (grupo == GrupoDt.COORDENADOR_HABILITACAO_MP);		
	}

	public boolean isCoordenadorHabilitacaoSsp() {
		int grupo = getGrupoCodigoToInt();
		return (grupo == GrupoDt.COORDENADOR_HABILITACAO_SSP);		
	}
	
	public boolean isDistribuidor1Grau() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (
			grupo == GrupoDt.DISTRIBUIDOR_PRIMEIRO_GRAU ) {
			return true;
		}
		return false;
	}
	public boolean isDistribuidor2Grau() {
		int grupo = Funcoes.StringToInt(getGrupoCodigo());
		if (
			grupo == GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU ) {
			return true;
		}
		return false;
	}
	
	public boolean isQualquerAssessor() {
		int grupo = getGrupoCodigoToInt();
		if ((grupo == GrupoDt.ASSESSOR) ||
				(grupo == GrupoDt.ASSESSOR_ADVOGADOS) ||
				(grupo == GrupoDt.ASSESSOR_DESEMBARGADOR) ||
				(grupo == GrupoDt.ASSESSOR_GABINETE_PRESIDENTE) ||
				(grupo == GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU) ||
				(grupo == GrupoDt.ASSESSOR_JUIZES_VARA) ||
				(grupo == GrupoDt.ASSESSOR_MP) ||
				(grupo == GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU) ){
			return true;
		}
		return false;
	}
}

