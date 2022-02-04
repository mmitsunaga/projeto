package br.gov.go.tj.projudi.ne;

import java.util.Date;

import br.gov.go.tj.projudi.dt.BairroDt;
import br.gov.go.tj.projudi.dt.CidadeDt;
import br.gov.go.tj.projudi.dt.ComarcaDt;
import br.gov.go.tj.projudi.dt.EnderecoDt;
import br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscArquivoDt;
import br.gov.go.tj.projudi.dt.UsuarioCejuscDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;

public class ConfirmacaoEmailInscricaoNe extends Negocio {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6225351385540176968L;
	public static final String NOME_CONTROLE 	= "ConfirmacaoEmailInscricao";
	public static final String NOME_CONTROLE_PROPOSTA 	= "ConfirmacaoEmailInscricaoPROPOSTA";
	
	public static final String CODIGO_PARA_HASH = "pr0jud1"; //projudi
	
	public static final String SEXO_NAO_ESCOLHIDO = "0";
	public static final String SEXO_FEMININO 	  = "F";
	public static final String SEXO_MASCULINO 	  = "M";
	
	public static final String CODIGO_PERFIL_NAO_ESCOLHIDO 			= "0";
	public static final String CODIGO_PERFIL_AGUARDANDO_APROVACAO 	= "1";
	public static final String CODIGO_PERFIL_APROVADO 				= "2";
	public static final String CODIGO_PERFIL_NAO_APROVADO 			= "3";
	
	public ConfirmacaoEmailInscricaoNe() {
		obLog = new LogNe();
	}
	
	/**
	 * Método para validar o código hash recebido do link do email
	 * @param String hashLink
	 * @param String emailInscricao
	 * @param String cpf
	 * @return boolean
	 * @throws Exception 
	 */
	public boolean validaHashConfirmarEmail(String hashLink, String emailInscricao, String cpf) throws Exception {
		boolean retorno = false;
			
		if( hashLink != null && emailInscricao != null && cpf != null ) {
			String hash = Funcoes.GeraHashMd5(emailInscricao + cpf + ConfirmacaoEmailInscricaoNe.CODIGO_PARA_HASH);
			if( hash != null && hash.equals(hashLink) ) {
				retorno = true;
			}
		}
		
		return retorno;
	}
	
	/**
	 * Método para pesquisar cidade para o select2 json.
	 * @param String tempNomeBusca1
	 * @param String tempNomeBusca2
	 * @param String posicaoPaginaAtual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoCidadeJSON(String tempNomeBusca1, String tempNomeBusca2, String posicaoPaginaAtual) throws Exception {
		CidadeNe cidadeNe = new CidadeNe();
		
		return cidadeNe.consultarDescricaoJSON(tempNomeBusca1, tempNomeBusca2, posicaoPaginaAtual);
	}
	
	/**
	 * Método para pesquisar comarca para o select2 json.
	 * @param String tempNomeBusca
	 * @param String posicaoPaginaAtual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoComarcaJSON(String tempNomeBusca, String posicaoPaginaAtual) throws Exception {
		ComarcaNe neObjeto = new ComarcaNe();
		
		return neObjeto.consultarDescricaoJSON(tempNomeBusca, posicaoPaginaAtual);
	}
	
	/**
	 * Método para consulta de Bairro para o select2 json.
	 * @param String descricao
	 * @param String cidade
	 * @param String uf
	 * @param String posicao
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoBairroJSON(String descricao, String cidade, String uf, String posicao) throws Exception {
		BairroNe bairroNe = new BairroNe();
		
		return bairroNe.consultarDescricaoJSON(descricao, cidade, uf, posicao);
	}
	
	/**
	 * Método para consultar os dados de orgão expedidor para o select2 json.
	 * @param String tempNomeBusca
	 * @param String sigla
	 * @param String posicaoPaginaAtual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoOrgaoExpedidorJSON(String tempNomeBusca, String sigla, String posicaoPaginaAtual) throws Exception {
		RgOrgaoExpedidorNe rgOrgaoExpedidorNe = new RgOrgaoExpedidorNe();
		
		return rgOrgaoExpedidorNe.consultarDescricaoJSON(sigla, tempNomeBusca, posicaoPaginaAtual);
	}
	
	/**
	 * Método para consultar os dados de serventia para o select2 json.
	 * @param String tempNomeBusca
	 * @param String idComarca
	 * @param String posicaoPaginaAtual
	 * @return String
	 * @throws Exception
	 */
	public String consultarDescricaoServentiaPreProcessualComarcaJSON(String tempNomeBusca, String idComarca, String posicaoPaginaAtual) throws Exception {
		ServentiaNe serventiaNe = new ServentiaNe();
		
		if( tempNomeBusca == null ) {
			tempNomeBusca = "";
		}
		
		return serventiaNe.consultarDescricaoServentiaPreProcessualComarcaJSON(tempNomeBusca, idComarca, posicaoPaginaAtual);
	}
	
