package de.planerio.developertest.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageableUtil {

    public static Pageable getPageable(final Integer page, final Integer pageSize) {
        final Pageable pageable;

        if ((page == null || page.equals(0)) ||
                (pageSize == null || pageSize.equals(0))) {

            pageable = Pageable.unpaged();

        } else {
            pageable = PageRequest.of(page-1, pageSize);
        }

        return pageable;
    }
}
