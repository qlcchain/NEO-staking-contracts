/** 
 * @Package contract 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年8月27日 下午3:40:54 
 * @version V1.0 
 */ 
package contract.testCase.v2;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractInvocation.Builder;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.model.types.StackItemType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.ArrayStackItem;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import utils.DateUtils;

/** 
 * @Description user lock nep5 qlc to contract
 * @author yfhuang521@gmail.com
 * @date 2020年8月27日 下午3:40:54 
 */
public class LockTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed5.ngd.network:20332"));

    ScriptHash contractScripthash = new ScriptHash("bfcbb52d61bc6d3ef2c8cf43f595f4bf5cac66c5");
    
    String userWif = "KyiLMuwnkwjNyuQJMmKvmFENCvC4rXAs9BdRSz9HTDmDFt93LRHt";
    
    /**
     * 
     * @Description Normal lock/use the same original HASH
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void lock1() {
		
        try {
			Account account = Account.fromWIF(userWif).build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("880000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("lock")
			.parameters(parameterList)
			.account(account)
			
			.build()
			.sign()
			.invoke();
JSONObject json = new JSONObject();
			System.out.println(contractInvocation.getResponse().getResult());
			System.out.println(contractInvocation.getTransaction().getTxId());
			List<StackItem> sItems = contractInvocation.testInvoke().getStack();
			if (sItems.size() > 0) {
			    StackItem item = sItems.get(0);
			    print(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
	
	/**
     * 
     * @Description 锁定QLC数量小于最小交易值
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void lock2() {
		
        try {
			Account account = Account.fromWIF(userWif).build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("0"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("lock")
			.parameters(parameterList)
			.account(account)
			
			.build()
			.sign()
			.invoke();

			System.out.println(contractInvocation.getResponse().getResult());
			System.out.println(contractInvocation.getTransaction().getTxId());
			List<StackItem> sItems = contractInvocation.testInvoke().getStack();
			if (sItems.size() > 0) {
			    StackItem item = sItems.get(0);
			    print(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
	
	/**
     * 
     * @Description 钱包QLC数量不够
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void lock3() {
		
        try {
			Account account = Account.fromWIF(userWif).build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("1000000000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("lock")
			.parameters(parameterList)
			.account(account)
			
			.build()
			.sign()
			.invoke();

			System.out.println(contractInvocation.getResponse().getResult());
			System.out.println(contractInvocation.getTransaction().getTxId());
			List<StackItem> sItems = contractInvocation.testInvoke().getStack();
			if (sItems.size() > 0) {
			    StackItem item = sItems.get(0);
			    print(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
	
	/**
     * 
     * @Description 缺少参数
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void lock4() {
		
        try {
			Account account = Account.fromWIF(userWif).build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("lock")
			.parameters(parameterList)
			.account(account)
			
			.build()
			.sign()
			.invoke();

			System.out.println(contractInvocation.getResponse().getResult());
			System.out.println(contractInvocation.getTransaction().getTxId());
			List<StackItem> sItems = contractInvocation.testInvoke().getStack();
			if (sItems.size() > 0) {
			    StackItem item = sItems.get(0);
			    print(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
	
	private void print(StackItem item) {
		 if (StackItemType.ARRAY.equals(item.getType())) {
	        ArrayStackItem itemArr = item.asArray();
	        for (int i=0; i<itemArr.size(); i++) {
	        	if (StackItemType.BYTE_ARRAY.equals(itemArr.get(i).getType())) {
	        		if (i == 11) {
	        			System.out.println(Numeric.toBigInt(itemArr.get(i).asByteArray().getValue()));
	        		} else {
	        			if (i == 2)
	        				System.out.println(Numeric.toHexString(itemArr.get(i).asByteArray().getValue()));
	        			else {
			        		String str = Numeric.toHexStringNoPrefix(itemArr.get(i).asByteArray().getValue());
			        		if (str.length() == 40)
			        			System.out.println(itemArr.get(i).asByteArray().getAsAddress());
			        		else if (str.length() == 64)
			        			System.out.println(Numeric.reverseHexString(str));
			        		else if (str.length() == 8)
			        			System.out.println(Numeric.fromFixed8ToDecimal(Numeric.reverseHexString(str)));
			        		else
			        			System.out.println(str);
	        			}
	        		}
	        	} else if (StackItemType.BOOLEAN.equals(itemArr.get(i).getType()))
	    			System.out.println(itemArr.get(i).asBoolean().getValue());
	        	else if (StackItemType.INTEGER.equals(itemArr.get(i).getType())) {
	        		BigInteger num = itemArr.get(i).asInteger().getValue();
	        		if (new BigInteger("1590000000").compareTo(num) < 0)
	        			System.out.println(DateUtils.timeStamp2Date(String.valueOf(num), "yyyy-mm-dd HH:mm:ss"));
	        		else
	        			System.out.println(num);
	        	}
	        }
       } else if (StackItemType.BYTE_ARRAY.equals(item.getType())) {
    	   System.out.println(Numeric.hexToString(Numeric.toHexStringNoPrefix(item.asByteArray().getValue())));
       }
	}
	
}
