package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioAfastamentoDt;
import br.gov.go.tj.projudi.ps.UsuarioAfastamentoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class UsuarioAfastamentoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8733701388334933143L;
	
	
	protected  UsuarioAfastamentoDt obDados;
		 

	public UsuarioAfastamentoNeGen() {			

		obLog = new LogNe(); 

		obDados = new UsuarioAfastamentoDt(); 

	}


//---------------------------------------------------------
	public void salvar(UsuarioAfastamentoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neUsuarioAfastamentosalvar()");
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			UsuarioAfastamentoPs obPersistencia = new UsuarioAfastamentoPs(obFabricaConexao.getConexao());
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

		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(UsuarioAfastamentoDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioAfastamentoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			obFabricaConexao.iniciarTransacao();
			UsuarioAfastamentoPs obPersistencia = new UsuarioAfastamentoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("UsuarioAfastamento", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		}catch(Exception e){
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public UsuarioAfastamentoDt consultarId(String id_usuarioafastamento ) throws Exception {

		UsuarioAfastamentoDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_UsuarioAfastamento" );
		FabricaConexao obFabricaConexao = null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			UsuarioAfastamentoPs obPersistencia = new UsuarioAfastamentoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuarioafastamento ); 
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
			try{
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				UsuarioAfastamentoPs obPersistencia = new UsuarioAfastamentoPs(obFabricaConexao.getConexao());
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
