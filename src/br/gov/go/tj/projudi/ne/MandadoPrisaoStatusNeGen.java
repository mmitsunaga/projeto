package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.MandadoPrisaoStatusDt;
import br.gov.go.tj.projudi.ps.MandadoPrisaoStatusPs;
import br.gov.go.tj.utils.FabricaConexao;

//---------------------------------------------------------
public abstract class MandadoPrisaoStatusNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1847022444225243552L;
	protected  MandadoPrisaoStatusDt obDados;


	public MandadoPrisaoStatusNeGen() {

		obLog = new LogNe(); 

		obDados = new MandadoPrisaoStatusDt(); 

	}


//---------------------------------------------------------
	public void salvar(MandadoPrisaoStatusDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoPrisaoStatusPs obPersistencia = new  MandadoPrisaoStatusPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("MandadoPrisaoStatus",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("MandadoPrisaoStatus",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(MandadoPrisaoStatusDt dados ); 


//---------------------------------------------------------

	public void excluir(MandadoPrisaoStatusDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			MandadoPrisaoStatusPs obPersistencia = new  MandadoPrisaoStatusPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("MandadoPrisaoStatus",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
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

		MandadoPrisaoStatusPs obPersistencia = new  MandadoPrisaoStatusPs(obFabricaConexao.getConexao()); 

			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;
	}
 //---------------------------------------------------------

	public MandadoPrisaoStatusDt consultarId(String id_mandadoprisaostatus ) throws Exception {

		MandadoPrisaoStatusDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			MandadoPrisaoStatusPs obPersistencia = new  MandadoPrisaoStatusPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_mandadoprisaostatus ); 
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

		MandadoPrisaoStatusPs obPersistencia = new  MandadoPrisaoStatusPs(obFabricaConexao.getConexao()); 

			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

}