	/**
	 * Método para salvar o usuário Cejusc(usuário, endereco e usuario-Cejusc).
	 * 
	 * @param UsuarioCejuscDt usuarioCejuscDt
	 * @throws Exception
	 */
	public void salvarUsuarioCejusc(UsuarioCejuscDt usuarioCejuscDt) throws Exception {
		
		UsuarioNe usuarioNe = new UsuarioNe();
		UsuarioCejuscNe usuarioCejuscNe = new UsuarioCejuscNe();
		UsuarioCejuscArquivoNe usuarioCejuscArquivoNe = new UsuarioCejuscArquivoNe();
		EnderecoNe enderecoNe = new EnderecoNe();
		
		FabricaConexao obFabricaConexao = null;
		
		obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
		try{

			obFabricaConexao.iniciarTransacao();
			
			//Tarefas padrão para cadastro de usuários
			if(usuarioCejuscDt != null && usuarioCejuscDt.getId() == null) {
				usuarioCejuscDt.setDataInscricao(Funcoes.DataHora(new Date()));
			}
			usuarioCejuscDt.setDataStatusAtual(Funcoes.DataHora(new Date()));
			usuarioCejuscDt.setCodigoStatusAtual(UsuarioCejuscDt.CODIGO_STATUS_AGUARDANDO_AVALIACAO);
			
			//Para o log
			usuarioCejuscDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			usuarioCejuscDt.setIpComputadorLog(usuarioCejuscDt.getIpComputadorLog());
			usuarioCejuscDt.getUsuarioDt().setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			usuarioCejuscDt.getUsuarioDt().setIpComputadorLog(usuarioCejuscDt.getIpComputadorLog());
			
			//Salva os dados
			//Salvar enderecoDt
			usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setId_UsuarioLog(UsuarioDt.SistemaProjudi);
			enderecoNe.salvar(usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario(), obFabricaConexao);
			usuarioCejuscDt.getUsuarioDt().setId_Endereco( usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getId() );
			
			//Salvar UsuarioDt
			//String senha = Long.toString(new Date().getTime());
			usuarioCejuscDt.getUsuarioDt().setUsuario(usuarioCejuscDt.getUsuarioDt().getCpf());
			usuarioNe.salvar(usuarioCejuscDt.getUsuarioDt(), obFabricaConexao);
			
			//Salva UsuarioCejuscDt
			usuarioCejuscDt.setCodigoStatusAtual(UsuarioCejuscDt.CODIGO_STATUS_AGUARDANDO_AVALIACAO);
			usuarioCejuscDt.setCodigoStatusAnterior(UsuarioCejuscDt.CODIGO_STATUS_AGUARDANDO_AVALIACAO);
			usuarioCejuscDt.setDataStatusAtual(Funcoes.DataHora(new Date()));
			usuarioCejuscDt.setDataStatusAnterior(Funcoes.DataHora(new Date()));
			usuarioCejuscNe.salvar(usuarioCejuscDt, obFabricaConexao);
			
			//Salvar UsuarioCejuscArquivoDt
			if( usuarioCejuscDt.getListaUsuarioCejuscArquivoDt() != null && usuarioCejuscDt.getListaUsuarioCejuscArquivoDt().size() > 0 ) {
				for( UsuarioCejuscArquivoDt usuarioCejuscArquivoDt : usuarioCejuscDt.getListaUsuarioCejuscArquivoDt() ) {
					
					usuarioCejuscArquivoDt.setId_UsuarioLog(UsuarioDt.SistemaProjudi);
					usuarioCejuscArquivoDt.setIpComputadorLog(usuarioCejuscDt.getIpComputadorLog());
					usuarioCejuscArquivoDt.setUsuarioCejuscDt(usuarioCejuscDt);
					
					usuarioCejuscArquivoNe.salvar(usuarioCejuscArquivoDt, obFabricaConexao);
				}
			}
			
			//Envia email confirmando inscrição
			new InscricaoConciliadorMediadorNe().enviarEmailComprovandoInscricao(usuarioCejuscDt.getUsuarioDt().getEMail(), usuarioCejuscDt.getUsuarioDt().getNome());
			
			obFabricaConexao.finalizarTransacao();
		}
		catch(Exception e) {
			obFabricaConexao.cancelarTransacao();
			throw e;
		}
		finally {
			obFabricaConexao.fecharConexao();
		}
	}
	
