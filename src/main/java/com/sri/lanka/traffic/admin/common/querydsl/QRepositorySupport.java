package com.sri.lanka.traffic.admin.common.querydsl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringPath;

public class QRepositorySupport {

	private QRepositorySupport() {
	}

	public static <T> OrderSpecifier[] makeOrderSpecifiers(final EntityPathBase<T> qClass, final Pageable pageable) {
		return pageable.getSort().stream().map(sort -> toOrderSpecifier(qClass, sort)).collect(Collectors.toList())
				.toArray(OrderSpecifier[]::new);
	}

	private static <T> OrderSpecifier toOrderSpecifier(final EntityPathBase<T> qClass, final Sort.Order sortOrder) {
		final Order orderMethod = toOrder(sortOrder);
		final PathBuilder<T> pathBuilder = new PathBuilder<>(qClass.getType(), qClass.getMetadata());
		return new OrderSpecifier(orderMethod, pathBuilder.get(sortOrder.getProperty()));
	}

	private static Order toOrder(final Sort.Order sortOrder) {
		if (sortOrder.isAscending()) {
			return Order.ASC;
		}
		return Order.DESC;
	}

	public static BooleanExpression containsKeyword(final StringPath stringPath, final String keyword) {
		if (Objects.isNull(keyword) || keyword.isBlank()) {
			return null;
		}
		return stringPath.contains(keyword);
	}

	public static <T> BooleanExpression toEqExpression(final SimpleExpression<T> simpleExpression, final T compared) {
		if (Objects.isNull(compared)) {
			return null;
		}
		return simpleExpression.eq(compared);
	}

	public static <T> Slice<T> toSlice(final Pageable pageable, final List<T> items) {
		if (items.size() > pageable.getPageSize()) {
			items.remove(items.size() - 1);
			return new SliceImpl<>(items, pageable, true);
		}
		return new SliceImpl<>(items, pageable, false);
	}

	public static BooleanExpression toGoeExpression(DateTimePath<LocalDateTime> dateTimePath, LocalDateTime dateTime) {
		if (Objects.isNull(dateTimePath)) {
			return null;
		}
		return dateTimePath.goe(dateTime);	
	}
	public static BooleanExpression toLoeExpression(DateTimePath<LocalDateTime> dateTimePath, LocalDateTime dateTime) {
		if (Objects.isNull(dateTimePath)) {
			return null;
		}
		return dateTimePath.loe(dateTime);	
	}
}
