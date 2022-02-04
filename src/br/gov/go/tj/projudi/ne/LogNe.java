package br.gov.go.tj.projudi.ne;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.AtributoLogDt;
import br.gov.go.tj.projudi.dt.AudienciaTipoDt;
import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CargoTipoDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ClassificadorDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EmpresaTipoDt;
import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.dt.EstadoCivilDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.GovernoTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.ObjetoPedidoDt;
import br.gov.go.tj.projudi.dt.ProcessoFaseDt;
import br.gov.go.tj.projudi.dt.ProcessoParteAusenciaDt;
import br.gov.go.tj.projudi.dt.ProcessoParteTipoDt;
import br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt;
import br.gov.go.tj.projudi.dt.ProcessoStatusDt;
import br.gov.go.tj.projudi.dt.ProcessoTipoDt;
import br.gov.go.tj.projudi.dt.ProfissaoDt;
import br.gov.go.tj.projudi.dt.RacaDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaSubtipoDt;
import br.gov.go.tj.projudi.dt.ServentiaTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt;
import br.gov.go.tj.projudi.ps.LogPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.TJDataHora;

//---------------------------------------------------------
public class LogNe extends LogNeGen {

	/**
     * 
     */
	private static final long serialVersionUID = 7381859928031759474L;

	public String getId_Log() {
		return obDados.getId();
	}

