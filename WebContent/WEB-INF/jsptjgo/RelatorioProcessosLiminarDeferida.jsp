<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.relatorios.RelatorioProcessoSegundoGrauDt"%>

<jsp:useBean id="processoTempoVidaTipoDt" scope="session" class= "br.gov.go.tj.projudi.dt.relatorios.ProcessoTempoVidaTipoDt"/>

<%@page import="br.gov.go.tj.projudi.dt.AreaDistribuicaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="java.util.HashMap"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>



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
		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; <%=request.getAttribute("tempPrograma")%> </h2></div>
			<form action="ProcessoTempoVidaTipo" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			<input id="paginaPrincipal" name="paginaPrincipal" type="hidden" value="<%=String.valueOf(Configuracao.Curinga7)%>"/>
			
			<div id="divPortaBotoes" class="divPortaBotoes">
				<input id="imgNovo"  class="imgNovo" title="Nova Consulta" name="imaNovo" type="image" src="./imagens/imgNovo.png"  
						onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" >
				<input id="imgImprimir"  class="imgImprimir" title="Gerar Relatório" name="imgImprimir" type="image" 
						src="./imagens/imgImprimir.png"   onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Imprimir)%>')">				
			</div>
			
			<br />
			<div id="divEditar" class="divEditar">
				
				<fieldset class="formEdicao"> 
					
					<label class="formEdicaoLabel" for="nomeBusca2">*Tempo de Distribuição</label><br>
					<select id="nomeBusca2" name ="nomeBusca2" class="formEdicaoCombo" >
						<option value="1"  >Até 20 dias</option>
						<option value="20"  >Mais de 20 dias</option>
						<option value="30"  >Mais de 30 dias</option>
						<option value="40"  >Mais de 40 dias</option>
						<option value="50"  >Mais de 50 dias</option>
						<option value="60"  >Mais de 60 dias</option>
						<option value="70"  >Mais de 70 dias</option>
						<option value="80"  >Mais de 80 dias</option>
						<option value="90"  >Mais de 90 dias</option>
						<option value="100"  >Mais de 100 dias</option>
						<option value="110"  >Mais de 110 dias</option>
						<option value="120"  >Mais de 120 dias</option>
						<option value="130"  >Mais de 130 dias</option>
						<option value=140""  >Mais de 140 dias</option>
						<option value=150""  >Mais de 150 dias</option>
						<option value="180"  >Mais de 180 dias</option>
						<option value="240"  >Mais de 240 dias</option>
						<option value="360"  >Mais de 360 dias</option>
						
					</select>
										
				</fieldset>
				
				
				<%
				
				String estatistica = request.getAttribute("estatistica").toString();				
				if (estatistica.equals("sim")) { %>			
				
					<fieldset class="formEdicao" id="fieldsetAreaDistribuicao" > 
						<legend class="formEdicaoLegenda">Serventia</legend>
						<label class="formEdicaoLabel" for="Id_Serventia">*Serventia
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarAreaDistribuicao" name="imaLocalizarAreaDistribuicao" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" > 
						<input class="FormEdicaoimgLocalizar" id="imaLimparAreaDistribuicao" name="imaLimparAreaDistribuicao" type="image"  src="./imagens/16x16/edit-clear.png"  onclick="LimparChaveEstrangeira('Id_Serventia','Serventia'); return false;" />
						</label><br>  
						
						<input id="Id_Serventia" name="Id_Serventia" type="hidden" value="<%=processoTempoVidaTipoDt.getIdServentia()%>"/>
						<input class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly="true" type="text" size="60" maxlength="60" value="<%=processoTempoVidaTipoDt.getServentia()%>"/><br />
					</fieldset> 		
				
				<%}%>		
				
			  </div>
			<%if (request.getAttribute("MensagemOk").toString().trim().equals("") == false){ %>
				<div class="divMensagemOk" id="MensagemOk"><%=request.getAttribute("MensagemOk").toString().trim()%></div>
			<%}%>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>