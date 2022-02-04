package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CrimeExecucaoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CrimeExecucaoPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class CrimeExecucaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 3525237308685504954L;
	
	protected  CrimeExecucaoDt obDados;


	public CrimeExecucaoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new CrimeExecucaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(CrimeExecucaoDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CrimeExecucaoPs obPersistencia = new  CrimeExecucaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("CrimeExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("CrimeExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CrimeExecucaoDt dados ); 


//---------------------------------------------------------

	public void excluir(CrimeExecucaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CrimeExecucaoPs obPersistencia = new  CrimeExecucaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("CrimeExecucao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

			CrimeExecucaoPs obPersistencia = new  CrimeExecucaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public CrimeExecucaoDt consultarId(String id_crimeexecucao ) throws Exception {

		CrimeExecucaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CrimeExecucaoPs obPersistencia = new  CrimeExecucaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_crimeexecucao ); 
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

			CrimeExecucaoPs obPersistencia = new  CrimeExecucaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	}
