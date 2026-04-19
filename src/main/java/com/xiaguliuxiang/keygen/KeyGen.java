package com.xiaguliuxiang.keygen;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

public class KeyGen {

    private static final String MY_RSA_PRIVATE_KEY_BASE64 = "MIIBSwIBADCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdrmVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXzrith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+GghdabPd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6EwoFhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhRkImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoEFgIUDQ1V7xwyJiSP80BgqElHYP6bZzk=";
    private static final PrivateKey MY_RSA_PRIVATE_KEY;

    static {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("DSA");
            EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(MY_RSA_PRIVATE_KEY_BASE64));
            MY_RSA_PRIVATE_KEY = keyFactory.generatePrivate(privateKeySpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateLicense(String licenseId, String licenseName, String licenseOrg)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Map<String, String> map = prepareLicense(licenseId, licenseName, licenseOrg);
        map.put("dbvis.license.seal", signLicense(map));
        return license2String(map, licenseName, licenseOrg);
    }

    private static Map<String, String> prepareLicense(String licenseId, String licenseName, String licenseOrg) {
        SortedMap<String, String> map = new TreeMap<>();
        map.put("dbvis.license.product", "DbVisualizer");
        map.put("dbvis.license.edition", "Personal");
        map.put("dbvis.license.version", "2019.09.09");
        map.put("dbvis.license.id", licenseId);
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
        signature.initSign(MY_RSA_PRIVATE_KEY);
        map.forEach((k, v) -> {
            try {
                signature.update(String.join("", k, v).getBytes(StandardCharsets.ISO_8859_1));
            } catch (SignatureException e) {
                throw new RuntimeException(e.getCause());
            }
        });
        return Base64.getEncoder().encodeToString(signature.sign());
    }

    private static String license2String(Map<String, String> map, String licenseName, String licenseOrg) {
        StringBuilder licenseBuilder = new StringBuilder("# DbVisualizer Pro License - " + licenseName + "/" + licenseOrg + System.lineSeparator());
        map.forEach((k, v) -> licenseBuilder.append(k).append("=").append(v).append(System.lineSeparator()));
        return licenseBuilder.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        String license = generateLicense("侠骨留香/Www.ChinaPYG.CoM", "侠骨留香", "ChinaPYG");
        System.out.println(license);
    }

    public static String randomString(int length) {
        String baseStr = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(baseStr.charAt(ThreadLocalRandom.current().nextInt(baseStr.length())));
        }
        return sb.toString();
    }

}
