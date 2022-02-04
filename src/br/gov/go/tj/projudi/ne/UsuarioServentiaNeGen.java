package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaDt;
import br.gov.go.tj.projudi.ps.UsuarioServentiaPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class UsuarioServentiaNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5026921239160458663L;	
	
	
	protected  UsuarioServentiaDt obDados;
	
	 


	public UsuarioServentiaNeGen() {		

		obLog = new LogNe(); 

		obDados = new UsuarioServentiaDt(); 

	}


//---------------------------------------------------------
	public void salvar(UsuarioServentiaDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..neUsuarioServentiasalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioServentia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("UsuarioServentia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(UsuarioServentiaDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioServentiaDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("UsuarioServentia", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public UsuarioServentiaDt consultarId(String id_usuarioserventia ) throws Exception {

		UsuarioServentiaDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_UsuarioServentia" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuarioserventia ); 
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
		//////System.out.println("..ne-ConsultaUsuarioServentia" ); 
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				UsuarioServentiaPs obPersistencia = new UsuarioServentiaPs(obFabricaConexao.getConexao());
				tempList=obPersistencia.consultarDescricao( descricao, posicao);
				//stUltimaConsulta=descricao;
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

	public List consultarDescricaoServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ServentiaNe Serventiane = new ServentiaNe(); 
			tempList = Serventiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Serventiane.getQuantidadePaginas();
			Serventiane = null;
		
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

	public List consultarDescricaoBairro(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			BairroNe Bairrone = new BairroNe(); 
			tempList = Bairrone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Bairrone.getQuantidadePaginas();
			Bairrone = null;
		
		return tempList;
	}

	public List consultarDescricaoCidade(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			CidadeNe Cidadene = new CidadeNe(); 
			tempList = Cidadene.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Cidadene.getQuantidadePaginas();
			Cidadene = null;
		
		return tempList;
	}

	public List consultarDescricaoUsuarioServentia(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			UsuarioServentiaNe UsuarioServentiane = new UsuarioServentiaNe(); 
			tempList = UsuarioServentiane.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = UsuarioServentiane.getQuantidadePaginas();
			UsuarioServentiane = null;
		
		return tempList;
	}

	}
