package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.BairroPs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class BairroNe extends BairroNeGen {
    private static final long serialVersionUID = -9047614239142734554L;
    
    public void salvar(BairroDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
			
			if (dados.getId().equalsIgnoreCase("") ) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Bairro", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			} 
			else {
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Bairro", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			
			//Trecho de código adicionado depois de reunião com Fred, Mirian, Junior, Marques e Ana Cláudia.
			//Base documentação deste trabalho é o email enviado no dia 16/03/2017 às 19:18hs para Mirian, Ana, Junior, Marques, Marcio, Jesus e Alex
			//Este código está dentro de try e caso dê erro não irá barrar de salvar o bairro.
			//Este trecho irá atualizar no SPG adicionando o ID_PROJUDI no SPG com o id do bairro do PJD que tem o código SPG preenchido.
			try {
				if( dados != null && 
					dados.getId() != null && !dados.getId().isEmpty() && 
					dados.getCodigoSPG() != null && !dados.getCodigoSPG().isEmpty() ) {
					
					SpgRegiaoZonaNe spgRegiaoZonaNe = new SpgRegiaoZonaNe();
					
					boolean atualizado = spgRegiaoZonaNe.atualizarIdProjudi(dados.getCodigoSPG(), dados.getId());
					
					if( atualizado ) {
						LogDt obLogBairroSPGDt = new LogDt("BairroSPG", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),"",dados.getPropriedades());
						obLog.salvar(obLogBairroSPGDt, obFabricaConexao);
					}
				}
			}
			catch(Exception e) {
				//Não faz nada para deixar o fluxo do cadastro/update do bairro terminar
			}
			
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}
		catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}

    public String Verificar(BairroDt dados) {
		String retornoVerificacao = "";
		
		if (dados.getBairro().equalsIgnoreCase("")) {
		    retornoVerificacao += "Descrição é é obrigatório.";
		}
		if (dados.getId_Cidade().equalsIgnoreCase("")) {
		    retornoVerificacao += "Cidade é é obrigatório.";
		}
		else {
			if (dados.getCodigoSPG() == null || dados.getCodigoSPG().equalsIgnoreCase("") || Funcoes.StringToLong(dados.getCodigoSPG().trim()) <= 100000) {
				try {
					CidadeDt cidadeDt = new CidadeNe().consultarId(dados.getId_Cidade());
					if (cidadeDt != null && cidadeDt.getId_Estado().trim().equalsIgnoreCase(EstadoDt.ESTADOCODIGOGOIAS)) {
						retornoVerificacao += "Código SPG é um campo obrigatório para cidades do estado de Goiás. Formato cidade+bairro SPG, sendo que o bairro deve possuir cinco posições. Ex: 796300005 [Goiânia: 7963 - Setor Oeste: 00005])";
					}
				}
				catch (Exception e) {}
			}
		}
		
		if (!dados.getBairro().equalsIgnoreCase("") && !dados.getId_Cidade().equalsIgnoreCase("") && dados.getId().equalsIgnoreCase("")) {
			try {
				BairroDt bairroDt = this.consultarBairro(dados.getBairro(), dados.getId_Cidade());
				if( bairroDt != null) {
					retornoVerificacao += "Este Bairro já está cadastrado para esta cidade.";
				}
			}
			catch (Exception e) {}
		}
		
		return retornoVerificacao;
    }

    /**
     * Consulta geral de bairros (utilizando filtro por bairro, cidade e uf)
     */
    public List consultarDescricao(String descricao, String cidade, String uf, String posicao) throws Exception {
    	List tempList = null;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarDescricao(descricao, cidade, uf, posicao);
            setQuantidadePaginas(tempList);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }

    /**
     * Consulta um bairro de acordo com descrição, cidade e estado informados
     * 
     * @param descricao
     *            , filtro para descrição do bairro
     * @param cidade
     *            , filtro para descrição da cidade
     * @param uf
     *            , filtro para estado
     */
    public BairroDt consultarBairro(String descricao, String cidade, String uf) throws Exception {
    	BairroDt bairroDt = null;
    	FabricaConexao obFabricaConexao = null;
    	try{
    	    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	    BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
    	    bairroDt = obPersistencia.consultarBairro(descricao, cidade, uf);
    	} finally {
    	    obFabricaConexao.fecharConexao();
    	}
    	return bairroDt;
    }
    
    public String consultarId(String descricao, String cidade, String uf, FabricaConexao obFabricaConexao) throws Exception {
    	String id = "";
    	BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
	    id = obPersistencia.consultarId(descricao, cidade, uf);
    	return id;
    }

    public BairroDt consultarId(String id_bairro, FabricaConexao obFabricaConexao) throws Exception {
    	BairroDt dtRetorno=null;
    	BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_bairro ); 
		obDados.copiar(dtRetorno);		
		return dtRetorno;
    }
    
    public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			
			BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
    
    public String consultarDescricaoJSON(String descricao, String cidade, String uf, String zona, String posicao) throws Exception {
    	String stTemp = null;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, cidade, uf, zona, posicao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
    
    public String consultarDescricaoBairrosGeralJSON(String descricao, String cidade, String uf, String zona, String posicao) throws Exception {
    	String stTemp = null;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoBairrosGeralJSON(descricao, cidade, uf, zona, posicao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
    
    public String consultarDescricaoJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
    	String stTemp = null;
        FabricaConexao obFabricaConexao = null;
        try {
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, cidade, uf, posicao);
        } catch (Exception e) {
            throw new Exception("<{ Erro ao consultar bairros. }> Local Exception: " + this.getClass().getName() + ".consultarDescricaoJSON(): " + e.getMessage(), e);
        } finally {
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
    
    public String consultarDescricaoLocomocaoJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
    	String stTemp = null;
        FabricaConexao obFabricaConexao = null;
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoLocomocaoJSON(descricao, cidade, uf, posicao);
        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
    
    public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		CidadeNe Cidadene = new CidadeNe(); 
		tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Cidadene.getQuantidadePaginas();
		Cidadene = null;

		return tempList;
	}
    
    public String consultarDescricaoCidadeJSON(String tempNomeBusca, String uf, String PosicaoPaginaAtual) throws Exception {
		String stTemp = "";
		
		CidadeNe Naturalidadene = new CidadeNe();
		stTemp = Naturalidadene.consultarDescricaoJSON(tempNomeBusca, uf, PosicaoPaginaAtual);
		
		return stTemp;
	}
    
    public List consultarIdCidade(String idCidade) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
			tempList = obPersistencia.consultarIdCidade(idCidade);
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
    
    public String consultarDescricaoZonaJSON(String tempNomeBusca1, String tempNomeBusca2, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp=null;
		ZonaNe zonaNe = new ZonaNe(); 
		stTemp = zonaNe.consultarDescricaoJSON(tempNomeBusca1, tempNomeBusca2, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoRegiaoJSON(String tempNomeBusca, String tempNomeBusca2, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp=null;
		RegiaoNe regiaoNe = new RegiaoNe(); 
		stTemp = regiaoNe.consultarDescricaoJSON(tempNomeBusca, tempNomeBusca2, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public BairroDt consultarBairroCodigoSPG(String codigoMunicipioSPG, String codigoBairroSPG) throws Exception {
    	BairroDt bairroDt = null;
    	FabricaConexao obFabricaConexao = null;
    	try{
    	    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	    BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
    	    bairroDt = obPersistencia.consultarBairroCodigoSPG(codigoMunicipioSPG, codigoBairroSPG);
    	} finally {
    	    obFabricaConexao.fecharConexao();
    	}
    	return bairroDt;
    }
	
	public BairroDt consultarBairroCodigoSPG(String codigoMunicipioSPG, String codigoBairroSPG, FabricaConexao obFabricaConexao ) throws Exception {
		BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
	    return obPersistencia.consultarBairroCodigoSPG(codigoMunicipioSPG, codigoBairroSPG);
    }
	
	/**
	 * Método para consultar bairros que tem as seguintes condições:
	 * CODIGO_SPG preenchido
	 * e 
	 * BAIRRO que esteja zoneado
	 * 
	 * Consulta feita para sincronizar dados dos bairros com os bairros do SPG.
	 * 
	 * @param String idCidade
	 * @return List<BairroDt>
	 * @throws Exception
	 */
	public List<BairroDt> consultarBairrosZoneados(String idCidade) throws Exception {
		List<BairroDt> listaBairroDt = null;
    	FabricaConexao obFabricaConexao = null;
    	
    	try {
    	    obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
    	    BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
    	    listaBairroDt = obPersistencia.consultarBairrosZoneados(idCidade);
    	}
    	finally {
    	    obFabricaConexao.fecharConexao();
    	}
    	
    	return listaBairroDt;
	}
	
	public BairroDt consultarBairroCodigo(String bairroCodigo) throws Exception {

		BairroDt retorno = null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarId(bairroCodigo); 
			obDados.copiar(retorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public BairroDt consultarBairro(String descricao, String idCidade) throws Exception {
    	BairroDt bairroDt = null;
    	FabricaConexao obFabricaConexao = null;
    	try{
    	    obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
    	    BairroPs obPersistencia = new BairroPs(obFabricaConexao.getConexao());
    	    bairroDt = obPersistencia.consultarBairro(descricao, idCidade);
    	}
    	finally {
    	    obFabricaConexao.fecharConexao();
    	}
    	return bairroDt;
    }
}