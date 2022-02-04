package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt;
import br.gov.go.tj.projudi.ps.UsuarioServentiaOabPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class UsuarioServentiaOabNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7067009688557672904L;
	
	
	protected  UsuarioServentiaOabDt obDados;
	
	 
	public UsuarioServentiaOabNeGen() {		

		obLog = new LogNe(); 

		obDados = new UsuarioServentiaOabDt(); 

	}


//---------------------------------------------------------
	public void salvar(UsuarioServentiaOabDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..neUsuarioServentiaOabsalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("UsuarioServentiaOab", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("UsuarioServentiaOab", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(UsuarioServentiaOabDt dados ); 


//---------------------------------------------------------

	public void excluir(UsuarioServentiaOabDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("UsuarioServentiaOab", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public UsuarioServentiaOabDt consultarId(String id_usuarioserventiaoab ) throws Exception {

		UsuarioServentiaOabDt dtRetorno=null;
		//////System.out.println("..ne-ConsultaId_UsuarioServentiaOab" );
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_usuarioserventiaoab ); 
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
		//////System.out.println("..ne-ConsultaUsuarioServentiaOab" ); 
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			UsuarioServentiaOabPs obPersistencia = new UsuarioServentiaOabPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
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
