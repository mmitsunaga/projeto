<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>

<jsp:useBean id="DataProvavelDt" scope="request" class="br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt"/>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt"%>

<html>
	<head>
		<title>Processo Execu��o Penal</title>
	
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
			AlterarValue('PassoEditar', '-1');
			AlterarAction('Formulario','BuscaProcesso');
			document.getElementById("Id_Processo").value = id_processo;
			var form = document.getElementById("Formulario");
			form.submit();
		}
		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo"> 
			<div class="area"><h2>&raquo; Processo de Execu��o Penal - <%=request.getAttribute("TituloPagina")%></h2></div>
			
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
				<input id="PassoImprimir" name="PassoImprimir" type="hidden" value="<%=request.getAttribute("PassoImprimir")%>"/>
				<input id="Id_Processo" name="Id_Processo" type="hidden" value=""/>
				<br />
				<div id="divEditar" class="divEditar">
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
						<input name="imgCancelar" type="submit" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>'); AlterarValue('PassoEditar','-1');AlterarValue('PassoImprimir','-1');"/>
						<input name="imgImprimir" type="submit" value="Imprimir Resultado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>'); AlterarValue('PassoImprimir','3');">
					</div><br />
				<%
				List listaSentenciado = (List) request.getAttribute("listaSentenciado");
				//Tela inicial de consulta de Processo com C�lculo
				String passoEditar = request.getAttribute("PassoEditar").toString();
				if (!passoEditar.equals("7") && !passoEditar.equals("8")){%>
					<fieldset  class="formEdicao">
						<legend class="formEdicaoLegenda">Per�odo que foi realizado o C�lculo de Liquida��o de Pena:</legend>
						<label class="formEdicaoLabel" >Data Inicial: </label><br>    
						<input class="formEdicaoInputSomenteLeitura" name="dataInicialPeriodo" id="dataInicialPeriodo" type="text" size="10" maxlength="10" value="<%=DataProvavelDt.getDataInicialPeriodo()%>" readonly="readonly"/>  
						<br />
						<label class="formEdicaoLabel" >Data Final: </label><br>    
					    <input class="formEdicaoInputSomenteLeitura" name="dataFinalPeriodo" id="dataFinalPeriodo" type="text" size="10" maxlength="10" value="<%=DataProvavelDt.getDataFinalPeriodo()%>" readonly="readonly"/>  
						<br />
					</fieldset>

				<%} if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){	%>
						<div id="divTabela" class="divTabela">
						<table id="ListaReeducandos" class="Tabela" width="100%">
		        			<thead>
		            			<tr class="TituloColuna">
									<th></th>
		            				<th align="center">Tipo do Processo</th>
		            				<th align="center">N� do Processo</th>
		                			<th align="left">Sentenciado</th>
		                			<th align="left">Regime</th>
									<th align="center">Data do C�lculo</th>
									<th align="center">Data Homologa��o</th>
									<th align="center">Data Prov�vel T�rmino</th>
		                			<th align="center">Data Prov�vel PR</th>
		                			<th align="center">Data Prov�vel LC</th>
		                			<th align="center">Data Mandado de Pris�o</th>
									<%if (!passoEditar.equals("8")){%>
		                			<th align="center">Analista de C�lculo</th>
									<%} %>
		               			</tr>
		           			</thead>
		    				<tbody id="tabListaProcesso">
				    		<%
				    		 	boolean boLinha=false; 
				    			if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){
				   	    			for (int i=0; i<listaSentenciado.size(); i++){
				   	    				DataProvavelDt sentenciado = new DataProvavelDt();
				   	    				sentenciado = (DataProvavelDt)listaSentenciado.get(i);
							%>			
									<tr class="TabelaLinha<%=(boLinha ? 1 : 2)%>" onclick="javascript: buscarProcesso(<%=sentenciado.getIdProcesso()%>);" title="Consultar Processo">
										<td><%=i+1%></td>
										<td align="center"><%=sentenciado.getProcessoTipo()%></td>
										<td align="center"><%=sentenciado.getNumeroProcesso()%></td>
										<td align="left"><%=sentenciado.getInformacaoSentenciado()%></td>
										<td align="left"><%=sentenciado.getRegime()%></td>
										<td align="center"><%=sentenciado.getDataCalculo()%></td>
										<td align="center"><%=sentenciado.getDataHomologacao()%></td>
										<td align="center"><%=sentenciado.getDataProvavelTerminoPena()%></td>
										<td align="center"><%=sentenciado.getDataProvavelProgressao()%></td>
			                   			<td align="center"><%=sentenciado.getDataProvavelLivramento()%></td>
			                   			<td align="center"><%=sentenciado.getDataValidadeMandadoPrisao()%></td>
										<%if (!passoEditar.equals("8")){%>
			                   			<td align="left"><%=sentenciado.getNomeUsuario()%></td>
										<%} %>
			                   		</tr>
		                   	<%		boLinha = !boLinha;}
				   	    		}%>
		   	    				
		                </table>
		               
		             </div>   
					</div>
				<%}else {%>			
					<div style="width:260px"></div><span class="span1">N�o foi encontrado sentenciado.</span><br />
				<%}%>
				<br />
			</form>
		<%@ include file="Padroes/Mensagens.jspf" %>
	</body>
</html>