	/**
	 * Método para salvar o UsuarioCejuscDt
	 * @param UsuarioCejuscDt dados
	 * @param obFabricaConexao
	 * @throws Exception
	 */
//	protected void salvar(UsuarioCejuscDt dados, FabricaConexao obFabricaConexao) throws Exception {
//		LogDt obLogDt;
//		UsuarioCejuscPs obPersistencia = new  UsuarioCejuscPs(obFabricaConexao.getConexao());
//		
//		if (dados.getId() == null || dados.getId().equalsIgnoreCase("")) {
//			obPersistencia.inserir(dados);
//			obLogDt = new LogDt("UsuarioCejusc", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Incluir), "", dados.getPropriedades());
//		}
//		else {
//			UsuarioCejuscDt obDados = this.consultarId(dados.getId(), obFabricaConexao);
//			obPersistencia.alterar(dados);
//			obLogDt = new LogDt("UsuarioCejusc", dados.getId(), dados.getId_UsuarioLog(), dados.getIpComputadorLog(), String.valueOf(LogTipoDt.Alterar), obDados.getPropriedades(), dados.getPropriedades());
//		}
//		
//		obLog.salvar(obLogDt, obFabricaConexao);
//	}
	
	/**
	 * Método para consultar UsuarioCejuscDt
	 * @param String idUsuarioCejusc
	 * @param FabricaConexao obFabricaConexao
	 * @return UsuarioCejuscDt
	 * @throws Exception
	 */
//	public UsuarioCejuscDt consultarId(String idUsuarioCejusc, FabricaConexao obFabricaConexao) throws Exception {
//		UsuarioCejuscPs obPersistencia = new  UsuarioCejuscPs(obFabricaConexao.getConexao());
//		return obPersistencia.consultarId(idUsuarioCejusc);
//	}
	
	/**
	 * Método para verificar se o Cejusc está na base de inscrições pelo **email**.
	 * @param UsuarioCejuscDt usuarioCejuscDt
	 * @throws Exception
	 */
	public void isEmailCejuscCadastrado(UsuarioCejuscDt usuarioCejuscDt) throws Exception {
		if( usuarioCejuscDt != null && usuarioCejuscDt.getUsuarioDt().getEMail() != null && usuarioCejuscDt.getUsuarioDt().getEMail().contains("@") ) {
			//UsuarioCejuscDt usuarioCejuscDtAUX = this.consultaUsuarioCejuscDtPorEmail(usuarioCejuscDt);
		}
	}
	
