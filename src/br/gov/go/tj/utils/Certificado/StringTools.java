package br.gov.go.tj.utils.Certificado;
import org.apache.log4j.Logger;

import br.gov.go.tj.utils.Funcoes;

/**
 * 
 * @author Leandro de Lima Lira
 * @author André Luis Cavalcanti Moreira
 * Esta classe tem a função de auxiliar na manipulação de octais.
 * Útil no tratamento de geração de certificados
 * 
 *
 */
public class StringTools {
    private static Logger log = Logger.getLogger(StringTools.class);


    /** Converts an IP-address string to octets of binary ints. 
     * ip is of form a.b.c.d, i.e. at least four octets
     * @param str string form of ip-address
     * @return octets, null if input format is invalid
     */
    public static byte[] ipStringToOctets(String str) {
        String[] toks = str.split("[.:]");
        if (toks.length == 4) {
            // IPv4 address
            byte[] ret = new byte[4];
            for (int i = 0;i<toks.length;i++) {
                int t = Funcoes.StringToInt(toks[i]);
                if (t>255) {
                    log.error("IPv4 address '"+str+"' contains octet > 255.");
                    return null;
                }
                ret[i] = (byte)t;
            }
            return ret;
        }
        if (toks.length == 8) {
            // IPv6 address
            byte[] ret = new byte[16];
            int ind = 0;
            for (int i = 0;i<toks.length;i++) {
                int t = Funcoes.StringToInt(toks[i]);
                if (t>0xFFFF) {
                    log.error("IPv6 address '"+str+"' contains part > 0xFFFF.");
                    return null;
                }
                int b1 = t & 0x00FF;
                ret[ind++] = (byte)b1;
                int b2 = t & 0xFF00;
                ret[ind++] = (byte)b2;
            }
        }
        log.error("Not a IPv4 OR IPv6 address.");
        return null;
    }
}
