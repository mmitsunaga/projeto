package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.ForumPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class ForumNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -1104106818948951736L;
	protected  ForumDt obDados;


	public ForumNeGen() {	

		obLog = new LogNe(); 

		obDados = new ForumDt(); 

	}


//---------------------------------------------------------
	public void salvar(ForumDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neForumsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Forum", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Forum", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(ForumDt dados ); 


//---------------------------------------------------------

	public void excluir(ForumDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Forum", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public ForumDt consultarId(String id_forum ) throws Exception {

		ForumDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Forum" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_forum ); 
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
		//////System.out.println("..ne-ConsultaForum" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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

	public List consultarDescricaoEndereco(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			EnderecoNe Enderecone = new EnderecoNe(); 
			tempList = Enderecone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Enderecone.getQuantidadePaginas();
			Enderecone = null;
		
		return tempList;
	}

	}
