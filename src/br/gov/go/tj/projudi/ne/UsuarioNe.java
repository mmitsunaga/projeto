package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import br.gov.go.tj.projudi.dt.AdvogadoDt;
import br.gov.go.tj.projudi.dt.AnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ArquivoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CertificadoDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoArquivoDt;
import br.gov.go.tj.projudi.dt.MovimentacaoProcessoDt;
import br.gov.go.tj.projudi.dt.PendenciaTipoDt;
import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.dt.PeticionamentoDt;
import br.gov.go.tj.projudi.dt.PreAnaliseConclusaoDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ProcessoParteDt;
import br.gov.go.tj.projudi.dt.ProcessoResponsavelDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioParteDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.dt.UsuarioUltimoLoginDt;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.projudi.ps.UsuarioPs;
import br.gov.go.tj.projudi.util.ProjudiPropriedades;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Certificado.PKCS12Parser;
import br.gov.go.tj.utils.Certificado.PKCS12ParsingException;
import br.gov.go.tj.utils.Certificado.Signer;
import br.gov.go.tj.utils.pdf.InterfaceSubReportJasper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import oracle.net.aso.a;
public class UsuarioNe extends UsuarioNeGen implements Serializable {

	private static final long serialVersionUID = -8260149271459221363L;
	public static final String SENHA_INVALIDA = "SENHA_INVALIDA_12345";
	// private UsuarioDt obDados;
	private int inPedido = -2147483648;
	private Set sePedidos = null;
	private String stVerificador;
	private String stVerificadorWebServiceMNI;

	// para manter compatibilidade com o projudi anterior
	//private List listaAtividadesPrincipais = null;

	// para manter compatibilidade com o projudi anterior

	private List listaServentiasGruposUsuario = null;
	private boolean boDuploLogin=false;
	private boolean LoginToken=false;

	public boolean getVerificaPermissao(int permissao) {
		if (obDados != null)
			return obDados.getPermissao(permissao);
		return false;
	}

	public String getId_Usuario() {
		if (obDados != null)
			return obDados.getId();
		return "";
	}
	
	public String getCpfUsuario() {
		if (obDados != null)
			return obDados.getCpf();
		return "";
	}

	public String getUsuario() {
		if (obDados != null)
			return obDados.getUsuario();
		return "";
	}

	public void setId_Grupo(String id_Grupo) {
		obDados.setId_Grupo(id_Grupo);
	}

	public UsuarioDt getUsuarioDt() {
		return obDados;
	}
	
	public void setUsuarioDt(UsuarioDt obDados) {
		this.obDados = obDados;
	}

	public String getCodigoHash(String codigo) throws Exception {
		String stTemp;
		stTemp = Funcoes.SenhaMd5(stVerificador + codigo);
		return stTemp;
	}

	public boolean VerificarCodigoHash(String codigo, String hash) {
		boolean boRetorno = false;
		try {
			String stTemp = Funcoes.SenhaMd5(stVerificador + codigo);
			if (stTemp.equals(hash))
				boRetorno = true;
		} catch (Exception e) {
			return false;
		}

		return boRetorno;
	}

