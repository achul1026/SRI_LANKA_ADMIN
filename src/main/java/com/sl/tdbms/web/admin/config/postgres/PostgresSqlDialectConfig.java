package com.sl.tdbms.web.admin.config.postgres;

import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostgresSqlDialectConfig extends PostgreSQL95Dialect{
	
	public PostgresSqlDialectConfig() {
        super();
        registerFunction("string_agg", new SQLFunctionTemplate( StandardBasicTypes.STRING, "string_agg(?1, ?2)"));
        registerFunction("array_agg", new StandardSQLFunction("array_agg", StandardBasicTypes.STRING));
        registerFunction("jsonb_agg", new StandardSQLFunction("jsonb_agg", StandardBasicTypes.STRING));
        registerFunction("json_agg", new StandardSQLFunction("json_agg", StandardBasicTypes.STRING));
        registerFunction("jsonb_agg_cd_ordered_asc_triple", new SQLFunctionTemplate(StandardBasicTypes.STRING, "jsonb_agg(jsonb_build_object('code', ?1, 'name', ?2) ORDER BY ?3 ASC, ?4 ASC, ?5 ASC)"));
        registerFunction("jsonb_agg_ordered_asc", new SQLFunctionTemplate(StandardBasicTypes.STRING,
                "jsonb_agg(jsonb_build_object('title', ?1, 'count', ?2) ORDER BY ?3 ASC)"));   
        registerFunction("jsonb_agg_ordered_desc", new SQLFunctionTemplate(StandardBasicTypes.STRING,
        		"jsonb_agg(jsonb_build_object('title', ?1, 'count', ?2) ORDER BY ?3 DESC)"));   
        registerFunction("array_agg_int", new SQLFunctionTemplate(StandardBasicTypes.STRING, "ARRAY_AGG(DISTINCT CAST(?1 AS INTEGER))"));
        registerFunction("extract_dow", new SQLFunctionTemplate(StandardBasicTypes.INTEGER, "EXTRACT(DOW FROM TO_DATE(?1, ?2))"));
        registerFunction("jsonb_agg_ordered_non_null", new SQLFunctionTemplate(StandardBasicTypes.STRING, "jsonb_agg(?1 order by ?2) filter (where ?1 is not null)"));
    }
}
