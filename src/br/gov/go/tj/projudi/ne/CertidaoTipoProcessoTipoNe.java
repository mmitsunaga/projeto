package br.gov.go.tj.projudi.ne;


import java.util.ArrayList;
import java.util.List;

import br.gov.go.tj.projudi.dt.CertidaoTipoProcessoTipoDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.CertidaoTipoProcessoTipoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class CertidaoTipoProcessoTipoNe extends CertidaoTipoProcessoTipoNeGen{

    private static final long serialVersionUID = -6240945871050440378L;

    //---------------------------------------------------------
	public  String Verificar(CertidaoTipoProcessoTipoDt dados ) {

		String stRetorno="";

		if (dados.getId_CertidaoTipo().length()==0)
			stRetorno += "O Campo Id_CertidaoTipo é obrigatório.";
		////System.out.println("..neCertidaoTipoProcessoTipoVerificar()");
		return stRetorno;

	}
	
	public String consultarDescricaoCertidaoTipoJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		CertidaoTipoNe CertidaoTipone = new CertidaoTipoNe(); 
		stTemp = CertidaoTipone.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);
		
		return stTemp;
	}
	
	public List consultarProcessoTipoCertidaoTipo(String id_certidaotipo ) throws Exception {

		lisGeral=null;
		FabricaConexao obFabricaConexao = null; 

			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			try{

				CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
				lisGeral=obPersistencia.consultarProcessoTipoCertidaoTipoGeral( id_certidaotipo);
			
			}finally{
				obFabricaConexao.fecharConexao();
			}
		return lisGeral;   
	}
	
	public void incluirMultiplo(CertidaoTipoProcessoTipoDt dados, String[] listaEditada) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

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
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public void excluir(CertidaoTipoProcessoTipoDt dados, String id) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null; 

		List lisExcluir = new ArrayList();
		//pego a lista geral e procuro os que tem id
		//somente os que tem id podem ser excluidos
		for (int i = 0; i < lisGeral.size(); i++) {
			CertidaoTipoProcessoTipoDt obDt = (CertidaoTipoProcessoTipoDt)lisGeral.get(i);
			//se tiver id vejo que o mesmo não esta mais na lista editada
			if (!obDt.getId().equalsIgnoreCase("") && obDt.getId_ProcessoTipo().equalsIgnoreCase(id)){
				lisExcluir.add(obDt);
			}
		}

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			CertidaoTipoProcessoTipoPs obPersistencia = new CertidaoTipoProcessoTipoPs(obFabricaConexao.getConexao());
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

}
