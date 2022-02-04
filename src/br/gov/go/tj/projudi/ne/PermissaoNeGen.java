package br.gov.go.tj.projudi.ne;

import java.util.List;

import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.PermissaoDt;
import br.gov.go.tj.projudi.ps.PermissaoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class PermissaoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = 4162286198197850297L;
	
	protected  PermissaoDt obDados;
		 

	public PermissaoNeGen() {
		

		obLog = new LogNe(); 

		obDados = new PermissaoDt(); 

	}


//---------------------------------------------------------
	public void salvar(PermissaoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;

		//////System.out.println("..nePermissaosalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().equalsIgnoreCase("" ) ) {				
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("Permissao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {				
				obPersistencia.alterar(dados);
				obLogDt = new LogDt("Permissao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(PermissaoDt dados ); 


//---------------------------------------------------------

	public void excluir(PermissaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("Permissao", dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId()); dados.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public PermissaoDt consultarId(String id_permissao ) throws Exception {

		PermissaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaId_Permissao" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_permissao ); 
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
		//////System.out.println("..ne-ConsultaPermissao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			//stUltimaConsulta=descricao;
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	
	public List consultarCodigoDescricao(String codigo, String descricao, String posicao ) throws Exception {
		List tempList=null;
		FabricaConexao obFabricaConexao = null;
		//////System.out.println("..ne-ConsultaPermissao" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			PermissaoPs obPersistencia = new PermissaoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarCodigoDescricao( codigo, descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}
	

	public List consultarDescricaoPermissao(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		PermissaoNe Permissaone = new PermissaoNe(); 
		tempList = Permissaone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = Permissaone.getQuantidadePaginas();
		Permissaone = null;
		
		return tempList;
	}

	public List consultarDescricaoPermissaoEspecial(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			PermissaoEspecialNe PermissaoEspecialne = new PermissaoEspecialNe(); 
			tempList = PermissaoEspecialne.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = PermissaoEspecialne.getQuantidadePaginas();
			PermissaoEspecialne = null;
		
		return tempList;
	}

	}
