<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.utils.DiaHoraEventos"%>

<jsp:useBean id="DataProvavelDt" scope="request" class="br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt"/>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.DataProvavelDt"%>

<html>
	<head>
		<title>Processo Execução Penal</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
		<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
		<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
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
						<input name="btnImprimir" type="submit" value="Imprimir Resultado" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('PassoImprimir','1');"/>
					</div>
				<%
				List listaSentenciado = (List) request.getAttribute("listaSentenciado");
				//Tela inicial de consulta de Data provavel
				if ((listaSentenciado!=null) && (listaSentenciado.size()>0)){
	        			boolean progressao = false;
	        			boolean livramento = false;
	        			boolean mandadoPrisao = false;
	        			if (!DataProvavelDt.getTipoConsulta().equals("")){
	        				String tipoConsulta = DataProvavelDt.getTipoConsulta();
	        				if (tipoConsulta.equals("PROGRESSAO")) progressao = true;
	        				else if (tipoConsulta.equals("LIVRAMENTO")) livramento = true;
	        				else if (tipoConsulta.equals("MANDADOPRISAO")) mandadoPrisao = true;
		        			%>
		        			<input id="tipoConsulta" name="tipoConsulta" type="hidden" value="<%=tipoConsulta%>"/>
		        			<fieldset  class="formEdicao">
								<legend class="formEdicaoLegenda">Período da Consulta</legend>
								<label class="formEdicaoLabel">Data Inicial: </label><br>    
								<input class="formEdicaoInputSomenteLeitura" name="dataInicialPeriodo" id="dataInicialPeriodo" type="text" size="10" maxlength="10" value="<%=DataProvavelDt.getDataInicialPeriodo()%>" readonly="readonly"/>  
								<br />
								<label class="formEdicaoLabel">Data Final: </label><br>    
							    <input class="formEdicaoInputSomenteLeitura" name="dataFinalPeriodo" id="dataFinalPeriodo" type="text" size="10" maxlength="10" value="<%=DataProvavelDt.getDataFinalPeriodo()%>" readonly="readonly"/>  
							</fieldset>
		        			<% } %>
						<div id="divTabela" class="divTabela">
						<table id="ListaReeducandos" class="Tabela" width="100%">
		        			<thead>
		            			<tr class="TituloColuna">
									<th></th>
		            				<th align="center" width="15%">Tipo do Processo</th>
		            				<th align="center" width="15%">Nº do Processo</th>
		                			<th  align="left" width="35%">Sentenciado</th>
									<th align="center">Data do Calculo</th>
									<th align="center">Data Homologação</th>
		                			<%if ((progressao) || (livramento)) {%><th align="center" width="11%">Data Provável Progressão de Regime</th><%} %>
		                			<%if ((progressao) || (livramento)) {%><th align="center" width="11%">Data Provável Livramento Condicional</th><%} %>
		                			<%if (mandadoPrisao) {%><th align="center" width="22%">Data Mandado de Prisão</th><%} %>
									<th align="center">Regime</th>
									<th align="center">Modalidade</th>
									<th align="center">Foragido</th>
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
										<td ><%=i+1%></td>
										<td align="center"><%=sentenciado.getProcessoTipo()%></td>										
										<td align="center"><%=sentenciado.getNumeroProcesso()%></td>
										<td align="left"><%=sentenciado.getInformacaoSentenciado()%></td>
										<td align="center"><%=sentenciado.getDataCalculo()%></td>	
										<td align="center"><%=sentenciado.getDataHomologacao()%></td>
										<%if ((progressao) || (livramento)) {%><td align="center"><%=sentenciado.getDataProvavelProgressao()%></td><%} %>
			                   			<%if ((progressao) || (livramento)) {%><td align="center"><%=sentenciado.getDataProvavelLivramento()%></td><%} %>
			                   			<%if (mandadoPrisao) {%><td align="center"><%=sentenciado.getDataValidadeMandadoPrisao()%></td><%} %>
										<td align="center"><%=sentenciado.getRegime()%></td>
										<td align="center"><%=sentenciado.getDescricaoModalidade()%></td>
										<td align="center"><%=sentenciado.getDataFuga()%></td>
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