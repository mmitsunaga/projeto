package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioArquivoDt;
import br.gov.go.tj.projudi.ps.UsuarioArquivoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class UsuarioArquivoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 6975913790902064453L;	
	protected  UsuarioArquivoDt obDados;


	public UsuarioArquivoNeGen() {
		
		obLog = new LogNe(); 

		obDados = new UsuarioArquivoDt(); 

	}


//---------------------------------------------------------
	public void salvar(UsuarioArquivoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neUsuarioArquivosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("UsuarioArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(UsuarioArquivoDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioArquivoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("UsuarioArquivo", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public UsuarioArquivoDt consultarId(String id_usuarioarquivo ) throws Exception {

		UsuarioArquivoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_UsuarioArquivo" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuarioarquivo ); 
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
		//////System.out.println("..ne-ConsultaUsuarioArquivo" ); 
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				UsuarioArquivoPs obPersistencia = new UsuarioArquivoPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoArquivo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ArquivoNe Arquivone = new ArquivoNe(); 
			tempList = Arquivone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Arquivone.getQuantidadePaginas();
			Arquivone = null;
		
		return tempList;
	}

	}
