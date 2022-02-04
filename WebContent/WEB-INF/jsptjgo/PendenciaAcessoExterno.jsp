<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class= "br.gov.go.tj.projudi.ne.UsuarioNe"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
 "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
	<title>Pend&ecirc;ncia</title>

	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
	
	<link rel="stylesheet" href="./css/jquery.tabs.css" type="text/css" media="print, projection, screen" />
	
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');

	</style>
			
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>      	
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/checks.js'></script> 

	<%@ include file="js/PendenciaPegar.js"%>
	
	<script type="text/javascript">
	function anexarArquivoNaoAssinado(){
		AlterarValue("arquivo",getTextoEditor("FCKeditor"));
		
		var testeNome = $("#nomeArquivo").val();
		if (testeNome=="")	AlterarValue("nomeArquivo","online.html");
		else  AlterarValue("nomeArquivo", testeNome+".html");		

		AlterarValue("assinado","false");
		inserirArquivo("false");
	}
	</script>

	<%if (request.getAttribute("habilitarDataLeitura") != null){ %>
		<link href="./js/jscalendar/dhtmlgoodies_calendar.css" type="text/css" rel="stylesheet" media="screen" />			
        <script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.maskedinput.js'></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>	
		<script type="text/javascript">
			$(document).ready(function(){
				$("#dataLeitura").mask("99/99/9999 99:99");
				$("#dataLeitura").focus();
				
				function verificarNaoLido(){
					if ($("#statusNaoLido").get(0).checked){
						$("#dataLeitura").get(0).disabled = true;
						$("#dataLeitura").val("");
					}
				}
				
				$("#statusNaoLido").change(function(){
					verificarNaoLido();
				});
				
				$("#statusLido").change(function(){
					if ($("#statusLido").get(0).checked){
						$("#dataLeitura").get(0).disabled = false;
					}
				});
				
			});
		</script>
	<%}%>
