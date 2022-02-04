<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>

<jsp:useBean id="RelatorioSumariodt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.RelatorioSumarioDt"/>

<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>

<html>
	<head>
		<title> <%=request.getAttribute("tempPrograma")%> </title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./js/jscalendar/dhtmlgoodies_calendar.css');
		</style>
		
		
		<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js"></script>
	</head>
	<body>
		<div id="divCorpo" class="divCorpo" >
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="RelatorioSumario" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Novo)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relat�rio" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">	

			</div/><br />
			<div id="divEditar" class="divEditar">

				<fieldset class="formEdicao"> 
					
					<legend class="formEdicaoLegenda">Per�odo</legend>
					<div class="periodo">
					<label for="DataInicial">M�s/Ano Inicial</label><br> 
					
					<select id="MesInicial" name ="MesInicial" class="formEdicaoCombo">
						<option value="1" <%if(RelatorioSumariodt.getMesInicial().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(RelatorioSumariodt.getMesInicial().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(RelatorioSumariodt.getMesInicial().equals("3")){%>selected="true"<%}%>>Mar�o</option>
						<option value="4" <%if(RelatorioSumariodt.getMesInicial().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(RelatorioSumariodt.getMesInicial().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(RelatorioSumariodt.getMesInicial().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(RelatorioSumariodt.getMesInicial().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(RelatorioSumariodt.getMesInicial().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(RelatorioSumariodt.getMesInicial().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(RelatorioSumariodt.getMesInicial().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(RelatorioSumariodt.getMesInicial().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(RelatorioSumariodt.getMesInicial().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoInicial" name ="AnoInicial" class="formEdicaoCombo" >
					<%
					for(int i = Funcoes.StringToInt(RelatorioSumariodt.getAnoInicial().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(RelatorioSumariodt.getAnoInicial().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
							</div>
							<div class="periodo">			
					<label for="DataFinal">M�s/Ano Final</label><br> 
					<select id="MesFinal" name ="MesFinal" class="formEdicaoCombo">
						<option value="1" <%if(RelatorioSumariodt.getMesFinal().equals("1")){%>selected="true"<%}%>>Janeiro</option>
						<option value="2" <%if(RelatorioSumariodt.getMesFinal().equals("2")){%>selected="true"<%}%>>Fevereiro</option>
						<option value="3" <%if(RelatorioSumariodt.getMesFinal().equals("3")){%>selected="true"<%}%>>Mar�o</option>
						<option value="4" <%if(RelatorioSumariodt.getMesFinal().equals("4")){%>selected="true"<%}%>>Abril</option>
						<option value="5" <%if(RelatorioSumariodt.getMesFinal().equals("5")){%>selected="true"<%}%> >Maio</option>
						<option value="6" <%if(RelatorioSumariodt.getMesFinal().equals("6")){%>selected="true"<%}%>>Junho</option>
						<option value="7" <%if(RelatorioSumariodt.getMesFinal().equals("7")){%>selected="true"<%}%>>Julho</option>
						<option value="8" <%if(RelatorioSumariodt.getMesFinal().equals("8")){%>selected="true"<%}%>>Agosto</option>
						<option value="9" <%if(RelatorioSumariodt.getMesFinal().equals("9")){%>selected="true"<%}%>>Setembro</option>
						<option value="10" <%if(RelatorioSumariodt.getMesFinal().equals("10")){%>selected="true"<%}%>>Outubro</option>
						<option value="11" <%if(RelatorioSumariodt.getMesFinal().equals("11")){%>selected="true"<%}%>>Novembro</option>
						<option value="12" <%if(RelatorioSumariodt.getMesFinal().equals("12")){%>selected="true"<%}%>>Dezembro</option>
					</select>
					<select id="AnoFinal" name ="AnoFinal" class="formEdicaoCombo">
					<%
					for(int i = Funcoes.StringToInt(RelatorioSumariodt.getAnoFinal().toString()) ; i >= 1997; i--) { %>
						<option value="<%=i%>" <%if(RelatorioSumariodt.getAnoFinal().equals(String.valueOf(i))){%>selected="true"<%}%> ><%=i%></option>
					<%} %>
					</select>
					</div>
					<label for="Aviso" style="float:left;margin-left:25px;"><small>Per�odo n�o pode superar 03(tr�s) meses.</small></label><br>  
				
				</fieldset>
				<fieldset class="formEdicao"> 
					<legend class="formEdicaoLegenda">Serventia</legend>
					<label class="formEdicaoLabel" for="Id_Serventia">Serventia (Vara) 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
					<input class="FormEdicaoimgLocalizar" id="imaLimparServentia" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" />
					</label><br> 
					<input id="Id_Serventia"  type="hidden" value="<%=RelatorioSumariodt.getId_Serventia()%>"/>
					<input  class="formEdicaoInputSomenteLeitura" id="Serventia"  readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getServentia()%>"/><br />
				</fieldset>
				
				<fieldset class="formEdicao"> 
				    <legend class="formEdicaoLegenda">Usu�rio</legend>
				    <label class="formEdicaoLabel" for="Id_Serventia">Servidor Judici�rio 
				    <input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
				    <input class="FormEdicaoimgLocalizar" id="imaLimparUsuario" name="imaLimparUsuario" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Usuario','Usuario'); return false;" />
				    </label><br> 
				    <input id="Id_Usuario"  type="hidden" value="<%=RelatorioSumariodt.getUsuario().getId()%>"/>
				    <input  class="formEdicaoInputSomenteLeitura" id="Usuario"  readonly="true" type="text" size="60" maxlength="60" value="<%=RelatorioSumariodt.getUsuario().getNome()%>"/>
				    
				    <br />
				</fieldset>
				<%if (RelatorioSumariodt.getUsuario().getListaUsuarioServentias() != null) { %>
						<%if (RelatorioSumariodt.getUsuario().getId().length() > 0){%>
						<fieldset class="formEdicao">
				    	 	<legend class="formEdicaoLegenda">Serventias do Usu�rio</legend> 
				    	  		<%@ include file="RelatorioSumarioListaServentiasUsuario.jspf"%> 
			    		</fieldset>
			    		<%} else{ %>
			    		<fieldset class="formEdicao">
				    	 	<legend class="formEdicaoLegenda">Usu�rios da Serventia</legend> 
				    	  		<%@ include file="RelatorioSumarioListaUsuariosServentia.jspf"%> 
			    		</fieldset>
			    		<%} %>
			    <%}%> 	
			  </div>
			
			</form>
		</div>
	<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
		<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
	<%}%>
	
	<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == false){ %>
		<div class="divMensagemErro" id="MensagemErro"> <%=request.getAttribute("MensagemErro").toString().trim()%></div>
	<%}%>
	</body>
</html>