package com.suyash.authservice.service;

public class JwtService {

    public static final String SECRET_KEY = "aVeryStrongSecretKeyThatIsAtLeast32CharactersLongForJWT";

    public String extractUsername(String token){
        return extractClaim(token , Claims::getSubject);
    
    }

    public <T> T extractClaim(String token , Function<Claims , T> claimResolver){
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
    
    private <T> T extractAllClaims(String token){
        return Jwts.parser().setSigningKey(getSignKey()).bulid().parserClaimsJws(token).getBody();
    }

    private Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
