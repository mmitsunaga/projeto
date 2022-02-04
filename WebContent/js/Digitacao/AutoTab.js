// JavaScript Document
function autoTab(campo,tamanho){
	if (campo.value.length==tamanho) 
		campo.form[(getIndex(campo)+1) % campo.form.length].focus();
}

function getIndex(input) {
	var index = -1, i = 0, found = false;
	while (i < input.form.length && index == -1)
		if (input.form[i] == input)index = i;
			else i++;
	return index;
}