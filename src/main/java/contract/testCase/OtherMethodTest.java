/** 
 * @Package contract.testCase 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月9日 下午12:36:53 
 * @version V1.0 
 */ 
package contract.testCase;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptHash;
import io.neow3j.contract.ContractInvocation.Builder;
import io.neow3j.model.types.StackItemType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.ArrayStackItem;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import qlc.utils.Helper;
import utils.DateUtils;

/** 
 * @Description transferOwner, querySwapInfo method test
 * @author yfhuang521@gmail.com
 * @date 2020年9月9日 下午12:36:53 
 */
public class OtherMethodTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed5.ngd.network:20332"));
	
    ScriptHash contractScripthash = new ScriptHash("0xfcad9c0b5cde026781f162da68fde930da6da77b");
    
    String ownerWif = "your wif";
	
	@Test
    public void transferOwner() throws Exception {
		
        Account account = Account.fromWIF(ownerWif).build();
        
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.byteArrayFromAddress("ANFnCg69c8VfE36hBhLZRrmofZ9CZU1vqZ");
        parameterList.add(parameter);
        
        Builder builder= new Builder(neow3j);
        ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
        .function("transferOwner")
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
	
	@Test
    public void querySwapInfo() throws IOException, ErrorResponseException {
		
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.
        		byteArray("bc64451b78a8afc9103e0b3e5fdbc062e23230bde2e29a324f63b119b86162a1");
        parameterList.add(parameter);
        
        Builder builder= new Builder(neow3j);
        ContractInvocation contractInvocation = builder.contractScriptHash(contractScripthash)
        .function("querySwapInfo")
        .parameters(parameterList)
        .build()
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
	        		if (i == 0)
	        			System.out.println(Numeric.hexToString(Numeric.toHexStringNoPrefix(itemArr.get(i).asByteArray().getValue())));
	        		else if (i == 12) {
	        			System.out.println(Numeric.toBigInt(itemArr.get(i).asByteArray().getValue()));
	        		} else {
	        			if (i == 3)
	        				System.out.println(Numeric.toHexString(itemArr.get(i).asByteArray().getValue()));
	        			else if (i == 15)
	        				System.out.println(itemArr.get(i).asByteArray().getAsString());
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
	        			System.out.println(DateUtils.timeStamp2Date(String.valueOf(num), "yyyy-MM-dd HH:mm:ss"));
	        		else
	        			System.out.println(num);
	        	}
	        }
        } else if (StackItemType.BYTE_ARRAY.equals(item.getType())) {
     	   System.out.println(Numeric.hexToString(Numeric.toHexStringNoPrefix(item.asByteArray().getValue())));
        }
	}
	
	public static void main(String[] args) {
		System.out.println(Helper.reverse("4717db2ea3a0d136c9c7f8eb83fa007f0c8206e8"));
	}

}
