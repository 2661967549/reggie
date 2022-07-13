package com.zyp.filter;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zyp.common.BaseContext;
import com.zyp.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 使用过滤器来检测用户是否登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {

    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        //0.不需要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/user/sendMsg",
                "/user/login"
        };

        //1.获取本次请求URI
        String requestURI = request.getRequestURI();

        //2.判断本次请求 是否 不用 处理
        if (check(urls,requestURI)){
            filterChain.doFilter(request,response);
            //退出方法
            return;

        //3-1.判断 后台 登录状态，如果已经登录，则放行
        }else if (request.getSession().getAttribute("employee") != null){

            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            //退出方法
            return;

            //3-2.判断 用户端 登录状态，如果已经登录，则放行
        }else if (request.getSession().getAttribute("user") != null){

            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request,response);
            //退出方法
            return;

            //4.未登录，通过输出流返回未登录的结果，让前端自己跳转页面
        }else{
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(R.error("NOTLOGIN")));
            return;
        }
    }

    /**
     * 判断是否需要放行
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI){
        for (String url :urls){
           if (PATH_MATCHER.match(url,requestURI)){
               return true;
           }
        }
        return false;
    }
}




























