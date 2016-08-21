package org.wingsdak.spbtmybatis.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.wingsdak.spbtmybatis.api.ResultVO;
import org.wingsdak.spbtmybatis.entity.PgDatabaseInfo;
import org.wingsdak.spbtmybatis.persistence.PgDatabaseMapper;

import com.alibaba.fastjson.JSON;

@RestController
public class DbController {
	@Autowired
	PgDatabaseMapper dbMapper;

	@RequestMapping(path="/databases", method=RequestMethod.GET)
    public ResultVO databases() {
		ResultVO result = new ResultVO();
		
		// 调用Mapper接口所映射的SQL文
		try{
			// 在PostgreSQL系统表中，UTF8编码的枚举值为6.
			List<PgDatabaseInfo> dbInfos = dbMapper.getUtf8Databases(6);
			String jsonStr = JSON.toJSONString(dbInfos, true);
			
			result.setReturnValue(ResultVO.RESULT_OK);
			result.setMessage("");
			result.setData(jsonStr);
			return result;
		}
		catch (Exception ex){
			result.setReturnValue(ResultVO.RESULT_NG);
			result.setMessage(ex.getMessage());
			result.setData("");
			return result;
		}
		
	}
}
