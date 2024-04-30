package com.sri.lanka.traffic.admin.config.postgres;

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
    }
}
