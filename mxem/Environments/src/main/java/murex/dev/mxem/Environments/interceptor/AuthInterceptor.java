package murex.dev.mxem.Environments.interceptor;

import lombok.extern.slf4j.Slf4j;
import murex.dev.mxem.Environments.exception.TokenNotValidException;
import murex.dev.mxem.Environments.util.BeanUtil;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

@Slf4j
public class AuthInterceptor extends HandlerInterceptorAdapter {

    private DiscoveryClient discoveryClient= BeanUtil.getBean(DiscoveryClient.class);

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
            try{
                String token = request.getHeader("Authorization");

                System.out.println("this is the request !!!!!!!!!!!");
                System.out.println(request.getRequestURL());
                System.out.println(request.getRemoteAddr());
                System.out.println(request.getRemotePort());
                System.out.println(request.getRemoteUser());
                System.out.println("Bonjour");

                System.out.println("this is the token !!!!! : ");
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
                    log.info("L'erreur est ici :(( ");
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
