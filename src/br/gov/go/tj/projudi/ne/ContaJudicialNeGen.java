package br.gov.go.tj.projudi.ne;

import java.util.List;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.ContaJudicialDt;
import br.gov.go.tj.projudi.ps.ContaJudicialPs;
import br.gov.go.tj.projudi.ne.ProcessoParteNe;
import br.gov.go.tj.projudi.ne.BancoNe;
import br.gov.go.tj.projudi.ne.ComarcaNe;
import br.gov.go.tj.projudi.ne.ServentiaNe;
//---------------------------------------------------------
public abstract class ContaJudicialNeGen extends Negocio{


	protected  ContaJudicialDt obDados;


	public ContaJudicialNeGen() {

		obLog = new LogNe(); 

		obDados = new ContaJudicialDt(); 

	}


//---------------------------------------------------------
	public void salvar(ContaJudicialDt dados ) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ContaJudicialPs obPersistencia = new  ContaJudicialPs(obFabricaConexao.getConexao()); 

			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ContaJudicial",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ContaJudicial",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ContaJudicialDt dados ); 


//---------------------------------------------------------

	public void excluir(ContaJudicialDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ContaJudicialPs obPersistencia = new  ContaJudicialPs(obFabricaConexao.getConexao()); 

			obLogDt = new LogDt("ContaJudicial",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		} catch (Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
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

			ContaJudicialPs obPersistencia = new  ContaJudicialPs(obFabricaConexao.getConexao()); 

				stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
			} finally{
				obFabricaConexao.fecharConexao();
			}
		return stTemp;
	}
 //---------------------------------------------------------

	public ContaJudicialDt consultarId(String id_contajudicial ) throws Exception {

		ContaJudicialDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;


		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ContaJudicialPs obPersistencia = new  ContaJudicialPs(obFabricaConexao.getConexao()); 

			dtRetorno= obPersistencia.consultarId(id_contajudicial ); 
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

			ContaJudicialPs obPersistencia = new  ContaJudicialPs(obFabricaConexao.getConexao()); 

				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoProcessoParte(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
			ProcessoParteNe ProcessoPartene = new ProcessoParteNe(); 
			tempList = ProcessoPartene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoPartene.getQuantidadePaginas();
			ProcessoPartene = null;
		return tempList;
	}

//	public String consultarDescricaoProcessoParteJSON(String descricao, String PosicaoPaginaAtual ){
//		String stTemp="";
//			stTemp = (new ProcessoParteNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
//		return stTemp;
//	}

	public List consultarDescricaoBanco(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
			BancoNe Bancone = new BancoNe(); 
			tempList = Bancone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Bancone.getQuantidadePaginas();
			Bancone = null;
		return tempList;
	}

	public String consultarDescricaoBancoJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";
			stTemp = (new BancoNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
			ComarcaNe Comarcane = new ComarcaNe(); 
			tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Comarcane.getQuantidadePaginas();
			Comarcane = null;
		return tempList;
	}

	public String consultarDescricaoComarcaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";
			stTemp = (new ComarcaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		return tempList;
	}

	public String consultarDescricaoServentiaJSON(String descricao, String PosicaoPaginaAtual ) throws Exception{
		String stTemp="";
			stTemp = (new ServentiaNe()).consultarDescricaoJSON(descricao, PosicaoPaginaAtual);
		return stTemp;
	}

	}
