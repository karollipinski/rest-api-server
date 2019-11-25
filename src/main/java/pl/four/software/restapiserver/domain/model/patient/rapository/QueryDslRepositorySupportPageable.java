package pl.four.software.restapiserver.domain.model.patient.rapository;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public abstract class QueryDslRepositorySupportPageable extends QuerydslRepositorySupport {

    public QueryDslRepositorySupportPageable(Class<?> domainClass) {
        super(domainClass);
    }

    protected <T> PageImpl<T> pagination(JPQLQuery<T> query, Pageable pageable) {

        long total = query.fetchCount();
        List<T> content = getQuerydsl().applyPagination(pageable, query)
                                       .fetch();
        return new PageImpl<>(content, pageable, total);
    }

}
