package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.AreaDistribuicaoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.AreaDistribuicaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class AreaDistribuicaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -268172251369285436L;	
	protected  AreaDistribuicaoDt obDados;


	public AreaDistribuicaoNeGen() {		

		obLog = new LogNe(); 

		obDados = new AreaDistribuicaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(AreaDistribuicaoDt dados ) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neAreaDistribuicaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AreaDistribuicaoPs obPersistencia = new AreaDistribuicaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("AreaDistribuicao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("AreaDistribuicao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(AreaDistribuicaoDt dados ); 


//---------------------------------------------------------

	public void excluir(AreaDistribuicaoDt dados) throws Exception{

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			AreaDistribuicaoPs obPersistencia = new AreaDistribuicaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("AreaDistribuicao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public AreaDistribuicaoDt consultarId(String id_areadistribuicao ) throws Exception{

		AreaDistribuicaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_AreaDistribuicao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			AreaDistribuicaoPs obPersistencia = new AreaDistribuicaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_areadistribuicao); 
			obDados.copiar(dtRetorno);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}

//---------------------------------------------------------

	public long getQuantidadePaginas() {
		return QuantidadePaginas;
	}
//---------------------------------------------------------

	public List consultarDescricao(String descricao, String posicao ) throws Exception{
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaAreaDistribuicao" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				AreaDistribuicaoPs obPersistencia = new AreaDistribuicaoPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
				tempList.remove(tempList.size()-1);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempList;   
	}

	public List consultarDescricaoForum(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		ForumNe Forumne = new ForumNe(); 
		tempList = Forumne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Forumne.getQuantidadePaginas();
		Forumne = null;
		
		return tempList;
	}

	public List consultarDescricaoServentiaSubtipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaSubtipoNe ServentiaSubtipone = new ServentiaSubtipoNe(); 
			tempList = ServentiaSubtipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ServentiaSubtipone.getQuantidadePaginas();
			ServentiaSubtipone = null;
		
		return tempList;
	}

	public List consultarDescricaoAreaDistribuicaoRelacionada(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception{
		List tempList=null;
		
			AreaDistribuicaoNe AreaDistribuicaoRelacionadane = new AreaDistribuicaoNe(); 
			tempList = AreaDistribuicaoRelacionadane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = AreaDistribuicaoRelacionadane.getQuantidadePaginas();
			AreaDistribuicaoRelacionadane = null;
		
		return tempList;
	}

	public List consultarDescricaoComarca(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ComarcaNe Comarcane = new ComarcaNe(); 
			tempList = Comarcane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Comarcane.getQuantidadePaginas();
			Comarcane = null;
		
		return tempList;
	}
	
	public ForumDt consultarForum(String id_Forum) throws Exception {
		ForumDt forum = null;
		
		ForumNe forumNe = new ForumNe(); 
		forum = forumNe.consultarId(id_Forum);
		
		return forum;
	}

}
