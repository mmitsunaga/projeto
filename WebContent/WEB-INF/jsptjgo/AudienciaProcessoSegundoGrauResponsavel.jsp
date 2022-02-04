<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="java.util.List"%>

<jsp:useBean id="AudienciaSegundoGrauDt" scope="request" class= "br.gov.go.tj.projudi.dt.AudienciaDt"/>

<%@page import="br.gov.go.tj.projudi.dt.AudienciaProcessoResponsavelDt"%>
<html>
	<head>
		<title>Responsáveis pela Audiência</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
	</head>

	<body>
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; Responsáveis pela Audiência</h2></div>
				<fieldset id="VisualizaDados" class="VisualizaDados">
					<legend>Responsáveis </legend>
					
					<br />
					<div> Sessão </div>
					<span> <%=AudienciaSegundoGrauDt.getDataAgendada()%> </span>
					
					<div> Processo </div>
			   		<span><a href="BuscaProcesso?Id_Processo=<%=AudienciaSegundoGrauDt.getAudienciaProcessoDt().getProcessoDt().getId()%>"><%=AudienciaSegundoGrauDt.getAudienciaProcessoDt().getProcessoDt().getProcessoNumero()%></a></span/>
			   		<br />
					
			     	<table id="Tabela" class="Tabela">
			        	<thead>
			            	<tr class="TituloColuna">
			                	<td>Cargo</td>
			                	<td>Nome Usuário</td>
			             	 </tr>
			           	</thead>
			          	<tbody id="tabResponsaveis">
						<%
						List listaResponsaveis = AudienciaSegundoGrauDt.getAudienciaProcessoDt().getListaResponsaveis();	
						boolean boLinha = false;
						if (listaResponsaveis != null && listaResponsaveis.size() > 0){
							for(int i = 0 ; i< listaResponsaveis.size();i++) {
							AudienciaProcessoResponsavelDt objTemp = (AudienciaProcessoResponsavelDt)listaResponsaveis.get(i);
						%>
					  		<tr class="<%=(boLinha?"Linha1":"Linha2")%>"> 
			       		        <td><%=objTemp.getCargoTipo()%></td>
				            	<td> <%=objTemp.getNome()%> </td>
				       		</tr>
						<%
							boLinha = !boLinha;
							}
						} else { %>
						<tr><td colspan="2"><em><b>Nenhum Responsável por essa Audiência.</b></em></td></tr>
						<% } %>
						</tbody>
					</table>
				</fieldset>
			</div>
		</div>
	</body>
</html>