package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.TemaSituacaoDt;
import br.gov.go.tj.projudi.ps.TemaSituacaoPs;

//---------------------------------------------------------
public abstract class TemaSituacaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1166955206778286756L;
	protected  TemaSituacaoDt obDados;


	public TemaSituacaoNeGen() {

		obLog = new LogNe(); 

		obDados = new TemaSituacaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(TemaSituacaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaSituacaoPs obPersistencia = new  TemaSituacaoPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Temasituacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("Temasituacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(TemaSituacaoDt dados ); 


//---------------------------------------------------------

	public void excluir(TemaSituacaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			TemaSituacaoPs obPersistencia = new  TemaSituacaoPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("Temasituacao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

 /**

  * Método para lista as area processuais

 * @author jrcorrea

 */

	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		FabricaConexao obFabricaConexao = null;
		String stTemp = "";
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

		TemaSituacaoPs obPersistencia = new  TemaSituacaoPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public TemaSituacaoDt consultarId(String id_temasituacao ) throws Exception {

		TemaSituacaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaSituacaoPs obPersistencia = new  TemaSituacaoPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_temasituacao ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas(){
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			TemaSituacaoPs obPersistencia = new  TemaSituacaoPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
}
