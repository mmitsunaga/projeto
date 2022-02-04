<%@page import="java.util.*"%>
<%@page import="br.gov.go.tj.utils.Configuracao"%>
<%@page import="br.gov.go.tj.projudi.dt.RegimeExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.LocalCumprimentoPenaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.PenaExecucaoTipoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.EventoExecucaoStatusDt"%>
<%@page import="br.gov.go.tj.projudi.dt.FormaCumprimentoExecucaoDt"%>
<%@page import="br.gov.go.tj.projudi.dt.SituacaoAtualTipoPenaDt"%>
<%@page import="br.gov.go.tj.projudi.dt.SituacaoAtualModalidadeDt"%>

<jsp:useBean id="SituacaoDt" scope="session" class="br.gov.go.tj.projudi.dt.SituacaoAtualExecucaoDt"/>

<script type="text/javascript">
	$(document).ready(function() {
		$("#SA_DataFuga").mask("99/99/9999");
		if ($("#SA_Id_EventoExecucaoStatus option:selected").val() == <%=EventoExecucaoStatusDt.FORAGIDO%>){
			$("#divFuga").show();
		} else {
			$("#divFuga").hide();
		}
	});

	function SA_selecionaRegime(){
		document.getElementById("SA_RegimeExecucao").value = $("#SA_Id_RegimeExecucao option:selected").text();
	}
	function SA_selecionaLocal(){
		document.getElementById("SA_LocalCumprimentoPena").value = $("#SA_Id_LocalCumprimentoPena option:selected").text();
	}
	function SA_selecionaStatus(){
		document.getElementById("SA_EventoExecucaoStatus").value = $("#SA_Id_EventoExecucaoStatus option:selected").text();
		if ($("#SA_Id_EventoExecucaoStatus option:selected").val() == <%=EventoExecucaoStatusDt.FORAGIDO%>){
			$("#divFuga").show();
		} else {
			$("#divFuga").hide();
		}
	}
	function SA_selecionaFormaCumprimento(){
		document.getElementById("SA_FormaCumprimento").value = $("#SA_Id_FormaCumprimento option:selected").text();
	}
	function SA_selecionaModalidade(){
		document.getElementById("SA_Modalidade").value = $("#SA_Id_Modalidade option:selected").text();
		AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');
		AlterarValue('PassoEditar','18');
		AlterarValue('posicaoLista','-1');
		FormSubmit('Formulario');
	}
	function SA_selecionaTipoPena(){
		document.getElementById("SA_PenaExecucaoTipo").value = $("#SA_Id_PenaExecucaoTipo option:selected").text();
		AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');
		AlterarValue('PassoEditar','19');
		AlterarValue('posicaoLista','-1');
		FormSubmit('Formulario');
		
	}




</script>
<br />
<div>
	<input type="hidden" id="posicaoLista" name="posicaoLista">
	<fieldset class="formEdicao"><legend class="formEdicaoLegenda">Situação atual de cumprimento da pena</legend> 
