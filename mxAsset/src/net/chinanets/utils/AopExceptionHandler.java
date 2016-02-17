package net.chinanets.utils;

import java.lang.reflect.Method;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;

import flex.messaging.MessageException;


/**
 * @author: duzj
 * @Action: 负责异常处理的Advice 使用Throw通知类型来实现Advice
 */
public class AopExceptionHandler implements ThrowsAdvice
{
	/**
	 * 重写afterThrowing()方法
	 * 
	 * @param method 执行方法
	 * @param args 执行参数
	 * @param target 执行实体
	 * @param subclass 父类
	 * @throws Throwable 异常
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName());
	public void afterThrowing(Method method, Object[ ] args, Object target, Throwable subclass) throws Throwable {
		String argParas = "";
		
		System.err.println("产生异常的方法名称：  " + method.getName());
		for(Object o:args){
			argParas += o.toString()+",";
			System.out.println("方法的参数：   " + o.toString());
		}
		System.out.println("代理对象：   " + target.getClass().getName());
		System.out.println("抛出的异常:    " + subclass.getMessage()+">>>>>>>"
				+ subclass.getCause());
		System.out.println("异常详细信息：　　　"+subclass.fillInStackTrace());
		
		String exMsg = Level.ERROR+": 执行 " + method.getName() +	"("+argParas.substring(0,argParas.length() - 1)  + ") 时有异常抛出!" + subclass;
		logger.log( Level.ERROR,exMsg);
		
	//	throw new Exception(exMsg);
		MessageException mse = new MessageException();
		mse.setCode(subclass.fillInStackTrace().toString());  
		mse.setMessage(exMsg);  
		mse.setDetails("Flex 调用 Service 中的方法报错,请检查传入的参数或调用的方法!");  
		mse.setRootCause(subclass.getCause());  
		throw mse;
	}

}