</head>
<body onLoad="atualizarArquivos();">
<div class="divCorpo">
	
	<%@ include file="Padroes/Mensagens.jspf"%>

	<form action="Pendencia" method="post" id="Formulario">
		<div class="area"><h2>&raquo; Resolver Pend&ecirc;ncia</h2></div>
		<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
		<input id="ultimaOperacao" name="ultimaOperacaoPendencia" type="hidden" value="<%=request.getAttribute("ultimaOperacaoPendencia")%>" />
		<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
		
		<%if (Pendenciadt!= null  && Pendenciadt.getId() != null && !Pendenciadt.equals("")){ %>
		
			<div id="divEditar" class="divEditar">
				<%if (Pendenciadt.isPodeLiberar()){%>
					<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=Pendenciadt.getId()%>&amp;fluxo=2">
						<!--  <img src="imagens/22x22/ico_liberar.png" alt="Liberar" title="Liberar a pend&ecirc;ncia" /> -->
						Liberar esta pend&ecirc;ncia
					</a>
				<%}%>
				
			<br />
			
			<div id="abas">		
				<ul>
					<li><a href="#dados"><span>Dados da Pend&ecirc;ncia</span></a></li>
					<li><a href="#insercao"><span>Resolver Pend&ecirc;ncia</span></a></li>					
					<%if (request.getAttribute("habilitarEncaminhar") != null && request.getAttribute("habilitarEncaminhar").toString().equals("true")){ %>
					<li><a href="#encaminhar"><span>Encaminhar</span></a></li>
					<%} %>
					<%if (request.getAttribute("habilitarTrocarTipoPendencia") != null && request.getAttribute("habilitarTrocarTipoPendencia").toString().equals("true")){ %>
						<li><a href="#TrocarTipo"><span>Trocar Tipo Pendência</span></a></li>
					<%}%>		
				</ul>
	
				<div id="dados">
					<label class="formEdicaoLabel">N&uacute;mero:</label>
					<%if (request.getAttribute("habilitarDetalhes") != null){%>
						<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=Pendenciadt.getId()%>&amp;CodigoPendencia=<%=Pendenciadt.getHash()%>&amp;fluxo=3"
							title="Ver detalhes da pend&ecirc;ncia n&uacute;mero <%=Pendenciadt.getId()%>">
							<b><%=Pendenciadt.getId()%></b><br />
						</a>
					<%} else { %>
						<b><%=Pendenciadt.getId()%></b><br />
					<% } %>
					
					<%if (Pendenciadt.getId_Processo() != null && !Pendenciadt.getId_Processo().equals("")){%>
						<label class="formEdicaoLabel">Processo:</label>
							<a href="Pendencia?PaginaAtual=<%=Configuracao.LocalizarDWR%>&amp;Id_Processo=<%=Pendenciadt.getId_Processo()%>"> 
								<%=Pendenciadt.getProcessoNumero()%>
							</a>
						<br />
						<label class="formEdicaoLabel">Movimenta&ccedil;&atilde;o:</label><%=Pendenciadt.getMovimentacao()%><br />
						<%if (Pendenciadt.getNomeParte() != null && !Pendenciadt.getNomeParte().equals("")){  %>
							<label class="formEdicaoLabel">Parte:</label><%=Pendenciadt.getNomeParte()%><br />
						<%} %>
					<%}%>
					
					<label class="formEdicaoLabel">Data Inicio:</label> <%=Pendenciadt.getDataInicio()%><br />
					<label class="formEdicaoLabel">Serventia Cadastrador:</label> <%=Pendenciadt.getServentiaUsuarioCadastrador()%><br />
					<label class="formEdicaoLabel">Cadastrador:</label><%=Pendenciadt.getNomeUsuarioCadastrador()%><br />
					<br />
					<%@ include file="TabelaArquivosPendencia.jspf" %>	
					<br />
				</div>
				
				<div id="insercao">
					<%if (request.getAttribute("habilitarResponder") != null && request.getAttribute("habilitarResponder").toString().equals("true")){ %>
							<%@ include file="Padroes/InsercaoArquivosPendenciaAssinador.jspf" %>
					<%} else { %>
							<%@ include file="Padroes/InsercaoArquivosSemAssinador.jspf"%>
					<% } %>
					
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
						<%if (request.getAttribute("PaginaAtual") != null && !request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
							<%if (request.getAttribute("habilitarResponder") != null){ %>
								<button type="submit" name="operacao" value="Responder" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')">
								<!--	<img src="imagens/22x22/ico_sucesso.png" alt="Responder Pendência" /> -->
									Responder
								</button>
							<%}%>
							<button type="submit" name="operacao" value="Anexar">
								<!--   <img src="imagens/22x22/btn_anexo.png" alt="Anexar Documento" /> -->
								Registrar
							</button>
						<%}%>
					</div>
				</div>
			
			   <%if (request.getAttribute("habilitarEncaminhar") != null && request.getAttribute("habilitarEncaminhar").toString().equals("true")){ %>
					<div id="encaminhar">				
						<%@ include file="PendenciaEncaminhar.jspf" %>
						<br />
						<div id="divConfirmarSalvar" class="ConfirmarSalvar">
							<%if (request.getAttribute("PaginaAtual") != null && !request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
								<button type="submit" name="operacao" value="Encaminhar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')">
									<!-- <img src="imagens/22x22/btn_encaminhar.png" alt="Encaminhar" title="Encaminhar a pend&ecirc;ncia" /> -->
										Encaminhar
								</button>
							<%}%>
						</div>
					</div>
				<%}%>
				<%if (request.getAttribute("habilitarTrocarTipoPendencia") != null && request.getAttribute("habilitarTrocarTipoPendencia").toString().equals("true")){ %>
					<div id="TrocarTipo">
						<label class="formEdicaoLabel">N&uacute;mero:</label>
						<%if (request.getAttribute("habilitarDetalhes") != null){%>
							<a href="Pendencia?PaginaAtual=7&amp;pendencia=<%=Pendenciadt.getId()%>&amp;CodigoPendencia=<%=Pendenciadt.getHash()%>&amp;fluxo=3"
								title="Ver detalhes da pend&ecirc;ncia n&uacute;mero <%=Pendenciadt.getId()%>">
								<b><%=Pendenciadt.getId()%></b><br />
							</a>
						<%} else { %>
							<b><%=Pendenciadt.getId()%></b><br />
						<% } %>
						
						<%if (Pendenciadt.getId_Processo() != null && !Pendenciadt.getId_Processo().equals("")){%>
							<label class="formEdicaoLabel">Processo:</label>
								<a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>"> 
									<%=Pendenciadt.getProcessoNumero()%>
								</a>
							<br />
							<%if (Pendenciadt.getNomeParte() != null && !Pendenciadt.getNomeParte().equals("")) {%>
								<label class="formEdicaoLabel">Parte:</label><%=Pendenciadt.getNomeParte()%><br />
							<%} %>
						<%}%>
						
						<label class="formEdicaoLabel">Tipo de Pêndencia:</label> <%=Pendenciadt.getPendenciaTipo()%><br />
						
						<%if (Pendenciadt.getPrazo() != null && !Pendenciadt.getPrazo().equals("")) {%>
							<label class="formEdicaoLabel">Prazo:</label> <%=Pendenciadt.getPrazo()+ "  Dia(s)"%><br />
						<%} %>
						<br />
						<fieldset>
							<legend>Selecionar Novo Tipo de Pendência</legend>
							<div class="col45">
							     <%String codPendenciaTipo = String.valueOf(request.getAttribute("codTipoPendencia")); %>
								<label class="formEdicaoLabel" for="status">*Pendência Tipo</label> <br/>
								<select name="codTipoPendencia" id="codTipoPendencia" size="1">
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase("-1")){%> selected <%}%> value="-1">--Selecione o Tipo de Pendência-- </option> 
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONTADORIA_CALCULO_CONTA))){%> selected <%}%>value="<%=PendenciaTipoDt.CONTADORIA_CALCULO_CONTA%>">Pedido Contadoria - Atualização de Conta</option>
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS))){%> selected <%}%>value="<%=PendenciaTipoDt.CONTADORIA_CALCULO_CUSTAS%>">Pedido Contadoria - Cálculo de Custas</option>
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO))){%> selected <%}%>value="<%=PendenciaTipoDt.CONTADORIA_CALCULO_LIQUIDACAO%>">Pedido Contadoria - Cálculo de Liquidaçao (CPC)</option>
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS))){%> selected <%}%>value="<%=PendenciaTipoDt.CONTADORIA_CALCULO_TRIBUTOS%>">Pedido Contadoria - Cálculo de Tributos</option>
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO))){%> selected <%}%>value="<%=PendenciaTipoDt.CONTADORIA_JUNTADA_DOCUMENTO%>">Pedido Contadoria - Juntada de documento</option>
									<option <%if(codPendenciaTipo != null && codPendenciaTipo.trim().equalsIgnoreCase(String.valueOf(PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL))){%> selected <%}%>value="<%=PendenciaTipoDt.PEDIDO_CONTADORIA_CRIMINAL%>">Pedido Contadoria - Criminal</option>
								</select>
							</div>
						</fieldset>
						<div id="divConfirmarSalvar" class="ConfirmarSalvar">
							<%if (request.getAttribute("PaginaAtual") != null && !request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
								<button type="submit" name="operacao" value="Efetuar Troca" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>')">
									Efetuar Troca
								</button>
							<%}%>
						</div>
					</div>
				<%} %>	
			</div>
		<%} else {%>
			<h2>Pend&ecirc;ncia n&atilde;o encontrada</h2>
		<%}%>
					
			<script type="text/javascript">
				iniciar();
				$(document).ready(function(){
					 $( "#abas" ).tabs({ active: <%= request.getSession().getAttribute("ultimaAba")%> });
				});
			</script>
			<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))) {%> 	
		    	<div id="divConfirmarSalvar" class="ConfirmarSalvar">
		        	<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
		        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		           	<%  }%> 
					<%if (request.getAttribute("PaginaAtual") != null && request.getAttribute("PaginaAtual").equals(Configuracao.SalvarResultado)){ %>
						<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')" >
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
							Confirmar
						</button>
						<br />
					<%}%>
				</div>
			 <%}%>
		</div>
	</form> 	
</div>
</body>
</html>