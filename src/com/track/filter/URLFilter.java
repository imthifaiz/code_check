package com.track.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.util.SendRedirectOverloadedResponse;



public class URLFilter implements Filter{
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub		
		System.out.println("Before Request :: UtfFilter.doFilter()");
		chain.doFilter(request,  new SendRedirectOverloadedResponse(request, response));
		System.out.println("After Request :: URLFilter.doFilter()");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
