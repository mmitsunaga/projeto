package br.gov.go.tj.projudi.ne;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.gov.go.tj.projudi.dt.PonteiroCejuscDt;
import br.gov.go.tj.projudi.dt.PonteiroCejuscStatuDt;
import br.gov.go.tj.projudi.dt.ServentiaDt;
import br.gov.go.tj.projudi.dt.UsuarioDt;
import br.gov.go.tj.projudi.ps.PonteiroCejuscPs;
import br.gov.go.tj.projudi.util.GerenciadorEmail;
import br.gov.go.tj.utils.FabricaConexao;
import br.gov.go.tj.utils.Funcoes;
import br.gov.go.tj.utils.MensagemException;
import br.gov.go.tj.utils.Relatorios;
import net.sf.jasperreports.engine.JRException;

//---------------------------------------------------------
public class PonteiroCejuscNe extends PonteiroCejuscNeGen{

	private static final long serialVersionUID = 2848330028369014544L;

	//---------------------------------------------------------
	
	public static final String LINK_CONFIRMACAO_PREFIXO 		 = "https://projudi.tjgo.jus.br/ConfirmaLink?url=";
	public static final String LINK_CONFIRMACAO_PARAMETROS_FIXOS = "pc?";
	public static final String SAL = "3e27a687cb1ee0bdf7bb6ed35035eed3";
	
	public  String Verificar(PonteiroCejuscDt dados ) {

		String stRetorno="";

		if (dados.getUsuCejusc().length()==0)
			stRetorno += "O Campo UsuCejusc � obrigat�rio.";
		if (dados.getServ().length()==0)
			stRetorno += "O Campo Serv � obrigat�rio.";
		if (dados.getPonteiroCejuscStatus().length()==0)
			stRetorno += "O Campo PonteiroCejuscStatus � obrigat�rio.";

		return stRetorno;

	}
	
	public String distribuir(String data, String id_audiencia_tipo, String id_serventia, int diaSemana, int periodo) throws Exception {
        
        String stPonteiro="";
		FabricaConexao obFabricaConexao = null; 
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			stPonteiro = obPersistencia.distribuir(data, id_audiencia_tipo,id_serventia, diaSemana, periodo);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}        

        return stPonteiro;
	}
	
	public String distribuirComExcecao(String data, String id_audiencia_tipo, String id_serventia, int diaSemana, int periodo, String id_usu_excecao) throws Exception {
        
        String stPonteiro="";
		FabricaConexao obFabricaConexao = null; 
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			stPonteiro = obPersistencia.distribuirComExcecao(data, id_audiencia_tipo,id_serventia, diaSemana, periodo, id_usu_excecao);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}        

        return stPonteiro;
	}
	
	public List< Map<String,String> > consultaPorData(String data, String id_serventia) throws Exception {
	        
		 	List< Map<String, String> > lisPonteiroCejusc = null;
			FabricaConexao obFabricaConexao = null; 
							
			try{
			
				obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
				
				PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao());
				
				lisPonteiroCejusc = obPersistencia.consultaPorData(data, id_serventia);
					
			} finally{
				obFabricaConexao.fecharConexao();
			}        
	
	        return lisPonteiroCejusc;
		}
	
	public String consultaPorDataJSON(String data, String id_serventia, String posicaoPaginaAtual) throws Exception {
	    
	 	String lisPonteiroCejusc = null;
		FabricaConexao obFabricaConexao = null; 
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			lisPonteiroCejusc = obPersistencia.consultaPorDataJSON(data, id_serventia, posicaoPaginaAtual);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}        
	
	    return lisPonteiroCejusc;
	}
	
public String consultaPonteiroCejuscDataStatusJSON(String data, String id_serventia, int status, String posicaoPaginaAtual) throws Exception {
	    
	 	String lisPonteiroCejusc = null;
		FabricaConexao obFabricaConexao = null; 
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			PonteiroCejuscPs obPersistencia = new PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			//Se o par�metro de status for igual � um dos tipos previstos no dt, retornar
			//a lista utilizando o par�metro. Caso contr�rios, retornar null;
			if( status == PonteiroCejuscStatuDt.AVISADO     ||
				status == PonteiroCejuscStatuDt.CONFIRMADO  ||
				status == PonteiroCejuscStatuDt.SUBSTITUIDO ||
				status == PonteiroCejuscStatuDt.REALIZADO   ||
				status == PonteiroCejuscStatuDt.FALTOU
			) {
				lisPonteiroCejusc = obPersistencia.consultaPonteiroCejuscDataStatusJSON(data, id_serventia, status, posicaoPaginaAtual);
			} else {
				lisPonteiroCejusc = null;
			}
				
		} finally{
			obFabricaConexao.fecharConexao();
		}        
	
	    return lisPonteiroCejusc;
	}
	
	public String salvarPonteiroCejuscStatus(PonteiroCejuscStatuDt ponteiroCejuscStatusDt) throws Exception {
		PonteiroCejuscStatuNe ponteiroCejuscStatuNe = new PonteiroCejuscStatuNe();
		ponteiroCejuscStatuNe.salvar(ponteiroCejuscStatusDt);
		return ponteiroCejuscStatusDt.getId();
	}
	
