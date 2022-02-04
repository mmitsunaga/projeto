function mostrarBeneficio(id_parte){
	MostrarOcultar("sub_ben"+id_parte);
	if($('#sub_ben'+id_parte).is(":visible")){
		$('#sub_ben'+id_parte).html(gerarHtmlBeneficio("sub_ben"+id_parte,id_parte));
		atualizarTabelaPasso('ProcessoParteBeneficio', "sub_ben"+id_parte,'Info Pris&atilde;o','podeEditar',undefined);			
	}
}

function gerarHtmlBeneficio(id_form, id_parte){ 				  		
	var stHtml ="<fieldset class='VisualizaDados' ><legend>Benef&iacute;cio da Parte </legend>";
	stHtml +="	<input type='hidden' id='Id_ProcessoParte' name='Id_ProcessoParte' value='"+id_parte+"' />";
	stHtml +="	<input type='hidden' id='Id_ProcessoParteBeneficio' name='Id_ProcessoParteBeneficio' value='' />";
	stHtml +="	<input type='hidden' id='Id_ProcessoBeneficio' name='Id_ProcessoBeneficio' value='' />";	
																		
																						
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='id'>Identificador</label>";
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='Id'  id='Id' size='10' type='text'  readonly value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col25'>";
	stHtml +="		<label  for='ProcessoParteBeneficio'>Benef&iacute;cio<img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick=\"MostrarBuscaPadrao('" + id_form + "','ProcessoBeneficio','Consulta de Benef&iacute;cio', 'Digite o Benef&iacute;cio', 'Id_ProcessoBeneficio', 'ProcessoBeneficio', ['Benef&iacute;cio'], [], '2', '15')\" ></label>";
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='ProcessoBeneficio'  id='ProcessoBeneficio' size='45' type='text'  readonly value=''>";
	stHtml +="	</blockquote>	<br />";
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='DataPrisao'>Dt.Inicial<img class='imgMargeLeft' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick=\"displayCalendar(DataInicial,'dd/mm/yyyy',this)\"/></label>"; 
	stHtml +="		<input class='formEdicaoInput' name='DataInicial'  id='DataInicial' type='text' size='10' maxlength='10' onkeyup='mascara_data(this)' onblur='verifica_data(this)' onkeypress='return DigitarSoNumero(this, event)' value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='DataPrisao'>Dt.Final<img class='imgMargeLeft' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick=\"displayCalendar(DataFinal,'dd/mm/yyyy',this)\"/></label>"; 
	stHtml +="		<input class='formEdicaoInput' name='DataFinal'  id='DataFinal' type='text' size='10' maxlength='10' onkeyup='mascara_data(this)' onblur='verifica_data(this)' onkeypress='return DigitarSoNumero(this, event)' value=''>";
	stHtml +="	</blockquote>";	

	stHtml +="	<center class='col100'>";
	stHtml +="		<input type='button' value='Salvar' onclick=\"salvarPasso('ProcessoParteBeneficio','" + id_form + "','Info Pris&atilde;o','sim')\">";
	stHtml +="	</center>";
		
	stHtml +="	<blockquote class='col100'>";
	stHtml +="		<table  id='tb_sub_ben"+id_parte+"' class='Tabela' >";
	stHtml +="		    <thead>";
	stHtml +="			<tr>";
	stHtml +="		      	<th class='colunaMinima' >Id</th>";
	stHtml +="		        <th width='60px' >Benef&iacute;cio</th>";
	stHtml +="		        <th width='30px' >Dt. Inicial</th>";
	stHtml +="		        <th width='30px' >Dt. Data Final</th>";
	stHtml +="		        <th class='colunaMinima'>info</th>";
	stHtml +="	        	<th class='colunaMinima'>Editar</th>";
/*	stHtml +="		        <th class='colunaMinima'>Excluir</th>"; */
	stHtml +="		      </tr>";
	stHtml +="		    </thead>";
	stHtml +="		    <tbody>";
	stHtml +="		    </tbody>";
	stHtml +="		 </table>";
	stHtml +="	</blockquote>";		
	stHtml +=" </fieldset>";
	
	return stHtml;																		
}