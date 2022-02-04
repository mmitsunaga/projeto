package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.EscalaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoDt;
import br.gov.go.tj.projudi.dt.ServentiaCargoEscalaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.ServentiaCargoEscalaPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.PreparedStatementTJGO;

public class ServentiaCargoEscalaNe extends ServentiaCargoEscalaNeGen {

    private static final long serialVersionUID = 522682300471399492L;

    
    /**
     * Método de para salvar ServentiaCargoEscalaDt.
     * Tb salva o histórico de status do ServentiaCargoEscalaDt. Caso o outro parâmetro idServentiaCargoEscalaStatus seja diferente do 
     * idServentiaCargoEscalaStatus corrente, ele é gravado.
     * @param ServentiaCargoEscalaDt
     * @throws Exception
     */
    public void salvar(ServentiaCargoEscalaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ServentiaCargoEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			} else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("ServentiaCargoEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
    
    public String Verificar(ServentiaCargoEscalaDt dados) {
        String stRetorno = "";
        if (dados.getId_ServentiaCargo().equalsIgnoreCase("")) {
            stRetorno += "\nUsuário Serventia é campo obrigatório. ";
        }
        if (dados.getId_Escala().equalsIgnoreCase("")) {
            stRetorno += "\nEscala é campo obrigatório. ";
        }
        if (dados.getServentiaCargoEscalaStatusDt().getId().equalsIgnoreCase("")) {
            stRetorno += "\nStatus é campo obrigatório. ";
        }

        return stRetorno;
    }

    public List consultarDescricao(String descricao, String posicao) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        List tempList = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarServentiaCargoEscala(descricao, posicao);
            setQuantidadePaginas(tempList);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }

