package br.gov.go.tj.projudi.ne;


import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.ComarcaCidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.dt.RegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaBairroRegiaoDt;
import br.gov.go.tj.projudi.dt.ZonaDt;
import br.gov.go.tj.projudi.ps.ZonaBairroRegiaoPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ZonaBairroRegiaoNe extends Negocio{

    /**
	 * 
	 */
	private static final long serialVersionUID = -6772505349704824586L;
	
	protected  ZonaBairroRegiaoDt obDados;
	
	public ZonaBairroRegiaoNe() {
		obLog = new LogNe(); 

		obDados = new ZonaBairroRegiaoDt(); 
	}

	public String Verificar(ZonaBairroRegiaoDt dados ) {
		String stRetorno="";
		
		if (dados.getId_Bairro().equalsIgnoreCase("")) {
			stRetorno += "Bairro é é obrigatório.";
		}
		if (dados.getId_Zona().equalsIgnoreCase("")) {
			stRetorno += "Zona é é obrigatório.";
		}
		if (dados.getId_Regiao().equalsIgnoreCase("")) {
			stRetorno += "Região é é obrigatório.";
		}
		return stRetorno;

	}
	
	public String VerificarSalvar(ZonaBairroRegiaoDt dados ) throws Exception {
		String stRetorno = this.Verificar(dados);
		
		if (stRetorno.trim().length() == 0) {
			ZonaBairroRegiaoDt regiaoCadastrada = this.consultarIdBairro(dados.getId_Bairro());
			
			if (regiaoCadastrada != null) {
				if ((dados.getId() == null || dados.getId().trim().length() == 0) ||
					(!dados.getId().trim().equalsIgnoreCase(regiaoCadastrada.getId().trim()))) {
					return "O bairro informado já está sendo utilizado com o identificador " + regiaoCadastrada.getId() + " - Zona: " + regiaoCadastrada.getZona() + " - Região: " + regiaoCadastrada.getRegiao() + ".";	
				} 
			}	
		}
		
		if (stRetorno.trim().length() == 0) {
			BairroDt bairroDt = new BairroNe().consultarId(dados.getId_Bairro());
			ZonaDt zonaDt = new ZonaNe().consultarId(dados.getId_Zona());
			RegiaoDt regiaoDt = new RegiaoNe().consultarId(dados.getId_Regiao());
			
			if (bairroDt != null && zonaDt != null && regiaoDt != null) {
				
				ComarcaCidadeDt comarcaCidadeDoBairroDt = new ComarcaCidadeNe().consultarIdCidade(bairroDt.getId_Cidade());
				
				if (comarcaCidadeDoBairroDt == null) {
					return "A cidade do bairro selecionado não está vinculada a nenhuma comarca.";
				}
				
				ComarcaDt comarcaDt = null;
				
				if (!comarcaCidadeDoBairroDt.getId_Comarca().trim().equalsIgnoreCase(zonaDt.getId_Comarca())) {
					comarcaDt = new ComarcaNe().consultarId(comarcaCidadeDoBairroDt.getId_Comarca());
					stRetorno += "A comarca do bairro deve ser igual à comarca da Zona. Comarca bairro: " + comarcaDt.getComarca() + " - Comarca da zona: " + zonaDt.getComarca() + ".";
				}
				
				if (!comarcaCidadeDoBairroDt.getId_Comarca().trim().equalsIgnoreCase(regiaoDt.getId_Comarca())) {
					if (comarcaDt == null) comarcaDt = new ComarcaNe().consultarId(comarcaCidadeDoBairroDt.getId_Comarca());
					stRetorno += "A comarca do bairro deve ser igual à comarca da Região. Comarca bairro: " + comarcaDt.getComarca() + " - Comarca da região: " + regiaoDt.getComarca() + ".";
				}
								
			} else {
				
				if (bairroDt == null) {
					stRetorno += "Bairro não cadastrado.";
				}
				
				if (zonaDt == null) {
					stRetorno += "Zona não cadastrada.";
				}
				
				if (regiaoDt == null) {
					stRetorno += "Região não cadastrada.";
				}
				
			}
		}
		
		return stRetorno;
	}
	
	public void salvar(ZonaBairroRegiaoDt dados) throws Exception {

		LogDt obLogDt;
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ZonaBairroRegiaoPs obPersistencia = new ZonaBairroRegiaoPs(obFabricaConexao.getConexao());
			if (dados.getId().length()==0) {
				obPersistencia.inserir(dados);
				obLogDt = new LogDt("ZonaBairroRegiao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir),"",dados.getPropriedades());
			}else {
				obPersistencia.alterar(dados); 
				obLogDt = new LogDt("ZonaBairroRegiao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),dados.getPropriedades());
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
	
	public void excluir(ZonaBairroRegiaoDt dados) throws Exception {

		LogDt obLogDt;

		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ZonaBairroRegiaoPs obPersistencia = new ZonaBairroRegiaoPs(obFabricaConexao.getConexao());
			obLogDt = new LogDt("ZonaBairroRegiao",dados.getId(), dados.getId_UsuarioLog(),dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),dados.getPropriedades(),"");
			obPersistencia.excluir(dados.getId());

			dados.limpar();
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		}catch(Exception e){ 
			obFabricaConexao.cancelarTransacao();
			throw e;
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public ZonaBairroRegiaoDt consultarId(String id_zonaBairroRegiao ) throws Exception {

		ZonaBairroRegiaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ZonaBairroRegiaoPs obPersistencia = new ZonaBairroRegiaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarId(id_zonaBairroRegiao ); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
	
	public ZonaBairroRegiaoDt consultarIdBairro(String id_bairro) throws Exception {

		ZonaBairroRegiaoDt dtRetorno=null;
		FabricaConexao obFabricaConexao = null;
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ZonaBairroRegiaoPs obPersistencia = new ZonaBairroRegiaoPs(obFabricaConexao.getConexao());
			dtRetorno= obPersistencia.consultarIdBairro(id_bairro); 
			obDados.copiar(dtRetorno);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return dtRetorno;
	}
			
	public String consultarDescricaoJSON(String descricao, String cidade, String bairro, String regiao, String posicao) throws Exception { 
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ZonaBairroRegiaoPs obPersistencia = new  ZonaBairroRegiaoPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, cidade, bairro, regiao, posicao);
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public String consultarDescricaoZonaJSON(String tempNomeBusca, String comarca, String PosicaoPaginaAtual) throws Exception { 
		String stTemp = "";
		ZonaNe zonaNe = new ZonaNe(); 
		stTemp = zonaNe.consultarDescricaoJSON(tempNomeBusca, comarca ,PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoBairroJSON(String tempNomeBusca, String cidade, String uf, String zona, String PosicaoPaginaAtual) throws Exception { 
		String stTemp = "";
		BairroNe bairroNe = new BairroNe(); 
		stTemp = bairroNe.consultarDescricaoJSON(tempNomeBusca, cidade, uf, zona, PosicaoPaginaAtual);
		return stTemp;
	}
	
	public String consultarDescricaoRegiaoJSON(String tempNomeBusca, String comarca, String PosicaoPaginaAtual) throws Exception { 
		String stTemp = "";
		RegiaoNe regiaoNe = new RegiaoNe(); 
		stTemp = regiaoNe.consultarDescricaoJSON(tempNomeBusca, comarca, PosicaoPaginaAtual);
		return stTemp;
	}

	public String consultarId_Regiao(String id_bairro) throws Exception {
		String  id_regiao="";
		FabricaConexao obFabricaConexao = null;
		
		try{ 
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			ZonaBairroRegiaoPs obPersistencia = new ZonaBairroRegiaoPs(obFabricaConexao.getConexao());
			id_regiao= obPersistencia.consultarId_Regiao(id_bairro);			
		} finally {
			obFabricaConexao.fecharConexao();
		}
		return id_regiao;
	}
	
	public BairroDt consultarBairroId(String id_bairro) throws Exception {
		return new BairroNe().consultarId(id_bairro);
	}
}
