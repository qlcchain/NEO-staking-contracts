/** 
 * @Package contract.testCase 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午3:03:40 
 * @version V1.0 
 */ 
package contract.testCase.v2;

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
import io.neow3j.model.types.TransactionAttributeUsageType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.wallet.Account;

/** 
 * @Description user unlock
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午3:03:40 
 */
public class UnlockTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed2.ngd.network:20332"));
	
    ScriptHash contractScripthash = new ScriptHash("bfcbb52d61bc6d3ef2c8cf43f595f4bf5cac66c5");
    
    String ownerWif = "KwzBhL26S1c7zRevgSJRYeh26kXWMnJ5CSTLHvpR31nARRKHVZ8h";
    
    /**
     * 
     * @Description  正常调用
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void unlock1() {

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
                    .function("unlock")
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .parameter(ContractParameter.integer(new BigInteger("20000000")))
                    .parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .parameter(ContractParameter.byteArray("0x51a9de5d8bc002325c0d616ef172ba5c1786580c7837e47606620a920e2eea07"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("1").pushInteger(1).opCode(OpCode.PACK).pushData("unlockV2").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
			    //print(item);
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
     * @Description  相同的ERC20锁定到合约的TXID多次请求
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void unlock2() {

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
                    .function("unlock")
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .parameter(ContractParameter.integer(new BigInteger("20000000")))
                    .parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .parameter(ContractParameter.byteArray("0x51a9de5d8bc002325c0d616ef172ba5c1786580c7837e47606620a920e2eea07"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("1").pushInteger(1).opCode(OpCode.PACK).pushData("unlockV2").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
			    //print(item);
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
     * @Description  缺少参数
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void unlock3() {

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
                    .function("unlock")
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .parameter(ContractParameter.integer(new BigInteger("20000000")))
                    //.parameter(ContractParameter.byteArray("0x2e1ac6242bb084029a9eb29dfb083757d27fced4"))
                    .parameter(ContractParameter.byteArray("0x51a9de5d8bc002325c0d616ef172ba5c1786580c7837e47606620a920e2eea07"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("1").pushInteger(1).opCode(OpCode.PACK).pushData("unlockV2").toArray() , contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
			    //print(item);
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
    
	public static void main(String[] args) {

       Account account = Account.fromWIF("Kxu2HyQFcAsGn8DXLBkySYRfK6VDkqMznbuWNHWUncQATXYBeDtL").build();
		System.out.println(account.getAddress());
	}
	
}
