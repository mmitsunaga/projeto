<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoTipoDt"%>

<jsp:useBean id="Recursodt" scope="session" class= "br.gov.go.tj.projudi.dt.RecursoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Recurso  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>	  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>	
	<script type='text/javascript'>
		var semParte = 0;
	  	function esconderDados(){
	  		$('#VisualizaDados1').toggle();
	  		$('#VisualizaDados2').toggle();	  			  			
	  	}
	  	$(document).ready(function() {			
	  		var componente = document.getElementById('semPartes');	  			  		
			if (componente != null && componente.checked) esconderDados();						
		});		
	  	
	</script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Definir Pólos do Recurso</h2></div>
		<form action="Recurso" method="post" name="Formulario" id="Formulario">
		
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />			
			
			<div id="divEditar" class="divEditar">
				<fieldset id="VisualizaDados3" class="VisualizaDados">
					<legend> Dados do Recurso </legend>
					<br />
					<div>Número Processo</div> 
					<span><a href="BuscaProcesso?Id_Processo=<%=Recursodt.getId_Processo()%>"><%=Recursodt.getProcessoDt().getProcessoNumero()%></a></span>
					
					<div>Data de Envio</div> 
					<span><%=Recursodt.getDataEnvio()%></span>
					<br />
					
					<div>Serventia Origem</div> 
					<span><%=Recursodt.getServentiaOrigem()%></span>
					
					<div>Serventia Recurso</div>
					<span><%=Recursodt.getServentiaRecurso()%></span>					
				</fieldset>
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Definir Pólos do Recurso</legend>
					
					<label class="formEdicaoLabel" for="Id_ProcessoTipo">* Classe
				    <input class="FormEdicaoimgLocalizar" id="imaLocalizarProcessoTipo" name="imaLocalizarProcessoTipo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ProcessoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
				    </label><br>  
				    <input type="hidden" name="Id_ProcessoTipo" id="Id_ProcessoTipo" value="<%=Recursodt.getId_ProcessoTipo()%>">
				    <input type="hidden" name="ProcessoTipoCodigo" id="ProcessoTipoCodigo" value="<%=Recursodt.getProcessoTipoCodigo()%>">
					<input class="formEdicaoInputSomenteLeitura"  readonly name="ProcessoTipo" id="ProcessoTipo" type="text" size="67" maxlength="100" value="<%=Recursodt.getProcessoTipo()%>"/><br />
					<br>
					
					<input name="semParte" id="semPartes" type="checkbox" <%=request.getAttribute("semParte")%> value="1" onclick="esconderDados();" /> Processo Sem Recorrente(s) e Recorrido(s)
					<%
					List listaPartes = Recursodt.getProcessoDt().getPartesProcesso();
					if (listaPartes!=null && listaPartes.size()>=1) {
					%>
						<fieldset id="VisualizaDados1" class="VisualizaDados">
							<legend> *Recorrente(s) </legend>
							<%						
	 	    				for (int i=0;i < listaPartes.size();i++){
		  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
		  			  			if (parteDt.getDataBaixa().length() == 0){
		 					%>
				   				<div>
					   				<input name="Recorrente" id="Recorrente" type="checkbox" value="<%=parteDt.getId_ProcessoParte()%>" 
					   				<%	
					   					List listaRecorrentes = Recursodt.getListaRecorrentes();
										if (listaRecorrentes != null && listaRecorrentes.size()>0){
							            	for(int j = 0 ; j< (listaRecorrentes.size());j++) {
							              		RecursoParteDt obj = (RecursoParteDt) listaRecorrentes.get(j);
							                   	if (obj.getId_ProcessoParte().equals(parteDt.getId_ProcessoParte())){%> 
							                   	checked="checked"
							        <% 			}
							               	}
										}
									%>
									/>
				   				</div> 
				       			<span><%=parteDt.getNome()%> </span>
				       			<div> CPF </div>
				        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<%	}
	 	    				}
							%>
						</fieldset>					
										
						<fieldset id="VisualizaDados2" class="VisualizaDados">
							<legend> *Recorridos(s) </legend>
							<%
	 	    				for (int i=0;i < listaPartes.size();i++){
		  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
		  			  			if (parteDt.getDataBaixa().length() == 0){
		 					%>
				   				<div>
					   				<input name="Recorrido" id="Recorrido" type="checkbox" value="<%=parteDt.getId_ProcessoParte()%>" 
					   				<%	
					   					List listaRecorridos = Recursodt.getListaRecorridos();
										if (listaRecorridos != null && listaRecorridos.size()>0){
							            	for(int j = 0 ; j< (listaRecorridos.size());j++) {
							            		RecursoParteDt obj = (RecursoParteDt) listaRecorridos.get(j);
							                   	if (obj.getId_ProcessoParte().equals(parteDt.getId_ProcessoParte())){%> 
							                   	checked="checked"
							        <% 			}
							               	}
										}
									%>
									/>
				   				</div> 
				       			<span><%=parteDt.getNome()%> </span>
				       			<div> CPF </div>
				        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<%	}
	 	    				}
							%>
						</fieldset>
					<%}%>
					<br />
					<br />
		
				    <%@ include file="AssuntosRecurso.jspf"%> 
				   	<br />
					<br />
					
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgInserir" type="submit" value="Definir Pólos" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						<input name="imgInserir" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
						<input name="imgInserir" type="submit" value="Converter Recurso em Processo" title="O Processo não é Recurso, foi enviado como Recurso equivocadamente" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');">
					</div>
				</fieldset>

			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>

			</div>

		</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>	
</body>
</html>
