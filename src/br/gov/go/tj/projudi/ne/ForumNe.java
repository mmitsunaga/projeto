package br.gov.go.tj.projudi.ne;


import java.util.List;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.ForumDt;
import br.gov.go.tj.projudi.dt.LogDt;
import br.gov.go.tj.projudi.dt.LogTipoDt;
import br.gov.go.tj.projudi.ps.ForumPs;
import br.gov.go.tj.utils.FabricaConexao;

public class ForumNe extends ForumNeGen{

    private static final long serialVersionUID = 6658584643105033817L;
        	
	protected  ForumDt obDados;


	public ForumNe() {
			
		obLog = new LogNe(); 
		obDados = new ForumDt(); 
	}
   
	/**
	 * M�todo respons�vel por validar os dados de cadastro de F�rum informados
	 * na tela.
	 * 
	 * @param forum - F�rum informado na tela
	 * @return mensagem de erro
	 * @author hmgodinho
	 */
    public String Verificar(ForumDt forum) {
		String mensagemErro = "";
		if (forum.getForum() == null || forum.getForum().equals("")) {
			mensagemErro += "Nome � obrigat�rio. ";
		}
		if (forum.getForumCodigo() == null || forum.getForumCodigo().equals("")) {
			mensagemErro += "C�digo � obrigat�rio. ";
		}
		if (forum.getId_Comarca() == null || forum.getId_Comarca().equals("")) {
			mensagemErro += "Comarca � obrigat�rio. ";
		}
		if (forum.getEnderecoForum().getLogradouro() == null || forum.getEnderecoForum().getLogradouro().equals("")) {
			mensagemErro += "Logradouro � obrigat�rio. ";
		}
		if (forum.getEnderecoForum().getId_Bairro() == null || forum.getEnderecoForum().getId_Bairro().equals("")) {
			mensagemErro += "Bairro � obrigat�rio. ";
		}
		if (forum.getEnderecoForum().getCep() == null || forum.getEnderecoForum().getCep().equals("")) {
			mensagemErro += "CEP � obrigat�rio. ";
		}
		return mensagemErro;
	}
	
	/**
	 * M�todo de consulta de Bairro por descri��o.
	 * @param descricao - descri��o do Bairro
	 * @param cidade - cidade do Bairro
	 * @param uf - uf da cidade
	 * @param posicao - posi��oo da p�gina
	 * @return lista de bairros
	 * @throws Exception
	 * @author hmgodinho
	 */
	public List consultarBairroDescricao(String descricao, String cidade, String uf, String posicao) throws Exception {
		List tempList = null;
		
		BairroNe Bairrone = new BairroNe();
		tempList = Bairrone.consultarDescricao(descricao, cidade, uf, posicao);
		QuantidadePaginas = Bairrone.getQuantidadePaginas();
		Bairrone = null;

		return tempList;
	}
	
	/**
	 * M�todo salva f�rum.
	 * @param forumDt - forum a ser salvo
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void salvar(ForumDt forumDt) throws Exception {
		LogDt obLogDt = new LogDt(forumDt.getId_UsuarioLog(), forumDt.getIpComputadorLog());
		EnderecoNe enderecoNe = null;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
			/* use o id do objeto para saber se os dados ja est�o ou n�o salvos */
			if (forumDt.getId().length() == 0) {

				enderecoNe = new EnderecoNe();
				String id_Endereco = new EnderecoNe().salvar(forumDt.getEnderecoForum(), obLogDt, obFabricaConexao);
				forumDt.setId_Endereco(id_Endereco);

				obPersistencia.inserir(forumDt);
				obLogDt = new LogDt("Forum", forumDt.getId(), forumDt.getId_UsuarioLog(), forumDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", forumDt.getPropriedades());
			} else {	
				// Se o endere�o n�o tiver sido cadastrado, � necess�rio cadastr�-lo antes de atualizar o f�rum
				if (forumDt.getId_Endereco().length() == 0) {
					enderecoNe = new EnderecoNe();
					String id_Endereco = new EnderecoNe().salvar(forumDt.getEnderecoForum(), obLogDt, obFabricaConexao);
					forumDt.setId_Endereco(id_Endereco);
				} else {
					// Se o endere�o tiver sido cadastrado, atualiza endere�o
					enderecoNe = new EnderecoNe();
					EnderecoDt enderecoDt = forumDt.getEnderecoForum();
					enderecoDt.setId(forumDt.getId_Endereco());
					enderecoNe.salvar(enderecoDt, obLogDt, obFabricaConexao);
				}
				
				obPersistencia.alterar(forumDt);
				obLogDt = new LogDt("Forum", forumDt.getId(), forumDt.getId_UsuarioLog(),forumDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar),obDados.getPropriedades(),forumDt.getPropriedades());
			}

