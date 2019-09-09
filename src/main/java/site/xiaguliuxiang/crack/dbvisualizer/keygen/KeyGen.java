package site.xiaguliuxiang.crack.dbvisualizer.keygen;

import site.xiaguliuxiang.crack.dbvisualizer.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author xiaguliuxiang@gmail.com
 * @date 2019-09-09 20:00:00
 */
public class KeyGen {
    private static final String PRIVATE_KEY_STR = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUDQ1V7xwyJiSP80BgqElHYP6bZzk=";
    private static final PrivateKey PRIVATE_KEY;

    static {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.decode(PRIVATE_KEY_STR));
            PRIVATE_KEY = keyFactory.generatePrivate(privateKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateLicense(String licenseName, String licenseOrg)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Map<String, String> map = prepareLicense(licenseName, licenseOrg);
        map.put("dbvis.license.seal", signLicense(map));
        return license2String(map);
    }

    private static Map<String, String> prepareLicense(String licenseName, String licenseOrg) {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("dbvis.license.product", "DbVisualizer");
        map.put("dbvis.license.edition", "Personal");
        map.put("dbvis.license.version", "2019.09.09");
        map.put("dbvis.license.id", "侠骨留香 / Www.ChinaPYG.CoM");
        map.put("dbvis.license.name", licenseName);
        map.put("dbvis.license.org", licenseOrg);
        map.put("dbvis.license.activation", "2019-09-09");
        map.put("dbvis.license.expire", "2099-12-31");
        map.put("dbvis.license.upgrade.expire", "2099-12-31");
        return map;
    }

    private static String signLicense(Map<String, String> map)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA1withDSA");
        signature.initSign(PRIVATE_KEY);
        map.forEach((k, v) -> {
            try {
                signature.update(String.join("", k, v).getBytes(StandardCharsets.ISO_8859_1));
            } catch (SignatureException e) {
                throw new RuntimeException(e.getCause());
            }
        });
        return Base64.encodeToString(signature.sign());
    }

    private static String license2String(Map<String, String> map) {
        StringBuilder licenseBuilder = new StringBuilder("# DbVisualizer Pro License - 侠骨留香/ChinaPYG 2019\n");
        map.forEach((k, v) -> licenseBuilder.append(k + "=" + v).append("\n"));
        return licenseBuilder.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String license = generateLicense("侠骨留香", "ChinaPYG");
        System.out.println(license);
    }
}
