<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//ENhttp://www.w3.org/TR/html4/loose.dtd">

<%@page import="br.gov.go.tj.utils.Funcoes"%>
<%@page import="br.gov.go.tj.projudi.dt.MandadoJudicialDt"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<jsp:useBean id="mandadoJudicial" scope="session" class="br.gov.go.tj.projudi.dt.MandadoJudicialDt" />

<html>
<head>
<meta content="text/html; charset=LATIN1" http-equiv="CONTENT-TYPE" />

<link href="./css/Principal.css" type="text/css" rel="stylesheet" />
<link href="./css/form.css" type="text/css" rel="stylesheet" />
<link href="./css/font-awesome-4.4.0/css/font-awesome.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="./js/jquery.js"></script>
<script type='text/javascript' src='./js/ui/jquery-ui.min.js'></script>
<script type='text/javascript' src='./js/Funcoes.js'></script>
<script language="javascript" type="text/javascript" src="./js/Digitacao/DigitarSoNumero.js"></script>
<script language="javascript" type="text/javascript" src="./js/Digitacao/AutoTab.js"></script>

</head>

<body>

	<div id="divCorpo" class="divCorpo">
		<div class="area">
			<h2>&raquo; Consulta Mandado Judicial</h2>
		</div>

		<form action="<%=request.getAttribute("tempRetorno")%>" method="post" name="Formulario" id="Formulario">

			<input id="PaginaAtual" name="PaginaAtual" type="hidden" value="<%=request.getAttribute("PaginaAtual")%>" />
			<input id="PosicaoPaginaAtual" name="PosicaoPaginaAtual" type="hidden" value="<%=request.getAttribute("PosicaoPaginaAtual")%>" />
			<input id="PaginaAnterior" name="PaginaAnterior" type="hidden" value="<%=request.getAttribute("PaginaAnterior")%>" />
					
			<%mandadoJudicial = (MandadoJudicialDt) request.getAttribute("mandadoJudicial"); %>
			
			
			<div id="divEditar" class="divEditar">
			
			    
			    <input type="image"  src="./imagens/imgLocalizar.png" title="Consultar Mandado" class="imgEditar" 
					onclick="AlterarValue('PaginaAtual','4')" 
     			    value="Consultar" name="botaoConsultar">
				
				<fieldset class="formEdicao">
				
					<legend class="formEdicaoLegenda">Informe o Número do Mandado</legend>
					
					<label class="formEdicaoLabel" for="id">*Número do Mandado</label> 
					
					<input name="id" id="id" type="text"  
					       size="20" value="" />  
					       
				</fieldset>
				
				<fieldset class="formEdicao">
					
					<legend class="formEdicaoLegenda">Dados do Mandado</legend>
					
					<label class="formEdicaoLabel" for="procNumero">Número</label> 

					<label class="formEdicaoLabel" for="procNumero">Processo </label> <br>			
					
					<input class="formEdicaoInputSomenteLeitura"
					       name="id" id="id" type="text" readonly="readonly"
						   size="20" value="<%=mandadoJudicial.getId()%>" />					
					
					<input class="formEdicaoInputSomenteLeitura"
					       name="procNumero" id="procNumero" type="text" readonly="readonly"
						   size="20" value="<%=mandadoJudicial.getProcNumero()%>" /> <br>								       
					
					<label class="formEdicaoLabel" for="procNumero">Tipo</label> 
	 							
					<label class="col10" for="nomeUsuarioServentia_1">Oficial de Justiça</label><br> 
							
					<input class="formEdicaoInputSomenteLeitura"
					       name="mandadoTipo" id="mandadoTipo" type="text" readonly="readonly"
					       size="20" value="<%=mandadoJudicial.getMandadoTipo()%>" />
						
					<input class="formEdicaoInputSomenteLeitura"
							name="nomeUsuarioServentia_1" id="nomeUsuarioServentia_1"
							readonly="readonly" type="text" size="71"
							value="<%=mandadoJudicial.getNomeUsuarioServentia_1()%>" /><br>
							
					
					<label class="formEdicaoLabel" for="status">Status</label> 
					
					<label class="col10" for="nomeUsuarioServentia_2">Companheiro </label><br>
					
					<input class="formEdicaoInputSomenteLeitura"
					       name="status" id="status" type="text" readonly="readonly"
					       size="20" value="<%=mandadoJudicial.getMandadoJudicialStatus()%>" />  
							
					<input class="formEdicaoInputSomenteLeitura"
					       name="nomeUsuarioServentia_2" id="nomeUsuarioServentia_2" readonly="readonly" type="text"
					       size="71" value="<%=mandadoJudicial.getNomeUsuarioServentia_2()%>" /><br>
					       
					
					<label class="formEdicaoLabel" for="assistencia">Info Custas</label> 
					
					<label class="col10" for="nomeServentia">Serventia</label><br>
					
					<input class="formEdicaoInputSomenteLeitura"
					       name="assistencia" id="assistencia" type="text" readonly="readonly"
					       size="20" value="<%=mandadoJudicial.getAssistencia()%>" />  
							
					<input class="formEdicaoInputSomenteLeitura"
					       name="nomeServentia" id="nomeServentia" readonly="readonly" type="text"
					       size="71" value="<%=mandadoJudicial.getNomeServentia()%>" /><br>					
					
					<label class="formEdicaoLabel" for="dataDistribuicao">Data Distribuição</label> 
						
					<label class="formEdicaoLabel" for="dataLimite">Data Limite</label> 
					
					<label class="formEdicaoLabel" for="dataRetorno">Data Retorno</label> <br>
					
					<input class="formEdicaoInputSomenteLeitura"
					       name="dataDistribuicao" id="dataDistribuicao" type="text" readonly="readonly"
						   size="20" value="<%=mandadoJudicial.getDataDistribuicao()%>" />
						
					<input class="formEdicaoInputSomenteLeitura"
					       name="dataLimite" id ="dataLimite" type="text" readonly="readonly"
					       size="20" value="<%=mandadoJudicial.getDataLimite()%>" />  
					       
					<input class="formEdicaoInputSomenteLeitura"
					       name="dataRetorno" id="dataRetorno" type="text" readonly="readonly"
					       size="20" value="<%=mandadoJudicial.getDataRetorno()%>" /> <br>     
					       
					       
					<label class="formEdicaoLabel" for="quantidadeLocomocao">Quantidade Loco</label> 
						
					<label class="formEdicaoLabel" for="valorLocomocao">Valor Loco</label>
					
					<label class="col30" for="mandJudPagamentoStatus">Status Pagamento</label> <br>
												
					<input class="formEdicaoInputSomenteLeitura"
					       name="quantidadeLocomoção" id="quantidadeLocomocao" type="text" readonly="readonly"
						   size="20" value="<%=mandadoJudicial.getQuantidadeLocomocao()%>" />
						
					<input class="formEdicaoInputSomenteLeitura"
					       name="valorLocomocao" id="valorLocomocao" type="text" readonly="readonly"
					       size="20" value="<%=mandadoJudicial.getValorLocomocao()%>" />   
					       
					<input class="formEdicaoInputSomenteLeitura"
					       name="mandJudPagamentoStatus" id="mandJudPagamentoStatus" type="text" readonly="readonly" 
						   size="20" value="<%=mandadoJudicial.getMandJudPagamentoStatus()%>" /> <br> 				
	
					<label class="col30" for="numeroProcesso">Comarca</label>
						
					<label class="col30" for="numeroProcesso">Bairro</label><br> 
						
					<input class="formEdicaoInputSomenteLeitura" 
					        name="comarca" id="comarca" type="text" readonly="readonly" 
					        size="58" value="<%=mandadoJudicial.getComarca()%>" />
						
					<input	class="formEdicaoInputSomenteLeitura" 
					        name="bairro" id="bairro" type="text" readonly="readonly"
					        size="58" value="<%=mandadoJudicial.getBairro()%>" /><br>
							
					<label class="col30" for="zona">Zona</label>
						
					<label class="col30" for="nomeOficial">Regiao</label><br> 
							
					<input class="formEdicaoInputSomenteLeitura"
					       name="zona" id="zona" readonly="readonly" type="text"
					       size="58" value="<%=mandadoJudicial.getZona()%>" />
						
					<input class="formEdicaoInputSomenteLeitura"
					       name="regiao" id="regiao" readonly="readonly" type="text"
					       size="58" value="<%=mandadoJudicial.getRegiao()%>" /><br>
					
					<label class="col35" for="usuarioAutorizacao">Usuário Status Pagamento</label>
						
					<label class="col35" for="data">Data</label><br>
						
					<input class="formEdicaoInputSomenteLeitura"
					       name="nomeUsuUltStatusOrdemPag" id="nomeUsuUltStatusOrdemPag"
						   type="text" readonly="readonly" size="68"
						   value="<%=mandadoJudicial.getNomeUsuPagamentoStatus()%>" />
					
					<input class="formEdicaoInputSomenteLeitura" name="dataUltStatusOrdemPag"
						   id="dataUltStatusOrdemPag" type="text" readonly="readonly"
						   size="20" value="<%=mandadoJudicial.getDataPagamentoStatus()%>" /> <br>			
	
					<label class="col35" for="usuarioEnvio">Usuário Envio de Créditos Diretoria Financeira</label>
						
					<label class="col35" for="dataEnvio">Data</label><br>
	
					<input class="formEdicaoInputSomenteLeitura"
						   name="nomeUsuEnvioOrdemPag" id="nomeUsuEnvioOrdemPag" type="text" readonly="readonly" 
						   size="68" value="<%=mandadoJudicial.getNomeUsuPagamentoEnvio()%>" />
						
					<input class="formEdicaoInputSomenteLeitura"
						   name="dataEnvioOrdemPag" id="dataEnvioOrdemPag" type="text" readonly="readonly" 
						   size="20" value="<%=mandadoJudicial.getDataPagamentoEnvio()%>" /><br>
							
					<label class="formEdicaoLabel" for="id">Modelo</label> <br>
					
					<input class="formEdicaoInputSomenteLeitura"
					       name="modelo" id="modelo" type="text" readonly="readonly"
					       size="85" value="<%=mandadoJudicial.getModelo()%>" />  <br>
					       
					<label class="col30" for="escala">Escala</label>  <br> 
									    
					<input class="formEdicaoInputSomenteLeitura"
						   name="escala" id="escala" type="text" readonly="readonly" 
						   size="85" value="<%=mandadoJudicial.getEscala()%>" />					
							   
				</fieldset>	 
				
				
				<%
				if (request.getAttribute("htmlCertidao") != null) {							
				    String[] htmlCertidao = (String[]) request.getAttribute("htmlCertidao");
					int contador = 1;
					for (String htmlCert: htmlCertidao) {
						if (!htmlCert.equalsIgnoreCase("") && htmlCert.contains("</a>")) {
				%>
				    		<br/>
							<%=htmlCert%>
							<br/>
				<%      } else if (!htmlCert.equalsIgnoreCase("")) { %>							
							             <br> <br>
						               	 <fieldset>
			    				           <legend>
								             <input class="FormEdicaoimgLocalizar"
								               name="imaLocalizarArquivoTipo" type="image"
								               src="./imagens/imgEditorTextoPequena.png"
								               onclick="MostrarOcultar('divTextoEditor'); return false;"
								               title="Texto (Esconder/Mostrar)" /> Texto Certidão <%= contador %>
								           </legend>
							           	   <div id="divTextoEditor" class="divTextoEditor"
									       style="display: block">
									       <%=htmlCert%>
							             </fieldset>
				<%
							             contador++;	
						       } 
						}
				}
				%>
					
	            <%@ include file="Padroes/Mensagens.jspf" %>
			</div>
	</div>
	
	</form>
	 
</body>


</html>
