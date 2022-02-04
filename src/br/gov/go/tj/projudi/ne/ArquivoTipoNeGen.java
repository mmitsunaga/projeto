package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ArquivoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.ArquivoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class ArquivoTipoNeGen extends Negocio{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3553824859202195661L;
	protected  ArquivoTipoDt obDados;
	public ArquivoTipoNeGen() {

		

		obLog = new LogNe(); 

		obDados = new ArquivoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ArquivoTipoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ArquivoTipoPs obPersistencia = new ArquivoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ArquivoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ArquivoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ArquivoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(ArquivoTipoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ArquivoTipoPs obPersistencia = new ArquivoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ArquivoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ArquivoTipoDt consultarId(String id_arquivotipo ) throws Exception {

		ArquivoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs obPersistencia = new ArquivoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_arquivotipo ); 
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
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ArquivoTipoPs obPersistencia = new ArquivoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally { 
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

}
