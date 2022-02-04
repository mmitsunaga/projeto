<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<jsp:useBean id="usuarioDt" scope="session" class= "br.gov.go.tj.projudi.dt.UsuarioDt"/>

<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaGrupoDt"%>
<html>
<head>
	<title>Habilitação de Advogado</title>

    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>	
    
    
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Habilitação de Advogado</h2></div>
			
			<form action="UsuarioServentiaOab" method="post" name="Formulario" id="Formulario">
			
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
				<input type="hidden" id="Id_UsuarioServentia" name="Id_UsuarioServentia">
	
				<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao">
						<legend class="formEdicaoLegenda">Habilitação/Desabilitação de Advogado</legend> 
						
						<br />
						<label class="formEdicaoLabel" for="serventia">*Usuário
						<input class="FormEdicaoimgLocalizar" id="imaLocalizarUsuario" name="imaLocalizarUsuario" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
						</label><br>  
						
						<input class="formEdicaoInputSomenteLeitura" readonly name="Usuario" id="Usuario" type="text" size="70" maxlength="80" value="<%=usuarioDt.getUsuario()%>">
					    <br /><br />
						
						<fieldset>
							<legend class="formEdicaoLegenda">Serventia/OAB</legend> 
							
				    		<div class="col15">
				    		<label class="formEdicaoLabel" for="OabNumero">*OAB/Matrícula</label><br> 
				    		<input class="formEdicaoInput" name="OabNumero" id="OabNumero"  type="text" size="12" maxlength="11" value="<%=usuarioDt.getUsuarioServentiaOab().getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" onkeyup=" autoTab(this,11)">
				    		</div>
				    		
				    		<div class="col15">
				   			<label class="formEdicaoLabel" for="OabComplemento">*Complemento</label><br> 
				   			<select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo">
				        		<option>N</option>
					       		<option>A</option>
					        	<option>B</option>
			   		        	<option>S</option>
				    	    	<option selected><%=usuarioDt.getUsuarioServentiaOab().getOabComplemento()%></option/>
					    	</select>
					    	</div>
						    
						    <div class="col45">
							<label class="formEdicaoLabel" for="serventia">*Serventia
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
							</label><br>  
							
							<input class="formEdicaoInputSomenteLeitura"  readonly name="Serventia" id="Serventia" type="text" size="48" maxlength="60" value="<%=usuarioDt.getServentia()%>">
							</div>

							<div class="col15 clear space">
					   		<input id="imgsalvar" class="imgsalvar" title="Salvar Serventia e Grupo" name="imasalvar" type="image"  src="./imagens/imgSalvar.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.Salvar)%>');" >
						    </div>
					    </fieldset>
						
				      	<fieldset>
							<legend class="formEdicaoLegenda">Habilitações Existentes</legend>
		   					<input id="ListaServidorJudiciario" name="ListaServidorJudiciario" type="hidden" value="<%=request.getAttribute("ListaServidorJudiciario")%>">
							<div id="divTabela" class="divTabela" > 
						    	<table id="Tabela" class="Tabela">
						        	<thead>
						            	<tr class="TituloColuna">
						                	<th></th>                  
						                  	<th>Serventia (OAB)</th>                
                  							<th>OAB/Matrícula</th>
                  							<th>Status</th>
						                  	<th>Opções</th>
						               	</tr>
						           	</thead>
						           	<tbody id="tabListaUsuario">
										<%
							  			List liTemp = usuarioDt.getListaUsuarioServentias();
										UsuarioDt objTemp;
							  			boolean boLinha = false;
							  			if (liTemp != null){
								  			for(int i = 0 ; i< liTemp.size();i++) {
								      			objTemp = (UsuarioDt)liTemp.get(i); %>
												<tr class="TabelaLinha<%=(boLinha?1:2)%>"  >
								                	<td><%=i+1%></td>                   
								                   	<td><%= objTemp.getServentia()+"-"+objTemp.getServentiaUf()%></td>        
								                   	<td><%= objTemp.getUsuarioServentiaOab().getOabNumero()+"-"+objTemp.getUsuarioServentiaOab().getOabComplemento()%></td>       
	                   								<%if (objTemp.getUsuarioServentiaAtivo().equalsIgnoreCase("true")){ %>
								                   	<td>ATIVO</td>
								                   	<%} else {%>
								                   	<td> <font color="red" size="-1"><strong>INATIVO</strong></font></td>
								                   	<%} 
								      				if(objTemp.getUsuarioServentiaAtivo().equalsIgnoreCase("true")) {%>
								      				<td class="Centralizado"><input name="formLocalizarimgexcluir" type="image"  title="Desabilitar Servidor na Serventia" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>');AlterarValue('Serventia','<%=objTemp.getServentia()%>');AlterarValue('OabNumero','<%=objTemp.getUsuarioServentiaOab().getOabNumero()%>');"></td>
								     				<%} else { %>
								     				<td class="Centralizado"><input name="formLocalizarimgEditar" type="image"  title="Habilitar Servidor na Serventia" src="./imagens/imgRestaurarPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');AlterarValue('Id_UsuarioServentia','<%=objTemp.getId_UsuarioServentia()%>');AlterarValue('Serventia','<%=objTemp.getServentia()%>');AlterarValue('OabNumero','<%=objTemp.getUsuarioServentiaOab().getOabNumero()%>');"></td>
								     				<%}%>
								               </tr>
											<%
												boLinha = !boLinha;
											}
										}%>
						           </tbody>
		       					</table>
		   					</div> 
		   				</fieldset>
			    	</fieldset>
			    	<%@ include file="Padroes/ConfirmarOperacao.jspf"%>    
			    </div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
	</body>
</html>