<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("ProcessoEventoExecucao")){ %>
		<label style="width: 25%" class="formEdicaoLabel" for="SA_DataAlteracao">Data da Informação: </label><br>
		<input class="formEdicaoInputSomenteLeitura" readonly id="SA_DataAlteracao" name="SA_DataAlteracao" type="text" value="<%=SituacaoDt.getDataAlteracao()%>"/>
		<br />
<%} %>
		<label style="width: 25%" class="formEdicaoLabel" for="SA_EventoExecucaoStatus">*Situação</label><br>
		<input type="hidden" id="SA_EventoExecucaoStatus" name="SA_EventoExecucaoStatus">
   	    <select style="width: 40%" name="SA_Id_EventoExecucaoStatus" id="SA_Id_EventoExecucaoStatus" onchange="SA_selecionaStatus();">
	    	<option value=""></option>
			<%List SA_listaStatus = (List) request.getSession().getAttribute("ListaStatus");
			if (SA_listaStatus != null){%> 
				<%for (int i=0; i<SA_listaStatus.size(); i++) {%>					
    		<option value="<%=((EventoExecucaoStatusDt)SA_listaStatus.get(i)).getId()%>" <%if (SituacaoDt.getIdEventoExecucaoStatus().equals(((EventoExecucaoStatusDt)SA_listaStatus.get(i)).getId())){%>selected<%}%>><%=((EventoExecucaoStatusDt)SA_listaStatus.get(i)).getEventoExecucaoStatus()%></option>
				<%} %>
			<%}%>
  		</select>
		<div id="divFuga" style="display: none">
			<br />
			<label class="formEdicaoLabel" style="width: 25%">*Data da Fuga</label><br>			
			<input class="formEdicaoInput" name="SA_DataFuga" id="SA_DataFuga" type="text" size="10" maxlength="10" value="<%=SituacaoDt.getDataFuga()%>" onKeyPress="return DigitarSoNumero(this, event)"/>
		</div>
		<br />

		<label style="width: 25%" class="formEdicaoLabel" for="SA_FormaCumprimento">*Forma de cumprimento da pena</label><br>
		<input type="hidden" id="SA_FormaCumprimento" name="SA_FormaCumprimento">
   	    <select style="width: 40%" name="SA_Id_FormaCumprimento" id="SA_Id_FormaCumprimento" onchange="SA_selecionaFormaCumprimento();">
	    	<option value=""></option>
			<%List SA_listaFormaCumprimento = (List) request.getSession().getAttribute("ListaFormaCumprimento");
			if (SA_listaFormaCumprimento != null){%> 
				<%for (int i=0; i<SA_listaFormaCumprimento.size(); i++) {%>					
    		<option value="<%=((FormaCumprimentoExecucaoDt)SA_listaFormaCumprimento.get(i)).getId()%>" <%if (SituacaoDt.getIdFormaCumprimento().equals(((FormaCumprimentoExecucaoDt)SA_listaFormaCumprimento.get(i)).getId())){%>selected<%}%>><%=((FormaCumprimentoExecucaoDt)SA_listaFormaCumprimento.get(i)).getFormaCumprimentoExecucao()%></option>
				<%} %>
			<%}%>
  		</select>
		<br />
