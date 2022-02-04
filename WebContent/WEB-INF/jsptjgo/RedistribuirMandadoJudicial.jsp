<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page  import="br.gov.go.tj.utils.Configuracao"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page  import="br.gov.go.tj.projudi.dt.MandadoTipoRedistribuicaoDt"%>
<%@page import="java.util.*"%>

<jsp:useBean id="Pendenciadt" scope="session" class= "br.gov.go.tj.projudi.dt.PendenciaDt"/>
<jsp:useBean id="MandadoJudicialdt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoJudicialDt"/>
<jsp:useBean id="MandadoTipoRedistribuicaoDt" scope="session" class= "br.gov.go.tj.projudi.dt.MandadoTipoRedistribuicaoDt"/>

<html>
<head>
	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE"/>
	<title>Redistribuir Mandado</title>
	<link href="./css/Principal.css"  type="text/css"  rel="stylesheet" />	<script type="text/javascript" src="js/jquery.js"> </script>
	<script type='text/javascript' src='js/ui/jquery-ui.min.js'></script>  
	<script type='text/javascript' src='./js/Funcoes.js'></script>
	<script type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js" ></script>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js" ></script>   
	<script type='text/javascript' src='js/checks.js'></script>  
      
</head>
<body>
  <div  id="divCorpo" class="divCorpo">
	<div id="divEditar" class="divEditar">
		<fieldset class="fieldEdicaoEscuro">
			<legend>Redistribuir Mandado</legend>
			
			<form action="MandadoJudicial" method="post" name="Formulario" id="Formulario">
			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
			<input name="Fluxo" id="Fluxo" type="hidden" value="<%=request.getAttribute("Fluxo")%>" />
			<fieldset class="formLocalizar">
				<legend>Consulta</legend>
					<label class="formEdicaoLabel">Número do Mandado:</label><br />
					<input class="formEdicaoInput" name="NumeroMandado" id="NumeroMandado" type="text" value="<%=request.getAttribute("NumeroMandado")==null?"":request.getAttribute("NumeroMandado")%>" onkeypress="return DigitarSoNumero(this, event)" /><br />
				
					<label class="formEdicaoLabel">Oficial:
					<input class="FormEdicaoimgLocalizar" id="imaLocalizarOficial" name="imaLocalizarOficial" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('Fluxo','redistribuir')" >
					<input class="FormEdicaoimgLocalizar" name="imaLimparOficial" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_UsuarioServentia_2', 'UsuarioServentia_2'); return false;" title="Limpar Oficial">
					</label><br />
					<input type="hidden" id="Id_UsuarioServentia_2" name="Id_UsuarioServentia_2" value="<%=MandadoJudicialdt.getId_UsuarioServentia_2()%>" />
					<input class="formEdicaoInputSomenteLeitura" name="UsuarioServentia_2"  id="UsuarioServentia_2"  type="text" size="60" value="<%=MandadoJudicialdt.getUsuarioServentiaDt_2().getNome()%>" readonly/><br />
	
