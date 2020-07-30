package ar.edu.itba.paw.webapp.util;

import ar.edu.itba.paw.webapp.dto.RequestDto;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Locale;

public class ApiUtils {

    public static String getLocale() {
        Locale locale = LocaleContextHolder.getLocale();
        String lang = locale.getLanguage() + "_" + locale.getCountry();
        if (lang.startsWith("en")) return "en_US";
        else return "es_AR";
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
}
