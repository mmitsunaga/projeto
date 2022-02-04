package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.GuiaEmissaoDt;
import br.gov.go.tj.projudi.dt.GuiaTipoDt;
import br.gov.go.tj.projudi.dt.LocomocaoDt;
import br.gov.go.tj.projudi.dt.LocomocaoSPGDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoJudicialDt;
import br.gov.go.tj.projudi.dt.ProcessoDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.LocomocaoPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;

public class LocomocaoNe extends Negocio {

	private static final long serialVersionUID = 5459475128130957118L;
	
	protected  LocomocaoDt obDados;
	
	public LocomocaoNe() {
		obLog = new LogNe(); 

		obDados = new LocomocaoDt(); 
	}
	
	public void salvar(LocomocaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			if (dados.getId() == null || dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Locomocao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Locomocao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void salvar(LocomocaoDt dados, FabricaConexao obFabricaConexao) throws Exception {

		LogDt obLogDt;
		
		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
		if (dados.getId() == null || dados.getId().length()==0) {
			obPersistencia.inserir(dados);
			obLogDt = new LogDt("Locomocao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
		}else {
			obPersistencia.alterar(dados); 
			obLogDt = new LogDt("Locomocao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
		}

		obDados.copiar(dados);
		obLog.salvar(obLogDt, obFabricaConexao);		
	}
	
	public void excluir(LocomocaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Locomocao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public LocomocaoDt consultarId(String id_locomocao ) throws Exception {

		LocomocaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_locomocao ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public List consultarDescricao(String descricao, String posicao ) throws Exception { 
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	/**
     * Método que consulta a LocomocaoDt pelo id do GuiaItem
     * @param String idGuiaItem
     * @return LocomocaoDt
     * @throws Exception
     */
    public LocomocaoDt consultarIdGuiaItem(String idGuiaItem) throws Exception {
    	LocomocaoDt locomocaoDt = null;
    	FabricaConexao obFabricaConexao = null;
    	
    	try{
    		obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
    		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
    		locomocaoDt = obPersistencia.consultarIdGuiaItem(idGuiaItem);
    		
    	}
    	finally{
    		obFabricaConexao.fecharConexao();
    	}
    	
    	return locomocaoDt;
    }
    
    /**
     * Método que consulta a LocomocaoDt pelo id do GuiaItem
     * @param String idGuiaItem
     * @return LocomocaoDt
     * @throws Exception
     */
    public LocomocaoDt consultarIdGuiaItem(String idGuiaItem, FabricaConexao obFabricaConexao) throws Exception {
		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarIdGuiaItem(idGuiaItem);
    }
    
    /**
     * Consulta de Locomoções não utilizadas. [CENTRAL DE MANDADOS DO PROJUDI]
     * @param idProcesso
     * @param descricao
     * @param cidade
     * @param uf
     * @param posicao
     * @return
     * @throws Exception
     */
//    public String consultarLocomocaoNaoUtilizadaJSON(String idProcesso, String descricao, String cidade, String uf, String posicao) throws Exception {
//    	String stTemp = null;
//        FabricaConexao obFabricaConexao = null;
//        try {
//            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//            LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
//            stTemp = obPersistencia.consultarLocomocaoNaoUtilizadaJSON(idProcesso, descricao, cidade, uf, posicao);
//        } finally {
//            obFabricaConexao.fecharConexao();
//        }
//        return stTemp;
//    }
    
    /**
     * Consulta de Locomoções não utilizadas. [CENTRAL DE MANDADOS DO SPG]
     * @param idProcesso
     * @param descricao
     * @param cidade
     * @param uf
     * @param posicao
     * @return
     * @throws Exception
     */
    public String consultarLocomocaoNaoUtilizadaJSON(String idProcesso, String descricao, String cidade, String uf, String zona, String posicao, boolean validaOficialVinculadoLocomocao) throws Exception {
    	return gerarJSON(consultarLocomocaoNaoUtilizada(idProcesso, descricao, cidade, uf, zona, validaOficialVinculadoLocomocao), posicao);
    }
    
    public List<LocomocaoDt> consultarLocomocaoNaoUtilizada(String idProcesso, boolean validaOficialVinculadoLocomocao) throws Exception {
    	return consultarLocomocaoNaoUtilizada(idProcesso, "", "", "", "", validaOficialVinculadoLocomocao);
    }
    
    public List<LocomocaoDt> consultarLocomocaoNaoUtilizada(String idProcesso, String idGuiaEmissao, boolean validaOficialVinculadoLocomocao) throws Exception {
    	List<LocomocaoDt> locomocoesNaoUtilizadasDoProcessoEGuia = new ArrayList<LocomocaoDt>();
    	
    	List<LocomocaoDt> locomocoesNaoUtilizadasDoProcesso = consultarLocomocaoNaoUtilizada(idProcesso, "", "", "", "", validaOficialVinculadoLocomocao);
    	
    	for(LocomocaoDt locomocaoDt : locomocoesNaoUtilizadasDoProcesso) {
    		if (locomocaoDt.getGuiaItemDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt() != null && 
    			locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getId() != null && 
    			idGuiaEmissao != null &&
    			locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getId().trim().equals(idGuiaEmissao.trim())) {
    			locomocoesNaoUtilizadasDoProcessoEGuia.add(locomocaoDt);
    		}
    	}
    	
    	return locomocoesNaoUtilizadasDoProcessoEGuia;
    }
    
    public List<LocomocaoDt> consultarLocomocaoNaoUtilizada(String idProcesso, String descricao, String cidade, String uf, String zona, boolean validaOficialVinculadoLocomocao) throws Exception {
    	List<LocomocaoDt> locomocoesNaoUtilizadasProjudi = new ArrayList<LocomocaoDt>();
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
            
            locomocoesNaoUtilizadasProjudi = obPersistencia.consultarLocomocaoNaoUtilizada(idProcesso, descricao, cidade, uf, zona, true, validaOficialVinculadoLocomocao);            
            if (locomocoesNaoUtilizadasProjudi.size() > 0) {
            	LocomocaoDt locomocaoDt = locomocoesNaoUtilizadasProjudi.get(0);
            	if (locomocaoDt.getGuiaItemDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getFlagGrau() != null) {
            		String comarcaCodigo = null;
            		ServentiaDt serventiaDt = null;
            		ComarcaDt comarcaDt = null;
            		ProcessoDt processoDt = new ProcessoNe().consultarId(idProcesso, obFabricaConexao);
            		if (processoDt != null && processoDt.getServentiaCodigo() != null) {
            			serventiaDt = new ServentiaNe().consultarServentiaCodigo(processoDt.getServentiaCodigo(), obFabricaConexao);
            		}
            		if (serventiaDt != null) {
            			comarcaDt = new ComarcaNe().consultarId(serventiaDt.getId_Comarca(), obFabricaConexao);
            		}
            		if (comarcaDt != null) {
            			comarcaCodigo = comarcaDt.getComarcaCodigo();
            		}
            		
            		if(Funcoes.StringToInt(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.PRIMEIRO_GRAU) {
            			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
            			locomocoesNaoUtilizadasProjudi = guiaSPGNe.consultarLocomocaoNaoUtilizada(locomocoesNaoUtilizadasProjudi, comarcaCodigo, validaOficialVinculadoLocomocao);
                	}
            		else {
            			if(Funcoes.StringToInt(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.SEGUNDO_GRAU) {
            				
            			}
            		}
            	}            	
            }
            
            //stTemp = obPersistencia.consultarLocomocaoNaoUtilizadaJSON(idProcesso, descricao, cidade, uf, posicao);
        }
        finally {
        	if( obFabricaConexao != null ) {
        		obFabricaConexao.fecharConexao();
        	}
        }
        
        return locomocoesNaoUtilizadasProjudi;
    }
    
    public List<LocomocaoDt> consultarTodasLocomocoes(String idGuia) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            return consultarTodasLocomocoes(idGuia, obFabricaConexao);
        }
        finally {
        	if( obFabricaConexao != null ) {
        		obFabricaConexao.fecharConexao();
        	}
        }
    }
    
    public List<LocomocaoDt> consultarTodasLocomocoes(String idGuia, FabricaConexao obFabricaConexao) throws Exception {
    	LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());            
        return obPersistencia.consultarTodasLocomocoes(idGuia);
    }
    
    public List<LocomocaoDt> consultarLocomocaoUtilizadaGuiaComplementar(String idProcesso, String idGuiaComplementar) throws Exception {
    	FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
            LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());            
            return obPersistencia.consultarLocomocaoUtilizadaGuiaComplementar(idProcesso, idGuiaComplementar);
        } finally {
            obFabricaConexao.fecharConexao();
        }
    }
    
    private String gerarJSON(List<LocomocaoDt> locomocoesNaoUtilizadasProjudi, String posicaoAtual) {
    	int qtdeColunas = 5;
    	long qtdPaginas = 1;
    	StringBuilder stTemp = new StringBuilder();
		stTemp.append("[");
		stTemp.append("{\"id\":\"-50000\",\"desc1\":\"").append(qtdPaginas);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		stTemp.append(",{\"id\":\"-60000\",\"desc1\":\"").append(posicaoAtual);
		for (int i = 2; i <= qtdeColunas; i++) {
			stTemp.append("\",\"desc" + i + "\":\"");
		}
		stTemp.append("\"}");
		for (LocomocaoDt locomocaoDt : locomocoesNaoUtilizadasProjudi) {
			stTemp.append(",{\"id\":\"").append(locomocaoDt.getId()).append("\",\"desc1\":\"").append(Funcoes.removerCaracteresEspeciaisEAspasDuplas(locomocaoDt.getBairroDt().getBairro()));
			stTemp.append("\",\"desc2\":\"").append(Funcoes.removerCaracteresEspeciaisEAspasDuplas(locomocaoDt.getBairroDt().getCidade()));
			stTemp.append("\",\"desc3\":\"").append(Funcoes.removerCaracteresEspeciaisEAspasDuplas(locomocaoDt.getBairroDt().getUf()));			
			stTemp.append("\",\"desc4\":\"").append(locomocaoDt.getZonaDt()!=null?Funcoes.removerCaracteresEspeciaisEAspasDuplas(locomocaoDt.getZonaDt().getZona()):"");
			stTemp.append("\",\"desc5\":\"").append(locomocaoDt.getRegiaoDt()!=null?Funcoes.removerCaracteresEspeciaisEAspasDuplas(locomocaoDt.getRegiaoDt().getRegiao()):"");						
			stTemp.append("\"}");
		}
		stTemp.append("]");
		return stTemp.toString();
    }
    
    public LocomocaoDt consultarIdCompleto(String id_locomocao ) throws Exception {

		LocomocaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarIdCompleto(id_locomocao); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public void cancelarGuiaEmitidaLocomocaoComplementar(GuiaEmissaoDt guiaEmissaoDt, FabricaConexao obFabricaConexao) throws Exception {
		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
				
		List<LocomocaoDt> locomocoesUtilizadasComplementarProjudi = obPersistencia.consultarLocomocoesUtilizadasGuiaComplementar(guiaEmissaoDt.getId_Processo(), guiaEmissaoDt.getId());
		
		if (locomocoesUtilizadasComplementarProjudi != null && locomocoesUtilizadasComplementarProjudi.size() > 0) {
			for (LocomocaoDt locomocaoDt : locomocoesUtilizadasComplementarProjudi) {
				locomocaoDt.setId_GuiaEmissaoComplementar(null);
				obPersistencia.alterar(locomocaoDt);
			}
			
			LocomocaoDt locomocaoDt = locomocoesUtilizadasComplementarProjudi.get(0);
        	if (locomocaoDt.getGuiaItemDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt() != null && locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getFlagGrau() != null) {
        		
        		String comarcaCodigo = null;
        		ServentiaDt serventiaDt = null;
        		ComarcaDt comarcaDt = null;
        		ProcessoDt processoDt = new ProcessoNe().consultarId(guiaEmissaoDt.getId_Processo(), obFabricaConexao);
        		if (processoDt != null && processoDt.getServentiaCodigo() != null) serventiaDt = new ServentiaNe().consultarServentiaCodigo(processoDt.getServentiaCodigo(), obFabricaConexao);
        		if (serventiaDt != null) comarcaDt = new ComarcaNe().consultarId(serventiaDt.getId_Comarca(), obFabricaConexao);
        		if (comarcaDt != null) comarcaCodigo = comarcaDt.getComarcaCodigo();
        		
        		if(Funcoes.StringToInt(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.PRIMEIRO_GRAU) {
        			GuiaSPGNe guiaSPGNe = new GuiaSPGNe();
        			locomocoesUtilizadasComplementarProjudi = guiaSPGNe.consultarLocomocaoNaoUtilizadaGuiaComplementar(locomocoesUtilizadasComplementarProjudi, comarcaCodigo, true);
        			guiaSPGNe.cancelarGuiaEmitidaLocomocaoComplementar(guiaEmissaoDt, locomocoesUtilizadasComplementarProjudi);
            	} else if(Funcoes.StringToInt(locomocaoDt.getGuiaItemDt().getGuiaEmissaoDt().getGuiaModeloDt().getFlagGrau()) == GuiaTipoDt.SEGUNDO_GRAU) {
            		
            	}
        	}
		}
	}
	/**
	 * Método para correção de mandados cadastrados com erro na central de mandados.
	 * 
	 * @param String idMandadoJudicial
     * @param int quantidade 
     * @param String id usuário sessão
     * @return boolean
     * @throws Exception
     * @author Fernando Meireles
     * 
     */ 
	public String correcaoMandadoCentral(String idMandado, int quantidade, String idUsuarioLog) throws Exception {
	   boolean retorno = false;
	   String mensagem = "";
	   FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);	
	   MandadoJudicialNe mandadoJudicialNe = new MandadoJudicialNe();
	   MandadoJudicialDt mandadoJudicialDt = null;
	   
	   try {
		   
	   	   obFabricaConexao.iniciarTransacao();
	   
		   mandadoJudicialDt = mandadoJudicialNe.consultarId(idMandado);
		   if (mandadoJudicialDt == null) 
			   throw new MensagemException("Número do Mandado Inexistente"); 
		   if (!mandadoJudicialDt.isAssistencia())
			   throw new MensagemException("Mandado não e de assistência");  
		   
		   UsuarioServentiaNe usuarioServentiaNe = new UsuarioServentiaNe();	
		   UsuarioServentiaDt usuarioServentiaDt = null;	   
		   usuarioServentiaDt = usuarioServentiaNe.buscaUsuarioServentiaIdUsuServ(mandadoJudicialDt.getId_UsuarioServentia_1());
    	   if (usuarioServentiaDt == null)
			   throw new MensagemException("Usuario serventia não encontrado");
		   
		   ServentiaDt serventiaDt = null;
		   ServentiaNe serventiaNe = new ServentiaNe();
		   serventiaDt = serventiaNe.consultarIdSimples(usuarioServentiaDt.getId_Serventia());	   
     	   if (!serventiaDt.getServentiaCodigoExterno().equalsIgnoreCase(ServentiaDt.CODIGO_INTERNO_SENADOR_CANEDO_CENTRAL_MANDADO))  
	           throw new MensagemException("Mandado nao pertence a Senador Canedo");   	   
	 	   
		   retorno = this.reservarLocomocao(mandadoJudicialDt.getId_Proc(), mandadoJudicialDt.getId(), mandadoJudicialDt.getId_Bairro(), quantidade, idUsuarioLog, null, obFabricaConexao);
		   if (retorno) {			       
			  mandadoJudicialDt.setAssistencia(Integer.toString(MandadoJudicialDt.NAO_ASSISTENCIA));
			  mandadoJudicialNe.alterarAssistencia(idMandado, MandadoJudicialDt.NAO_ASSISTENCIA, obFabricaConexao);
			  LogNe logNe = new LogNe();
			  LogDt logDt = new LogDt("MandadoJudicial", mandadoJudicialDt.getId(), idUsuarioLog, mandadoJudicialDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", mandadoJudicialDt.getPropriedades());
			  logNe.salvar(logDt, obFabricaConexao);
			  mensagem = "Operação realizada com sucesso";		   
		   } else {	    	   
		     throw new MensagemException("Erro ao reservar locomoção");
		   }			   
		   obFabricaConexao.finalizarTransacao();	   
       } catch (Exception e) {
		   obFabricaConexao.cancelarTransacao();
		   throw e;
	   }
	   finally {
		   obFabricaConexao.fecharConexao();
	   }
	   return mensagem;
	} 
	
	/**
	 * Método para reservar locomoção para mandado judicial.
	 * 
	 * @param String idProcesso
	 * @param String idMandadoJudicial
	 * @param String idBairro
	 * @param String quantidade
	 * @param String idUsuarioLog
	 * @param String idUsuarioOficial
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception
	 */
	public boolean reservarLocomocao(String idProcesso, String idMandadoJudicial, String idBairro, int quantidade, String idUsuarioLog, String idUsuarioOficial, FabricaConexao obFabricaConexao) throws Exception {
			
		///
		/// Procura Guia de Saldo no Spg.   
		///
		
		ImportaDadosSPGNe objNe = new ImportaDadosSPGNe();
		FabricaConexao objFcAdabas = new FabricaConexao(FabricaConexao.ADABAS);		
		int qtdeLocomocaoUsada = 0;
		if (quantidade > 0) {
			qtdeLocomocaoUsada = objNe.consultaGuiaSaldoLocomocao(Integer.parseInt(idProcesso), quantidade, Integer.parseInt(idBairro), Integer.parseInt(idMandadoJudicial), obFabricaConexao, objFcAdabas );
			quantidade = quantidade - qtdeLocomocaoUsada;			
		} 
		
		// Fim procura		
				
		boolean retorno = false;
		
		/**
		 * *****************************************************
		 * *****************************************************
		 * Verificar os ids dos usuários do log e do oficial. Registrar para quem foi reservado o mandado.
		 * *****************************************************
		 * *****************************************************
		 */
		if(quantidade > 0) {
			if( idProcesso != null && idMandadoJudicial != null && idBairro != null) {
			
				LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
				retorno = obPersistencia.reservarLocomocaoMandadoJudicial(idProcesso, idBairro, idMandadoJudicial, quantidade);
				
				//registra log
				if( retorno ) {
					
					//Atualiza as locomoções que foram complementadas (id_guia_emis_comp) para facilitar o pagamento
					this.reservarLocomocaoGuiasComplementadas(idMandadoJudicial, obFabricaConexao);
					
					//consulta locomoções
					List<LocomocaoDt> listaLocomocaoReservada = obPersistencia.consultarLocomocaoVinculadaMandado(idMandadoJudicial);
					
					if( listaLocomocaoReservada == null || listaLocomocaoReservada.isEmpty() ) {
						throw new MensagemException("Não foi encontrada locomoções marcadas para o bairro("+idBairro+") e processo("+idProcesso+").");
					}
					
					/////
					//// grava controle spg-projudi  das guias vinculadas as locomocoes usadas
					////
					objNe.gravaControleSpg(idMandadoJudicial, idProcesso, obFabricaConexao, objFcAdabas);
					///
					/// fim atualiza
					///
					LogNe logNe = new LogNe();
					for( LocomocaoDt locomocaoDt : listaLocomocaoReservada ) {
						LogDt logDt = new LogDt("Locomocao", locomocaoDt.getId(), idUsuarioLog, locomocaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), "", locomocaoDt.getPropriedades());
						logNe.salvar(logDt, obFabricaConexao);
					}
					
				} else {
					throw new MensagemException("Não foram encontradas locomoções suficientes para neste processo para o bairro em questão.");
				}
			}
		}
		else {
			if (qtdeLocomocaoUsada == 0)  
				throw new MensagemException("Não foi possível identificar a quantidade de locomoções a serem reservadas.");
			else retorno = true;
		}  
		//// spg
		 if (objFcAdabas != null) {
	      objFcAdabas.fecharConexao(); 
		 }
		///
		return retorno;
	}
	
	/**
	 * Método que vincula as locomoções complementadas com o idMandadoJudicial. 
	 * Este método corrige o valor total quando uma locomoção foi complementada.
	 * 
	 * @param String idMandadoJudicial
	 * @param FabricaConexao obFabricaConexao
	 * @throws Exception
	 * 
	 * @author fasoares
	 */
	private void reservarLocomocaoGuiasComplementadas(String idMandadoJudicial, FabricaConexao obFabricaConexao) throws Exception {
		if( idMandadoJudicial != null ) {
			
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			
			obPersistencia.reservarLocomocaoMandadoJudicialGuiaComplementada(idMandadoJudicial);
		}
    }
	
	/**
	 * Método para liberar locomocao que está reservada para mandado judicial.
	 * @param String idMandadoJudicial
	 * @param String idUsuarioLog
	 * @param FabricaConexao obFabricaConexao
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean liberarLocomocao(String idMandadoJudicial, int qtdLiberar, String idUsuarioLog, FabricaConexao obFabricaConexao) throws Exception {
		
		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
		
		boolean retorno = false;
		int qtdLiberada = 0; 
		int quantidade = 0;
		LocomocaoDt locomocaoDt = null;
				
		//consulta locomoções reservadas de guias que não são de saldo		
	 
		List<LocomocaoDt> listaLocomocaoReservada = obPersistencia.consultaLiberacaoLocomocao(idMandadoJudicial);
	
		if(!listaLocomocaoReservada.isEmpty() ) {				
			
		   for (int i = 0; i < qtdLiberar; i++) {			
			    
			   if (qtdLiberada < listaLocomocaoReservada.size()) {
			   
			       quantidade = obPersistencia.liberarLocomocao(listaLocomocaoReservada.get(i).getId());
			       
			       if (quantidade == 0) {
			    	   throw new MensagemException("Erro ao liberar locomoção do mandado");
			       }
			       
			       qtdLiberada ++;
				
		 	       obPersistencia.liberarLocomocaoComplementada(listaLocomocaoReservada.get(0).getId_Guia_Emissao());				
		           LogNe logNe = new LogNe();
		   	       locomocaoDt = listaLocomocaoReservada.get(i);
		           LogDt logDt = new LogDt("Locomocao", locomocaoDt.getId(), idUsuarioLog, locomocaoDt.getIpComputadorLog(), String.valueOf(LogTipoDt.LiberarLocomocao), "", locomocaoDt.getPropriedades());
		  		   logNe.salvar(logDt, obFabricaConexao);
			   }  
		   }
		   qtdLiberar = qtdLiberar - qtdLiberada;			 			   
		}
		
		if (qtdLiberar > 0)	{  //  verifica se tem locomocao de guia de saldo
			
			ImportaDadosSPGNe objNe = new ImportaDadosSPGNe();
			
			FabricaConexao objFcAdabas = new FabricaConexao(FabricaConexao.ADABAS);		
			
			qtdLiberada = objNe.LiberarLocomocaoGuiaSaldo(idMandadoJudicial, qtdLiberar, obFabricaConexao, objFcAdabas);
			
			qtdLiberar = qtdLiberar - qtdLiberada;	
			
			objFcAdabas.fecharConexao();			
		} 
		if (qtdLiberar == 0) {
			retorno = true;
		} else {
			throw new MensagemException("Não foram encontradas locomocoes para liberar.");
		}	
		return retorno;			
	}
	
	/**
     * Método que consulta a LocomocaoDt pelo id do GuiaItem de itens de penhora e/ou avaliacao. 
     * @param String idGuiaItem
     * @return LocomocaoDt
     * @throws Exception
     */
    public LocomocaoDt consultarLocomocaoDeItemPenhora_Avaliacao(String idGuiaItem) throws Exception {
    	LocomocaoDt locomocaoDt = null;
    	FabricaConexao obFabricaConexao = null;
    	
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
    		locomocaoDt = obPersistencia.consultarLocomocaoDeItemPenhora_Avaliacao(idGuiaItem);
    	}
    	finally{
    		obFabricaConexao.fecharConexao();
    	}
    	
    	return locomocaoDt;
    }
    
    /**
     * Método para consultar as locomoções-mandados da view no SPG V_SPGAGUIAS_LOCOMOCOES_MANDADOS(_REM)
     * 
     * @param String numeroGuiaCompleto
     * @return List<LocomocaoSPGDt>
     * @throws Exception
     */
    public List<LocomocaoSPGDt> consultarLocomocaoMandadoSPG(String numeroGuiaCompleto) throws Exception {
    	List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
    	
    	if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
    		
    		numeroGuiaCompleto = numeroGuiaCompleto.trim().replace(" ", "").replace("/", "").replace("-", "");
    		
    		GuiaEmissaoDt guiaSPG = new GuiaSPGNe().consultarGuiaEmissaoSPG(numeroGuiaCompleto);
    		
    		if( guiaSPG != null ) {
    			
    			if( guiaSPG.isGuiaEmitidaSPGCapital() ) {
    				listaLocomocaoSPGDt = this.consultarLocomocaoMandadoSPG( guiaSPG.getId(), "" );
    			}
    			else {
    				listaLocomocaoSPGDt = this.consultarLocomocaoMandadoSPG( guiaSPG.getId(), "_REM" );
    			}
    		}
    		
    	}
    	
    	return listaLocomocaoSPGDt;
    }
    
    /**
     * Método para consultar as locomoções-mandados da view no SPG V_SPGAGUIAS_LOCOMOCOES_MANDADOS(_REM)
     * 
     * @param String numeroGuiaCompleto
     * @return List<LocomocaoSPGDt>
     * @throws Exception
     */
    public List<LocomocaoSPGDt> consultarListaLocomocaoSPG(String numeroGuiaCompleto) throws Exception {
    	List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
    	
    	if( numeroGuiaCompleto != null && !numeroGuiaCompleto.isEmpty() ) {
    		
    		numeroGuiaCompleto = numeroGuiaCompleto.trim().replace(" ", "").replace("/", "").replace("-", "");
    		
    		GuiaEmissaoDt guiaSPG = new GuiaSPGNe().consultarGuiaEmissaoSPG(numeroGuiaCompleto);
    		
    		if( guiaSPG != null ) {
    			
    			if( guiaSPG.isGuiaEmitidaSPGCapital() ) {
    				listaLocomocaoSPGDt = this.consultarLocomocaoSPG( guiaSPG.getId(), "" );
    			}
    			else {
    				listaLocomocaoSPGDt = this.consultarLocomocaoSPG( guiaSPG.getId(), "_REM" );
    			}
    		}
    		
    	}
    	
    	return listaLocomocaoSPGDt;
    }
    
    /**
     * Método para consultar as locomoções no SPG
     * 
     * @param String isnGuia
     * @param String remoto
     * @return List<LocomocaoSPGDt>
     * @throws Exception
     */
    public List<LocomocaoSPGDt> consultarLocomocaoSPG(String isnGuia, String remoto) throws Exception {
    	List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
    	FabricaConexao obFabricaConexao = null;
    	
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
    		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
    		if( isnGuia != null && remoto != null ) {
    			listaLocomocaoSPGDt = obPersistencia.consultarLocomocaoSPG(isnGuia, remoto);
    		}
    	}
    	finally {
    		obFabricaConexao.fecharConexao();
    	}
    	
    	
    	return listaLocomocaoSPGDt;
    }
    
    /**
     * Método para consultar as locomoções-mandados da view no SPG V_SPGAGUIAS_LOCOMOCOES_MANDADOS(_REM)
     * 
     * @param String isnGuia
     * @param String remoto
     * @return List<LocomocaoSPGDt>
     * @throws Exception
     */
    public List<LocomocaoSPGDt> consultarLocomocaoMandadoSPG(String isnGuia, String remoto) throws Exception {
    	List<LocomocaoSPGDt> listaLocomocaoSPGDt = null;
    	FabricaConexao obFabricaConexao = null;
    	
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.ADABAS);
    		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
    		if( isnGuia != null && remoto != null ) {
    			listaLocomocaoSPGDt = obPersistencia.consultarLocomocaoMandadoSPG(isnGuia, remoto);
    		}
    	}
    	finally {
    		obFabricaConexao.fecharConexao();
    	}
    	
    	
    	return listaLocomocaoSPGDt;
    }
    
