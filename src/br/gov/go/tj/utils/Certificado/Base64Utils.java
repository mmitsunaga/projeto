package br.gov.go.tj.utils.Certificado;

public class Base64Utils {

    private static byte[] mBase64EncMap, mBase64DecMap;

    /**
     * Inicia o alfabeto Base64 (especificado no RFC-2045).
     */
    static {
        byte[] base64Map = {
            (byte)'A', (byte)'B', (byte)'C', (byte)'D', (byte)'E', (byte)'F',
            (byte)'G', (byte)'H', (byte)'I', (byte)'J', (byte)'K', (byte)'L',
            (byte)'M', (byte)'N', (byte)'O', (byte)'P', (byte)'Q', (byte)'R',
            (byte)'S', (byte)'T', (byte)'U', (byte)'V', (byte)'W', (byte)'X',
            (byte)'Y', (byte)'Z',
            (byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f',
            (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l',
            (byte)'m', (byte)'n', (byte)'o', (byte)'p', (byte)'q', (byte)'r',
            (byte)'s', (byte)'t', (byte)'u', (byte)'v', (byte)'w', (byte)'x',
            (byte)'y', (byte)'z',
            (byte)'0', (byte)'1', (byte)'2', (byte)'3', (byte)'4', (byte)'5',
            (byte)'6', (byte)'7', (byte)'8', (byte)'9', (byte)'+', (byte)'/' };
        mBase64EncMap = base64Map;
        mBase64DecMap = new byte[128];
        for (int i=0; i<mBase64EncMap.length; i++)
            mBase64DecMap[mBase64EncMap[i]] = (byte) i;
    }

    /**
     * Esta classe não deve ser iniciada.
     */
    private Base64Utils() {
    }

    /**
     * Codifica um array de byte em Base64,
     * como especificado no RFC-2045 (Seção 6.8).
     *
     * @param dados o dado a ser codificado
     * @return <var>dados</var> codificado em Base64
     * @exception IllegalArgumentException se NULL ou um array vazio é fornecido
     */
    public static String base64Encode(byte[] dados) {
        if ((dados == null) || (dados.length == 0))
            throw new IllegalArgumentException("<{Não é possível codificar NULL ou um array vazio.}> Local Exception: Base64Utils.base64Encode()");

        byte encodedBuf[] = new byte[((dados.length+2)/3)*4];

        int srcIndex, destIndex;
        for (srcIndex=0, destIndex=0; srcIndex < dados.length-2; srcIndex += 3) {
            encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex] >>> 2) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex+1] >>> 4) & 017 | (dados[srcIndex] << 4) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex+2] >>> 6) & 003 | (dados[srcIndex+1] << 2) & 077];
            encodedBuf[destIndex++] = mBase64EncMap[dados[srcIndex+2] & 077];
        }

        if (srcIndex < dados.length) {
            encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex] >>> 2) & 077];
            if (srcIndex < dados.length-1) {
                encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex+1] >>> 4) & 017 | (dados[srcIndex] << 4) & 077];
                encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex+1] << 2) & 077];
            } else {
                encodedBuf[destIndex++] = mBase64EncMap[(dados[srcIndex] << 4) & 077];
            }
        }

        while (destIndex < encodedBuf.length) {
            encodedBuf[destIndex] = (byte) '=';
            destIndex++;
        }

        String result = new String(encodedBuf);
        return result;
    }


    /**
     * Descodifica um array de byte em Base64,
     * como especificado no RFC-2045 (Seção 6.8).
     *
     * @param dados o dado a ser descodificado
     * @return <var>dados</var> descodificado da Base64
     * @exception IllegalArgumentException se NULL ou um array vazio é fornecido
     */
    public static byte[] base64Decode(String dados) {
        if ((dados == null) || (dados.length() == 0))
            throw new IllegalArgumentException("<{Não é possível codificar NULL ou um array vazio.}> Local Exception: Base64Utils.base64Decode()");

        byte[] data = dados.getBytes();

        int tail = data.length;
        while (data[tail-1] == '=')
            tail--;

        byte decodedBuf[] = new byte[tail - data.length/4];

        for (int i = 0; i < data.length; i++)
            data[i] = mBase64DecMap[data[i]];

        int srcIndex, destIndex;
        for (srcIndex = 0, destIndex=0; destIndex < decodedBuf.length-2;
                srcIndex += 4, destIndex += 3) {
            decodedBuf[destIndex] = (byte) ( ((data[srcIndex] << 2) & 255) | ((data[srcIndex+1] >>> 4) & 003) );
            decodedBuf[destIndex+1] = (byte) ( ((data[srcIndex+1] << 4) & 255) | ((data[srcIndex+2] >>> 2) & 017) );
            decodedBuf[destIndex+2] = (byte) ( ((data[srcIndex+2] << 6) & 255) | (data[srcIndex+3] & 077) );
        }

        if (destIndex < decodedBuf.length)
            decodedBuf[destIndex] = (byte) ( ((data[srcIndex] << 2) & 255) | ((data[srcIndex+1] >>> 4) & 003) );
        if (++destIndex < decodedBuf.length)
            decodedBuf[destIndex] = (byte) ( ((data[srcIndex+1] << 4) & 255) | ((data[srcIndex+2] >>> 2) & 017) );

        return decodedBuf;
    }

}
