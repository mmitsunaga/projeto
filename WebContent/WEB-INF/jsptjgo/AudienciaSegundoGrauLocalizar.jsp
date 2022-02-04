<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.AudienciaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaSubtipoDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>


<jsp:useBean id="AudienciaSegundoGraudt" scope="session" class= "br.gov.go.tj.projudi.dt.AudienciaSegundoGrauDt" />
<jsp:useBean id="UsuarioSessaoDt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt" />

<html>
	<head>
		<title><%=request.getAttribute("TituloPagina")%></title>
    	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
		
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
   		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
		
		<%@ include file="./js/Paginacao.js"%>
	</head>
	
	<body>
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; CONSULTA DE SESSÕES AGENDADAS</h2></div>
			<form name="Formulario" id="Formulario" action="AudienciaSegundoGrau" method="post" >
			
				<input type="hidden"  id="PaginaAtual" name="PaginaAtual" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input type="hidden"  id="PaginaAnterior" name="PaginaAnterior" value="<%=request.getAttribute("PaginaAnterior")%>" />
				<input type="hidden" id="PassoEditar" name="PassoEditar" value="<%=request.getAttribute("PassoEditar")%>" />
				<input type="hidden" id="fluxo" name="fluxo" value="<%=request.getAttribute("fluxo")%>" />
				<input type="hidden" id="Id_Audiencia" name="Id_Audiencia" />
				
				<div id="divLocalizar" class="divLocalizar">
					<fieldset id="formLocalizar" class="formLocalizar">
						<legend id="formLocalizarLegenda" class="formLocalizarLegenda">Parâmetros para Consulta</legend>
						<div class="col20">
						<label for="DataInicialConsulta">Data Inicial</label><br> 
			    		<input class="formLocalizarInput" name="DataInicialConsulta" id="DataInicialConsulta" type="text" size="10" maxlength="10" title="Clique para escolher uma data." value="<%=AudienciaSegundoGraudt.getDataInicialConsulta()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)"/> 
			    		<img id="calDataInicialConsulta" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataInicialConsulta,'dd/mm/yyyy',this)" />			    	
						</div>
						
						<div class="col30">
			    		<label for="DataFinalConsulta">Data Final</label><br> 
			    		<input class="formLocalizarInput" name="DataFinalConsulta" id="DataFinalConsulta" type="text" size="10" maxlength="10" title="Clique para escolher uma data." value="<%=AudienciaSegundoGraudt.getDataFinalConsulta()%>" onkeyup="mascara_data(this)" onkeypress="return DigitarSoNumero(this, event)"/> 
			    		<img id="calDataFinalConsulta" class="calendario" src="./imagens/dlcalendar_2.gif" title="Calendário"  alt="Calendário" onclick="displayCalendar(document.forms[0].DataFinalConsulta,'dd/mm/yyyy',this)" />
						</div>
						<div class="clear"></div>
					
						<% 	int statusAudiencia = -1;
							if (request.getAttribute("StatusAudiencia") != null) statusAudiencia = Funcoes.StringToInt(request.getAttribute("StatusAudiencia").toString());
						%>
					   	<label class="formLocalizarLabel" for="Id_AudienciaProcessoStatus">Status da Sessão</label><br>
					   	<input type="radio" name="StatusAudiencia" value="<%=AudienciaDt.ABERTA%>" <%=(statusAudiencia == AudienciaDt.ABERTA?"checked":"")%>/>Abertas
		       			<input type="radio" name="StatusAudiencia" value="<%=AudienciaDt.FINALIZADA%>" <%=(statusAudiencia == AudienciaDt.FINALIZADA?"checked":"")%>/>Finalizadas
		       			<input type="radio" name="StatusAudiencia" value="-1" <%=(statusAudiencia == -1?"checked":"")%> />Todas
					   	<br />
	
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input class="imgLocalizar" id="imgLocalizar" name="imaLocalizar" value="Consultar" type="submit"  title="Localizar - Localiza audiências de acordo com os campos informados" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Localizar)%>')" />
							<input class="imgNovo" id="imgNovo" name="imaNovo" value="Limpar" type="submit" title="Novo - Limpar Tela" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" />
						</div>
						
					</fieldset>
				   	
				   	<fieldset id="formLocalizar" class="formLocalizar">
						<legend>Sessões Agendadas</legend>
					   	<div id="divTabela" class="divTabela">
					   	
					   		<table id="Tabela" class="Tabela">
					        	<thead>
					            	<tr class="TituloColuna">
					                 	<th>Data/Hora</th>
					                 	<%if(UsuarioSessaoDt.isGabineteSegundoGrau() || UsuarioSessaoDt.isSegundoGrau() ||  UsuarioSessaoDt.isUPJ()){%>
				  	                   	<th>Tipo</th>		
				  	                    <% } %>		
					                 	<th>Serventia </th>
					                 	<th>Status</th>
					                  	<th class="colunaMinima">Detalhar</th>
					                  	<%
					                  		if(UsuarioSessaoDt.isGabineteSegundoGrau() || UsuarioSessaoDt.isSegundoGrau() ||  UsuarioSessaoDt.isUPJ()){
					                  	%>
					                  	<th width="10%">Pauta do Dia</th>
					                  	<th class="colunaMinima">Adiados</th>					                  	
					                  	<th width="9%">Em Mesa Para Julgamento</th>
					                  	<%
					                  		}
					                  	%>
					                  	<th class="colunaMinima">Ata</th>
					               	</tr>
					           	</thead>
					           	<tbody id="tabListaAudiencia"> 
									<%
 										List listaAudiencias = (List)request.getAttribute("ListaAudiencias");
 												 				AudienciaDt audienciaDt;
 												  				boolean linha = false;
 										
 												  				if (listaAudiencias != null){
 												  					for(int i = 0 ; i < listaAudiencias.size();i++) {
 												  	  					audienciaDt = (AudienciaDt)listaAudiencias.get(i);
 									%>
					  	  					<tr class="TabelaLinha<%=(linha?1:2)%>" align="center"> 
					  	                		<td onclick="selecionaSubmete('<%=audienciaDt.getId()%>','<%=audienciaDt.getDataAgendada()%>')"><%=audienciaDt.getDataAgendada()%></td>
					  	                		<%if(UsuarioSessaoDt.isGabineteSegundoGrau() || UsuarioSessaoDt.isSegundoGrau() ||  UsuarioSessaoDt.isUPJ()){%>
					  	                			<% if (audienciaDt.isVirtual()) { %>
						  	                   		<td> Virtual </td>
						  	                   		<% } else { %>
						  	                   		<td> Presencial </td>	
						  	                   		<% } %>			
						  	                    <% } %>	
					  	                		
					  	                		<td onclick="selecionaSubmete('<%=audienciaDt.getId()%>','<%=audienciaDt.getDataAgendada()%>')"><%=audienciaDt.getServentia()%></td>
					  	                		<%
					  	                			if (!audienciaDt.getDataMovimentacao().equals("")){
					  	                		%>
					  	                		<td> Finalizada </td>
					  	                		<%
					  	                			} else {
					  	                		%>
					  	                		<td> Aberta </td>
					  	                		<%
					  	                			}
					  	                		%>
				  	                   			<td>
				  	                   				<input name="formLocalizarimgEditar" type="image" src="./imagens/imgEditar.png" onclick="AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','0');" />
				  	                   			</td>
				  	                   			<%
				  	                   				if(UsuarioSessaoDt.isGabineteSegundoGrau() || UsuarioSessaoDt.isSegundoGrau() ||  UsuarioSessaoDt.isUPJ()){
				  	                   			%>
				  	                   			<td>
				  	                   				<input name="formLocalizarimgImprimirPautaDia" type="image" src="./imagens/22x22/btn_odt.png" onclick="AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');AlterarValue('PassoEditar','1');" />
				  	                   			</td>
				  	                   			<td>
				  	                   				<input name="formLocalizarimgImprimirAdiados" type="image" src="./imagens/22x22/btn_odt.png" onclick="AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');AlterarValue('PassoEditar','2');" />
				  	                   			</td>				  	                   							  	                   			
				  	                   			<td>
				  	                   				<input name="formLocalizarimgImprimirEmMesaParaJulgamento" type="image" src="./imagens/22x22/btn_odt.png" onclick="AlterarValue('Id_Audiencia','<%=audienciaDt.getId()%>');AlterarValue('PaginaAtual','<%=Configuracao.Imprimir%>');AlterarValue('PassoEditar','3');" />
				  	                   			</td>
				  	                   			<%}%>
				  	                   			<td>
					  	                			<%if (audienciaDt.getId_ArquivoFinalizacaoSessao() != null && audienciaDt.getId_ArquivoFinalizacaoSessao().trim().length() > 0){%>
					  	                				<a href="AudienciaSegundoGrau?PassoEditar=1&IdArquivo=<%=audienciaDt.getId_ArquivoFinalizacaoSessao()%>"><img style="border: none;" src="./imagens/22x22/ico_arquivos.png" title="Arquivo Ata da Sessão">&nbsp;</a>
					  	                			<%}%>
					  	                		</td>
						  	               	</tr>
					  				<%		linha = !linha;
					  					} //Fim FOR
									}%> 
					           	</tbody>
					       	</table>
					   	</div>
				   	</fieldset>
				   	
				</div>
				
				<% if (listaAudiencias != null){%>
				    <%@ include file="Padroes/PaginacaoAudiencia.jspf"%>
				<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>