package br.gov.go.tj.projudi.ne;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaRelacionadaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.ps.ServentiaCargoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ServentiaCargoNe extends ServentiaCargoNeGen {

	private static final long serialVersionUID = -4398062608845383639L;

	/**
	 * Sobrescrevendo método salvar para tratamento se cargo está vazio
	 */
	public void salvar(ServentiaCargoDt dados) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			if (!dados.getQuantidadeDistribuicao().equalsIgnoreCase(obDados.getQuantidadeDistribuicao())) {
			}

			// Se não tem usuário, é porque cargo está vazio
			if (dados.getId_UsuarioServentiaGrupo().length() == 0) dados.setCodigoTemp(String.valueOf(ServentiaCargoDt.VAZIO));
			else dados.setCodigoTemp(String.valueOf(ServentiaCargoDt.OCUPADO));

			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();

//			// -----------CONTROLE DE
//			// DISTRIBUIÇÃO--------------------------------------------------------------------
//
//			// Verifico se está sendo criado cargos que podem particiar das
//			// distribuições
//			if ((Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.JUIZ_1_GRAU) || (Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.PROMOTOR_JUIZADO) || (Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.RELATOR_TURMA)) {
//				// verifico se o cargo tem valor na quantidade de distribuição
//				if (boQuantidadeAlterada) DistribuicaoProcessoServentiaCargo.getInstance().lerDados(dados.getId_Serventia());
//			} else if (Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.CONCILIADOR_JUIZADO) {
//				// verifico se o cargo tem valor na quantidade de distribuição
//				//if (boQuantidadeAlterada) DistribuicaoProcessoConciliador.getInstance().lerDados(dados.getId_Serventia());
//			}
			// -------------------------------------------------------------------------------

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	public void excluir(ServentiaCargoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir), dados.getPropriedades(), "");
			obPersistencia.excluir(dados.getId());

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

