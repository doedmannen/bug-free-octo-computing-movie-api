package com.movienights.api.services;
import com.movienights.api.entities.Log;
import com.movienights.api.repos.LogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Service
public class LogServices {

    @Autowired
    LogRepo logRepo;

    public void Loga(HttpServletRequest httpServletRequest , HttpServletResponse httpServletResponse){
        Log l = new Log();
        l.setUrl(httpServletRequest.getRequestURI());
        l.setQuery(httpServletRequest.getQueryString());
        Calendar calendar = Calendar.getInstance();
        long time = calendar.getTimeInMillis();
        l.setWhen(time);
        if(l.getUrl().startsWith("/js") || l.getUrl().startsWith("/favicon.ico") || l.getUrl().startsWith("/css/")){
        }else{
            if(getIP(httpServletRequest) != null){
                l.setStatuscode(httpServletResponse.getStatus());
                l.setIp(getIP(httpServletRequest));
                logRepo.save(l);
            }
        }
    }

    public String getIP(HttpServletRequest httpServletRequest) {
        String remoteAddr = "";
        if (httpServletRequest != null) {
            remoteAddr = httpServletRequest.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = httpServletRequest.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