	/**
	 * Método para verificar se o Cejusc está na base de usuários.
	 * @return UsuarioCejuscDt
	 * @throws Exception 
	 */
	public void isCejuscCadastrado(UsuarioCejuscDt usuarioCejuscDt) throws Exception {
//		//Verificar se o usuário já tem cadastro
//		if( usuarioCejuscDt.getUsuarioDt().getCpf() != null ) {
//			UsuarioDt usuarioDt = this.consultaUsuarioDt(usuarioCejuscDt.getUsuarioDt().getCpf());
//			
//			if( usuarioDt != null && usuarioDt.getId() != null && usuarioDt.getId().length() > 0 ) {
//
//				//Consulta o usuário completo pelo Id
//				usuarioDt = this.consultaUsuarioDtPorId(usuarioDt.getId());
//				
//				//Iguais, ou seja, já existe na base de usuários
//				if( usuarioDt.getCpf() != null && usuarioDt.getCpf().equals(usuarioCejuscDt.getUsuarioDt().getCpf()) ) {
//					
//					//extrai informações do banco para a tela do Cejusc
//					usuarioCejuscDt.getUsuarioDt().setId(usuarioDt.getId());
//					
//					usuarioCejuscDt.getUsuarioDt().setNome(usuarioDt.getNome());
//					usuarioCejuscDt.getUsuarioDt().setCpf(usuarioDt.getCpf());
//					usuarioCejuscDt.getUsuarioDt().setSexo(usuarioDt.getSexo());
//					usuarioCejuscDt.getUsuarioDt().setDataNascimento(usuarioDt.getDataNascimento());
//					usuarioCejuscDt.getUsuarioDt().setRg(usuarioDt.getRg());
//					usuarioCejuscDt.getUsuarioDt().setId_RgOrgaoExpedidor(usuarioDt.getId_RgOrgaoExpedidor());
//					usuarioCejuscDt.getUsuarioDt().setRgOrgaoExpedidor(usuarioDt.getRgOrgaoExpedidor());
//					usuarioCejuscDt.getUsuarioDt().setRgDataExpedicao(usuarioDt.getRgDataExpedicao());
//					usuarioCejuscDt.getUsuarioDt().setTituloEleitor(usuarioDt.getTituloEleitor());
//					usuarioCejuscDt.getUsuarioDt().setTituloEleitorZona(usuarioDt.getTituloEleitorZona());
//					usuarioCejuscDt.getUsuarioDt().setTituloEleitorSecao(usuarioDt.getTituloEleitorSecao());
//					usuarioCejuscDt.getUsuarioDt().setCtps(usuarioDt.getCtps());
//					usuarioCejuscDt.getUsuarioDt().setCtpsSerie(usuarioDt.getCtpsSerie());
//					usuarioCejuscDt.getUsuarioDt().setPis(usuarioDt.getPis());
//					usuarioCejuscDt.getUsuarioDt().setNumeroConciliador(usuarioDt.getNumeroConciliador());
//					usuarioCejuscDt.getUsuarioDt().setId_Naturalidade(usuarioDt.getId_Naturalidade());
//					usuarioCejuscDt.getUsuarioDt().setNaturalidade(usuarioDt.getNaturalidade());
//					usuarioCejuscDt.getUsuarioDt().setTelefone(usuarioDt.getTelefone());
//					usuarioCejuscDt.getUsuarioDt().setCelular(usuarioDt.getCelular());
//					
//					//O endereco manter o preenchido na inscricao
////					usuarioCejuscDt.getUsuarioDt().setEnderecoUsuario(new EnderecoDt());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setId(usuarioDt.getId());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setLogradouro(usuarioDt.getLogradouro());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setNumero(usuarioDt.getNumero());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setComplemento(usuarioDt.getComplemento());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setId_Bairro(usuarioDt.getId_Bairro());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setBairro(usuarioDt.getBairro());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setId_Cidade(usuarioDt.getId_Cidade());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setCidade(usuarioDt.getCidade());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setUf(usuarioDt.getUf());
////					usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().setCep(usuarioDt.getCep());
//				}
//			}
//		}
	}
	
