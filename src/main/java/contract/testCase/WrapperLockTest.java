/** 
 * @Package contract.testCase 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午2:50:23 
 * @version V1.0 
 */ 
package contract.testCase;

import java.io.IOException;
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
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import utils.DateUtils;

/** 
 * @Description wrapper lock nep5 qlc to contract
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午2:50:23 
 */
public class WrapperLockTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed2.ngd.network:20332"));

    ScriptHash contractScripthash = new ScriptHash("84be590a68903cdc37c6afe62e9056c70cc22f0e");
    
    /**
     * 
     * @Description Normal lock/use the same original HASH
     * @throws IOException
     * @throws ErrorResponseException 
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午2:53:23
     */
	@Test
    public void wrapperLock12() throws IOException, ErrorResponseException {
		
        Account account = Account.fromWIF("WIF").build();
        
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.byteArray("ede5d91c4db8d66238f3fa2cabe33dc6cf1bc11af8f32ecb3b58d2799a2274e6");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("500000000"));
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("20"));
        parameterList.add(parameter);
        
        Builder builder= new Builder(neow3j);
        ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
        .function("wrapperLock")
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
        
    }
	
	/**
     * 
     * @Description The number of locked QLC is less than the minimum transaction value
     * @throws IOException
     * @throws ErrorResponseException 
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午2:53:23
     */
	@Test
    public void wrapperLock3() throws IOException, ErrorResponseException {
		
        Account account = Account.fromWIF("WIF").build();
        
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.byteArray("38827049dc76600768d3182681bd1056a98a33f61e3bb5205b81ef9263ffd2e5");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("50000000"));
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("20"));
        parameterList.add(parameter);
        
        Builder builder= new Builder(neow3j);
        ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
        .function("wrapperLock")
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
        
    }
	
	/**
     * 
     * @Description Wallet QLC is not enough
     * @throws IOException
     * @throws ErrorResponseException 
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午2:53:23
     */
	@Test
    public void wrapperLock4() throws IOException, ErrorResponseException {
		
        Account account = Account.fromWIF("WIF").build();
        
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.byteArray("38827049dc76600768d3182681bd1056a98a33f61e3bb5205b81ef9263ffd2e5");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("500000000000000"));
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("20"));
        parameterList.add(parameter);
        
        Builder builder= new Builder(neow3j);
        ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
        .function("wrapperLock")
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
        
    }/**
     * 
     * @Description Lack of parameter
     * @throws IOException
     * @throws ErrorResponseException 
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午2:53:23
     */
	@Test
    public void wrapperLock5() throws IOException, ErrorResponseException {
		
        Account account = Account.fromWIF("WIF").build();
        
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.byteArray("38827049dc76600768d3182681bd1056a98a33f61e3bb5205b81ef9263ffd2e5");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArrayFromAddress("ARNpaFJhp6SHziRomrK4cenWw66C8VVFyv");
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("500000000"));
        parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4");
        //parameterList.add(parameter);
        parameter = null;
        parameter = ContractParameter.integer(new BigInteger("20"));
        //parameterList.add(parameter);
        
        Builder builder= new Builder(neow3j);
        ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
        .function("wrapperLock")
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
