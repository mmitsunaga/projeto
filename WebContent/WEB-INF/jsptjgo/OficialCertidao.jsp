<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.OficialCertidaoDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ModeloDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>

<jsp:useBean id="OficialCertidaodt" scope="session" class= "br.gov.go.tj.projudi.dt.OficialCertidaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title> |<%=request.getAttribute("tempPrograma")%>| Busca de OficialCertidao  </title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type='text/javascript' src='./js/jquery.js'> </script>
	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
	<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>


	<%/*<script language="javascript" type="text/javascript" src="./js/EsconderDiv.js" ></script>*/%>
	
	<script type="text/javascript">
		function RetirarEnter(campo, event ) { 

			var tecla;		
			
			if(navigator.userAgent.indexOf('IE 5')>0 ||	navigator.userAgent.indexOf('IE 6')>0) 
				tecla= event.keyCode;
			 else tecla= event.which;      
			
			key = String.fromCharCode( tecla); 
			
			if ( tecla == 13 )  return  false;

			return true;						  
		} 
	</script>



</head>

<body onLoad="Toolbar()">
	<div id="divCorpo" class="divCorpo" >
		<div class="area"><h2>&raquo; Criação e Consulta de Certidão</h2></div>
<%-- <% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%> --%>
<!-- 		<form action="OficialCertidao" method="post" name="Formulario" id="Formulario" OnSubmit="JavaScript:return VerificarCampos()"> -->
<%-- <%} else {%> --%>
		<form action="OficialCertidao" method="post" name="Formulario" id="Formulario" >
<%-- <%}%> --%>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">
			<input id="PassoEditar" name="PassoEditar" type="hidden" value="<%=request.getAttribute("PassoEditar")%>">
			<input id="posicaoLista" name="posicaoLista" type="hidden" value="0">
			<input id="Status" name="Status" type="hidden" value="<%=OficialCertidaodt.getStatus()%>">
			
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
<!-- 			<div id="divPortaBotoes" class="divPortaBotoes"> -->
<%-- 				<%@ include file="Padroes/Botoes.jspf"%>				<a class="divPortaBotoesLink" href="Ajuda/OficialCertidaoAjuda.html" target="_blank">  <img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" /> </a> --%>
<!-- 			</div> -->
			<div id="divEditar" class="divEditar">
				<br />
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Criar uma nova certidão </legend>
					
					<input type="hidden" id="id_Modelo" name="Id_Modelo" value="<%=OficialCertidaodt.getId_Modelo()%>" />
					<div class="col60">
					<label class="formEdicaoLabel"> *Modelo de certidão 
					<input class="FormEdicaoimgLocalizar" name="imaLocalizarModelo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ModeloDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" title="Selecionar Modelo de Certidão" />
					<input class="FormEdicaoimgLocalizar" name="imaLimparModelo" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('id_Modelo', 'modelo'); return false;" title="Limpar Modelo" />
					</label><br>
					<input class="formEdicaoInputSomenteLeitura" name="Modelo" readonly type="text" size="60" maxlength="60" id="modelo" value="<%=OficialCertidaodt.getModelo()%>" onkeypress="return RetirarEnter(this, event)" />
					<br>
					</div>
					<div class="col30">
					<label class="formEdicaoLabel" for="NumeroMandadoSPG">*Número do mandado</label><br>    
				    <input class="formEdicaoInput" name="NumeroMandadoSPG" id="NumeroMandadoSPG"  type="text" size="12" maxlength="12" value="" onkeypress="return RetirarEnter(this, event)">
				    
				    </div>
				    <div class="clear space">&nbsp;</div>
				    <input name="imgConsultarSpg" type="submit" value="Consultar SPG" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('tempFluxo1','1');" onkeypress="return RetirarEnter(this, event)"/>
				</fieldset>
				<br />
				<fieldset class="formEdicao"  > 
					<legend class="formEdicaoLegenda">Dados Certidão </legend>
   					<div class="col30">
   					<label class="formEdicaoLabel" for="Id_OficialCertidao">Número da Certidão: </label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_OficialCertidao"  id="Id_OficialCertidao"  type="text"  readonly="true" value="<%=OficialCertidaodt.getId()%>" onkeypress="return RetirarEnter(this, event)"><br>
   					</div>
   					<div class="col30">
   					<label class="formEdicaoLabel" for="Id_Mandado">Mandado:  </label><br> <input class="formEdicaoInputSomenteLeitura" name="Id_Mandado"  id="Id_Mandado"  type="text"  readonly="true" value="<%=OficialCertidaodt.getNumeroMandado()%>" onkeypress="return RetirarEnter(this, event)"><br>
   					</div>
   					<div class="col30">
   					<label class="formEdicaoLabel" for="Id_Mandado">Data de emissão:  </label><br> <input class="formEdicaoInputSomenteLeitura" name="dataEmissao"  id="dataEmissao"  type="text"  readonly="true" value="<%=OficialCertidaodt.getDataEmissao()%>" onkeypress="return RetirarEnter(this, event)"><br>
   				</div>
   				</fieldset>
				<div class="space">&nbsp;</div>
				<div id="Editor" class="Editor" style="display:block" >        			
					<% if (OficialCertidaodt.getStatus().equals("1") || OficialCertidaodt.getTexto()==null || OficialCertidaodt.getTexto().equals("")) {%>
					<textarea disabled="disabled" class="ckeditor" cols="80" id="TextoEditor" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>
					<%}else{%>
						<textarea class="ckeditor" cols="80" id="TextoEditor" name="TextoEditor" rows="20"><%=request.getAttribute("TextoEditor")%></textarea>	
					<%}%>																		
				</div>
				<script type="text/javascript">
						CKEDITOR.replace( 'TextoEditor',
						{
							toolbar :
							[
								['Styles','Format','Font','FontSize'],
								//['Bold','Italic','Underline','StrikeThrough','-','Undo','Redo','-','Cut','Copy','Paste','Find','Replace','-','Outdent','Indent','-','Table', 'Print'],
								['Bold','Italic','Underline','StrikeThrough','-','Undo','Redo','-','Cut','Copy','Paste','Find','Replace','-','Outdent','Indent','-','Table'],
								['NumberedList','BulletedList','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'],
								
							]
						});
				</script>
								
					<div id="divConfirmarSalvar" class="ConfirmarSalvar">
						<% if((OficialCertidaodt.getStatus().equals("0")) && (OficialCertidaodt.getNumeroMandado()!=null && !OficialCertidaodt.getNumeroMandado().equals("")) && (OficialCertidaodt.getTexto()!=null && !OficialCertidaodt.getTexto().equals(""))){ %>
						<button type="submit" name="operacao" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');" >
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Salvar" /> -->
							Salvar
						</button>
						<%}%>
						
						<% if((OficialCertidaodt.getStatus().equals("0")) && (OficialCertidaodt.getId()!=null && !OficialCertidaodt.getId().equals(""))&& (OficialCertidaodt.getTexto()!=null && !OficialCertidaodt.getTexto().equals(""))){ %>
						<button type="submit" name="operacao" value="Excluir" onclick="confirmaExclusao('Deseja realmente excluir este documento?', '<%=Configuracao.Excluir%>', '-1','0');"/>
						<!--	<img src="imagens/22x22/ico_sucesso.png" alt="Excluir" /> -->
							Excluir
						</button>
						<%}%>
						
						<% if((OficialCertidaodt.getStatus().equals("0")) && (OficialCertidaodt.getId()!=null && !OficialCertidaodt.getId().equals(""))&& (OficialCertidaodt.getTexto()!=null && !OficialCertidaodt.getTexto().equals(""))){ %>
						<button type="submit" name="operacao" value="Finalizar" onclick="ConfirmaOperacao('Deseja realmente finalizar este documento?','<%=Configuracao.Editar%>', '2');" >
							<!-- <img src="imagens/22x22/ico_sucesso.png" alt="Finalizar" /> -->
							Finalizar
						</button>
						<%}%>
						
					</div>
					<br />	<br />
			</div>
		</form>
	</div>
	<%@ include file="Padroes/Mensagens.jspf" %>
</body>
</html>
