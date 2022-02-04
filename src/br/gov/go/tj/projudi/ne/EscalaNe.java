package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.EscalaTipoDt;
import br.gov.go.tj.projudi.dt.GrupoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoTipoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaStatusDt;
import br.gov.go.tj.projudi.ps.EscalaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class EscalaNe extends EscalaNeGen {

    private static final long serialVersionUID = -8569707542611844184L;

    public static final int SUCESSO = 1;
	public static final int SEM_SUCESSO = 2;
	public static final int JA_EXISTE = 3;
	public static final int ERRO = 4;
    
    /**
	 * Verificar campos obrigatórios
	 */
	public String Verificar(EscalaDt dados) {
		StringBuffer stRetorno = new StringBuffer("");

		if (!dados.isPlantao() && !dados.isAdhoc()) {
			if (dados.getId_EscalaTipo().equalsIgnoreCase("")) {
				stRetorno.append("Área é campo obrigatório.\n\n ");
			}
			if (dados.getId_MandadoTipo().equalsIgnoreCase("")) {
				stRetorno.append("Tipo de Mandado é campo obrigatório.\n\n ");
			}
			if (dados.getId_Regiao().equalsIgnoreCase("")) {
				stRetorno.append("Região é campo obrigatório.\n\n ");
			}
			if (dados.getQuantidadeMandado().equalsIgnoreCase("")
					|| dados.getQuantidadeMandado().equalsIgnoreCase("0")) {
				dados.setQuantidadeMandado("1");
			}
		} else
			dados.setQuantidadeMandado("1");

		if (dados.getId_Serventia().equalsIgnoreCase("")) {
			stRetorno.append("Serventia é campo obrigatório.\n\n ");
		}
		if (dados.getAtivo().equalsIgnoreCase("")) {
			stRetorno.append("Ativa é campo obrigatório.\n\n ");
		}
		return stRetorno.toString();
	}

	/**
	 * Buscar dados das escalas cadastradas
	 * @throws Exception 
	 */
	public List consultarDescricao(String descricao, String posicao) throws Exception{
		List tempList = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarEscalas(descricao, posicao);
			QuantidadePaginas = (Long) tempList.get(tempList.size() - 1);
			tempList.remove(tempList.size() - 1);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;
	}
	
	/**
	 * Método para Consultar Escala.
	 * @param mandadoTipoDt
	 * @param EscalaTipoDt
	 * @param regiaoDt
	 * @param serventiaDt
	 * @return EscalaDt
	 * @throws Exception
	 */
	public EscalaDt consultarEscala(MandadoTipoDt mandadoTipoDt, EscalaTipoDt EscalaTipoDt, RegiaoDt regiaoDt) throws Exception{
		EscalaDt escalaDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			
			escalaDt = obPersistencia.consultarEscala(mandadoTipoDt, EscalaTipoDt, regiaoDt);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return escalaDt;
	}
	
	/**
	 * Método para consultar escala.
	 * @param String idBairro
	 * @param String idServentia
	 * @param String idMandadoTipo
	 * @return EscalaDt
	 * @throws Exception
	 */
	public EscalaDt consultarEscala(String idBairro, String idServentia, String idMandadoTipo) throws Exception{
		EscalaDt escalaDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			
			escalaDt = obPersistencia.consultarEscala(idBairro, idServentia, idMandadoTipo);
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return escalaDt;
	}
	
	public String consultarIdEscala(String idRegiao, String idServentia, String idMandadoTipo, FabricaConexao fabConexao) throws Exception {
		String idEscala = "";
		
		EscalaPs obPersistencia = new EscalaPs(fabConexao.getConexao());
		idEscala = obPersistencia.consultarIdEscala(idRegiao, idServentia, idMandadoTipo);
		
		return idEscala;
	}

	/**
	 * Método de consulta das escalas ativas
	 * @return List
	 * @throws Exception
	 */
	public List consultarEscalasAtivas() throws Exception{
		List liTemp = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			
			liTemp = obPersistencia.consultarEscalasAtivas();
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return liTemp;
	}

	/**
	 * Método para Salvar Escala.
	 * @param EscalaDt
	 * @return int
	 * @throws Exception
	 */
	public String salvarEscala(EscalaDt escalaDt) throws Exception {
		
		String retorno = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());

			ServentiaNe serventiaNe = new ServentiaNe();
			ServentiaDt serventiaDt = null;

			RegiaoNe regiaoNe = new RegiaoNe();
			RegiaoDt regiaoDt = null;

			EscalaTipoNe escalaTipoNe = new EscalaTipoNe();
			EscalaTipoDt escalaTipoDt = null;

			MandadoTipoNe mandadoTipoNe = new MandadoTipoNe();
			MandadoTipoDt mandadoTipoDt = null;

			String idRegiao = "";

			String idEsc = null;

			serventiaDt = serventiaNe.consultarId(escalaDt.getId_Serventia());

			if (serventiaDt.isCentralMandados()) {

				// Fluxo para criação de escala de plantão e Adhoc:
				// 1) Colocar a área Cível
				// 2) Colocar o tipo de mandado como "Comum"
				// 3) Colocar a quantidade de mandados como "1"
				// 4) Pegar a primeira região da comarca no banco e colocar ela
				// Fazer estes passos pois estas informações, no caso de plantão,
				// serão adicionadas somente por causa das restrições de not null
				// do banco mas não serão utilizadas.
				
				if (escalaDt.isPlantao()) {
					//Plantão
					ComarcaNe comarcaNe = new ComarcaNe();
					ComarcaDt comarcaDt = null;
					comarcaDt = comarcaNe.consultarId(serventiaDt.getId_Comarca());
					escalaDt.setEscala(comarcaDt.getComarca() + " - PLANTÃO");
					escalaDt.setId_EscalaTipo(AreaDt.CIVEL);
					escalaDt.setId_MandadoTipo(String.valueOf(MandadoTipoDt.PLANTAO));
					escalaDt.setQuantidadeMandado("1");
					idRegiao = regiaoNe.consultarIdPrimeraRegiaoComarca(serventiaDt.getId_Comarca());
					escalaDt.setId_Regiao(idRegiao);
					idEsc = consultarIdEscalaPlantao(escalaDt.getId_Serventia(), obFabricaConexao);
					if(idEsc == null) {
						obPersistencia.inserir(escalaDt);
						escalaDt.setId(escalaDt.getId());
					} else {
						retorno = "Não é possível criar duas escalas de Plantão para a mesma Central de Mandados, use a escala já existente.";
					}
					
				} else if(escalaDt.isAdhoc()){
					
					//Ad hoc
					ComarcaNe comarcaNe = new ComarcaNe();
					ComarcaDt comarcaDt = null;
					comarcaDt = comarcaNe.consultarId(serventiaDt.getId_Comarca());
					escalaDt.setEscala(comarcaDt.getComarca() + " - AD HOC");
					escalaDt.setId_EscalaTipo(AreaDt.CIVEL);
					escalaDt.setId_MandadoTipo(String.valueOf(MandadoTipoDt.COMUM));
					escalaDt.setQuantidadeMandado("1");
					idRegiao = regiaoNe.consultarIdPrimeraRegiaoComarca(serventiaDt.getId_Comarca());
					escalaDt.setId_Regiao(idRegiao);
					idEsc = consultarIdEscalaAdhoc(escalaDt.getId_Serventia(), obFabricaConexao);
					if (idEsc == null) {
						obPersistencia.inserir(escalaDt);
						escalaDt.setId(escalaDt.getId());
					} else {
						retorno = "Não é possível criar duas escalas de Ad Hoc para a mesma Central de Mandados, use a escala já existente.";
					}
					
				} else if(escalaDt.isTipoEspecialNormal()){
					
					//Normal
					regiaoDt = regiaoNe.consultarId(escalaDt.getId_Regiao());
					escalaTipoDt = escalaTipoNe.consultarId(escalaDt.getId_EscalaTipo());
					mandadoTipoDt = mandadoTipoNe.consultarId(escalaDt.getId_MandadoTipo());
					escalaDt.setEscala(regiaoDt.getComarca() + " - " + regiaoDt.getRegiao() + " - " + escalaTipoDt.getEscalaTipo() + " - " + mandadoTipoDt.getMandadoTipo());
					//Se o dt vier sem um id tenta fazer uma inclusão. 
					if (escalaDt.getId() == null || escalaDt.getId().isEmpty()) {
					
						//Se já existir uma escala criada para os mesmos dados, alerto o usuário para utilizar a escala existente.
						String nomeEsc = consultarNomeEscalaNormal(escalaDt.getId_Regiao(), escalaDt.getId_Serventia(), escalaDt.getId_MandadoTipo(), escalaDt.getId_EscalaTipo(), obFabricaConexao);
						if(nomeEsc != null) {
							retorno = "A escala <b>\"" + nomeEsc + "\"</b> já existe para os mesmos dados informados. Utilize a escala existente para que não existam escalas duplicadas.";
						} else {
							//Se a inclusão for inédita, deixa incluir.
							obPersistencia.inserir(escalaDt);
							escalaDt.setId(escalaDt.getId());
						}
					
					} 
					else {
						//Se o dt vier com um id, faz a alteração de uma escala existente.
						obPersistencia.alterar(escalaDt);
					}
					
				}
				else {
					
					//Nehum dos tipos esperados
					retorno = "Impossível identificar entre Normal, Plantão ou Ad Hoc.";
				}
				
			} else {
				retorno =  "Serventia não e do tipo Central de Mandados.";
			}

		} finally {
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}

	/**
	 * Método para Excluir Escala.
	 * @param EscalaDt dados
	 * @param LogDt logDt
	 * @throws Exception
	 */
	public void excluirEscala(EscalaDt dados, LogDt logDt) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Escala", dados.getId(), logDt.getId_Usuario(),logDt.getIpComputador(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoJSON(String descricao, String idServ, String posicao) throws Exception{
		String stTemp = "";
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarEscalasJSON(descricao, idServ, posicao);
		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	public String consultarDescricaoEscalaTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		EscalaTipoNe escalaTipoNe = new EscalaTipoNe(); 
		stTemp = escalaTipoNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoMandadoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		MandadoTipoNe MandadoTipone = new MandadoTipoNe(); 
		stTemp = MandadoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoServentiaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		ServentiaNe serventiaNe = new ServentiaNe(); 
		stTemp = serventiaNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoBairroJSON(String stNomeBusca1, String stNomeBusca2, String stNomeBusca3, String stNomeBusca4, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = "";
		
		BairroNe Bairrone = new BairroNe(); 
		stTemp = Bairrone.consultarDescricaoJSON(stNomeBusca1, stNomeBusca2, stNomeBusca3, stNomeBusca4, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarDescricaoZonaJSON(String tempNomeBusca1, String tempNomeBusca2, String PosicaoPaginaAtual ) throws Exception {
		String stTemp=null;
		ZonaNe Zonane = new ZonaNe(); 
		stTemp = Zonane.consultarDescricaoJSON(tempNomeBusca1, tempNomeBusca2, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoPorComarcaJSON(String tempNomeBusca, String idComarca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp=null;
		RegiaoNe Regiaone = new RegiaoNe(); 
		stTemp = Regiaone.consultarDescricaoPorComarcaJSON(tempNomeBusca, idComarca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoServentiaCargoEscalaStatusJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp=null;
		ServentiaCargoEscalaStatusNe serventiaCargoEscalaStatusNe = new ServentiaCargoEscalaStatusNe(); 
		stTemp = serventiaCargoEscalaStatusNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String[] redistribuicaoEscala(String id_escala, String id_serv_cargo_esc, FabricaConexao conexao) throws Exception{
		String stTemp[] = null; 
		
		try {
			if(Funcoes.StringToInt(id_escala) != 0) {
				EscalaPs obPersistencia = new EscalaPs(conexao.getConexao());
				stTemp = obPersistencia.redistribuicaoEscala(id_escala, id_serv_cargo_esc);
			} else {
				throw new MensagemException("Escala não localizada");
			}
		} finally {}
		return stTemp;
	}
	
	/**
	 * 
	 * @param id_bairro
	 * @param id_ServentiaCentralMandado
	 * @param id_mandadoTipo
	 * @return {idUsuarioServentia, idServentiaCargo}
	 * @throws Exception
	 */
	public String[] distribuicaoEscala(String id_bairro, String id_ServentiaCentralMandado, String id_MandadoTipo, String id_AreaProcesso,String isAssistencia) throws Exception {
		String stTemp[] = null; 
		String id_regiao = null;
		int escalaTipoCodigo = 999;
		
		FabricaConexao obFabricaConexao = null;
		ZonaBairroRegiaoNe zoNe = new ZonaBairroRegiaoNe();
		
		id_regiao = zoNe.consultarId_Regiao(id_bairro);
		
		//Lança uma MensagemException explicativa caso algum dos dados esteja errado.
		verificaDadosDistribuicaoEscala(id_regiao, isAssistencia, id_MandadoTipo, id_AreaProcesso);
		
		if(isAssistencia.equals("1")){
			escalaTipoCodigo = EscalaTipoDt.ASSISTENCIA;
		} 
		else{
			//Pegar o área do processo para definir o tipo de escala.
			switch(id_AreaProcesso) {
				case AreaDt.CIVEL:
					escalaTipoCodigo = EscalaTipoDt.CIVEL;
					break;
				
				case AreaDt.CRIMINAL:
					escalaTipoCodigo = EscalaTipoDt.CRIMINAL;
					break;
			}
		}
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscalaPs obPersistencia = new EscalaPs(obFabricaConexao.getConexao());
			//Helleno
//			if(obPersistencia.existeEscala(id_regiao, id_ServentiaCentralMandado, id_mandadoTipo)) {
				stTemp = obPersistencia.distribuicaoEscala(id_regiao, id_ServentiaCentralMandado, id_MandadoTipo, escalaTipoCodigo);
//			} else {
//				throw new MensagemException("Não foi possivel localizar uma escala para esse tipo de mandado");
//			}
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
	
	
	/**
	 * Verifica se todos os dados necessários para distribuir o mandado na escala estão preenchidos corretamente.
	 * @param id_regiao
	 * @param isAssistencia
	 * @param id_MandadoTipo
	 * @param id_AreaProcesso
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public void verificaDadosDistribuicaoEscala(String id_regiao, String isAssistencia, String id_MandadoTipo, String id_AreaProcesso) throws Exception {
		String mensagem = "";
		
		if (id_regiao == null){
			mensagem += " Não foi possivel encontrar a região do bairro selecionado.";
		}
		
		if (isAssistencia == null || (!isAssistencia.equals("0") && !isAssistencia.equals("1"))){
			mensagem += " Não foi possível verificar se trata-se de um processo com assistência.";
		}
		
		if(id_MandadoTipo == null || id_MandadoTipo.isEmpty()){
			mensagem += " Não foi possível identificar o tipo do mandado.";
		}
		
		if(id_AreaProcesso == null || id_AreaProcesso.isEmpty()){
			mensagem += " Não foi possível identificar a área do processo.";
		}
		
		if(!mensagem.isEmpty()) {
			throw new MensagemException(" <{ "+ mensagem +" }> Local Exception: " + this.getClass().getName() + ".distribuicaoEscala(): ");
		}	
	}
	
	/**
	 * Método que consulta o nome da Comarca à qual pertence uma determinada Serventia. 
	 * @param idServentia - ID da Serventia
	 * @return nome da Comarca
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarNomeComarca (String idServentia) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe(); 
		ServentiaDt serventiaDt = serventiaNe.consultarId(idServentia);
		return serventiaDt.getComarca();
	}
	
	public String consultarCargosServentiaJSON(String idServentia, String descricao, String posicao) throws Exception {
		String stTemp = "";
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        stTemp = serventiaCargoNe.consultarCargosServentiaJSON(idServentia, descricao, posicao);
        
        return stTemp;
    }
	
	public void inserirServentiaCargoEscalaHistorico(ServentiaCargoEscalaDt serventiaCargoEscalaDt) throws Exception {
		
		ServentiaCargoEscalaHistoricoNe serventiaCargoEscalaHistoricoNe = new ServentiaCargoEscalaHistoricoNe();
		
		serventiaCargoEscalaHistoricoNe.inserir(serventiaCargoEscalaDt);
		
	}
	
	public List consultarServentiaCargoEscalaPorEscala(String idEscala) throws Exception { 
		List tempList=null;
		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe(); 
		tempList = serventiaCargoEscalaNe.consultarUsuarioServentiaEscalaPorEscala(idEscala);
		return tempList;
	}

	public List consultarServentiaCargoEscalaAtivoSuspensoPorEscala(String idEscala) throws Exception { 
		List tempList=null;
		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe(); 
		tempList = serventiaCargoEscalaNe.consultarUsuarioServentiaEscalaAtivoSuspensoPorEscala(idEscala);
		return tempList;
	}
	
	public void excluirServentiaCargoEscala(ServentiaCargoEscalaDt serventiaCargoEscalaDt) throws Exception {
		
		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();
		
		serventiaCargoEscalaNe.excluir(serventiaCargoEscalaDt);
		
	}
	
	public String consultarGrupoEspecificoServentiaJSON(String nomeBusca, String idServentia, String posicaoPaginaAtual) throws Exception { 
		String tempList=null;
		UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe(); 
		tempList = usuarioServentiaNe.consultarGrupoEspecificoServentiaJSON(nomeBusca, idServentia, String.valueOf(GrupoDt.OFICIAL_JUSTICA), posicaoPaginaAtual);
		return tempList;
	}
	
	/**
	 * Método que insere e atualiza os dados do usuáiro serventia escala na escala.
	 * @param idServentiaCargoEscala
	 * @param idServentiaCargo
	 * @param idStatus
	 * @param idEscala
	 * @param usuarioSessao
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String salvarServentiaCargoEscala(String idServentiaCargoEscala, String idServentiaCargo, String idStatus, String idEscala, UsuarioNe usuarioSessao) throws Exception {
		ServentiaCargoEscalaStatusNe serventiaCargoEscalaStatusNe = new ServentiaCargoEscalaStatusNe();
		ServentiaCargoEscalaStatusDt serventiaCargoEscalaStatusDt = serventiaCargoEscalaStatusNe.consultarId(idStatus);
		
		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe(); 
		ServentiaCargoEscalaDt serventiaCargoEscalaDt = new ServentiaCargoEscalaDt();
		serventiaCargoEscalaDt.setId_ServentiaCargo(idServentiaCargo);
		serventiaCargoEscalaDt.setId(idServentiaCargoEscala);
		serventiaCargoEscalaDt.setId_Escala(idEscala);
		serventiaCargoEscalaDt.setServentiaCargoEscalaStatusDt(serventiaCargoEscalaStatusDt);
		//É preciso alterar o status de Ativo igual ao Ativo do usuarioServentiaEscalaStatusDt
		serventiaCargoEscalaDt.setAtivo(serventiaCargoEscalaStatusDt.getAtivo());
		serventiaCargoEscalaDt.setId_UsuarioLog(usuarioSessao.getUsuarioDt().getId());
		serventiaCargoEscalaDt.setIpComputadorLog(usuarioSessao.getUsuarioDt().getIpComputadorLog());
		serventiaCargoEscalaNe.salvar(serventiaCargoEscalaDt);
		
		return serventiaCargoEscalaDt.getId();
	}
	
	/**
	 * Consulta o ID do UsuarioServentiaEscalaStatus pelo código.
	 * @param statusCodigo - codigo do status
	 * @return ID do status
	 * @throws Exception
	 * @author hmgodinho
	 */
	public String consultarIdStatusPorCodigo(String statusCodigo) throws Exception {
		ServentiaCargoEscalaStatusNe usuarioServentiaEscalaStatusNe = new ServentiaCargoEscalaStatusNe();
		return usuarioServentiaEscalaStatusNe.consultarIdStatusPorCodigo(statusCodigo);
	}

	public ServentiaCargoEscalaDt consultarServentiaCargoEscala(String idServentiaCargoEscala) throws Exception {
		ServentiaCargoEscalaNe serventiaCargoEscalaNe = new ServentiaCargoEscalaNe();
		return serventiaCargoEscalaNe.consultarId(idServentiaCargoEscala);
	}
	
	/**
	 * Recebe o id de uma central de mandados e retorna o id da escala de plantão dela.
	 * @param idCentralMandado
	 * @param fabricaConexao
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarIdEscalaPlantao(String idCentralMandado, FabricaConexao fabricaConexao) throws Exception {
		EscalaPs escalaPs = new EscalaPs(fabricaConexao.getConexao());
		return escalaPs.consultarIdEscalaPlantao(idCentralMandado);
	}
	
	/**
	 * Recebe o id de uma central de mandados e retorna o id da escala ad hocdela.
	 * @param idCentralMandado
	 * @param fabricaConexao
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarIdEscalaAdhoc(String idCentralMandado, FabricaConexao fabricaConexao) throws Exception {
		EscalaPs escalaPs = new EscalaPs(fabricaConexao.getConexao());
		return escalaPs.consultarIdEscalaAdhoc(idCentralMandado);
	}
	
	/**
	 * Caso exista uma escala do tipo_especial normal que já contenha os mesmo dados dos parâmetros, retorna seu nome.
	 * caso não encontre, retorna null. Utilizado para garantir que escalas não sejam duplicadas para os mesmos dados. 
	 * @param id_regiao
	 * @param id_ServentiaCentralMandado
	 * @param id_mandadoTipo
	 * @param id_escala_tipo
	 * @param fabricaConexao
	 * @return
	 * @throws Exception
	 */
	public String consultarNomeEscalaNormal(String id_regiao, String id_ServentiaCentralMandado, String id_mandadoTipo, String id_escala_tipo, FabricaConexao fabricaConexao) throws Exception {
		EscalaPs escalaPs = new EscalaPs(fabricaConexao.getConexao());
		return escalaPs.consultarNomeEscalaNormal(id_regiao, id_ServentiaCentralMandado, id_mandadoTipo, id_escala_tipo);
	}
	
    public void desativaServentiaCargoEscala(String idServCargoEsc) throws Exception {
    	new ServentiaCargoEscalaNe().desativaServentiaCargoEscala(idServCargoEsc);
    }
    
    public void ativaServentiaCargoEscala(String idServCargoEsc) throws Exception {
    	new ServentiaCargoEscalaNe().ativaServentiaCargoEscala(idServCargoEsc);
    }
}
