package tr.com.argela.BackgammonGame.be.service;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class RequestServiceImpl implements RequestService{

    private final String LOCALHOST_IP4 = "127.0.0.1";
    private final String LOCALHOST_IP6 = "0:0:0:0:0:0:0:1";
    private final String UNKNOWN = "unknown";

    @Override
    public String getClientIpAddress(HttpServletRequest request) {

        String clientIpAddress = request.getHeader("X-Forwarded-For");
        if(StringUtils.hasLength(clientIpAddress) || UNKNOWN.equals(clientIpAddress)){
            clientIpAddress = request.getHeader("Proxy-Client-Ip");
        }
        if(!StringUtils.hasLength(clientIpAddress) || UNKNOWN.equals(clientIpAddress)){
            clientIpAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if(!StringUtils.hasLength(clientIpAddress) || UNKNOWN.equals(clientIpAddress)){
            clientIpAddress = request.getRemoteAddr();
            if(LOCALHOST_IP4.equals(clientIpAddress)|| LOCALHOST_IP6.equals(clientIpAddress)){
                try{
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    clientIpAddress = inetAddress.getHostAddress();
                }catch(UnknownHostException e){
                    e.printStackTrace();
                }
            }
        }

        if(StringUtils.hasLength(clientIpAddress)&& clientIpAddress.length()>15&&clientIpAddress.indexOf(",")>0){
            clientIpAddress = clientIpAddress.substring(0, clientIpAddress.indexOf(","));

        }
        return clientIpAddress;
    }
    
}
