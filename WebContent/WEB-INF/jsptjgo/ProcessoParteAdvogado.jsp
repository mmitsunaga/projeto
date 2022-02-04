<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.ProcessoParteDt"%>
<%@page import="br.gov.go.tj.projudi.dt.AdvogadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.UsuarioDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EstadoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.ServentiaDt"%>

<jsp:useBean id="ProcessoParteAdvogadodt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoParteAdvogadoDt"/>
<jsp:useBean id="processoDt" scope="session" class= "br.gov.go.tj.projudi.dt.ProcessoDt"/>

<%@page import="java.util.Iterator"%>
<html>
	<head>
		<title>Parte Advogado do Processo</title>
	
    	<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE">
		<style type="text/css">
			@import url('./css/Principal.css');
			@import url('./css/Paginacao.css');
		</style>
      	
      	
      	<script type='text/javascript' src='./js/Funcoes.js'></script>
      	<script type='text/javascript' src='./js/jquery.js'></script>
      	<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>   
      	<script type='text/javascript' src='./js/Digitacao/AutoTab.js'></script>
      	<script type='text/javascript' src='./js/Digitacao/DigitarSoNumero.js'></script>
      	
      	<script language="javascript" type="text/javascript">
      	function modificarRecebimentoIntimacao(idProcessoParteAdvogado) {
			//Alterando página atual para Curinga 9
      	 	document.getElementById('PaginaAtual').value = 9;
			document.getElementById('idProcessoParteTipoAlterarRecebimento').value = idProcessoParteAdvogado;
      	 	submit();
      	}
      	function modificarAdvogadoDativo(idProcessoParteAdvogado) {
			//Alterando página atual para Curinga 1
      	 	document.getElementById('PaginaAtual').value = 1;
			document.getElementById('idProcessoParteTipoAlterarDativo').value = idProcessoParteAdvogado;
      	 	submit();
      	} 
      	$( document ).ready(
			function(){
				$( 'input#optionAdvogado' ).change( 
					function(){ 
						$( 'fieldset#fieldsetAdvogado' ).show();
						$( 'fieldset#fieldsetPromotor' ).hide();
						$( 'fieldset#fieldsetProcurador' ).hide();
					}
				);

				$( 'input#optionPromotor' ).change( 
					function(){ 
						$( 'fieldset#fieldsetAdvogado' ).hide();
						$( 'fieldset#fieldsetPromotor' ).show();
						$( 'fieldset#fieldsetProcurador' ).hide();
						$( '#Id_Serventia' ).attr('value', '');
						$( '#Serventia' ).attr('value', '');
					}
				);
				
				$( 'input#optionProcurador' ).change( 
						function(){ 
							$( 'fieldset#fieldsetAdvogado' ).hide();
							$( 'fieldset#fieldsetPromotor' ).hide();
							$( 'fieldset#fieldsetProcurador' ).show();
							$( '#Id_Serventia' ).attr('value', '');
							$( '#Serventia2' ).attr('value', '');
						}
					);
				
				if( $( 'input#tempFluxo1' ).val() === '2' ){
					$( 'fieldset#fieldsetAdvogado' ).hide();
					$( 'fieldset#fieldsetPromotor' ).show();
					$( 'fieldset#fieldsetProcurador' ).hide();
					$( 'input#optionAdvogado' ).get( 0 ).checked = false;
					$( 'input#optionPromotor' ).get( 0 ).checked = true;
					$( 'input#optionProcurador' ).get( 0 ).checked = false;
				}else if( $( 'input#tempFluxo1' ).val() === '3' ){
					$( 'fieldset#fieldsetAdvogado' ).hide();
					$( 'fieldset#fieldsetPromotor' ).hide();
					$( 'fieldset#fieldsetProcurador' ).show();
					$( 'input#optionAdvogado' ).get( 0 ).checked = false;
					$( 'input#optionPromotor' ).get( 0 ).checked = false;
					$( 'input#optionProcurador' ).get( 0 ).checked = true;
				}else {
					$( 'fieldset#fieldsetAdvogado' ).show();
					$( 'fieldset#fieldsetPromotor' ).hide();
					$( 'fieldset#fieldsetProcurador' ).hide();
					$( 'input#optionAdvogado' ).get( 0 ).checked = true;
					$( 'input#optionPromotor' ).get( 0 ).checked = false;
					$( 'input#optionProcurador' ).get( 0 ).checked = false;
				}
			}
      	);
      	</script>      			
      			
	</head>

	<body>
  		<div id="divCorpo" class="divCorpo">
	  		<div class="area"><h2>&raquo; Habilitar Advogados\Promotores (Substituto Processual)</h2></div>

			<form action="ProcessoParteAdvogado" method="post" name="Formulario" id="Formulario">

	  			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
	  			<input id="__Pedido__" name="__Pedido__" type="hidden" value="<%=request.getAttribute("__Pedido__")%>" />
	  			<input id="tempFluxo1" name="tempFluxo1" type="hidden" value="<%=request.getAttribute("tempFluxo1")%>">  
	  			<input id="ListaAdvogadosHabilitacao" name="ListaAdvogadosHabilitacao" type="hidden" value="<%=request.getAttribute("ListaAdvogadosHabilitacao")%>"/>
	  			<input name="TituloPagina" type="hidden" value="<%=request.getAttribute("tempTituloPagina")%>" />
	
			  	<div id="divEditar" class="divEditar">
					<fieldset class="formEdicao"> 
			    		<legend class="formEdicaoLegenda">Habilitação no Processo:  <span class="span"><a id="numeroProcesso" href="BuscaProcesso?Id_Processo=<%=processoDt.getId_Processo()%>"><%=processoDt.getProcessoNumero()%></a></span/> </legend>
			   			<input type="radio" checked name="opcao" id="optionAdvogado"/><label for="optionAdvogado">Habilitação Advogado/Defensor Público</label><br>
			   			<input type="radio" name="opcao" id="optionPromotor" /><label for="optionPromotor">Habilitação Promotor</label><br>
			   			<input type="radio" name="opcao" id="optionProcurador" /><label for="optionProcurador">Habilitação Procurador/Grandes Litigantes</label><br>
			   			<br /><br />
			   			<fieldset id="fieldsetAdvogado"> 
			    			<legend class="formEdicaoLegenda">Consultar Advogado</legend>
			   			      
					   			<label class="formEdicaoLabel" for="Id_UsuarioServentia">*OAB/Matrícula</label><br>  
							    <%if(request.getSession().getAttribute("solicitacaoProcurador") != null && (Boolean)request.getSession().getAttribute("solicitacaoProcurador")) { %>
							    	<input class="formEdicaoInput" name="OabNumero" id="OabNumero" type="text" size="20" maxlength="30" value="<%=ProcessoParteAdvogadodt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" readonly="readonly" >
						       	<%} else {%>
						       		<input class="formEdicaoInput" name="OabNumero" id="OabNumero" type="text" size="20" maxlength="30" value="<%=ProcessoParteAdvogadodt.getOabNumero()%>" onkeypress="return DigitarSoNumero(this, event)" >
						       	<%} %>
								
						       	<%if(request.getSession().getAttribute("solicitacaoProcurador") != null && (Boolean)request.getSession().getAttribute("solicitacaoProcurador")) { %>
						       		<input class="formEdicaoInput" name="OabComplemento" id="OabComplemento" type="text" size="2" value="<%=ProcessoParteAdvogadodt.getOabComplemento()%>" readonly="readonly" >
						       	<%} else {%>
								    <select name="OabComplemento" id="OabComplemento" class="formEdicaoCombo" title="Complemento" >
								    	<option></option>	
							        	<option>N</option>
								       	<option>A</option>
								        <option>B</option>
						   		        <option>S</option>
						   		        <option selected><%=ProcessoParteAdvogadodt.getOabComplemento()%></option>
							    	</select>
						       	<%} %>
						       	<br>
						       	<label class="formEdicaoLabel" for="Id_UsuarioServentia">*Tipo de Advogado</label><br>
						    	 <%if(request.getSession().getAttribute("solicitacaoProcurador") != null && (Boolean)request.getSession().getAttribute("solicitacaoProcurador")) { %>
						    		<input type="hidden" name="AdvogadoTipo" id="AdvogadoTipo" value="<%=ProcessoParteAdvogadodt.getAdvogadoTipo()%>">
						    		<input class="formEdicaoInput" name="AdvogadoTipoLabel" id="AdvogadoTipoLabel" type="text" size="40" value="<%=AdvogadoDt.getDescricao(ProcessoParteAdvogadodt.getAdvogadoTipo()) %>" readonly="readonly" >
						    	<%} else {%>
						    		<select name="AdvogadoTipo" class="formEdicaoCombo">
						    			<option value="-1"> Selecione </option>
						    			<%
						    			AdvogadoDt advDt = new AdvogadoDt();
						    			String[] stTiposAdvogados = advDt.getTiposAdvogado();
						    			for(int i=1; i<stTiposAdvogados.length; i++){%>
						    			<option value="<%=i%>" <%=(stTiposAdvogados[i].equalsIgnoreCase(ProcessoParteAdvogadodt.getAdvogadoTipo())?"selected":"")%>> <%=stTiposAdvogados[i]%> </option>
						    			<%} %>
						    		</select>
						    	
						    	<%} %>
						    	<br>
						    	<label class="formEdicaoLabel" for="Id_UsuarioServentia">*UF Órgão  
						    	 <%if(request.getSession().getAttribute("solicitacaoProcurador") != null && (Boolean)request.getSession().getAttribute("solicitacaoProcurador")) { %>
						    		</label><br>
						    		<input class="formEdicaoInput" name="EstadoOabUf" id="EstadoOabUf" type="text" size="5" value="<%=ProcessoParteAdvogadodt.getEstadoOabUf()%>" readonly="readonly" >
						    	<%} else {%>
						    		<input class="FormEdicaoimgLocalizar" id="imaLocalizarEstado" name="imaLocalizarEstado" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(EstadoDt.CodigoPermissao  * Configuracao.QtdPermissao + Configuracao.Localizar  )%>')" > 					
							    	</label><br>
							    	<input class="formEdicaoInputSomenteLeitura" readonly="true" name="EstadoOabUf" id="EstadoOabUf" type="text" size="5" maxlength="20" value="<%=ProcessoParteAdvogadodt.getEstadoOabUf()%>" <%if(!ProcessoParteAdvogadodt.getOabNumero().equals("")) { %> readonly="readonly" <%} %>/>
						    	<%} %>
						    	
						    	<br />
						    	<label class="formEdicaoLabel" for="RecebeIntimacao">Recebe Intimação*</label><br> 
						    	<select name="RecebeIntimacao" class="formEdicaoCombo">
					    			<option value="true" <%if(ProcessoParteAdvogadodt.getRecebeIntimacao()) {%> selected="selected" <%} %>> Sim </option>
					    			<option value="false"  <%if(!ProcessoParteAdvogadodt.getRecebeIntimacao()) {%> selected="selected" <%} %> > Não </option>
					    		</select><br />
					    		<label for="Dativo">Dativo</label> <input class="formEdicaoInput" name="Dativo" id="Dativo"  type="checkbox"  value="true" <% if(ProcessoParteAdvogadodt.getDativo().equalsIgnoreCase("true")){%>  checked<%}%>>
					    	
					    	<br />
					    	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input  id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" title="Consultar Advogado" alt="Consultar Advogado" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); AlterarValue('tempFluxo1','1')" />
							</div>
						</fieldset>	
						
						<fieldset class="formEdicao" id="fieldsetPromotor" style="display: none;"> 
			    			<legend class="formEdicaoLegenda">Consultar Promotor (Substituto Processual)</legend>
			    			
			    			<input name="Id_Serventia"  id="Id_Serventia"  type="hidden"  value="<%=request.getAttribute("Id_Serventia")%>"/>						
							<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('tempFluxo1','2')" >							 
							</label><br> 
							<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="Serventia" id="Serventia" type="text" size="60" maxlength="60" value="<%=request.getAttribute("Serventia")%>"/>
							<label for="Aviso" style="float:left;margin-left:25px;" ><small>Selecione a Serventia para listar os Promotores disponíveis.</small></label><br> <br />
			   			      
					    	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input  id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" title="Consultar Promotor" alt="Consultar Advogado" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); AlterarValue('tempFluxo1','2')" />
							</div>
						</fieldset>
						
						<fieldset class="formEdicao" id="fieldsetProcurador" style="display: none;"> 
			    			<legend class="formEdicaoLegenda">Consultar Defensor</legend>
			    			
							<label class="formEdicaoLabel" for="Id_Serventia">*Serventia 
							<input class="FormEdicaoimgLocalizar" id="imaLocalizarServentia" name="imaLocalizarServentia" type="image"  src="./imagens/imgLocalizarPequena.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(ServentiaDt.CodigoPermissao * Configuracao.QtdPermissao + Configuracao.Localizar)%>');AlterarValue('tempFluxo1','3')" >							 
							</label><br> 
							<input  class="formEdicaoInputSomenteLeitura"  readonly="readonly" name="Serventia2" id="Serventia2" type="text" size="60" maxlength="60" value="<%=request.getAttribute("Serventia")%>"/>
							<label for="Aviso" style="float:left;margin-left:25px;" ><small>Selecione a Serventia para listar os Procuradores disponíveis.</small></label><br> <br />
			   			      
					    	<div id="divBotoesCentralizados" class="divBotoesCentralizados">
								<input  id="formLocalizarBotao" class="formLocalizarBotao" type="submit" name="Localizar" title="Consultar Procurador" alt="Consultar Advogado" value="Consultar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Localizar%>'); AlterarValue('tempFluxo1','3')" />
							</div>
						</fieldset>		
			    		
			    	<br />
					<fieldset>
						<legend>Lista de Advogados/Promotores para Habilitação</legend>
						
						<%
						 List liAdvogados =  (List) request.getSession().getAttribute("ListaAdvogadosHabilitacao");
						 UsuarioDt objTempAdvogado;
							
						if (liAdvogados != null && liAdvogados.size() > 0){
						%>
							<table class="Tabela" id="TabelaAvogados">
										<thead>
											<tr>
												<th></th>
												<th>Nome</th>
												<th width="30%">OAB/Matrícula</th>
												<th>Serventia</th>
												<th></th>
											</tr>
										</thead>
										<tbody>
											<%
												String stTempNome="";
												for(int f = 0 ; f< liAdvogados.size();f++) {
													objTempAdvogado = (UsuarioDt) liAdvogados.get(f); %>
													<%if (stTempNome.equalsIgnoreCase("")) { stTempNome="a";%> 
									                   	<tr class="TabelaLinha1"> 
													<%}else{ stTempNome=""; %>    
									                   	<tr class="TabelaLinha2">
													<%}%>
									                   		<td> <%=f+1%></td>
								                    	<td>
								                    		<%= objTempAdvogado.getNome()%>
								                    	</td>
								                   		<td align="center">
								                   			<%= objTempAdvogado.getOabNumero()+ " - "+objTempAdvogado.getOabComplemento()%>
								                   		</td>
								                   		<td align="center">
								                   			<%= objTempAdvogado.getServentia()%>
								                   		</td>
								                   		<%  boolean checked = false;
								                   			if (ProcessoParteAdvogadodt != null 
								                   					&& ProcessoParteAdvogadodt.getId_UsuarioServentiaAdvogado().equals(objTempAdvogado.getId_UsuarioServentia())){ 
								                   				checked = true;
								                   			}
								                   		%> 
								                   		<td align="center">
															<input type="radio" <%=checked?"checked":"" %> name="usuariosServentiaAdvogado" value="<%=objTempAdvogado.getId_UsuarioServentia()%>" />
														</td>
								                   		</tr>
											<%}%>
										</tbody>
										<%
								} else { %>
								<tbody>
									<tr>
										<td><em>Consulte um Advogado/Promotor para habilitação.</em></td>			  	  							    
								    </tr>
							  	</tbody>								    
								<% } %>
							</table>		
						</fieldset>	
						<br />				    		
			    		
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Polo Ativo </legend>
						
							<%
														String[] partesSelecionadas = (String[])request.getAttribute("ListaAdvogadoParte");
																			List listaPromoventes = processoDt.getListaPolosAtivos();
																			if (listaPromoventes != null){
														  	    			for (int i=0;i < listaPromoventes.size();i++){
														   			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPromoventes.get(i);
														   			  			if (parteDt.getDataBaixa().length() == 0){
													%>
							
					   				<div>
						   				<input name="ProcessoParteAdvogado" id="ProcessoParteAdvogado" type="checkbox" value=<%=parteDt.getId_ProcessoParte()%>
						   					<%	
						   						if (partesSelecionadas != null && partesSelecionadas.length>0){
						   							for (int j = 0; j < partesSelecionadas.length; j++) {
				                    			 		if (partesSelecionadas[j].equals(parteDt.getId_ProcessoParte())){%> 
				                    				 		checked
				                    		<% 			}
				                    		 		}
						   						}
						   					%>
						   				>
					   				</div> 
					       			<span><%=parteDt.getNome()%> </span>
					       		
					       			<div> CPF </div>
					        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<% 		}
			  	    			}}
							%>
						</fieldset>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Polo Passivo </legend>
							<%
								List listaPromovidos = processoDt.getListaPolosPassivos();
													if (listaPromovidos != null){
								  	    				for (int i=0;i < listaPromovidos.size();i++){
								   			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaPromovidos.get(i);
								   			  			if (parteDt.getDataBaixa().length() == 0){
							%>
							
								<div>
					   				<input class="formEdicaoInput" name="ProcessoParteAdvogado" id="ProcessoParteAdvogado" type="checkbox" value=<%=parteDt.getId_ProcessoParte()%> 
					   					<%	
					   					if (partesSelecionadas != null && partesSelecionadas.length>0){
			                    		 		for (int j = 0; j < partesSelecionadas.length; j++) {
			                    		 			if (partesSelecionadas[j].equals(parteDt.getId_ProcessoParte())){%>  
			                    				 		checked
			                    		<% 			}
			                    		 		}
					   						}
					   					%>
					   				>
					   			</div>
				       			<span><%=parteDt.getNome()%> </span>
				       		
				       			<div> CPF </div>
				        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<% 		}
			   			  			}
								}
			   			  	%>
						</fieldset>
						
						<fieldset id="VisualizaDados" class="VisualizaDados">
							<legend> Outra(s) Parte(s) </legend>
						
							<%
								List listaOutrasPartes = processoDt.getListaOutrasPartes();
								if (listaOutrasPartes != null){
			  	    			for (int i=0;i < listaOutrasPartes.size();i++){
			   			  			ProcessoParteDt parteDt = (ProcessoParteDt)listaOutrasPartes.get(i);
			   			  			if (parteDt.getDataBaixa().length() == 0){
			  				%>
							
					   				<div>
						   				<input name="ProcessoParteAdvogado" id="ProcessoParteAdvogado" type="checkbox" value=<%=parteDt.getId_ProcessoParte()%>
						   					<%	
						   						if (partesSelecionadas != null && partesSelecionadas.length>0){
						   							for (int j = 0; j < partesSelecionadas.length; j++) {
				                    			 		if (partesSelecionadas[j].equals(parteDt.getId_ProcessoParte())){%> 
				                    				 		checked
				                    		<% 			}
				                    		 		}
						   						}
						   					%>
						   				>
					   				</div> 
					       			<span><%=parteDt.getNome()%> </span>
					       		
					       			<div> CPF </div>
					        		<span><%=parteDt.getCpfCnpjFormatado()%></span/><br />
							<% 		}
			  	    			}}
							%>
						</fieldset>
						
						<input type="hidden" name="idProcessoParteTipoAlterarRecebimento" id="idProcessoParteTipoAlterarRecebimento" value="<%=request.getParameter("idProcessoParteTipoAlterarRecebimento")%>"/>
						<input type="hidden" name="idProcessoParteTipoAlterarDativo" id="idProcessoParteTipoAlterarDativo" value="<%=request.getParameter("idProcessoParteTipoAlterarDativo")%>"/>
						
						<div id="divBotoesCentralizados" class="divBotoesCentralizados"> 
							<input name="imgInserir" type="submit" value="Inserir" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Salvar %>');"> 
	 					</div>
					</fieldset>
					
					 <%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Salvar))
						    && (request.getAttribute("MensagemErro") == null || request.getAttribute("MensagemErro").equals(""))) {%> 	
    				<div id="divConfirmarSalvar" class="ConfirmarSalvar">
        				<input class="imgsalvar" type="image" src="./imagens/imgSalvar.png" onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.SalvarResultado)%>')"  > <br />
        				<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
        				<div class="divMensagemsalvar"><%=request.getAttribute("Mensagem")%></div>
           				<% } %>
      				</div>
 					<%}%>
					
					<fieldset id="VisualizaDados" class="VisualizaDados">
				      	<legend>Advogados Habilitados</legend>
				      	<input type="hidden" id="posicaoLista" name="posicaoLista" value="<%=request.getAttribute("posicaoLista")%>">
				      	
						<%
							List listaAdvogados = processoDt.getListaAdvogados();		
							ProcessoParteAdvogadoDt objTemp;
							if (listaAdvogados != null && listaAdvogados.size() > 0){
								objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(0);
						%>
						
				     	<table id="Tabela" class="Tabela">
				        	<thead>
				            	<tr class="TituloColuna" align="center">
				                	<td>Desabilitar</td>
				                	<td>Parte</td>
				                	<td>OAB/Matrícula</td>
				                  	<td>Advogado</td>
				                  	<td>Habilitação</td>
				                  	<td>Dativo</td>
				                  	<td>Recebe Intimação</td>
				             	 </tr>
				           	</thead>
				          	<tbody id="tabListaAdvogadoParte">
							<%
								boolean boTeste=false;
								for(int i = 0 ; i< listaAdvogados.size();i++) {
							   		objTemp = (ProcessoParteAdvogadoDt)listaAdvogados.get(i);
							%>
						  		<tr class="<%=(boTeste?"Linha1":"Linha2")%>"> 
					                <td align="center" width="7%">
					                	<input name="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Excluir%>');AlterarValue('posicaoLista','<%=i%>')" title="Desabilitar Advogado do Processo">
					                </td> 
					                <td width="35%"><%= objTemp.getNomeParte() + " - " + objTemp.getProcessoParteTipo()%>
						                <%if (objTemp.getPrincipal() != null && objTemp.getPrincipal().equalsIgnoreCase("true")) {%>
						                		- <b>Adv. Principal</b> 
						                <%} %>
				                	</td>
				       		        <td width="10%" align="center"><%=objTemp.getOabNumero()+ " " + objTemp.getOabComplemento()%></td>
					                <td width="30%"><%= objTemp.getNomeAdvogado() %> </td>
					            	<td width="10%" align="center"> <%=objTemp.getDataEntrada()%> </td>
					            	<td width="10%" align="center"> <%if(objTemp.getDativo()!=null && objTemp.getDativo().length() > 0 && objTemp.getDativo().equals("1")){%>Sim<%} else { %>Não<%} %>
					            		<input name="button2" id="button3" title="Alterar Status Advogado Dativo." type="image" 
							      			src="./imagens/imgRestaurarPequena.png"  onclick="javascript:modificarAdvogadoDativo(<%=objTemp.getId()%>); return false;" />
					            	</td>
					            	
					            	<td width="10%" align="center"> 
					            		<%if(objTemp.getRecebeIntimacao()== true){%>Sim<%} else { %>Não<%} %>
					            		<input name="button2" id="button2" title="Alterar recebimento de de intimação." type="image" 
							      			src="./imagens/imgRestaurarPequena.png" onclick="javascript:modificarRecebimentoIntimacao(<%=objTemp.getId()%>); return false;" />
					            	</td>
					       		</tr>
							<%
									boTeste = !boTeste;
								}
							} else { %>
								<em>Nenhum Advogado habilitado nesse processo.</em>
							<% } %>
						   	</tbody>
					  	</table>
					</fieldset>
					
					<%if (request.getAttribute("PaginaAnterior").toString().equalsIgnoreCase(String.valueOf(Configuracao.Excluir))) {%> 	  
    				<div id="divConfirmarxcluir" class="ConfirmarExcluir">
		        		<input class="imgexcluir" type="image" src="./imagens/imgExcluir.png"  onclick="AlterarValue('PaginaAtual','<%=String.valueOf(Configuracao.ExcluirResultado)%>')"  > <br />
        				<% if (request.getAttribute("Mensagem") != null && !request.getAttribute("Mensagem").equals("")) { %>
						<div class="divMensagemexcluir"><%=request.getAttribute("Mensagem")%></div>
						<% }%> 
      				</div>
 					<%}%>
					
				</div>
			</form>
			<%@ include file="Padroes/Mensagens.jspf" %>
		</div>
		
	</body>
</html>