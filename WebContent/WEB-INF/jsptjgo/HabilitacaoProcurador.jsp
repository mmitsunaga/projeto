<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page  import="java.util.List"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.CidadeDt"%>
<%@page import="br.gov.go.tj.projudi.dt.RgOrgaoExpedidorDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>



<%@page import="java.util.Enumeration"%>

<jsp:useBean id="Advogadodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>
<jsp:useBean id="UsuarioServentiaOabdt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioServentiaOabDt"/>
<jsp:useBean id="Enderecodt" scope="session" class= "br.gov.go.tj.projudi.dt.EnderecoDt"/>




<jsp:useBean id="UsuarioServentiaGrupodt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"/>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"%>

<html>
<head>
	<title>Habilitação de Promotor</title>

      <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
			@import url('js/jscalendar/dhtmlgoodies_calendar.css?random=20051112');
		</style>
      
      
      <script type='text/javascript' src='./js/Funcoes.js'></script>
      <script type='text/javascript' src='./js/jquery.js'></script>
      <script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>  
	  <script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
	  <script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>
	  <script type="text/javascript" src="./js/jscalendar/dhtmlgoodies_calendar.js?random=20060118"></script>
	  <script type="text/javascript" src="./js/Digitacao/DigitarData.js"></script>	
</head>

<body>
	<div id="divCorpo" class="divCorpo">
  		<div class="area"><h2>&raquo; Habilitação de Procurador </h2></div>
		
		<form action="Advogado" method="post" name="Formulario" id="Formulario" <% if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)))  {%>OnSubmit="JavaScript:return VerificarCampos()"<%}%>>
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>"/>
			<input id="PaginaEspcfc" name="PaginaEspcfc" type="hidden" value="HabilitacaoProcurador"/>
			<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>">
			<input id="Curinga" name="Curinga" type="hidden" value="<%=request.getAttribute("Curinga")%>">
			<input id="Passo" name="Passo" type="hidden" value="<%=request.getAttribute("Passo")%>">
			<input type="hidden" id="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>" name="<%=request.getAttribute("tempBuscaId_Usuario").toString()%>">
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>"/>
			  
		    <div id="divEditar" class="divEditar">
		  		<fieldset class="formEdicao"> 
			    	<legend class="formEdicaoLegenda">Habilitar Procurador</legend>
			    	
			    	<!-- OAB's do Advogado -->
			    	<fieldset>
			    	
			    	<legend class="formEdicaoLegenda">Procurador</legend>
				    	<div class="col45">
				    	<label class="formEdicaoLabel" for="usuario">*Nome</label>
				    	<input id="serventiaCodigoHddn" name="serventiaCodigoHddn" type="hidden" value="<%=((UsuarioDt) session.getAttribute("UsuarioSessaoDt")).getServentiaCodigo()%>">
				    	<input id="Id_UsuarioServentia" name="Id_UsuarioServentia" type="hidden" value="<%=Advogadodt.getId_UsuarioServentia()%>">
				    	<input id="Id_Usuario" name="Id_Usuario" type="hidden" value=null>
				    	<input id="OabNumeroRe" name="OabNumeroRe" type="hidden" value=null>
				    	<input id="OabComplementoRe" name="OabComplementoRe" type="hidden" value=null>
						

				    	<input  class="formEdicaoInputSomenteLeitura"  readonly name="Usuario" id="Usuario" type="text" size="40" maxlength="60" value="<%=Advogadodt.getNome()%>">
				    	<div class="col15"><input id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" value="Localizar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('Passo','1')"/></div>
				    	</div>
				    	
				    	
				    	
				    	
				    	<%if (request.getParameter("Passo")==null){ %>
				    	<div class="clear"></div>			    		
			    		<div class="col15">
			    		<label class="formEdicaoLabel" for="OabNumero">*Número</label><br> <input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
			   			</div>
			   			
			   			<div class="col15">
			   			<label class="formEdicaoLabel" for="OabComplemento">*Complemento</label><br> 
			   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
				        		<option>N</option>
					       		<option>A</option>
					        	<option>B</option>
			   		        	<option>S</option>
				    	</select>
				    	</div>				    	
				    	<div class="clear"></div>				    	
				    	<div class="col15"><input align="middle" name="formLocalizarimgSalvar" type="image"  title="Habilitar Procurador" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('Passo','2')"></div>
				    	<%} %>
				    	
				    	
			    	</fieldset>
			    	<fieldset>
							<legend class="formEdicaoLegenda">Habilitações Existentes</legend>
		   					<input id="ListaServidorJudiciario" name="ListaServidorJudiciario" type="hidden" value="<%=request.getAttribute("ListaServidorJudiciario")%>">
							<div id="divTabela" class="divTabela" > 
						    	<table id="Tabela" class="Tabela">
						        	<thead>
						            	<tr class="TituloColuna">
						                	<th></td>                  
						                  	<th>Nome</th>
						                  	<th>Status</th>
						                  	<th>Opções</th>
						               	</tr>
						           	</thead>
						           	<tbody id="tabListaUsuario">
										<%
										if(request.getAttribute("listaProcuradores") != null) {
								  			List liTemp = (List) request.getAttribute("listaProcuradores");
								  			UsuarioDt objTemp;
								  			for(int i = 0 ; i< liTemp.size();i++) {
								      			objTemp = (UsuarioDt)liTemp.get(i); %>
												<tr class="TabelaLinha<%=(i%2+1)%>"  >
								                	<td><%=i+1%></td>                   
								                   	<td><%= objTemp.getNome()%></td>
								                	
								      			<%if (objTemp.getAtivo().equalsIgnoreCase("1")){ %>
							                   	<td align="center">ATIVO</td>
							                   	<%} else {%>
							                   	<td align="center"><font color="red" size="-1"><strong>INATIVO</strong></font></td>
							                   	<%}
							      				if(objTemp.getAtivo().equalsIgnoreCase("1")) {
							      				%>
							      				<td align="center"><input align="middle" name="formLocalizarimgexcluir" type="image"  title="Desabilitar Servidor na Serventia" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('Passo','3');AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>')"></td>
							     				<%} else { %>
							     				<td align="center"><input align="middle" name="formLocalizarimgEditar" type="image"  title="Habilitar Servidor na Serventia" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga7%>');AlterarValue('Passo','2');AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>');AlterarValue('Id_Usuario','<%=objTemp.getId()%>');AlterarValue('OabNumeroRe','<%=objTemp.getOabNumero()%>');AlterarValue('OabComplementoRe','<%=objTemp.getOabComplemento()%>')"></td>
							     				<%}%>
								               </tr>
											<%
											}
										}%>
						           </tbody>
		       					</table>
		   					</div> 
		   				</fieldset>
				</fieldset>
			    <%if (!request.getAttribute("Curinga").toString().equalsIgnoreCase("S")){%>
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			    <%}%>
			    </div>
  		</form>
  		<%@ include file="Padroes/Mensagens.jspf" %>
	</div>
</body>
</html>
