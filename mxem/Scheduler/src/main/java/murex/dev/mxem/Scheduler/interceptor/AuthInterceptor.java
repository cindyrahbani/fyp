package murex.dev.mxem.Scheduler.interceptor;


import murex.dev.mxem.Scheduler.exception.TokenNotValidException;
import murex.dev.mxem.Scheduler.util.BeanUtil;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class AuthInterceptor extends HandlerInterceptorAdapter {

    private DiscoveryClient discoveryClient= BeanUtil.getBean(DiscoveryClient.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
            try{
                String token = request.getHeader("Authorization");
                System.out.println("Bonjour");
                System.out.println(token);

                URI uri= discoveryClient.getInstances("Authorization")
                        .stream()
                        .map(si -> si.getUri())
                        .findFirst().get();

                URL url = new URL(uri.toURL(),"/validate");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty ("Authorization", token);
                conn.setRequestMethod("POST");

                if (conn.getResponseCode() !=200) {
                    throw new TokenNotValidException();
                }
                 return true;
            }
            catch (TokenNotValidException e) {
                e.printStackTrace();
                response.getWriter().write(e.getMessage());
            }
            return true;
    }

    public Boolean authUrl(String token) {

        try{
            URI uri= discoveryClient.getInstances("Authorization")
                    .stream()
                    .map(si -> si.getUri())
                    .findFirst().get();

            URL url = new URL(uri.toURL(),"/validate");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty ("Authorization", token);
            conn.setRequestMethod("POST");

            if (conn.getResponseCode() !=200) {
                return(false);
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public void validateToken(String token) throws TokenNotValidException {
        if(authUrl(token)==false){
            throw new TokenNotValidException();
        }
    }




}
