<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.*"%>
<%@page import="br.gov.go.tj.projudi.dt.*"%>
<%@page import="br.gov.go.tj.projudi.ne.*"%>

<jsp:useBean id="usuarioCejuscDt" scope="session" class="br.gov.go.tj.projudi.dt.UsuarioCejuscDt"/>

<%
String nomeUsuario = "";
if( usuarioCejuscDt != null && usuarioCejuscDt.getUsuarioDt() != null && usuarioCejuscDt.getUsuarioDt().getNome() != null ) {
	nomeUsuario = usuarioCejuscDt.getUsuarioDt().getNome();
}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<title>TJGO/Projudi - Formulário de Aprovação do Candidato</title>
		<style type="text/css">
		     @import url('./css/Principal.css');
		     @import url('./css/Paginacao.css');
		     #bkg_projudi { display: none;}
		</style>
		<link type='text/css' rel='stylesheet' href='js/jscalendar/dhtmlgoodies_calendar.css?random=20051112' media='screen' />
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		<script type='text/javascript' src='./js/FuncoesGuia.js?v=20201015'></script>
		<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
		<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
		<script type='text/javascript' src='./js/Digitacao/MascararValor.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>
		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo">
		  	
			<div class="area">
				<h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Formulário de Aprovação dos Candidatos</h2>
			</div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="formAprovarCandidato" id="formAprovarCandidato">
				
				<div id="divPortaBotoes" class="divPortaBotoes">
				
				  	<input id="imgLocalizar" class="imgLocalizar" title="Localizar Candidatos Inscritos" name="imaLocalizar" type="image"  src="./imagens/imgLocalizar.png" width="23" height="23" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>');">
				  	<input id="imgLocalizar" class="imgLocalizar" title="Aprovar Candidato(a) <%=nomeUsuario%>" name="imaLocalizar" type="image"  src="./imagens/22x22/ico_sucesso.png" width="23" height="23" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');AlterarValue('PassoEditar','<%=String.valueOf(Configuracao.Curinga6)%>');">
				  	<input id="imgLocalizar" class="imgLocalizar" title="Adicionar Pendência para Candidato <%=nomeUsuario%>" name="imaLocalizar" type="image"  src="./imagens/32x32/ico_importante.png" width="23" height="23" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');AlterarValue('PassoEditar','<%=String.valueOf(Configuracao.Curinga7)%>');">
				  	<input id="imgLocalizar" class="imgLocalizar" title="Reprovar Candidato(a) <%=nomeUsuario%>" name="imaLocalizar" type="image"  src="./imagens/22x22/ico_erro.png" width="26" height="26" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');AlterarValue('PassoEditar','<%=String.valueOf(Configuracao.Curinga8)%>');">
		  		</div>
				
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				
				<div id="divEditar" class="divEditar">
				
				<%if( usuarioCejuscDt != null && usuarioCejuscDt.getId() != null && usuarioCejuscDt.getId().length() > 0 && usuarioCejuscDt.getUsuarioDt() != null && usuarioCejuscDt.getUsuarioDt().getId() != null ) { %>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend style="background-color: white;">
							Status Atual do Candidato(a)
						</legend>
						
						<div> Status </div>
						<span style="color:<%=usuarioCejuscDt.getCorLabelCodigoStatus(usuarioCejuscDt.getCodigoStatusAtual())%>;">
							<%
							if( usuarioCejuscDt.getCodigoStatusAtual() != null && usuarioCejuscDt.getCodigoStatusAtual().length() > 0 ) {
							%>
								<%=usuarioCejuscDt.getLabelCodigoStatus(usuarioCejuscDt.getCodigoStatusAtual())%>
							<%
							}
							%>
						</span>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Opções Escolhidas
						</legend>
						
						<%if( usuarioCejuscDt.getOpcaoConciliador() != null && !usuarioCejuscDt.getOpcaoConciliador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) { %>
							<div> Conciliador </div>
							<span>
								<%=usuarioCejuscDt.getLabelOpcaoPerfil(usuarioCejuscDt.getOpcaoConciliador()) %>
							</span>
						<%} %>
						
						<%if( usuarioCejuscDt.getOpcaoMediador() != null && !usuarioCejuscDt.getOpcaoMediador().equals(ConfirmacaoEmailInscricaoNe.CODIGO_PERFIL_NAO_ESCOLHIDO) ) { %>
							<div> Mediador </div>
							<span>
								<%=usuarioCejuscDt.getLabelOpcaoPerfil(usuarioCejuscDt.getOpcaoMediador()) %>
							</span>
						<%} %>
						
						<div> Voluntário </div>
						<span>
							<%if( usuarioCejuscDt.getVoluntario() != null && usuarioCejuscDt.getVoluntario().equals(UsuarioCejuscDt.CODIGO_VOLUNTARIO_SIM) ) { %>
								SIM
							<%} %>
							<%if( usuarioCejuscDt.getVoluntario() != null && usuarioCejuscDt.getVoluntario().equals(UsuarioCejuscDt.CODIGO_VOLUNTARIO_NAO) ) { %>
								NÃO
							<%} %>
						</span>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Dados Pessoais
						</legend>
						
						<div> Nome </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getNome()%>
						</span>
						
						<div> Sexo </div>
						<span>
							<%
							String sexo = "";
							if( usuarioCejuscDt.getUsuarioDt().getSexo() != null && usuarioCejuscDt.getUsuarioDt().getSexo().toString().length() > 0 && usuarioCejuscDt.getUsuarioDt().getSexo().equals(ConfirmacaoEmailInscricaoNe.SEXO_MASCULINO) ) {
								sexo = "Masculino";
							}
							if( usuarioCejuscDt.getUsuarioDt().getSexo() != null && usuarioCejuscDt.getUsuarioDt().getSexo().toString().length() > 0 && usuarioCejuscDt.getUsuarioDt().getSexo().equals(ConfirmacaoEmailInscricaoNe.SEXO_FEMININO) ) {
								sexo = "Feminina";
							}
							%>
							<%=sexo%>
						</span>
						
						<div> Data de Nascimento </div>
						<span>
							<%=Funcoes.TelaData(usuarioCejuscDt.getUsuarioDt().getDataNascimento())%>
						</span>
						
						<div> Naturalidade </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getNaturalidade()%>
						</span>
						<div class="clearfix"></div>
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Documentação
						</legend>
						
						<div> CPF </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getCpf()%>
						</span>
						
						<div> RG </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getRg()%>
						</span>
						
						<div> Orgão Expedidor </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getRgOrgaoExpedidor()%>
						</span>
						
						<div> Data de Expedição </div>
						<span>
							<%=Funcoes.TelaData(usuarioCejuscDt.getUsuarioDt().getRgDataExpedicao())%>
						</span>
						
						<div> Título de Eleitor </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getTituloEleitor()%>
						</span>
						
						<div> Zona Eleitoral </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getTituloEleitorZona()%>
						</span>
						
						<div> Seção Eleitoral </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getTituloEleitorSecao()%>
						</span>
						
						<div> CTPS </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getCtps()%>
						</span>
						
						<div> CTPS Série </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getCtpsSerie()%>
						</span>
						
						<div> Número do PIS </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getPis()%>
						</span>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Dados Bancários
						</legend>
						
						<div> Banco </div>
						<span>
							<%
                       		for( BancoDt bancoDt : usuarioCejuscDt.getListaBancosConveniados() ) {
                       			if( usuarioCejuscDt.getCodigoBanco() != null && bancoDt.getBancoCodigo().equals(usuarioCejuscDt.getCodigoBanco()) ) {
                       		%>
								<%=bancoDt.getBanco()%>
							<%	}
							}
							%>
						</span>
						
						<div> Número da Conta </div>
						<span>
							<%=usuarioCejuscDt.getNumeroConta()%>
						</span>
						
						<div> Número Agência </div>
						<span>
							<%=usuarioCejuscDt.getNumeroAgencia()%>
						</span>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Endereço
						</legend>
						
						<div> Logradouro </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getLogradouro()%>
						</span>
						
						<div> Número </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getNumero()%>
						</span>
						
						<div> Complemento </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getComplemento()%>
						</span>
						
						<div> CEP </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getCep()%>
						</span>
						
						<div> Bairro </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getBairro()%>
						</span>
						
						<div> Cidade </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getCidade()%>
						</span>
						
						<div> UF </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getEnderecoUsuario().getUf()%>
						</span>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Contatos
						</legend>
						
						<div> Telefone Fixo </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getTelefone()%>
						</span>
						
						<div> Telefone Celular </div>
						<span>
							<%=usuarioCejuscDt.getUsuarioDt().getCelular()%>
						</span>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Arquivos dos Certificados
						</legend>
						
						<ul>
							<%if( usuarioCejuscDt.getListaUsuarioCejuscArquivoDt() != null && usuarioCejuscDt.getListaUsuarioCejuscArquivoDt().size() > 0 ) { 
								for( UsuarioCejuscArquivoDt usuarioCejuscArquivoDt: usuarioCejuscDt.getListaUsuarioCejuscArquivoDt() ) {%>
								
								<li><a href="<%=AprovarCandidatoNe.NOME_CONTROLE_WEB_XML%>?PaginaAtual=<%=Configuracao.Curinga6%>&id=<%=usuarioCejuscArquivoDt.getId()%>&hashcookie=<%=new Date().getTime()%>" target="_blank"><%=usuarioCejuscArquivoDt.getNomeArquivo()%></a></li>
								
							<%	}
							} %>
						</ul>
						
					</fieldset>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
						<legend>
							Currículo
						</legend>
						
						<div></div>
						<span>
							<%=usuarioCejuscDt.getCurriculo()%>
						</span>
						
					</fieldset>
						
				<%}	%>
				
				<%
				if( request.getAttribute("Curinga") != null && request.getAttribute("Curinga").toString().length() > 0 ) {
					String curingaOpcaoStatus = request.getAttribute("Curinga").toString();
				%>
					<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
					
					<fieldset id="VisualizaDados" class="VisualizaDados" style="background-color: silver;">
						<legend style="background-color: white;">
							Opções do Candidato
						</legend>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend style="background-color: white;">
								Status Escolhido para o Candidato(a) <%=usuarioCejuscDt.getUsuarioDt().getNome()%>
							</legend>
							
							<div> Status Escolhido </div>
							<span>
								<%
								String curinga = request.getAttribute("Curinga").toString();
								String statusEscolhido = "";
								switch(Integer.parseInt(curinga)) {
									//Aprovado
									case Configuracao.Curinga6 : {
										statusEscolhido = "Aprovado(a)";
										break;
									}
									
									//Pendente
									case Configuracao.Curinga7 : {
										statusEscolhido = "Pendente";
										break;
									}
									
									//Reprovado
									case Configuracao.Curinga8 : {
										statusEscolhido = "Reprovado(a)";
										break;
									}
								}
								%>
								<%=statusEscolhido%>
							</span>
						</fieldset>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend style="background-color: white;">
								Observação do Status
							</legend>
							
							<textarea id="editor" name="editor" style="width:100%;height:80px;"><%=usuarioCejuscDt.getObservacaoStatus()%></textarea>
							
						</fieldset>
						
						<span>
							<input id="formButton" class="botao" type="button" name="formButton" onclick="AlterarValue('PaginaAtual','<%=Configuracao.SalvarResultado%>');AlterarValue('PassoEditar','<%=request.getAttribute("Curinga")%>');submit();" value="<%=request.getAttribute("labelButton")%>" />
						</span>
					
					</fieldset>
					
				<%} %>
				
				</div>				
		  	</form>   
		</div>
		
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>