<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
   "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
   
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ArquivoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="Modelodt" scope="session" class= "br.gov.go.tj.projudi.dt.ModeloDt"/>

<html>
	<head>
		<title>Modelos</title>
		<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />
				
    	<style type="text/css">
    		@import url('./css/Principal.css');
    		@import url('./css/Paginacao.css');
    		@import url('./css/menusimples.css');
    	</style>
    	      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
		<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>
		<script type='text/javascript' src='./js/jquery.js'></script>
   		<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script> 
   		<script type='text/javascript' src='./js/ckeditor/ckeditor.js?v=24092018'></script>   		
				
		<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
		<script type="text/javascript">
			function VerificarCampos() {
				if (document.Formulario.PaginaAtual.value == <%=Configuracao.SalvarResultado%>){
					with(document.Formulario) {
						if (SeNulo(Modelo, "O Campo Descrição é obrigatório!")) return false;
						submit();
					}
				}
			}
		</script>
		<%}%>
	</head>	
	<body onload="redimensionar();">
  		<div id="divCorpo" class="divCorpo" >
			<div class="area"><h2>&raquo; Cadastro de Modelo</h2></div>
			
			<% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>
  			<form action="Modelo" method="post" name="Formulario" id="Formulario" onsubmit="JavaScript:return VerificarCampos()">
			<%} else {%>
			<form action="Modelo" method="post" name="Formulario" id="Formulario">
			<%}%>
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				
				<div id="divPortaBotoes" class="divPortaBotoes">
      				<%@ include file="Padroes/Botoes.jspf"%>
      				      
      				<input id="imgEditar" class="imgEditar" title="Copiar Modelo" name="imgEditar" type="image" src="./imagens/imgEditar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Curinga6)%>')" />
      				<a class="divPortaBotoesLink" href="Ajuda/ModeloAjuda.html" target="_blank">
      					<img id="imgAjuda" class="imgAjuda" title="Ajuda" src="./imagens/imgAjuda.png" alt="Ajuda" />
      				</a>
  				</div>
  				
				<div id="divEditar" class="divEditar"  style="min-height:750px">
					<!-- <fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Cadastro de Modelo </legend> -->
<div class="space"></div>
<div class="col40">
						<label class="formEdicaoLabel formEdicaoLabelPequeno">*Tipo de Arquivo
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarArquivoTipo" name="imaLocalizarArquivoTipo" type="image"  src="./imagens/imgLocalizarPequena.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ArquivoTipoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" />
						</label><br>  
						  
						<input class="formEdicaoInputSomenteLeitura"  readonly name="ArquivoTipo" id="ArquivoTipo" type="text" size="40" maxlength="255" value="<%=Modelodt.getArquivoTipo()%>" />
					
					</div>
					<div class="col50">
						<label  class="formEdicaoLabel formEdicaoLabelPequeno" for="Modelo">*Descrição</label><br> 
						<input class="formEdicaoInput" name="Modelo" id="Modelo"  type="text" size="64" maxlength="60" value="<%=Modelodt.getModelo()%>" onkeyup=" autoTab(this,60)"/><br />
						</div>
										
<!-- -------alteração locomoção----------------------------- -->		              
                    
                   <%  
                   String grupo = "";
				   String serventia = "";
				   if (request.getAttribute("serventia") != null)
					   serventia = (String) request.getAttribute("serventia");					
				   if (request.getAttribute("grupo") != null)
					    grupo = (String) request.getAttribute("grupo");					   
				   
				   if (serventia.equalsIgnoreCase(ServentiaDt.GERENCIAMENTO_SISTEMA_PRODUDI) 
    				  && grupo.equalsIgnoreCase(Integer.toString(GrupoDt.GERENCIAMENTO_TABELAS))) {                                                                        
        		        
        		   %>         		                   
                   <div id="divEditar" class="divEditar" style="min-height: 750px">
				   <div class="space"></div>
				   <%
				   if (Modelodt.getId_ArquivoTipo().equalsIgnoreCase(Integer.toString(ArquivoTipoDt.MANDADO))) {
				   %>
				   <div class="col50">
				   <label class="formEdicaoLabel formEdicaoLabelPequeno"
				   for="Modelo">*Locomoção</label><br>
				   <select id="qtdLocomocao" name="qtdLocomocao" class="formEdicaoCombo">
				   <%
					  String qtdLocomocao = "";
					  if (request.getAttribute("qtdLocomocao") != null)
					     qtdLocomocao = (String) request.getAttribute("qtdLocomocao");
					  String selected = "";
					  for (int i = 1; i < 6; i++) {
				   %>
				   <option value="<%=i%>"
				   <%
				        if (qtdLocomocao != ""  &&  i == Integer.parseInt(qtdLocomocao)) {%>
				   selected="selected"   
				   <%}%>>
				   <%=i%>
                   </option>
				   <%}%>
				   </select>
				   </div> 
				   <%}}%>
				    											
						
<!--  ---------------fim------------------------------------ -->					
						
					<div class="space"></div>
					<div id="ForaEditor" class="ForaEditor">
							<div id="menuEspecial" class="menuEspecial" style="margin-top:15px">  <jsp:include page="variaveis.jsp" />  	</div> 
					</div>	
					<div class="clear"></div>		        		
					<div id="Editor" class="Editor">
					             			
						<textarea class="ckeditor" cols="80" id="editor1" name="Texto" rows="15"><%=Modelodt.getTexto()%></textarea>

					</div>
							
					<!-- </fieldset> -->
					<%if (request.getAttribute("MensagemErro").toString().trim().equals("") == true){ %>
						<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
					<% } %>					
				</div>
				
			</form>
				
			<%@include file="Padroes/Mensagens.jspf"%>	
				
		</div>
		
	</body>
</html>