public boolean isBancaOcupada(String id_serventia, String idCargo, String dia) throws Exception {
        
        boolean isOcupada = false;
		FabricaConexao obFabricaConexao = null; 
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.PERSISTENCIA);
			
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			isOcupada = obPersistencia.isBancaOcupada(id_serventia, idCargo, dia);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}        

        return isOcupada;
	}

	/**
	 * M�todo para enviar email para candidato informando que ele foi sorteado.
	 * 
	 * @param String idUsuCejusc
	 * @param String data
	 * @param String bancaPeriodo
	 * @param String idPonteiroCejusc
	 * @return boolean
	 * @throws Exception
	 */
	public boolean enviarEmailInformarSorteado(String idUsuCejusc, String data, String bancaPeriodo, String idPonteiroCejusc) throws Exception {
		boolean retorno = false;
		UsuarioDt usuarioDt = null;
		UsuarioNe usuarioNe = new UsuarioNe();
		PonteiroCejuscNe pcNe = new PonteiroCejuscNe();
		ServentiaNe servNe = new ServentiaNe();
		UsuarioCejuscNe usuarioCejuscNe = new UsuarioCejuscNe();
		String idUsuario;
		PonteiroCejuscDt pcDt = null;
		ServentiaDt servDt = null;
		
		if(idPonteiroCejusc != null ) {
			pcDt = pcNe.consultarId(idPonteiroCejusc);
			servDt = servNe.consultarId(pcDt.getId_Serv());
		} 
		else {
			throw new MensagemException("N�o foi poss�vel localizar o registro do sorteio.");
		}
		
		if(idUsuCejusc != null && !idUsuCejusc.equals("")) {
			idUsuario = usuarioCejuscNe.consultarIdUsu(idUsuCejusc);
			usuarioDt = usuarioNe.consultarUsuarioCompleto(idUsuario);
		}		
		
		if( usuarioDt.getEMail() != null && !usuarioDt.getEMail().equals("")) {
			
			String linkVaiComparecer = this.geraLinkConfirmacaoComparecimento(idPonteiroCejusc, "s");
			String linkNaoVaiComparecer = this.geraLinkConfirmacaoComparecimento(idPonteiroCejusc, "n");
			
			
			String mensagemEmail = "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "Ol�! Voc� foi sorteado para realizar uma audi�ncia no <b>"+ servDt.getServentia() +"</b> no dia <b>"+ data +"</b> no per�odo <b>"+ bancaPeriodo +"</b>. Para confirmar voc� deve clicar em um dos seguintes links de acordo com a sua op��o:";
			mensagemEmail += "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "- Para confirmar o seu comparecimento: <br/>";
			mensagemEmail += "<a href='"+linkVaiComparecer+"'>CLIQUE AQUI (COMPARECER)</a>";
			mensagemEmail += "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "- Para informar que N�O comparecer� na audi�ncia: <br/>";
			mensagemEmail += "<a href='"+linkNaoVaiComparecer+"'>CLIQUE AQUI (N�O COMPARECER)</a>";
			mensagemEmail += "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "<br />";
			mensagemEmail += "<p style='font-size:small'>";
			mensagemEmail += "Caso esteja com dificuldade em clicar no link acima, copie e cole o endere�o abaixo que corresponde � sua op��o em uma nova janela ou aba de seu navegador e pressione a tecla ENTER para acessar a p�gina.";
			mensagemEmail += "<br />";
			mensagemEmail += "Endere�o para confirmar o seu comparecimento: ";
			mensagemEmail += linkVaiComparecer;
			mensagemEmail += "<br />";
			mensagemEmail += "Endere�o para informar que N�O comparecer�: ";
			mensagemEmail += linkNaoVaiComparecer;
			mensagemEmail += "</p>";
			
			new GerenciadorEmail(usuarioDt.getNome(), usuarioDt.getEMail(), "TJGO: Voc� foi sorteado para uma concilia��o/media��o", mensagemEmail, GerenciadorEmail.ENVIAR_EMAIL_HTML_ADM).start();
						
			retorno = true;
		}
		
		return retorno;
	}
	
	/**
	 * Recebe um link de confirma��o de comparecimento que foi enviado para o usu�rio e processa ele.
	 * 
	 * @param String url
	 * @throws Exception
	 */
	public String processaLinkConfirmacaoComparecimento(String url) throws Exception {
		
		String comp = null;
		String id = null;
		String v = null;
		String mensagem = null;
		PonteiroCejuscNe ponteiroCejuscNe = new PonteiroCejuscNe();
		PonteiroCejuscDt ponteiroCejuscDt;
		
		String parametros[] = url.split("&");
		
		for(String param: parametros) {
			if( param.startsWith("comp=") ) {
				comp = param.substring(5);
			} 
			else if( param.startsWith("id=") ) {
				id = param.substring(3);
			}
			else if( param.startsWith("v=") ) {
				v = param.substring(2);
			}
		}
		
		if( this.validaHashLinkConfirmacao(id, v)  ) {
			
			ponteiroCejuscDt = ponteiroCejuscNe.consultarId(id);
			if(ponteiroCejuscDt != null) {
				
				//S� pode utilizar o link se o ponteiroCejusc estiver no status de "Avisado"
				if( ponteiroCejuscDt.getId_PonteiroCejuscStatus().equals( String.valueOf(PonteiroCejuscStatuDt.AVISADO) ) ) {
					
					if( comp != null & comp.equals("s") ) {
						ponteiroCejuscDt.setId_PonteiroCejuscStatus( String.valueOf(PonteiroCejuscStatuDt.CONFIRMADO) );
						mensagem  = "Presen�a na audi�ncia confirmada.";
					} else if( comp != null & comp.equals("n") ) {
						ponteiroCejuscDt.setId_PonteiroCejuscStatus( String.valueOf(PonteiroCejuscStatuDt.RECUSADO) );
						mensagem  = "Confirmado que voc� n�o comparecer�.";
					}
					ponteiroCejuscDt.setId_UsuarioLog(UsuarioDt.USUARIO_PUBLICO);
					ponteiroCejuscNe.salvar(ponteiroCejuscDt);
					
				}
				else {
					mensagem  = "Esta audi�ncia n�o pode mais ser confirmada.";
				}
			
			}
			
//			mensagem = "Link processado. Comp = " + comp;
		}
		else {
			mensagem = "Par�metros inv�lidos.";
		}

		return mensagem;
	}
	
	/**
	 * Cria o par�metro url que ser� incorporado a um e-mail e enviado para o conciliador CEJUSC
	 * para confirmar que ir� comparecer � uma audi�ncia ou avisar que n�o comparecer�. Recebe o
	 * par�metro comp que ser� utilizado para diferenciar se trata-se de um link de comparecimento
	 * ou de falta.
	 * 
	 * @param String idPonteiroCejusc
	 * @param String comp
	 * @throws Exception
	 */
	private String geraLinkConfirmacaoComparecimento(String idPonteiroCejusc, String comp) throws Exception {
		String url;
		
		url = Funcoes.aesEncrypt( LINK_CONFIRMACAO_PARAMETROS_FIXOS + "comp=" + comp + "&id=" + idPonteiroCejusc + "&v=" + this.geraHashLinkConfirmacao(idPonteiroCejusc) + "&dt=" + new Date().getTime() );
		
		return LINK_CONFIRMACAO_PREFIXO + url;
	}
	
	/**
	 * Cria um hash que ser� utilizado para identificar se os par�metros do link foram modificados pelo usu�rio. 
	 * Cria um md5 da concatena��o "ponteiroCejuscDt.getId() + ponteiroCejuscDt.getId_UsuCejusc() + ponteiroCejuscDt.getData() + SAL".
	 * Quando recebe qualquer link gera o mesmo hash das informa��es obtidas no banco e compara como que recebeu.
	 * 
	 * @param String idPonteiroCejusc
	 * @throws Exception
	 */
	private String geraHashLinkConfirmacao(String idPonteiroCejusc) throws Exception {
		String hashLinkConfirmacao = null;
		PonteiroCejuscDt ponteiroCejuscDt;
		PonteiroCejuscNe ponteiroCejuscNe = new PonteiroCejuscNe(); 
		
		if(idPonteiroCejusc != null) {
			ponteiroCejuscDt = ponteiroCejuscNe.consultarId(idPonteiroCejusc);
			hashLinkConfirmacao = Funcoes.GeraHashMd5( ponteiroCejuscDt.getId() + ponteiroCejuscDt.getId_UsuCejusc() + ponteiroCejuscDt.getData() + SAL);
		}
		
		return hashLinkConfirmacao;
	}
	
	/**
	 * Utilizando o hash, verifica se as informa��es recebidas pelo link de confirma��o s�o v�lidas ou se
	 * foram adulteradas. Recebe o idPonteiroCejusc e busca as informa��es deste registro no banco para
	 * gerar o hash e comparar com o recebido.
	 * 
	 * @param String idPonteiroCejusc
	 * @param String hashParam
	 * @throws Exception
	 */
	private boolean validaHashLinkConfirmacao(String idPonteiroCejusc, String hashParam) throws Exception {
		String hashCorreto = null;
		PonteiroCejuscDt ponteiroCejuscDt;
		PonteiroCejuscNe ponteiroCejuscNe = new PonteiroCejuscNe(); 
		
		if(idPonteiroCejusc != null) {
			ponteiroCejuscDt = ponteiroCejuscNe.consultarId(idPonteiroCejusc);
			if( ponteiroCejuscDt != null) {
				hashCorreto = Funcoes.GeraHashMd5( ponteiroCejuscDt.getId() + ponteiroCejuscDt.getId_UsuCejusc() + ponteiroCejuscDt.getData() + SAL);
			}
		}
		
		if( hashCorreto != null && hashCorreto.equals(hashParam) ) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Traz um relat�rio de quantidade de audi�ncias realizadas por conciliador/mediador.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_serventia
	 * @return
	 * @author hrrosa
	 * @since 13/03/2017
	 * @throws Exception
	 */
	public String consultaRelatorioAudienciaConciliador(String dataInicial, String dataFinal, String posicaoPaginaAtual) throws Exception {
        
	 	//List lisPonteiroCejusc = null;
		String retorno;
		
		FabricaConexao obFabricaConexao = null; 
						
		try{
		
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			
			PonteiroCejuscPs obPersistencia = new  PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			retorno = obPersistencia.consultaRelatorioAudienciaConciliadorJSON(dataInicial, dataFinal, posicaoPaginaAtual);
				
		} finally{
			obFabricaConexao.fecharConexao();
		}        

        return retorno;
	}
	
	/**
	 * Imprime um relat�rio de quantidade de audi�ncias realizadas por conciliador/mediador.
	 * 
	 * @param dataInicial
	 * @param dataFinal
	 * @param id_serventia
	 * @return
	 * @author hrrosa
	 * @throws JRException 
	 * @since 20/03/2017
	 * @throws Exception
	 */
	public byte[] relAudienciaConciliador(String dataInicial, String dataFinal, String diretorioProjetos, String diretorioProjeto) throws Exception {
			
		
		byte[] temp = null;
		FabricaConexao obFabricaConexao =null;
		try{
			obFabricaConexao = new FabricaConexao(FabricaConexao.CONSULTA);
			PonteiroCejuscPs obPersistencia = new PonteiroCejuscPs(obFabricaConexao.getConexao());
			
			String pathRelatorio = Relatorios.getPathRelatorio(diretorioProjeto);
			String nomeRelatorio = "estatisticaConciliadorCejusc";
			
			List listaEstatisticaConciliador = obPersistencia.consultaRelatorioAudienciaConciliador(dataInicial, dataFinal);

			// PAR�METROS DO RELAT�RIO
			Map parametros = new HashMap();
			parametros.put("caminhoLogo", Relatorios.getCaminhoLogotipoRelatorio(diretorioProjeto));
			parametros.put("dataAtual", new Date());
			parametros.put("titulo", "Listagem de Tipos de Processo");
			parametros.put("parametroConsulta", dataInicial + " � " + dataFinal);
			parametros.put("usuarioResponsavelRelatorio", "yyyyyyyyyyyy");

			temp = Relatorios.gerarRelatorioPdf(pathRelatorio, nomeRelatorio, parametros, listaEstatisticaConciliador);

		
		} finally{
			obFabricaConexao.fecharConexao();
		}
		return temp;
		
			
	}
}
