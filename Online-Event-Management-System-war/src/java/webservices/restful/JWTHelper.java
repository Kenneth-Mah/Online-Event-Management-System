/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webservices.restful;

import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author lekhsian
 */
public class JWTHelper {

    //specify your own random secret String
    private static String SECRET = "asdioasg18923t120rbgflodigy10123489126421gjlkasgdlsa";

    public static Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET),
            SignatureAlgorithm.HS256.getJcaName());
}
