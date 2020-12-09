/** 
 * @Package contract 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年8月27日 下午3:40:54 
 * @version V1.0 
 */ 
package contract.testCase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

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
public class UserLockTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed5.ngd.network:20332"));

    ScriptHash contractScripthash = new ScriptHash("66c47fdd454bcfdd7cfe1643e26e7b9dd7ec8df8");
    
    /**
     * 
     * @Description Normal lock/use the same original HASH
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void userLock12() {
		
        try {
			Account account = Account.fromWIF("your wif").build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArray("514ed2cae954aa3333caaecc514ebfe7a1ea808b79d68aa7f9fd1d255387f282");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("520000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("40"));
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("userLock")
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
     * @Description The number of transactions is less than the minimum
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void userLock3() {
		
        try {
			Account account = Account.fromWIF("WIF").build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArray("132b4c1d3b76670d681aab1d44628a9309133a04882f8608c4081e112ecc49ce");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("1000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("40"));
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("userLock")
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
     * @Description Wallet QLC is not enough
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void userLock4() {
		
        try {
			Account account = Account.fromWIF("WIF").build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArray("ddd56bc449f8d6c323d96b269bd09e6f890ed5daad687ca4cbb53993d8d8191a");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("1000000000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("40"));
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("userLock")
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
     * @Description Use the wrong wrapperInAddress
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void userLock5() {
		
        try {
			Account account = Account.fromWIF("WIF").build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArray("51c940c6b5be9cf44e6855b4a2594e428d848755e62f5a770802ed01dd9ca50f");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.string("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztyj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("200000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("40"));
			parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("userLock")
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
     * @Description Lack of parameter
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:23:37
     */
	@Test
    public void userLock6() {
		
        try {
			Account account = Account.fromWIF("WIF").build();
			
			List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
			ContractParameter parameter = ContractParameter.byteArray("51c940c6b5be9cf44e6855b4a2594e428d848755e62f5a770802ed01dd9ca50f");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("200000000"));
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
			parameterList.add(parameter);
			parameter = null;
			parameter = ContractParameter.integer(new BigInteger("40"));
			//parameterList.add(parameter);
			
			Builder builder= new Builder(neow3j);
			ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
			.function("userLock")
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
