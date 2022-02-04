package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.CejuscDisponibilidadeDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CejuscDisponibilidadePs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class CejuscDisponibilidadeNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4004791486287071096L;
	protected  CejuscDisponibilidadeDt obDados;


	public CejuscDisponibilidadeNeGen() {

		obLog = new LogNe(); 

		obDados = new CejuscDisponibilidadeDt(); 

	}


//---------------------------------------------------------
	public void salvar(CejuscDisponibilidadeDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("CejuscDisponibilidade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("CejuscDisponibilidade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			System.out.println("..ne-salvar"+ e.getMessage()); 
			throw new Exception(" <{Erro:.....}> CejuscDisponibilidadeNeGen.salvar() " + e.getMessage() );
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CejuscDisponibilidadeDt dados ); 


//---------------------------------------------------------

	public void excluir(CejuscDisponibilidadeDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("CejuscDisponibilidade",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
			
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		} finally {
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
				CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally {
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public CejuscDisponibilidadeDt consultarId(String id_cejuscdisponibilidade ) throws Exception {

		CejuscDisponibilidadeDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_cejuscdisponibilidade ); 
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

			CejuscDisponibilidadePs obPersistencia = new  CejuscDisponibilidadePs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		return tempList;
	}

	public String consultarDescricaoServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new ServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoUsuarioCejusc(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			UsuarioCejuscNe UsuarioCejuscne = new UsuarioCejuscNe(); 
			tempList = UsuarioCejuscne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioCejuscne.getQuantidadePaginas();
			UsuarioCejuscne = null;
		return tempList;
	}

	public String consultarDescricaoUsuarioCejuscJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new UsuarioCejuscNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoAudienciaTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception { 
		List tempList=null;
			AudienciaTipoNe AudienciaTipone = new AudienciaTipoNe(); 
			tempList = AudienciaTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AudienciaTipone.getQuantidadePaginas();
			AudienciaTipone = null;
		return tempList;
	}

	public String consultarDescricaoAudienciaTipoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception { 
		String stTemp = (new AudienciaTipoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

}