	/**
	 * Método para consultar usuário via número cpf.
	 * @param String cpf
	 * @return UsuarioDt
	 * @throws Exception
	 */
	public UsuarioDt consultarUsuarioCpf(String cpf) throws Exception {
		UsuarioNe usuarioNe = new UsuarioNe();
		UsuarioDt usuarioDt = null;
		
		if( cpf != null && cpf.length() > 0 ) {
			usuarioDt = usuarioNe.consultarUsuarioCpf(cpf);
		}
		
		return usuarioDt;
	}
	
	/**
	 * Método para consultar UsuarioDt por ID
	 * @param String idUsuario
	 * @return UsuarioDt
	 * @throws Exception
	 */
	public UsuarioDt consultaUsuarioDtPorId(String idUsuario) throws Exception {
		UsuarioNe usuarioNe = new UsuarioNe();
		UsuarioDt usuarioDt = null;
		
		if( idUsuario != null && idUsuario.length() > 0 ) {
			usuarioDt = usuarioNe.consultarId(idUsuario);
		}
		
		return usuarioDt;
	}
	
	/**
	 * Método para consultar enderecoDt pelo Id.
	 * @param String idEndereco
	 * @return EnderecoDt
	 * @throws Exception
	 */
	public EnderecoDt consultaEnderecoDtPorId(String idEndereco) throws Exception {
		EnderecoNe enderecoNe = new EnderecoNe();
		EnderecoDt enderecoDt = null;
		
		if( idEndereco != null && idEndereco.length() > 0 ) {
			enderecoDt = enderecoNe.consultarId(idEndereco);
		}
		
		return enderecoDt;
	}
	
	/**
	 * Método para consultar a comarcaDt
	 * @param String idComarca
	 * @return ComarcaDt
	 * @throws Exception
	 */
	public ComarcaDt consultaComarcaDtId(String idComarca) throws Exception {
		return new ComarcaNe().consultarId(idComarca);
	}
	
	/**
	 * Método para consultar a serventiaDt
	 * @param String idServentia
	 * @return ServentiaDt
	 * @throws Exception
	 */
	public ServentiaDt consultaServentiaDtId(String idServentia) throws Exception {
		return new ServentiaNe().consultarId(idServentia);
	}
	
	/**
	 * Método para consulta de CidadeDt pelo Id.
	 * @param CidadeDt idCidadeDt
	 * @return CidadeDt
	 * @throws Exception
	 */
	public CidadeDt consultaCidadeDtId(String idCidadeDt) throws Exception {
		CidadeNe cidadeNe = new CidadeNe();
		return cidadeNe.consultarId(idCidadeDt);
	}
	
	/**
	 * Método para consulta de BairroDt pelo Id.
	 * @param BairroDt idBairroDt
	 * @return BairroDt
	 * @throws Exception
	 */
	public BairroDt consultaBairroDtId(String idBairroDt) throws Exception {
		BairroNe bairroNe = new BairroNe();
		return bairroNe.consultarId(idBairroDt);
	}
	
	/**
	 * Método para consulta de RgOrgaoExpedidorDt pelo Id.
	 * @param RgOrgaoExpedidorDt idRgOrgaoExpedidorDt
	 * @return RgOrgaoExpedidorDt
	 * @throws Exception
	 */
	public RgOrgaoExpedidorDt consultaRgOrgaoExpedidorDtId(String idRgOrgaoExpedidorDt) throws Exception {
		RgOrgaoExpedidorNe rgOrgaoExpedidorNe = new RgOrgaoExpedidorNe();
		return rgOrgaoExpedidorNe.consultarId(idRgOrgaoExpedidorDt);
	}
}
