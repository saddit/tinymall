package jmu.shijh.tinymall.common.util;

import io.jsonwebtoken.*;

import java.util.Date;

public class Jwt {
    public static final String ISSUER = "shijh";
    public static final String SUBJECT = "who";

    /**
     * 设置token的过期时间 7天
     */
    public static final long EXPIRATION = Times.WEEK;

    /**
     * 秘钥，不同的环境应该配置不同的秘钥，注意保存好，不要泄露
     */
    public static final String APPSECRET_KEY = "shijh_secret";

    public static String generateToken(Long uid) {
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setSubject(SUBJECT)
                .claim("UID", uid)
                .setIssuedAt(Times.now())
                .setExpiration(Times.now(EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, APPSECRET_KEY).compact();
    }

    /**
     * 解密token获取唯一ID
     * @param token jwt token
     * @return key
     * @throws ExpiredJwtException 过时失效
     * @throws UnsupportedJwtException 非法类型
     * @throws SignatureException 签名非法
     */
    public static Long verifyToken(String token) throws JwtException {
        return Jwts.parser().setSigningKey(APPSECRET_KEY).parseClaimsJws(token).getBody().get("UID", Long.class);
    }

    public static Date getExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(APPSECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
