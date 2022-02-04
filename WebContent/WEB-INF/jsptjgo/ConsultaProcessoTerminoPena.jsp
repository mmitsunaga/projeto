<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>

<jsp:useBean id="RelatorioExecpenwebDt" scope="request" class="br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt"/>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioExecpenwebDt"%>

<html>
	<head>
		<title>Processo Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
		<script type='text/javascript' src='./js/jquery.mask.min.js'></script>
		<script type="text/javascript">
			function buscarProcesso(id_processo) {
				//apresentar os dados do Processo
				AlterarValue('PaginaAtual', '-1');
				AlterarAction('Formulario','BuscaProcesso');
				obj = document.getElementById("Id_Processo");
				obj.value = id_processo;
				document.Formulario.submit();
			}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Processo de Execução Penal - <%=request.getAttribute("TituloPagina")%></h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" /> 
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>"/>
				<input id="PassoImprimir" name="PassoImprimir" type="hidden" value="<%=request.getAttribute("PassoImprimir")%>"/>
				<input id="Id_Processo" name="Id_Processo" type="hidden" value=""/>
				<br />
				<div id="divEditar" class="divEditar">
					<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgCancelar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('PassoEditar','-1');AlterarValue('PassoImprimir','-1');"/>
						<input name="btnImprimir" type="submit" value="Imprimir Resultado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('PassoImprimir','5');"/>
					</div>
					<br />
				<%
				List listaSentenciado = (List) request.getAttribute("listaSentenciado");
				if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){
				%>
						<div id="divTabela" class="divTabela">
						<table id="ListaReeducandos" class="Tabela" width="100%">
		        			<thead>
		            			<tr class="TituloColuna">
									<th></th>
		            				<th align="center">Nº do Processo</th>
		                			<th align="left">Sentenciado</th>
		                			<th align="left">Regime</th>
		                			<th align="center">Término da Pena</th>
		                			<th align="center">Data do Cálculo</th>
									<% if (request.getAttribute("beneficio") != null){%>
		                			<th align="center"><%=request.getAttribute("beneficio")%></th>
									<%} %>
		               			</tr>
		           			</thead>
		    				<tbody id="tabListaProcesso">
				    		<%
				    		 	boolean boLinha=false; 
				    			if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){
				   	    			for (int i=0; i<listaSentenciado.size(); i++){
				   	    				RelatorioExecpenwebDt sentenciado = new RelatorioExecpenwebDt();
				   	    				sentenciado = (RelatorioExecpenwebDt)listaSentenciado.get(i);
							%>			
									<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>" onclick="javascript: buscarProcesso(<%=sentenciado.getIdProcesso()%>);" title="Consultar Processo">
										<td><%=i+1%></td>
										<td align="center"><%=sentenciado.getProcessoNumeroCompleto()%></td>										
										<td align="left"><%=sentenciado.getSentenciado()%></td>
										<td align="left"><%=sentenciado.getRegime()%></td>
										<td align="center"><%=sentenciado.getDataTerminoPena()%></td>
										<td align="center"><%=sentenciado.getDataCalculo()%></td>
									<% if (request.getAttribute("beneficio") != null){%>
										<td align="center"><%=sentenciado.getDataProgressao()%></td>
									<%} %>
			                   		</tr>
		                   	<%		boLinha = !boLinha;}
				   	    		}%>
		                </table>
		             </div>   
					</div>
				<%
					}else {
				%>			
					<div style="width:260px"></div><span class="span1">Não foi encontrado sentenciado.</span><br />
				<%}%>
				<br />
			</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>