<%if (request.getAttribute("tempPrograma").toString().equalsIgnoreCase("ProcessoEventoExecucao")){ %>
		<label style="width: 25%" class="formEdicaoLabel" for="SA_LocalCumprimentoPena">*Local de Cumprimento de Pena</label><br>
		<input type="hidden" id="SA_LocalCumprimentoPena" name="SA_LocalCumprimentoPena">
   	    <select style="width: 40%" name="SA_Id_LocalCumprimentoPena" id="SA_Id_LocalCumprimentoPena" onchange="SA_selecionaLocal();">
	    	<option value=""></option>
			<%List SA_listaLocal = (List) request.getSession().getAttribute("ListaLocal");
			if (SA_listaLocal != null){%> 
				<%for (int i=0; i<SA_listaLocal.size(); i++) {%>					
    		<option value="<%=((LocalCumprimentoPenaDt)SA_listaLocal.get(i)).getId()%>" <%if (SituacaoDt.getIdLocalCumprimentoPena().equals(((LocalCumprimentoPenaDt)SA_listaLocal.get(i)).getId())){%>selected<%}%>><%=((LocalCumprimentoPenaDt)SA_listaLocal.get(i)).getLocalCumprimentoPena()%></option>
				<%} %>
			<%}%>
  		</select>
		<br />

		<label style="width: 25%" class="formEdicaoLabel" for="SA_RegimeExecucao">Regime</label><br>  
		<input type="hidden" id="SA_RegimeExecucao" name="SA_RegimeExecucao">
		<select style="width: 40%" name="SA_Id_RegimeExecucao" id="SA_Id_RegimeExecucao" onchange="SA_selecionaRegime();">
	    	<option value=""></option>
			<% List SA_listaRegime_PPL = (List) request.getSession().getAttribute("SA_ListaRegime_PPL");
			if (SA_listaRegime_PPL != null){%> 
				<%for (int i=0; i<SA_listaRegime_PPL.size(); i++) {%>					
    		<option value="<%=((RegimeExecucaoDt)SA_listaRegime_PPL.get(i)).getId()%>" <%if (SituacaoDt.getIdRegime().equals(((RegimeExecucaoDt)SA_listaRegime_PPL.get(i)).getId())){%>selected<%}%>><%=((RegimeExecucaoDt)SA_listaRegime_PPL.get(i)).getRegimeExecucao()%></option>
				<%} %>
			<%}%>
  		</select>
		<br />

		<label style="width: 25%" class="formEdicaoLabel" for="SA_PenaExecucaoTipo">Tipo de pena</label><br>
		<input type="hidden" id="SA_PenaExecucaoTipo" name="SA_PenaExecucaoTipo">
		<select style="width: 40%" name="SA_Id_PenaExecucaoTipo" id="SA_Id_PenaExecucaoTipo" onChange="SA_selecionaTipoPena();">
		    <option value=""></option>
			<%List SA_listaPenaExecucaoTipo = (List) request.getSession().getAttribute("SA_ListaPenaExecucaoTipo");
				if (SA_listaPenaExecucaoTipo != null){%> 
				<%for (int i=0; i<SA_listaPenaExecucaoTipo.size(); i++) {%>					
	    	<option value="<%=((PenaExecucaoTipoDt)SA_listaPenaExecucaoTipo.get(i)).getId()%>"><%=((PenaExecucaoTipoDt)SA_listaPenaExecucaoTipo.get(i)).getPenaExecucaoTipo()%></option>
				<%} %>
				<%}%>
	    </select>
		<br />
		<%
   			List SA_listaTipoPena = SituacaoDt.getListaSituacaoAtualTipoPenaDt();
   	    	if (SA_listaTipoPena != null && SA_listaTipoPena.size() > 0){ 
   	    		for (int i=0; i<SA_listaTipoPena.size();i++){
   	    			SituacaoAtualTipoPenaDt tipoPena = (SituacaoAtualTipoPenaDt)SA_listaTipoPena.get(i); %>
				<table style="width:50%">
				<tr>
      				<td>
						<input style="text-align: right; width: 95%"  class="formEdicaoInputSomenteLeitura" readonly name="SA_TipoPena_<%=i%>" id="SA_TipoPena_<%=i%>" type="text" value="<%=tipoPena.getPenaExecucaoTipo()%>">  
					</td>
   	 				<td valign="middle"><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" 
						onclick="confirmaExclusao('Confirma exclusão?', '<%=Configuracao.Editar%>', '19', '<%=i%>');" 
						title="Excluir tipo de pena"/>
					</td>
      	 		</tr>
       	 		</table>
       		<%	} %>
   		<% } %>

		<label style="width: 25%"  class="formEdicaoLabel" for="SA_Modalidade">Modalidade</label><br>  
		<input type="hidden" id="SA_Modalidade" name="SA_Modalidade">
		<select style="width: 40%" name="SA_Id_Modalidade" id="SA_Id_Modalidade" onchange="SA_selecionaModalidade();">
		    <option value=""></option>
			<%List SA_listaMod = (List) request.getSession().getAttribute("ListaModalidade");
				if (SA_listaMod != null){%> 
				<%for (int i=0; i<SA_listaMod.size(); i++) {%>					
	    	<option value="<%=((RegimeExecucaoDt)SA_listaMod.get(i)).getId()%>"><%=((RegimeExecucaoDt)SA_listaMod.get(i)).getRegimeExecucao()%></option>
				<%} %>
			<%}%>
  		</select>
		<br />
		<%
   			List SA_listaModalidade = SituacaoDt.getListaSituacaoAtualModalidadeDt();
   	    	if (SA_listaModalidade != null && SA_listaModalidade.size() > 0){ 
   	    		for (int i=0; i<SA_listaModalidade.size();i++){
   	    			SituacaoAtualModalidadeDt modalidade = (SituacaoAtualModalidadeDt)SA_listaModalidade.get(i); %>
				<table style="width:50%">
				<tr>
      				<td>
						<input style="text-align: right; width: 95%" class="formEdicaoInputSomenteLeitura" readonly name="SA_Modalidade_<%=i%>" id="SA_Modalidade_<%=i%>" type="text" value="<%=modalidade.getModalidade()%>">  
					</td>
   	 				<td valign="middle"><input name="imgExcluir" class="imgExcluir" type="image" src="./imagens/imgExcluirPequena.png" 
						onclick="confirmaExclusao('Confirma exclusão?', '<%=Configuracao.Editar%>', '18', '<%=i%>');" 
						title="Excluir modalidade"/>
					</td>
      	 		</tr>
       	 		</table>
       		<%	} %>
   		<% } %>
		
		<div id="divBotoesCentralizados" class="divBotoesCentralizados">
			<input id="bt_salvar" name="imgCalcular" type="submit" value="Salvar" onclick="AlterarValue('PaginaAtual','<%=Configuracao.Editar%>');AlterarValue('PassoEditar','20');"> 
	    </div>	
 <% } %>
	</fieldset>
</div>