//			// -----------CONTROLE DE DISTRIBUIÇÃO--------------------------------------------------------------------
//			// verifico se o cargo tem valor na quantidade de distribuição
//			if (Funcoes.StringToInt(dados.getQuantidadeDistribuicao()) > 0)
//			// Verifico se está sendo criado cargos que podem particiar das
//				// distribuições
//				if ((Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.JUIZ_1_GRAU) || (Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.PROMOTOR_JUIZADO) || (Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.RELATOR_TURMA)) {
//					DistribuicaoProcessoServentiaCargo.getInstance().lerDados(dados.getId_Serventia());
//				} else if (Funcoes.StringToInt(dados.getCargoTipoCodigo()) == CargoTipoDt.CONCILIADOR_JUIZADO) {
//					//DistribuicaoProcessoConciliador.getInstance().lerDados(dados.getId_Serventia());
//				}
//			// -------------------------------------------------------------------------------

			dados.limpar();

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método responsável por criar uma estrutura padrão de cargos dentro de uma serventia.
	 * @param idServentia - ID da serventia 
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void salvarEstruturaPadrao(ServentiaCargoDt serventiaCargoDt) throws Exception {
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
			
			ServentiaNe serventiaNe = new ServentiaNe();
			ServentiaDt serventiaDt = serventiaNe.consultarId(serventiaCargoDt.getId_Serventia());
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CIVEL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.VARAS_CIVEL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.AUDITORIA_MILITAR_CIVEL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_EXECUACAO_FISCAL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_ESTADUAL_INTERIOR)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_ESTADUAL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_EXECUACAO_FISCAL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAZENDA_PUBLICA_MUNICIPAL_INTERIOR)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_FAZENDA_PUBLICA)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAMILIA_CAPITAL)) ||
					serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.FAMILIA_INTERIOR)) ||
					serventiaDt.isUPJs()) {
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário " + i, CargoTipoDt.ANALISTA_JUDICIARIO_VARA, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);				
				}
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Técnico Judiciário " + i, CargoTipoDt.TECNICO_JUDICIARIO_VARA, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Pré-Processual " + i, CargoTipoDt.ANALISTA_PRE_PROCESSUAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Conciliador ", CargoTipoDt.CONCILIADOR_JUIZADO, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				if(!serventiaDt.isUPJs()) {
					this.criarServentiaCargo(serventiaDt.getId(), "Juiz ", CargoTipoDt.JUIZ_1_GRAU, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
					this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto ", CargoTipoDt.JUIZ_1_GRAU, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.PLANTAO_PRIMEIRO_GRAU)) 
					|| serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.AUDITORIA_MILITAR_CRIMINAL))
					|| serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.PLANTAO_AUDIENCIA_CUSTODIA))) {
				for (int i = 1; i <= 2; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário 1º grau - Plantonista " + i, CargoTipoDt.ANALISTA_JUDICIARIO_VARA_CRIMINAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);				
				}
				for (int i = 1; i <= 2; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário 1º grau - Plantonista " + i, CargoTipoDt.ANALISTA_JUDICIARIO_VARA, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz 1º grau - Plantonista", CargoTipoDt.JUIZ_1_GRAU, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto 1º grau - Plantonista", CargoTipoDt.JUIZ_1_GRAU, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.JUIZADO_ESPECIAL_CRIMINAL))
					|| serventiaDt.isUpjCriminal()) {
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário " + i, CargoTipoDt.ANALISTA_JUDICIARIO_VARA_CRIMINAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);				
				}
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Técnico Judiciário " + i, CargoTipoDt.TECNICO_JUDICIARIO_VARA_CRIMINAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Pré-Processual " + i, CargoTipoDt.ANALISTA_PRE_PROCESSUAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Conciliador ", CargoTipoDt.CONCILIADOR_JUIZADO, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				if(!serventiaDt.isUpjCriminal()){ 
					this.criarServentiaCargo(serventiaDt.getId(), "Juiz ", CargoTipoDt.JUIZ_1_GRAU, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
					this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto ", CargoTipoDt.JUIZ_1_GRAU, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.VARA_CRIMINAL))) {
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário " + i, CargoTipoDt.ANALISTA_JUDICIARIO_VARA_CRIMINAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);				
				}
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Técnico Judiciário " + i, CargoTipoDt.TECNICO_JUDICIARIO_VARA_CRIMINAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz ", CargoTipoDt.JUIZ_1_GRAU, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto ", CargoTipoDt.JUIZ_1_GRAU, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_CIVEL))) {
				for (int i = 1; i <= 9; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário Inf e Juventude - Cível " + i, CargoTipoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_CIVEL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);				
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz da Inf e Juventude - Cível", CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto da Inf e Juventude - Cível", CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_CIVEL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.INFANCIA_JUVENTUDE_INFRACIONAL))) {
				for (int i = 1; i <= 3; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Judiciário Inf e Juventude - Infracional " + i, CargoTipoDt.ANALISTA_JUDICIARIO_INFANCIA_JUVENTUDE_INFRACIONAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);				
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz da Inf e Juventude - Infracional", CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto da Inf e Juventude - Infracional", CargoTipoDt.JUIZ_INFANCIA_JUVENTUDE_INFRACIONAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.PREPROCESSUAL))) {
				for (int i = 1; i <= 5; i++) {
					this.criarServentiaCargo(serventiaDt.getId(), "Analista Pré-Processual " + i, CargoTipoDt.ANALISTA_PRE_PROCESSUAL, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				}
				this.criarServentiaCargo(serventiaDt.getId(), "Conciliador ", CargoTipoDt.CONCILIADOR_JUIZADO, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz ", CargoTipoDt.JUIZ_1_GRAU, 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Juiz Substituto ", CargoTipoDt.JUIZ_1_GRAU, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
			}
			
			if(serventiaDt.getServentiaSubtipoCodigo().equals(String.valueOf(ServentiaSubtipoDt.MP_PRIMEIRO_GRAU))) {
				this.criarServentiaCargo(serventiaDt.getId(), "Promotor ", CargoTipoDt.MINISTERIO_PUBLICO , 1, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Promotor Substituto ", CargoTipoDt.MINISTERIO_PUBLICO , 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
				this.criarServentiaCargo(serventiaDt.getId(), "Coordenador de Promotoria ", CargoTipoDt.COORDENADOR_PROMOTORIA, 0, 20, serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), obPersistencia, obFabricaConexao);		
			}

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método que preenche um Serventia Cargo e faz sua inserção no sistema. É usado pelo método salvarEstruturaPadrao desta classe. Esse método foi criado para simplificar o código e facilitar
	 * a manutenção caso haja necessidade futura.
	 * @param idServentia - ID da serventia 
	 * @param titulo - descrição do cargo
	 * @param cargoTipoCodigo - código do cargo tipo
	 * @param quantidadeDistribuicao - quantidade de distribuição para o cargo
	 * @param prazoAgenda - prazo da agenda do cargo
	 * @param idUsuarioLog - ID do usuário logado
	 * @param ipComputadorLog - IP do computador do usuário logado
	 * @param obPersistencia - persistencia
	 * @param obFabricaConexao - conexao
	 * @throws Exception
	 * @author hmgodinho
	 */
	private void criarServentiaCargo(String idServentia, String titulo, int cargoTipoCodigo, int quantidadeDistribuicao, int prazoAgenda, String idUsuarioLog, String ipComputadorLog, ServentiaCargoPs obPersistencia, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt serventiaCargoDt = new ServentiaCargoDt();
		CargoTipoNe cargoTipoNe = new CargoTipoNe();
		try{
			serventiaCargoDt.setId("");
			serventiaCargoDt.setId_Serventia(idServentia);
			serventiaCargoDt.setServentiaCargo(titulo);
			serventiaCargoDt.setId_CargoTipo(cargoTipoNe.consultarIdCargoTipoCodigo(cargoTipoCodigo));
			serventiaCargoDt.setCargoTipoCodigo(String.valueOf(cargoTipoCodigo));
			serventiaCargoDt.setQuantidadeDistribuicao(String.valueOf(quantidadeDistribuicao));
			serventiaCargoDt.setCodigoTemp(String.valueOf(ServentiaCargoDt.VAZIO));
			serventiaCargoDt.setPrazoAgenda(String.valueOf(prazoAgenda));
			
			obPersistencia.inserir(serventiaCargoDt);
			LogDt obLogDt = new LogDt("ServentiaCargo", serventiaCargoDt.getId(), idUsuarioLog, ipComputadorLog, String.valueOf(LogTipoDt.Incluir), "", serventiaCargoDt.getPropriedades());
			obLog.salvar(obLogDt, obFabricaConexao);
		} catch(Exception e) {
			throw e;
		}
	}
	
	/**
	 * Consulta todos os serventia cargos de um usuario da serventia
	 * 
	 * @author Ronneesley Moura Teles
	 * @since 07/11/2008 15:16
	 * @param String
	 *            idUsuarioServentia, id do usuario da serventia
	 * @return List
	 * @throws Exception
	 */
	public List consultarServentiasCargos(String idUsuarioServentia) throws Exception {
		List lista = null;
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			lista = obPersistencia.consultarServentiasCargos(idUsuarioServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return lista;
	}

	/**
	 * Método responsável por validar os dados informados pelo usuário no
	 * cadastro de cargos de uma serventia
	 * 
	 * @param serventiaCargoDt
	 * @return String mensagemRetorno
	 * @throws Exception 
	 */
	public String Verificar(ServentiaCargoDt serventiaCargoDt) throws Exception{
		String mensagemRetorno = "";

		if (serventiaCargoDt.getServentiaCargo().length() == 0) mensagemRetorno += "O campo Descrição é obrigatório. \n";
		if (serventiaCargoDt.getId_Serventia().length() == 0) mensagemRetorno += "O campo Serventia é obrigatório. \n";
		if (serventiaCargoDt.getId_CargoTipo().length() == 0) mensagemRetorno += "O campo Tipo de Cargo é obrigatório. \n";
		
		if (serventiaCargoDt.isSubstituicao()){
			serventiaCargoDt.setQuantidadeDistribuicao("0");
			if (serventiaCargoDt.getId_ServentiaSubtipo() == null ||serventiaCargoDt.getId_ServentiaSubtipo().length() == 0) 
				mensagemRetorno += "A Serventia Sub-Tipo é obrigatória. \n";
			if (serventiaCargoDt.getDataInicialSubstituicao()==null || serventiaCargoDt.getDataInicialSubstituicao().length() == 0) 
				mensagemRetorno += "A data Inicial do período é obrigatória. \n";
			if (serventiaCargoDt.getDataFinalSubstituicao() ==null || serventiaCargoDt.getDataFinalSubstituicao().length() == 0)
				mensagemRetorno += "A data Final do período é obrigatória. \n";
			
			if (serventiaCargoDt.getDataInicialSubstituicao()!=null && serventiaCargoDt.getDataInicialSubstituicao().length() > 0
					&& serventiaCargoDt.getDataFinalSubstituicao()!=null && serventiaCargoDt.getDataFinalSubstituicao().length() > 0){
				SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");  
			    Date dataInicial = format.parse(serventiaCargoDt.getDataInicialSubstituicao());
			    Date dataFinal = format.parse(serventiaCargoDt.getDataFinalSubstituicao()); 
			      
			    if (dataInicial.after(dataFinal)) { 
			    	mensagemRetorno += "A Data Inicial do Período deve ser Menor ou Igual a Data Final. \n";
			    }				
			}
			
		} else{
			if (serventiaCargoDt.getQuantidadeDistribuicao().length() == 0) mensagemRetorno += "O campo Quantidade de Distribuição é obrigatório.";
			serventiaCargoDt.setDataInicialSubstituicao("");
			serventiaCargoDt.setDataFinalSubstituicao("");
			serventiaCargoDt.setId_ServentiaSubtipo("null");
			serventiaCargoDt.setServentiaSubtipo("");
			serventiaCargoDt.setServentiaSubtipoCodigo("");
		}
		
		if (serventiaCargoDt.getCargoTipoCodigo() != null && serventiaCargoDt.getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.DESEMBARGADOR)) && Funcoes.StringToInt(serventiaCargoDt.getQuantidadeDistribuicao()) > 0){
			List tempList = this.consultarServentiaCargos(serventiaCargoDt.getId_Serventia());
			if (tempList != null){
				ServentiaCargoDt objTemp;					
				for(int i = 0 ; i< tempList.size();i++) {
					objTemp = (ServentiaCargoDt)tempList.get(i);
					if (serventiaCargoDt.getId() != null && objTemp.getId() != null && !serventiaCargoDt.getId().equalsIgnoreCase(objTemp.getId()) && objTemp.getCargoTipoCodigo() != null && objTemp.getCargoTipoCodigo().equalsIgnoreCase(String.valueOf(CargoTipoDt.DESEMBARGADOR)) && Funcoes.StringToInt(objTemp.getQuantidadeDistribuicao()) > 0){
						mensagemRetorno += "\nNa serventia selecionada já existe um cargo do tipo desembargador com quantidade de distribuição diferente de zero.";
					}
				}
			}
		}

		return mensagemRetorno;
	}

	/**
	 * Método responsável por validar os dados informados em uma habilitação de
	 * usuário em um cargo
	 * 
	 * @param serventiaCargoDt
	 * @return String mensagemRetorno
	 */
	public String verificarHabilitacaoCargo(ServentiaCargoDt serventiaCargoDt) {
		String mensagemRetorno = "";

		if (serventiaCargoDt.getId_Serventia().length() == 0) mensagemRetorno += "Serventia é campo obrigatório. \n";
		if (serventiaCargoDt.getId_CargoTipo().length() == 0) mensagemRetorno += "Tipo de Cargo é campo obrigatório. \n";
		if (serventiaCargoDt.getId().length() == 0) mensagemRetorno += "Cargo é campo obrigatório. \n";
		if (serventiaCargoDt.getId_UsuarioServentiaGrupo().length() == 0) mensagemRetorno += "Usuário é é obrigatório.";

		return mensagemRetorno;
	}

	/**
	 * Método responsável por validar se um ServentiaCargo pode ser desabilitado
	 * (retirada de usuário) ou excluído. Um cargo só poderá ser limpado ou
	 * excluído se não for responsável por processos, pendências ou audiências.
	 * 
	 * @param serventiaCargoDt
	 * @param tipoAcao - indica se está limpando ou excluindo o cargo. Valores aceitáveis: E (para excluir o cargo) ou L (para limpar o cargo).
	 * @return String mensagemRetorno
	 */
	public String verificarDesabilitacaoCargo(ServentiaCargoDt serventiaCargoDt, String tipoAcao) throws Exception {
		String stRetorno = "";
		PendenciaNe pendenciaNe = new PendenciaNe();
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			int grupoTipo = Funcoes.StringToInt(serventiaCargoDt.getGrupoTipoUsuarioCodigo());

			// Verifica se há conclusões em aberto
			if (grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || grupoTipo == GrupoTipoDt.JUIZ_TURMA) {
				if (pendenciaNe.verificaConclusoesAbertasServentiaCargo(serventiaCargoDt.getId(), obFabricaConexao)) stRetorno = "Há Conclusões em aberto para esse Cargo. \n";
			}

			// Verifica se há audiências pendentes
			if (grupoTipo == GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU || grupoTipo == GrupoTipoDt.CONCILIADOR_VARA) {
				if (new AudienciaProcessoNe().verificaAudienciasPendentesServentiaCargo(serventiaCargoDt.getId(), obFabricaConexao)) stRetorno += "Há Audiências em aberto para esse Cargo. \n";
			}

			// Verifica se há pendências ativas para o ServentiaCargo que será excluído
			if (pendenciaNe.verificaPendenciasAtivasServentiaCargo(serventiaCargoDt.getId(), obFabricaConexao)) {
				stRetorno += "Há pendência(s) em aberto para esse Cargo. \n";
			}

			//Antes de excluir o cargo, deve-se verificar.
			if(tipoAcao != null && tipoAcao.equalsIgnoreCase("E")){
				// Verifica se Cargo é responsável por um processo
				if (new ProcessoNe().verificaProcessosServentiaCargo(serventiaCargoDt.getId(), obFabricaConexao)) {
					stRetorno += "Esse Cargo é responsável por processo(s). \n";
				}
			  
				//Verifica se há registro no ponteiro_log para o cargo
				PonteiroLogNe ponteiroLogNe = new PonteiroLogNe();
				if(ponteiroLogNe.haDistribuicaoServentiaCargo(serventiaCargoDt.getId())){
					stRetorno += "Não é possível excluir o cargo, pois há registro de distribuição de processos/responsabilidade para o mesmo. Excluir o cargo afetará a consistência dos relatórios de distribuição.";
				}
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Método que retorna o Id_ServentiaCargo para um usuário de determinada
	 * serventia passado
	 * 
	 * @param id_UsuarioServentia,
	 *            id do usuário em uma serventia
	 * @return id_ServentiaCargo, serventia cargo ocupado pelo usuário na
	 *         serventia
	 * @throws Exception
	 */
	public String consultarServentiaCargo(String id_UsuarioServentia) throws Exception {
		String stRetorno = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarServentiaCargo(id_UsuarioServentia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	public String consultarCargosServentiaJSON(String idServentia, String descricao, String posicao) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs( obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarCargosServentiaJSON(idServentia, descricao, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	
	/**
	 * Consulta todos os cargos cadastrados para uma determinada serventia.
	 * Retorna tanto os cargos ocupados quanto os vazios, e se for um
	 * administrador deverá retornar os cargos de todas serventias
	 * 
	 * @param id_serventia,
	 *            identificação da serventia
	 * @param grupoCodigo,
	 *            grupo do usuário que está consultando os cargos
	 * @author msapaula
	 */
	public List consultarServentiaCargos(UsuarioNe usuarioNe) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao =null;
		try{
			//int grupo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoCodigo());
			int grupoTipo = Funcoes.StringToInt(usuarioNe.getUsuarioDt().getGrupoTipoCodigo());
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			switch (grupoTipo) {
				//case GrupoDt.ADMINISTRADORES:
				case GrupoTipoDt.ADMINISTRADOR:
					liTemp = obPersistencia.consultarServentiaCargos(null, usuarioNe);
					break;

				default:
					liTemp = obPersistencia.consultarServentiaCargos(usuarioNe.getUsuarioDt().getId_Serventia(), usuarioNe);
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Método responsável por consultar os cargos nas serventias relacionadas à
	 * serventia atual do usuário da sessão.
	 * 
	 * @param id_Serventia -
	 *            ID da Serventia do usuário da sessão
	 * @return lista de cargos
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarCargosServentiasRelacionadas(String id_Serventia) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao =null;
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			List listaServentias = serventiaRelacionadaNe.consultarServentiasRel(id_Serventia);
			liTemp = obPersistencia.consultarCargosServentiasRelacionadas(listaServentias);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

//	/**
//	 * Retorna todos os ServentiaCargos que serão utilizados na distribuição de processos. 
//	 * Esse método é chamado ao iniciar o sistema, e deve retornar todos os cargos de uma serventia, de acordo com o tipo dessa serventia.
//	 * 
//	 * SOMENTE SERVENTIACARGO COM QUANTIDADE DE DISTRIBUIÇÃO MAIOR QUE "0"(ZERO)
//	 * PODE ENTRAR NA DISTRIBUIÇÃO.
//	 * 
//	 * Se o tipo da serventia é: 
//	 * 		- Vara: retorna os juízes que podem ser responsáveis por processos, 
//	 * 		- Turma: retorna os relatores que podem ser responsáveis, e 
//	 * 		- Promotoria: retorna os promotores que podem ser responsáveis por processos.
//	 * 
//	 * @author msapaula
//	 */
//	public List consultarServentiaCargosDistribuicao(String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo){
//		List liTemp = null;
//		FabricaConexao obFabricaConexao =null;
//		try{
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
//
//			switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
//				case ServentiaTipoDt.VARA:
//					liTemp = obPersistencia.consultarServentiaCargosDistribuicao(id_Serventia, GrupoTipoDt.JUIZ_VARA);
//					break;
//
//				case ServentiaTipoDt.SEGUNDO_GRAU:
//					if (serventiaSubtipoCodigo.length() > 0 && Funcoes.StringToInt(serventiaSubtipoCodigo) == ServentiaSubtipoDt.CAMARA_CIVEL) {
//						liTemp = obPersistencia.consultarServentiaCargosDistribuicaoSegundoGrau(id_Serventia);
//					} else liTemp = obPersistencia.consultarServentiaCargosDistribuicao(id_Serventia, GrupoTipoDt.JUIZ_TURMA);
//					break;
//
//				case ServentiaTipoDt.PROMOTORIA:
//					liTemp = obPersistencia.consultarServentiaCargosDistribuicao(id_Serventia, GrupoTipoDt.PROMOTOR);
//					break;
//
//			}
//		} finally{
//			obFabricaConexao.fecharConexao();
//		}
//		return liTemp;
//	}

	   public String consultarServentiaCargosDistribuicao1Grau(String id_Serventia, String id_processoTipo, int grupoTipoCodigo) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaCargosDistribuicao1Grau(id_Serventia, id_processoTipo, grupoTipoCodigo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	    }
	   
	   public String consultarServentiaCargosDistribuicao1Grau(String id_Serventia,  int grupoTipoCodigo) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaCargosDistribuicao1Grau(id_Serventia,  grupoTipoCodigo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	    }
	   
		/**
		 * Método responsável por fazer a redistribuição para serventia cargo de gabinete
		 * 
		 * @param id_Serventia_grupo, id_serventia_cargo
		 * @return o id_serv_cargo 
		 * @throws Exception
		 * @author jrcorrea
		 */
	   
	   public String consultarServentiaGrupoReDistribuicao(String id_Serventia_grupo, String id_serventia_cargo) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaGrupoReDistribuicao(id_Serventia_grupo, id_serventia_cargo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	   }
	   
		/**
		 * Método responsável por fazer a distribuição para serventia cargo de gabinete
		 * 
		 * @param id_Serventia_grupo, 
		 * @return o id_serv_cargo 
		 * @throws Exception
		 * @author jrcorrea
		 */
	   
	   public String consultarServentiaGrupoDistribuicao(String id_Serventia_grupo) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaGrupoDistribuicao(id_Serventia_grupo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	   }
	   
	   /**
	   * Consulta os cargos que serão utilizados na distribuição de pendencia de
	   * acordo com a serventia passada
	   * 
	   * param id_serventiao, identificação o serventia 
	   * return o id_serv_cargo de quem recebeu menos pendenica
	   * 
	   * author lsbernardes
	   * data 08/08/2018
	   */
	   public String consultarServentiaCargoDistribuicao(String id_serventia, FabricaConexao conexao) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	        	if (conexao == null)  
	        		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				else 
					obFabricaConexao = conexao;
	        	
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaCargoDistribuicao(id_serventia);
	         
	        
	        } finally{
	        	if (conexao == null) 
	        		obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	   }
	   
	   /**
	   * Consulta os cargos que serão utilizados na distribuição de pendencia de
	   * acordo com a serventia passada e o tipo de pendencia
	   * 
	   * param id_serventia, identificação o serventia 
	   * param pendenciaTipoCodigo, identificação do tipo de pendencia 
	   * return o id_serv_cargo de quem recebeu menos pendencia
	   * 
	   * author lsbernardes
	   * data 08/08/2018
	   */
	   public String consultarServentiaCargoDistribuicao(String id_serventia, String pendenciaTipoCodigo, FabricaConexao conexao) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	        	if (conexao == null)  
	        		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				else 
					obFabricaConexao = conexao;
	        	
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaCargoDistribuicao(id_serventia, pendenciaTipoCodigo);
	         
	        
	        } finally{
	        	if (conexao == null) 
	        		obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	   }
	   
	   public String consultarPrevensaoServentiaGrupo(String id_Serventia_grupo, String id_pend) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarPrevensaoServentiaGrupo(id_Serventia_grupo, id_pend);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	    }
	   public String consultarServentiaCargosDistribuicaoTurma(String id_Serventia, String id_processoTipo, int grupoTipoCodigo) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaCargosDistribuicaoTurma(id_Serventia, id_processoTipo, grupoTipoCodigo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	    }
	   
	   public String consultarServentiaCargosDistribuicaoTurma(String id_Serventia,  int grupoTipoCodigo) throws Exception {
	        String stTemp = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            stTemp = obPersistencia.consultarServentiaCargosDistribuicaoTurma(id_Serventia,  grupoTipoCodigo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return stTemp;
	    }

	   public ServentiaCargoDt consultarServentiaCargosDistribuicao2Grau(String id_Serventia, String id_processoTipo, int grupoTipoCodigo) throws Exception {
	        ServentiaCargoDt serventiaCargoDt = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            serventiaCargoDt = obPersistencia.consultarServentiaCargosDistribuicao2Grau(id_Serventia, id_processoTipo, grupoTipoCodigo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return serventiaCargoDt;
	    }
	   
	   public ServentiaCargoDt consultarServentiaCargosDistribuicao2Grau(String id_Serventia,  int grupoTipoCodigo) throws Exception {
	        ServentiaCargoDt serventiaCargoDt = null;
	        FabricaConexao obFabricaConexao =null;
	        try{
	            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
	            ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

	            serventiaCargoDt = obPersistencia.consultarServentiaCargosDistribuicao2Grau(id_Serventia,  grupoTipoCodigo);
	         
	        
	        } finally{
	            obFabricaConexao.fecharConexao();
	        }
	        return serventiaCargoDt;
	    }
	   
	   /**
		 * Método que consulta outros relator, revisor e vogal dentro da mesma câmara para receber o processo.
		 * @param id_serventia - ID da Servendia
		 * @param Id_ProcessoTipo - Tipo de processo
		 * @param grupoTipoCodigo - grupo tipo
		 * @param id_RelatorAtual - id do relator atual do processo
		 * @return lista de possíveis desembargadores que podem receber o processo
		 * @throws Exception
		 * @author hmgodinho
		 */
	   public ServentiaCargoDt consultarServentiaCargosDistribuicao2GrauPropriaServentia(String id_Serventia, String id_processoTipo, String grupoTipoCodigo, String id_RelatorAtual) throws Exception {
			ServentiaCargoDt serventiaCargoDt = null;
			FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
				serventiaCargoDt = obPersistencia.consultarServentiaCargosDistribuicao2GrauPropriaServentia(id_Serventia, id_processoTipo, grupoTipoCodigo, String.valueOf(CargoTipoDt.DESEMBARGADOR), id_RelatorAtual);
	
			
			} finally{
				obFabricaConexao.fecharConexao();
			}
			return serventiaCargoDt;
		}
	   
	   /**
		 * Método que consulta outros relator, revisor e vogal dentro da mesma câmara para receber o processo.
		 * @param id_serventia - ID da Servendia
		 * @param grupoTipoCodigo - grupo tipo
		 * @param id_RelatorAtual - id do relator atual do processo
		 * @return lista de possíveis desembargadores que podem receber o processo
		 * @throws Exception
		 * @author jrcorrea
		 * 09/10/2013
		 */
	   public ServentiaCargoDt consultarServentiaCargosDistribuicao2GrauPropriaServentia(String id_Serventia, String grupoTipoCodigo, String id_RelatorAtual) throws Exception {
			ServentiaCargoDt serventiaCargoDt = null;
			FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
				serventiaCargoDt = obPersistencia.consultarServentiaCargosDistribuicao2GrauPropriaServentia(id_Serventia,  grupoTipoCodigo, String.valueOf(CargoTipoDt.DESEMBARGADOR), id_RelatorAtual);
	
			
			} finally{
				obFabricaConexao.fecharConexao();
			}
			return serventiaCargoDt;
		}
	   
//	/**
//	 * Consulta os conciliadores de uma determinada serventia
//	 * 
//	 * @param id_serventia
//	 *            identificação da serventia
//	 * @author msapaula
//	 */
//	public List consultarConciliadores(String id_serventia){
//		List liTemp = null;
//		FabricaConexao obFabricaConexao =null;
//		try{
//			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
//			liTemp = obPersistencia.consultarServentiaCargosDistribuicao(id_serventia, GrupoTipoDt.CONCILIADOR_VARA);
//		} finally{
//			obFabricaConexao.fecharConexao();
//		}
//		return liTemp;
//	}

	/**
	 * Consulta os cargos cadastrados para uma determinada serventia
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do cargo
	 * @param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * @param id_Serventia,
	 *            identificação da serventia
	 * 
	 * @author Keila Sousa Silva, msapaula
	 * @since 08/10/2008
	 */
	public List consultarServentiaCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, null);
			QuantidadePaginas = (Long) listaServentiaCargos.get(listaServentiaCargos.size() - 1);
			listaServentiaCargos.remove(listaServentiaCargos.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}
	
	/**
	 * Consulta os cargos cadastrados para uma determinada serventia
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do cargo
	 * @param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * @param id_Serventia,
	 *            identificação da serventia
	 * 
	 * @author Leandro Bernardes
	 */
	public List consultarServentiaCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupoTipoCodigo) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, grupoTipoCodigo);
			QuantidadePaginas = (Long) listaServentiaCargos.get(listaServentiaCargos.size() - 1);
			listaServentiaCargos.remove(listaServentiaCargos.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}
	

	public String consultarServentiaCargosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupoTipoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, grupoTipoCodigo);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	
	/**
	 * Consulta os cargos cadastrados para uma determinada serventia
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do cargo
	 * @param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * @param id_Serventia,
	 *            identificação da serventia
	 * 
	 * @author Leandro Bernardes
	 */
	public List consultarServentiaCargosGrupo(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String grupoCodigo) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			listaServentiaCargos = obPersistencia.consultarServentiaCargosGrupo(nomeBusca, posicaoPaginaAtual, id_Serventia, grupoCodigo);
			QuantidadePaginas = (Long) listaServentiaCargos.get(listaServentiaCargos.size() - 1);
			listaServentiaCargos.remove(listaServentiaCargos.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}

	/**
	 * Consulta os cargos cadastrados para uma determinada serventia, de acordo
	 * com o tipo dessa serventia.
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do cargo
	 * @param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * @param serventiaDt,
	 *            objeto com dados da serventia
	 * 
	 * @author msapaula
	 */
	public List consultarServentiaCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
				case ServentiaTipoDt.VARA:
					listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU));
					break;

				case ServentiaTipoDt.SEGUNDO_GRAU:
					if(ServentiaSubtipoDt.isSegundoGrau(serventiaSubtipoCodigo)){
						listaServentiaCargos = obPersistencia.consultarServentiaCargosDesembargadores(nomeBusca, posicaoPaginaAtual, id_Serventia);
					}
					else{
						listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.JUIZ_TURMA));	
					}					
					break;

				case ServentiaTipoDt.PROMOTORIA:
					listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.MP));
					break;
				case ServentiaTipoDt.GABINETE:
					listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE));
					break;
				case ServentiaTipoDt.INFORMATICA:
					listaServentiaCargos = obPersistencia.consultarServentiaCargos(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.ANALISTA_TI));
					break;
			}

			if (listaServentiaCargos != null && listaServentiaCargos.size() > 0) {
				QuantidadePaginas = (Long) listaServentiaCargos.get(listaServentiaCargos.size() - 1);
				listaServentiaCargos.remove(listaServentiaCargos.size() - 1);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}

	/**
	 * Consulta os tipos de cargos cadastrados para uma determinada serventia
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do tipo de cargo
	 * @param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * @param id_Serventia,
	 *            identificação da serventia
	 * 
	 * @author msapaula
	 */
	public List consultarTiposCargos(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List listaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			listaCargos = obPersistencia.consultarTiposCargos(nomeBusca, posicaoPaginaAtual, id_Serventia);
			QuantidadePaginas = (Long) listaCargos.get(listaCargos.size() - 1);
			listaCargos.remove(listaCargos.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaCargos;
	}

	/**
	 * Realiza chamada ao método em UsuarioServentiaGrupoNe
	 */
	public List consultarUsuarioServentiaCargo(String id_CargoTipo, String id_Serventia, String descricao, String posicao) throws Exception {
		List tempList = null;
		
		UsuarioServentiaGrupoNe UsuarioServentiaGrupone = new UsuarioServentiaGrupoNe();
		tempList = UsuarioServentiaGrupone.consultarUsuarioServentiaCargo(id_CargoTipo, id_Serventia, descricao, posicao);
		QuantidadePaginas = UsuarioServentiaGrupone.getQuantidadePaginas();
		UsuarioServentiaGrupone = null;
		
		return tempList;
	}

	/**
	 * Retorna o usuário que ocupa o cargo de presidente de uma turma recursal
	 * 
	 * @param id_Serventia, identificação da serventia turma recursal
	 * 
	 * @author msapaula
	 * @throws Exception 
	 */
	public ServentiaCargoDt getPresidenteTurmaRecursal(String id_Serventia) throws Exception{
		return this.getPresidenteTurmaRecursal(id_Serventia, null);
	}

	/**
	 * Retorna o usuário que ocupa o cargo de presidente de uma turma recursal
	 * 
	 * @param id_Serventia, identificação da serventia turma recursal
	 * @param conexao, conexão já ativa
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt getPresidenteTurmaRecursal(String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCargo(id_Serventia, CargoTipoDt.PRESIDENTE_TURMA,false);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de presidente de uma serventia do segundo grau (Camara, Corte, Sessão)
	 * 
	 * @param id_Serventia, identificação da serventia turma recursal
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt getPresidenteSegundoGrau(String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		FabricaConexao obFabricaConexao = null;
		ServentiaDt serventiaPresidencia = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			// Consultando a serventia relacionada à câmara do tipo Presidência
			serventiaPresidencia = serventiaRelacionadaNe.consultarServentiaRelacionadaPresidencia(id_Serventia, obFabricaConexao);
			
			//"Não foi localizada uma serventia do tipo presidência relacionada a esta serventia."
			if (serventiaPresidencia != null) {
				ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
				//Primeiro tenta localizar o substituto
				dtRetorno = obPersistencia.consultarServentiaCargoSubstituicao(serventiaPresidencia.getId(), serventiaPresidencia.getServentiaSubtipoCodigo(), CargoTipoDt.DESEMBARGADOR);
				if(dtRetorno == null) {
					//se não achar substituto, localiza o desembargador titular do gabinete
					dtRetorno = obPersistencia.consultarServentiaCargoNaoSubstituicao(serventiaPresidencia.getId(), CargoTipoDt.DESEMBARGADOR);
				}
			}		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Método que localiza o último relator a receber o processo naquela serventia. 
	 * 
	 * @param id_Serventia - ID da serventia do processo
	 * @param id_Processo - ID do processo
	 * @param id_areaDistribuicao - ID da área de distribuição
	 * @return ID do serv cargo do relator
	 * @throws Exception
	 * 
	 * @author hmgodinho
	 */
	public String getUltimoRelatorProcesso (String id_Serventia, String id_Processo, String id_areaDistribuicao) throws Exception {
		String id_serv_cargo = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			id_serv_cargo = obPersistencia.getUltimoRelatorProcesso(id_Serventia, id_Processo, id_areaDistribuicao);
			
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return id_serv_cargo;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de substituição de uma serventia do segundo grau (Camara, Corte, Sessão)
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param codServentiaSubTipo, codigo da serventia subtipo
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt getDesembargadorSubstituto (String id_Serventia, String codServentiaSubTipo, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else 
				obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCargoSubstituicao(id_Serventia,codServentiaSubTipo, CargoTipoDt.DESEMBARGADOR);
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de substituição de uma serventia do segundo grau (Camara, Corte, Sessão)
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param codServentiaSubTipo, codigo da serventia subtipo
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public ServentiaCargoDt getDesembargadorSubstituto(String id_Serventia, String codServentiaSubTipo) throws Exception{
		return this.getDesembargadorSubstituto(id_Serventia, codServentiaSubTipo, null);
	}

	/**
	 * Retorna o Cargo de Distribuidor para um gabinete passado
	 * 
	 * @param id_Serventia, identificação da serventia turma recursal
	 * @param conexao, conexão já ativa
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt getDistribuidorGabinete(String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCargo(id_Serventia, CargoTipoDt.DISTRIBUIDOR_GABINETE,true);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o Cargo de Distribuidor Câmara para uma serventia passada
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param conexao, conexão já ativa
	 * 
	 * @author msapaula
	 */
	public ServentiaCargoDt getDistribuidorCamara(String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		if (conexao == null) obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
		try{
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCargo(id_Serventia, CargoTipoDt.DISTRIBUIDOR_CAMARA,true);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	/**
	 * Retorna o Id_UsuarioServentia que ocupa o cargo de presidente de uma
	 * turma recursal
	 * 
	 * @param id_Serventia
	 *            identificação da serventia turma recursal
	 * 
	 * @author msapaula
	 */
	public String getUsuarioServentiaPresidenteTurmaRecursal(String id_Serventia) throws Exception {
		String stRetorno = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			stRetorno = obPersistencia.consultarUsuarioServentiaCargo(id_Serventia, CargoTipoDt.PRESIDENTE_TURMA);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}

	/**
	 * Método responsável em trocar o usuário de um serventia cargo. Poderá ser
	 * utilizado tanto para trocar o usuário, quanto para retirar um usuário de
	 * um cargo deixando-o vazio
	 * 
	 * @param serventiaCargoDt,
	 *            dt com dados do cargo e usuário
	 * @author msapaula
	 */
	public void habilitaUsuarioServentiaCargo(ServentiaCargoDt serventiaCargoDt) throws Exception {
	    FabricaConexao obFabricaConexao =null;
		try{
			ServentiaCargoDt serventiaCargoAnterior = this.consultarId(serventiaCargoDt.getId());

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			String statusCargo = String.valueOf(ServentiaCargoDt.VAZIO);
			if (serventiaCargoDt.getId_UsuarioServentiaGrupo() != null && !serventiaCargoDt.getId_UsuarioServentiaGrupo().equals("")) statusCargo = String.valueOf(ServentiaCargoDt.OCUPADO);

			String valorAtual = "[Id_ServentiaCargo:" + serventiaCargoDt.getId() + ";Id_UsuarioServentiaGrupo:" + serventiaCargoAnterior.getId_UsuarioServentiaGrupo() + ";CodigoTemp:" + serventiaCargoAnterior.getCodigoTemp() + "]";
			String valorNovo = "[Id_ServentiaCargo:" + serventiaCargoDt.getId() + ";Id_UsuarioServentiaGrupo:" + serventiaCargoDt.getId_UsuarioServentiaGrupo() + ";CodigoTemp:" + statusCargo + "]";

			LogDt obLogDt = new LogDt("ServentiaCargo", serventiaCargoDt.getId(), serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.TrocaUsuarioCargo), valorAtual, valorNovo);
			obPersistencia.alterarUsuarioServentiaCargo(serventiaCargoDt.getId(), serventiaCargoDt.getId_UsuarioServentiaGrupo(), statusCargo);

			obLog.salvar(obLogDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável por retirar um usuário habilitado em um cargo,
	 * deixando este vazio
	 * 
	 * @param serventiaCargoDt,
	 *            dt com dados do cargo
	 * @author msapaula
	 */
	public void desabilitaUsuarioServentiaCargo(ServentiaCargoDt serventiaCargoDt) throws Exception {
	    FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			String valorAtual = "[Id_ServentiaCargo:" + serventiaCargoDt.getId() + ";Id_UsuarioServentiaGrupo:" + serventiaCargoDt.getId_UsuarioServentiaGrupo() + ";CodigoTemp:" + String.valueOf(ServentiaCargoDt.OCUPADO) + "]";
			String valorNovo = "[Id_ServentiaCargo:" + serventiaCargoDt.getId() + ";Id_UsuarioServentiaGrupo:'';CodigoTemp:" + String.valueOf(ServentiaCargoDt.VAZIO) + "]";

			LogDt obLogDt = new LogDt("ServentiaCargo", serventiaCargoDt.getId(), serventiaCargoDt.getId_UsuarioLog(), serventiaCargoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.TrocaUsuarioCargo), valorAtual, valorNovo);
			obPersistencia.alterarUsuarioServentiaCargo(serventiaCargoDt.getId(), "null", String.valueOf(ServentiaCargoDt.VAZIO));

			obLog.salvar(obLogDt, obFabricaConexao);

			obFabricaConexao.finalizarTransacao();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}

	/**
	 * Método responsável por consultar os cargos de uma serventia, para o qual
	 * podem sem criadas agendas de audiências
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do cargo
	 * @param posicaoPaginaAtual,
	 *            parâmetro para paginação
	 * @param id_Serventia,
	 *            identificação da serventia
	 * 
	 * @author Keila Sousa Silva, msapaula
	 */
	public List consultarServentiaCargosAgendaAudiencia(String nomeBusca, String posicaoPaginaAtual, String id_Serventia) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			listaServentiaCargos = obPersistencia.consultarServentiaCargosAgendaAudiencia(nomeBusca, posicaoPaginaAtual, id_Serventia);
			QuantidadePaginas = (Long) listaServentiaCargos.get(listaServentiaCargos.size() - 1);
			listaServentiaCargos.remove(listaServentiaCargos.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}

	/**
	 * Consulta todos os cargos cadastrados para uma determinada serventia, retornando tanto os cargos ocupados quanto os vazios.
	 * 
	 * @param id_serventia, identificação da serventia
	 * @return lista de cargos da serventia
	 * @author hmgodinho
	 */
	public List consultarServentiaCargos(String idServentia) throws Exception {
		List liTemp = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			liTemp = obPersistencia.consultarServentiaCargos(idServentia, null);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Método responsável por consultar um cargo de um serventia pelo ID.
	 * 
	 * @param idServentiaCargo - ID do cargo da serventia
	 * @param FabricaConexao - conexao fabrica de conexão
	 * @return cargo da serventia consultado
	 * @author hmgodinho
	 */
	public ServentiaCargoDt consultarServentiaCargoId(String idServentiaCargo, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt serventiaCargo = null;
		FabricaConexao obFabricaConexao =null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			serventiaCargo = obPersistencia.consultarId(idServentiaCargo);

		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return serventiaCargo;
	}
	
	/**
	 * Retorna o Cargo de Desembargador Câmara para uma serventia passada
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 */
	public ServentiaCargoDt consultarServentiaCargoDesembargador(String id_Serventia, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarServentiaCargo(id_Serventia, CargoTipoDt.DESEMBARGADOR,false);
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Método responsável por consultar um cargo de um serventia pelo ID.
	 * 
	 * @param idServentiaCargo - ID do cargo da serventia
	 * @return cargo da serventia consultado
	 * @author lsbernardes
	 * @throws Exception 
	 */
	public ServentiaCargoDt consultarServentiaCargoId(String idServentiaCargo) throws Exception{
		return this.consultarServentiaCargoId(idServentiaCargo, null);
	}
	
	/**
	 * Consulta todos os cargos cadastrados do tipo desembargador relacionados aos gabinetes vinculados à câmara
	 * 
	 * @param nomeBusca,
	 *            filtro para descrição do cargo 
	 * @param id_ServentiaCamara,
	 *            identificação da serventia 
	 * 
	 * @author mmgomes
	 * @since 24/11/2011
	 */
	public List consultarServentiaCargosDesembargadores(String nomeBusca, String id_ServentiaCamara) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			listaServentiaCargos = obPersistencia.consultarServentiaCargosDesembargadores(nomeBusca, id_ServentiaCamara);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}
	
	/**
	 * Consulta os juízes cadastrados nas Turmas ou 1º grau
	 * 
	 * @param id_Serventia: identificação da serventia 
	 * 
	 * @author lsbernardes, hmgodinho
	 */
	public List consultarServentiaCargosJuizes(String idServentia) throws Exception {
		List listaServentiaCargos = null;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			ServentiaNe serventiaNe = new ServentiaNe();
			ServentiaDt serventiaDt = serventiaNe.consultarId(idServentia);
			
			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			if (serventiaDt.isUPJs()) {
				listaServentiaCargos = obPersistencia.consultarServentiaCargosJuizUPJ(idServentia);
			} else {
				listaServentiaCargos = obPersistencia.consultarServentiaCargosJuizes(idServentia, Integer.parseInt(serventiaDt.getId_ServentiaSubtipo()));
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaServentiaCargos;
	}
	
	public String consultarDescricaoServentiaSubtipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
		stTemp = ServentiaSubtipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarServentiaCargosJSON(String nomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoServentiaCargoMagistradosPorServentiaJSON(String nomeBusca, String id_Serventia, String posicaoPaginaAtual) throws Exception {
		String stTemp;
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoServentiaCargoMagistradosPorServentiaJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	
	/**
	 * Retorna o usuário que ocupa o cargo de presidente de uma serventia do segundo grau (Camara, Corte, Sessão)
	 *
	 * @param conexao, conexão já ativa
	 * 
	 * @author mmgomes
	 */
	public ServentiaCargoDt getPresidenteTribunalDeJustica(FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ServentiaNe serventiaNe = new ServentiaNe();
		FabricaConexao obFabricaConexao = null;
		ServentiaDt serventiaPresidenciaTJGO = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			// Consultando a serventia do tipo Presidência do TJGO
			serventiaPresidenciaTJGO = serventiaNe.consultarGabinetePresidenciaTJGO(obFabricaConexao);
			
			//"Não foi localizada uma serventia do tipo Presidência do TJGO."
			if (serventiaPresidenciaTJGO != null) 
			{
				ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
				dtRetorno = obPersistencia.consultarServentiaCargoComQuantidadeDistribuicao(serventiaPresidenciaTJGO.getId(), CargoTipoDt.DESEMBARGADOR);
			}		
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de presidente de uma serventia do segundo grau (Camara, Corte, Sessão)
	 *	 
	 * @param conexao, conexão já ativa
	 * 
	 * @author mmgomes
	 */
	public ServentiaCargoDt getVicePresidenteTribunalDeJustica(FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;
		ServentiaNe serventiaNe = new ServentiaNe();
		FabricaConexao obFabricaConexao = null;
		ServentiaDt serventiaPresidenciaTJGO = null;
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			// Consultando a serventia do tipo Vice Presidência do TJGO
			serventiaPresidenciaTJGO = serventiaNe.consultarGabineteVicePresidenciaTJGO(obFabricaConexao);
			
			//"Não foi localizada uma serventia do tipo Vice Presidência do TJGO."
			if (serventiaPresidenciaTJGO != null) 
			{
				ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
				dtRetorno = obPersistencia.consultarServentiaCargoComQuantidadeDistribuicao(serventiaPresidenciaTJGO.getId(), CargoTipoDt.DESEMBARGADOR);
			}		
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe(); 
		stTemp = Serventiane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de desembargador de uma serventia do segundo grau
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param conexao, conexão já ativa
	 * 
	 * @author mmgomes
	 */
	public ServentiaCargoDt getDesembargadorTitular(String id_ServentiaGabinte, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;		
		FabricaConexao obFabricaConexao = null;		
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			dtRetorno = obPersistencia.consultarServentiaCargoComQuantidadeDistribuicao(id_ServentiaGabinte, CargoTipoDt.DESEMBARGADOR);					
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de desembargador de uma serventia do segundo grau sem distribuição
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param conexao, conexão já ativa
	 * 
	 * @author jvosantos
	 */
	public ServentiaCargoDt getDesembargadorTitularSemDistribuicao(String id_ServentiaGabinte, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;		
		FabricaConexao obFabricaConexao = null;		
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			dtRetorno = obPersistencia.consultarServentiaCargo(id_ServentiaGabinte, CargoTipoDt.DESEMBARGADOR);					
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de desembargador de uma serventia do segundo grau sem considerar a quantidade de distribuição
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param conexao, conexão já ativa
	 * 
	 * @author jvosantos
	 */
	public ServentiaCargoDt getDesembargadorTitularSemQuantidadeDistribuicao(String id_ServentiaGabinte, FabricaConexao conexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;		
		FabricaConexao obFabricaConexao = null;		
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			dtRetorno = obPersistencia.consultarServentiaCargo(id_ServentiaGabinte, CargoTipoDt.DESEMBARGADOR);					
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de desembargador de uma serventia do segundo grau
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param id_Processo, identificação do processo
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 */
	public String getDesembargadorResponsavel(String id_Serventia, String id_Processo, FabricaConexao conexao) throws Exception {
		String id_ServentiaCargo = null;	
		FabricaConexao obFabricaConexao = null;		
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			id_ServentiaCargo = obPersistencia.getDesembargadorResponsavel(id_Serventia, id_Processo);					
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return id_ServentiaCargo;
	}
	
	
	/**
	 * Retorna o usuário que ocupa o cargo de desembargador de uma serventia do segundo grau
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 */
	public String getDesembargadorServentia(String id_Serventia, FabricaConexao conexao) throws Exception {
		String id_ServentiaCargo = null;	
		FabricaConexao obFabricaConexao = null;		
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			id_ServentiaCargo = obPersistencia.getDesembargadorServentia(id_Serventia);					
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return id_ServentiaCargo;
	}
	
	public String consultarDescricaoCargoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp;
		
		CargoTipoNe CargoTipone = new CargoTipoNe(); 
		stTemp = CargoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarUsuarioServentiaCargoJSON(String id_CargoTipo, String id_Serventia, String descricao, String posicao) throws Exception {
		String stTemp = "";
		
		UsuarioServentiaGrupoNe UsuarioServentiaGrupone = new UsuarioServentiaGrupoNe();
		stTemp = UsuarioServentiaGrupone.consultarUsuarioServentiaCargoJSON(id_CargoTipo, id_Serventia, descricao, posicao);
		
		return stTemp;
	}
	
	public String consultarServentiaCargosJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaTipoCodigo, String serventiaSubtipoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

			switch (Funcoes.StringToInt(serventiaTipoCodigo)) {
				case ServentiaTipoDt.VARA:
					if(ServentiaSubtipoDt.isUPJs(serventiaSubtipoCodigo)){
						stTemp = obPersistencia.consultarServentiaCargosJuizUPJJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
					} else {
						stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU));						
					}
					break;
				case ServentiaTipoDt.SEGUNDO_GRAU:
					if (Funcoes.StringToInt(serventiaSubtipoCodigo) == ServentiaSubtipoDt.UPJ_TURMA_RECURSAL) {
						stTemp = obPersistencia.consultarServentiaCargosJuizUPJJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
					}else if(ServentiaSubtipoDt.isSegundoGrau(serventiaSubtipoCodigo)){
						stTemp = obPersistencia.consultarServentiaCargosDesembargadoresJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
					}	else{
						stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.JUIZ_TURMA));	
					}					
					break;
				case ServentiaTipoDt.PROMOTORIA:
					stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.MP));
					break;
				case ServentiaTipoDt.GABINETE:
					if (Funcoes.StringToInt(serventiaSubtipoCodigo) == ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
						stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE_FLUXO));
					} else {
						stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.ASSISTENTE_GABINETE));
					}
					break;
				case ServentiaTipoDt.INFORMATICA:
					stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.ANALISTA_TI));
					break;
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarMagistradoGabineteJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			if (Funcoes.StringToLong(serventiaSubTipoCodigo)==ServentiaSubtipoDt.GABINETE_FLUXO_UPJ) {
				stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_PRIMEIRO_GRAU));
			}else {
				stTemp = obPersistencia.consultarServentiaCargosJSON(nomeBusca, posicaoPaginaAtual, id_Serventia, String.valueOf(GrupoTipoDt.MAGISTRADO_SEGUNDO_GRAU));
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarServentiaCargosAgendaAudienciaJSON(String nomeBusca, String posicaoPaginaAtual, String id_Serventia, String serventiaSubTipoCodigo) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao =null;
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			if (ServentiaSubtipoDt.isUPJs(serventiaSubTipoCodigo)) {
				stTemp = obPersistencia.consultarServentiaCargosAgendaAudienciaUpjJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
			}else {
				stTemp = obPersistencia.consultarServentiaCargosAgendaAudienciaJSON(nomeBusca, posicaoPaginaAtual, id_Serventia);
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

	public String consultarId_ServentiaCargo( String id_UsuarioServentia, int grupoTipo) throws Exception {
		String id_ServentiaCargo = null;	
		FabricaConexao obFabricaConexao = null;		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{
			
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			id_ServentiaCargo = obPersistencia.consultarId_ServentiaCargo(id_UsuarioServentia,  grupoTipo);
						
			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return id_ServentiaCargo;
	}
	
	public void inserir(ServentiaCargoDt dados, FabricaConexao obFabricaConexao) throws Exception {
		LogDt obLogDt;
		
		ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());

		// Se não tem usuário, é porque cargo está vazio
		if (dados.getId_UsuarioServentiaGrupo().length() == 0) dados.setCodigoTemp(String.valueOf(ServentiaCargoDt.VAZIO));
		else dados.setCodigoTemp(String.valueOf(ServentiaCargoDt.OCUPADO));

		obPersistencia.inserir(dados);
		obLogDt = new LogDt("ServentiaCargo", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);		
	}
	
	public ServentiaCargoDt consultarId(String id_serventiacargo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt dtRetorno=null;
		ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_serventiacargo ); 
		obDados.copiar(dtRetorno);
		return dtRetorno;
	}
	
	public List consultarTurmaJulgadoraSessaoCivel(String id_serventia, String id_servCargoRelator) throws Exception {
		List lista = new ArrayList<>();
		FabricaConexao obFabricaConexao =null;		

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
			List<ServentiaCargoDt> temp_lista = obPersistencia.consultarServentiaCargosDesembargadoresOrdemAntiguidade(id_serventia);
			for (int i = 0; i < temp_lista.size(); i++) {
				ServentiaCargoDt servCargo = temp_lista.get(i);
				if (servCargo.getId() != null && servCargo.getId().equals(id_servCargoRelator)){
					lista.add(servCargo);
					lista.add(temp_lista.get((i+1)%temp_lista.size()));
					lista.add(temp_lista.get((i+2)%temp_lista.size()));
				break;
				}				
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return lista;		
	}
	
	public List<ServentiaCargoDt> consultarTurmaJulgadoraSessaoCorteEspecial(String id_serventia, String id_servCargoRelator) throws Exception {
		ServentiaRelacionadaNe serventiaRelacionadaNe = new ServentiaRelacionadaNe();
		List<ServentiaRelacionadaDt> listaServentiasRelacoes = serventiaRelacionadaNe.consultarServentiasRelacionadas(id_serventia, ServentiaTipoDt.GABINETE);
		listaServentiasRelacoes = listaServentiasRelacoes
									.stream()
									.filter(servRelacionada -> StringUtils.isNotEmpty(servRelacionada.getOrdemTurmaJulgadora()))
									.collect(Collectors.toList());
		ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
		Queue<ServentiaCargoDt> listaServentiasDesembargadores = new LinkedList<ServentiaCargoDt>();
		int swaps = 0;
		boolean encontrouRelator = false;
		for (ServentiaRelacionadaDt serventiaRelacaoDt : listaServentiasRelacoes) {
				ServentiaCargoDt servCargoDt = new ServentiaCargoDt();
				servCargoDt = serventiaCargoNe.getDesembargadorTitularSemQuantidadeDistribuicao(serventiaRelacaoDt.getId_ServentiaRelacao(), null);
				servCargoDt.setId_Serventia(serventiaRelacaoDt.getId_ServentiaRelacao());
				servCargoDt.setServentia(serventiaRelacaoDt.getServentiaRelacao());
				servCargoDt.setCargoTipo("Desembargador");
				listaServentiasDesembargadores.add(servCargoDt);
				if(StringUtils.equals(servCargoDt.getId(), id_servCargoRelator)) {
					encontrouRelator = true;
				} else if(!encontrouRelator) {
					++swaps;
				}
		}
		
		for(int i = 0; i < swaps; ++i) {
			ServentiaCargoDt sc = listaServentiasDesembargadores.remove();
			listaServentiasDesembargadores.add(sc);
		}

		return listaServentiasDesembargadores.stream().collect(Collectors.toCollection(ArrayList::new));
	}
	
	public List<ServentiaCargoDt> consultarServentiaCargosDesembargadoresOrdemAntiguidade(String id_ServentiaCamara, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarServentiaCargosDesembargadoresOrdemAntiguidade(id_ServentiaCamara);
	}
		
	public List<ServentiaCargoDt> consultarServentiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem(String id_ServentiaCamara, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarServentiaCargosDesembargadoresOrdemAntiguidadeTrazendoOrdem(id_ServentiaCamara);
	}

	public List<ServentiaCargoDt> consultarServentiaCargosDesembargadoresOrdemAntiguidade(String id_ServentiaCamara) throws Exception {
		List<ServentiaCargoDt> lista = new ArrayList<>();
		FabricaConexao obFabricaConexao =null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			lista = consultarServentiaCargosDesembargadoresOrdemAntiguidade(id_ServentiaCamara,obFabricaConexao);	
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
		return lista;	
		
	}
	
	/**
	 * Retorna o usuário que ocupa o cargo de desembargador de uma serventia do segundo grau
	 * 
	 * @param id_Serventia, identificação da serventia
	 * @param id_Processo, identificação do processo
	 * @param conexao, conexão já ativa
	 * 
	 * @author lsbernardes
	 */
	public String getJuizUPJResponsavel(String id_Serventia, String id_Processo, FabricaConexao conexao) throws Exception {
		String id_ServentiaCargo = null;	
		FabricaConexao obFabricaConexao = null;		
		try{
			if (conexao == null)  obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			else obFabricaConexao = conexao;
			
			ServentiaCargoPs obPersistencia = new  ServentiaCargoPs(obFabricaConexao.getConexao());
				
			id_ServentiaCargo = obPersistencia.getJuizUPJResponsavel(id_Serventia, id_Processo);					
			
		
		} finally{
			if (conexao == null) obFabricaConexao.fecharConexao();
		}
		return id_ServentiaCargo;
	}


	
	public ServentiaCargoDt consultarServentiaCargpoDistribuicaoGabineteFluxo(String id_Serventia, int cargoTipoCodigo, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoDt dtRetorno = null;				
		
		ServentiaCargoPs obPersistencia = new ServentiaCargoPs(obFabricaConexao.getConexao());
		dtRetorno = obPersistencia.consultarServentiaCargpoDistribuicaoGabineteFluxo(id_Serventia, cargoTipoCodigo);		
		
		return dtRetorno;
	}

}
