<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.projudi.dt.PermissaoEspecialDt"%>
<%@page import="br.gov.go.tj.projudi.dt.TransitoJulgadoEventoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.CalculoLiquidacaoDt"%>
<html>
	<head>
		<title>Detalhamento do(s) evento(s) de comutação</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/menusimples.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/tabelas.js'></script>
		<script type="text/javascript">
			function buscarProcesso() {
				//apresentar os dados do Processo
				AlterarValue('PaginaAtual', '-1');
				AlterarAction('Formulario','BuscaProcesso');
				document.Formulario.submit();
			}

		</script>
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
  			<div class="area"><h2>&raquo; Detalhamento dos eventos de comutação</h2></div>
  			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input id="permissaoEditarEvento" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("permissaoEditarEvento")%>" />
				<input id="Id_Processo" name="Id_Processo" type="hidden" value="<%=processoDt.getId()%>"/>
				<div id="divEditar" class="divEditar">
					<fieldset id="VisualizaDados" class="VisualizaDados"><legend> Dados do Processo </legend>
						<table>
							<tr><td><div> Processo</div><span><a href="<%=request.getAttribute("tempRetornoProcesso")%>?Id_Processo=<%=processoDt.getId()%>&PassoBusca=2"><%=processoDt.getProcessoNumeroCompleto()%></a></span><br>
									<div> Serventia</div><span> <%=processoDt.getServentia()%> </a></span><br /><br />
									<div> Sentenciado</div><span> <%=processoDt.getPrimeiroPoloPassivoNome()%></span><br>
									<div> Nome da Mãe</div><span> <%=processoDt.getPrimeiroPoloPassivoNomeMae()%></span><br>
									<div> Data de Nascimento</div><span> <%=processoDt.getPrimeiroPoloPassivoDataNascimento()%></span>
								</td>
							</tr>
						</table>
					</fieldset><br /><br />

					<% 	List listaTJComutacao = (List) (request.getAttribute("listaTJComutacao"));
						if (listaTJComutacao == null){%>
					<fieldset class="formEdicao">
						<legend> Dados da Comutação</legend>
						<div> Não há o evento de comutação para este sentenciado!</div>
						<br></br>
					</fieldset>
					<%} else {%>
						
					<!-- TRÂNSITO EM JULGADO -->
					<fieldset class="formEdicao"><legend> Trânsito(s) em Julgado com Comutação </legend>
						<% 	List listaTJ = (List) ((List) request.getAttribute("listaTJComutacao")).get(0); 
							String idTJ = "";
							String idCondenacao = "";
							for (int i=0; i<listaTJ.size(); i++){
								TransitoJulgadoEventoDt ctj = (TransitoJulgadoEventoDt) listaTJ.get(i);
								//if(idTJ.equals(ctj.getId_TransitoJulgado())){ //criar tabela 
								if(idCondenacao.equals(ctj.getId_CondenacaoExecucao())){ //criar tabela %>

									<tr>
										<td align="center" ><%=ctj.getDataInicioEvento()%></td>
										<td align="center" ><%=ctj.getFracao()%></td>
										<td align="center" ><%=ctj.getTempoPenaRemanescenteTJAnos()%></td>
										<td align="center" ><%=ctj.getTempoComutacaoAnos()%></td>
									</tr>

								<%}  else {%>
									<% //if(idTJ.length() > 0){	
									 if(idCondenacao.length() > 0){%>
	
									</tbody>
						    	</table>
	
									<%}  //idTJ = ctj.getId_TransitoJulgado();
										idCondenacao = ctj.getId_CondenacaoExecucao();%>
	
								<br />
								<div style="margin-left: 3%">Data do TJ: <%=ctj.getDataInicioTransito()%> - Tempo de Pena da Condenação: <%=ctj.getTempoPenaTJAnos() %> (a-m-d) </div> 
				    			<table class="Tabela" cellpadding="0" cellspacing="0" style="font-size: 10px !important;width:50%; margin-left: 3%">			        			<thead>
				            			<tr >
				                			<th align="center">Data Comutação</th>
				                   			<th align="center">Fração</th>
				                			<th align="center">Tempo Pena Remanescente</th>
				                			<th align="center">Tempo Comutado</th>
				               			</tr>
				           			</thead>
				    				<tbody id="tabListaProcesso">
										<tr>
											<td align="center" ><%=ctj.getDataInicioEvento()%></td>
											<td align="center" ><%=ctj.getFracao()%></td>
											<td align="center" ><%=ctj.getTempoPenaRemanescenteTJAnos()%></td>
											<td align="center" ><%=ctj.getTempoComutacaoAnos()%></td>
										</tr>									
								<%}
							} // fim do FOR%> 
								</tbody>
					    	</table>
					</fieldset>
					<!-- FIM - TRÂNSITO EM JULGADO -->
					<br>
					<!-- COMUTAÇÃO -->
					<% List listaComutacao = (List) ((List) request.getAttribute("listaTJComutacao")).get(1); %>
					<fieldset class="formEdicao"><legend> Comutação - Consolidação </legend>
		    			<table class="Tabela" cellpadding="0" cellspacing="0" style="font-size: 10px !important; margin-left: 3%; width:50%;">
		        			<thead>
		            			<tr><th align="center">Data</th>
		                			<th align="center">Fração</th>
		                			<th align="center">Tempo Total da Comutação</th>
		               			</tr>
		           			</thead>
		    				<tbody>
								<%for (int i=0; i<listaComutacao.size(); i++){
									TransitoJulgadoEventoDt ctj = (TransitoJulgadoEventoDt)listaComutacao.get(i);%>
								<tr><td align="center" ><%=ctj.getDataInicioEvento()%></td>
									<td align="center" ><%=ctj.getFracao()%></td>
									<td align="center" ><%=ctj.getTempoComutacaoAnos()%></td>
								</tr>
								<% }%>
		           			</tbody>
				    	</table>
					</fieldset>
					<!-- FIM - COMUTAÇÃO -->
				<% }%>
				</div>
			</form>
		</div>
	</body>
</html> 