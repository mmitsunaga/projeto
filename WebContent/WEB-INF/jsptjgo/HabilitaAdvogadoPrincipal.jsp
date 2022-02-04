<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de Advogados  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="./js/jquery.js"> </script>
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'></script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
</head>

<body>
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; |<%=request.getAttribute("tempPrograma")%>| Busca de Advogados </h2></div>
		
		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

			<input type="hidden" id="posicaoLista" name="posicaoLista" value="<%=request.getAttribute("posicaoLista")%>">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
	
			<fieldset id="VisualizaDados" class="VisualizaDados">
		      	<legend>Advogados Habilitados</legend>
		      	
		    	<div> N&uacute;mero Processo</div>
				<span><a href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/><br />
		  	
				<%
					List listaAdvogados = processoDt.getListaAdvogados();		
					ProcessoParteAdvogadoDt objTemp;
					if (listaAdvogados != null && listaAdvogados.size() > 0){
						objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(0);
				%>
				
		     	<table id="Tabela" class="Tabela">
		        	<thead>
		            	<tr class="TituloColuna">
		                	<td width="12%">Tipo</td>
		                	<td>OAB</td>
		                  	<td>Advogado</td>
		                  	<td>Parte</td>
		             	 </tr>
		           	</thead>
		          	<tbody id="tabListaAdvogadoParte">
					<%
						boolean boTeste=false;
						for(int i = 0 ; i< listaAdvogados.size();i++) {
					   		objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(i);
					%>
				  		<tr class="<%=(boTeste?"Linha1":"Linha2")%>"> 
			                <td>
			                	<% if (objTemp.getPrincipal().equalsIgnoreCase("true")){%>
			                	<b>Adv. Principal</b>
			                	<% } else { %>
			                	<input name="imgEditar" type="image" src="./imagens/22x22/btn_salvar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.LocalizarAutoPai%>');AlterarValue('posicaoLista','<%=i%>')" title="Definir Advogado Principal">
			                	<% } %>
			                </td>
		       		        <td><%=objTemp.getOabNumero()+ " " + objTemp.getOabComplemento()%></td>
			                <td width="35%"><%= objTemp.getNomeAdvogado() %> </td>
			            	<td width="35%"><%= objTemp.getNomeParte()%></td>
			       		</tr>
					<%
							boTeste = !boTeste;
						}
					} else { %><br /> Nenhum Advogado pode ser habilitado como Principal nesse processo. <br />
					<% } %>
				   	</tbody>
			  	</table>
			</fieldset>
	
			<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.LocalizarAutoPai))) {%> 	
		    	<div id="divSalvar" class="divSalvar" class="divsalvar">
		        	<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')"  /><br />
		        		<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
		      	</div>
		 	<%}%>
		 	
		</form> 
	</div> 
<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>