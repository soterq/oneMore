package com.unifun.voice.jwt;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.*;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;

import com.unifun.voice.helpers.Constants;
import com.unifun.voice.jsonmapper.model.TokenResponse;
import com.unifun.voice.jsonmapper.model.User;
import com.unifun.voice.orm.repository.SessionTableRepository;
import io.jsonwebtoken.*;

@RequestScoped
@Path("/login")
public class LoginUser {
    private static Map<String, String> refreshTokens; //singleton
    // TOKEN DATA
    @Inject
    SessionTableRepository str;

    public static Map<String,String> getRefreshTokenInstance() {
        if(refreshTokens == null) {
            refreshTokens =  new HashMap<String, String>();
        }
        return refreshTokens;
    }

    @POST
    public String getUserData(String req) {
        User user = ldapLogin(req);
        if(user != null) {
            String token = generateToken("AngularFE001","JavaBE001", "Auth-Token", 1000*10*1, user);
            // refresh_token needs to hide user identity
            String refreshToken = generateToken("AngularFE001","JavaBE001", "Auth-Token", 1000*16*1, user);
            addTokenToList(user, refreshToken);
            return tokenResponse(token, refreshToken);
        }
        return "notok";
    }

    private User ldapLogin(String reqBody) {
        Jsonb jsonb = JsonbBuilder.create();
        User user = jsonb.fromJson(reqBody, User.class);
        String username = user.getUsername();
        String password = user.getPassword();

        // ldapCheck
        try {
            Hashtable<String, String> env = new Hashtable<String, String>();
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put(Context.PROVIDER_URL,"ldap://localhost:389");
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.SECURITY_PRINCIPAL,"cn="+username+",ou=users, dc=fun,dc=in");
            env.put(Context.SECURITY_CREDENTIALS,password);
            DirContext ctx = new InitialDirContext(env);
            ctx.close();
            return user;
        }
        catch(Exception e) {
            return null;
        }
    }

    private  String generateToken(String id, String issuer, String subject, long ttlMillis, User user) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Constants.SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration, ex: 60 * 1000 * 1 = 1 min
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        String token = builder.compact();
        return token;
    }

    private void addTokenToList(User user, String refreshToken) {
        LoginUser.getRefreshTokenInstance().put(user.getUsername(), refreshToken);
    }

    private String tokenResponse(String token, String refreshToken) {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setJwt(token);
        tokenResponse.setRefreshToken(refreshToken);
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(tokenResponse);
    }

//    private String genUUID() {
//        String resultUUID = UUID.randomUUID().toString();
//        resultUUID += UUID.randomUUID().toString();
//        resultUUID += UUID.randomUUID().toString();
//        System.out.println(resultUUID);
//        return resultUUID;
//    }

}