//    /**
//     * Método para consultar locomocao vinculado a um mandado, bairro e processo.
//     * 
//     * @param String idProcesso
//     * @param String idMandadoJudicial
//     * @param String idBairro
//     * @param FabricaConexao obFabricaConexao
//     * @return List<LocomocaoDt>
//     * @throws Exception
//     */
//    private List<LocomocaoDt> consultarLocomocaoVinculadaMandado(String idMandadoJudicial, FabricaConexao obFabricaConexao) throws Exception {
//    	List<LocomocaoDt> listaLocomocaoDt = null;
//    	
//   		if( idMandadoJudicial != null && !idMandadoJudicial.isEmpty()) {
//   			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
//   			
//   			listaLocomocaoDt = obPersistencia.consultarLocomocaoVinculadaMandado(idMandadoJudicial);
//   		}
//    	
//    	return listaLocomocaoDt;
//    }
    
    /**
     * Método para consultar a quantidade de locomoções reservadas e vinculadas a um mandado.
     * 
     * @param String idMandadoJudicial
     * @param String idBairro
     * @return int
     * @throws Exception
     * @author hrrosa
     */    
     public int consultarQtdLocomocaoVinculadaMandado(String idMandadoJudicial, FabricaConexao obFabricaConexao) throws Exception {
    	return new LocomocaoPs(obFabricaConexao.getConexao()).consultarQtdLocomocaoVinculadaMandado(idMandadoJudicial);
    }
    
    /**
     * Método para consultar saldo de locomocoes no processo disponiveis para serem utilizadas.
     * 
     * @param String idProcesso
     * @return List<LocomocaoDt>
     * @throws Exception
     * 
     * @author fasoares
     */
    public List<LocomocaoDt> consultaLocomocoesProcessoDisponiveis(String idProcesso) throws Exception {
    	List<LocomocaoDt> listaLocomocaoDt = null;
    	FabricaConexao obFabricaConexao = null;
    	
    	try {
    		obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
    		LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
    		if( idProcesso != null && idProcesso != null ) {
    			listaLocomocaoDt = obPersistencia.consultaLocomocoesProcessoDisponiveis(idProcesso);
    		}
    	}
    	finally {
    		obFabricaConexao.fecharConexao();
    	}
    	
    	return listaLocomocaoDt;
    }
    
    /**
     * Método para consultar se guia tem algum item de locomocao vinculada a mandado.
     * 
     * @param String idGuiaEmissao
     * @param FabricaConexao obFabricaConexao
     * @param obFabricaConexao
     * @return boolean
     * @throws Exception
     * @author fasoares
     */
    public boolean isGuiaVinculadaLocomocaoMandado(String idGuiaEmissao, FabricaConexao obFabricaConexao) throws Exception {

		boolean retorno = false;
		
		if( idGuiaEmissao != null && !idGuiaEmissao.isEmpty() && obFabricaConexao != null ) {
			LocomocaoPs obPersistencia = new LocomocaoPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.isGuiaVinculadaLocomocaoMandado(idGuiaEmissao);
		}
		
		return retorno;
	
    }
}