	/**
	 * Salva (inseri ou altera) um log no banco de dados, utilizando uma fabrica
	 * de conexao determinada
	 * @author Ronneesley Moura Teles
	 * @since 06/06/2008 10:28
	 * @param LogDt dados, dados a serem persistidos
	 * @param FabricaConexao obfabricaconexao, fabrica de conexao
	 * @return void
	 * @throws Exception
	 */
	public void salvar(LogDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());

		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
		} else {
			obPersistencia.alterar(dados);
		}
		obDados.copiar(dados);


	}

	/**
	 * Consulta logs de acordo com parâmetros passados
	 * 
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * 
	 * @author jrcorre
	 * @since 03/12/2013
	 */
	public String consultarLogJSON(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String posicao, String id_Tabela) throws Exception {
		String  logs ="";
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			logs = obPersistencia.consultarLogJSON(codigo, tabela, dataInicial, dataFinal, id_LogTipo, posicao, id_Tabela);			
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return logs;
	}
	
	// ---------------------------------------------------------
	public String Verificar(LogDt dados) {
		String stRetorno = "";
		if (dados == null) {
			stRetorno = "É necessário informar os filtros para pesquisa";
		} else {
			stRetorno = Verificar(dados.getId(), dados.getDataInicial(), dados.getDataFinal());
		}		
		return stRetorno;
	}
	
	public String Verificar(String codigo, String dataInicial, String dataFinal) {
		String stRetorno = "";
		if ((codigo == null || codigo.trim().length() == 0) && (dataInicial == null || dataInicial.trim().length() == 0 && dataFinal == null || dataFinal.trim().length() == 0)) {
			stRetorno = "É necessário informar no mínimo o Código ou a Data Inicial e a Data Final.";
		} else if (codigo == null || codigo.trim().length() == 0) {
			if (!Funcoes.validaData(dataInicial)) stRetorno = "Data Inicial é inválida.";
			if (!Funcoes.validaData(dataFinal)) stRetorno = "Data Final é inválida.";
			
			if (stRetorno == "") 
			{
				try
				{
					TJDataHora dtInicial = new TJDataHora(dataInicial);
					TJDataHora dtFinal = new TJDataHora(dataFinal);
					if (dtInicial.getAno() != dtFinal.getAno()) stRetorno = "O ano deve ser o mesmo para a data inicial e data final.";
				} catch(Exception Mensagem) {
					stRetorno = Mensagem.getMessage();
				}			
			}			
		}			
		return stRetorno;
	}
		
	public LogDt consultarId(String id_log) throws Exception {
		LogDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarId(id_log);
			if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public void salvarErro(LogDt logDt) throws Exception {
		String stHash = Funcoes.GeraHashMd5_32(logDt.getValorNovo());
		
		LogDt log = null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			log = obPersistencia.consultarLogHash(stHash);
			
			if (log!=null){
				//se encontrou um log de erro só adciono 1 no contador de erros deste tipo
				log.addErro();
				obPersistencia.incluirErroDia(log);
				obDados.copiar(log);
			}else{
				logDt.setHash(stHash);
				logDt.setQtdErrosDia(1);
				obPersistencia.inserirErro(logDt);
				obDados.copiar(logDt);
			}
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		
	}
	
	public void salvar(LogDt dados) throws Exception {
	    FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("")) {
				obPersistencia.inserir(dados);
			} else {
				obPersistencia.alterar(dados);
			}

			obDados.copiar(dados);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Listagem de logs gerados em data específica.
	 * @param data - data do log
	 * @return lista de logs
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List listarLogErros(String data, String posicao) throws Exception {
		List listaLogs = null;
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new  LogPs(obFabricaConexao.getConexao());
			listaLogs = obPersistencia.listarLogErros(data, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return listaLogs;
	}
	
	/**
	 * Consulta logs de acordo com parâmetros passados
	 * 
	 * @param tabela
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_LogTipo
	 * @param posicao
	 * 
	 * @author msapaula
	 */
	public List consultarLog(String codigo, String tabela, String dataInicial, String dataFinal, String id_LogTipo, String logTipoCodigo, String posicao, String id_Tabela) throws Exception {
		List logs = null;
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			logs = obPersistencia.consultarLog(codigo, tabela, dataInicial, dataFinal, id_LogTipo, logTipoCodigo, posicao, id_Tabela);
			QuantidadePaginas = (Long) logs.get(logs.size() - 1);
			logs.remove(logs.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return logs;
	}
	
	public LogDt consultarUltimoLog(String logTipoCodigo) throws Exception {
		LogDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new  LogPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarUltimoLog(logTipoCodigo);
			if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public String consultarLogTipoDescricaoJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception{
		String stTemp = "";
		LogTipoNe neObjeto = new LogTipoNe();
		
		stTemp = neObjeto.consultarDescricaoJSON( tempNomeBusca, posicaoPaginaAtual);
		
		neObjeto = null;
		return stTemp;
	}
	
	//Método que recebe a lista de logs do PS e a prepara para enviar para o CT.
	public List obterListaLog(String id, String ano, String tipo) throws Exception {
				
		FabricaConexao obFabricaConexao = null; 
		
		List<LogDt> listaRetorno = new ArrayList<LogDt>();
		List<LogDt> listaFinal = new ArrayList<LogDt>();
		String stTemp[] = new String[3];
		
		try{
			
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);			
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			if(tipo.equalsIgnoreCase("Processo")){
				listaRetorno = obPersistencia.consultarLogProcesso(id, ano);
			}
			else if(tipo.equalsIgnoreCase("Serventia")){
				listaRetorno = obPersistencia.consultarLogServentia(id, ano);
			}
			
			else if(tipo.equalsIgnoreCase("Usuario")){
				
				List listaProvisoria = new ArrayList();				
				List liUsuServ = new UsuarioServentiaNe().consultarDescricaoUsuarioServentia(id);
				
				listaRetorno.addAll(obPersistencia.consultarLogUsuario(id, ano, "Usuario"));
				
				List listaProvisoria2 = new ArrayList();
				List listaProvisoria3 = new ArrayList();
				
				String cadeiaUsuServ1 = "";
				String cadeiaUsuServ2 = "";
				
				for(int k = 0; k < liUsuServ.size(); k++){					
					UsuarioDt usuarioDt = (UsuarioDt) liUsuServ.get(k);					
					if(usuarioDt != null){
						if(usuarioDt.getId_UsuarioServentia() != null && !usuarioDt.getId_UsuarioServentia().equalsIgnoreCase("null")){
							
							if(k == (liUsuServ.size() - 1)){
								cadeiaUsuServ1 += usuarioDt.getId_UsuarioServentia();
								cadeiaUsuServ2 += usuarioDt.getId_UsuarioServentiaGrupo();
							} else {
								cadeiaUsuServ1 += usuarioDt.getId_UsuarioServentia() + ",";
								cadeiaUsuServ2 += usuarioDt.getId_UsuarioServentiaGrupo() + ",";
							}
						}						
					}				
				}
				if(cadeiaUsuServ1 != ""){
					listaProvisoria2 = obPersistencia.consultarLogUsuario(cadeiaUsuServ1, ano, "UsuarioServentia");
					if(listaProvisoria2 != null && !listaProvisoria2.isEmpty()){
						listaRetorno.addAll(listaProvisoria2);
					}
				}
				if(cadeiaUsuServ2 != ""){
					listaProvisoria3 = obPersistencia.consultarLogUsuario(cadeiaUsuServ2, ano, "UsuarioServentiaGrupo");
					if(listaProvisoria3 != null && !listaProvisoria3.isEmpty()){
						listaRetorno.addAll(listaProvisoria3);
					}
				}
			}// FIM DO IF (USUARIO)
			
			else if(tipo.equalsIgnoreCase("Advogado")){
				listaRetorno = obPersistencia.consultarLogAdvogado(id, ano);
			}
			else {
				listaRetorno = null;
			}
			
			//Varre a lista inicial e transforma os atributos do tipo string (param antigo e param novo) em uma lista de objetos do tipo AtributoLogDt.
			for(int i = 0; i < listaRetorno.size(); i++){
				
				LogDt logDt = new LogDt();
				
				logDt.setId( listaRetorno.get(i).getId() );
				logDt.setTabela( listaRetorno.get(i).getTabela() );
				logDt.setId_LogTipo( listaRetorno.get(i).getId_LogTipo() );
				logDt.setLogTipo( listaRetorno.get(i).getLogTipo() );
				logDt.setLogTipoCodigo( listaRetorno.get(i).getLogTipoCodigo() );
				logDt.setData( listaRetorno.get(i).getData() );
				logDt.setHora( listaRetorno.get(i).getHora() );
				logDt.setId_Usuario( listaRetorno.get(i).getId_Usuario() );
				logDt.setUsuario( listaRetorno.get(i).getUsuario() );
				logDt.setNomeUsuario( listaRetorno.get(i).getNomeUsuario() );
				logDt.setIpComputador( listaRetorno.get(i).getIpComputador() );				
				logDt.setCodigoTemp( listaRetorno.get(i).getCodigoTemp() );
				logDt.setId_Tabela( listaRetorno.get(i).getId_Tabela() );
				
				String valorAntigo = listaRetorno.get(i).getValorAtual();
				String valorNovo = listaRetorno.get(i).getValorNovo();
				
				//PRIMEIRA OPÇÃO (tanto valorAntigo quanto valorNovo contêm valores)
				if(valorAntigo.contains(";") && valorNovo.contains(";")){
					
					List<AtributoLogDt> liTemp = new ArrayList<AtributoLogDt>();
								
					valorAntigo = valorAntigo.replace("[", "");
					valorAntigo = valorAntigo.replace("]", "");
					valorAntigo = valorAntigo.replace("; ", ". ");
					valorAntigo = valorAntigo.trim();
					
					String[] va = valorAntigo.split(";");
					
					valorNovo = valorNovo.replace("[", "");
					valorNovo = valorNovo.replace("]", "");
					valorNovo = valorNovo.replace("; ", ". ");
					valorNovo = valorNovo.trim();
										
					String[] vn = valorNovo.split(";");
					
					for(int j = 0; j < va.length; j++){

						AtributoLogDt atributoLogDt = new AtributoLogDt();
						String[] vaTemp = va[j].split(":");
						String[] vnTemp = vn[j].split(":");
						
						atributoLogDt.setNomeCampo(vaTemp[0]);
						
						if(vaTemp.length > 1){
							
							if(vaTemp[1] != null && !vaTemp[1].equalsIgnoreCase("null")){
								
								stTemp = verificarCampoLog(vaTemp[0], vaTemp[1], tipo);
								
								if(((vaTemp[0].indexOf("data") != -1) || (vaTemp[0].indexOf("Data") != -1)) && (vaTemp.length == 4)) { //se o campo for do tipo data, concatena também os minutos e segundos do horário (posições 2 e 3)
									atributoLogDt.setValorAntigo(vaTemp[1] + ":" + vaTemp[2] + ":" + vaTemp[3]);
								}
								else if(stTemp[0].equalsIgnoreCase("alterar")){
									atributoLogDt.setNomeCampo(stTemp[1]);
									atributoLogDt.setValorAntigo(stTemp[2]);
								}
								else {
									atributoLogDt.setValorAntigo(vaTemp[1]);
								}
							}
						}
						else {
							atributoLogDt.setValorAntigo("");
						}
						if(vnTemp.length > 1){
							
							if(vnTemp[1] != null && !vnTemp[1].equalsIgnoreCase("null")){
								
								stTemp = verificarCampoLog(vnTemp[0], vnTemp[1], tipo);
								
								if(((vnTemp[0].indexOf("data") != -1) || (vnTemp[0].indexOf("Data") != -1)) && (vnTemp.length == 4)) { //se o campo for do tipo data, concatena também os minutos e segundos do horário (posições 2 e 3)
									atributoLogDt.setValorNovo(vnTemp[1] + ":" + vnTemp[2] + ":" + vnTemp[3]);
								}
								else if(stTemp[0].equalsIgnoreCase("alterar")){
									atributoLogDt.setNomeCampo(stTemp[1]);
									atributoLogDt.setValorNovo(stTemp[2]);
								}
								else {
									atributoLogDt.setValorNovo(vnTemp[1]);
								}
							}
						}						
						else {
							atributoLogDt.setValorNovo("");
						}						
						if(atributoLogDt.getValorAntigo() != null && atributoLogDt.getValorNovo() != null){
//							if(!atributoLogDt.getValorAntigo().equalsIgnoreCase(atributoLogDt.getValorNovo())){ //se não houver diferença entre os campos, não adiciona essa linha na lista
								if(stTemp[0] != null && !(stTemp[0].equalsIgnoreCase("retirar"))){
									liTemp.add(atributoLogDt);
								}
//							}						
						}
					}					
					logDt.setListaAtributos(liTemp);					
				}//fim do if (1ª opção)
				
				//SEGUNDA OPÇÃO (somente valorNovo contém valores)
				else if(!valorAntigo.contains(";") && valorNovo.contains(";")){
					
					List<AtributoLogDt> liTemp = new ArrayList<AtributoLogDt>();
					
					valorNovo = valorNovo.replace("[", "");
					valorNovo = valorNovo.replace("]", "");
					valorNovo = valorNovo.replace("; ", ". ");
					valorNovo = valorNovo.trim();
					
					String[] vn = valorNovo.split(";");		
					
					for(int j = 0; j < vn.length; j++){
						
						AtributoLogDt atributoLogDt = new AtributoLogDt();
						String[] vnTemp = vn[j].split(":");
						
						atributoLogDt.setNomeCampo(vnTemp[0]);
						atributoLogDt.setValorAntigo("");
						
						if(vnTemp.length > 1){
							
							if(vnTemp[1] != null && !vnTemp[1].equalsIgnoreCase("null")){
								
								stTemp = verificarCampoLog(vnTemp[0], vnTemp[1], tipo);
								
								if(((vnTemp[0].indexOf("data") != -1) || (vnTemp[0].indexOf("Data") != -1)) && (vnTemp.length == 4)) { //se o campo for do tipo data, concatena também os minutos e segundos do horário (posições 2 e 3)
									atributoLogDt.setValorNovo(vnTemp[1] + ":" + vnTemp[2] + ":" + vnTemp[3]);
								}
								else if(stTemp[0] != null && stTemp[0].equalsIgnoreCase("alterar")){
									atributoLogDt.setNomeCampo(stTemp[1]);
									atributoLogDt.setValorNovo(stTemp[2]);
								}
								else {
									atributoLogDt.setValorNovo(vnTemp[1]);
								}
							}
						}
						else{
							atributoLogDt.setValorNovo("");
						}
						if(atributoLogDt.getValorAntigo() != null && atributoLogDt.getValorNovo() != null){
//							if(!atributoLogDt.getValorAntigo().equalsIgnoreCase(atributoLogDt.getValorNovo())){ //se não houver diferença entre os campos, não adiciona essa linha na lista
								if(stTemp[0] != null && !(stTemp[0].equalsIgnoreCase("retirar"))){
									liTemp.add(atributoLogDt);
								}
//							}						
						}
					}					
					logDt.setListaAtributos(liTemp);					
				}//fim do else if (2ª opção)
				
				//TERCEIRA OPÇÃO (somente valorAntigo contém valores)
				else if(valorAntigo.contains(";") && !valorNovo.contains(";")){
					
					List<AtributoLogDt> liTemp = new ArrayList<AtributoLogDt>();
					
					valorAntigo = valorAntigo.replace("[", "");
					valorAntigo = valorAntigo.replace("]", "");
					valorAntigo = valorAntigo.replace("; ", ". ");
					valorAntigo = valorAntigo.trim();
					
					String[] va = valorAntigo.split(";");
					
					for(int j = 0; j < va.length; j++){
							
						AtributoLogDt atributoLogDt = new AtributoLogDt();						
						String[] vaTemp = va[j].split(":");		
						
						atributoLogDt.setNomeCampo(vaTemp[0]);
						atributoLogDt.setValorNovo("");
						
						if(vaTemp.length > 1){							
							
							if(vaTemp[1] != null && !vaTemp[1].equalsIgnoreCase("null")){
								
								stTemp = verificarCampoLog(vaTemp[0], vaTemp[1], tipo);
								
								if(((vaTemp[0].indexOf("data") != -1) || (vaTemp[0].indexOf("Data") != -1)) && (vaTemp.length == 4)) { //se o campo for do tipo data, concatena também os minutos e segundos do horário (posições 2 e 3)
									atributoLogDt.setValorAntigo(vaTemp[1] + ":" + vaTemp[2] + ":" + vaTemp[3]);
								}
								else if(stTemp[0].equalsIgnoreCase("alterar")){
									atributoLogDt.setNomeCampo(stTemp[1]);
									atributoLogDt.setValorAntigo(stTemp[2]);
								}
								else {
									atributoLogDt.setValorAntigo(vaTemp[1]);
								}
							}
						}
						else {
							atributoLogDt.setValorAntigo("");
						}
						if(atributoLogDt.getValorAntigo() != null && atributoLogDt.getValorNovo() != null){
//							if(!atributoLogDt.getValorAntigo().equalsIgnoreCase(atributoLogDt.getValorNovo())){ //se não houver diferença entre os campos, não adiciona essa linha na lista
								if(stTemp[0] != null && !(stTemp[0].equalsIgnoreCase("retirar"))){
									liTemp.add(atributoLogDt);
								}
//							}						
						}
					}					
					logDt.setListaAtributos(liTemp);					
				}//fim do else if (3ª opção)
				listaFinal.add(logDt);				
			}
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaFinal;
	}
	
	public String[] verificarCampoLog(String nomeCampo, String valor, String tipo) throws Exception{
		
		String[] retorno = preencherVetorLog(nomeCampo, valor);
		String[] vetCampos = null; 

		if(tipo.equalsIgnoreCase("Processo")){//caso seja log de processo (deve retirar estes campos. A descrição será obtida através do id correspondente)
			String[] aux = {"ProcessoTipo", "ProcessoPrioridade", "Serventia", "ServentiaOrigem", "Area", "ObjetoPedido", "Classificador", "ProcessoParteTipo", "ProcessoParteAusencia", "CidadeNaturalidade", "EstadoCivil", "Profissao", "Endereco", "RgOrgaoExpedidor", "Escolaridade", "EstadoCtpsUf", "GovernoTipo", "EmpresaTipo", "ServentiaCargo", "CargoTipo", "Grupo", "Bairro", "Cidade", "ProcessoFase", "ProcessoStatus"};
			vetCampos = aux;
		}
		else if(tipo.equalsIgnoreCase("Serventia")){//caso seja log de serventia (deve retirar estes campos. A descrição será obtida através do id correspondente)
			String[] aux = {"Serventia", "ServentiaTipo", "ServentiaSubtipo", "Area", "Comarca", "AreaDistribuicao", "AreaDistribuicaoSecundaria", "EstadoRepresentacao", "AudienciaTipo", "Bairro", "ServentiaCodigo", "ServentiaCodigoExterno", "ServentiaSubTipoCodigo"};
			vetCampos = aux;
		}
		else if(tipo.equalsIgnoreCase("Usuario")){//caso seja log de usuario (deve retirar estes campos. A descrição será obtida através do id correspondente)
			String[] aux = {"Bairro", "Id_Endereco", "Endereco", "Senha", "CtpsEstado", "Naturalidade", "RgOrgaoExpedidor", "Serventia", "Comarca", "BairroServentia", "CidadeServentia", "ServentiaCodigo", "ServentiaCodigoExterno", "Grupo"};
			vetCampos = aux;
		}
		else if(tipo.equalsIgnoreCase("Advogado")){//caso seja log de advogado (deve retirar estes campos. A descrição será obtida através do id correspondente)
			String[] aux = {"Bairro", "Id_Endereco", "Endereco", "Senha", "CtpsEstado", "Naturalidade", "RgOrgaoExpedidor", "Serventia", "Comarca", "BairroServentia", "CidadeServentia", "ServentiaCodigo", "ServentiaCodigoExterno", "Grupo"};
			vetCampos = aux;
		}
		
		if(vetCampos != null){			
			for(int i = 0; i < vetCampos.length; i++){
				if(vetCampos[i].equalsIgnoreCase(nomeCampo)){
					retorno = preencherVetorLog("retirar", "");
					break;
				}
			}			
		}
		return retorno;
	}
	
	public String[] preencherVetorLog(String nomeCampo, String valor) throws Exception  {
		
		String[] vetRetorno = new String[3];
		
		switch (nomeCampo) {
		
		case "manter":
			vetRetorno[0] = nomeCampo;
			vetRetorno[1] = "";
			vetRetorno[2] = "";				
			break;
			
		case "retirar":
			vetRetorno[0] = nomeCampo;
			vetRetorno[1] = "";
			vetRetorno[2] = "";				
			break;
		
		case "Id_ProcessoTipo":
			if(valor != null && !valor.isEmpty()){
				ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarId(valor);
				if(processoTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Tipo (id)";
					vetRetorno[2] = processoTipoDt.getProcessoTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}
			}
			break;

		case "Id_ProcessoFase":
			if(valor != null && !valor.isEmpty()){
				ProcessoFaseDt processoFaseDt = new ProcessoFaseNe().consultarId(valor);
				if(processoFaseDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Fase (id)";
					vetRetorno[2] = processoFaseDt.getProcessoFase() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}
			}
			break;
			
		case "Id_ProcessoStatus":
			if(valor != null && !valor.isEmpty()){
				ProcessoStatusDt processoStatusDt = new ProcessoStatusNe().consultarId(valor);
				if(processoStatusDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Status (id)";
					vetRetorno[2] = processoStatusDt.getProcessoStatus() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ProcessoPrioridade":
			if(valor != null && !valor.isEmpty()){
				ProcessoPrioridadeDt processoPrioridadeDt = new ProcessoPrioridadeNe().consultarId(valor);
				if(processoPrioridadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Prioridade (id)";
					vetRetorno[2] = processoPrioridadeDt.getProcessoPrioridade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Serventia":
			if(valor != null && !valor.isEmpty()){
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia (id)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ServentiaOrigem":
			if(valor != null && !valor.isEmpty()){
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia Origem (id)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Area":
			if(valor != null && !valor.isEmpty()){
				AreaDt areaDt = new AreaNe().consultarId(valor);
				if(areaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Área (id)";
					vetRetorno[2] = areaDt.getArea() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ObjetoPedido":
			if(valor != null && !valor.isEmpty()){
				ObjetoPedidoDt objetoPedidoDt = new ObjetoPedidoNe().consultarId(valor);
				if(objetoPedidoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Objeto Pedido (id)";
					vetRetorno[2] = objetoPedidoDt.getObjetoPedido() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Classificador":				
			if(valor != null && !valor.isEmpty()){					
				ClassificadorDt classificadorDt = new ClassificadorNe().consultarId(valor);					
				if(classificadorDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Classificador (id)";							
					vetRetorno[2] = classificadorDt.getClassificador() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}
			}
			break;
			
		case "ForumCodigo":
			if(valor != null && !valor.isEmpty()){
				ForumDt forumDt = new ForumNe().consultarForumCodigo(valor);
				if(forumDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Fórum (cod)";
					vetRetorno[2] = forumDt.getForum() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ProcessoTipoCodigo":
			if(valor != null && !valor.isEmpty()){
				ProcessoTipoDt processoTipoDt = new ProcessoTipoNe().consultarProcessoTipoCodigo(valor);
				if(processoTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Tipo (cod)";
					vetRetorno[2] = processoTipoDt.getProcessoTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ProcessoFaseCodigo":
			if(valor != null && !valor.isEmpty()){
				ProcessoFaseDt processoFaseDt = new ProcessoFaseNe().consultarProcessoFaseCodigo(Integer.parseInt(valor));
				if(processoFaseDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Fase (cod)";
					vetRetorno[2] = processoFaseDt.getProcessoFase() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ProcessoStatusCodigo":
			if(valor != null && !valor.isEmpty()){
				ProcessoStatusDt processoStatusDt = new ProcessoStatusNe().consultarProcessoStatusCodigo(Integer.parseInt(valor));
				if(processoStatusDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Status (cod)";
					vetRetorno[2] = processoStatusDt.getProcessoStatus() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ProcessoPrioridadeCodigo":
			if(valor != null && !valor.isEmpty()){
				ProcessoPrioridadeDt processoPrioridadeDt = new ProcessoPrioridadeNe().consultarProcessoPrioridadeCodigo(valor);
				if(processoPrioridadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Prioridade (cod)";
					vetRetorno[2] = processoPrioridadeDt.getProcessoPrioridade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ServentiaCodigo":
			if(valor != null && !valor.isEmpty()){
				ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia (cod)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ServentiaOrigemCodigo":
			if(valor != null && !valor.isEmpty()){
				ServentiaDt serventiaDt = new ServentiaNe().consultarServentiaCodigo(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia Origem (cod)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "AreaCodigo":
			if(valor != null && !valor.isEmpty()){
				AreaDt areaDt = new AreaNe().consultarAreaCodigo(Integer.parseInt(valor));
				if(areaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Área (cod)";
					vetRetorno[2] = areaDt.getArea() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ObjetoPedidoCodigo":
			if(valor != null && !valor.isEmpty()){
				ObjetoPedidoDt objetoPedidoDt = new ObjetoPedidoNe().consultarObjetoPedidoCodigo(valor);
				if(objetoPedidoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Objeto Pedido (cod)";
					vetRetorno[2] = objetoPedidoDt.getObjetoPedido() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "Id_AreaDistribuicao":
			if(valor != null && !valor.isEmpty()){
				AreaDistribuicaoDt areaDistribuicaoDt = new AreaDistribuicaoNe().consultarId(valor);
				if(areaDistribuicaoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Área Distribuição Primária (id)";
					vetRetorno[2] = areaDistribuicaoDt.getAreaDistribuicao() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_AreaDistribuicaoSecundaria":
			if(valor != null && !valor.isEmpty()){
				AreaDistribuicaoDt areaDistribuicaoDt = new AreaDistribuicaoNe().consultarId(valor);
				if(areaDistribuicaoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Área Distribuição Secundária (id)";
					vetRetorno[2] = areaDistribuicaoDt.getAreaDistribuicao() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ProcessoParteTipo":
			if(valor != null && !valor.isEmpty()){
				ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarId(valor);
				if(processoParteTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Parte Tipo (id)";
					vetRetorno[2] = processoParteTipoDt.getProcessoParteTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ProcessoParteAusencia":
			if(valor != null && !valor.isEmpty()){
				ProcessoParteAusenciaDt processoParteAusenciaDt = new ProcessoParteAusenciaNe().consultarId(valor);
				if(processoParteAusenciaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Parte Ausência (id)";
					vetRetorno[2] = processoParteAusenciaDt.getProcessoParteAusencia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Naturalidade":
			if(valor != null && !valor.isEmpty()){
				CidadeDt cidadeDt = new CidadeNe().consultarId(valor);
				if(cidadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Naturalidade (id)";
					vetRetorno[2] = cidadeDt.getCidade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_EstadoCivil":
			if(valor != null && !valor.isEmpty()){
				EstadoCivilDt estadoCivilDt = new EstadoCivilNe().consultarId(valor);
				if(estadoCivilDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Estado Civil (id)";
					vetRetorno[2] = estadoCivilDt.getEstadoCivil() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Profissao":
			if(valor != null && !valor.isEmpty()){
				ProfissaoDt profissaoDt = new ProfissaoNe().consultarId(valor);
				if(profissaoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Profissão (id)";
					vetRetorno[2] = profissaoDt.getProfissao() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;			
			
		case "Id_RgOrgaoExpedidor":
			if(valor != null && !valor.isEmpty()){
				RgOrgaoExpedidorDt rgOrgaoExpedidorDt = new RgOrgaoExpedidorNe().consultarId(valor);
				if(rgOrgaoExpedidorDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "RG Órgão Expedidor (id)";
					vetRetorno[2] = rgOrgaoExpedidorDt.getSigla() + " - " + rgOrgaoExpedidorDt.getRgOrgaoExpedidor() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Escolaridade":
			if(valor != null && !valor.isEmpty()){
				EscolaridadeDt escolaridadeDt = new EscolaridadeNe().consultarId(valor);
				if(escolaridadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Escolaridade (id)";
					vetRetorno[2] = escolaridadeDt.getEscolaridade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_CtpsUf":
			if(valor != null && !valor.isEmpty()){
				EstadoDt estadoDt = new EstadoNe().consultarId(valor);
				if(estadoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "UF Ctps (id)";
					vetRetorno[2] = estadoDt.getUf() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
		
		case "Id_GovernoTipo":
			if(valor != null && !valor.isEmpty()){
				GovernoTipoDt governoTipoDt = new GovernoTipoNe().consultarId(valor);
				if(governoTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Governo Tipo (id)";
					vetRetorno[2] = governoTipoDt.getGovernoTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_EmpresaTipo":
			if(valor != null && !valor.isEmpty()){
				EmpresaTipoDt empresaTipoDt = new EmpresaTipoNe().consultarId(valor);
				if(empresaTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Empresa Tipo (id)";
					vetRetorno[2] = empresaTipoDt.getEmpresaTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "ProcessoParteTipoCodigo":
			if(valor != null && !valor.isEmpty()){
				ProcessoParteTipoDt processoParteTipoDt = new ProcessoParteTipoNe().consultarProcessoParteTipoCodigo(valor);
				if(processoParteTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Parte Tipo (cod)";
					vetRetorno[2] = processoParteTipoDt.getProcessoParteTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "ProcessoParteAusenciaCodigo":
			if(valor != null && !valor.isEmpty()){
				ProcessoParteAusenciaDt processoParteAusenciaDt = new ProcessoParteAusenciaNe().consultarId(valor);
				if(processoParteAusenciaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Processo Parte Ausência (cod)";
					vetRetorno[2] = processoParteAusenciaDt.getProcessoParteAusencia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "Id_Raca":
			if(valor != null && !valor.isEmpty()){
				RacaDt racaDt = new RacaNe().consultarId(valor);
				if(racaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Raça (id)";
					vetRetorno[2] = racaDt.getRaca() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;		
			
		case "Id_ServentiaCargo":
			if(valor != null && !valor.isEmpty()){
				UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();
				vetRetorno[0] = "alterar";
				vetRetorno[1] = "Serventia Cargo (id)";
				vetRetorno[2] = usuarioServentiaNe.consultarNomePendenciaResponsavel(valor) + " (" + valor + ")";;				
			}
			break;
			
		case "Id_CargoTipo":
			if(valor != null && !valor.isEmpty()){
				CargoTipoDt cargoTipoDt = new CargoTipoNe().consultarId(valor);
				if(cargoTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Cargo Tipo (id)";
					vetRetorno[2] = cargoTipoDt.getCargoTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Grupo":
			if(valor != null && !valor.isEmpty()){
				GrupoDt grupoDt = new GrupoNe().consultarId(valor);
				if(grupoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Grupo (id)";
					vetRetorno[2] = grupoDt.getGrupo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "GrupoCodigo":
			if(valor != null && !valor.isEmpty()){
				GrupoDt grupoDt = new GrupoNe().consultarGrupoCodigo(valor);
				if(grupoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Grupo (cod)";
					vetRetorno[2] = grupoDt.getGrupo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}																
			}
			break;
			
		case "CargoTipoCodigo":
			if(valor != null && !valor.isEmpty() && !valor.equals("0")){ //CargoTipoCodigo Não pode ser zero.
				CargoTipoDt cargoTipoDt = new CargoTipoNe().consultarCargoTipoCodigo(valor);
				if(cargoTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Cargo Tipo (cod)";
					vetRetorno[2] = cargoTipoDt.getCargoTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}					
			}
			break;

		case "Id_Bairro":
			if(valor != null && !valor.isEmpty()){
				BairroDt bairroDt = new BairroNe().consultarId(valor);
				if(bairroDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Bairro (id)";
					vetRetorno[2] = bairroDt.getBairro() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "BairroCodigo":
			if(valor != null && !valor.isEmpty()){
				BairroDt bairroDt = new BairroNe().consultarBairroCodigo(valor);
				if(bairroDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Bairro (cod)";
					vetRetorno[2] = bairroDt.getBairro() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}					
			}
			break;
			
		case "Id_Cidade":
			if(valor != null && !valor.isEmpty()){
				CidadeDt cidadeDt = new CidadeNe().consultarId(valor);
				if(cidadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Cidade (id)";
					vetRetorno[2] = cidadeDt.getCidade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "CidadeCodigo":
			if(valor != null && !valor.isEmpty()){
				CidadeDt cidadeDt = new CidadeNe().consultarCidadeCodigo(valor);
				if(cidadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Cidade (cod)";
					vetRetorno[2] = cidadeDt.getCidade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}					
			}
			break;
			
		case "EstadoCodigo":
			if(valor != null && !valor.isEmpty()){
				EstadoDt estadoDt = new EstadoNe().consultarEstadoCodigo(valor);
				if(estadoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Estado (cod)";
					vetRetorno[2] = estadoDt.getEstado() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}					
			}
			break;
			
		case "ID_SERV":
			if(valor != null && !valor.isEmpty()){
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia (id)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ServentiaTipo":
			if(valor != null && !valor.isEmpty()){
				ServentiaTipoDt serventiaTipoDt = new ServentiaTipoNe().consultarId(valor);
				if(serventiaTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia Tipo (id)";
					vetRetorno[2] = serventiaTipoDt.getServentiaTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ServentiaSubtipo":
			if(valor != null && !valor.isEmpty()){
				ServentiaSubtipoDt serventiaSubtipoDt = new ServentiaSubtipoNe().consultarId(valor);
				if(serventiaSubtipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia SubTipo (id)";
					vetRetorno[2] = serventiaSubtipoDt.getServentiaSubtipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_Comarca":
			if(valor != null && !valor.isEmpty()){
				ComarcaDt comarcaDt = new ComarcaNe().consultarId(valor);
				if(comarcaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Comarca (id)";
					vetRetorno[2] = comarcaDt.getComarca() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_EstadoRepresentacao":
			if(valor != null && !valor.isEmpty()){
				EstadoDt estadoDt = new EstadoNe().consultarId(valor);
				if(estadoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Estado Representação (id)";
					vetRetorno[2] = estadoDt.getEstado() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_AudienciaTipo":
			if(valor != null && !valor.isEmpty()){
				AudienciaTipoDt audienciaTipoDt = new AudienciaTipoNe().consultarId(valor);
				if(audienciaTipoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Audiência Tipo (id)";
					vetRetorno[2] = audienciaTipoDt.getAudienciaTipo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;

		case "Id_ServentiaRelacionada":
			if((valor != "") && (!valor.equalsIgnoreCase("null"))){
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia Relacionada (id)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_ServentiaSubstituicao":
			if(valor != null && !valor.isEmpty()){
				ServentiaDt serventiaDt = new ServentiaNe().consultarId(valor);
				if(serventiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Serventia Substituição (id)";
					vetRetorno[2] = serventiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;			

		case "Id_BairroServentia":
			if(valor != null && !valor.isEmpty()){
				BairroDt bairroDt = new BairroNe().consultarId(valor);
				if(bairroDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Bairro Serventia (id)";
					vetRetorno[2] = bairroDt.getBairro() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;			
			
		case "Id_CidadeServentia":
			if(valor != null && !valor.isEmpty()){
				CidadeDt cidadeDt = new CidadeNe().consultarId(valor);
				if(cidadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Cidade Serventia (id)";
					vetRetorno[2] = cidadeDt.getCidade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;			
			
		case "ComarcaCodigo":
			if(valor != null && !valor.isEmpty()){
				ComarcaDt comarcaDt = new ComarcaNe().consultarComarcaCodigo(valor);
				if(comarcaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Comarca (cod)";
					vetRetorno[2] = comarcaDt.getComarca() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "BairroCodigoServentia":
			if(valor != null && !valor.isEmpty()){
				BairroDt bairroDt = new BairroNe().consultarBairroCodigo(valor);
				if(bairroDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Bairro (cod)";
					vetRetorno[2] = bairroDt.getBairro() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}					
			}
			break;
			
		case "CidadeCodigoServentia":
			if(valor != null && !valor.isEmpty()){
				CidadeDt cidadeDt = new CidadeNe().consultarCidadeCodigo(valor);
				if(cidadeDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Cidade (cod)";
					vetRetorno[2] = cidadeDt.getCidade() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}					
			}
			break;
		
		case "Id_UsuarioServentia":
			if(valor != null && !valor.isEmpty()){
				UsuarioServentiaDt usuarioServentiaDt = new UsuarioServentiaNe().consultarId(valor);
				if(usuarioServentiaDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Usuário Serventia (id)";
					vetRetorno[2] = usuarioServentiaDt.getServentia() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		case "Id_UsuarioServentiaGrupo":
			if(valor != null && !valor.isEmpty()){
				UsuarioServentiaGrupoDt usuarioServentiaGrupoDt = new UsuarioServentiaGrupoNe().consultarId(valor);
				if(usuarioServentiaGrupoDt != null){
					vetRetorno[0] = "alterar";
					vetRetorno[1] = "Usuário Serventia Grupo (id)";
					vetRetorno[2] = usuarioServentiaGrupoDt.getGrupo() + " (" + valor + ")";
				} else {
					vetRetorno = preencherVetorLog("retirar", "");
				}									
			}
			break;
			
		default:				
			if(valor.equalsIgnoreCase("true")){
				vetRetorno[0] = "alterar";
				vetRetorno[1] = nomeCampo;
				vetRetorno[2] = "Sim";
			}
			else if(valor.equalsIgnoreCase("false")){
				vetRetorno[0] = "alterar";
				vetRetorno[1] = nomeCampo;
				vetRetorno[2] = "Não";
			}
			else {
				vetRetorno = preencherVetorLog("manter", "");
			}				
			break;
		}	
		return vetRetorno;
	}
	
	public List consultarLogPendencias(String idProcesso, String idServCargo) throws Exception {
		
		FabricaConexao obFabricaConexao = null; 
		List listaPendencias = new ArrayList();		
		List listaArquivos = new ArrayList();		
		List listaFinal = new ArrayList();
		
		try{
				
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			listaPendencias = obPersistencia.consultarListaPendencias(idProcesso, idServCargo);
			
			if(listaPendencias.size() > 0){
				
				for(int i = 0; i < listaPendencias.size(); i++){

					String[] stTemp = listaPendencias.get(i).toString().split(":");

					listaArquivos = obPersistencia.consultarListaArquivos(stTemp[0]);
					
					if(listaArquivos.size() > 0){
						
						for(int j = 0; j < listaArquivos.size(); j++){
							
							List liTemp = obPersistencia.consultarLogPendencias((String) listaArquivos.get(j), stTemp[0], stTemp[1]);
							
							if(liTemp.size() > 0){
								
								listaFinal.add(liTemp);								
							}
						}						
					}
				}				
			}
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return listaFinal;
	}

	public String consultarLogErroJSON(String codigo) throws Exception {
		String  logs ="";
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			logs = obPersistencia.consultarLogErroJSON(codigo);			
			
		
		} finally{
			obFabricaConexao.fecharConexao();
		}

		return logs;
	}

	public LogDt consultarIdLogErro(String stId) throws Exception {
		LogDt dtRetorno = null;
		FabricaConexao obFabricaConexao = null; 

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			dtRetorno = obPersistencia.consultarIdLogErro(stId);
			if (dtRetorno != null) obDados.copiar(dtRetorno);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}	
	
	public void salvar(LogDt dados, Connection connection) throws Exception {

		LogPs obPersistencia = new LogPs(connection);

		if (dados.getId().equalsIgnoreCase("")) {
			obPersistencia.inserir(dados);
		} else {
			obPersistencia.alterar(dados);
		}
		obDados.copiar(dados);


	}
	
	/**
	 * Método para consultar o histórico nos logs da guia.
	 * 
	 * @param String idGuiaProjudi
	 * @param String idISNSPG
	 * @param String numeroGuiaCompleto
	 * 
	 * @return List<LogDt>
	 * 
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public List<LogDt> consultarLogsGuia(String idGuiaProjudi, String idISNSPG, String numeroGuiaCompleto) throws Exception {
		List<LogDt> listaLogDt = new ArrayList<LogDt>();
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			listaLogDt = obPersistencia.consultarLogsGuia(idGuiaProjudi, idISNSPG, numeroGuiaCompleto);
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return listaLogDt;
	}
	
	/**
	 * Método que consulta o último log de boleto emitido hoje.
	 * 
	 * @return LogDt
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	public LogDt consultaLogUltimoBoletoEmitidoHoje() throws Exception {
		LogDt logDt = null;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			logDt = obPersistencia.consultaLogUltimoBoletoEmitidoHoje();
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
		
		return logDt;
	}

	public Date getDataHoraBDProducao() throws Exception {
		Date tempDt = null;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			tempDt = obPersistencia.getDataHoraBD();

		}finally {
			obFabricaConexao.fecharConexao();
		}
		
		return tempDt;
	}
	
	public Date getDataHoraBDDataGuard() throws Exception {
		Date tempDt = null;
		
		FabricaConexao obFabricaConexao = null;
		try {
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			tempDt = obPersistencia.getDataHoraBD();

		}finally {
			obFabricaConexao.fecharConexao();
		}
		
		return tempDt;
	}
	
	//Método que recebe a lista de logs de liberação de acesso ao processo
	public List<String> consultarLogLiberacaoAcesso(String id_Processo, Date data_Inicial, Date data_Final) throws Exception {
			
		List<String> listaLiberacaoAcesso = new ArrayList<String>();
		FabricaConexao obFabricaConexao = null;
		
		try{			
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);			
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			Calendar calInicial = Calendar.getInstance();
			calInicial.setTime(data_Inicial);
			calInicial.set(Calendar.HOUR_OF_DAY, 0);
			calInicial.set(Calendar.MINUTE, 0);
			calInicial.set(Calendar.SECOND, 0);
			calInicial.set(Calendar.MILLISECOND, 0);
			
			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(data_Final);
			calFinal.set(Calendar.HOUR_OF_DAY, 23);
			calFinal.set(Calendar.MINUTE, 59);
			calFinal.set(Calendar.SECOND, 59);
			calFinal.set(Calendar.MILLISECOND, 999);	
			if (calInicial.getTimeInMillis() > calFinal.getTimeInMillis()) {
				throw new MensagemException("A data inicial deve ser menor que data final.");
			}
			if (calInicial.get(Calendar.YEAR)!=calFinal.get(Calendar.YEAR)) {
				throw new MensagemException("As datas devem estar dentro do mesmo ano, se necessário faça duas ou mais pesquisas para incorporar anos diferentes.");
			}
			
			listaLiberacaoAcesso = obPersistencia.consultarLogLiberacaoAcesso(id_Processo, calInicial.getTime(), calFinal.getTime());
		}
		finally{
			obFabricaConexao.fecharConexao();
		}		
		return listaLiberacaoAcesso;
	}
	
	//Método que recebe a lista de logs de registros de acesso a arquivos do processo
	public List<String> consultarLogAcessoArquivo(String id_Processo, Date data_Inicial, Date data_Final) throws Exception {
		
		List<String> listaAcessoArquivo = new ArrayList<String>();
		FabricaConexao obFabricaConexao = null;
		
		try{			
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);			
			LogPs obPersistencia = new LogPs(obFabricaConexao.getConexao());
			
			Calendar calInicial = Calendar.getInstance();
			calInicial.setTime(data_Inicial);
//			calInicial.set(Calendar.HOUR_OF_DAY, 0);
//			calInicial.set(Calendar.MINUTE, 0);
//			calInicial.set(Calendar.SECOND, 0);
//			calInicial.set(Calendar.MILLISECOND, 0);
//			
//			Calendar calFinal = Calendar.getInstance();
//			calFinal.setTime(data_Final);
//			calFinal.set(Calendar.HOUR_OF_DAY, 23);
//			calFinal.set(Calendar.MINUTE, 59);
//			calFinal.set(Calendar.SECOND, 59);
//			calFinal.set(Calendar.MILLISECOND, 999);					

			listaAcessoArquivo = obPersistencia.consultarLogAcessoArquivo(id_Processo, data_Inicial, data_Final, calInicial.get(Calendar.YEAR));	
		}
		finally {
			obFabricaConexao.fecharConexao();
		}		
		return listaAcessoArquivo;
	}
}