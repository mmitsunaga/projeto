package br.gov.go.tj.utils;

public class CpfHelper {

	public static String removeMascara(String cpfComMascara) throws IllegalArgumentException {
		if(cpfComMascara == null || cpfComMascara.trim().isEmpty())
			return null;
		if(cpfComMascara.length() != 14){
			throw new IllegalArgumentException("CPF inválido " + cpfComMascara + ". Deve conter 14 caracteres no formato 999.999.999-99");
		}
		return cpfComMascara.substring(0, 3) + cpfComMascara.substring(4, 7) + cpfComMascara.substring(8, 11) + cpfComMascara.substring(12, 14);
	}

	public static String adicionaMascara(String cpfSemMascara) throws IllegalArgumentException {
		if(cpfSemMascara == null || cpfSemMascara.trim().isEmpty())
			return null;
		if(cpfSemMascara.length() != 11){
			throw new IllegalArgumentException("CPF inválido " + cpfSemMascara + ". Deve conter 11 digitos numéricos.");
		}
		return cpfSemMascara.substring(0, 3) + "." + cpfSemMascara.substring(3, 6) + "." + cpfSemMascara.substring(6, 9) + "-" + cpfSemMascara.substring(9, 11);
	}

	public static boolean isCpfValido(String cpf) {
		if(cpf == null || cpf.trim().isEmpty() || cpf.length() != 11 || cpf.equals("11111111111") || cpf.equals("22222222222") || cpf.equals("33333333333")
				|| cpf.equals("44444444444") || cpf.equals("55555555555") || cpf.equals("66666666666") || cpf.equals("77777777777")
				|| cpf.equals("88888888888") || cpf.equals("99999999999") || cpf.equals("00000000000")){
			return false;
		}
		String recalculado = cpf.substring(0, 9);
		recalculado += calculaDVParaCpf(recalculado);
		recalculado += calculaDVParaCpf(recalculado);
		return cpf.equals(recalculado);
	}
	
	private static String calculaDVParaCpf(String codigo) {
		int soma = 0;
		int tamanho = codigo.length();
		int peso = tamanho + 1;
		for(int i = 0; i < tamanho; i++){
			soma += Integer.parseInt(codigo.substring(i, i + 1)) * peso;
			peso--;
		}
		int resto = (soma % 11);
		if(resto < 2){
			return "0";
		}
		else{
			return Integer.valueOf(11 - resto).toString();
		}
	}
}