	/**
	 * Método que verifica se o usuárioNe pode consultar áreas de distribuição
	 * que não
	 * estejam vinculadas à sua serventia atual.
	 * 
	 * @return true - pode , false = não pode
	 * @author hmgodinho
	 */
	public boolean podeConsultarOutrasAreasDistribuicao() {
		if (this.getUsuarioDt().getGrupoCodigo() != null
				&& (this.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ADMINISTRADORES))
				|| this.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ESTATISTICA))
				|| this.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.DISTRIBUIDOR_SEGUNDO_GRAU)))) {
			return true;
		}
		return false;
	}

	@Override
	public String Verificar(UsuarioDt dados) {
		return "";
	}

	/**
	 * Configura o hash para um usuario
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 10/11/2008
	 * @return void
	 */
	private void configurarHash() {
		// char[] stSorte
		// ={'Q','W','E','R','T','Y','U','I','O','P','A','S','D','F','G','H','J','K','L','Ç','Z','X','C','V','B','N','M','!','@','#','$','%','¨','&','*','(',')'};
		String stSorte = "QWERTYUIOPASDFGHJKLÇZXCVBNM!@#$%¨&*()";

		stVerificador = String.valueOf((new Date()).getTime());
		stVerificador += getId_Usuario();

		// 3 caracteres aleatorios
		Random rd = new Random();
		int l = stSorte.length() - 1;
		int n[] = { rd.nextInt(l), rd.nextInt(l), rd.nextInt(l) };

		// Adiciona numeros sorteados
		for (int i = 0; i < n.length; i++)
			stVerificador += stSorte.charAt(n[i]);
	}

	public UsuarioNe() {
		super();

		sePedidos = new HashSet();

//		// para manter compatibilidade com o projudi anterior
//		listaAtividadesPrincipais = new ArrayList();
//
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.ADMINISTRADORES));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.ADVOGADO_PARTICULAR));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.AUTORIDADES_POLICIAIS));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.CADASTRADORES));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.CONCILIADORES_VARA));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.CONSULTORES));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.CONTADORES_VARA));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.ASSESSOR_JUIZES_VARA));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.JUIZES_VARA));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL));
//		listaAtividadesPrincipais.add(new Integer(GrupoDt.MINISTERIO_PUBLICO));
//		// para manter compatibilidade com o projudi anterior
	}

	public long getPedido() {
		sePedidos.add(++inPedido);
		return inPedido;
	}

	public boolean verificarPedido(String pedido) {
		boolean boRetorno = false;
		if (pedido != null)
			if (!pedido.equalsIgnoreCase("null")) {
				int inTemp = Funcoes.StringToInt(pedido);
				if (sePedidos.contains(inTemp)) {
					sePedidos.remove(inTemp);
					boRetorno = true;
				}
			}
		return boRetorno;
	}

	/**
	 * Método genérico para verificar dados comuns de um usuário.
	 * 
	 * @author msapaula
	 * @throws Exception
	 */
	public String VerificarDadosUsuario(UsuarioDt dados) throws Exception {
		String stRetorno = "";
		if (dados.getNome().equalsIgnoreCase(""))
			stRetorno += "Nome é campo obrigatório. \n";

		/* Retirando obrigatoriedade de Usuario pois sempre será o CPF */
		// if (dados.getUsuario().equalsIgnoreCase("")) stRetorno +=
		// "Usuário é campo obrigatório. \n";
		if (dados.getUsuario() != null && dados.getUsuario().length() > 0) {
			UsuarioDt obj = this.consultarUsuarioLogin(dados.getUsuario());
			if (obj != null && !obj.getId().equals(dados.getId()))
				stRetorno += "Login já cadastrado. \n";
		}
		if (dados.getSexo().equalsIgnoreCase(""))
			stRetorno += "Sexo é campo obrigatório. \n";
		if (dados.getDataNascimento().equalsIgnoreCase(""))
			stRetorno += "Data Nascimento é campo obrigatório. \n";
		if (dados.getId_Naturalidade().equalsIgnoreCase(""))
			stRetorno += "Naturalidade é campo obrigatório. \n";
		if (dados.getRg().equalsIgnoreCase(""))
			stRetorno += "RG é campo obrigatório. \n";
		if (dados.getId_RgOrgaoExpedidor().equalsIgnoreCase(""))
			stRetorno += "Orgão Expedidor é campo obrigatório. \n";
		if (dados.getCpf().equalsIgnoreCase(""))
			stRetorno += "CPF é campo obrigatório. \n";
		else {
			if (Funcoes.testaCPFCNPJ(dados.getCpf())) {
				UsuarioDt obj = this.consultarUsuarioCpf(dados.getCpf());
				if (obj != null && !obj.getId().equals(dados.getId()))
					stRetorno += "CPF já cadastrado. \n";
			} else {
				stRetorno += "CPF Inválido. ";
			}
		}

		stRetorno += new EnderecoNe().Verificar(dados.getEnderecoUsuario());

		return stRetorno;
	}

	/**
	 * Verificar Campos Obrigatórios no cadastro de Advogado
	 * 
	 * @throws Exception
	 */
	public String VerificarAdvogado(UsuarioDt dados) throws Exception {
		String stRetorno = "";

		stRetorno += this.VerificarDadosUsuario(dados); // Verifica dados comuns
		// a qualquer usuário

		if (dados.getListaUsuarioServentias() == null || dados.getListaUsuarioServentias().size() == 0) {
			if (dados.getUsuarioServentiaOab().getOabNumero().equalsIgnoreCase(""))
				stRetorno += "Número da OAB é campo obrigatório. \n";
			if (dados.getUsuarioServentiaOab().getOabComplemento().equalsIgnoreCase(""))
				stRetorno += "Complemento da OAB é campo obrigatório. \n";
			if (dados.getId_Serventia().equalsIgnoreCase(""))
				stRetorno += "Selecione a OAB desejada. ";
		}
		return stRetorno;
	}

	/**
	 * Verificar Campos Obrigatórios no Cadastro de Servidor Judiciário
	 * 
	 * @author leandro
	 * @throws Exception
	 */
	public String VerificarServidorJudiciario(UsuarioDt dados) throws Exception {
		String stRetorno = "";

		// Chama método para verificar dados comuns
		stRetorno += this.VerificarDadosUsuario(dados);

		if (dados.getMatriculaTjGo().equalsIgnoreCase(""))
			stRetorno += "Matrícula é campo obrigatório. \n";

		if (dados.getListaUsuarioServentias() == null || dados.getListaUsuarioServentias().size() == 0) {
			if (dados.getId_Serventia().equalsIgnoreCase(""))
				stRetorno += "Selecione uma Serventia para Habilitação. \n";
			if (dados.getId_Grupo().equalsIgnoreCase(""))
				stRetorno += "Selecione o Grupo do usuário. \n";
		}

		if (dados.getDataNascimento() != null && dados.getDataNascimento().length() > 0 && !Funcoes.validaData(dados.getDataNascimento()))
			stRetorno += "Data de Nascimento inválida. \n";

		if (dados.getRgDataExpedicao() != null && dados.getRgDataExpedicao().length() > 0 && !Funcoes.validaData(dados.getRgDataExpedicao()))
			stRetorno += "Data de Expedição inválida. \n";

		return stRetorno;
	}

	/**
	 * Valida dados de um usuário que está sendo cadastrado para uma parte de
	 * processo
	 * 
	 * @param dados
	 * @author msapaula
	 */
	public String VerificarUsuarioParte(UsuarioDt dados) {

		String stRetorno = "";

		if (dados.getNome().equalsIgnoreCase(""))
			stRetorno += "Nome é campo obrigatório. ";
		if (dados.getUsuario().equalsIgnoreCase(""))
			stRetorno += "Usuário é campo obrigatório. ";
		if (dados.getSexo().equalsIgnoreCase(""))
			stRetorno += "Sexo é campo obrigatório. ";
		if (dados.getDataNascimento().equalsIgnoreCase(""))
			stRetorno += "Data Nascimento é campo obrigatório. ";
		if (dados.getId_Naturalidade().equalsIgnoreCase(""))
			stRetorno += "Naturalidade é campo obrigatório. ";
		if (dados.getRg().equalsIgnoreCase(""))
			stRetorno += "RG é campo obrigatório. ";
		if (dados.getId_RgOrgaoExpedidor().equalsIgnoreCase(""))
			stRetorno += "Orgão Expedidor é campo obrigatório. ";
		if (dados.getCpf().equalsIgnoreCase(""))
			stRetorno += "CPF é campo obrigatório. ";

		stRetorno += new EnderecoNe().Verificar(dados.getEnderecoUsuario());
		return stRetorno;

	}

	public String VerificarAssistenteServentia(UsuarioDt dados) throws Exception {
		String stRetorno = "";
		String nome = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			List listUsuarioDt = obPersistencia.consultarDescricaoAssistenteServentia(dados.getId(), dados.getId_UsuarioServentiaChefe(), dados.getId_Serventia());
			if (listUsuarioDt != null && listUsuarioDt.size() > 0) {
				nome = ((UsuarioDt) listUsuarioDt.get(0)).getNome();
				stRetorno += " O Usuário \"" + nome + "\"" + " já foi Habilitado na Serventia.";
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}

		// //System.out.println("..neUsuarioVerificar()");
		return stRetorno;
	}

	// Metodo criado para verificar se o assistente está ativo na serventia,
	// usado no cadastro do assessor - 14/10/2016 - Jelves
	public UsuarioDt VerificarAssistenteAtivoServentia(UsuarioDt dados) throws Exception {
		UsuarioDt stRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			List listUsuarioDt = obPersistencia.consultarDescricaoAssistenteServentia(dados.getId(), dados.getId_UsuarioServentiaChefe(), dados.getId_Serventia());
			if (listUsuarioDt != null && listUsuarioDt.size() > 0) {
				stRetorno = ((UsuarioDt) listUsuarioDt.get(0));
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	public String VerificarAssistente(UsuarioDt dados) throws Exception {
		String stRetorno = "";
		FabricaConexao obFabricaConexao = null;
		if (dados.getCpf().equalsIgnoreCase("")) {
			stRetorno += "CPF é campo obrigatório. ";
		} else {
			if (Funcoes.testaCPFCNPJ(dados.getCpf())) {
				try {
					obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
					UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
					UsuarioDt usuarioDt = obPersistencia.consultarUsuarioCpf(dados.getCpf());
					if (usuarioDt != null && usuarioDt.getId() != null && !usuarioDt.getId().equals("")) {
						stRetorno += " O Usuário \"" + usuarioDt.getNome() + "\"" + " já foi cadastrado.";
					}
				} finally {
					obFabricaConexao.fecharConexao();
				}
			} else {
				stRetorno += "CPF Inválido. ";
			}
		}

		if (dados.getNome().equalsIgnoreCase(""))
			stRetorno += "Nome é campo obrigatório. ";
		if (dados.getSexo().equalsIgnoreCase(""))
			stRetorno += "Sexo é campo obrigatório. ";
		if (dados.getDataNascimento().equalsIgnoreCase(""))
			stRetorno += "Data Nascimento é campo obrigatório. ";
		if (dados.getId_Naturalidade().equalsIgnoreCase(""))
			stRetorno += "Naturalidade é campo obrigatório. ";
		if (dados.getRg().equalsIgnoreCase(""))
			stRetorno += "RG é campo obrigatório. ";
		if (dados.getId_RgOrgaoExpedidor().equalsIgnoreCase(""))
			stRetorno += "Orgão Expedidor é campo obrigatório. ";
		stRetorno += new EnderecoNe().Verificar(dados.getEnderecoUsuario());

		return stRetorno;

	}
	
	public String VerificarAtualizacaoAssistente(UsuarioDt dados) throws Exception {
		String stRetorno = "";
		if (dados.getCpf().equalsIgnoreCase(""))
			stRetorno += "CPF é campo obrigatório. ";
		if (dados.getNome().equalsIgnoreCase(""))
			stRetorno += "Nome é campo obrigatório. ";
		if (dados.getSexo().equalsIgnoreCase(""))
			stRetorno += "Sexo é campo obrigatório. ";
		if (dados.getDataNascimento().equalsIgnoreCase(""))
			stRetorno += "Data Nascimento é campo obrigatório. ";
		if (dados.getId_Naturalidade().equalsIgnoreCase(""))
			stRetorno += "Naturalidade é campo obrigatório. ";
		if (dados.getRg().equalsIgnoreCase(""))
			stRetorno += "RG é campo obrigatório. ";
		if (dados.getId_RgOrgaoExpedidor().equalsIgnoreCase(""))
			stRetorno += "Orgão Expedidor é campo obrigatório. ";
		stRetorno += new EnderecoNe().Verificar(dados.getEnderecoUsuario());

		return stRetorno;

	}

//	public List getTiposPrincipaisFuncoes() {
//		return listaAtividadesPrincipais;
//	}

	public List getAdministradores() throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			lista = obPersistencia.getAdministradores();

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return lista;
	}

	/**
	 * Consulta todos usuários cadastrados no sistema de acordo com os filtros
	 * passados
	 * 
	 * @param grupo
	 *            , grupo a ser filtrado
	 * @param descricao
	 *            , nome do usuário a ser pesquisado
	 */
	public List consultarUsuarios(String grupo, String serventia, String descricao) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			lista = obPersistencia.consultarUsuarios(grupo, serventia, descricao);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return lista;
	}

	public void AtualizarPermissoes() throws Exception {
		FabricaConexao obFabricaConexao = null;
		try {
			if (!obDados.getId().equalsIgnoreCase("")) {
				// //System.out.println("..neAtualizarPermissoes 2");
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
				obDados.setPermissoes(obPersistencia.ConsultaUsuarioPermissoes(obDados.getId_UsuarioServentia(), obDados.getId_Grupo()));
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável em organizar o menu de um usuário, vinculando pais e
	 * filhos de uma lista
	 * recebida.
	 * 
	 * @author jrcorrea
	 */

	private String listaMenus(List lista) {
		String stTemp = "";
		List liPermissaoPai = new ArrayList();
		// guardo todos os objeto que não tem pais, ou seja, os menus de
		// primeiro nivel
		for (int i = 0; i < lista.size(); i++) {
			PermissaoDt tempPermissaoDt = (PermissaoDt) lista.get(i);
			if (tempPermissaoDt.getId_PermissaoPai().length() == 0) {
				liPermissaoPai.add(tempPermissaoDt);
			} else {
				// Atribuo os filhos aos pais
				for (int k = 0; k < lista.size(); k++) {
					PermissaoDt tempPermissaoDt2 = (PermissaoDt) lista.get(k);
					if (tempPermissaoDt2.getId().equals(tempPermissaoDt.getId_PermissaoPai()))
						tempPermissaoDt2.incluirPermissao(tempPermissaoDt);
				}
			}
		}
		// pego os menus dos pais, e eles pegam os dos filhos
		for (int i = 0; i < liPermissaoPai.size(); i++)
			stTemp += ((PermissaoDt) liPermissaoPai.get(i)).getListaMenu();

		return stTemp;
	}

	/**
	 * Obtém a lista de permissões pais vinculados aos filhos
	 * 
	 * @author gschiquini
	 * @param lista
	 * @return
	 */
	private List listaMenusPJD(List lista) {
		List liPermissaoPai = new ArrayList(), listaPermissaoConsultar = new ArrayList();
		// guardo todos os objeto que não tem pais, ou seja, os menus de
		// primeiro nivel
		
		List<String> liConsultarAux = new ArrayList<String>();
		Boolean temMenuProcessoConsultar = false;
		
		for (int i = 0; i < lista.size(); i++) {
			PermissaoDt tempPermissaoDt = (PermissaoDt) lista.get(i);

			if (tempPermissaoDt.getPermissao().equals("1º Grau")) {
				PermissaoDt primeiroGrauCivel = new PermissaoDt();
				primeiroGrauCivel.setEMenu("true");
				primeiroGrauCivel.setPermissao("Cível");
				primeiroGrauCivel.setId_PermissaoPai(tempPermissaoDt.getId());
				primeiroGrauCivel.setId_UsuarioLog(tempPermissaoDt.getId_UsuarioLog());
				primeiroGrauCivel.setIpComputadorLog(tempPermissaoDt.getIpComputadorLog());
				primeiroGrauCivel.setIrPara("userMainFrame");
				primeiroGrauCivel.setOrdenacao("-1");
				primeiroGrauCivel.setLink("ProcessoCivelSemAssistencia?PaginaAtual=4");
				lista.add(1, primeiroGrauCivel);
				primeiroGrauCivel = new PermissaoDt();
				primeiroGrauCivel.setEMenu("true");
				primeiroGrauCivel.setPermissao("Criminal");
				primeiroGrauCivel.setId_PermissaoPai(tempPermissaoDt.getId());
				primeiroGrauCivel.setId_UsuarioLog(tempPermissaoDt.getId_UsuarioLog());
				primeiroGrauCivel.setIpComputadorLog(tempPermissaoDt.getIpComputadorLog());
				primeiroGrauCivel.setIrPara("userMainFrame");
				primeiroGrauCivel.setOrdenacao("-1");
				primeiroGrauCivel.setLink("ProcessoCriminal?PaginaAtual=4");
				lista.add(2, primeiroGrauCivel);
			}
			
			if(tempPermissaoDt.getPermissao().equals("Processo Status")) {
				tempPermissaoDt.setPermissao("Situação do Processo");
			}

			if (tempPermissaoDt.getId_PermissaoPai().length() == 0) {
				liPermissaoPai.add(tempPermissaoDt);
			} else {
				// Atribuo os filhos aos pais
				for (int k = 0; k < lista.size(); k++) {
					if (tempPermissaoDt.getPermissao().equals("Trocar Serventia") || tempPermissaoDt.getPermissao().equals("Físico Cível")) {
						continue;
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Cíveis Com Assistência"))) {
						obDados.getMapPermissoes().put("civeisComAssistencia", true);
						this.procuraPermissoesFilhas(tempPermissaoDt.getId(), "civeisComAssistencia", lista);
						continue;
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Cíveis Sem Assistência")) || Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Cíveis Com Custas"))) {
						obDados.getMapPermissoes().put("civeisSemAssistencia", true);
						this.procuraPermissoesFilhas(tempPermissaoDt.getId(), "civeisSemAssistencia", lista);
						
						if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissaoPai()).equals(Funcoes.RemoveAcentos("2º Grau")))
							obDados.getMapPermissoes().put("segundoGrauCiveisSemAssistencia", true);
						continue;
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Criminais Com Assistência"))) {
						this.procuraPermissoesFilhas(tempPermissaoDt.getId(), "criminaisComAssistencia", lista);
						continue;
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Criminais Sem Assistência"))) {
						continue;
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Criar Com Vínculo"))) {
						obDados.getMapPermissoes().put("criarComVinculo", true);
						this.procuraPermissoesFilhas(tempPermissaoDt.getId(), "criarComVinculo", lista);
						continue;
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Cíveis"))) {
						obDados.getMapPermissoes().put("segundoGrauCiveisComAssistencia", true);
						this.procuraPermissoesFilhas(tempPermissaoDt.getId(), "segundoGrauCiveisComAssistencia", lista);
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Criminais"))) {
						this.procuraPermissoesFilhas(tempPermissaoDt.getId(), "segundoGrauCriminaisComAssistencia", lista);
					}
					if (Funcoes.RemoveAcentos(tempPermissaoDt.getPermissao()).equals(Funcoes.RemoveAcentos("Criar Sem Vínculo"))) {
						continue;
					}

					PermissaoDt tempPermissaoDt2 = (PermissaoDt) lista.get(k);
					if (tempPermissaoDt2.getId().equals(tempPermissaoDt.getId_PermissaoPai())) {
						// Retirar a opção consultar do menu principal e inclui
						// na pesquisaProcesso
						if (tempPermissaoDt2.getPermissao().equals("Consultar")) {
							liConsultarAux.add(tempPermissaoDt.getPermissao());
							listaPermissaoConsultar.add(tempPermissaoDt);
							continue;
						}
						// Mantém apenas 1 menu Consultar no Menu Principal Processos e inclui
						// na pesquisaProcesso
						if (tempPermissaoDt2.getPermissao().equals("Processos") && tempPermissaoDt.getPermissao().startsWith("Consultar")){
							if (!tempPermissaoDt.getLink().equals("#")){
								liConsultarAux.add(tempPermissaoDt.getPermissao());
								listaPermissaoConsultar.add(tempPermissaoDt);
							};
							if (temMenuProcessoConsultar){
								continue;
							}
							temMenuProcessoConsultar = true;														
						}
						tempPermissaoDt2.incluirPermissao(tempPermissaoDt);
					}
				}
				
				
			}
		}
		// Map de teclas de atalho
		HashMap<String, String> accesskey = new HashMap<>();
		accesskey.put("Processos", "r");
		accesskey.put("Audiências", "u");
		accesskey.put("Cadastros", "c");
		accesskey.put("Estatísticas", "t");
		accesskey.put("Página Inicial", "h");

		for (int i = 0; i < liPermissaoPai.size(); i++) {

			// Coloca o menu Cadastros como primeiro da lista
			if (((PermissaoDt) liPermissaoPai.get(i)).getPermissao().equals("Cadastros")) {
				PermissaoDt perm = (PermissaoDt) liPermissaoPai.get(i);
				liPermissaoPai.remove(liPermissaoPai.get(i));
				liPermissaoPai.add(1, perm);
			}

			// A pedido da karla o box cumprimentos e conclusões deixam de
			// existir e voltam a aparecer no menu principal

			// if (((PermissaoDt)
			// liPermissaoPai.get(i)).getPermissao().equals("Conclusões") ||
			// ((PermissaoDt)
			// liPermissaoPai.get(i)).getPermissao().equals("Cumprimentos")) {
			//
			// if (((PermissaoDt)
			// liPermissaoPai.get(i)).getPermissao().equals("Conclusões")) {
			// // Suprime o menu conclusoes
			// UsuarioDt.liConclusoes = ((PermissaoDt)
			// liPermissaoPai.get(i)).getListaMenuBox();
			// liPermissaoPai.remove(i);
			// }
			// if (((PermissaoDt)
			// liPermissaoPai.get(i)).getPermissao().equals("Cumprimentos")) {
			// // Suprime o menu cumprimentos
			// UsuarioDt.liCumprimentos = ((PermissaoDt)
			// liPermissaoPai.get(i)).getListaMenuBox();
			// liPermissaoPai.remove(i);
			// }
			// break;
			// } else {
			for (int j = 0; j < liPermissaoPai.size(); j++) {
				PermissaoDt permissao = (PermissaoDt) liPermissaoPai.get(j);
				if (permissao.getPermissao().equals("Processos")) {
					if (permissao.getVeFilhos() != null) {
						for (PermissaoDt p1 : permissao.getVeFilhos()) {
							if (p1.getPermissao().equals("2º Grau") && p1.getVeFilhos() != null) {
								for (PermissaoDt p2 : p1.getVeFilhos()) {
									if (p2.getPermissao().equals("Cíveis")) {
										obDados.getMapPermissoes().put("segundoGrauCiveisComAssistencia", true);
										p2.setLink("ProcessoSegundoGrauCivel?PaginaAtual=4&dependente=true','conteudoLista");
									} else if (p2.getPermissao().equals("Criminais")) {
										p2.setLink("ProcessoSegundoGrauCriminal?PaginaAtual=4");
									}
								}

							}
							
							if (p1.getPermissao().equals("Consultar") && !p1.getLink().matches("BuscaProcesso?.*")) {
								for (Object a : listaPermissaoConsultar) {
									PermissaoDt pTemp = (PermissaoDt) a;
									if (pTemp.getPermissao().equals("Todos")) {
										p1.setLink(pTemp.getLink());
										break;
									}
									p1.setLink(pTemp.getLink());
								}
								// p1.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=2','conteudo");
								if (p1.getVeFilhos() != null) {
									for (PermissaoDt p2 : p1.getVeFilhos())
										if (p2.getPermissao().equals("Todos")) {
											p2.setLink("BuscaProcessoUsuarioExterno?PaginaAtual=2");
											break;
										}
								}
								break;
							}
						}
						for (PermissaoDt p1 : permissao.getVeFilhos()) {
							if(Funcoes.RemoveAcentos(p1.getPermissao()).equals(Funcoes.RemoveAcentos("Cadastrar Processos"))) {
								PermissaoDt primeiro = new PermissaoDt(), segundo = new PermissaoDt();
								PermissaoDt filho = new PermissaoDt();
								obDados.getMapPermissoes().put("civeisComAssistencia", true);
								obDados.getMapPermissoes().put("segundoGrauCiveisComAssistencia", true);
								obDados.getMapPermissoes().put("segundoGrauCiveisSemAssistencia", true);
								
								
								primeiro.setPermissao("1º Grau");
								filho.setPermissao("Cível");
								filho.setLink("ProcessoCivelSemAssistencia?PaginaAtual=4");
								primeiro.incluirPermissao(filho);
								filho = new PermissaoDt();
								filho.setPermissao("Criminal");
								filho.setLink("ProcessoCriminal?PaginaAtual=4");
								primeiro.incluirPermissao(filho);
								filho = new PermissaoDt();
								filho.setPermissao("Cível");
								filho.setLink("ProcessoSegundoGrauCivel?PaginaAtual=4&amp;dependente=true");
								segundo.setPermissao("2º Grau");
								segundo.incluirPermissao(filho);
								filho = new PermissaoDt();
								filho.setPermissao("Criminal");
								filho.setLink("ProcessoSegundoGrauCriminal?PaginaAtual=4");
								segundo.incluirPermissao(filho);
								PermissaoDt[] listaTemp = permissao.getVeFilhos();
								permissao.incluirPermissao(primeiro);
								permissao.incluirPermissao(segundo);
								PermissaoDt pai = new PermissaoDt();
								permissao.incluirPermissao(primeiro);
								pai.setPermissao(permissao.getPermissao());
								pai.setLink(permissao.getLink());
								pai.incluirPermissao(primeiro);
								pai.incluirPermissao(segundo);
								
								
								primeiro = new PermissaoDt();
								primeiro.setPermissao("Turmas Julgadoras");
								filho = new PermissaoDt();
								filho.setPermissao("Cível");
								filho.setLink("ProcessoSegundoGrauCivel?PaginaAtual=4&amp;&amp;turmaJulgadora=true");
								primeiro.incluirPermissao(filho);
								filho = new PermissaoDt();
								filho.setPermissao("Criminal");
								filho.setLink("ProcessoSegundoGrauCriminal?PaginaAtual=4&amp;turmaJulgadora=true");
								primeiro.incluirPermissao(filho);
								permissao.incluirPermissao(primeiro);
								pai.incluirPermissao(primeiro);
								
								for (int k = 1; k < listaTemp.length; k++) {
									pai.incluirPermissao(listaTemp[k]);
								}
								permissao = null;
								p1 = null;
								liPermissaoPai.set(j, pai);
								break;
							}
						}
					}
				}

			}
			for (int j = 0; j < liPermissaoPai.size(); j++) {
				PermissaoDt permissao = (PermissaoDt) liPermissaoPai.get(j), permFilho = permissao;
				while (permFilho.getVeFilhos() != null && permFilho.getVeFilhos().length > 0){
					permFilho = permFilho.getVeFilhos()[0];

				}
				permissao.setLink(permFilho.getLink());
				permissao.setPermissaoEspecial(accesskey.get(permissao.getPermissao()));
				liPermissaoPai.set(j, permissao);
			}
			// }
		}
		
		//Excluí opção buscar Todos Usuário Externo, caso tenha permissao para o BuscaProcesso
		for (int i = 0; i < liConsultarAux.size(); i++) {
			if (liConsultarAux.get(i).equals("Consultar Todos")){
				for (int j = 0; j < liConsultarAux.size(); j++) {
					if (liConsultarAux.get(j).equals("Todos")){
						liConsultarAux.remove(j);
						break;
					}
				}
			break;
			}
		}

		obDados.setLiConsultar(liConsultarAux);
		return liPermissaoPai;
	}
	
	private void procuraPermissoesFilhas(String idPermissao, String nomePermPai ,List lista){
		Iterator it = lista.iterator();
		while(it.hasNext()){
			PermissaoDt permFilho = (PermissaoDt) it.next();
			if(permFilho.getId_PermissaoPai().equals(idPermissao)){
				obDados.getMapPermissoes().put(nomePermPai+permFilho.getPermissao(), true);
			}
			
		}
	}

	/**
	 * Consulta todas as permissões e menus vinculados ao usuario. Obtém todos
	 * os menus e sub-menus
	 * em uma consulta e chama o método que irá vincular pais e filhos.
	 * 
	 * @author jrcorrea
	 */
	public String getMenu() throws Exception {
		List tempList = null;
		PermissaoNe obPermissaoNe = new PermissaoNe();
		FabricaConexao obFabricaConexao = null;
		try {
			if (obDados != null && obDados.getId().length() > 0) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
				obDados.setPermissoes(obPersistencia.ConsultaUsuarioPermissoes(obDados.getId_UsuarioServentia(), obDados.getId_Grupo()));
				tempList = obPermissaoNe.getMenu(obDados.getId_UsuarioServentia(), obDados.getId_Grupo());
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaMenus(tempList);
	}

	/**
	 * Consulta todas as permissões e menus vinculados ao usuario_public, pelo
	 * seu grupo
	 * em uma consulta e chama o método que irá vincular pais e filhos.
	 * 
	 * @author jrcorrea
	 *         22/07/2016
	 */
	public String getPermissoesGrupo() throws Exception {
		List tempList = null;
		PermissaoNe obPermissaoNe = new PermissaoNe();
		FabricaConexao obFabricaConexao = null;
		try {
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			if (obDados != null && obDados.getId_Grupo().length() > 0) {
				UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
				obDados.setPermissoes(obPersistencia.consultarPermissoesGrupo(obDados.getId_Grupo()));
				tempList = obPermissaoNe.getMenu(obDados.getId_UsuarioServentia(), obDados.getId_Grupo());
				this.configurarHash();
				this.configurarHashWebServiceMNI();
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return listaMenus(tempList);
	}

	/**
	 * Método que efetua Consulta de Menu Especial de acordo com o tipo passado.
	 * Ao efetuar uma
	 * consulta, o código e a string do Menu são armazenados no
	 * mapPermissaoEspecial, o qual é
	 * verificado antes de efetuar uma nova consulta
	 * 
	 * @param permissaoEspecialCodigo
	 *            código do menu a ser retornado
	 * @return String do menu
	 * @author jrcorrea
	 */
	public String getMenuEspecial(int permissaoEspecialCodigo) throws Exception {
		String stRetorno = "";
		PermissaoNe obPermissaoNe = new PermissaoNe();

		if (!obDados.getId().equalsIgnoreCase("")) {

			if (!obDados.getMapPermissaoEspecial().containsKey(permissaoEspecialCodigo) ) {
				stRetorno = listaMenus(obPermissaoNe.getMenuEspecial(obDados.getId_UsuarioServentia(), obDados.getId_Grupo(), permissaoEspecialCodigo));
				obDados.addPermissaoEspecial(permissaoEspecialCodigo, stRetorno);
			} else
				stRetorno = (String) obDados.getMapPermissaoEspecial().get(permissaoEspecialCodigo);
		}

		return stRetorno;
	}

	/**
	 * Método que verifica se login e senha passados estão cadastrados no
	 * sistema
	 * 
	 * @param usuario
	 *            , login digitado
	 * @param senha
	 *            , senha digitada
	 * @return
	 */
	public boolean logarUsuarioSenha(String usuario, String senha) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obDados = obPersistencia.logarUsuarioSenha(usuario, senha);
			if (!obDados.getId().equalsIgnoreCase("")) {
				boRetorno = true;
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Método que verifica se login e senha passados estão cadastrados no
	 * sistema, não deve ser utilizado no login
	 * 
	 * @param usuario
	 *            , login digitado
	 * @param senha
	 *            , senha digitada
	 * @return
	 *         jrcorrea
	 *         25/08/2016
	 */
	public boolean verificarUsuarioSenha(String usuario, String senha) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obDados = obPersistencia.verificarUsuarioSenha(usuario, senha);
			if (!obDados.getId().equalsIgnoreCase("")) {
				boRetorno = true;
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Método que verifica se login passado por certificado está cadastrado no
	 * sistema
	 * 
	 * @param usuario
	 *            , login digitado
	 * @param senha
	 *            , senha digitada
	 * @return
	 */
	public boolean consultaUsuarioCertificado(String cpf) throws Exception {
		if (cpf == null)
			return false;
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obDados = obPersistencia.consultarUsuarioCpf(cpf);
			if (obDados != null && !obDados.getId().equalsIgnoreCase("")) {
				boRetorno = true;
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Método utilizado para atualizar o usuário logado através dos webservices.
	 * 
	 * @param usuarioDt
	 * @throws Exception
	 */
	public void atualizaUsuarioLogado(UsuarioDt usuarioDt) throws Exception
	{
		if (!usuarioDt.getId().equalsIgnoreCase("")) {
			this.obDados = usuarioDt;
			this.listaServentiasGruposUsuario = null;
			this.consultarServentiasGrupos();
		}
	}

	/**
	 * Consultar serventias vinculadas a um usuário e devolver uma lista de
	 * objetos
	 * UsuarioServentiaDt Chama UsuarioServentiaNe para realizar essa consulta
	 */
	@SuppressWarnings("unchecked")
	public List consultarServentiasUsuario(String usuario) throws Exception {
		List listaServentias = null;

		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		listaServentias = usuarioServentiaNe.consultarServentiasUsuario(usuario);

		return listaServentias;
	}

	/**
	 * Consultar serventias e grupos vinculados a um usuário e devolver uma
	 * lista com esses dados.
	 * Chama UsuarioServentiaGrupoNe para realizar essa consulta
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List consultarServentiasGrupos() throws Exception {

		if (listaServentiasGruposUsuario == null) {
			UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
			listaServentiasGruposUsuario = usuarioServentiaGrupoNe.consultarServentiasGrupos(getId_Usuario());
		}

		return listaServentiasGruposUsuario;
	}
	
	public List consultarServentiasGrupos(FabricaConexao obFabricaConexao) throws Exception {

		if (listaServentiasGruposUsuario == null) {
			UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
			listaServentiasGruposUsuario = usuarioServentiaGrupoNe.consultarServentiasGrupos(getId_Usuario(), obFabricaConexao);
		}

		return listaServentiasGruposUsuario;
	}
	
	public List consultarServentiasGrupos(String id_usuarioServentia, 
										  String grupoCodigo,
										  String id_serventiaCargo,
									      String id_serventiaCargoUsuarioChefe,
									      String id_usuarioServentiaChefe) throws Exception {

		if (listaServentiasGruposUsuario == null) {
			UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
			listaServentiasGruposUsuario = usuarioServentiaGrupoNe.consultarServentiasGrupos(getId_Usuario(),
																							id_usuarioServentia,
																							grupoCodigo,
																							id_serventiaCargo,
																							id_serventiaCargoUsuarioChefe,
																							id_usuarioServentiaChefe);
		}

		return listaServentiasGruposUsuario;
	}

	/**
	 * Consulta para obter registro de UsuarioServentia conforme código do
	 * usuário e da serventia
	 * passados. Utiliza UsuarioServentiaNe para realizar consulta.
	 */
 	public UsuarioServentiaDt buscaUsuarioServentiaId(String id_Usuario, String id_Serventia) throws Exception {
       UsuarioServentiaDt dtRetorno = null;

	   UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
       dtRetorno = usuarioServentiaNe.buscaUsuarioServentiaId(id_Usuario, id_Serventia);

       return dtRetorno;
    }

	/**
	 * Consulta para obter registro de UsuarioServentia conforme código do
	 * usuário e da serventia
	 * passados. Utiliza UsuarioServentiaNe para realizar consulta.
	 */
	public UsuarioServentiaDt buscaUsuarioServentiaId(String id_Usuario, String id_Serventia, String id_UsuarioServentiaChefe) throws Exception {
		UsuarioServentiaDt dtRetorno = null;

		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		dtRetorno = usuarioServentiaNe.buscaUsuarioServentiaId(id_Usuario, id_Serventia, id_UsuarioServentiaChefe);

		return dtRetorno;
	}

	/**
	 * Consultar serventias no caso de Corregedoria filtrando as que sejam do
	 * tipo Vara e Turma
	 * Recursal. Utiliza ServentiaNe para realizar consulta.
	 */
	@SuppressWarnings("unchecked")
	public List consultarServentiasVaraTurma() throws Exception {
		List liTemp = null;

		ServentiaNe serventiaNe = new ServentiaNe();
		liTemp = serventiaNe.consultarServentiasVaraTurma();

		return liTemp;
	}

	/**
	 * Método que obtem o UsuarioDt para o login passado
	 * 
	 * @param loginUsuario
	 *            : login do usuário que deseja consultar
	 */
	public UsuarioDt consultarUsuarioLogin(String loginUsuario) throws Exception {
		UsuarioDt usuarioDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			usuarioDt = obPersistencia.consultarUsuarioLogin(loginUsuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return usuarioDt;
	}
	
	public UsuarioDt consultarUsuarioLogin(String loginUsuario, FabricaConexao obFabricaConexao ) throws Exception {
		UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarUsuarioLogin(loginUsuario);
	}

	/**
	 * Método que obtem o UsuarioDt para o CPF passado
	 * 
	 * @param loginUsuario
	 *            : login do usuário que deseja consultar
	 */
	public UsuarioDt consultarUsuarioCpf(String cpfUsuario) throws Exception {
		UsuarioDt usuarioDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			usuarioDt = obPersistencia.consultarUsuarioCpf(cpfUsuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return usuarioDt;
	}

	/**
	 * Método para consultar um advogado de acordo com a oab e tipo da serventia
	 * passada
	 * 
	 * @param oabNumero
	 *            , número da OAB
	 * @param oabComplemento
	 *            , complemento da OAB
	 * @param oabEstado
	 *            , estado da OAB
	 * @param advogadoTipo
	 *            , define o tipo do advogado a ser consultado
	 */
	public List consultarAdvogadoOab(String oabNumero, String complementoOab, String estadoOab, String advogadoTipo) throws Exception {
		List liTemp = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(advogadoTipo)) {
			case AdvogadoDt.ADVOGADO_PARTICULAR:
				liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab, ServentiaTipoDt.ORDEM_ADVOGADOS_BRASIL);
				break;
			case AdvogadoDt.DEFENSOR_PUBLICO:
				liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab, ServentiaTipoDt.DEFENSORIA_PUBLICA);
				break;
			case AdvogadoDt.PROCURADOR_MUNICIPAL:
				liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab, ServentiaTipoDt.PROCURADORIA_GERAL_MUNICIPAL);
				break;
			case AdvogadoDt.PROCURADOR_ESTADUAL:
				liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab, ServentiaTipoDt.PROCURADORIA_GERAL_ESTADO);
				break;
			case AdvogadoDt.PROCURADOR_UNIAO:
				liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab, ServentiaTipoDt.PROCURADORIA_UNIAO);
				break;
			case AdvogadoDt.ADVOGADO_PUBLICO:
				liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab, ServentiaTipoDt.ADVOGADO_PUBLICO_AUTARQUIAS);
				break;
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Método para consultar um advogado de acordo com a oab e tipo da serventia
	 * passada
	 * 
	 * @param oabNumero
	 *            , número da OAB
	 * @param oabComplemento
	 *            , complemento da OAB
	 * @param oabEstado
	 *            , estado da OAB
	 * @param advogadoTipo
	 *            , define o tipo do advogado a ser consultado
	 */
	public List consultarAdvogadoOab(String oabNumero, String complementoOab, String estadoOab) throws Exception {
		List liTemp = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			liTemp = obPersistencia.consultarAdvogadoOab(oabNumero, complementoOab, estadoOab);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Método para consultar um advogado de acordo com a oab e tipo da serventia
	 * passada
	 * 
	 * @param id_Serv
	 *            , identificador da serventia
	 */
	public List consultarPromotorSubstitutoProcessual(String id_Serv) throws Exception {
		List liTemp = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarPromotorSubstitutoProcessual(id_Serv);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	public List getListaServentiasGruposUsuario() {
		return listaServentiasGruposUsuario;
	}

	public void limparListaServentiasGruposUsuario() {
		listaServentiasGruposUsuario = null;
	}

	public void salvar(UsuarioDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;

		UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
		if (dados.getId().equalsIgnoreCase("")) {
			// senha padrão
			if (dados.getSenha().equalsIgnoreCase(""))
				dados.setSenha("12345");
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
		} else {
			obPersistencia.alterar(dados);
			obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);

	}

	public void salvar(UsuarioDt dados) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			salvar(dados, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	public String verificarAlteracaoSenha(UsuarioDt usuarioDt, String senhaatual, String novasenha, String novasenhacomparacao, boolean validarSenhaAtual) throws Exception {
		String stRetorno = "";
		if (usuarioDt.getId().length() == 0)
			stRetorno += "Nenhum Usuário foi selecionado. \n";
		if(validarSenhaAtual) {
			if (senhaatual.length() == 0)
				stRetorno += "Digite a Senha Atual. \n";
			else if (!usuarioDt.getSenha().equals(Funcoes.SenhaMd5(senhaatual))) {
				stRetorno += "Senha Atual incorreta. \n";
			}
		}
		
		if (!novasenha.equals(novasenhacomparacao))
			stRetorno += "A nova senha deve ser igual a senha de comparação. \n";
		
		stRetorno += Funcoes.errosSenha(novasenha);
		
		return stRetorno;
	}

	/**
	 * Método responsável em alterar a senha de um usuário
	 */
	public void alterarSenha(UsuarioDt dados, String senhaAtual, String senhaNova) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", "");
			obPersistencia.alterarSenha(dados.getId(), senhaAtual, senhaNova);

			obLog.salvar(obLogDt, obFabricaConexao);
			dados.setSenha(Funcoes.SenhaMd5(senhaNova));
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método responsável em alterar a senha de um usuário quando ele utiliza a recuperação de senha.
	 * ATENÇÃO: Não exige a senha atual portanto este método deve ser utilizado apenas pela recuperação de senha
	 * com validação do link enviado pro e-mail do usuário. Esta funcionalidade será utilizada para quando o
	 * usuário esquecer a sua senha.
	 * 
	 * @author hrrosa
	 */
	public void alterarSenhaSemVerificarAtual(UsuarioDt dados, String senhaNova) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", "");
			obPersistencia.alterarSenhaSemVerificarAtual(dados.getId(), senhaNova);

			obLog.salvar(obLogDt, obFabricaConexao);
			dados.setSenha(Funcoes.SenhaMd5(senhaNova));
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva Log de Acesso assim que um usuário loga no sistema e seleciona uma
	 * serventia e grupo para trabalhar
	 * 
	 * @param propriedades
	 * @throws Exception
	 */
	public void salvarLogAcesso(String propriedades) throws Exception {

		LogDt obLogDt = new LogDt("Usuario", getId_Usuario(), getId_Usuario(), getIpComputadorLog(), String.valueOf(LogTipoDt.Acesso), propriedades, "");
		new LogNe().salvar(obLogDt);

	}

	public String getIpComputadorLog() {
		if (obDados != null)
			return obDados.getIpComputadorLog();
		return "";
	}

	/**
	 * Consultar os dados completos de um usuário para possibilitar alteração
	 * dos mesmos
	 * 
	 * @param id_usuario
	 *            , identificação do usuário
	 * 
	 * @author msapaula
	 */
	public UsuarioDt consultarUsuarioCompleto(String id_usuario) throws Exception {

		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarUsuarioCompleto(id_usuario);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consultar os dados completos de um usuário para possibilitar alteração
	 * dos mesmos
	 * 
	 * @param id_usuario
	 *            , identificação do usuário
	 * 
	 * @author msapaula
	 */
	public UsuarioDt consultarUsuarioCompletoPJD(String id_usuario) throws Exception {

		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarUsuarioCompletoPJD(id_usuario);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Método responsável em limpar a senha de um usuário
	 */
	public void limparSenha(UsuarioDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		if (dados.getId().length() == 0)
			throw new MensagemException("Escolha um Usuário para limpar a senha.");

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Usuario", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.LimparSenha), "", "");
			obPersistencia.limparSenha(dados.getId());

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Ativa um Usuário
	 * 
	 * @param usuarioDt
	 *            , objeto com dados para ativação
	 * @author msapaula
	 *         alteração jrcorrea 24/08/2016 ativa somente o usuário
	 *         a ativação do serventia cargo e usuario serventia serão no
	 *         usuario serventia grupo
	 */
	public void ativarUsuario(UsuarioDt usuarioDt) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Usuario:" + usuarioDt.getId() + ";Ativo:" + UsuarioDt.INATIVO + "]";
			String valorNovo = "[Id_Usuario:" + usuarioDt.getId() + ";Ativo:" + UsuarioDt.ATIVO + "]";

			LogDt obLogDt = new LogDt("Usuario", usuarioDt.getId(), usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
			obPersistencia.alterarStatusUsuario(usuarioDt.getId(), UsuarioDt.ATIVO);

			obLog.salvar(obLogDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	/**
	 * Ativa um Usuário
	 * 
	 * @param usuarioDt
	 *            , objeto com dados para ativação
	 * @author msapaula
	 *         alteração jrcorrea 24/08/2016 ativa somente o usuário
	 *         a ativação do serventia cargo e usuario serventia serão no
	 *         usuario serventia grupo
	 * 
	 *         Alteração hrrosa 17/11/2016. Replicando o método acima e
	 *         alterando para receber a conexão como
	 *         parâmetro. Utilizar para ativar o usuário na mesma transação de
	 *         outro método.
	 */
	public void ativarUsuario(UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {

		UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

		String valorAtual = "[Id_Usuario:" + usuarioDt.getId() + ";Ativo:" + UsuarioDt.INATIVO + "]";
		String valorNovo = "[Id_Usuario:" + usuarioDt.getId() + ";Ativo:" + UsuarioDt.ATIVO + "]";

		LogDt obLogDt = new LogDt("Usuario", usuarioDt.getId(), usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.HabilitacaoUsuario), valorAtual, valorNovo);
		obPersistencia.alterarStatusUsuario(usuarioDt.getId(), UsuarioDt.ATIVO);

		obLog.salvar(obLogDt, obFabricaConexao);

	}

	/**
	 * Desativa um usuário
	 * 
	 * @param usuarioDt
	 *            , objeto do usuário que será desativado
	 * 
	 * @author msapaula
	 */
	public void desativarUsuario(UsuarioDt usuarioDt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_Usuario:" + usuarioDt.getId() + ";Ativo:" + UsuarioDt.ATIVO + "]";
			String valorNovo = "[Id_Usuario:" + usuarioDt.getId() + ";Ativo:" + UsuarioDt.INATIVO + "]";

			LogDt obLogDt = new LogDt("Usuario", usuarioDt.getId(), usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog(), String.valueOf(LogTipoDt.DesabilitacaoUsuario), valorAtual, valorNovo);
			// Desativa usuário
			obPersistencia.alterarStatusUsuario(usuarioDt.getId(), UsuarioDt.INATIVO);

			// Desativa todos UsuarioServentia
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			usuarioServentiaNe.desativarUsuarioServentias(usuarioDt.getId(), obFabricaConexao);

			// Desativa todos UsuarioServentiaGrupo
			UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
			usuarioServentiaGrupoNe.desativarUsuarioServentiasGrupos(usuarioDt.getId(), obFabricaConexao);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Chama método para Desativar um UsuarioServentia
	 */
	public void desativarUsuarioServentia(UsuarioDt usuarioDt) throws Exception {
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
		usuarioServentiaDt.setId(usuarioDt.getId_UsuarioServentia());
		usuarioServentiaDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
		usuarioServentiaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		usuarioServentiaNe.desativarUsuarioServentia(usuarioServentiaDt);
	}

	/**
	 * Chama método para consultar
	 */
	public String consultarIdUsuarioServentiaGrupo(String idGrupo, String idServentia, String idUsuario) throws Exception {
		String stRetorno = "";

		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		stRetorno = usuarioServentiaGrupoNe.consultarIdUsuarioServentiaGrupo(idGrupo, idServentia, idUsuario);

		return stRetorno;
	}

	/**
	 * Chama método para Desativar um UsuarioServentiaGrupo
	 */
	public void desativarUsuarioServentiaGrupo(UsuarioDt usuarioDt) throws Exception {
		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
		usuarioServentiaGrupoDt.setId(usuarioDt.getId_UsuarioServentiaGrupo());
		usuarioServentiaGrupoDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
		usuarioServentiaGrupoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());
		usuarioServentiaGrupoNe.desativarUsuarioServentiaGrupo(usuarioServentiaGrupoDt);
	}

	/**
	 * Chama método para Ativar um UsuarioServentia
	 */
	public void ativarAdvogado(UsuarioDt usuarioDt) throws Exception {
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
		usuarioServentiaDt.setId(usuarioDt.getId_UsuarioServentia());
		usuarioServentiaDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
		usuarioServentiaDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

		usuarioServentiaNe.ativarAdvogado(usuarioServentiaDt);
	}

	/**
	 * Método para controlar se o cadastro poderá ser efetuado, para evitar que
	 * um cadastrador crie novos usuários de qualquer perfil.
	 * 
	 * @param usuarioDt
	 *            - usuário que está sendo cadastrado
	 * @param usuarioSessao
	 *            - usuário da sessão que está realizando o cadastro
	 * @return mensagem de erro caso necessário
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String podeHabilitarUsuarioPerfilEspecifico(UsuarioDt usuarioDt, UsuarioNe usuarioSessao) throws Exception {
		UsuarioServentiaGrupoNe usgNe = new UsuarioServentiaGrupoNe();
		UsuarioServentiaGrupoDt usgDt = usgNe.consultarId(usuarioDt.getId_UsuarioServentiaGrupo());
		// Valida o cadastro para usuário ADMINISTRADOR - somente outro
		// administrador poderá efetuar o cadastro
		if (usgDt.getGrupoCodigo().equals(String.valueOf(GrupoDt.ADMINISTRADORES))) {
			if (!usuarioSessao.getUsuarioDt().getGrupoCodigo().equals(String.valueOf(GrupoDt.ADMINISTRADORES)))
				return "Cadastro não efetuado. Somente ADMINISTRADORES podem cadastrar/habilitar usuários para o perfil ADMINISTRADOR.";
		}

		return "";
	}

	/**
	 * Chama método para Ativar um UsuarioServentiaGrupo
	 */
	public void ativarUsuarioServentiaGrupo(UsuarioDt usuarioDt) throws Exception {

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try {

			obFabricaConexao.iniciarTransacao();

			UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
			UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
			usuarioServentiaGrupoDt.setId(usuarioDt.getId_UsuarioServentiaGrupo());
			usuarioServentiaGrupoDt.setId_UsuarioLog(usuarioDt.getId_UsuarioLog());
			usuarioServentiaGrupoDt.setIpComputadorLog(usuarioDt.getIpComputadorLog());

			// Ativa o usuário na serventia
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			usuarioServentiaNe.ativarUsuarioServentia(usuarioDt.getId_UsuarioServentiaGrupo(), obFabricaConexao);

			usuarioServentiaGrupoNe.ativarUsuarioServentiaGrupo(usuarioServentiaGrupoDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Consultar os dados completos de um usuário, inclusive endereço
	 * 
	 * @param id_usuario
	 *            , identificação do usuário
	 */
	public UsuarioDt consultarAdvogadoId(String id_usuario) throws Exception {

		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarAdvogadoId(id_usuario);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta um Advogado baseado no Id_UsuarioServentia passado
	 * 
	 * @param id_UsuarioServentia
	 */
	public UsuarioDt consultarAdvogadoServentiaId(String id_UsuarioServentia) throws Exception {
		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarAdvogadoServentiaId(id_UsuarioServentia);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta um Usuário baseado no Id_UsuarioServentia passado
	 * 
	 * @author vfosantos
	 * @param id_UsuarioServentia
	 */
	public UsuarioDt consultarUsuarioServentiaIdParaEnvioDeEmail(String id_UsuarioServentia) throws Exception {
		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarUsuarioServentiaIdParaEnvioDeEmail(id_UsuarioServentia);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta os dados do Desembargador baseado no Id da Serventia informado.
	 * 
	 * @param idServentia
	 *            - ID da Serventia
	 * @return Id do usuário desembargador
	 */
	public String consultarIdServentiaCargoDesembargadorServentia(String idServentia) throws Exception {
		String idServentiaCargoDesembargador = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			idServentiaCargoDesembargador = obPersistencia.consultarIdServentiaCargoDesembargadorServentia(idServentia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return idServentiaCargoDesembargador;
	}

	/**
	 * Consulta Advogados Ativos ou Inativos
	 * 
	 * @param nome
	 *            , filtro para nome do usuário
	 * @param usuario
	 *            , filtro para login do usuário
	 * @param posicao
	 *            , parametro para paginação
	 */
	public List consultarDescricaoAdvogado(String nome, String usuario, String oab, String rg, String cpf, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoAdvogado(nome, usuario, oab, rg, cpf, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta Habilitações de um Advogado em Serventias, e suas respectivas
	 * OAB's
	 * 
	 * @param id_Usuario
	 *            , identificação do Advogado
	 */
	public List consultarServentiaOabAdvogado(String id_Usuario) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiaOabAdvogado(id_Usuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta Habilitações de um Advogado em Serventias, e suas respectivas
	 * OAB's
	 * 
	 * @param id_Usuario
	 *            , identificação do Advogado
	 */
	public List consultarServentiaOabAdvogadoHabilitacao(String id_Usuario, String id_Serventia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiaOabAdvogadoHabilitacao(id_Usuario, id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Método responsável em salvar ou atualizar os dados da Serventia e OAB
	 * de um Advogado
	 * 
	 * @param advogadodt
	 *            , objeto com dados do advogado
	 */
	public void salvarUsuarioServentiaOab(UsuarioDt advogadodt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogDt logDt = new LogDt(advogadodt.getId_UsuarioLog(), advogadodt.getIpComputadorLog());
			salvarUsuarioServentiaOab(advogadodt, logDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	private void salvarUsuarioServentiaOab(UsuarioDt advogadodt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		try {

			// Salva UsuarioServentia
			usuarioServentiaDt = new UsuarioServentiaDt();
			usuarioServentiaDt.setId(advogadodt.getId_UsuarioServentia());
			usuarioServentiaDt.setId_Serventia(advogadodt.getId_Serventia());
			usuarioServentiaDt.setId_Usuario(advogadodt.getId());
			
			if (advogadodt.getUsuarioServentiaOab().isInativo()){
				usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.INATIVO));
			}else{
				usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
			}
			
			usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
			usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
			new UsuarioServentiaNe().salvar(usuarioServentiaDt, obFabricaConexao);

			// Salva UsuarioServentiaGrupo
			if (advogadodt.getId_UsuarioServentiaGrupo().trim().length() > 0){
				usuarioServentiaGrupoDt = usuarioServentiaGrupoNe.consultarId(advogadodt.getId_UsuarioServentiaGrupo());
			}

			if (usuarioServentiaGrupoDt == null){
				usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
				advogadodt.setId_UsuarioServentia(usuarioServentiaDt.getId());
			}

			ServentiaDt serventiDt = new ServentiaNe().consultarId(advogadodt.getId_Serventia(), obFabricaConexao);

			if (serventiDt == null || serventiDt.getServentiaTipoCodigo().length() == 0) {
				throw new MensagemException("Verifique a serventia do Usuário, não foi possivel pegar o seu tipo código");
			}

			int serventiaTipo = Funcoes.StringToInt(serventiDt.getServentiaTipoCodigo());

			String id_Grupo = new GrupoNe().consultarId_GrupoAdvogadoServentiaTipo(serventiaTipo, obFabricaConexao);

			advogadodt.setId_Grupo(id_Grupo);
			usuarioServentiaGrupoNe.salvarUsuarioServentiaGrupo(usuarioServentiaGrupoDt, advogadodt, logDt, obFabricaConexao);

			// salva UsuarioServentiaOab
			usuarioServentiaOabDt = new UsuarioServentiaOabDt();
			new UsuarioServentiaOabNe().salvarUsuarioServentiaOab(usuarioServentiaOabDt, advogadodt, logDt, obFabricaConexao);

			new ProcessoParteAdvogadoNe().vinculeAdvogadosPartesProcessoImportadosSPG(usuarioServentiaOabDt.getId_UsuarioServentia(), usuarioServentiaOabDt.getOabNumero(), serventiDt.getEstadoRepresentacao(), usuarioServentiaOabDt.getOabComplemento(), advogadodt.getNome(), advogadodt.getCpf(), obFabricaConexao);

		} catch (Exception e) {
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");
			usuarioServentiaOabDt.setId("");
			throw e;
		}
	}


	/**
	 * Salva um usuário do tipo Advogado (insere ou altera dados). Internamente
	 * realiza tratamento
	 * para salvar Endereço, UsuarioServentia, UsuarioServentiaGrupo e
	 * UsuarioServentiaOab. Quando
	 * se tratar de alteração deve verificar se é necessário revogar o
	 * certificado do usuário.
	 * 
	 * @author leandro, msapaula
	 */
	public String salvarAdvogado(UsuarioDt advogadodt ) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogDt logDt = new LogDt(advogadodt.getId_UsuarioLog(), advogadodt.getIpComputadorLog());
			String retorno = salvarAdvogado(advogadodt, logDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			return retorno;
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método usado para o cadastro automático de advogados por certificado digital ICP-BR
	 * @param advogadodt
	 * @return
	 * @throws Exception
	 */
	public String salvarAdvogadoComCertificado(UsuarioDt advogadodt) throws Exception {
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogDt logDt = new LogDt(advogadodt.getId_UsuarioLog(), advogadodt.getIpComputadorLog());
			advogadodt.setAtivo(String.valueOf(UsuarioDt.ATIVO));	
			String retorno = salvarAdvogado(advogadodt, logDt, obFabricaConexao);
			for (Object o : advogadodt.getListaUsuarioServentias()) {
				UsuarioServentiaOabDt s = (UsuarioServentiaOabDt) o;
				if (s.getId() == null || s.getId().isEmpty()) {
					advogadodt.setUsuarioServentiaOab(s);
					advogadodt.setId_Serventia(s.getIdServentia());
					advogadodt.setId_UsuarioServentia("");
					salvarUsuarioServentiaOab(advogadodt, logDt, obFabricaConexao);
				} else {
					UsuarioServentiaNe ne = new UsuarioServentiaNe();
					UsuarioServentiaDt usu_serv = ne.consultarId(s.getId_UsuarioServentia(), obFabricaConexao);
					usu_serv.setId_UsuarioLog(advogadodt.getId_UsuarioLog());
					if (s.isInativo() && usu_serv.isAtivo())
						ne.desativarUsuarioServentia(usu_serv, obFabricaConexao);
					if (!s.isInativo() && !usu_serv.isAtivo())
						ne.ativarUsuarioServentia(usu_serv, obFabricaConexao);
				}
			}
			obFabricaConexao.finalizarTransacao();
			return retorno;
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	private String salvarAdvogado(UsuarioDt advogadodt, LogDt logDt, FabricaConexao obFabricaConexao) throws Exception {
		String stRetorno = "";
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		try {
			// Setando como Usuario o CPF cadastrado
			advogadodt.setUsuario(advogadodt.getCpf());

			// Se Advogado não possui Id salva o Usuario, UsuarioServentia,
			// UsuarioServentiaGrupo e UsuarioServentiaGrupoOAB
			if (advogadodt.getId().equalsIgnoreCase("")) {
				// Salva endereço
				String id_Endereco = new EnderecoNe().salvar(advogadodt.getEnderecoUsuario(), logDt, obFabricaConexao);

				// Salva Usuário

				advogadodt.setAtivo(String.valueOf(UsuarioDt.ATIVO));
				advogadodt.setId_Endereco(id_Endereco);
				salvar(advogadodt, obFabricaConexao);
				
				// Salva UsuarioServentia
		 	       
				usuarioServentiaDt = new UsuarioServentiaDt();
			   	usuarioServentiaDt.setId(advogadodt.getId_UsuarioServentia());
				usuarioServentiaDt.setId_Serventia(advogadodt.getId_Serventia());
				usuarioServentiaDt.setId_Usuario(advogadodt.getId());
				usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
				usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
				usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
				usuarioServentiaNe.salvar(usuarioServentiaDt, obFabricaConexao);					
				if (advogadodt.getUsuarioServentiaOab().isInativo()) {
					new UsuarioServentiaNe().desativarUsuarioServentia(usuarioServentiaDt, obFabricaConexao);
				}
				advogadodt.setId_UsuarioServentia(usuarioServentiaDt.getId());	

				// Salva UsuarioServentiaGrupo
				
				usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
				ServentiaNe serventiaNe = new ServentiaNe();
				String id_Grupo = "";

				ServentiaDt serventiDt = new ServentiaNe().consultarId(advogadodt.getId_Serventia(), obFabricaConexao);

				if (serventiDt == null || serventiDt.getServentiaTipoCodigo().length() == 0) {
					throw new MensagemException("Verifique a serventia do Usuário, não foi possivel pegar o seu tipo código");
				}

				int serventiaTipo = Funcoes.StringToInt(serventiDt.getServentiaTipoCodigo());

				id_Grupo = new GrupoNe().consultarId_GrupoAdvogadoServentiaTipo(serventiaTipo, obFabricaConexao);

				advogadodt.setId_Grupo(id_Grupo);

				new UsuarioServentiaGrupoNe().salvarUsuarioServentiaGrupo(usuarioServentiaGrupoDt, advogadodt, logDt, obFabricaConexao);

				// salva UsuarioServentiaOab
				usuarioServentiaOabDt = new UsuarioServentiaOabDt();
				new UsuarioServentiaOabNe().salvarUsuarioServentiaOab(usuarioServentiaOabDt, advogadodt, logDt, obFabricaConexao);
				new ProcessoParteAdvogadoNe().vinculeAdvogadosPartesProcessoImportadosSPG(usuarioServentiaOabDt.getId_UsuarioServentia(), usuarioServentiaOabDt.getOabNumero(), serventiDt.getEstadoRepresentacao(), usuarioServentiaOabDt.getOabComplemento(), advogadodt.getNome(), advogadodt.getCpf(), obFabricaConexao);
			} else {
				// Se algum dado pessoal foi modificado deve revogar o
				// certificado
				if ((!advogadodt.getNome().equalsIgnoreCase(obDados.getNome()) || !advogadodt.getCpf().equalsIgnoreCase(obDados.getCpf())
						|| !advogadodt.getRg().equalsIgnoreCase(obDados.getRg()) || !advogadodt.getRgOrgaoExpedidor().equalsIgnoreCase(obDados.getRgOrgaoExpedidor())
						|| !advogadodt.getRgOrgaoExpedidorUf().equalsIgnoreCase(obDados.getRgOrgaoExpedidorUf()) || !advogadodt.getDataNascimento().equals(obDados.getDataNascimento()))) {
					CertificadoNe certificadoNe = new CertificadoNe();
					boolean boRetorno = certificadoNe.revogarCertificadoValido(advogadodt.getId(), advogadodt.getId_UsuarioLog(), advogadodt.getIpComputadorLog(), obFabricaConexao);
					if (boRetorno)
						stRetorno += " Certificado do usuário foi revogado.";
				}

				
				// Atualiza endereço e dados do advogado
				EnderecoNe enderecoNe = new EnderecoNe();
				EnderecoDt enderecoDt = advogadodt.getEnderecoUsuario();
				enderecoDt.setId(advogadodt.getId_Endereco());
				enderecoNe.salvar(enderecoDt, logDt, obFabricaConexao);
				//
				advogadodt.setId_Endereco(enderecoDt.getId());
				//
				salvar(advogadodt, obFabricaConexao);				
				obDados.copiar(advogadodt);
			}			
		} catch (Exception e) {
			advogadodt.setId("");
			if (advogadodt.getEnderecoUsuario() != null)
				advogadodt.getEnderecoUsuario().setId("");
			if (usuarioServentiaDt != null)
				usuarioServentiaDt.setId("");
			if (usuarioServentiaGrupoDt != null)
				usuarioServentiaGrupoDt.setId("");
			if (usuarioServentiaOabDt != null)
				usuarioServentiaOabDt.setId("");
			throw e;
		}
		return stRetorno;
	}


	/**
	 * Salva um Servidor Judiciário (insere ou altera dados), ou seja, qualquer
	 * usuário que não seja
	 * advogado. Internamente realiza tratamento para salvar Endereço,
	 * UsuarioServentia e
	 * UsuarioServentiaGrupo.
	 * Se dados como Nome, Rg, Cpf e Data de Nascimento tiverem sido alterados,
	 * o certificado do usuário deve ser revogado.
	 * 
	 * @author leandro, msapaula
	 */
	public String salvarServidorJudiciario(UsuarioDt servidorJudiciarioDt) throws Exception {
		
		String stRetorno = "";
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		FabricaConexao obFabricaConexao = null;
		UsuarioServentiaNe  usuarioServentiaNe = new UsuarioServentiaNe();
		
		try {
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(servidorJudiciarioDt.getId_UsuarioLog(), servidorJudiciarioDt.getIpComputadorLog());

			// Setando como Usuario o CPF cadastrado
			servidorJudiciarioDt.setUsuario(servidorJudiciarioDt.getCpf());

			// Se não possuir Id Salva o Usuário, UsuarioServentia e
			// UsuarioServentiaGrupo
			if (servidorJudiciarioDt.getId().equalsIgnoreCase("")) {
				// Salva endereço
				String id_Endereco = new EnderecoNe().salvar(servidorJudiciarioDt.getEnderecoUsuario(), logDt, obFabricaConexao);

				// Salva Usuário
				
				servidorJudiciarioDt.setAtivo(String.valueOf(UsuarioDt.ATIVO));
				servidorJudiciarioDt.setId_Endereco(id_Endereco);
				salvar(servidorJudiciarioDt, obFabricaConexao);

				// Salva UsuarioServentia
				
				usuarioServentiaDt = new UsuarioServentiaDt();
			   	usuarioServentiaDt.setId(servidorJudiciarioDt.getId_UsuarioServentia());
				usuarioServentiaDt.setId_Serventia(servidorJudiciarioDt.getId_Serventia());
				usuarioServentiaDt.setId_Usuario(servidorJudiciarioDt.getId());
				usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
				usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
				usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
				usuarioServentiaNe.salvar(usuarioServentiaDt, obFabricaConexao);					
				servidorJudiciarioDt.setId_UsuarioServentia(usuarioServentiaDt.getId());

				// Salva UsuarioServentiaGrupo
				
				usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
				new UsuarioServentiaGrupoNe().salvarUsuarioServentiaGrupo(usuarioServentiaGrupoDt, servidorJudiciarioDt, logDt, obFabricaConexao);

			} else {
				// Verifica se dados pessoais foram alterados para revogar
				// certificado
				if ((!servidorJudiciarioDt.getNome().equalsIgnoreCase(obDados.getNome()) || !servidorJudiciarioDt.getCpf().equalsIgnoreCase(obDados.getCpf())
						|| !servidorJudiciarioDt.getRg().equalsIgnoreCase(obDados.getRg())
						|| !servidorJudiciarioDt.getRgOrgaoExpedidor().equalsIgnoreCase(obDados.getRgOrgaoExpedidor())
						|| !servidorJudiciarioDt.getRgOrgaoExpedidorUf().equalsIgnoreCase(obDados.getRgOrgaoExpedidorUf()) || !servidorJudiciarioDt
						.getDataNascimento().equals(obDados.getDataNascimento()))) {
					CertificadoNe certificadoNe = new CertificadoNe();
					boolean boRetorno = certificadoNe.revogarCertificadoValido(servidorJudiciarioDt.getId(), servidorJudiciarioDt.getId_UsuarioLog(), servidorJudiciarioDt.getIpComputadorLog(),
							obFabricaConexao);
					if (boRetorno)
						stRetorno += " Certificado do usuário foi revogado.";
				}

				// Atualiza endereço e dados do advogado
				EnderecoNe enderecoNe = new EnderecoNe();
				EnderecoDt enderecoDt = servidorJudiciarioDt.getEnderecoUsuario();
				enderecoDt.setId(servidorJudiciarioDt.getId_Endereco());
				enderecoNe.salvar(enderecoDt, logDt, obFabricaConexao);
				//
				servidorJudiciarioDt.setId_Endereco(enderecoDt.getId());
				//
				salvar(servidorJudiciarioDt, obFabricaConexao);
				obDados.copiar(servidorJudiciarioDt);
			}
			
			obFabricaConexao.finalizarTransacao();
			
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			servidorJudiciarioDt.setId("");
			servidorJudiciarioDt.getEnderecoUsuario().setId("");
			if (usuarioServentiaDt != null)
			{
				usuarioServentiaDt.setId("");
				usuarioServentiaGrupoDt.setId("");
			}
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Consulta um Usuário de acordo com id passado
	 * 
	 * @param id_usuario
	 */
	public UsuarioDt consultarServidorJudiciarioId(String id_usuario) throws Exception {

		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServidorJudiciarioId(id_usuario);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta um Usuário de acordo com Id_UsuarioServentiaGrupo passado
	 * 
	 * @param id_UsuarioServentiaGrupo
	 *            , identificação do usuário na serventia e grupo
	 */
	public UsuarioDt consultarUsuarioServentiaGrupoId(String id_UsuarioServentiaGrupo) throws Exception {

		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarUsuarioServentiaGrupoId(id_UsuarioServentiaGrupo);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Consulta Servidor Judicários Ativos ou Inativos
	 * 
	 * @param nome
	 *            , filtro para nome do usuário
	 * @param usuario
	 *            , filtro para login do usuário
	 * @param posicao
	 *            , parametro para paginação
	 */
	public List consultarDescricaoServidorJudiciario(String nome, String usuario, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoServidorJudiciario(nome, usuario, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta dados de um Servidor Judiciário, e seus respectivos grupos e
	 * serventias onde está
	 * habilitado. Retorna ATIVOS ou INATIVOS
	 * 
	 * @param id_Usuario
	 *            , identificação do usuário para o qual serão consultados
	 *            serventias e grupos
	 * 
	 */
	public List consultarServentiasGruposUsuario(String id_Usuario) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarServentiasGruposUsuario(id_Usuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * CONSULTA SERVIDOR JUDICIARIO (UsuarioServentia) ATIVOS OU INATIVOS POR ID
	 * USUARIO
	 */
	public List consultarDescricaoServidorJudiciario(String id_Serventia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoServidorJudiciario(id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public List consultarDescricaoAssistenteServentia(String id_Usuario, String id_UsuarioServentia, String id_Serventia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoAssistenteServentia(id_Usuario, id_UsuarioServentia, id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public List consultarDescricaoAdvogadosProcuradoresServentia(String id_Serventia) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoAdvogadosProcuradoresServentia(id_Serventia);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta todos os usuários cadastrados no sistema
	 *
	 * @param nome
	 *            , filtro para nome do usuário
	 * @param usuario
	 *            , filtro para usuário
	 * @param posicao
	 *            , parâmetro para paginação
	 * 
	 * @author msapaula, lsbernardes
	 */
	public List consultarTodosUsuarios(String nome, String usuario, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarTodosUsuarios(nome, usuario, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	/**
	 * Consulta todos os promotores cadastrados no sistema
	 *
	 * @param nome
	 *            , filtro para nome do usuário
	 * @param usuario
	 *            , filtro para usuário
	 * @param posicao
	 *            , parâmetro para paginação
	 * 
	 * @author msapaula, lsbernardes
	 */
	public List consultarTodosPromotores(String nome, String usuario, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarTodosPromotores(nome, usuario, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public String consultarTodosPromotoresJSON(String nome, String usuario, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarTodosPromotoresJSON(nome, usuario, posicao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Consulta todos os advogados cadastrados no sistema
	 *
	 * @param nome
	 *            , filtro para nome do usuário
	 * @param usuario
	 *            , filtro para usuário
	 * @param posicao
	 *            , parâmetro para paginação
	 * 
	 * @author msapaula, lsbernardes
	 */
	public List consultarTodosAdvogados(String nome, String usuario, String id_Serventia, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarTodosAdvogados(nome, usuario, id_Serventia, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public String consultarTodosAdvogadosJSON(String nome, String usuario, String id_Serventia, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarTodosAdvogadosJSON(nome, usuario, id_Serventia, posicao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Consulta todos os usuários cadastrados no sistema
	 *
	 * @param nome
	 *            , filtro para nome do usuário
	 * @param posicao
	 *            , parâmetro para paginação
	 * 
	 */
	public List consultarTodosUsuarios(String nome, String posicao) throws Exception {
		return this.consultarTodosUsuarios(nome, null, posicao);
	}

	// CONSULTA ASSISTENTE (Usuario) POR ID USUARIO
	public UsuarioDt consultarAssistenteId(String id_usuario) throws Exception {

		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarAssistenteId(id_usuario);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * CONSULTA ASSISTENTE (UsuarioServentiaGrupo) POR ID USUARIOSERVENTIAGRUPO
	 */
	public UsuarioDt consultarAssistenteServentiaGrupoId(String id_UsuarioServentiaGrupo) throws Exception {
		UsuarioDt dtRetorno = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarAssistenteServentiaGrupoId(id_UsuarioServentiaGrupo);
			obDados.copiar(dtRetorno);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Salva assistente de um usuário.
	 * Se quem está cadastrando é um juiz, o assistente terá o perfil de
	 * Assistente de Juiz,
	 * caso contrário Assistente Geral
	 * 
	 * @param assistentedt
	 * @throws Exception
	 */
	public void salvarAssistenteServentia(UsuarioDt assistentedt) throws Exception {
		String idGrupo = null;
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			// Quando o chefe for juiz ou advogado, o assistente terá um perfil
			// diferenciado
			// switch (Funcoes.StringToInt(assistentedt.getGrupoUsuarioChefe()))
			// {
			switch (Funcoes.StringToInt(assistentedt.getGrupoTipoUsuarioChefe())) {
			// case GrupoDt.JUIZES_VARA:
			case GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_JUIZES_VARA);
				break;

			// case GrupoDt.JUIZES_TURMA_RECURSAL:
			case GrupoTipoDt.JUIZ_TURMA:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
				break;

			case GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU);
				break;

			case GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_DESEMBARGADOR);
				break;

			// case GrupoDt.ADVOGADOS:
			// case GrupoDt.PROMOTORES:
			case GrupoTipoDt.ADVOGADO:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_ADVOGADOS);
				break;
			case GrupoTipoDt.MP:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_MP);
				break;
			default:
				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR);
				break;
			}

			obFabricaConexao.iniciarTransacao();

			// SALVA UsuarioServentia

			UsuarioServentiaNe us = new UsuarioServentiaNe();
			usuarioServentiaDt = new UsuarioServentiaDt();
			usuarioServentiaDt.setId_UsuarioServentiaChefe(assistentedt.getId_UsuarioServentiaChefe());
			usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
			usuarioServentiaDt.setId_Serventia(assistentedt.getId_Serventia());
			usuarioServentiaDt.setId_Usuario(assistentedt.getId());
			usuarioServentiaDt.setId_UsuarioLog(assistentedt.getId_UsuarioLog());
			usuarioServentiaDt.setIpComputadorLog(assistentedt.getIpComputadorLog());
			us.salvar(usuarioServentiaDt, obFabricaConexao);

			// SALVA UsuarioServentiaGrupo
			UsuarioServentiaGrupoNe usg = new UsuarioServentiaGrupoNe();
			usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
			usuarioServentiaGrupoDt.setId_UsuarioServentia(usuarioServentiaDt.getId());

			if (idGrupo == null)
				throw new MensagemException("Grupo Não Encontrado.");
			usuarioServentiaGrupoDt.setId_Grupo(idGrupo);
			usuarioServentiaGrupoDt.setId_UsuarioLog(assistentedt.getId_UsuarioLog());
			usuarioServentiaGrupoDt.setIpComputadorLog(assistentedt.getIpComputadorLog());
			usg.salvar(usuarioServentiaGrupoDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}

	}

	// SALVA ASSISTENTE (Usuario)
	public void salvarAssistente(UsuarioDt assistentedt) throws Exception {
		String idGrupo = null;
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{


			// Quando o chefe for juiz ou advogado, o assistente terá um perfil
			// diferenciado
			switch (Funcoes.StringToInt(assistentedt.getGrupoUsuarioChefe())) {
    			case GrupoDt.JUIZES_VARA:
    			case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
    			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
    			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_JUIZES_VARA);
    				break;
    
    			case GrupoDt.JUIZES_TURMA_RECURSAL:
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU);
    				break;
    
    			case GrupoDt.PRESIDENTE_SEGUNDO_GRAU:
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU);
    				break;
    
    			case GrupoDt.DESEMBARGADOR:
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_DESEMBARGADOR);
    				break;
    
    			case GrupoDt.ADVOGADO_PARTICULAR:
    			case GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL:
    			case GrupoDt.ADVOGADO_PUBLICO_ESTADUAL:
    			case GrupoDt.ADVOGADO_PUBLICO_UNIAO:
    			case GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO:
    			case GrupoDt.ADVOGADO_PUBLICO:
    			case GrupoDt.ADVOGADO_DEFENSOR_PUBLICO:
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_ADVOGADOS);
    				break;
    			case GrupoDt.MP_TCE:						
    			case GrupoDt.MINISTERIO_PUBLICO:			
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR_MP);
    				break;
    			default:
    				idGrupo = new GrupoNe().consultarCodigo(GrupoDt.ASSESSOR);
    				break;
			}

			obFabricaConexao.iniciarTransacao();

			// SE ASSISTENTE NÃO POSSUIR ID SALVA o USUARIO, UsuarioServentia e
			// UsuarioServentiaGrupo
			if (assistentedt.getId().equalsIgnoreCase("")) {

				// SALVAR ENDERECO DO USUARIO (ServidorJudiciario)
				EnderecoNe enderecoNe = new EnderecoNe();
				EnderecoDt enderecoDt = new EnderecoDt();
				enderecoDt.setId_Bairro(assistentedt.getEnderecoUsuario().getId_Bairro());
				enderecoDt.setLogradouro(assistentedt.getEnderecoUsuario().getLogradouro());
				enderecoDt.setNumero(assistentedt.getEnderecoUsuario().getNumero());
				enderecoDt.setComplemento(assistentedt.getEnderecoUsuario().getComplemento());
				enderecoDt.setCep(assistentedt.getEnderecoUsuario().getCep());
				enderecoDt.setId_UsuarioLog(assistentedt.getId_UsuarioLog());
				enderecoDt.setIpComputadorLog(assistentedt.getIpComputadorLog());
				enderecoNe.salvar(enderecoDt, obFabricaConexao);
				assistentedt.setId_Endereco(enderecoDt.getId());

				// SALVA USUARIO
				assistentedt.setAtivo("1"); // 1-ATIVO
				salvar(assistentedt, obFabricaConexao);

				// SALVA UsuarioServentia
				UsuarioServentiaNe us = new UsuarioServentiaNe();
				usuarioServentiaDt = new UsuarioServentiaDt();
				usuarioServentiaDt.setId_Serventia(assistentedt.getId_Serventia());
				usuarioServentiaDt.setId_Usuario(assistentedt.getId());
				usuarioServentiaDt.setAtivo(assistentedt.getAtivo());
				usuarioServentiaDt.setId_UsuarioLog(assistentedt.getId_UsuarioLog());
				usuarioServentiaDt.setIpComputadorLog(assistentedt.getIpComputadorLog());
				usuarioServentiaDt.setId_UsuarioServentiaChefe(assistentedt.getId_UsuarioServentiaChefe());
				us.salvar(usuarioServentiaDt, obFabricaConexao);

				// SALVA UsuarioServentiaGrupo
				UsuarioServentiaGrupoNe usg = new UsuarioServentiaGrupoNe();
				usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
				usuarioServentiaGrupoDt.setId_UsuarioServentia(usuarioServentiaDt.getId());

				if (idGrupo == null)
					throw new MensagemException("Erro: Grupo Não Encontrado.");
				usuarioServentiaGrupoDt.setId_Grupo(idGrupo);
				usuarioServentiaGrupoDt.setId_UsuarioLog(assistentedt.getId_UsuarioLog());
				usuarioServentiaGrupoDt.setIpComputadorLog(assistentedt.getIpComputadorLog());
				usg.salvar(usuarioServentiaGrupoDt, obFabricaConexao);

			}
			
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			assistentedt.setId("");
			assistentedt.getEnderecoUsuario().setId("");
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Salva Usuário Parte
	 */
	public void salvarUsuarioParte(UsuarioParteDt usuarioParteDt) throws Exception {
		EnderecoNe enderecoNe = new EnderecoNe();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();

			LogDt logDt = new LogDt(usuarioParteDt.getId_UsuarioLog(), usuarioParteDt.getIpComputadorLog());

			if (usuarioParteDt.getId().equalsIgnoreCase("")) {
				// Salva endereço
				EnderecoDt enderecoDt = usuarioParteDt.getEnderecoUsuario();
				enderecoNe.salvar(enderecoDt, obFabricaConexao);

				// Salva Usuário
				usuarioParteDt.setAtivo("1"); // 1-ATIVO
				usuarioParteDt.setId_Endereco(enderecoDt.getId());
				salvar(usuarioParteDt, obFabricaConexao);

				// Salva Usuário Serventia (Serventia PARTE)
				UsuarioServentiaNe us = new UsuarioServentiaNe();
				UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaDt();
				usuarioServentiaDt.setId_Usuario(usuarioParteDt.getId());
				ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(String.valueOf(ServentiaDt.Parte));
				usuarioServentiaDt.setId_Serventia(serventiaDt.getId());
				usuarioServentiaDt.setAtivo(usuarioParteDt.getAtivo());
				usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
				usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
				us.salvar(usuarioServentiaDt, obFabricaConexao);

				// Salva UsuarioServentiaGrupo (Grupo PARTE)
				UsuarioServentiaGrupoNe usg = new UsuarioServentiaGrupoNe();
				UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
				usuarioServentiaGrupoDt.setId_UsuarioServentia(usuarioServentiaDt.getId());
				GrupoDt grupoDt = new GrupoNe().consultarGrupoCodigo(String.valueOf(GrupoDt.PARTES));
				usuarioServentiaGrupoDt.setId_Grupo(grupoDt.getId());
				usuarioServentiaGrupoDt.setId_UsuarioLog(logDt.getId_Usuario());
				usuarioServentiaGrupoDt.setIpComputadorLog(logDt.getIpComputador());
				usg.salvar(usuarioServentiaGrupoDt, obFabricaConexao);

				// Atualiza Parte para armazenar id_UsuarioServentia
				ProcessoParteDt parteDt = new ProcessoParteDt();
				parteDt.setId_ProcessoParte(usuarioParteDt.getParteDt().getId_ProcessoParte());
				parteDt.setId_UsuarioServentia(usuarioServentiaDt.getId());
				parteDt.setId_UsuarioLog(logDt.getId_Usuario());
				parteDt.setIpComputadorLog(logDt.getIpComputador());
				new ProcessoParteNe().habilitaUsuarioParte(parteDt, logDt, obFabricaConexao);

			} else {
				// Se possuir Id atualiza os dados do Usuário e Endereço
				EnderecoDt enderecoDt = usuarioParteDt.getEnderecoUsuario();
				enderecoNe.salvar(enderecoDt, obFabricaConexao);
				//
				usuarioParteDt.setId_Endereco(enderecoDt.getId());
				//

				// Atualiza Usuário
				salvar(usuarioParteDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Chama método responsável em Habilitar um Usuário em uma Serventia e
	 * Grupo.
	 * 
	 * @param usuarioDt
	 *            , objeto com dados do usuário, serventia e grupo
	 * @author leandro, msapaula
	 */
	public void salvarUsuarioServentiaGrupo(UsuarioDt usuarioDt) throws Exception {
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		UsuarioServentiaDt usuarioServentiaDt = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
			LogDt logDt = new LogDt(usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog());
			
			// Salva UsuarioServentia
	 	       
			usuarioServentiaDt = new UsuarioServentiaDt();
		   	usuarioServentiaDt.setId(usuarioDt.getId_UsuarioServentia());
			usuarioServentiaDt.setId_Serventia(usuarioDt.getId_Serventia());
			usuarioServentiaDt.setId_Usuario(usuarioDt.getId());
			usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
			usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
			usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
			usuarioServentiaNe.salvar(usuarioServentiaDt, obFabricaConexao);			
			usuarioDt.setId_UsuarioServentia(usuarioServentiaDt.getId());			

			// Salva UsuarioServentiaGrupo
			
			usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
			new UsuarioServentiaGrupoNe().salvarUsuarioServentiaGrupo(usuarioServentiaGrupoDt, usuarioDt, logDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Chama método responsável em Habilitar um Usuário em uma Serventia e
	 * Grupo.
	 * Copiei este método do método acima(public void
	 * salvarUsuarioServentiaGrupo(UsuarioDt usuarioDt))
	 * 
	 * @param UsuarioDt
	 *            usuarioDt, objeto com dados do usuário, serventia e grupo
	 * @param FabricaConexao
	 *            obFabricaConexao
	 * @author fred
	 */
	public void salvarUsuarioServentiaGrupo(UsuarioDt usuarioDt, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		try {
			LogDt logDt = new LogDt(usuarioDt.getId_UsuarioLog(), usuarioDt.getIpComputadorLog());

			// Salva UsuarioServentia
			
	        usuarioServentiaDt = new UsuarioServentiaDt();
		   	usuarioServentiaDt.setId(usuarioDt.getId_UsuarioServentia());
			usuarioServentiaDt.setId_Serventia(usuarioDt.getId_Serventia());
			usuarioServentiaDt.setId_Usuario(usuarioDt.getId());
			usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
			usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
			usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
			usuarioServentiaNe.salvar(usuarioServentiaDt, obFabricaConexao);			
			usuarioDt.setId_UsuarioServentia(usuarioServentiaDt.getId());			

			// Salva UsuarioServentiaGrupo
			
			usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
			new UsuarioServentiaGrupoNe().salvarUsuarioServentiaGrupo(usuarioServentiaGrupoDt, usuarioDt, logDt, obFabricaConexao);

		} catch (Exception e) {
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");
			throw e;
		}
	}

	/**
	 * Método responsável por verificar se um determinado servidor já está
	 * cadastrado em uma Serventia
	 * e Grupo passados, independente se o status do cadastro é ativo ou
	 * inativo.
	 * 
	 * @param usuarioDt
	 *            - dados do advogado
	 * @return = true se já estiver habilitado e false se não estiver habilitado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean verificarHabilitacaoServidor(UsuarioDt usuarioDt) throws Exception {
		boolean retorno;

		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
		usuarioServentiaGrupoDt.setId_Grupo(usuarioDt.getId_Grupo());
		usuarioServentiaGrupoDt.setId_Serventia(usuarioDt.getId_Serventia());
		usuarioServentiaGrupoDt.setId_Usuario(usuarioDt.getId());
		retorno = usuarioServentiaGrupoNe.verificarHabilitacaoUsuario(usuarioServentiaGrupoDt);

		return retorno;
	}

	/**
	 * Método que verifica se o cadastro de senha para partes pode ser realizado
	 * para o processo
	 * passado
	 * 
	 * @param processoDt
	 *            dt de processo
	 * @param usuarioDt
	 *            usuário que vai cadastrar senha
	 * 
	 * @author msapaula
	 */
	public String podeHabilitarUsuarioParte(ProcessoDt processoDt, UsuarioDt usuarioDt) {
		String stMensagem = "";
		// Se usuário for de serventia diferente do processo, não poderá
		// habilitar advogados
		if (!processoDt.getId_Serventia().equals(usuarioDt.getId_Serventia()))
			stMensagem += "Sem permissão para cadastrar senha para partes nesse processo.";

		if (processoDt.isArquivado()){
			stMensagem += " Não é possível executar essa ação, processo está arquivado. \n";
		}else if (processoDt.isErroMigracao()){
			stMensagem += " Não é possível executar essa ação, processo está com ERRO DE MIGRAÇÃO. \n";
		}

		return stMensagem;
	}

	/**
	 * Prepara dados para vincular uma Parte ao Usuario cadastrado para a mesma.
	 * 
	 * @param usuarioParteDt
	 *            , dt com os dados da parte e usuarioServentia
	 * 
	 * @author msapaula
	 */
	public void habilitaUsuarioParte(ProcessoParteDt parteDt, String id_UsuarioServentia, LogDt logDt) throws Exception {
		ProcessoParteNe processoParteNe = new ProcessoParteNe();

		parteDt.setId_UsuarioServentia(id_UsuarioServentia);
		processoParteNe.habilitaUsuarioParte(parteDt, logDt, null);

		processoParteNe = null;
	}

	/**
	 * Prepara dados para desvincular um Usuario de uma parte
	 * 
	 * @param usuarioParteDt
	 *            , dt com dados da parte e usuario
	 * 
	 * @author msapaula
	 */
	public void desabilitaUsuarioParte(ProcessoParteDt parteDt, LogDt logDt) throws Exception {
		ProcessoParteNe processoParteNe = new ProcessoParteNe();

		parteDt.setId_UsuarioServentia("null");
		processoParteNe.desabilitaUsuarioParte(parteDt, logDt);

		processoParteNe = null;
	}

	/**
	 * Realiza chamada a UsuarioServentiaNe para localizar o Id_UsuarioServentia
	 * para determinada
	 * parte de acordo com o CPF
	 */
	public UsuarioServentiaDt consultarUsuarioServentiaParte(String cpfParte) throws Exception {
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		return usuarioServentiaNe.consultarUsuarioServentiaParte(cpfParte);
	}

	/**
	 * Verifica se usuário logado é externo
	 */
	public boolean isUsuarioExterno() {
		return this.isUsuarioExterno(obDados.getGrupoCodigo());
	}

	/**
	 * Método que valida se usuário em questão é um usuário externo (Advogados,
	 * Delegados, Promotores, Contadores e Consultores)
	 * 
	 * @param grupoCodigo
	 *            , código do grupo a ser verificado
	 */
	public boolean isUsuarioExterno(String grupoCodigo) {
		boolean boRetorno = false;

		int grupo = Funcoes.StringToInt(grupoCodigo);
		if (grupo == GrupoDt.ADVOGADO_PARTICULAR || grupo == GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL || grupo == GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO || grupo == GrupoDt.ADVOGADO_PUBLICO_ESTADUAL
				|| grupo == GrupoDt.ADVOGADO_PUBLICO_UNIAO || grupo == GrupoDt.ADVOGADO_DEFENSOR_PUBLICO
				|| grupo == GrupoDt.ASSESSOR_MP || grupo == GrupoDt.ASSESSOR_ADVOGADOS || grupo == GrupoDt.AUTORIDADES_POLICIAIS || grupo == GrupoDt.MINISTERIO_PUBLICO || grupo == GrupoDt.MP_TCE || grupo == GrupoDt.CONSULTORES
				|| grupo == GrupoDt.ASSESSOR || grupo == GrupoDt.PARTES || grupo == GrupoDt.POPULACAO || grupo == GrupoDt.COORDENADOR_PROMOTORIA
				|| grupo == GrupoDt.COORDENADOR_PROCURADORIA_ESTADUAL || grupo == GrupoDt.COORDENADOR_DEFENSORIA_PUBLICA
				|| grupo == GrupoDt.COORDENADOR_PROCURADORIA_MUNICIPAL || grupo == GrupoDt.COORDENADOR_PROCURADORIA_FEDERAL || grupo == GrupoDt.COORDENADOR_ESCRITORIO_JURIDICO
				|| grupo == GrupoDt.ADVOGADO_PUBLICO || grupo == GrupoDt.COORDENADOR_ADVOCACIA_PUBLICA) {
			boRetorno = true;
		}
		return boRetorno;
	}

	public String verificarServentiaOAB(UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";

		UsuarioServentiaOabNe oabNe = new UsuarioServentiaOabNe();
		stRetorno = oabNe.VerificarOAB(usuarioDt);
		oabNe = null;

		return stRetorno;
	}

	public String verificarServentiaGrupoUsuario(UsuarioDt usuarioDt) throws Exception {
		String stRetorno = "";

		UsuarioServentiaGrupoNe grupoNe = new UsuarioServentiaGrupoNe();
		stRetorno = grupoNe.VerificarServidorJudiciario(usuarioDt);
		grupoNe = null;

		return stRetorno;
	}

	public List consultarDescricaoBairro(String descricao, String cidade, String uf, String posicao) throws Exception {
		List tempList = null;

		BairroNe Bairrone = new BairroNe();
		tempList = Bairrone.consultarDescricao(descricao, cidade, uf, posicao);
		QuantidadePaginas = Bairrone.getQuantidadePaginas();
		Bairrone = null;

		return tempList;
	}

	public List consultarDescricaoRgOrgaoExpedidor(String tempNomeBusca, String sigla, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;

		RgOrgaoExpedidorNe rgOrgaoExpedidorne = new RgOrgaoExpedidorNe();
		tempList = rgOrgaoExpedidorne.consultarDescricao(tempNomeBusca, sigla, PosicaoPaginaAtual);
		QuantidadePaginas = rgOrgaoExpedidorne.getQuantidadePaginas();
		rgOrgaoExpedidorne = null;

		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;

		CidadeNe Naturalidadene = new CidadeNe();
		tempList = Naturalidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Naturalidadene.getQuantidadePaginas();
		Naturalidadene = null;

		return tempList;
	}

	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public List consultarServentiasHabilitacaoAdvogado(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		List tempList = null;

		ServentiaNe Serventiane = new ServentiaNe();
		tempList = Serventiane.consultarServentiasHabilitacaoAdvogado(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Serventiane.getQuantidadePaginas();
		Serventiane = null;

		return tempList;
	}

	public List consultarServentiasHabilitacaoUsuarios(String tempNomeBusca, String PosicaoPaginaAtual, String serventiaTipo, String grupoCodigo) throws Exception {
		List tempList = null;

		ServentiaNe Serventiane = new ServentiaNe();
		tempList = Serventiane.consultarServentiasHabilitacaoUsuarios(tempNomeBusca, PosicaoPaginaAtual, serventiaTipo, grupoCodigo);
		QuantidadePaginas = Serventiane.getQuantidadePaginas();
		Serventiane = null;

		return tempList;
	}

	public List consultarGruposHabilitacaoServidores(String tempNomeBusca, String PosicaoPaginaAtual, String grupoCodigo) throws Exception {
		List tempList = null;

		GrupoNe grupone = new GrupoNe();
		tempList = grupone.consultarGruposHabilitacaoServidores(tempNomeBusca, PosicaoPaginaAtual, grupoCodigo);
		QuantidadePaginas = grupone.getQuantidadePaginas();
		grupone = null;

		return tempList;
	}

	public List consultarGruposListaUsuarios(String tempNomeBusca, String PosicaoPaginaAtual, String grupoCodigo) throws Exception {
		List tempList = null;

		GrupoNe grupone = new GrupoNe();
		tempList = grupone.consultarGruposListaUsuarios(tempNomeBusca, PosicaoPaginaAtual, grupoCodigo);
		QuantidadePaginas = grupone.getQuantidadePaginas();
		grupone = null;

		return tempList;
	}

	public String consultarGruposListaUsuariosJSON(String tempNomeBusca, String PosicaoPaginaAtual, String grupoCodigo) throws Exception {
		String stTemp = "";

		GrupoNe grupoNe = new GrupoNe();
		stTemp = grupoNe.consultarGruposListaUsuariosJSON(tempNomeBusca, PosicaoPaginaAtual, grupoCodigo);

		return stTemp;
	}

	public ProcessoParteDt consultaProcessoParteId(String id_ProcessoParte) throws Exception {
		ProcessoParteDt dtRetorno = null;

		ProcessoParteNe parteNe = new ProcessoParteNe();
		dtRetorno = parteNe.consultarId(id_ProcessoParte);
		parteNe = null;

		return dtRetorno;
	}

	public String verificarUsuarioServentiaOab(UsuarioDt usuarioDt) throws Exception {
		String stRetorno = null;

		UsuarioServentiaOabNe usuarioServentiaOabNe = new UsuarioServentiaOabNe();
		stRetorno = usuarioServentiaOabNe.VerificarOAB(usuarioDt);
		usuarioServentiaOabNe = null;

		return stRetorno;
	}

	public byte[] relListaUsuarios(String diretorioProjetos, List listaUsuarios, String titulo) throws Exception {
		byte[] temp = null;
		// UsuarioBeanDecoratorJRDataSource ei = new
		// UsuarioBeanDecoratorJRDataSource(bean);
		InterfaceSubReportJasper ei = new InterfaceSubReportJasper(listaUsuarios);
		String pathJasper = diretorioProjetos + "WEB-INF" + File.separator + "relatorios" + File.separator + "listaUsuarios" + File.separator;

		// parâmetros do relatório
		Map parametros = new HashMap();
		parametros.put("pathRelatorio", pathJasper + "detalhesUsuarios.jasper");
		parametros.put("tipoAtividade", titulo);

		ByteArrayOutputStream baos = null;

		try {
			JasperPrint jp = JasperFillManager.fillReport(pathJasper + "relatorioUsuarios.jasper", parametros, ei);
			JRPdfExporter jr = new JRPdfExporter();
			jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
			baos = new ByteArrayOutputStream();
			jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
			jr.exportReport();

			temp = baos.toByteArray();

			baos.close();

		} catch (JRException e) {
			try {
				if (baos != null)
					baos.close();
			} catch (Exception e2) {
			}
		}
		return temp;
	}

	/**
	 * Chama método que irá realizar a consulta
	 * 
	 * @throws Exception
	 */
	public List consultarUsuarioServentiasGrupos(String id) throws Exception {
		List tempList = null;

		UsuarioServentiaGrupoNe grupoNe = new UsuarioServentiaGrupoNe();
		tempList = grupoNe.consultarServentiasGruposServidorJudiciario(id);
		grupoNe = null;

		return tempList;
	}

	/**
	 * Método que valida o acesso dos usuários dos GrupoTipo de Assistentes
	 * (ASSISTENTE, ASSESSOR_JUIZ_VARA_TURMA, ASSISTENTE_ADVOGADO_PROMOTOR)
	 * 
	 * @param usuario
	 *            - UsuarioDt - é o usuário assistente que será validado
	 * @return String - mensagem de erro, caso exista. Se retornar null
	 *         significa que não há erro e o acesso está correto.
	 * @throws Exception
	 */
	public String validarAcessoAssistentes(UsuarioDt usuario) throws Exception {

		// Valida se o usuário está ativo no cargo/serventia
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
		UsuarioServentiaDt usuarioServentiaDt = usuarioServentiaNe.consultarId(usuario.getId_UsuarioServentia());
		if (!usuarioServentiaDt.isAtivo()) {
			return "Usuário não está ativo no cargo/serventia.";
		}

		// Valida se o chefe do usuário está ativo no cargo/serventia
		String id_Chefe = usuarioServentiaDt.getId_UsuarioServentiaChefe();
		if (id_Chefe==null || id_Chefe.isEmpty()){
			return "O chefe do Usuário não foi encontrado.";
		}
		usuarioServentiaDt = usuarioServentiaNe.consultarId(id_Chefe);
		if (!usuarioServentiaDt.isAtivo()) {
			return "O chefe do Usuário não está ativo no cargo/serventia.";
		}

		// Se o chefe do assistente for do grupo Advogados, não há necessidade
		// de validar o perfil
		// do chefe.
		if (!usuario.isAdvogadoUsuarioChefe()) {
			// Valida se o chefe do usuário está habilitado em um cargo da
			// serventia
			if (usuario.getId_ServentiaCargoUsuarioChefe() == null || usuario.getId_ServentiaCargoUsuarioChefe().equalsIgnoreCase("")) {
				return "O chefe do Usuário não está habilitado em um cargo da Serventia.";
			}
		}

		return null;
	}

	/**
	 * Método responsável por verificar se um determinado advogado já está
	 * habilitado em uma Serventia
	 * e Grupo passados.
	 * 
	 * @param advogadoDt
	 *            - dados do advogado
	 * @return = true se já estiver habilitado e false se não estiver habilitado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean verificarHabilitacaoUsuario(UsuarioDt advogadoDt) throws Exception {
		boolean retorno;

		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
		usuarioServentiaGrupoDt.setId_Serventia(advogadoDt.getId_Serventia());
		usuarioServentiaGrupoDt.setId_Usuario(advogadoDt.getId());

		retorno = usuarioServentiaGrupoNe.verificarHabilitacaoAdvogado(usuarioServentiaGrupoDt);

		return retorno;
	}

	public String consultarAdvogadoPublicoJSON(String oab, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAdvogadoPublicoJSON(oab, PosicaoPaginaAtual);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public List consultarAdvogadoPublicoServentiaJSON(String id_serv) throws Exception {
		// String stTemp = "";
		List liTemp = new ArrayList();
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarAdvogadoPublicoServentiaJSON(id_serv);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	public String consultarDescricaoAdvogadoJSON(String nome, String usuario, String oab, String rg, String cpf, String posicao) throws Exception {
		return consultarDescricaoAdvogadoJSON(nome, usuario, oab, rg, cpf, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}

	public String consultarDescricaoAdvogadoJSON(String nome, String usuario, String oab, String rg, String cpf, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoAdvogadoJSON(nome, usuario, oab, rg, cpf, posicao, ordenacao, quantidadeRegistros);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarDescricaoRgOrgaoExpedidorJSON(String sigla, String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String tempList = null;

		RgOrgaoExpedidorNe rgOrgaoExpedidorne = new RgOrgaoExpedidorNe();
		tempList = rgOrgaoExpedidorne.consultarDescricaoJSON(sigla, tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = rgOrgaoExpedidorne.getQuantidadePaginas();
		rgOrgaoExpedidorne = null;

		return tempList;
	}

	public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";

		CidadeNe Naturalidadene = new CidadeNe();
		stTemp = Naturalidadene.consultarDescricaoJSON(tempNomeBusca, uf, PosicaoPaginaAtual);

		return stTemp;
	}

	public String consultarServentiasHabilitacaoAdvogadoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";

		ServentiaNe Serventiane = new ServentiaNe();
		stTemp = Serventiane.consultarServentiasHabilitacaoAdvogadoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}

	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";

		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);

		return stTemp;
	}

	public BairroDt consultarDescricaoBairroId(String idBairro) throws Exception {
		BairroDt bairroDt = new BairroDt();

		BairroNe Bairrone = new BairroNe();
		bairroDt = Bairrone.consultarId(idBairro);

		return bairroDt;
	}

	public String consultarTodosUsuariosJSON(String nome, String usuario, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarTodosUsuariosJSON(nome, usuario, posicao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarDescricaoServidorJudiciarioJSON(String nome, String usuario, String posicao) throws Exception {
		return consultarDescricaoServidorJudiciarioJSON(nome, usuario, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}

	public String consultarDescricaoServidorJudiciarioJSON(String nome, String usuario, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServidorJudiciarioJSON(nome, usuario, posicao, ordenacao, quantidadeRegistros);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarServentiasHabilitacaoUsuariosJSON(String tempNomeBusca, String PosicaoPaginaAtual, String serventiaTipo, String grupoCodigo, String idComarca) throws Exception {
		String stTemp = "";

		ServentiaNe Serventiane = new ServentiaNe();
		stTemp = Serventiane.consultarServentiasHabilitacaoUsuariosJSON(tempNomeBusca, PosicaoPaginaAtual, serventiaTipo, grupoCodigo, idComarca);

		return stTemp;
	}

	public String consultarServentiasHabilitacaoUsuariosJSON(String tempNomeBusca, String PosicaoPaginaAtual, String serventiaTipo, String grupoCodigo) throws Exception {
		String stTemp = "";
		ServentiaNe Serventiane = new ServentiaNe();
		stTemp = Serventiane.consultarServentiasHabilitacaoUsuariosJSON(tempNomeBusca, PosicaoPaginaAtual, serventiaTipo, grupoCodigo);
		return stTemp;
	}

	public String consultarGruposHabilitacaoServidoresJSON(String tempNomeBusca, String PosicaoPaginaAtual, String grupoCodigo) throws Exception {
		String stTemp = "";

		GrupoNe grupone = new GrupoNe();
		stTemp = grupone.consultarGruposHabilitacaoServidoresJSON(tempNomeBusca, PosicaoPaginaAtual, grupoCodigo);

		return stTemp;
	}

	public GrupoDt consultarGrupoId(String stId) throws Exception {
		GrupoDt grupoDt = new GrupoDt();

		GrupoNe grupone = new GrupoNe();
		grupoDt = grupone.consultarId(stId);

		return grupoDt;
	}

	public String consultarAdvogadoOAB(String codigoComarca, String oab, String complemento, String uf) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarAdvogadoOAB(codigoComarca, oab, complemento, uf);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	/**
	 * Método que verifica se login e senha passados estão cadastrados no
	 * sistema
	 * 
	 * @param usuario
	 *            , login digitado
	 * @param senha
	 *            , senha digitada
	 * @return
	 */
	public boolean consultaUsuarioSenhaMD5(String usuario, String senha) throws Exception {
		boolean boRetorno = false;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obDados = obPersistencia.consultaUsuarioSenhaMD5(usuario, senha);
			if (!obDados.getId().equalsIgnoreCase("")) {
				boRetorno = true;
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return boRetorno;
	}

	/**
	 * Método para consultar um advogado de acordo com a oab e tipo da serventia
	 * passada
	 * 
	 * @param id_Serv
	 *            , identificador da serventia
	 * @author asrocha 21/11/2014
	 */
	public List consultarProcuradorJuridico(String id_Serv) throws Exception {
		List liTemp = new ArrayList();

		UsuarioServentiaOabNe usuarioServentiaOabNe = new UsuarioServentiaOabNe();
		liTemp = usuarioServentiaOabNe.consultarUsuariosServentiaOab(id_Serv);

		return liTemp;
	}

	/**
	 * Método que consulta o nome do usuário pelo serventia cargo dele.
	 * 
	 * @param idServCargo
	 *            - ID do Serventia Cargo
	 * @return nome do usuário
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarNomeUsuarioServentiaCargo(String idServCargo) throws Exception {
		String nome = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			nome = obPersistencia.consultarNomeUsuarioServentiaCargo(idServCargo);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return nome;
	}
	
	/**
	 * Método que consulta o nome do usuário pelo serventia cargo dele.
	 * 
	 * @param idServCargo
	 *            - ID do Serventia Cargo
	 * @return nome do usuário
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarNomeCargoUsuarioServentiaCargo(String idServCargo) throws Exception {
		String nome = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			nome = obPersistencia.consultarNomeCargoUsuarioServentiaCargo(idServCargo);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return nome;
	}

	public boolean isAdvogado() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		return  (GrupoCodigo == GrupoDt.MINISTERIO_PUBLICO ||
				GrupoCodigo == GrupoDt.ADVOGADO_PARTICULAR ||
				GrupoCodigo == GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO ||
				GrupoCodigo == GrupoDt.ADVOGADO_PUBLICO_UNIAO ||
				GrupoCodigo == GrupoDt.ADVOGADO_PUBLICO_ESTADUAL ||
				GrupoCodigo == GrupoDt.ADVOGADO_DEFENSOR_PUBLICO ||
				GrupoCodigo == GrupoDt.ADVOGADO_PUBLICO ||
				GrupoCodigo == GrupoDt.ADVOGADO_PUBLICO_MUNICIPAL);
			
	}
	
	public boolean isAdvogadoParticular() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		return (
				GrupoCodigo == GrupoDt.ADVOGADO_PARTICULAR ||
				GrupoCodigo == GrupoDt.ADVOGADO_PARTICULAR_ESCRITORIO_JURIDICO
				);
			
	}
	
	public boolean isAdministrador() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		if (obDados != null && Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.ADMINISTRADORES)
			return true;
		else
			return false;
	}
	
	public boolean isPublico() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		if (obDados != null && Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.PUBLICO)
			return true;
		else
			return false;
	}
	
	public boolean isConsultor() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		if (obDados != null && Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.CONSULTORES)
			return true;
		else
			return false;
	}
	
	public boolean isGerenciamentoSegundoGrau() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		if (obDados != null && Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.GERENCIAMENTO_SEGUNDO_GRAU)
			return true;
		else
			return false;
	}

	/**
	 * Método para verificar se o usuário atual pertence ao grupo de contadores.
	 * 
	 * @return boolean
	 */
	public boolean isContador() {
		if (obDados != null
				&&
				(Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.CONTADOR_PROCURADORIA_MUNICIPAL ||
				Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.CONTADORES_VARA)) {
			return true;
		}

		return false;
	}
	
	public boolean isCoodernadorCentralMandado() {
		if (obDados != null 
				&&
				Funcoes.StringToInt(obDados.getGrupoCodigo()) == GrupoDt.COORDENADOR_CENTRAL_MANDADO) {
			return true;
		}

		return false;
	}
	
	public boolean isMagistrado() {
		if (obDados != null)
			return obDados.isMagistrado();

		return false;
	}
	
	/**
	 * Método que verifica se o usuário da sessão pode gerar a informação de 
	 * antecedentes criminais da parte.
	 * @param idProcesso
	 * @return boolean com a liberação do acesso
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean isPodeGerarInformacaoAntecedentes(String idProcesso) throws Exception{
		ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso);
		if(obDados.isMesmaServentia(processoDt.getId_Serventia())
				|| new ProcessoResponsavelNe().isResponsavelProcesso(obDados.getId_ServentiaCargo(), processoDt.getId())
				|| obDados.isServentiaInfanciaJuventudeInfracional()) {
			return true;
		}
		return false;
	}

	public boolean isTurma() {
		int GrupoCodigo = Funcoes.StringToInt(obDados.getGrupoCodigo());
		if (	GrupoCodigo == GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL ||
				GrupoCodigo == GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL)
			return true;
		else
			return false;
	}

	/**
	 * Método que consulta o último login do usuário.
	 * 
	 * @param idUsuario
	 *            - ID do Usuário.
	 * @return UsuarioUltimoLoginDt
	 * @throws Exception
	 * @author mmgomes
	 */
	public UsuarioUltimoLoginDt consultarUsuarioUltimoLogin(String idUsuario) throws Exception {
		UsuarioUltimoLoginDt usuarioLogin = null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			usuarioLogin = obPersistencia.consultarUsuarioUltimoLogin(idUsuario);

		} finally {
			obFabricaConexao.fecharConexao();
		}

		return usuarioLogin;
	}

	/**
	 * Método que grava o último login do usuário.
	 * 
	 * @param dados
	 *            - UsuarioUltimoLoginDt.
	 * @throws Exception
	 * @author mmgomes
	 */
	public void inserirUsuarioUltimoLogin(UsuarioUltimoLoginDt dados) throws Exception {
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			obPersistencia.inserirUsuarioUltimoLogin(dados);
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	//desaqui: hataraku
	public void gravarTipoMenuMovimentacao(UsuarioUltimoLoginDt dados) throws Exception
	{
		FabricaConexao obFabricaConexao = null;
		try
		{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());

			obPersistencia.gravarTipoMenuMovimentacao(dados);
		}
		finally
		{
			obFabricaConexao.fecharConexao();
		}
	}

	public boolean isAtivo() {

		if (obDados != null)
			return obDados.isAtivo();
		return false;

	}

	public void libereAssistenteGuardarParaAssinar(UsuarioDt assistentedt) throws Exception {
		new UsuarioServentiaNe().libereAssistenteGuardarParaAssinar(assistentedt);
	}

	public void bloqueieAssistenteGuardarParaAssinar(UsuarioDt assistentedt) throws Exception {
		new UsuarioServentiaNe().bloqueieAssistenteGuardarParaAssinar(assistentedt);
	}

	public void setPermissao(int pemissao) {
		obDados.setPermissao(pemissao);

	}

	private String listaMenuEspecial(List lista) {
		String stTemp = "";
		List liPermissaoPai = new ArrayList();
		// guardo todos os objeto que não tem pais, ou seja, os menus de
		// primeiro nivel
		for (int i = 0; i < lista.size(); i++) {
			PermissaoDt tempPermissaoDt = (PermissaoDt) lista.get(i);
			if (tempPermissaoDt.getId_PermissaoPai().length() == 0) {
				liPermissaoPai.add(tempPermissaoDt);
			} else {
				// Atribuo os filhos aos pais
				for (int k = 0; k < lista.size(); k++) {
					PermissaoDt tempPermissaoDt2 = (PermissaoDt) lista.get(k);
					if (tempPermissaoDt2.getId().equals(tempPermissaoDt.getId_PermissaoPai()))
						tempPermissaoDt2.incluirPermissao(tempPermissaoDt);
				}
			}
		}
		// pego os menus dos pais, e eles pegam os dos filhos
		for (int i = 0; i < liPermissaoPai.size(); i++)
			stTemp += ((PermissaoDt) liPermissaoPai.get(i)).getListaMenuEspecial();
		return stTemp;
	}

	private List listaMenuEspecialPJD(List lista) {
		List liPermissaoPai = new ArrayList();
		// guardo todos os objeto que não tem pais, ou seja, os menus de
		// primeiro nivel
		for (int i = 0; i < lista.size(); i++) {
			PermissaoDt tempPermissaoDt = (PermissaoDt) lista.get(i);
			
			//Solitação Karal altera o nome do menu na capa do processo.
			if(tempPermissaoDt.getPermissao().equals("Rec. Rep./Rec. Rep. Geral")){
				tempPermissaoDt.setPermissao("Recurso Repetitivo ou de Repercussão Geral");
			}
			
			if (tempPermissaoDt.getId_PermissaoPai().length() == 0) {
				liPermissaoPai.add(tempPermissaoDt);
			} else {
				// Atribuo os filhos aos pais
				for (int k = 0; k < lista.size(); k++) {
					PermissaoDt tempPermissaoDt2 = (PermissaoDt) lista.get(k);
					if (tempPermissaoDt2.getId().equals(tempPermissaoDt.getId_PermissaoPai()))
						tempPermissaoDt2.incluirPermissao(tempPermissaoDt);
				}
			}
		}
		return liPermissaoPai;
	}

	/**
	 * Retorna a lista de permissões para montar o menu do usuário na página
	 * 
	 * @author gschiquini
	 * @throws Exception
	 */
	public List getMenuPJD() throws Exception {
		List tempList = null;
		PermissaoNe obPermissaoNe = new PermissaoNe();
		FabricaConexao obFabricaConexao = null;
		try {
			if (obDados != null && obDados.getId().length() > 0) {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
				obDados.setPermissoes(obPersistencia.ConsultaUsuarioPermissoes(obDados.getId_UsuarioServentia(), obDados.getId_Grupo()));
				tempList = obPermissaoNe.getMenu(obDados.getId_UsuarioServentia(), obDados.getId_Grupo());
			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		obDados.getLiConsultar().clear();
		tempList = listaMenusPJD(tempList);
		return tempList;
	}

	public List getMenuEspecialPJD(int permissaoEspecialCodigo) throws Exception {
		String stRetorno = "";
		List listaPermissoes = null;
		PermissaoNe obPermissaoNe = new PermissaoNe();
		if (!obDados.getId().equalsIgnoreCase("")) {
			stRetorno = listaMenuEspecial(obPermissaoNe.getMenuEspecial(obDados.getId_UsuarioServentia(), obDados.getId_Grupo(), permissaoEspecialCodigo));
			listaPermissoes = listaMenuEspecialPJD(obPermissaoNe.getMenuEspecial(obDados.getId_UsuarioServentia(), obDados.getId_Grupo(), permissaoEspecialCodigo));
			obDados.addPermissaoEspecial(permissaoEspecialCodigo, stRetorno);
		}
		return listaPermissoes;
	}

	public String consultarDescricaoAssistenteServentiaJSON(String id_Usuario, String id_UsuarioServentia, String id_Serventia, String qntRegistro, String posicao, String tpOrdenacao, String pesquisa)
			throws Exception {
		String tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarDescricaoAssistenteServentiaJSON(id_Usuario, id_UsuarioServentia, id_Serventia, qntRegistro, posicao, tpOrdenacao, pesquisa);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}

	public UsuarioDt consultarAssistenteServentiaCpfJSON(String id_UsuarioServentia, String id_Serventia, String cpf) throws Exception {
		UsuarioDt usu;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			usu = obPersistencia.consultarAssistenteServentiaCpfJSON(id_UsuarioServentia, id_Serventia, cpf);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return usu;

	}

	public String consultarEmailUsuario(String idUsuario) throws Exception {
		String email;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			email = obPersistencia.consultarEmailUsuario(idUsuario);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return email;

	}

	public void setId_ProcessoCodigoAcesso(String stId) {		
		if (obDados !=null){
			obDados.setId_ProcessoCodigoAcesso(stId);			
		}		
	}

	public String getServentia() {
		if (obDados !=null){
			return obDados.getServentia();
		}
		return "";
	}
	
	public boolean isProprio() {
		if (obDados !=null){
			switch(obDados.getGrupoCodigoToInt()){
				case GrupoDt.ADMINISTRADORES: 
				case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL: 
				case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
				case GrupoDt.ANALISTA_FINANCEIRO:
				case GrupoDt.ANALISTAS_EXECUCAO_PENAL: 
				case GrupoDt.ANALISTA_JUDICIARIO_PRESIDENCIA:
				case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
				case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL: 
				case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
				case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL:
				case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
				case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL: 
				case GrupoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL:
				case GrupoDt.ANALISTA_PRE_PROCESSUAL:
				case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:
				case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
				case GrupoDt.JUIZ_LEIGO:
				case GrupoDt.ASSESSOR_JUIZES_VARA:
				case GrupoDt.CENTRAL_MANDADOS:
				case GrupoDt.CONCILIADORES_VARA:
				case GrupoDt.CONTADOR_PROCURADORIA_MUNICIPAL:
				case GrupoDt.CONTADORES_VARA:
				case GrupoDt.DISTRIBUIDOR_CAMARA:	
				case GrupoDt.ESTAGIARIO_PRIMEIRO_GRAU:
				case GrupoDt.ESTAGIARIO_SEGUNDO_GRAU: 
				case GrupoDt.ESTATISTICA: 
				case GrupoDt.JUIZ_AUXILIAR_PRESIDENCIA: 
				case GrupoDt.JUIZ_CORREGEDOR: 
				case GrupoDt.JUIZ_EXECUCAO_PENAL: 
				case GrupoDt.JUIZES_TURMA_RECURSAL:
				case GrupoDt.JUIZES_VARA:
				case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU:
				case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
				case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
				case GrupoDt.PRESIDENTE_SEGUNDO_GRAU: 
				case GrupoDt.TECNICO_EXECUCAO_PENAL:
				case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL: 
				case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL: 
				case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL: 
				case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
				case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL: 
					return true;
			}
			
		}
		
		return false;
	}

	public boolean isTemIdUsuario() {
		return this.getId_Usuario() != null && this.getId_Usuario().length() > 0 && !this.getId_Usuario().equals(UsuarioDt.USUARIO_PUBLICO);
	}

	public String getId_UsuarioServentia() {
		if (obDados != null)
			return obDados.getId_UsuarioServentia();
		return "";		
	}

	public boolean isAssessorMagistrado() {
		if (obDados != null){
			return obDados.isAssessorMagistrado();
		}
		return false;
	}
	public String getId_UsuarioServentiaChefe(){
		if (obDados != null){
			return obDados.getId_UsuarioServentiaChefe();
		}
		return null;
	}

	public int getNivelAcesso() {
				
		int grupoCodigo = getUsuarioDt().getGrupoCodigoToInt();
	
		switch (grupoCodigo) {
			case GrupoDt.AUTORIDADES_POLICIAIS:
				return MovimentacaoArquivoDt.ACESSO_SOMENTE_DELEGACIA_MP_CARTORIO_MAGISTRADO;
			case GrupoDt.MINISTERIO_PUBLICO:
			case GrupoDt.MP_TCE:
				return MovimentacaoArquivoDt.ACESSO_SOMENTE_MP_CARTORIO_MAGISTRADO;
			case GrupoDt.ANALISTAS_JUDICIARIOS_TURMA_RECURSAL:
			case GrupoDt.ANALISTA_CALCULO_EXECUCAO_PENAL:
			case GrupoDt.ANALISTA_CALCULO_PROCESSO_FISICO:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.ANALISTA_PRE_PROCESSUAL:
			case GrupoDt.ANALISTAS_EXECUCAO_PENAL:
			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case GrupoDt.ANALISTA_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case GrupoDt.ANALISTA_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			case GrupoDt.TECNICO_EXECUCAO_PENAL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CIVEL:
			case GrupoDt.TECNICOS_JUDICIARIOS_TURMA_RECURSAL:
			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CIVEL:
			case GrupoDt.TECNICO_JUDICIARIO_PRIMEIRO_GRAU_CRIMINAL:
			case GrupoDt.TECNICO_JUDICIARIO_SEGUNDO_GRAU_CRIMINAL:
			case GrupoDt.CONCILIADORES_VARA:
			case GrupoDt.DISTRIBUIDOR_CAMARA:			
			case GrupoDt.ASSISTENTE_GABINETE_FLUXO:
			case GrupoDt.ASSISTENTE_GABINETE:
			case GrupoDt.ESTAGIARIO_GABINETE:
				return MovimentacaoArquivoDt.ACESSO_SOMENTE_CARTORIO_MAGISTRADO;
			case GrupoDt.DESEMBARGADOR:			
			case GrupoDt.JUIZ_EXECUCAO_PENAL:
			case GrupoDt.JUIZES_TURMA_RECURSAL:
			case GrupoDt.JUIZES_VARA:
			case GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU: 
			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL:
			case GrupoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL:
			case GrupoDt.ASSESSOR:
			case GrupoDt.ASSESSOR_DESEMBARGADOR:
			case GrupoDt.ASSESSOR_JUIZES_SEGUNDO_GRAU:
			case GrupoDt.ASSESSOR_JUIZES_VARA:
			case GrupoDt.JUIZ_LEIGO:
			case GrupoDt.ASSESSOR_PRESIDENTE_SEGUNDO_GRAU:						
				 return MovimentacaoArquivoDt.ACESSO_SOMENTE_MAGISTRADO;			
		}
		
		 return MovimentacaoArquivoDt.ACESSO_NORMAL;
	}

	public String getId_Serventia() {
		if (obDados != null){
			return obDados.getId_Serventia();
		}  
		return null;
	}
	
	public boolean isGrupoCodigo(int[] valor){
		int grupoCodigo = getUsuarioDt().getGrupoCodigoToInt();
		for (int i=0;i<valor.length;i++){
			if (valor[i]==grupoCodigo)
				return true;
		}
		return false;
	}

	public String getGrupoCodigo() {
		if (obDados != null){
			return obDados.getGrupoCodigo();
		}  
		return null;
	}

	public int getGrupoCodigoToInt() {
		if (obDados != null){
			return obDados.getGrupoCodigoToInt();
		}  
		return 0;
	}
	
	public int getGrupoTipoCodigoToInt() {
		if (obDados != null){
			return obDados.getGrupoTipoCodigoToInt();
		}  
		return 0;
	}
	public boolean isUsuarioInterno() {
		if (obDados != null){
			return obDados.isUsuarioInterno();
		}  
		return false;
	}

	public boolean isMp() {
		if (obDados != null){
			return obDados.isMp();
		}  
		return false;
	}

	public boolean isPodeExibirPendenciaAssinatura(boolean multipla, int pendencia_tipo_codigo) {
		//nao pode guardar quando for multipla
		if (multipla){
			return false;
		}
		//se não tem o Pendenciatipocodigo, houve algum erro entao retorna false
		if (pendencia_tipo_codigo==-1){
			return false;
		}
		//voto náo pode ser guardado
		if (PendenciaTipoDt.CONCLUSO_VOTO == pendencia_tipo_codigo) {
			return false;
		}
		if (isMagistrado() || isMp() || isAdvogado()){
			return true;
		}
		//quando nao tiver permissao
		if (!getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao)){
			return false;
		}
		
		if (isPodeGuardarParaAssinar()){
			return true;
		}
		
		return false;
		
	}

	public boolean isPodeGuardarParaAssinar() {
		if (obDados!=null){
			return obDados.isPodeGuardarParaAssinar();
		}
		return false;
	}
	
	
	public boolean isAnalistaVara() {
		if (obDados != null){
			return obDados.isAnalistaVara();
		}
		return false;
	}
	
	public boolean isTecnicoJudiciario() {
		if (obDados != null){
			return obDados.isTecnicoJudiciario();
		}
		return false;
	}
	
	public boolean isAnalistaFinanceiro() {
		if (obDados != null){
			return obDados.isAnalistaFinanceiro();
		}
		return false;
	}
	
	public String getId_ServentiaCargo() {
		if (obDados != null){
			return obDados.getId_ServentiaCargo();
		}
		return "";
	}

	public boolean isMesmaServentia(String id_serv){
		if (obDados != null){
			return obDados.isMesmaServentia(id_serv);
		}
		return false;
	}
	
	public boolean isDesembargador() {
		if (obDados != null){
			return obDados.isDesembargador();
		}
		return false;
	}
	
	public boolean isDistribuidorGabinete() {
		if (obDados != null){
			return obDados.isDistribuidorGabinete();
		}
		return false;
	}
	
	public boolean isDistribuidorCamara() {
		if (obDados != null){
			return obDados.isDistribuidorCamara();
		}
		return false;
	}
	
	public boolean isAssistenteGabinete() {
		if (obDados != null){
			return obDados.isAssistenteGabinete();
		}
		return false;
	}
	
	public boolean isEstagiarioGabinete() {
		if (obDados != null){
			return obDados.isEstagiarioGabinete();
		}
		return false;
	}
	
	public boolean isDistribuidor() {
		if (obDados != null){
			return obDados.isDistribuidor();
		}
		return false;
	}
	
	public boolean isPresidenteSegundoGrau() {
		if (obDados != null){
			return obDados.isPresidenteSegundoGrau();
		}
		return false;
	}
	
	public boolean isPodeGerarPdfCompleto(){
		 return getVerificaPermissao(ProcessoDt.CodigoPermissaoPDF_COMPLETO);			 
	}
	
	public boolean isPodeGerarEvento(){
		return getVerificaPermissao(MovimentacaoProcessoDt.CodigoPermissaoSalvarEvento);
	}
	
	public boolean isPodeGerarPendencia(){
		return getVerificaPermissao(MovimentacaoProcessoDt.CodigoPermissaoGerarPendencias);
	}
	
//	public boolean isPodeBloquear(){
//			
//		if (grupoTipo == GrupoTipoDt.DESEMBARGADOR	|| grupoTipo == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU		&& (Processone.isResponsavelProcesso(usuarioSessao.getUsuarioDt().getId_ServentiaCargo(), id_Processo)))
//			{
//				return true;
//			} 
//			else if ((grupoTipo == GrupoTipoDt.JUIZ_VARA || grupoTipo == GrupoTipoDt.JUIZ_TURMA
//					|| grupoTipo == GrupoTipoDt.ANALISTA_VARA || grupoTipo == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU
//					|| grupoTipo == GrupoTipoDt.ANALISTA_EXECUCAO || grupoTipo == GrupoTipoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS) 
//					&& (usuarioSessao.getUsuarioDt().getId_Serventia().equalsIgnoreCase(id_ServentiaProcesso)) ) {
//				return true;
//			}
//			return false;
//	}
	
	public boolean isPodeVisualizarClassificador(){
		//Permissão para visualizar classificador: todos usuários internos podem visualizar
		if (isUsuarioInterno()) {
			return true;
		}
		return false;
	}
	
	public boolean isPodeGerarCodigoAcesso(String id_ServentiaProcesso){
		int grupoTipo = getUsuarioDt().getGrupoTipoCodigoToInt();
		
		if (getUsuarioDt().getId_Serventia().equalsIgnoreCase(id_ServentiaProcesso) && 
				(grupoTipo == GrupoTipoDt.ANALISTA_VARA|| 
				grupoTipo == GrupoTipoDt.TECNICO_VARA || 
				 grupoTipo == GrupoTipoDt.ANALISTA_EXECUCAO || 
				 grupoTipo == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU || 
				 grupoTipo == GrupoTipoDt.TECNICO_TURMA_SEGUNDO_GRAU)) {
				return true;
		}
		
		return false;
	}
	
	public boolean isPodeCarregarApplet(){
		int grupoTipo = getUsuarioDt().getGrupoTipoCodigoToInt();
		
		if 	(grupoTipo != GrupoTipoDt.ADVOGADO && 
			grupoTipo != GrupoTipoDt.PARTE && 
			grupoTipo != GrupoTipoDt.POPULACAO ){
	
				return true;
		}
		
		return false;
	}
	
	//public boolean isSubstitutoProcessoSegundoGrau(String id_Processo, String id_serventiaProcesso, String grupoCodigo, String id_serventiaCargo) throws Exception {
	public boolean isPodeBloquear(String id_Processo, String id_ServentiaProcesso) throws Exception{
		int grupoTipo = getUsuarioDt().getGrupoTipoCodigoToInt();
		
		if ( (grupoTipo == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU	|| grupoTipo == GrupoTipoDt.PRESIDENTE_SEGUNDO_GRAU)
			  && (
				    (new ProcessoNe()).isResponsavelProcesso(getUsuarioDt().getId_ServentiaCargo(), id_Processo)
				    || (new ProcessoNe()).isSubstitutoProcessoSegundoGrau(id_Processo, id_ServentiaProcesso, getUsuarioDt().getGrupoTipoCodigo(), getUsuarioDt().getId_ServentiaCargo()) 
				 )
		   ){
			return true;
		} else if ((grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || grupoTipo == GrupoTipoDt.JUIZ_TURMA
				|| grupoTipo == GrupoTipoDt.ANALISTA_VARA || grupoTipo == GrupoTipoDt.ANALISTA_TURMA_SEGUNDO_GRAU
				|| grupoTipo == GrupoTipoDt.ANALISTA_EXECUCAO || grupoTipo == GrupoTipoDt.DIVISAO_RECURSOS_CONSTITUCIONAIS) 
				&& (isMesmaServentia(id_ServentiaProcesso)
						|| new ProcessoNe().isResponsavelProcesso(getUsuarioDt().getId_ServentiaCargo(), id_Processo) ) ) {
			return true;
		}
		return false;
	}

	public boolean isAssessorDesembargador() {
		if (obDados != null){
			return obDados.isAssessorDesembargador();
		}
		return false;
	}
	
	public boolean isUsuarioGabinete() {
		int grupoTipo = getUsuarioDt().getGrupoTipoCodigoToInt();
		if ( grupoTipo== GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU || grupoTipo==GrupoTipoDt.ASSESSOR_DESEMBARGADOR 
				|| grupoTipo==GrupoTipoDt.ASSISTENTE_GABINETE || grupoTipo==GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO 
				|| grupoTipo==GrupoTipoDt.DISTRIBUIDOR_GABINETE){
			return true;
		}
		if(grupoTipo == GrupoTipoDt.ESTAGIARIO || grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU) {
			int grupo = getUsuarioDt().getGrupoCodigoToInt();
			if(grupo == GrupoDt.ESTAGIARIO_GABINETE || grupo == GrupoDt.MAGISTRADO_UPJ_PRIMEIRO_GRAU)  {
				return true;
			}
		}
		return false;
	}
	
	public List setHashIdProcesso(List listaProcessos) throws Exception {
		
		for(ProcessoDt processo: (List<ProcessoDt>) listaProcessos) {
			processo.setHash(getCodigoHash(processo.getId()));
		}
		
		return listaProcessos;
	}
	
	public boolean isAutoridadePolicial() {
		if (obDados != null)
			return obDados.isAutoridadePolicial();

		return false;
	}

	public boolean isAssessorMP() {
		if (obDados != null){
			return obDados.isAssessorMP();
		}
		return false;
	}

	public boolean isAssessor() {
		if (obDados != null){
			return obDados.isAssessor();
		}
		return false;
	}

	public boolean isAssessorAdvogado() {
		if (obDados != null){
			return obDados.isAssessorAdvogado();
		}
		return false;
	}

	public boolean isAssessorJuizVaraTurma() {
		if (obDados != null){
			return obDados.isAssessoJuizVaraTurma();
		}			
		return false;
	}

	public boolean isAssessorPresidenteSegundoGrau() {
		if (obDados != null){
			return obDados.isAssessorPresidenteSegundoGrau();
		}			
		return false;			
	}

	public boolean isCoordenadorOab() {
		if (obDados != null){
			return obDados.isCoordenadorOab();
		}			
		return false;		
	}
	
	public boolean isJuventudeInfracional() {
		if (obDados != null){
			return obDados.isJuventudeInfracional();
		}			
		return false;	
	}
	
	public boolean isJuizLeigo() {
		if (obDados != null){
			return obDados.isJuizLeigo();
		}			
		return false;		
	}
	
	public boolean isJuizUPJ() {
		if (obDados != null){
			return obDados.isJuizUPJ();
		}			
		return false;		
	}
	
	public boolean isOficial() {
		if (obDados != null){
			return obDados.isOficial();
		}			
		return false;		
	}

	public boolean isValidadoGooogle() {
		if (obDados != null){
			return obDados.isValidadoGooogle();
		}			
		return false;	
	}

	public void setGoogleOK() {		
		if (obDados != null){
			 obDados.setGoogleOK();
		}			
	}
	
	/**
	 * Método responsável em alterar o status Master de advogado/procurador
	 * @param dados, UsuarioDt para salvar o log
	 * @param statusMasterAtual, Status Master Atual
	 * @param statusMasterNovo, Novo status Master
	 * @param id_Usu_Serv_OAB, ID do usuário serventia oab que terá o status master alterado 
	 * 
	 * @throws Exception
	 */
	public void alterarStatusAdvovogadoMaster(UsuarioDt dados, String statusMasterAtual, int statusMasterNovo, String id_UsuarioServentia_Oab) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("USU_SERV_OAB", id_UsuarioServentia_Oab, dados.getId(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "statusMaster : "+Funcoes.BancoLogicoBoolean(statusMasterAtual),  "statusMaster : "+Funcoes.BancoLogicoBoolean(String.valueOf(statusMasterNovo)));
			obPersistencia.alterarStatusAdvovogadoMaster(id_UsuarioServentia_Oab, statusMasterNovo);

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Verificar Campos Obrigatórios para habilitação de um Advogado/Procurador por um escritório jurídico
	 * 
	 * @throws Exception
	 */
	public String VerificarAdvogadoHabilitacao(UsuarioDt dados) throws Exception {
		String stRetorno = "";

		if (dados.getCpf().equalsIgnoreCase(""))
			stRetorno += "O CPF é campo obrigatório. \n";
		if (dados.getOabNumero().equalsIgnoreCase(""))
			stRetorno += "Número da OAB é campo obrigatório. \n";
		if (dados.getOabComplemento().equalsIgnoreCase(""))
			stRetorno += "Complemento da OAB é campo obrigatório. \n";
		if (dados.getOabEstado().equalsIgnoreCase(""))
			stRetorno += "O UF da OAB é campo obrigatório. ";
		
		return stRetorno;
	}
	
	/**
	 * Método para consultar um advogado de acordo com dados da oab e cpf
	 * 
	 * @param oabNumero, número da OAB
	 * @param oabComplemento, complemento da OAB
	 * @param oabEstado, estado da OAB
	 * @param cpf, cpf do usuário
	 */
	public UsuarioDt consultarAdvogadoOabCPF(String oabNumero, String oabComplemento, String oabEstado, String cpf) throws Exception {
		FabricaConexao obFabricaConexao = null;
		UsuarioDt usuarioDt = null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			usuarioDt = obPersistencia.consultarAdvogadoOabCPF(oabNumero, oabComplemento, oabEstado, cpf);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return usuarioDt;
	}
	
	/**
	 * Método responsável em salvarr os dados da Serventia, Grupo e OAB de um Advogado/Procuradaro. Habilitação por usuários externos
	 * 
	 * @param advogadodt, objeto com dados do advogado
	 * @param gestor, objeto com dados do coordenador que realizará a habilitação
	 * 
	 */
	public void salvarHabilitacaoAdvogadoProcuradorServentia(UsuarioDt advogadodt, UsuarioDt gestor) throws Exception {
		UsuarioServentiaDt usuarioServentiaDt = null;
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = null;
		UsuarioServentiaOabDt usuarioServentiaOabDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LogDt logDt = new LogDt(gestor.getId(), gestor.getIpComputadorLog());
			
			// SALVA USUARIO SERVENTIA ********************************************************************************************
			usuarioServentiaDt = new UsuarioServentiaDt();
			usuarioServentiaDt.setId_Serventia(gestor.getId_Serventia());
			usuarioServentiaDt.setId_Usuario(advogadodt.getId());
			usuarioServentiaDt.setAtivo(String.valueOf(UsuarioServentiaDt.ATIVO));
			
			usuarioServentiaDt.setId_UsuarioLog(logDt.getId_Usuario());
			usuarioServentiaDt.setIpComputadorLog(logDt.getIpComputador());
			new UsuarioServentiaNe().salvar(usuarioServentiaDt, obFabricaConexao);

			// SALVA USUARIO SERVENTIA GRUPO *************************************************************************************
			ServentiaDt serventiDt = new ServentiaNe().consultarId(gestor.getId_Serventia(), obFabricaConexao);
			if (serventiDt == null || serventiDt.getServentiaTipoCodigo().length() == 0) {
				throw new MensagemException("Verifique o tipo de serventia do Usuário, não foi possivel pegar o seu tipo código");
			}
			int serventiaTipoCodigo = Funcoes.StringToInt(serventiDt.getServentiaTipoCodigo());
			String id_Grupo = new GrupoNe().consultarId_GrupoAdvogadoServentiaTipo(serventiaTipoCodigo, obFabricaConexao);
			
			usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
			usuarioServentiaGrupoDt.setId_UsuarioServentia(usuarioServentiaDt.getId());
			usuarioServentiaGrupoDt.setId_Grupo(id_Grupo);
			usuarioServentiaGrupoDt.setAtivo(String.valueOf(UsuarioServentiaGrupoDt.ATIVO));
			
			usuarioServentiaGrupoDt.setId_UsuarioLog(logDt.getId_Usuario());
			usuarioServentiaGrupoDt.setIpComputadorLog(logDt.getIpComputador());
			new UsuarioServentiaGrupoNe().salvar(usuarioServentiaGrupoDt, obFabricaConexao);

			// salva UsuarioServentiaOab*******************************************************************************************
			usuarioServentiaOabDt = new UsuarioServentiaOabDt();
			usuarioServentiaOabDt.setOabNumero(advogadodt.getOabNumero());
			usuarioServentiaOabDt.setOabComplemento(advogadodt.getOabComplemento());
			usuarioServentiaOabDt.setId_UsuarioServentia(usuarioServentiaDt.getId());
			
			usuarioServentiaOabDt.setId_UsuarioLog(logDt.getId_Usuario());
			usuarioServentiaOabDt.setIpComputadorLog(logDt.getIpComputador());
			new UsuarioServentiaOabNe().salvar(usuarioServentiaOabDt, obFabricaConexao);
			
			obFabricaConexao.finalizarTransacao();

		} catch (Exception e) {
			usuarioServentiaDt.setId("");
			usuarioServentiaGrupoDt.setId("");
			usuarioServentiaOabDt.setId("");
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarServentiaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp ="";
		ServentiaNe neObjeto = new ServentiaNe();
		stTemp = neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
		return stTemp;
	}
	
	/**
	 * Desativa um usuário em uma serventia
	 * 
	 * @param usuarioServentiaDt, objeto com dados para ativar ou desativar
	 * @param statusAtual, status atual do usuário serventia
	 * @param statusNovo, status novo do usuário serventia
	 * @param logTipo, tipo de log que será utilizado
	 * 
	 * 
	 * @author lsbernardes
	 */
	public void ativarDesativarUsuarioServentiaAdvogadoProcurador(UsuarioServentiaDt usuarioServentiaDt, int statusAtual, int statusNovo, int logTipo) throws Exception {
		 new UsuarioServentiaNe().ativarDesativarUsuarioServentiaAdvogadoProcurador(usuarioServentiaDt, statusAtual, statusNovo, logTipo);
   }
	
	/**
	 * Método responsável por verificar se um determinado advogado já está habilitado em uma Serventia.
	 * 
	 * @param id_Usuario, identificação do usuário
	 * @param id_Serventia, identificação da serventia
	 * @return true se já estiver habilitado e false se não estiver habilitado
	 * @throws Exception
	 * @author hmgodinho
	 */
	public boolean verificarHabilitacaoUsuarioEscritorioJuridico(String id_Usuario, String id_Serventia) throws Exception {
		boolean retorno;

		UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
		UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoDt();
		usuarioServentiaGrupoDt.setId_Serventia(id_Serventia);
		usuarioServentiaGrupoDt.setId_Usuario(id_Usuario);

		retorno = usuarioServentiaGrupoNe.verificarHabilitacaoAdvogado(usuarioServentiaGrupoDt);

		return retorno;
	}
	

	/**
     * Consulta pendencias do tipo Intimação ou Citação de um procurador/advogado/defensor da serventia do usuário logado
	 * 
	 * @author lsbernardes
	 * @param id_UsuarioServentia, identificação do usuario para verificar se existe pendências
	 * @return int, quantidade de pendências encontradas
	 * @throws Exception
	 */
	public int possuiIntimacoesCitacoesAbertasUsuario(String id_UsuarioServentia ) throws Exception {
		if (id_UsuarioServentia != null && id_UsuarioServentia.length()>0){
			return new PendenciaNe().possuiIntimacoesCitacoesAbertasUsuario(id_UsuarioServentia);
		} else {
			return 0;
		}
   }
	
	/**
	 * Método que consulta o nome do usuário pelo identiticação do usuário serventia dele.
	 * 
	 * @param id_Usu_Serv - ID do usuário serventia
	 * @return nome do usuário
	 * @throws Exception
	 * @author lsbernardes
	 */
	public String consultarNomeUsuario(String id_Usu_Serv, FabricaConexao obFabricaConexao) throws Exception {
		String nome = null;
		UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
		nome = obPersistencia.consultarNomeUsuario(id_Usu_Serv);
		return nome;
	}
	
	public boolean isInteligenciaN0() {
		if (obDados != null){
			return obDados.isInteligenciaN0();
		}			
		return false;	
	}

	public boolean isDuploLogin() {
		return boDuploLogin;
	}

	public void validadeDuploLogin(boolean b) {
		boDuploLogin = false;
	}

	public void temDuplaAutentificacao() throws Exception {
		UsuarioFoneNe usu = new UsuarioFoneNe();
		if( usu.temDuplaAutentificacao(getId_Usuario())) {
			boDuploLogin = true;
		}else{
			boDuploLogin = false;
		}		
	}
	
	public boolean isInteligenciaN1() {
		if (obDados != null){
			return obDados.isInteligenciaN1();
		}			
		return false;	
	}
	
	public boolean isInteligenciaN2() {
		if (obDados != null){
			return obDados.isInteligenciaN2();
		}			
		return false;	
	}
	
	public boolean isInteligenciaN3() {
		if (obDados != null){
			return obDados.isInteligenciaN3();
		}			
		return false;	
	}
	
	public boolean isInteligenciaN4() {
		if (obDados != null){
			return obDados.isInteligenciaN4();
		}			
		return false;	
	}
	
	// jvosantos - 15/10/2019 16:32 - Criar get que estava faltando
	public String getId_ServentiaCargoUsuarioChefe() {
		return getUsuarioDt().getId_ServentiaCargoUsuarioChefe();
	}
	
	public boolean isHabilitadoConsultarHistoricoConclusao() {
		if (isDistribuidorGabinete() && isGabinetePresidenciaTjgo()) {
			return true;
		}
//		if(Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getGrupoCodigo()) == GrupoDt.DISTRIBUIDOR_GABINETE
//				&&(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo() != null
//					&& Funcoes.StringToInt(usuarioSessao.getUsuarioDt().getServentiaSubtipoCodigo()) == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO)){
//			request.setAttribute("HabilitadoConsultarHistoricoConclusao", "true");
//		}
		
		return false;
	}

	public boolean isGabinetePresidenciaTjgo() {
		return obDados.isGabinetePresidenciaTjgo();
	}

	public boolean isGabineteVicePresidenciaTjgo() {
		return obDados.isGabineteVicePresidenciaTjgo();
	}

	public boolean isGabineteUpj() {
		return obDados.isGabineteUPJ();
	}
	
	public boolean isServentiaUpjFamilia() {
		return obDados.isServentiaUPJFamilia();
	}
	
	public boolean isServentiaUpjSucessoes() {
		return obDados.isServentiaUPJSucessoes();
	}
	
	public boolean isServentiaUpjCriminal() {
		return obDados.isServentiaUPJCriminal();
	}

	public boolean isAssistenteGabineteComFluxo() {
		return obDados.isAssistenteGabineteComFluxo();
	}

	public boolean isGabinete() {
		return obDados.isGabinete();
	}

	public String getServentiaSubTipoCodigo() {
		if (obDados != null){
			return obDados.getServentiaSubtipoCodigo();
		}
		return null;
	}
	
	public boolean isPodeAnalisar() {
		return getVerificaPermissao(AnaliseConclusaoDt.CodigoPermissao);
	}
	public boolean isPodePreAnalisar() {
		return getVerificaPermissao(PreAnaliseConclusaoDt.CodigoPermissao);
	}

	public String getId_ServentiaSubtipo() {
		if (obDados != null){
			return obDados.getId_ServentiaSubtipo();
		}
		return null;
	}

	public boolean isCertificadoCarregado() {
		if (obDados != null){
			return obDados.isCertificadoCarregado();
		}
		return false;	
	}

	public void carregarCertificado() throws Exception {
		CertificadoNe cert = new CertificadoNe();
		FabricaConexao obFabricaConexao = null;
		CertificadoDt certDt= null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			
			certDt = cert.consultarCertificadoUsuario(obDados.getId(), obFabricaConexao);					
		} finally {
			obFabricaConexao.fecharConexao();
		}
		if (certDt==null) {
			throw new MensagemException("Não foi possível encontrar um certificado válido");
		}
		obDados.setCertificado(certDt);
	}

	public boolean isSenhaCertificado() {
		if (obDados != null){
			return obDados.isSenhaCertificado();
		}
		return false;	
	}

	public void setSenhaCertificado(String senhaCertificado) {
		obDados.setSenhaCertificado(senhaCertificado);		
	}
	
	public String getSenhaCertificado() {
		return obDados.getSenhaCertificado();
	}
	
	public void assinarByte(ArquivoDt dtArquivo) throws Exception {
		ByteArrayInputStream pkcs12 = new ByteArrayInputStream(obDados.getCertificadoConteudo());
		PKCS12Parser p12parser;
		try {
			 p12parser = new PKCS12Parser(pkcs12, obDados.getSenhaCertificado());
		} catch (Exception e) {
			// Se não conseguir carregar o certificado lança exceção e limpa a senha
			this.setSenhaCertificado("");
			throw e;
		}
	    Signer.signBuffer(dtArquivo, p12parser.getCertificate(), p12parser.getPrivateKey(), p12parser.getCertificateChain());
	    dtArquivo.setAssinado(true);
	}
	
	public String getTipoArquivoUpload() {
		if (isAdvogadoParticular()) {
			return ".p7s";
		}
		return ".p7s,.pdf,.mp3";
	}
	
	public String getTipoArquivoMidiaPublicada() {
		return ProjudiPropriedades.getInstance().getObjectStorageUploadTipoArquivo();
	}
	
	public boolean getTipoEnvioMidiaUploadJavaScript() {
		return ProjudiPropriedades.getInstance().isObjectStorageUploadEnvioJavascript();
	}
	
	public int getTamanhoMBArquivoMidiaPublicada() {
		return ProjudiPropriedades.getInstance().getObjectStorageUploadTamanhoMaximo();
	}
	
	public int getQuantidadeMaximaArquivosMidiaPublicada() {
		return ProjudiPropriedades.getInstance().getObjectStorageUploadQuantidadeMaxima();
	}
	
	public boolean getObjectStorageUploadVerboPUT() {
		return ProjudiPropriedades.getInstance().isObjectStorageUploadVerboPUT();
	}
	
	public boolean getObjectStorageUploadForceIframeTransport() {
		return ProjudiPropriedades.getInstance().isObjectStorageUploadForceIframeTransport();
	}
	
	public int getTamanhoMBParteArquivoMidiaPublicada() {
		return ProjudiPropriedades.getInstance().getObjectStorageUploadTamanhoParte();
	}
	
	public boolean isCoordenadorHabilitacaoMp() {		
		if (obDados != null){
			return obDados.isCoordenadorHabilitacaoMp();
		}			
		return false;		
	}
	
	public boolean isCoordenadorHabilitacaoSsp() {		
		if (obDados != null){
			return obDados.isCoordenadorHabilitacaoSsp();
		}			
		return false;		
	}

	public List consultarTodosPoliciais(String nome, String usuario, String posicao) throws Exception {
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarTodosPoliciais(nome, usuario, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	public String consultarTodosPoliciaisJSON(String nome, String usuario, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarTodosPoliciaisJSON(nome, usuario, posicao);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public boolean isLoginToken() {	
		return LoginToken;
	}
	
	public void setLoginToken() {
		LoginToken= true;
	}
	
	public boolean isPodePeticionar() {
		return getVerificaPermissao(PeticionamentoDt.CodigoPermissao);
	}
	
	public boolean isPodeTrocarResponsavel() {
		return getVerificaPermissao(ProcessoResponsavelDt.CodigoPermissao);		
	}
	
	public boolean isPodeTrocarResponsavelDistribuicao(){
		boolean retorno = false;
		
		int servSubTipoCodigo = Funcoes.StringToInt(getServentiaSubTipoCodigo());
		if ((servSubTipoCodigo == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ || servSubTipoCodigo == ServentiaSubtipoDt.GABINETE_PRESIDENCIA_TJGO || servSubTipoCodigo == ServentiaSubtipoDt.GABINETE_VICE_PRESIDENCIA_TJGO ) &&
				(getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE || getGrupoTipoCodigoToInt() == GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO || 
					getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU ||  getGrupoTipoCodigoToInt() == GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU)
			){
			retorno = true;
		}
		
		return retorno;
	}
	
//	public boolean isAcessoPredatorio() {
//		// TODO Auto-generated method stub
//		return (System.currentTimeMillis() - obDados.getTimeaUlitmoAcesso())<100;
//	}
 	
 	public UsuarioDt consultarId(String id_usuario, FabricaConexao obFabricaConexao) throws Exception {
		UsuarioDt dtRetorno=null;
		UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_usuario ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
	
 	private void configurarHashWebServiceMNI() {
 		stVerificadorWebServiceMNI = getId_Usuario();
 		stVerificadorWebServiceMNI += getCpfUsuario();
 		stVerificadorWebServiceMNI += "WEBSERVICEMNIPROJUDITJGO";
	}
 	
 	public String getCodigoHashWebServiceMNI(String codigo) throws Exception {
		String stTemp;
		stTemp = Funcoes.SenhaMd5(stVerificadorWebServiceMNI + codigo);
		return stTemp;
	}

	public boolean VerificarCodigoHashWebServiceMNI(String codigo, String hash) {
		boolean boRetorno = false;
		try {
			String stTemp = Funcoes.SenhaMd5(stVerificadorWebServiceMNI + codigo);
			if (stTemp.equals(hash))
				boRetorno = true;
		} catch (Exception e) {
			return false;
		}

		return boRetorno;
	}
	
	public boolean VerificarCodigoHashWebService(String codigo, String hash) {
		if (VerificarCodigoHashWebServiceMNI(codigo, hash)) return true;
		return VerificarCodigoHash(codigo, hash);
	}

	public String consultarOficialJusticaJSON(String nome, String posicao, String idServ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioPs obPersistencia = new UsuarioPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarOficialJusticaJSON(nome, posicao, idServ);

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public boolean isSegundoGrau() {		
		if (obDados != null){
			return obDados.isSegundoGrau();
		}			
		return false;		
	}
	
	public boolean isGabineteSegundoGrau() {		
		if (obDados != null){
			return obDados.isGabineteSegundoGrau();
		}			
		return false;		
	}
	
	public boolean isUPJSegundoGrau() {		
		if (obDados != null){
			return obDados.isUPJSegundoGrau();
		}			
		return false;		
	}
	
	public boolean isTurmaJulgadora() {		
		if (obDados != null){
			return obDados.isTurmaJulgadora();
		}			
		return false;		
	}	
	
	public boolean isJuizTurma() {		
		if (obDados != null){
			return obDados.isJuizTurma();
		}			
		return false;		
	}	
	
	public boolean isGabineteFluxoUPJ(){
		if (obDados != null){
			return obDados.isGabineteUPJ();
		}			
		return false;
	}
	
	/**
	 * Consultar serventias e grupos vinculados a um usuário e devolver uma lista com esses dados. Webservices
	 * Chama UsuarioServentiaGrupoNe para realizar essa consulta
	 * @author asrocha
	 */
	@SuppressWarnings("unchecked")
	public String consultarServentiasGruposJSON() throws Exception {
		JSONArray aJson = new JSONArray();
		JSONObject oJson = null;
		UsuarioDt usuarioDt = null;
		
		if (listaServentiasGruposUsuario == null) {
			UsuarioServentiaGrupoNe usuarioServentiaGrupoNe = new UsuarioServentiaGrupoNe();
			listaServentiasGruposUsuario = usuarioServentiaGrupoNe.consultarServentiasGrupos(getId_Usuario());
		}
		
		for (int i = 0; i < listaServentiasGruposUsuario.size(); i++) {
			oJson = new JSONObject();
			usuarioDt = (UsuarioDt) listaServentiasGruposUsuario.get(i);
			oJson.put("idServentia", usuarioDt.getId_Serventia());
			oJson.put("descricaoServentia", usuarioDt.getServentia());
			oJson.put("serventiaCodigo", usuarioDt.getServentiaCodigo());
			oJson.put("grupoCodigo", usuarioDt.getGrupoCodigo());
			oJson.put("descricaoGrupo", usuarioDt.getGrupo());
			oJson.put("idServentiaCargo", usuarioDt.getId_ServentiaCargo());
			aJson.put(oJson);
		}
		
		return aJson.toString();
	}
	
	public void definirServentiaGrupo(String id_serventia, String grupoCodigo, String id_serventiaCargo) throws Exception {

		// Percorrendo lista de serventias/grupos para encontrar objeto correspondente a serventia/grupo escolhido		
		for (int i = 0; i < listaServentiasGruposUsuario.size(); i++) {
			obDados = ((UsuarioDt) listaServentiasGruposUsuario.get(i));
																												 /// se o id_serventiaCargo == null deve-se ignorar
			if (obDados.getGrupoCodigo().equals(grupoCodigo) && obDados.getId_Serventia().equals(id_serventia) && (id_serventiaCargo.isEmpty() || obDados.getId_ServentiaCargo().equals(id_serventiaCargo))) {
				break;
			}
			obDados = null;
		}		
	}
	public boolean isDistribuidor1Grau() {
		if (obDados != null){
			return obDados.isDistribuidor1Grau();
		}
		return false;
	}
	public boolean isDistribuidor2Grau() {
		if (obDados != null){
			return obDados.isDistribuidor2Grau();
		}
		return false;
	}

	public boolean isQualquerAssessor() {
		if (obDados != null){
			return obDados.isQualquerAssessor();
		}
		return false;
	}
}