			obDados.copiar(forumDt);
			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * M�todo exclui f�rum.
	 * @param forumDt - forum a ser exclu�do
	 * @throws Exception
	 * @author hmgodinho
	 */
	public void excluir(ForumDt forumDt) throws Exception {
		LogDt obLogDt;
		FabricaConexao obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
			
			obLogDt = new LogDt("Forum", forumDt.getId(), forumDt.getId_UsuarioLog(),forumDt.getIpComputadorLog(), String.valueOf(LogTipoDt.Excluir),forumDt.getPropriedades(),"");
			obPersistencia.excluir(forumDt.getId()); 

			EnderecoNe enderecoNe = new EnderecoNe();
			EnderecoDt enderecoDt = forumDt.getEnderecoForum();
			enderecoDt.setId(forumDt.getId_Endereco());
			enderecoDt.setId_UsuarioLog(forumDt.getId_UsuarioLog());
			enderecoDt.setIpComputadorLog(forumDt.getIpComputadorLog());
			enderecoNe.excluir(enderecoDt, obFabricaConexao);
			enderecoDt.limpar();

			forumDt.limpar();

			obLog.salvar(obLogDt, obFabricaConexao);
			obFabricaConexao.finalizarTransacao();

		
		}finally{
			obFabricaConexao.fecharConexao();
		}
	}
	
	public String consultarDescricaoJSON(String descricao, String posicao ) throws Exception {
		String stTemp = "";
		FabricaConexao obFabricaConexao = null;

		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			ForumPs obPersistencia = new  ForumPs(obFabricaConexao.getConexao());
			stTemp = obPersistencia.consultarDescricaoJSON(descricao, posicao);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return stTemp;   
	}
	
	public BairroDt consultarDescricaoBairroId(String idBairro) throws Exception {
		BairroDt bairroDt = new BairroDt();
		
		BairroNe Bairrone = new BairroNe();
		bairroDt = Bairrone.consultarId(idBairro);
		
		return bairroDt;
	}
	
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		String stTemp = "";
		
		BairroNe Bairrone = new BairroNe();
		stTemp = Bairrone.consultarDescricaoJSON(descricao, cidade, uf, "", posicao);
		
		return stTemp;
	}
	
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String PosicaoPaginaAtual ) throws Exception {
		String stTemp = "";
		
		ComarcaNe Comarcane = new ComarcaNe(); 
		stTemp = Comarcane.consultarDescricaoJSON(tempNomeBusca, PosicaoPaginaAtual);

		return stTemp;
	}

	public ForumDt consultarForumCodigo(String forumCodigo ) throws Exception {
		ForumDt retorno = null;
		FabricaConexao obFabricaConexao = null;

		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			ForumPs obPersistencia = new  ForumPs(obFabricaConexao.getConexao());
			retorno = obPersistencia.consultarForumCodigo(forumCodigo);
		
		}finally{
			obFabricaConexao.fecharConexao();
		}
		return retorno;   
	}
	
	public ForumDt consultarId(String id_forum, FabricaConexao obFabricaConexao) throws Exception {
		ForumDt dtRetorno=null;
		ForumPs obPersistencia = new ForumPs(obFabricaConexao.getConexao());
		dtRetorno= obPersistencia.consultarId(id_forum ); 
		if (dtRetorno != null) obDados.copiar(dtRetorno);
		return dtRetorno;
	}
	
	public ForumDt consultarForumCodigo(String forumCodigo, FabricaConexao obFabricaConexao) throws Exception {
		ForumPs obPersistencia = new  ForumPs(obFabricaConexao.getConexao());
		return obPersistencia.consultarForumCodigo(forumCodigo);		
	}
}
