package be.abis.sandwich.aspect;

import be.abis.sandwich.model.Credentials;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.hc.client5.http.utils.Base64;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.StringTokenizer;

@Component
@Aspect
public class UserInfoAspect {
    @Autowired(required = false)
    private HttpServletRequest request;

    @Autowired
    private Credentials credentials;

    private URI uri;
    public String userName;
    public String userPassword;

    @Before("execution(* be.abis.sandwich.controller.*.*(..))")
    public void getUserInfo(JoinPoint pjp) {
        System.out.println("----------");
        String up  = credentialsWithBasicAuthentication();
        if (up != null) {
            String[] cParts = up.split(":");
            credentials.setUserName(cParts[0]);
            credentials.setUserPassword(cParts[1]);
        }
        System.out.println("[Credentials] <" + credentials.getUserName() + "><" + credentials.getUserPassword() + ">");
        return;
    }

    private String credentialsWithBasicAuthentication() {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();

                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                        String credentials = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");
//                        System.out.println("Credentials: " + credentials);
                        int p = credentials.indexOf(":");
                        if (p != -1) {
                            String login = credentials.substring(0, p).trim();
                            String password = credentials.substring(p + 1).trim();

                            return credentials; // new Credentials(login, password);
                        } else {
                            System.out.println("Invalid authentication token");
                        }
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("Couldn't retrieve authentication : " + e);
                    }
                }
            }
        }

        return null;
    }
}
