package br.gov.go.tj.projudi.ne;

import java.util.List;
import java.util.ArrayList;

import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioFoneDt;
import br.gov.go.tj.projudi.ps.UsuarioFonePs;

 import br.gov.go.tj.projudi.ne.UsuarioNe;
//---------------------------------------------------------
public abstract class UsuarioFoneNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 50849375439802345L;
	protected  UsuarioFoneDt obDados;


	public UsuarioFoneNeGen() {

		obLog = new LogNe(); 

		obDados = new UsuarioFoneDt(); 

	}

//---------------------------------------------------------
	 public abstract String Verificar(UsuarioFoneDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioFoneDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("UsuarioFone",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally{
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
			try {
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public UsuarioFoneDt consultarId(String id_usufone ) throws Exception {

		UsuarioFoneDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_usufone ); 
			obDados.copiar(dtRetorno);
		}finally{
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

			UsuarioFonePs obPersistencia = new UsuarioFonePs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoUsuario(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			UsuarioNe Usuarione = new UsuarioNe(); 
			tempList = Usuarione.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Usuarione.getQuantidadePaginas();
			Usuarione = null;
		return tempList;
	}

	public String consultarDescricaoUsuarioJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
