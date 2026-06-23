package ua.chekmaryov.barber_stat.util;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

public class SpecificationUtil {

    public static <S, T> Specification<S> equals(Function<Root<S>, Expression<T>> pathExtractor, T value) {
        if (value == null) {
            return null;
        }

        return (root, _, cb) -> cb.equal(pathExtractor.apply(root), value);
    }

    public static <S, Y extends Comparable<? super Y>> Specification<S> between(
            Function<Root<S>, Expression<Y>> pathExtractor, Y start, Y end) {

        if (start == null && end == null) {
            return null;
        }

        return (root, _, cb) -> {
            Expression<Y> expr = pathExtractor.apply(root);

            if (start != null && end != null) {
                if (start.compareTo(end) > 0) {
                    throw new IllegalArgumentException("Start value cannot be strictly greater than end value");
                }
                return cb.between(expr, start, end);
            }
            return start != null
                    ? cb.greaterThanOrEqualTo(expr, start)
                    : cb.lessThanOrEqualTo(expr, end);
        };
    }
}
