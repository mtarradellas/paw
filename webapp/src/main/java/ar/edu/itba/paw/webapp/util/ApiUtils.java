package ar.edu.itba.paw.webapp.util;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;

public class ApiUtils {

    public static String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
    }

    public User loggedUser(UserService userService, Authentication auth) {
        if (auth == null) return null;
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))) {
            return null;
        }
        Optional<User> opUser = userService.findByUsername(auth.getName());
        if (opUser.isPresent()) {
            User user = opUser.get();
            String locale = getLocale();
            if (user.getLocale() == null || !user.getLocale().equalsIgnoreCase(locale)) {
                userService.updateLocale(user, locale);
            }
            return opUser.get();
        }
        return null;
    }

    public static Response paginatedListResponse(int amount, int pageSize, int page, UriInfo uriInfo, Object genericEntity) {
        final int firstPage = 1;
        final int lastPage  = (int) Math.ceil((double) amount / pageSize);
        final int prevPage  = (page == 1) ? lastPage : page - 1;
        final int nextPage  = (page == lastPage) ? firstPage : page + 1;

        final URI first = uriInfo.getAbsolutePathBuilder().queryParam("page", firstPage).build();
        final URI last  = uriInfo.getAbsolutePathBuilder().queryParam("page", lastPage).build();
        final URI prev  = uriInfo.getAbsolutePathBuilder().queryParam("page", prevPage).build();
        final URI next  = uriInfo.getAbsolutePathBuilder().queryParam("page", nextPage).build();

        return javax.ws.rs.core.Response.ok(genericEntity)
                .link(first, "first")
                .link(last, "last")
                .link(prev, "prev")
                .link(next, "next")
                .build();
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
}