<!-- 					<label class="formEdicaoLabel">Direcionar para: -->
<%-- 					<input class="FormEdicaoimgLocalizar" id="imaLocalizarOficial" name="imaLocalizarOficial" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('Fluxo','redistribuir')" > --%>
<!-- 					<input class="FormEdicaoimgLocalizar" name="imaLimparOficial" type="image"  src="./imagens/16x16/edit-clear.png" onclick="LimparChaveEstrangeira('Id_UsuarioServentia_2', 'UsuarioServentia_2'); return false;" title="Limpar Oficial"> -->
<!-- 					</label><br /> -->
<%-- 					<input type="hidden" id="idOficialDirecionado" name="idOficialDirecionado" value="<%=request.getAttribute("idOficialDirecionado")%>" /> --%>
<%-- 					<input class="formEdicaoInputSomenteLeitura" name="oficialDirecionado"  id="oficialDirecionado"  type="text" size="60" value="<%=request.getAttribute("oficialDirecionado")%>" readonly/><br /> --%>
					
					
			 	   	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				   	<button type="submit" name="consultar" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('Fluxo','consultar')">Consultar</button>
				   	</div>						   	

                    <label class="formEdicaoLabel" for="tipoArquivo">*Motivo
							</label><br> <select id="idMandTipoRedist"
							name="idMandTipoRedist" class="formEdicaoCombo">
							<!-- 	<option value=""></option>  -->
							<%
								List listTemp = (List) request.getAttribute("listaTipo");
									if (listTemp != null) {
										for (int i = 0; i < listTemp.size(); i++) {
											MandadoTipoRedistribuicaoDt = (MandadoTipoRedistribuicaoDt) listTemp.get(i);
							%>
							<option value="<%=MandadoTipoRedistribuicaoDt.getIdMandTipoRedist()%>"
								<%if (MandadoTipoRedistribuicaoDt.getIdMandTipoRedist() != null
								&& MandadoTipoRedistribuicaoDt.getIdMandTipoRedist().equals(request.getAttribute("idMandTipoRedist"))) {%>
								selected="selected" <%}%>>
							
							<%=MandadoTipoRedistribuicaoDt.getMandTipoRedist()%>
							</option>
							<%
								}
									}
							%>
						</select><br>
						
						
		 
			
				    <br><br>   
				 					
					<label>Redistribuir este mandado para a escala de plantão? 
<!-- 						<input type="checkbox" name="redistribuirPlantao"> -->
						<input type="radio" name="redistribuirPlantao" value="sim" <%= "sim".equals(request.getAttribute("redistribuirPlantao")) ? "checked":"" %>> Sim
						&nbsp;&nbsp;
  						<input type="radio" name="redistribuirPlantao" value="nao" <%= !"sim".equals(request.getAttribute("redistribuirPlantao")) ? "checked":"" %>> Não 
					</label>
					
					<div class="divClear"></div>
				   	
					<div id="divTabela" class="divTabela"> 
					<%if(request.getAttribute("ListaMandados") != null) {%> 
				    	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna">
				            		<td width="5" class="Centralizado"><input type="checkbox" id="chkSelTodos" value="" onclick="atualizarChecks(this, 'divTabela')" title="Alterar os estados de todos os itens da lista" /></td>
				                  	<td>Número</td>
				                  	<td>Oficial</td>
				                  	<td width="150px">Data Distribuição</td>
				               	</tr>
				           	</thead>
				           	<tbody id="tabListaMandado">
								<%
								List liTemp = (List)request.getAttribute("ListaMandados");
								MandadoJudicialDt mandadoDt;
								String processoNumero="";
								boolean boLinha=false; 
								if(liTemp!=null)
								for(int i = 0 ; i< liTemp.size();i++) {
									mandadoDt = (MandadoJudicialDt)liTemp.get(i); 
								%> 
								<tr class="TabelaLinha<%=(boLinha?1:2)%>"> 
									<td class="Centralizado"><input class="formEdicaoCheckBox" name="mandados" type="checkbox" value="<%=mandadoDt.getId()%>"></td>
				                   	<td><%=mandadoDt.getId()%></td>
	                	            <td><%=mandadoDt.getUsuarioServentiaDt_1().getNome()%></td>
								  	<td><%=mandadoDt.getDataDistribuicao()%></td>
			               		</tr>
								<%}%>
				           	</tbody>
				       	</table>
				       	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
				   			<button type="submit" name="redistribuir" value="Redistribuir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Curinga6%>');AlterarValue('Fluxo','redistribuir')">Redistribuir</button>
				   		</div>
				    <%}%>
				    
   						   	
				   	</div> 
				</fieldset>
				
			<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
			</form>
		</fieldset>
	</div>
 </div>
<%@ include file="Padroes/Mensagens.jspf"%>
</body>
</html>
