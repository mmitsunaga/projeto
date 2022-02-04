<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoPrioridadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>

<jsp:useBean id="processoDt" scope="session" class="br.gov.go.tj.projudi.dt.ProcessoDt"/>
<jsp:useBean id="UsuarioSessao" scope="session" class="br.gov.go.tj.projudi.ne.UsuarioNe"/>

<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<html>
	<head>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<title> Reserva ou Exclusão de Agendas para Audiências </title>
	
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
		<script type="text/javascript" src="./js/jquery.js"> </script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		
		<script language="javascript" type="text/javascript">	
			$(document).ready(					
				function() {
					//pego todos objetos que foram marcados com a class nomes
					//e verifico se tem número no nome
					 $(".nomes").each(function( index ) {
					 	var texto =  $( this ).text();
						for(var numero=0; numero<=9; numero++){
							texto= texto.replace(numero,'<p class="destacarNumero" tag="Foi utilizado número no Nome, favor conferir com os dados da petição" title="Foi utilizado número no Nome, favor conferir com os dados da petição">'+ numero +'</p>');
						}
		
						$( this ).html(texto);			
					});		
				}
			); 	
		</script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo;  <%=request.getAttribute("tipoAcao")%> Processo de Cálculo </h2></div>
			<div id="divLocalizar" class="divLocalizar">
				<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
					<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
					<input id="opc" name="opc" type="hidden" value="<%=request.getAttribute("opc")%>" />
					<input id="tipoAcao" name="tipoAcao" type="hidden" value="<%=request.getAttribute("tipoAcao")%>" />
						<br />
						<br />
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Dados do Processo </legend>
							
							<div> N&uacute;mero</div>
							<span> <%=processoDt.getProcessoNumeroCompleto()%></span>
							
							<% if (!processoDt.getTcoNumero().equals("")){ %>
							<br /><div> Protocolo SSP </div> <span><%=processoDt.getTcoNumero()%></span>
							<%} %>
						
							<%@ include file="BuscaPartesProcesso.jspf"%>
						     	
					    	<fieldset id="VisualizaDados" class="VisualizaDados">
					    		<legend> Outras Informações </legend>
					    		
						   		<div> Serventia </div>
								<span class="span1"><%= processoDt.getServentia()%></span><br />
								
						   		<div> Classe </div>
					    	  	<span style="width: 500px;"><%=processoDt.getProcessoTipo()%> </span><br />
					    	  	
					    	  	<%@ include file="Padroes\BuscaAssuntosProcesso.jspf"%>
					    	  	
					    	  	<% if (!processoDt.getValor().equals("Null")){ %>
								<div> Valor da Causa</div>
								<span class="span1"><%=processoDt.getValor()%></span>
								<%} %>
								
					    	  	<div> Processo Originário </div>
								<span class="span2"><a href="BuscaProcesso?ProcessoOutraServentia=true&Id_Processo=<%=processoDt.getId_ProcessoPrincipal()%>"><%=processoDt.getProcessoNumeroPrincipal()%></a></span/><br />	
								
								<div> Fase Processual</div>	
								<span class="span1"><%=processoDt.getProcessoFase()%></span>
											
								<div> Apenso(s)</div>
								<span class="span2">
									<% if (processoDt.temApensos()){%>
									<a href="ProcessoApenso?PaginaAtual=<%=Configuracao.Localizar%>">Visualizar</a>
									<% } %>
								</span><br />
								
								<div> Classificador </div>
							    <span class="span1">
							    <% if (UsuarioSessao.isPodeVisualizarClassificador()){ %>
							   		<%= processoDt.getClassificador()%>
							   	<% } %>
							    </span>
							    
							    <div> Data Distribui&ccedil;&atilde;o</div>
								<span class="span2"><%=processoDt.getDataRecebimento()%></span><br />		
							    
								<div> Segredo de Justi&ccedil;a</div>
								<span class="span1"><%=(processoDt.getSegredoJustica().equalsIgnoreCase("false")?"NÃO":"SIM")%></span>
								
								<div> Prioridade</div>
								<span class="span2"><%=processoDt.getProcessoPrioridade()%></span/>
								<%if(processoDt.isPrioridade() && UsuarioSessao.getVerificaPermissao(ProcessoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Curinga8)) {%>
									<a href="Processo?PaginaAtual=<%=Configuracao.Curinga8%>" border="0">
										<img src='imagens/16x16/edit-clear.png' alt="Retirar Prioridade" title="Retirar Prioridade" border="0">
									</a>
								<%} %>
								<br />
								
								<div> Status </div>
								<span class="span1"><%=processoDt.getProcessoStatus()%></span/>
							</fieldset>
				   			<br />
				   			<br />
					    	<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Justificativa </legend>
								<label class="formEdicaoLabel" for="Obs">Informe o motivo do arquivamento:</label><br><br/>
								<input id="radioArquivar" name="radioArquivar" type="radio" value="Cálculo_duplicado"/>Cálculo duplicado<br />
								<input id="radioArquivar" name="radioArquivar" type="radio" value="Transferência_de_Comarca"/>Transferência de Comarca<br />
								<input id="radioArquivar" name="radioArquivar" type="radio" value="Transferência_de_Estado"/>Transferência de Estado<br />
								<input id="radioArquivar" name="radioArquivar" type="radio" value="Morte_do_Agente"/>Morte do Agente<br />
								<input id="radioArquivar" name="radioArquivar" type="radio" value="Extinção"/>Extinção<br />
								<input id="radioArquivar" name="radioArquivar" type="radio" value="Outro"/>Outro<br />
								<label class="formEdicaoLabel" for="Obs">Observação</label><br>    
					    		<input class="formEdicaoInput" name="Obs" id="Obs" type="text" size="20" maxlength="20"/>
								<br />
							</fieldset>
						</fieldset>
				   		<br />
			   			<br />
						<div id="divSalvar" class="divSalvar" class="divsalvar" align="center">
			    			<br />
		        			<button type="submit" name="operacao" value="Confirmar" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga9)%>')" >
								<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Confirmar" /> -->
								Confirmar
							</button>
						</div>
				
				</form>
<%@ include file="Padroes/Mensagens.jspf" %>
			</div>  
		</div>
	</body>
</html>