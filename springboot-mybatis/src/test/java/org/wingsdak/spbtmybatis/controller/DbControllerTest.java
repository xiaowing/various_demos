package org.wingsdak.spbtmybatis.controller;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.wingsdak.spbtmybatis.Application;
import org.wingsdak.spbtmybatis.api.ResultVO;
import org.wingsdak.spbtmybatis.mockdata.TestUtilities;
import org.wingsdak.spbtmybatis.persistence.PgDatabaseMapper;

import com.alibaba.fastjson.JSON;

@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！ 
@SpringApplicationConfiguration(classes = Application.class) // 指定我们SpringBoot工程的Application启动类
@WebAppConfiguration
public class DbControllerTest {
	@Autowired
	@InjectMocks
	private DbController dbController;

	@Mock
	private PgDatabaseMapper pgDbMapper;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testDbController_NormalResponse() {
		when(pgDbMapper.getUtf8Databases(anyInt())).thenReturn(TestUtilities.ProduceSingleDbInfoList());
		ResultVO result = dbController.databases();
		
		Assert.assertEquals(ResultVO.RESULT_OK, result.getReturnValue());
		Assert.assertNotNull(result.getData());
		String jsonStr = JSON.toJSONString(result.getData());
		System.out.println(jsonStr);
	}

}
