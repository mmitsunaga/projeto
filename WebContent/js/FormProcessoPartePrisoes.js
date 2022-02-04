function mostrarPrisoes(id_parte, Id_Processo){
	MostrarOcultar("sub_pri"+id_parte);
	if($('#sub_pri'+id_parte).is(":visible")){
		$('#sub_pri'+id_parte).html(gerarHtmlPrisao("sub_pri"+id_parte,id_parte, Id_Processo));
		atualizarTabelaPasso('ProcessoPartePrisao', "sub_pri"+id_parte,'Info Pris&atilde;o','podeEditar',undefined);			
	}
}

function gerarHtmlPrisao(id_form, id_parte, Id_Processo){ 				  		
	var stHtml ="<fieldset class='VisualizaDados' ><legend> Pris&otilde;es da Parte </legend>";
	stHtml +="	<input type='hidden' id='Id_ProcessoParte' name='Id_ProcessoParte' value='"+id_parte+"' />";
	stHtml +="	<input type='hidden' id='Id_ProcessoPartePrisao' name='Id_ProcessoPartePrisao' value='' />";
	stHtml +="	<input type='hidden' id='Id_PrisaoTipo' name='Id_PrisaoTipo' value='' />";
	stHtml +="	<input type='hidden' id='Id_LocalCumpPena' name='Id_LocalCumpPena' value='' />";
	stHtml +="	<input type='hidden' id='Id_EventoTipo' name='Id_EventoTipo' value='' />";
	stHtml +="	<input type='hidden' id='Id_MoviEvento' name='Id_MoviEvento' value='' />";
	stHtml +="	<input type='hidden' id='Id_MoviPrisao' name='Id_MoviPrisao' value='' />";
	stHtml +="	<input type='hidden' id='Id_Processo' name='Id_Processo' value='"+Id_Processo+"' />";																			
																						
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='id'>Identificador</label>";
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='Id'  id='Id' size='10' type='text'  readonly value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='DataPrisao'>Dt.Pris&atilde;o<img class='imgMargeLeft' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick=\"displayCalendar(DataPrisao,'dd/mm/yyyy',this)\"/></label>"; 
	stHtml +="		<input class='formEdicaoInput' name='DataPrisao'  id='DataPrisao' type='text' size='10' maxlength='10' onkeyup='mascara_data(this)' onblur='verifica_data(this)' onkeypress='return DigitarSoNumero(this, event)' value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col25'>";
	stHtml +="		<label  for='id'>Pris&atilde;o Tipo <img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick=\"MostrarBuscaPadrao('" + id_form + "','PrisaoTipo','Consulta de Tipo de Pris&atilde;o', 'Digite o Tipo de Pris&atilde;o e clique em consultar.', 'Id_PrisaoTipo', 'PrisaoTipo', ['Pris&atilde;o'], [], '2', '15')\" ></label>";
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='PrisaoTipo'  id='PrisaoTipo' size='45' type='text'   value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='PrazoPrisao'>Prazo</label>";
	stHtml +="		<input class='formEdicaoInput' name='PrazoPrisao'  id='PrazoPrisao' maxlength='5' size='5' type='text' value='' onkeypress='return DigitarSoNumero(this, event)' onkeyup='autoTab(this,5)'>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col25'>";
	stHtml +="		<label  for='MoviPrisao'>Mov.Pris&atilde;o<img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick=\"MostrarBuscaPadrao('" + id_form + "','Movimentacao','Consulta de Movimenta&ccedil;&atilde;o do Processo', 'Digite a movimenta&ccedil;&atilde;o tipo', 'Id_MoviPrisao', 'MoviPrisao', ['Movimentaç&atilde;o Tipo'], [], '2', '15','Id_Processo')\" ></label>";
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='MoviPrisao'  id='MoviPrisao' size='45' type='text'  readonly value=''>";
	stHtml +="	</blockquote>	<br />";
	stHtml +="	<blockquote class='col35'>";
	stHtml +="		<label  for='LocalCumpPena'>Local<img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick=\"MostrarBuscaPadrao('" + id_form + "','LocalCumprimentoPena','Consulta de Local de Cumprimento de Pena', 'Digite o nome do Local.', 'Id_LocalCumpPena', 'LocalCumpPena', ['Local'], [], '2', '15')\" ></label>"; 
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='LocalCumpPena'  id='LocalCumpPena' size='60' type='text'  readonly value=''>";
	stHtml +="	</blockquote>";
/*	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='id'>Inf. Fam&iacute;lia</label>";
	stHtml +="		<select id='InformouFamiliares' name='InformouFamiliares' class='formEdicaoCombo'>";
	stHtml +="			<option value=''></option>";
	stHtml +="    		<option value='0'  >N&Atilde;O</option>";
	stHtml +="    		<option value='1'  >SIM</option>";
	stHtml +="		</select>";	
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='id'>Inf. Defensoria</label> ";
	stHtml +="		<select id='InformouDefensoria' name='InformouDefensoria' class='formEdicaoCombo'>";
	stHtml +="			<option value=''></option>";
	stHtml +="    		<option value='0'  >N&atilde;O</option>";
	stHtml +="    		<option value='1'  >SIM</option>";
	stHtml +="		</select>";
	stHtml +="	</blockquote>";*/
	stHtml +=" <fieldset class='VisualizaDados' ><legend> Encerramento</legend>";
	stHtml +="	<blockquote class='col10'>";
	stHtml +="		<label  for='DataEvento'>Dt.Evento<img class='imgMargeLeft' src='./imagens/dlcalendar_2.gif' height='13' width='13' title='Calendário'  alt='Calendário' onclick=\"displayCalendar(DataEvento,'dd/mm/yyyy',this);\"/></label>"; 
	stHtml +="		<input class='formEdicaoInput' name='DataEvento'  id='DataEvento' type='text' size='10' maxlength='10' onkeyup='mascara_data(this)' onblur='verifica_data(this)' onkeypress='return DigitarSoNumero(this, event)' value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col25'>";
	stHtml +="		<label  for='EventoTipo'>EventoTipo<img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick=\"MostrarBuscaPadrao('" + id_form + "','EventoTipo','Consulta de Tipo de Evento', 'Digite o Tipo de Evento e clique em consultar.', 'Id_EventoTipo', 'EventoTipo', ['Evento Tipo'], [], '2', '15'); \" ></label>"; 
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='EventoTipo'  id='EventoTipo' size='45' type='text'  readonly value=''>";
	stHtml +="	</blockquote>";
	stHtml +="	<blockquote class='col25'>";
	stHtml +="		<label  for='id'>Mov.Evento<img class='imgMargeLeft' src='./imagens/imgLocalizarPequena.png' onclick=\"MostrarBuscaPadrao('" + id_form + "','Movimentacao','Consulta de Movimenta&ccedil;&atilde;o do Processo', 'Digite a movimenta&ccedil;&atilde;o tipo', 'Id_MoviEvento', 'MoviEvento', ['Movimentaç&atilde;o Tipo'], [], '2', '15','Id_Processo'); \" ></label>";
	stHtml +="		<input class='formEdicaoInputSomenteLeitura' name='MoviEvento'  id='MoviEvento' size='45' type='text'  readonly value=''>";
	stHtml +="	</blockquote>";
	stHtml +=" </fieldset>";
	stHtml +="	<blockquote class='col60'>";
	stHtml +="		<label  for='Email'>*Observacao</label>";
	stHtml +="		<input class='formEdicaoInput' name='Observacao' id='Observacao'  type='text' size='60' maxlength='255' value='' onkeyup='autoTab(this,60)'>";
	stHtml +="	</blockquote>";
	stHtml +="	<center class='col100'>";
	stHtml +="		<input type='button' value='Salvar' onclick=\"salvarPasso('ProcessoPartePrisao','" + id_form + "','Info Pris&atilde;o','sim')\">";
	stHtml +="	</center>";
			
	stHtml +="	<blockquote class='col100'>";
	stHtml +="		<table  id='tb_sub_pri"+id_parte+"' class='Tabela' >";
	stHtml +="		    <thead>";
	stHtml +="			<tr>";
	stHtml +="		      	<th class='colunaMinima' >Id</th>";
	stHtml +="		        <th width='30px' >Dt. Pris&atilde;o</th>";
	stHtml +="		        <th width='60px' >Local</th>";
	stHtml +="		        <th width='30px' >Prazo</th>";
	stHtml +="		        <th width='50px' >Mov. Pris&atilde;o</th>";
	stHtml +="		        <th width='50px' >Mov. Evento</th>";
	stHtml +="		        <th width='30px' >Dt. Evento</th>";
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