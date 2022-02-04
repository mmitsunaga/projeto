<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<jsp:useBean id="EstatisticaPublicadt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.EstatisticaPublicaDt"/>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
		
		<script type="text/javascript">

		$(document).ready(function() {
			var Totalci1=0;
			$('.ci1').each(function(index) {
				Totalci1 += parseInt($(this).html());
	 		});
			$('.ci1f').html(Totalci1);
		});
		$(document).ready(function() {
			var Totalci2=0;
			$('.ci2').each(function(index) {
				Totalci2 += parseInt($(this).html());
	 		});
			$('.ci2f').html(Totalci2);
		});
		$(document).ready(function() {
			var Totalci3=0;
			$('.ci3').each(function(index) {
				Totalci3 += parseInt($(this).html());
	 		});
			$('.ci3f').html(Totalci3);
		});
		$(document).ready(function() {
			var Totalcr1=0;
			$('.cr1').each(function(index) {
				Totalcr1 += parseInt($(this).html());
	 		});
			$('.cr1f').html(Totalcr1);
		});
		$(document).ready(function() {
			var Totalcr2=0;
			$('.cr2').each(function(index) {
				Totalcr2 += parseInt($(this).html());
	 		});
			$('.cr2f').html(Totalcr2);
		});
		$(document).ready(function() {
			var Totalcr3=0;
			$('.cr3').each(function(index) {
				Totalcr3 += parseInt($(this).html());
	 		});
			$('.cr3f').html(Totalcr3);
		});
		$(document).ready(function() {
			var Totalc1=0;
			$('.ci1f').each(function(index) {
				Totalc1 += parseInt($(this).html());
	 		});
			$('.cr1f').each(function(index) {
				Totalc1 += parseInt($(this).html());
	 		});
			$('.c1ft').html(Totalc1);
		});
		$(document).ready(function() {
			var Totalc2=0;
			$('.ci2f').each(function(index) {
				Totalc2 += parseInt($(this).html());
	 		});
			$('.cr2f').each(function(index) {
				Totalc2 += parseInt($(this).html());
	 		});
			$('.c2ft').html(Totalc2);
		});
		$(document).ready(function() {
			var Totalc3=0;
			$('.ci3f').each(function(index) {
				Totalc3 += parseInt($(this).html());
	 		});
			$('.cr3f').each(function(index) {
				Totalc3 += parseInt($(this).html());
	 		});
			$('.c3ft').html(Totalc3);
		});
		</script>
		
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
			<% if (request.getSession().getAttribute("TipoConsulta") != null && request.getSession().getAttribute("TipoConsulta").equals("Publica")) { %>
  			<style type="text/css"> #bkg_projudi{ display:none } </style>
  			<%@ include file="/CabecalhoPublico.html" %>
  			<% } %>
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="EstatisticaPublica" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PassoBusca" name="PassoBusca" type="hidden" value="<%=request.getAttribute("PassoBusca")%>"/>
			
			<!-- INÍCIO DA ESTATISTICA GERAL DE PROCESSOS DA SERVENTIA -->
			<div id="divTabela" class="divTabela"> 
				<table id="Tabela" class="Tabela" width="50%">
					<thead>
						<tr>
							<th colspan="6">ESTATISTICA GERAL DE PROCESSOS DA SERVENTIA</th>
						</tr>
						<tr>
							<th class="5%">&nbsp;</th>
							<th width="20%">&nbsp;</th>
							<th width="20%">Ativos</th>
							<th width="20%">Arquivados</th>
							<th width="30%">Recebidos no mês</th>
							<th class="5%">&nbsp;</th>
						</tr>
						</thead>
					<tbody id="tabListaComarca">
					<tr   >
						<td width="5%">&nbsp;</td>
						<td width="20%">Cível</td>
						<td class="ci1f" width="20%" align="center">&nbsp;</td> 
						<td class="ci2f" width="20%" align="center">&nbsp;</td> 
						<td class="ci3f" width="30%" align="center">&nbsp;</td> 
						<td width="5%">&nbsp;</td>
					</tr>
					<tr  >
						<td width="5%">&nbsp;</td>
						<td width="20%">Criminal</td>
						<td class="cr1f" width="20%" align="center">&nbsp;</td> 
						<td class="cr2f" width="20%" align="center">&nbsp;</td> 
						<td class="cr3f" width="30%" align="center">&nbsp;</td> 
						<td width="5%">&nbsp;</td>
					</tr>
					<tr  >
						<td width="5%">&nbsp;</td>
						<td width="20%">Total</td>
						<td class="c1ft" width="20%" align="center">&nbsp;</td> 
						<td class="c2ft" width="20%" align="center">&nbsp;</td> 
						<td class="c3ft" width="30%" align="center">&nbsp;</td> 
						<td width="5%">&nbsp;</td>
					</tr>
					</tbody>
				</table>
			</div> 
			<!-- FIM DA ESTATISTICA GERAL DE PROCESSOS DA SERVENTIA -->
			
			<!-- INÍCIO DA ESTATISTICA GERAL DE PROCESSOS POR SERVENTIA DO ESTADO -->
			<div id="divTabela" class="divTabela"> 
				<table id="Tabela" class="Tabela" width="50%">
					<thead>
						<tr>
							<th colspan="6">ESTATISTICA GERAL DE PROCESSOS POR SERVENTIA</th>
						</tr>
						<tr>
							<th class="5%">&nbsp;</th>
							<th width="20%">&nbsp;</th>
							<th width="20%">Ativos</th>
							<th width="20%">Arquivados</th>
							<th width="30%">Recebidos no mês</th>
							<th class="5%">&nbsp;</th>
						</tr>
						</thead>
					<tbody id="tabListaServentia">
					<%
	  				List liTemp = EstatisticaPublicadt.getListaProcessosServentia();
	 				HashMap objTemp = new HashMap();
	  				boolean boLinha=false; 
	  				long totalCivelAtivo = 0;
	  				long totalCivelArquivado = 0;
	  				long totalCivelRecebido = 0;
	  				long totalCriminalAtivo = 0;
	  				long totalCriminalArquivado = 0;
	  				long totalCriminalRecebido = 0;
	  				if(liTemp!=null){ 
		  				for(int i = 0 ; i< liTemp.size();i++) {
		      				objTemp = (HashMap)liTemp.get(i); 
		      				totalCivelAtivo = 0;
			  				totalCivelArquivado = 0;
			  				totalCivelRecebido = 0;
			  				totalCriminalAtivo = 0;
			  				totalCriminalArquivado = 0;
			  				totalCriminalRecebido = 0;
			  				boLinha = !boLinha;
		      				%>
		  					<tr class="TabelaLinha<%=(boLinha?1:2)%>">
								<th colspan="6"><%= objTemp.get("Serventia")%></th>
							</tr>
							<%
							boLinha = !boLinha;
							%>
							<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								<td width="5%">&nbsp;</td>
								<td width="20%">Cível</td>
								<td class="ci1" width="20%" align="center"><%= (Long)objTemp.get("CivelAtivo")%></td> 
								<%totalCivelAtivo = totalCivelAtivo + (Long)objTemp.get("CivelAtivo");%>
								<td class="ci2" width="20%" align="center"><%= (Long)objTemp.get("CivelArquivado")%></td> 
								<%totalCivelArquivado = totalCivelArquivado + (Long)objTemp.get("CivelArquivado");%>
								<td class="ci3" width="30%" align="center"><%= (Long)objTemp.get("CivelRecebido")%></td> 
								<%totalCivelRecebido = totalCivelRecebido + (Long)objTemp.get("CivelRecebido");%>
								<td width="5%">&nbsp;</td>
							</tr>
							<%
							boLinha = !boLinha;
							%>
							<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								<td width="5%">&nbsp;</td>
								<td width="20%">Criminal</td>
								<td class="cr1" width="20%" align="center"><%= (Long)objTemp.get("CriminalAtivo")%></td> 
								<%totalCriminalAtivo = totalCriminalAtivo + (Long)objTemp.get("CriminalAtivo");%>
								<td class="cr2" width="20%" align="center"><%= (Long)objTemp.get("CriminalArquivado")%></td> 
								<%totalCriminalArquivado = totalCriminalArquivado + (Long)objTemp.get("CriminalArquivado");%>
								<td class="cr3" width="30%" align="center"><%= (Long)objTemp.get("CriminalRecebido")%></td> 
								<%totalCriminalRecebido = totalCriminalRecebido + (Long)objTemp.get("CriminalRecebido");%>
								<td width="5%">&nbsp;</td>
							</tr>
							<%
							boLinha = !boLinha;
							%>
							<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								<td width="5%">&nbsp;</td>
								<td width="20%">Total</td>
								<td width="20%" align="center"><%=totalCivelAtivo + totalCriminalAtivo %></td> 
								<td width="20%" align="center"><%=totalCivelArquivado + totalCriminalArquivado%></td> 
								<td width="30%" align="center"><%=totalCivelRecebido + totalCriminalRecebido%></td> 
								<td width="5%">&nbsp;</td>
							</tr> 
						<%}
					}%>
					</tbody>
				</table>
			</div> 
			<!-- FIM DA ESTATISTICA GERAL DE PROCESSOS POR SERVENTIA DO ESTADO -->
			
			<br/><br/>
			
			<br/><br/>
			
			</form>
		</div>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>