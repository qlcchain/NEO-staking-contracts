/** 
 * @Package contract.testCase 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月9日 下午12:36:53 
 * @version V1.0 
 */ 
package contract.testCase.v2;

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
import qlc.utils.Helper;
import utils.DateUtils;

/** 
 * @Description transferOwner, querySwapInfo method test
 * @author yfhuang521@gmail.com
 * @date 2020年9月9日 下午12:36:53 
 */
public class OtherMethodTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed5.ngd.network:20332"));
	
    ScriptHash contractScripthash = new ScriptHash("bfcbb52d61bc6d3ef2c8cf43f595f4bf5cac66c5");
    
    String ownerWif = "Kxu2HyQFcAsGn8DXLBkySYRfK6VDkqMznbuWNHWUncQATXYBeDtL";
	
	@Test
    public void querySwapInfo() throws IOException, ErrorResponseException {
		
        List<ContractParameter> parameterList = new ArrayList<ContractParameter>();
        ContractParameter parameter = ContractParameter.byteArray("eccf34fa0217099ee12bbb3e942d6dbec673b462430588ff8c8dc61905770b62");
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
	
	// 反序
	@Test
	public void reverse() {
		System.out.println(Helper.reverse("620b770519c68d8cff88054362b473c6be6d2d943ebb2be19e091702fa34cfec"));
	}
	
	private void print(StackItem item) {
		System.out.println("**** Swap Info ****");
		if (StackItemType.ARRAY.equals(item.getType())) {
	        ArrayStackItem itemArr = item.asArray();
	        for (int i=0; i<itemArr.size(); i++) {
	        	if (StackItemType.BYTE_ARRAY.equals(itemArr.get(i).getType())) {
	        		if (i==0 || i==1)
	        			System.out.println(itemArr.get(i).asByteArray().getAsAddress());
	        		else if (i == 3)
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

}
