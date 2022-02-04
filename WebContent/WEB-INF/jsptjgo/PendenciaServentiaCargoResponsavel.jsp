<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PendenciaResponsavelDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.ne.UsuarioNe"%>

<jsp:useBean id="PendenciaResponsaveldt" class= "br.gov.go.tj.projudi.dt.PendenciaResponsavelDt" scope="session"/>
<jsp:useBean id="Pendenciadt" class= "br.gov.go.tj.projudi.dt.PendenciaDt" scope="session"/>

<html>
<head>
	<title>Troca de Responsável da Conclusão</title>	
    <meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
	<style type="text/css">
		@import url('./css/Principal.css');
		@import url('./css/Paginacao.css');
	</style>
    
    	  
    <script type='text/javascript' src='./js/Funcoes.js'></script>
    <script type='text/javascript' src='./js/jquery.js'></script>
   	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
	<!--<script type="text/javascript" src="./js/ui/jquery.tabs.min.js"></script>-->
	<link rel='stylesheet' href='./css/jquery.tabs.css' type='text/css' media='print, projection, screen'>
	<script type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>	
	
	<script language="javascript" type="text/javascript">
		<%if(request.getSession().getAttribute("TrocouResponsavel") == null || 
			 request.getSession().getAttribute("TrocouResponsavel").toString().trim().toUpperCase() != "S"){%>
		var podeAlterarServentia = true;
	    <% } else { %>
	    var podeAlterarServentia = false;
	    <% } %>
	    
			function acaoTela(check){		
				if (check.checked){
					AlterarValue('novoResponsavel', 'true');
					document.Formulario.id_UsuarioServentiaCargo.checked = false;
					document.Formulario.id_UsuarioServentiaCargo.disabled = true;
				}else{
					AlterarValue('novoResponsavel', 'false');
					document.Formulario.id_UsuarioServentiaCargo.disabled = false;
				}
			}	
			
			function acaoSelecaoResponsavel(radio){
				if (radio.checked) {
					
					var linhaTabelaTR = radio.parentNode.parentNode; //tr
					
					var idServentia = $(linhaTabelaTR).find('.idServentiaTable').html();
					var serventia = $(linhaTabelaTR).find('.serventiaTable').html();
					
					if (possuiValor(idServentia) &&
					    possuiValor(serventia)) {
						
						if (!podeAlterarServentia) {
							
							
							$('#Id_ServentiaCargo').val('');
							$('#ServentiaCargo').val('');
						} else {
							if (!possuiValor($('#Serventia').val()) &&
							    (!possuiValor($('#Id_ServentiaCargo').val()) ||
								 !possuiValor($('#ServentiaCargo').val()))) {										
								$('#Id_Serventia').val(idServentia);
								$('#Serventia').val(serventia);			
							}							
						}						
					}
				}				
			}	
			
			function possuiValor(campo) {
				if (campo == null) {
				 	return false;
				} else if (campo == '') {
					return false;
				} else if (campo == 'null') {
					return false;
				} else {
					return true;
				}				
			}
		</script>	      
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Definir ou Trocar o Responsável da Conclusão</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
				<input id="novoResponsavel" name="novoResponsavel" type="hidden" value="<%=request.getAttribute("novoResponsavel")%>" />
						
				<div id="divEditar" class="divEditar">
					<%
					UsuarioNe UsuarioSessao = (UsuarioNe) request.getSession().getAttribute("UsuarioSessao");
					%>
				
					<fieldset class="formEdicao">
						<legend>Definir ou Trocar Responsável</legend>
						<fieldset class="formEdicao">
							<legend><%="Pendência:"+Pendenciadt.getId()+" - ("+Pendenciadt.getPendencia()+")"%>; Processo N&uacute;mero:  <a href="BuscaProcesso?Id_Processo=<%=Pendenciadt.getId_Processo()%>&PassoBusca=2"><%=Pendenciadt.getProcessoNumero()%></a> </legend>
							<table class="Tabela" id="TabelaArquivos">
								<thead>
									<tr>
										<th></th>
										<th width="30%">Cargo</th>
										<th width="0%">IdServentia</th>
										<th width="20%">Serventia</th>
										<th width="50%" colspan="2">Responsável</th>
										<th></th>
										<%if(UsuarioSessao.getVerificaPermissao(3940)){ %>
											<th>Remover</th>
										<%}%>
									</tr>
								</thead>							
								<tbody>
									<%
										List liResponsaveis = Pendenciadt.getResponsaveis();
										PendenciaResponsavelDt objTempResponsavel;
										String stTempNome="";
										
										for(int f = 0 ; liResponsaveis != null && f< liResponsaveis.size();f++) {
											objTempResponsavel = (PendenciaResponsavelDt)liResponsaveis.get(f); %>
											<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
							                   	<tr class="TabelaLinha1"> 
											<%}else{ stTempNome=""; %>    
							                   	<tr class="TabelaLinha2">
											<%}%>
							                   	<td> <%=f+1%></td>
							                <%if (objTempResponsavel.getServentiaCargo() != null && objTempResponsavel.getServentiaCargo().length()>0) {%>
												<td><%=objTempResponsavel.getServentiaCargo()%></td>
												<td class="idServentiaTable" align="center"><%=objTempResponsavel.getId_Serventia()%></td>
												<td class="serventiaTable" align="center"><%=objTempResponsavel.getServentia()%></td>
											<% } else {%>
													<td align="center">---</td>
													<td class="idServentiaTable" align="center"><%=UsuarioSessao.getUsuarioDt().getId_Serventia()%></td>
													<td class="serventiaTable" align="center"><%=UsuarioSessao.getUsuarioDt().getServentia()%></td>
											<%} %>
						                    	
						                    <%if (objTempResponsavel.getUsuarioResponsavel() != null && objTempResponsavel.getUsuarioResponsavel().length()>0) {%>
												<td><%=objTempResponsavel.getUsuarioResponsavel()%></td>
												<td><%=objTempResponsavel.getNomeUsuarioServentiaCargo()%></td>
											<% } else {%>
													<td align="center">---</td>
													<td align="center">---</td>
											<%} %>
						                   	<%  boolean checked = false;
						                   		if (request.getSession().getAttribute("id_UsuarioServentiaCargoAtural") != null 
						                   				&& request.getSession().getAttribute("id_UsuarioServentiaCargoAtural").toString().equals(objTempResponsavel.getId_ServentiaCargo())){ 
						                   			checked = true;
						                   		}
						                   	%> 
						                   		<td align="center">
													<input type="radio"  <%if(PendenciaResponsaveldt.isNovoResponsavel()){%>disabled="true"<%}%> id="id_UsuarioServentiaCargo" <%=checked?"checked":"" %> name="id_UsuarioServentiaCargo" value="<%=objTempResponsavel.getId_ServentiaCargo()%>" onchange="acaoSelecaoResponsavel(this)" />
												</td>
												<%if(UsuarioSessao.getVerificaPermissao(3940)){ %>
												<td align='center'>
													<img alt="Remover Responsável" src="./imagens/16x16/edit-clear.png" onclick="window.open('<%=request.getAttribute("tempRetorno")%>?PaginaAtual=<%=Configuracao.Excluir%>&id_pend_resp=<%=objTempResponsavel.getId()%>','_self');">
												</td>
												<%}%>
						                   		</tr>
										<%}%>
								</tbody>
							</table>
							<%if(liResponsaveis != null && liResponsaveis.size()==1 &&
								 (request.getSession().getAttribute("assistenteGab") == null 
	                   			  || !request.getSession().getAttribute("assistenteGab").toString().equals("true"))) {%>
								<input type="checkbox" class="formEdicaoInput" onchange="acaoTela(this)" id="novoResponsavel" value="Novo Responsável" title="Em Sustituição" <%if(PendenciaResponsaveldt.isNovoResponsavel()){%> checked="true" <%}%>/> Novo Responsável (Assistente ou Distribuidor)
								<br />
							<%}	%>	
						</fieldset>
						<%if(request.getSession().getAttribute("TrocouResponsavel") == null || 
							 request.getSession().getAttribute("TrocouResponsavel").toString().trim().toUpperCase() != "S"){%>
							<br />
							<fieldset class="formEdicao">
								<legend>Novo Responsável</legend>
								
								<label class="formEdicaoLabel">Serventia								
								<%								
								
								if (request.getSession().getAttribute("PermiteTrocarDeServentia") == null ||
									(boolean)request.getSession().getAttribute("PermiteTrocarDeServentia")) {
									if(request.getSession().getAttribute("assistenteGab") == null 
		                   			   || !request.getSession().getAttribute("assistenteGab").toString().equals("true")) {%>
		                   			   <%if(!(UsuarioSessao.getUsuarioDt().getGrupoCodigoToInt() == GrupoDt.DISTRIBUIDOR_GABINETE)){ %>
											<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png" 
												onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>'); " 
															title="Selecione a serventia" />
											<input class="FormEdicaoimgLocalizar" name="imaLimparServentia" type="image"  src="./imagens/16x16/edit-clear.png"
												onclick="LimparChaveEstrangeiraCampoRelacionado('Id_Serventia', 'Serventia','Id_ServentiaCargo', 'ServentiaCargo'); return false;" title="Limpar a serventia" />
										<%}	%>
									<%}	%>
								<% } else { %>
									&nbsp[Tipo de pendência sem permissão para trocar de serventia]
								<%}	%>								 
								</label><br>
								<input type="hidden" id="Id_Serventia" name="Id_Serventia" value="<%=request.getAttribute("Id_Serventia")%>" />
								<input class="formEdicaoInputSomenteLeitura" id="Serventia" name="Serventia" readonly="true" type="text" size="50" maxlength="50" value="<%=request.getAttribute("Serventia")%>" />	
								<br />
								
								<input type="hidden" id="Id_ServentiaCargo" name="Id_ServentiaCargo" value="<%=request.getAttribute("Id_ServentiaCargo")%>" />
								
								<label class="formEdicaoLabel">Cargo da Serventia
								<input class="FormEdicaoimgLocalizar" name="imaLocalizarServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png" 
									onclick="AlterarValue('PaginaAtual', '<%=String.valueOf(ServentiaCargoDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');" 
												title="Selecione o cargo da serventia" />
								
								<input class="FormEdicaoimgLocalizar" name="imaLimparServentiaCargo" type="image"  src="./imagens/16x16/edit-clear.png"
									onclick="LimparChaveEstrangeira('Id_ServentiaCargo', 'ServentiaCargo'); return false;" 	title="Limpar o cargo da serventia" />
								</label><br>
								<input class="formEdicaoInputSomenteLeitura" name="ServentiaCargo" readonly type="text" size="50" maxlength="50" id="ServentiaCargo" value="<%=request.getAttribute("ServentiaCargo")%>" />	
							
				    		</fieldset>
					    	<%if (!request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar)) && !request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {%>	
							<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
							</div>
							<%}%>
						<%}%>
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>