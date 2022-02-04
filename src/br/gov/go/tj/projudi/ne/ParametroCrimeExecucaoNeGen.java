package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ParametroCrimeExecucaoDt;
import br.gov.go.tj.projudi.ps.ParametroCrimeExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ParametroCrimeExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5402337804012204557L;
	protected  ParametroCrimeExecucaoDt obDados;


	public ParametroCrimeExecucaoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new ParametroCrimeExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(ParametroCrimeExecucaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ParametroCrimeExecucaoPs obPersistencia = new  ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ParametroCrimeExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ParametroCrimeExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ParametroCrimeExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(ParametroCrimeExecucaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ParametroCrimeExecucaoPs obPersistencia = new  ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ParametroCrimeExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

			ParametroCrimeExecucaoPs obPersistencia = new  ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public ParametroCrimeExecucaoDt consultarId(String id_parametrocrimeexecucao ) throws Exception {

		ParametroCrimeExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ParametroCrimeExecucaoPs obPersistencia = new  ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_parametrocrimeexecucao ); 
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

			ParametroCrimeExecucaoPs obPersistencia = new  ParametroCrimeExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoCrimeExecucao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		CrimeExecucaoNe CrimeExecucaone = new CrimeExecucaoNe(); 
		tempList = CrimeExecucaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = CrimeExecucaone.getQuantidadePaginas();
		CrimeExecucaone = null;
		return tempList;
	}
}
