function EvitarEspacoDuplo(campo) {
	var intIndexOfMatch = campo.value.indexOf("  ");
	while (intIndexOfMatch != -1){
  		campo.value = campo.value.replace( "  ", " " );
  		intIndexOfMatch = intIndexOfMatch = campo.value.indexOf("  ");
	}
}