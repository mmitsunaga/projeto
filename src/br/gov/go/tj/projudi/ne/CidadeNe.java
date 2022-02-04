package br.gov.go.tj.projudi.ne;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.EstadoDt;
import br.gov.go.tj.projudi.ps.CidadePs;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.pdf.InterfaceJasper;

public class CidadeNe extends CidadeNeGen {
	
/**
 * Método para criar um lista com id e descrição da cidade no formato jSon
 * @author jrcorrea	
 */
//	public String getCidades(String nome, String posicao) throws Exception {
//		StringBuilder stTemp = new StringBuilder();
//		FabricaConexao obFabricaConexao = null;
//	
//		List Lista = null;
//
//		if (obNegocio == null) obNegocio = new CidadeNe();
//		Lista = obNegocio.consultarDescricao(nome, posicao);
//		////System.out.println(Lista);
//		tempList.add(Lista);
//		tempList.add(Funcoes.StringToLong(posicao));
//		tempList.add(obNegocio.getQuantidadePaginas());
//		
//    	try{
//    	    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
//    	    CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
//    
//    	    Lista = obPersistencia.consultarDescricao(nome, posicao);
//    	    
//    	} finally{
//    	    obFabricaConexao.fecharConexao();
//    	}
//    	
//    	
//		
//		return stTemp.toString();
//	}

    /**
     * 
     */
    private static final long serialVersionUID = -6189371822317550849L;

    public String Verificar(CidadeDt dados) {
	String retornoVerificacao = "";
	if (dados.getCidade().equalsIgnoreCase("")) {
	    retornoVerificacao += "Descrição é é obrigatório.";
	}
	if (dados.getId_Estado().equalsIgnoreCase("")) {
	    retornoVerificacao += "Estado é é obrigatório.";
	}
	if (dados.getId_Estado().equals(EstadoDt.ESTADOCODIGOGOIAS) && ( dados.getCodigoSPG() == null || dados.getCodigoSPG().equalsIgnoreCase("") ) ) {
	    retornoVerificacao += "Código SPG é é obrigatório.";
	}
	return retornoVerificacao;
    }

    /**
     * Método responsável em consultar uma cidade, baseado na descrição e Uf passados
     */
    public CidadeDt buscaCidade(String descricaoCidade, String estadoCidade) throws Exception {
    	CidadeDt cidadeDt = null;
    	FabricaConexao obFabricaConexao = null;
    
    	try{
    	    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	    CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
    
    	    cidadeDt = obPersistencia.buscaCidade(descricaoCidade, estadoCidade);
    	
    	} finally{
    	    obFabricaConexao.fecharConexao();
    	}
    	return cidadeDt;
    }

    /**
     * Consulta cidades (utilizando filtro por cidade e uf)
     */
    public List consultarDescricao(String descricao, String uf, String posicao) throws Exception {
        List tempList = null;
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
            tempList = obPersistencia.consultarDescricao(descricao, uf, posicao);
            setQuantidadePaginas(tempList);
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return tempList;
    }
    
    /**
     * Consulta cidades (utilizando filtro por cidade e uf)
     */
    public String consultarDescricaoJSON(String descricao, String uf, String posicao) throws Exception {
        String stTemp ="";
        FabricaConexao obFabricaConexao = null; 
        
        try{
            obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
            CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
            stTemp = obPersistencia.consultarDescricaoJSON(descricao, uf, posicao);
                        
        } finally{
            obFabricaConexao.fecharConexao();
        }
        return stTemp;
    }
    

    /**
	 * Consulta o Id da cidade
	 * @param descricao: descrição da cidade
	 * @param uf: sigla do estado
	 */
	public String consultarIdCidade(String descricaoCidade, String uf, FabricaConexao obFabricaConexao) throws Exception {
        String id = "";
        
        CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
        id = obPersistencia.consultarIdCidade(descricaoCidade, uf);
        
        return id;
    }
    
