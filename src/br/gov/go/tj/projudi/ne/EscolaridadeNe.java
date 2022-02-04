package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.EscolaridadeDt;
import br.gov.go.tj.projudi.ps.EscolaridadePs;
import br.gov.go.tj.projudi.ps.Persistencia;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public class EscolaridadeNe extends EscolaridadeNeGen{

//

/**
     * 
     */
    private static final long serialVersionUID = -6283355694371986436L;

    //---------------------------------------------------------
	public  String Verificar(EscolaridadeDt dados ) {

		String stRetorno="";

		if (dados.getEscolaridade().length()==0)
			stRetorno += "O Campo Escolaridade é obrigatório.";
		////System.out.println("..neEscolaridadeVerificar()");
		return stRetorno;

	}
	
    public EscolaridadeDt consultarCodigo(String codigo_escolaridade ) throws Exception {
    	EscolaridadeDt dtRetorno=null;
    	FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscolaridadePs obPersistencia = new EscolaridadePs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarCodigo(codigo_escolaridade ); 
			if (dtRetorno!=null) obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

	public String consultarDescricaoEscolaridadeJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
    	String stRetorno="";
    	FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			EscolaridadePs obPersistencia = new EscolaridadePs(obFabricaConexao.getConexao());
			stRetorno= obPersistencia.consultarDescricaoEscolaridadeJSON(tempNomeBusca,posicaoPaginaAtual ); 
			
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stRetorno;
	}
	
	/**

	  * Método para lista as area processuais

	 * @author jrcorrea

	 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		return consultarDescricaoJSON(descricao, posicao, Persistencia.ORDENACAO_PADRAO, null);
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao, String ordenacao, String quantidadeRegistros) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			EscolaridadePs obPersistencia = new EscolaridadePs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao, ordenacao, quantidadeRegistros);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}

}
