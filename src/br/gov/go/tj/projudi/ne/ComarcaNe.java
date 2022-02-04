package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.DescritorComboDt;
import br.gov.go.tj.projudi.ps.ComarcaPs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

public class ComarcaNe extends ComarcaNeGen{

    private static final long serialVersionUID = -5031433082359333807L;

	public  String Verificar(ComarcaDt dados ) {

		String stRetorno="";

		return stRetorno;

	}
	
	/**
	 * Consulta a comarca à partir do Id
	 * @throws Exception
	 */
	public ComarcaDt consultarId(String idComarca, FabricaConexao obFabricaConexao) throws Exception {
		ComarcaDt retorno=null;
		 
		ComarcaPs obPersistencia = new ComarcaPs(obFabricaConexao.getConexao());
		retorno = obPersistencia.consultarId(idComarca); 
		
		return retorno;
	}
	
	public String consultarCodigo(String comarcaCodigo) throws Exception {
		String retorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaPs obPersistencia = new ComarcaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarNomeCodigo(comarcaCodigo); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public ComarcaDt consultarComarcaCodigo(String comarcaCodigo) throws Exception {
		ComarcaDt retorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaPs obPersistencia = new ComarcaPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarComarcaCodigo(comarcaCodigo); 
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao) throws Exception {
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaPs obPersistencia = new ComarcaPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON( descricao, posicao, ordenacao, quantidadeRegistros);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	/**
	 * @author jrcorrea
	 * @return List com as comarcas
	 * @throws Exception
	 * @data 10/09/2015 as 09:55
	 */
	
	public List listar() throws Exception {
		List lisTemp = null;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaPs obPersistencia = new ComarcaPs(obFabricaConexao.getConexao());
			lisTemp = obPersistencia.listar();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return lisTemp;  
	}

	public List<DescritorComboDt> consultarDescricaoCombo(String descricao) throws Exception {
		FabricaConexao obFabricaConexao = null; 
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ComarcaPs obPersistencia = new ComarcaPs(obFabricaConexao.getConexao());
			return obPersistencia.consultarDescricaoCombo(descricao);
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para extrair o código da comarca do info_local_certidao da guia do SPG.
	 * 
	 * @param String infoLocalCertidao
	 * @return String codigoComarca
	 * @throws Exception
	 */
	public String extrairComarcaCodigoInfoLocalCertidaoSPG(String infoLocalCertidao) throws Exception {
		String codigoComarca = null;
		
		if( infoLocalCertidao != null && !infoLocalCertidao.isEmpty() && !infoLocalCertidao.equals("null") ) {
			
			if( infoLocalCertidao.length() > 3 ) {
				codigoComarca = infoLocalCertidao.substring(0, infoLocalCertidao.length() - 3);
			}
			
		}
		
		return codigoComarca;
	}
}