    /**
     * Consulta lista de ServentiaCargoEscalaDt pelo Id da Escala.
     * @param String id_escala
     * @return List<ServentiaCargoEscalaDt>
     * @throws Exception
     */
    public List consultarUsuariosServentiaEscala(String id_escala) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        List liTemp = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            liTemp = obPersistencia.consultarUsuariosServentiaEscala(id_escala);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return liTemp;
    }
    
    public String consultarUsuariosServentiaEscala(String id_servCargo, String id_escala) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        String id = "";
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            id = obPersistencia.consultarUsuariosServentiaEscala(id_servCargo, id_escala);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return id;
    }
    
    /**
     * Método que ativa ou desativa um usuário serventia escala.
     * @param ServentiaCargoDt
     * @param EscalaDt
     * @param int flag
     * @return boolean
     * @throws Exception
     */
    public boolean ativaDesativaUsuarioEscala(UsuarioServentiaDt usuarioServentiaDt, EscalaDt escalaDt, int flag) throws Exception {
    	FabricaConexao obFabricaConexao = null;
    	boolean retorno = false;
    	try{
    		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    		ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            retorno = obPersistencia.ativaDesativaUsuarioEscala(usuarioServentiaDt, escalaDt, flag);
    	
    	}
    	finally{
    		obFabricaConexao.fecharConexao();
    	}
    	
    	return retorno;
    }
    
    public void excluir(ServentiaCargoEscalaDt dados) throws Exception {

    	FabricaConexao obFabricaConexao = null;
		LogDt obLogDt;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ServentiaCargoEscala", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
    
    
    /**
     * Método de consulta por Id do ServentiaCargoEscala.
     * @param String id_ServentiaCargoescala
     * @return ServentiaCargoEscalaDt
     * @throws Exception
     */
    public ServentiaCargoEscalaDt consultarId(String id_ServentiaCargoescala) throws Exception {

		ServentiaCargoEscalaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_ServentiaCargoescala ); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
    
    public ServentiaCargoEscalaDt consultarIdServentiaCargo(String id_ServentiaCargo) throws Exception {

		ServentiaCargoEscalaDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs( obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarIdServentiaCargo(id_ServentiaCargo); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

    /**
     * Método para recarregar a lista de distribuicao de mandado quando for alterado o ServentiaCargo.
     * @throws Exception
     */
	public void recarregarListaDistribuicaoMandado() throws Exception {

		DistribuirMandadoNe distribuirMandadoNe = new DistribuirMandadoNe();
		distribuirMandadoNe.recarregarListaDistribuicaoMandado();

	}
	
	/**
	 * Método para inserir ServentiaCargoEscalaStatusHistórico apartir do módulo de ServentiaCargoEscala.
	 * @param ServentiaCargoEscalaDt
	 * @param String idServentiaCargoEscalaStatus
	 * @throws Exception
	 */
	public void inserirServentiaCargoEscalaHistorico(ServentiaCargoEscalaDt serventiaCargoEscalaDt) throws Exception {
		
		ServentiaCargoEscalaHistoricoNe serventiaCargoEscalaHistoricoNe = new ServentiaCargoEscalaHistoricoNe();
		
		serventiaCargoEscalaHistoricoNe.inserir(serventiaCargoEscalaDt);
		
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        String stTemp = "";
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarServentiaCargoEscalaJSON(descricao, posicao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
	
	public String consultarCargosServentiaJSON(String idServentia, String descricao, String posicao) throws Exception {
		String stTemp = "";
        ServentiaCargoNe serventiaCargoNe = new ServentiaCargoNe();
        stTemp = serventiaCargoNe.consultarCargosServentiaJSON(idServentia, descricao, posicao);
        
        return stTemp;
    }
	
	/**
	 * Método que consulta a lista de Usuario Serventia Escala cadastrados para determinada Escala.
	 * @param idEscala - ID da Escala
	 * @return lista de usuários
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarUsuarioServentiaEscalaPorEscala(String idEscala) throws Exception {
    	FabricaConexao obFabricaConexao = null;
    	List tempList = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarServentiaCargoEscalaPorEscala(idEscala);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }
	
	
	/**
	 * Método que consulta a lista de Usuario Serventia Escala cadastrados para determinada Escala.
	 * @param idEscala - ID da Escala
	 * @return lista de usuários
	 * @throws Exception
	 */
	public List consultarUsuarioServentiaEscalaAtivoSuspensoPorEscala(String idEscala) throws Exception {
    	FabricaConexao obFabricaConexao = null;
    	List tempList = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarServentiaCargoEscalaAtivoSuspensoPorEscala(idEscala);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }
	
	public String consultarServentiasHabilitacaoAdvogadoJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe();
		stTemp = Serventiane.consultarServentiasHabilitacaoAdvogadoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarServentiasCentralMandadosJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ServentiaNe Serventiane = new ServentiaNe();
		stTemp = Serventiane.consultarServentiasCentralMandadosJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarEscalaJSON(String tempNomeBusca, String idServ, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		EscalaNe escalaNe = new EscalaNe();
		stTemp = escalaNe.consultarDescricaoJSON(tempNomeBusca, idServ, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public String consultarServentiaCargoEscalaStatusJSON(String tempNomeBusca, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		ServentiaCargoEscalaStatusNe serventiaCargoEscalaStatusNe = new ServentiaCargoEscalaStatusNe();
		stTemp = serventiaCargoEscalaStatusNe.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}

	/**
	* Método que desativa um usuário serventia em todas as escalas.
	* @param idServCargo
	* @return boolean
	* @throws Exception
	* @author hrrosa
	*/
	public void desativaServCargoTodasEscalas(String idServCargo, FabricaConexao obFabricaConexao) throws Exception {
		if(obFabricaConexao == null || idServCargo == null) {
			throw new MensagemException("Parâmetros inválidos");
		}
		
		ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
		obPersistencia.desativaServCargoTodasEscalas(idServCargo);
	}

	/**
	* Método que ativa um usuário serventia em todas as escalas.
	* @param idServCargo
	* @return boolean
	* @throws Exception
	* @author hrrosa
	*/
	public void ativaServCargoTodasEscalas(String idServCargo, FabricaConexao obFabricaConexao) throws Exception {
		if(obFabricaConexao == null || idServCargo == null) {
			throw new MensagemException("Parâmetros inválidos");
	 	}
		ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
		obPersistencia.ativaServCargoTodasEscalas(idServCargo);
	}
 
	public List<EscalaDt> consultarServentiaCargoEscalaPorServentiaCargo(String idServentiaCargo, FabricaConexao obFabricaConexao) throws Exception {
		List<EscalaDt>  liTemp = null;
		
		ServentiaCargoEscalaPs serventiaCargoEscalaPs = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
		liTemp = serventiaCargoEscalaPs.consultarServentiaCargoEscalaPorServentiaCargo(idServentiaCargo);
		
		return liTemp;
	}
	
	/**
	 * Consulta os oficiais cadastrados na escala Ad Hoc da central de mandados especificada pelo idServ
	 * @param descricao
	 * @param posicao
	 * @param idServ
	 * @return
	 * @throws Exception
	 * @author hrrosa
	 */
	public String consultarOficialAdhocJSON(String descricao, String posicao, String idServ) throws Exception {
		FabricaConexao obFabricaConexao = null;
    	String resultado = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
            resultado = obPersistencia.consultarOficialAdhocJSON(descricao, posicao, idServ);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return resultado;
	}
	
	/**
	 * Quando ao expedir o mandado opta-se por escolher nominalmente um oficial Ad hoc,
	 * este método é utilizado para retornar os dados deste oficial que são necessários
	 * para registrar esta escolha na expedição. O idServentiaCargoEscala é referente ao
	 * oficial Ad hoc escohido.
	 * 	
	 * @param idServentiaCargoEscala
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * 
	 * @author hrrosa
	 */
	public String[] retornarOficialAdhocEscolhido(String idServentiaCargoEscala, FabricaConexao obFabricaConexao) throws Exception {	
		ServentiaCargoEscalaPs serventiaCargoEscalaPs = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
		return serventiaCargoEscalaPs.retornarOficialAdhocEscolhido(idServentiaCargoEscala, obFabricaConexao);
	}
	
	/**
	 * Desativa serventia cargo escala.
	 * @param idServCargoEsc
	 * @throws Exception
	 * @author hrrosa
	 */
    public void desativaServentiaCargoEscala(String idServCargoEsc) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
	        obPersistencia.desativaServentiaCargoEscala(idServCargoEsc);
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
	/**
	 * Ativa serventia cargo escala.
	 * @param idServCargoEsc
	 * @throws Exception
	 * @author hrrosa
	 */
    public void ativaServentiaCargoEscala(String idServCargoEsc) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
	        obPersistencia.ativaServentiaCargoEscala(idServCargoEsc);
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
    /**
	 * Método que ativa todas as escalas de um oficial.
	 * 
	 * @param idUsuario
	 * @param idServentia 
	 * @param fabrica
	 * @return void
	 * @throws Exception
	 * @author Fernando Meireles
	 */
	public void ativaServentiaCargoTodasEscalas(String idUsuServ, FabricaConexao obFabricaConexao) throws Exception {
		ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(obFabricaConexao.getConexao());
		obPersistencia.ativaServentiaCargoTodasEscalas(idUsuServ);
	}
	
	/**
	 * Método que desativa escalas de um oficial dependendo do afastamento escolhido.
	 * 
	 * @param idUsuServ
	 * @param idAfastamento
	 * @param Fabrica Conexao
	 * @return void
	 * @throws Exception
	 * @author Fernando Meireles
	 */
	
	public void desativaServCargoEscAfastamento(String idUsuServ, String idAfastamento, FabricaConexao objFc)
			throws Exception {
			ServentiaCargoEscalaPs obPersistencia = new ServentiaCargoEscalaPs(objFc.getConexao());
			obPersistencia.desativaServCargoEscalaAfastamento(idUsuServ, idAfastamento);
	}
}

    
    
    
    
    
    
    
    
    
