<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AssuntoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RecursoParteDt"%>


<jsp:useBean id="Recursodt" scope="session" class= "br.gov.go.tj.projudi.dt.RecursoDt"/>

<html>
		 <head>
			<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
			<title> |<%=request.getAttribute("tempPrograma")%>| Conversão de Recurso em Processo </title>
			<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	
			<script type='text/javascript' src='./js/jquery.js'> </script>
			<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
			<script type='text/javascript' src='./js/Funcoes.js'></script>
		</head> 

<body>	
	<div id="divCorpo" class="divCorpo" >
		
	 		<div class="area"><h2>&raquo; Confirmar Conversão de Recurso em Processo</h2></div>
			
			<form action="Recurso" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				
	 			<div id="divEditar" class="divEditar"> 
	 				<fieldset id="VisualizaDados" class="VisualizaDados"> 
	 					<legend> Dados do Recurso </legend> 
						<br />
						<dl>
						<div>Número Processo</div> 
						<span><a href="BuscaProcesso?Id_Processo=<%=Recursodt.getId_Processo()%>"><%=Recursodt.getProcessoDt().getProcessoNumero()%></a></span>
						
						<div>Data de Envio</div> 
						<span><%=Recursodt.getDataEnvio()%></span>
						<br />
						
						<div>Serventia Origem</div> 
						<span><%=Recursodt.getServentiaOrigem()%></span>
						
						<div>Serventia Recurso</div>
						<span><%=Recursodt.getServentiaRecurso()%></span>
						<br />
						
						<div>Classe</div>
						<span><%=Recursodt.getProcessoTipo()%></span>
						</dl>
	 					<fieldset id="VisualizaDados" class="VisualizaDados"> 
	 						<legend> *Parte(s) </legend> 
							<%
							List listaPartes = Recursodt.getProcessoDt().getPartesProcesso();
	 	    				for (int i=0;i < listaPartes.size();i++){
		  			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPartes.get(i);
		  			  			if (parteDt.getDataBaixa().length() == 0){
		 					%>
				       			<span><%=parteDt.getNome()%> </span>
				       			<div> CPF </div>
				        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<%	}
	 	    				}
							%>						
	 					</fieldset> 
						
	 				</fieldset> 
						
						
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgInserir" type="submit" value="Confirmar Conversão Recurso em Processo" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('PassoEditar',1);"> 	 
											
					</div>
												
	
	 			</div> 
	
				</form>
			</div>
			<%@ include file="Padroes/Mensagens.jspf" %>

	</body>
</html>
