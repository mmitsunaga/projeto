<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaDt"%>

<jsp:useBean id="relatorioSituacaoGabineteDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioSituacaoGabineteDt"/>
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
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioSituacaoGabinete" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			
			<br />
			<div id="divEditar" class="divEditar">
	
				<input type="hidden" id="tipoConsulta" name="tipoConsulta" value="<%=request.getAttribute("TipoConsulta") %>" />
				<input type="hidden" name="Id_Processo" id="Id_Processo" value=""/>
			
				<%if(request.getAttribute("TipoConsulta") != null && request.getAttribute("TipoConsulta").toString().equals("1")) {%>
					<!-- INÍCIO DA LISTA DE PROCESSOS DISTRIBUIDOS -->
					<div id="divTabela" class="divTabela"> 
						<table id="Tabela" class="Tabela" width="50%">
							<thead>
								<tr>
									<th colspan="4">PROCESSOS DISTRIBUIDOS</th>
								</tr>
								<tr>
									<th width="10%">&nbsp;</th>
									<th width="20%">Número do Processo</th>
									<th width="50%">Tipo de Processo</th>
									<th width="20%">Data de Distribuição</th>
								</tr>
								</thead>
							<tbody id="tabLista">
							<%
			  				List liTemp = relatorioSituacaoGabineteDt.getListaItens();
							ProcessoDt objTemp = new ProcessoDt();
			  				boolean boLinha=false; 
			  				if(liTemp!=null){ 
				  				for(int i = 0 ; i< liTemp.size();i++) {
				      				objTemp = (ProcessoDt)liTemp.get(i); 
									%>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
										<td width="10%"><%=i+1%></td>
										<td width="20%" align="center"><a onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getId_Processo()%>'); FormSubmit('Formulario');" ><%=objTemp.getProcessoNumero()%></a></td>
										<td width="50%"><%=objTemp.getProcessoTipo()%></td>
										<td width="20%" align="center"><%=objTemp.getDataRecebimento()%></td>
									</tr>
								<% boLinha = !boLinha;
								}
							}%>
							</tbody>
						</table>
					</div> 
					<!-- FIM DA LISTA DE PROCESSOS DISTRIBUIDOS -->
				<%} else if(request.getAttribute("TipoConsulta") != null && request.getAttribute("TipoConsulta").toString().equals("2")) {%>
				
				<%} else if(request.getAttribute("TipoConsulta") != null && request.getAttribute("TipoConsulta").toString().equals("3")) { %>
					<!-- INÍCIO DA LISTA DE PROCESSOS CONCLUSOS PENDENTES -->
					<div id="divTabela" class="divTabela"> 
						<table id="Tabela" class="Tabela" width="50%">
							<thead>
								<tr>
									<th colspan="4">PROCESSOS CONCLUSOS PENDENTES</th>
								</tr>
								<tr>
									<th width="10%">&nbsp;</th>
									<th width="20%">Número do Processo</th>
									<th width="50%">Tipo da Pendência</th>
									<th width="20%">Data da Pendência</th>
								</tr>
								</thead>
							<tbody id="tabLista">
							<%
			  				List liTemp = relatorioSituacaoGabineteDt.getListaItens();
							PendenciaDt objTemp = new PendenciaDt();
			  				boolean boLinha=false; 
			  				if(liTemp!=null){ 
				  				for(int i = 0 ; i< liTemp.size();i++) {
				      				objTemp = (PendenciaDt)liTemp.get(i); 
									%>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
										<td width="10%"><%=i+1%></td>
										<td width="20%" align="center"><a onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getId_Processo()%>'); FormSubmit('Formulario');" ><%=objTemp.getProcessoNumero() + "." + objTemp.getDigitoVerificador()%></a></td>
										<td width="50%"><%=objTemp.getPendenciaTipo()%></td>
										<td width="20%" align="center"><%=objTemp.getDataInicio()%></td>
									</tr>
								<% boLinha = !boLinha; 
								}
							}%>
							</tbody>
						</table>
					</div> 
					<!-- FIM DA LISTA DE PROCESSOS CONCLUSOS PENDENTES -->
				<%} else if(request.getAttribute("TipoConsulta") != null && request.getAttribute("TipoConsulta").toString().equals("4")) { %>
					<!-- INÍCIO DA LISTA DE PROCESSOS CONCLUSOS FINALIZADOS -->
					<div id="divTabela" class="divTabela"> 
						<table id="Tabela" class="Tabela" width="50%">
							<thead>
								<tr>
									<th colspan="5">PROCESSOS CONCLUSOS FINALIZADOS</th>
								</tr>
								<tr>
									<th width="5%">&nbsp;</th>
									<th width="15%">Número do Processo</th>
									<th width="40%">Tipo da Pendência</th>
									<th width="20%">Data de Início</th>
									<th width="20%">Data de Término</th>
								</tr>
								</thead>
							<tbody id="tabLista">
							<%
			  				List liTemp = relatorioSituacaoGabineteDt.getListaItens();
							PendenciaDt objTemp = new PendenciaDt();
			  				boolean boLinha=false; 
			  				if(liTemp!=null){ 
				  				for(int i = 0 ; i< liTemp.size();i++) {
				      				objTemp = (PendenciaDt)liTemp.get(i); 
									%>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
										<td width="5%"><%=i+1%></td>
										<td width="20%" align="center"><a onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getId_Processo()%>'); FormSubmit('Formulario');" ><%=objTemp.getProcessoNumero() + "." + objTemp.getDigitoVerificador()%></a></td>
										<td width="40%"><%=objTemp.getPendenciaTipo()%></td>
										<td width="20%" align="center"><%=objTemp.getDataInicio()%></td>
										<td width="20%" align="center"><%=objTemp.getDataFim()%></td>
									</tr>
								<% boLinha = !boLinha; 
								}
							}%>
							</tbody>
						</table>
					</div> 
					<!-- FIM DA LISTA DE PROCESSOS CONCLUSOS FINALIZADOS -->
				<%} else if(request.getAttribute("TipoConsulta") != null && request.getAttribute("TipoConsulta").toString().equals("5")) { %>
					<!-- INÍCIO DA LISTA DE PROCESSOS PARA REVISÃO -->
					<div id="divTabela" class="divTabela"> 
						<table id="Tabela" class="Tabela" width="50%">
							<thead>
								<tr>
									<th colspan="4">PROCESSOS PARA REVISÃO</th>
								</tr>
								<tr>
									<th width="10%">&nbsp;</th>
									<th width="20%">Número do Processo</th>
									<th width="50%">Tipo da Pendência</th>
									<th width="20%">Data de Início</th>
								</tr>
								</thead>
							<tbody id="tabLista">
							<%
			  				List liTemp = relatorioSituacaoGabineteDt.getListaItens();
							PendenciaDt objTemp = new PendenciaDt();
			  				boolean boLinha=false; 
			  				if(liTemp!=null){ 
				  				for(int i = 0 ; i< liTemp.size();i++) {
				      				objTemp = (PendenciaDt)liTemp.get(i); 
									%>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
										<td width="10%"><%=i+1%></td>
										<td width="20%" align="center"><a onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getId_Processo()%>'); FormSubmit('Formulario');" ><%=objTemp.getProcessoNumero() + "." + objTemp.getDigitoVerificador()%></a></td>
										<td width="50%"><%=objTemp.getPendenciaTipo()%></td>
										<td width="20%" align="center"><%=objTemp.getDataInicio()%></td>
									</tr>
								<% boLinha = !boLinha; 
								}
							}%>
							</tbody>
						</table>
					</div> 
					<!-- FIM DA LISTA DE PROCESSOS CONCLUSOS FINALIZADOS -->
				<%} else if(request.getAttribute("TipoConsulta") != null && request.getAttribute("TipoConsulta").toString().equals("999")) { %>
					<!-- INÍCIO DA LISTA DE MOVIMENTAÇÃO TIPO CLASSE -->
					<div id="divTabela" class="divTabela"> 
						<table id="Tabela" class="Tabela" width="50%">
							<thead>
								<tr>
									<th colspan="4"><%=relatorioSituacaoGabineteDt.getMovimentacaoTipoClasse() %></th>
								</tr>
								<tr>
									<th width="10%">&nbsp;</th>
									<th width="20%">Número do Processo</th>
									<th width="50%">Tipo de Movimentação</th>
									<th width="20%">Data da Movimentação</th>
								</tr>
								</thead>
							<tbody id="tabLista">
							<%
			  				List liTemp = relatorioSituacaoGabineteDt.getListaItens();
							PendenciaDt objTemp = new PendenciaDt();
			  				boolean boLinha=false; 
			  				if(liTemp!=null){ 
				  				for(int i = 0 ; i< liTemp.size();i++) {
				      				objTemp = (PendenciaDt)liTemp.get(i); 
									%>
									<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
										<td width="10%"><%=i+1%></td>
										<td width="20%" align="center"><a onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>'); AlterarValue('Id_Processo','<%=objTemp.getId_Processo()%>'); FormSubmit('Formulario');" ><%=objTemp.getProcessoNumero() + "." + objTemp.getDigitoVerificador()%></a></td>
										<td width="50%"><%=objTemp.getPendenciaTipo()%></td>
										<td width="20%" align="center"><%=objTemp.getDataInicio()%></td>
									</tr>
								<% boLinha = !boLinha; 
								}
							}%>
							</tbody>
						</table>
					</div> 
					<!-- FIM DA LISTA DE MOVIMENTAÇÃO TIPO CLASSE -->
				<%} %>
				<button name="imgVoltar" value="Voltar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');" >
                  	<img src="./imagens/imgVoltarPequena.png" alt="Voltar" />
                   	Voltar
                </button>
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>