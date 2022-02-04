<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaCargoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.GrupoDt"%>

<jsp:useBean id="ProcessoResponsaveldt" class= "br.gov.go.tj.projudi.dt.ProcessoResponsavelDt" scope="session"/>


<%@page import="br.gov.go.tj.projudi.dt.UsuarioServentiaDt"%><html>
<head>
	<title>Troca de Responsável do Processo</title>	
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
	      
</head>

	<body>
		<div id="divCorpo" class="divCorpo">
			<div class="area"><h2>&raquo; Troca de Responsável do Processo</h2></div>
		
			<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">
				<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
				<input  name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />				
				<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />	
						
				<div id="divEditar">
					<fieldset class="formEdicao">
						<legend>Trocar Procurador/Defensor/(Advogado Escritório Jurídico) Responsável do Processo</legend>
						
						<%
							List processos = ProcessoResponsaveldt.getListaProcessos();
						%>

						<%		
						if (processos != null){
							for (int i=0;i<processos.size();i++){
								ProcessoDt processoDt = (ProcessoDt)processos.get(i);
						%>
								<fieldset class="formEdicao">
									<legend>N&uacute;mero Processo:  <a href="BuscaProcessoUsuarioExterno?Id_Processo=<%=processoDt.getId_Processo()%>&PassoBusca=2"><%=processoDt.getProcessoNumero()%></a> </legend>
									<table class="Tabela" id="TabelaArquivos">
										<thead>
											<tr>
												<th></th>
												<th>Responsável Atual</th>
												<th width="30%">OAB/Matrícula</th>
												<th>Serventia</th>
												<th></th>
											</tr>
										</thead>
										<tbody>
											<%
												List liProcessoParteAdvogado = processoDt.getListaProcessoParteAdvogado();
											    ProcessoParteAdvogadoDt objTempProcessoParteDt;
												String stTempNome="";
												for(int f = 0 ; f< liProcessoParteAdvogado.size();f++) {
													objTempProcessoParteDt = (ProcessoParteAdvogadoDt)liProcessoParteAdvogado.get(f); %>
													<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
									                   	<tr class="TabelaLinha1"> 
													<%}else{ stTempNome=""; %>    
									                   	<tr class="TabelaLinha2">
													<%}%>
									                   		<td> <%=i+1%></td>
								                    	<td>
								                    		<%= objTempProcessoParteDt.getNomeAdvogado()%>
								                    	</td>
								                   		<td align="center">
								                   			<%= objTempProcessoParteDt.getOabNumero()+ " - "+objTempProcessoParteDt.getOabComplemento()%>
								                   		</td>
								                   		<td align="center">
								                   			<%= objTempProcessoParteDt.getServentiaHabilitacao()%>
								                   		</td>
								                   		<%  boolean checked = false;
								                   			if (request.getSession().getAttribute("id_UsuarioServentiaAtual") != null 
								                   					&& request.getSession().getAttribute("id_UsuarioServentiaAtual").toString().equals(objTempProcessoParteDt.getId_UsuarioServentiaAdvogado())){ 
								                   				checked = true;
								                   			}
								                   		%> 
								                   		<td align="center">
															<input type="radio" <%=checked?"checked":"" %> name="id_UsuarioServentiaAtual" value="<%=objTempProcessoParteDt.getId_UsuarioServentiaAdvogado()%>" />
														</td>
								                   		</tr>
											<%}%>
										</tbody>
										<% 	}
								} else { %>
								<tbody>
									<tr>
										<td><em>Selecione processo(s) para Troca de Responsável.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>		
						</fieldset>	
						<br />
						<fieldset class="formEdicao">
							<legend>Novo Responsável</legend>
							
							<label class="formEdicaoLabel" for="responsavel">*Novo Responsável 
			    			<input class="FormEdicaoimgLocalizar" id="imaLocalizarId_UsuarioServentia" name="imaLocalizarId_ServentiaCargo" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(UsuarioServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>')" >  
			    			</label><br> 
			    			<input class="formEdicaoInputSomenteLeitura"  readonly name="UsuarioServentia" id="UsuarioServentia" type="text" size="80" maxlength="100" value="<%=ProcessoResponsaveldt.getUsuarioServentia()%>"/><br />
			    		</fieldset>
				    		
						<div id="divBotoesCentralizados" class="divBotoesCentralizados">
							<input name="imgConcluir" type="submit" value="Concluir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar%>');"> 
						</div>
					</fieldset>
					
					<%@ include file="Padroes/ConfirmarOperacao.jspf"%>
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
 		</div>
	</body>
</html>