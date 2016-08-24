package org.wingsdak.spbtmybatis.mockdata;

import java.util.ArrayList;
import java.util.List;

import org.wingsdak.spbtmybatis.entity.PgDatabaseInfo;

public class TestUtilities {
	public static List<PgDatabaseInfo> ProduceSingleDbInfoList(){
		List<PgDatabaseInfo> ret = new ArrayList<>();
		PgDatabaseInfo pgDbInfo = new PgDatabaseInfo();
		pgDbInfo.setDatabaseCtype("Shift-JIS");
		pgDbInfo.setDatabaseDbaName("testdb");
		pgDbInfo.setDatabaseName("sa");
		ret.add(pgDbInfo);
		return ret;
	}
}
