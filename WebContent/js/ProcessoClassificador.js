function buscaDados(posicao, tamanho) {
	if (posicao==null) posicao=$("#CaixaTextoPosicionar").val()-1;
	
	url = 'BuscaProcesso?AJAX=ajax&PaginaAtual=3&fluxo=1&Id_Classificador='+ document.getElementById('Id_Classificador').value 
	+ '&Id_ServentiaCargo=' + document.getElementById('Id_ServentiaCargo').value
	+ '&Id_ProcessoTipo=' + document.getElementById('Id_ProcessoTipo').value
	+ '&Id_Assunto=' + document.getElementById('Id_Assunto').value
	+ '&PosicaoPaginaAtual=' + posicao;
	
	var Id_Classificador = document.getElementById('Id_Classificador').value;
	var Id_ProcessoTipo = document.getElementById('Id_ProcessoTipo').value;
	var Id_Assunto = document.getElementById('Id_Assunto').value;
	var Id_ServentiaCargo = document.getElementById('Id_ServentiaCargo').value;
	
	if((Id_Classificador=="" || Id_Classificador=="undefined") && (Id_ProcessoTipo=="" || Id_ProcessoTipo=="undefined") && (Id_Assunto=="" || Id_Assunto=="undefined") && (Id_ServentiaCargo=="" || Id_ServentiaCargo=="undefined")){		
			mostrarMensagemErro('Projudi - Erro', 'Informe o Classificador, ou o Processo Tipo, ou o Assunto ou o Juiz Respons&aacute;vel.');
			return;
	}
	
	var corpoTabela = "";
	corpoTabela +='<td align="center"><input class="formEdicaoCheckBox" name="processos" type="checkbox" value="' + 'JsoNid' + '"></td>';
	corpoTabela +='<td width="130" align="center">';
	corpoTabela +='JsoNdesc1' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc2' + '</td>';
	corpoTabela +='<td>' + 'JsoNdesc3' + '</td>';
	corpoTabela +='<td class="lista_data">' + 'JsoNdesc4' + '</td>';
	corpoTabela +='<td align="center">';
	corpoTabela +='<input name="formLocalizarimgEditar" type="image"  src="./imagens/imgEditar.png" onclick="AlterarValue(\'PaginaAtual\',\'-1\'); AlterarValue(\'Id_Processo\',\'' + 'JsoNid' + '\');" >';     
	corpoTabela +='</td>';
	
	
	buscaDadosJSON(posicao, tamanho, url, corpoTabela, null, '#tabListaProcesso');

}