package org.wingsdak.spbtmybatis.persistence;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.wingsdak.spbtmybatis.entity.PgDatabaseInfo;

public interface PgDatabaseMapper {
	@Select("SELECT datname, rolname, datctype "
			+ "FROM pg_database, pg_roles "
			+ "WHERE pg_database.encoding=#{encodingType} "
			+ "AND pg_database.datdba=pg_roles.oid;")
	@Results(value = {
			@Result(property="databaseName", column="datname", jdbcType=JdbcType.VARCHAR),
			@Result(property="databaseDbaName", column="rolname", jdbcType=JdbcType.VARCHAR),
			@Result(property="databaseCtype", column="datctype", jdbcType=JdbcType.VARCHAR)
	})
	List<PgDatabaseInfo> getUtf8Databases(@Param("encodingType") int encodeType);
}
