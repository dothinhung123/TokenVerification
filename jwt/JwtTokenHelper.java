package com.go.tokenverification.jwt;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Slf4j
@Component
public class JwtTokenHelper {

    private JWSSigner jwsSigner;

    private JWSVerifier jwsVerifier;

    public JwtTokenHelper() throws JOSEException {
        initialize();
    }
    public void initialize() throws JOSEException {
        jwsSigner = new MACSigner(JwtConstants.JWT_RSK.getBytes());
        jwsVerifier = new MACVerifier(JwtConstants.JWT_RSK.getBytes());
    }
    public String createToken(String payload , long expireTime) throws JOSEException {

        ZonedDateTime zdt = ZonedDateTime.now();

        //prepare JWT with claim set
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(payload)
                .issuer("auth")
                .expirationTime(Date.from(zdt.plusMinutes(expireTime).toInstant()))
                .issueTime(Date.from(zdt.toInstant()))
                .build();
        //JWT  prepared and ready to be signed
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256),jwtClaimsSet);

        //apply HMAC protection. JWT signing
        signedJWT.sign(jwsSigner);

        return signedJWT.serialize();

    }

    public String getPayload(String token) throws ParseException, JOSEException, JwtExpiredException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        if(!signedJWT.verify(jwsVerifier)){
            log.warn("Signature verification failed {}", signedJWT);
            throw new JOSEException("Signature verification failed");
        }

        JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
        if(jwtClaimsSet.getExpirationTime()==null){
            log.error("No expiration time on SignedJWT claimset {}", jwtClaimsSet);
            throw new JOSEException("No expiration time on SignedJWT claimset");
        }

        ZonedDateTime jwtExpirationTime = ZonedDateTime.ofInstant(jwtClaimsSet.getExpirationTime().toInstant(), ZoneOffset.UTC);
		ZonedDateTime currentTime = LocalDateTime.now().atZone(ZoneOffset.UTC);

//		if(jwtExpirationTime.isBefore(currentTime.minus(5, ChronoUnit.MINUTES))) { // 2 mins time sync grace period
//			throw new JwtExpiredException("JWT time expired", jwtClaimsSet.getSubject(), jwtExpirationTime);
//		}

        return  jwtClaimsSet.getSubject();
    }
}
