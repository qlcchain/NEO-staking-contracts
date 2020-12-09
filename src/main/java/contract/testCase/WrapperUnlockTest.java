/** 
 * @Package contract 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年8月27日 下午3:40:54 
 * @version V1.0 
 */ 
package contract.testCase;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.neow3j.constants.OpCode;
import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptBuilder;
import io.neow3j.contract.ScriptHash;
import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.crypto.transaction.RawTransactionAttribute;
import io.neow3j.model.types.StackItemType;
import io.neow3j.model.types.TransactionAttributeUsageType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.ArrayStackItem;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.utils.Numeric;
import io.neow3j.wallet.Account;
import utils.DateUtils;

/** 
 * @Description wrapper unlock
 * @author yfhuang521@gmail.com
 * @date 2020年8月27日 下午3:40:54 
 */
public class WrapperUnlockTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed2.ngd.network:20332"));

    ScriptHash contractScripthash = new ScriptHash("66c47fdd454bcfdd7cfe1643e26e7b9dd7ec8df8");
    
    String ownerWif = "KwzBhL26S1c7zRevgSJRYeh26kXWMnJ5CSTLHvpR31nARRKHVZ8h";
	
    /**
     * 
     * @Description Unlocked normally/unlocked records are unlocked again
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:26:06
     */
	@Test
    public void wrapperUnlock12() {

        try {

            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );

            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("wrapperUnlock")
                    .parameter(ContractParameter.string("ddbda109309f9fafa6dd6a9cb9f1df40"))
                    .parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("ddbda109309f9fafa6dd6a9cb9f1df40").pushInteger(1).opCode(OpCode.PACK).pushData("wrapperUnlock").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
			    print(item);
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
    /**
     * 
     * @Description Incorrect text
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:26:06
     */
	@Test
    public void wrapperUnlock3() {

        try {

            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );

            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("wrapperUnlock")
                    .parameter(ContractParameter.string("695fdc01081a47868c096bc754dfc3f5"))
                    .parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("695fdc01081a47868c096bc754dfc3f5").pushInteger(1).opCode(OpCode.PACK).pushData("wrapperUnlock").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
			    print(item);
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
    /**
     * 
     * @Description Lack of parameter
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:26:06
     */
	@Test
    public void wrapperUnlock5() {

        try {

            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );

            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("wrapperUnlock")
                    .parameter(ContractParameter.string("d68dad8531bc4b52a68120280323ffcd"))
                    //.parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder()
            		.pushData("1")
            		.pushInteger(1).opCode(OpCode.PACK)
            		.pushData("1").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
			    print(item);
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
	
	/**
     * 
     * @Description Timeout call
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年8月27日 下午4:26:06
     */
	@Test
    public void wrapperUnlock6() {

        try {

            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );

            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("wrapperUnlock")
                    .parameter(ContractParameter.string("d68dad8531bc4b52a68120280323ffcd"))
                    .parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("d68dad8531bc4b52a68120280323ffcd").pushInteger(1).opCode(OpCode.PACK).pushData("wrapperUnlock").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
			    print(item);
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
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
