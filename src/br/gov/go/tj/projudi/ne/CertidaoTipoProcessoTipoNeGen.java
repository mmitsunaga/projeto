package br.gov.go.tj.projudi.ne;

import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertidaoTipoProcessoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CertidaoTipoProcessoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;
//---------------------------------------------------------
public abstract class CertidaoTipoProcessoTipoNeGen extends Negocio{


	/**
	 * 
	 */
	private static final long serialVersionUID = -5958317189549043858L;
	protected  CertidaoTipoProcessoTipoDt obDados;

	protected List lisGeral = null; 
	public CertidaoTipoProcessoTipoNeGen() {

		

		obLog = new LogNe(); 

		obDados = new CertidaoTipoProcessoTipoDt(); 

	}


//---------------------------------------------------------
	public void salvarMultiplo(CertidaoTipoProcessoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neCertidaoTipoProcessoTiposalvarMultiplo()");
		//verifico os ids as serem excluidos
		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			CertidaoTipoProcessoTipoDt obDt = (CertidaoTipoProcessoTipoDt)lisGeral.get(i);
			boolean boEncontrado =false;
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("")){
				//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
				if(listaEditada != null && listaEditada.length > 0) {
					//verifico qual id saiu da lista editada
					for(int j=0; j< listaEditada.length; j++) {
						if (obDt.getId_ProcessoTipo().equalsIgnoreCase((String)listaEditada[j])){
							boEncontrado = true;
							break;
						}
					}
				}
				//se o id do objeto não foi encontrado na lista editada coloco o objeto para ser excluido
				if (!boEncontrado) {
					lisExcluir.add(obDt);
				}
			}
		}

		//verifico os ids as serem incluidos
		List lisIncluir = new ArrayList();
		//verifica se a listaEditada é nula, se for é pq ficou vazia na tela e não precisa prosseguir
		if(listaEditada != null && listaEditada.length > 0) {
			for (int i = 0; i < listaEditada.length; i++){
				for(int j=0; j< lisGeral.size(); j++){
					CertidaoTipoProcessoTipoDt obDt = (CertidaoTipoProcessoTipoDt)lisGeral.get(j);
					if (obDt.getId_ProcessoTipo().equalsIgnoreCase((String)listaEditada[i]) && obDt.getId().equalsIgnoreCase("")){
						lisIncluir.add(obDt);
						break;
					}
				}
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
			for(int i = 0; i < lisIncluir.size(); i++) {
				CertidaoTipoProcessoTipoDt obDt = (CertidaoTipoProcessoTipoDt)lisIncluir.get(i);
				obPersistencia.inserir(obDt);
				obLogDt = new LogDt("CertidaoTipoProcessoTipo", obDt.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",obDt.getPropriedades());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			for(int i = 0; i < lisExcluir.size(); i++) {
				CertidaoTipoProcessoTipoDt obDtTemp = (CertidaoTipoProcessoTipoDt)lisExcluir.get(i); 

				obLogDt = new LogDt("CertidaoTipoProcessoTipo", obDtTemp.getId() , dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),obDtTemp.getPropriedades(),"");
				obPersistencia.excluir(obDtTemp.getId());
				obLog.salvar(obLogDt, obFabricaConexao);
			}
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

//---------------------------------------------------------
	public String consultarProcessoTipoCertidaoTipoUlLiCheckBox(String id_certidaotipo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null; 
		String tempUlLi="<ul>";
		//////System.out.println("..ne-consultarProcessoTipoCertidaoTipoUlLiCheckBoxCertidaoTipoProcessoTipo" ); 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarProcessoTipoCertidaoTipoGeral( id_certidaotipo);
				for(int i = 0; i < lisGeral.size(); i++) {
					CertidaoTipoProcessoTipoDt obDtTemp = (CertidaoTipoProcessoTipoDt)lisGeral.get(i); 

					tempUlLi+= obDtTemp.getListaLiCheckBox();
				}
				tempUlLi+="</ul>";
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return tempUlLi;   
	}

//---------------------------------------------------------
	public void salvar(CertidaoTipoProcessoTipoDt dados ) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		//////System.out.println("..neCertidaoTipoProcessoTiposalvar()");
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
			/* use o iu do objeto para saber se os dados ja estão ou não salvos */
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("CertidaoTipoProcessoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("CertidaoTipoProcessoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
			}

			obDados.copiar(dados);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}


//---------------------------------------------------------
	 public abstract String Verificar(CertidaoTipoProcessoTipoDt dados ); 


//---------------------------------------------------------

	public void excluir(CertidaoTipoProcessoTipoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("CertidaoTipoProcessoTipo",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}

 //---------------------------------------------------------

	public CertidaoTipoProcessoTipoDt consultarId(String id_certidaotipoprocessotipo ) throws Exception {

		CertidaoTipoProcessoTipoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null; 
		//////System.out.println("..ne-ConsultaId_CertidaoTipoProcessoTipo" );

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_certidaotipoprocessotipo ); 
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
		//////System.out.println("..ne-ConsultaCertidaoTipoProcessoTipo" ); 

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
			tempList=obPersistencia.consultarDescricao( descricao, posicao);
			QuantidadePaginas = (Long)tempList.get(tempList.size()-1);
			tempList.remove(tempList.size()-1);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return tempList;   
	}

	public List consultarDescricaoCertidaoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
		CertidaoTipoNe CertidaoTipone = new CertidaoTipoNe(); 
		tempList = CertidaoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
		QuantidadePaginas = CertidaoTipone.getQuantidadePaginas();
		CertidaoTipone = null;
		
		return tempList;
	}

	public List consultarDescricaoProcessoTipo(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		List tempList=null;
		
			ProcessoTipoNe ProcessoTipone = new ProcessoTipoNe(); 
			tempList = ProcessoTipone.consultarDescricao(tempNomeBusca, PosicaoPaginaAtual);
			QuantidadePaginas = ProcessoTipone.getQuantidadePaginas();
			ProcessoTipone = null;
		
		return tempList;
	}

	}
