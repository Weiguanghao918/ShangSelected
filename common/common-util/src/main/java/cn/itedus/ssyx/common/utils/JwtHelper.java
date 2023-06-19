package cn.itedus.ssyx.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-16 17:03
 * @description:
 */

public class JwtHelper {

    private static long tokenExpiration = 365 * 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "ssyx";

    /**
     * 根据userId和userName生成token
     *
     * @param userId   用户ID
     * @param userName 用户名称
     * @return token
     */
    public static String createToken(Long userId, String userName) {
        String token = Jwts
                .builder()
                .setSubject("ssyx-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    /**
     * 根据token获取userId
     *
     * @param token token
     * @return userId
     */
    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        Jws<Claims> claimsJws = Jwts
                .parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer) claims.get("userId");
        return userId.longValue();
        // return 1L;
    }

    public static String getUserName(String token) {
        if (StringUtils.isEmpty(token)) {
            return "";
        }
        Jws<Claims> claimsJws = Jwts
                .parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("userName");
    }

    public static void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可。
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken(7L, "admin");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }
}