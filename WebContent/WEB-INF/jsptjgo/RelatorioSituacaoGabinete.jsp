<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.MovimentacaoTipoClasseDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

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
	
				<input type="hidden" id="tipoConsulta" name="tipoConsulta" value="" />
			
				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Período</legend>
					<div class="periodo">
					<label for="DataInicial">*Mês/Ano Inicial</label><br> 
					
					<select id="MesInicial" name ="MesInicial" class="formEdicaoCombo">
						<option value="1" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("3")){%>selected="true"<%}%>>Março</option>
						<option value="4" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(relatorioSituacaoGabineteDt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoInicial" name ="AnoInicial" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(relatorioSituacaoGabineteDt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioSituacaoGabineteDt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
								</div>
								<div class="periodo">		
					<label for="DataFinal">*Mês/Ano Final</label><br> 
					<select id="MesFinal" name ="MesFinal" class="formEdicaoCombo">
						<option value="1" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("3")){%>selected="true"<%}%>>Março</option>
						<option value="4" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(relatorioSituacaoGabineteDt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoFinal" name ="AnoFinal" class="formEdicaoCombo">
					<%
					for(int i = Funcoes.StringToInt(relatorioSituacaoGabineteDt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(relatorioSituacaoGabineteDt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
					</div>
					<label for="Aviso" style="float:left;margin-left:25px;"><small>Período não pode superar 03(três) meses.</small></label><br>  
				
				</fieldset>
				<div id="divBotoesCentralizados" class="divBotoesCentralizados">
					<input name="imgSubmeter" type="submit" value="Buscar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>');">
					<input name="imgLimpar" type="submit" value="Limpar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Novo%>');">
				</div>
				<br /> 
				<br />
				<br />
			
				<!-- Se o nome do desembargador estiver preenchido, é porque a consulta já foi feita e há necessidade de apresentar a tabela abaixo. -->
				<%if(relatorioSituacaoGabineteDt.getNomeResponsavel() != null && !relatorioSituacaoGabineteDt.getNomeResponsavel().equals("")) {%>
					<fieldset class="formEdicao"> 
						<legend class="formEdicaoLegenda"> <%=relatorioSituacaoGabineteDt.getNomeResponsavel()%> </legend>
						<table>
							<tr>
								<td align="left"">
									<a href="RelatorioSituacaoGabinete?PaginaAtual=2&TipoConsulta=1">
										Processos Distribuídos
									</a>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									<%=relatorioSituacaoGabineteDt.getQtdeProcessosDistribuidos()%>
								</td>
							</tr>
							<tr>
								<td align="left">
									<label>Votos Proferidos (em desenvolvimento)</label><br>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									-
								</td>
							</tr>
							<tr>
								<td align="left">
									<a href="RelatorioSituacaoGabinete?PaginaAtual=2&TipoConsulta=3">
										Processos Conclusos Pendentes
									</a>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									<%=relatorioSituacaoGabineteDt.getQtdeConclusosPendentes()%> 
								</td>
							</tr>
							<tr>
								<td align="left">
									<a href="RelatorioSituacaoGabinete?PaginaAtual=2&TipoConsulta=4">
										Processos Conclusos Finalizados
									</a>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									<%=relatorioSituacaoGabineteDt.getQtdeConclusosFinalizados()%> 
								</td>
							</tr>
							<tr>
								<td align="left">
									<a href="RelatorioSituacaoGabinete?PaginaAtual=2&TipoConsulta=5">
										Processos para Revisão
									</a>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									<%=relatorioSituacaoGabineteDt.getQtdeRevisao()%>
								</td>
							</tr>
							<tr>
								<td align="left">
									<label>Processos Remanescentes (em desenvolvimento)</label><br>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									-
								</td>
							</tr>
							
							<%
								List liTemp = (List)relatorioSituacaoGabineteDt.getListaMovimentacaoTipoClasse();
								MovimentacaoTipoClasseDt movimentacaoTipoClasseDt;
								if(liTemp!=null)
								for(int i = 0 ; i< liTemp.size();i++) {
									movimentacaoTipoClasseDt = (MovimentacaoTipoClasseDt)liTemp.get(i); 
							%> 
							<tr>
								<td align="left">
									<a href="RelatorioSituacaoGabinete?PaginaAtual=2&TipoConsulta=999&IdMovimentacaoTipoClasse=<%=movimentacaoTipoClasseDt.getId()%>&MovimentacaoTipoClasse=<%=movimentacaoTipoClasseDt.getMovimentacaoTipoClasse()%>">
										<%=movimentacaoTipoClasseDt.getMovimentacaoTipoClasse()%>
									</a>
								</td>
								<td align="left">&nbsp;</td>
								<td>
									<%=movimentacaoTipoClasseDt.getCodigoTemp()%>
								</td>
							</tr>
							<%
								}
							%>
							
				    	</table>
				    </fieldset>
				<%} %>
				   
				
				
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>