    /**
     * Método responsável por criar o relatório de listagem das Cidades no formato PDF.
     * 
     * @param stCaminho
     * @return
     * @throws Exception
     */
    public byte[] relListagemCidades(String stCaminho) throws Exception {
    	byte[] temp = null;
    	ByteArrayOutputStream baos = null;
    	FabricaConexao obFabricaConexao = null; 
    	try{
    	    obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
    	    CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
    
    	    List liCidades = obPersistencia.relListagemCidades();
    
    	    InterfaceJasper ei = new InterfaceJasper(liCidades);
    
    	    // PATH PARA OS ARQUIVOS JASPER DO RELATORIO
    	    // MOVIMENTACAO*****************************************************
    	    String pathJasper = stCaminho + "WEB-INF" + File.separator + "relatorios" + File.separator + "ListagemCidades.jasper";
    
    	    // parâmetros do relatório
    	    Map parametros = new HashMap();
    	    parametros.put("caminhoLogo", stCaminho + "imagens" + File.separator + "logoEstadoGoias02.jpg");
    
    	    JasperPrint jp = JasperFillManager.fillReport(pathJasper, parametros, ei);
    
    	    JRPdfExporter jr = new JRPdfExporter();
    	    jr.setParameter(JRExporterParameter.JASPER_PRINT, jp);
    	    baos = new ByteArrayOutputStream();
    	    jr.setParameter(JRPdfExporterParameter.OUTPUT_STREAM, baos);
    	    jr.exportReport();
    
    	    temp = baos.toByteArray();
    
    	} finally{
    	    obFabricaConexao.fecharConexao();
    	    baos = null;
    	}
    	return temp;
      }

	/**
	 * ATENÇÃO: NUNCA UTILIZAR!
	 * Fred!
	 */
	public List consultarCidadesGoias() throws Exception {
		List listaCidadeDt = null;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
			listaCidadeDt = obPersistencia.consultarCidadesGoias();
		
		}
		finally{
			obFabricaConexao.fecharConexao();
		}
		
		return listaCidadeDt;
	}
    /**
     * Consulta cidades (utilizando filtro por descricao do estado)
     */
    public String consultarDescricaoEstadoJSON(String descricao, String posicao) throws Exception {
        String stTemp ="";               
                                
        stTemp = (new EstadoNe()).consultarDescricaoJSON(descricao,  posicao);                       
        
        return stTemp;
    }
    
	public List consultarIdEstado(String idEstado) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CidadePs obPersistencia = new  CidadePs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarIdEstado(idEstado);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public CidadeDt consultarCidadeCodigo(String codigoCidade ) throws Exception {

		CidadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_Cidade" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarCidadeCodigo(codigoCidade); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	/**
	 * Consulta uma cidade através do código SPG.
	 * @param codigoSPG
	 * @param obFabricaConexao
	 * @return
	 * @throws Exception
	 * @author mmgomes
	 */
	public CidadeDt consultarCodigoSPG(String codigoSPG, FabricaConexao obFabricaConexao) throws Exception {
		CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
		return obPersistencia.consultarCodigoSPG(codigoSPG ); 
	}
   
    /**
     * Consulta Cidades (utilizando filtro por cidade e uf) e considerando um campo de ordenação
     * @param descricao
     * @param ordenacao
     * @param posicao
     * @param quantidadeRegistros
     * @return
     * @throws Exception
     */
    public String consultarDescricaoJSON (String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
    	 String stTemp ="";
         FabricaConexao obFabricaConexao = null;         
         try {
             obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
             CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
             stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
             
         } finally {
             obFabricaConexao.fecharConexao();
         }
         return stTemp;
	}
    
    public CidadeDt consultarId(String id_cidade, FabricaConexao obFabricaConexao) throws Exception {
		CidadeDt dtRetorno=null;
		CidadePs obPersistencia = new CidadePs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_cidade );
		if (dtRetorno != null) {
			obDados.copiar(dtRetorno);	
		} else {
			obDados = new CidadeDt();
		}
		return dtRetorno;
	}
}