package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaAfastamentoDt;
import br.gov.go.tj.projudi.ps.UsuarioServentiaAfastamentoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class UsuarioServentiaAfastamentoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8733701388334933143L;
	
	
	protected  UsuarioServentiaAfastamentoDt obDados;
		 

	public UsuarioServentiaAfastamentoNeGen() {			

		obLog = new LogNe(); 

		obDados = new UsuarioServentiaAfastamentoDt(); 

	}


//---------------------------------------------------------
	public void salvar(UsuarioServentiaAfastamentoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neUsuarioAfastamentosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaAfastamentoPs obPersistencia = new UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioAfastamento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("UsuarioAfastamento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(UsuarioServentiaAfastamentoDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioServentiaAfastamentoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaAfastamentoPs obPersistencia = new UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("UsuarioAfastamento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public UsuarioServentiaAfastamentoDt consultarId(String id_usuarioServentiaafastamento ) throws Exception {

		UsuarioServentiaAfastamentoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_UsuarioAfastamento" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaAfastamentoPs obPersistencia = new UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuarioServentiaafastamento ); 
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
		//////System.out.println("..ne-ConsultaUsuarioAfastamento" ); 
		FabricaConexao obFabricaConexao = null;
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				UsuarioServentiaAfastamentoPs obPersistencia = new UsuarioServentiaAfastamentoPs(obFabricaConexao.getConexao());
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

	public List consultarDescricaoAfastamento(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			AfastamentoNe Afastamentone = new AfastamentoNe(); 
			tempList = Afastamentone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = Afastamentone.getQuantidadePaginas();
			Afastamentone = null;
		
		return tempList;
	}

	}
