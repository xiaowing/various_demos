package org.wingsdak.spbtmybatis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wingsdak.spbtmybatis.api.ResultVO;
import org.wingsdak.spbtmybatis.entity.PgDatabaseInfo;
import org.wingsdak.spbtmybatis.persistence.PgDatabaseMapper;
import org.wingsdak.spbtmybatis.utility.Logger;

@RestController
public class DbController {
	@Autowired
	PgDatabaseMapper dbMapper;

	@RequestMapping(path="/databases", method=RequestMethod.GET)
    public ResultVO databases() {
		ResultVO result = new ResultVO();
		Logger.info("A new request coming.");
		
		// 调用Mapper接口所映射的SQL文
		try{
			// 在PostgreSQL系统表中，UTF8编码的枚举值为6.
			List<PgDatabaseInfo> dbInfos = dbMapper.getUtf8Databases(6);
			
			result.setReturnValue(ResultVO.RESULT_OK);
			result.setMessage("");
			result.setData(dbInfos);
			return result;
		}
		catch (Exception ex){
			Logger.error(ex, "An unexpected error occurred during executing sql: %s", ex.getMessage());
			result.setReturnValue(ResultVO.RESULT_NG);
			result.setMessage(ex.getMessage());
			result.setData("");
			return result;
		}
		
	}
}
