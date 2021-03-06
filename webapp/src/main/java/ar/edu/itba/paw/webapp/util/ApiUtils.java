package ar.edu.itba.paw.webapp.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Stream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import ar.edu.itba.paw.webapp.exception.BadRequestException;
import com.google.gson.Gson;

import org.omg.CosNaming._BindingIteratorImplBase;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;

public class ApiUtils {

    public static String getLocale(HttpServletRequest request) {
        Locale locale = request.getLocale();
        String lang = locale.getLanguage();
        if (lang.equals("en")) return "en_US";
        else return "es_AR";
    }

    public static User loggedUser(HttpServletRequest request, UserService userService, Authentication auth) {
        if (auth == null) return null;
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            return null;
        }
        Optional<User> opUser = userService.findByUsername(auth.getName());
        if (opUser.isPresent()) {
            User user = opUser.get();
            String locale = getLocale(request);
            if (user.getLocale() == null || !user.getLocale().equalsIgnoreCase(locale)) {
                userService.updateLocale(user, locale);
            }
            return opUser.get();
        }
        return null;
    }

    public static String frontUri(UriInfo uriInfo) {
        String str = uriInfo.getBaseUri().toString();
        int idx = str.lastIndexOf("api/");
        return str.substring(0, idx);
    }

    public static Response paginatedListResponse(int amount, int pageSize, int page, UriInfo uriInfo, Collection<?> list, String query, Map<String, Object> data) {
        int lastPage = (int) Math.ceil((double) amount / (double) pageSize);
        if (lastPage == 0) lastPage = 1;

        Map<String, Object> json = new HashMap<>();
        json.put("amount", amount);
        json.put("pagesize", pageSize);
        json.put("pages", lastPage);
        json.put("list", list);
        if (data != null) json.putAll(data);

        final int firstPage = 1;
        final int prevPage  = (page == 1) ? 0 : page - 1;
        final int nextPage  = (page == lastPage) ? 0 : page + 1;

        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
        if (query != null) uriBuilder.replaceQuery(query);

        final URI first = uriBuilder.clone().queryParam("page", firstPage).build();
        final URI last  = uriBuilder.clone().queryParam("page", lastPage).build();

        Response.ResponseBuilder responseBuilder = Response.ok(new Gson().toJson(json));
        responseBuilder = responseBuilder.link(first, "first").link(last, "last");

        if (prevPage != 0 ) {
            final URI prev  = uriBuilder.clone().queryParam("page", prevPage).build();
            responseBuilder = responseBuilder.link(prev, "prev");
        }
        if (nextPage != 0 ) {
            final URI next  = uriBuilder.clone().queryParam("page", nextPage).build();
            responseBuilder = responseBuilder.link(next, "next");
        }

        return responseBuilder.build();
    }

    public static String readToken(Resource token) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> tokenStream = Files.lines(token.getFile().toPath(), StandardCharsets.UTF_8)) {
            tokenStream.forEach(contentBuilder::append);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static List<byte[]> cropImages(List<byte[]> photos) {
        List<byte[]> croppedList = new ArrayList<>();
        photos.forEach(photo ->{
            ByteArrayInputStream bis = new ByteArrayInputStream(photo);
            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(bis);
                int height = bufferedImage.getHeight(), width = bufferedImage.getWidth();

                BufferedImage cropped = bufferedImage;
                int diff = Math.abs(height-width);
                if(width>height){
                    cropped = bufferedImage.getSubimage(diff/2, 0, width-diff, height);
                }
                else{ if(width<height)
                    cropped = bufferedImage.getSubimage(0, diff/2, width, height-diff);
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(cropped, "jpg", baos );
                baos.flush();
                byte[] imageInByte = baos.toByteArray();
                baos.close();
                croppedList.add(imageInByte);
            } catch (IOException e) {
                throw new BadRequestException("Error cropping image");
            }
        });
        return croppedList;
    }
}
