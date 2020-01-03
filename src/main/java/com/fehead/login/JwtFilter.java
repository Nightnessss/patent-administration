package com.fehead.login;


import com.fehead.error.EmBusinessError;
import io.jsonwebtoken.Jwts;

import javax.security.auth.login.LoginException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 写代码 敲快乐
 * だからよ...止まるんじゃねぇぞ
 * ▏n
 * █▏　､⺍
 * █▏ ⺰ʷʷｨ
 * █◣▄██◣
 * ◥██████▋
 * 　◥████ █▎
 * 　　███▉ █▎
 * 　◢████◣⌠ₘ℩
 * 　　██◥█◣\≫
 * 　　██　◥█◣
 * 　　█▉　　█▊
 * 　　█▊　　█▊
 * 　　█▊　　█▋
 * 　　 █▏　　█▙
 * 　　 █
 *
 * @author Nightnessss 2019/12/28 23:25
 */
@WebFilter(filterName = "JwtFilter", urlPatterns = {"/patent/*"})
public class JwtFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;
        //等到请求头信息authorization信息
        final String authHeader = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            chain.doFilter(req, res);
        } else {

            if (authHeader == null || !authHeader.startsWith("bearer ")) {

                request.getRequestDispatcher("/login/fail").forward(request, response);
                return;
            }
//            else {
//                String username = Jwts.parser()
//                        .setSigningKey("LoginUser")
//                        .parseClaimsJws(authHeader.replace("bearer ", ""))
//                        .getBody()
//                        .getSubject();
//                System.out.println(username);
//            }


            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {

    }
}
