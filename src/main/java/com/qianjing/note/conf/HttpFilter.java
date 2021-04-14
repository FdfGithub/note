package com.qianjing.note.conf;

import com.qianjing.note.common.RequestHolder;
import com.qianjing.note.pojo.User;
import com.qianjing.note.vo.UserInfoVO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class HttpFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        UserInfoVO userInfoVO = (UserInfoVO) request.getSession().getAttribute("user");
        if (userInfoVO!=null){
            RequestHolder.add(userInfoVO.getId());
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
