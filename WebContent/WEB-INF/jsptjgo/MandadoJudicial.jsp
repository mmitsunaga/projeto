<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>

<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<jsp:useBean id="MandadoJudicialdt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoJudicialDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title>Mandado</title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="js/jquery.js"> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>   
      
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
	<div id="divEditar" class="divEditar">
		<fieldset class="fieldEdicaoEscuro">
			<legend>Inserir Oficial Companheiro</legend>
			
			<form action="MandadoJudicial" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input name="Fluxo" id="Fluxo" type="hidden" value="<%=request.getAttribute("Fluxo")%>" />
			<div id="divPortaBotoes" class="divPortaBotoes">
				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/MandadoJudicialAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a>
				<input id="imgsalvar" alt="Salvar" class="imgsalvar" title="Salvar - Salva os dados digitados" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');AlterarValue('Fluxo','salvarOficial')" />
			</div>
			<fieldset class="formLocalizar">
				<legend>Dados do Mandado</legend>
					<label class="formEdicaoLabel">N&uacute;mero:
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarMandado" name="imaLocalizarMandado" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga7)%>')" >
					</label><br />
					<input class="formEdicaoInputSomenteLeitura" name="NumeroMandado" id="NumeroMandado" type="text" value="<%=MandadoJudicialdt.getId()%>" onkeypress="return DigitarSoNumero(this, event)" /><br />
				
					<%if (Pendenciadt!= null  && Pendenciadt.getId() != null && !Pendenciadt.getId().equals("")){ %>
																		
							<label class="formEdicaoLabel">Movimenta&ccedil;&atilde;o:</label><br><%=Pendenciadt.getMovimentacao()%><br />
							
							<%if (Pendenciadt.getNomeParte() != null && !Pendenciadt.getNomeParte().trim().equals("")){%>
								<label class="formEdicaoLabel">Parte:</label><br><%=Pendenciadt.getNomeParte()%><br />
							<%}%>
						
							<label class="formEdicaoLabel">Tipo de Pend&ecirc;ncia:</label><br> <%=Pendenciadt.getPendenciaTipo()%><br />
							<label class="formEdicaoLabel">Status:</label><br> <%=Pendenciadt.getPendenciaStatus()%><br />
							<label class="formEdicaoLabel">Data Inicio:</label><br> <%=Pendenciadt.getDataInicio()%><br />
							<label class="formEdicaoLabel">Data Fim:</label><br> <%=Pendenciadt.getDataFim()%><br />
							<%if (Pendenciadt.getPrazo() != null && !Pendenciadt.getPrazo().equals("")) {%>
								<label class="formEdicaoLabel">Prazo:</label><br> <%=Pendenciadt.getPrazo()+ "  Dia(s)"%><br />
							<%} %>
							<%if (Pendenciadt.getDataLimite() != null && !Pendenciadt.getDataLimite().equals("") ){%>
								<label class="formEdicaoLabel">Data Limite:</label><br> <%=Pendenciadt.getDataLimite()%><br />
							<%}%>
							<br />
							
							<label class="formEdicaoLabel">Oficial:</label><br>
							<input type="hidden" id="Id_UsuarioServentia" name="Id_UsuarioServentia" value="<%=MandadoJudicialdt.getId_UsuarioServentia_1()%>" />
							<input class="formEdicaoInputSomenteLeitura" name="UsuarioServentia"  id="UsuarioServentia"  type="text" size="60" value="<%=MandadoJudicialdt.getNomeUsuarioServentia_1()%>" readonly/><br />
							
							<label class="formEdicaoLabel">Oficial Companheiro:
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarOficial" name="imaLocalizarOficial" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >
							<input class="FormEdicaoimgLocalizar" name="imaLimparOficial" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_UsuarioServentia_2', 'UsuarioServentia_2'); return false;" title="Limpar Oficial">
							</label><br />
							<input type="hidden" id="Id_UsuarioServentia_2" name="Id_UsuarioServentia_2" value="<%=MandadoJudicialdt.getId_UsuarioServentia_2()%>" />
							<input class="formEdicaoInputSomenteLeitura" name="UsuarioServentia_2"  id="UsuarioServentia_2"  type="text" size="60" value="<%=MandadoJudicialdt.getNomeUsuarioServentia_2()%>" readonly/><br />
				
						<%}%>
				</fieldset>
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>
		</fieldset>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
 </div>
</body>
</html>
